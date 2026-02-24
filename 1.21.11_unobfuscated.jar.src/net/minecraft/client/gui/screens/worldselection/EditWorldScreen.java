/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.gui.components.toasts.Toast;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.layouts.SpacerElement;
/*     */ import net.minecraft.client.gui.screens.BackupConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.StringUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.LevelSummary;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class EditWorldScreen
/*     */   extends Screen
/*     */ {
/*  42 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  43 */   private static final Component NAME_LABEL = (Component)Component.translatable("selectWorld.enterName").withStyle(ChatFormatting.GRAY);
/*  44 */   private static final Component RESET_ICON_BUTTON = (Component)Component.translatable("selectWorld.edit.resetIcon");
/*  45 */   private static final Component FOLDER_BUTTON = (Component)Component.translatable("selectWorld.edit.openFolder");
/*  46 */   private static final Component BACKUP_BUTTON = (Component)Component.translatable("selectWorld.edit.backup");
/*  47 */   private static final Component BACKUP_FOLDER_BUTTON = (Component)Component.translatable("selectWorld.edit.backupFolder");
/*  48 */   private static final Component OPTIMIZE_BUTTON = (Component)Component.translatable("selectWorld.edit.optimize");
/*  49 */   private static final Component OPTIMIZE_TITLE = (Component)Component.translatable("optimizeWorld.confirm.title");
/*  50 */   private static final Component OPTIMIIZE_DESCRIPTION = (Component)Component.translatable("optimizeWorld.confirm.description");
/*  51 */   private static final Component OPTIMIIZE_CONFIRMATION = (Component)Component.translatable("optimizeWorld.confirm.proceed");
/*  52 */   private static final Component SAVE_BUTTON = (Component)Component.translatable("selectWorld.edit.save");
/*     */   
/*     */   private static final int DEFAULT_WIDTH = 200;
/*     */   
/*     */   private static final int VERTICAL_SPACING = 4;
/*     */   private static final int HALF_WIDTH = 98;
/*  58 */   private final LinearLayout layout = LinearLayout.vertical().spacing(5);
/*     */   
/*     */   private final BooleanConsumer callback;
/*     */   private final LevelStorageSource.LevelStorageAccess levelAccess;
/*     */   private final EditBox nameEdit;
/*     */   
/*     */   public static EditWorldScreen create(Minecraft minecraft, LevelStorageSource.LevelStorageAccess levelAccess, BooleanConsumer callback) throws IOException {
/*  65 */     LevelSummary summary = levelAccess.getSummary(levelAccess.getDataTag());
/*  66 */     return new EditWorldScreen(minecraft, levelAccess, summary.getLevelName(), callback);
/*     */   }
/*     */   
/*     */   private EditWorldScreen(Minecraft minecraft, LevelStorageSource.LevelStorageAccess levelAccess, String name, BooleanConsumer callback) {
/*  70 */     super((Component)Component.translatable("selectWorld.edit.title"));
/*  71 */     this.callback = callback;
/*  72 */     this.levelAccess = levelAccess;
/*     */     
/*  74 */     Font font = minecraft.font;
/*  75 */     this.layout.addChild((LayoutElement)new SpacerElement(200, 20));
/*  76 */     this.layout.addChild((LayoutElement)new StringWidget(NAME_LABEL, font));
/*  77 */     this.nameEdit = (EditBox)this.layout.addChild((LayoutElement)new EditBox(font, 200, 20, NAME_LABEL));
/*  78 */     this.nameEdit.setValue(name);
/*     */ 
/*     */     
/*  81 */     LinearLayout bottomButtonRow = LinearLayout.horizontal().spacing(4);
/*  82 */     Button renameButton = (Button)bottomButtonRow.addChild(
/*  83 */         (LayoutElement)Button.builder(SAVE_BUTTON, button -> onRename(this.nameEdit.getValue()))
/*  84 */         .width(98)
/*  85 */         .build());
/*  86 */     bottomButtonRow.addChild(
/*  87 */         (LayoutElement)Button.builder(CommonComponents.GUI_CANCEL, button -> onClose())
/*  88 */         .width(98)
/*  89 */         .build());
/*     */ 
/*     */     
/*  92 */     this.nameEdit.setResponder(newName -> renameButton.active = !StringUtil.isBlank(newName));
/*  93 */     ((Button)this.layout.addChild(
/*  94 */         (LayoutElement)Button.builder(RESET_ICON_BUTTON, button -> {
/*     */             levelAccess.getIconFile().ifPresent(());
/*     */             
/*     */             button.active = false;
/*  98 */           }).width(200)
/*  99 */         .build()))
/* 100 */       .active = levelAccess.getIconFile().filter(x$0 -> Files.isRegularFile(x$0, new java.nio.file.LinkOption[0])).isPresent();
/*     */     
/* 102 */     this.layout.addChild(
/* 103 */         (LayoutElement)Button.builder(FOLDER_BUTTON, button -> Util.getPlatform().openPath(levelAccess.getLevelPath(LevelResource.ROOT)))
/*     */ 
/*     */         
/* 106 */         .width(200)
/* 107 */         .build());
/*     */     
/* 109 */     this.layout.addChild(
/* 110 */         (LayoutElement)Button.builder(BACKUP_BUTTON, button -> {
/*     */             boolean success = makeBackupAndShowToast(levelAccess);
/*     */ 
/*     */             
/*     */             this.callback.accept(!success);
/* 115 */           }).width(200)
/* 116 */         .build());
/*     */     
/* 118 */     this.layout.addChild(
/* 119 */         (LayoutElement)Button.builder(BACKUP_FOLDER_BUTTON, button -> {
/*     */             LevelStorageSource levelSource = minecraft.getLevelSource();
/*     */             Path path = levelSource.getBackupPath();
/*     */             try {
/*     */               FileUtil.createDirectoriesSafe(path);
/* 124 */             } catch (IOException e) {
/*     */               throw new RuntimeException(e);
/*     */             } 
/*     */             
/*     */             Util.getPlatform().openPath(path);
/* 129 */           }).width(200)
/* 130 */         .build());
/*     */     
/* 132 */     this.layout.addChild(
/* 133 */         (LayoutElement)Button.builder(OPTIMIZE_BUTTON, button -> minecraft.setScreen((Screen)new BackupConfirmScreen((), (), OPTIMIZE_TITLE, OPTIMIIZE_DESCRIPTION, OPTIMIIZE_CONFIRMATION, true)))
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 142 */         .width(200)
/* 143 */         .build());
/*     */ 
/*     */     
/* 146 */     this.layout.addChild((LayoutElement)new SpacerElement(200, 20));
/* 147 */     this.layout.addChild((LayoutElement)bottomButtonRow);
/*     */     
/* 149 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {
/* 154 */     setInitialFocus((GuiEventListener)this.nameEdit);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/* 159 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/* 164 */     this.layout.arrangeElements();
/* 165 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 170 */     if (this.nameEdit.isFocused() && event.isConfirmation()) {
/* 171 */       onRename(this.nameEdit.getValue());
/* 172 */       onClose();
/* 173 */       return true;
/*     */     } 
/* 175 */     return super.keyPressed(event);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 180 */     this.callback.accept(false);
/*     */   }
/*     */   
/*     */   private void onRename(String newName) {
/*     */     try {
/* 185 */       this.levelAccess.renameLevel(newName);
/* 186 */     } catch (IOException|net.minecraft.nbt.NbtException|net.minecraft.nbt.ReportedNbtException e) {
/* 187 */       LOGGER.error("Failed to access world '{}'", this.levelAccess.getLevelId(), e);
/* 188 */       SystemToast.onWorldAccessFailure(this.minecraft, this.levelAccess.getLevelId());
/*     */     } 
/* 190 */     this.callback.accept(true);
/*     */   }
/*     */   
/*     */   public static boolean makeBackupAndShowToast(LevelStorageSource.LevelStorageAccess access) {
/* 194 */     long size = 0L;
/* 195 */     IOException exception = null;
/*     */     try {
/* 197 */       size = access.makeWorldBackup();
/* 198 */     } catch (IOException e) {
/* 199 */       exception = e;
/*     */     } 
/*     */     
/* 202 */     if (exception != null) {
/* 203 */       MutableComponent mutableComponent3 = Component.translatable("selectWorld.edit.backupFailed");
/* 204 */       MutableComponent mutableComponent4 = Component.literal(exception.getMessage());
/* 205 */       Minecraft.getInstance().getToastManager().addToast((Toast)new SystemToast(SystemToast.SystemToastId.WORLD_BACKUP, (Component)mutableComponent3, (Component)mutableComponent4));
/* 206 */       return false;
/*     */     } 
/* 208 */     MutableComponent mutableComponent1 = Component.translatable("selectWorld.edit.backupCreated", new Object[] { access.getLevelId() });
/* 209 */     MutableComponent mutableComponent2 = Component.translatable("selectWorld.edit.backupSize", new Object[] { Mth.ceil(size / 1048576.0D) });
/* 210 */     Minecraft.getInstance().getToastManager().addToast((Toast)new SystemToast(SystemToast.SystemToastId.WORLD_BACKUP, (Component)mutableComponent1, (Component)mutableComponent2));
/* 211 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 216 */     super.render(graphics, mouseX, mouseY, a);
/* 217 */     graphics.drawCenteredString(this.font, this.title, this.width / 2, 15, -1);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/worldselection/EditWorldScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */