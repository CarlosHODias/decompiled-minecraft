/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public final class ServerboundClientTickEndPacket extends Record implements net.minecraft.network.protocol.Packet<ServerGamePacketListener> {
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundClientTickEndPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundClientTickEndPacket;
/*    */   }
/*    */   
/* 10 */   public static final ServerboundClientTickEndPacket INSTANCE = new ServerboundClientTickEndPacket();
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundClientTickEndPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundClientTickEndPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundClientTickEndPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundClientTickEndPacket;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public static final StreamCodec<io.netty.buffer.ByteBuf, ServerboundClientTickEndPacket> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundClientTickEndPacket> type() {
/* 15 */     return GamePacketTypes.SERVERBOUND_CLIENT_TICK_END;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 20 */     listener.handleClientTickEnd(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundClientTickEndPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */