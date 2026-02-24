/*    */ package net.minecraft.world.level.levelgen;
/*    */ public final class NoiseRouter extends Record { private final DensityFunction barrierNoise;
/*    */   private final DensityFunction fluidLevelFloodednessNoise;
/*    */   private final DensityFunction fluidLevelSpreadNoise;
/*    */   private final DensityFunction lavaNoise;
/*    */   private final DensityFunction temperature;
/*    */   private final DensityFunction vegetation;
/*    */   private final DensityFunction continents;
/*    */   private final DensityFunction erosion;
/*    */   
/* 11 */   public NoiseRouter(DensityFunction barrierNoise, DensityFunction fluidLevelFloodednessNoise, DensityFunction fluidLevelSpreadNoise, DensityFunction lavaNoise, DensityFunction temperature, DensityFunction vegetation, DensityFunction continents, DensityFunction erosion, DensityFunction depth, DensityFunction ridges, DensityFunction preliminarySurfaceLevel, DensityFunction finalDensity, DensityFunction veinToggle, DensityFunction veinRidged, DensityFunction veinGap) { this.barrierNoise = barrierNoise; this.fluidLevelFloodednessNoise = fluidLevelFloodednessNoise; this.fluidLevelSpreadNoise = fluidLevelSpreadNoise; this.lavaNoise = lavaNoise; this.temperature = temperature; this.vegetation = vegetation; this.continents = continents; this.erosion = erosion; this.depth = depth; this.ridges = ridges; this.preliminarySurfaceLevel = preliminarySurfaceLevel; this.finalDensity = finalDensity; this.veinToggle = veinToggle; this.veinRidged = veinRidged; this.veinGap = veinGap; } private final DensityFunction depth; private final DensityFunction ridges; private final DensityFunction preliminarySurfaceLevel; private final DensityFunction finalDensity; private final DensityFunction veinToggle; private final DensityFunction veinRidged; private final DensityFunction veinGap; public static final com.mojang.serialization.Codec<NoiseRouter> CODEC; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/NoiseRouter;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/NoiseRouter; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/NoiseRouter;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/NoiseRouter; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/NoiseRouter;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/NoiseRouter;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public DensityFunction barrierNoise() { return this.barrierNoise; } public DensityFunction fluidLevelFloodednessNoise() { return this.fluidLevelFloodednessNoise; } public DensityFunction fluidLevelSpreadNoise() { return this.fluidLevelSpreadNoise; } public DensityFunction lavaNoise() { return this.lavaNoise; } public DensityFunction temperature() { return this.temperature; } public DensityFunction vegetation() { return this.vegetation; } public DensityFunction continents() { return this.continents; } public DensityFunction erosion() { return this.erosion; } public DensityFunction depth() { return this.depth; } public DensityFunction ridges() { return this.ridges; } public DensityFunction preliminarySurfaceLevel() { return this.preliminarySurfaceLevel; } public DensityFunction finalDensity() { return this.finalDensity; } public DensityFunction veinToggle() { return this.veinToggle; } public DensityFunction veinRidged() { return this.veinRidged; } public DensityFunction veinGap() { return this.veinGap; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static com.mojang.serialization.codecs.RecordCodecBuilder<NoiseRouter, DensityFunction> field(String name, java.util.function.Function<NoiseRouter, DensityFunction> getter) {
/* 38 */     return DensityFunction.HOLDER_HELPER_CODEC.fieldOf(name).forGetter(getter);
/*    */   }
/*    */   static {
/* 41 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)field("barrier", NoiseRouter::barrierNoise), (com.mojang.datafixers.kinds.App)field("fluid_level_floodedness", NoiseRouter::fluidLevelFloodednessNoise), (com.mojang.datafixers.kinds.App)field("fluid_level_spread", NoiseRouter::fluidLevelSpreadNoise), (com.mojang.datafixers.kinds.App)field("lava", NoiseRouter::lavaNoise), (com.mojang.datafixers.kinds.App)field("temperature", NoiseRouter::temperature), (com.mojang.datafixers.kinds.App)field("vegetation", NoiseRouter::vegetation), (com.mojang.datafixers.kinds.App)field("continents", NoiseRouter::continents), (com.mojang.datafixers.kinds.App)field("erosion", NoiseRouter::erosion), (com.mojang.datafixers.kinds.App)field("depth", NoiseRouter::depth), (com.mojang.datafixers.kinds.App)field("ridges", NoiseRouter::ridges), (com.mojang.datafixers.kinds.App)field("preliminary_surface_level", NoiseRouter::preliminarySurfaceLevel), (com.mojang.datafixers.kinds.App)field("final_density", NoiseRouter::finalDensity), (com.mojang.datafixers.kinds.App)field("vein_toggle", NoiseRouter::veinToggle), (com.mojang.datafixers.kinds.App)field("vein_ridged", NoiseRouter::veinRidged), (com.mojang.datafixers.kinds.App)field("vein_gap", NoiseRouter::veinGap)).apply((com.mojang.datafixers.kinds.Applicative)i, NoiseRouter::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public NoiseRouter mapAll(DensityFunction.Visitor visitor) {
/* 63 */     return new NoiseRouter(
/* 64 */         this.barrierNoise.mapAll(visitor), 
/* 65 */         this.fluidLevelFloodednessNoise.mapAll(visitor), 
/* 66 */         this.fluidLevelSpreadNoise.mapAll(visitor), 
/* 67 */         this.lavaNoise.mapAll(visitor), 
/* 68 */         this.temperature.mapAll(visitor), 
/* 69 */         this.vegetation.mapAll(visitor), 
/* 70 */         this.continents.mapAll(visitor), 
/* 71 */         this.erosion.mapAll(visitor), 
/* 72 */         this.depth.mapAll(visitor), 
/* 73 */         this.ridges.mapAll(visitor), 
/* 74 */         this.preliminarySurfaceLevel.mapAll(visitor), 
/* 75 */         this.finalDensity.mapAll(visitor), 
/* 76 */         this.veinToggle.mapAll(visitor), 
/* 77 */         this.veinRidged.mapAll(visitor), 
/* 78 */         this.veinGap.mapAll(visitor));
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/NoiseRouter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */