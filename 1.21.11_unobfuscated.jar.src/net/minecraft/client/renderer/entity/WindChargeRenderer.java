/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.object.projectile.WindChargeModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge;
/*    */ 
/*    */ public class WindChargeRenderer extends EntityRenderer<AbstractWindCharge, EntityRenderState> {
/* 16 */   private static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/projectiles/wind_charge.png");
/*    */   
/*    */   private final WindChargeModel model;
/*    */   
/*    */   public WindChargeRenderer(EntityRendererProvider.Context context) {
/* 21 */     super(context);
/* 22 */     this.model = new WindChargeModel(context.bakeLayer(ModelLayers.WIND_CHARGE));
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 27 */     submitNodeCollector.submitModel((Model)this.model, state, poseStack, RenderTypes.breezeWind(TEXTURE_LOCATION, xOffset(state.ageInTicks) % 1.0F, 0.0F), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/*    */     
/* 29 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */   
/*    */   protected float xOffset(float t) {
/* 33 */     return t * 0.03F;
/*    */   }
/*    */ 
/*    */   
/*    */   public EntityRenderState createRenderState() {
/* 38 */     return new EntityRenderState();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/WindChargeRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */