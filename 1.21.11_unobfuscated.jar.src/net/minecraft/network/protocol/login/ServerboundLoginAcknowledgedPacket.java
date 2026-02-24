/*    */ package net.minecraft.network.protocol.login;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ServerboundLoginAcknowledgedPacket implements Packet<ServerLoginPacketListener> {
/*  9 */   public static final ServerboundLoginAcknowledgedPacket INSTANCE = new ServerboundLoginAcknowledgedPacket();
/* 10 */   public static final StreamCodec<ByteBuf, ServerboundLoginAcknowledgedPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PacketType<ServerboundLoginAcknowledgedPacket> type() {
/* 17 */     return LoginPacketTypes.SERVERBOUND_LOGIN_ACKNOWLEDGED;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerLoginPacketListener listener) {
/* 22 */     listener.handleLoginAcknowledgement(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isTerminal() {
/* 27 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/login/ServerboundLoginAcknowledgedPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */