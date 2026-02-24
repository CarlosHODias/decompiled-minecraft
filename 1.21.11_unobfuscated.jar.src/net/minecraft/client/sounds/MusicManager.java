/*     */ package net.minecraft.client.sounds;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*     */ import net.minecraft.client.resources.sounds.Sound;
/*     */ import net.minecraft.client.resources.sounds.SoundInstance;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.sounds.Music;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MusicManager
/*     */ {
/*     */   private static final int STARTING_DELAY = 100;
/*  21 */   private final RandomSource random = RandomSource.create();
/*     */   private final Minecraft minecraft;
/*     */   private SoundInstance currentMusic;
/*     */   private MusicFrequency gameMusicFrequency;
/*  25 */   private float currentGain = 1.0F;
/*  26 */   private int nextSongDelay = 100;
/*     */   private boolean toastShown = false;
/*     */   
/*     */   public MusicManager(Minecraft minecraft) {
/*  30 */     this.minecraft = minecraft;
/*  31 */     this.gameMusicFrequency = (MusicFrequency)minecraft.options.musicFrequency().get();
/*     */   }
/*     */   
/*     */   public void tick() {
/*  35 */     float volume = this.minecraft.getMusicVolume();
/*  36 */     if (this.currentMusic != null && this.currentGain != volume) {
/*  37 */       boolean stillPlaying = fadePlaying(volume);
/*  38 */       if (!stillPlaying) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */     
/*  43 */     Music music = this.minecraft.getSituationalMusic();
/*  44 */     if (music == null) {
/*  45 */       this.nextSongDelay = Math.max(this.nextSongDelay, 100);
/*     */       
/*     */       return;
/*     */     } 
/*  49 */     if (this.currentMusic != null) {
/*  50 */       if (canReplace(music, this.currentMusic)) {
/*  51 */         this.minecraft.getSoundManager().stop(this.currentMusic);
/*  52 */         this.nextSongDelay = Mth.nextInt(this.random, 0, music.minDelay() / 2);
/*     */       } 
/*     */       
/*  55 */       if (!this.minecraft.getSoundManager().isActive(this.currentMusic)) {
/*  56 */         this.currentMusic = null;
/*  57 */         this.nextSongDelay = Math.min(this.nextSongDelay, this.gameMusicFrequency.getNextSongDelay(music, this.random));
/*     */       } 
/*     */     } 
/*     */     
/*  61 */     this.nextSongDelay = Math.min(this.nextSongDelay, this.gameMusicFrequency.getNextSongDelay(music, this.random));
/*     */     
/*  63 */     if (this.currentMusic == null && this.nextSongDelay-- <= 0) {
/*  64 */       startPlaying(music);
/*     */     }
/*     */   }
/*     */   
/*     */   private static boolean canReplace(Music music, SoundInstance currentMusic) {
/*  69 */     return (music.replaceCurrentMusic() && !((SoundEvent)music.sound().value()).location().equals(currentMusic.getIdentifier()));
/*     */   }
/*     */   
/*     */   public void startPlaying(Music music) {
/*  73 */     SoundEvent soundEvent = (SoundEvent)music.sound().value();
/*  74 */     this.currentMusic = (SoundInstance)SimpleSoundInstance.forMusic(soundEvent);
/*  75 */     switch (this.minecraft.getSoundManager().play(this.currentMusic)) {
/*     */       case STARTED:
/*  77 */         this.minecraft.getToastManager().showNowPlayingToast();
/*  78 */         this.toastShown = true; break;
/*     */       case STARTED_SILENTLY:
/*  80 */         this.toastShown = false; break;
/*     */     } 
/*  82 */     this.nextSongDelay = Integer.MAX_VALUE;
/*     */   }
/*     */   
/*     */   public void showNowPlayingToastIfNeeded() {
/*  86 */     if (!this.toastShown) {
/*  87 */       this.minecraft.getToastManager().showNowPlayingToast();
/*  88 */       this.toastShown = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void stopPlaying(Music music) {
/*  93 */     if (isPlayingMusic(music)) {
/*  94 */       stopPlaying();
/*     */     }
/*     */   }
/*     */   
/*     */   public void stopPlaying() {
/*  99 */     if (this.currentMusic != null) {
/* 100 */       this.minecraft.getSoundManager().stop(this.currentMusic);
/* 101 */       this.currentMusic = null;
/* 102 */       this.minecraft.getToastManager().hideNowPlayingToast();
/*     */     } 
/* 104 */     this.nextSongDelay += 100;
/*     */   }
/*     */   
/*     */   private boolean fadePlaying(float volume) {
/* 108 */     if (this.currentMusic == null) {
/* 109 */       return false;
/*     */     }
/* 111 */     if (this.currentGain == volume) {
/* 112 */       return true;
/*     */     }
/* 114 */     if (this.currentGain < volume) {
/*     */ 
/*     */       
/* 117 */       this.currentGain += Mth.clamp(this.currentGain, 5.0E-4F, 0.005F);
/* 118 */       if (this.currentGain > volume) {
/* 119 */         this.currentGain = volume;
/*     */       }
/*     */     }
/*     */     else {
/*     */       
/* 124 */       this.currentGain = 0.03F * volume + 0.97F * this.currentGain;
/* 125 */       if (Math.abs(this.currentGain - volume) < 1.0E-4F || this.currentGain < volume) {
/* 126 */         this.currentGain = volume;
/*     */       }
/*     */     } 
/* 129 */     this.currentGain = Mth.clamp(this.currentGain, 0.0F, 1.0F);
/* 130 */     if (this.currentGain <= 1.0E-4F) {
/* 131 */       stopPlaying();
/* 132 */       return false;
/*     */     } 
/*     */     
/* 135 */     this.minecraft.getSoundManager().updateCategoryVolume(SoundSource.MUSIC, this.currentGain);
/* 136 */     return true;
/*     */   }
/*     */   
/*     */   public boolean isPlayingMusic(Music music) {
/* 140 */     if (this.currentMusic == null) {
/* 141 */       return false;
/*     */     }
/*     */     
/* 144 */     return ((SoundEvent)music.sound().value()).location().equals(this.currentMusic.getIdentifier());
/*     */   }
/*     */   
/*     */   public String getCurrentMusicTranslationKey() {
/* 148 */     if (this.currentMusic != null) {
/* 149 */       Sound sound = this.currentMusic.getSound();
/* 150 */       if (sound != null) {
/* 151 */         return sound.getLocation().toShortLanguageKey();
/*     */       }
/*     */     } 
/* 154 */     return null;
/*     */   }
/*     */   
/*     */   public void setMinutesBetweenSongs(MusicFrequency musicFrequency) {
/* 158 */     this.gameMusicFrequency = musicFrequency;
/* 159 */     this.nextSongDelay = this.gameMusicFrequency.getNextSongDelay(this.minecraft.getSituationalMusic(), this.random);
/*     */   }
/*     */   
/*     */   public enum MusicFrequency implements StringRepresentable {
/* 163 */     DEFAULT("DEFAULT", "options.music_frequency.default", 20),
/* 164 */     FREQUENT("FREQUENT", "options.music_frequency.frequent", 10),
/* 165 */     CONSTANT("CONSTANT", "options.music_frequency.constant", 0);
/*     */ 
/*     */     
/* 168 */     public static final Codec<MusicFrequency> CODEC = (Codec<MusicFrequency>)StringRepresentable.fromEnum(MusicFrequency::values);
/*     */     
/*     */     private final String name;
/*     */     private final int maxFrequency;
/*     */     private final Component caption;
/*     */     
/*     */     MusicFrequency(String name, String translationKey, int maxFrequencyMinutes) {
/* 175 */       this.name = name;
/* 176 */       this.maxFrequency = maxFrequencyMinutes * 1200;
/* 177 */       this.caption = (Component)Component.translatable(translationKey);
/*     */     }
/*     */     
/*     */     private int getNextSongDelay(Music music, RandomSource random) {
/* 181 */       if (music == null)
/* 182 */         return this.maxFrequency; 
/* 183 */       if (this == CONSTANT) {
/* 184 */         return 100;
/*     */       }
/* 186 */       int minFrequency = Math.min(music.minDelay(), this.maxFrequency);
/* 187 */       int maxFrequency = Math.min(music.maxDelay(), this.maxFrequency);
/* 188 */       return Mth.nextInt(random, minFrequency, maxFrequency);
/*     */     }
/*     */ 
/*     */     
/*     */     public Component caption() {
/* 193 */       return this.caption;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 198 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/sounds/MusicManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */