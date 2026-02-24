/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ 
/*    */ public class ClientboundStartConfigurationPacket
/*    */   implements Packet<ClientGamePacketListener>
/*    */ {
/* 13 */   public static final ClientboundStartConfigurationPacket INSTANCE = new ClientboundStartConfigurationPacket();
/* 14 */   public static final StreamCodec<ByteBuf, ClientboundStartConfigurationPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PacketType<ClientboundStartConfigurationPacket> type() {
/* 21 */     return GamePacketTypes.CLIENTBOUND_START_CONFIGURATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 26 */     listener.handleConfigurationStart(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isTerminal() {
/* 31 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundStartConfigurationPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */