/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*    */ 
/*    */ public class SpriteCoordinateExpander implements VertexConsumer {
/*    */   private final VertexConsumer delegate;
/*    */   private final TextureAtlasSprite sprite;
/*    */   
/*    */   public SpriteCoordinateExpander(VertexConsumer delegate, TextureAtlasSprite sprite) {
/* 11 */     this.delegate = delegate;
/* 12 */     this.sprite = sprite;
/*    */   }
/*    */ 
/*    */   
/*    */   public VertexConsumer addVertex(float x, float y, float z) {
/* 17 */     return this.delegate.addVertex(x, y, z);
/*    */   }
/*    */ 
/*    */   
/*    */   public VertexConsumer setColor(int r, int g, int b, int a) {
/* 22 */     return this.delegate.setColor(r, g, b, a);
/*    */   }
/*    */ 
/*    */   
/*    */   public VertexConsumer setColor(int color) {
/* 27 */     return this.delegate.setColor(color);
/*    */   }
/*    */ 
/*    */   
/*    */   public VertexConsumer setUv(float u, float v) {
/* 32 */     return this.delegate.setUv(this.sprite.getU(u), this.sprite.getV(v));
/*    */   }
/*    */ 
/*    */   
/*    */   public VertexConsumer setUv1(int u, int v) {
/* 37 */     return this.delegate.setUv1(u, v);
/*    */   }
/*    */ 
/*    */   
/*    */   public VertexConsumer setUv2(int u, int v) {
/* 42 */     return this.delegate.setUv2(u, v);
/*    */   }
/*    */ 
/*    */   
/*    */   public VertexConsumer setNormal(float x, float y, float z) {
/* 47 */     return this.delegate.setNormal(x, y, z);
/*    */   }
/*    */ 
/*    */   
/*    */   public VertexConsumer setLineWidth(float width) {
/* 52 */     this.delegate.setLineWidth(width);
/* 53 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void addVertex(float x, float y, float z, int color, float u, float v, int overlayCoords, int lightCoords, float nx, float ny, float nz) {
/* 58 */     this.delegate.addVertex(x, y, z, color, this.sprite.getU(u), this.sprite.getV(v), overlayCoords, lightCoords, nx, ny, nz);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/SpriteCoordinateExpander.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */