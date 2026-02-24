/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.EndGatewayRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.EndPortalRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TheEndGatewayBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.TheEndPortalBlockEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class TheEndGatewayRenderer extends AbstractEndPortalRenderer<TheEndGatewayBlockEntity, EndGatewayRenderState> {
/* 19 */   private static final Identifier BEAM_LOCATION = Identifier.withDefaultNamespace("textures/entity/end_gateway_beam.png");
/*    */ 
/*    */   
/*    */   public EndGatewayRenderState createRenderState() {
/* 23 */     return new EndGatewayRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(TheEndGatewayBlockEntity blockEntity, EndGatewayRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 28 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 29 */     Level level = blockEntity.getLevel();
/* 30 */     if (blockEntity.isSpawning() || (blockEntity.isCoolingDown() && level != null)) {
/* 31 */       state.scale = blockEntity.isSpawning() ? blockEntity.getSpawnPercent(partialTicks) : blockEntity.getCooldownPercent(partialTicks);
/* 32 */       double beamDistance = blockEntity.isSpawning() ? blockEntity.getLevel().getMaxY() : 50.0D;
/* 33 */       state.scale = Mth.sin((state.scale * 3.1415927F));
/* 34 */       state.height = Mth.floor(state.scale * beamDistance);
/* 35 */       state.color = blockEntity.isSpawning() ? DyeColor.MAGENTA.getTextureDiffuseColor() : DyeColor.PURPLE.getTextureDiffuseColor();
/* 36 */       state.animationTime = (blockEntity.getLevel() != null) ? (Math.floorMod(blockEntity.getLevel().getGameTime(), 40) + partialTicks) : 0.0F;
/*    */     } else {
/* 38 */       state.height = 0;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(EndGatewayRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 44 */     if (state.height > 0) {
/* 45 */       BeaconRenderer.submitBeaconBeam(poseStack, submitNodeCollector, BEAM_LOCATION, state.scale, state.animationTime, -state.height, state.height * 2, state.color, 0.15F, 0.175F);
/*    */     }
/* 47 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getOffsetUp() {
/* 52 */     return 1.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getOffsetDown() {
/* 57 */     return 0.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected net.minecraft.client.renderer.rendertype.RenderType renderType() {
/* 62 */     return net.minecraft.client.renderer.rendertype.RenderTypes.endGateway();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getViewDistance() {
/* 67 */     return 256;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/TheEndGatewayRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */