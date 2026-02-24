/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class HumanoidRenderState extends ArmedEntityRenderState {
/*    */   public float swimAmount;
/*  9 */   public float speedValue = 1.0F;
/*    */   public float maxCrossbowChargeDuration;
/*    */   public float ticksUsingItem;
/* 12 */   public HumanoidArm attackArm = HumanoidArm.RIGHT;
/* 13 */   public InteractionHand useItemHand = InteractionHand.MAIN_HAND;
/*    */   public boolean isCrouching;
/*    */   public boolean isFallFlying;
/*    */   public boolean isVisuallySwimming;
/*    */   public boolean isPassenger;
/*    */   public boolean isUsingItem;
/*    */   public float elytraRotX;
/*    */   public float elytraRotY;
/*    */   public float elytraRotZ;
/* 22 */   public ItemStack headEquipment = ItemStack.EMPTY;
/* 23 */   public ItemStack chestEquipment = ItemStack.EMPTY;
/* 24 */   public ItemStack legsEquipment = ItemStack.EMPTY;
/* 25 */   public ItemStack feetEquipment = ItemStack.EMPTY;
/*    */ 
/*    */ 
/*    */   
/*    */   public float ticksUsingItem(HumanoidArm arm) {
/* 30 */     if (this.isUsingItem && ((this.useItemHand == InteractionHand.MAIN_HAND) ? true : false) == ((arm == this.mainArm) ? true : false)) {
/* 31 */       return this.ticksUsingItem;
/*    */     }
/* 33 */     return 0.0F;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/HumanoidRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */