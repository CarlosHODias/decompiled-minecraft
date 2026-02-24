/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.animal.rabbit.RabbitModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.RabbitRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.animal.rabbit.Rabbit;
/*    */ 
/*    */ public class RabbitRenderer extends AgeableMobRenderer<Rabbit, RabbitRenderState, RabbitModel> {
/* 10 */   private static final Identifier RABBIT_BROWN_LOCATION = Identifier.withDefaultNamespace("textures/entity/rabbit/brown.png");
/* 11 */   private static final Identifier RABBIT_WHITE_LOCATION = Identifier.withDefaultNamespace("textures/entity/rabbit/white.png");
/* 12 */   private static final Identifier RABBIT_BLACK_LOCATION = Identifier.withDefaultNamespace("textures/entity/rabbit/black.png");
/* 13 */   private static final Identifier RABBIT_GOLD_LOCATION = Identifier.withDefaultNamespace("textures/entity/rabbit/gold.png");
/* 14 */   private static final Identifier RABBIT_SALT_LOCATION = Identifier.withDefaultNamespace("textures/entity/rabbit/salt.png");
/* 15 */   private static final Identifier RABBIT_WHITE_SPLOTCHED_LOCATION = Identifier.withDefaultNamespace("textures/entity/rabbit/white_splotched.png");
/* 16 */   private static final Identifier RABBIT_TOAST_LOCATION = Identifier.withDefaultNamespace("textures/entity/rabbit/toast.png");
/* 17 */   private static final Identifier RABBIT_EVIL_LOCATION = Identifier.withDefaultNamespace("textures/entity/rabbit/caerbannog.png");
/*    */   
/*    */   public RabbitRenderer(EntityRendererProvider.Context context) {
/* 20 */     super(context, new RabbitModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.RABBIT)), new RabbitModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.RABBIT_BABY)), 0.3F);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(RabbitRenderState state) {
/* 25 */     if (state.isToast) {
/* 26 */       return RABBIT_TOAST_LOCATION;
/*    */     }
/*    */     
/* 29 */     switch (state.variant) { default: throw new MatchException(null, null);case BROWN: case WHITE: case BLACK: case GOLD: case SALT: case WHITE_SPLOTCHED: case EVIL: break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 36 */       RABBIT_EVIL_LOCATION;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public RabbitRenderState createRenderState() {
/* 42 */     return new RabbitRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Rabbit entity, RabbitRenderState state, float partialTicks) {
/* 47 */     super.extractRenderState(entity, state, partialTicks);
/* 48 */     state.jumpCompletion = entity.getJumpCompletion(partialTicks);
/* 49 */     state.isToast = checkMagicName((net.minecraft.world.entity.Entity)entity, "Toast");
/* 50 */     state.variant = entity.getVariant();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/RabbitRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */