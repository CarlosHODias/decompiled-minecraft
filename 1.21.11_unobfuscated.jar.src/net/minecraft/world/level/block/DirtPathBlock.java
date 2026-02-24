/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class DirtPathBlock extends Block {
/* 18 */   public static final MapCodec<DirtPathBlock> CODEC = simpleCodec(DirtPathBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<DirtPathBlock> codec() {
/* 22 */     return CODEC;
/*    */   }
/*    */   
/* 25 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 15.0D);
/*    */   
/*    */   protected DirtPathBlock(BlockBehaviour.Properties properties) {
/* 28 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean useShapeForLightOcclusion(BlockState state) {
/* 33 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 38 */     if (!defaultBlockState().canSurvive((LevelReader)context.getLevel(), context.getClickedPos())) {
/* 39 */       return Block.pushEntitiesUp(defaultBlockState(), Blocks.DIRT.defaultBlockState(), (net.minecraft.world.level.LevelAccessor)context.getLevel(), context.getClickedPos());
/*    */     }
/* 41 */     return super.getStateForPlacement(context);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 46 */     if (directionToNeighbour == Direction.UP && 
/* 47 */       !state.canSurvive(level, pos)) {
/* 48 */       ticks.scheduleTick(pos, this, 1);
/*    */     }
/*    */     
/* 51 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 56 */     FarmBlock.turnToDirt(null, state, (net.minecraft.world.level.Level)level, pos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 61 */     BlockState aboveState = level.getBlockState(pos.above());
/* 62 */     return (!aboveState.isSolid() || aboveState.getBlock() instanceof FenceGateBlock);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
/* 67 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 72 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/DirtPathBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */