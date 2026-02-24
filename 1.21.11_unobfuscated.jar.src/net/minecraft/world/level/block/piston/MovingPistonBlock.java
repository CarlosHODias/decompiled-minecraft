/*     */ package net.minecraft.world.level.block.piston;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Collections;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.BaseEntityBlock;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Mirror;
/*     */ import net.minecraft.world.level.block.RenderShape;
/*     */ import net.minecraft.world.level.block.Rotation;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.PistonType;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class MovingPistonBlock extends BaseEntityBlock {
/*  38 */   public static final MapCodec<MovingPistonBlock> CODEC = simpleCodec(MovingPistonBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<MovingPistonBlock> codec() {
/*  42 */     return CODEC;
/*     */   }
/*     */   
/*  45 */   public static final EnumProperty<Direction> FACING = PistonHeadBlock.FACING;
/*  46 */   public static final EnumProperty<PistonType> TYPE = PistonHeadBlock.TYPE;
/*     */   
/*     */   public MovingPistonBlock(BlockBehaviour.Properties properties) {
/*  49 */     super(properties);
/*  50 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)TYPE, (Comparable)PistonType.DEFAULT));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  55 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BlockEntity newMovingBlockEntity(BlockPos position, BlockState blockState, BlockState movedState, Direction direction, boolean extending, boolean isSourcePiston) {
/*  60 */     return new PistonMovingBlockEntity(position, blockState, movedState, direction, extending, isSourcePiston);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/*  65 */     return createTickerHelper(type, BlockEntityType.PISTON, PistonMovingBlockEntity::tick);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
/*  71 */     BlockPos relative = pos.relative(((Direction)state.getValue((Property)FACING)).getOpposite());
/*  72 */     BlockState blockState = level.getBlockState(relative);
/*  73 */     if (blockState.getBlock() instanceof PistonBaseBlock && (Boolean)blockState.getValue((Property)PistonBaseBlock.EXTENDED)) {
/*  74 */       level.removeBlock(relative, false);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  81 */     if (!level.isClientSide() && level.getBlockEntity(pos) == null) {
/*     */       
/*  83 */       level.removeBlock(pos, false);
/*  84 */       return (InteractionResult)InteractionResult.CONSUME;
/*     */     } 
/*  86 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected java.util.List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
/*  92 */     PistonMovingBlockEntity entity = getBlockEntity((BlockGetter)params.getLevel(), BlockPos.containing((Position)params.getParameter(LootContextParams.ORIGIN)));
/*  93 */     if (entity == null) {
/*  94 */       return Collections.emptyList();
/*     */     }
/*     */     
/*  97 */     return entity.getMovedState().getDrops(params);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 103 */     return Shapes.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 108 */     PistonMovingBlockEntity blockEntity = getBlockEntity(level, pos);
/* 109 */     if (blockEntity != null) {
/* 110 */       return blockEntity.getCollisionShape(level, pos);
/*     */     }
/* 112 */     return Shapes.empty();
/*     */   }
/*     */   
/*     */   private PistonMovingBlockEntity getBlockEntity(BlockGetter level, BlockPos pos) {
/* 116 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 117 */     if (blockEntity instanceof PistonMovingBlockEntity) {
/* 118 */       return (PistonMovingBlockEntity)blockEntity;
/*     */     }
/* 120 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   protected RenderShape getRenderShape(BlockState state) {
/* 125 */     return RenderShape.INVISIBLE;
/*     */   }
/*     */ 
/*     */   
/*     */   protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
/* 130 */     return ItemStack.EMPTY;
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 135 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 140 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 145 */     builder.add(new Property[] { (Property)FACING, (Property)TYPE });
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 150 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/piston/MovingPistonBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */