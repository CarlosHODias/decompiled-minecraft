/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import com.mojang.math.Axis;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.model.player.PlayerModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.LivingEntityRenderer;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.AvatarRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public abstract class StuckInBodyLayer<M extends PlayerModel, S> extends RenderLayer<AvatarRenderState, M> {
/*    */   private final Model<S> model;
/*    */   private final S modelState;
/*    */   
/*    */   public StuckInBodyLayer(LivingEntityRenderer<?, AvatarRenderState, M> renderer, Model<S> model, S modelState, Identifier texture, PlacementStyle placementStyle) {
/* 23 */     super((RenderLayerParent)renderer);
/* 24 */     this.model = model;
/* 25 */     this.modelState = modelState;
/* 26 */     this.texture = texture;
/* 27 */     this.placementStyle = placementStyle;
/*    */   }
/*    */   private final Identifier texture;
/*    */   private final PlacementStyle placementStyle;
/*    */   
/*    */   private void submitStuckItem(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float directionX, float directionY, float directionZ, int outlineColor) {
/* 33 */     float directionXZ = Mth.sqrt(directionX * directionX + directionZ * directionZ);
/* 34 */     float yRot = (float)(Math.atan2(directionX, directionZ) * 57.2957763671875D);
/* 35 */     float xRot = (float)(Math.atan2(directionY, directionXZ) * 57.2957763671875D);
/*    */     
/* 37 */     poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(yRot - 90.0F));
/* 38 */     poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(xRot));
/*    */     
/* 40 */     submitNodeCollector.submitModel(this.model, this.modelState, poseStack, this.model.renderType(this.texture), lightCoords, OverlayTexture.NO_OVERLAY, outlineColor, null);
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
/* 45 */     int count = numStuck(state);
/* 46 */     if (count <= 0) {
/*    */       return;
/*    */     }
/*    */     
/* 50 */     RandomSource random = RandomSource.create(state.id);
/* 51 */     for (int i = 0; i < count; i++) {
/* 52 */       poseStack.pushPose();
/* 53 */       ModelPart modelPart = ((PlayerModel)getParentModel()).getRandomBodyPart(random);
/* 54 */       ModelPart.Cube cube = modelPart.getRandomCube(random);
/* 55 */       modelPart.translateAndRotate(poseStack);
/*    */       
/* 57 */       float midX = random.nextFloat();
/* 58 */       float midY = random.nextFloat();
/* 59 */       float midZ = random.nextFloat();
/* 60 */       if (this.placementStyle == PlacementStyle.ON_SURFACE) {
/* 61 */         int plane = random.nextInt(3);
/* 62 */         switch (plane) { case 0:
/* 63 */             midX = snapToFace(midX); break;
/* 64 */           case 1: midY = snapToFace(midY); break;
/* 65 */           default: midZ = snapToFace(midZ);
/*    */             break; }
/*    */       
/*    */       } 
/* 69 */       poseStack.translate(
/* 70 */           Mth.lerp(midX, cube.minX, cube.maxX) / 16.0F, 
/* 71 */           Mth.lerp(midY, cube.minY, cube.maxY) / 16.0F, 
/* 72 */           Mth.lerp(midZ, cube.minZ, cube.maxZ) / 16.0F);
/*    */ 
/*    */       
/* 75 */       submitStuckItem(poseStack, submitNodeCollector, lightCoords, -(midX * 2.0F - 1.0F), -(midY * 2.0F - 1.0F), -(midZ * 2.0F - 1.0F), state.outlineColor);
/*    */       
/* 77 */       poseStack.popPose();
/*    */     } 
/*    */   }
/*    */   
/*    */   private static float snapToFace(float value) {
/* 82 */     return (value > 0.5F) ? 1.0F : 0.5F;
/*    */   }
/*    */   protected abstract int numStuck(AvatarRenderState paramAvatarRenderState);
/*    */   
/* 86 */   public enum PlacementStyle { IN_CUBE,
/* 87 */     ON_SURFACE; }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/StuckInBodyLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */