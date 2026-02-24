/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.SpawnerRenderState;
/*    */ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TrialSpawnerBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawner;
/*    */ import net.minecraft.world.level.block.entity.trialspawner.TrialSpawnerStateData;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class TrialSpawnerRenderer
/*    */   implements BlockEntityRenderer<TrialSpawnerBlockEntity, SpawnerRenderState> {
/*    */   public TrialSpawnerRenderer(BlockEntityRendererProvider.Context context) {
/* 21 */     this.entityRenderer = context.entityRenderer();
/*    */   }
/*    */   private final EntityRenderDispatcher entityRenderer;
/*    */   
/*    */   public SpawnerRenderState createRenderState() {
/* 26 */     return new SpawnerRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(TrialSpawnerBlockEntity blockEntity, SpawnerRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 31 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 32 */     if (blockEntity.getLevel() == null) {
/*    */       return;
/*    */     }
/* 35 */     TrialSpawner spawner = blockEntity.getTrialSpawner();
/* 36 */     TrialSpawnerStateData data = spawner.getStateData();
/* 37 */     Entity displayEntity = data.getOrCreateDisplayEntity(spawner, blockEntity.getLevel(), spawner.getState());
/* 38 */     extractSpawnerData(state, partialTicks, displayEntity, this.entityRenderer, data.getOSpin(), data.getSpin());
/*    */   }
/*    */   
/*    */   static void extractSpawnerData(SpawnerRenderState state, float partialTicks, Entity displayEntity, EntityRenderDispatcher entityRenderer, double oSpin, double spin) {
/* 42 */     if (displayEntity == null) {
/*    */       return;
/*    */     }
/* 45 */     state.displayEntity = entityRenderer.extractEntity(displayEntity, partialTicks);
/* 46 */     state.displayEntity.lightCoords = state.lightCoords;
/* 47 */     state.spin = (float)Mth.lerp(partialTicks, oSpin, spin) * 10.0F;
/* 48 */     state.scale = 0.53125F;
/* 49 */     float maxLength = Math.max(displayEntity.getBbWidth(), displayEntity.getBbHeight());
/* 50 */     if (maxLength > 1.0D) {
/* 51 */       state.scale /= maxLength;
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(SpawnerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 57 */     if (state.displayEntity != null)
/* 58 */       SpawnerRenderer.submitEntityInSpawner(poseStack, submitNodeCollector, state.displayEntity, this.entityRenderer, state.spin, state.scale, camera); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/TrialSpawnerRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */