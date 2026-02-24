/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.creaking.CreakingModel;
/*    */ import net.minecraft.client.renderer.entity.state.CreakingRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.creaking.Creaking;
/*    */ 
/*    */ public class CreakingRenderer<T extends Creaking> extends MobRenderer<T, CreakingRenderState, CreakingModel> {
/* 12 */   private static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/creaking/creaking.png");
/* 13 */   private static final Identifier EYES_TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/creaking/creaking_eyes.png");
/*    */   
/*    */   public CreakingRenderer(EntityRendererProvider.Context context) {
/* 16 */     super(context, new CreakingModel(context.bakeLayer(ModelLayers.CREAKING)), 0.6F);
/* 17 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<CreakingRenderState, CreakingModel>)new net.minecraft.client.renderer.entity.layers.LivingEntityEmissiveLayer(this, renderState -> EYES_TEXTURE_LOCATION, (state, ageInTicks) -> state.eyesGlowing ? 1.0F : 0.0F, (net.minecraft.client.model.EntityModel)new CreakingModel(
/*    */ 
/*    */ 
/*    */             
/* 21 */             context.bakeLayer(ModelLayers.CREAKING_EYES)), net.minecraft.client.renderer.rendertype.RenderTypes::eyes, true));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(CreakingRenderState state) {
/* 29 */     return TEXTURE_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public CreakingRenderState createRenderState() {
/* 34 */     return new CreakingRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(T entity, CreakingRenderState state, float partialTicks) {
/* 39 */     super.extractRenderState(entity, state, partialTicks);
/* 40 */     state.attackAnimationState.copyFrom(((Creaking)entity).attackAnimationState);
/* 41 */     state.invulnerabilityAnimationState.copyFrom(((Creaking)entity).invulnerabilityAnimationState);
/* 42 */     state.deathAnimationState.copyFrom(((Creaking)entity).deathAnimationState);
/* 43 */     if (entity.isTearingDown()) {
/*    */       
/* 45 */       state.deathTime = 0.0F;
/* 46 */       state.hasRedOverlay = false;
/*    */       
/* 48 */       state.eyesGlowing = entity.hasGlowingEyes();
/*    */     } else {
/* 50 */       state.eyesGlowing = entity.isActive();
/*    */     } 
/* 52 */     state.canMove = entity.canMove();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/CreakingRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */