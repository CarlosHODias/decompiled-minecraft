/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.warden.WardenModel;
/*    */ import net.minecraft.client.renderer.entity.layers.LivingEntityEmissiveLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.WardenRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.monster.warden.Warden;
/*    */ 
/*    */ public class WardenRenderer extends MobRenderer<Warden, WardenRenderState, WardenModel> {
/* 13 */   private static final Identifier TEXTURE = Identifier.withDefaultNamespace("textures/entity/warden/warden.png");
/* 14 */   private static final Identifier BIOLUMINESCENT_LAYER_TEXTURE = Identifier.withDefaultNamespace("textures/entity/warden/warden_bioluminescent_layer.png");
/* 15 */   private static final Identifier HEART_TEXTURE = Identifier.withDefaultNamespace("textures/entity/warden/warden_heart.png");
/* 16 */   private static final Identifier PULSATING_SPOTS_TEXTURE_1 = Identifier.withDefaultNamespace("textures/entity/warden/warden_pulsating_spots_1.png");
/* 17 */   private static final Identifier PULSATING_SPOTS_TEXTURE_2 = Identifier.withDefaultNamespace("textures/entity/warden/warden_pulsating_spots_2.png");
/*    */   
/*    */   public WardenRenderer(EntityRendererProvider.Context context) {
/* 20 */     super(context, new WardenModel(context.bakeLayer(ModelLayers.WARDEN)), 0.9F);
/*    */     
/* 22 */     WardenModel bioluminescentModel = new WardenModel(context.bakeLayer(ModelLayers.WARDEN_BIOLUMINESCENT));
/* 23 */     WardenModel pulsatingSpotsModel = new WardenModel(context.bakeLayer(ModelLayers.WARDEN_PULSATING_SPOTS));
/* 24 */     WardenModel tendrilsModel = new WardenModel(context.bakeLayer(ModelLayers.WARDEN_TENDRILS));
/* 25 */     WardenModel heartModel = new WardenModel(context.bakeLayer(ModelLayers.WARDEN_HEART));
/*    */     
/* 27 */     addLayer((RenderLayer<WardenRenderState, WardenModel>)new LivingEntityEmissiveLayer(this, renderState -> BIOLUMINESCENT_LAYER_TEXTURE, (warden, ageInTicks) -> 1.0F, (EntityModel)bioluminescentModel, RenderTypes::entityTranslucentEmissive, false));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 34 */     addLayer((RenderLayer<WardenRenderState, WardenModel>)new LivingEntityEmissiveLayer(this, renderState -> PULSATING_SPOTS_TEXTURE_1, (warden, ageInTicks) -> Math.max(0.0F, net.minecraft.util.Mth.cos((ageInTicks * 0.045F)) * 0.25F), (EntityModel)pulsatingSpotsModel, RenderTypes::entityTranslucentEmissive, false));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 42 */     addLayer((RenderLayer<WardenRenderState, WardenModel>)new LivingEntityEmissiveLayer(this, renderState -> PULSATING_SPOTS_TEXTURE_2, (warden, ageInTicks) -> Math.max(0.0F, net.minecraft.util.Mth.cos((ageInTicks * 0.045F + 3.1415927F)) * 0.25F), (EntityModel)pulsatingSpotsModel, RenderTypes::entityTranslucentEmissive, false));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 50 */     addLayer((RenderLayer<WardenRenderState, WardenModel>)new LivingEntityEmissiveLayer(this, renderState -> TEXTURE, (warden, ageInTicks) -> warden.tendrilAnimation, (EntityModel)tendrilsModel, RenderTypes::entityTranslucentEmissive, false));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 58 */     addLayer((RenderLayer<WardenRenderState, WardenModel>)new LivingEntityEmissiveLayer(this, renderState -> HEART_TEXTURE, (warden, ageInTicks) -> warden.heartAnimation, (EntityModel)heartModel, RenderTypes::entityTranslucentEmissive, false));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(WardenRenderState state) {
/* 70 */     return TEXTURE;
/*    */   }
/*    */ 
/*    */   
/*    */   public WardenRenderState createRenderState() {
/* 75 */     return new WardenRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Warden entity, WardenRenderState state, float partialTicks) {
/* 80 */     super.extractRenderState(entity, state, partialTicks);
/* 81 */     state.tendrilAnimation = entity.getTendrilAnimation(partialTicks);
/* 82 */     state.heartAnimation = entity.getHeartAnimation(partialTicks);
/* 83 */     state.roarAnimationState.copyFrom(entity.roarAnimationState);
/* 84 */     state.sniffAnimationState.copyFrom(entity.sniffAnimationState);
/* 85 */     state.emergeAnimationState.copyFrom(entity.emergeAnimationState);
/* 86 */     state.diggingAnimationState.copyFrom(entity.diggingAnimationState);
/* 87 */     state.attackAnimationState.copyFrom(entity.attackAnimationState);
/* 88 */     state.sonicBoomAnimationState.copyFrom(entity.sonicBoomAnimationState);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/WardenRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */