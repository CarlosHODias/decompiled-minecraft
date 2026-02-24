/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ public final class DiskConfiguration extends Record implements FeatureConfiguration {
/*    */   private final net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider stateProvider;
/*    */   private final net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate target;
/*    */   private final net.minecraft.util.valueproviders.IntProvider radius;
/*    */   private final int halfHeight;
/*    */   public static final com.mojang.serialization.Codec<DiskConfiguration> CODEC;
/*    */   
/*  9 */   public DiskConfiguration(net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider stateProvider, net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate target, net.minecraft.util.valueproviders.IntProvider radius, int halfHeight) { this.stateProvider = stateProvider; this.target = target; this.radius = radius; this.halfHeight = halfHeight; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration; } public net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider stateProvider() { return this.stateProvider; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate target() { return this.target; } public net.minecraft.util.valueproviders.IntProvider radius() { return this.radius; } public int halfHeight() { return this.halfHeight; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 15 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.world.level.levelgen.feature.stateproviders.RuleBasedBlockStateProvider.CODEC.fieldOf("state_provider").forGetter(DiskConfiguration::stateProvider), (com.mojang.datafixers.kinds.App)net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.CODEC.fieldOf("target").forGetter(DiskConfiguration::target), (com.mojang.datafixers.kinds.App)net.minecraft.util.valueproviders.IntProvider.codec(0, 8).fieldOf("radius").forGetter(DiskConfiguration::radius), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.intRange(0, 4).fieldOf("half_height").forGetter(DiskConfiguration::halfHeight)).apply((com.mojang.datafixers.kinds.Applicative)i, DiskConfiguration::new));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/DiskConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */