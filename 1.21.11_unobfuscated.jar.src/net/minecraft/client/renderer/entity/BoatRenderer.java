/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.boat.BoatModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.BoatRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Unit;
/*    */ 
/*    */ public class BoatRenderer extends AbstractBoatRenderer {
/*    */   private final Model.Simple waterPatchModel;
/*    */   private final Identifier texture;
/*    */   private final EntityModel<BoatRenderState> model;
/*    */   
/*    */   public BoatRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelId) {
/* 23 */     super(context);
/* 24 */     this.texture = modelId.model().withPath(p -> "textures/entity/" + p + ".png");
/* 25 */     this.waterPatchModel = new Model.Simple(context.bakeLayer(ModelLayers.BOAT_WATER_PATCH), t -> RenderTypes.waterMask());
/* 26 */     this.model = (EntityModel<BoatRenderState>)new BoatModel(context.bakeLayer(modelId));
/*    */   }
/*    */ 
/*    */   
/*    */   protected EntityModel<BoatRenderState> model() {
/* 31 */     return this.model;
/*    */   }
/*    */ 
/*    */   
/*    */   protected RenderType renderType() {
/* 36 */     return this.model.renderType(this.texture);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void submitTypeAdditions(BoatRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
/* 41 */     if (!state.isUnderWater)
/* 42 */       submitNodeCollector.submitModel((Model)this.waterPatchModel, Unit.INSTANCE, poseStack, this.waterPatchModel.renderType(this.texture), lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/BoatRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */