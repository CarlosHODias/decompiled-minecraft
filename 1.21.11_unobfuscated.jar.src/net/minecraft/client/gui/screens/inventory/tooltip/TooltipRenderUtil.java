/*    */ package net.minecraft.client.gui.screens.inventory.tooltip;
/*    */ 
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.renderer.RenderPipelines;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class TooltipRenderUtil
/*    */ {
/*  9 */   private static final Identifier BACKGROUND_SPRITE = Identifier.withDefaultNamespace("tooltip/background");
/* 10 */   private static final Identifier FRAME_SPRITE = Identifier.withDefaultNamespace("tooltip/frame");
/*    */   
/*    */   public static final int MOUSE_OFFSET = 12;
/*    */   
/*    */   private static final int PADDING = 3;
/*    */   
/*    */   public static final int PADDING_LEFT = 3;
/*    */   
/*    */   public static final int PADDING_RIGHT = 3;
/*    */   public static final int PADDING_TOP = 3;
/*    */   public static final int PADDING_BOTTOM = 3;
/*    */   private static final int MARGIN = 9;
/*    */   
/*    */   public static void renderTooltipBackground(GuiGraphics graphics, int x, int y, int w, int h, Identifier style) {
/* 24 */     int x0 = x - 3 - 9;
/* 25 */     int y0 = y - 3 - 9;
/* 26 */     int paddedWidth = w + 3 + 3 + 18;
/* 27 */     int paddedHeight = h + 3 + 3 + 18;
/* 28 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, getBackgroundSprite(style), x0, y0, paddedWidth, paddedHeight);
/* 29 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, getFrameSprite(style), x0, y0, paddedWidth, paddedHeight);
/*    */   }
/*    */   
/*    */   private static Identifier getBackgroundSprite(Identifier style) {
/* 33 */     if (style == null) {
/* 34 */       return BACKGROUND_SPRITE;
/*    */     }
/* 36 */     return style.withPath(path -> "tooltip/" + path + "_background");
/*    */   }
/*    */   
/*    */   private static Identifier getFrameSprite(Identifier style) {
/* 40 */     if (style == null) {
/* 41 */       return FRAME_SPRITE;
/*    */     }
/* 43 */     return style.withPath(path -> "tooltip/" + path + "_frame");
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/tooltip/TooltipRenderUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */