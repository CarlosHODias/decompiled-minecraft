/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.world.level.ChunkPos;
/*    */ 
/*    */ public final class ClientboundForgetLevelChunkPacket extends Record implements net.minecraft.network.protocol.Packet<ClientGamePacketListener> {
/*    */   private final ChunkPos pos;
/*    */   
/*  9 */   public ClientboundForgetLevelChunkPacket(ChunkPos pos) { this.pos = pos; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket; } public ChunkPos pos() { return this.pos; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public static final net.minecraft.network.codec.StreamCodec<FriendlyByteBuf, ClientboundForgetLevelChunkPacket> STREAM_CODEC = net.minecraft.network.protocol.Packet.codec(ClientboundForgetLevelChunkPacket::write, ClientboundForgetLevelChunkPacket::new);
/*    */   
/*    */   private ClientboundForgetLevelChunkPacket(FriendlyByteBuf input) {
/* 13 */     this(input.readChunkPos());
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 17 */     output.writeChunkPos(this.pos);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundForgetLevelChunkPacket> type() {
/* 22 */     return GamePacketTypes.CLIENTBOUND_FORGET_LEVEL_CHUNK;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 27 */     listener.handleForgetLevelChunk(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundForgetLevelChunkPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */