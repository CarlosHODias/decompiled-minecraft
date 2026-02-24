/*    */ package com.mojang.blaze3d.vertex;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Tesselator
/*    */ {
/*    */   private static final int MAX_BYTES = 786432;
/*    */   private final ByteBufferBuilder buffer;
/*    */   private static Tesselator instance;
/*    */   
/*    */   public static void init() {
/* 14 */     if (instance != null) {
/* 15 */       throw new IllegalStateException("Tesselator has already been initialized");
/*    */     }
/* 17 */     instance = new Tesselator();
/*    */   }
/*    */   
/*    */   public static Tesselator getInstance() {
/* 21 */     if (instance == null) {
/* 22 */       throw new IllegalStateException("Tesselator has not been initialized");
/*    */     }
/* 24 */     return instance;
/*    */   }
/*    */   
/*    */   public Tesselator(int size) {
/* 28 */     this.buffer = new ByteBufferBuilder(size);
/*    */   }
/*    */   
/*    */   public Tesselator() {
/* 32 */     this(786432);
/*    */   }
/*    */   
/*    */   public BufferBuilder begin(VertexFormat.Mode mode, VertexFormat format) {
/* 36 */     return new BufferBuilder(this.buffer, mode, format);
/*    */   }
/*    */   
/*    */   public void clear() {
/* 40 */     this.buffer.clear();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/vertex/Tesselator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */