/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.Containers;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ 
/*    */ public abstract class AbstractFurnaceBlock extends BaseEntityBlock {
/* 26 */   public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
/* 27 */   public static final BooleanProperty LIT = BlockStateProperties.LIT;
/*    */   
/*    */   protected AbstractFurnaceBlock(BlockBehaviour.Properties properties) {
/* 30 */     super(properties);
/* 31 */     registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)FACING, (Comparable)Direction.NORTH)).setValue((Property)LIT, false));
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends AbstractFurnaceBlock> codec();
/*    */ 
/*    */   
/*    */   protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
/* 39 */     if (!level.isClientSide()) {
/* 40 */       openContainer(level, pos, player);
/*    */     }
/* 42 */     return (InteractionResult)InteractionResult.SUCCESS;
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract void openContainer(Level paramLevel, BlockPos paramBlockPos, Player paramPlayer);
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 49 */     return (BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)context.getHorizontalDirection().getOpposite());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
/* 54 */     Containers.updateNeighboursAfterDestroy(state, (Level)level, pos);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean hasAnalogOutputSignal(BlockState state) {
/* 59 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 64 */     return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(level.getBlockEntity(pos));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState rotate(BlockState state, Rotation rotation) {
/* 69 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected BlockState mirror(BlockState state, Mirror mirror) {
/* 74 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 79 */     builder.add(new Property[] { (Property)FACING, (Property)LIT });
/*    */   }
/*    */   
/*    */   protected static <T extends net.minecraft.world.level.block.entity.BlockEntity> BlockEntityTicker<T> createFurnaceTicker(Level level, BlockEntityType<T> actualType, BlockEntityType<? extends AbstractFurnaceBlockEntity> expectedType) {
/* 83 */     ServerLevel serverLevel = (ServerLevel)level; return (level instanceof ServerLevel) ? createTickerHelper(actualType, expectedType, (innerLevel, pos, state, entity) -> AbstractFurnaceBlockEntity.serverTick(serverLevel, pos, state, entity)) : null;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/AbstractFurnaceBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */