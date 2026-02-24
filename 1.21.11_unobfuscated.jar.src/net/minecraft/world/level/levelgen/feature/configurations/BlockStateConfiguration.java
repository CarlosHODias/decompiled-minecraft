/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ 
/*    */ public class BlockStateConfiguration implements FeatureConfiguration {
/*    */   public static final com.mojang.serialization.Codec<BlockStateConfiguration> CODEC;
/*    */   
/*    */   static {
/*  7 */     CODEC = net.minecraft.world.level.block.state.BlockState.CODEC.fieldOf("state").xmap(BlockStateConfiguration::new, c -> c.state).codec();
/*    */   }
/*    */   public final net.minecraft.world.level.block.state.BlockState state;
/*    */   
/*    */   public BlockStateConfiguration(net.minecraft.world.level.block.state.BlockState state) {
/* 12 */     this.state = state;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/BlockStateConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */