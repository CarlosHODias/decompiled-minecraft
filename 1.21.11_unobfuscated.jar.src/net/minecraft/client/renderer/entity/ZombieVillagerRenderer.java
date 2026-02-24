/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.zombie.ZombieVillagerModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombieVillagerRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.zombie.ZombieVillager;
/*    */ 
/*    */ public class ZombieVillagerRenderer extends HumanoidMobRenderer<ZombieVillager, ZombieVillagerRenderState, ZombieVillagerModel<ZombieVillagerRenderState>> {
/* 12 */   private static final Identifier ZOMBIE_VILLAGER_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie_villager/zombie_villager.png");
/*    */   
/*    */   public ZombieVillagerRenderer(EntityRendererProvider.Context context) {
/* 15 */     super(context, new ZombieVillagerModel(
/*    */           
/* 17 */           context.bakeLayer(ModelLayers.ZOMBIE_VILLAGER)), new ZombieVillagerModel(
/* 18 */           context.bakeLayer(ModelLayers.ZOMBIE_VILLAGER_BABY)), 0.5F, VillagerRenderer.CUSTOM_HEAD_TRANSFORMS);
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 23 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<ZombieVillagerRenderState, ZombieVillagerModel<ZombieVillagerRenderState>>)new net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer(this, 
/* 24 */           ArmorModelSet.bake(ModelLayers.ZOMBIE_VILLAGER_ARMOR, context.getModelSet(), ZombieVillagerModel::new), 
/* 25 */           ArmorModelSet.bake(ModelLayers.ZOMBIE_VILLAGER_BABY_ARMOR, context.getModelSet(), ZombieVillagerModel::new), 
/* 26 */           context.getEquipmentRenderer()));
/*    */     
/* 28 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<ZombieVillagerRenderState, ZombieVillagerModel<ZombieVillagerRenderState>>)new net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer(this, context.getResourceManager(), "zombie_villager", (net.minecraft.client.model.EntityModel)new ZombieVillagerModel(context.bakeLayer(ModelLayers.ZOMBIE_VILLAGER_NO_HAT)), (net.minecraft.client.model.EntityModel)new ZombieVillagerModel(context.bakeLayer(ModelLayers.ZOMBIE_VILLAGER_BABY_NO_HAT))));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(ZombieVillagerRenderState state) {
/* 33 */     return ZOMBIE_VILLAGER_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public ZombieVillagerRenderState createRenderState() {
/* 38 */     return new ZombieVillagerRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(ZombieVillager entity, ZombieVillagerRenderState state, float partialTicks) {
/* 43 */     super.extractRenderState(entity, state, partialTicks);
/* 44 */     state.isConverting = entity.isConverting();
/* 45 */     state.villagerData = entity.getVillagerData();
/* 46 */     state.isAggressive = entity.isAggressive();
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isShaking(ZombieVillagerRenderState state) {
/* 51 */     return (super.isShaking(state) || state.isConverting);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ZombieVillagerRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */