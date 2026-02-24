/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.resources.RegistryFileCodec;
/*     */ import net.minecraft.util.KeyDispatchDataCodec;
/*     */ import net.minecraft.world.level.levelgen.blending.Blender;
/*     */ import net.minecraft.world.level.levelgen.synth.NormalNoise;
/*     */ 
/*     */ public interface DensityFunction {
/*  13 */   public static final Codec<DensityFunction> DIRECT_CODEC = DensityFunctions.DIRECT_CODEC;
/*  14 */   public static final Codec<Holder<DensityFunction>> CODEC = (Codec<Holder<DensityFunction>>)RegistryFileCodec.create(Registries.DENSITY_FUNCTION, DIRECT_CODEC); public static final Codec<DensityFunction> HOLDER_HELPER_CODEC;
/*     */   static {
/*  16 */     HOLDER_HELPER_CODEC = CODEC.xmap(HolderHolder::new, value -> {
/*     */           if (value instanceof DensityFunctions.HolderHolder) {
/*     */             DensityFunctions.HolderHolder holder = (DensityFunctions.HolderHolder)value;
/*     */             return holder.function();
/*     */           } 
/*     */           return new Holder.Direct(value);
/*     */         });
/*     */   }
/*     */   
/*     */   public static interface ContextProvider {
/*     */     DensityFunction.FunctionContext forIndex(int param1Int);
/*     */     
/*     */     void fillAllDirectly(double[] param1ArrayOfdouble, DensityFunction param1DensityFunction);
/*     */   }
/*     */   
/*     */   public static final class NoiseHolder extends Record {
/*     */     private final Holder<NormalNoise.NoiseParameters> noiseData;
/*     */     private final NormalNoise noise;
/*     */     public static final Codec<NoiseHolder> CODEC;
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #46	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #46	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;
/*     */     }
/*     */     
/*     */     public NoiseHolder(Holder<NormalNoise.NoiseParameters> noiseData, NormalNoise noise)
/*     */     {
/*  46 */       this.noiseData = noiseData; this.noise = noise; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #46	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$NoiseHolder;
/*  46 */       //   0	8	1	o	Ljava/lang/Object; } public Holder<NormalNoise.NoiseParameters> noiseData() { return this.noiseData; } public NormalNoise noise() { return this.noise; }
/*     */ 
/*     */ 
/*     */     
/*     */     public NoiseHolder(Holder<NormalNoise.NoiseParameters> noiseData) {
/*  51 */       this(noiseData, null);
/*     */     }
/*     */     static {
/*  54 */       CODEC = NormalNoise.NoiseParameters.CODEC.xmap(data -> new NoiseHolder(data, null), NoiseHolder::noiseData);
/*     */     }
/*     */     public double getValue(double x, double y, double z) {
/*  57 */       return (this.noise == null) ? 0.0D : this.noise.getValue(x, y, z);
/*     */     }
/*     */     
/*     */     public double maxValue() {
/*  61 */       return (this.noise == null) ? 2.0D : this.noise.maxValue();
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface Visitor {
/*     */     DensityFunction apply(DensityFunction param1DensityFunction);
/*     */     
/*     */     default DensityFunction.NoiseHolder visitNoise(DensityFunction.NoiseHolder noise) {
/*  69 */       return noise;
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface SimpleFunction
/*     */     extends DensityFunction {
/*     */     default void fillArray(double[] output, DensityFunction.ContextProvider contextProvider) {
/*  76 */       contextProvider.fillAllDirectly(output, this);
/*     */     }
/*     */ 
/*     */     
/*     */     default DensityFunction mapAll(DensityFunction.Visitor visitor) {
/*  81 */       return visitor.apply(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface FunctionContext {
/*     */     int blockX();
/*     */     
/*     */     int blockY();
/*     */     
/*     */     int blockZ();
/*     */     
/*     */     default Blender getBlender() {
/*  93 */       return Blender.empty();
/*     */     } }
/*     */   public static final class SinglePointContext extends Record implements FunctionContext { private final int blockX; private final int blockY; private final int blockZ;
/*     */     
/*  97 */     public SinglePointContext(int blockX, int blockY, int blockZ) { this.blockX = blockX; this.blockY = blockY; this.blockZ = blockZ; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/DensityFunction$SinglePointContext;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #97	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  97 */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$SinglePointContext; } public int blockX() { return this.blockX; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/DensityFunction$SinglePointContext;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #97	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$SinglePointContext; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/DensityFunction$SinglePointContext;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #97	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/DensityFunction$SinglePointContext;
/*  97 */       //   0	8	1	o	Ljava/lang/Object; } public int blockY() { return this.blockY; } public int blockZ() { return this.blockZ; }
/*     */      }
/*     */   default DensityFunction clamp(double min, double max) {
/* 100 */     return new DensityFunctions.Clamp(this, min, max);
/*     */   }
/*     */   
/*     */   default DensityFunction abs() {
/* 104 */     return DensityFunctions.map(this, DensityFunctions.Mapped.Type.ABS);
/*     */   }
/*     */   
/*     */   default DensityFunction square() {
/* 108 */     return DensityFunctions.map(this, DensityFunctions.Mapped.Type.SQUARE);
/*     */   }
/*     */   
/*     */   default DensityFunction cube() {
/* 112 */     return DensityFunctions.map(this, DensityFunctions.Mapped.Type.CUBE);
/*     */   }
/*     */   
/*     */   default DensityFunction halfNegative() {
/* 116 */     return DensityFunctions.map(this, DensityFunctions.Mapped.Type.HALF_NEGATIVE);
/*     */   }
/*     */   
/*     */   default DensityFunction quarterNegative() {
/* 120 */     return DensityFunctions.map(this, DensityFunctions.Mapped.Type.QUARTER_NEGATIVE);
/*     */   }
/*     */   
/*     */   default DensityFunction invert() {
/* 124 */     return DensityFunctions.map(this, DensityFunctions.Mapped.Type.INVERT);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   default DensityFunction squeeze() {
/* 131 */     return DensityFunctions.map(this, DensityFunctions.Mapped.Type.SQUEEZE);
/*     */   }
/*     */   
/*     */   double compute(FunctionContext paramFunctionContext);
/*     */   
/*     */   void fillArray(double[] paramArrayOfdouble, ContextProvider paramContextProvider);
/*     */   
/*     */   DensityFunction mapAll(Visitor paramVisitor);
/*     */   
/*     */   double minValue();
/*     */   
/*     */   double maxValue();
/*     */   
/*     */   KeyDispatchDataCodec<? extends DensityFunction> codec();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/DensityFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */