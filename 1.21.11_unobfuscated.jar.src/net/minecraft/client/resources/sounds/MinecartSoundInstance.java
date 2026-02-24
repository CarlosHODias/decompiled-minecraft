/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
/*    */ 
/*    */ public class MinecartSoundInstance
/*    */   extends AbstractTickableSoundInstance
/*    */ {
/*    */   private static final float VOLUME_MIN = 0.0F;
/*    */   private static final float VOLUME_MAX = 0.7F;
/*    */   private static final float PITCH_MIN = 0.0F;
/*    */   private static final float PITCH_MAX = 1.0F;
/*    */   private static final float PITCH_DELTA = 0.0025F;
/*    */   private final AbstractMinecart minecart;
/* 17 */   private float pitch = 0.0F;
/*    */   
/*    */   public MinecartSoundInstance(AbstractMinecart minecart) {
/* 20 */     super(SoundEvents.MINECART_RIDING, SoundSource.NEUTRAL, SoundInstance.createUnseededRandom());
/* 21 */     this.minecart = minecart;
/* 22 */     this.looping = true;
/* 23 */     this.delay = 0;
/* 24 */     this.volume = 0.0F;
/* 25 */     this.x = (float)minecart.getX();
/* 26 */     this.y = (float)minecart.getY();
/* 27 */     this.z = (float)minecart.getZ();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canPlaySound() {
/* 32 */     return !this.minecart.isSilent();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canStartSilent() {
/* 37 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 42 */     if (this.minecart.isRemoved()) {
/* 43 */       stop();
/*    */       
/*    */       return;
/*    */     } 
/* 47 */     this.x = (float)this.minecart.getX();
/* 48 */     this.y = (float)this.minecart.getY();
/* 49 */     this.z = (float)this.minecart.getZ();
/*    */     
/* 51 */     float speed = (float)this.minecart.getDeltaMovement().horizontalDistance();
/* 52 */     boolean offRail = (!this.minecart.isOnRails() && this.minecart.getBehavior() instanceof net.minecraft.world.entity.vehicle.minecart.NewMinecartBehavior);
/* 53 */     if (speed >= 0.01F && this.minecart.level().tickRateManager().runsNormally() && !offRail) {
/* 54 */       this.pitch = Mth.clamp(this.pitch + 0.0025F, 0.0F, 1.0F);
/*    */       
/* 56 */       this.volume = Mth.lerp(Mth.clamp(speed, 0.0F, 0.5F), 0.0F, 0.7F);
/*    */     } else {
/* 58 */       this.pitch = 0.0F;
/* 59 */       this.volume = 0.0F;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/sounds/MinecartSoundInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */