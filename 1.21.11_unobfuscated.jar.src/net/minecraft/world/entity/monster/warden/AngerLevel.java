/*    */ package net.minecraft.world.entity.monster.warden;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public enum AngerLevel
/*    */ {
/* 10 */   CALM(0, SoundEvents.WARDEN_AMBIENT, SoundEvents.WARDEN_LISTENING),
/* 11 */   AGITATED(40, SoundEvents.WARDEN_AGITATED, SoundEvents.WARDEN_LISTENING_ANGRY),
/* 12 */   ANGRY(80, SoundEvents.WARDEN_ANGRY, SoundEvents.WARDEN_LISTENING_ANGRY);
/*    */   
/*    */   static {
/* 15 */     SORTED_LEVELS = (AngerLevel[])Util.make(values(), values -> Arrays.sort(values, ()));
/*    */   }
/*    */   
/*    */   private static final AngerLevel[] SORTED_LEVELS;
/*    */   private final int minimumAnger;
/*    */   private final SoundEvent ambientSound;
/*    */   private final SoundEvent listeningSound;
/*    */   
/*    */   AngerLevel(int minimumAnger, SoundEvent ambientSound, SoundEvent listeningSound) {
/* 24 */     this.minimumAnger = minimumAnger;
/* 25 */     this.ambientSound = ambientSound;
/* 26 */     this.listeningSound = listeningSound;
/*    */   }
/*    */   
/*    */   public int getMinimumAnger() {
/* 30 */     return this.minimumAnger;
/*    */   }
/*    */   
/*    */   public SoundEvent getAmbientSound() {
/* 34 */     return this.ambientSound;
/*    */   }
/*    */   
/*    */   public SoundEvent getListeningSound() {
/* 38 */     return this.listeningSound;
/*    */   }
/*    */   
/*    */   public static AngerLevel byAnger(int anger) {
/* 42 */     for (AngerLevel level : SORTED_LEVELS) {
/* 43 */       if (anger >= level.minimumAnger) {
/* 44 */         return level;
/*    */       }
/*    */     } 
/* 47 */     return CALM;
/*    */   }
/*    */   
/*    */   public boolean isAngry() {
/* 51 */     return (this == ANGRY);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/monster/warden/AngerLevel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */