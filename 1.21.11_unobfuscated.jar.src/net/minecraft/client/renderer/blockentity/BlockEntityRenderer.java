/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public interface BlockEntityRenderer<T extends BlockEntity, S extends BlockEntityRenderState>
/*    */ {
/*    */   default void extractRenderState(T blockEntity, S state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 16 */     BlockEntityRenderState.extractBase((BlockEntity)blockEntity, (BlockEntityRenderState)state, breakProgress);
/*    */   }
/*    */   S createRenderState();
/*    */   void submit(S paramS, PoseStack paramPoseStack, SubmitNodeCollector paramSubmitNodeCollector, CameraRenderState paramCameraRenderState);
/*    */   
/*    */   default boolean shouldRenderOffScreen() {
/* 22 */     return false;
/*    */   }
/*    */   
/*    */   default int getViewDistance() {
/* 26 */     return 64;
/*    */   }
/*    */   
/*    */   default boolean shouldRender(T blockEntity, Vec3 cameraPosition) {
/* 30 */     return Vec3.atCenterOf((Vec3i)blockEntity.getBlockPos()).closerThan((Position)cameraPosition, getViewDistance());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/BlockEntityRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */