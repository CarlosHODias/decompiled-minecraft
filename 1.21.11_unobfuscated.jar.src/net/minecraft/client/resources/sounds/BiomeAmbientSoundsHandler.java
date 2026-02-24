/*     */ package net.minecraft.client.resources.sounds;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.player.LocalPlayer;
/*     */ import net.minecraft.client.sounds.SoundManager;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.sounds.SoundEvent;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.attribute.AmbientAdditionsSettings;
/*     */ import net.minecraft.world.attribute.AmbientMoodSettings;
/*     */ import net.minecraft.world.attribute.AmbientSounds;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributeSystem;
/*     */ import net.minecraft.world.attribute.EnvironmentAttributes;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BiomeAmbientSoundsHandler
/*     */   implements AmbientSoundHandler
/*     */ {
/*     */   private static final int LOOP_SOUND_CROSS_FADE_TIME = 40;
/*     */   private static final float SKY_MOOD_RECOVERY_RATE = 0.001F;
/*     */   private final LocalPlayer player;
/*     */   private final SoundManager soundManager;
/*     */   private final RandomSource random;
/*  32 */   private final Object2ObjectArrayMap<Holder<SoundEvent>, LoopSoundInstance> loopSounds = new Object2ObjectArrayMap();
/*     */   
/*     */   private float moodiness;
/*     */   private Holder<SoundEvent> previousLoopSound;
/*     */   
/*     */   public BiomeAmbientSoundsHandler(LocalPlayer player, SoundManager soundManager) {
/*  38 */     this.random = player.level().getRandom();
/*     */     
/*  40 */     this.player = player;
/*  41 */     this.soundManager = soundManager;
/*     */   }
/*     */   
/*     */   public float getMoodiness() {
/*  45 */     return this.moodiness;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  50 */     this.loopSounds.values().removeIf(AbstractTickableSoundInstance::isStopped);
/*     */     
/*  52 */     Level level = this.player.level();
/*  53 */     EnvironmentAttributeSystem environmentAttributes = level.environmentAttributes();
/*  54 */     AmbientSounds ambientSounds = (AmbientSounds)environmentAttributes.getValue(EnvironmentAttributes.AMBIENT_SOUNDS, this.player.position());
/*     */     
/*  56 */     Holder<SoundEvent> currentLoopSound = ambientSounds.loop().orElse(null);
/*  57 */     if (!Objects.equals(currentLoopSound, this.previousLoopSound)) {
/*  58 */       this.previousLoopSound = currentLoopSound;
/*     */       
/*  60 */       this.loopSounds.values().forEach(LoopSoundInstance::fadeOut);
/*     */       
/*  62 */       if (currentLoopSound != null) {
/*  63 */         this.loopSounds.compute(currentLoopSound, (biomeKey, soundInstance) -> {
/*     */               if (soundInstance == null) {
/*     */                 soundInstance = new LoopSoundInstance((SoundEvent)currentLoopSound.value());
/*     */                 
/*     */                 this.soundManager.play(soundInstance);
/*     */               } 
/*     */               soundInstance.fadeIn();
/*     */               return soundInstance;
/*     */             });
/*     */       }
/*     */     } 
/*  74 */     for (AmbientAdditionsSettings additions : (Iterable<AmbientAdditionsSettings>)ambientSounds.additions()) {
/*  75 */       if (this.random.nextDouble() < additions.tickChance()) {
/*  76 */         this.soundManager.play(SimpleSoundInstance.forAmbientAddition((SoundEvent)additions.soundEvent().value()));
/*     */       }
/*     */     } 
/*     */     
/*  80 */     ambientSounds.mood().ifPresent(mood -> {
/*     */           int searchSpan = level.blockSearchExtent() * 2 + 1;
/*     */           BlockPos blockSamplingPos = BlockPos.containing(this.player.getX() + this.random.nextInt(searchSpan) - level.blockSearchExtent(), this.player.getEyeY() + this.random.nextInt(searchSpan) - level.blockSearchExtent(), this.player.getZ() + this.random.nextInt(searchSpan) - level.blockSearchExtent());
/*     */           int skyBrightness = level.getBrightness(LightLayer.SKY, blockSamplingPos);
/*     */           if (skyBrightness > 0) {
/*     */             this.moodiness -= skyBrightness / 15.0F * 0.001F;
/*     */           } else {
/*     */             this.moodiness -= (level.getBrightness(LightLayer.BLOCK, blockSamplingPos) - 1) / level.tickDelay();
/*     */           } 
/*     */           if (this.moodiness >= 1.0F) {
/*     */             double blockSampleX = blockSamplingPos.getX() + 0.5D, blockSampleY = blockSamplingPos.getY() + 0.5D, blockSampleZ = blockSamplingPos.getZ() + 0.5D, blockDirectionX = blockSampleX - this.player.getX(), blockDirectionY = blockSampleY - this.player.getEyeY(), blockDirectionZ = blockSampleZ - this.player.getZ(), blockDistance = Math.sqrt(blockDirectionX * blockDirectionX + blockDirectionY * blockDirectionY + blockDirectionZ * blockDirectionZ), soundSourceDistance = blockDistance + level.soundPositionOffset();
/*     */             SimpleSoundInstance moodSoundInstance = SimpleSoundInstance.forAmbientMood((SoundEvent)level.soundEvent().value(), this.random, this.player.getX() + blockDirectionX / blockDistance * soundSourceDistance, this.player.getEyeY() + blockDirectionY / blockDistance * soundSourceDistance, this.player.getZ() + blockDirectionZ / blockDistance * soundSourceDistance);
/*     */             this.soundManager.play(moodSoundInstance);
/*     */             this.moodiness = 0.0F;
/*     */           } else {
/*     */             this.moodiness = Math.max(this.moodiness, 0.0F);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class LoopSoundInstance
/*     */     extends AbstractTickableSoundInstance
/*     */   {
/*     */     private int fadeDirection;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private int fade;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public LoopSoundInstance(SoundEvent soundEvent) {
/* 128 */       super(soundEvent, SoundSource.AMBIENT, SoundInstance.createUnseededRandom());
/*     */       
/* 130 */       this.looping = true;
/* 131 */       this.delay = 0;
/* 132 */       this.volume = 1.0F;
/* 133 */       this.relative = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public void tick() {
/* 138 */       if (this.fade < 0) {
/* 139 */         stop();
/*     */       }
/*     */       
/* 142 */       this.fade += this.fadeDirection;
/* 143 */       this.volume = Mth.clamp(this.fade / 40.0F, 0.0F, 1.0F);
/*     */     }
/*     */     
/*     */     public void fadeOut() {
/* 147 */       this.fade = Math.min(this.fade, 40);
/* 148 */       this.fadeDirection = -1;
/*     */     }
/*     */     
/*     */     public void fadeIn() {
/* 152 */       this.fade = Math.max(0, this.fade);
/* 153 */       this.fadeDirection = 1;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/sounds/BiomeAmbientSoundsHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */