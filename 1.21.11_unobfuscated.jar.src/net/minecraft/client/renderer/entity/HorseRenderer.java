/*    */ package net.minecraft.client.renderer.entity;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.animal.equine.HorseModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HorseRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.equine.Horse;
/*    */ import net.minecraft.world.entity.animal.equine.Variant;
/*    */ 
/*    */ public final class HorseRenderer extends AbstractHorseRenderer<Horse, HorseRenderState, HorseModel> {
/* 18 */   private static final Map<Variant, Identifier> LOCATION_BY_VARIANT = com.google.common.collect.Maps.newEnumMap(Map.of(Variant.WHITE, 
/* 19 */         Identifier.withDefaultNamespace("textures/entity/horse/horse_white.png"), Variant.CREAMY, 
/* 20 */         Identifier.withDefaultNamespace("textures/entity/horse/horse_creamy.png"), Variant.CHESTNUT, 
/* 21 */         Identifier.withDefaultNamespace("textures/entity/horse/horse_chestnut.png"), Variant.BROWN, 
/* 22 */         Identifier.withDefaultNamespace("textures/entity/horse/horse_brown.png"), Variant.BLACK, 
/* 23 */         Identifier.withDefaultNamespace("textures/entity/horse/horse_black.png"), Variant.GRAY, 
/* 24 */         Identifier.withDefaultNamespace("textures/entity/horse/horse_gray.png"), Variant.DARK_BROWN, 
/* 25 */         Identifier.withDefaultNamespace("textures/entity/horse/horse_darkbrown.png")));
/*    */ 
/*    */   
/*    */   public HorseRenderer(EntityRendererProvider.Context context) {
/* 29 */     super(context, new HorseModel(context.bakeLayer(ModelLayers.HORSE)), new HorseModel(context.bakeLayer(ModelLayers.HORSE_BABY)));
/*    */     
/* 31 */     addLayer((RenderLayer<HorseRenderState, HorseModel>)new net.minecraft.client.renderer.entity.layers.HorseMarkingLayer(this));
/* 32 */     addLayer((RenderLayer<HorseRenderState, HorseModel>)new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.HORSE_BODY, state -> state.bodyArmorItem, (EntityModel)new HorseModel(
/*    */ 
/*    */             
/* 35 */             context.bakeLayer(ModelLayers.HORSE_ARMOR)), (EntityModel)new HorseModel(
/* 36 */             context.bakeLayer(ModelLayers.HORSE_BABY_ARMOR)), 2));
/*    */ 
/*    */     
/* 39 */     addLayer((RenderLayer<HorseRenderState, HorseModel>)new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.HORSE_SADDLE, state -> state.saddle, (EntityModel)new net.minecraft.client.model.animal.equine.EquineSaddleModel(
/*    */ 
/*    */             
/* 42 */             context.bakeLayer(ModelLayers.HORSE_SADDLE)), (EntityModel)new net.minecraft.client.model.animal.equine.EquineSaddleModel(
/* 43 */             context.bakeLayer(ModelLayers.HORSE_BABY_SADDLE)), 2));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(HorseRenderState state) {
/* 50 */     return LOCATION_BY_VARIANT.get(state.variant);
/*    */   }
/*    */ 
/*    */   
/*    */   public HorseRenderState createRenderState() {
/* 55 */     return new HorseRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Horse entity, HorseRenderState state, float partialTicks) {
/* 60 */     super.extractRenderState(entity, state, partialTicks);
/* 61 */     state.variant = entity.getVariant();
/* 62 */     state.markings = entity.getMarkings();
/* 63 */     state.bodyArmorItem = entity.getBodyArmorItem().copy();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/HorseRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */