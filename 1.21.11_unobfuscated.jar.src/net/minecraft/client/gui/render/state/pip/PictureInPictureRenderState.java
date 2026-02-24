/*    */ package net.minecraft.client.gui.render.state.pip;
/*    */ 
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.gui.render.state.ScreenArea;
/*    */ import org.joml.Matrix3x2f;
/*    */ 
/*    */ public interface PictureInPictureRenderState
/*    */   extends ScreenArea {
/*  9 */   public static final Matrix3x2f IDENTITY_POSE = new Matrix3x2f();
/*    */   
/*    */   int x0();
/*    */   
/*    */   int x1();
/*    */   
/*    */   int y0();
/*    */   
/*    */   int y1();
/*    */   
/*    */   float scale();
/*    */   
/*    */   default Matrix3x2f pose() {
/* 22 */     return IDENTITY_POSE;
/*    */   }
/*    */   
/*    */   ScreenRectangle scissorArea();
/*    */   
/*    */   static ScreenRectangle getBounds(int x0, int y0, int x1, int y1, ScreenRectangle scissorArea) {
/* 28 */     ScreenRectangle bounds = new ScreenRectangle(x0, y0, x1 - x0, y1 - y0);
/* 29 */     return (scissorArea != null) ? scissorArea.intersection(bounds) : bounds;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/state/pip/PictureInPictureRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */