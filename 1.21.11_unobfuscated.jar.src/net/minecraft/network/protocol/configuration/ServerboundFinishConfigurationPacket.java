/*    */ package net.minecraft.network.protocol.configuration;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ServerboundFinishConfigurationPacket implements Packet<ServerConfigurationPacketListener> {
/*  9 */   public static final ServerboundFinishConfigurationPacket INSTANCE = new ServerboundFinishConfigurationPacket();
/* 10 */   public static final StreamCodec<ByteBuf, ServerboundFinishConfigurationPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PacketType<ServerboundFinishConfigurationPacket> type() {
/* 17 */     return ConfigurationPacketTypes.SERVERBOUND_FINISH_CONFIGURATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerConfigurationPacketListener listener) {
/* 22 */     listener.handleConfigurationFinished(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isTerminal() {
/* 27 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/configuration/ServerboundFinishConfigurationPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */