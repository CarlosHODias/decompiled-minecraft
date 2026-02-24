/*    */ package net.minecraft.world.level.block.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.CalibratedSculkSensorBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
/*    */ 
/*    */ public class CalibratedSculkSensorBlockEntity
/*    */   extends SculkSensorBlockEntity {
/*    */   public CalibratedSculkSensorBlockEntity(BlockPos worldPosition, BlockState blockState) {
/* 17 */     super(BlockEntityType.CALIBRATED_SCULK_SENSOR, worldPosition, blockState);
/*    */   }
/*    */ 
/*    */   
/*    */   public VibrationSystem.User createVibrationUser() {
/* 22 */     return new VibrationUser(getBlockPos());
/*    */   }
/*    */   
/*    */   protected class VibrationUser extends SculkSensorBlockEntity.VibrationUser {
/*    */     public VibrationUser(BlockPos blockPos) {
/* 27 */       super(blockPos);
/*    */     }
/*    */ 
/*    */     
/*    */     public int getListenerRadius() {
/* 32 */       return 16;
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean canReceiveVibration(ServerLevel level, BlockPos pos, Holder<GameEvent> event, GameEvent.Context context) {
/* 37 */       int comparisonType = getBackSignal((Level)level, this.blockPos, CalibratedSculkSensorBlockEntity.this.getBlockState());
/*    */       
/* 39 */       if (comparisonType != 0 && VibrationSystem.getGameEventFrequency(event) != comparisonType) {
/* 40 */         return false;
/*    */       }
/*    */       
/* 43 */       return super.canReceiveVibration(level, pos, event, context);
/*    */     }
/*    */     
/*    */     private int getBackSignal(Level level, BlockPos pos, BlockState state) {
/* 47 */       Direction direction = ((Direction)state.getValue((Property)CalibratedSculkSensorBlock.FACING)).getOpposite();
/* 48 */       return level.getSignal(pos.relative(direction), direction);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/CalibratedSculkSensorBlockEntity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */