/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.monster.witch.WitchModel;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.WitchRenderState;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class WitchItemLayer extends CrossedArmsItemLayer<WitchRenderState, WitchModel> {
/*    */   public WitchItemLayer(RenderLayerParent<WitchRenderState, WitchModel> renderer) {
/* 11 */     super(renderer);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void applyTranslation(WitchRenderState state, PoseStack poseStack) {
/* 16 */     if (state.isHoldingPotion) {
/* 17 */       getParentModel().root().translateAndRotate(poseStack);
/* 18 */       getParentModel().translateToHead(poseStack);
/* 19 */       getParentModel().getNose().translateAndRotate(poseStack);
/* 20 */       poseStack.translate(0.0625F, 0.25F, 0.0F);
/* 21 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(180.0F));
/* 22 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(140.0F));
/* 23 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(10.0F));
/* 24 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(180.0F));
/*    */       return;
/*    */     } 
/* 27 */     super.applyTranslation(state, poseStack);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/WitchItemLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */