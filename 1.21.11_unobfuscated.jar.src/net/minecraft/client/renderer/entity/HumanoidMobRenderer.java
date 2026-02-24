/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.WingsLayer;
/*    */ import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*    */ import net.minecraft.tags.ItemTags;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.item.CrossbowItem;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.SwingAnimationType;
/*    */ import net.minecraft.world.item.component.SwingAnimation;
/*    */ 
/*    */ public abstract class HumanoidMobRenderer<T extends Mob, S extends HumanoidRenderState, M extends HumanoidModel<S>> extends AgeableMobRenderer<T, S, M> {
/*    */   public HumanoidMobRenderer(EntityRendererProvider.Context context, M model, float shadow) {
/* 25 */     this(context, model, model, shadow);
/*    */   }
/*    */   
/*    */   public HumanoidMobRenderer(EntityRendererProvider.Context context, M model, M babyModel, float shadow) {
/* 29 */     this(context, model, babyModel, shadow, CustomHeadLayer.Transforms.DEFAULT);
/*    */   }
/*    */   
/*    */   public HumanoidMobRenderer(EntityRendererProvider.Context context, M model, M babyModel, float shadow, CustomHeadLayer.Transforms customHeadTransforms) {
/* 33 */     super(context, model, babyModel, shadow);
/*    */     
/* 35 */     addLayer((RenderLayer<S, M>)new CustomHeadLayer(this, context.getModelSet(), context.getPlayerSkinRenderCache(), customHeadTransforms));
/* 36 */     addLayer((RenderLayer<S, M>)new WingsLayer(this, context.getModelSet(), context.getEquipmentRenderer()));
/* 37 */     addLayer((RenderLayer<S, M>)new net.minecraft.client.renderer.entity.layers.ItemInHandLayer(this));
/*    */   }
/*    */   
/*    */   protected HumanoidModel.ArmPose getArmPose(T mob, HumanoidArm arm) {
/* 41 */     ItemStack itemHeldByArm = mob.getItemHeldByArm(arm);
/* 42 */     SwingAnimation anim = (SwingAnimation)itemHeldByArm.get(net.minecraft.core.component.DataComponents.SWING_ANIMATION);
/* 43 */     if (anim != null && anim.type() == SwingAnimationType.STAB && ((Mob)mob).swinging) {
/* 44 */       return HumanoidModel.ArmPose.SPEAR;
/*    */     }
/* 46 */     if (itemHeldByArm.is(ItemTags.SPEARS)) {
/* 47 */       return HumanoidModel.ArmPose.SPEAR;
/*    */     }
/* 49 */     return HumanoidModel.ArmPose.EMPTY;
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(T entity, S state, float partialTicks) {
/* 54 */     super.extractRenderState(entity, state, partialTicks);
/* 55 */     extractHumanoidRenderState((LivingEntity)entity, (HumanoidRenderState)state, partialTicks, this.itemModelResolver);
/* 56 */     ((HumanoidRenderState)state).leftArmPose = getArmPose(entity, HumanoidArm.LEFT);
/* 57 */     ((HumanoidRenderState)state).rightArmPose = getArmPose(entity, HumanoidArm.RIGHT);
/*    */   }
/*    */   
/*    */   public static void extractHumanoidRenderState(LivingEntity entity, HumanoidRenderState state, float partialTicks, ItemModelResolver itemModelResolver) {
/* 61 */     ArmedEntityRenderState.extractArmedEntityRenderState(entity, (ArmedEntityRenderState)state, itemModelResolver, partialTicks);
/*    */     
/* 63 */     state.isCrouching = entity.isCrouching();
/* 64 */     state.isFallFlying = entity.isFallFlying();
/* 65 */     state.isVisuallySwimming = entity.isVisuallySwimming();
/* 66 */     state.isPassenger = entity.isPassenger();
/* 67 */     state.speedValue = 1.0F;
/* 68 */     if (state.isFallFlying) {
/* 69 */       state.speedValue = (float)entity.getDeltaMovement().lengthSqr();
/* 70 */       state.speedValue /= 0.2F;
/* 71 */       state.speedValue *= state.speedValue * state.speedValue;
/*    */     } 
/* 73 */     if (state.speedValue < 1.0F) {
/* 74 */       state.speedValue = 1.0F;
/*    */     }
/* 76 */     state.swimAmount = entity.getSwimAmount(partialTicks);
/* 77 */     state.attackArm = getAttackArm(entity);
/* 78 */     state.useItemHand = entity.getUsedItemHand();
/* 79 */     state.maxCrossbowChargeDuration = CrossbowItem.getChargeDuration(entity.getUseItem(), entity);
/* 80 */     state.ticksUsingItem = entity.getTicksUsingItem(partialTicks);
/* 81 */     state.isUsingItem = entity.isUsingItem();
/* 82 */     state.elytraRotX = entity.elytraAnimationState.getRotX(partialTicks);
/* 83 */     state.elytraRotY = entity.elytraAnimationState.getRotY(partialTicks);
/* 84 */     state.elytraRotZ = entity.elytraAnimationState.getRotZ(partialTicks);
/* 85 */     state.headEquipment = getEquipmentIfRenderable(entity, EquipmentSlot.HEAD);
/* 86 */     state.chestEquipment = getEquipmentIfRenderable(entity, EquipmentSlot.CHEST);
/* 87 */     state.legsEquipment = getEquipmentIfRenderable(entity, EquipmentSlot.LEGS);
/* 88 */     state.feetEquipment = getEquipmentIfRenderable(entity, EquipmentSlot.FEET);
/*    */   }
/*    */   
/*    */   private static ItemStack getEquipmentIfRenderable(LivingEntity entity, EquipmentSlot slot) {
/* 92 */     ItemStack itemStack = entity.getItemBySlot(slot);
/* 93 */     return net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer.shouldRender(itemStack, slot) ? itemStack.copy() : ItemStack.EMPTY;
/*    */   }
/*    */   
/*    */   private static HumanoidArm getAttackArm(LivingEntity entity) {
/* 97 */     HumanoidArm mainArm = entity.getMainArm();
/* 98 */     return (entity.swingingArm == InteractionHand.MAIN_HAND) ? mainArm : mainArm.getOpposite();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/HumanoidMobRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */