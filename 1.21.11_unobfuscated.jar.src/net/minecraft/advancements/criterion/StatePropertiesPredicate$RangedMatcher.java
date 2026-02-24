/*     */ package net.minecraft.advancements.criterion;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.world.level.block.state.StateHolder;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ final class RangedMatcher
/*     */   extends Record
/*     */   implements StatePropertiesPredicate.ValueMatcher
/*     */ {
/*     */   private final Optional<String> minValue;
/*     */   private final Optional<String> maxValue;
/*     */   public static final Codec<RangedMatcher> CODEC;
/*     */   
/*     */   public final String toString() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$RangedMatcher;)Ljava/lang/String;
/*     */     //   6: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #88	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$RangedMatcher;
/*     */   }
/*     */   
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$RangedMatcher;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #88	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$RangedMatcher;
/*     */   }
/*     */   
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$RangedMatcher;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #88	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/StatePropertiesPredicate$RangedMatcher;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   
/*     */   private RangedMatcher(Optional<String> minValue, Optional<String> maxValue) {
/*  88 */     this.minValue = minValue; this.maxValue = maxValue; } public Optional<String> minValue() { return this.minValue; } public Optional<String> maxValue() { return this.maxValue; } static {
/*  89 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Codec.STRING.optionalFieldOf("min").forGetter(RangedMatcher::minValue), (App)Codec.STRING.optionalFieldOf("max").forGetter(RangedMatcher::maxValue)).apply((Applicative)i, RangedMatcher::new));
/*     */   }
/*     */ 
/*     */   
/*  93 */   public static final StreamCodec<ByteBuf, RangedMatcher> STREAM_CODEC = StreamCodec.composite(
/*  94 */       ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), RangedMatcher::minValue, 
/*  95 */       ByteBufCodecs.optional(ByteBufCodecs.STRING_UTF8), RangedMatcher::maxValue, RangedMatcher::new);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends Comparable<T>> boolean match(StateHolder<?, ?> state, Property<T> property) {
/* 101 */     Comparable<Comparable> comparable = state.getValue(property);
/*     */     
/* 103 */     if (this.minValue.isPresent()) {
/* 104 */       Optional<T> typedMinValue = property.getValue(this.minValue.get());
/* 105 */       if (typedMinValue.isEmpty() || comparable.compareTo((Comparable)typedMinValue.get()) < 0) {
/* 106 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 110 */     if (this.maxValue.isPresent()) {
/* 111 */       Optional<T> typedMaxValue = property.getValue(this.maxValue.get());
/* 112 */       if (typedMaxValue.isEmpty() || comparable.compareTo((Comparable)typedMaxValue.get()) > 0) {
/* 113 */         return false;
/*     */       }
/*     */     } 
/*     */     
/* 117 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/StatePropertiesPredicate$RangedMatcher.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */