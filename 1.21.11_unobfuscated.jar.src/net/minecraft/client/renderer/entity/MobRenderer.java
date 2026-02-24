/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public abstract class MobRenderer<T extends Mob, S extends LivingEntityRenderState, M extends net.minecraft.client.model.EntityModel<? super S>> extends LivingEntityRenderer<T, S, M> {
/*    */   public MobRenderer(EntityRendererProvider.Context context, M model, float shadow) {
/* 11 */     super(context, model, shadow);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldShowName(T entity, double distanceToCameraSq) {
/* 16 */     return (super.shouldShowName(entity, distanceToCameraSq) && (entity.shouldShowName() || (entity.hasCustomName() && entity == this.entityRenderDispatcher.crosshairPickEntity)));
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getShadowRadius(S state) {
/* 21 */     return super.getShadowRadius(state) * ((LivingEntityRenderState)state).ageScale;
/*    */   }
/*    */   
/*    */   protected static boolean checkMagicName(Entity entity, String magicName) {
/* 25 */     Component customName = entity.getCustomName();
/* 26 */     return (customName != null && magicName.equals(customName.getString()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/MobRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */