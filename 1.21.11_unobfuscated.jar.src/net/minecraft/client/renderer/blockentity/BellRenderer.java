/*    */ package net.minecraft.client.renderer.blockentity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.bell.BellModel;
/*    */ import net.minecraft.client.renderer.Sheets;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.blockentity.state.BellRenderState;
/*    */ import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
/*    */ import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.client.resources.model.Material;
/*    */ import net.minecraft.client.resources.model.MaterialSet;
/*    */ import net.minecraft.world.level.block.entity.BellBlockEntity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class BellRenderer implements BlockEntityRenderer<BellBlockEntity, BellRenderState> {
/* 22 */   public static final Material BELL_TEXTURE = Sheets.BLOCK_ENTITIES_MAPPER.defaultNamespaceApply("bell/bell_body");
/*    */   
/*    */   private final MaterialSet materials;
/*    */   private final BellModel model;
/*    */   
/*    */   public BellRenderer(BlockEntityRendererProvider.Context context) {
/* 28 */     this.materials = context.materials();
/* 29 */     this.model = new BellModel(context.bakeLayer(ModelLayers.BELL));
/*    */   }
/*    */ 
/*    */   
/*    */   public BellRenderState createRenderState() {
/* 34 */     return new BellRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(BellBlockEntity blockEntity, BellRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
/* 39 */     super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
/* 40 */     state.ticks = blockEntity.ticks + partialTicks;
/* 41 */     state.shakeDirection = blockEntity.shaking ? blockEntity.clickDirection : null;
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(BellRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 46 */     BellModel.State modelState = new BellModel.State(state.ticks, state.shakeDirection);
/* 47 */     this.model.setupAnim(modelState);
/* 48 */     RenderType renderType = BELL_TEXTURE.renderType(RenderTypes::entitySolid);
/* 49 */     submitNodeCollector.submitModel((Model)this.model, modelState, poseStack, renderType, state.lightCoords, OverlayTexture.NO_OVERLAY, -1, this.materials.get(BELL_TEXTURE), 0, state.breakProgress);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/BellRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */