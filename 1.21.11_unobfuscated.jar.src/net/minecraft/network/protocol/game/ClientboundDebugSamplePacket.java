/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public final class ClientboundDebugSamplePacket extends Record implements net.minecraft.network.protocol.Packet<ClientGamePacketListener> {
/*    */   private final long[] sample;
/*    */   private final net.minecraft.util.debugchart.RemoteDebugSampleType debugSampleType;
/*    */   
/*  9 */   public ClientboundDebugSamplePacket(long[] sample, net.minecraft.util.debugchart.RemoteDebugSampleType debugSampleType) { this.sample = sample; this.debugSampleType = debugSampleType; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundDebugSamplePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugSamplePacket; } public long[] sample() { return this.sample; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundDebugSamplePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugSamplePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundDebugSamplePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundDebugSamplePacket;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.util.debugchart.RemoteDebugSampleType debugSampleType() { return this.debugSampleType; }
/* 10 */    public static final net.minecraft.network.codec.StreamCodec<FriendlyByteBuf, ClientboundDebugSamplePacket> STREAM_CODEC = net.minecraft.network.protocol.Packet.codec(ClientboundDebugSamplePacket::write, ClientboundDebugSamplePacket::new);
/*    */   
/*    */   private ClientboundDebugSamplePacket(FriendlyByteBuf input) {
/* 13 */     this(input.readLongArray(), (net.minecraft.util.debugchart.RemoteDebugSampleType)input.readEnum(net.minecraft.util.debugchart.RemoteDebugSampleType.class));
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 17 */     output.writeLongArray(this.sample);
/* 18 */     output.writeEnum((Enum)this.debugSampleType);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundDebugSamplePacket> type() {
/* 23 */     return GamePacketTypes.CLIENTBOUND_DEBUG_SAMPLE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 28 */     listener.handleDebugSample(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundDebugSamplePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */