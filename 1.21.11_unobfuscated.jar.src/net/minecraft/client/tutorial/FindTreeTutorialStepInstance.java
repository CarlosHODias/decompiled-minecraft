/*    */ package net.minecraft.client.tutorial;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.components.toasts.Toast;
/*    */ import net.minecraft.client.gui.components.toasts.TutorialToast;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.player.LocalPlayer;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.tags.ItemTags;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.phys.BlockHitResult;
/*    */ import net.minecraft.world.phys.HitResult;
/*    */ 
/*    */ public class FindTreeTutorialStepInstance
/*    */   implements TutorialStepInstance
/*    */ {
/*    */   private static final int HINT_DELAY = 6000;
/* 24 */   private static final Component TITLE = (Component)Component.translatable("tutorial.find_tree.title");
/* 25 */   private static final Component DESCRIPTION = (Component)Component.translatable("tutorial.find_tree.description");
/*    */   
/*    */   private final Tutorial tutorial;
/*    */   private TutorialToast toast;
/*    */   private int timeWaiting;
/*    */   
/*    */   public FindTreeTutorialStepInstance(Tutorial tutorial) {
/* 32 */     this.tutorial = tutorial;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 37 */     this.timeWaiting++;
/*    */     
/* 39 */     if (!this.tutorial.isSurvival()) {
/* 40 */       this.tutorial.setStep(TutorialSteps.NONE);
/*    */       
/*    */       return;
/*    */     } 
/* 44 */     Minecraft minecraft = this.tutorial.getMinecraft();
/* 45 */     if (this.timeWaiting == 1) {
/* 46 */       LocalPlayer player = minecraft.player;
/* 47 */       if (player != null && (
/* 48 */         hasCollectedTreeItems(player) || hasPunchedTreesPreviously(player))) {
/* 49 */         this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
/*    */         
/*    */         return;
/*    */       } 
/*    */     } 
/*    */     
/* 55 */     if (this.timeWaiting >= 6000 && 
/* 56 */       this.toast == null) {
/* 57 */       this.toast = new TutorialToast(minecraft.font, TutorialToast.Icons.TREE, TITLE, DESCRIPTION, false);
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
/*    */   public void onLookAt(ClientLevel level, HitResult hit) {
/* 73 */     if (hit.getType() == HitResult.Type.BLOCK) {
/* 74 */       BlockState state = level.getBlockState(((BlockHitResult)hit).getBlockPos());
/* 75 */       if (state.is(BlockTags.COMPLETES_FIND_TREE_TUTORIAL)) {
/* 76 */         this.tutorial.setStep(TutorialSteps.PUNCH_TREE);
/*    */       }
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onGetItem(ItemStack itemStack) {
/* 83 */     if (itemStack.is(ItemTags.COMPLETES_FIND_TREE_TUTORIAL)) {
/* 84 */       this.tutorial.setStep(TutorialSteps.CRAFT_PLANKS);
/*    */     }
/*    */   }
/*    */   
/*    */   private static boolean hasCollectedTreeItems(LocalPlayer player) {
/* 89 */     return player.getInventory().hasAnyMatching(item -> item.is(ItemTags.COMPLETES_FIND_TREE_TUTORIAL));
/*    */   }
/*    */   
/*    */   public static boolean hasPunchedTreesPreviously(LocalPlayer player) {
/* 93 */     for (Holder<Block> holder : (Iterable<Holder<Block>>)BuiltInRegistries.BLOCK.getTagOrEmpty(BlockTags.COMPLETES_FIND_TREE_TUTORIAL)) {
/* 94 */       Block block = (Block)holder.value();
/* 95 */       if (player.getStats().getValue(Stats.BLOCK_MINED.get(block)) > 0) {
/* 96 */         return true;
/*    */       }
/*    */     } 
/* 99 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/tutorial/FindTreeTutorialStepInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */