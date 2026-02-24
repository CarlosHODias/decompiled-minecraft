/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.GhastRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Ghast;
/*    */ 
/*    */ public class GhastRenderer extends MobRenderer<Ghast, GhastRenderState, net.minecraft.client.model.monster.ghast.GhastModel> {
/* 10 */   private static final Identifier GHAST_LOCATION = Identifier.withDefaultNamespace("textures/entity/ghast/ghast.png");
/* 11 */   private static final Identifier GHAST_SHOOTING_LOCATION = Identifier.withDefaultNamespace("textures/entity/ghast/ghast_shooting.png");
/*    */   
/*    */   public GhastRenderer(EntityRendererProvider.Context context) {
/* 14 */     super(context, new net.minecraft.client.model.monster.ghast.GhastModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.GHAST)), 1.5F);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(GhastRenderState state) {
/* 19 */     if (state.isCharging) {
/* 20 */       return GHAST_SHOOTING_LOCATION;
/*    */     }
/* 22 */     return GHAST_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public GhastRenderState createRenderState() {
/* 27 */     return new GhastRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Ghast entity, GhastRenderState state, float partialTicks) {
/* 32 */     super.extractRenderState(entity, state, partialTicks);
/* 33 */     state.isCharging = entity.isCharging();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/GhastRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */