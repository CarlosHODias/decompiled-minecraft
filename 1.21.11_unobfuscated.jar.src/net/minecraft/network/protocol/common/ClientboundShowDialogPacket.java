/*    */ package net.minecraft.network.protocol.common;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.server.dialog.Dialog;
/*    */ 
/*    */ public final class ClientboundShowDialogPacket extends Record implements net.minecraft.network.protocol.Packet<ClientCommonPacketListener> {
/*    */   private final Holder<Dialog> dialog;
/*    */   
/* 11 */   public ClientboundShowDialogPacket(Holder<Dialog> dialog) { this.dialog = dialog; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/common/ClientboundShowDialogPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundShowDialogPacket; } public Holder<Dialog> dialog() { return this.dialog; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/common/ClientboundShowDialogPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/common/ClientboundShowDialogPacket; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/common/ClientboundShowDialogPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/common/ClientboundShowDialogPacket;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 14 */   } public static final StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, ClientboundShowDialogPacket> STREAM_CODEC = StreamCodec.composite(Dialog.STREAM_CODEC, ClientboundShowDialogPacket::dialog, ClientboundShowDialogPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 19 */   public static final StreamCodec<io.netty.buffer.ByteBuf, ClientboundShowDialogPacket> CONTEXT_FREE_STREAM_CODEC = StreamCodec.composite(
/* 20 */       Dialog.CONTEXT_FREE_STREAM_CODEC.map(Holder::direct, Holder::value), ClientboundShowDialogPacket::dialog, ClientboundShowDialogPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundShowDialogPacket> type() {
/* 26 */     return CommonPacketTypes.CLIENTBOUND_SHOW_DIALOG;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientCommonPacketListener listener) {
/* 31 */     listener.handleShowDialog(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/common/ClientboundShowDialogPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */