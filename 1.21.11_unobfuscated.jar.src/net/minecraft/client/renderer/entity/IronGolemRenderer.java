/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.animal.golem.IronGolemModel;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.IronGolemRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*    */ 
/*    */ public class IronGolemRenderer extends MobRenderer<IronGolem, IronGolemRenderState, IronGolemModel> {
/* 14 */   private static final Identifier GOLEM_LOCATION = Identifier.withDefaultNamespace("textures/entity/iron_golem/iron_golem.png");
/*    */   
/*    */   public IronGolemRenderer(EntityRendererProvider.Context context) {
/* 17 */     super(context, new IronGolemModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.IRON_GOLEM)), 0.7F);
/* 18 */     addLayer((RenderLayer<IronGolemRenderState, IronGolemModel>)new net.minecraft.client.renderer.entity.layers.IronGolemCrackinessLayer(this));
/* 19 */     addLayer((RenderLayer<IronGolemRenderState, IronGolemModel>)new net.minecraft.client.renderer.entity.layers.IronGolemFlowerLayer(this));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(IronGolemRenderState state) {
/* 24 */     return GOLEM_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public IronGolemRenderState createRenderState() {
/* 29 */     return new IronGolemRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(IronGolem entity, IronGolemRenderState state, float partialTicks) {
/* 34 */     super.extractRenderState(entity, state, partialTicks);
/* 35 */     state.attackTicksRemaining = (entity.getAttackAnimationTick() > 0.0F) ? (entity.getAttackAnimationTick() - partialTicks) : 0.0F;
/* 36 */     state.offerFlowerTick = entity.getOfferFlowerTick();
/* 37 */     state.crackiness = entity.getCrackiness();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setupRotations(IronGolemRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 42 */     super.setupRotations(state, poseStack, bodyRot, entityScale);
/* 43 */     if (state.walkAnimationSpeed < 0.01D) {
/*    */       return;
/*    */     }
/*    */     
/* 47 */     float p = 13.0F;
/* 48 */     float wp = state.walkAnimationPos + 6.0F;
/* 49 */     float triangleWave = (Math.abs(wp % 13.0F - 6.5F) - 3.25F) / 3.25F;
/* 50 */     poseStack.mulPose((org.joml.Quaternionfc)com.mojang.math.Axis.ZP.rotationDegrees(6.5F * triangleWave));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/IronGolemRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */