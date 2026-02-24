/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.animal.bee.Bee;
/*    */ 
/*    */ public abstract class BeeSoundInstance
/*    */   extends AbstractTickableSoundInstance {
/*    */   private static final float VOLUME_MIN = 0.0F;
/*    */   private static final float VOLUME_MAX = 1.2F;
/*    */   private static final float PITCH_MIN = 0.0F;
/*    */   protected final Bee bee;
/*    */   private boolean hasSwitched;
/*    */   
/*    */   public BeeSoundInstance(Bee bee, SoundEvent event, SoundSource source) {
/* 18 */     super(event, source, SoundInstance.createUnseededRandom());
/* 19 */     this.bee = bee;
/* 20 */     this.x = (float)bee.getX();
/* 21 */     this.y = (float)bee.getY();
/* 22 */     this.z = (float)bee.getZ();
/* 23 */     this.looping = true;
/* 24 */     this.delay = 0;
/* 25 */     this.volume = 0.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 30 */     boolean shouldSwitchSounds = shouldSwitchSounds();
/* 31 */     if (shouldSwitchSounds && !isStopped()) {
/*    */       
/* 33 */       Minecraft.getInstance().getSoundManager().queueTickingSound(getAlternativeSoundInstance());
/* 34 */       this.hasSwitched = true;
/*    */     } 
/*    */     
/* 37 */     if (this.bee.isRemoved() || this.hasSwitched) {
/* 38 */       stop();
/*    */       
/*    */       return;
/*    */     } 
/* 42 */     this.x = (float)this.bee.getX();
/* 43 */     this.y = (float)this.bee.getY();
/* 44 */     this.z = (float)this.bee.getZ();
/*    */     
/* 46 */     float speed = (float)this.bee.getDeltaMovement().horizontalDistance();
/* 47 */     if (speed >= 0.01F) {
/* 48 */       this.pitch = Mth.lerp(Mth.clamp(speed, getMinPitch(), getMaxPitch()), getMinPitch(), getMaxPitch());
/*    */       
/* 50 */       this.volume = Mth.lerp(Mth.clamp(speed, 0.0F, 0.5F), 0.0F, 1.2F);
/*    */     } else {
/* 52 */       this.pitch = 0.0F;
/* 53 */       this.volume = 0.0F;
/*    */     } 
/*    */   }
/*    */   
/*    */   private float getMinPitch() {
/* 58 */     if (this.bee.isBaby()) {
/* 59 */       return 1.1F;
/*    */     }
/* 61 */     return 0.7F;
/*    */   }
/*    */ 
/*    */   
/*    */   private float getMaxPitch() {
/* 66 */     if (this.bee.isBaby()) {
/* 67 */       return 1.5F;
/*    */     }
/* 69 */     return 1.1F;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canStartSilent() {
/* 75 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canPlaySound() {
/* 80 */     return !this.bee.isSilent();
/*    */   }
/*    */   
/*    */   protected abstract AbstractTickableSoundInstance getAlternativeSoundInstance();
/*    */   
/*    */   protected abstract boolean shouldSwitchSounds();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/sounds/BeeSoundInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */