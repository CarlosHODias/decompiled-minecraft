/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.monster.illager.IllagerModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.IllagerRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.illager.AbstractIllager;
/*    */ 
/*    */ public abstract class IllagerRenderer<T extends AbstractIllager, S extends IllagerRenderState> extends MobRenderer<T, S, IllagerModel<S>> {
/*    */   protected IllagerRenderer(EntityRendererProvider.Context context, IllagerModel<S> model, float shadow) {
/* 12 */     super(context, model, shadow);
/* 13 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<S, IllagerModel<S>>)new net.minecraft.client.renderer.entity.layers.CustomHeadLayer(this, context.getModelSet(), context.getPlayerSkinRenderCache()));
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(T entity, S state, float partialTicks) {
/* 18 */     super.extractRenderState(entity, state, partialTicks);
/* 19 */     net.minecraft.client.renderer.entity.state.ArmedEntityRenderState.extractArmedEntityRenderState((LivingEntity)entity, (net.minecraft.client.renderer.entity.state.ArmedEntityRenderState)state, this.itemModelResolver, partialTicks);
/* 20 */     ((IllagerRenderState)state).isRiding = entity.isPassenger();
/* 21 */     ((IllagerRenderState)state).mainArm = entity.getMainArm();
/* 22 */     ((IllagerRenderState)state).armPose = entity.getArmPose();
/* 23 */     ((IllagerRenderState)state).maxCrossbowChargeDuration = (((IllagerRenderState)state).armPose == AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE) ? net.minecraft.world.item.CrossbowItem.getChargeDuration(entity.getUseItem(), (LivingEntity)entity) : 0;
/* 24 */     ((IllagerRenderState)state).ticksUsingItem = entity.getTicksUsingItem(partialTicks);
/* 25 */     ((IllagerRenderState)state).attackAnim = entity.getAttackAnim(partialTicks);
/* 26 */     ((IllagerRenderState)state).isAggressive = entity.isAggressive();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/IllagerRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */