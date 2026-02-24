/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public final class ClientboundTickingStatePacket extends Record implements net.minecraft.network.protocol.Packet<ClientGamePacketListener> {
/*    */   private final float tickRate;
/*    */   private final boolean isFrozen;
/*    */   
/*  9 */   public ClientboundTickingStatePacket(float tickRate, boolean isFrozen) { this.tickRate = tickRate; this.isFrozen = isFrozen; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundTickingStatePacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTickingStatePacket; } public float tickRate() { return this.tickRate; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundTickingStatePacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTickingStatePacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundTickingStatePacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundTickingStatePacket;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public boolean isFrozen() { return this.isFrozen; }
/* 10 */    public static final net.minecraft.network.codec.StreamCodec<FriendlyByteBuf, ClientboundTickingStatePacket> STREAM_CODEC = net.minecraft.network.protocol.Packet.codec(ClientboundTickingStatePacket::write, ClientboundTickingStatePacket::new);
/*    */   
/*    */   private ClientboundTickingStatePacket(FriendlyByteBuf input) {
/* 13 */     this(
/* 14 */         input.readFloat(), 
/* 15 */         input.readBoolean());
/*    */   }
/*    */ 
/*    */   
/*    */   public static ClientboundTickingStatePacket from(net.minecraft.world.TickRateManager manager) {
/* 20 */     return new ClientboundTickingStatePacket(manager.tickrate(), manager.isFrozen());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 24 */     output.writeFloat(this.tickRate);
/* 25 */     output.writeBoolean(this.isFrozen);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundTickingStatePacket> type() {
/* 30 */     return GamePacketTypes.CLIENTBOUND_TICKING_STATE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 35 */     listener.handleTickingState(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundTickingStatePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */