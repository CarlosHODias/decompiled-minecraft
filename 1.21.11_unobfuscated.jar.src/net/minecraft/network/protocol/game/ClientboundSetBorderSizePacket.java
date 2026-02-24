/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.border.WorldBorder;
/*    */ 
/*    */ public class ClientboundSetBorderSizePacket implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetBorderSizePacket> STREAM_CODEC = Packet.codec(ClientboundSetBorderSizePacket::write, ClientboundSetBorderSizePacket::new);
/*    */   
/*    */   private final double size;
/*    */   
/*    */   public ClientboundSetBorderSizePacket(WorldBorder border) {
/* 15 */     this.size = border.getLerpTarget();
/*    */   }
/*    */   
/*    */   private ClientboundSetBorderSizePacket(FriendlyByteBuf input) {
/* 19 */     this.size = input.readDouble();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 23 */     output.writeDouble(this.size);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundSetBorderSizePacket> type() {
/* 28 */     return GamePacketTypes.CLIENTBOUND_SET_BORDER_SIZE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 33 */     listener.handleSetBorderSize(this);
/*    */   }
/*    */   
/*    */   public double getSize() {
/* 37 */     return this.size;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundSetBorderSizePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */