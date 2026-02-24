/*    */ package net.minecraft.client.gui.components;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.player.PlayerSkin;
/*    */ 
/*    */ public class PlayerFaceRenderer
/*    */ {
/*    */   public static final int SKIN_HEAD_U = 8;
/*    */   public static final int SKIN_HEAD_V = 8;
/*    */   public static final int SKIN_HEAD_WIDTH = 8;
/*    */   public static final int SKIN_HEAD_HEIGHT = 8;
/*    */   public static final int SKIN_HAT_U = 40;
/*    */   public static final int SKIN_HAT_V = 8;
/*    */   public static final int SKIN_HAT_WIDTH = 8;
/*    */   public static final int SKIN_HAT_HEIGHT = 8;
/*    */   public static final int SKIN_TEX_WIDTH = 64;
/*    */   public static final int SKIN_TEX_HEIGHT = 64;
/*    */   
/*    */   public static void draw(GuiGraphics graphics, PlayerSkin skin, int x, int y, int size) {
/* 22 */     draw(graphics, skin, x, y, size, -1);
/*    */   }
/*    */   
/*    */   public static void draw(GuiGraphics graphics, PlayerSkin skin, int x, int y, int size, int color) {
/* 26 */     draw(graphics, skin.body().texturePath(), x, y, size, true, false, color);
/*    */   }
/*    */   
/*    */   public static void draw(GuiGraphics graphics, Identifier texture, int x, int y, int size, boolean hat, boolean flip, int color) {
/* 30 */     int skinHeadV = 8 + (flip ? 8 : 0);
/* 31 */     int skinHeadHeight = 8 * (flip ? -1 : 1);
/* 32 */     graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 8.0F, skinHeadV, size, size, 8, skinHeadHeight, 64, 64, color);
/*    */     
/* 34 */     if (hat) {
/* 35 */       drawHat(graphics, texture, x, y, size, flip, color);
/*    */     }
/*    */   }
/*    */   
/*    */   private static void drawHat(GuiGraphics graphics, Identifier texture, int x, int y, int size, boolean flip, int color) {
/* 40 */     int skinHatV = 8 + (flip ? 8 : 0);
/* 41 */     int skinHatHeight = 8 * (flip ? -1 : 1);
/*    */     
/* 43 */     graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x, y, 40.0F, skinHatV, size, size, 8, skinHatHeight, 64, 64, color);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/PlayerFaceRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */