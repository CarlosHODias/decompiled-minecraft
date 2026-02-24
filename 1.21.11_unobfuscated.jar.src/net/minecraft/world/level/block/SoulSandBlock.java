/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class SoulSandBlock extends Block {
/* 19 */   public static final MapCodec<SoulSandBlock> CODEC = simpleCodec(SoulSandBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<SoulSandBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */   
/* 26 */   private static final VoxelShape SHAPE = Block.column(16.0D, 0.0D, 14.0D);
/*    */   
/*    */   private static final int BUBBLE_COLUMN_CHECK_DELAY = 20;
/*    */   
/*    */   public SoulSandBlock(BlockBehaviour.Properties properties) {
/* 31 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 36 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
/* 41 */     return Shapes.block();
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getVisualShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 46 */     return Shapes.block();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/* 51 */     BubbleColumnBlock.updateColumn((net.minecraft.world.level.LevelAccessor)level, pos.above(), state);
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 56 */     if (directionToNeighbour == Direction.UP && neighbourState.is(Blocks.WATER)) {
/* 57 */       ticks.scheduleTick(pos, this, 20);
/*    */     }
/*    */     
/* 60 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 65 */     level.scheduleTick(pos, this, 20);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPathfindable(BlockState state, net.minecraft.world.level.pathfinder.PathComputationType type) {
/* 70 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
/* 75 */     return 0.2F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SoulSandBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */