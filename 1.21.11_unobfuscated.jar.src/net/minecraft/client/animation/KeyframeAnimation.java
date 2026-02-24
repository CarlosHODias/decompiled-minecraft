/*     */ package net.minecraft.client.animation;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.AnimationState;
/*     */ import org.joml.Vector3f;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class KeyframeAnimation
/*     */ {
/*     */   private final AnimationDefinition definition;
/*     */   private final List<Entry> entries;
/*     */   
/*     */   private KeyframeAnimation(AnimationDefinition definition, List<Entry> entries) {
/*  20 */     this.definition = definition;
/*  21 */     this.entries = entries;
/*     */   }
/*     */   
/*     */   static KeyframeAnimation bake(ModelPart root, AnimationDefinition definition) {
/*  25 */     List<Entry> entries = new ArrayList<>();
/*  26 */     Function<String, ModelPart> partLookup = root.createPartLookup();
/*     */     
/*  28 */     for (Map.Entry<String, List<AnimationChannel>> entry : definition.boneAnimations().entrySet()) {
/*  29 */       String partName = entry.getKey();
/*  30 */       List<AnimationChannel> channels = entry.getValue();
/*     */       
/*  32 */       ModelPart part = partLookup.apply(partName);
/*  33 */       if (part == null) {
/*  34 */         throw new IllegalArgumentException("Cannot animate " + partName + ", which does not exist in model");
/*     */       }
/*     */       
/*  37 */       for (AnimationChannel channel : channels) {
/*  38 */         entries.add(new Entry(part, channel.target(), channel.keyframes()));
/*     */       }
/*     */     } 
/*     */     
/*  42 */     return new KeyframeAnimation(definition, List.copyOf(entries));
/*     */   }
/*     */   
/*     */   public void applyStatic() {
/*  46 */     apply(0L, 1.0F);
/*     */   }
/*     */   
/*     */   public void applyWalk(float animationPos, float animationSpeed, float speedFactor, float scaleFactor) {
/*  50 */     long time = (long)(animationPos * 50.0F * speedFactor);
/*  51 */     float scale = Math.min(animationSpeed * scaleFactor, 1.0F);
/*  52 */     apply(time, scale);
/*     */   }
/*     */   
/*     */   public void apply(AnimationState animationState, float currentTime) {
/*  56 */     apply(animationState, currentTime, 1.0F);
/*     */   }
/*     */   
/*     */   public void apply(AnimationState animationState, float currentTime, float speedFactor) {
/*  60 */     animationState.ifStarted(state -> apply((long)((float)speedFactor.getTimeInMillis(currentTime) * currentTime), 1.0F));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void apply(long millisSinceStart, float targetScale) {
/*  66 */     float secondsSinceStart = getElapsedSeconds(millisSinceStart);
/*  67 */     Vector3f scratchVector = new Vector3f();
/*  68 */     for (Entry entry : this.entries) {
/*  69 */       entry.apply(secondsSinceStart, targetScale, scratchVector);
/*     */     }
/*     */   }
/*     */   
/*     */   private float getElapsedSeconds(long millisSinceStart) {
/*  74 */     float secondsSinceStart = (float)millisSinceStart / 1000.0F;
/*  75 */     return this.definition.looping() ? (secondsSinceStart % this.definition.lengthInSeconds()) : secondsSinceStart;
/*     */   }
/*     */   private static final class Entry extends Record { private final ModelPart part; private final AnimationChannel.Target target; private final Keyframe[] keyframes;
/*     */     
/*  79 */     private Entry(ModelPart part, AnimationChannel.Target target, Keyframe[] keyframes) { this.part = part; this.target = target; this.keyframes = keyframes; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/animation/KeyframeAnimation$Entry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #79	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  79 */       //   0	7	0	this	Lnet/minecraft/client/animation/KeyframeAnimation$Entry; } public ModelPart part() { return this.part; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/animation/KeyframeAnimation$Entry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #79	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/animation/KeyframeAnimation$Entry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/animation/KeyframeAnimation$Entry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #79	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/animation/KeyframeAnimation$Entry;
/*  79 */       //   0	8	1	o	Ljava/lang/Object; } public AnimationChannel.Target target() { return this.target; } public Keyframe[] keyframes() { return this.keyframes; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void apply(float secondsSinceStart, float targetScale, Vector3f scratchVector) {
/*     */       float lerpAlpha;
/*  86 */       int prev = Math.max(0, Mth.binarySearch(0, this.keyframes.length, i -> (secondsSinceStart <= this.keyframes[secondsSinceStart].timestamp())) - 1);
/*  87 */       int next = Math.min(this.keyframes.length - 1, prev + 1);
/*     */       
/*  89 */       Keyframe previousFrame = this.keyframes[prev];
/*  90 */       Keyframe nextFrame = this.keyframes[next];
/*     */       
/*  92 */       float keyframeTimeDelta = secondsSinceStart - previousFrame.timestamp();
/*     */       
/*  94 */       if (next != prev) {
/*  95 */         lerpAlpha = Mth.clamp(keyframeTimeDelta / (nextFrame.timestamp() - previousFrame.timestamp()), 0.0F, 1.0F);
/*     */       } else {
/*  97 */         lerpAlpha = 0.0F;
/*     */       } 
/*     */       
/* 100 */       nextFrame.interpolation().apply(scratchVector, lerpAlpha, this.keyframes, prev, next, targetScale);
/* 101 */       this.target.apply(this.part, scratchVector);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/animation/KeyframeAnimation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */