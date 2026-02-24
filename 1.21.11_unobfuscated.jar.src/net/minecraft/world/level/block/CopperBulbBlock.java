/*    */ package net.minecraft.world.level.block;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.BooleanProperty;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.redstone.Orientation;
/*    */ 
/*    */ public class CopperBulbBlock extends Block {
/* 19 */   public static final MapCodec<CopperBulbBlock> CODEC = simpleCodec(CopperBulbBlock::new);
/*    */ 
/*    */   
/*    */   protected MapCodec<? extends CopperBulbBlock> codec() {
/* 23 */     return CODEC;
/*    */   }
/*    */   
/* 26 */   public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
/* 27 */   public static final BooleanProperty LIT = BlockStateProperties.LIT;
/*    */   
/*    */   public CopperBulbBlock(BlockBehaviour.Properties properties) {
/* 30 */     super(properties);
/* 31 */     registerDefaultState((BlockState)((BlockState)defaultBlockState().setValue((Property)LIT, false)).setValue((Property)POWERED, false));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
/* 36 */     if (oldState.getBlock() != state.getBlock() && level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 37 */       checkAndFlip(state, serverLevel, pos); }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   protected void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, Orientation orientation, boolean movedByPiston) {
/* 43 */     if (level instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level;
/* 44 */       checkAndFlip(state, serverLevel, pos); }
/*    */   
/*    */   }
/*    */   
/*    */   public void checkAndFlip(BlockState state, ServerLevel level, BlockPos pos) {
/* 49 */     boolean signal = level.hasNeighborSignal(pos);
/*    */     
/* 51 */     if (signal == (Boolean)state.getValue((Property)POWERED)) {
/*    */       return;
/*    */     }
/*    */     
/* 55 */     BlockState newState = state;
/* 56 */     if (!((Boolean)state.getValue((Property)POWERED))) {
/* 57 */       newState = (BlockState)newState.cycle((Property)LIT);
/* 58 */       level.playSound(null, pos, (Boolean)newState.getValue((Property)LIT) ? SoundEvents.COPPER_BULB_TURN_ON : SoundEvents.COPPER_BULB_TURN_OFF, SoundSource.BLOCKS);
/*    */     } 
/* 60 */     level.setBlock(pos, (BlockState)newState.setValue((Property)POWERED, signal), 3);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
/* 65 */     builder.add(new Property[] { (Property)LIT, (Property)POWERED });
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean hasAnalogOutputSignal(BlockState state) {
/* 70 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
/* 75 */     return (Boolean)level.getBlockState(pos).getValue((Property)LIT) ? 15 : 0;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/CopperBulbBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */