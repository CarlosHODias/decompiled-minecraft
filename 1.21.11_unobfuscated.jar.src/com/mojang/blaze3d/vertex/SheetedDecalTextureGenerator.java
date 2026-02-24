/*    */ package com.mojang.blaze3d.vertex;
/*    */ 
/*    */ import net.minecraft.core.Direction;
/*    */ import org.joml.Matrix3f;
/*    */ import org.joml.Matrix3fc;
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Matrix4fc;
/*    */ import org.joml.Quaternionfc;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ 
/*    */ public class SheetedDecalTextureGenerator
/*    */   implements VertexConsumer
/*    */ {
/*    */   private final VertexConsumer delegate;
/*    */   private final Matrix4f cameraInversePose;
/*    */   private final Matrix3f normalInversePose;
/*    */   private final float textureScale;
/* 19 */   private final Vector3f worldPos = new Vector3f();
/* 20 */   private final Vector3f normal = new Vector3f();
/*    */   
/*    */   private float x;
/*    */   private float y;
/*    */   private float z;
/*    */   
/*    */   public SheetedDecalTextureGenerator(VertexConsumer delegate, PoseStack.Pose cameraPose, float textureScale) {
/* 27 */     this.delegate = delegate;
/* 28 */     this.cameraInversePose = new Matrix4f((Matrix4fc)cameraPose.pose()).invert();
/* 29 */     this.normalInversePose = new Matrix3f((Matrix3fc)cameraPose.normal()).invert();
/* 30 */     this.textureScale = textureScale;
/*    */   }
/*    */ 
/*    */   
/*    */   public VertexConsumer addVertex(float x, float y, float z) {
/* 35 */     this.x = x;
/* 36 */     this.y = y;
/* 37 */     this.z = z;
/* 38 */     this.delegate.addVertex(x, y, z);
/* 39 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public VertexConsumer setColor(int r, int g, int b, int a) {
/* 45 */     this.delegate.setColor(-1);
/* 46 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public VertexConsumer setColor(int color) {
/* 52 */     this.delegate.setColor(-1);
/* 53 */     return this;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public VertexConsumer setUv(float u, float v) {
/* 59 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public VertexConsumer setUv1(int u, int v) {
/* 64 */     this.delegate.setUv1(u, v);
/* 65 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public VertexConsumer setUv2(int u, int v) {
/* 70 */     this.delegate.setUv2(u, v);
/* 71 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public VertexConsumer setNormal(float x, float y, float z) {
/* 76 */     this.delegate.setNormal(x, y, z);
/*    */     
/* 78 */     Vector3f normal = this.normalInversePose.transform(x, y, z, this.normal);
/* 79 */     Direction direction = Direction.getApproximateNearest(normal.x(), normal.y(), normal.z());
/*    */     
/* 81 */     Vector3f worldPos = this.cameraInversePose.transformPosition(this.x, this.y, this.z, this.worldPos);
/*    */     
/* 83 */     worldPos.rotateY(3.1415927F);
/* 84 */     worldPos.rotateX(-1.5707964F);
/* 85 */     worldPos.rotate((Quaternionfc)direction.getRotation());
/*    */     
/* 87 */     this.delegate.setUv(-worldPos.x() * this.textureScale, -worldPos.y() * this.textureScale);
/*    */     
/* 89 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public VertexConsumer setLineWidth(float width) {
/* 94 */     this.delegate.setLineWidth(width);
/* 95 */     return this;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/vertex/SheetedDecalTextureGenerator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */