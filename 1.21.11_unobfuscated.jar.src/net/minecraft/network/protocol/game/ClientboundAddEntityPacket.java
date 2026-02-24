/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import java.util.UUID;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.server.level.ServerEntity;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class ClientboundAddEntityPacket implements Packet<ClientGamePacketListener> {
/*  20 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundAddEntityPacket> STREAM_CODEC = Packet.codec(ClientboundAddEntityPacket::write, ClientboundAddEntityPacket::new);
/*     */   
/*     */   private final int id;
/*     */   
/*     */   private final UUID uuid;
/*     */   private final EntityType<?> type;
/*     */   private final double x;
/*     */   private final double y;
/*     */   private final double z;
/*     */   private final Vec3 movement;
/*     */   private final byte xRot;
/*     */   private final byte yRot;
/*     */   private final byte yHeadRot;
/*     */   private final int data;
/*     */   
/*     */   public ClientboundAddEntityPacket(Entity entity, ServerEntity serverEntity) {
/*  36 */     this(entity, serverEntity, 0);
/*     */   }
/*     */   
/*     */   public ClientboundAddEntityPacket(Entity entity, ServerEntity serverEntity, int data) {
/*  40 */     this(entity.getId(), entity.getUUID(), serverEntity.getPositionBase().x(), serverEntity.getPositionBase().y(), serverEntity.getPositionBase().z(), serverEntity.getLastSentXRot(), serverEntity.getLastSentYRot(), entity.getType(), data, serverEntity.getLastSentMovement(), serverEntity.getLastSentYHeadRot());
/*     */   }
/*     */   
/*     */   public ClientboundAddEntityPacket(Entity entity, int data, BlockPos pos) {
/*  44 */     this(entity.getId(), entity.getUUID(), pos.getX(), pos.getY(), pos.getZ(), entity.getXRot(), entity.getYRot(), entity.getType(), data, entity.getDeltaMovement(), entity.getYHeadRot());
/*     */   }
/*     */   
/*     */   public ClientboundAddEntityPacket(int id, UUID uuid, double x, double y, double z, float xRot, float yRot, EntityType<?> type, int data, Vec3 movement, double yHeadRot) {
/*  48 */     this.id = id;
/*  49 */     this.uuid = uuid;
/*  50 */     this.x = x;
/*  51 */     this.y = y;
/*  52 */     this.z = z;
/*  53 */     this.movement = movement;
/*  54 */     this.xRot = Mth.packDegrees(xRot);
/*  55 */     this.yRot = Mth.packDegrees(yRot);
/*  56 */     this.yHeadRot = Mth.packDegrees((float)yHeadRot);
/*  57 */     this.type = type;
/*  58 */     this.data = data;
/*     */   }
/*     */   
/*     */   private ClientboundAddEntityPacket(RegistryFriendlyByteBuf input) {
/*  62 */     this.id = input.readVarInt();
/*  63 */     this.uuid = input.readUUID();
/*  64 */     this.type = (EntityType)ByteBufCodecs.registry(Registries.ENTITY_TYPE).decode(input);
/*  65 */     this.x = input.readDouble();
/*  66 */     this.y = input.readDouble();
/*  67 */     this.z = input.readDouble();
/*  68 */     this.movement = input.readLpVec3();
/*  69 */     this.xRot = input.readByte();
/*  70 */     this.yRot = input.readByte();
/*  71 */     this.yHeadRot = input.readByte();
/*  72 */     this.data = input.readVarInt();
/*     */   }
/*     */   
/*     */   private void write(RegistryFriendlyByteBuf output) {
/*  76 */     output.writeVarInt(this.id);
/*  77 */     output.writeUUID(this.uuid);
/*  78 */     ByteBufCodecs.registry(Registries.ENTITY_TYPE).encode(output, this.type);
/*  79 */     output.writeDouble(this.x);
/*  80 */     output.writeDouble(this.y);
/*  81 */     output.writeDouble(this.z);
/*  82 */     output.writeLpVec3(this.movement);
/*  83 */     output.writeByte(this.xRot);
/*  84 */     output.writeByte(this.yRot);
/*  85 */     output.writeByte(this.yHeadRot);
/*  86 */     output.writeVarInt(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public PacketType<ClientboundAddEntityPacket> type() {
/*  91 */     return GamePacketTypes.CLIENTBOUND_ADD_ENTITY;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ClientGamePacketListener listener) {
/*  96 */     listener.handleAddEntity(this);
/*     */   }
/*     */   
/*     */   public int getId() {
/* 100 */     return this.id;
/*     */   }
/*     */   
/*     */   public UUID getUUID() {
/* 104 */     return this.uuid;
/*     */   }
/*     */   
/*     */   public EntityType<?> getType() {
/* 108 */     return this.type;
/*     */   }
/*     */   
/*     */   public double getX() {
/* 112 */     return this.x;
/*     */   }
/*     */   
/*     */   public double getY() {
/* 116 */     return this.y;
/*     */   }
/*     */   
/*     */   public double getZ() {
/* 120 */     return this.z;
/*     */   }
/*     */   
/*     */   public Vec3 getMovement() {
/* 124 */     return this.movement;
/*     */   }
/*     */   
/*     */   public float getXRot() {
/* 128 */     return Mth.unpackDegrees(this.xRot);
/*     */   }
/*     */   
/*     */   public float getYRot() {
/* 132 */     return Mth.unpackDegrees(this.yRot);
/*     */   }
/*     */   
/*     */   public float getYHeadRot() {
/* 136 */     return Mth.unpackDegrees(this.yHeadRot);
/*     */   }
/*     */   
/*     */   public int getData() {
/* 140 */     return this.data;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundAddEntityPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */