/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EquineRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.equine.AbstractHorse;
/*    */ 
/*    */ public abstract class AbstractHorseRenderer<T extends AbstractHorse, S extends EquineRenderState, M extends net.minecraft.client.model.EntityModel<? super S>> extends AgeableMobRenderer<T, S, M> {
/*    */   public AbstractHorseRenderer(EntityRendererProvider.Context context, M model, M babyModel) {
/* 10 */     super(context, model, babyModel, 0.75F);
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(T entity, S state, float partialTicks) {
/* 15 */     super.extractRenderState(entity, state, partialTicks);
/* 16 */     ((EquineRenderState)state).saddle = entity.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.SADDLE).copy();
/* 17 */     ((EquineRenderState)state).bodyArmorItem = entity.getBodyArmorItem().copy();
/* 18 */     ((EquineRenderState)state).isRidden = entity.isVehicle();
/* 19 */     ((EquineRenderState)state).eatAnimation = entity.getEatAnim(partialTicks);
/* 20 */     ((EquineRenderState)state).standAnimation = entity.getStandAnim(partialTicks);
/* 21 */     ((EquineRenderState)state).feedingAnimation = entity.getMouthAnim(partialTicks);
/* 22 */     ((EquineRenderState)state).animateTail = (((AbstractHorse)entity).tailCounter > 0);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/AbstractHorseRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */