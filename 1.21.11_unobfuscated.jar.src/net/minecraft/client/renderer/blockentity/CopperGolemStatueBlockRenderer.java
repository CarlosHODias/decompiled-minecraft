/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.statue.CopperGolemStatueModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.CopperGolemStatueRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.core.Direction;
/*    */ import net.minecraft.world.entity.animal.golem.CopperGolemOxidationLevels;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.CopperGolemStatueBlock;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.CopperGolemStatueBlockEntity;
/*    */ import net.minecraft.world.level.block.state.properties.BlockStateProperties;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class CopperGolemStatueBlockRenderer implements BlockEntityRenderer<CopperGolemStatueBlockEntity, CopperGolemStatueRenderState> {
/* 27 */   private final Map<CopperGolemStatueBlock.Pose, CopperGolemStatueModel> models = new HashMap<>();
/*    */   
/*    */   public CopperGolemStatueBlockRenderer(BlockEntityRendererProvider.Context context) {
/* 30 */     EntityModelSet modelSet = context.entityModelSet();
/* 31 */     this.models.put(CopperGolemStatueBlock.Pose.STANDING, new CopperGolemStatueModel(modelSet.bakeLayer(ModelLayers.COPPER_GOLEM)));
/* 32 */     this.models.put(CopperGolemStatueBlock.Pose.RUNNING, new CopperGolemStatueModel(modelSet.bakeLayer(ModelLayers.COPPER_GOLEM_RUNNING)));
/* 33 */     this.models.put(CopperGolemStatueBlock.Pose.SITTING, new CopperGolemStatueModel(modelSet.bakeLayer(ModelLayers.COPPER_GOLEM_SITTING)));
/* 34 */     this.models.put(CopperGolemStatueBlock.Pose.STAR, new CopperGolemStatueModel(modelSet.bakeLayer(ModelLayers.COPPER_GOLEM_STAR)));
/*    */   }
/*    */ 
/*    */   
/*    */   public CopperGolemStatueRenderState createRenderState() {
/* 39 */     return new CopperGolemStatueRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(CopperGolemStatueBlockEntity blockEntity, CopperGolemStatueRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 44 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 45 */     state.direction = (Direction)blockEntity.getBlockState().getValue((Property)CopperGolemStatueBlock.FACING);
/* 46 */     state.pose = (CopperGolemStatueBlock.Pose)blockEntity.getBlockState().getValue((Property)BlockStateProperties.COPPER_GOLEM_POSE);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(CopperGolemStatueRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 51 */     Block block = state.blockState.getBlock(); if (block instanceof CopperGolemStatueBlock) { CopperGolemStatueBlock copperGolemStatueBlock = (CopperGolemStatueBlock)block;
/* 52 */       poseStack.pushPose();
/*    */       
/* 54 */       poseStack.translate(0.5F, 0.0F, 0.5F);
/* 55 */       CopperGolemStatueModel model = this.models.get(state.pose);
/* 56 */       Direction direction = state.direction;
/* 57 */       RenderType renderType = RenderTypes.entityCutoutNoCull(CopperGolemOxidationLevels.getOxidationLevel(copperGolemStatueBlock.getWeatheringState()).texture());
/* 58 */       submitNodeCollector.submitModel((net.minecraft.client.model.Model)model, direction, poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, 0, state.breakProgress);
/* 59 */       poseStack.popPose(); }
/*    */   
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/CopperGolemStatueBlockRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */