/*    */ package net.minecraft.client.animation;
/*    */ 
/*    */ import java.util.List;
/*    */ 
/*    */ public final class AnimationDefinition extends Record {
/*    */   private final float lengthInSeconds;
/*    */   private final boolean looping;
/*    */   private final java.util.Map<String, List<AnimationChannel>> boneAnimations;
/*    */   
/* 10 */   public AnimationDefinition(float lengthInSeconds, boolean looping, java.util.Map<String, List<AnimationChannel>> boneAnimations) { this.lengthInSeconds = lengthInSeconds; this.looping = looping; this.boneAnimations = boneAnimations; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/animation/AnimationDefinition;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/client/animation/AnimationDefinition; } public float lengthInSeconds() { return this.lengthInSeconds; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/animation/AnimationDefinition;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/animation/AnimationDefinition; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/animation/AnimationDefinition;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/animation/AnimationDefinition;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public boolean looping() { return this.looping; } public java.util.Map<String, List<AnimationChannel>> boneAnimations() { return this.boneAnimations; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public KeyframeAnimation bake(net.minecraft.client.model.geom.ModelPart root) {
/* 16 */     return KeyframeAnimation.bake(root, this);
/*    */   }
/*    */   
/*    */   public static class Builder {
/*    */     private final float length;
/* 21 */     private final java.util.Map<String, List<AnimationChannel>> animationByBone = com.google.common.collect.Maps.newHashMap();
/*    */     private boolean looping;
/*    */     
/*    */     public static Builder withLength(float lengthInSeconds) {
/* 25 */       return new Builder(lengthInSeconds);
/*    */     }
/*    */     
/*    */     private Builder(float length) {
/* 29 */       this.length = length;
/*    */     }
/*    */     
/*    */     public Builder looping() {
/* 33 */       this.looping = true;
/* 34 */       return this;
/*    */     }
/*    */     
/*    */     public Builder addAnimation(String boneName, AnimationChannel animation) {
/* 38 */       ((List<AnimationChannel>)this.animationByBone.computeIfAbsent(boneName, k -> new java.util.ArrayList())).add(animation);
/* 39 */       return this;
/*    */     }
/*    */     
/*    */     public AnimationDefinition build() {
/* 43 */       return new AnimationDefinition(this.length, this.looping, this.animationByBone);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/animation/AnimationDefinition.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */