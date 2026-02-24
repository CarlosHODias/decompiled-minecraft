/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.TropicalFishRenderState;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.fish.TropicalFish;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class TropicalFishRenderer extends MobRenderer<TropicalFish, TropicalFishRenderState, EntityModel<TropicalFishRenderState>> {
/*    */   private final EntityModel<TropicalFishRenderState> smallModel;
/*    */   private final EntityModel<TropicalFishRenderState> largeModel;
/* 21 */   private static final Identifier SMALL_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fish/tropical_a.png");
/* 22 */   private static final Identifier LARGE_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fish/tropical_b.png");
/*    */   
/*    */   public TropicalFishRenderer(EntityRendererProvider.Context context) {
/* 25 */     super(context, (EntityModel<TropicalFishRenderState>)new net.minecraft.client.model.animal.fish.TropicalFishSmallModel(context.bakeLayer(ModelLayers.TROPICAL_FISH_SMALL)), 0.15F);
/* 26 */     this.smallModel = getModel();
/* 27 */     this.largeModel = (EntityModel<TropicalFishRenderState>)new net.minecraft.client.model.animal.fish.TropicalFishLargeModel(context.bakeLayer(ModelLayers.TROPICAL_FISH_LARGE));
/* 28 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<TropicalFishRenderState, EntityModel<TropicalFishRenderState>>)new net.minecraft.client.renderer.entity.layers.TropicalFishPatternLayer(this, context.getModelSet()));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(TropicalFishRenderState state) {
/* 33 */     switch (state.pattern.base()) { default: throw new MatchException(null, null);case SMALL: case LARGE: break; }  return 
/*    */       
/* 35 */       LARGE_TEXTURE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public TropicalFishRenderState createRenderState() {
/* 41 */     return new TropicalFishRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(TropicalFish entity, TropicalFishRenderState state, float partialTicks) {
/* 46 */     super.extractRenderState(entity, state, partialTicks);
/* 47 */     state.pattern = entity.getPattern();
/* 48 */     state.baseColor = entity.getBaseColor().getTextureDiffuseColor();
/* 49 */     state.patternColor = entity.getPatternColor().getTextureDiffuseColor();
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(TropicalFishRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 54 */     switch (state.pattern.base()) { default: throw new MatchException(null, null);case SMALL: case LARGE: break; }  this
/*    */       
/* 56 */       .model = this.largeModel;
/*    */     
/* 58 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getModelTint(TropicalFishRenderState state) {
/* 63 */     return state.baseColor;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setupRotations(TropicalFishRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 68 */     super.setupRotations(state, poseStack, bodyRot, entityScale);
/*    */     
/* 70 */     float bodyZRot = 4.3F * Mth.sin((0.6F * state.ageInTicks));
/* 71 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(bodyZRot));
/*    */     
/* 73 */     if (!state.isInWater) {
/* 74 */       poseStack.translate(0.2F, 0.1F, 0.0F);
/* 75 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(90.0F));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/TropicalFishRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */