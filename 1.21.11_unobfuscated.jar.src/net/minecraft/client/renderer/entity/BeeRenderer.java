/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.animal.bee.BeeModel;
/*    */ import net.minecraft.client.renderer.entity.state.BeeRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.bee.Bee;
/*    */ 
/*    */ public class BeeRenderer extends AgeableMobRenderer<Bee, BeeRenderState, BeeModel> {
/* 11 */   private static final Identifier ANGRY_BEE_TEXTURE = Identifier.withDefaultNamespace("textures/entity/bee/bee_angry.png");
/* 12 */   private static final Identifier ANGRY_NECTAR_BEE_TEXTURE = Identifier.withDefaultNamespace("textures/entity/bee/bee_angry_nectar.png");
/* 13 */   private static final Identifier BEE_TEXTURE = Identifier.withDefaultNamespace("textures/entity/bee/bee.png");
/* 14 */   private static final Identifier NECTAR_BEE_TEXTURE = Identifier.withDefaultNamespace("textures/entity/bee/bee_nectar.png");
/*    */   
/*    */   public BeeRenderer(EntityRendererProvider.Context context) {
/* 17 */     super(context, new BeeModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.BEE)), new BeeModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.BEE_BABY)), 0.4F);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(BeeRenderState state) {
/* 22 */     if (state.isAngry) {
/* 23 */       if (state.hasNectar) {
/* 24 */         return ANGRY_NECTAR_BEE_TEXTURE;
/*    */       }
/* 26 */       return ANGRY_BEE_TEXTURE;
/* 27 */     }  if (state.hasNectar) {
/* 28 */       return NECTAR_BEE_TEXTURE;
/*    */     }
/* 30 */     return BEE_TEXTURE;
/*    */   }
/*    */ 
/*    */   
/*    */   public BeeRenderState createRenderState() {
/* 35 */     return new BeeRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Bee entity, BeeRenderState state, float partialTicks) {
/* 40 */     super.extractRenderState(entity, state, partialTicks);
/* 41 */     state.rollAmount = entity.getRollAmount(partialTicks);
/* 42 */     state.hasStinger = !entity.hasStung();
/* 43 */     state.isOnGround = (entity.onGround() && entity.getDeltaMovement().lengthSqr() < 1.0E-7D);
/* 44 */     state.isAngry = entity.isAngry();
/* 45 */     state.hasNectar = entity.hasNectar();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/BeeRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */