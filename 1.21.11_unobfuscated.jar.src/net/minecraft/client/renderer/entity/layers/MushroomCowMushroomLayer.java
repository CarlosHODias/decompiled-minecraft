/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.animal.cow.CowModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.block.BlockRenderDispatcher;
/*    */ import net.minecraft.client.renderer.block.model.BlockStateModel;
/*    */ import net.minecraft.client.renderer.entity.LivingEntityRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.MushroomCowRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class MushroomCowMushroomLayer extends RenderLayer<MushroomCowRenderState, CowModel> {
/*    */   public MushroomCowMushroomLayer(RenderLayerParent<MushroomCowRenderState, CowModel> renderer, BlockRenderDispatcher blockRenderer) {
/* 20 */     super(renderer);
/* 21 */     this.blockRenderer = blockRenderer;
/*    */   }
/*    */   private final BlockRenderDispatcher blockRenderer;
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, MushroomCowRenderState state, float yRot, float xRot) {
/* 26 */     if (state.isBaby) {
/*    */       return;
/*    */     }
/*    */     
/* 30 */     boolean appearsGlowingWithInvisibility = (state.appearsGlowing() && state.isInvisible);
/*    */     
/* 32 */     if (state.isInvisible && !appearsGlowingWithInvisibility) {
/*    */       return;
/*    */     }
/*    */     
/* 36 */     BlockState mushroomBlockState = state.variant.getBlockState();
/* 37 */     int overlayCoords = LivingEntityRenderer.getOverlayCoords((LivingEntityRenderState)state, 0.0F);
/* 38 */     BlockStateModel model = this.blockRenderer.getBlockModel(mushroomBlockState);
/*    */     
/* 40 */     poseStack.pushPose();
/* 41 */     poseStack.translate(0.2F, -0.35F, 0.5F);
/* 42 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-48.0F));
/* 43 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/* 44 */     poseStack.translate(-0.5F, -0.5F, -0.5F);
/* 45 */     submitMushroomBlock(poseStack, submitNodeCollector, lightCoords, appearsGlowingWithInvisibility, state.outlineColor, mushroomBlockState, overlayCoords, model);
/* 46 */     poseStack.popPose();
/*    */     
/* 48 */     poseStack.pushPose();
/* 49 */     poseStack.translate(0.2F, -0.35F, 0.5F);
/* 50 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(42.0F));
/* 51 */     poseStack.translate(0.1F, 0.0F, -0.6F);
/* 52 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-48.0F));
/* 53 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/* 54 */     poseStack.translate(-0.5F, -0.5F, -0.5F);
/* 55 */     submitMushroomBlock(poseStack, submitNodeCollector, lightCoords, appearsGlowingWithInvisibility, state.outlineColor, mushroomBlockState, overlayCoords, model);
/* 56 */     poseStack.popPose();
/*    */     
/* 58 */     poseStack.pushPose();
/* 59 */     getParentModel().getHead().translateAndRotate(poseStack);
/* 60 */     poseStack.translate(0.0F, -0.7F, -0.2F);
/* 61 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-78.0F));
/* 62 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/* 63 */     poseStack.translate(-0.5F, -0.5F, -0.5F);
/* 64 */     submitMushroomBlock(poseStack, submitNodeCollector, lightCoords, appearsGlowingWithInvisibility, state.outlineColor, mushroomBlockState, overlayCoords, model);
/* 65 */     poseStack.popPose();
/*    */   }
/*    */   
/*    */   private void submitMushroomBlock(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, boolean appearsGlowingWithInvisibility, int outlineColor, BlockState mushroomBlockState, int overlayCoords, BlockStateModel model) {
/* 69 */     if (appearsGlowingWithInvisibility) {
/* 70 */       submitNodeCollector.submitBlockModel(poseStack, RenderTypes.outline(TextureAtlas.LOCATION_BLOCKS), model, 0.0F, 0.0F, 0.0F, lightCoords, overlayCoords, outlineColor);
/*    */     } else {
/* 72 */       submitNodeCollector.submitBlock(poseStack, mushroomBlockState, lightCoords, overlayCoords, outlineColor);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/MushroomCowMushroomLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */