/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.animal.llama.LlamaSpitModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LlamaSpitRenderState;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.LlamaSpit;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class LlamaSpitRenderer extends EntityRenderer<LlamaSpit, LlamaSpitRenderState> {
/* 15 */   private static final Identifier LLAMA_SPIT_LOCATION = Identifier.withDefaultNamespace("textures/entity/llama/spit.png");
/*    */   
/*    */   private final LlamaSpitModel model;
/*    */   
/*    */   public LlamaSpitRenderer(EntityRendererProvider.Context context) {
/* 20 */     super(context);
/* 21 */     this.model = new LlamaSpitModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.LLAMA_SPIT));
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(LlamaSpitRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 26 */     poseStack.pushPose();
/*    */     
/* 28 */     poseStack.translate(0.0F, 0.15F, 0.0F);
/* 29 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(state.yRot - 90.0F));
/* 30 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(state.xRot));
/*    */     
/* 32 */     submitNodeCollector.submitModel((net.minecraft.client.model.Model)this.model, state, poseStack, this.model.renderType(LLAMA_SPIT_LOCATION), state.lightCoords, net.minecraft.client.renderer.texture.OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/*    */     
/* 34 */     poseStack.popPose();
/*    */     
/* 36 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */ 
/*    */   
/*    */   public LlamaSpitRenderState createRenderState() {
/* 41 */     return new LlamaSpitRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(LlamaSpit entity, LlamaSpitRenderState state, float partialTicks) {
/* 46 */     super.extractRenderState(entity, state, partialTicks);
/* 47 */     state.xRot = entity.getXRot(partialTicks);
/* 48 */     state.yRot = entity.getYRot(partialTicks);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/LlamaSpitRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */