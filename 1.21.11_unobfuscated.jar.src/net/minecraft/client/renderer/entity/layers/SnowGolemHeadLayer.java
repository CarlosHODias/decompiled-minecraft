/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.animal.golem.SnowGolemModel;
/*    */ import net.minecraft.client.renderer.ItemBlockRenderTypes;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.block.BlockRenderDispatcher;
/*    */ import net.minecraft.client.renderer.block.model.BlockStateModel;
/*    */ import net.minecraft.client.renderer.entity.LivingEntityRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SnowGolemRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class SnowGolemHeadLayer extends RenderLayer<SnowGolemRenderState, SnowGolemModel> {
/*    */   public SnowGolemHeadLayer(RenderLayerParent<SnowGolemRenderState, SnowGolemModel> renderer, BlockRenderDispatcher blockRenderer) {
/* 23 */     super(renderer);
/* 24 */     this.blockRenderer = blockRenderer;
/*    */   }
/*    */   private final BlockRenderDispatcher blockRenderer;
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SnowGolemRenderState state, float yRot, float xRot) {
/* 29 */     if (!state.hasPumpkin) {
/*    */       return;
/*    */     }
/*    */     
/* 33 */     if (state.isInvisible && !state.appearsGlowing()) {
/*    */       return;
/*    */     }
/*    */     
/* 37 */     poseStack.pushPose();
/* 38 */     getParentModel().getHead().translateAndRotate(poseStack);
/*    */     
/* 40 */     float s = 0.625F;
/* 41 */     poseStack.translate(0.0F, -0.34375F, 0.0F);
/* 42 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F));
/* 43 */     poseStack.scale(0.625F, -0.625F, -0.625F);
/*    */ 
/*    */     
/* 46 */     BlockState pumpkinBlockState = Blocks.CARVED_PUMPKIN.defaultBlockState();
/* 47 */     BlockStateModel model = this.blockRenderer.getBlockModel(pumpkinBlockState);
/* 48 */     int overlayCoords = LivingEntityRenderer.getOverlayCoords((LivingEntityRenderState)state, 0.0F);
/*    */     
/* 50 */     poseStack.translate(-0.5F, -0.5F, -0.5F);
/*    */ 
/*    */     
/* 53 */     RenderType renderType = (state.appearsGlowing() && state.isInvisible) ? RenderTypes.outline(TextureAtlas.LOCATION_BLOCKS) : ItemBlockRenderTypes.getRenderType(pumpkinBlockState);
/* 54 */     submitNodeCollector.submitBlockModel(poseStack, renderType, model, 0.0F, 0.0F, 0.0F, lightCoords, overlayCoords, state.outlineColor);
/*    */     
/* 56 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/SnowGolemHeadLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */