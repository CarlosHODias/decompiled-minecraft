/*    */ package net.minecraft.client.multiplayer.prediction;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*    */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*    */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.player.LocalPlayer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BlockStatePredictionHandler implements AutoCloseable {
/* 13 */   private final Long2ObjectOpenHashMap<ServerVerifiedState> serverVerifiedStates = new Long2ObjectOpenHashMap();
/*    */   private int currentSequenceNr;
/*    */   private boolean isPredicting;
/*    */   
/*    */   public void retainKnownServerState(BlockPos pos, BlockState state, LocalPlayer player) {
/* 18 */     this.serverVerifiedStates.compute(pos.asLong(), (key, serverVerifiedState) -> (serverVerifiedState != null) ? serverVerifiedState.setSequence(this.currentSequenceNr) : new ServerVerifiedState(this.currentSequenceNr, state, state.position()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean updateKnownServerState(BlockPos pos, BlockState blockState) {
/* 27 */     ServerVerifiedState serverVerifiedState = (ServerVerifiedState)this.serverVerifiedStates.get(pos.asLong());
/* 28 */     if (serverVerifiedState == null) {
/* 29 */       return false;
/*    */     }
/* 31 */     serverVerifiedState.setBlockState(blockState);
/* 32 */     return true;
/*    */   }
/*    */   
/*    */   public void endPredictionsUpTo(int sequence, ClientLevel clientLevel) {
/* 36 */     ObjectIterator<Long2ObjectMap.Entry<ServerVerifiedState>> stateIterator = this.serverVerifiedStates.long2ObjectEntrySet().iterator();
/* 37 */     while (stateIterator.hasNext()) {
/* 38 */       Long2ObjectMap.Entry<ServerVerifiedState> next = (Long2ObjectMap.Entry<ServerVerifiedState>)stateIterator.next();
/* 39 */       ServerVerifiedState serverVerifiedState = (ServerVerifiedState)next.getValue();
/* 40 */       if (serverVerifiedState.sequence <= sequence) {
/* 41 */         BlockPos pos = BlockPos.of(next.getLongKey());
/* 42 */         stateIterator.remove();
/*    */         
/* 44 */         clientLevel.syncBlockState(pos, serverVerifiedState.blockState, serverVerifiedState.playerPos);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   public BlockStatePredictionHandler startPredicting() {
/* 50 */     this.currentSequenceNr++;
/* 51 */     this.isPredicting = true;
/* 52 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 57 */     this.isPredicting = false;
/*    */   }
/*    */   
/*    */   public int currentSequence() {
/* 61 */     return this.currentSequenceNr;
/*    */   }
/*    */   
/*    */   public boolean isPredicting() {
/* 65 */     return this.isPredicting;
/*    */   }
/*    */   
/*    */   private static class ServerVerifiedState {
/*    */     private final Vec3 playerPos;
/*    */     private int sequence;
/*    */     private BlockState blockState;
/*    */     
/*    */     private ServerVerifiedState(int sequence, BlockState blockState, Vec3 playerPos) {
/* 74 */       this.sequence = sequence;
/* 75 */       this.blockState = blockState;
/* 76 */       this.playerPos = playerPos;
/*    */     }
/*    */     
/*    */     private ServerVerifiedState setSequence(int sequence) {
/* 80 */       this.sequence = sequence;
/* 81 */       return this;
/*    */     }
/*    */     
/*    */     private void setBlockState(BlockState blockState) {
/* 85 */       this.blockState = blockState;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/prediction/BlockStatePredictionHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */