/*    */ package net.minecraft.world.level.levelgen.feature.rootplacers;
/*    */ public final class MangroveRootPlacement extends Record {
/*    */   private final net.minecraft.core.HolderSet<net.minecraft.world.level.block.Block> canGrowThrough;
/*    */   private final net.minecraft.core.HolderSet<net.minecraft.world.level.block.Block> muddyRootsIn;
/*    */   private final net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider muddyRootsProvider;
/*    */   private final int maxRootWidth;
/*    */   private final int maxRootLength;
/*    */   private final float randomSkewChance;
/*    */   public static final com.mojang.serialization.Codec<MangroveRootPlacement> CODEC;
/*    */   
/* 11 */   public MangroveRootPlacement(net.minecraft.core.HolderSet<net.minecraft.world.level.block.Block> canGrowThrough, net.minecraft.core.HolderSet<net.minecraft.world.level.block.Block> muddyRootsIn, net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider muddyRootsProvider, int maxRootWidth, int maxRootLength, float randomSkewChance) { this.canGrowThrough = canGrowThrough; this.muddyRootsIn = muddyRootsIn; this.muddyRootsProvider = muddyRootsProvider; this.maxRootWidth = maxRootWidth; this.maxRootLength = maxRootLength; this.randomSkewChance = randomSkewChance; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement; } public net.minecraft.core.HolderSet<net.minecraft.world.level.block.Block> canGrowThrough() { return this.canGrowThrough; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.core.HolderSet<net.minecraft.world.level.block.Block> muddyRootsIn() { return this.muddyRootsIn; } public net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider muddyRootsProvider() { return this.muddyRootsProvider; } public int maxRootWidth() { return this.maxRootWidth; } public int maxRootLength() { return this.maxRootLength; } public float randomSkewChance() { return this.randomSkewChance; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 19 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.core.RegistryCodecs.homogeneousList(net.minecraft.core.registries.Registries.BLOCK).fieldOf("can_grow_through").forGetter(()), (com.mojang.datafixers.kinds.App)net.minecraft.core.RegistryCodecs.homogeneousList(net.minecraft.core.registries.Registries.BLOCK).fieldOf("muddy_roots_in").forGetter(()), (com.mojang.datafixers.kinds.App)net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider.CODEC.fieldOf("muddy_roots_provider").forGetter(()), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.intRange(1, 12).fieldOf("max_root_width").forGetter(()), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.intRange(1, 64).fieldOf("max_root_length").forGetter(()), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.floatRange(0.0F, 1.0F).fieldOf("random_skew_chance").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, MangroveRootPlacement::new));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/rootplacers/MangroveRootPlacement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */