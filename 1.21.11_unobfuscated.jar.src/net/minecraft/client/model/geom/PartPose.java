/*    */ package net.minecraft.client.model.geom;public final class PartPose extends Record { private final float x; private final float y; private final float z; private final float xRot; private final float yRot; private final float zRot; private final float xScale; private final float yScale; private final float zScale;
/*    */   
/*  3 */   public PartPose(float x, float y, float z, float xRot, float yRot, float zRot, float xScale, float yScale, float zScale) { this.x = x; this.y = y; this.z = z; this.xRot = xRot; this.yRot = yRot; this.zRot = zRot; this.xScale = xScale; this.yScale = yScale; this.zScale = zScale; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/model/geom/PartPose;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  3 */     //   0	7	0	this	Lnet/minecraft/client/model/geom/PartPose; } public float x() { return this.x; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/geom/PartPose;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/model/geom/PartPose; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/geom/PartPose;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/model/geom/PartPose;
/*  3 */     //   0	8	1	o	Ljava/lang/Object; } public float y() { return this.y; } public float z() { return this.z; } public float xRot() { return this.xRot; } public float yRot() { return this.yRot; } public float zRot() { return this.zRot; } public float xScale() { return this.xScale; } public float yScale() { return this.yScale; } public float zScale() { return this.zScale; }
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
/* 14 */   public static final PartPose ZERO = offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
/*    */   
/*    */   public static PartPose offset(float x, float y, float z) {
/* 17 */     return offsetAndRotation(x, y, z, 0.0F, 0.0F, 0.0F);
/*    */   }
/*    */   
/*    */   public static PartPose rotation(float x, float y, float z) {
/* 21 */     return offsetAndRotation(0.0F, 0.0F, 0.0F, x, y, z);
/*    */   }
/*    */   
/*    */   public static PartPose offsetAndRotation(float offsetX, float offsetY, float offsetZ, float rotationX, float rotationY, float rotationZ) {
/* 25 */     return new PartPose(offsetX, offsetY, offsetZ, rotationX, rotationY, rotationZ, 1.0F, 1.0F, 1.0F);
/*    */   }
/*    */   
/*    */   public PartPose translated(float x, float y, float z) {
/* 29 */     return new PartPose(this.x + x, this.y + y, this.z + z, this.xRot, this.yRot, this.zRot, this.xScale, this.yScale, this.zScale);
/*    */   }
/*    */   
/*    */   public PartPose withScale(float scale) {
/* 33 */     return new PartPose(this.x, this.y, this.z, this.xRot, this.yRot, this.zRot, scale, scale, scale);
/*    */   }
/*    */   
/*    */   public PartPose scaled(float factor) {
/* 37 */     if (factor == 1.0F) {
/* 38 */       return this;
/*    */     }
/* 40 */     return scaled(factor, factor, factor);
/*    */   }
/*    */   
/*    */   public PartPose scaled(float scaleX, float scaleY, float scaleZ) {
/* 44 */     return new PartPose(this.x * scaleX, this.y * scaleY, this.z * scaleZ, this.xRot, this.yRot, this.zRot, this.xScale * scaleX, this.yScale * scaleY, this.zScale * scaleZ);
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/geom/PartPose.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */