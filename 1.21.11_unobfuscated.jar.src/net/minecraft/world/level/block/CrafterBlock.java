/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.FrontAndTop;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.Container;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.MenuProvider;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.context.BlockPlaceContext;
/*     */ import net.minecraft.world.item.crafting.CraftingInput;
/*     */ import net.minecraft.world.item.crafting.CraftingRecipe;
/*     */ import net.minecraft.world.item.crafting.RecipeCache;
/*     */ import net.minecraft.world.item.crafting.RecipeHolder;
/*     */ import net.minecraft.world.item.crafting.RecipeInput;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.CrafterBlockEntity;
/*     */ import net.minecraft.world.level.block.entity.HopperBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class CrafterBlock extends BaseEntityBlock {
/*  43 */   public static final MapCodec<CrafterBlock> CODEC = simpleCodec(CrafterBlock::new);
/*  44 */   public static final BooleanProperty CRAFTING = BlockStateProperties.CRAFTING;
/*  45 */   public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;
/*  46 */   private static final EnumProperty<FrontAndTop> ORIENTATION = BlockStateProperties.ORIENTATION;
/*     */   private static final int MAX_CRAFTING_TICKS = 6;
/*     */   private static final int CRAFTING_TICK_DELAY = 4;
/*  49 */   private static final RecipeCache RECIPE_CACHE = new RecipeCache(10);
/*     */   
/*     */   private static final int CRAFTER_ADVANCEMENT_DIAMETER = 17;
/*     */   
/*     */   public CrafterBlock(BlockBehaviour.Properties properties) {
/*  54 */     super(properties);
/*  55 */     registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)ORIENTATION, (Comparable)FrontAndTop.NORTH_UP))
/*  56 */         .setValue((Property)TRIGGERED, false))
/*  57 */         .setValue((Property)CRAFTING, false));
/*     */   }
/*     */ 
/*     */   
/*     */   protected MapCodec<CrafterBlock> codec() {
/*  62 */     return CODEC;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/*  67 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/*  72 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*  73 */     if (blockEntity instanceof CrafterBlockEntity) { CrafterBlockEntity crafterBlockEntity = (CrafterBlockEntity)blockEntity;
/*  74 */       return crafterBlockEntity.getRedstoneSignal(); }
/*     */     
/*  76 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, net.minecraft.world.level.redstone.Orientation orientation, boolean movedByPiston) {
/*  81 */     boolean shouldTrigger = level.hasNeighborSignal(pos);
/*  82 */     boolean isTriggered = (Boolean)state.getValue((Property)TRIGGERED);
/*  83 */     BlockEntity blockEntity = level.getBlockEntity(pos);
/*     */     
/*  85 */     if (shouldTrigger && !isTriggered) {
/*  86 */       level.scheduleTick(pos, this, 4);
/*  87 */       level.setBlock(pos, (BlockState)state.setValue((Property)TRIGGERED, true), 2);
/*  88 */       setBlockEntityTriggered(blockEntity, true);
/*  89 */     } else if (!shouldTrigger && isTriggered) {
/*  90 */       level.setBlock(pos, (BlockState)((BlockState)state.setValue((Property)TRIGGERED, false)).setValue((Property)CRAFTING, false), 2);
/*  91 */       setBlockEntityTriggered(blockEntity, false);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
/*  97 */     dispenseFrom(state, level, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 102 */     return level.isClientSide() ? null : createTickerHelper(type, BlockEntityType.CRAFTER, CrafterBlockEntity::serverTick);
/*     */   }
/*     */   
/*     */   private void setBlockEntityTriggered(BlockEntity blockEntity, boolean triggered) {
/* 106 */     if (blockEntity instanceof CrafterBlockEntity) { CrafterBlockEntity crafterBlockEntity = (CrafterBlockEntity)blockEntity;
/* 107 */       crafterBlockEntity.setTriggered(triggered); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 113 */     CrafterBlockEntity crafterBlockEntity = new CrafterBlockEntity(worldPosition, blockState);
/* 114 */     crafterBlockEntity.setTriggered((blockState.hasProperty((Property)TRIGGERED) && (Boolean)blockState.getValue((Property)TRIGGERED)));
/* 115 */     return (BlockEntity)crafterBlockEntity;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 120 */     Direction nearestLookingDirection = context.getNearestLookingDirection().getOpposite();
/* 121 */     switch (nearestLookingDirection) { default: throw new MatchException(null, null);
/*     */       case DOWN: 
/*     */       case UP: 
/* 124 */       case NORTH: case SOUTH: case WEST: case EAST: break; }  Direction verticalDirection = Direction.UP;
/*     */ 
/*     */     
/* 127 */     return (BlockState)((BlockState)defaultBlockState()
/* 128 */       .setValue((Property)ORIENTATION, (Comparable)FrontAndTop.fromFrontAndTop(nearestLookingDirection, verticalDirection)))
/* 129 */       .setValue((Property)TRIGGERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {
/* 134 */     if ((Boolean)state.getValue((Property)TRIGGERED)) {
/* 135 */       level.scheduleTick(pos, this, 4);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 141 */     net.minecraft.world.Containers.updateNeighboursAfterDestroy(state, (Level)level, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 146 */     if (!level.isClientSide()) { BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof CrafterBlockEntity) { CrafterBlockEntity crafter = (CrafterBlockEntity)blockEntity;
/* 147 */         player.openMenu((MenuProvider)crafter); }
/*     */        }
/* 149 */      return (InteractionResult)InteractionResult.SUCCESS;
/*     */   }
/*     */   protected void dispenseFrom(BlockState state, ServerLevel level, BlockPos pos) {
/*     */     CrafterBlockEntity blockEntity;
/* 153 */     BlockEntity blockEntity1 = level.getBlockEntity(pos); if (blockEntity1 instanceof CrafterBlockEntity) { blockEntity = (CrafterBlockEntity)blockEntity1; }
/*     */     else
/*     */     { return; }
/*     */     
/* 157 */     CraftingInput craftInput = blockEntity.asCraftInput();
/* 158 */     Optional<RecipeHolder<CraftingRecipe>> recipe = getPotentialResults(level, craftInput);
/*     */     
/* 160 */     if (recipe.isEmpty()) {
/* 161 */       level.levelEvent(1050, pos, 0);
/*     */       
/*     */       return;
/*     */     } 
/* 165 */     RecipeHolder<CraftingRecipe> pickedRecipe = recipe.get();
/*     */     
/* 167 */     ItemStack results = ((CraftingRecipe)pickedRecipe.value()).assemble((RecipeInput)craftInput, (HolderLookup.Provider)level.registryAccess());
/*     */     
/* 169 */     if (results.isEmpty()) {
/* 170 */       level.levelEvent(1050, pos, 0);
/*     */       
/*     */       return;
/*     */     } 
/* 174 */     blockEntity.setCraftingTicksRemaining(6);
/* 175 */     level.setBlock(pos, (BlockState)state.setValue((Property)CRAFTING, true), 2);
/* 176 */     results.onCraftedBySystem((Level)level);
/*     */ 
/*     */     
/* 179 */     dispenseItem(level, pos, blockEntity, results, state, pickedRecipe);
/*     */ 
/*     */     
/* 182 */     for (ItemStack remainingItem : (Iterable<ItemStack>)((CraftingRecipe)pickedRecipe.value()).getRemainingItems(craftInput)) {
/* 183 */       if (!remainingItem.isEmpty()) {
/* 184 */         dispenseItem(level, pos, blockEntity, remainingItem, state, pickedRecipe);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 189 */     blockEntity.getItems().forEach(it -> {
/*     */           if (it.isEmpty()) {
/*     */             return;
/*     */           }
/*     */           it.shrink(1);
/*     */         });
/* 195 */     blockEntity.setChanged();
/*     */   }
/*     */   
/*     */   public static Optional<RecipeHolder<CraftingRecipe>> getPotentialResults(ServerLevel level, CraftingInput input) {
/* 199 */     return RECIPE_CACHE.get(level, input);
/*     */   }
/*     */   
/*     */   private void dispenseItem(ServerLevel level, BlockPos pos, CrafterBlockEntity blockEntity, ItemStack results, BlockState blockState, RecipeHolder<?> recipe) {
/* 203 */     Direction direction = ((FrontAndTop)blockState.getValue((Property)ORIENTATION)).front();
/* 204 */     Container into = HopperBlockEntity.getContainerAt((Level)level, pos.relative(direction));
/* 205 */     ItemStack remaining = results.copy();
/*     */     
/* 207 */     if (into != null && (into instanceof CrafterBlockEntity || results.getCount() > into.getMaxStackSize(results))) {
/*     */       
/* 209 */       while (!remaining.isEmpty()) {
/* 210 */         ItemStack copy = remaining.copyWithCount(1);
/*     */         
/* 212 */         ItemStack itemStack = HopperBlockEntity.addItem((Container)blockEntity, into, copy, direction.getOpposite());
/*     */         
/* 214 */         if (!itemStack.isEmpty()) {
/*     */           break;
/*     */         }
/* 217 */         remaining.shrink(1);
/*     */       } 
/* 219 */     } else if (into != null) {
/*     */       
/* 221 */       while (!remaining.isEmpty()) {
/* 222 */         int oldSize = remaining.getCount();
/* 223 */         remaining = HopperBlockEntity.addItem((Container)blockEntity, into, remaining, direction.getOpposite());
/*     */         
/* 225 */         if (oldSize == remaining.getCount()) {
/*     */           break;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 232 */     if (!remaining.isEmpty()) {
/* 233 */       Vec3 centerPos = Vec3.atCenterOf((Vec3i)pos);
/* 234 */       Vec3 itemSpawnOffset = centerPos.relative(direction, 0.7D);
/* 235 */       net.minecraft.core.dispenser.DefaultDispenseItemBehavior.spawnItem((Level)level, remaining, 6, direction, (Position)itemSpawnOffset);
/*     */       
/* 237 */       for (ServerPlayer player : (Iterable<ServerPlayer>)level.getEntitiesOfClass(ServerPlayer.class, net.minecraft.world.phys.AABB.ofSize(centerPos, 17.0D, 17.0D, 17.0D))) {
/* 238 */         CriteriaTriggers.CRAFTER_RECIPE_CRAFTED.trigger(player, recipe.id(), (java.util.List)blockEntity.getItems());
/*     */       }
/*     */       
/* 241 */       level.levelEvent(1049, pos, 0);
/* 242 */       level.levelEvent(2010, pos, direction.get3DDataValue());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 248 */     return (BlockState)state.setValue((Property)ORIENTATION, (Comparable)rotation.rotation().rotate((FrontAndTop)state.getValue((Property)ORIENTATION)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 253 */     return (BlockState)state.setValue((Property)ORIENTATION, (Comparable)mirror.rotation().rotate((FrontAndTop)state.getValue((Property)ORIENTATION)));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 258 */     builder.add(new Property[] { (Property)ORIENTATION, (Property)TRIGGERED, (Property)CRAFTING });
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/CrafterBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */