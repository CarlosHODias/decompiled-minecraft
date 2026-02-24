/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.AdultAndBabyModelPair;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.animal.pig.ColdPigModel;
/*    */ import net.minecraft.client.model.animal.pig.PigModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.PigRenderState;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.pig.Pig;
/*    */ import net.minecraft.world.entity.animal.pig.PigVariant;
/*    */ 
/*    */ public class PigRenderer extends MobRenderer<Pig, PigRenderState, PigModel> {
/*    */   public PigRenderer(EntityRendererProvider.Context context) {
/* 26 */     super(context, new PigModel(context.bakeLayer(ModelLayers.PIG)), 0.7F);
/* 27 */     this.models = bakeModels(context);
/* 28 */     addLayer((RenderLayer<PigRenderState, PigModel>)new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), net.minecraft.client.resources.model.EquipmentClientInfo.LayerType.PIG_SADDLE, state -> state.saddle, (EntityModel)new PigModel(
/*    */ 
/*    */             
/* 31 */             context.bakeLayer(ModelLayers.PIG_SADDLE)), (EntityModel)new PigModel(
/* 32 */             context.bakeLayer(ModelLayers.PIG_BABY_SADDLE))));
/*    */   }
/*    */   private final Map<PigVariant.ModelType, AdultAndBabyModelPair<PigModel>> models;
/*    */   
/*    */   private static Map<PigVariant.ModelType, AdultAndBabyModelPair<PigModel>> bakeModels(EntityRendererProvider.Context context) {
/* 37 */     return Maps.newEnumMap(Map.of(PigVariant.ModelType.NORMAL, new AdultAndBabyModelPair((Model)new PigModel(
/* 38 */               context.bakeLayer(ModelLayers.PIG)), (Model)new PigModel(context.bakeLayer(ModelLayers.PIG_BABY))), PigVariant.ModelType.COLD, new AdultAndBabyModelPair((Model)new ColdPigModel(
/* 39 */               context.bakeLayer(ModelLayers.COLD_PIG)), (Model)new ColdPigModel(context.bakeLayer(ModelLayers.COLD_PIG_BABY)))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void submit(PigRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 45 */     if (state.variant == null) {
/*    */       return;
/*    */     }
/* 48 */     this.model = (PigModel)((AdultAndBabyModelPair)this.models.get(state.variant.modelAndTexture().model())).getModel(state.isBaby);
/* 49 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(PigRenderState state) {
/* 54 */     return (state.variant == null) ? net.minecraft.client.renderer.texture.MissingTextureAtlasSprite.getLocation() : state.variant.modelAndTexture().asset().texturePath();
/*    */   }
/*    */ 
/*    */   
/*    */   public PigRenderState createRenderState() {
/* 59 */     return new PigRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Pig entity, PigRenderState state, float partialTicks) {
/* 64 */     super.extractRenderState(entity, state, partialTicks);
/* 65 */     state.saddle = entity.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.SADDLE).copy();
/* 66 */     state.variant = (PigVariant)entity.getVariant().value();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/PigRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */