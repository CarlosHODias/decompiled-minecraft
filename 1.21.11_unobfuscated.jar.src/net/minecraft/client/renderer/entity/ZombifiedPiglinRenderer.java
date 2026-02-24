/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.monster.piglin.ZombifiedPiglinModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombifiedPiglinRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
/*    */ 
/*    */ public class ZombifiedPiglinRenderer extends HumanoidMobRenderer<ZombifiedPiglin, ZombifiedPiglinRenderState, ZombifiedPiglinModel> {
/* 11 */   private static final Identifier ZOMBIFIED_PIGLIN_LOCATION = Identifier.withDefaultNamespace("textures/entity/piglin/zombified_piglin.png");
/*    */   
/*    */   public ZombifiedPiglinRenderer(EntityRendererProvider.Context context, ModelLayerLocation body, ModelLayerLocation babyBody, ArmorModelSet<ModelLayerLocation> armorSet, ArmorModelSet<ModelLayerLocation> babyArmorSet) {
/* 14 */     super(context, new ZombifiedPiglinModel(context.bakeLayer(body)), new ZombifiedPiglinModel(context.bakeLayer(babyBody)), 0.5F, PiglinRenderer.PIGLIN_CUSTOM_HEAD_TRANSFORMS);
/*    */     
/* 16 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<ZombifiedPiglinRenderState, ZombifiedPiglinModel>)new net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer(this, 
/* 17 */           ArmorModelSet.bake(armorSet, context.getModelSet(), ZombifiedPiglinModel::new), 
/* 18 */           ArmorModelSet.bake(babyArmorSet, context.getModelSet(), ZombifiedPiglinModel::new), 
/* 19 */           context.getEquipmentRenderer()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(ZombifiedPiglinRenderState state) {
/* 25 */     return ZOMBIFIED_PIGLIN_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public ZombifiedPiglinRenderState createRenderState() {
/* 30 */     return new ZombifiedPiglinRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(ZombifiedPiglin entity, ZombifiedPiglinRenderState state, float partialTicks) {
/* 35 */     super.extractRenderState(entity, state, partialTicks);
/* 36 */     state.isAggressive = entity.isAggressive();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ZombifiedPiglinRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */