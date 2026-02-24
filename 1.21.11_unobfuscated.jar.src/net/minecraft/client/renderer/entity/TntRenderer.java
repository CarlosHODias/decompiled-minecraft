/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.TntRenderState;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.item.PrimedTnt;
/*    */ import org.joml.Quaternionfc;
/*    */ 
/*    */ public class TntRenderer extends EntityRenderer<PrimedTnt, TntRenderState> {
/*    */   public TntRenderer(EntityRendererProvider.Context context) {
/* 13 */     super(context);
/* 14 */     this.shadowRadius = 0.5F;
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(TntRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 19 */     poseStack.pushPose();
/* 20 */     poseStack.translate(0.0F, 0.5F, 0.0F);
/* 21 */     float fuse = state.fuseRemainingInTicks;
/* 22 */     if (state.fuseRemainingInTicks < 10.0F) {
/* 23 */       float g = 1.0F - state.fuseRemainingInTicks / 10.0F;
/* 24 */       g = net.minecraft.util.Mth.clamp(g, 0.0F, 1.0F);
/* 25 */       g *= g;
/* 26 */       g *= g;
/* 27 */       float s = 1.0F + g * 0.3F;
/* 28 */       poseStack.scale(s, s, s);
/*    */     } 
/*    */     
/* 31 */     poseStack.mulPose((Quaternionfc)com.mojang.math.Axis.YP.rotationDegrees(-90.0F));
/* 32 */     poseStack.translate(-0.5F, -0.5F, 0.5F);
/* 33 */     poseStack.mulPose((Quaternionfc)com.mojang.math.Axis.YP.rotationDegrees(90.0F));
/*    */     
/* 35 */     if (state.blockState != null) {
/* 36 */       TntMinecartRenderer.submitWhiteSolidBlock(state.blockState, poseStack, submitNodeCollector, state.lightCoords, ((int)fuse / 5 % 2 == 0), state.outlineColor);
/*    */     }
/*    */     
/* 39 */     poseStack.popPose();
/*    */     
/* 41 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */ 
/*    */   
/*    */   public TntRenderState createRenderState() {
/* 46 */     return new TntRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(PrimedTnt entity, TntRenderState state, float partialTicks) {
/* 51 */     super.extractRenderState(entity, state, partialTicks);
/*    */ 
/*    */     
/* 54 */     state.fuseRemainingInTicks = entity.getFuse() - partialTicks + 1.0F;
/* 55 */     state.blockState = entity.getBlockState();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/TntRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */