/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.world.TickRateManager;
/*    */ 
/*    */ public final class ClientboundTickingStepPacket extends Record implements net.minecraft.network.protocol.Packet<ClientGamePacketListener> {
/*    */   private final int tickSteps;
/*    */   
/*  9 */   public ClientboundTickingStepPacket(int tickSteps) { this.tickSteps = tickSteps; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundTickingStepPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTickingStepPacket; } public int tickSteps() { return this.tickSteps; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundTickingStepPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundTickingStepPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundTickingStepPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundTickingStepPacket;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public static final net.minecraft.network.codec.StreamCodec<FriendlyByteBuf, ClientboundTickingStepPacket> STREAM_CODEC = net.minecraft.network.protocol.Packet.codec(ClientboundTickingStepPacket::write, ClientboundTickingStepPacket::new);
/*    */   
/*    */   private ClientboundTickingStepPacket(FriendlyByteBuf input) {
/* 13 */     this(input.readVarInt());
/*    */   }
/*    */   
/*    */   public static ClientboundTickingStepPacket from(TickRateManager manager) {
/* 17 */     return new ClientboundTickingStepPacket(manager.frozenTicksToRun());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 21 */     output.writeVarInt(this.tickSteps);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundTickingStepPacket> type() {
/* 26 */     return GamePacketTypes.CLIENTBOUND_TICKING_STEP;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 31 */     listener.handleTickingStep(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundTickingStepPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */