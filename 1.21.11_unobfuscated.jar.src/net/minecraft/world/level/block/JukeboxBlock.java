/*     */ package net.minecraft.world.level.block;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.Containers;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.InteractionResult;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.JukeboxPlayable;
/*     */ import net.minecraft.world.item.component.TypedEntityData;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.entity.BlockEntity;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*     */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*     */ import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.block.state.StateDefinition;
/*     */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*     */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ 
/*     */ public class JukeboxBlock extends BaseEntityBlock {
/*  31 */   public static final MapCodec<JukeboxBlock> CODEC = simpleCodec(JukeboxBlock::new);
/*     */ 
/*     */   
/*     */   public MapCodec<JukeboxBlock> codec() {
/*  35 */     return CODEC;
/*     */   }
/*     */   
/*  38 */   public static final BooleanProperty HAS_RECORD = BlockStateProperties.HAS_RECORD;
/*     */   
/*     */   protected JukeboxBlock(BlockBehaviour.Properties properties) {
/*  41 */     super(properties);
/*  42 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)HAS_RECORD, false));
/*     */   }
/*     */ 
/*     */   
/*     */   public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity by, ItemStack itemStack) {
/*  47 */     super.setPlacedBy(level, pos, state, by, itemStack);
/*  48 */     TypedEntityData<BlockEntityType<?>> blockEntityData = (TypedEntityData<BlockEntityType<?>>)itemStack.get(DataComponents.BLOCK_ENTITY_DATA);
/*  49 */     if (blockEntityData != null && blockEntityData.contains("RecordItem")) {
/*  50 */       level.setBlock(pos, (BlockState)state.setValue((Property)HAS_RECORD, true), 2);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/*  56 */     if ((Boolean)state.getValue((Property)HAS_RECORD)) { BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof JukeboxBlockEntity) { JukeboxBlockEntity jukebox = (JukeboxBlockEntity)blockEntity;
/*  57 */         jukebox.popOutTheItem();
/*  58 */         return (InteractionResult)InteractionResult.SUCCESS; }
/*     */        }
/*     */     
/*  61 */     return (InteractionResult)InteractionResult.PASS;
/*     */   }
/*     */ 
/*     */   
/*     */   protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
/*  66 */     if ((Boolean)state.getValue((Property)HAS_RECORD)) {
/*  67 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/*  70 */     ItemStack toInsert = player.getItemInHand(hand);
/*  71 */     InteractionResult result = JukeboxPlayable.tryInsertIntoJukebox(level, pos, toInsert, player);
/*     */     
/*  73 */     if (!result.consumesAction()) {
/*  74 */       return (InteractionResult)InteractionResult.TRY_WITH_EMPTY_HAND;
/*     */     }
/*     */     
/*  77 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/*  82 */     Containers.updateNeighboursAfterDestroy(state, (Level)level, pos);
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/*  87 */     return (BlockEntity)new JukeboxBlockEntity(worldPosition, blockState);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSignalSource(BlockState state) {
/*  92 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/*  97 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof JukeboxBlockEntity) { JukeboxBlockEntity jukebox = (JukeboxBlockEntity)blockEntity; if (jukebox.getSongPlayer().isPlaying())
/*  98 */         return 15;  }
/*     */     
/* 100 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean hasAnalogOutputSignal(BlockState state) {
/* 105 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 110 */     BlockEntity blockEntity = level.getBlockEntity(pos); if (blockEntity instanceof JukeboxBlockEntity) { JukeboxBlockEntity jukebox = (JukeboxBlockEntity)blockEntity;
/* 111 */       return jukebox.getComparatorOutput(); }
/*     */ 
/*     */     
/* 114 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 119 */     builder.add(new Property[] { (Property)HAS_RECORD });
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 124 */     if ((Boolean)blockState.getValue((Property)HAS_RECORD)) {
/* 125 */       return createTickerHelper(type, BlockEntityType.JUKEBOX, JukeboxBlockEntity::tick);
/*     */     }
/* 127 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/JukeboxBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */