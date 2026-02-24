/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import java.util.List;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.projectile.TridentModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ThrownTridentRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderType;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Unit;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class ThrownTridentRenderer extends EntityRenderer<ThrownTrident, ThrownTridentRenderState> {
/* 20 */   public static final Identifier TRIDENT_LOCATION = Identifier.withDefaultNamespace("textures/entity/trident.png");
/*    */   
/*    */   private final TridentModel model;
/*    */   
/*    */   public ThrownTridentRenderer(EntityRendererProvider.Context context) {
/* 25 */     super(context);
/* 26 */     this.model = new TridentModel(context.bakeLayer(ModelLayers.TRIDENT));
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(ThrownTridentRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 31 */     poseStack.pushPose();
/*    */     
/* 33 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(state.yRot - 90.0F));
/* 34 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(state.xRot + 90.0F));
/*    */     
/* 36 */     List<RenderType> renderTypes = ItemRenderer.getFoilRenderTypes(this.model.renderType(TRIDENT_LOCATION), false, state.isFoil);
/* 37 */     for (int i = 0; i < renderTypes.size(); i++) {
/* 38 */       submitNodeCollector.order(i).submitModel((net.minecraft.client.model.Model)this.model, Unit.INSTANCE, poseStack, renderTypes.get(i), state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
/*    */     }
/*    */     
/* 41 */     poseStack.popPose();
/*    */     
/* 43 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */ 
/*    */   
/*    */   public ThrownTridentRenderState createRenderState() {
/* 48 */     return new ThrownTridentRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(ThrownTrident entity, ThrownTridentRenderState state, float partialTicks) {
/* 53 */     super.extractRenderState(entity, state, partialTicks);
/* 54 */     state.yRot = entity.getYRot(partialTicks);
/* 55 */     state.xRot = entity.getXRot(partialTicks);
/* 56 */     state.isFoil = entity.isFoil();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ThrownTridentRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */