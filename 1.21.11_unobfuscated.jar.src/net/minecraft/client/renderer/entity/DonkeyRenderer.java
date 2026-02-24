/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.animal.equine.DonkeyModel;
/*    */ import net.minecraft.client.model.animal.equine.EquineSaddleModel;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.DonkeyRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
/*    */ 
/*    */ public class DonkeyRenderer<T extends AbstractChestedHorse> extends AbstractHorseRenderer<T, DonkeyRenderState, DonkeyModel> {
/*    */   public DonkeyRenderer(EntityRendererProvider.Context context, Type type) {
/* 18 */     super(context, new DonkeyModel(
/* 19 */           context.bakeLayer(type.model)), new DonkeyModel(
/* 20 */           context.bakeLayer(type.babyModel)));
/*    */     
/* 22 */     this.texture = type.texture;
/* 23 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<DonkeyRenderState, DonkeyModel>)new net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer(this, context.getEquipmentRenderer(), type.saddleLayer, state -> state.saddle, (EntityModel)new EquineSaddleModel(
/*    */ 
/*    */             
/* 26 */             context.bakeLayer(type.saddleModel)), (EntityModel)new EquineSaddleModel(
/* 27 */             context.bakeLayer(type.babySaddleModel))));
/*    */   }
/*    */   
/*    */   private final Identifier texture;
/*    */   
/*    */   public Identifier getTextureLocation(DonkeyRenderState state) {
/* 33 */     return this.texture;
/*    */   }
/*    */ 
/*    */   
/*    */   public DonkeyRenderState createRenderState() {
/* 38 */     return new DonkeyRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(T entity, DonkeyRenderState state, float partialTicks) {
/* 43 */     super.extractRenderState(entity, state, partialTicks);
/* 44 */     state.hasChest = entity.hasChest();
/*    */   }
/*    */   
/*    */   public enum Type {
/* 48 */     DONKEY(
/* 49 */       Identifier.withDefaultNamespace("textures/entity/horse/donkey.png"), ModelLayers.DONKEY, ModelLayers.DONKEY_BABY, EquipmentClientInfo.LayerType.DONKEY_SADDLE, ModelLayers.DONKEY_SADDLE, ModelLayers.DONKEY_BABY_SADDLE),
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 56 */     MULE(
/* 57 */       Identifier.withDefaultNamespace("textures/entity/horse/mule.png"), ModelLayers.MULE, ModelLayers.MULE_BABY, EquipmentClientInfo.LayerType.MULE_SADDLE, ModelLayers.MULE_SADDLE, ModelLayers.MULE_BABY_SADDLE);
/*    */ 
/*    */     
/*    */     private final Identifier texture;
/*    */     
/*    */     private final ModelLayerLocation model;
/*    */     
/*    */     private final ModelLayerLocation babyModel;
/*    */     
/*    */     private final EquipmentClientInfo.LayerType saddleLayer;
/*    */     
/*    */     private final ModelLayerLocation saddleModel;
/*    */     
/*    */     private final ModelLayerLocation babySaddleModel;
/*    */ 
/*    */     
/*    */     Type(Identifier texture, ModelLayerLocation model, ModelLayerLocation babyModel, EquipmentClientInfo.LayerType saddleLayer, ModelLayerLocation saddleModel, ModelLayerLocation babySaddleModel) {
/* 74 */       this.texture = texture;
/* 75 */       this.model = model;
/* 76 */       this.babyModel = babyModel;
/* 77 */       this.saddleLayer = saddleLayer;
/* 78 */       this.saddleModel = saddleModel;
/* 79 */       this.babySaddleModel = babySaddleModel;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/DonkeyRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */