/*    */ package net.minecraft.client.gui.font;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import org.joml.Matrix4f;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface PlainTextRenderable
/*    */   extends TextRenderable.Styled
/*    */ {
/*    */   public static final float DEFAULT_WIDTH = 8.0F;
/*    */   public static final float DEFAULT_HEIGHT = 8.0F;
/*    */   public static final float DEFUAULT_ASCENT = 8.0F;
/*    */   
/*    */   default void render(Matrix4f pose, VertexConsumer buffer, int packedLightCoords, boolean flat) {
/* 20 */     float frontDepth = 0.0F;
/* 21 */     if (shadowColor() != 0) {
/* 22 */       renderSprite(pose, buffer, packedLightCoords, shadowOffset(), shadowOffset(), 0.0F, shadowColor());
/* 23 */       if (!flat) {
/* 24 */         frontDepth += 0.03F;
/*    */       }
/*    */     } 
/* 27 */     renderSprite(pose, buffer, packedLightCoords, 0.0F, 0.0F, frontDepth, color());
/*    */   }
/*    */   
/*    */   void renderSprite(Matrix4f paramMatrix4f, VertexConsumer paramVertexConsumer, int paramInt1, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt2);
/*    */   
/*    */   float x();
/*    */   
/*    */   float y();
/*    */   
/*    */   int color();
/*    */   
/*    */   int shadowColor();
/*    */   
/*    */   float shadowOffset();
/*    */   
/*    */   default float width() {
/* 43 */     return 8.0F;
/*    */   }
/*    */   
/*    */   default float height() {
/* 47 */     return 8.0F;
/*    */   }
/*    */   
/*    */   default float ascent() {
/* 51 */     return 8.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   default float left() {
/* 56 */     return x();
/*    */   }
/*    */ 
/*    */   
/*    */   default float right() {
/* 61 */     return left() + width();
/*    */   }
/*    */ 
/*    */   
/*    */   default float top() {
/* 66 */     return y() + 7.0F - ascent();
/*    */   }
/*    */ 
/*    */   
/*    */   default float bottom() {
/* 71 */     return activeTop() + height();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/PlainTextRenderable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */