/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.IntegerProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class FlowerBedBlock extends VegetationBlock implements BonemealableBlock, SegmentableBlock {
/*  24 */   public static final MapCodec<FlowerBedBlock> CODEC = simpleCodec(FlowerBedBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<FlowerBedBlock> codec() {
/*  28 */     return CODEC;
/*     */   }
/*     */   
/*  31 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*  32 */   public static final IntegerProperty AMOUNT = BlockStateProperties.FLOWER_AMOUNT;
/*     */   
/*     */   private final Function<BlockState, VoxelShape> shapes;
/*     */   
/*     */   protected FlowerBedBlock(BlockBehaviour.Properties properties) {
/*  37 */     super(properties);
/*  38 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)AMOUNT, 1));
/*     */     
/*  40 */     this.shapes = makeShapes();
/*     */   }
/*     */   
/*     */   private Function<BlockState, VoxelShape> makeShapes() {
/*  44 */     return getShapeForEachState(getShapeCalculator(FACING, AMOUNT));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState rotate(BlockState state, Rotation rotation) {
/*  49 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState mirror(BlockState state, Mirror mirror) {
/*  54 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
/*  59 */     if (canBeReplaced(state, context, AMOUNT)) {
/*  60 */       return true;
/*     */     }
/*  62 */     return super.canBeReplaced(state, context);
/*     */   }
/*     */ 
/*     */   
/*     */   public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  67 */     return this.shapes.apply(state);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getShapeHeight() {
/*  72 */     return 3.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public IntegerProperty getSegmentAmountProperty() {
/*  77 */     return AMOUNT;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  82 */     return getStateForPlacement(context, this, AMOUNT, FACING);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/*  87 */     builder.add(new Property[] { (Property)FACING, (Property)AMOUNT });
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
/*  97 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
/* 102 */     int currentAmount = (Integer)state.getValue((Property)AMOUNT);
/* 103 */     if (currentAmount < 4) {
/* 104 */       level.setBlock(pos, (BlockState)state.setValue((Property)AMOUNT, currentAmount + 1), 2);
/*     */     } else {
/* 106 */       popResource((Level)level, pos, new ItemStack(this));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/FlowerBedBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */