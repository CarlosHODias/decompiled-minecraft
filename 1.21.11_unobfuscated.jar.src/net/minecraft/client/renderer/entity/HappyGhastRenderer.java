/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.animal.ghast.HappyGhastHarnessModel;
/*    */ import net.minecraft.client.model.animal.ghast.HappyGhastModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HappyGhastRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.happyghast.HappyGhast;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class HappyGhastRenderer extends AgeableMobRenderer<HappyGhast, HappyGhastRenderState, HappyGhastModel> {
/* 16 */   private static final Identifier GHAST_LOCATION = Identifier.withDefaultNamespace("textures/entity/ghast/happy_ghast.png");
/* 17 */   private static final Identifier GHAST_BABY_LOCATION = Identifier.withDefaultNamespace("textures/entity/ghast/happy_ghast_baby.png");
/* 18 */   private static final Identifier GHAST_ROPES = Identifier.withDefaultNamespace("textures/entity/ghast/happy_ghast_ropes.png");
/*    */   
/*    */   public HappyGhastRenderer(EntityRendererProvider.Context context) {
/* 21 */     super(context, new HappyGhastModel(context.bakeLayer(ModelLayers.HAPPY_GHAST)), new HappyGhastModel(context.bakeLayer(ModelLayers.HAPPY_GHAST_BABY)), 2.0F);
/*    */     
/* 23 */     addLayer((RenderLayer<HappyGhastRenderState, HappyGhastModel>)new net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer(this, context.getEquipmentRenderer(), net.minecraft.client.resources.model.EquipmentClientInfo.LayerType.HAPPY_GHAST_BODY, state -> state.bodyItem, (net.minecraft.client.model.EntityModel)new HappyGhastHarnessModel(
/*    */ 
/*    */             
/* 26 */             context.bakeLayer(ModelLayers.HAPPY_GHAST_HARNESS)), (net.minecraft.client.model.EntityModel)new HappyGhastHarnessModel(
/* 27 */             context.bakeLayer(ModelLayers.HAPPY_GHAST_BABY_HARNESS))));
/*    */ 
/*    */     
/* 30 */     addLayer((RenderLayer<HappyGhastRenderState, HappyGhastModel>)new net.minecraft.client.renderer.entity.layers.RopesLayer(this, context.getModelSet(), GHAST_ROPES));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(HappyGhastRenderState state) {
/* 35 */     if (state.isBaby) {
/* 36 */       return GHAST_BABY_LOCATION;
/*    */     }
/* 38 */     return GHAST_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public HappyGhastRenderState createRenderState() {
/* 43 */     return new HappyGhastRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   protected AABB getBoundingBoxForCulling(HappyGhast entity) {
/* 48 */     AABB aabb = super.getBoundingBoxForCulling(entity);
/* 49 */     float height = entity.getBbHeight();
/* 50 */     return aabb.setMinY(aabb.minY - (height / 2.0F));
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(HappyGhast entity, HappyGhastRenderState state, float partialTicks) {
/* 55 */     super.extractRenderState(entity, state, partialTicks);
/* 56 */     state.bodyItem = entity.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.BODY).copy();
/* 57 */     state.isRidden = entity.isVehicle();
/* 58 */     state.isLeashHolder = entity.isLeashHolder();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/HappyGhastRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */