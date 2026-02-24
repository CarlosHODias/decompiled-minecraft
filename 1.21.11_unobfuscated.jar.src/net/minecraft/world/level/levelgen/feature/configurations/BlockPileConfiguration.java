/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ public class BlockPileConfiguration implements FeatureConfiguration {
/*    */   public static final com.mojang.serialization.Codec<BlockPileConfiguration> CODEC;
/*    */   
/*    */   static {
/*  7 */     CODEC = net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider.CODEC.fieldOf("state_provider").xmap(BlockPileConfiguration::new, c -> c.stateProvider).codec();
/*    */   }
/*    */   public final net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider stateProvider;
/*    */   
/*    */   public BlockPileConfiguration(net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider stateProvider) {
/* 12 */     this.stateProvider = stateProvider;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/BlockPileConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */