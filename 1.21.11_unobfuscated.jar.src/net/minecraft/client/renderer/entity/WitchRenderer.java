/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.monster.witch.WitchModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.WitchRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Witch;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class WitchRenderer extends MobRenderer<Witch, WitchRenderState, WitchModel> {
/* 14 */   private static final Identifier WITCH_LOCATION = Identifier.withDefaultNamespace("textures/entity/witch.png");
/*    */   
/*    */   public WitchRenderer(EntityRendererProvider.Context context) {
/* 17 */     super(context, new WitchModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.WITCH)), 0.5F);
/*    */     
/* 19 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<WitchRenderState, WitchModel>)new net.minecraft.client.renderer.entity.layers.WitchItemLayer(this));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(WitchRenderState state) {
/* 24 */     return WITCH_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public WitchRenderState createRenderState() {
/* 29 */     return new WitchRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Witch entity, WitchRenderState state, float partialTicks) {
/* 34 */     super.extractRenderState(entity, state, partialTicks);
/* 35 */     HoldingEntityRenderState.extractHoldingEntityRenderState((LivingEntity)entity, (HoldingEntityRenderState)state, this.itemModelResolver);
/* 36 */     state.entityId = entity.getId();
/* 37 */     ItemStack mainHandItem = entity.getMainHandItem();
/* 38 */     state.isHoldingItem = !mainHandItem.isEmpty();
/* 39 */     state.isHoldingPotion = mainHandItem.is(net.minecraft.world.item.Items.POTION);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/WitchRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */