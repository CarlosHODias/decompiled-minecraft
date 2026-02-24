/*    */ package net.minecraft.network.protocol.configuration;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ServerboundAcceptCodeOfConductPacket extends Record implements net.minecraft.network.protocol.Packet<ServerConfigurationPacketListener> {
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/configuration/ServerboundAcceptCodeOfConductPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ServerboundAcceptCodeOfConductPacket;
/*    */   }
/*    */   
/*  9 */   public static final ServerboundAcceptCodeOfConductPacket INSTANCE = new ServerboundAcceptCodeOfConductPacket();
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/configuration/ServerboundAcceptCodeOfConductPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ServerboundAcceptCodeOfConductPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/configuration/ServerboundAcceptCodeOfConductPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/configuration/ServerboundAcceptCodeOfConductPacket;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public static final StreamCodec<io.netty.buffer.ByteBuf, ServerboundAcceptCodeOfConductPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundAcceptCodeOfConductPacket> type() {
/* 14 */     return ConfigurationPacketTypes.SERVERBOUND_ACCEPT_CODE_OF_CONDUCT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerConfigurationPacketListener listener) {
/* 19 */     listener.handleAcceptCodeOfConduct(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/configuration/ServerboundAcceptCodeOfConductPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */