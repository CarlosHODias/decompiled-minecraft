/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.monster.phantom.PhantomModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.PhantomRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Phantom;
/*    */ 
/*    */ public class PhantomRenderer extends MobRenderer<Phantom, PhantomRenderState, PhantomModel> {
/* 13 */   private static final Identifier PHANTOM_LOCATION = Identifier.withDefaultNamespace("textures/entity/phantom.png");
/*    */   
/*    */   public PhantomRenderer(EntityRendererProvider.Context context) {
/* 16 */     super(context, new PhantomModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.PHANTOM)), 0.75F);
/*    */     
/* 18 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<PhantomRenderState, PhantomModel>)new net.minecraft.client.renderer.entity.layers.PhantomEyesLayer(this));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(PhantomRenderState state) {
/* 23 */     return PHANTOM_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public PhantomRenderState createRenderState() {
/* 28 */     return new PhantomRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Phantom entity, PhantomRenderState state, float partialTicks) {
/* 33 */     super.extractRenderState(entity, state, partialTicks);
/* 34 */     state.flapTime = entity.getUniqueFlapTickOffset() + state.ageInTicks;
/* 35 */     state.size = entity.getPhantomSize();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void scale(PhantomRenderState state, PoseStack poseStack) {
/* 40 */     float scale = 1.0F + 0.15F * state.size;
/* 41 */     poseStack.scale(scale, scale, scale);
/*    */     
/* 43 */     poseStack.translate(0.0F, 1.3125F, 0.1875F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setupRotations(PhantomRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 48 */     super.setupRotations(state, poseStack, bodyRot, entityScale);
/* 49 */     poseStack.mulPose((org.joml.Quaternionfc)com.mojang.math.Axis.XP.rotationDegrees(state.xRot));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/PhantomRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */