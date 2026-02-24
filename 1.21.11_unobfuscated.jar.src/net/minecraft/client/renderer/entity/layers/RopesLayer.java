/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.animal.ghast.HappyGhastModel;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HappyGhastRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.tags.ItemTags;
/*    */ 
/*    */ public class RopesLayer<M extends HappyGhastModel> extends RenderLayer<HappyGhastRenderState, M> {
/*    */   private final RenderType ropes;
/*    */   
/*    */   public RopesLayer(RenderLayerParent<HappyGhastRenderState, M> renderer, EntityModelSet modelSet, Identifier ropesTexture) {
/* 22 */     super(renderer);
/* 23 */     this.ropes = RenderTypes.entityCutoutNoCull(ropesTexture);
/* 24 */     this.adultModel = new HappyGhastModel(modelSet.bakeLayer(ModelLayers.HAPPY_GHAST_ROPES));
/* 25 */     this.babyModel = new HappyGhastModel(modelSet.bakeLayer(ModelLayers.HAPPY_GHAST_BABY_ROPES));
/*    */   }
/*    */   private final HappyGhastModel adultModel; private final HappyGhastModel babyModel;
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, HappyGhastRenderState state, float yRot, float xRot) {
/* 30 */     if (!state.isLeashHolder || !state.bodyItem.is(ItemTags.HARNESSES)) {
/*    */       return;
/*    */     }
/* 33 */     HappyGhastModel model = state.isBaby ? this.babyModel : this.adultModel;
/* 34 */     submitNodeCollector.submitModel((Model)model, state, poseStack, this.ropes, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/RopesLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */