/*    */ package com.mojang.blaze3d.textures;
/*    */ 
/*    */ public enum TextureFormat {
/*  4 */   RGBA8(4),
/*  5 */   RED8(1),
/*  6 */   RED8I(1),
/*  7 */   DEPTH32(4);
/*    */   
/*    */   private final int pixelSize;
/*    */ 
/*    */   
/*    */   TextureFormat(int pixelSize) {
/* 13 */     this.pixelSize = pixelSize;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public int pixelSize() {
/* 22 */     return this.pixelSize;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasColorAspect() {
/* 31 */     return (this == RGBA8 || this == RED8);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasDepthAspect() {
/* 40 */     return (this == DEPTH32);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/textures/TextureFormat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */