/*    */ package net.minecraft.client.animation;
/*    */ 
/*    */ 
/*    */ public final class AnimationChannel extends Record {
/*    */   private final Target target;
/*    */   private final Keyframe[] keyframes;
/*    */   
/*  8 */   public AnimationChannel(Target target, Keyframe... keyframes) { this.target = target; this.keyframes = keyframes; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/animation/AnimationChannel;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/client/animation/AnimationChannel; } public Target target() { return this.target; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/animation/AnimationChannel;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/animation/AnimationChannel; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/animation/AnimationChannel;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/animation/AnimationChannel;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public Keyframe[] keyframes() { return this.keyframes; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static interface Interpolation
/*    */   {
/*    */     org.joml.Vector3f apply(org.joml.Vector3f param1Vector3f, float param1Float1, Keyframe[] param1ArrayOfKeyframe, int param1Int1, int param1Int2, float param1Float2);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static class Targets
/*    */   {
/* 22 */     public static final AnimationChannel.Target POSITION = net.minecraft.client.model.geom.ModelPart::offsetPos;
/* 23 */     public static final AnimationChannel.Target ROTATION = net.minecraft.client.model.geom.ModelPart::offsetRotation;
/* 24 */     public static final AnimationChannel.Target SCALE = net.minecraft.client.model.geom.ModelPart::offsetScale; }
/*    */   public static class Interpolations { public static final AnimationChannel.Interpolation LINEAR; public static final AnimationChannel.Interpolation CATMULLROM;
/*    */     
/*    */     static {
/* 28 */       LINEAR = ((vector, alpha, keyframes, prev, next, targetScale) -> {
/*    */           org.joml.Vector3fc point0 = keyframes[prev].postTarget(), point1 = keyframes[next].preTarget();
/*    */ 
/*    */           
/*    */           return point0.lerp(point1, alpha, vector).mul(targetScale);
/*    */         });
/*    */       
/* 35 */       CATMULLROM = ((vector, alpha, keyframes, prev, next, targetScale) -> {
/*    */           org.joml.Vector3fc point0 = keyframes[Math.max(0, prev - 1)].postTarget(), point1 = keyframes[prev].postTarget(), point2 = keyframes[next].postTarget(), point3 = keyframes[Math.min(keyframes.length - 1, next + 1)].postTarget();
/*    */           vector.set(net.minecraft.util.Mth.catmullrom(alpha, point0.x(), point1.x(), point2.x(), point3.x()) * targetScale, net.minecraft.util.Mth.catmullrom(alpha, point0.y(), point1.y(), point2.y(), point3.y()) * targetScale, net.minecraft.util.Mth.catmullrom(alpha, point0.z(), point1.z(), point2.z(), point3.z()) * targetScale);
/*    */           return vector;
/*    */         });
/*    */     } }
/*    */ 
/*    */   
/*    */   public static interface Target {
/*    */     void apply(net.minecraft.client.model.geom.ModelPart param1ModelPart, org.joml.Vector3f param1Vector3f);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/animation/AnimationChannel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */