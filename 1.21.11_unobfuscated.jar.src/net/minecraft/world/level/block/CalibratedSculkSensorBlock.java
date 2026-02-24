/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.context.BlockPlaceContext;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.entity.CalibratedSculkSensorBlockEntity;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.EnumProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*    */ 
/*    */ public class CalibratedSculkSensorBlock extends SculkSensorBlock {
/* 21 */   public static final MapCodec<CalibratedSculkSensorBlock> CODEC = simpleCodec(CalibratedSculkSensorBlock::new);
/*    */ 
/*    */   
/*    */   public MapCodec<CalibratedSculkSensorBlock> codec() {
/* 25 */     return CODEC;
/*    */   }
/*    */   
/* 28 */   public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;
/*    */   
/*    */   public CalibratedSculkSensorBlock(BlockBehaviour.Properties properties) {
/* 31 */     super(properties);
/* 32 */     registerDefaultState((BlockState)defaultBlockState().setValue((Property)FACING, (Comparable)Direction.NORTH));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 37 */     return (BlockEntity)new CalibratedSculkSensorBlockEntity(worldPosition, blockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public <T extends BlockEntity> net.minecraft.world.level.block.entity.BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 42 */     if (!level.isClientSide()) {
/* 43 */       return createTickerHelper(type, BlockEntityType.CALIBRATED_SCULK_SENSOR, (innerLevel, pos, state, entity) -> VibrationSystem.Ticker.tick(innerLevel, entity.getVibrationData(), entity.getVibrationUser()));
/*    */     }
/*    */     
/* 46 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState getStateForPlacement(BlockPlaceContext context) {
/* 51 */     return (BlockState)super.getStateForPlacement(context).setValue((Property)FACING, (Comparable)context.getHorizontalDirection());
/*    */   }
/*    */ 
/*    */   
/*    */   public int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
/* 56 */     if (direction != state.getValue((Property)FACING)) {
/* 57 */       return super.getSignal(state, level, pos, direction);
/*    */     }
/* 59 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 64 */     super.createBlockStateDefinition(builder);
/* 65 */     builder.add(new Property[] { (Property)FACING });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public BlockState rotate(BlockState state, Rotation rotation) {
/* 71 */     return (BlockState)state.setValue((Property)FACING, (Comparable)rotation.rotate((Direction)state.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   public BlockState mirror(BlockState state, Mirror mirror) {
/* 76 */     return state.rotate(mirror.getRotation((Direction)state.getValue((Property)FACING)));
/*    */   }
/*    */ 
/*    */   
/*    */   public int getActiveTicks() {
/* 81 */     return 10;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/CalibratedSculkSensorBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */