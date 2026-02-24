/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.world.attribute.LerpFunction;
/*     */ 
/*     */ 
/*     */ public class KeyframeTrackSampler<T>
/*     */ {
/*     */   private final Optional<Integer> periodTicks;
/*     */   private final LerpFunction<T> lerp;
/*     */   private final List<Segment<T>> segments;
/*     */   
/*     */   KeyframeTrackSampler(KeyframeTrack<T> track, Optional<Integer> periodTicks, LerpFunction<T> lerp) {
/*  16 */     this.periodTicks = periodTicks;
/*  17 */     this.lerp = lerp;
/*  18 */     this.segments = bakeSegments(track, periodTicks);
/*     */   }
/*     */   
/*     */   private static <T> List<Segment<T>> bakeSegments(KeyframeTrack<T> track, Optional<Integer> periodTicks) {
/*  22 */     List<Keyframe<T>> keyframes = track.keyframes();
/*  23 */     if (keyframes.size() == 1) {
/*  24 */       T value = ((Keyframe<T>)keyframes.getFirst()).value();
/*  25 */       return List.of(new Segment<>(EasingType.CONSTANT, value, 0, value, 0));
/*     */     } 
/*  27 */     List<Segment<T>> segments = new ArrayList<>();
/*     */ 
/*     */     
/*  30 */     if (periodTicks.isPresent()) {
/*  31 */       Keyframe<T> firstKeyframe = keyframes.getFirst();
/*  32 */       Keyframe<T> lastKeyframe = keyframes.getLast();
/*  33 */       segments.add(new Segment<>(track, lastKeyframe, 
/*     */ 
/*     */             
/*  36 */             lastKeyframe.ticks() - (Integer)periodTicks.get(), firstKeyframe, 
/*     */             
/*  38 */             firstKeyframe.ticks()));
/*     */       
/*  40 */       addSegmentsFromKeyframes(track, keyframes, segments);
/*  41 */       segments.add(new Segment<>(track, lastKeyframe, 
/*     */ 
/*     */             
/*  44 */             lastKeyframe.ticks(), firstKeyframe, 
/*     */             
/*  46 */             firstKeyframe.ticks() + (Integer)periodTicks.get()));
/*     */     } else {
/*     */       
/*  49 */       addSegmentsFromKeyframes(track, keyframes, segments);
/*     */     } 
/*  51 */     return List.copyOf(segments);
/*     */   }
/*     */   
/*     */   private static <T> void addSegmentsFromKeyframes(KeyframeTrack<T> track, List<Keyframe<T>> keyframes, List<Segment<T>> output) {
/*  55 */     for (int i = 0; i < keyframes.size() - 1; i++) {
/*  56 */       Keyframe<T> keyframe = keyframes.get(i);
/*  57 */       Keyframe<T> nextKeyframe = keyframes.get(i + 1);
/*  58 */       output.add(new Segment<>(track, keyframe, keyframe.ticks(), nextKeyframe, nextKeyframe.ticks()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public T sample(long ticks) {
/*  63 */     long sampleTicks = loopTicks(ticks);
/*  64 */     Segment<T> segment = getSegmentAt(sampleTicks);
/*  65 */     if (sampleTicks <= segment.fromTicks)
/*  66 */       return segment.fromValue; 
/*  67 */     if (sampleTicks >= segment.toTicks) {
/*  68 */       return segment.toValue;
/*     */     }
/*  70 */     float alpha = (float)(sampleTicks - segment.fromTicks) / (segment.toTicks - segment.fromTicks);
/*  71 */     float easedAlpha = segment.easing.apply(alpha);
/*  72 */     return (T)this.lerp.apply(easedAlpha, segment.fromValue, segment.toValue);
/*     */   }
/*     */   
/*     */   private Segment<T> getSegmentAt(long currentTicks) {
/*  76 */     for (Segment<T> segment : this.segments) {
/*  77 */       if (currentTicks < segment.toTicks) {
/*  78 */         return segment;
/*     */       }
/*     */     } 
/*  81 */     return this.segments.getLast();
/*     */   }
/*     */   
/*     */   private long loopTicks(long ticks) {
/*  85 */     if (this.periodTicks.isPresent()) {
/*  86 */       return Math.floorMod(ticks, (Integer)this.periodTicks.get());
/*     */     }
/*  88 */     return ticks;
/*     */   } private static final class Segment<T> extends Record {
/*     */     private final EasingType easing; private final T fromValue;
/*  91 */     public int toTicks() { return this.toTicks; } private final int fromTicks; private final T toValue; private final int toTicks; public T toValue() { return this.toValue; } public int fromTicks() { return this.fromTicks; } public T fromValue() { return this.fromValue; } public EasingType easing() { return this.easing; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/KeyframeTrackSampler$Segment;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #91	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/KeyframeTrackSampler$Segment;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  91 */       //   0	8	0	this	Lnet/minecraft/util/KeyframeTrackSampler$Segment<TT;>; } private Segment(EasingType easing, T fromValue, int fromTicks, T toValue, int toTicks) { this.easing = easing; this.fromValue = fromValue; this.fromTicks = fromTicks; this.toValue = toValue; this.toTicks = toTicks; }
/*     */      public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/KeyframeTrackSampler$Segment;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #91	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/KeyframeTrackSampler$Segment;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/KeyframeTrackSampler$Segment<TT;>;
/*     */     }
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/KeyframeTrackSampler$Segment;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #91	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/KeyframeTrackSampler$Segment;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/KeyframeTrackSampler$Segment<TT;>;
/*     */     }
/*     */     public Segment(KeyframeTrack<T> track, Keyframe<T> from, int fromTicks, Keyframe<T> to, int toTicks) {
/*  99 */       this(
/* 100 */           track.easingType(), 
/* 101 */           from.value(), fromTicks, 
/* 102 */           to.value(), toTicks);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/KeyframeTrackSampler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */