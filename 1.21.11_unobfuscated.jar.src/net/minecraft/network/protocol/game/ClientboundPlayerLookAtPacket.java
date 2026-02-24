/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.commands.arguments.EntityAnchorArgument;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class ClientboundPlayerLookAtPacket implements Packet<ClientGamePacketListener> {
/* 14 */   public static final StreamCodec<FriendlyByteBuf, ClientboundPlayerLookAtPacket> STREAM_CODEC = Packet.codec(ClientboundPlayerLookAtPacket::write, ClientboundPlayerLookAtPacket::new);
/*    */   
/*    */   private final double x;
/*    */   private final double y;
/*    */   private final double z;
/*    */   private final int entity;
/*    */   private final EntityAnchorArgument.Anchor fromAnchor;
/*    */   private final EntityAnchorArgument.Anchor toAnchor;
/*    */   private final boolean atEntity;
/*    */   
/*    */   public ClientboundPlayerLookAtPacket(EntityAnchorArgument.Anchor fromAnchor, double x, double y, double z) {
/* 25 */     this.fromAnchor = fromAnchor;
/* 26 */     this.x = x;
/* 27 */     this.y = y;
/* 28 */     this.z = z;
/* 29 */     this.entity = 0;
/* 30 */     this.atEntity = false;
/* 31 */     this.toAnchor = null;
/*    */   }
/*    */   
/*    */   public ClientboundPlayerLookAtPacket(EntityAnchorArgument.Anchor fromAnchor, Entity entity, EntityAnchorArgument.Anchor toAnchor) {
/* 35 */     this.fromAnchor = fromAnchor;
/* 36 */     this.entity = entity.getId();
/* 37 */     this.toAnchor = toAnchor;
/* 38 */     Vec3 pos = toAnchor.apply(entity);
/* 39 */     this.x = pos.x;
/* 40 */     this.y = pos.y;
/* 41 */     this.z = pos.z;
/* 42 */     this.atEntity = true;
/*    */   }
/*    */   
/*    */   private ClientboundPlayerLookAtPacket(FriendlyByteBuf input) {
/* 46 */     this.fromAnchor = (EntityAnchorArgument.Anchor)input.readEnum(EntityAnchorArgument.Anchor.class);
/* 47 */     this.x = input.readDouble();
/* 48 */     this.y = input.readDouble();
/* 49 */     this.z = input.readDouble();
/* 50 */     this.atEntity = input.readBoolean();
/* 51 */     if (this.atEntity) {
/* 52 */       this.entity = input.readVarInt();
/* 53 */       this.toAnchor = (EntityAnchorArgument.Anchor)input.readEnum(EntityAnchorArgument.Anchor.class);
/*    */     } else {
/* 55 */       this.entity = 0;
/* 56 */       this.toAnchor = null;
/*    */     } 
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 61 */     output.writeEnum((Enum)this.fromAnchor);
/* 62 */     output.writeDouble(this.x);
/* 63 */     output.writeDouble(this.y);
/* 64 */     output.writeDouble(this.z);
/* 65 */     output.writeBoolean(this.atEntity);
/* 66 */     if (this.atEntity) {
/* 67 */       output.writeVarInt(this.entity);
/* 68 */       output.writeEnum((Enum)this.toAnchor);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public PacketType<ClientboundPlayerLookAtPacket> type() {
/* 74 */     return GamePacketTypes.CLIENTBOUND_PLAYER_LOOK_AT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 79 */     listener.handleLookAt(this);
/*    */   }
/*    */   
/*    */   public EntityAnchorArgument.Anchor getFromAnchor() {
/* 83 */     return this.fromAnchor;
/*    */   }
/*    */   
/*    */   public Vec3 getPosition(Level level) {
/* 87 */     if (this.atEntity) {
/* 88 */       Entity entity = level.getEntity(this.entity);
/* 89 */       if (entity == null) {
/* 90 */         return new Vec3(this.x, this.y, this.z);
/*    */       }
/* 92 */       return this.toAnchor.apply(entity);
/*    */     } 
/* 94 */     return new Vec3(this.x, this.y, this.z);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundPlayerLookAtPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */