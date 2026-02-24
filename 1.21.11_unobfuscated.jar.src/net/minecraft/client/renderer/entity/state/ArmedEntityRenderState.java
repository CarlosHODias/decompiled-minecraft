/*    */ package net.minecraft.client.renderer.entity.state;
/*    */ 
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*    */ import net.minecraft.client.renderer.item.ItemStackRenderState;
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.SwingAnimationType;
/*    */ 
/*    */ 
/*    */ public class ArmedEntityRenderState
/*    */   extends LivingEntityRenderState
/*    */ {
/* 16 */   public HumanoidArm mainArm = HumanoidArm.RIGHT;
/* 17 */   public HumanoidModel.ArmPose rightArmPose = HumanoidModel.ArmPose.EMPTY;
/* 18 */   public final ItemStackRenderState rightHandItemState = new ItemStackRenderState();
/* 19 */   public ItemStack rightHandItemStack = ItemStack.EMPTY;
/*    */   
/* 21 */   public HumanoidModel.ArmPose leftArmPose = HumanoidModel.ArmPose.EMPTY;
/* 22 */   public final ItemStackRenderState leftHandItemState = new ItemStackRenderState();
/* 23 */   public ItemStack leftHandItemStack = ItemStack.EMPTY;
/*    */   
/* 25 */   public SwingAnimationType swingAnimationType = SwingAnimationType.WHACK;
/*    */   public float attackTime;
/*    */   
/*    */   public ItemStackRenderState getMainHandItemState() {
/* 29 */     return (this.mainArm == HumanoidArm.RIGHT) ? this.rightHandItemState : this.leftHandItemState;
/*    */   }
/*    */   
/*    */   public ItemStack getMainHandItemStack() {
/* 33 */     return (this.mainArm == HumanoidArm.RIGHT) ? this.rightHandItemStack : this.leftHandItemStack;
/*    */   }
/*    */   
/*    */   public ItemStack getUseItemStackForArm(HumanoidArm arm) {
/* 37 */     return (arm == HumanoidArm.RIGHT) ? this.rightHandItemStack : this.leftHandItemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public float ticksUsingItem(HumanoidArm arm) {
/* 42 */     return 0.0F;
/*    */   }
/*    */   
/*    */   public static void extractArmedEntityRenderState(LivingEntity entity, ArmedEntityRenderState state, ItemModelResolver itemModelResolver, float partialTicks) {
/* 46 */     state.mainArm = entity.getMainArm();
/*    */     
/* 48 */     ItemStack itemStack = entity.getMainHandItem();
/* 49 */     state.swingAnimationType = itemStack.getSwingAnimation().type();
/*    */     
/* 51 */     state.attackTime = entity.getAttackAnim(partialTicks);
/*    */     
/* 53 */     itemModelResolver.updateForLiving(state.rightHandItemState, entity.getItemHeldByArm(HumanoidArm.RIGHT), ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, entity);
/* 54 */     itemModelResolver.updateForLiving(state.leftHandItemState, entity.getItemHeldByArm(HumanoidArm.LEFT), ItemDisplayContext.THIRD_PERSON_LEFT_HAND, entity);
/* 55 */     state.leftHandItemStack = entity.getItemHeldByArm(HumanoidArm.LEFT).copy();
/* 56 */     state.rightHandItemStack = entity.getItemHeldByArm(HumanoidArm.RIGHT).copy();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/ArmedEntityRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */