/*     */ package net.minecraft.client.resources.sounds;
/*     */ 
/*     */ import net.minecraft.client.sounds.SoundManager;
/*     */ import net.minecraft.client.sounds.WeighedSoundEvents;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.RandomSource;
/*     */ 
/*     */ public abstract class AbstractSoundInstance
/*     */   implements SoundInstance
/*     */ {
/*     */   protected Sound sound;
/*     */   protected final SoundSource source;
/*     */   protected final Identifier identifier;
/*  16 */   protected float volume = 1.0F;
/*  17 */   protected float pitch = 1.0F;
/*     */   protected double x;
/*     */   protected double y;
/*     */   protected double z;
/*     */   protected boolean looping;
/*     */   protected int delay;
/*  23 */   protected SoundInstance.Attenuation attenuation = SoundInstance.Attenuation.LINEAR;
/*     */   protected boolean relative;
/*     */   protected RandomSource random;
/*     */   
/*     */   protected AbstractSoundInstance(SoundEvent event, SoundSource source, RandomSource random) {
/*  28 */     this(event.location(), source, random);
/*     */   }
/*     */   
/*     */   protected AbstractSoundInstance(Identifier identifier, SoundSource source, RandomSource random) {
/*  32 */     this.identifier = identifier;
/*  33 */     this.source = source;
/*  34 */     this.random = random;
/*     */   }
/*     */ 
/*     */   
/*     */   public Identifier getIdentifier() {
/*  39 */     return this.identifier;
/*     */   }
/*     */ 
/*     */   
/*     */   public WeighedSoundEvents resolve(SoundManager soundManager) {
/*  44 */     if (this.identifier.equals(SoundManager.INTENTIONALLY_EMPTY_SOUND_LOCATION)) {
/*  45 */       this.sound = SoundManager.INTENTIONALLY_EMPTY_SOUND;
/*  46 */       return SoundManager.INTENTIONALLY_EMPTY_SOUND_EVENT;
/*     */     } 
/*  48 */     WeighedSoundEvents soundEvent = soundManager.getSoundEvent(this.identifier);
/*  49 */     if (soundEvent == null) {
/*  50 */       this.sound = SoundManager.EMPTY_SOUND;
/*     */     } else {
/*  52 */       this.sound = soundEvent.getSound(this.random);
/*     */     } 
/*     */     
/*  55 */     return soundEvent;
/*     */   }
/*     */ 
/*     */   
/*     */   public Sound getSound() {
/*  60 */     return this.sound;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundSource getSource() {
/*  65 */     return this.source;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLooping() {
/*  70 */     return this.looping;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getDelay() {
/*  75 */     return this.delay;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getVolume() {
/*  80 */     return this.volume * this.sound.getVolume().sample(this.random);
/*     */   }
/*     */ 
/*     */   
/*     */   public float getPitch() {
/*  85 */     return this.pitch * this.sound.getPitch().sample(this.random);
/*     */   }
/*     */ 
/*     */   
/*     */   public double getX() {
/*  90 */     return this.x;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getY() {
/*  95 */     return this.y;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getZ() {
/* 100 */     return this.z;
/*     */   }
/*     */ 
/*     */   
/*     */   public SoundInstance.Attenuation getAttenuation() {
/* 105 */     return this.attenuation;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isRelative() {
/* 110 */     return this.relative;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 115 */     return "SoundInstance[" + String.valueOf(this.identifier) + "]";
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/sounds/AbstractSoundInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */