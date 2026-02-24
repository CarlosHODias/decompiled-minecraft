/*    */ package net.minecraft.world.level.block;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.ScheduledTickAccess;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.AttachFace;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public abstract class FaceAttachedHorizontalDirectionalBlock extends HorizontalDirectionalBlock {
/* 17 */   public static final EnumProperty<AttachFace> FACE = BlockStateProperties.ATTACH_FACE;
/*    */   
/*    */   protected FaceAttachedHorizontalDirectionalBlock(BlockBehaviour.Properties properties) {
/* 20 */     super(properties);
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract com.mojang.serialization.MapCodec<? extends FaceAttachedHorizontalDirectionalBlock> codec();
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 28 */     return canAttach(level, pos, getConnectedDirection(state).getOpposite());
/*    */   }
/*    */   
/*    */   public static boolean canAttach(LevelReader level, BlockPos pos, Direction direction) {
/* 32 */     BlockPos relative = pos.relative(direction);
/* 33 */     return level.getBlockState(relative).isFaceSturdy((BlockGetter)level, relative, direction.getOpposite());
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 38 */     for (Direction direction : context.getNearestLookingDirections()) {
/*    */       BlockState state;
/* 40 */       if (direction.getAxis() == Direction.Axis.Y) {
/* 41 */         state = (BlockState)((BlockState)defaultBlockState().setValue((Property)FACE, (direction == Direction.UP) ? (Comparable)AttachFace.CEILING : (Comparable)AttachFace.FLOOR)).setValue((Property)FACING, (Comparable)context.getHorizontalDirection());
/*    */       } else {
/* 43 */         state = (BlockState)((BlockState)defaultBlockState().setValue((Property)FACE, (Comparable)AttachFace.WALL)).setValue((Property)FACING, (Comparable)direction.getOpposite());
/*    */       } 
/*    */       
/* 46 */       if (state.canSurvive((LevelReader)context.getLevel(), context.getClickedPos())) {
/* 47 */         return state;
/*    */       }
/*    */     } 
/*    */     
/* 51 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 56 */     if (getConnectedDirection(state).getOpposite() == directionToNeighbour && !state.canSurvive(level, pos)) {
/* 57 */       return Blocks.AIR.defaultBlockState();
/*    */     }
/* 59 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*    */   }
/*    */   
/*    */   protected static Direction getConnectedDirection(BlockState state) {
/* 63 */     switch ((AttachFace)state.getValue((Property)FACE)) {
/*    */       case CEILING:
/* 65 */         return Direction.DOWN;
/*    */       case FLOOR:
/* 67 */         return Direction.UP;
/*    */     } 
/* 69 */     return (Direction)state.getValue((Property)FACING);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/FaceAttachedHorizontalDirectionalBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */