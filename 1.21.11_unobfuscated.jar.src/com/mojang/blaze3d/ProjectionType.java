/*    */ package com.mojang.blaze3d;
/*    */ import com.mojang.blaze3d.vertex.VertexSorting;
/*    */ import org.joml.Matrix4f;
/*    */ 
/*    */ public enum ProjectionType {
/*    */   static {
/*  7 */     PERSPECTIVE = new ProjectionType("PERSPECTIVE", 0, VertexSorting.DISTANCE_TO_ORIGIN, (matrix, bias) -> matrix.scale(1.0F - bias / 4096.0F));
/*  8 */     ORTHOGRAPHIC = new ProjectionType("ORTHOGRAPHIC", 1, VertexSorting.ORTHOGRAPHIC_Z, (matrix, bias) -> matrix.translate(0.0F, 0.0F, bias / 512.0F));
/*    */   }
/*    */   PERSPECTIVE, ORTHOGRAPHIC;
/*    */   private final VertexSorting vertexSorting;
/*    */   private final LayeringTransform layeringTransform;
/*    */   
/*    */   ProjectionType(VertexSorting vertexSorting, LayeringTransform layeringTransform) {
/* 15 */     this.vertexSorting = vertexSorting;
/* 16 */     this.layeringTransform = layeringTransform;
/*    */   }
/*    */   
/*    */   public VertexSorting vertexSorting() {
/* 20 */     return this.vertexSorting;
/*    */   }
/*    */   
/*    */   public void applyLayeringTransform(Matrix4f matrix, float bias) {
/* 24 */     this.layeringTransform.apply(matrix, bias);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   private static interface LayeringTransform {
/*    */     void apply(Matrix4f param1Matrix4f, float param1Float);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/ProjectionType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */