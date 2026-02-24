/*     */ package net.minecraft.network.protocol.game;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ 
/*     */ public class ClientboundLevelParticlesPacket implements Packet<ClientGamePacketListener> {
/*  12 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundLevelParticlesPacket> STREAM_CODEC = Packet.codec(ClientboundLevelParticlesPacket::write, ClientboundLevelParticlesPacket::new);
/*     */   
/*     */   private final double x;
/*     */   private final double y;
/*     */   private final double z;
/*     */   private final float xDist;
/*     */   private final float yDist;
/*     */   private final float zDist;
/*     */   private final float maxSpeed;
/*     */   private final int count;
/*     */   private final boolean overrideLimiter;
/*     */   private final boolean alwaysShow;
/*     */   private final ParticleOptions particle;
/*     */   
/*     */   public <T extends ParticleOptions> ClientboundLevelParticlesPacket(T particle, boolean overrideLimiter, boolean alwaysShow, double x, double y, double z, float xDist, float yDist, float zDist, float maxSpeed, int count) {
/*  27 */     this.particle = (ParticleOptions)particle;
/*  28 */     this.overrideLimiter = overrideLimiter;
/*  29 */     this.alwaysShow = alwaysShow;
/*  30 */     this.x = x;
/*  31 */     this.y = y;
/*  32 */     this.z = z;
/*  33 */     this.xDist = xDist;
/*  34 */     this.yDist = yDist;
/*  35 */     this.zDist = zDist;
/*  36 */     this.maxSpeed = maxSpeed;
/*  37 */     this.count = count;
/*     */   }
/*     */   
/*     */   private ClientboundLevelParticlesPacket(RegistryFriendlyByteBuf input) {
/*  41 */     this.overrideLimiter = input.readBoolean();
/*  42 */     this.alwaysShow = input.readBoolean();
/*  43 */     this.x = input.readDouble();
/*  44 */     this.y = input.readDouble();
/*  45 */     this.z = input.readDouble();
/*  46 */     this.xDist = input.readFloat();
/*  47 */     this.yDist = input.readFloat();
/*  48 */     this.zDist = input.readFloat();
/*  49 */     this.maxSpeed = input.readFloat();
/*  50 */     this.count = input.readInt();
/*  51 */     this.particle = (ParticleOptions)ParticleTypes.STREAM_CODEC.decode(input);
/*     */   }
/*     */   
/*     */   private void write(RegistryFriendlyByteBuf output) {
/*  55 */     output.writeBoolean(this.overrideLimiter);
/*  56 */     output.writeBoolean(this.alwaysShow);
/*  57 */     output.writeDouble(this.x);
/*  58 */     output.writeDouble(this.y);
/*  59 */     output.writeDouble(this.z);
/*  60 */     output.writeFloat(this.xDist);
/*  61 */     output.writeFloat(this.yDist);
/*  62 */     output.writeFloat(this.zDist);
/*  63 */     output.writeFloat(this.maxSpeed);
/*  64 */     output.writeInt(this.count);
/*  65 */     ParticleTypes.STREAM_CODEC.encode(output, this.particle);
/*     */   }
/*     */ 
/*     */   
/*     */   public PacketType<ClientboundLevelParticlesPacket> type() {
/*  70 */     return GamePacketTypes.CLIENTBOUND_LEVEL_PARTICLES;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ClientGamePacketListener listener) {
/*  75 */     listener.handleParticleEvent(this);
/*     */   }
/*     */   
/*     */   public boolean isOverrideLimiter() {
/*  79 */     return this.overrideLimiter;
/*     */   }
/*     */   
/*     */   public boolean alwaysShow() {
/*  83 */     return this.alwaysShow;
/*     */   }
/*     */   
/*     */   public double getX() {
/*  87 */     return this.x;
/*     */   }
/*     */   
/*     */   public double getY() {
/*  91 */     return this.y;
/*     */   }
/*     */   
/*     */   public double getZ() {
/*  95 */     return this.z;
/*     */   }
/*     */   
/*     */   public float getXDist() {
/*  99 */     return this.xDist;
/*     */   }
/*     */   
/*     */   public float getYDist() {
/* 103 */     return this.yDist;
/*     */   }
/*     */   
/*     */   public float getZDist() {
/* 107 */     return this.zDist;
/*     */   }
/*     */   
/*     */   public float getMaxSpeed() {
/* 111 */     return this.maxSpeed;
/*     */   }
/*     */   
/*     */   public int getCount() {
/* 115 */     return this.count;
/*     */   }
/*     */   
/*     */   public ParticleOptions getParticle() {
/* 119 */     return this.particle;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundLevelParticlesPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */