/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.stats.Stats;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.item.ItemEntity;
/*     */ import net.minecraft.world.entity.monster.Shulker;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.DyeColor;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.ShulkerBoxBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.level.storage.loot.LootParams;
/*     */ import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
/*     */ import net.minecraft.world.phys.AABB;
/*     */ import net.minecraft.world.phys.shapes.CollisionContext;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ public class ShulkerBoxBlock extends BaseEntityBlock {
/*     */   public static final MapCodec<ShulkerBoxBlock> CODEC;
/*     */   
/*     */   static {
/*  43 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DyeColor.CODEC.optionalFieldOf("color").forGetter(()), (App)propertiesCodec()).apply((Applicative)i, ()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MapCodec<ShulkerBoxBlock> codec() {
/*  50 */     return CODEC;
/*     */   }
/*     */   
/*  53 */   public static final java.util.Map<Direction, VoxelShape> SHAPES_OPEN_SUPPORT = Shapes.rotateAll(Block.boxZ(16.0D, 0.0D, 1.0D));
/*     */   
/*  55 */   public static final EnumProperty<Direction> FACING = DirectionalBlock.FACING;
/*     */   
/*  57 */   public static final Identifier CONTENTS = Identifier.withDefaultNamespace("contents");
/*     */   
/*     */   private final DyeColor color;
/*     */   
/*     */   public ShulkerBoxBlock(DyeColor color, BlockBehaviour.Properties properties) {
/*  62 */     super(properties);
/*  63 */     this.color = color;
/*  64 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.UP));
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  69 */     return (BlockEntity)new ShulkerBoxBlockEntity(this.color, worldPosition, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/*  74 */     return createTickerHelper(type, BlockEntityType.SHULKER_BOX, ShulkerBoxBlockEntity::tick);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, net.minecraft.world.phys.BlockHitResult hitResult) {
/*  79 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level; BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof ShulkerBoxBlockEntity) { ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)blockEntity; if (canOpen(state, level, pos, shulkerBoxBlockEntity))
/*  80 */         { player.openMenu((MenuProvider)shulkerBoxBlockEntity);
/*  81 */           player.awardStat(Stats.OPEN_SHULKER_BOX);
/*  82 */           net.minecraft.world.entity.monster.piglin.PiglinAi.angerNearbyPiglins(serverLevel, player, true); }  }
/*     */        }
/*  84 */      return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   
/*     */   private static boolean canOpen(BlockState state, Level level, BlockPos pos, ShulkerBoxBlockEntity blockEntity) {
/*  88 */     if (blockEntity.getAnimationStatus() != ShulkerBoxBlockEntity.AnimationStatus.CLOSED) {
/*  89 */       return true;
/*     */     }
/*     */     
/*  92 */     AABB lidOpenBoundingBox = Shulker.getProgressDeltaAabb(1.0F, (Direction)state.getValue((Property)FACING), 0.0F, 0.5F, pos.getBottomCenter()).deflate(1.0E-6D);
/*  93 */     return level.noCollision(lidOpenBoundingBox);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/*  98 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)context.getClickedFace());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 103 */     builder.add(new Property[] { (Property)FACING });
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
/* 108 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/* 109 */     if (blockEntity instanceof ShulkerBoxBlockEntity) { ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)blockEntity;
/* 110 */       if (!level.isClientSide() && player.preventsBlockDrops() && !shulkerBoxBlockEntity.isEmpty()) {
/*     */         
/* 112 */         ItemStack itemStack = getColoredItemStack(getColor());
/* 113 */         itemStack.applyComponents(blockEntity.collectComponents());
/*     */         
/* 115 */         ItemEntity entity = new ItemEntity(level, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, itemStack);
/* 116 */         entity.setDefaultPickUpDelay();
/* 117 */         level.addFreshEntity((Entity)entity);
/*     */       } else {
/* 119 */         shulkerBoxBlockEntity.unpackLootTable(player);
/*     */       }  }
/*     */     
/* 122 */     return super.playerWillDestroy(level, pos, state, player);
/*     */   }
/*     */ 
/*     */   
/*     */   protected java.util.List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
/* 127 */     BlockEntity blockEntity = (BlockEntity)params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
/*     */     
/* 129 */     if (blockEntity instanceof ShulkerBoxBlockEntity) { ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)blockEntity;
/* 130 */       params = params.withDynamicDrop(CONTENTS, output -> {
/*     */             for (int i = 0; i < shulkerBoxBlockEntity.getContainerSize(); i++) {
/*     */               output.accept(shulkerBoxBlockEntity.getItem(i));
/*     */             }
/*     */           }); }
/*     */ 
/*     */     
/* 137 */     return super.getDrops(state, params);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 142 */     net.minecraft.world.Containers.updateNeighboursAfterDestroy(state, (Level)level, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getBlockSupportShape(BlockState state, BlockGetter level, BlockPos pos) {
/* 147 */     BlockEntity entity = level.getBlockEntity(pos);
/* 148 */     if (entity instanceof ShulkerBoxBlockEntity) { ShulkerBoxBlockEntity shulker = (ShulkerBoxBlockEntity)entity; if (!shulker.isClosed())
/* 149 */         return SHAPES_OPEN_SUPPORT.get(((Direction)state.getValue((Property)FACING)).getOpposite());  }
/*     */     
/* 151 */     return Shapes.block();
/*     */   }
/*     */ 
/*     */   
/*     */   protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
/* 156 */     BlockEntity entity = level.getBlockEntity(pos);
/* 157 */     if (entity instanceof ShulkerBoxBlockEntity) { ShulkerBoxBlockEntity shulkerBoxBlockEntity = (ShulkerBoxBlockEntity)entity;
/* 158 */       return Shapes.create(shulkerBoxBlockEntity.getBoundingBox(state)); }
/*     */     
/* 160 */     return Shapes.block();
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean propagatesSkylightDown(BlockState state) {
/* 165 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/* 170 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 175 */     return net.minecraft.world.inventory.AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
/*     */   }
/*     */   
/*     */   public static Block getBlockByColor(DyeColor color) {
/* 179 */     if (color == null) {
/* 180 */       return Blocks.SHULKER_BOX;
/*     */     }
/* 182 */     switch (color) { default: throw new MatchException(null, null);case WHITE: case ORANGE: case MAGENTA: case LIGHT_BLUE: case YELLOW: case LIME: case PINK: case GRAY: case LIGHT_GRAY: case CYAN: case BLUE: case BROWN: case GREEN: case RED: case BLACK: case PURPLE: break; }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 198 */       Blocks.PURPLE_SHULKER_BOX;
/*     */   }
/*     */ 
/*     */   
/*     */   public DyeColor getColor() {
/* 203 */     return this.color;
/*     */   }
/*     */   
/*     */   public static ItemStack getColoredItemStack(DyeColor color) {
/* 207 */     return new ItemStack(getBlockByColor(color));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 212 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 217 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/ShulkerBoxBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */