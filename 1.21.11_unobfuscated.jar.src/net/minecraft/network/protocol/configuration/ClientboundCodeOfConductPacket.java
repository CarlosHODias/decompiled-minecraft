/*    */ package net.minecraft.network.protocol.configuration;
/*    */ 
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ClientboundCodeOfConductPacket extends Record implements net.minecraft.network.protocol.Packet<ClientConfigurationPacketListener> {
/*    */   private final String codeOfConduct;
/*    */   
/*  9 */   public ClientboundCodeOfConductPacket(String codeOfConduct) { this.codeOfConduct = codeOfConduct; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket; } public String codeOfConduct() { return this.codeOfConduct; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public static final StreamCodec<io.netty.buffer.ByteBuf, ClientboundCodeOfConductPacket> STREAM_CODEC = StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.STRING_UTF8, ClientboundCodeOfConductPacket::codeOfConduct, ClientboundCodeOfConductPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundCodeOfConductPacket> type() {
/* 17 */     return ConfigurationPacketTypes.CLIENTBOUND_CODE_OF_CONDUCT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientConfigurationPacketListener listener) {
/* 22 */     listener.handleCodeOfConduct(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/configuration/ClientboundCodeOfConductPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */