/*    */ package net.minecraft.client.gui.components;
/*    */ import net.minecraft.client.gui.ActiveTextCollector;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.resources.SplashManager;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.Util;
/*    */ import org.joml.Matrix3x2f;
/*    */ import org.joml.Matrix3x2fc;
/*    */ 
/*    */ public class SplashRenderer {
/* 14 */   public static final SplashRenderer CHRISTMAS = new SplashRenderer(SplashManager.CHRISTMAS);
/* 15 */   public static final SplashRenderer NEW_YEAR = new SplashRenderer(SplashManager.NEW_YEAR);
/* 16 */   public static final SplashRenderer HALLOWEEN = new SplashRenderer(SplashManager.HALLOWEEN);
/*    */   
/*    */   private static final int WIDTH_OFFSET = 123;
/*    */   
/*    */   private static final int HEIGH_OFFSET = 69;
/*    */   private static final float TEXT_ANGLE = -0.34906584F;
/*    */   private final Component splash;
/*    */   
/*    */   public SplashRenderer(Component splash) {
/* 25 */     this.splash = splash;
/*    */   }
/*    */   
/*    */   public void render(GuiGraphics graphics, int screenWidth, Font font, float alpha) {
/* 29 */     int textWidth = font.width((FormattedText)this.splash);
/*    */     
/* 31 */     ActiveTextCollector textRenderer = graphics.textRenderer();
/*    */     
/* 33 */     float textPhase = 1.8F - Mth.abs(Mth.sin(((float)(Util.getMillis() % 1000L) / 1000.0F * 6.2831855F)) * 0.1F);
/* 34 */     float textScale = textPhase * 100.0F / (textWidth + 32);
/*    */     
/* 36 */     Matrix3x2f transform = new Matrix3x2f(textRenderer.defaultParameters().pose())
/* 37 */       .translate(screenWidth / 2.0F + 123.0F, 69.0F)
/* 38 */       .rotate(-0.34906584F)
/* 39 */       .scale(textScale);
/*    */     
/* 41 */     ActiveTextCollector.Parameters renderParameters = textRenderer.defaultParameters()
/* 42 */       .withOpacity(alpha)
/* 43 */       .withPose((Matrix3x2fc)transform);
/*    */     
/* 45 */     textRenderer.accept(net.minecraft.client.gui.TextAlignment.LEFT, -textWidth / 2, -8, renderParameters, this.splash);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/SplashRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */