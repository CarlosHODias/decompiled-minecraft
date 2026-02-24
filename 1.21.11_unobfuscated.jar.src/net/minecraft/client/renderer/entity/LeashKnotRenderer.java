/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.leash.LeashKnotModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
/*    */ 
/*    */ public class LeashKnotRenderer extends EntityRenderer<LeashFenceKnotEntity, EntityRenderState> {
/* 14 */   private static final Identifier KNOT_LOCATION = Identifier.withDefaultNamespace("textures/entity/lead_knot.png");
/*    */   
/*    */   private final LeashKnotModel model;
/*    */   
/*    */   public LeashKnotRenderer(EntityRendererProvider.Context context) {
/* 19 */     super(context);
/* 20 */     this.model = new LeashKnotModel(context.bakeLayer(ModelLayers.LEASH_KNOT));
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 25 */     poseStack.pushPose();
/*    */     
/* 27 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/*    */     
/* 29 */     submitNodeCollector.submitModel((Model)this.model, state, poseStack, this.model.renderType(KNOT_LOCATION), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/*    */     
/* 31 */     poseStack.popPose();
/*    */     
/* 33 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityRenderState createRenderState() {
/* 38 */     return new EntityRenderState();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/LeashKnotRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */