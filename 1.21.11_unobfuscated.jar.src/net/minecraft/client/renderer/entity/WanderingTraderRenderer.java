/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.npc.VillagerModel;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.VillagerRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
/*    */ 
/*    */ public class WanderingTraderRenderer extends MobRenderer<WanderingTrader, VillagerRenderState, VillagerModel> {
/* 13 */   private static final Identifier VILLAGER_BASE_SKIN = Identifier.withDefaultNamespace("textures/entity/wandering_trader.png");
/*    */   
/*    */   public WanderingTraderRenderer(EntityRendererProvider.Context context) {
/* 16 */     super(context, new VillagerModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.WANDERING_TRADER)), 0.5F);
/*    */     
/* 18 */     addLayer((RenderLayer<VillagerRenderState, VillagerModel>)new net.minecraft.client.renderer.entity.layers.CustomHeadLayer(this, context.getModelSet(), context.getPlayerSkinRenderCache()));
/* 19 */     addLayer((RenderLayer<VillagerRenderState, VillagerModel>)new net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer(this));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(VillagerRenderState state) {
/* 24 */     return VILLAGER_BASE_SKIN;
/*    */   }
/*    */ 
/*    */   
/*    */   public VillagerRenderState createRenderState() {
/* 29 */     return new VillagerRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(WanderingTrader entity, VillagerRenderState state, float partialTicks) {
/* 34 */     super.extractRenderState(entity, state, partialTicks);
/* 35 */     HoldingEntityRenderState.extractHoldingEntityRenderState((LivingEntity)entity, (HoldingEntityRenderState)state, this.itemModelResolver);
/* 36 */     state.isUnhappy = (entity.getUnhappyCounter() > 0);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/WanderingTraderRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */