/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.ChatType;
/*    */ import net.minecraft.network.chat.FilterMask;
/*    */ import net.minecraft.network.chat.MessageSignature;
/*    */ 
/*    */ public final class ClientboundPlayerChatPacket extends Record implements net.minecraft.network.protocol.Packet<ClientGamePacketListener> {
/*    */   private final int globalIndex;
/*    */   private final java.util.UUID sender;
/*    */   private final int index;
/*    */   private final MessageSignature signature;
/*    */   private final net.minecraft.network.chat.SignedMessageBody.Packed body;
/*    */   private final net.minecraft.network.chat.Component unsignedContent;
/*    */   private final FilterMask filterMask;
/*    */   private final ChatType.Bound chatType;
/*    */   
/* 18 */   public ClientboundPlayerChatPacket(int globalIndex, java.util.UUID sender, int index, MessageSignature signature, net.minecraft.network.chat.SignedMessageBody.Packed body, net.minecraft.network.chat.Component unsignedContent, FilterMask filterMask, ChatType.Bound chatType) { this.globalIndex = globalIndex; this.sender = sender; this.index = index; this.signature = signature; this.body = body; this.unsignedContent = unsignedContent; this.filterMask = filterMask; this.chatType = chatType; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 18 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket; } public int globalIndex() { return this.globalIndex; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundPlayerChatPacket;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.UUID sender() { return this.sender; } public int index() { return this.index; } public MessageSignature signature() { return this.signature; } public net.minecraft.network.chat.SignedMessageBody.Packed body() { return this.body; } public net.minecraft.network.chat.Component unsignedContent() { return this.unsignedContent; } public FilterMask filterMask() { return this.filterMask; } public ChatType.Bound chatType() { return this.chatType; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static final net.minecraft.network.codec.StreamCodec<RegistryFriendlyByteBuf, ClientboundPlayerChatPacket> STREAM_CODEC = net.minecraft.network.protocol.Packet.codec(ClientboundPlayerChatPacket::write, ClientboundPlayerChatPacket::new);
/*    */   
/*    */   private ClientboundPlayerChatPacket(RegistryFriendlyByteBuf input) {
/* 32 */     this(
/* 33 */         input.readVarInt(), 
/* 34 */         input.readUUID(), 
/* 35 */         input.readVarInt(), (MessageSignature)
/* 36 */         input.readNullable(MessageSignature::read), new net.minecraft.network.chat.SignedMessageBody.Packed((net.minecraft.network.FriendlyByteBuf)input), 
/*    */         
/* 38 */         (net.minecraft.network.chat.Component)net.minecraft.network.FriendlyByteBuf.readNullable((io.netty.buffer.ByteBuf)input, (net.minecraft.network.codec.StreamDecoder)net.minecraft.network.chat.ComponentSerialization.TRUSTED_STREAM_CODEC), 
/* 39 */         FilterMask.read((net.minecraft.network.FriendlyByteBuf)input), (ChatType.Bound)
/* 40 */         ChatType.Bound.STREAM_CODEC.decode(input));
/*    */   }
/*    */ 
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 45 */     output.writeVarInt(this.globalIndex);
/* 46 */     output.writeUUID(this.sender);
/* 47 */     output.writeVarInt(this.index);
/* 48 */     output.writeNullable(this.signature, MessageSignature::write);
/* 49 */     this.body.write((net.minecraft.network.FriendlyByteBuf)output);
/* 50 */     net.minecraft.network.FriendlyByteBuf.writeNullable((io.netty.buffer.ByteBuf)output, this.unsignedContent, (net.minecraft.network.codec.StreamEncoder)net.minecraft.network.chat.ComponentSerialization.TRUSTED_STREAM_CODEC);
/* 51 */     FilterMask.write((net.minecraft.network.FriendlyByteBuf)output, this.filterMask);
/* 52 */     ChatType.Bound.STREAM_CODEC.encode(output, this.chatType);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundPlayerChatPacket> type() {
/* 57 */     return GamePacketTypes.CLIENTBOUND_PLAYER_CHAT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 62 */     listener.handlePlayerChat(this);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isSkippable() {
/* 67 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundPlayerChatPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */