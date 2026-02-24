/*     */ package net.minecraft.world.level.block.piston;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.ScheduledTickAccess;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.DirectionalBlock;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.PistonType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.redstone.ExperimentalRedstoneUtils;
/*     */ import net.minecraft.world.level.redstone.Orientation;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class PistonHeadBlock extends DirectionalBlock {
/*  36 */   public static final MapCodec<PistonHeadBlock> CODEC = simpleCodec(PistonHeadBlock::new);
/*     */ 
/*     */   
/*     */   protected MapCodec<PistonHeadBlock> codec() {
/*  40 */     return CODEC;
/*     */   }
/*     */   
/*  43 */   public static final EnumProperty<PistonType> TYPE = BlockStateProperties.PISTON_TYPE;
/*  44 */   public static final BooleanProperty SHORT = BlockStateProperties.SHORT;
/*     */   
/*     */   public static final int PLATFORM_THICKNESS = 4;
/*     */   
/*  48 */   private static final VoxelShape SHAPE_PLATFORM = Block.boxZ(16.0D, 0.0D, 4.0D);
/*  49 */   private static final Map<Direction, VoxelShape> SHAPES_SHORT = Shapes.rotateAll(Shapes.or(SHAPE_PLATFORM, 
/*     */         
/*  51 */         Block.boxZ(4.0D, 4.0D, 16.0D)));
/*     */ 
/*     */   
/*  54 */   private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateAll(Shapes.or(SHAPE_PLATFORM, 
/*     */         
/*  56 */         Block.boxZ(4.0D, 4.0D, 20.0D)));
/*     */ 
/*     */   
/*     */   public PistonHeadBlock(BlockBehaviour.Properties properties) {
/*  60 */     super(properties);
/*  61 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)TYPE, (Comparable)PistonType.DEFAULT)).setValue((Property)SHORT, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean useShapeForLightOcclusion(BlockState state) {
/*  66 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/*  71 */     return ((Boolean)state.getValue((Property)SHORT) ? SHAPES_SHORT : SHAPES).get(state.getValue((Property)FACING));
/*     */   }
/*     */   
/*     */   private boolean isFittingBase(BlockState armState, BlockState potentialBase) {
/*  75 */     Block baseBlock = (armState.getValue((Property)TYPE) == PistonType.DEFAULT) ? Blocks.PISTON : Blocks.STICKY_PISTON;
/*  76 */     return (potentialBase.is(baseBlock) && (Boolean)potentialBase.getValue((Property)PistonBaseBlock.EXTENDED) && potentialBase.getValue((Property)FACING) == armState.getValue((Property)FACING));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/*  81 */     if (!level.isClientSide() && player.preventsBlockDrops()) {
/*  82 */       BlockPos basePos = pos.relative(((Direction)state.getValue((Property)FACING)).getOpposite());
/*  83 */       if (isFittingBase(state, level.getBlockState(basePos))) {
/*  84 */         level.destroyBlock(basePos, false);
/*     */       }
/*     */     } 
/*  87 */     return super.playerWillDestroy(level, pos, state, player);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/*  93 */     BlockPos basePos = pos.relative(((Direction)state.getValue((Property)FACING)).getOpposite());
/*  94 */     if (isFittingBase(state, level.getBlockState(basePos))) {
/*  95 */       level.destroyBlock(basePos, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
/* 101 */     if (directionToNeighbour.getOpposite() == state.getValue((Property)FACING) && 
/* 102 */       !state.canSurvive(level, pos)) {
/* 103 */       return Blocks.AIR.defaultBlockState();
/*     */     }
/*     */     
/* 106 */     return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
/* 111 */     BlockState base = level.getBlockState(pos.relative(((Direction)state.getValue((Property)FACING)).getOpposite()));
/*     */     
/* 113 */     return (isFittingBase(state, base) || (base.is(Blocks.MOVING_PISTON) && base.getValue((Property)FACING) == state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 118 */     if (state.canSurvive((LevelReader)level, pos)) {
/* 119 */       level.neighborChanged(pos.relative(((Direction)state.getValue((Property)FACING)).getOpposite()), block, ExperimentalRedstoneUtils.withFront(orientation, ((Direction)state.getValue((Property)FACING)).getOpposite()));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 125 */     return new ItemStack((state.getValue((Property)TYPE) == PistonType.STICKY) ? (ItemLike)Blocks.STICKY_PISTON : (ItemLike)Blocks.PISTON);
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 130 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 135 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 140 */     builder.add(new Property[] { (Property)FACING, (Property)TYPE, (Property)SHORT });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, net.minecraft.world.level.pathfinder.PathComputationType type) {
/* 145 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/piston/PistonHeadBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */