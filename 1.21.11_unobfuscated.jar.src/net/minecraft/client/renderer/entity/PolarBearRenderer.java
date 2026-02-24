/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.animal.polarbear.PolarBearModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.PolarBearRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.animal.polarbear.PolarBear;
/*    */ 
/*    */ public class PolarBearRenderer extends AgeableMobRenderer<PolarBear, PolarBearRenderState, PolarBearModel> {
/* 10 */   private static final Identifier BEAR_LOCATION = Identifier.withDefaultNamespace("textures/entity/bear/polarbear.png");
/*    */   
/*    */   public PolarBearRenderer(EntityRendererProvider.Context context) {
/* 13 */     super(context, new PolarBearModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.POLAR_BEAR)), new PolarBearModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.POLAR_BEAR_BABY)), 0.9F);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(PolarBearRenderState state) {
/* 18 */     return BEAR_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public PolarBearRenderState createRenderState() {
/* 23 */     return new PolarBearRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(PolarBear entity, PolarBearRenderState state, float partialTicks) {
/* 28 */     super.extractRenderState(entity, state, partialTicks);
/* 29 */     state.standScale = entity.getStandingAnimationScale(partialTicks);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/PolarBearRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */