/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.LivingEntityRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public abstract class RenderLayer<S extends EntityRenderState, M extends EntityModel<? super S>> {
/*    */   private final RenderLayerParent<S, M> renderer;
/*    */   
/*    */   public RenderLayer(RenderLayerParent<S, M> renderer) {
/* 18 */     this.renderer = renderer;
/*    */   }
/*    */   
/*    */   protected static <S extends LivingEntityRenderState> void coloredCutoutModelCopyLayerRender(Model<? super S> model, Identifier texture, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, int color, int order) {
/* 22 */     if (!((LivingEntityRenderState)state).isInvisible) {
/* 23 */       renderColoredCutoutModel(model, texture, poseStack, submitNodeCollector, lightCoords, state, color, order);
/*    */     }
/*    */   }
/*    */   
/*    */   protected static <S extends LivingEntityRenderState> void renderColoredCutoutModel(Model<? super S> model, Identifier texture, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, int color, int order) {
/* 28 */     submitNodeCollector.order(order).submitModel(model, state, poseStack, RenderTypes.entityCutoutNoCull(texture), lightCoords, LivingEntityRenderer.getOverlayCoords((LivingEntityRenderState)state, 0.0F), color, null, ((LivingEntityRenderState)state).outlineColor, null);
/*    */   }
/*    */   
/*    */   public M getParentModel() {
/* 32 */     return (M)this.renderer.getModel();
/*    */   }
/*    */   
/*    */   public abstract void submit(PoseStack paramPoseStack, SubmitNodeCollector paramSubmitNodeCollector, int paramInt, S paramS, float paramFloat1, float paramFloat2);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/RenderLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */