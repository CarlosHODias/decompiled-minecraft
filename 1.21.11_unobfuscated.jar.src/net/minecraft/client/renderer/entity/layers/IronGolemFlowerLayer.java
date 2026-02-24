/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.animal.golem.IronGolemModel;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class IronGolemFlowerLayer extends RenderLayer<IronGolemRenderState, IronGolemModel> {
/*    */   public IronGolemFlowerLayer(RenderLayerParent<IronGolemRenderState, IronGolemModel> renderer) {
/* 16 */     super(renderer);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, IronGolemRenderState state, float yRot, float xRot) {
/* 21 */     if (state.offerFlowerTick == 0) {
/*    */       return;
/*    */     }
/*    */     
/* 25 */     poseStack.pushPose();
/* 26 */     ModelPart arm = getParentModel().getFlowerHoldingArm();
/*    */ 
/*    */     
/* 29 */     arm.translateAndRotate(poseStack);
/* 30 */     poseStack.translate(-1.1875F, 1.0625F, -0.9375F);
/*    */     
/* 32 */     poseStack.translate(0.5F, 0.5F, 0.5F);
/* 33 */     float s = 0.5F;
/* 34 */     poseStack.scale(0.5F, 0.5F, 0.5F);
/* 35 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-90.0F));
/* 36 */     poseStack.translate(-0.5F, -0.5F, -0.5F);
/*    */     
/* 38 */     submitNodeCollector.submitBlock(poseStack, Blocks.POPPY.defaultBlockState(), lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
/* 39 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/IronGolemFlowerLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */