/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import org.joml.Matrix4f;
/*    */ import org.joml.Vector3f;
/*    */ 
/*    */ public final class BlockElementRotation extends Record {
/*    */   private final org.joml.Vector3fc origin;
/*    */   private final RotationValue value;
/*    */   private final boolean rescale;
/*    */   private final org.joml.Matrix4fc transform;
/*    */   
/* 12 */   public BlockElementRotation(org.joml.Vector3fc origin, RotationValue value, boolean rescale, org.joml.Matrix4fc transform) { this.origin = origin; this.value = value; this.rescale = rescale; this.transform = transform; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/BlockElementRotation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockElementRotation; } public org.joml.Vector3fc origin() { return this.origin; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/BlockElementRotation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockElementRotation; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/BlockElementRotation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/BlockElementRotation;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public RotationValue value() { return this.value; } public boolean rescale() { return this.rescale; } public org.joml.Matrix4fc transform() { return this.transform; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockElementRotation(org.joml.Vector3fc origin, RotationValue value, boolean rescale) {
/* 19 */     this(origin, value, rescale, (org.joml.Matrix4fc)computeTransform(value, rescale));
/*    */   }
/*    */   
/*    */   private static Matrix4f computeTransform(RotationValue value, boolean rescale) {
/* 23 */     Matrix4f result = value.transformation();
/* 24 */     if (rescale && !com.mojang.math.MatrixUtil.isIdentity((org.joml.Matrix4fc)result)) {
/* 25 */       org.joml.Vector3fc scale = computeRescale((org.joml.Matrix4fc)result);
/* 26 */       result.scale(scale);
/*    */     } 
/* 28 */     return result;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static org.joml.Vector3fc computeRescale(org.joml.Matrix4fc rotation) {
/* 44 */     Vector3f scratch = new Vector3f();
/* 45 */     float scaleX = scaleFactorForAxis(rotation, net.minecraft.core.Direction.Axis.X, scratch);
/* 46 */     float scaleY = scaleFactorForAxis(rotation, net.minecraft.core.Direction.Axis.Y, scratch);
/* 47 */     float scaleZ = scaleFactorForAxis(rotation, net.minecraft.core.Direction.Axis.Z, scratch);
/* 48 */     return (org.joml.Vector3fc)scratch.set(scaleX, scaleY, scaleZ);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static float scaleFactorForAxis(org.joml.Matrix4fc rotation, net.minecraft.core.Direction.Axis axis, Vector3f scratch) {
/* 58 */     Vector3f axisUnit = scratch.set(axis.getPositive().getUnitVec3f());
/* 59 */     Vector3f transformedAxisUnit = rotation.transformDirection(axisUnit);
/* 60 */     float absX = org.joml.Math.abs(transformedAxisUnit.x);
/* 61 */     float absY = org.joml.Math.abs(transformedAxisUnit.y);
/* 62 */     float absZ = org.joml.Math.abs(transformedAxisUnit.z);
/*    */     
/* 64 */     float maxComponent = org.joml.Math.max(org.joml.Math.max(absX, absY), absZ);
/* 65 */     return 1.0F / maxComponent;
/*    */   }
/*    */   
/*    */   public static final class SingleAxisRotation extends Record implements RotationValue {
/*    */     private final net.minecraft.core.Direction.Axis axis;
/*    */     private final float angle;
/*    */     
/* 72 */     public SingleAxisRotation(net.minecraft.core.Direction.Axis axis, float angle) { this.axis = axis; this.angle = angle; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/BlockElementRotation$SingleAxisRotation;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #72	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockElementRotation$SingleAxisRotation; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/BlockElementRotation$SingleAxisRotation;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #72	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockElementRotation$SingleAxisRotation; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/BlockElementRotation$SingleAxisRotation;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #72	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/BlockElementRotation$SingleAxisRotation;
/* 72 */       //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.core.Direction.Axis axis() { return this.axis; } public float angle() { return this.angle; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public Matrix4f transformation() {
/* 78 */       Matrix4f result = new Matrix4f();
/* 79 */       if (this.angle == 0.0F) {
/* 80 */         return result;
/*    */       }
/* 82 */       org.joml.Vector3fc rotateAround = this.axis.getPositive().getUnitVec3f();
/* 83 */       result.rotation(this.angle * 0.017453292F, rotateAround);
/* 84 */       return result;
/*    */     } }
/*    */   public static final class EulerXYZRotation extends Record implements RotationValue { private final float x; private final float y; private final float z;
/*    */     
/* 88 */     public EulerXYZRotation(float x, float y, float z) { this.x = x; this.y = y; this.z = z; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/BlockElementRotation$EulerXYZRotation;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #88	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockElementRotation$EulerXYZRotation; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/BlockElementRotation$EulerXYZRotation;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #88	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/BlockElementRotation$EulerXYZRotation; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/BlockElementRotation$EulerXYZRotation;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #88	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/BlockElementRotation$EulerXYZRotation;
/* 88 */       //   0	8	1	o	Ljava/lang/Object; } public float x() { return this.x; } public float y() { return this.y; } public float z() { return this.z; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public Matrix4f transformation() {
/* 95 */       return new Matrix4f().rotationZYX(this.z * 0.017453292F, this.y * 0.017453292F, this.x * 0.017453292F);
/*    */     } }
/*    */ 
/*    */   
/*    */   public static interface RotationValue {
/*    */     Matrix4f transformation();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/BlockElementRotation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */