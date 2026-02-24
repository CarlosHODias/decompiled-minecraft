/*    */ package net.minecraft.client.tutorial;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.components.toasts.Toast;
/*    */ import net.minecraft.client.gui.components.toasts.TutorialToast;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.player.LocalPlayer;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.ItemTags;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class PunchTreeTutorialStepInstance
/*    */   implements TutorialStepInstance
/*    */ {
/*    */   private static final int HINT_DELAY = 600;
/* 19 */   private static final Component TITLE = (Component)Component.translatable("tutorial.punch_tree.title");
/* 20 */   private static final Component DESCRIPTION = (Component)Component.translatable("tutorial.punch_tree.description", new Object[] { Tutorial.key("attack") });
/*    */   
/*    */   private final Tutorial tutorial;
/*    */   private TutorialToast toast;
/*    */   private int timeWaiting;
/*    */   private int resetCount;
/*    */   
/*    */   public PunchTreeTutorialStepInstance(Tutorial tutorial) {
/* 28 */     this.tutorial = tutorial;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 33 */     this.timeWaiting++;
/*    */     
/* 35 */     if (!this.tutorial.isSurvival()) {
/* 36 */       this.tutorial.setStep(TutorialSteps.NONE);
/*    */       
/*    */       return;
/*    */     } 
/* 40 */     Minecraft minecraft = this.tutorial.getMinecraft();
/* 41 */     if (this.timeWaiting == 1) {
/* 42 */       LocalPlayer player = minecraft.player;
/* 43 */       if (player != null) {
/* 44 */         if (player.getInventory().contains(ItemTags.LOGS)) {
/* 45 */           this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
/*    */           return;
/*    */         } 
/* 48 */         if (FindTreeTutorialStepInstance.hasPunchedTreesPreviously(player)) {
/* 49 */           this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
/*    */           
/*    */           return;
/*    */         } 
/*    */       } 
/*    */     } 
/* 55 */     if ((this.timeWaiting >= 600 || this.resetCount > 3) && 
/* 56 */       this.toast == null) {
/* 57 */       this.toast = new TutorialToast(minecraft.font, TutorialToast.Icons.TREE, TITLE, DESCRIPTION, true);
/* 58 */       minecraft.getToastManager().addToast((Toast)this.toast);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void clear() {
/* 65 */     if (this.toast != null) {
/* 66 */       this.toast.hide();
/* 67 */       this.toast = null;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onDestroyBlock(ClientLevel level, BlockPos pos, BlockState state, float percent) {
/* 73 */     boolean isLogBlock = state.is(BlockTags.LOGS);
/* 74 */     if (isLogBlock && percent > 0.0F) {
/* 75 */       if (this.toast != null) {
/* 76 */         this.toast.updateProgress(percent);
/*    */       }
/* 78 */       if (percent >= 1.0F) {
/* 79 */         this.tutorial.setStep(TutorialSteps.OPEN_INVENTORY);
/*    */       }
/* 81 */     } else if (this.toast != null) {
/* 82 */       this.toast.updateProgress(0.0F);
/* 83 */     } else if (isLogBlock) {
/* 84 */       this.resetCount++;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onGetItem(ItemStack itemStack) {
/* 90 */     if (itemStack.is(ItemTags.LOGS)) {
/* 91 */       this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
/*    */       return;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/tutorial/PunchTreeTutorialStepInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */