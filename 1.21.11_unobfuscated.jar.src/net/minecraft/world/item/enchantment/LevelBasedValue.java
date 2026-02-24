/*     */ package net.minecraft.world.item.enchantment;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.function.BiFunction;
/*     */ import net.minecraft.core.Registry;
/*     */ 
/*     */ public interface LevelBasedValue {
/*     */   public static final Codec<LevelBasedValue> DISPATCH_CODEC;
/*     */   public static final Codec<LevelBasedValue> CODEC;
/*     */   
/*     */   static {
/*  16 */     DISPATCH_CODEC = net.minecraft.core.registries.BuiltInRegistries.ENCHANTMENT_LEVEL_BASED_VALUE_TYPE.byNameCodec().dispatch(LevelBasedValue::codec, c -> c);
/*     */     
/*  18 */     CODEC = Codec.either(Constant.CODEC, DISPATCH_CODEC).xmap(either -> (LevelBasedValue)either.map((), ()), levelBasedValue -> {
/*     */           Constant constant = (Constant)levelBasedValue;
/*     */           return (levelBasedValue instanceof Constant) ? Either.left(constant) : Either.right(levelBasedValue);
/*     */         });
/*     */   }
/*     */   static MapCodec<? extends LevelBasedValue> bootstrap(Registry<MapCodec<? extends LevelBasedValue>> registry) {
/*  24 */     Registry.register(registry, "clamped", Clamped.CODEC);
/*  25 */     Registry.register(registry, "fraction", Fraction.CODEC);
/*  26 */     Registry.register(registry, "levels_squared", LevelsSquared.CODEC);
/*  27 */     Registry.register(registry, "linear", Linear.CODEC);
/*  28 */     Registry.register(registry, "exponent", Exponent.CODEC);
/*  29 */     return (MapCodec<? extends LevelBasedValue>)Registry.register(registry, "lookup", Lookup.CODEC);
/*     */   }
/*     */   
/*     */   static Constant constant(float value) {
/*  33 */     return new Constant(value);
/*     */   }
/*     */   
/*     */   static Linear perLevel(float base, float perLevelAboveFirst) {
/*  37 */     return new Linear(base, perLevelAboveFirst);
/*     */   }
/*     */   
/*     */   static Linear perLevel(float perLevel) {
/*  41 */     return perLevel(perLevel, perLevel);
/*     */   }
/*     */   
/*     */   static Lookup lookup(java.util.List<Float> values, LevelBasedValue fallback) {
/*  45 */     return new Lookup(values, fallback);
/*     */   }
/*     */   float calculate(int paramInt);
/*     */   
/*     */   MapCodec<? extends LevelBasedValue> codec();
/*     */   
/*     */   public static final class Constant extends Record implements LevelBasedValue { private final float value;
/*     */     
/*  53 */     public Constant(float value) { this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Constant;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #53	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  53 */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Constant; } public float value() { return this.value; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Constant;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #53	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Constant; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Constant;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #53	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Constant;
/*  54 */       //   0	8	1	o	Ljava/lang/Object; } public static final Codec<Constant> CODEC = Codec.FLOAT.xmap(Constant::new, Constant::value); public static final MapCodec<Constant> TYPED_CODEC; static {
/*  55 */       TYPED_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("value").forGetter(Constant::value)).apply((Applicative)i, Constant::new));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public float calculate(int level) {
/*  61 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public MapCodec<Constant> codec() {
/*  66 */       return TYPED_CODEC;
/*     */     } }
/*     */   public static final class Lookup extends Record implements LevelBasedValue { private final java.util.List<Float> values; private final LevelBasedValue fallback; public static final MapCodec<Lookup> CODEC;
/*     */     
/*  70 */     public Lookup(java.util.List<Float> values, LevelBasedValue fallback) { this.values = values; this.fallback = fallback; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #70	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Lookup;
/*  70 */       //   0	8	1	o	Ljava/lang/Object; } public java.util.List<Float> values() { return this.values; } public LevelBasedValue fallback() { return this.fallback; } static {
/*  71 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.listOf().fieldOf("values").forGetter(Lookup::values), (App)LevelBasedValue.CODEC.fieldOf("fallback").forGetter(Lookup::fallback)).apply((Applicative)i, Lookup::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float calculate(int level) {
/*  78 */       return (level <= this.values.size()) ? (Float)this.values.get(level - 1) : this.fallback.calculate(level);
/*     */     }
/*     */ 
/*     */     
/*     */     public MapCodec<Lookup> codec() {
/*  83 */       return CODEC;
/*     */     } }
/*     */   public static final class Linear extends Record implements LevelBasedValue { private final float base; private final float perLevelAboveFirst; public static final MapCodec<Linear> CODEC;
/*     */     
/*  87 */     public Linear(float base, float perLevelAboveFirst) { this.base = base; this.perLevelAboveFirst = perLevelAboveFirst; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Linear;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Linear; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Linear;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Linear; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Linear;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #87	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Linear;
/*  87 */       //   0	8	1	o	Ljava/lang/Object; } public float base() { return this.base; } public float perLevelAboveFirst() { return this.perLevelAboveFirst; } static {
/*  88 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("base").forGetter(Linear::base), (App)Codec.FLOAT.fieldOf("per_level_above_first").forGetter(Linear::perLevelAboveFirst)).apply((Applicative)i, Linear::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float calculate(int level) {
/*  95 */       return this.base + this.perLevelAboveFirst * (level - 1);
/*     */     }
/*     */ 
/*     */     
/*     */     public MapCodec<Linear> codec() {
/* 100 */       return CODEC;
/*     */     } }
/*     */   public static final class Clamped extends Record implements LevelBasedValue { private final LevelBasedValue value; private final float min; private final float max; public static final MapCodec<Clamped> CODEC;
/*     */     
/* 104 */     public Clamped(LevelBasedValue value, float min, float max) { this.value = value; this.min = min; this.max = max; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #104	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Clamped;
/* 104 */       //   0	8	1	o	Ljava/lang/Object; } public LevelBasedValue value() { return this.value; } public float min() { return this.min; } public float max() { return this.max; }
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 109 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LevelBasedValue.CODEC.fieldOf("value").forGetter(Clamped::value), (App)Codec.FLOAT.fieldOf("min").forGetter(Clamped::min), (App)Codec.FLOAT.fieldOf("max").forGetter(Clamped::max)).apply((Applicative)i, Clamped::new)).validate(u -> (u.max <= u.min) ? com.mojang.serialization.DataResult.error(()) : com.mojang.serialization.DataResult.success(u));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float calculate(int level) {
/* 118 */       return net.minecraft.util.Mth.clamp(this.value.calculate(level), this.min, this.max);
/*     */     }
/*     */ 
/*     */     
/*     */     public MapCodec<Clamped> codec() {
/* 123 */       return CODEC;
/*     */     } }
/*     */   public static final class Fraction extends Record implements LevelBasedValue { private final LevelBasedValue numerator; private final LevelBasedValue denominator; public static final MapCodec<Fraction> CODEC;
/*     */     
/* 127 */     public Fraction(LevelBasedValue numerator, LevelBasedValue denominator) { this.numerator = numerator; this.denominator = denominator; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Fraction;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #127	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Fraction; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Fraction;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #127	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Fraction; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Fraction;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #127	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Fraction;
/* 127 */       //   0	8	1	o	Ljava/lang/Object; } public LevelBasedValue numerator() { return this.numerator; } public LevelBasedValue denominator() { return this.denominator; } static {
/* 128 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LevelBasedValue.CODEC.fieldOf("numerator").forGetter(Fraction::numerator), (App)LevelBasedValue.CODEC.fieldOf("denominator").forGetter(Fraction::denominator)).apply((Applicative)i, Fraction::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float calculate(int level) {
/* 135 */       float denominator = this.denominator.calculate(level);
/* 136 */       if (denominator == 0.0F) {
/* 137 */         return 0.0F;
/*     */       }
/* 139 */       return this.numerator.calculate(level) / denominator;
/*     */     }
/*     */ 
/*     */     
/*     */     public MapCodec<Fraction> codec() {
/* 144 */       return CODEC;
/*     */     } }
/*     */   public static final class Exponent extends Record implements LevelBasedValue { private final LevelBasedValue base; private final LevelBasedValue power; public static final MapCodec<Exponent> CODEC;
/*     */     
/* 148 */     public Exponent(LevelBasedValue base, LevelBasedValue power) { this.base = base; this.power = power; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Exponent;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #148	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Exponent; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Exponent;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #148	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Exponent; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$Exponent;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #148	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$Exponent;
/* 148 */       //   0	8	1	o	Ljava/lang/Object; } public LevelBasedValue base() { return this.base; } public LevelBasedValue power() { return this.power; } static {
/* 149 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LevelBasedValue.CODEC.fieldOf("base").forGetter(Exponent::base), (App)LevelBasedValue.CODEC.fieldOf("power").forGetter(Exponent::power)).apply((Applicative)i, Exponent::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public float calculate(int level) {
/* 156 */       return (float)Math.pow(this.base.calculate(level), this.power.calculate(level));
/*     */     }
/*     */ 
/*     */     
/*     */     public MapCodec<Exponent> codec() {
/* 161 */       return CODEC;
/*     */     } }
/*     */   public static final class LevelsSquared extends Record implements LevelBasedValue { private final float added;
/*     */     public static final MapCodec<LevelsSquared> CODEC;
/*     */     
/* 166 */     public LevelsSquared(float added) { this.added = added; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$LevelsSquared;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #166	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$LevelsSquared; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$LevelsSquared;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #166	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$LevelsSquared; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/item/enchantment/LevelBasedValue$LevelsSquared;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #166	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/item/enchantment/LevelBasedValue$LevelsSquared;
/* 166 */       //   0	8	1	o	Ljava/lang/Object; } public float added() { return this.added; } static {
/* 167 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("added").forGetter(LevelsSquared::added)).apply((Applicative)i, LevelsSquared::new));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public float calculate(int level) {
/* 173 */       return net.minecraft.util.Mth.square(level) + this.added;
/*     */     }
/*     */ 
/*     */     
/*     */     public MapCodec<LevelsSquared> codec() {
/* 178 */       return CODEC;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/enchantment/LevelBasedValue.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */