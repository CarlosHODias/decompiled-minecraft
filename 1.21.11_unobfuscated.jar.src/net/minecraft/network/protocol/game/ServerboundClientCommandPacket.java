/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundClientCommandPacket implements Packet<ServerGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ServerboundClientCommandPacket> STREAM_CODEC = Packet.codec(ServerboundClientCommandPacket::write, ServerboundClientCommandPacket::new);
/*    */   
/*    */   private final Action action;
/*    */   
/*    */   public ServerboundClientCommandPacket(Action action) {
/* 14 */     this.action = action;
/*    */   }
/*    */   
/*    */   private ServerboundClientCommandPacket(FriendlyByteBuf input) {
/* 18 */     this.action = (Action)input.readEnum(Action.class);
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 22 */     output.writeEnum(this.action);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundClientCommandPacket> type() {
/* 27 */     return GamePacketTypes.SERVERBOUND_CLIENT_COMMAND;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 32 */     listener.handleClientCommand(this);
/*    */   }
/*    */   
/*    */   public Action getAction() {
/* 36 */     return this.action;
/*    */   }
/*    */   
/*    */   public enum Action {
/* 40 */     PERFORM_RESPAWN,
/* 41 */     REQUEST_STATS;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundClientCommandPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */