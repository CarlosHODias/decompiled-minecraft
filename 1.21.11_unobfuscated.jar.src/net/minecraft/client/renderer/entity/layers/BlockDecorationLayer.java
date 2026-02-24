/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BlockDecorationLayer<S extends EntityRenderState, M extends EntityModel<S>>
/*    */   extends RenderLayer<S, M>
/*    */ {
/*    */   private final Function<S, Optional<BlockState>> blockState;
/*    */   private final Consumer<PoseStack> transform;
/*    */   
/*    */   public BlockDecorationLayer(RenderLayerParent<S, M> renderer, Function<S, Optional<BlockState>> blockState, Consumer<PoseStack> transform) {
/* 27 */     super(renderer);
/* 28 */     this.blockState = blockState;
/* 29 */     this.transform = transform;
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
/* 34 */     Optional<BlockState> optionalBlockState = this.blockState.apply(state);
/* 35 */     if (optionalBlockState.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 39 */     BlockState blockState = optionalBlockState.get();
/* 40 */     Block block = blockState.getBlock();
/* 41 */     boolean isCopperGolemStatue = block instanceof net.minecraft.world.level.block.CopperGolemStatueBlock;
/*    */     
/* 43 */     poseStack.pushPose();
/*    */     
/* 45 */     this.transform.accept(poseStack);
/*    */     
/* 47 */     if (!isCopperGolemStatue) {
/* 48 */       poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(180.0F));
/*    */     }
/* 50 */     if (isCopperGolemStatue || block instanceof net.minecraft.world.level.block.AbstractSkullBlock || block instanceof net.minecraft.world.level.block.AbstractBannerBlock || block instanceof net.minecraft.world.level.block.AbstractChestBlock) {
/* 51 */       poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0F));
/*    */     }
/* 53 */     if (block instanceof net.minecraft.world.level.block.FlowerBedBlock) {
/* 54 */       poseStack.translate(-0.25D, -1.5D, -0.25D);
/* 55 */     } else if (!isCopperGolemStatue) {
/* 56 */       poseStack.translate(-0.5D, -1.5D, -0.5D);
/*    */     } else {
/* 58 */       poseStack.translate(-0.5D, 0.0D, -0.5D);
/*    */     } 
/*    */     
/* 61 */     submitNodeCollector.submitBlock(poseStack, blockState, lightCoords, OverlayTexture.NO_OVERLAY, ((EntityRenderState)state).outlineColor);
/*    */     
/* 63 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/BlockDecorationLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */