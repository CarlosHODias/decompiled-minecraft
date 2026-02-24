/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class UseItemGoal<T extends Mob>
/*    */   extends Goal
/*    */ {
/*    */   private final T mob;
/*    */   private final ItemStack item;
/*    */   private final Predicate<? super T> canUseSelector;
/*    */   private final SoundEvent finishUsingSound;
/*    */   
/*    */   public UseItemGoal(T mob, ItemStack item, SoundEvent finishUsingSound, Predicate<? super T> canUseSelector) {
/* 19 */     this.mob = mob;
/* 20 */     this.item = item;
/* 21 */     this.finishUsingSound = finishUsingSound;
/* 22 */     this.canUseSelector = canUseSelector;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 27 */     return this.canUseSelector.test(this.mob);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 32 */     return this.mob.isUsingItem();
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 37 */     this.mob.setItemSlot(EquipmentSlot.MAINHAND, this.item.copy());
/* 38 */     this.mob.startUsingItem(InteractionHand.MAIN_HAND);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 43 */     this.mob.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
/*    */     
/* 45 */     if (this.finishUsingSound != null)
/* 46 */       this.mob.playSound(this.finishUsingSound, 1.0F, this.mob.getRandom().nextFloat() * 0.2F + 0.9F); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/goal/UseItemGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */