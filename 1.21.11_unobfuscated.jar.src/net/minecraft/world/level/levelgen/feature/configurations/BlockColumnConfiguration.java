/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ 
/*    */ public final class BlockColumnConfiguration extends Record implements FeatureConfiguration {
/*    */   private final java.util.List<Layer> layers;
/*    */   private final net.minecraft.core.Direction direction;
/*    */   private final net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate allowedPlacement;
/*    */   private final boolean prioritizeTip;
/*    */   public static final com.mojang.serialization.Codec<BlockColumnConfiguration> CODEC;
/*    */   
/* 12 */   public BlockColumnConfiguration(java.util.List<Layer> layers, net.minecraft.core.Direction direction, net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate allowedPlacement, boolean prioritizeTip) { this.layers = layers; this.direction = direction; this.allowedPlacement = allowedPlacement; this.prioritizeTip = prioritizeTip; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration; } public java.util.List<Layer> layers() { return this.layers; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.core.Direction direction() { return this.direction; } public net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate allowedPlacement() { return this.allowedPlacement; } public boolean prioritizeTip() { return this.prioritizeTip; }
/*    */    static {
/* 14 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((App)Layer.CODEC.listOf().fieldOf("layers").forGetter(BlockColumnConfiguration::layers), (App)net.minecraft.core.Direction.CODEC.fieldOf("direction").forGetter(BlockColumnConfiguration::direction), (App)net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.CODEC.fieldOf("allowed_placement").forGetter(BlockColumnConfiguration::allowedPlacement), (App)com.mojang.serialization.Codec.BOOL.fieldOf("prioritize_tip").forGetter(BlockColumnConfiguration::prioritizeTip)).apply((com.mojang.datafixers.kinds.Applicative)i, BlockColumnConfiguration::new));
/*    */   }
/*    */   
/*    */   public static final class Layer extends Record { private final net.minecraft.util.valueproviders.IntProvider height;
/*    */     private final net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider state;
/*    */     public static final com.mojang.serialization.Codec<Layer> CODEC;
/*    */     
/* 21 */     public Layer(net.minecraft.util.valueproviders.IntProvider height, net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider state) { this.height = height; this.state = state; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #21	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration$Layer;
/* 21 */       //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.util.valueproviders.IntProvider height() { return this.height; } public net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider state() { return this.state; } static {
/* 22 */       CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((App)net.minecraft.util.valueproviders.IntProvider.NON_NEGATIVE_CODEC.fieldOf("height").forGetter(Layer::height), (App)net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider.CODEC.fieldOf("provider").forGetter(Layer::state)).apply((com.mojang.datafixers.kinds.Applicative)i, Layer::new));
/*    */     } }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Layer layer(net.minecraft.util.valueproviders.IntProvider height, net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider state) {
/* 29 */     return new Layer(height, state);
/*    */   }
/*    */   
/*    */   public static BlockColumnConfiguration simple(net.minecraft.util.valueproviders.IntProvider height, net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider state) {
/* 33 */     return new BlockColumnConfiguration(java.util.List.of(layer(height, state)), net.minecraft.core.Direction.UP, net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate.ONLY_IN_AIR_PREDICATE, false);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/BlockColumnConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */