/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundLockDifficultyPacket implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundLockDifficultyPacket> STREAM_CODEC = Packet.codec(ServerboundLockDifficultyPacket::write, ServerboundLockDifficultyPacket::new);
/*    */   
/*    */   private final boolean locked;
/*    */   
/*    */   public ServerboundLockDifficultyPacket(boolean locked) {
/* 14 */     this.locked = locked;
/*    */   }
/*    */   
/*    */   private ServerboundLockDifficultyPacket(FriendlyByteBuf input) {
/* 18 */     this.locked = input.readBoolean();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeBoolean(this.locked);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundLockDifficultyPacket> type() {
/* 27 */     return GamePacketTypes.SERVERBOUND_LOCK_DIFFICULTY;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 32 */     listener.handleLockDifficulty(this);
/*    */   }
/*    */   
/*    */   public boolean isLocked() {
/* 36 */     return this.locked;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundLockDifficultyPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */