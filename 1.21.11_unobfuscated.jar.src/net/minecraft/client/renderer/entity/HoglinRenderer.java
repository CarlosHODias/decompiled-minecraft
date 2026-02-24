/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.renderer.entity.state.HoglinRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.hoglin.Hoglin;
/*    */ 
/*    */ public class HoglinRenderer extends AbstractHoglinRenderer<Hoglin> {
/*  9 */   private static final Identifier HOGLIN_LOCATION = Identifier.withDefaultNamespace("textures/entity/hoglin/hoglin.png");
/*    */   
/*    */   public HoglinRenderer(EntityRendererProvider.Context context) {
/* 12 */     super(context, net.minecraft.client.model.geom.ModelLayers.HOGLIN, net.minecraft.client.model.geom.ModelLayers.HOGLIN_BABY, 0.7F);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(HoglinRenderState state) {
/* 17 */     return HOGLIN_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Hoglin entity, HoglinRenderState state, float partialTicks) {
/* 22 */     super.extractRenderState(entity, state, partialTicks);
/* 23 */     state.isConverting = entity.isConverting();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isShaking(HoglinRenderState state) {
/* 28 */     return (super.isShaking(state) || state.isConverting);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/HoglinRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */