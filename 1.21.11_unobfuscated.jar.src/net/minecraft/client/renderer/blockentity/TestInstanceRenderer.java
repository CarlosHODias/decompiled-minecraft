/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BeaconRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityWithBoundingBoxRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.TestInstanceRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.gizmos.GizmoStyle;
/*    */ import net.minecraft.gizmos.Gizmos;
/*    */ import net.minecraft.gizmos.TextGizmo;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TestInstanceBlockEntity;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class TestInstanceRenderer implements BlockEntityRenderer<TestInstanceBlockEntity, TestInstanceRenderState> {
/*    */   private static final float ERROR_PADDING = 0.02F;
/* 24 */   private final BeaconRenderer<TestInstanceBlockEntity> beacon = new BeaconRenderer<>();
/* 25 */   private final BlockEntityWithBoundingBoxRenderer<TestInstanceBlockEntity> box = new BlockEntityWithBoundingBoxRenderer<>();
/*    */ 
/*    */   
/*    */   public TestInstanceRenderState createRenderState() {
/* 29 */     return new TestInstanceRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(TestInstanceBlockEntity blockEntity, TestInstanceRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 34 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 35 */     state.beaconRenderState = new BeaconRenderState();
/* 36 */     BlockEntityRenderState.extractBase((BlockEntity)blockEntity, (BlockEntityRenderState)state.beaconRenderState, breakProgress);
/* 37 */     BeaconRenderer.extract(blockEntity, state.beaconRenderState, partialTicks, cameraPosition);
/* 38 */     state.blockEntityWithBoundingBoxRenderState = new BlockEntityWithBoundingBoxRenderState();
/* 39 */     BlockEntityRenderState.extractBase((BlockEntity)blockEntity, (BlockEntityRenderState)state.blockEntityWithBoundingBoxRenderState, breakProgress);
/* 40 */     BlockEntityWithBoundingBoxRenderer.extract(blockEntity, state.blockEntityWithBoundingBoxRenderState);
/* 41 */     state.errorMarkers.clear();
/* 42 */     for (TestInstanceBlockEntity.ErrorMarker marker : (Iterable<TestInstanceBlockEntity.ErrorMarker>)blockEntity.getErrorMarkers()) {
/* 43 */       state.errorMarkers.add(new TestInstanceBlockEntity.ErrorMarker(marker.pos(), marker.text()));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(TestInstanceRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 49 */     this.beacon.submit(state.beaconRenderState, poseStack, submitNodeCollector, camera);
/* 50 */     this.box.submit(state.blockEntityWithBoundingBoxRenderState, poseStack, submitNodeCollector, camera);
/*    */     
/* 52 */     for (TestInstanceBlockEntity.ErrorMarker error : (Iterable<TestInstanceBlockEntity.ErrorMarker>)state.errorMarkers) {
/* 53 */       submitErrorMarker(error);
/*    */     }
/*    */   }
/*    */   
/*    */   private void submitErrorMarker(TestInstanceBlockEntity.ErrorMarker error) {
/* 58 */     BlockPos pos = error.pos();
/*    */     
/* 60 */     Gizmos.cuboid(new AABB(pos).inflate(0.019999999552965164D), GizmoStyle.fill(ARGB.colorFromFloat(0.375F, 1.0F, 0.0F, 0.0F)));
/*    */     
/* 62 */     String text = error.text().getString();
/* 63 */     float scale = 0.16F;
/*    */     
/* 65 */     Gizmos.billboardText(text, Vec3.atLowerCornerWithOffset((Vec3i)pos, 0.5D, 1.2D, 0.5D), TextGizmo.Style.whiteAndCentered().withScale(0.16F)).setAlwaysOnTop();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldRenderOffScreen() {
/* 70 */     return (this.beacon.shouldRenderOffScreen() || this.box.shouldRenderOffScreen());
/*    */   }
/*    */ 
/*    */   
/*    */   public int getViewDistance() {
/* 75 */     return Math.max(this.beacon.getViewDistance(), this.box.getViewDistance());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean shouldRender(TestInstanceBlockEntity blockEntity, Vec3 cameraPosition) {
/* 80 */     return (this.beacon.shouldRender(blockEntity, cameraPosition) || this.box.shouldRender(blockEntity, cameraPosition));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/TestInstanceRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */