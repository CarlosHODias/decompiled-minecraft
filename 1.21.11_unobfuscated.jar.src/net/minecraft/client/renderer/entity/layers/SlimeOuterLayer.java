/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.slime.SlimeModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.LivingEntityRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.SlimeRenderer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SlimeRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ 
/*    */ public class SlimeOuterLayer extends RenderLayer<SlimeRenderState, SlimeModel> {
/*    */   public SlimeOuterLayer(RenderLayerParent<SlimeRenderState, SlimeModel> renderer, EntityModelSet modelSet) {
/* 19 */     super(renderer);
/* 20 */     this.model = new SlimeModel(modelSet.bakeLayer(ModelLayers.SLIME_OUTER));
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SlimeRenderState state, float yRot, float xRot) {
/* 25 */     boolean appearsGlowingWithInvisibility = (state.appearsGlowing() && state.isInvisible);
/*    */     
/* 27 */     if (state.isInvisible && !appearsGlowingWithInvisibility) {
/*    */       return;
/*    */     }
/*    */     
/* 31 */     int overlayCoords = LivingEntityRenderer.getOverlayCoords((LivingEntityRenderState)state, 0.0F);
/* 32 */     if (appearsGlowingWithInvisibility) {
/* 33 */       submitNodeCollector.order(1).submitModel((Model)this.model, state, poseStack, RenderTypes.outline(SlimeRenderer.SLIME_LOCATION), lightCoords, overlayCoords, -1, null, state.outlineColor, null);
/*    */     } else {
/* 35 */       submitNodeCollector.order(1).submitModel((Model)this.model, state, poseStack, RenderTypes.entityTranslucent(SlimeRenderer.SLIME_LOCATION), lightCoords, overlayCoords, -1, null, state.outlineColor, null);
/*    */     } 
/*    */   }
/*    */   
/*    */   private final SlimeModel model;
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/SlimeOuterLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */