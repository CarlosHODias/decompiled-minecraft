/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class SoulFireBlock extends BaseFireBlock {
/* 13 */   public static final MapCodec<SoulFireBlock> CODEC = simpleCodec(SoulFireBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<SoulFireBlock> codec() {
/* 17 */     return CODEC;
/*    */   }
/*    */   
/*    */   public SoulFireBlock(BlockBehaviour.Properties properties) {
/* 21 */     super(properties, 2.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, net.minecraft.util.RandomSource random) {
/* 26 */     if (canSurvive(state, level, pos)) {
/* 27 */       return defaultBlockState();
/*    */     }
/*    */     
/* 30 */     return Blocks.AIR.defaultBlockState();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 35 */     return canSurviveOnBlock(level.getBlockState(pos.below()));
/*    */   }
/*    */   
/*    */   public static boolean canSurviveOnBlock(BlockState state) {
/* 39 */     return state.is(BlockTags.SOUL_FIRE_BASE_BLOCKS);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canBurn(BlockState state) {
/* 44 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SoulFireBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */