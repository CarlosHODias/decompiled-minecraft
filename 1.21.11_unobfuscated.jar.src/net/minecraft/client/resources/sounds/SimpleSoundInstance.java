/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class SimpleSoundInstance extends AbstractSoundInstance {
/*    */   public SimpleSoundInstance(SoundEvent sound, SoundSource source, float volume, float pitch, RandomSource random, BlockPos pos) {
/* 13 */     this(sound, source, volume, pitch, random, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
/*    */   }
/*    */   
/*    */   public static SimpleSoundInstance forUI(SoundEvent sound, float pitch) {
/* 17 */     return forUI(sound, pitch, 0.25F);
/*    */   }
/*    */   
/*    */   public static SimpleSoundInstance forUI(Holder<SoundEvent> sound, float pitch) {
/* 21 */     return forUI((SoundEvent)sound.value(), pitch);
/*    */   }
/*    */   
/*    */   public static SimpleSoundInstance forUI(SoundEvent sound, float pitch, float volume) {
/* 25 */     return new SimpleSoundInstance(sound.location(), SoundSource.UI, volume, pitch, SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D, true);
/*    */   }
/*    */   
/*    */   public static SimpleSoundInstance forMusic(SoundEvent sound) {
/* 29 */     return new SimpleSoundInstance(sound.location(), SoundSource.MUSIC, 1.0F, 1.0F, SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D, true);
/*    */   }
/*    */   
/*    */   public static SimpleSoundInstance forJukeboxSong(SoundEvent sound, Vec3 pos) {
/* 33 */     return new SimpleSoundInstance(sound, SoundSource.RECORDS, 4.0F, 1.0F, SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.LINEAR, pos.x, pos.y, pos.z);
/*    */   }
/*    */   
/*    */   public static SimpleSoundInstance forLocalAmbience(SoundEvent sound, float pitch, float volume) {
/* 37 */     return new SimpleSoundInstance(sound.location(), SoundSource.AMBIENT, volume, pitch, SoundInstance.createUnseededRandom(), false, 0, SoundInstance.Attenuation.NONE, 0.0D, 0.0D, 0.0D, true);
/*    */   }
/*    */   
/*    */   public static SimpleSoundInstance forAmbientAddition(SoundEvent sound) {
/* 41 */     return forLocalAmbience(sound, 1.0F, 1.0F);
/*    */   }
/*    */   
/*    */   public static SimpleSoundInstance forAmbientMood(SoundEvent sound, RandomSource random, double x, double y, double z) {
/* 45 */     return new SimpleSoundInstance(sound, SoundSource.AMBIENT, 1.0F, 1.0F, random, false, 0, SoundInstance.Attenuation.LINEAR, x, y, z);
/*    */   }
/*    */   
/*    */   public SimpleSoundInstance(SoundEvent sound, SoundSource source, float volume, float pitch, RandomSource random, double x, double y, double z) {
/* 49 */     this(sound, source, volume, pitch, random, false, 0, SoundInstance.Attenuation.LINEAR, x, y, z);
/*    */   }
/*    */   
/*    */   private SimpleSoundInstance(SoundEvent sound, SoundSource source, float volume, float pitch, RandomSource random, boolean looping, int delay, SoundInstance.Attenuation attenuation, double x, double y, double z) {
/* 53 */     this(sound.location(), source, volume, pitch, random, looping, delay, attenuation, x, y, z, false);
/*    */   }
/*    */   
/*    */   public SimpleSoundInstance(Identifier location, SoundSource source, float volume, float pitch, RandomSource random, boolean looping, int delay, SoundInstance.Attenuation attenuation, double x, double y, double z, boolean relative) {
/* 57 */     super(location, source, random);
/* 58 */     this.volume = volume;
/* 59 */     this.pitch = pitch;
/* 60 */     this.x = x;
/* 61 */     this.y = y;
/* 62 */     this.z = z;
/* 63 */     this.looping = looping;
/* 64 */     this.delay = delay;
/* 65 */     this.attenuation = attenuation;
/* 66 */     this.relative = relative;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/sounds/SimpleSoundInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */