/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.npc.VillagerModel;
/*    */ import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.VillagerRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ 
/*    */ public class VillagerRenderer extends AgeableMobRenderer<Villager, VillagerRenderState, VillagerModel> {
/* 14 */   private static final Identifier VILLAGER_BASE_SKIN = Identifier.withDefaultNamespace("textures/entity/villager/villager.png");
/* 15 */   public static final CustomHeadLayer.Transforms CUSTOM_HEAD_TRANSFORMS = new CustomHeadLayer.Transforms(-0.1171875F, -0.07421875F, 1.0F);
/*    */   
/*    */   public VillagerRenderer(EntityRendererProvider.Context context) {
/* 18 */     super(context, new VillagerModel(context.bakeLayer(ModelLayers.VILLAGER)), new VillagerModel(context.bakeLayer(ModelLayers.VILLAGER_BABY)), 0.5F);
/*    */     
/* 20 */     addLayer((RenderLayer<VillagerRenderState, VillagerModel>)new CustomHeadLayer(this, context.getModelSet(), context.getPlayerSkinRenderCache(), CUSTOM_HEAD_TRANSFORMS));
/* 21 */     addLayer((RenderLayer<VillagerRenderState, VillagerModel>)new net.minecraft.client.renderer.entity.layers.VillagerProfessionLayer(this, context.getResourceManager(), "villager", (net.minecraft.client.model.EntityModel)new VillagerModel(context.bakeLayer(ModelLayers.VILLAGER_NO_HAT)), (net.minecraft.client.model.EntityModel)new VillagerModel(context.bakeLayer(ModelLayers.VILLAGER_BABY_NO_HAT))));
/* 22 */     addLayer((RenderLayer<VillagerRenderState, VillagerModel>)new net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer(this));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(VillagerRenderState state) {
/* 27 */     return VILLAGER_BASE_SKIN;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getShadowRadius(VillagerRenderState state) {
/* 32 */     float radius = super.getShadowRadius(state);
/* 33 */     if (state.isBaby)
/*    */     {
/* 35 */       return radius * 0.5F;
/*    */     }
/* 37 */     return radius;
/*    */   }
/*    */ 
/*    */   
/*    */   public VillagerRenderState createRenderState() {
/* 42 */     return new VillagerRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Villager entity, VillagerRenderState state, float partialTicks) {
/* 47 */     super.extractRenderState(entity, state, partialTicks);
/* 48 */     net.minecraft.client.renderer.entity.state.HoldingEntityRenderState.extractHoldingEntityRenderState((LivingEntity)entity, (net.minecraft.client.renderer.entity.state.HoldingEntityRenderState)state, this.itemModelResolver);
/* 49 */     state.isUnhappy = (entity.getUnhappyCounter() > 0);
/* 50 */     state.villagerData = entity.getVillagerData();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/VillagerRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */