/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ 
/*    */ public class EndFlashState
/*    */ {
/*    */   public static final int SOUND_DELAY_IN_TICKS = 30;
/*    */   private static final int FLASH_INTERVAL_IN_TICKS = 600;
/*    */   private static final int MAX_FLASH_OFFSET_IN_TICKS = 200;
/*    */   private static final int MIN_FLASH_DURATION_IN_TICKS = 100;
/*    */   private static final int MAX_FLASH_DURATION_IN_TICKS = 380;
/*    */   private long flashSeed;
/*    */   private int offset;
/*    */   private int duration;
/*    */   private float intensity;
/*    */   private float oldIntensity;
/*    */   private float xAngle;
/*    */   private float yAngle;
/*    */   
/*    */   public void tick(long gameTime) {
/* 23 */     calculateFlashParameters(gameTime);
/* 24 */     this.oldIntensity = this.intensity;
/* 25 */     this.intensity = calculateIntensity(gameTime);
/*    */   }
/*    */   
/*    */   private void calculateFlashParameters(long gameTime) {
/* 29 */     long newSeed = gameTime / 600L;
/* 30 */     if (newSeed != this.flashSeed) {
/* 31 */       RandomSource randomSource = RandomSource.create(newSeed);
/* 32 */       randomSource.nextFloat();
/* 33 */       this.offset = Mth.randomBetweenInclusive(randomSource, 0, 200);
/* 34 */       this.duration = Mth.randomBetweenInclusive(randomSource, 100, Math.min(380, 600 - this.offset));
/* 35 */       this.xAngle = Mth.randomBetween(randomSource, -60.0F, 10.0F);
/* 36 */       this.yAngle = Mth.randomBetween(randomSource, -180.0F, 180.0F);
/* 37 */       this.flashSeed = newSeed;
/*    */     } 
/*    */   }
/*    */   
/*    */   private float calculateIntensity(long gameTime) {
/* 42 */     long gameTimeWithinInterval = gameTime % 600L;
/* 43 */     if (gameTimeWithinInterval < this.offset || gameTimeWithinInterval > (this.offset + this.duration)) {
/* 44 */       return 0.0F;
/*    */     }
/* 46 */     return Mth.sin(((float)(gameTimeWithinInterval - this.offset) * 3.1415927F / this.duration));
/*    */   }
/*    */   
/*    */   public float getXAngle() {
/* 50 */     return this.xAngle;
/*    */   }
/*    */   
/*    */   public float getYAngle() {
/* 54 */     return this.yAngle;
/*    */   }
/*    */   
/*    */   public float getIntensity(float partialTicks) {
/* 58 */     return Mth.lerp(partialTicks, this.oldIntensity, this.intensity);
/*    */   }
/*    */   
/*    */   public boolean flashStartedThisTick() {
/* 62 */     return (this.intensity > 0.0F && this.oldIntensity <= 0.0F);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/EndFlashState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */