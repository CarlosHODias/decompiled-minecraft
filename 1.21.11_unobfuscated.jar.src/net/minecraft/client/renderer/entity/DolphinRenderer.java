/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.animal.dolphin.DolphinModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.DolphinRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.dolphin.Dolphin;
/*    */ 
/*    */ public class DolphinRenderer extends AgeableMobRenderer<Dolphin, DolphinRenderState, DolphinModel> {
/* 13 */   private static final Identifier DOLPHIN_LOCATION = Identifier.withDefaultNamespace("textures/entity/dolphin.png");
/*    */   
/*    */   public DolphinRenderer(EntityRendererProvider.Context context) {
/* 16 */     super(context, new DolphinModel(context.bakeLayer(ModelLayers.DOLPHIN)), new DolphinModel(context.bakeLayer(ModelLayers.DOLPHIN_BABY)), 0.7F);
/*    */     
/* 18 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<DolphinRenderState, DolphinModel>)new net.minecraft.client.renderer.entity.layers.DolphinCarryingItemLayer(this));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(DolphinRenderState state) {
/* 23 */     return DOLPHIN_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public DolphinRenderState createRenderState() {
/* 28 */     return new DolphinRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Dolphin entity, DolphinRenderState state, float partialTicks) {
/* 33 */     super.extractRenderState(entity, state, partialTicks);
/* 34 */     net.minecraft.client.renderer.entity.state.HoldingEntityRenderState.extractHoldingEntityRenderState((LivingEntity)entity, (net.minecraft.client.renderer.entity.state.HoldingEntityRenderState)state, this.itemModelResolver);
/* 35 */     state.isMoving = (entity.getDeltaMovement().horizontalDistanceSqr() > 1.0E-7D);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/DolphinRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */