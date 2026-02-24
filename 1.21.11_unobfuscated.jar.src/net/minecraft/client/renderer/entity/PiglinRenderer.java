/*    */ package net.minecraft.client.renderer.entity;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.monster.piglin.PiglinModel;
/*    */ import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.PiglinRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
/*    */ 
/*    */ public class PiglinRenderer extends HumanoidMobRenderer<AbstractPiglin, PiglinRenderState, PiglinModel> {
/* 14 */   private static final Identifier PIGLIN_LOCATION = Identifier.withDefaultNamespace("textures/entity/piglin/piglin.png");
/* 15 */   private static final Identifier PIGLIN_BRUTE_LOCATION = Identifier.withDefaultNamespace("textures/entity/piglin/piglin_brute.png");
/*    */ 
/*    */   
/* 18 */   public static final CustomHeadLayer.Transforms PIGLIN_CUSTOM_HEAD_TRANSFORMS = new CustomHeadLayer.Transforms(0.0F, 0.0F, 1.0019531F);
/*    */   
/*    */   public PiglinRenderer(EntityRendererProvider.Context context, ModelLayerLocation body, ModelLayerLocation babyBody, ArmorModelSet<ModelLayerLocation> armorSet, ArmorModelSet<ModelLayerLocation> babyArmorSet) {
/* 21 */     super(context, new PiglinModel(context.bakeLayer(body)), new PiglinModel(context.bakeLayer(babyBody)), 0.5F, PIGLIN_CUSTOM_HEAD_TRANSFORMS);
/*    */     
/* 23 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<PiglinRenderState, PiglinModel>)new net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer(this, 
/* 24 */           ArmorModelSet.bake(armorSet, context.getModelSet(), PiglinModel::new), 
/* 25 */           ArmorModelSet.bake(babyArmorSet, context.getModelSet(), PiglinModel::new), 
/* 26 */           context.getEquipmentRenderer()));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(PiglinRenderState state) {
/* 32 */     return state.isBrute ? PIGLIN_BRUTE_LOCATION : PIGLIN_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public PiglinRenderState createRenderState() {
/* 37 */     return new PiglinRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(AbstractPiglin entity, PiglinRenderState state, float partialTicks) {
/* 42 */     super.extractRenderState(entity, state, partialTicks);
/* 43 */     state.isBrute = (entity.getType() == net.minecraft.world.entity.EntityType.PIGLIN_BRUTE);
/* 44 */     state.armPose = entity.getArmPose();
/* 45 */     state.maxCrossbowChageDuration = net.minecraft.world.item.CrossbowItem.getChargeDuration(entity.getUseItem(), (LivingEntity)entity);
/* 46 */     state.isConverting = entity.isConverting();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isShaking(PiglinRenderState state) {
/* 51 */     return (super.isShaking(state) || state.isConverting);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/PiglinRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */