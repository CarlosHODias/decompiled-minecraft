/*    */ package net.minecraft.network.protocol.common;
/*    */ 
/*    */ import java.util.UUID;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public final class ServerboundResourcePackPacket extends Record implements net.minecraft.network.protocol.Packet<ServerCommonPacketListener> {
/*    */   private final UUID id;
/*    */   private final Action action;
/*    */   
/* 10 */   public ServerboundResourcePackPacket(UUID id, Action action) { this.id = id; this.action = action; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket; } public UUID id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ServerboundResourcePackPacket;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public Action action() { return this.action; }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final net.minecraft.network.codec.StreamCodec<FriendlyByteBuf, ServerboundResourcePackPacket> STREAM_CODEC = net.minecraft.network.protocol.Packet.codec(ServerboundResourcePackPacket::write, ServerboundResourcePackPacket::new);
/*    */   
/*    */   private ServerboundResourcePackPacket(FriendlyByteBuf input) {
/* 17 */     this(
/* 18 */         input.readUUID(), (Action)
/* 19 */         input.readEnum(Action.class));
/*    */   }
/*    */ 
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 24 */     output.writeUUID(this.id);
/* 25 */     output.writeEnum(this.action);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundResourcePackPacket> type() {
/* 30 */     return CommonPacketTypes.SERVERBOUND_RESOURCE_PACK;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerCommonPacketListener listener) {
/* 35 */     listener.handleResourcePackResponse(this);
/*    */   }
/*    */   
/*    */   public enum Action {
/* 39 */     SUCCESSFULLY_LOADED,
/* 40 */     DECLINED,
/* 41 */     FAILED_DOWNLOAD,
/* 42 */     ACCEPTED,
/* 43 */     DOWNLOADED,
/* 44 */     INVALID_URL,
/* 45 */     FAILED_RELOAD,
/* 46 */     DISCARDED;
/*    */ 
/*    */     
/*    */     public boolean isTerminal() {
/* 50 */       return (this != ACCEPTED && this != DOWNLOADED);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/common/ServerboundResourcePackPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */