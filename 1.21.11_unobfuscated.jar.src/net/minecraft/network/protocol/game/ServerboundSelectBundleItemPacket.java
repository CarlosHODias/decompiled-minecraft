/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ public final class ServerboundSelectBundleItemPacket extends Record implements net.minecraft.network.protocol.Packet<ServerGamePacketListener> {
/*    */   private final int slotId;
/*    */   private final int selectedItemIndex;
/*    */   
/*  9 */   public ServerboundSelectBundleItemPacket(int slotId, int selectedItemIndex) { this.slotId = slotId; this.selectedItemIndex = selectedItemIndex; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ServerboundSelectBundleItemPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSelectBundleItemPacket; } public int slotId() { return this.slotId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ServerboundSelectBundleItemPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ServerboundSelectBundleItemPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ServerboundSelectBundleItemPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ServerboundSelectBundleItemPacket;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public int selectedItemIndex() { return this.selectedItemIndex; }
/* 10 */    public static final net.minecraft.network.codec.StreamCodec<FriendlyByteBuf, ServerboundSelectBundleItemPacket> STREAM_CODEC = net.minecraft.network.protocol.Packet.codec(ServerboundSelectBundleItemPacket::write, ServerboundSelectBundleItemPacket::new);
/*    */   
/*    */   private ServerboundSelectBundleItemPacket(FriendlyByteBuf input) {
/* 13 */     this(input.readVarInt(), input.readVarInt());
/* 14 */     if (this.selectedItemIndex < 0 && this.selectedItemIndex != -1) {
/* 15 */       throw new IllegalArgumentException("Invalid selectedItemIndex: " + this.selectedItemIndex);
/*    */     }
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 20 */     output.writeVarInt(this.slotId);
/* 21 */     output.writeVarInt(this.selectedItemIndex);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundSelectBundleItemPacket> type() {
/* 26 */     return GamePacketTypes.SERVERBOUND_BUNDLE_ITEM_SELECTED;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 31 */     listener.handleBundleItemSelectedPacket(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundSelectBundleItemPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */