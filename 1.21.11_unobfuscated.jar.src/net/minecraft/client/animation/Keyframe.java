/*    */ package net.minecraft.client.animation;public final class Keyframe extends Record { private final float timestamp; private final org.joml.Vector3fc preTarget;
/*    */   private final org.joml.Vector3fc postTarget;
/*    */   private final AnimationChannel.Interpolation interpolation;
/*    */   
/*  5 */   public AnimationChannel.Interpolation interpolation() { return this.interpolation; } public org.joml.Vector3fc postTarget() { return this.postTarget; } public org.joml.Vector3fc preTarget() { return this.preTarget; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/animation/Keyframe;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/animation/Keyframe;
/*  5 */     //   0	8	1	o	Ljava/lang/Object; } public float timestamp() { return this.timestamp; } public Keyframe(float timestamp, org.joml.Vector3fc preTarget, org.joml.Vector3fc postTarget, AnimationChannel.Interpolation interpolation) { this.timestamp = timestamp; this.preTarget = preTarget; this.postTarget = postTarget; this.interpolation = interpolation; }
/*    */    public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/animation/Keyframe;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/animation/Keyframe;
/*    */   } public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/animation/Keyframe;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/animation/Keyframe;
/*    */   }
/*    */   public Keyframe(float timestamp, org.joml.Vector3fc postTarget, AnimationChannel.Interpolation interpolation) {
/* 12 */     this(timestamp, postTarget, postTarget, interpolation);
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/animation/Keyframe.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */