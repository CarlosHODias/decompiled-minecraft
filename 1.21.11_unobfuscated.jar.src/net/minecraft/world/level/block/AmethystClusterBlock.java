/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import net.minecraft.world.level.material.Fluids;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class AmethystClusterBlock extends AmethystBlock implements SimpleWaterloggedBlock {
/*     */   static {
/*  29 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.FLOAT.fieldOf("height").forGetter(()), (App)Codec.FLOAT.fieldOf("width").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, AmethystClusterBlock::new));
/*     */   }
/*     */ 
/*     */   
/*     */   public static final MapCodec<AmethystClusterBlock> CODEC;
/*     */ 
/*     */   
/*     */   public MapCodec<AmethystClusterBlock> codec() {
/*  37 */     return CODEC;
/*     */   }
/*     */   
/*  40 */   public static final net.minecraft.world.level.block.state.properties.BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
/*  41 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.FACING;
/*     */   
/*     */   private final float height;
/*     */   
/*     */   private final float width;
/*     */   private final java.util.Map<Direction, VoxelShape> shapes;
/*     */   
/*     */   public AmethystClusterBlock(float height, float width, BlockBehaviour.Properties props) {
/*  49 */     super(props);
/*  50 */     registerDefaultState((BlockState)((BlockState)defaultBlockState().setValue((Property)WATERLOGGED, false)).setValue((Property)FACING, (Comparable)Direction.UP));
/*  51 */     this.shapes = Shapes.rotateAll(Block.boxZ(width, (16.0F - height), 16.0D));
/*     */     
/*  53 */     this.height = height;
/*  54 */     this.width = width;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
/*  59 */     return this.shapes.get(state.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/*  64 */     Direction direction = (Direction)state.getValue((Property)FACING);
/*  65 */     BlockPos adjacentPos = pos.relative(direction.getOpposite());
/*  66 */     return level.getBlockState(adjacentPos).isFaceSturdy((BlockGetter)level, adjacentPos, direction);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/*  71 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/*  72 */       ticks.scheduleTick(pos, (net.minecraft.world.level.material.Fluid)Fluids.WATER, Fluids.WATER.getTickDelay(level));
/*     */     }
/*     */     
/*  75 */     if (directionToNeighbour == ((Direction)state.getValue((Property)FACING)).getOpposite() && !state.canSurvive(level, pos)) {
/*  76 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/*  79 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  84 */     Level level = context.getLevel();
/*  85 */     BlockPos pos = context.getClickedPos();
/*  86 */     return (BlockState)((BlockState)defaultBlockState()
/*  87 */       .setValue((Property)WATERLOGGED, (level.getFluidState(pos).getType() == Fluids.WATER)))
/*  88 */       .setValue((Property)FACING, (Comparable)context.getClickedFace());
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/*  93 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/*  98 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected FluidState getFluidState(BlockState state) {
/* 103 */     if ((Boolean)state.getValue((Property)WATERLOGGED)) {
/* 104 */       return Fluids.WATER.getSource(false);
/*     */     }
/* 106 */     return super.getFluidState(state);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 111 */     builder.add(new Property[] { (Property)WATERLOGGED, (Property)FACING });
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/AmethystClusterBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */