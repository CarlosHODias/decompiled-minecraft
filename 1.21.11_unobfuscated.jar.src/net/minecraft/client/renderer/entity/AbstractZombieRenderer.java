/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombieRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*    */ import net.minecraft.world.item.component.SwingAnimation;
/*    */ 
/*    */ public abstract class AbstractZombieRenderer<T extends Zombie, S extends ZombieRenderState, M extends net.minecraft.client.model.monster.zombie.ZombieModel<S>> extends HumanoidMobRenderer<T, S, M> {
/* 15 */   private static final Identifier ZOMBIE_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/zombie.png");
/*    */   
/*    */   protected AbstractZombieRenderer(EntityRendererProvider.Context context, M model, M babyModel, ArmorModelSet<M> armorSet, ArmorModelSet<M> babyArmorSet) {
/* 18 */     super(context, model, babyModel, 0.5F);
/*    */     
/* 20 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<S, M>)new net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer(this, armorSet, babyArmorSet, context.getEquipmentRenderer()));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(S state) {
/* 25 */     return ZOMBIE_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(T entity, S state, float partialTicks) {
/* 30 */     super.extractRenderState(entity, state, partialTicks);
/* 31 */     ((ZombieRenderState)state).isAggressive = entity.isAggressive();
/* 32 */     ((ZombieRenderState)state).isConverting = entity.isUnderWaterConverting();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isShaking(S state) {
/* 37 */     return (super.isShaking(state) || ((ZombieRenderState)state).isConverting);
/*    */   }
/*    */ 
/*    */   
/*    */   protected HumanoidModel.ArmPose getArmPose(T mob, HumanoidArm arm) {
/* 42 */     SwingAnimation otherAnim = (SwingAnimation)mob.getItemHeldByArm(arm.getOpposite()).get(net.minecraft.core.component.DataComponents.SWING_ANIMATION);
/* 43 */     if (otherAnim != null && otherAnim.type() == net.minecraft.world.item.SwingAnimationType.STAB)
/*    */     {
/* 45 */       return HumanoidModel.ArmPose.SPEAR;
/*    */     }
/* 47 */     return super.getArmPose(mob, arm);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/AbstractZombieRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */