/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public abstract class EnergySwirlLayer<S extends EntityRenderState, M extends EntityModel<S>> extends RenderLayer<S, M> {
/*    */   public EnergySwirlLayer(RenderLayerParent<S, M> renderer) {
/* 15 */     super(renderer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
/* 20 */     if (!isPowered(state)) {
/*    */       return;
/*    */     }
/*    */     
/* 24 */     float t = ((EntityRenderState)state).ageInTicks;
/* 25 */     M model = model();
/*    */     
/* 27 */     submitNodeCollector.order(1).submitModel((Model)model, state, poseStack, RenderTypes.energySwirl(getTextureLocation(), xOffset(t) % 1.0F, t * 0.01F % 1.0F), lightCoords, OverlayTexture.NO_OVERLAY, -8355712, null, ((EntityRenderState)state).outlineColor, null);
/*    */   }
/*    */   
/*    */   protected abstract boolean isPowered(S paramS);
/*    */   
/*    */   protected abstract float xOffset(float paramFloat);
/*    */   
/*    */   protected abstract Identifier getTextureLocation();
/*    */   
/*    */   protected abstract M model();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/EnergySwirlLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */