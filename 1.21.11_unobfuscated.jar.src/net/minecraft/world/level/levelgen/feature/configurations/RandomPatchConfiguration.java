/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ public final class RandomPatchConfiguration extends Record implements FeatureConfiguration {
/*    */   private final int tries;
/*    */   private final int xzSpread;
/*    */   private final int ySpread;
/*    */   private final net.minecraft.core.Holder<net.minecraft.world.level.levelgen.placement.PlacedFeature> feature;
/*    */   public static final com.mojang.serialization.Codec<RandomPatchConfiguration> CODEC;
/*    */   
/*  9 */   public RandomPatchConfiguration(int tries, int xzSpread, int ySpread, net.minecraft.core.Holder<net.minecraft.world.level.levelgen.placement.PlacedFeature> feature) { this.tries = tries; this.xzSpread = xzSpread; this.ySpread = ySpread; this.feature = feature; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration; } public int tries() { return this.tries; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public int xzSpread() { return this.xzSpread; } public int ySpread() { return this.ySpread; } public net.minecraft.core.Holder<net.minecraft.world.level.levelgen.placement.PlacedFeature> feature() { return this.feature; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 15 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(128).forGetter(RandomPatchConfiguration::tries), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.NON_NEGATIVE_INT.fieldOf("xz_spread").orElse(7).forGetter(RandomPatchConfiguration::xzSpread), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.NON_NEGATIVE_INT.fieldOf("y_spread").orElse(3).forGetter(RandomPatchConfiguration::ySpread), (com.mojang.datafixers.kinds.App)net.minecraft.world.level.levelgen.placement.PlacedFeature.CODEC.fieldOf("feature").forGetter(RandomPatchConfiguration::feature)).apply((com.mojang.datafixers.kinds.Applicative)i, RandomPatchConfiguration::new));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/RandomPatchConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */