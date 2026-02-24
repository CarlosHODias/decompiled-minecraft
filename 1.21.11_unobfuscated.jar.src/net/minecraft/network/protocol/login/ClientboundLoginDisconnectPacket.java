/*    */ package net.minecraft.network.protocol.login;
/*    */ 
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.ComponentSerialization;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public final class ClientboundLoginDisconnectPacket extends Record implements Packet<ClientLoginPacketListener> {
/*    */   private final Component reason;
/*    */   
/* 16 */   public ClientboundLoginDisconnectPacket(Component reason) { this.reason = reason; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/login/ClientboundLoginDisconnectPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ClientboundLoginDisconnectPacket; } public Component reason() { return this.reason; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/login/ClientboundLoginDisconnectPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/login/ClientboundLoginDisconnectPacket; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/login/ClientboundLoginDisconnectPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/login/ClientboundLoginDisconnectPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 19 */   } private static final net.minecraft.resources.RegistryOps<com.google.gson.JsonElement> OPS = RegistryAccess.EMPTY.createSerializationContext((DynamicOps)com.mojang.serialization.JsonOps.INSTANCE);
/*    */ 
/*    */ 
/*    */   
/* 23 */   public static final StreamCodec<io.netty.buffer.ByteBuf, ClientboundLoginDisconnectPacket> STREAM_CODEC = StreamCodec.composite(
/*    */       
/* 25 */       ByteBufCodecs.lenientJson(262144).apply(ByteBufCodecs.fromCodec((DynamicOps)OPS, ComponentSerialization.CODEC)), ClientboundLoginDisconnectPacket::reason, ClientboundLoginDisconnectPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundLoginDisconnectPacket> type() {
/* 31 */     return LoginPacketTypes.CLIENTBOUND_LOGIN_DISCONNECT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientLoginPacketListener listener) {
/* 36 */     listener.handleDisconnect(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/login/ClientboundLoginDisconnectPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */