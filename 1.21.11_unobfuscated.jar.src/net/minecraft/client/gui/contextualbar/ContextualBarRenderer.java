/*    */ package net.minecraft.client.gui.contextualbar;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.Window;
/*    */ import java.util.Objects;
/*    */ import net.minecraft.client.DeltaTracker;
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ 
/*    */ public interface ContextualBarRenderer {
/*    */   public static final int WIDTH = 182;
/*    */   
/* 15 */   public static final ContextualBarRenderer EMPTY = new ContextualBarRenderer() {
/*    */       public void renderBackground(GuiGraphics graphics, DeltaTracker deltaTracker) {}
/*    */       
/*    */       public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {}
/*    */     };
/*    */   
/*    */   public static final int HEIGHT = 5;
/*    */   
/*    */   default int left(Window window) {
/* 24 */     return (window.getGuiScaledWidth() - 182) / 2;
/*    */   }
/*    */   public static final int MARGIN_BOTTOM = 24;
/*    */   default int top(Window window) {
/* 28 */     return window.getGuiScaledHeight() - 24 - 5;
/*    */   }
/*    */   void renderBackground(GuiGraphics paramGuiGraphics, DeltaTracker paramDeltaTracker);
/*    */   
/*    */   void render(GuiGraphics paramGuiGraphics, DeltaTracker paramDeltaTracker);
/*    */   
/*    */   static void renderExperienceLevel(GuiGraphics graphics, Font font, int experienceLevel) {
/* 35 */     MutableComponent mutableComponent = Component.translatable("gui.experience.level", new Object[] { experienceLevel });
/* 36 */     int x = (graphics.guiWidth() - font.width((FormattedText)mutableComponent)) / 2;
/* 37 */     Objects.requireNonNull(font); int y = graphics.guiHeight() - 24 - 9 - 2;
/*    */     
/* 39 */     graphics.drawString(font, (Component)mutableComponent, x + 1, y, -16777216, false);
/* 40 */     graphics.drawString(font, (Component)mutableComponent, x - 1, y, -16777216, false);
/* 41 */     graphics.drawString(font, (Component)mutableComponent, x, y + 1, -16777216, false);
/* 42 */     graphics.drawString(font, (Component)mutableComponent, x, y - 1, -16777216, false);
/* 43 */     graphics.drawString(font, (Component)mutableComponent, x, y, -8323296, false);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/contextualbar/ContextualBarRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */