/*    */ package net.minecraft.client.sounds;
/*    */ 
/*    */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*    */ import net.minecraft.client.resources.sounds.SoundInstance;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ 
/*    */ public final class SoundPreviewHandler
/*    */ {
/*    */   private static SoundInstance activePreview;
/*    */   private static SoundSource previousCategory;
/*    */   
/*    */   public static void preview(SoundManager soundManager, SoundSource category, float volume) {
/* 15 */     stopOtherCategoryPreview(soundManager, category);
/* 16 */     if (canPlaySound(soundManager)) {
/* 17 */       switch (category) { case RECORDS: 
/*    */         case WEATHER: 
/*    */         case BLOCKS: 
/*    */         case HOSTILE: 
/*    */         case NEUTRAL: 
/*    */         case PLAYERS: 
/*    */         case AMBIENT: 
/*    */         case UI: 
/*    */         default:
/* 26 */           break; }  SoundEvent previewSound = SoundEvents.EMPTY;
/*    */       
/* 28 */       if (previewSound != SoundEvents.EMPTY) {
/* 29 */         activePreview = (SoundInstance)SimpleSoundInstance.forUI(previewSound, 1.0F, volume);
/* 30 */         soundManager.play(activePreview);
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private static void stopOtherCategoryPreview(SoundManager soundManager, SoundSource category) {
/* 36 */     if (previousCategory != category) {
/* 37 */       previousCategory = category;
/* 38 */       if (activePreview != null) {
/* 39 */         soundManager.stop(activePreview);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   private static boolean canPlaySound(SoundManager soundManager) {
/* 45 */     return (activePreview == null || !soundManager.isActive(activePreview));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/sounds/SoundPreviewHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */