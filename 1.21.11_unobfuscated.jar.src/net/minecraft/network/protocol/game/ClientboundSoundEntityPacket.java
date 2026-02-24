/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ 
/*    */ public class ClientboundSoundEntityPacket implements Packet<ClientGamePacketListener> {
/* 13 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSoundEntityPacket> STREAM_CODEC = Packet.codec(ClientboundSoundEntityPacket::write, ClientboundSoundEntityPacket::new);
/*    */   
/*    */   private final Holder<SoundEvent> sound;
/*    */   private final SoundSource source;
/*    */   private final int id;
/*    */   private final float volume;
/*    */   private final float pitch;
/*    */   private final long seed;
/*    */   
/*    */   public ClientboundSoundEntityPacket(Holder<SoundEvent> sound, SoundSource source, Entity sourceEntity, float volume, float pitch, long seed) {
/* 23 */     this.sound = sound;
/* 24 */     this.source = source;
/* 25 */     this.id = sourceEntity.getId();
/* 26 */     this.volume = volume;
/* 27 */     this.pitch = pitch;
/* 28 */     this.seed = seed;
/*    */   }
/*    */   
/*    */   private ClientboundSoundEntityPacket(RegistryFriendlyByteBuf input) {
/* 32 */     this.sound = (Holder<SoundEvent>)SoundEvent.STREAM_CODEC.decode(input);
/* 33 */     this.source = (SoundSource)input.readEnum(SoundSource.class);
/* 34 */     this.id = input.readVarInt();
/* 35 */     this.volume = input.readFloat();
/* 36 */     this.pitch = input.readFloat();
/* 37 */     this.seed = input.readLong();
/*    */   }
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 41 */     SoundEvent.STREAM_CODEC.encode(output, this.sound);
/* 42 */     output.writeEnum((Enum)this.source);
/* 43 */     output.writeVarInt(this.id);
/* 44 */     output.writeFloat(this.volume);
/* 45 */     output.writeFloat(this.pitch);
/* 46 */     output.writeLong(this.seed);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundSoundEntityPacket> type() {
/* 51 */     return GamePacketTypes.CLIENTBOUND_SOUND_ENTITY;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 56 */     listener.handleSoundEntityEvent(this);
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> getSound() {
/* 60 */     return this.sound;
/*    */   }
/*    */   
/*    */   public SoundSource getSource() {
/* 64 */     return this.source;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 68 */     return this.id;
/*    */   }
/*    */   
/*    */   public float getVolume() {
/* 72 */     return this.volume;
/*    */   }
/*    */   
/*    */   public float getPitch() {
/* 76 */     return this.pitch;
/*    */   }
/*    */   
/*    */   public long getSeed() {
/* 80 */     return this.seed;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundSoundEntityPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */