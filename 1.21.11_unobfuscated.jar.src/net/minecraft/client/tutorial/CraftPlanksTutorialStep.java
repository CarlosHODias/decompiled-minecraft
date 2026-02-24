/*    */ package net.minecraft.client.tutorial;
/*    */ 
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.components.toasts.Toast;
/*    */ import net.minecraft.client.gui.components.toasts.TutorialToast;
/*    */ import net.minecraft.client.player.LocalPlayer;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.tags.ItemTags;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class CraftPlanksTutorialStep
/*    */   implements TutorialStepInstance
/*    */ {
/*    */   private static final int HINT_DELAY = 1200;
/* 20 */   private static final Component CRAFT_TITLE = (Component)Component.translatable("tutorial.craft_planks.title");
/* 21 */   private static final Component CRAFT_DESCRIPTION = (Component)Component.translatable("tutorial.craft_planks.description");
/*    */   
/*    */   private final Tutorial tutorial;
/*    */   private TutorialToast toast;
/*    */   private int timeWaiting;
/*    */   
/*    */   public CraftPlanksTutorialStep(Tutorial tutorial) {
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
/* 44 */         if (player.getInventory().contains(ItemTags.PLANKS)) {
/* 45 */           this.tutorial.setStep(TutorialSteps.NONE);
/*    */           return;
/*    */         } 
/* 48 */         if (hasCraftedPlanksPreviously(player, ItemTags.PLANKS)) {
/* 49 */           this.tutorial.setStep(TutorialSteps.NONE);
/*    */           
/*    */           return;
/*    */         } 
/*    */       } 
/*    */     } 
/* 55 */     if (this.timeWaiting >= 1200 && 
/* 56 */       this.toast == null) {
/* 57 */       this.toast = new TutorialToast(minecraft.font, TutorialToast.Icons.WOODEN_PLANKS, CRAFT_TITLE, CRAFT_DESCRIPTION, false);
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
/*    */   public void onGetItem(ItemStack itemStack) {
/* 73 */     if (itemStack.is(ItemTags.PLANKS)) {
/* 74 */       this.tutorial.setStep(TutorialSteps.NONE);
/*    */     }
/*    */   }
/*    */   
/*    */   public static boolean hasCraftedPlanksPreviously(LocalPlayer player, TagKey<Item> tag) {
/* 79 */     for (Holder<Item> item : (Iterable<Holder<Item>>)BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
/* 80 */       if (player.getStats().getValue(Stats.ITEM_CRAFTED.get(item.value())) > 0) {
/* 81 */         return true;
/*    */       }
/*    */     } 
/* 84 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/tutorial/CraftPlanksTutorialStep.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */