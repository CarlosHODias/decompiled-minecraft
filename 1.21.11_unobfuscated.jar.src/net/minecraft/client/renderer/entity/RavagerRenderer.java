/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.monster.ravager.RavagerModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.RavagerRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Ravager;
/*    */ 
/*    */ public class RavagerRenderer extends MobRenderer<Ravager, RavagerRenderState, RavagerModel> {
/* 11 */   private static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/illager/ravager.png");
/*    */   
/*    */   public RavagerRenderer(EntityRendererProvider.Context context) {
/* 14 */     super(context, new RavagerModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.RAVAGER)), 1.1F);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(RavagerRenderState state) {
/* 19 */     return TEXTURE_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public RavagerRenderState createRenderState() {
/* 24 */     return new RavagerRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Ravager entity, RavagerRenderState state, float partialTicks) {
/* 29 */     super.extractRenderState(entity, state, partialTicks);
/* 30 */     state.stunnedTicksRemaining = (entity.getStunnedTick() > 0.0F) ? (entity.getStunnedTick() - partialTicks) : 0.0F;
/* 31 */     state.attackTicksRemaining = (entity.getAttackTick() > 0.0F) ? (entity.getAttackTick() - partialTicks) : 0.0F;
/*    */     
/* 33 */     if (entity.getRoarTick() > 0) {
/* 34 */       state.roarAnimation = ((20 - entity.getRoarTick()) + partialTicks) / 20.0F;
/*    */     } else {
/* 36 */       state.roarAnimation = 0.0F;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/RavagerRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */