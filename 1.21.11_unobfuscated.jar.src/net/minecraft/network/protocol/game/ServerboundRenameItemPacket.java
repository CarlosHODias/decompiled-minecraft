/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundRenameItemPacket implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundRenameItemPacket> STREAM_CODEC = Packet.codec(ServerboundRenameItemPacket::write, ServerboundRenameItemPacket::new);
/*    */   
/*    */   private final String name;
/*    */   
/*    */   public ServerboundRenameItemPacket(String name) {
/* 14 */     this.name = name;
/*    */   }
/*    */   
/*    */   private ServerboundRenameItemPacket(FriendlyByteBuf input) {
/* 18 */     this.name = input.readUtf();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeUtf(this.name);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundRenameItemPacket> type() {
/* 27 */     return GamePacketTypes.SERVERBOUND_RENAME_ITEM;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 32 */     listener.handleRenameItem(this);
/*    */   }
/*    */   
/*    */   public String getName() {
/* 36 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundRenameItemPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */