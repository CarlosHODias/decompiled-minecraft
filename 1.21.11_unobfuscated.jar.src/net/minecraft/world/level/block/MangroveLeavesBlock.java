/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class MangroveLeavesBlock extends TintedParticleLeavesBlock implements BonemealableBlock {
/*    */   static {
/* 14 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ExtraCodecs.floatRange(0.0F, 1.0F).fieldOf("leaf_particle_chance").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, MangroveLeavesBlock::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<MangroveLeavesBlock> CODEC;
/*    */   
/*    */   public com.mojang.serialization.MapCodec<MangroveLeavesBlock> codec() {
/* 21 */     return CODEC;
/*    */   }
/*    */   
/*    */   public MangroveLeavesBlock(float leafParticleChance, BlockBehaviour.Properties properties) {
/* 25 */     super(leafParticleChance, properties);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 30 */     return level.getBlockState(pos.below()).isAir();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(net.minecraft.world.level.Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 35 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(net.minecraft.server.level.ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 40 */     level.setBlock(pos.below(), MangrovePropaguleBlock.createNewHangingPropagule(), 2);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockPos getParticlePos(BlockPos blockPos) {
/* 45 */     return blockPos.below();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/MangroveLeavesBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */