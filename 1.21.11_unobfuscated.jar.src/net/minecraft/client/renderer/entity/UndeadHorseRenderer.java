/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.animal.equine.AbstractEquineModel;
/*    */ import net.minecraft.client.model.animal.equine.EquineSaddleModel;
/*    */ import net.minecraft.client.model.animal.equine.HorseModel;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EquineRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class UndeadHorseRenderer extends AbstractHorseRenderer<net.minecraft.world.entity.animal.equine.AbstractHorse, EquineRenderState, AbstractEquineModel<EquineRenderState>> {
/*    */   public UndeadHorseRenderer(EntityRendererProvider.Context context, Type type) {
/* 18 */     super(context, (AbstractEquineModel<EquineRenderState>)new HorseModel(
/* 19 */           context.bakeLayer(type.model)), (AbstractEquineModel<EquineRenderState>)new HorseModel(
/* 20 */           context.bakeLayer(type.babyModel)));
/*    */     
/* 22 */     this.texture = type.texture;
/*    */     
/* 24 */     addLayer((RenderLayer<EquineRenderState, AbstractEquineModel<EquineRenderState>>)new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.HORSE_BODY, state -> state.bodyArmorItem, (EntityModel)new HorseModel(
/*    */ 
/*    */             
/* 27 */             context.bakeLayer(ModelLayers.UNDEAD_HORSE_ARMOR)), (EntityModel)new HorseModel(
/* 28 */             context.bakeLayer(ModelLayers.UNDEAD_HORSE_BABY_ARMOR))));
/*    */ 
/*    */     
/* 31 */     addLayer((RenderLayer<EquineRenderState, AbstractEquineModel<EquineRenderState>>)new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), type.saddleLayer, state -> state.saddle, (EntityModel)new EquineSaddleModel(
/*    */ 
/*    */             
/* 34 */             context.bakeLayer(type.saddleModel)), (EntityModel)new EquineSaddleModel(
/* 35 */             context.bakeLayer(type.babySaddleModel))));
/*    */   }
/*    */   
/*    */   private final Identifier texture;
/*    */   
/*    */   public Identifier getTextureLocation(EquineRenderState state) {
/* 41 */     return this.texture;
/*    */   }
/*    */ 
/*    */   
/*    */   public EquineRenderState createRenderState() {
/* 46 */     return new EquineRenderState();
/*    */   }
/*    */   
/*    */   public enum Type {
/* 50 */     SKELETON(
/* 51 */       Identifier.withDefaultNamespace("textures/entity/horse/horse_skeleton.png"), ModelLayers.SKELETON_HORSE, ModelLayers.SKELETON_HORSE_BABY, EquipmentClientInfo.LayerType.SKELETON_HORSE_SADDLE, ModelLayers.SKELETON_HORSE_SADDLE, ModelLayers.SKELETON_HORSE_BABY_SADDLE),
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 58 */     ZOMBIE(
/* 59 */       Identifier.withDefaultNamespace("textures/entity/horse/horse_zombie.png"), ModelLayers.ZOMBIE_HORSE, ModelLayers.ZOMBIE_HORSE_BABY, EquipmentClientInfo.LayerType.ZOMBIE_HORSE_SADDLE, ModelLayers.ZOMBIE_HORSE_SADDLE, ModelLayers.ZOMBIE_HORSE_BABY_SADDLE);
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
/* 76 */       this.texture = texture;
/* 77 */       this.model = model;
/* 78 */       this.babyModel = babyModel;
/* 79 */       this.saddleLayer = saddleLayer;
/* 80 */       this.saddleModel = saddleModel;
/* 81 */       this.babySaddleModel = babySaddleModel;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/UndeadHorseRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */