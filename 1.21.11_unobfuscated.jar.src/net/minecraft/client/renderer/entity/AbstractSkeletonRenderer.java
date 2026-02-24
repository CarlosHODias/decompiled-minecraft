/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.HumanoidModel;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.monster.skeleton.SkeletonModel;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.monster.skeleton.AbstractSkeleton;
/*    */ 
/*    */ public abstract class AbstractSkeletonRenderer<T extends AbstractSkeleton, S extends SkeletonRenderState> extends HumanoidMobRenderer<T, S, SkeletonModel<S>> {
/*    */   public AbstractSkeletonRenderer(EntityRendererProvider.Context context, ModelLayerLocation body, ArmorModelSet<ModelLayerLocation> armorSet) {
/* 14 */     this(context, armorSet, new SkeletonModel(context.bakeLayer(body)));
/*    */   }
/*    */   
/*    */   public AbstractSkeletonRenderer(EntityRendererProvider.Context context, ArmorModelSet<ModelLayerLocation> armorSet, SkeletonModel<S> bodyModel) {
/* 18 */     super(context, bodyModel, 0.5F);
/*    */     
/* 20 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<S, SkeletonModel<S>>)new net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer(this, 
/* 21 */           ArmorModelSet.bake(armorSet, context.getModelSet(), SkeletonModel::new), 
/* 22 */           context.getEquipmentRenderer()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void extractRenderState(T entity, S state, float partialTicks) {
/* 28 */     super.extractRenderState(entity, state, partialTicks);
/* 29 */     ((SkeletonRenderState)state).isAggressive = entity.isAggressive();
/* 30 */     ((SkeletonRenderState)state).isShaking = entity.isShaking();
/* 31 */     ((SkeletonRenderState)state).isHoldingBow = entity.getMainHandItem().is(net.minecraft.world.item.Items.BOW);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isShaking(S state) {
/* 36 */     return ((SkeletonRenderState)state).isShaking;
/*    */   }
/*    */ 
/*    */   
/*    */   protected HumanoidModel.ArmPose getArmPose(T mob, HumanoidArm arm) {
/* 41 */     if (mob.getMainArm() == arm && mob.isAggressive() && mob.getMainHandItem().is(net.minecraft.world.item.Items.BOW)) {
/* 42 */       return HumanoidModel.ArmPose.BOW_AND_ARROW;
/*    */     }
/* 44 */     return super.getArmPose(mob, arm);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/AbstractSkeletonRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */