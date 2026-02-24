/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.animal.goat.GoatModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.GoatRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.animal.goat.Goat;
/*    */ 
/*    */ public class GoatRenderer extends AgeableMobRenderer<Goat, GoatRenderState, GoatModel> {
/* 10 */   private static final Identifier GOAT_LOCATION = Identifier.withDefaultNamespace("textures/entity/goat/goat.png");
/*    */   
/*    */   public GoatRenderer(EntityRendererProvider.Context context) {
/* 13 */     super(context, new GoatModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.GOAT)), new GoatModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.GOAT_BABY)), 0.7F);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(GoatRenderState state) {
/* 18 */     return GOAT_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public GoatRenderState createRenderState() {
/* 23 */     return new GoatRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Goat entity, GoatRenderState state, float partialTicks) {
/* 28 */     super.extractRenderState(entity, state, partialTicks);
/* 29 */     state.hasLeftHorn = entity.hasLeftHorn();
/* 30 */     state.hasRightHorn = entity.hasRightHorn();
/* 31 */     state.rammingXHeadRot = entity.getRammingXHeadRot();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/GoatRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */