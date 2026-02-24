/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.animal.turtle.TurtleModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.TurtleRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.animal.turtle.Turtle;
/*    */ 
/*    */ public class TurtleRenderer extends AgeableMobRenderer<Turtle, TurtleRenderState, TurtleModel> {
/* 10 */   private static final Identifier TURTLE_LOCATION = Identifier.withDefaultNamespace("textures/entity/turtle/big_sea_turtle.png");
/*    */   
/*    */   public TurtleRenderer(EntityRendererProvider.Context context) {
/* 13 */     super(context, new TurtleModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.TURTLE)), new TurtleModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.TURTLE_BABY)), 0.7F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getShadowRadius(TurtleRenderState state) {
/* 18 */     float radius = super.getShadowRadius(state);
/* 19 */     if (state.isBaby)
/*    */     {
/* 21 */       return radius * 0.83F;
/*    */     }
/* 23 */     return radius;
/*    */   }
/*    */ 
/*    */   
/*    */   public TurtleRenderState createRenderState() {
/* 28 */     return new TurtleRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Turtle entity, TurtleRenderState state, float partialTicks) {
/* 33 */     super.extractRenderState(entity, state, partialTicks);
/* 34 */     state.isOnLand = (!entity.isInWater() && entity.onGround());
/* 35 */     state.isLayingEgg = entity.isLayingEgg();
/* 36 */     state.hasEgg = (!entity.isBaby() && entity.hasEgg());
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(TurtleRenderState state) {
/* 41 */     return TURTLE_LOCATION;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/TurtleRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */