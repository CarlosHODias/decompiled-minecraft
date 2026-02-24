/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*     */ import java.net.URI;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class ConfirmLinkScreen
/*     */   extends ConfirmScreen {
/*  17 */   private static final Component WARNING_TEXT = (Component)Component.translatable("chat.link.warning")
/*  18 */     .withColor(-13108);
/*     */   
/*     */   private static final int BUTTON_WIDTH = 100;
/*     */   
/*     */   private final String url;
/*     */   private final boolean showWarning;
/*     */   
/*     */   public ConfirmLinkScreen(BooleanConsumer callback, String url, boolean trusted) {
/*  26 */     this(callback, (Component)confirmMessage(trusted), (Component)Component.literal(url), url, trusted ? CommonComponents.GUI_CANCEL : CommonComponents.GUI_NO, trusted);
/*     */   }
/*     */   
/*     */   public ConfirmLinkScreen(BooleanConsumer callback, Component title, String url, boolean trusted) {
/*  30 */     this(callback, title, (Component)confirmMessage(trusted, url), url, trusted ? CommonComponents.GUI_CANCEL : CommonComponents.GUI_NO, trusted);
/*     */   }
/*     */   
/*     */   public ConfirmLinkScreen(BooleanConsumer callback, Component title, URI uri, boolean trusted) {
/*  34 */     this(callback, title, uri.toString(), trusted);
/*     */   }
/*     */   
/*     */   public ConfirmLinkScreen(BooleanConsumer callback, Component title, Component message, URI uri, Component noButton, boolean trusted) {
/*  38 */     this(callback, title, message, uri.toString(), noButton, true);
/*     */   }
/*     */   
/*     */   public ConfirmLinkScreen(BooleanConsumer callback, Component title, Component message, String url, Component noButtonComponent, boolean trusted) {
/*  42 */     super(callback, title, message);
/*  43 */     this.yesButtonComponent = trusted ? CommonComponents.GUI_OPEN_IN_BROWSER : CommonComponents.GUI_YES;
/*  44 */     this.noButtonComponent = noButtonComponent;
/*  45 */     this.showWarning = !trusted;
/*  46 */     this.url = url;
/*     */   }
/*     */   
/*     */   protected static MutableComponent confirmMessage(boolean trusted, String url) {
/*  50 */     return confirmMessage(trusted).append(CommonComponents.SPACE).append((Component)Component.literal(url));
/*     */   }
/*     */   
/*     */   protected static MutableComponent confirmMessage(boolean trusted) {
/*  54 */     return Component.translatable(trusted ? "chat.link.confirmTrusted" : "chat.link.confirm");
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalText() {
/*  59 */     if (this.showWarning) {
/*  60 */       this.layout.addChild((LayoutElement)new StringWidget(WARNING_TEXT, this.font));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addButtons(LinearLayout buttonLayout) {
/*  66 */     this.yesButton = (Button)buttonLayout.addChild((LayoutElement)Button.builder(this.yesButtonComponent, button -> this.callback.accept(true)).width(100).build());
/*  67 */     buttonLayout.addChild((LayoutElement)Button.builder(CommonComponents.GUI_COPY_TO_CLIPBOARD, button -> {
/*     */             copyToClipboard();
/*     */             this.callback.accept(false);
/*  70 */           }).width(100).build());
/*  71 */     this.noButton = (Button)buttonLayout.addChild((LayoutElement)Button.builder(this.noButtonComponent, button -> this.callback.accept(false)).width(100).build());
/*     */   }
/*     */   
/*     */   public void copyToClipboard() {
/*  75 */     this.minecraft.keyboardHandler.setClipboard(this.url);
/*     */   }
/*     */   
/*     */   public static void confirmLinkNow(Screen parentScreen, String uri, boolean trusted) {
/*  79 */     Minecraft minecraft = Minecraft.getInstance();
/*  80 */     minecraft.setScreen(new ConfirmLinkScreen(shouldOpen -> { if (shouldOpen) Util.getPlatform().openUri(uri);  minecraft.setScreen(parentScreen); }, uri, trusted));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void confirmLinkNow(Screen parentScreen, URI uri, boolean trusted) {
/*  91 */     Minecraft minecraft = Minecraft.getInstance();
/*  92 */     minecraft.setScreen(new ConfirmLinkScreen(shouldOpen -> {
/*     */             if (shouldOpen) {
/*     */               Util.getPlatform().openUri(uri);
/*     */             }
/*     */             
/*     */             minecraft.setScreen(parentScreen);
/*  98 */           }, uri.toString(), trusted));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void confirmLinkNow(Screen parentScreen, URI uri) {
/* 103 */     confirmLinkNow(parentScreen, uri, true);
/*     */   }
/*     */   
/*     */   public static void confirmLinkNow(Screen parentScreen, String uri) {
/* 107 */     confirmLinkNow(parentScreen, uri, true);
/*     */   }
/*     */   
/*     */   public static Button.OnPress confirmLink(Screen parentScreen, String uri, boolean trusted) {
/* 111 */     return button -> confirmLinkNow(parentScreen, uri, trusted);
/*     */   }
/*     */   
/*     */   public static Button.OnPress confirmLink(Screen parentScreen, URI uri, boolean trusted) {
/* 115 */     return button -> confirmLinkNow(parentScreen, uri, trusted);
/*     */   }
/*     */   
/*     */   public static Button.OnPress confirmLink(Screen parentScreen, String uri) {
/* 119 */     return confirmLink(parentScreen, uri, true);
/*     */   }
/*     */   
/*     */   public static Button.OnPress confirmLink(Screen parentScreen, URI uri) {
/* 123 */     return confirmLink(parentScreen, uri, true);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/ConfirmLinkScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */