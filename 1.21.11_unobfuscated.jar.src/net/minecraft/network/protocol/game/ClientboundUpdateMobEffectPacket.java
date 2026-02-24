/*     */ package net.minecraft.network.protocol.game;
/*     */ 
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.network.codec.StreamDecoder;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketType;
/*     */ import net.minecraft.world.effect.MobEffect;
/*     */ import net.minecraft.world.effect.MobEffectInstance;
/*     */ 
/*     */ public class ClientboundUpdateMobEffectPacket
/*     */   implements Packet<ClientGamePacketListener> {
/*  15 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateMobEffectPacket> STREAM_CODEC = Packet.codec(ClientboundUpdateMobEffectPacket::write, ClientboundUpdateMobEffectPacket::new);
/*     */   
/*     */   private static final int FLAG_AMBIENT = 1;
/*     */   
/*     */   private static final int FLAG_VISIBLE = 2;
/*     */   private static final int FLAG_SHOW_ICON = 4;
/*     */   private static final int FLAG_BLEND = 8;
/*     */   private final int entityId;
/*     */   private final Holder<MobEffect> effect;
/*     */   private final int effectAmplifier;
/*     */   private final int effectDurationTicks;
/*     */   private final byte flags;
/*     */   
/*     */   public ClientboundUpdateMobEffectPacket(int entityId, MobEffectInstance effect, boolean blend) {
/*  29 */     this.entityId = entityId;
/*  30 */     this.effect = effect.getEffect();
/*  31 */     this.effectAmplifier = effect.getAmplifier();
/*  32 */     this.effectDurationTicks = effect.getDuration();
/*  33 */     byte flags = 0;
/*     */     
/*  35 */     if (effect.isAmbient()) {
/*  36 */       flags = (byte)(flags | 0x1);
/*     */     }
/*  38 */     if (effect.isVisible()) {
/*  39 */       flags = (byte)(flags | 0x2);
/*     */     }
/*  41 */     if (effect.showIcon()) {
/*  42 */       flags = (byte)(flags | 0x4);
/*     */     }
/*  44 */     if (blend) {
/*  45 */       flags = (byte)(flags | 0x8);
/*     */     }
/*  47 */     this.flags = flags;
/*     */   }
/*     */   
/*     */   private ClientboundUpdateMobEffectPacket(RegistryFriendlyByteBuf input) {
/*  51 */     this.entityId = input.readVarInt();
/*  52 */     this.effect = (Holder<MobEffect>)MobEffect.STREAM_CODEC.decode(input);
/*  53 */     this.effectAmplifier = input.readVarInt();
/*  54 */     this.effectDurationTicks = input.readVarInt();
/*  55 */     this.flags = input.readByte();
/*     */   }
/*     */   
/*     */   private void write(RegistryFriendlyByteBuf output) {
/*  59 */     output.writeVarInt(this.entityId);
/*  60 */     MobEffect.STREAM_CODEC.encode(output, this.effect);
/*  61 */     output.writeVarInt(this.effectAmplifier);
/*  62 */     output.writeVarInt(this.effectDurationTicks);
/*  63 */     output.writeByte(this.flags);
/*     */   }
/*     */ 
/*     */   
/*     */   public PacketType<ClientboundUpdateMobEffectPacket> type() {
/*  68 */     return GamePacketTypes.CLIENTBOUND_UPDATE_MOB_EFFECT;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handle(ClientGamePacketListener listener) {
/*  73 */     listener.handleUpdateMobEffect(this);
/*     */   }
/*     */   
/*     */   public int getEntityId() {
/*  77 */     return this.entityId;
/*     */   }
/*     */   
/*     */   public Holder<MobEffect> getEffect() {
/*  81 */     return this.effect;
/*     */   }
/*     */   
/*     */   public int getEffectAmplifier() {
/*  85 */     return this.effectAmplifier;
/*     */   }
/*     */   
/*     */   public int getEffectDurationTicks() {
/*  89 */     return this.effectDurationTicks;
/*     */   }
/*     */   
/*     */   public boolean isEffectVisible() {
/*  93 */     return ((this.flags & 0x2) != 0);
/*     */   }
/*     */   
/*     */   public boolean isEffectAmbient() {
/*  97 */     return ((this.flags & 0x1) != 0);
/*     */   }
/*     */   
/*     */   public boolean effectShowsIcon() {
/* 101 */     return ((this.flags & 0x4) != 0);
/*     */   }
/*     */   
/*     */   public boolean shouldBlend() {
/* 105 */     return ((this.flags & 0x8) != 0);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundUpdateMobEffectPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */