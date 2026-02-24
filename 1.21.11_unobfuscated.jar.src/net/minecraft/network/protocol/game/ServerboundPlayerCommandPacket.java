/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class ServerboundPlayerCommandPacket implements Packet<ServerGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ServerboundPlayerCommandPacket> STREAM_CODEC = Packet.codec(ServerboundPlayerCommandPacket::write, ServerboundPlayerCommandPacket::new);
/*    */   
/*    */   private final int id;
/*    */   private final Action action;
/*    */   private final int data;
/*    */   
/*    */   public ServerboundPlayerCommandPacket(Entity entity, Action action) {
/* 17 */     this(entity, action, 0);
/*    */   }
/*    */   
/*    */   public ServerboundPlayerCommandPacket(Entity entity, Action action, int data) {
/* 21 */     this.id = entity.getId();
/* 22 */     this.action = action;
/* 23 */     this.data = data;
/*    */   }
/*    */   
/*    */   private ServerboundPlayerCommandPacket(FriendlyByteBuf input) {
/* 27 */     this.id = input.readVarInt();
/* 28 */     this.action = (Action)input.readEnum(Action.class);
/* 29 */     this.data = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 33 */     output.writeVarInt(this.id);
/* 34 */     output.writeEnum(this.action);
/* 35 */     output.writeVarInt(this.data);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ServerboundPlayerCommandPacket> type() {
/* 40 */     return GamePacketTypes.SERVERBOUND_PLAYER_COMMAND;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ServerGamePacketListener listener) {
/* 45 */     listener.handlePlayerCommand(this);
/*    */   }
/*    */   
/*    */   public int getId() {
/* 49 */     return this.id;
/*    */   }
/*    */   
/*    */   public Action getAction() {
/* 53 */     return this.action;
/*    */   }
/*    */   
/*    */   public int getData() {
/* 57 */     return this.data;
/*    */   }
/*    */   
/*    */   public enum Action {
/* 61 */     STOP_SLEEPING,
/* 62 */     START_SPRINTING,
/* 63 */     STOP_SPRINTING,
/* 64 */     START_RIDING_JUMP,
/* 65 */     STOP_RIDING_JUMP,
/* 66 */     OPEN_INVENTORY,
/* 67 */     START_FALL_FLYING;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ServerboundPlayerCommandPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */