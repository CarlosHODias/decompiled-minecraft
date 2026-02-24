/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.renderer.entity.state.BatRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ambient.Bat;
/*    */ 
/*    */ public class BatRenderer extends MobRenderer<Bat, BatRenderState, net.minecraft.client.model.ambient.BatModel> {
/* 10 */   private static final Identifier BAT_LOCATION = Identifier.withDefaultNamespace("textures/entity/bat.png");
/*    */   
/*    */   public BatRenderer(EntityRendererProvider.Context context) {
/* 13 */     super(context, new net.minecraft.client.model.ambient.BatModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.BAT)), 0.25F);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(BatRenderState state) {
/* 18 */     return BAT_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public BatRenderState createRenderState() {
/* 23 */     return new BatRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Bat entity, BatRenderState state, float partialTicks) {
/* 28 */     super.extractRenderState(entity, state, partialTicks);
/* 29 */     state.isResting = entity.isResting();
/* 30 */     state.flyAnimationState.copyFrom(entity.flyAnimationState);
/* 31 */     state.restAnimationState.copyFrom(entity.restAnimationState);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/BatRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */