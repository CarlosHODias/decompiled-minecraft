/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public class ClientboundRotateHeadPacket implements Packet<ClientGamePacketListener> {
/* 14 */   public static final StreamCodec<FriendlyByteBuf, ClientboundRotateHeadPacket> STREAM_CODEC = Packet.codec(ClientboundRotateHeadPacket::write, ClientboundRotateHeadPacket::new);
/*    */   
/*    */   private final int entityId;
/*    */   private final byte yHeadRot;
/*    */   
/*    */   public ClientboundRotateHeadPacket(Entity entity, byte yHeadRot) {
/* 20 */     this.entityId = entity.getId();
/* 21 */     this.yHeadRot = yHeadRot;
/*    */   }
/*    */   
/*    */   private ClientboundRotateHeadPacket(FriendlyByteBuf input) {
/* 25 */     this.entityId = input.readVarInt();
/* 26 */     this.yHeadRot = input.readByte();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 30 */     output.writeVarInt(this.entityId);
/* 31 */     output.writeByte(this.yHeadRot);
/*    */   }
/*    */ 
/*    */   
/*    */   public PacketType<ClientboundRotateHeadPacket> type() {
/* 36 */     return GamePacketTypes.CLIENTBOUND_ROTATE_HEAD;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 41 */     listener.handleRotateMob(this);
/*    */   }
/*    */   
/*    */   public Entity getEntity(Level level) {
/* 45 */     return level.getEntity(this.entityId);
/*    */   }
/*    */   
/*    */   public float getYHeadRot() {
/* 49 */     return Mth.unpackDegrees(this.yHeadRot);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundRotateHeadPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */