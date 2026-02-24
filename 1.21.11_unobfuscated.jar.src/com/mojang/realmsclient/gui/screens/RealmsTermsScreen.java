/*    */ package com.mojang.realmsclient.gui.screens;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.client.RealmsClient;
/*    */ import com.mojang.realmsclient.dto.RealmsServer;
/*    */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*    */ import com.mojang.realmsclient.util.task.GetServerDetailsTask;
/*    */ import com.mojang.realmsclient.util.task.LongRunningTask;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.input.KeyEvent;
/*    */ import net.minecraft.client.input.MouseButtonEvent;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.realms.RealmsScreen;
/*    */ import net.minecraft.util.CommonLinks;
/*    */ import net.minecraft.util.Util;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class RealmsTermsScreen extends RealmsScreen {
/* 24 */   private static final Logger LOGGER = LogUtils.getLogger();
/* 25 */   private static final Component TITLE = (Component)Component.translatable("mco.terms.title");
/* 26 */   private static final Component TERMS_STATIC_TEXT = (Component)Component.translatable("mco.terms.sentence.1");
/* 27 */   private static final Component TERMS_LINK_TEXT = (Component)CommonComponents.space().append((Component)Component.translatable("mco.terms.sentence.2").withStyle(Style.EMPTY.withUnderlined(true)));
/*    */   
/*    */   private final Screen lastScreen;
/*    */   
/*    */   private final RealmsServer realmsServer;
/*    */   private boolean onLink;
/*    */   
/*    */   public RealmsTermsScreen(Screen lastScreen, RealmsServer realmsServer) {
/* 35 */     super(TITLE);
/* 36 */     this.lastScreen = lastScreen;
/* 37 */     this.realmsServer = realmsServer;
/*    */   }
/*    */ 
/*    */   
/*    */   public void init() {
/* 42 */     int columnWidth = this.width / 4 - 2;
/*    */     
/* 44 */     addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable("mco.terms.buttons.agree"), button -> agreedToTos()).bounds(this.width / 4, row(12), columnWidth, 20).build());
/* 45 */     addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable("mco.terms.buttons.disagree"), button -> this.minecraft.setScreen(this.lastScreen)).bounds(this.width / 2 + 4, row(12), columnWidth, 20).build());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean keyPressed(KeyEvent event) {
/* 50 */     if (event.key() == 256) {
/* 51 */       this.minecraft.setScreen(this.lastScreen);
/* 52 */       return true;
/*    */     } 
/* 54 */     return super.keyPressed(event);
/*    */   }
/*    */   
/*    */   private void agreedToTos() {
/* 58 */     RealmsClient client = RealmsClient.getOrCreate();
/*    */     try {
/* 60 */       client.agreeToTos();
/* 61 */       this.minecraft.setScreen((Screen)new RealmsLongRunningMcoTaskScreen(this.lastScreen, new LongRunningTask[] { (LongRunningTask)new GetServerDetailsTask(this.lastScreen, this.realmsServer) }));
/* 62 */     } catch (RealmsServiceException e) {
/* 63 */       LOGGER.error("Couldn't agree to TOS", (Throwable)e);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 69 */     if (this.onLink) {
/* 70 */       this.minecraft.keyboardHandler.setClipboard(CommonLinks.REALMS_TERMS.toString());
/* 71 */       Util.getPlatform().openUri(CommonLinks.REALMS_TERMS);
/* 72 */       return true;
/*    */     } 
/*    */     
/* 75 */     return super.mouseClicked(event, doubleClick);
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getNarrationMessage() {
/* 80 */     return (Component)CommonComponents.joinForNarration(new Component[] { super.getNarrationMessage(), TERMS_STATIC_TEXT }).append(CommonComponents.SPACE).append(TERMS_LINK_TEXT);
/*    */   }
/*    */ 
/*    */   
/*    */   public void render(GuiGraphics graphics, int xm, int ym, float a) {
/* 85 */     super.render(graphics, xm, ym, a);
/*    */     
/* 87 */     graphics.drawCenteredString(this.font, this.title, this.width / 2, 17, -1);
/* 88 */     graphics.drawString(this.font, TERMS_STATIC_TEXT, this.width / 2 - 120, row(5), -1);
/* 89 */     int firstPartWidth = this.font.width((FormattedText)TERMS_STATIC_TEXT);
/*    */     
/* 91 */     int x1 = this.width / 2 - 121 + firstPartWidth;
/* 92 */     int y1 = row(5);
/* 93 */     int x2 = x1 + this.font.width((FormattedText)TERMS_LINK_TEXT) + 1;
/* 94 */     java.util.Objects.requireNonNull(this.font); int y2 = y1 + 1 + 9;
/*    */     
/* 96 */     this.onLink = (x1 <= xm && xm <= x2 && y1 <= ym && ym <= y2);
/* 97 */     graphics.drawString(this.font, TERMS_LINK_TEXT, this.width / 2 - 120 + firstPartWidth, row(5), this.onLink ? -9670204 : -13408581);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/RealmsTermsScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */