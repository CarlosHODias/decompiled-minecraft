/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ServerboundCommandSuggestionPacket implements Packet<ServerGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundCommandSuggestionPacket> STREAM_CODEC = Packet.codec(ServerboundCommandSuggestionPacket::write, ServerboundCommandSuggestionPacket::new);
/*    */   
/*    */   private final int id;
/*    */   private final String command;
/*    */   
/*    */   public ServerboundCommandSuggestionPacket(int id, String command) {
/* 16 */     this.id = id;
/* 17 */     this.command = command;
/*    */   }
/*    */   
/*    */   private ServerboundCommandSuggestionPacket(FriendlyByteBuf input) {
/* 21 */     this.id = input.readVarInt();
/* 22 */     this.command = input.readUtf(32500);
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 26 */     output.writeVarInt(this.id);
/* 27 */     output.writeUtf(this.command, 32500);
/*    */   }
/*    */ 
/*    */   
/*    */   public PacketType<ServerboundCommandSuggestionPacket> type() {
/* 32 */     return GamePacketTypes.SERVERBOUND_COMMAND_SUGGESTION;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 37 */     listener.handleCustomCommandSuggestions(this);
/*    */   }
/*    */   
/*    */   public int getId() {
/* 41 */     return this.id;
/*    */   }
/*    */   
/*    */   public String getCommand() {
/* 45 */     return this.command;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundCommandSuggestionPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */