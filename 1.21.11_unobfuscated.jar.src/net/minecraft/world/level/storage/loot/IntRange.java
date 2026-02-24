/*     */ package net.minecraft.world.level.storage.loot;
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.Set;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
/*     */ import net.minecraft.world.level.storage.loot.providers.number.NumberProviders;
/*     */ 
/*     */ public class IntRange {
/*     */   static {
/*  21 */     RECORD_CODEC = RecordCodecBuilder.create(i -> i.group((App)NumberProviders.CODEC.optionalFieldOf("min").forGetter(()), (App)NumberProviders.CODEC.optionalFieldOf("max").forGetter(())).apply((Applicative)i, IntRange::new));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  26 */     CODEC = Codec.either((Codec)Codec.INT, RECORD_CODEC).xmap(e -> (IntRange)e.map(IntRange::exact, Function.identity()), range -> {
/*     */           OptionalInt exact = range.unpackExact();
/*     */           return exact.isPresent() ? Either.left(exact.getAsInt()) : Either.right(range);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Codec<IntRange> RECORD_CODEC;
/*     */ 
/*     */   
/*     */   public static final Codec<IntRange> CODEC;
/*     */ 
/*     */   
/*     */   private final NumberProvider min;
/*     */ 
/*     */   
/*     */   private final NumberProvider max;
/*     */ 
/*     */   
/*     */   private final IntLimiter limiter;
/*     */ 
/*     */   
/*     */   private final IntChecker predicate;
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<ContextKey<?>> getReferencedContextParams() {
/*  55 */     ImmutableSet.Builder<ContextKey<?>> result = ImmutableSet.builder();
/*  56 */     if (this.min != null) {
/*  57 */       result.addAll(this.min.getReferencedContextParams());
/*     */     }
/*  59 */     if (this.max != null) {
/*  60 */       result.addAll(this.max.getReferencedContextParams());
/*     */     }
/*  62 */     return (Set<ContextKey<?>>)result.build();
/*     */   }
/*     */   
/*     */   private IntRange(Optional<NumberProvider> min, Optional<NumberProvider> max) {
/*  66 */     this(min.orElse(null), max.orElse(null));
/*     */   }
/*     */   
/*     */   private IntRange(NumberProvider min, NumberProvider max) {
/*  70 */     this.min = min;
/*  71 */     this.max = max;
/*     */     
/*  73 */     if (min == null) {
/*  74 */       if (max == null) {
/*  75 */         this.limiter = ((context, value) -> value);
/*  76 */         this.predicate = ((context, value) -> true);
/*     */       } else {
/*  78 */         this.limiter = ((context, value) -> Math.min(max.getInt(context), value));
/*  79 */         this.predicate = ((context, value) -> (value <= max.getInt(context)));
/*     */       }
/*     */     
/*  82 */     } else if (max == null) {
/*  83 */       this.limiter = ((context, value) -> Math.max(min.getInt(context), value));
/*  84 */       this.predicate = ((context, value) -> (value >= min.getInt(context)));
/*     */     } else {
/*  86 */       this.limiter = ((context, value) -> Mth.clamp(value, min.getInt(context), max.getInt(context)));
/*  87 */       this.predicate = ((context, value) -> (value >= min.getInt(context) && value <= max.getInt(context)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static IntRange exact(int value) {
/*  93 */     ConstantValue c = ConstantValue.exactly(value);
/*  94 */     return new IntRange((Optional)Optional.of(c), (Optional)Optional.of(c));
/*     */   }
/*     */   
/*     */   public static IntRange range(int min, int max) {
/*  98 */     return new IntRange((Optional)Optional.of(ConstantValue.exactly(min)), (Optional)Optional.of(ConstantValue.exactly(max)));
/*     */   }
/*     */   
/*     */   public static IntRange lowerBound(int value) {
/* 102 */     return new IntRange((Optional)Optional.of(ConstantValue.exactly(value)), Optional.empty());
/*     */   }
/*     */   
/*     */   public static IntRange upperBound(int value) {
/* 106 */     return new IntRange(Optional.empty(), (Optional)Optional.of(ConstantValue.exactly(value)));
/*     */   }
/*     */   
/*     */   public int clamp(LootContext context, int value) {
/* 110 */     return this.limiter.apply(context, value);
/*     */   }
/*     */   
/*     */   public boolean test(LootContext context, int value) {
/* 114 */     return this.predicate.test(context, value);
/*     */   }
/*     */   
/*     */   private OptionalInt unpackExact() {
/* 118 */     if (java.util.Objects.equals(this.min, this.max)) { NumberProvider numberProvider = this.min; if (numberProvider instanceof ConstantValue) { ConstantValue constant = (ConstantValue)numberProvider;
/* 119 */         if (Math.floor(constant.value()) == constant.value())
/* 120 */           return OptionalInt.of((int)constant.value());  }
/*     */        }
/*     */     
/* 123 */     return OptionalInt.empty();
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface IntLimiter {
/*     */     int apply(LootContext param1LootContext, int param1Int);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   private static interface IntChecker {
/*     */     boolean test(LootContext param1LootContext, int param1Int);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/IntRange.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */