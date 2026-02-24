/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ServerboundPlayerActionPacket implements Packet<ServerGamePacketListener> {
/* 11 */   public static final StreamCodec<FriendlyByteBuf, ServerboundPlayerActionPacket> STREAM_CODEC = Packet.codec(ServerboundPlayerActionPacket::write, ServerboundPlayerActionPacket::new);
/*    */   
/*    */   private final BlockPos pos;
/*    */   private final Direction direction;
/*    */   private final Action action;
/*    */   private final int sequence;
/*    */   
/*    */   public ServerboundPlayerActionPacket(Action action, BlockPos pos, Direction direction, int sequence) {
/* 19 */     this.action = action;
/* 20 */     this.pos = pos.immutable();
/* 21 */     this.direction = direction;
/* 22 */     this.sequence = sequence;
/*    */   }
/*    */   
/*    */   public ServerboundPlayerActionPacket(Action action, BlockPos pos, Direction direction) {
/* 26 */     this(action, pos, direction, 0);
/*    */   }
/*    */   
/*    */   private ServerboundPlayerActionPacket(FriendlyByteBuf input) {
/* 30 */     this.action = (Action)input.readEnum(Action.class);
/* 31 */     this.pos = input.readBlockPos();
/* 32 */     this.direction = Direction.from3DDataValue(input.readUnsignedByte());
/* 33 */     this.sequence = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 37 */     output.writeEnum(this.action);
/* 38 */     output.writeBlockPos(this.pos);
/* 39 */     output.writeByte(this.direction.get3DDataValue());
/* 40 */     output.writeVarInt(this.sequence);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundPlayerActionPacket> type() {
/* 45 */     return GamePacketTypes.SERVERBOUND_PLAYER_ACTION;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 50 */     listener.handlePlayerAction(this);
/*    */   }
/*    */   
/*    */   public BlockPos getPos() {
/* 54 */     return this.pos;
/*    */   }
/*    */   
/*    */   public Direction getDirection() {
/* 58 */     return this.direction;
/*    */   }
/*    */   
/*    */   public Action getAction() {
/* 62 */     return this.action;
/*    */   }
/*    */   
/*    */   public int getSequence() {
/* 66 */     return this.sequence;
/*    */   }
/*    */   
/*    */   public enum Action {
/* 70 */     START_DESTROY_BLOCK,
/* 71 */     ABORT_DESTROY_BLOCK,
/* 72 */     STOP_DESTROY_BLOCK,
/* 73 */     DROP_ALL_ITEMS,
/* 74 */     DROP_ITEM,
/* 75 */     RELEASE_USE_ITEM,
/* 76 */     SWAP_ITEM_WITH_OFFHAND,
/* 77 */     STAB;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundPlayerActionPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */