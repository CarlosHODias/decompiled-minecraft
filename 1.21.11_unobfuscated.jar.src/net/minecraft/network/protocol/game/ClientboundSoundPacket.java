/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ 
/*    */ public class ClientboundSoundPacket implements Packet<ClientGamePacketListener> {
/* 12 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSoundPacket> STREAM_CODEC = Packet.codec(ClientboundSoundPacket::write, ClientboundSoundPacket::new);
/*    */   
/*    */   public static final float LOCATION_ACCURACY = 8.0F;
/*    */   
/*    */   private final Holder<SoundEvent> sound;
/*    */   private final SoundSource source;
/*    */   private final int x;
/*    */   private final int y;
/*    */   private final int z;
/*    */   private final float volume;
/*    */   private final float pitch;
/*    */   private final long seed;
/*    */   
/*    */   public ClientboundSoundPacket(Holder<SoundEvent> sound, SoundSource source, double x, double y, double z, float volume, float pitch, long seed) {
/* 26 */     this.sound = sound;
/* 27 */     this.source = source;
/* 28 */     this.x = (int)(x * 8.0D);
/* 29 */     this.y = (int)(y * 8.0D);
/* 30 */     this.z = (int)(z * 8.0D);
/* 31 */     this.volume = volume;
/* 32 */     this.pitch = pitch;
/* 33 */     this.seed = seed;
/*    */   }
/*    */   
/*    */   private ClientboundSoundPacket(RegistryFriendlyByteBuf input) {
/* 37 */     this.sound = (Holder<SoundEvent>)SoundEvent.STREAM_CODEC.decode(input);
/* 38 */     this.source = (SoundSource)input.readEnum(SoundSource.class);
/* 39 */     this.x = input.readInt();
/* 40 */     this.y = input.readInt();
/* 41 */     this.z = input.readInt();
/* 42 */     this.volume = input.readFloat();
/* 43 */     this.pitch = input.readFloat();
/* 44 */     this.seed = input.readLong();
/*    */   }
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 48 */     SoundEvent.STREAM_CODEC.encode(output, this.sound);
/* 49 */     output.writeEnum((Enum)this.source);
/* 50 */     output.writeInt(this.x);
/* 51 */     output.writeInt(this.y);
/* 52 */     output.writeInt(this.z);
/* 53 */     output.writeFloat(this.volume);
/* 54 */     output.writeFloat(this.pitch);
/* 55 */     output.writeLong(this.seed);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundSoundPacket> type() {
/* 60 */     return GamePacketTypes.CLIENTBOUND_SOUND;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 65 */     listener.handleSoundEvent(this);
/*    */   }
/*    */   
/*    */   public Holder<SoundEvent> getSound() {
/* 69 */     return this.sound;
/*    */   }
/*    */   
/*    */   public SoundSource getSource() {
/* 73 */     return this.source;
/*    */   }
/*    */   
/*    */   public double getX() {
/* 77 */     return (this.x / 8.0F);
/*    */   }
/*    */   
/*    */   public double getY() {
/* 81 */     return (this.y / 8.0F);
/*    */   }
/*    */   
/*    */   public double getZ() {
/* 85 */     return (this.z / 8.0F);
/*    */   }
/*    */   
/*    */   public float getVolume() {
/* 89 */     return this.volume;
/*    */   }
/*    */   
/*    */   public float getPitch() {
/* 93 */     return this.pitch;
/*    */   }
/*    */   
/*    */   public long getSeed() {
/* 97 */     return this.seed;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundSoundPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */