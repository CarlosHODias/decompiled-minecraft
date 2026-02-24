/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.animal.camel.CamelModel;
/*    */ import net.minecraft.client.model.animal.camel.CamelSaddleModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
/*    */ import net.minecraft.client.renderer.entity.state.CamelRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.camel.Camel;
/*    */ 
/*    */ public class CamelRenderer extends AgeableMobRenderer<Camel, CamelRenderState, CamelModel> {
/* 14 */   private static final Identifier CAMEL_LOCATION = Identifier.withDefaultNamespace("textures/entity/camel/camel.png");
/*    */   
/*    */   public CamelRenderer(EntityRendererProvider.Context context) {
/* 17 */     super(context, new CamelModel(context.bakeLayer(ModelLayers.CAMEL)), new CamelModel(context.bakeLayer(ModelLayers.CAMEL_BABY)), 0.7F);
/* 18 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer)createCamelSaddleLayer(context));
/*    */   }
/*    */   
/*    */   protected SimpleEquipmentLayer<CamelRenderState, CamelModel, CamelSaddleModel> createCamelSaddleLayer(EntityRendererProvider.Context context) {
/* 22 */     return new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), net.minecraft.client.resources.model.EquipmentClientInfo.LayerType.CAMEL_SADDLE, state -> state.saddle, (net.minecraft.client.model.EntityModel)new CamelSaddleModel(
/*    */ 
/*    */           
/* 25 */           context.bakeLayer(ModelLayers.CAMEL_SADDLE)), (net.minecraft.client.model.EntityModel)new CamelSaddleModel(
/* 26 */           context.bakeLayer(ModelLayers.CAMEL_BABY_SADDLE)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(CamelRenderState state) {
/* 32 */     return CAMEL_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public CamelRenderState createRenderState() {
/* 37 */     return new CamelRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Camel entity, CamelRenderState state, float partialTicks) {
/* 42 */     super.extractRenderState(entity, state, partialTicks);
/* 43 */     state.saddle = entity.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.SADDLE).copy();
/* 44 */     state.isRidden = entity.isVehicle();
/*    */     
/* 46 */     state.jumpCooldown = Math.max(entity.getJumpCooldown() - partialTicks, 0.0F);
/* 47 */     state.sitAnimationState.copyFrom(entity.sitAnimationState);
/* 48 */     state.sitPoseAnimationState.copyFrom(entity.sitPoseAnimationState);
/* 49 */     state.sitUpAnimationState.copyFrom(entity.sitUpAnimationState);
/* 50 */     state.idleAnimationState.copyFrom(entity.idleAnimationState);
/* 51 */     state.dashAnimationState.copyFrom(entity.dashAnimationState);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/CamelRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */