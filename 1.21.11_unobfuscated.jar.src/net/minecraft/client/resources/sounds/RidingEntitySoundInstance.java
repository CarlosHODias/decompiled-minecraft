/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class RidingEntitySoundInstance extends AbstractTickableSoundInstance {
/*    */   private final Player player;
/*    */   private final Entity entity;
/*    */   private final boolean underwaterSound;
/*    */   private final float volumeMin;
/*    */   private final float volumeMax;
/*    */   private final float volumeAmplifier;
/*    */   
/*    */   public RidingEntitySoundInstance(Player player, Entity entity, boolean underwaterSound, SoundEvent soundEvent, SoundSource soundSource, float volumeMin, float volumeMax, float volumeAmplifier) {
/* 18 */     super(soundEvent, soundSource, SoundInstance.createUnseededRandom());
/* 19 */     this.player = player;
/* 20 */     this.entity = entity;
/* 21 */     this.underwaterSound = underwaterSound;
/* 22 */     this.volumeMin = volumeMin;
/* 23 */     this.volumeMax = volumeMax;
/* 24 */     this.volumeAmplifier = volumeAmplifier;
/*    */     
/* 26 */     this.attenuation = SoundInstance.Attenuation.NONE;
/* 27 */     this.looping = true;
/* 28 */     this.delay = 0;
/* 29 */     this.volume = volumeMin;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canPlaySound() {
/* 34 */     return !this.entity.isSilent();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canStartSilent() {
/* 39 */     return true;
/*    */   }
/*    */   
/*    */   protected boolean shouldNotPlayUnderwaterSound() {
/* 43 */     return (this.underwaterSound != this.entity.isUnderWater());
/*    */   }
/*    */   
/*    */   protected float getEntitySpeed() {
/* 47 */     return (float)this.entity.getDeltaMovement().length();
/*    */   }
/*    */   
/*    */   protected boolean shoudlPlaySound() {
/* 51 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 56 */     if (this.entity.isRemoved() || !this.player.isPassenger() || this.player.getVehicle() != this.entity) {
/* 57 */       stop();
/*    */       
/*    */       return;
/*    */     } 
/* 61 */     if (shouldNotPlayUnderwaterSound()) {
/* 62 */       this.volume = this.volumeMin;
/*    */       
/*    */       return;
/*    */     } 
/* 66 */     float speed = getEntitySpeed();
/* 67 */     if (speed >= 0.01F && shoudlPlaySound()) {
/* 68 */       this.volume = this.volumeAmplifier * Mth.clampedLerp(speed, this.volumeMin, this.volumeMax);
/*    */     } else {
/* 70 */       this.volume = this.volumeMin;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/sounds/RidingEntitySoundInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */