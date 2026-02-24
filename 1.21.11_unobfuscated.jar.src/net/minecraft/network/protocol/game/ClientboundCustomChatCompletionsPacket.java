/*    */ package net.minecraft.network.protocol.game;
/*    */ public final class ClientboundCustomChatCompletionsPacket extends Record implements net.minecraft.network.protocol.Packet<ClientGamePacketListener> {
/*    */   private final Action action;
/*    */   private final java.util.List<String> entries;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket;
/*    */   }
/*    */   
/* 14 */   public ClientboundCustomChatCompletionsPacket(Action action, java.util.List<String> entries) { this.action = action; this.entries = entries; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public Action action() { return this.action; } public java.util.List<String> entries() { return this.entries; }
/* 15 */    public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.FriendlyByteBuf, ClientboundCustomChatCompletionsPacket> STREAM_CODEC = net.minecraft.network.protocol.Packet.codec(ClientboundCustomChatCompletionsPacket::write, ClientboundCustomChatCompletionsPacket::new);
/*    */   
/*    */   public enum Action {
/* 18 */     ADD,
/* 19 */     REMOVE,
/* 20 */     SET;
/*    */   }
/*    */   
/*    */   private ClientboundCustomChatCompletionsPacket(net.minecraft.network.FriendlyByteBuf input) {
/* 24 */     this((Action)
/* 25 */         input.readEnum(Action.class), 
/* 26 */         input.readList(net.minecraft.network.FriendlyByteBuf::readUtf));
/*    */   }
/*    */ 
/*    */   
/*    */   private void write(net.minecraft.network.FriendlyByteBuf output) {
/* 31 */     output.writeEnum(this.action);
/* 32 */     output.writeCollection(this.entries, net.minecraft.network.FriendlyByteBuf::writeUtf);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundCustomChatCompletionsPacket> type() {
/* 37 */     return GamePacketTypes.CLIENTBOUND_CUSTOM_CHAT_COMPLETIONS;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 42 */     listener.handleCustomChatCompletions(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundCustomChatCompletionsPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */