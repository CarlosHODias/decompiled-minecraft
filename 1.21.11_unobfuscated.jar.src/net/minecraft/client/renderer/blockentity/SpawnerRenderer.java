/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.SpawnerRenderState;
/*    */ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.BaseSpawner;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class SpawnerRenderer implements BlockEntityRenderer<SpawnerBlockEntity, SpawnerRenderState> {
/*    */   public SpawnerRenderer(BlockEntityRendererProvider.Context context) {
/* 21 */     this.entityRenderer = context.entityRenderer();
/*    */   }
/*    */ 
/*    */   
/*    */   public SpawnerRenderState createRenderState() {
/* 26 */     return new SpawnerRenderState();
/*    */   }
/*    */   private final EntityRenderDispatcher entityRenderer;
/*    */   
/*    */   public void extractRenderState(SpawnerBlockEntity blockEntity, SpawnerRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 31 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 32 */     if (blockEntity.getLevel() == null) {
/*    */       return;
/*    */     }
/* 35 */     BaseSpawner spawner = blockEntity.getSpawner();
/* 36 */     Entity displayEntity = spawner.getOrCreateDisplayEntity(blockEntity.getLevel(), blockEntity.getBlockPos());
/* 37 */     TrialSpawnerRenderer.extractSpawnerData(state, partialTicks, displayEntity, this.entityRenderer, spawner.getOSpin(), spawner.getSpin());
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(SpawnerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 42 */     if (state.displayEntity != null) {
/* 43 */       submitEntityInSpawner(poseStack, submitNodeCollector, state.displayEntity, this.entityRenderer, state.spin, state.scale, camera);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void submitEntityInSpawner(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, EntityRenderState displayEntity, EntityRenderDispatcher entityRenderer, float spin, float scale, CameraRenderState camera) {
/* 48 */     poseStack.pushPose();
/* 49 */     poseStack.translate(0.5F, 0.4F, 0.5F);
/* 50 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(spin));
/* 51 */     poseStack.translate(0.0F, -0.2F, 0.0F);
/* 52 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-30.0F));
/* 53 */     poseStack.scale(scale, scale, scale);
/* 54 */     entityRenderer.submit(displayEntity, camera, 0.0D, 0.0D, 0.0D, poseStack, submitNodeCollector);
/* 55 */     poseStack.popPose();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/SpawnerRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */