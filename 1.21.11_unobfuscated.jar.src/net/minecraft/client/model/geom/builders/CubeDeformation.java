/*    */ package net.minecraft.client.model.geom.builders;
/*    */ 
/*    */ public class CubeDeformation {
/*  4 */   public static final CubeDeformation NONE = new CubeDeformation(0.0F);
/*    */   
/*    */   final float growX;
/*    */   final float growY;
/*    */   final float growZ;
/*    */   
/*    */   public CubeDeformation(float growX, float growY, float growZ) {
/* 11 */     this.growX = growX;
/* 12 */     this.growY = growY;
/* 13 */     this.growZ = growZ;
/*    */   }
/*    */   
/*    */   public CubeDeformation(float grow) {
/* 17 */     this(grow, grow, grow);
/*    */   }
/*    */   
/*    */   public CubeDeformation extend(float factor) {
/* 21 */     return new CubeDeformation(this.growX + factor, this.growY + factor, this.growZ + factor);
/*    */   }
/*    */   
/*    */   public CubeDeformation extend(float factorX, float factorY, float factorZ) {
/* 25 */     return new CubeDeformation(this.growX + factorX, this.growY + factorY, this.growZ + factorZ);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/geom/builders/CubeDeformation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */