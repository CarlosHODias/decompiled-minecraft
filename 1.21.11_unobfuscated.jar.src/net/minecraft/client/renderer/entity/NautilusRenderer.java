/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.animal.nautilus.NautilusModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.NautilusRenderState;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.nautilus.AbstractNautilus;
/*    */ 
/*    */ public class NautilusRenderer<T extends AbstractNautilus> extends AgeableMobRenderer<T, NautilusRenderState, NautilusModel> {
/* 15 */   private static final Identifier NAUTILUS_LOCATION = Identifier.withDefaultNamespace("textures/entity/nautilus/nautilus.png");
/* 16 */   private static final Identifier NAUTILUS_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/nautilus/nautilus_baby.png");
/*    */   
/*    */   public NautilusRenderer(EntityRendererProvider.Context context) {
/* 19 */     super(context, new NautilusModel(
/* 20 */           context.bakeLayer(ModelLayers.NAUTILUS)), new NautilusModel(
/* 21 */           context.bakeLayer(ModelLayers.NAUTILUS_BABY)), 0.7F);
/* 22 */     addLayer((RenderLayer<NautilusRenderState, NautilusModel>)new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.NAUTILUS_BODY, state -> state.bodyArmorItem, (net.minecraft.client.model.EntityModel)new net.minecraft.client.model.animal.nautilus.NautilusArmorModel(
/*    */ 
/*    */             
/* 25 */             context.bakeLayer(ModelLayers.NAUTILUS_ARMOR)), null));
/*    */ 
/*    */     
/* 28 */     addLayer((RenderLayer<NautilusRenderState, NautilusModel>)new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.NAUTILUS_SADDLE, state -> state.saddle, (net.minecraft.client.model.EntityModel)new net.minecraft.client.model.animal.nautilus.NautilusSaddleModel(
/*    */ 
/*    */             
/* 31 */             context.bakeLayer(ModelLayers.NAUTILUS_SADDLE)), null));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(NautilusRenderState state) {
/* 38 */     return state.isBaby ? NAUTILUS_BABY_LOCATION : NAUTILUS_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public NautilusRenderState createRenderState() {
/* 43 */     return new NautilusRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(T entity, NautilusRenderState state, float partialTicks) {
/* 48 */     super.extractRenderState(entity, state, partialTicks);
/* 49 */     state.saddle = entity.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.SADDLE).copy();
/* 50 */     state.bodyArmorItem = entity.getBodyArmorItem().copy();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/NautilusRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */