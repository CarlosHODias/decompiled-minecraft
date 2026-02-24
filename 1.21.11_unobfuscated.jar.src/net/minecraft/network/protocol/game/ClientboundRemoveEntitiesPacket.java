/*    */ package net.minecraft.network.protocol.game;
/*    */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*    */ import it.unimi.dsi.fastutil.ints.IntList;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundRemoveEntitiesPacket implements Packet<ClientGamePacketListener> {
/* 12 */   public static final StreamCodec<FriendlyByteBuf, ClientboundRemoveEntitiesPacket> STREAM_CODEC = Packet.codec(ClientboundRemoveEntitiesPacket::write, ClientboundRemoveEntitiesPacket::new);
/*    */   
/*    */   private final IntList entityIds;
/*    */   
/*    */   public ClientboundRemoveEntitiesPacket(IntList ids) {
/* 17 */     this.entityIds = (IntList)new IntArrayList(ids);
/*    */   }
/*    */   
/*    */   public ClientboundRemoveEntitiesPacket(int... ids) {
/* 21 */     this.entityIds = (IntList)new IntArrayList(ids);
/*    */   }
/*    */   
/*    */   private ClientboundRemoveEntitiesPacket(FriendlyByteBuf input) {
/* 25 */     this.entityIds = input.readIntIdList();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 29 */     output.writeIntIdList(this.entityIds);
/*    */   }
/*    */ 
/*    */   
/*    */   public PacketType<ClientboundRemoveEntitiesPacket> type() {
/* 34 */     return GamePacketTypes.CLIENTBOUND_REMOVE_ENTITIES;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 39 */     listener.handleRemoveEntities(this);
/*    */   }
/*    */   
/*    */   public IntList getEntityIds() {
/* 43 */     return this.entityIds;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundRemoveEntitiesPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */