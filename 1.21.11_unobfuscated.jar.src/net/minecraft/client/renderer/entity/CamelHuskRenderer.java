/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.animal.camel.CamelModel;
/*    */ import net.minecraft.client.model.animal.camel.CamelSaddleModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
/*    */ import net.minecraft.client.renderer.entity.state.CamelRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class CamelHuskRenderer extends CamelRenderer {
/* 12 */   private static final Identifier CAMEL_HUSK_LOCATION = Identifier.withDefaultNamespace("textures/entity/camel/camel_husk.png");
/*    */   
/*    */   public CamelHuskRenderer(EntityRendererProvider.Context context) {
/* 15 */     super(context);
/*    */   }
/*    */ 
/*    */   
/*    */   protected SimpleEquipmentLayer<CamelRenderState, CamelModel, CamelSaddleModel> createCamelSaddleLayer(EntityRendererProvider.Context context) {
/* 20 */     return new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), net.minecraft.client.resources.model.EquipmentClientInfo.LayerType.CAMEL_HUSK_SADDLE, state -> state.saddle, (EntityModel)new CamelSaddleModel(
/*    */ 
/*    */           
/* 23 */           context.bakeLayer(ModelLayers.CAMEL_HUSK_SADDLE)), (EntityModel)new CamelSaddleModel(
/* 24 */           context.bakeLayer(ModelLayers.CAMEL_HUSK_BABY_SADDLE)));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(CamelRenderState state) {
/* 30 */     return CAMEL_HUSK_LOCATION;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/CamelHuskRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */