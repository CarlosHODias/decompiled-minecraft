/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.gui.ComponentPath;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*    */ import net.minecraft.client.gui.navigation.FocusNavigationEvent;
/*    */ import net.minecraft.client.gui.screens.LoadingDotsText;
/*    */ import net.minecraft.client.sounds.SoundManager;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class LoadingDotsWidget extends AbstractWidget {
/*    */   private final Font font;
/*    */   
/*    */   public LoadingDotsWidget(Font font, Component message) {
/* 19 */     super(0, 0, font.width((FormattedText)message), 9 * 3, message);
/* 20 */     this.font = font;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 25 */     int centerX = getX() + getWidth() / 2;
/* 26 */     int centerY = getY() + getHeight() / 2;
/*    */     
/* 28 */     Component message = getMessage();
/* 29 */     Objects.requireNonNull(this.font); graphics.drawString(this.font, message, centerX - this.font.width((FormattedText)message) / 2, centerY - 9, -1);
/*    */     
/* 31 */     String dots = LoadingDotsText.get(Util.getMillis());
/* 32 */     Objects.requireNonNull(this.font); graphics.drawString(this.font, dots, centerX - this.font.width(dots) / 2, centerY + 9, -8355712);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void updateWidgetNarration(NarrationElementOutput output) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void playDownSound(SoundManager soundManager) {}
/*    */ 
/*    */   
/*    */   public boolean isActive() {
/* 45 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public ComponentPath nextFocusPath(FocusNavigationEvent navigationEvent) {
/* 50 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/LoadingDotsWidget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */