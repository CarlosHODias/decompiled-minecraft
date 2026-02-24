/*    */ package net.minecraft.network.protocol.configuration;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundFinishConfigurationPacket implements Packet<ClientConfigurationPacketListener> {
/*  9 */   public static final ClientboundFinishConfigurationPacket INSTANCE = new ClientboundFinishConfigurationPacket();
/* 10 */   public static final StreamCodec<ByteBuf, ClientboundFinishConfigurationPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PacketType<ClientboundFinishConfigurationPacket> type() {
/* 17 */     return ConfigurationPacketTypes.CLIENTBOUND_FINISH_CONFIGURATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientConfigurationPacketListener listener) {
/* 22 */     listener.handleConfigurationFinished(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isTerminal() {
/* 27 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/configuration/ClientboundFinishConfigurationPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */