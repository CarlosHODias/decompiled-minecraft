/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.shapes.CollisionContext;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ public class LeafLitterBlock extends VegetationBlock implements SegmentableBlock {
/* 19 */   public static final MapCodec<LeafLitterBlock> CODEC = simpleCodec(LeafLitterBlock::new);
/*    */   
/* 21 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*    */   
/*    */   private final Function<BlockState, VoxelShape> shapes;
/*    */   
/*    */   public LeafLitterBlock(BlockBehaviour.Properties properties) {
/* 26 */     super(properties);
/* 27 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)getSegmentAmountProperty(), 1));
/* 28 */     this.shapes = makeShapes();
/*    */   }
/*    */   
/*    */   private Function<BlockState, VoxelShape> makeShapes() {
/* 32 */     return getShapeForEachState(getShapeCalculator(FACING, getSegmentAmountProperty()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected MapCodec<LeafLitterBlock> codec() {
/* 37 */     return CODEC;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState rotate(BlockState state, Rotation rotation) {
/* 42 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState mirror(BlockState state, Mirror mirror) {
/* 47 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
/* 52 */     if (canBeReplaced(state, context, getSegmentAmountProperty())) {
/* 53 */       return true;
/*    */     }
/* 55 */     return super.canBeReplaced(state, context);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 60 */     BlockPos belowPos = pos.below();
/* 61 */     return level.getBlockState(belowPos).isFaceSturdy((BlockGetter)level, belowPos, Direction.UP);
/*    */   }
/*    */ 
/*    */   
/*    */   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 66 */     return this.shapes.apply(state);
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 71 */     return getStateForPlacement(context, this, getSegmentAmountProperty(), FACING);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 76 */     builder.add(new Property[] { (Property)FACING, (Property)getSegmentAmountProperty() });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/LeafLitterBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */