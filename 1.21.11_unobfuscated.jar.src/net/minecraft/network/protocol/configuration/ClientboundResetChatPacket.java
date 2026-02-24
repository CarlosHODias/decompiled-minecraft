/*    */ package net.minecraft.network.protocol.configuration;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundResetChatPacket implements Packet<ClientConfigurationPacketListener> {
/*  9 */   public static final ClientboundResetChatPacket INSTANCE = new ClientboundResetChatPacket();
/* 10 */   public static final StreamCodec<ByteBuf, ClientboundResetChatPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PacketType<ClientboundResetChatPacket> type() {
/* 17 */     return ConfigurationPacketTypes.CLIENTBOUND_RESET_CHAT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientConfigurationPacketListener listener) {
/* 22 */     listener.handleResetChat(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/configuration/ClientboundResetChatPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */