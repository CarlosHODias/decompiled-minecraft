/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.animal.wolf.WolfModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.WolfRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.wolf.Wolf;
/*    */ 
/*    */ public class WolfRenderer extends AgeableMobRenderer<Wolf, WolfRenderState, WolfModel> {
/*    */   public WolfRenderer(EntityRendererProvider.Context context) {
/* 15 */     super(context, new WolfModel(context.bakeLayer(ModelLayers.WOLF)), new WolfModel(context.bakeLayer(ModelLayers.WOLF_BABY)), 0.5F);
/*    */     
/* 17 */     addLayer((RenderLayer<WolfRenderState, WolfModel>)new net.minecraft.client.renderer.entity.layers.WolfArmorLayer(this, context.getModelSet(), context.getEquipmentRenderer()));
/* 18 */     addLayer((RenderLayer<WolfRenderState, WolfModel>)new net.minecraft.client.renderer.entity.layers.WolfCollarLayer(this));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected int getModelTint(WolfRenderState state) {
/* 24 */     float wetShade = state.wetShade;
/* 25 */     if (wetShade == 1.0F) {
/* 26 */       return -1;
/*    */     }
/* 28 */     return net.minecraft.util.ARGB.colorFromFloat(1.0F, wetShade, wetShade, wetShade);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(WolfRenderState state) {
/* 33 */     return state.texture;
/*    */   }
/*    */ 
/*    */   
/*    */   public WolfRenderState createRenderState() {
/* 38 */     return new WolfRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Wolf entity, WolfRenderState state, float partialTicks) {
/* 43 */     super.extractRenderState(entity, state, partialTicks);
/* 44 */     state.isAngry = entity.isAngry();
/* 45 */     state.isSitting = entity.isInSittingPose();
/* 46 */     state.tailAngle = entity.getTailAngle();
/* 47 */     state.headRollAngle = entity.getHeadRollAngle(partialTicks);
/* 48 */     state.shakeAnim = entity.getShakeAnim(partialTicks);
/* 49 */     state.texture = entity.getTexture();
/* 50 */     state.wetShade = entity.getWetShade(partialTicks);
/* 51 */     state.collarColor = entity.isTame() ? entity.getCollarColor() : null;
/* 52 */     state.bodyArmorItem = entity.getBodyArmorItem().copy();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/WolfRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */