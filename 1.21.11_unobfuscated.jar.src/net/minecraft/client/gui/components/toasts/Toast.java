/*    */ package net.minecraft.client.gui.components.toasts;
/*    */ 
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.client.gui.GuiGraphics;
/*    */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*    */ import net.minecraft.client.resources.sounds.SoundInstance;
/*    */ import net.minecraft.client.sounds.SoundManager;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public interface Toast {
/* 13 */   public static final Object NO_TOKEN = new Object();
/*    */   
/*    */   public static final int DEFAULT_WIDTH = 160;
/*    */   public static final int SLOT_HEIGHT = 32;
/*    */   
/*    */   Visibility getWantedVisibility();
/*    */   
/*    */   void update(ToastManager paramToastManager, long paramLong);
/*    */   
/*    */   default SoundEvent getSoundEvent() {
/* 23 */     return null;
/*    */   }
/*    */   
/*    */   void render(GuiGraphics paramGuiGraphics, Font paramFont, long paramLong);
/*    */   
/*    */   default Object getToken() {
/* 29 */     return NO_TOKEN;
/*    */   }
/*    */   
/*    */   default float xPos(int screenWidth, float visiblePortion) {
/* 33 */     return screenWidth - width() * visiblePortion;
/*    */   }
/*    */   
/*    */   default float yPos(int firstSlotIndex) {
/* 37 */     return (firstSlotIndex * height());
/*    */   }
/*    */   
/*    */   default int width() {
/* 41 */     return 160;
/*    */   }
/*    */   
/*    */   default int height() {
/* 45 */     return 32;
/*    */   }
/*    */   
/*    */   default int occcupiedSlotCount() {
/* 49 */     return Mth.positiveCeilDiv(height(), 32);
/*    */   }
/*    */   
/*    */   default void onFinishedRendering() {}
/*    */   
/*    */   public enum Visibility
/*    */   {
/* 56 */     SHOW(SoundEvents.UI_TOAST_IN),
/* 57 */     HIDE(SoundEvents.UI_TOAST_OUT);
/*    */     
/*    */     private final SoundEvent soundEvent;
/*    */ 
/*    */     
/*    */     Visibility(SoundEvent soundEvent) {
/* 63 */       this.soundEvent = soundEvent;
/*    */     }
/*    */     
/*    */     public void playSound(SoundManager manager) {
/* 67 */       manager.play((SoundInstance)SimpleSoundInstance.forUI(this.soundEvent, 1.0F, 1.0F));
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/toasts/Toast.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */