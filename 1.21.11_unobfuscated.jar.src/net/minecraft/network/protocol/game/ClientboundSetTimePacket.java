/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ 
/*    */ public final class ClientboundSetTimePacket extends Record implements net.minecraft.network.protocol.Packet<ClientGamePacketListener> {
/*    */   private final long gameTime;
/*    */   private final long dayTime;
/*    */   private final boolean tickDayTime;
/*    */   
/*  9 */   public ClientboundSetTimePacket(long gameTime, long dayTime, boolean tickDayTime) { this.gameTime = gameTime; this.dayTime = dayTime; this.tickDayTime = tickDayTime; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket; } public long gameTime() { return this.gameTime; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetTimePacket;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public long dayTime() { return this.dayTime; } public boolean tickDayTime() { return this.tickDayTime; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.FriendlyByteBuf, ClientboundSetTimePacket> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.LONG, ClientboundSetTimePacket::gameTime, net.minecraft.network.codec.ByteBufCodecs.LONG, ClientboundSetTimePacket::dayTime, net.minecraft.network.codec.ByteBufCodecs.BOOL, ClientboundSetTimePacket::tickDayTime, ClientboundSetTimePacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundSetTimePacket> type() {
/* 23 */     return GamePacketTypes.CLIENTBOUND_SET_TIME;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 28 */     listener.handleSetTime(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundSetTimePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */