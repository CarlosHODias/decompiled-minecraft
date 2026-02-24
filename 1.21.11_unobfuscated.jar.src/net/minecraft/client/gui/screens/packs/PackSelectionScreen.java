/*     */ package net.minecraft.client.gui.screens.packs;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.google.common.hash.Hashing;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.StandardWatchEventKinds;
/*     */ import java.nio.file.WatchEvent;
/*     */ import java.nio.file.WatchKey;
/*     */ import java.nio.file.WatchService;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.AlertScreen;
/*     */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.NoticeWithLinkScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.repository.Pack;
/*     */ import net.minecraft.server.packs.repository.PackDetector;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.resources.IoSupplier;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.validation.DirectoryValidator;
/*     */ import net.minecraft.world.level.validation.ForbiddenSymlinkInfo;
/*     */ import org.apache.commons.lang3.mutable.MutableBoolean;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PackSelectionScreen extends Screen {
/*  59 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  61 */   private static final Component AVAILABLE_TITLE = (Component)Component.translatable("pack.available.title");
/*  62 */   private static final Component SELECTED_TITLE = (Component)Component.translatable("pack.selected.title");
/*  63 */   private static final Component OPEN_PACK_FOLDER_TITLE = (Component)Component.translatable("pack.openFolder");
/*  64 */   private static final Component SEARCH = (Component)Component.translatable("gui.packSelection.search").withStyle(EditBox.SEARCH_HINT_STYLE);
/*     */   
/*     */   private static final int LIST_WIDTH = 200;
/*     */   private static final int HEADER_ELEMENT_SPACING = 4;
/*     */   private static final int SEARCH_BOX_HEIGHT = 15;
/*  69 */   private static final Component DRAG_AND_DROP = (Component)Component.translatable("pack.dropInfo").withStyle(net.minecraft.ChatFormatting.GRAY);
/*  70 */   private static final Component DIRECTORY_BUTTON_TOOLTIP = (Component)Component.translatable("pack.folderInfo");
/*     */   
/*     */   private static final int RELOAD_COOLDOWN = 20;
/*  73 */   private static final Identifier DEFAULT_ICON = Identifier.withDefaultNamespace("textures/misc/unknown_pack.png");
/*     */   
/*  75 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */   
/*     */   private final PackSelectionModel model;
/*     */   
/*     */   private Watcher watcher;
/*     */   
/*     */   private long ticksToReload;
/*     */   private TransferableSelectionList availablePackList;
/*     */   private TransferableSelectionList selectedPackList;
/*     */   private EditBox search;
/*     */   private final Path packDir;
/*     */   private Button doneButton;
/*  87 */   private final Map<String, Identifier> packIcons = Maps.newHashMap();
/*     */   
/*     */   public PackSelectionScreen(PackRepository repository, Consumer<PackRepository> output, Path packDir, Component title) {
/*  90 */     super(title);
/*  91 */     this.model = new PackSelectionModel(this::populateLists, this::getPackIcon, repository, output);
/*  92 */     this.packDir = packDir;
/*  93 */     this.watcher = Watcher.create(packDir);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/*  98 */     this.model.commit();
/*  99 */     closeWatcher();
/*     */   }
/*     */   
/*     */   private void closeWatcher() {
/* 103 */     if (this.watcher != null) {
/*     */       try {
/* 105 */         this.watcher.close();
/* 106 */         this.watcher = null;
/* 107 */       } catch (Exception exception) {}
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() {
/* 114 */     Objects.requireNonNull(this.font); Objects.requireNonNull(this.font); this.layout.setHeaderHeight(4 + 9 + 4 + 9 + 4 + 15 + 4);
/* 115 */     LinearLayout header = (LinearLayout)this.layout.addToHeader((LayoutElement)LinearLayout.vertical().spacing(4));
/* 116 */     header.defaultCellSetting().alignHorizontallyCenter();
/* 117 */     header.addChild((LayoutElement)new StringWidget(getTitle(), this.font));
/* 118 */     header.addChild((LayoutElement)new StringWidget(DRAG_AND_DROP, this.font));
/* 119 */     this.search = (EditBox)header.addChild((LayoutElement)new EditBox(this.font, 0, 0, 200, 15, (Component)Component.empty()));
/* 120 */     this.search.setHint(SEARCH);
/* 121 */     this.search.setResponder(this::updateFilteredEntries);
/* 122 */     this.availablePackList = (TransferableSelectionList)this.layout.addToContents((LayoutElement)new TransferableSelectionList(this.minecraft, this, 200, this.height - 66, AVAILABLE_TITLE));
/* 123 */     this.selectedPackList = (TransferableSelectionList)this.layout.addToContents((LayoutElement)new TransferableSelectionList(this.minecraft, this, 200, this.height - 66, SELECTED_TITLE));
/*     */     
/* 125 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 126 */     footer.addChild((LayoutElement)Button.builder(OPEN_PACK_FOLDER_TITLE, button -> Util.getPlatform().openPath(this.packDir))
/* 127 */         .tooltip(Tooltip.create(DIRECTORY_BUTTON_TOOLTIP))
/* 128 */         .build());
/*     */     
/* 130 */     this.doneButton = (Button)footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onClose()).build());
/*     */     
/* 132 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/* 133 */     repositionElements();
/* 134 */     reload();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {
/* 139 */     if (this.search != null) {
/* 140 */       setInitialFocus((GuiEventListener)this.search);
/*     */     } else {
/* 142 */       super.setInitialFocus();
/*     */     } 
/*     */   }
/*     */   
/*     */   private void updateFilteredEntries(String value) {
/* 147 */     filterEntries(value, this.model.getSelected(), this.selectedPackList);
/* 148 */     filterEntries(value, this.model.getUnselected(), this.availablePackList);
/*     */   }
/*     */   
/*     */   private void filterEntries(String value, Stream<PackSelectionModel.Entry> oldEntries, TransferableSelectionList listToUpdate) {
/* 152 */     if (listToUpdate == null) {
/*     */       return;
/*     */     }
/* 155 */     String lowerCaseValue = value.toLowerCase(Locale.ROOT);
/* 156 */     Stream<PackSelectionModel.Entry> filteredEntries = oldEntries.filter(packEntry -> (value.isBlank() || packEntry.getId().toLowerCase(Locale.ROOT).contains(lowerCaseValue) || packEntry.getTitle().getString().toLowerCase(Locale.ROOT).contains(lowerCaseValue) || packEntry.getDescription().getString().toLowerCase(Locale.ROOT).contains(lowerCaseValue)));
/*     */ 
/*     */ 
/*     */     
/* 160 */     listToUpdate.updateList(filteredEntries, null);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/* 165 */     this.layout.arrangeElements();
/* 166 */     if (this.availablePackList != null) {
/* 167 */       this.availablePackList.updateSizeAndPosition(200, this.layout.getContentHeight(), this.width / 2 - 15 - 200, this.layout.getHeaderHeight());
/*     */     }
/* 169 */     if (this.selectedPackList != null) {
/* 170 */       this.selectedPackList.updateSizeAndPosition(200, this.layout.getContentHeight(), this.width / 2 + 15, this.layout.getHeaderHeight());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 176 */     if (this.watcher != null) {
/*     */       try {
/* 178 */         if (this.watcher.pollForChanges())
/*     */         {
/* 180 */           this.ticksToReload = 20L;
/*     */         }
/* 182 */       } catch (IOException e) {
/* 183 */         LOGGER.warn("Failed to poll for directory {} changes, stopping", this.packDir);
/* 184 */         closeWatcher();
/*     */       } 
/*     */     }
/*     */     
/* 188 */     if (this.ticksToReload > 0L && 
/* 189 */       --this.ticksToReload == 0L) {
/* 190 */       reload();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   private void populateLists(PackSelectionModel.EntryBase transferredEntry) {
/* 196 */     if (this.selectedPackList != null) {
/* 197 */       this.selectedPackList.updateList(this.model.getSelected(), transferredEntry);
/*     */     }
/* 199 */     if (this.availablePackList != null) {
/* 200 */       this.availablePackList.updateList(this.model.getUnselected(), transferredEntry);
/*     */     }
/* 202 */     if (this.search != null) {
/* 203 */       updateFilteredEntries(this.search.getValue());
/*     */     }
/* 205 */     if (this.doneButton != null) {
/* 206 */       this.doneButton.active = !this.selectedPackList.children().isEmpty();
/*     */     }
/*     */   }
/*     */   
/*     */   private void reload() {
/* 211 */     this.model.findNewPacks();
/* 212 */     populateLists(null);
/* 213 */     this.ticksToReload = 0L;
/* 214 */     this.packIcons.clear();
/*     */   }
/*     */   
/*     */   protected static void copyPacks(Minecraft minecraft, List<Path> files, Path targetDir) {
/* 218 */     MutableBoolean showErrorToast = new MutableBoolean();
/* 219 */     files.forEach(pack -> { try { Stream<Path> contents = Files.walk(pack, new java.nio.file.FileVisitOption[0]); try { contents.forEach(()); if (contents != null)
/* 220 */                 contents.close();  } catch (Throwable throwable) { if (contents != null) try { contents.close(); } catch (Throwable throwable1)
/*     */                 { throwable.addSuppressed(throwable1); }
/*     */               
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               throw throwable; }
/*     */              }
/* 229 */           catch (IOException e)
/*     */           { LOGGER.warn("Failed to copy datapack file from {} to {}", pack, targetDir);
/*     */             showErrorToast.setTrue(); }
/*     */         
/*     */         });
/* 234 */     if (showErrorToast.isTrue()) {
/* 235 */       SystemToast.onPackCopyFailure(minecraft, targetDir.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onFilesDrop(List<Path> files) {
/* 241 */     String names = extractPackNames(files).collect(Collectors.joining(", "));
/* 242 */     this.minecraft.setScreen((Screen)new ConfirmScreen(result -> {
/*     */             if (files) {
/*     */               List<Path> packCandidates = new ArrayList<>(files.size());
/*     */               Set<Path> leftoverPacks = new HashSet<>(files);
/*     */               PackDetector<Path> packDetector = new PackDetector<Path>(this, this.minecraft.directoryValidator())
/*     */                 {
/*     */                   protected Path createZipPack(Path content) {
/* 249 */                     return content;
/*     */                   }
/*     */ 
/*     */                   
/*     */                   protected Path createDirectoryPack(Path content) {
/* 254 */                     return content;
/*     */                   }
/*     */                 },  ;
/*     */               
/*     */               List<ForbiddenSymlinkInfo> issues = new ArrayList<>();
/*     */               for (Path path : (Iterable<Path>)files) {
/*     */                 try {
/*     */                   Path candidate = (Path)packDetector.detectPackResources(path, issues);
/*     */                   if (candidate == null) {
/*     */                     LOGGER.warn("Path {} does not seem like pack", path);
/*     */                     continue;
/*     */                   } 
/*     */                   packCandidates.add(candidate);
/*     */                   leftoverPacks.remove(candidate);
/* 268 */                 } catch (IOException e) {
/*     */                   LOGGER.warn("Failed to check {} for packs", path, e);
/*     */                 } 
/*     */               } 
/*     */               
/*     */               if (!issues.isEmpty()) {
/*     */                 this.minecraft.setScreen(NoticeWithLinkScreen.createPackSymlinkWarningScreen(()));
/*     */                 
/*     */                 return;
/*     */               } 
/*     */               
/*     */               if (!packCandidates.isEmpty()) {
/*     */                 copyPacks(this.minecraft, packCandidates, this.packDir);
/*     */                 
/*     */                 reload();
/*     */               } 
/*     */               
/*     */               if (!leftoverPacks.isEmpty()) {
/*     */                 String leftoverNames = extractPackNames(leftoverPacks).collect(Collectors.joining(", "));
/*     */                 
/*     */                 this.minecraft.setScreen((Screen)new AlertScreen((), (Component)Component.translatable("pack.dropRejected.title"), (Component)Component.translatable("pack.dropRejected.message", new Object[] { leftoverNames })));
/*     */                 
/*     */                 return;
/*     */               } 
/*     */             } 
/*     */             
/*     */             this.minecraft.setScreen(this);
/* 295 */           }, (Component)Component.translatable("pack.dropConfirm"), 
/* 296 */           (Component)Component.literal(names)));
/*     */   }
/*     */   
/*     */   private static Stream<String> extractPackNames(Collection<Path> files) {
/* 300 */     return files.stream().map(Path::getFileName).map(Path::toString);
/*     */   }
/*     */   private Identifier loadPackIcon(TextureManager textureManager, Pack pack) {
/*     */     
/* 304 */     try { PackResources packResources = pack.open(); 
/* 305 */       try { IoSupplier<InputStream> resource = packResources.getRootResource(new String[] { "pack.png" });
/* 306 */         if (resource == null)
/* 307 */         { Identifier identifier = DEFAULT_ICON;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 317 */           if (packResources != null) packResources.close();  return identifier; }  String id = pack.getId(); Identifier location = Identifier.withDefaultNamespace("pack/" + Util.sanitizeName(id, Identifier::validPathChar) + "/" + String.valueOf(Hashing.sha1().hashUnencodedChars(id)) + "/icon"); InputStream stream = (InputStream)resource.get(); try { NativeImage iconImage = NativeImage.read(stream); Objects.requireNonNull(location); textureManager.register(location, (AbstractTexture)new net.minecraft.client.renderer.texture.DynamicTexture(location::toString, iconImage)); Identifier identifier = location; if (stream != null) stream.close();  if (packResources != null) packResources.close();  return identifier; } catch (Throwable throwable) { if (stream != null) try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable throwable) { if (packResources != null) try { packResources.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/* 318 */     { LOGGER.warn("Failed to load icon from pack {}", pack.getId(), e);
/*     */       
/* 320 */       return DEFAULT_ICON; }
/*     */   
/*     */   }
/*     */   private Identifier getPackIcon(Pack pack) {
/* 324 */     return this.packIcons.computeIfAbsent(pack.getId(), s -> loadPackIcon(this.minecraft.getTextureManager(), pack));
/*     */   }
/*     */   
/*     */   private static class Watcher implements AutoCloseable {
/*     */     private final WatchService watcher;
/*     */     private final Path packPath;
/*     */     
/*     */     public Watcher(Path packPath) throws IOException {
/* 332 */       this.packPath = packPath;
/* 333 */       this.watcher = packPath.getFileSystem().newWatchService();
/*     */ 
/*     */       
/* 336 */       try { watchDir(packPath);
/*     */ 
/*     */         
/* 339 */         DirectoryStream<Path> paths = Files.newDirectoryStream(packPath); 
/* 340 */         try { for (Path path : paths) {
/* 341 */             if (Files.isDirectory(path, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
/* 342 */               watchDir(path);
/*     */             }
/*     */           } 
/* 345 */           if (paths != null) paths.close();  } catch (Throwable throwable) { if (paths != null)
/* 346 */             try { paths.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/* 347 */       { this.watcher.close();
/* 348 */         throw e; }
/*     */     
/*     */     }
/*     */     
/*     */     public static Watcher create(Path packDir) {
/*     */       try {
/* 354 */         return new Watcher(packDir);
/* 355 */       } catch (IOException e) {
/* 356 */         PackSelectionScreen.LOGGER.warn("Failed to initialize pack directory {} monitoring", packDir, e);
/* 357 */         return null;
/*     */       } 
/*     */     }
/*     */     
/*     */     private void watchDir(Path packPath) throws IOException {
/* 362 */       packPath.register(this.watcher, (WatchEvent.Kind<?>[])new WatchEvent.Kind[] { StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE, StandardWatchEventKinds.ENTRY_MODIFY });
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean pollForChanges() throws IOException {
/*     */       boolean hasChanges = false;
/*     */       WatchKey key;
/* 370 */       while ((key = this.watcher.poll()) != null) {
/* 371 */         List<WatchEvent<?>> watchEvents = key.pollEvents();
/* 372 */         for (WatchEvent<?> watchEvent : watchEvents) {
/* 373 */           hasChanges = true;
/*     */           
/* 375 */           Path newPath = this.packPath.resolve((Path)watchEvent.context());
/* 376 */           if (key.watchable() == this.packPath && watchEvent.kind() == StandardWatchEventKinds.ENTRY_CREATE && Files.isDirectory(newPath, new LinkOption[] { LinkOption.NOFOLLOW_LINKS })) {
/* 377 */             watchDir(newPath);
/*     */           }
/*     */         } 
/*     */         
/* 381 */         key.reset();
/*     */       } 
/*     */       
/* 384 */       return hasChanges;
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 389 */       this.watcher.close();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/packs/PackSelectionScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */