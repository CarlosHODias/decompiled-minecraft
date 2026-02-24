/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.AdultAndBabyModelPair;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.animal.chicken.ChickenModel;
/*    */ import net.minecraft.client.model.animal.chicken.ColdChickenModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.ChickenRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.chicken.Chicken;
/*    */ import net.minecraft.world.entity.animal.chicken.ChickenVariant;
/*    */ 
/*    */ public class ChickenRenderer extends MobRenderer<Chicken, ChickenRenderState, ChickenModel> {
/*    */   public ChickenRenderer(EntityRendererProvider.Context context) {
/* 24 */     super(context, new ChickenModel(context.bakeLayer(ModelLayers.CHICKEN)), 0.3F);
/* 25 */     this.models = bakeModels(context);
/*    */   }
/*    */   private final Map<ChickenVariant.ModelType, AdultAndBabyModelPair<ChickenModel>> models;
/*    */   private static Map<ChickenVariant.ModelType, AdultAndBabyModelPair<ChickenModel>> bakeModels(EntityRendererProvider.Context context) {
/* 29 */     return com.google.common.collect.Maps.newEnumMap(Map.of(ChickenVariant.ModelType.NORMAL, new AdultAndBabyModelPair((Model)new ChickenModel(
/* 30 */               context.bakeLayer(ModelLayers.CHICKEN)), (Model)new ChickenModel(context.bakeLayer(ModelLayers.CHICKEN_BABY))), ChickenVariant.ModelType.COLD, new AdultAndBabyModelPair((Model)new ColdChickenModel(
/* 31 */               context.bakeLayer(ModelLayers.COLD_CHICKEN)), (Model)new ColdChickenModel(context.bakeLayer(ModelLayers.COLD_CHICKEN_BABY)))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void submit(ChickenRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 37 */     if (state.variant == null) {
/*    */       return;
/*    */     }
/* 40 */     this.model = (ChickenModel)((AdultAndBabyModelPair)this.models.get(state.variant.modelAndTexture().model())).getModel(state.isBaby);
/* 41 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(ChickenRenderState state) {
/* 46 */     return (state.variant == null) ? MissingTextureAtlasSprite.getLocation() : state.variant.modelAndTexture().asset().texturePath();
/*    */   }
/*    */ 
/*    */   
/*    */   public ChickenRenderState createRenderState() {
/* 51 */     return new ChickenRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Chicken entity, ChickenRenderState state, float partialTicks) {
/* 56 */     super.extractRenderState(entity, state, partialTicks);
/* 57 */     state.flap = Mth.lerp(partialTicks, entity.oFlap, entity.flap);
/* 58 */     state.flapSpeed = Mth.lerp(partialTicks, entity.oFlapSpeed, entity.flapSpeed);
/* 59 */     state.variant = (ChickenVariant)entity.getVariant().value();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ChickenRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */