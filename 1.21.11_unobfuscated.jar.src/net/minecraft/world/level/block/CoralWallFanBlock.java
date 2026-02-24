/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class CoralWallFanBlock extends BaseCoralWallFanBlock {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)CoralBlock.DEAD_CORAL_FIELD.forGetter(()), (App)propertiesCodec()).apply((com.mojang.datafixers.kinds.Applicative)i, CoralWallFanBlock::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<CoralWallFanBlock> CODEC;
/*    */   private final Block deadBlock;
/*    */   
/*    */   public com.mojang.serialization.MapCodec<CoralWallFanBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected CoralWallFanBlock(Block deadBlock, net.minecraft.world.level.block.state.BlockBehaviour.Properties properties) {
/* 29 */     super(properties);
/* 30 */     this.deadBlock = deadBlock;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 35 */     tryScheduleDieTick(state, (BlockGetter)level, (ScheduledTickAccess)level, level.random, pos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState state, net.minecraft.server.level.ServerLevel level, BlockPos pos, RandomSource random) {
/* 40 */     if (!scanForWater(state, (BlockGetter)level, pos)) {
/* 41 */       level.setBlock(pos, (BlockState)((BlockState)this.deadBlock.defaultBlockState().setValue((Property)WATERLOGGED, false)).setValue((Property)FACING, state.getValue((Property)FACING)), 2);
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 47 */     if (directionToNeighbour.getOpposite() == state.getValue((Property)FACING) && !state.canSurvive(level, pos)) {
/* 48 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/*    */     
/* 51 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 52 */       ticks.scheduleTick(pos, (net.minecraft.world.level.material.Fluid)net.minecraft.world.level.material.Fluids.WATER, net.minecraft.world.level.material.Fluids.WATER.getTickDelay(level));
/*    */     }
/* 54 */     tryScheduleDieTick(state, (BlockGetter)level, ticks, random, pos);
/*    */     
/* 56 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/CoralWallFanBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */