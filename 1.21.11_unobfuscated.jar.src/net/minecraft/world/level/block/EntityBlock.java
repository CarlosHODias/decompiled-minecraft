/*    */ package net.minecraft.world.level.block;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityTicker;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gameevent.GameEventListener;
/*    */ 
/*    */ public interface EntityBlock
/*    */ {
/*    */   BlockEntity newBlockEntity(BlockPos paramBlockPos, BlockState paramBlockState);
/*    */   
/*    */   default <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
/* 17 */     return null;
/*    */   }
/*    */   
/*    */   default <T extends BlockEntity> GameEventListener getListener(ServerLevel level, T blockEntity) {
/* 21 */     if (blockEntity instanceof GameEventListener.Provider) { GameEventListener.Provider<?> provider = (GameEventListener.Provider)blockEntity;
/* 22 */       return provider.getListener(); }
/*    */ 
/*    */     
/* 25 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/EntityBlock.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */