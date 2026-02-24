/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.strider.StriderModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.StriderRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Strider;
/*    */ 
/*    */ public class StriderRenderer extends AgeableMobRenderer<Strider, StriderRenderState, StriderModel> {
/* 13 */   private static final Identifier STRIDER_LOCATION = Identifier.withDefaultNamespace("textures/entity/strider/strider.png");
/* 14 */   private static final Identifier COLD_LOCATION = Identifier.withDefaultNamespace("textures/entity/strider/strider_cold.png");
/*    */   
/*    */   private static final float SHADOW_RADIUS = 0.5F;
/*    */   
/*    */   public StriderRenderer(EntityRendererProvider.Context context) {
/* 19 */     super(context, new StriderModel(context.bakeLayer(ModelLayers.STRIDER)), new StriderModel(context.bakeLayer(ModelLayers.STRIDER_BABY)), 0.5F);
/*    */     
/* 21 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<StriderRenderState, StriderModel>)new net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer(this, context.getEquipmentRenderer(), net.minecraft.client.resources.model.EquipmentClientInfo.LayerType.STRIDER_SADDLE, state -> state.saddle, (EntityModel)new StriderModel(
/*    */ 
/*    */             
/* 24 */             context.bakeLayer(ModelLayers.STRIDER_SADDLE)), (EntityModel)new StriderModel(
/* 25 */             context.bakeLayer(ModelLayers.STRIDER_BABY_SADDLE))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(StriderRenderState state) {
/* 31 */     return state.isSuffocating ? COLD_LOCATION : STRIDER_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getShadowRadius(StriderRenderState state) {
/* 36 */     float radius = super.getShadowRadius(state);
/* 37 */     if (state.isBaby)
/*    */     {
/* 39 */       return radius * 0.5F;
/*    */     }
/* 41 */     return radius;
/*    */   }
/*    */ 
/*    */   
/*    */   public StriderRenderState createRenderState() {
/* 46 */     return new StriderRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Strider entity, StriderRenderState state, float partialTicks) {
/* 51 */     super.extractRenderState(entity, state, partialTicks);
/* 52 */     state.saddle = entity.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.SADDLE).copy();
/* 53 */     state.isSuffocating = entity.isSuffocating();
/* 54 */     state.isRidden = entity.isVehicle();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isShaking(StriderRenderState state) {
/* 59 */     return (super.isShaking(state) || state.isSuffocating);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/StriderRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */