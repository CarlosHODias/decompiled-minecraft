/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.Level;
/*     */ 
/*     */ public abstract class ClientboundMoveEntityPacket
/*     */   implements Packet<ClientGamePacketListener>
/*     */ {
/*     */   protected final int entityId;
/*     */   protected final short xa;
/*     */   protected final short ya;
/*     */   protected final short za;
/*     */   protected final byte yRot;
/*     */   protected final byte xRot;
/*     */   protected final boolean onGround;
/*     */   protected final boolean hasRot;
/*     */   protected final boolean hasPos;
/*     */   
/*     */   public static class PosRot
/*     */     extends ClientboundMoveEntityPacket {
/*  27 */     public static final StreamCodec<FriendlyByteBuf, PosRot> STREAM_CODEC = Packet.codec(PosRot::write, PosRot::read);
/*     */     
/*     */     public PosRot(int id, short xa, short ya, short za, byte yRot, byte xRot, boolean onGround) {
/*  30 */       super(id, xa, ya, za, yRot, xRot, onGround, true, true);
/*     */     }
/*     */     
/*     */     private static PosRot read(FriendlyByteBuf input) {
/*  34 */       int entityId = input.readVarInt();
/*  35 */       short xa = input.readShort();
/*  36 */       short ya = input.readShort();
/*  37 */       short za = input.readShort();
/*  38 */       byte yRot = input.readByte();
/*  39 */       byte xRot = input.readByte();
/*  40 */       boolean onGround = input.readBoolean();
/*     */       
/*  42 */       return new PosRot(entityId, xa, ya, za, yRot, xRot, onGround);
/*     */     }
/*     */     
/*     */     private void write(FriendlyByteBuf output) {
/*  46 */       output.writeVarInt(this.entityId);
/*  47 */       output.writeShort(this.xa);
/*  48 */       output.writeShort(this.ya);
/*  49 */       output.writeShort(this.za);
/*  50 */       output.writeByte(this.yRot);
/*  51 */       output.writeByte(this.xRot);
/*  52 */       output.writeBoolean(this.onGround);
/*     */     }
/*     */ 
/*     */     
/*     */     public PacketType<PosRot> type() {
/*  57 */       return GamePacketTypes.CLIENTBOUND_MOVE_ENTITY_POS_ROT;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Pos extends ClientboundMoveEntityPacket {
/*  62 */     public static final StreamCodec<FriendlyByteBuf, Pos> STREAM_CODEC = Packet.codec(Pos::write, Pos::read);
/*     */     
/*     */     public Pos(int id, short xa, short ya, short za, boolean onGround) {
/*  65 */       super(id, xa, ya, za, (byte)0, (byte)0, onGround, false, true);
/*     */     }
/*     */     
/*     */     private static Pos read(FriendlyByteBuf input) {
/*  69 */       int entityId = input.readVarInt();
/*  70 */       short xa = input.readShort();
/*  71 */       short ya = input.readShort();
/*  72 */       short za = input.readShort();
/*  73 */       boolean onGround = input.readBoolean();
/*     */       
/*  75 */       return new Pos(entityId, xa, ya, za, onGround);
/*     */     }
/*     */     
/*     */     private void write(FriendlyByteBuf output) {
/*  79 */       output.writeVarInt(this.entityId);
/*  80 */       output.writeShort(this.xa);
/*  81 */       output.writeShort(this.ya);
/*  82 */       output.writeShort(this.za);
/*  83 */       output.writeBoolean(this.onGround);
/*     */     }
/*     */ 
/*     */     
/*     */     public PacketType<Pos> type() {
/*  88 */       return GamePacketTypes.CLIENTBOUND_MOVE_ENTITY_POS;
/*     */     }
/*     */   }
/*     */   
/*     */   public static class Rot extends ClientboundMoveEntityPacket {
/*  93 */     public static final StreamCodec<FriendlyByteBuf, Rot> STREAM_CODEC = Packet.codec(Rot::write, Rot::read);
/*     */     
/*     */     public Rot(int id, byte yRot, byte xRot, boolean onGround) {
/*  96 */       super(id, (short)0, (short)0, (short)0, yRot, xRot, onGround, true, false);
/*     */     }
/*     */     
/*     */     private static Rot read(FriendlyByteBuf input) {
/* 100 */       int entityId = input.readVarInt();
/* 101 */       byte yRot = input.readByte();
/* 102 */       byte xRot = input.readByte();
/* 103 */       boolean onGround = input.readBoolean();
/*     */       
/* 105 */       return new Rot(entityId, yRot, xRot, onGround);
/*     */     }
/*     */     
/*     */     private void write(FriendlyByteBuf output) {
/* 109 */       output.writeVarInt(this.entityId);
/* 110 */       output.writeByte(this.yRot);
/* 111 */       output.writeByte(this.xRot);
/* 112 */       output.writeBoolean(this.onGround);
/*     */     }
/*     */ 
/*     */     
/*     */     public PacketType<Rot> type() {
/* 117 */       return GamePacketTypes.CLIENTBOUND_MOVE_ENTITY_ROT;
/*     */     }
/*     */   }
/*     */   
/*     */   protected ClientboundMoveEntityPacket(int entityId, short xa, short ya, short za, byte yRot, byte xRot, boolean onGround, boolean hasRot, boolean hasPos) {
/* 122 */     this.entityId = entityId;
/* 123 */     this.xa = xa;
/* 124 */     this.ya = ya;
/* 125 */     this.za = za;
/* 126 */     this.yRot = yRot;
/* 127 */     this.xRot = xRot;
/* 128 */     this.onGround = onGround;
/* 129 */     this.hasRot = hasRot;
/* 130 */     this.hasPos = hasPos;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handle(ClientGamePacketListener listener) {
/* 138 */     listener.handleMoveEntity(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 143 */     return "Entity_" + super.toString();
/*     */   }
/*     */   
/*     */   public Entity getEntity(Level level) {
/* 147 */     return level.getEntity(this.entityId);
/*     */   }
/*     */   
/*     */   public short getXa() {
/* 151 */     return this.xa;
/*     */   }
/*     */   
/*     */   public short getYa() {
/* 155 */     return this.ya;
/*     */   }
/*     */   
/*     */   public short getZa() {
/* 159 */     return this.za;
/*     */   }
/*     */   
/*     */   public float getYRot() {
/* 163 */     return Mth.unpackDegrees(this.yRot);
/*     */   }
/*     */   
/*     */   public float getXRot() {
/* 167 */     return Mth.unpackDegrees(this.xRot);
/*     */   }
/*     */   
/*     */   public boolean hasRotation() {
/* 171 */     return this.hasRot;
/*     */   }
/*     */   
/*     */   public boolean hasPosition() {
/* 175 */     return this.hasPos;
/*     */   }
/*     */   
/*     */   public boolean isOnGround() {
/* 179 */     return this.onGround;
/*     */   }
/*     */   
/*     */   public abstract PacketType<? extends ClientboundMoveEntityPacket> type();
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundMoveEntityPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */