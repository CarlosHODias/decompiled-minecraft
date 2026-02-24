/*    */ package net.minecraft.client;
/*    */ 
/*    */ import java.util.function.Consumer;
/*    */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.game.ServerboundBlockEntityTagQueryPacket;
/*    */ import net.minecraft.network.protocol.game.ServerboundEntityTagQueryPacket;
/*    */ 
/*    */ 
/*    */ public class DebugQueryHandler
/*    */ {
/*    */   private final ClientPacketListener connection;
/* 15 */   private int transactionId = -1;
/*    */   
/*    */   private Consumer<CompoundTag> callback;
/*    */   
/*    */   public DebugQueryHandler(ClientPacketListener connection) {
/* 20 */     this.connection = connection;
/*    */   }
/*    */   
/*    */   public boolean handleResponse(int transactionId, CompoundTag tag) {
/* 24 */     if (this.transactionId == transactionId && this.callback != null) {
/* 25 */       this.callback.accept(tag);
/* 26 */       this.callback = null;
/* 27 */       return true;
/*    */     } 
/*    */     
/* 30 */     return false;
/*    */   }
/*    */   
/*    */   private int startTransaction(Consumer<CompoundTag> callback) {
/* 34 */     this.callback = callback;
/* 35 */     return this.transactionId++;
/*    */   }
/*    */   
/*    */   public void queryEntityTag(int entityId, Consumer<CompoundTag> callback) {
/* 39 */     int transactionId = startTransaction(callback);
/* 40 */     this.connection.send((Packet)new ServerboundEntityTagQueryPacket(transactionId, entityId));
/*    */   }
/*    */   
/*    */   public void queryBlockEntityTag(BlockPos blockPos, Consumer<CompoundTag> callback) {
/* 44 */     int transactionId = startTransaction(callback);
/* 45 */     this.connection.send((Packet)new ServerboundBlockEntityTagQueryPacket(transactionId, blockPos));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/DebugQueryHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */