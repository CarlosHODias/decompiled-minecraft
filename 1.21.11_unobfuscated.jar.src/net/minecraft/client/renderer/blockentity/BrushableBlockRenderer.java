/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.renderer.LevelRenderer;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.BrushableBlockRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.item.ItemModelResolver;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.item.ItemDisplayContext;
/*    */ import net.minecraft.world.level.BlockAndTintGetter;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BrushableBlockEntity;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class BrushableBlockRenderer implements BlockEntityRenderer<BrushableBlockEntity, BrushableBlockRenderState> {
/*    */   public BrushableBlockRenderer(BlockEntityRendererProvider.Context context) {
/* 24 */     this.itemModelResolver = context.itemModelResolver();
/*    */   }
/*    */   private final ItemModelResolver itemModelResolver;
/*    */   
/*    */   public BrushableBlockRenderState createRenderState() {
/* 29 */     return new BrushableBlockRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(BrushableBlockEntity blockEntity, BrushableBlockRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 34 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 35 */     state.hitDirection = blockEntity.getHitDirection();
/* 36 */     state.dustProgress = (Integer)blockEntity.getBlockState().getValue((Property)BlockStateProperties.DUSTED);
/*    */     
/* 38 */     if (blockEntity.getLevel() != null && blockEntity.getHitDirection() != null) {
/* 39 */       state.lightCoords = LevelRenderer.getLightColor(LevelRenderer.BrightnessGetter.DEFAULT, (BlockAndTintGetter)blockEntity.getLevel(), blockEntity.getBlockState(), blockEntity.getBlockPos().relative(blockEntity.getHitDirection()));
/*    */     }
/*    */     
/* 42 */     this.itemModelResolver.updateForTopItem(state.itemState, blockEntity.getItem(), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(BrushableBlockRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 47 */     if (state.dustProgress <= 0 || state.hitDirection == null || state.itemState.isEmpty()) {
/*    */       return;
/*    */     }
/*    */     
/* 51 */     poseStack.pushPose();
/* 52 */     poseStack.translate(0.0F, 0.5F, 0.0F);
/* 53 */     float[] translations = translations(state.hitDirection, state.dustProgress);
/*    */     
/* 55 */     poseStack.translate(translations[0], translations[1], translations[2]);
/* 56 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(75.0F));
/* 57 */     boolean eastWest = (state.hitDirection == Direction.EAST || state.hitDirection == Direction.WEST);
/* 58 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(((eastWest ? 90 : 0) + 11)));
/* 59 */     poseStack.scale(0.5F, 0.5F, 0.5F);
/*    */     
/* 61 */     state.itemState.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, 0);
/* 62 */     poseStack.popPose();
/*    */   }
/*    */   
/*    */   private float[] translations(Direction direction, int completionState) {
/* 66 */     float[] xyzTranslations = { 0.5F, 0.0F, 0.5F };
/*    */     
/* 68 */     float completionOffset = completionState / 10.0F * 0.75F;
/* 69 */     switch (direction) { case EAST:
/* 70 */         xyzTranslations[0] = 0.73F + completionOffset; break;
/* 71 */       case WEST: xyzTranslations[0] = 0.25F - completionOffset; break;
/* 72 */       case UP: xyzTranslations[1] = 0.25F + completionOffset; break;
/* 73 */       case DOWN: xyzTranslations[1] = -0.23F - completionOffset; break;
/* 74 */       case NORTH: xyzTranslations[2] = 0.25F - completionOffset; break;
/* 75 */       case SOUTH: xyzTranslations[2] = 0.73F + completionOffset; break; }
/*    */     
/* 77 */     return xyzTranslations;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/BrushableBlockRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */