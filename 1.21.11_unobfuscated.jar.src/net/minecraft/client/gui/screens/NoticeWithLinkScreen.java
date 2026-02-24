/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import java.net.URI;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.layouts.FrameLayout;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.CommonLinks;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class NoticeWithLinkScreen extends Screen {
/*  17 */   private static final Component SYMLINK_WORLD_TITLE = (Component)Component.translatable("symlink_warning.title.world").withStyle(ChatFormatting.BOLD);
/*  18 */   private static final Component SYMLINK_WORLD_MESSAGE_TEXT = (Component)Component.translatable("symlink_warning.message.world", new Object[] { Component.translationArg(CommonLinks.SYMLINK_HELP) });
/*     */   
/*  20 */   private static final Component SYMLINK_PACK_TITLE = (Component)Component.translatable("symlink_warning.title.pack").withStyle(ChatFormatting.BOLD);
/*  21 */   private static final Component SYMLINK_PACK_MESSAGE_TEXT = (Component)Component.translatable("symlink_warning.message.pack", new Object[] { Component.translationArg(CommonLinks.SYMLINK_HELP) });
/*     */   
/*     */   private final Component message;
/*     */   private final URI uri;
/*     */   private final Runnable onClose;
/*  26 */   private final GridLayout layout = new GridLayout().rowSpacing(10);
/*     */   
/*     */   public NoticeWithLinkScreen(Component title, Component message, URI uri, Runnable onClose) {
/*  29 */     super(title);
/*  30 */     this.message = message;
/*  31 */     this.uri = uri;
/*  32 */     this.onClose = onClose;
/*     */   }
/*     */   
/*     */   public static Screen createWorldSymlinkWarningScreen(Runnable onClose) {
/*  36 */     return new NoticeWithLinkScreen(SYMLINK_WORLD_TITLE, SYMLINK_WORLD_MESSAGE_TEXT, CommonLinks.SYMLINK_HELP, onClose);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Screen createPackSymlinkWarningScreen(Runnable onClose) {
/*  45 */     return new NoticeWithLinkScreen(SYMLINK_PACK_TITLE, SYMLINK_PACK_MESSAGE_TEXT, CommonLinks.SYMLINK_HELP, onClose);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void init() {
/*  55 */     super.init();
/*     */     
/*  57 */     this.layout.defaultCellSetting().alignHorizontallyCenter();
/*  58 */     GridLayout.RowHelper rowHelper = this.layout.createRowHelper(1);
/*     */     
/*  60 */     rowHelper.addChild((LayoutElement)new StringWidget(this.title, this.font));
/*  61 */     rowHelper.addChild((LayoutElement)new MultiLineTextWidget(this.message, this.font).setMaxWidth(this.width - 50).setCentered(true));
/*     */     
/*  63 */     int buttonWidth = 120;
/*     */     
/*  65 */     GridLayout buttonGrid = new GridLayout().columnSpacing(5);
/*  66 */     GridLayout.RowHelper buttonRow = buttonGrid.createRowHelper(3);
/*  67 */     buttonRow.addChild(
/*  68 */         (LayoutElement)Button.builder(CommonComponents.GUI_OPEN_IN_BROWSER, button -> Util.getPlatform().openUri(this.uri))
/*     */ 
/*     */ 
/*     */         
/*  72 */         .size(120, 20)
/*  73 */         .build());
/*     */     
/*  75 */     buttonRow.addChild(
/*  76 */         (LayoutElement)Button.builder(CommonComponents.GUI_COPY_LINK_TO_CLIPBOARD, button -> this.minecraft.keyboardHandler.setClipboard(this.uri.toString()))
/*     */ 
/*     */ 
/*     */         
/*  80 */         .size(120, 20)
/*  81 */         .build());
/*     */     
/*  83 */     buttonRow.addChild(
/*  84 */         (LayoutElement)Button.builder(CommonComponents.GUI_BACK, button -> onClose())
/*     */ 
/*     */ 
/*     */         
/*  88 */         .size(120, 20)
/*  89 */         .build());
/*     */ 
/*     */     
/*  92 */     rowHelper.addChild((LayoutElement)buttonGrid);
/*     */     
/*  94 */     repositionElements();
/*  95 */     this.layout.visitWidgets(this::addRenderableWidget);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/* 100 */     this.layout.arrangeElements();
/* 101 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getNarrationMessage() {
/* 106 */     return (Component)CommonComponents.joinForNarration(new Component[] { super.getNarrationMessage(), this.message });
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 111 */     this.onClose.run();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/NoticeWithLinkScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */