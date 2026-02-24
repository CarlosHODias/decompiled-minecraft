/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import net.minecraft.client.player.LocalPlayer;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ 
/*    */ public class ElytraOnPlayerSoundInstance
/*    */   extends AbstractTickableSoundInstance
/*    */ {
/*    */   public static final int DELAY = 20;
/*    */   private final LocalPlayer player;
/*    */   private int time;
/*    */   
/*    */   public ElytraOnPlayerSoundInstance(LocalPlayer player) {
/* 17 */     super(SoundEvents.ELYTRA_FLYING, SoundSource.PLAYERS, SoundInstance.createUnseededRandom());
/*    */     
/* 19 */     this.player = player;
/* 20 */     this.looping = true;
/* 21 */     this.delay = 0;
/* 22 */     this.volume = 0.1F;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 27 */     this.time++;
/* 28 */     if (this.player.isRemoved() || (this.time > 20 && !this.player.isFallFlying())) {
/* 29 */       stop();
/*    */       
/*    */       return;
/*    */     } 
/* 33 */     this.x = (float)this.player.getX();
/* 34 */     this.y = (float)this.player.getY();
/* 35 */     this.z = (float)this.player.getZ();
/*    */     
/* 37 */     float speed = (float)this.player.getDeltaMovement().lengthSqr();
/* 38 */     if (speed >= 1.0E-7D) {
/* 39 */       this.volume = Mth.clamp(speed / 4.0F, 0.0F, 1.0F);
/*    */     } else {
/* 41 */       this.volume = 0.0F;
/*    */     } 
/*    */ 
/*    */     
/* 45 */     if (this.time < 20) {
/* 46 */       this.volume = 0.0F;
/* 47 */     } else if (this.time < 40) {
/* 48 */       this.volume *= (this.time - 20) / 20.0F;
/*    */     } 
/*    */     
/* 51 */     float pitchThreshold = 0.8F;
/* 52 */     if (this.volume > 0.8F) {
/* 53 */       this.pitch = 1.0F + this.volume - 0.8F;
/*    */     } else {
/* 55 */       this.pitch = 1.0F;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/sounds/ElytraOnPlayerSoundInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */