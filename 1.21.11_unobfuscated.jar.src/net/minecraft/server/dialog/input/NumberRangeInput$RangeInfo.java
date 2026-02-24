/*     */ package net.minecraft.server.dialog.input;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class RangeInfo
/*     */   extends Record
/*     */ {
/*     */   private final float start;
/*     */   private final float end;
/*     */   private final Optional<Float> initial;
/*     */   private final Optional<Float> step;
/*     */   public static final MapCodec<RangeInfo> MAP_CODEC;
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/dialog/input/NumberRangeInput$RangeInfo;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #39	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/NumberRangeInput$RangeInfo;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/dialog/input/NumberRangeInput$RangeInfo;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #39	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/server/dialog/input/NumberRangeInput$RangeInfo;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/dialog/input/NumberRangeInput$RangeInfo;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #39	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/server/dialog/input/NumberRangeInput$RangeInfo;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   public RangeInfo(float start, float end, Optional<Float> initial, Optional<Float> step) {
/*  39 */     this.start = start; this.end = end; this.initial = initial; this.step = step; } public float start() { return this.start; } public float end() { return this.end; } public Optional<Float> initial() { return this.initial; } public Optional<Float> step() { return this.step; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static {
/*  52 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("start").forGetter(RangeInfo::start), (App)Codec.FLOAT.fieldOf("end").forGetter(RangeInfo::end), (App)Codec.FLOAT.optionalFieldOf("initial").forGetter(RangeInfo::initial), (App)ExtraCodecs.POSITIVE_FLOAT.optionalFieldOf("step").forGetter(RangeInfo::step)).apply((Applicative)i, RangeInfo::new)).validate(range -> {
/*     */           if (range.initial.isPresent()) {
/*     */             double initial = (Float)range.initial.get(), min = Math.min(range.start, range.end), max = Math.max(range.start, range.end);
/*     */             if (initial < min || initial > max) {
/*     */               return DataResult.error(());
/*     */             }
/*     */           } 
/*     */           return DataResult.success(range);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public float computeScaledValue(float sliderValue) {
/*  68 */     float valueInRange = Mth.lerp(sliderValue, this.start, this.end);
/*     */     
/*  70 */     if (this.step.isEmpty())
/*     */     {
/*  72 */       return valueInRange;
/*     */     }
/*     */     
/*  75 */     float step = (Float)this.step.get();
/*  76 */     float initialValue = initialScaledValue();
/*  77 */     float deltaToInitial = valueInRange - initialValue;
/*     */     
/*  79 */     int stepsOutsideInitial = Math.round(deltaToInitial / step);
/*  80 */     float result = initialValue + stepsOutsideInitial * step;
/*  81 */     if (!isOutOfRange(result)) {
/*  82 */       return result;
/*     */     }
/*     */     
/*  85 */     int oneStepLess = stepsOutsideInitial - Mth.sign(stepsOutsideInitial);
/*  86 */     return initialValue + oneStepLess * step;
/*     */   }
/*     */   
/*     */   private boolean isOutOfRange(float scaledValue) {
/*  90 */     float sliderPos = scaledValueToSlider(scaledValue);
/*  91 */     return (sliderPos < 0.0D || sliderPos > 1.0D);
/*     */   }
/*     */   
/*     */   private float initialScaledValue() {
/*  95 */     if (this.initial.isPresent()) {
/*  96 */       return (Float)this.initial.get();
/*     */     }
/*     */     
/*  99 */     return (this.start + this.end) / 2.0F;
/*     */   }
/*     */   
/*     */   public float initialSliderValue() {
/* 103 */     float value = initialScaledValue();
/* 104 */     return scaledValueToSlider(value);
/*     */   }
/*     */   
/*     */   private float scaledValueToSlider(float value) {
/* 108 */     if (this.start == this.end) {
/* 109 */       return 0.5F;
/*     */     }
/* 111 */     return Mth.inverseLerp(value, this.start, this.end);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/input/NumberRangeInput$RangeInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */