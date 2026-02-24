/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import java.util.Map;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.Shapes;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public interface SegmentableBlock
/*    */ {
/*    */   public static final int MIN_SEGMENT = 1;
/*    */   public static final int MAX_SEGMENT = 4;
/* 19 */   public static final IntegerProperty AMOUNT = BlockStateProperties.SEGMENT_AMOUNT;
/*    */   
/*    */   default Function<BlockState, VoxelShape> getShapeCalculator(EnumProperty<Direction> facing, IntegerProperty amount) {
/* 22 */     Map<Direction, VoxelShape> shapes = Shapes.rotateHorizontal(Block.box(0.0D, 0.0D, 0.0D, 8.0D, getShapeHeight(), 8.0D));
/* 23 */     return state -> {
/*    */         VoxelShape shape = Shapes.empty();
/*    */         Direction direction = (Direction)state.getValue((Property)facing);
/*    */         int count = (Integer)state.getValue((Property)amount);
/*    */         for (int i = 0; i < count; i++) {
/*    */           shape = Shapes.or(shape, (VoxelShape)shapes.get(direction));
/*    */           direction = direction.getCounterClockWise();
/*    */         } 
/*    */         return shape.singleEncompassing();
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   default IntegerProperty getSegmentAmountProperty() {
/* 38 */     return AMOUNT;
/*    */   }
/*    */   
/*    */   default double getShapeHeight() {
/* 42 */     return 1.0D;
/*    */   }
/*    */   
/*    */   default boolean canBeReplaced(BlockState state, BlockPlaceContext context, IntegerProperty segment) {
/* 46 */     return (!context.isSecondaryUseActive() && context.getItemInHand().is(state.getBlock().asItem()) && (Integer)state.getValue((Property)segment) < 4);
/*    */   }
/*    */ 
/*    */   
/*    */   default BlockState getStateForPlacement(BlockPlaceContext context, Block block, IntegerProperty segment, EnumProperty<Direction> facing) {
/* 51 */     BlockState state = context.getLevel().getBlockState(context.getClickedPos());
/* 52 */     if (state.is(block)) {
/* 53 */       return (BlockState)state.setValue((Property)segment, Math.min(4, (Integer)state.getValue((Property)segment) + 1));
/*    */     }
/* 55 */     return (BlockState)block.defaultBlockState().setValue((Property)facing, (Comparable)context.getHorizontalDirection().getOpposite());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/SegmentableBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */