/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.blaze3d.platform.NativeImage;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.LinkOption;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.BasicFileAttributes;
/*     */ import java.time.Instant;
/*     */ import java.time.ZoneId;
/*     */ import java.time.ZonedDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.time.format.FormatStyle;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionException;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.SelectableEntry;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.gui.screens.AlertScreen;
/*     */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.ErrorScreen;
/*     */ import net.minecraft.client.gui.screens.FaviconTexture;
/*     */ import net.minecraft.client.gui.screens.GenericMessageScreen;
/*     */ import net.minecraft.client.gui.screens.LoadingDotsText;
/*     */ import net.minecraft.client.gui.screens.NoticeWithLinkScreen;
/*     */ import net.minecraft.client.gui.screens.ProgressScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*     */ import net.minecraft.client.resources.sounds.SoundInstance;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.Style;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import net.minecraft.world.level.storage.LevelStorageException;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.LevelSummary;
/*     */ import net.minecraft.world.level.validation.ContentValidationException;
/*     */ import net.minecraft.world.level.validation.ForbiddenSymlinkInfo;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class WorldSelectionList extends ObjectSelectionList<WorldSelectionList.Entry> {
/*  75 */   public static final DateTimeFormatter DATE_FORMAT = Util.localizedDateFormatter(FormatStyle.SHORT);
/*     */   
/*  77 */   private static final Identifier ERROR_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("world_list/error_highlighted");
/*  78 */   private static final Identifier ERROR_SPRITE = Identifier.withDefaultNamespace("world_list/error");
/*  79 */   private static final Identifier MARKED_JOIN_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("world_list/marked_join_highlighted");
/*  80 */   private static final Identifier MARKED_JOIN_SPRITE = Identifier.withDefaultNamespace("world_list/marked_join");
/*  81 */   private static final Identifier WARNING_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("world_list/warning_highlighted");
/*  82 */   private static final Identifier WARNING_SPRITE = Identifier.withDefaultNamespace("world_list/warning");
/*  83 */   private static final Identifier JOIN_HIGHLIGHTED_SPRITE = Identifier.withDefaultNamespace("world_list/join_highlighted");
/*  84 */   private static final Identifier JOIN_SPRITE = Identifier.withDefaultNamespace("world_list/join");
/*  85 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  87 */   private static final Component FROM_NEWER_TOOLTIP_1 = (Component)Component.translatable("selectWorld.tooltip.fromNewerVersion1").withStyle(ChatFormatting.RED);
/*  88 */   private static final Component FROM_NEWER_TOOLTIP_2 = (Component)Component.translatable("selectWorld.tooltip.fromNewerVersion2").withStyle(ChatFormatting.RED);
/*  89 */   private static final Component SNAPSHOT_TOOLTIP_1 = (Component)Component.translatable("selectWorld.tooltip.snapshot1").withStyle(ChatFormatting.GOLD);
/*  90 */   private static final Component SNAPSHOT_TOOLTIP_2 = (Component)Component.translatable("selectWorld.tooltip.snapshot2").withStyle(ChatFormatting.GOLD);
/*  91 */   private static final Component WORLD_LOCKED_TOOLTIP = (Component)Component.translatable("selectWorld.locked").withStyle(ChatFormatting.RED);
/*  92 */   private static final Component WORLD_REQUIRES_CONVERSION = (Component)Component.translatable("selectWorld.conversion.tooltip").withStyle(ChatFormatting.RED);
/*  93 */   private static final Component INCOMPATIBLE_VERSION_TOOLTIP = (Component)Component.translatable("selectWorld.incompatible.tooltip").withStyle(ChatFormatting.RED);
/*  94 */   private static final Component WORLD_EXPERIMENTAL = (Component)Component.translatable("selectWorld.experimental");
/*     */   
/*     */   private final Screen screen;
/*     */   
/*     */   private CompletableFuture<List<LevelSummary>> pendingLevels;
/*     */   
/*     */   private List<LevelSummary> currentlyDisplayedLevels;
/*     */   
/*     */   private final LoadingHeader loadingHeader;
/*     */   
/*     */   private final EntryType entryType;
/*     */   private String filter;
/*     */   private boolean hasPolled;
/*     */   private final Consumer<LevelSummary> onEntrySelect;
/*     */   private final Consumer<WorldListEntry> onEntryInteract;
/*     */   
/*     */   private WorldSelectionList(Screen screen, Minecraft minecraft, int width, int height, String filter, WorldSelectionList oldList, Consumer<LevelSummary> onEntrySelect, Consumer<WorldListEntry> onEntryInteract, EntryType entryType) {
/* 111 */     super(minecraft, width, height, 0, 36);
/* 112 */     this.screen = screen;
/*     */     
/* 114 */     this.loadingHeader = new LoadingHeader(minecraft);
/*     */     
/* 116 */     this.filter = filter;
/* 117 */     this.onEntrySelect = onEntrySelect;
/* 118 */     this.onEntryInteract = onEntryInteract;
/* 119 */     this.entryType = entryType;
/* 120 */     if (oldList != null) {
/* 121 */       this.pendingLevels = oldList.pendingLevels;
/*     */     } else {
/* 123 */       this.pendingLevels = loadLevels();
/*     */     } 
/* 125 */     addEntry((AbstractSelectionList.Entry)this.loadingHeader);
/* 126 */     handleNewLevels(pollLevelsIgnoreErrors());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void clearEntries() {
/* 131 */     children().forEach(Entry::close);
/* 132 */     super.clearEntries();
/*     */   }
/*     */   
/*     */   private List<LevelSummary> pollLevelsIgnoreErrors() {
/*     */     try {
/* 137 */       List<LevelSummary> completedLevels = this.pendingLevels.getNow(null);
/* 138 */       if (this.entryType == EntryType.UPLOAD_WORLD) {
/* 139 */         if (completedLevels != null && !this.hasPolled) {
/* 140 */           this.hasPolled = true;
/* 141 */           completedLevels = completedLevels.stream().filter(LevelSummary::canUpload).toList();
/*     */         } else {
/* 143 */           return null;
/*     */         } 
/*     */       }
/* 146 */       return completedLevels;
/* 147 */     } catch (CompletionException|java.util.concurrent.CancellationException ignore) {
/* 148 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void reloadWorldList() {
/* 154 */     this.pendingLevels = loadLevels();
/*     */   }
/*     */ 
/*     */   
/*     */   public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 159 */     List<LevelSummary> newLevels = pollLevelsIgnoreErrors();
/*     */     
/* 161 */     if (newLevels != this.currentlyDisplayedLevels) {
/* 162 */       handleNewLevels(newLevels);
/*     */     }
/* 164 */     super.renderWidget(graphics, mouseX, mouseY, a);
/*     */   }
/*     */   
/*     */   private void handleNewLevels(List<LevelSummary> levels) {
/* 168 */     if (levels == null) {
/*     */       return;
/*     */     }
/* 171 */     if (levels.isEmpty()) {
/* 172 */       switch (this.entryType.ordinal()) { case 0:
/* 173 */           CreateWorldScreen.openFresh(this.minecraft, () -> this.minecraft.setScreen(null)); break;
/*     */         case 1:
/* 175 */           clearEntries();
/* 176 */           addEntry((AbstractSelectionList.Entry)new NoWorldsEntry((Component)Component.translatable("mco.upload.select.world.none"), this.screen.getFont()));
/*     */           break; }
/*     */     
/*     */     } else {
/* 180 */       fillLevels(this.filter, levels);
/* 181 */       this.currentlyDisplayedLevels = levels;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateFilter(String newFilter) {
/* 186 */     if (this.currentlyDisplayedLevels != null && !newFilter.equals(this.filter)) {
/* 187 */       fillLevels(newFilter, this.currentlyDisplayedLevels);
/*     */     }
/* 189 */     this.filter = newFilter;
/*     */   }
/*     */   
/*     */   private CompletableFuture<List<LevelSummary>> loadLevels() {
/*     */     LevelStorageSource.LevelCandidates levelCandidates;
/*     */     try {
/* 195 */       levelCandidates = this.minecraft.getLevelSource().findLevelCandidates();
/* 196 */     } catch (LevelStorageException e) {
/* 197 */       LOGGER.error("Couldn't load level list", (Throwable)e);
/* 198 */       handleLevelLoadFailure(e.getMessageComponent());
/* 199 */       return CompletableFuture.completedFuture(List.of());
/*     */     } 
/*     */     
/* 202 */     return this.minecraft.getLevelSource().loadLevelSummaries(levelCandidates)
/* 203 */       .exceptionally(throwable -> {
/*     */           this.minecraft.delayCrash(CrashReport.forThrowable(throwable, "Couldn't load level list"));
/*     */           return List.of();
/*     */         });
/*     */   }
/*     */   
/*     */   private void fillLevels(String filter, List<LevelSummary> levels) {
/* 210 */     List<Entry> worldEntries = new ArrayList<>();
/* 211 */     Optional<WorldListEntry> selectedOpt = getSelectedOpt();
/* 212 */     WorldListEntry entryToSelect = null;
/* 213 */     for (LevelSummary level : (Iterable<LevelSummary>)levels.stream().filter(level -> filterAccepts(filter.toLowerCase(Locale.ROOT), filter)).toList()) {
/* 214 */       WorldListEntry worldListEntry = new WorldListEntry(this, level);
/* 215 */       if (selectedOpt.isPresent() && ((WorldListEntry)selectedOpt.get()).getLevelSummary().getLevelId().equals(worldListEntry.getLevelSummary().getLevelId())) {
/* 216 */         entryToSelect = worldListEntry;
/*     */       }
/* 218 */       worldEntries.add(worldListEntry);
/*     */     } 
/* 220 */     removeEntries(children().stream().filter(entry -> !worldEntries.contains(entry)).toList());
/* 221 */     worldEntries.forEach(entry -> {
/*     */           if (!children().contains(entry)) {
/*     */             addEntry((AbstractSelectionList.Entry)entry);
/*     */           }
/*     */         });
/* 226 */     setSelected(entryToSelect);
/* 227 */     notifyListUpdated();
/*     */   }
/*     */   
/*     */   private boolean filterAccepts(String filter, LevelSummary level) {
/* 231 */     return (level.getLevelName().toLowerCase(Locale.ROOT).contains(filter) || level.getLevelId().toLowerCase(Locale.ROOT).contains(filter));
/*     */   }
/*     */   
/*     */   private void notifyListUpdated() {
/* 235 */     refreshScrollAmount();
/* 236 */     this.screen.triggerImmediateNarration(true);
/*     */   }
/*     */   
/*     */   private void handleLevelLoadFailure(Component message) {
/* 240 */     this.minecraft.setScreen((Screen)new ErrorScreen((Component)Component.translatable("selectWorld.unable_to_load"), message));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRowWidth() {
/* 245 */     return 270;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSelected(Entry selected) {
/* 250 */     super.setSelected((AbstractSelectionList.Entry)selected);
/* 251 */     if (this.onEntrySelect != null) {
/* 252 */       WorldListEntry entry = (WorldListEntry)selected; this.onEntrySelect.accept((selected instanceof WorldListEntry) ? entry.summary : null);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Optional<WorldListEntry> getSelectedOpt() {
/* 257 */     Entry selected = (Entry)getSelected();
/* 258 */     if (selected instanceof WorldListEntry) { WorldListEntry worldEntry = (WorldListEntry)selected;
/* 259 */       return Optional.of(worldEntry); }
/*     */     
/* 261 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public void returnToScreen() {
/* 265 */     reloadWorldList();
/* 266 */     this.minecraft.setScreen(this.screen);
/*     */   }
/*     */   
/*     */   public Screen getScreen() {
/* 270 */     return this.screen;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateWidgetNarration(NarrationElementOutput output) {
/* 275 */     if (children().contains(this.loadingHeader)) {
/* 276 */       this.loadingHeader.updateNarration(output);
/*     */       return;
/*     */     } 
/* 279 */     super.updateWidgetNarration(output);
/*     */   }
/*     */   
/*     */   public static abstract class Entry
/*     */     extends ObjectSelectionList.Entry<Entry>
/*     */     implements AutoCloseable {
/*     */     public void close() {}
/*     */     
/*     */     public LevelSummary getLevelSummary() {
/* 288 */       return null;
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class NoWorldsEntry extends Entry {
/*     */     private final StringWidget stringWidget;
/*     */     
/*     */     public NoWorldsEntry(Component component, Font font) {
/* 296 */       this.stringWidget = new StringWidget(component, font);
/*     */     }
/*     */ 
/*     */     
/*     */     public Component getNarration() {
/* 301 */       return this.stringWidget.getMessage();
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 306 */       this.stringWidget.setPosition(getContentXMiddle() - this.stringWidget.getWidth() / 2, getContentYMiddle() - this.stringWidget.getHeight() / 2);
/* 307 */       this.stringWidget.render(graphics, mouseX, mouseY, a);
/*     */     }
/*     */   }
/*     */   
/*     */   public final class WorldListEntry
/*     */     extends Entry
/*     */     implements SelectableEntry {
/*     */     private static final int ICON_SIZE = 32;
/*     */     private final WorldSelectionList list;
/*     */     private final Minecraft minecraft;
/*     */     private final Screen screen;
/*     */     private final LevelSummary summary;
/*     */     private final FaviconTexture icon;
/*     */     private final StringWidget worldNameText;
/*     */     private final StringWidget idAndLastPlayedText;
/*     */     private final StringWidget infoText;
/*     */     private Path iconFile;
/*     */     
/*     */     public WorldListEntry(WorldSelectionList list, LevelSummary summary) {
/* 326 */       this.list = list;
/* 327 */       this.minecraft = list.minecraft;
/* 328 */       this.screen = list.getScreen();
/* 329 */       this.summary = summary;
/* 330 */       this.icon = FaviconTexture.forWorld(this.minecraft.getTextureManager(), summary.getLevelId());
/* 331 */       this.iconFile = summary.getIcon();
/*     */       
/* 333 */       int maxTextWidth = list.getRowWidth() - getTextX() - 2;
/* 334 */       MutableComponent mutableComponent1 = Component.literal(summary.getLevelName());
/* 335 */       this.worldNameText = new StringWidget((Component)mutableComponent1, this.minecraft.font);
/* 336 */       this.worldNameText.setMaxWidth(maxTextWidth);
/* 337 */       if (this.minecraft.font.width((FormattedText)mutableComponent1) > maxTextWidth) {
/* 338 */         this.worldNameText.setTooltip(Tooltip.create((Component)mutableComponent1));
/*     */       }
/*     */       
/* 341 */       String levelIdAndDate = summary.getLevelId();
/* 342 */       long lastPlayed = summary.getLastPlayed();
/* 343 */       if (lastPlayed != -1L) {
/* 344 */         ZonedDateTime lastPlayedTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(lastPlayed), ZoneId.systemDefault());
/* 345 */         levelIdAndDate = levelIdAndDate + " (" + levelIdAndDate + ")";
/*     */       } 
/* 347 */       MutableComponent mutableComponent2 = Component.literal(levelIdAndDate).withColor(-8355712);
/* 348 */       this.idAndLastPlayedText = new StringWidget((Component)mutableComponent2, this.minecraft.font);
/* 349 */       this.idAndLastPlayedText.setMaxWidth(maxTextWidth);
/* 350 */       if (this.minecraft.font.width(levelIdAndDate) > maxTextWidth) {
/* 351 */         this.idAndLastPlayedText.setTooltip(Tooltip.create((Component)mutableComponent2));
/*     */       }
/*     */       
/* 354 */       Component info = ComponentUtils.mergeStyles(summary.getInfo(), Style.EMPTY.withColor(-8355712));
/* 355 */       this.infoText = new StringWidget(info, this.minecraft.font);
/* 356 */       this.infoText.setMaxWidth(maxTextWidth);
/* 357 */       if (this.minecraft.font.width((FormattedText)info) > maxTextWidth) {
/* 358 */         this.infoText.setTooltip(Tooltip.create(info));
/*     */       }
/*     */       
/* 361 */       validateIconFile();
/* 362 */       loadIcon();
/*     */     }
/*     */     
/*     */     private void validateIconFile() {
/* 366 */       if (this.iconFile == null) {
/*     */         return;
/*     */       }
/*     */       try {
/* 370 */         BasicFileAttributes attributes = Files.readAttributes(this.iconFile, BasicFileAttributes.class, new LinkOption[] { LinkOption.NOFOLLOW_LINKS });
/* 371 */         if (attributes.isSymbolicLink()) {
/* 372 */           List<ForbiddenSymlinkInfo> issues = this.minecraft.directoryValidator().validateSymlink(this.iconFile);
/* 373 */           if (!issues.isEmpty()) {
/* 374 */             WorldSelectionList.LOGGER.warn("{}", ContentValidationException.getMessage(this.iconFile, issues));
/* 375 */             this.iconFile = null;
/*     */           } else {
/* 377 */             attributes = Files.readAttributes(this.iconFile, BasicFileAttributes.class, new LinkOption[0]);
/*     */           } 
/*     */         } 
/* 380 */         if (!attributes.isRegularFile()) {
/* 381 */           this.iconFile = null;
/*     */         }
/* 383 */       } catch (NoSuchFileException e) {
/* 384 */         this.iconFile = null;
/* 385 */       } catch (IOException e) {
/* 386 */         WorldSelectionList.LOGGER.error("could not validate symlink", e);
/* 387 */         this.iconFile = null;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public Component getNarration() {
/* 393 */       MutableComponent mutableComponent = Component.translatable("narrator.select.world_info", new Object[] {
/* 394 */             this.summary.getLevelName(), 
/* 395 */             Component.translationArg(new Date(this.summary.getLastPlayed())), 
/* 396 */             this.summary.getInfo()
/*     */           });
/* 398 */       if (this.summary.isLocked()) {
/* 399 */         mutableComponent = CommonComponents.joinForNarration(new Component[] { (Component)mutableComponent, WorldSelectionList.WORLD_LOCKED_TOOLTIP });
/*     */       }
/* 401 */       if (this.summary.isExperimental()) {
/* 402 */         mutableComponent = CommonComponents.joinForNarration(new Component[] { (Component)mutableComponent, WorldSelectionList.WORLD_EXPERIMENTAL });
/*     */       }
/* 404 */       return (Component)Component.translatable("narrator.select", new Object[] { mutableComponent });
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 409 */       int textX = getTextX();
/* 410 */       this.worldNameText.setPosition(textX, getContentY() + 1);
/* 411 */       this.worldNameText.render(graphics, mouseX, mouseY, a);
/*     */       
/* 413 */       Objects.requireNonNull(this.minecraft.font); this.idAndLastPlayedText.setPosition(textX, getContentY() + 9 + 3);
/* 414 */       this.idAndLastPlayedText.render(graphics, mouseX, mouseY, a);
/*     */       
/* 416 */       Objects.requireNonNull(this.minecraft.font); Objects.requireNonNull(this.minecraft.font); this.infoText.setPosition(textX, getContentY() + 9 + 9 + 3);
/* 417 */       this.infoText.render(graphics, mouseX, mouseY, a);
/*     */       
/* 419 */       graphics.blit(RenderPipelines.GUI_TEXTURED, this.icon.textureLocation(), getContentX(), getContentY(), 0.0F, 0.0F, 32, 32, 32, 32);
/*     */       
/* 421 */       if (this.list.entryType == WorldSelectionList.EntryType.SINGLEPLAYER && ((Boolean)this.minecraft.options.touchscreen().get() || hovered)) {
/* 422 */         graphics.fill(getContentX(), getContentY(), getContentX() + 32, getContentY() + 32, -1601138544);
/* 423 */         int relX = mouseX - getContentX();
/* 424 */         int relY = mouseY - getContentY();
/*     */         
/* 426 */         boolean isOverIcon = mouseOverIcon(relX, relY, 32);
/* 427 */         Identifier joinSprite = isOverIcon ? WorldSelectionList.JOIN_HIGHLIGHTED_SPRITE : WorldSelectionList.JOIN_SPRITE;
/* 428 */         Identifier warningSprite = isOverIcon ? WorldSelectionList.WARNING_HIGHLIGHTED_SPRITE : WorldSelectionList.WARNING_SPRITE;
/* 429 */         Identifier errorSprite = isOverIcon ? WorldSelectionList.ERROR_HIGHLIGHTED_SPRITE : WorldSelectionList.ERROR_SPRITE;
/* 430 */         Identifier joinWithErrorSprite = isOverIcon ? WorldSelectionList.MARKED_JOIN_HIGHLIGHTED_SPRITE : WorldSelectionList.MARKED_JOIN_SPRITE;
/* 431 */         if (this.summary instanceof LevelSummary.SymlinkLevelSummary || this.summary instanceof LevelSummary.CorruptedLevelSummary) {
/* 432 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, errorSprite, getContentX(), getContentY(), 32, 32);
/* 433 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, joinWithErrorSprite, getContentX(), getContentY(), 32, 32);
/*     */           
/*     */           return;
/*     */         } 
/* 437 */         if (this.summary.isLocked()) {
/* 438 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, errorSprite, getContentX(), getContentY(), 32, 32);
/* 439 */           if (isOverIcon) {
/* 440 */             graphics.setTooltipForNextFrame(this.minecraft.font.split((FormattedText)WorldSelectionList.WORLD_LOCKED_TOOLTIP, 175), mouseX, mouseY);
/*     */           }
/* 442 */         } else if (this.summary.requiresManualConversion()) {
/* 443 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, errorSprite, getContentX(), getContentY(), 32, 32);
/* 444 */           if (isOverIcon) {
/* 445 */             graphics.setTooltipForNextFrame(this.minecraft.font.split((FormattedText)WorldSelectionList.WORLD_REQUIRES_CONVERSION, 175), mouseX, mouseY);
/*     */           }
/* 447 */         } else if (!this.summary.isCompatible()) {
/* 448 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, errorSprite, getContentX(), getContentY(), 32, 32);
/* 449 */           if (isOverIcon) {
/* 450 */             graphics.setTooltipForNextFrame(this.minecraft.font.split((FormattedText)WorldSelectionList.INCOMPATIBLE_VERSION_TOOLTIP, 175), mouseX, mouseY);
/*     */           }
/* 452 */         } else if (this.summary.shouldBackup()) {
/* 453 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, joinWithErrorSprite, getContentX(), getContentY(), 32, 32);
/* 454 */           if (this.summary.isDowngrade()) {
/* 455 */             graphics.blitSprite(RenderPipelines.GUI_TEXTURED, errorSprite, getContentX(), getContentY(), 32, 32);
/* 456 */             if (isOverIcon) {
/* 457 */               graphics.setTooltipForNextFrame((List)ImmutableList.of(WorldSelectionList.FROM_NEWER_TOOLTIP_1.getVisualOrderText(), WorldSelectionList.FROM_NEWER_TOOLTIP_2.getVisualOrderText()), mouseX, mouseY);
/*     */             }
/* 459 */           } else if (!SharedConstants.getCurrentVersion().stable()) {
/* 460 */             graphics.blitSprite(RenderPipelines.GUI_TEXTURED, warningSprite, getContentX(), getContentY(), 32, 32);
/* 461 */             if (isOverIcon) {
/* 462 */               graphics.setTooltipForNextFrame((List)ImmutableList.of(WorldSelectionList.SNAPSHOT_TOOLTIP_1.getVisualOrderText(), WorldSelectionList.SNAPSHOT_TOOLTIP_2.getVisualOrderText()), mouseX, mouseY);
/*     */             }
/*     */           } 
/* 465 */           if (isOverIcon) {
/* 466 */             WorldSelectionList.this.handleCursor(graphics);
/*     */           }
/*     */         } else {
/* 469 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, joinSprite, getContentX(), getContentY(), 32, 32);
/* 470 */           if (isOverIcon) {
/* 471 */             WorldSelectionList.this.handleCursor(graphics);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private int getTextX() {
/* 478 */       return getContentX() + 32 + 3;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 483 */       if (canInteract()) {
/* 484 */         int relX = (int)event.x() - getContentX();
/* 485 */         int relY = (int)event.y() - getContentY();
/*     */         
/* 487 */         if (doubleClick || (mouseOverIcon(relX, relY, 32) && this.list.entryType == WorldSelectionList.EntryType.SINGLEPLAYER)) {
/* 488 */           this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/* 489 */           Consumer<WorldListEntry> onEntryInteract = this.list.onEntryInteract;
/* 490 */           if (onEntryInteract != null) {
/* 491 */             onEntryInteract.accept(this);
/* 492 */             return true;
/*     */           } 
/*     */         } 
/*     */       } 
/* 496 */       return super.mouseClicked(event, doubleClick);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean keyPressed(KeyEvent event) {
/* 501 */       if (event.isSelection() && canInteract()) {
/* 502 */         this.minecraft.getSoundManager().play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/* 503 */         Consumer<WorldListEntry> onEntryInteract = this.list.onEntryInteract;
/* 504 */         if (onEntryInteract != null) {
/* 505 */           onEntryInteract.accept(this);
/* 506 */           return true;
/*     */         } 
/*     */       } 
/* 509 */       return super.keyPressed(event);
/*     */     }
/*     */     
/*     */     public boolean canInteract() {
/* 513 */       return (this.summary.primaryActionActive() || this.list.entryType == WorldSelectionList.EntryType.UPLOAD_WORLD);
/*     */     }
/*     */     
/*     */     public void joinWorld() {
/* 517 */       if (!this.summary.primaryActionActive()) {
/*     */         return;
/*     */       }
/*     */       
/* 521 */       if (this.summary instanceof LevelSummary.SymlinkLevelSummary) {
/* 522 */         this.minecraft.setScreen(NoticeWithLinkScreen.createWorldSymlinkWarningScreen(() -> this.minecraft.setScreen(this.screen)));
/*     */         
/*     */         return;
/*     */       } 
/* 526 */       Objects.requireNonNull(this.list); this.minecraft.createWorldOpenFlows().openWorld(this.summary.getLevelId(), this.list::returnToScreen);
/*     */     }
/*     */     
/*     */     public void deleteWorld() {
/* 530 */       this.minecraft.setScreen((Screen)new ConfirmScreen(result -> {
/*     */               if (result) {
/*     */                 this.minecraft.setScreen((Screen)new ProgressScreen(true));
/*     */                 
/*     */                 doDeleteWorld();
/*     */               } 
/*     */               
/*     */               this.list.returnToScreen();
/* 538 */             }, (Component)Component.translatable("selectWorld.deleteQuestion"), 
/* 539 */             (Component)Component.translatable("selectWorld.deleteWarning", new Object[] { this.summary.getLevelName()
/* 540 */               }), (Component)Component.translatable("selectWorld.deleteButton"), CommonComponents.GUI_CANCEL));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void doDeleteWorld() {
/* 546 */       LevelStorageSource levelSource = this.minecraft.getLevelSource();
/* 547 */       String levelId = this.summary.getLevelId();
/*     */       
/* 549 */       try { LevelStorageSource.LevelStorageAccess access = levelSource.createAccess(levelId); 
/* 550 */         try { access.deleteLevel();
/* 551 */           if (access != null) access.close();  } catch (Throwable throwable) { if (access != null) try { access.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 552 */       { SystemToast.onWorldDeleteFailure(this.minecraft, levelId);
/* 553 */         WorldSelectionList.LOGGER.error("Failed to delete world {}", levelId, e); }
/*     */     
/*     */     } public void editWorld() {
/*     */       LevelStorageSource.LevelStorageAccess access;
/*     */       EditWorldScreen editScreen;
/* 558 */       queueLoadScreen();
/* 559 */       String levelId = this.summary.getLevelId();
/*     */       
/*     */       try {
/* 562 */         access = this.minecraft.getLevelSource().validateAndCreateAccess(levelId);
/* 563 */       } catch (IOException e) {
/* 564 */         SystemToast.onWorldAccessFailure(this.minecraft, levelId);
/* 565 */         WorldSelectionList.LOGGER.error("Failed to access level {}", levelId, e);
/* 566 */         this.list.reloadWorldList();
/*     */         return;
/* 568 */       } catch (ContentValidationException e) {
/* 569 */         WorldSelectionList.LOGGER.warn("{}", e.getMessage());
/* 570 */         this.minecraft.setScreen(NoticeWithLinkScreen.createWorldSymlinkWarningScreen(() -> this.minecraft.setScreen(this.screen)));
/*     */         
/*     */         return;
/*     */       } 
/*     */       try {
/* 575 */         editScreen = EditWorldScreen.create(this.minecraft, access, result -> {
/*     */               access.safeClose();
/*     */ 
/*     */               
/*     */               this.list.returnToScreen();
/*     */             });
/* 581 */       } catch (IOException|net.minecraft.nbt.NbtException|net.minecraft.nbt.ReportedNbtException e) {
/* 582 */         access.safeClose();
/* 583 */         SystemToast.onWorldAccessFailure(this.minecraft, levelId);
/* 584 */         WorldSelectionList.LOGGER.error("Failed to load world data {}", levelId, e);
/* 585 */         this.list.reloadWorldList();
/*     */         return;
/*     */       } 
/* 588 */       this.minecraft.setScreen(editScreen);
/*     */     }
/*     */     
/*     */     public void recreateWorld() {
/* 592 */       queueLoadScreen();
/*     */       
/* 594 */       try { LevelStorageSource.LevelStorageAccess access = this.minecraft.getLevelSource().validateAndCreateAccess(this.summary.getLevelId());
/*     */         
/* 596 */         try { Pair<LevelSettings, WorldCreationContext> recreatedSettings = this.minecraft.createWorldOpenFlows().recreateWorldData(access);
/* 597 */           LevelSettings levelSettings = (LevelSettings)recreatedSettings.getFirst();
/* 598 */           WorldCreationContext creationContext = (WorldCreationContext)recreatedSettings.getSecond();
/* 599 */           Path dataPackDir = CreateWorldScreen.createTempDataPackDirFromExistingWorld(access.getLevelPath(LevelResource.DATAPACK_DIR), this.minecraft);
/*     */           
/* 601 */           creationContext.validate();
/* 602 */           if (creationContext.options().isOldCustomizedWorld()) {
/* 603 */             this.minecraft.setScreen((Screen)new ConfirmScreen(result -> {
/*     */                     Objects.requireNonNull(this.list); this.minecraft.setScreen(dataPackDir ? CreateWorldScreen.createFromExisting(this.minecraft, this.list::returnToScreen, levelSettings, levelSettings, creationContext) : this.screen);
/* 605 */                   }, (Component)Component.translatable("selectWorld.recreate.customized.title"), 
/* 606 */                   (Component)Component.translatable("selectWorld.recreate.customized.text"), CommonComponents.GUI_PROCEED, CommonComponents.GUI_CANCEL));
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 611 */             Objects.requireNonNull(this.list); this.minecraft.setScreen(CreateWorldScreen.createFromExisting(this.minecraft, this.list::returnToScreen, levelSettings, creationContext, dataPackDir));
/*     */           } 
/* 613 */           if (access != null) access.close();  } catch (Throwable throwable) { if (access != null) try { access.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (ContentValidationException e)
/* 614 */       { WorldSelectionList.LOGGER.warn("{}", e.getMessage());
/* 615 */         this.minecraft.setScreen(NoticeWithLinkScreen.createWorldSymlinkWarningScreen(() -> this.minecraft.setScreen(this.screen))); }
/* 616 */       catch (Exception e)
/* 617 */       { WorldSelectionList.LOGGER.error("Unable to recreate world", e);
/* 618 */         this.minecraft.setScreen((Screen)new AlertScreen(() -> this.minecraft.setScreen(this.screen), 
/*     */               
/* 620 */               (Component)Component.translatable("selectWorld.recreate.error.title"), 
/* 621 */               (Component)Component.translatable("selectWorld.recreate.error.text"))); }
/*     */     
/*     */     }
/*     */ 
/*     */     
/*     */     private void queueLoadScreen() {
/* 627 */       this.minecraft.setScreenAndShow((Screen)new GenericMessageScreen((Component)Component.translatable("selectWorld.data_read")));
/*     */     }
/*     */     
/*     */     private void loadIcon() {
/* 631 */       boolean shouldHaveIcon = (this.iconFile != null && Files.isRegularFile(this.iconFile, new LinkOption[0]));
/* 632 */       if (shouldHaveIcon) { 
/* 633 */         try { InputStream stream = Files.newInputStream(this.iconFile, new java.nio.file.OpenOption[0]); 
/* 634 */           try { this.icon.upload(NativeImage.read(stream));
/* 635 */             if (stream != null) stream.close();  } catch (Throwable throwable) { if (stream != null) try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Throwable t)
/* 636 */         { WorldSelectionList.LOGGER.error("Invalid icon for world {}", this.summary.getLevelId(), t);
/* 637 */           this.iconFile = null; }
/*     */          }
/*     */       else
/* 640 */       { this.icon.clear(); }
/*     */     
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() {
/* 646 */       if (!this.icon.isClosed()) {
/* 647 */         this.icon.close();
/*     */       }
/*     */     }
/*     */     
/*     */     public String getLevelName() {
/* 652 */       return this.summary.getLevelName();
/*     */     }
/*     */ 
/*     */     
/*     */     public LevelSummary getLevelSummary() {
/* 657 */       return this.summary;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class LoadingHeader extends Entry {
/* 662 */     private static final Component LOADING_LABEL = (Component)Component.translatable("selectWorld.loading_list");
/*     */     
/*     */     private final Minecraft minecraft;
/*     */     
/*     */     public LoadingHeader(Minecraft minecraft) {
/* 667 */       this.minecraft = minecraft;
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 672 */       int labelX = (this.minecraft.screen.width - this.minecraft.font.width((FormattedText)LOADING_LABEL)) / 2;
/* 673 */       Objects.requireNonNull(this.minecraft.font); int labelY = getContentY() + (getContentHeight() - 9) / 2;
/* 674 */       graphics.drawString(this.minecraft.font, LOADING_LABEL, labelX, labelY, -1);
/*     */       
/* 676 */       String dots = LoadingDotsText.get(Util.getMillis());
/* 677 */       int dotsX = (this.minecraft.screen.width - this.minecraft.font.width(dots)) / 2;
/* 678 */       Objects.requireNonNull(this.minecraft.font); int dotsY = labelY + 9;
/* 679 */       graphics.drawString(this.minecraft.font, dots, dotsX, dotsY, -8355712);
/*     */     }
/*     */ 
/*     */     
/*     */     public Component getNarration() {
/* 684 */       return LOADING_LABEL;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Builder
/*     */   {
/*     */     private final Minecraft minecraft;
/*     */     private final Screen screen;
/*     */     private int width;
/*     */     private int height;
/* 694 */     private String filter = "";
/* 695 */     private WorldSelectionList.EntryType type = WorldSelectionList.EntryType.SINGLEPLAYER;
/*     */     
/* 697 */     private WorldSelectionList oldList = null;
/* 698 */     private Consumer<LevelSummary> onEntrySelect = null;
/* 699 */     private Consumer<WorldSelectionList.WorldListEntry> onEntryInteract = null;
/*     */     
/*     */     public Builder(Minecraft minecraft, Screen screen) {
/* 702 */       this.minecraft = minecraft;
/* 703 */       this.screen = screen;
/*     */     }
/*     */     
/*     */     public Builder width(int width) {
/* 707 */       this.width = width;
/* 708 */       return this;
/*     */     }
/*     */     
/*     */     public Builder height(int height) {
/* 712 */       this.height = height;
/* 713 */       return this;
/*     */     }
/*     */     
/*     */     public Builder filter(String filter) {
/* 717 */       this.filter = filter;
/* 718 */       return this;
/*     */     }
/*     */     
/*     */     public Builder oldList(WorldSelectionList oldList) {
/* 722 */       this.oldList = oldList;
/* 723 */       return this;
/*     */     }
/*     */     
/*     */     public Builder onEntrySelect(Consumer<LevelSummary> onEntrySelect) {
/* 727 */       this.onEntrySelect = onEntrySelect;
/* 728 */       return this;
/*     */     }
/*     */     
/*     */     public Builder onEntryInteract(Consumer<WorldSelectionList.WorldListEntry> onEntryInteract) {
/* 732 */       this.onEntryInteract = onEntryInteract;
/* 733 */       return this;
/*     */     }
/*     */     
/*     */     public Builder uploadWorld() {
/* 737 */       this.type = WorldSelectionList.EntryType.UPLOAD_WORLD;
/* 738 */       return this;
/*     */     }
/*     */     
/*     */     public WorldSelectionList build() {
/* 742 */       return new WorldSelectionList(this.screen, this.minecraft, this.width, this.height, this.filter, this.oldList, this.onEntrySelect, this.onEntryInteract, this.type);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum EntryType {
/* 747 */     SINGLEPLAYER,
/* 748 */     UPLOAD_WORLD;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/worldselection/WorldSelectionList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */