/*    */ package net.minecraft.client.gui.screens;
/*    */ import java.net.URI;
/*    */ import java.nio.file.Path;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.StringWidget;
/*    */ import net.minecraft.client.gui.layouts.FrameLayout;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.network.DisconnectionDetails;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class DisconnectedScreen extends Screen {
/* 14 */   private static final Component TO_SERVER_LIST = (Component)Component.translatable("gui.toMenu");
/* 15 */   private static final Component TO_TITLE = (Component)Component.translatable("gui.toTitle");
/* 16 */   private static final Component REPORT_TO_SERVER_TITLE = (Component)Component.translatable("gui.report_to_server");
/* 17 */   private static final Component OPEN_REPORT_DIR_TITLE = (Component)Component.translatable("gui.open_report_dir");
/*    */   
/*    */   private final Screen parent;
/*    */   
/*    */   private final DisconnectionDetails details;
/*    */   private final Component buttonText;
/* 23 */   private final LinearLayout layout = LinearLayout.vertical();
/*    */   
/*    */   public DisconnectedScreen(Screen parent, Component title, Component reason) {
/* 26 */     this(parent, title, new DisconnectionDetails(reason));
/*    */   }
/*    */   
/*    */   public DisconnectedScreen(Screen parent, Component title, Component reason, Component buttonText) {
/* 30 */     this(parent, title, new DisconnectionDetails(reason), buttonText);
/*    */   }
/*    */   
/*    */   public DisconnectedScreen(Screen parent, Component title, DisconnectionDetails details) {
/* 34 */     this(parent, title, details, TO_SERVER_LIST);
/*    */   }
/*    */   
/*    */   public DisconnectedScreen(Screen parent, Component title, DisconnectionDetails details, Component buttonText) {
/* 38 */     super(title);
/* 39 */     this.parent = parent;
/* 40 */     this.details = details;
/* 41 */     this.buttonText = buttonText;
/*    */   }
/*    */   
/*    */   protected void init() {
/*    */     Button backButton;
/* 46 */     this.layout.defaultCellSetting().alignHorizontallyCenter().padding(10);
/* 47 */     this.layout.addChild((LayoutElement)new StringWidget(this.title, this.font));
/* 48 */     this.layout.addChild((LayoutElement)new net.minecraft.client.gui.components.MultiLineTextWidget(this.details.reason(), this.font).setMaxWidth(this.width - 50).setCentered(true));
/*    */     
/* 50 */     this.layout.defaultCellSetting().padding(2);
/* 51 */     this.details.bugReportLink().ifPresent(bugReportLink -> this.layout.addChild((LayoutElement)Button.builder(REPORT_TO_SERVER_TITLE, ConfirmLinkScreen.confirmLink(this, bugReportLink, false)).width(200).build()));
/*    */ 
/*    */ 
/*    */     
/* 55 */     this.details.report().ifPresent(report -> this.layout.addChild((LayoutElement)Button.builder(OPEN_REPORT_DIR_TITLE, ()).width(200).build()));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 60 */     if (this.minecraft.allowsMultiplayer()) {
/* 61 */       backButton = Button.builder(this.buttonText, button -> this.minecraft.setScreen(this.parent)).width(200).build();
/*    */     } else {
/* 63 */       backButton = Button.builder(TO_TITLE, button -> this.minecraft.setScreen(new TitleScreen())).width(200).build();
/*    */     } 
/* 65 */     this.layout.addChild((LayoutElement)backButton);
/*    */     
/* 67 */     this.layout.arrangeElements();
/* 68 */     this.layout.visitWidgets(this::addRenderableWidget);
/* 69 */     repositionElements();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void repositionElements() {
/* 74 */     FrameLayout.centerInRectangle((LayoutElement)this.layout, getRectangle());
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getNarrationMessage() {
/* 79 */     return (Component)CommonComponents.joinForNarration(new Component[] { this.title, this.details.reason() });
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldCloseOnEsc() {
/* 84 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/DisconnectedScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */