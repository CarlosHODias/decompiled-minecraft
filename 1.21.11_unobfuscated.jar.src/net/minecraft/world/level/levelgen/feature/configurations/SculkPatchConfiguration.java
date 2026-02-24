/*   */ package net.minecraft.world.level.levelgen.feature.configurations;public final class SculkPatchConfiguration extends Record implements FeatureConfiguration { private final int chargeCount; private final int amountPerCharge; private final int spreadAttempts; private final int growthRounds;
/*   */   private final int spreadRounds;
/*   */   private final net.minecraft.util.valueproviders.IntProvider extraRareGrowths;
/*   */   private final float catalystChance;
/*   */   public static final com.mojang.serialization.Codec<SculkPatchConfiguration> CODEC;
/*   */   
/* 7 */   public SculkPatchConfiguration(int chargeCount, int amountPerCharge, int spreadAttempts, int growthRounds, int spreadRounds, net.minecraft.util.valueproviders.IntProvider extraRareGrowths, float catalystChance) { this.chargeCount = chargeCount; this.amountPerCharge = amountPerCharge; this.spreadAttempts = spreadAttempts; this.growthRounds = growthRounds; this.spreadRounds = spreadRounds; this.extraRareGrowths = extraRareGrowths; this.catalystChance = catalystChance; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 7 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration; } public int chargeCount() { return this.chargeCount; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration;
/* 7 */     //   0	8	1	o	Ljava/lang/Object; } public int amountPerCharge() { return this.amountPerCharge; } public int spreadAttempts() { return this.spreadAttempts; } public int growthRounds() { return this.growthRounds; } public int spreadRounds() { return this.spreadRounds; } public net.minecraft.util.valueproviders.IntProvider extraRareGrowths() { return this.extraRareGrowths; } public float catalystChance() { return this.catalystChance; } static {
/* 8 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.intRange(1, 32).fieldOf("charge_count").forGetter(SculkPatchConfiguration::chargeCount), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.intRange(1, 500).fieldOf("amount_per_charge").forGetter(SculkPatchConfiguration::amountPerCharge), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.intRange(1, 64).fieldOf("spread_attempts").forGetter(SculkPatchConfiguration::spreadAttempts), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.intRange(0, 8).fieldOf("growth_rounds").forGetter(SculkPatchConfiguration::growthRounds), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.intRange(0, 8).fieldOf("spread_rounds").forGetter(SculkPatchConfiguration::spreadRounds), (com.mojang.datafixers.kinds.App)net.minecraft.util.valueproviders.IntProvider.CODEC.fieldOf("extra_rare_growths").forGetter(SculkPatchConfiguration::extraRareGrowths), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.floatRange(0.0F, 1.0F).fieldOf("catalyst_chance").forGetter(SculkPatchConfiguration::catalystChance)).apply((com.mojang.datafixers.kinds.Applicative)i, SculkPatchConfiguration::new));
/*   */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/SculkPatchConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */