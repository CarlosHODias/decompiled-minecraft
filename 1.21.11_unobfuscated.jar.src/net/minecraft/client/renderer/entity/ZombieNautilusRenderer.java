/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.animal.nautilus.NautilusModel;
/*    */ import net.minecraft.client.model.animal.nautilus.NautilusSaddleModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.SimpleEquipmentLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.NautilusRenderState;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.resources.model.EquipmentClientInfo;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.nautilus.ZombieNautilus;
/*    */ import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ZombieNautilusRenderer extends MobRenderer<ZombieNautilus, NautilusRenderState, NautilusModel> {
/*    */   public ZombieNautilusRenderer(EntityRendererProvider.Context context) {
/* 27 */     super(context, new NautilusModel(context.bakeLayer(ModelLayers.ZOMBIE_NAUTILUS)), 0.7F);
/* 28 */     addLayer((RenderLayer<NautilusRenderState, NautilusModel>)new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.NAUTILUS_BODY, state -> state.bodyArmorItem, (EntityModel)new net.minecraft.client.model.animal.nautilus.NautilusArmorModel(
/*    */ 
/*    */             
/* 31 */             context.bakeLayer(ModelLayers.NAUTILUS_ARMOR)), null));
/*    */ 
/*    */     
/* 34 */     addLayer((RenderLayer<NautilusRenderState, NautilusModel>)new SimpleEquipmentLayer(this, context.getEquipmentRenderer(), EquipmentClientInfo.LayerType.NAUTILUS_SADDLE, state -> state.saddle, (EntityModel)new NautilusSaddleModel(
/*    */ 
/*    */             
/* 37 */             context.bakeLayer(ModelLayers.NAUTILUS_SADDLE)), null));
/*    */ 
/*    */     
/* 40 */     this.models = bakeModels(context);
/*    */   }
/*    */   private final Map<ZombieNautilusVariant.ModelType, NautilusModel> models;
/*    */   private static Map<ZombieNautilusVariant.ModelType, NautilusModel> bakeModels(EntityRendererProvider.Context context) {
/* 44 */     return Maps.newEnumMap(Map.of(ZombieNautilusVariant.ModelType.NORMAL, new NautilusModel(
/* 45 */             context.bakeLayer(ModelLayers.ZOMBIE_NAUTILUS)), ZombieNautilusVariant.ModelType.WARM, new net.minecraft.client.model.monster.nautilus.ZombieNautilusCoralModel(
/* 46 */             context.bakeLayer(ModelLayers.ZOMBIE_NAUTILUS_CORAL))));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void submit(NautilusRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 52 */     if (state.variant == null) {
/*    */       return;
/*    */     }
/* 55 */     this.model = (NautilusModel)this.models.get(state.variant.modelAndTexture().model());
/* 56 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(NautilusRenderState state) {
/* 61 */     return (state.variant == null) ? net.minecraft.client.renderer.texture.MissingTextureAtlasSprite.getLocation() : state.variant.modelAndTexture().asset().texturePath();
/*    */   }
/*    */ 
/*    */   
/*    */   public NautilusRenderState createRenderState() {
/* 66 */     return new NautilusRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(ZombieNautilus entity, NautilusRenderState state, float partialTicks) {
/* 71 */     super.extractRenderState(entity, state, partialTicks);
/* 72 */     state.saddle = entity.getItemBySlot(EquipmentSlot.SADDLE).copy();
/* 73 */     state.bodyArmorItem = entity.getBodyArmorItem().copy();
/* 74 */     state.variant = (ZombieNautilusVariant)entity.getVariant().value();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ZombieNautilusRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */