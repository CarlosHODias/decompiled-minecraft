/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.syncher.SynchedEntityData;
/*    */ 
/*    */ public final class ClientboundSetEntityDataPacket extends Record implements Packet<ClientGamePacketListener> {
/*    */   private final int id;
/*    */   private final List<SynchedEntityData.DataValue<?>> packedItems;
/*    */   
/* 12 */   public ClientboundSetEntityDataPacket(int id, List<SynchedEntityData.DataValue<?>> packedItems) { this.id = id; this.packedItems = packedItems; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundSetEntityDataPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetEntityDataPacket; } public int id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundSetEntityDataPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetEntityDataPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundSetEntityDataPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundSetEntityDataPacket;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public List<SynchedEntityData.DataValue<?>> packedItems() { return this.packedItems; }
/* 13 */    public static final net.minecraft.network.codec.StreamCodec<RegistryFriendlyByteBuf, ClientboundSetEntityDataPacket> STREAM_CODEC = Packet.codec(ClientboundSetEntityDataPacket::write, ClientboundSetEntityDataPacket::new);
/*    */   
/*    */   public static final int EOF_MARKER = 255;
/*    */   
/*    */   private ClientboundSetEntityDataPacket(RegistryFriendlyByteBuf input) {
/* 18 */     this(
/* 19 */         input.readVarInt(), 
/* 20 */         unpack(input));
/*    */   }
/*    */ 
/*    */   
/*    */   private static void pack(List<SynchedEntityData.DataValue<?>> items, RegistryFriendlyByteBuf output) {
/* 25 */     for (SynchedEntityData.DataValue<?> item : items) {
/* 26 */       item.write(output);
/*    */     }
/* 28 */     output.writeByte(255);
/*    */   }
/*    */   
/*    */   private static List<SynchedEntityData.DataValue<?>> unpack(RegistryFriendlyByteBuf input) {
/* 32 */     List<SynchedEntityData.DataValue<?>> result = new java.util.ArrayList<>();
/*    */     
/*    */     int id;
/* 35 */     while ((id = input.readUnsignedByte()) != 255) {
/* 36 */       result.add(SynchedEntityData.DataValue.read(input, id));
/*    */     }
/*    */     
/* 39 */     return result;
/*    */   }
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 43 */     output.writeVarInt(this.id);
/* 44 */     pack(this.packedItems, output);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundSetEntityDataPacket> type() {
/* 49 */     return GamePacketTypes.CLIENTBOUND_SET_ENTITY_DATA;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 54 */     listener.handleSetEntityData(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundSetEntityDataPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */