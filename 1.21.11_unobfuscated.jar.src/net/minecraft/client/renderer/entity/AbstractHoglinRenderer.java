/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.monster.hoglin.HoglinModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HoglinRenderState;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public abstract class AbstractHoglinRenderer<T extends Mob & net.minecraft.world.entity.monster.hoglin.HoglinBase> extends AgeableMobRenderer<T, HoglinRenderState, HoglinModel> {
/*    */   public AbstractHoglinRenderer(EntityRendererProvider.Context context, ModelLayerLocation adultLayer, ModelLayerLocation babyLayer, float shadow) {
/* 11 */     super(context, new HoglinModel(context.bakeLayer(adultLayer)), new HoglinModel(context.bakeLayer(babyLayer)), shadow);
/*    */   }
/*    */ 
/*    */   
/*    */   public HoglinRenderState createRenderState() {
/* 16 */     return new HoglinRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(T entity, HoglinRenderState state, float partialTicks) {
/* 21 */     super.extractRenderState(entity, state, partialTicks);
/* 22 */     state.attackAnimationRemainingTicks = ((net.minecraft.world.entity.monster.hoglin.HoglinBase)entity).getAttackAnimationRemainingTicks();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/AbstractHoglinRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */