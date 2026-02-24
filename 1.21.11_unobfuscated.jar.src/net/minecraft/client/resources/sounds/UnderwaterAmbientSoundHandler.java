/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import net.minecraft.client.player.LocalPlayer;
/*    */ import net.minecraft.client.sounds.SoundManager;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ 
/*    */ public class UnderwaterAmbientSoundHandler
/*    */   implements AmbientSoundHandler {
/*    */   public static final float CHANCE_PER_TICK = 0.01F;
/*    */   public static final float RARE_CHANCE_PER_TICK = 0.001F;
/*    */   public static final float ULTRA_RARE_CHANCE_PER_TICK = 1.0E-4F;
/*    */   private static final int MINIMUM_TICK_DELAY = 0;
/*    */   private final LocalPlayer player;
/*    */   private final SoundManager soundManager;
/* 15 */   private int tickDelay = 0;
/*    */   
/*    */   public UnderwaterAmbientSoundHandler(LocalPlayer player, SoundManager soundManager) {
/* 18 */     this.player = player;
/* 19 */     this.soundManager = soundManager;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 24 */     this.tickDelay--;
/*    */     
/* 26 */     if (this.tickDelay <= 0 && this.player.isUnderWater()) {
/* 27 */       float rand = (this.player.level()).random.nextFloat();
/* 28 */       if (rand < 1.0E-4F) {
/* 29 */         this.tickDelay = 0;
/* 30 */         this.soundManager.play(new UnderwaterAmbientSoundInstances.SubSound(this.player, SoundEvents.AMBIENT_UNDERWATER_LOOP_ADDITIONS_ULTRA_RARE));
/* 31 */       } else if (rand < 0.001F) {
/* 32 */         this.tickDelay = 0;
/* 33 */         this.soundManager.play(new UnderwaterAmbientSoundInstances.SubSound(this.player, SoundEvents.AMBIENT_UNDERWATER_LOOP_ADDITIONS_RARE));
/* 34 */       } else if (rand < 0.01F) {
/* 35 */         this.tickDelay = 0;
/* 36 */         this.soundManager.play(new UnderwaterAmbientSoundInstances.SubSound(this.player, SoundEvents.AMBIENT_UNDERWATER_LOOP_ADDITIONS));
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/sounds/UnderwaterAmbientSoundHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */