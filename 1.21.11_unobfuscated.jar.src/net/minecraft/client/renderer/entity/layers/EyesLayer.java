/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ 
/*    */ public abstract class EyesLayer<S extends EntityRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
/*    */   public EyesLayer(RenderLayerParent<S, M> renderer) {
/* 14 */     super(renderer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
/* 19 */     submitNodeCollector.order(1).submitModel((Model)getParentModel(), state, poseStack, renderType(), lightCoords, OverlayTexture.NO_OVERLAY, -1, null, ((EntityRenderState)state).outlineColor, null);
/*    */   }
/*    */   
/*    */   public abstract RenderType renderType();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/EyesLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */