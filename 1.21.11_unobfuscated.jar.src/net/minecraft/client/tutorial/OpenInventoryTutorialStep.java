/*    */ package net.minecraft.client.tutorial;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.components.toasts.Toast;
/*    */ import net.minecraft.client.gui.components.toasts.TutorialToast;
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class OpenInventoryTutorialStep
/*    */   implements TutorialStepInstance
/*    */ {
/*    */   private static final int HINT_DELAY = 600;
/* 12 */   private static final Component TITLE = (Component)Component.translatable("tutorial.open_inventory.title");
/* 13 */   private static final Component DESCRIPTION = (Component)Component.translatable("tutorial.open_inventory.description", new Object[] { Tutorial.key("inventory") });
/*    */   
/*    */   private final Tutorial tutorial;
/*    */   private TutorialToast toast;
/*    */   private int timeWaiting;
/*    */   
/*    */   public OpenInventoryTutorialStep(Tutorial tutorial) {
/* 20 */     this.tutorial = tutorial;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 25 */     this.timeWaiting++;
/*    */     
/* 27 */     if (!this.tutorial.isSurvival()) {
/* 28 */       this.tutorial.setStep(TutorialSteps.NONE);
/*    */       
/*    */       return;
/*    */     } 
/* 32 */     if (this.timeWaiting >= 600 && 
/* 33 */       this.toast == null) {
/* 34 */       Minecraft minecraft = this.tutorial.getMinecraft();
/* 35 */       this.toast = new TutorialToast(minecraft.font, TutorialToast.Icons.RECIPE_BOOK, TITLE, DESCRIPTION, false);
/* 36 */       minecraft.getToastManager().addToast((Toast)this.toast);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void clear() {
/* 43 */     if (this.toast != null) {
/* 44 */       this.toast.hide();
/* 45 */       this.toast = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onOpenInventory() {
/* 51 */     this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/tutorial/OpenInventoryTutorialStep.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */