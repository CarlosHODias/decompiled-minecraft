/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class AzaleaBlock extends VegetationBlock implements BonemealableBlock {
/* 18 */   public static final MapCodec<AzaleaBlock> CODEC = simpleCodec(AzaleaBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<AzaleaBlock> codec() {
/* 22 */     return CODEC;
/*    */   }
/*    */   
/* 25 */   private static final VoxelShape SHAPE = Shapes.or(
/* 26 */       Block.column(16.0D, 8.0D, 16.0D), 
/* 27 */       Block.column(4.0D, 0.0D, 8.0D));
/*    */ 
/*    */   
/*    */   protected AzaleaBlock(BlockBehaviour.Properties properties) {
/* 31 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 36 */     return SHAPE;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
/* 41 */     return (state.is(Blocks.CLAY) || super.mayPlaceOn(state, level, pos));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/* 46 */     return level.getFluidState(pos.above()).isEmpty();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/* 51 */     return (level.random.nextFloat() < 0.45D);
/*    */   }
/*    */ 
/*    */   
/*    */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 56 */     net.minecraft.world.level.block.grower.TreeGrower.AZALEA.growTree(level, level.getChunkSource().getGenerator(), pos, state, random);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 61 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/AzaleaBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */