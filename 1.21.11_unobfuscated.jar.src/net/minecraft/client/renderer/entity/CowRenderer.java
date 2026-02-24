/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.AdultAndBabyModelPair;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.animal.cow.CowModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.CowRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.cow.Cow;
/*    */ import net.minecraft.world.entity.animal.cow.CowVariant;
/*    */ 
/*    */ public class CowRenderer extends MobRenderer<Cow, CowRenderState, CowModel> {
/*    */   public CowRenderer(EntityRendererProvider.Context context) {
/* 22 */     super(context, new CowModel(context.bakeLayer(ModelLayers.COW)), 0.7F);
/* 23 */     this.models = bakeModels(context);
/*    */   }
/*    */   private final Map<CowVariant.ModelType, AdultAndBabyModelPair<CowModel>> models;
/*    */   private static Map<CowVariant.ModelType, AdultAndBabyModelPair<CowModel>> bakeModels(EntityRendererProvider.Context context) {
/* 27 */     return com.google.common.collect.Maps.newEnumMap(Map.of(CowVariant.ModelType.NORMAL, new AdultAndBabyModelPair((Model)new CowModel(
/* 28 */               context.bakeLayer(ModelLayers.COW)), (Model)new CowModel(context.bakeLayer(ModelLayers.COW_BABY))), CowVariant.ModelType.WARM, new AdultAndBabyModelPair((Model)new CowModel(
/* 29 */               context.bakeLayer(ModelLayers.WARM_COW)), (Model)new CowModel(context.bakeLayer(ModelLayers.WARM_COW_BABY))), CowVariant.ModelType.COLD, new AdultAndBabyModelPair((Model)new CowModel(
/* 30 */               context.bakeLayer(ModelLayers.COLD_COW)), (Model)new CowModel(context.bakeLayer(ModelLayers.COLD_COW_BABY)))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(CowRenderState state) {
/* 36 */     return (state.variant == null) ? MissingTextureAtlasSprite.getLocation() : state.variant.modelAndTexture().asset().texturePath();
/*    */   }
/*    */ 
/*    */   
/*    */   public CowRenderState createRenderState() {
/* 41 */     return new CowRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Cow entity, CowRenderState state, float partialTicks) {
/* 46 */     super.extractRenderState(entity, state, partialTicks);
/* 47 */     state.variant = (CowVariant)entity.getVariant().value();
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(CowRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 52 */     if (state.variant == null) {
/*    */       return;
/*    */     }
/* 55 */     this.model = (CowModel)((AdultAndBabyModelPair)this.models.get(state.variant.modelAndTexture().model())).getModel(state.isBaby);
/* 56 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/CowRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */