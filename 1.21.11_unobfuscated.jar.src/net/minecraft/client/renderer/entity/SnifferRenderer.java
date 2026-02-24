/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.animal.sniffer.SnifferModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SnifferRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.sniffer.Sniffer;
/*    */ 
/*    */ public class SnifferRenderer extends AgeableMobRenderer<Sniffer, SnifferRenderState, SnifferModel> {
/* 12 */   private static final Identifier SNIFFER_LOCATION = Identifier.withDefaultNamespace("textures/entity/sniffer/sniffer.png");
/*    */   
/*    */   public SnifferRenderer(EntityRendererProvider.Context context) {
/* 15 */     super(context, new SnifferModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.SNIFFER)), new SnifferModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.SNIFFER_BABY)), 1.1F);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(SnifferRenderState state) {
/* 20 */     return SNIFFER_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public SnifferRenderState createRenderState() {
/* 25 */     return new SnifferRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Sniffer entity, SnifferRenderState state, float partialTicks) {
/* 30 */     super.extractRenderState(entity, state, partialTicks);
/* 31 */     state.isSearching = entity.isSearching();
/* 32 */     state.diggingAnimationState.copyFrom(entity.diggingAnimationState);
/* 33 */     state.sniffingAnimationState.copyFrom(entity.sniffingAnimationState);
/* 34 */     state.risingAnimationState.copyFrom(entity.risingAnimationState);
/* 35 */     state.feelingHappyAnimationState.copyFrom(entity.feelingHappyAnimationState);
/* 36 */     state.scentingAnimationState.copyFrom(entity.scentingAnimationState);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected net.minecraft.world.phys.AABB getBoundingBoxForCulling(Sniffer entity) {
/* 42 */     return super.getBoundingBoxForCulling(entity).inflate(0.6000000238418579D);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/SnifferRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */