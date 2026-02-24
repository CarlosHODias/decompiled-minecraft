/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.animal.feline.CatModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.CatRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.feline.Cat;
/*    */ 
/*    */ public class CatRenderer extends AgeableMobRenderer<Cat, CatRenderState, CatModel> {
/*    */   public CatRenderer(EntityRendererProvider.Context context) {
/* 15 */     super(context, new CatModel(context.bakeLayer(ModelLayers.CAT)), new CatModel(context.bakeLayer(ModelLayers.CAT_BABY)), 0.4F);
/*    */     
/* 17 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<CatRenderState, CatModel>)new net.minecraft.client.renderer.entity.layers.CatCollarLayer(this, context.getModelSet()));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(CatRenderState state) {
/* 22 */     return state.texture;
/*    */   }
/*    */ 
/*    */   
/*    */   public CatRenderState createRenderState() {
/* 27 */     return new CatRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Cat entity, CatRenderState state, float partialTicks) {
/* 32 */     super.extractRenderState(entity, state, partialTicks);
/* 33 */     state.texture = ((net.minecraft.world.entity.animal.feline.CatVariant)entity.getVariant().value()).assetInfo().texturePath();
/* 34 */     state.isCrouching = entity.isCrouching();
/* 35 */     state.isSprinting = entity.isSprinting();
/* 36 */     state.isSitting = entity.isInSittingPose();
/* 37 */     state.lieDownAmount = entity.getLieDownAmount(partialTicks);
/* 38 */     state.lieDownAmountTail = entity.getLieDownAmountTail(partialTicks);
/* 39 */     state.relaxStateOneAmount = entity.getRelaxStateOneAmount(partialTicks);
/* 40 */     state.isLyingOnTopOfSleepingPlayer = entity.isLyingOnTopOfSleepingPlayer();
/* 41 */     state.collarColor = entity.isTame() ? entity.getCollarColor() : null;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setupRotations(CatRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 46 */     super.setupRotations(state, poseStack, bodyRot, entityScale);
/*    */     
/* 48 */     float lieDownAmount = state.lieDownAmount;
/* 49 */     if (lieDownAmount > 0.0F) {
/* 50 */       poseStack.translate(0.4F * lieDownAmount, 0.15F * lieDownAmount, 0.1F * lieDownAmount);
/* 51 */       poseStack.mulPose((org.joml.Quaternionfc)com.mojang.math.Axis.ZP.rotationDegrees(net.minecraft.util.Mth.rotLerp(lieDownAmount, 0.0F, 90.0F)));
/*    */ 
/*    */       
/* 54 */       if (state.isLyingOnTopOfSleepingPlayer)
/* 55 */         poseStack.translate(0.15F * lieDownAmount, 0.0F, 0.0F); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/CatRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */