/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundMountScreenOpenPacket implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundMountScreenOpenPacket> STREAM_CODEC = Packet.codec(ClientboundMountScreenOpenPacket::write, ClientboundMountScreenOpenPacket::new);
/*    */   
/*    */   private final int containerId;
/*    */   private final int inventoryColumns;
/*    */   private final int entityId;
/*    */   
/*    */   public ClientboundMountScreenOpenPacket(int containerId, int inventoryColumns, int entityId) {
/* 16 */     this.containerId = containerId;
/* 17 */     this.inventoryColumns = inventoryColumns;
/* 18 */     this.entityId = entityId;
/*    */   }
/*    */   
/*    */   private ClientboundMountScreenOpenPacket(FriendlyByteBuf input) {
/* 22 */     this.containerId = input.readContainerId();
/* 23 */     this.inventoryColumns = input.readVarInt();
/* 24 */     this.entityId = input.readInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 28 */     output.writeContainerId(this.containerId);
/* 29 */     output.writeVarInt(this.inventoryColumns);
/* 30 */     output.writeInt(this.entityId);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundMountScreenOpenPacket> type() {
/* 35 */     return GamePacketTypes.CLIENTBOUND_MOUNT_SCREEN_OPEN;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 40 */     listener.handleMountScreenOpen(this);
/*    */   }
/*    */   
/*    */   public int getContainerId() {
/* 44 */     return this.containerId;
/*    */   }
/*    */   
/*    */   public int getInventoryColumns() {
/* 48 */     return this.inventoryColumns;
/*    */   }
/*    */   
/*    */   public int getEntityId() {
/* 52 */     return this.entityId;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundMountScreenOpenPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */