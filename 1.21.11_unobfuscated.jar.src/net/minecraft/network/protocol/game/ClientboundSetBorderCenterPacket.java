/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.border.WorldBorder;
/*    */ 
/*    */ public class ClientboundSetBorderCenterPacket implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetBorderCenterPacket> STREAM_CODEC = Packet.codec(ClientboundSetBorderCenterPacket::write, ClientboundSetBorderCenterPacket::new);
/*    */   
/*    */   private final double newCenterX;
/*    */   private final double newCenterZ;
/*    */   
/*    */   public ClientboundSetBorderCenterPacket(WorldBorder border) {
/* 16 */     this.newCenterX = border.getCenterX();
/* 17 */     this.newCenterZ = border.getCenterZ();
/*    */   }
/*    */   
/*    */   private ClientboundSetBorderCenterPacket(FriendlyByteBuf input) {
/* 21 */     this.newCenterX = input.readDouble();
/* 22 */     this.newCenterZ = input.readDouble();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 26 */     output.writeDouble(this.newCenterX);
/* 27 */     output.writeDouble(this.newCenterZ);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundSetBorderCenterPacket> type() {
/* 32 */     return GamePacketTypes.CLIENTBOUND_SET_BORDER_CENTER;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 37 */     listener.handleSetBorderCenter(this);
/*    */   }
/*    */   
/*    */   public double getNewCenterZ() {
/* 41 */     return this.newCenterZ;
/*    */   }
/*    */   
/*    */   public double getNewCenterX() {
/* 45 */     return this.newCenterX;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundSetBorderCenterPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */