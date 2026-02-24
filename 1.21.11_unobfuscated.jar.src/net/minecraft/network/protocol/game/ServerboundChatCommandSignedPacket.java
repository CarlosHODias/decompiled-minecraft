/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public final class ServerboundChatCommandSignedPacket extends Record implements net.minecraft.network.protocol.Packet<ServerGamePacketListener> {
/*    */   private final String command;
/*    */   private final java.time.Instant timeStamp;
/*    */   private final long salt;
/*    */   private final net.minecraft.commands.arguments.ArgumentSignatures argumentSignatures;
/*    */   private final net.minecraft.network.chat.LastSeenMessages.Update lastSeenMessages;
/*    */   
/* 12 */   public ServerboundChatCommandSignedPacket(String command, java.time.Instant timeStamp, long salt, net.minecraft.commands.arguments.ArgumentSignatures argumentSignatures, net.minecraft.network.chat.LastSeenMessages.Update lastSeenMessages) { this.command = command; this.timeStamp = timeStamp; this.salt = salt; this.argumentSignatures = argumentSignatures; this.lastSeenMessages = lastSeenMessages; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket; } public String command() { return this.command; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public java.time.Instant timeStamp() { return this.timeStamp; } public long salt() { return this.salt; } public net.minecraft.commands.arguments.ArgumentSignatures argumentSignatures() { return this.argumentSignatures; } public net.minecraft.network.chat.LastSeenMessages.Update lastSeenMessages() { return this.lastSeenMessages; }
/* 13 */    public static final net.minecraft.network.codec.StreamCodec<FriendlyByteBuf, ServerboundChatCommandSignedPacket> STREAM_CODEC = net.minecraft.network.protocol.Packet.codec(ServerboundChatCommandSignedPacket::write, ServerboundChatCommandSignedPacket::new);
/*    */   
/*    */   private ServerboundChatCommandSignedPacket(FriendlyByteBuf input) {
/* 16 */     this(
/* 17 */         input.readUtf(), 
/* 18 */         input.readInstant(), 
/* 19 */         input.readLong(), new net.minecraft.commands.arguments.ArgumentSignatures(input), new net.minecraft.network.chat.LastSeenMessages.Update(input));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 26 */     output.writeUtf(this.command);
/* 27 */     output.writeInstant(this.timeStamp);
/* 28 */     output.writeLong(this.salt);
/* 29 */     this.argumentSignatures.write(output);
/* 30 */     this.lastSeenMessages.write(output);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundChatCommandSignedPacket> type() {
/* 35 */     return GamePacketTypes.SERVERBOUND_CHAT_COMMAND_SIGNED;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 40 */     listener.handleSignedChatCommand(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundChatCommandSignedPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */