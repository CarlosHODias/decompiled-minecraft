/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.SkullBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.pathfinder.PathComputationType;
/*    */ import net.minecraft.world.level.redstone.Orientation;
/*    */ 
/*    */ public abstract class AbstractSkullBlock extends BaseEntityBlock {
/* 21 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/*    */   private final SkullBlock.Type type;
/*    */   
/*    */   public AbstractSkullBlock(SkullBlock.Type type, BlockBehaviour.Properties properties) {
/* 25 */     super(properties);
/* 26 */     this.type = type;
/* 27 */     registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue((Property)POWERED, false));
/*    */   }
/*    */ 
/*    */   
/*    */   protected abstract MapCodec<? extends AbstractSkullBlock> codec();
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 35 */     return (BlockEntity)new SkullBlockEntity(worldPosition, blockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 40 */     if (level.isClientSide()) {
/* 41 */       boolean isAnimated = (blockState.is(Blocks.DRAGON_HEAD) || 
/* 42 */         blockState.is(Blocks.DRAGON_WALL_HEAD) || 
/* 43 */         blockState.is(Blocks.PIGLIN_HEAD) || 
/* 44 */         blockState.is(Blocks.PIGLIN_WALL_HEAD));
/*    */       
/* 46 */       if (isAnimated) {
/* 47 */         return createTickerHelper(type, BlockEntityType.SKULL, SkullBlockEntity::animation);
/*    */       }
/*    */     } 
/* 50 */     return null;
/*    */   }
/*    */   
/*    */   public SkullBlock.Type getType() {
/* 54 */     return this.type;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPathfindable(BlockState state, PathComputationType type) {
/* 59 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 64 */     builder.add(new Property[] { (Property)POWERED });
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 69 */     return (BlockState)defaultBlockState()
/* 70 */       .setValue((Property)POWERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 75 */     if (level.isClientSide()) {
/*    */       return;
/*    */     }
/*    */     
/* 79 */     boolean signal = level.hasNeighborSignal(pos);
/* 80 */     if (signal != (Boolean)state.getValue((Property)POWERED))
/* 81 */       level.setBlock(pos, (BlockState)state.setValue((Property)POWERED, signal), 2); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/AbstractSkullBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */