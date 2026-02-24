/*      */ package net.minecraft.world.level.levelgen;
/*      */ import com.mojang.datafixers.kinds.App;
/*      */ import com.mojang.datafixers.kinds.Applicative;
/*      */ import com.mojang.datafixers.util.Either;
/*      */ import com.mojang.datafixers.util.Function3;
/*      */ import com.mojang.datafixers.util.Function4;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.MapCodec;
/*      */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*      */ import it.unimi.dsi.fastutil.doubles.Double2DoubleFunction;
/*      */ import java.util.Arrays;
/*      */ import java.util.Optional;
/*      */ import java.util.function.BiFunction;
/*      */ import java.util.function.Function;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.util.CubicSpline;
/*      */ import net.minecraft.util.ExtraCodecs;
/*      */ import net.minecraft.util.KeyDispatchDataCodec;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.StringRepresentable;
/*      */ import net.minecraft.world.level.dimension.DimensionType;
/*      */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*      */ import net.minecraft.world.level.levelgen.synth.SimplexNoise;
/*      */ 
/*      */ public final class DensityFunctions {
/*      */   private static final Codec<DensityFunction> CODEC;
/*      */   protected static final double MAX_REASONABLE_NOISE_VALUE = 1000000.0D;
/*      */   
/*      */   static {
/*   33 */     CODEC = net.minecraft.core.registries.BuiltInRegistries.DENSITY_FUNCTION_TYPE.byNameCodec().dispatch(function -> function.codec().codec(), Function.identity());
/*      */   }
/*      */ 
/*      */   
/*   37 */   private static final Codec<Double> NOISE_VALUE_CODEC = Codec.doubleRange(-1000000.0D, 1000000.0D);
/*      */   
/*      */   public static final Codec<DensityFunction> DIRECT_CODEC;
/*      */   
/*      */   static {
/*   42 */     DIRECT_CODEC = Codec.either(NOISE_VALUE_CODEC, CODEC).xmap(either -> (DensityFunction)either.map(DensityFunctions::constant, Function.identity()), function -> {
/*      */           if (function instanceof Constant) {
/*      */             Constant constant = (Constant)function;
/*      */             return Either.left(constant.value());
/*      */           } 
/*      */           return Either.right(function);
/*      */         });
/*      */   } public static MapCodec<? extends DensityFunction> bootstrap(Registry<MapCodec<? extends DensityFunction>> registry) {
/*   50 */     register(registry, "blend_alpha", BlendAlpha.CODEC);
/*   51 */     register(registry, "blend_offset", BlendOffset.CODEC);
/*   52 */     register(registry, "beardifier", BeardifierMarker.CODEC);
/*   53 */     register(registry, "old_blended_noise", net.minecraft.world.level.levelgen.synth.BlendedNoise.CODEC);
/*   54 */     for (Marker.Type value : Marker.Type.values()) {
/*   55 */       register(registry, value.getSerializedName(), (KeyDispatchDataCodec)value.codec);
/*      */     }
/*   57 */     register(registry, "noise", (KeyDispatchDataCodec)Noise.CODEC);
/*   58 */     register(registry, "end_islands", (KeyDispatchDataCodec)EndIslandDensityFunction.CODEC);
/*   59 */     register(registry, "weird_scaled_sampler", (KeyDispatchDataCodec)WeirdScaledSampler.CODEC);
/*   60 */     register(registry, "shifted_noise", (KeyDispatchDataCodec)ShiftedNoise.CODEC);
/*   61 */     register(registry, "range_choice", (KeyDispatchDataCodec)RangeChoice.CODEC);
/*   62 */     register(registry, "shift_a", (KeyDispatchDataCodec)ShiftA.CODEC);
/*   63 */     register(registry, "shift_b", (KeyDispatchDataCodec)ShiftB.CODEC);
/*   64 */     register(registry, "shift", (KeyDispatchDataCodec)Shift.CODEC);
/*   65 */     register(registry, "blend_density", (KeyDispatchDataCodec)BlendDensity.CODEC);
/*   66 */     register(registry, "clamp", (KeyDispatchDataCodec)Clamp.CODEC);
/*   67 */     for (Mapped.Type value : Mapped.Type.values()) {
/*   68 */       register(registry, value.getSerializedName(), (KeyDispatchDataCodec)value.codec);
/*      */     }
/*   70 */     for (TwoArgumentSimpleFunction.Type value : TwoArgumentSimpleFunction.Type.values()) {
/*   71 */       register(registry, value.getSerializedName(), (KeyDispatchDataCodec)value.codec);
/*      */     }
/*   73 */     register(registry, "spline", (KeyDispatchDataCodec)Spline.CODEC);
/*   74 */     register(registry, "constant", (KeyDispatchDataCodec)Constant.CODEC);
/*   75 */     register(registry, "y_clamped_gradient", (KeyDispatchDataCodec)YClampedGradient.CODEC);
/*   76 */     return register(registry, "find_top_surface", (KeyDispatchDataCodec)FindTopSurface.CODEC);
/*      */   }
/*      */   
/*      */   private static MapCodec<? extends DensityFunction> register(Registry<MapCodec<? extends DensityFunction>> registry, String name, KeyDispatchDataCodec<? extends DensityFunction> codec) {
/*   80 */     return (MapCodec<? extends DensityFunction>)Registry.register(registry, name, codec.codec());
/*      */   }
/*      */   
/*      */   private static <A, O> KeyDispatchDataCodec<O> singleArgumentCodec(Codec<A> argumentCodec, Function<A, O> constructor, Function<O, A> getter) {
/*   84 */     return KeyDispatchDataCodec.of(argumentCodec.fieldOf("argument").xmap(constructor, getter));
/*      */   }
/*      */   
/*      */   private static <O> KeyDispatchDataCodec<O> singleFunctionArgumentCodec(Function<DensityFunction, O> constructor, Function<O, DensityFunction> getter) {
/*   88 */     return singleArgumentCodec(DensityFunction.HOLDER_HELPER_CODEC, constructor, getter);
/*      */   }
/*      */   
/*      */   private static <O> KeyDispatchDataCodec<O> doubleFunctionArgumentCodec(BiFunction<DensityFunction, DensityFunction, O> constructor, Function<O, DensityFunction> firstArgumentGetter, Function<O, DensityFunction> secondArgumentGetter) {
/*   92 */     return KeyDispatchDataCodec.of(RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument1").forGetter(firstArgumentGetter), (App)DensityFunction.HOLDER_HELPER_CODEC.fieldOf("argument2").forGetter(secondArgumentGetter)).apply((Applicative)i, constructor)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static <O> KeyDispatchDataCodec<O> makeCodec(MapCodec<O> dataCodec) {
/*   99 */     return KeyDispatchDataCodec.of(dataCodec);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static DensityFunction interpolated(DensityFunction function) {
/*  106 */     return new Marker(Marker.Type.Interpolated, function);
/*      */   }
/*      */   
/*      */   public static DensityFunction flatCache(DensityFunction function) {
/*  110 */     return new Marker(Marker.Type.FlatCache, function);
/*      */   }
/*      */   
/*      */   public static DensityFunction cache2d(DensityFunction function) {
/*  114 */     return new Marker(Marker.Type.Cache2D, function);
/*      */   }
/*      */   
/*      */   public static DensityFunction cacheOnce(DensityFunction function) {
/*  118 */     return new Marker(Marker.Type.CacheOnce, function);
/*      */   }
/*      */   
/*      */   public static DensityFunction cacheAllInCell(DensityFunction function) {
/*  122 */     return new Marker(Marker.Type.CacheAllInCell, function);
/*      */   }
/*      */   
/*      */   public static DensityFunction mappedNoise(Holder<NormalNoise.NoiseParameters> noiseData, @Deprecated double xzScale, double yScale, double minTarget, double maxTarget) {
/*  126 */     return mapFromUnitTo(new Noise(new DensityFunction.NoiseHolder(noiseData), xzScale, yScale), minTarget, maxTarget);
/*      */   }
/*      */   
/*      */   public static DensityFunction mappedNoise(Holder<NormalNoise.NoiseParameters> noiseData, double yScale, double minTarget, double maxTarget) {
/*  130 */     return mappedNoise(noiseData, 1.0D, yScale, minTarget, maxTarget);
/*      */   }
/*      */   
/*      */   public static DensityFunction mappedNoise(Holder<NormalNoise.NoiseParameters> noiseData, double minTarget, double maxTarget) {
/*  134 */     return mappedNoise(noiseData, 1.0D, 1.0D, minTarget, maxTarget);
/*      */   }
/*      */   
/*      */   public static DensityFunction shiftedNoise2d(DensityFunction shiftX, DensityFunction shiftZ, double xzScale, Holder<NormalNoise.NoiseParameters> noiseData) {
/*  138 */     return new ShiftedNoise(shiftX, zero(), shiftZ, xzScale, 0.0D, new DensityFunction.NoiseHolder(noiseData));
/*      */   }
/*      */   
/*      */   public static DensityFunction noise(Holder<NormalNoise.NoiseParameters> noiseData) {
/*  142 */     return noise(noiseData, 1.0D, 1.0D);
/*      */   }
/*      */   
/*      */   public static DensityFunction noise(Holder<NormalNoise.NoiseParameters> noiseData, double xzScale, double yScale) {
/*  146 */     return new Noise(new DensityFunction.NoiseHolder(noiseData), xzScale, yScale);
/*      */   }
/*      */   
/*      */   public static DensityFunction noise(Holder<NormalNoise.NoiseParameters> noiseData, double yScale) {
/*  150 */     return noise(noiseData, 1.0D, yScale);
/*      */   }
/*      */   
/*      */   public static DensityFunction rangeChoice(DensityFunction input, double minInclusive, double maxExclusive, DensityFunction whenInRange, DensityFunction whenOutOfRange) {
/*  154 */     return new RangeChoice(input, minInclusive, maxExclusive, whenInRange, whenOutOfRange);
/*      */   }
/*      */   
/*      */   public static DensityFunction shiftA(Holder<NormalNoise.NoiseParameters> noiseData) {
/*  158 */     return new ShiftA(new DensityFunction.NoiseHolder(noiseData));
/*      */   }
/*      */   
/*      */   public static DensityFunction shiftB(Holder<NormalNoise.NoiseParameters> noiseData) {
/*  162 */     return new ShiftB(new DensityFunction.NoiseHolder(noiseData));
/*      */   }
/*      */   
/*      */   public static DensityFunction shift(Holder<NormalNoise.NoiseParameters> noiseData) {
/*  166 */     return new Shift(new DensityFunction.NoiseHolder(noiseData));
/*      */   }
/*      */   
/*      */   public static DensityFunction blendDensity(DensityFunction input) {
/*  170 */     return new BlendDensity(input);
/*      */   }
/*      */   
/*      */   public static DensityFunction endIslands(long seed) {
/*  174 */     return new EndIslandDensityFunction(seed);
/*      */   }
/*      */   
/*      */   public static DensityFunction weirdScaledSampler(DensityFunction input, Holder<NormalNoise.NoiseParameters> noiseData, WeirdScaledSampler.RarityValueMapper rarityValueMapper) {
/*  178 */     return new WeirdScaledSampler(input, new DensityFunction.NoiseHolder(noiseData), rarityValueMapper);
/*      */   }
/*      */   
/*      */   public static DensityFunction add(DensityFunction f1, DensityFunction f2) {
/*  182 */     return TwoArgumentSimpleFunction.create(TwoArgumentSimpleFunction.Type.ADD, f1, f2);
/*      */   }
/*      */   
/*      */   public static DensityFunction mul(DensityFunction f1, DensityFunction f2) {
/*  186 */     return TwoArgumentSimpleFunction.create(TwoArgumentSimpleFunction.Type.MUL, f1, f2);
/*      */   }
/*      */   
/*      */   public static DensityFunction min(DensityFunction f1, DensityFunction f2) {
/*  190 */     return TwoArgumentSimpleFunction.create(TwoArgumentSimpleFunction.Type.MIN, f1, f2);
/*      */   }
/*      */   
/*      */   public static DensityFunction max(DensityFunction f1, DensityFunction f2) {
/*  194 */     return TwoArgumentSimpleFunction.create(TwoArgumentSimpleFunction.Type.MAX, f1, f2);
/*      */   }
/*      */   
/*      */   public static DensityFunction spline(CubicSpline<Spline.Point, Spline.Coordinate> spline) {
/*  198 */     return new Spline(spline);
/*      */   }
/*      */   
/*      */   public static DensityFunction zero() {
/*  202 */     return Constant.ZERO;
/*      */   }
/*      */   
/*      */   public static DensityFunction constant(double value) {
/*  206 */     return new Constant(value);
/*      */   }
/*      */   
/*      */   public static DensityFunction yClampedGradient(int fromY, int toY, double fromValue, double toValue) {
/*  210 */     return new YClampedGradient(fromY, toY, fromValue, toValue);
/*      */   }
/*      */   
/*      */   public static DensityFunction map(DensityFunction function, Mapped.Type type) {
/*  214 */     return Mapped.create(type, function);
/*      */   }
/*      */   
/*      */   private static DensityFunction mapFromUnitTo(DensityFunction function, double min, double max) {
/*  218 */     double middle = (min + max) * 0.5D;
/*  219 */     double factor = (max - min) * 0.5D;
/*      */     
/*  221 */     return add(constant(middle), mul(constant(factor), function));
/*      */   }
/*      */   
/*      */   public static DensityFunction blendAlpha() {
/*  225 */     return BlendAlpha.INSTANCE;
/*      */   }
/*      */   
/*      */   public static DensityFunction blendOffset() {
/*  229 */     return BlendOffset.INSTANCE;
/*      */   }
/*      */   
/*      */   public static DensityFunction lerp(DensityFunction alpha, DensityFunction first, DensityFunction second) {
/*  233 */     if (first instanceof Constant) { Constant constant = (Constant)first;
/*  234 */       return lerp(alpha, constant.value, second); }
/*      */     
/*  236 */     DensityFunction alphaCached = cacheOnce(alpha);
/*  237 */     DensityFunction oneMinusAlpha = add(mul(alphaCached, constant(-1.0D)), constant(1.0D));
/*  238 */     return add(mul(first, oneMinusAlpha), mul(second, alphaCached));
/*      */   }
/*      */ 
/*      */   
/*      */   public static DensityFunction lerp(DensityFunction factor, double first, DensityFunction second) {
/*  243 */     return add(mul(factor, add(second, constant(-first))), constant(first));
/*      */   }
/*      */   
/*      */   public static DensityFunction findTopSurface(DensityFunction density, DensityFunction upperBound, int lowerBound, int stepSize) {
/*  247 */     return new FindTopSurface(density, upperBound, lowerBound, stepSize);
/*      */   }
/*      */ 
/*      */   
/*      */   private static interface TransformerWithContext
/*      */     extends DensityFunction
/*      */   {
/*      */     DensityFunction input();
/*      */     
/*      */     default double compute(DensityFunction.FunctionContext context) {
/*  257 */       return transform(context, input().compute(context));
/*      */     }
/*      */ 
/*      */     
/*      */     default void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  262 */       input().fillArray(output, contextProvider);
/*  263 */       for (int i = 0; i < output.length; i++)
/*  264 */         output[i] = transform(contextProvider.forIndex(i), output[i]); 
/*      */     }
/*      */     
/*      */     double transform(DensityFunction.FunctionContext param1FunctionContext, double param1Double);
/*      */   }
/*      */   
/*      */   private static interface PureTransformer
/*      */     extends DensityFunction
/*      */   {
/*      */     DensityFunction input();
/*      */     
/*      */     default double compute(DensityFunction.FunctionContext context) {
/*  276 */       return transform(input().compute(context));
/*      */     }
/*      */ 
/*      */     
/*      */     default void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  281 */       input().fillArray(output, contextProvider);
/*  282 */       for (int i = 0; i < output.length; i++)
/*  283 */         output[i] = transform(output[i]); 
/*      */     }
/*      */     
/*      */     double transform(double param1Double);
/*      */   }
/*      */   
/*      */   protected enum BlendAlpha
/*      */     implements DensityFunction.SimpleFunction {
/*  291 */     INSTANCE;
/*  292 */     public static final KeyDispatchDataCodec<DensityFunction> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
/*      */ 
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  296 */       return 1.0D;
/*      */     }
/*      */ 
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  301 */       Arrays.fill(output, 1.0D);
/*      */     }
/*      */ 
/*      */     
/*      */     public double minValue() {
/*  306 */       return 1.0D;
/*      */     }
/*      */ 
/*      */     
/*      */     public double maxValue() {
/*  311 */       return 1.0D;
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  316 */       return CODEC;
/*      */     }
/*      */   }
/*      */   
/*      */   protected enum BlendOffset implements DensityFunction.SimpleFunction {
/*  321 */     INSTANCE;
/*  322 */     public static final KeyDispatchDataCodec<DensityFunction> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(INSTANCE));
/*      */ 
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  326 */       return 0.0D;
/*      */     }
/*      */ 
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  331 */       Arrays.fill(output, 0.0D);
/*      */     }
/*      */ 
/*      */     
/*      */     public double minValue() {
/*  336 */       return 0.0D;
/*      */     }
/*      */ 
/*      */     
/*      */     public double maxValue() {
/*  341 */       return 0.0D;
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  346 */       return CODEC;
/*      */     }
/*      */   }
/*      */   
/*      */   public static interface BeardifierOrMarker extends DensityFunction.SimpleFunction {
/*  351 */     public static final KeyDispatchDataCodec<DensityFunction> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(DensityFunctions.BeardifierMarker.INSTANCE));
/*      */ 
/*      */     
/*      */     default KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  355 */       return CODEC;
/*      */     }
/*      */   }
/*      */   
/*      */   protected enum BeardifierMarker implements BeardifierOrMarker {
/*  360 */     INSTANCE;
/*      */ 
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  364 */       return 0.0D;
/*      */     }
/*      */ 
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  369 */       Arrays.fill(output, 0.0D);
/*      */     }
/*      */ 
/*      */     
/*      */     public double minValue() {
/*  374 */       return 0.0D;
/*      */     }
/*      */ 
/*      */     
/*      */     public double maxValue() {
/*  379 */       return 0.0D;
/*      */     } } @net.minecraft.util.VisibleForDebug
/*      */   public static final class HolderHolder extends Record implements DensityFunction { private final Holder<DensityFunction> function; public final String toString() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #388	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;
/*      */     } public final int hashCode() {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #388	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;
/*      */     } public final boolean equals(Object o) {
/*      */       // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #388	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$HolderHolder;
/*      */       //   0	8	1	o	Ljava/lang/Object;
/*      */     }
/*  388 */     public Holder<DensityFunction> function() { return this.function; } public HolderHolder(Holder<DensityFunction> function) {
/*  389 */       this.function = function;
/*      */     }
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  392 */       return ((DensityFunction)this.function.value()).compute(context);
/*      */     }
/*      */ 
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  397 */       ((DensityFunction)this.function.value()).fillArray(output, contextProvider);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public DensityFunction mapAll(DensityFunction.Visitor visitor) {
/*  403 */       return visitor.apply(new HolderHolder((Holder<DensityFunction>)new Holder.Direct(((DensityFunction)this.function.value()).mapAll(visitor))));
/*      */     }
/*      */ 
/*      */     
/*      */     public double minValue() {
/*  408 */       return this.function.isBound() ? ((DensityFunction)this.function.value()).minValue() : Double.NEGATIVE_INFINITY;
/*      */     }
/*      */ 
/*      */     
/*      */     public double maxValue() {
/*  413 */       return this.function.isBound() ? ((DensityFunction)this.function.value()).maxValue() : Double.POSITIVE_INFINITY;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  419 */       throw new UnsupportedOperationException("Calling .codec() on HolderHolder");
/*      */     } }
/*      */ 
/*      */   
/*      */   public static interface MarkerOrMarked
/*      */     extends DensityFunction {
/*      */     DensityFunctions.Marker.Type type();
/*      */     
/*      */     DensityFunction wrapped();
/*      */     
/*      */     default KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  430 */       return (KeyDispatchDataCodec)(type()).codec;
/*      */     }
/*      */ 
/*      */     
/*      */     default DensityFunction mapAll(DensityFunction.Visitor visitor) {
/*  435 */       return visitor.apply(new DensityFunctions.Marker(type(), wrapped().mapAll(visitor)));
/*      */     } }
/*      */   protected static final class Marker extends Record implements MarkerOrMarked { private final Type type; private final DensityFunction wrapped;
/*      */     
/*  439 */     protected Marker(Type type, DensityFunction wrapped) { this.type = type; this.wrapped = wrapped; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #439	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #439	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #439	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Marker;
/*  439 */       //   0	8	1	o	Ljava/lang/Object; } public Type type() { return this.type; } public DensityFunction wrapped() { return this.wrapped; }
/*      */     
/*  441 */     enum Type implements StringRepresentable { Interpolated("interpolated"),
/*  442 */       FlatCache("flat_cache"),
/*  443 */       Cache2D("cache_2d"),
/*  444 */       CacheOnce("cache_once"),
/*  445 */       CacheAllInCell("cache_all_in_cell");
/*      */       private final String name;
/*      */       private final KeyDispatchDataCodec<DensityFunctions.MarkerOrMarked> codec;
/*      */       
/*      */       Type(String name) {
/*  450 */         this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> new DensityFunctions.Marker(this, input), DensityFunctions.MarkerOrMarked::wrapped);
/*      */ 
/*      */         
/*  453 */         this.name = name;
/*      */       }
/*      */ 
/*      */       
/*      */       public String getSerializedName() {
/*  458 */         return this.name;
/*      */       } }
/*      */ 
/*      */ 
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  464 */       return this.wrapped.compute(context);
/*      */     }
/*      */ 
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  469 */       this.wrapped.fillArray(output, contextProvider);
/*      */     }
/*      */ 
/*      */     
/*      */     public double minValue() {
/*  474 */       return this.wrapped.minValue();
/*      */     }
/*      */     
/*      */     public double maxValue()
/*      */     {
/*  479 */       return this.wrapped.maxValue();
/*      */     } }
/*      */    enum Type implements StringRepresentable { Interpolated("interpolated"), FlatCache("flat_cache"), Cache2D("cache_2d"), CacheOnce("cache_once"), CacheAllInCell("cache_all_in_cell"); private final String name; private final KeyDispatchDataCodec<DensityFunctions.MarkerOrMarked> codec; Type(String name) { this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> new DensityFunctions.Marker(this, input), DensityFunctions.MarkerOrMarked::wrapped);
/*      */       this.name = name; } public String getSerializedName() { return this.name; } } protected static final class Noise extends Record implements DensityFunction { private final DensityFunction.NoiseHolder noise; @Deprecated private final double xzScale; private final double yScale; public static final MapCodec<Noise> DATA_CODEC;
/*  483 */     protected Noise(DensityFunction.NoiseHolder noise, @Deprecated double xzScale, double yScale) { this.noise = noise; this.xzScale = xzScale; this.yScale = yScale; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #483	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #483	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #483	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Noise;
/*  483 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction.NoiseHolder noise() { return this.noise; } @Deprecated public double xzScale() { return this.xzScale; } public double yScale() { return this.yScale; } static {
/*  484 */       DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.NoiseHolder.CODEC.fieldOf("noise").forGetter(Noise::noise), (App)Codec.DOUBLE.fieldOf("xz_scale").forGetter(Noise::xzScale), (App)Codec.DOUBLE.fieldOf("y_scale").forGetter(Noise::yScale)).apply((Applicative)i, Noise::new));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  490 */     public static final KeyDispatchDataCodec<Noise> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */ 
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  494 */       return this.noise.getValue(context.blockX() * this.xzScale, context.blockY() * this.yScale, context.blockZ() * this.xzScale);
/*      */     }
/*      */ 
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  499 */       contextProvider.fillAllDirectly(output, this);
/*      */     }
/*      */ 
/*      */     
/*      */     public DensityFunction mapAll(DensityFunction.Visitor visitor) {
/*  504 */       return visitor.apply(new Noise(visitor.visitNoise(this.noise), this.xzScale, this.yScale));
/*      */     }
/*      */ 
/*      */     
/*      */     public double minValue() {
/*  509 */       return -maxValue();
/*      */     }
/*      */ 
/*      */     
/*      */     public double maxValue() {
/*  514 */       return this.noise.maxValue();
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  519 */       return (KeyDispatchDataCodec)CODEC;
/*      */     } }
/*      */ 
/*      */   
/*      */   protected static final class EndIslandDensityFunction
/*      */     implements DensityFunction.SimpleFunction {
/*  525 */     public static final KeyDispatchDataCodec<EndIslandDensityFunction> CODEC = KeyDispatchDataCodec.of(MapCodec.unit(new EndIslandDensityFunction(0L)));
/*      */     
/*      */     private static final float ISLAND_THRESHOLD = -0.9F;
/*      */     private final SimplexNoise islandNoise;
/*      */     
/*      */     public EndIslandDensityFunction(long seed) {
/*  531 */       RandomSource islandRandom = new LegacyRandomSource(seed);
/*      */       
/*  533 */       islandRandom.consumeCount(17292);
/*  534 */       this.islandNoise = new SimplexNoise(islandRandom);
/*      */     }
/*      */     
/*      */     private static float getHeightValue(SimplexNoise islandNoise, int sectionX, int sectionZ) {
/*  538 */       int chunkX = sectionX / 2;
/*  539 */       int chunkZ = sectionZ / 2;
/*  540 */       int subSectionX = sectionX % 2;
/*  541 */       int subSectionZ = sectionZ % 2;
/*      */ 
/*      */       
/*  544 */       float doffs = 100.0F - Mth.sqrt((sectionX * sectionX + sectionZ * sectionZ)) * 8.0F;
/*  545 */       doffs = Mth.clamp(doffs, -100.0F, 80.0F);
/*      */ 
/*      */       
/*  548 */       for (int xo = -12; xo <= 12; xo++) {
/*  549 */         for (int zo = -12; zo <= 12; zo++) {
/*  550 */           long totalChunkX = (chunkX + xo);
/*  551 */           long totalChunkZ = (chunkZ + zo);
/*  552 */           if (totalChunkX * totalChunkX + totalChunkZ * totalChunkZ > 4096L && islandNoise.getValue(totalChunkX, totalChunkZ) < -0.8999999761581421D) {
/*  553 */             float islandSize = (Mth.abs((float)totalChunkX) * 3439.0F + Mth.abs((float)totalChunkZ) * 147.0F) % 13.0F + 9.0F;
/*  554 */             float xd = (subSectionX - xo * 2);
/*  555 */             float zd = (subSectionZ - zo * 2);
/*  556 */             float newDoffs = 100.0F - Mth.sqrt(xd * xd + zd * zd) * islandSize;
/*  557 */             newDoffs = Mth.clamp(newDoffs, -100.0F, 80.0F);
/*  558 */             doffs = Math.max(doffs, newDoffs);
/*      */           } 
/*      */         } 
/*      */       } 
/*      */       
/*  563 */       return doffs;
/*      */     }
/*      */ 
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  568 */       return (getHeightValue(this.islandNoise, context.blockX() / 8, context.blockZ() / 8) - 8.0D) / 128.0D;
/*      */     }
/*      */ 
/*      */     
/*      */     public double minValue() {
/*  573 */       return -0.84375D;
/*      */     }
/*      */ 
/*      */     
/*      */     public double maxValue() {
/*  578 */       return 0.5625D;
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  583 */       return (KeyDispatchDataCodec)CODEC;
/*      */     } }
/*      */   protected static final class WeirdScaledSampler extends Record implements TransformerWithContext { private final DensityFunction input; private final DensityFunction.NoiseHolder noise; private final RarityValueMapper rarityValueMapper; private static final MapCodec<WeirdScaledSampler> DATA_CODEC;
/*      */     
/*  587 */     protected WeirdScaledSampler(DensityFunction input, DensityFunction.NoiseHolder noise, RarityValueMapper rarityValueMapper) { this.input = input; this.noise = noise; this.rarityValueMapper = rarityValueMapper; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$WeirdScaledSampler;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #587	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$WeirdScaledSampler; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$WeirdScaledSampler;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #587	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$WeirdScaledSampler; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$WeirdScaledSampler;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #587	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$WeirdScaledSampler;
/*  587 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction input() { return this.input; } public DensityFunction.NoiseHolder noise() { return this.noise; } public RarityValueMapper rarityValueMapper() { return this.rarityValueMapper; } static {
/*  588 */       DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(WeirdScaledSampler::input), (App)DensityFunction.NoiseHolder.CODEC.fieldOf("noise").forGetter(WeirdScaledSampler::noise), (App)RarityValueMapper.CODEC.fieldOf("rarity_value_mapper").forGetter(WeirdScaledSampler::rarityValueMapper)).apply((Applicative)i, WeirdScaledSampler::new));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  594 */     public static final KeyDispatchDataCodec<WeirdScaledSampler> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */ 
/*      */     
/*      */     public enum RarityValueMapper
/*      */       implements StringRepresentable
/*      */     {
/*  600 */       TYPE1("type_1", NoiseRouterData.QuantizedSpaghettiRarity::getSpaghettiRarity3D, 2.0D),
/*  601 */       TYPE2("type_2", NoiseRouterData.QuantizedSpaghettiRarity::getSphaghettiRarity2D, 3.0D);
/*      */ 
/*      */       
/*  604 */       public static final Codec<RarityValueMapper> CODEC = (Codec<RarityValueMapper>)StringRepresentable.fromEnum(RarityValueMapper::values);
/*      */       
/*      */       private final String name;
/*      */       private final Double2DoubleFunction mapper;
/*      */       private final double maxRarity;
/*      */       
/*      */       RarityValueMapper(String name, Double2DoubleFunction mapper, double maxRarity) {
/*  611 */         this.name = name;
/*  612 */         this.mapper = mapper;
/*  613 */         this.maxRarity = maxRarity;
/*      */       }
/*      */ 
/*      */       
/*      */       public String getSerializedName() {
/*  618 */         return this.name;
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*      */     public double transform(DensityFunction.FunctionContext context, double input) {
/*  624 */       double rarity = this.rarityValueMapper.mapper.get(input);
/*  625 */       return rarity * Math.abs(this.noise.getValue(
/*  626 */             context.blockX() / rarity, 
/*  627 */             context.blockY() / rarity, 
/*  628 */             context.blockZ() / rarity));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public DensityFunction mapAll(DensityFunction.Visitor visitor) {
/*  634 */       return visitor.apply(new WeirdScaledSampler(this.input.mapAll(visitor), visitor.visitNoise(this.noise), this.rarityValueMapper));
/*      */     }
/*      */ 
/*      */     
/*      */     public double minValue() {
/*  639 */       return 0.0D;
/*      */     }
/*      */ 
/*      */     
/*      */     public double maxValue() {
/*  644 */       return this.rarityValueMapper.maxRarity * this.noise.maxValue();
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec()
/*      */     {
/*  649 */       return (KeyDispatchDataCodec)CODEC;
/*      */     } }
/*      */    public enum RarityValueMapper implements StringRepresentable { TYPE1("type_1", NoiseRouterData.QuantizedSpaghettiRarity::getSpaghettiRarity3D, 2.0D), TYPE2("type_2", NoiseRouterData.QuantizedSpaghettiRarity::getSphaghettiRarity2D, 3.0D); public static final Codec<RarityValueMapper> CODEC = (Codec<RarityValueMapper>)StringRepresentable.fromEnum(RarityValueMapper::values); private final String name; private final Double2DoubleFunction mapper; private final double maxRarity; RarityValueMapper(String name, Double2DoubleFunction mapper, double maxRarity) { this.name = name; this.mapper = mapper;
/*      */       this.maxRarity = maxRarity; } public String getSerializedName() { return this.name; } } protected static final class ShiftedNoise extends Record implements DensityFunction { private final DensityFunction shiftX; private final DensityFunction shiftY; private final DensityFunction shiftZ; private final double xzScale; private final double yScale; private final DensityFunction.NoiseHolder noise; private static final MapCodec<ShiftedNoise> DATA_CODEC;
/*  653 */     protected ShiftedNoise(DensityFunction shiftX, DensityFunction shiftY, DensityFunction shiftZ, double xzScale, double yScale, DensityFunction.NoiseHolder noise) { this.shiftX = shiftX; this.shiftY = shiftY; this.shiftZ = shiftZ; this.xzScale = xzScale; this.yScale = yScale; this.noise = noise; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #653	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #653	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #653	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftedNoise;
/*  653 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction shiftX() { return this.shiftX; } public DensityFunction shiftY() { return this.shiftY; } public DensityFunction shiftZ() { return this.shiftZ; } public double xzScale() { return this.xzScale; } public double yScale() { return this.yScale; } public DensityFunction.NoiseHolder noise() { return this.noise; }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static {
/*  661 */       DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_x").forGetter(ShiftedNoise::shiftX), (App)DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_y").forGetter(ShiftedNoise::shiftY), (App)DensityFunction.HOLDER_HELPER_CODEC.fieldOf("shift_z").forGetter(ShiftedNoise::shiftZ), (App)Codec.DOUBLE.fieldOf("xz_scale").forGetter(ShiftedNoise::xzScale), (App)Codec.DOUBLE.fieldOf("y_scale").forGetter(ShiftedNoise::yScale), (App)DensityFunction.NoiseHolder.CODEC.fieldOf("noise").forGetter(ShiftedNoise::noise)).apply((Applicative)i, ShiftedNoise::new));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  670 */     public static final KeyDispatchDataCodec<ShiftedNoise> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */ 
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  674 */       double x = context.blockX() * this.xzScale + this.shiftX.compute(context);
/*  675 */       double y = context.blockY() * this.yScale + this.shiftY.compute(context);
/*  676 */       double z = context.blockZ() * this.xzScale + this.shiftZ.compute(context);
/*  677 */       return this.noise.getValue(x, y, z);
/*      */     }
/*      */ 
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  682 */       contextProvider.fillAllDirectly(output, this);
/*      */     }
/*      */ 
/*      */     
/*      */     public DensityFunction mapAll(DensityFunction.Visitor visitor) {
/*  687 */       return visitor.apply(new ShiftedNoise(
/*  688 */             this.shiftX.mapAll(visitor), 
/*  689 */             this.shiftY.mapAll(visitor), 
/*  690 */             this.shiftZ.mapAll(visitor), this.xzScale, this.yScale, 
/*      */ 
/*      */             
/*  693 */             visitor.visitNoise(this.noise)));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public double minValue() {
/*  699 */       return -maxValue();
/*      */     }
/*      */ 
/*      */     
/*      */     public double maxValue() {
/*  704 */       return this.noise.maxValue();
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  709 */       return (KeyDispatchDataCodec)CODEC;
/*      */     } }
/*      */   private static final class RangeChoice extends Record implements DensityFunction { private final DensityFunction input; private final double minInclusive; private final double maxExclusive; private final DensityFunction whenInRange; private final DensityFunction whenOutOfRange; public static final MapCodec<RangeChoice> DATA_CODEC;
/*      */     
/*  713 */     private RangeChoice(DensityFunction input, double minInclusive, double maxExclusive, DensityFunction whenInRange, DensityFunction whenOutOfRange) { this.input = input; this.minInclusive = minInclusive; this.maxExclusive = maxExclusive; this.whenInRange = whenInRange; this.whenOutOfRange = whenOutOfRange; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #713	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #713	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #713	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$RangeChoice;
/*  713 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction input() { return this.input; } public double minInclusive() { return this.minInclusive; } public double maxExclusive() { return this.maxExclusive; } public DensityFunction whenInRange() { return this.whenInRange; } public DensityFunction whenOutOfRange() { return this.whenOutOfRange; } static {
/*  714 */       DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.HOLDER_HELPER_CODEC.fieldOf("input").forGetter(RangeChoice::input), (App)DensityFunctions.NOISE_VALUE_CODEC.fieldOf("min_inclusive").forGetter(RangeChoice::minInclusive), (App)DensityFunctions.NOISE_VALUE_CODEC.fieldOf("max_exclusive").forGetter(RangeChoice::maxExclusive), (App)DensityFunction.HOLDER_HELPER_CODEC.fieldOf("when_in_range").forGetter(RangeChoice::whenInRange), (App)DensityFunction.HOLDER_HELPER_CODEC.fieldOf("when_out_of_range").forGetter(RangeChoice::whenOutOfRange)).apply((Applicative)i, RangeChoice::new));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  722 */     public static final KeyDispatchDataCodec<RangeChoice> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */ 
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  726 */       double inputValue = this.input.compute(context);
/*  727 */       if (inputValue >= this.minInclusive && inputValue < this.maxExclusive) {
/*  728 */         return this.whenInRange.compute(context);
/*      */       }
/*  730 */       return this.whenOutOfRange.compute(context);
/*      */     }
/*      */ 
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  735 */       this.input.fillArray(output, contextProvider);
/*  736 */       for (int i = 0; i < output.length; i++) {
/*  737 */         double v = output[i];
/*  738 */         if (v >= this.minInclusive && v < this.maxExclusive) {
/*  739 */           output[i] = this.whenInRange.compute(contextProvider.forIndex(i));
/*      */         } else {
/*  741 */           output[i] = this.whenOutOfRange.compute(contextProvider.forIndex(i));
/*      */         } 
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public DensityFunction mapAll(DensityFunction.Visitor visitor) {
/*  748 */       return visitor.apply(new RangeChoice(this.input.mapAll(visitor), this.minInclusive, this.maxExclusive, this.whenInRange.mapAll(visitor), this.whenOutOfRange.mapAll(visitor)));
/*      */     }
/*      */ 
/*      */     
/*      */     public double minValue() {
/*  753 */       return Math.min(this.whenInRange.minValue(), this.whenOutOfRange.minValue());
/*      */     }
/*      */ 
/*      */     
/*      */     public double maxValue() {
/*  758 */       return Math.max(this.whenInRange.maxValue(), this.whenOutOfRange.maxValue());
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  763 */       return (KeyDispatchDataCodec)CODEC;
/*      */     } }
/*      */ 
/*      */   
/*      */   static interface ShiftNoise
/*      */     extends DensityFunction {
/*      */     DensityFunction.NoiseHolder offsetNoise();
/*      */     
/*      */     default double minValue() {
/*  772 */       return -maxValue();
/*      */     }
/*      */ 
/*      */     
/*      */     default double maxValue() {
/*  777 */       return offsetNoise().maxValue() * 4.0D;
/*      */     }
/*      */     
/*      */     default double compute(double localX, double localY, double localZ) {
/*  781 */       return offsetNoise().getValue(localX * 0.25D, localY * 0.25D, localZ * 0.25D) * 4.0D;
/*      */     }
/*      */ 
/*      */     
/*      */     default void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  786 */       contextProvider.fillAllDirectly(output, this);
/*      */     } }
/*      */   protected static final class ShiftA extends Record implements ShiftNoise { private final DensityFunction.NoiseHolder offsetNoise;
/*      */     
/*  790 */     protected ShiftA(DensityFunction.NoiseHolder offsetNoise) { this.offsetNoise = offsetNoise; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftA;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #790	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftA; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftA;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #790	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftA; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftA;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #790	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftA;
/*  790 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction.NoiseHolder offsetNoise() { return this.offsetNoise; }
/*  791 */      private static final KeyDispatchDataCodec<ShiftA> CODEC = DensityFunctions.singleArgumentCodec(DensityFunction.NoiseHolder.CODEC, ShiftA::new, ShiftA::offsetNoise);
/*      */ 
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  795 */       return compute(context.blockX(), 0.0D, context.blockZ());
/*      */     }
/*      */ 
/*      */     
/*      */     public DensityFunction mapAll(DensityFunction.Visitor visitor) {
/*  800 */       return visitor.apply(new ShiftA(visitor.visitNoise(this.offsetNoise)));
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  805 */       return (KeyDispatchDataCodec)CODEC;
/*      */     } }
/*      */   protected static final class ShiftB extends Record implements ShiftNoise { private final DensityFunction.NoiseHolder offsetNoise;
/*      */     
/*  809 */     protected ShiftB(DensityFunction.NoiseHolder offsetNoise) { this.offsetNoise = offsetNoise; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #809	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #809	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #809	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$ShiftB;
/*  809 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction.NoiseHolder offsetNoise() { return this.offsetNoise; }
/*  810 */      private static final KeyDispatchDataCodec<ShiftB> CODEC = DensityFunctions.singleArgumentCodec(DensityFunction.NoiseHolder.CODEC, ShiftB::new, ShiftB::offsetNoise);
/*      */ 
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  814 */       return compute(context.blockZ(), context.blockX(), 0.0D);
/*      */     }
/*      */ 
/*      */     
/*      */     public DensityFunction mapAll(DensityFunction.Visitor visitor) {
/*  819 */       return visitor.apply(new ShiftB(visitor.visitNoise(this.offsetNoise)));
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  824 */       return (KeyDispatchDataCodec)CODEC;
/*      */     } }
/*      */   protected static final class Shift extends Record implements ShiftNoise { private final DensityFunction.NoiseHolder offsetNoise;
/*      */     
/*  828 */     protected Shift(DensityFunction.NoiseHolder offsetNoise) { this.offsetNoise = offsetNoise; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Shift;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #828	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Shift; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Shift;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #828	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Shift; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Shift;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #828	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Shift;
/*  828 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction.NoiseHolder offsetNoise() { return this.offsetNoise; }
/*  829 */      private static final KeyDispatchDataCodec<Shift> CODEC = DensityFunctions.singleArgumentCodec(DensityFunction.NoiseHolder.CODEC, Shift::new, Shift::offsetNoise);
/*      */ 
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/*  833 */       return compute(context.blockX(), context.blockY(), context.blockZ());
/*      */     }
/*      */ 
/*      */     
/*      */     public DensityFunction mapAll(DensityFunction.Visitor visitor) {
/*  838 */       return visitor.apply(new Shift(visitor.visitNoise(this.offsetNoise)));
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  843 */       return (KeyDispatchDataCodec)CODEC;
/*      */     } }
/*      */   private static final class BlendDensity extends Record implements TransformerWithContext { private final DensityFunction input;
/*      */     
/*  847 */     private BlendDensity(DensityFunction input) { this.input = input; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$BlendDensity;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #847	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$BlendDensity; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$BlendDensity;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #847	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$BlendDensity; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$BlendDensity;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #847	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$BlendDensity;
/*  847 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction input() { return this.input; }
/*  848 */      private static final KeyDispatchDataCodec<BlendDensity> CODEC = DensityFunctions.singleFunctionArgumentCodec(BlendDensity::new, BlendDensity::input);
/*      */ 
/*      */     
/*      */     public double transform(DensityFunction.FunctionContext context, double input) {
/*  852 */       return context.getBlender().blendDensity(context, input);
/*      */     }
/*      */ 
/*      */     
/*      */     public DensityFunction mapAll(DensityFunction.Visitor visitor) {
/*  857 */       return visitor.apply(new BlendDensity(this.input.mapAll(visitor)));
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public double minValue() {
/*  863 */       return Double.NEGATIVE_INFINITY;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public double maxValue() {
/*  869 */       return Double.POSITIVE_INFINITY;
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  874 */       return (KeyDispatchDataCodec)CODEC;
/*      */     } }
/*      */   protected static final class Clamp extends Record implements PureTransformer { private final DensityFunction input; private final double minValue; private final double maxValue; private static final MapCodec<Clamp> DATA_CODEC;
/*      */     
/*  878 */     protected Clamp(DensityFunction input, double minValue, double maxValue) { this.input = input; this.minValue = minValue; this.maxValue = maxValue; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #878	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #878	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #878	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Clamp;
/*  878 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction input() { return this.input; } public double minValue() { return this.minValue; } public double maxValue() { return this.maxValue; } static {
/*  879 */       DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.DIRECT_CODEC.fieldOf("input").forGetter(Clamp::input), (App)DensityFunctions.NOISE_VALUE_CODEC.fieldOf("min").forGetter(Clamp::minValue), (App)DensityFunctions.NOISE_VALUE_CODEC.fieldOf("max").forGetter(Clamp::maxValue)).apply((Applicative)i, Clamp::new));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  885 */     public static final KeyDispatchDataCodec<Clamp> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */ 
/*      */     
/*      */     public double transform(double input) {
/*  889 */       return Mth.clamp(input, this.minValue, this.maxValue);
/*      */     }
/*      */ 
/*      */     
/*      */     public DensityFunction mapAll(DensityFunction.Visitor visitor) {
/*  894 */       return new Clamp(this.input.mapAll(visitor), this.minValue, this.maxValue);
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/*  899 */       return (KeyDispatchDataCodec)CODEC;
/*      */     } }
/*      */   protected static final class Mapped extends Record implements PureTransformer { private final Type type; private final DensityFunction input; private final double minValue; private final double maxValue;
/*      */     
/*  903 */     protected Mapped(Type type, DensityFunction input, double minValue, double maxValue) { this.type = type; this.input = input; this.minValue = minValue; this.maxValue = maxValue; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #903	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #903	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #903	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Mapped;
/*  903 */       //   0	8	1	o	Ljava/lang/Object; } public Type type() { return this.type; } public DensityFunction input() { return this.input; } public double minValue() { return this.minValue; } public double maxValue() { return this.maxValue; }
/*      */      public static Mapped create(Type type, DensityFunction input) {
/*  905 */       double minValue = input.minValue();
/*  906 */       double maxValue = input.maxValue();
/*  907 */       double minImage = transform(type, minValue);
/*  908 */       double maxImage = transform(type, maxValue);
/*  909 */       if (type == Type.INVERT) {
/*  910 */         if (minValue < 0.0D && maxValue > 0.0D) {
/*  911 */           return new Mapped(type, input, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
/*      */         }
/*      */         
/*  914 */         return new Mapped(type, input, maxImage, minImage);
/*      */       } 
/*  916 */       if (type == Type.ABS || type == Type.SQUARE)
/*      */       {
/*  918 */         return new Mapped(type, input, Math.max(0.0D, minValue), Math.max(minImage, maxImage));
/*      */       }
/*      */       
/*  921 */       return new Mapped(type, input, minImage, maxImage);
/*      */     }
/*      */     
/*      */     enum Type implements StringRepresentable {
/*  925 */       ABS("abs"),
/*  926 */       SQUARE("square"),
/*  927 */       CUBE("cube"),
/*  928 */       HALF_NEGATIVE("half_negative"),
/*  929 */       QUARTER_NEGATIVE("quarter_negative"),
/*  930 */       INVERT("invert"),
/*  931 */       SQUEEZE("squeeze");
/*      */       private final String name;
/*      */       private final KeyDispatchDataCodec<DensityFunctions.Mapped> codec;
/*      */       
/*      */       Type(String name) {
/*  936 */         this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> DensityFunctions.Mapped.create(this, input), DensityFunctions.Mapped::input);
/*      */ 
/*      */         
/*  939 */         this.name = name;
/*      */       }
/*      */ 
/*      */       
/*      */       public String getSerializedName() {
/*  944 */         return this.name;
/*      */       } }
/*      */     
/*      */     private static double transform(Type type, double input) {
/*      */       double c;
/*  949 */       switch (type.ordinal()) { default: throw new MatchException(null, null);
/*      */         case 0: 
/*      */         case 1: 
/*      */         case 2: 
/*  953 */         case 3: if (input > 0.0D);
/*  954 */         case 4: if (input > 0.0D);
/*      */         case 5:
/*      */         
/*      */         case 6:
/*  958 */           c = Mth.clamp(input, -1.0D, 1.0D); }
/*  959 */        return c / 2.0D - c * c * c / 24.0D;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public double transform(double input) {
/*  966 */       return transform(this.type, input);
/*      */     }
/*      */ 
/*      */     
/*      */     public Mapped mapAll(DensityFunction.Visitor visitor) {
/*  971 */       return create(this.type, this.input.mapAll(visitor));
/*      */     }
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec()
/*      */     {
/*  976 */       return (KeyDispatchDataCodec)this.type.codec;
/*      */     } }
/*      */    enum Type implements StringRepresentable { ABS("abs"), SQUARE("square"), CUBE("cube"), HALF_NEGATIVE("half_negative"), QUARTER_NEGATIVE("quarter_negative"), INVERT("invert"),
/*      */     SQUEEZE("squeeze"); private final String name; private final KeyDispatchDataCodec<DensityFunctions.Mapped> codec; Type(String name) { this.codec = DensityFunctions.singleFunctionArgumentCodec(input -> DensityFunctions.Mapped.create(this, input), DensityFunctions.Mapped::input);
/*      */       this.name = name; } public String getSerializedName() { return this.name; } }
/*  981 */   static interface TwoArgumentSimpleFunction extends DensityFunction { public static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*      */     
/*      */     static TwoArgumentSimpleFunction create(Type type, DensityFunction argument1, DensityFunction argument2) {
/*  984 */       double min1 = argument1.minValue();
/*  985 */       double min2 = argument2.minValue();
/*      */       
/*  987 */       double max1 = argument1.maxValue();
/*  988 */       double max2 = argument2.maxValue();
/*      */       
/*  990 */       if (type == Type.MIN || type == Type.MAX) {
/*  991 */         boolean firstAlwaysBiggerThanSecond = (min1 >= max2);
/*  992 */         boolean secondAlwaysBiggerThanFirst = (min2 >= max1);
/*  993 */         if (firstAlwaysBiggerThanSecond || secondAlwaysBiggerThanFirst) {
/*  994 */           LOGGER.warn("Creating a {} function between two non-overlapping inputs: {} and {}", new Object[] { type, argument1, argument2 });
/*      */         }
/*      */       } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1004 */       switch (type.ordinal()) { default: throw new MatchException(null, null);
/*      */         case 0: 
/*      */         case 3: 
/*      */         case 2: 
/*      */         case 1:
/* 1009 */           if (min1 > 0.0D && min2 > 0.0D);
/*      */ 
/*      */           
/* 1012 */           if (max1 < 0.0D && max2 < 0.0D);
/*      */           break; }
/*      */       
/* 1015 */       double minValue = Math.min(min1 * max2, max1 * min2);
/*      */ 
/*      */ 
/*      */       
/* 1019 */       switch (type.ordinal()) { default: throw new MatchException(null, null);
/*      */         case 0: 
/*      */         case 3: 
/*      */         case 2: 
/*      */         case 1:
/* 1024 */           if (min1 > 0.0D && min2 > 0.0D);
/*      */ 
/*      */           
/* 1027 */           if (max1 < 0.0D && max2 < 0.0D);
/*      */           break; }
/*      */       
/* 1030 */       double maxValue = Math.max(min1 * min2, max1 * max2);
/*      */ 
/*      */ 
/*      */       
/* 1034 */       if (type == Type.MUL || type == Type.ADD) {
/* 1035 */         if (argument1 instanceof DensityFunctions.Constant) { DensityFunctions.Constant constant = (DensityFunctions.Constant)argument1;
/* 1036 */           return new DensityFunctions.MulOrAdd((type == Type.ADD) ? DensityFunctions.MulOrAdd.Type.ADD : DensityFunctions.MulOrAdd.Type.MUL, argument2, minValue, maxValue, constant.value); }
/*      */         
/* 1038 */         if (argument2 instanceof DensityFunctions.Constant) { DensityFunctions.Constant constant = (DensityFunctions.Constant)argument2;
/* 1039 */           return new DensityFunctions.MulOrAdd((type == Type.ADD) ? DensityFunctions.MulOrAdd.Type.ADD : DensityFunctions.MulOrAdd.Type.MUL, argument1, minValue, maxValue, constant.value); }
/*      */       
/*      */       } 
/*      */       
/* 1043 */       return new DensityFunctions.Ap2(type, argument1, argument2, minValue, maxValue);
/*      */     } Type type();
/*      */     DensityFunction argument1();
/*      */     DensityFunction argument2();
/* 1047 */     public enum Type implements StringRepresentable { ADD("add"),
/* 1048 */       MUL("mul"),
/* 1049 */       MIN("min"),
/* 1050 */       MAX("max"); private final KeyDispatchDataCodec<DensityFunctions.TwoArgumentSimpleFunction> codec; private final String name;
/*      */       
/*      */       Type(String name) {
/* 1053 */         this.codec = DensityFunctions.doubleFunctionArgumentCodec((argument1, argument2) -> DensityFunctions.TwoArgumentSimpleFunction.create(this, argument1, argument2), DensityFunctions.TwoArgumentSimpleFunction::argument1, DensityFunctions.TwoArgumentSimpleFunction::argument2);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1062 */         this.name = name;
/*      */       }
/*      */ 
/*      */       
/*      */       public String getSerializedName() {
/* 1067 */         return this.name;
/*      */       } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     default KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 1079 */       return (KeyDispatchDataCodec)(type()).codec;
/*      */     } } public enum Type implements StringRepresentable { ADD("add"), MUL("mul"), MIN("min"), MAX("max"); private final KeyDispatchDataCodec<DensityFunctions.TwoArgumentSimpleFunction> codec; private final String name; Type(String name) { this.codec = DensityFunctions.doubleFunctionArgumentCodec((argument1, argument2) -> DensityFunctions.TwoArgumentSimpleFunction.create(this, argument1, argument2), DensityFunctions.TwoArgumentSimpleFunction::argument1, DensityFunctions.TwoArgumentSimpleFunction::argument2);
/*      */       this.name = name; } public String getSerializedName() { return this.name; } }
/*      */   private static final class MulOrAdd extends Record implements TwoArgumentSimpleFunction, PureTransformer { private final Type specificType; private final DensityFunction input; private final double minValue; private final double maxValue; private final double argument;
/* 1083 */     private MulOrAdd(Type specificType, DensityFunction input, double minValue, double maxValue, double argument) { this.specificType = specificType; this.input = input; this.minValue = minValue; this.maxValue = maxValue; this.argument = argument; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1083	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1083	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1083	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$MulOrAdd;
/* 1083 */       //   0	8	1	o	Ljava/lang/Object; } public Type specificType() { return this.specificType; } public DensityFunction input() { return this.input; } public double minValue() { return this.minValue; } public double maxValue() { return this.maxValue; } public double argument() { return this.argument; }
/*      */     
/* 1085 */     enum Type { MUL,
/* 1086 */       ADD; }
/*      */ 
/*      */ 
/*      */     
/*      */     public DensityFunctions.TwoArgumentSimpleFunction.Type type() {
/* 1091 */       return (this.specificType == Type.MUL) ? DensityFunctions.TwoArgumentSimpleFunction.Type.MUL : DensityFunctions.TwoArgumentSimpleFunction.Type.ADD;
/*      */     }
/*      */ 
/*      */     
/*      */     public DensityFunction argument1() {
/* 1096 */       return DensityFunctions.constant(this.argument);
/*      */     }
/*      */ 
/*      */     
/*      */     public DensityFunction argument2() {
/* 1101 */       return this.input;
/*      */     }
/*      */ 
/*      */     
/*      */     public double transform(double input) {
/* 1106 */       switch (this.specificType.ordinal()) { default: throw new MatchException(null, null);case 0: case 1: break; }  return 
/*      */         
/* 1108 */         input + this.argument;
/*      */     }
/*      */ 
/*      */     
/*      */     public DensityFunction mapAll(DensityFunction.Visitor visitor) {
/*      */       double minValue, maxValue;
/* 1114 */       DensityFunction function = this.input.mapAll(visitor);
/* 1115 */       double min = function.minValue();
/* 1116 */       double max = function.maxValue();
/*      */ 
/*      */       
/* 1119 */       if (this.specificType == Type.ADD) {
/* 1120 */         minValue = min + this.argument;
/* 1121 */         maxValue = max + this.argument;
/* 1122 */       } else if (this.argument >= 0.0D) {
/* 1123 */         minValue = min * this.argument;
/* 1124 */         maxValue = max * this.argument;
/*      */       } else {
/* 1126 */         minValue = max * this.argument;
/* 1127 */         maxValue = min * this.argument;
/*      */       } 
/* 1129 */       return new MulOrAdd(this.specificType, function, minValue, maxValue, this.argument);
/*      */     } }
/*      */   enum Type { MUL, ADD; }
/*      */   private static final class Ap2 extends Record implements TwoArgumentSimpleFunction { private final DensityFunctions.TwoArgumentSimpleFunction.Type type; private final DensityFunction argument1; private final DensityFunction argument2; private final double minValue; private final double maxValue;
/* 1133 */     private Ap2(DensityFunctions.TwoArgumentSimpleFunction.Type type, DensityFunction argument1, DensityFunction argument2, double minValue, double maxValue) { this.type = type; this.argument1 = argument1; this.argument2 = argument2; this.minValue = minValue; this.maxValue = maxValue; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1133	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1133	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1133	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Ap2;
/* 1133 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunctions.TwoArgumentSimpleFunction.Type type() { return this.type; } public DensityFunction argument1() { return this.argument1; } public DensityFunction argument2() { return this.argument2; }
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/* 1136 */       double v1 = this.argument1.compute(context);
/* 1137 */       switch (this.type.ordinal()) { default: throw new MatchException(null, null);
/*      */         case 0: 
/*      */         case 1:
/* 1140 */           if (v1 == 0.0D);
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 2:
/* 1146 */           if (v1 < this.argument2.minValue());
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 3:
/* 1152 */           if (v1 > this.argument2.maxValue());
/*      */           break; }
/*      */       
/* 1155 */       return Math.max(v1, this.argument2.compute(context));
/*      */     }
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*      */       double[] v2;
/*      */       int i;
/*      */       double min, max;
/*      */       int j, k;
/* 1162 */       this.argument1.fillArray(output, contextProvider);
/* 1163 */       switch (this.type.ordinal()) {
/*      */         case 0:
/* 1165 */           v2 = new double[output.length];
/* 1166 */           this.argument2.fillArray(v2, contextProvider);
/* 1167 */           for (j = 0; j < output.length; j++) {
/* 1168 */             output[j] = output[j] + v2[j];
/*      */           }
/*      */           break;
/*      */         case 1:
/* 1172 */           for (i = 0; i < output.length; i++) {
/* 1173 */             double v = output[i];
/* 1174 */             output[i] = (v == 0.0D) ? 0.0D : (v * this.argument2.compute(contextProvider.forIndex(i)));
/*      */           } 
/*      */           break;
/*      */         case 2:
/* 1178 */           min = this.argument2.minValue();
/* 1179 */           for (k = 0; k < output.length; k++) {
/* 1180 */             double v = output[k];
/* 1181 */             output[k] = (v < min) ? v : Math.min(v, this.argument2.compute(contextProvider.forIndex(k)));
/*      */           } 
/*      */           break;
/*      */         case 3:
/* 1185 */           max = this.argument2.maxValue();
/* 1186 */           for (k = 0; k < output.length; k++) {
/* 1187 */             double v = output[k];
/* 1188 */             output[k] = (v > max) ? v : Math.max(v, this.argument2.compute(contextProvider.forIndex(k)));
/*      */           } 
/*      */           break;
/*      */       } 
/*      */     }
/*      */ 
/*      */     
/*      */     public DensityFunction mapAll(DensityFunction.Visitor visitor) {
/* 1196 */       return visitor.apply(DensityFunctions.TwoArgumentSimpleFunction.create(this.type, this.argument1.mapAll(visitor), this.argument2.mapAll(visitor)));
/*      */     }
/*      */ 
/*      */     
/*      */     public double minValue() {
/* 1201 */       return this.minValue;
/*      */     }
/*      */ 
/*      */     
/*      */     public double maxValue() {
/* 1206 */       return this.maxValue;
/*      */     } }
/*      */   public static final class Spline extends Record implements DensityFunction { private final CubicSpline<Point, Coordinate> spline;
/*      */     
/* 1210 */     public Spline(CubicSpline<Point, Coordinate> spline) { this.spline = spline; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1210	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1210	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1210	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline;
/* 1210 */       //   0	8	1	o	Ljava/lang/Object; } public CubicSpline<Point, Coordinate> spline() { return this.spline; }
/* 1211 */      private static final Codec<CubicSpline<Point, Coordinate>> SPLINE_CODEC = CubicSpline.codec(Coordinate.CODEC);
/* 1212 */     private static final MapCodec<Spline> DATA_CODEC = SPLINE_CODEC.fieldOf("spline").xmap(Spline::new, Spline::spline);
/*      */     
/* 1214 */     public static final KeyDispatchDataCodec<Spline> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */ 
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/* 1218 */       return this.spline.apply(new Point(context));
/*      */     }
/*      */ 
/*      */     
/*      */     public double minValue() {
/* 1223 */       return this.spline.minValue();
/*      */     }
/*      */ 
/*      */     
/*      */     public double maxValue() {
/* 1228 */       return this.spline.maxValue();
/*      */     }
/*      */ 
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 1233 */       contextProvider.fillAllDirectly(output, this);
/*      */     }
/*      */ 
/*      */     
/*      */     public DensityFunction mapAll(DensityFunction.Visitor visitor) {
/* 1238 */       return visitor.apply(new Spline(this.spline.mapAll(c -> c.mapAll(visitor))));
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 1243 */       return (KeyDispatchDataCodec)CODEC;
/*      */     }
/*      */     public static final class Coordinate extends Record implements net.minecraft.util.BoundedFloatFunction<Point> { private final Holder<DensityFunction> function;
/* 1246 */       public Coordinate(Holder<DensityFunction> function) { this.function = function; } public final int hashCode() { // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;)I
/*      */         //   6: ireturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #1246	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate; } public final boolean equals(Object o) { // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: aload_1
/*      */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;Ljava/lang/Object;)Z
/*      */         //   7: ireturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #1246	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;
/* 1246 */         //   0	8	1	o	Ljava/lang/Object; } public Holder<DensityFunction> function() { return this.function; }
/* 1247 */        public static final Codec<Coordinate> CODEC = DensityFunction.CODEC.xmap(Coordinate::new, Coordinate::function);
/*      */ 
/*      */ 
/*      */       
/*      */       public String toString() {
/* 1252 */         Optional<ResourceKey<DensityFunction>> key = this.function.unwrapKey();
/* 1253 */         if (key.isPresent()) {
/* 1254 */           ResourceKey<DensityFunction> name = key.get();
/* 1255 */           if (name == NoiseRouterData.CONTINENTS) {
/* 1256 */             return "continents";
/*      */           }
/* 1258 */           if (name == NoiseRouterData.EROSION) {
/* 1259 */             return "erosion";
/*      */           }
/* 1261 */           if (name == NoiseRouterData.RIDGES) {
/* 1262 */             return "weirdness";
/*      */           }
/* 1264 */           if (name == NoiseRouterData.RIDGES_FOLDED) {
/* 1265 */             return "ridges";
/*      */           }
/*      */         } 
/* 1268 */         return "Coordinate[" + String.valueOf(this.function) + "]";
/*      */       }
/*      */ 
/*      */       
/*      */       public float apply(DensityFunctions.Spline.Point point) {
/* 1273 */         return (float)((DensityFunction)this.function.value()).compute(point.context());
/*      */       }
/*      */ 
/*      */       
/*      */       public float minValue() {
/* 1278 */         return this.function.isBound() ? (float)((DensityFunction)this.function.value()).minValue() : Float.NEGATIVE_INFINITY;
/*      */       }
/*      */ 
/*      */       
/*      */       public float maxValue() {
/* 1283 */         return this.function.isBound() ? (float)((DensityFunction)this.function.value()).maxValue() : Float.POSITIVE_INFINITY;
/*      */       }
/*      */       
/*      */       public Coordinate mapAll(DensityFunction.Visitor visitor) {
/* 1287 */         return new Coordinate((Holder<DensityFunction>)new Holder.Direct(((DensityFunction)this.function.value()).mapAll(visitor)));
/*      */       } }
/*      */     public static final class Point extends Record { private final DensityFunction.FunctionContext context;
/*      */       
/* 1291 */       public Point(DensityFunction.FunctionContext context) { this.context = context; } public final String toString() { // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;)Ljava/lang/String;
/*      */         //   6: areturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #1291	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point; } public final int hashCode() { // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;)I
/*      */         //   6: ireturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #1291	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point; } public final boolean equals(Object o) { // Byte code:
/*      */         //   0: aload_0
/*      */         //   1: aload_1
/*      */         //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;Ljava/lang/Object;)Z
/*      */         //   7: ireturn
/*      */         // Line number table:
/*      */         //   Java source line number -> byte code offset
/*      */         //   #1291	-> 0
/*      */         // Local variable table:
/*      */         //   start	length	slot	name	descriptor
/*      */         //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;
/* 1291 */         //   0	8	1	o	Ljava/lang/Object; } public DensityFunction.FunctionContext context() { return this.context; } } } public static final class Coordinate extends Record implements net.minecraft.util.BoundedFloatFunction<Spline.Point> { private final Holder<DensityFunction> function; public Coordinate(Holder<DensityFunction> function) { this.function = function; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1246	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1246	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Coordinate;
/* 1291 */       //   0	8	1	o	Ljava/lang/Object; } public Holder<DensityFunction> function() { return this.function; } public static final Codec<Coordinate> CODEC = DensityFunction.CODEC.xmap(Coordinate::new, Coordinate::function); public String toString() { Optional<ResourceKey<DensityFunction>> key = this.function.unwrapKey(); if (key.isPresent()) { ResourceKey<DensityFunction> name = key.get(); if (name == NoiseRouterData.CONTINENTS) return "continents";  if (name == NoiseRouterData.EROSION) return "erosion";  if (name == NoiseRouterData.RIDGES) return "weirdness";  if (name == NoiseRouterData.RIDGES_FOLDED) return "ridges";  }  return "Coordinate[" + String.valueOf(this.function) + "]"; } public float apply(DensityFunctions.Spline.Point point) { return (float)((DensityFunction)this.function.value()).compute(point.context()); } public float minValue() { return this.function.isBound() ? (float)((DensityFunction)this.function.value()).minValue() : Float.NEGATIVE_INFINITY; } public float maxValue() { return this.function.isBound() ? (float)((DensityFunction)this.function.value()).maxValue() : Float.POSITIVE_INFINITY; } public Coordinate mapAll(DensityFunction.Visitor visitor) { return new Coordinate((Holder<DensityFunction>)new Holder.Direct(((DensityFunction)this.function.value()).mapAll(visitor))); } } public static final class Point extends Record { public Point(DensityFunction.FunctionContext context) { this.context = context; } private final DensityFunction.FunctionContext context; public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1291	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1291	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1291	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Spline$Point;
/* 1291 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction.FunctionContext context() { return this.context; }
/*      */      }
/*      */   private static final class Constant extends Record implements DensityFunction.SimpleFunction { private final double value;
/* 1294 */     private Constant(double value) { this.value = value; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1294	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1294	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1294	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$Constant;
/* 1294 */       //   0	8	1	o	Ljava/lang/Object; } public double value() { return this.value; }
/* 1295 */      private static final KeyDispatchDataCodec<Constant> CODEC = DensityFunctions.singleArgumentCodec(DensityFunctions.NOISE_VALUE_CODEC, Constant::new, Constant::value);
/* 1296 */     private static final Constant ZERO = new Constant(0.0D);
/*      */ 
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/* 1300 */       return this.value;
/*      */     }
/*      */ 
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 1305 */       Arrays.fill(output, this.value);
/*      */     }
/*      */ 
/*      */     
/*      */     public double minValue() {
/* 1310 */       return this.value;
/*      */     }
/*      */ 
/*      */     
/*      */     public double maxValue() {
/* 1315 */       return this.value;
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 1320 */       return (KeyDispatchDataCodec)CODEC;
/*      */     } }
/*      */   private static final class YClampedGradient extends Record implements DensityFunction.SimpleFunction { private final int fromY; private final int toY; private final double fromValue; private final double toValue; private static final MapCodec<YClampedGradient> DATA_CODEC;
/*      */     
/* 1324 */     private YClampedGradient(int fromY, int toY, double fromValue, double toValue) { this.fromY = fromY; this.toY = toY; this.fromValue = fromValue; this.toValue = toValue; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1324	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1324	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1324	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$YClampedGradient;
/* 1324 */       //   0	8	1	o	Ljava/lang/Object; } public int fromY() { return this.fromY; } public int toY() { return this.toY; } public double fromValue() { return this.fromValue; } public double toValue() { return this.toValue; } static {
/* 1325 */       DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.intRange(DimensionType.MIN_Y * 2, DimensionType.MAX_Y * 2).fieldOf("from_y").forGetter(YClampedGradient::fromY), (App)Codec.intRange(DimensionType.MIN_Y * 2, DimensionType.MAX_Y * 2).fieldOf("to_y").forGetter(YClampedGradient::toY), (App)DensityFunctions.NOISE_VALUE_CODEC.fieldOf("from_value").forGetter(YClampedGradient::fromValue), (App)DensityFunctions.NOISE_VALUE_CODEC.fieldOf("to_value").forGetter(YClampedGradient::toValue)).apply((Applicative)i, YClampedGradient::new));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1332 */     public static final KeyDispatchDataCodec<YClampedGradient> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */ 
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/* 1336 */       return Mth.clampedMap(context.blockY(), this.fromY, this.toY, this.fromValue, this.toValue);
/*      */     }
/*      */ 
/*      */     
/*      */     public double minValue() {
/* 1341 */       return Math.min(this.fromValue, this.toValue);
/*      */     }
/*      */ 
/*      */     
/*      */     public double maxValue() {
/* 1346 */       return Math.max(this.fromValue, this.toValue);
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 1351 */       return (KeyDispatchDataCodec)CODEC;
/*      */     } }
/*      */   private static final class FindTopSurface extends Record implements DensityFunction { private final DensityFunction density; private final DensityFunction upperBound; private final int lowerBound; private final int cellHeight; private static final MapCodec<FindTopSurface> DATA_CODEC;
/*      */     
/* 1355 */     private FindTopSurface(DensityFunction density, DensityFunction upperBound, int lowerBound, int cellHeight) { this.density = density; this.upperBound = upperBound; this.lowerBound = lowerBound; this.cellHeight = cellHeight; } public final String toString() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;)Ljava/lang/String;
/*      */       //   6: areturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1355	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface; } public final int hashCode() { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;)I
/*      */       //   6: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1355	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface; } public final boolean equals(Object o) { // Byte code:
/*      */       //   0: aload_0
/*      */       //   1: aload_1
/*      */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;Ljava/lang/Object;)Z
/*      */       //   7: ireturn
/*      */       // Line number table:
/*      */       //   Java source line number -> byte code offset
/*      */       //   #1355	-> 0
/*      */       // Local variable table:
/*      */       //   start	length	slot	name	descriptor
/*      */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunctions$FindTopSurface;
/* 1355 */       //   0	8	1	o	Ljava/lang/Object; } public DensityFunction density() { return this.density; } public DensityFunction upperBound() { return this.upperBound; } public int lowerBound() { return this.lowerBound; } public int cellHeight() { return this.cellHeight; } static {
/* 1356 */       DATA_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DensityFunction.HOLDER_HELPER_CODEC.fieldOf("density").forGetter(FindTopSurface::density), (App)DensityFunction.HOLDER_HELPER_CODEC.fieldOf("upper_bound").forGetter(FindTopSurface::upperBound), (App)Codec.intRange(DimensionType.MIN_Y * 2, DimensionType.MAX_Y * 2).fieldOf("lower_bound").forGetter(FindTopSurface::lowerBound), (App)ExtraCodecs.POSITIVE_INT.fieldOf("cell_height").forGetter(FindTopSurface::cellHeight)).apply((Applicative)i, FindTopSurface::new));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1363 */     public static final KeyDispatchDataCodec<FindTopSurface> CODEC = DensityFunctions.makeCodec(DATA_CODEC);
/*      */ 
/*      */     
/*      */     public double compute(DensityFunction.FunctionContext context) {
/* 1367 */       int topY = Mth.floor(this.upperBound.compute(context) / this.cellHeight) * this.cellHeight;
/* 1368 */       if (topY <= this.lowerBound) {
/* 1369 */         return this.lowerBound;
/*      */       }
/*      */       
/* 1372 */       for (int blockY = topY; blockY >= this.lowerBound; blockY -= this.cellHeight) {
/* 1373 */         if (this.density.compute(new DensityFunction.SinglePointContext(context.blockX(), blockY, context.blockZ())) > 0.0D) {
/* 1374 */           return blockY;
/*      */         }
/*      */       } 
/* 1377 */       return this.lowerBound;
/*      */     }
/*      */ 
/*      */     
/*      */     public void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/* 1382 */       contextProvider.fillAllDirectly(output, this);
/*      */     }
/*      */ 
/*      */     
/*      */     public DensityFunction mapAll(DensityFunction.Visitor visitor) {
/* 1387 */       return visitor.apply(new FindTopSurface(
/* 1388 */             this.density.mapAll(visitor), 
/* 1389 */             this.upperBound.mapAll(visitor), this.lowerBound, this.cellHeight));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public double minValue() {
/* 1397 */       return this.lowerBound;
/*      */     }
/*      */ 
/*      */     
/*      */     public double maxValue() {
/* 1402 */       return Math.max(this.lowerBound, this.upperBound.maxValue());
/*      */     }
/*      */ 
/*      */     
/*      */     public KeyDispatchDataCodec<? extends DensityFunction> codec() {
/* 1407 */       return (KeyDispatchDataCodec)CODEC;
/*      */     } }
/*      */ 
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/DensityFunctions.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */