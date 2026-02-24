/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.floats.FloatArrayList;
/*     */ import it.unimi.dsi.fastutil.floats.FloatList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public interface CubicSpline<C, I extends BoundedFloatFunction<C>>
/*     */   extends BoundedFloatFunction<C> {
/*     */   @VisibleForDebug
/*     */   public static final class Multipoint<C, I extends BoundedFloatFunction<C>> extends Record implements CubicSpline<C, I>
/*     */   {
/*     */     private final I coordinate;
/*     */     private final float[] locations;
/*     */     private final List<CubicSpline<C, I>> values;
/*     */     private final float[] derivatives;
/*     */     
/*  31 */     public I coordinate() { return this.coordinate; } private final float minValue; private final float maxValue; public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/CubicSpline$Multipoint;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint<TC;TI;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/CubicSpline$Multipoint;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Multipoint<TC;TI;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/CubicSpline$Multipoint;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #31	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/CubicSpline$Multipoint;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*  31 */       //   0	8	0	this	Lnet/minecraft/util/CubicSpline$Multipoint<TC;TI;>; } public float[] locations() { return this.locations; } public List<CubicSpline<C, I>> values() { return this.values; } public float[] derivatives() { return this.derivatives; } public float minValue() { return this.minValue; } public float maxValue() { return this.maxValue; }
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
/*     */     public Multipoint(I coordinate, float[] locations, List<CubicSpline<C, I>> values, float[] derivatives, float minValue, float maxValue)
/*     */     {
/*  47 */       validateSizes(locations, values, derivatives); this.coordinate = coordinate; this.locations = locations;
/*     */       this.values = values;
/*     */       this.derivatives = derivatives;
/*     */       this.minValue = minValue;
/*  51 */       this.maxValue = maxValue; } private static <C, I extends BoundedFloatFunction<C>> Multipoint<C, I> create(I coordinate, float[] locations, List<CubicSpline<C, I>> values, float[] derivatives) { validateSizes(locations, values, derivatives);
/*     */       
/*  53 */       int lastIndex = locations.length - 1;
/*     */       
/*  55 */       float minValue = Float.POSITIVE_INFINITY;
/*  56 */       float maxValue = Float.NEGATIVE_INFINITY;
/*     */       
/*  58 */       float minInput = coordinate.minValue();
/*  59 */       float maxInput = coordinate.maxValue();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  65 */       if (minInput < locations[0]) {
/*  66 */         float edge1 = linearExtend(minInput, locations, ((CubicSpline)values.get(0)).minValue(), derivatives, 0);
/*  67 */         float edge2 = linearExtend(minInput, locations, ((CubicSpline)values.get(0)).maxValue(), derivatives, 0);
/*     */         
/*  69 */         minValue = Math.min(minValue, Math.min(edge1, edge2));
/*  70 */         maxValue = Math.max(maxValue, Math.max(edge1, edge2));
/*     */       } 
/*     */ 
/*     */       
/*  74 */       if (maxInput > locations[lastIndex]) {
/*  75 */         float edge1 = linearExtend(maxInput, locations, ((CubicSpline)values.get(lastIndex)).minValue(), derivatives, lastIndex);
/*  76 */         float edge2 = linearExtend(maxInput, locations, ((CubicSpline)values.get(lastIndex)).maxValue(), derivatives, lastIndex);
/*     */         
/*  78 */         minValue = Math.min(minValue, Math.min(edge1, edge2));
/*  79 */         maxValue = Math.max(maxValue, Math.max(edge1, edge2));
/*     */       } 
/*     */ 
/*     */       
/*  83 */       for (CubicSpline<C, I> value : values) {
/*  84 */         minValue = Math.min(minValue, value.minValue());
/*  85 */         maxValue = Math.max(maxValue, value.maxValue());
/*     */       } 
/*     */       
/*  88 */       for (int i = 0; i < lastIndex; i++) {
/*  89 */         float x1 = locations[i];
/*  90 */         float x2 = locations[i + 1];
/*  91 */         float xDiff = x2 - x1;
/*     */         
/*  93 */         CubicSpline<C, I> v1 = values.get(i);
/*  94 */         CubicSpline<C, I> v2 = values.get(i + 1);
/*     */         
/*  96 */         float min1 = v1.minValue();
/*  97 */         float max1 = v1.maxValue();
/*  98 */         float min2 = v2.minValue();
/*  99 */         float max2 = v2.maxValue();
/*     */         
/* 101 */         float d1 = derivatives[i];
/* 102 */         float d2 = derivatives[i + 1];
/*     */         
/* 104 */         if (d1 != 0.0F || d2 != 0.0F) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 109 */           float p1 = d1 * xDiff;
/* 110 */           float p2 = d2 * xDiff;
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
/* 122 */           float minLerp1 = Math.min(min1, min2);
/* 123 */           float maxLerp1 = Math.max(max1, max2);
/*     */           
/* 125 */           float minA = p1 - max2 + min1;
/* 126 */           float maxA = p1 - min2 + max1;
/*     */           
/* 128 */           float minB = -p2 + min2 - max1;
/* 129 */           float maxB = -p2 + max2 - min1;
/*     */           
/* 131 */           float minLerp2 = Math.min(minA, minB);
/* 132 */           float maxLerp2 = Math.max(maxA, maxB);
/*     */           
/* 134 */           minValue = Math.min(minValue, minLerp1 + 0.25F * minLerp2);
/* 135 */           maxValue = Math.max(maxValue, maxLerp1 + 0.25F * maxLerp2);
/*     */         } 
/*     */       } 
/* 138 */       return new Multipoint<>(coordinate, locations, values, derivatives, minValue, maxValue); }
/*     */ 
/*     */     
/*     */     private static float linearExtend(float input, float[] locations, float value, float[] derivatives, int index) {
/* 142 */       float derivative = derivatives[index];
/* 143 */       if (derivative == 0.0F)
/*     */       {
/* 145 */         return value;
/*     */       }
/* 147 */       return value + derivative * (input - locations[index]);
/*     */     }
/*     */     
/*     */     private static <C, I extends BoundedFloatFunction<C>> void validateSizes(float[] locations, List<CubicSpline<C, I>> values, float[] derivatives) {
/* 151 */       if (locations.length != values.size() || locations.length != derivatives.length) {
/* 152 */         throw new IllegalArgumentException("All lengths must be equal, got: " + locations.length + " " + values.size() + " " + derivatives.length);
/*     */       }
/* 154 */       if (locations.length == 0) {
/* 155 */         throw new IllegalArgumentException("Cannot create a multipoint spline with no points");
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public float apply(C c) {
/* 161 */       float input = this.coordinate.apply(c);
/* 162 */       int start = findIntervalStart(this.locations, input);
/*     */       
/* 164 */       int lastIndex = this.locations.length - 1;
/*     */ 
/*     */       
/* 167 */       if (start < 0) {
/* 168 */         return linearExtend(input, this.locations, ((CubicSpline)this.values.get(0)).apply(c), this.derivatives, 0);
/*     */       }
/* 170 */       if (start == lastIndex) {
/* 171 */         return linearExtend(input, this.locations, ((CubicSpline)this.values.get(lastIndex)).apply(c), this.derivatives, lastIndex);
/*     */       }
/* 173 */       float x1 = this.locations[start];
/* 174 */       float x2 = this.locations[start + 1];
/* 175 */       float t = (input - x1) / (x2 - x1);
/*     */       
/* 177 */       BoundedFloatFunction<C> f1 = this.values.get(start);
/* 178 */       BoundedFloatFunction<C> f2 = this.values.get(start + 1);
/* 179 */       float d1 = this.derivatives[start];
/* 180 */       float d2 = this.derivatives[start + 1];
/*     */       
/* 182 */       float y1 = f1.apply(c);
/* 183 */       float y2 = f2.apply(c);
/*     */       
/* 185 */       float a = d1 * (x2 - x1) - y2 - y1;
/* 186 */       float b = -d2 * (x2 - x1) + y2 - y1;
/*     */ 
/*     */       
/* 189 */       float offset = Mth.lerp(t, y1, y2) + t * (1.0F - t) * Mth.lerp(t, a, b);
/* 190 */       return offset;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static int findIntervalStart(float[] locations, float input) {
/* 198 */       return Mth.binarySearch(0, locations.length, i -> (input < locations[i])) - 1;
/*     */     }
/*     */ 
/*     */     
/*     */     @VisibleForTesting
/*     */     public String parityString() {
/* 204 */       return "Spline{coordinate=" + String.valueOf(this.coordinate) + ", locations=" + toString(this.locations) + ", derivatives=" + toString(this.derivatives) + ", values=" + (String)this.values.stream().map(CubicSpline::parityString).collect(Collectors.joining(", ", "[", "]")) + "}";
/*     */     }
/*     */     
/*     */     private String toString(float[] arr) {
/* 208 */       return "[" + (String)IntStream.range(0, arr.length).mapToDouble(i -> arr[i]).<CharSequence>mapToObj(f -> String.format(Locale.ROOT, "%.3f", new Object[] { f })).collect(Collectors.joining(", ")) + "]";
/*     */     }
/*     */     
/*     */     public CubicSpline<C, I> mapAll(CubicSpline.CoordinateVisitor<I> visitor)
/*     */     {
/* 213 */       return create(
/* 214 */           visitor.visit(this.coordinate), this.locations, 
/*     */           
/* 216 */           values().stream().map(v -> v.mapAll(visitor)).toList(), this.derivatives);
/*     */     } } @VisibleForDebug
/*     */   public static final class Constant<C, I extends BoundedFloatFunction<C>> extends Record implements CubicSpline<C, I> { private final float value;
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/CubicSpline$Constant;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #222	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Constant;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Constant<TC;TI;>;
/*     */     }
/* 222 */     public float value() { return this.value; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/CubicSpline$Constant;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #222	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Constant;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/CubicSpline$Constant<TC;TI;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/CubicSpline$Constant;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #222	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/CubicSpline$Constant;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	8	0	this	Lnet/minecraft/util/CubicSpline$Constant<TC;TI;>; }
/* 223 */     public Constant(float value) { this.value = value; }
/*     */     
/*     */     public float apply(C c) {
/* 226 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public String parityString() {
/* 231 */       return String.format(Locale.ROOT, "k=%.3f", new Object[] { this.value });
/*     */     }
/*     */ 
/*     */     
/*     */     public float minValue() {
/* 236 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public float maxValue() {
/* 241 */       return this.value;
/*     */     }
/*     */ 
/*     */     
/*     */     public CubicSpline<C, I> mapAll(CubicSpline.CoordinateVisitor<I> visitor) {
/* 246 */       return this;
/*     */     } }
/*     */ 
/*     */   
/*     */   static <C, I extends BoundedFloatFunction<C>> Codec<CubicSpline<C, I>> codec(Codec<I> coordinateCodec) {
/* 251 */     MutableObject<Codec<CubicSpline<C, I>>> result = new MutableObject();
/*     */ 
/*     */ 
/*     */     
/* 255 */     Codec<Point<C, I>> pointCodec = RecordCodecBuilder.create(i -> i.group((App)Codec.FLOAT.fieldOf("location").forGetter(Point::location), (App)Codec.lazyInitialized((Supplier)result).fieldOf("value").forGetter(Point::value), (App)Codec.FLOAT.fieldOf("derivative").forGetter(Point::derivative)).apply((Applicative)i, ()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 261 */     Codec<Multipoint<C, I>> multipointCodec = RecordCodecBuilder.create(i -> i.group((App)coordinateCodec.fieldOf("coordinate").forGetter(Multipoint::coordinate), (App)ExtraCodecs.<T>nonEmptyList(pointCodec.listOf()).fieldOf("points").forGetter(())).apply((Applicative)i, ()));
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
/* 277 */     result.setValue(Codec.either((Codec)Codec.FLOAT, multipointCodec).xmap(e -> (CubicSpline)e.map(Constant::new, ()), s -> {
/*     */             Constant<C, I> c = (Constant<C, I>)s;
/*     */             return (s instanceof Constant) ? Either.left(c.value()) : Either.right(s);
/*     */           }));
/* 281 */     return (Codec<CubicSpline<C, I>>)result.get();
/*     */   }
/*     */   
/*     */   static <C, I extends BoundedFloatFunction<C>> CubicSpline<C, I> constant(float value) {
/* 285 */     return new Constant<>(value);
/*     */   }
/*     */   
/*     */   static <C, I extends BoundedFloatFunction<C>> Builder<C, I> builder(I coordinate) {
/* 289 */     return new Builder<>(coordinate);
/*     */   }
/*     */   
/*     */   static <C, I extends BoundedFloatFunction<C>> Builder<C, I> builder(I coordinate, BoundedFloatFunction<Float> valueTransformer) {
/* 293 */     return new Builder<>(coordinate, valueTransformer);
/*     */   }
/*     */   @VisibleForDebug
/*     */   String parityString();
/*     */   CubicSpline<C, I> mapAll(CoordinateVisitor<I> paramCoordinateVisitor);
/*     */   public static final class Builder<C, I extends BoundedFloatFunction<C>> { private final I coordinate; private final BoundedFloatFunction<Float> valueTransformer;
/* 299 */     private final FloatList locations = (FloatList)new FloatArrayList();
/* 300 */     private final List<CubicSpline<C, I>> values = Lists.newArrayList();
/* 301 */     private final FloatList derivatives = (FloatList)new FloatArrayList();
/*     */     
/*     */     protected Builder(I coordinate) {
/* 304 */       this(coordinate, BoundedFloatFunction.IDENTITY);
/*     */     }
/*     */     
/*     */     protected Builder(I coordinate, BoundedFloatFunction<Float> valueTransformer) {
/* 308 */       this.coordinate = coordinate;
/* 309 */       this.valueTransformer = valueTransformer;
/*     */     }
/*     */     
/*     */     public Builder<C, I> addPoint(float location, float value) {
/* 313 */       return addPoint(location, new CubicSpline.Constant<>(this.valueTransformer.apply(value)), 0.0F);
/*     */     }
/*     */     
/*     */     public Builder<C, I> addPoint(float location, float value, float derivative) {
/* 317 */       return addPoint(location, new CubicSpline.Constant<>(this.valueTransformer.apply(value)), derivative);
/*     */     }
/*     */     
/*     */     public Builder<C, I> addPoint(float location, CubicSpline<C, I> sampler) {
/* 321 */       return addPoint(location, sampler, 0.0F);
/*     */     }
/*     */     
/*     */     private Builder<C, I> addPoint(float location, CubicSpline<C, I> sampler, float derivative) {
/* 325 */       if (!this.locations.isEmpty() && location <= this.locations.getFloat(this.locations.size() - 1)) {
/* 326 */         throw new IllegalArgumentException("Please register points in ascending order");
/*     */       }
/* 328 */       this.locations.add(location);
/* 329 */       this.values.add(sampler);
/* 330 */       this.derivatives.add(derivative);
/* 331 */       return this;
/*     */     }
/*     */     
/*     */     public CubicSpline<C, I> build() {
/* 335 */       if (this.locations.isEmpty()) {
/* 336 */         throw new IllegalStateException("No elements added");
/*     */       }
/* 338 */       return CubicSpline.Multipoint.create(this.coordinate, this.locations.toFloatArray(), (List<CubicSpline<C, I>>)ImmutableList.copyOf(this.values), this.derivatives.toFloatArray());
/*     */     } }
/*     */ 
/*     */   
/*     */   public static interface CoordinateVisitor<I> {
/*     */     I visit(I param1I);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/CubicSpline.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */