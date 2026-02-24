/*    */ package net.minecraft.client.renderer.entity;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.model.animal.golem.CopperGolemModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.layers.LivingEntityEmissiveLayer;
/*    */ import net.minecraft.client.renderer.entity.layers.RenderLayer;
/*    */ import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.CopperGolemRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.golem.CopperGolem;
/*    */ import net.minecraft.world.entity.animal.golem.CopperGolemOxidationLevels;
/*    */ import net.minecraft.world.item.BlockItem;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.BlockItemStateProperties;
/*    */ 
/*    */ public class CopperGolemRenderer extends MobRenderer<CopperGolem, CopperGolemRenderState, CopperGolemModel> {
/*    */   public CopperGolemRenderer(EntityRendererProvider.Context context) {
/* 24 */     super(context, new CopperGolemModel(context.bakeLayer(ModelLayers.COPPER_GOLEM)), 0.5F);
/* 25 */     addLayer((RenderLayer<CopperGolemRenderState, CopperGolemModel>)new LivingEntityEmissiveLayer(this, getEyeTextureLocationProvider(), (copperGolem, ageInTicks) -> 1.0F, (net.minecraft.client.model.EntityModel)new CopperGolemModel(context.bakeLayer(ModelLayers.COPPER_GOLEM)), net.minecraft.client.renderer.rendertype.RenderTypes::eyes, false));
/* 26 */     addLayer((RenderLayer<CopperGolemRenderState, CopperGolemModel>)new net.minecraft.client.renderer.entity.layers.ItemInHandLayer(this));
/* 27 */     java.util.Objects.requireNonNull(this.model); addLayer((RenderLayer<CopperGolemRenderState, CopperGolemModel>)new net.minecraft.client.renderer.entity.layers.BlockDecorationLayer(this, s -> s.blockOnAntenna, this.model::applyBlockOnAntennaTransform));
/* 28 */     addLayer((RenderLayer<CopperGolemRenderState, CopperGolemModel>)new net.minecraft.client.renderer.entity.layers.CustomHeadLayer(this, context.getModelSet(), context.getPlayerSkinRenderCache()));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(CopperGolemRenderState state) {
/* 33 */     return CopperGolemOxidationLevels.getOxidationLevel(state.weathering).texture();
/*    */   }
/*    */   
/*    */   private static java.util.function.Function<CopperGolemRenderState, Identifier> getEyeTextureLocationProvider() {
/* 37 */     return renderState -> CopperGolemOxidationLevels.getOxidationLevel(renderState.weathering).eyeTexture();
/*    */   }
/*    */ 
/*    */   
/*    */   public CopperGolemRenderState createRenderState() {
/* 42 */     return new CopperGolemRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(CopperGolem entity, CopperGolemRenderState state, float partialTicks) {
/* 47 */     super.extractRenderState(entity, state, partialTicks);
/* 48 */     ArmedEntityRenderState.extractArmedEntityRenderState((LivingEntity)entity, (ArmedEntityRenderState)state, this.itemModelResolver, partialTicks);
/* 49 */     state.weathering = entity.getWeatherState();
/* 50 */     state.copperGolemState = entity.getState();
/* 51 */     state.idleAnimationState.copyFrom(entity.getIdleAnimationState());
/* 52 */     state.interactionGetItem.copyFrom(entity.getInteractionGetItemAnimationState());
/* 53 */     state.interactionGetNoItem.copyFrom(entity.getInteractionGetNoItemAnimationState());
/* 54 */     state.interactionDropItem.copyFrom(entity.getInteractionDropItemAnimationState());
/* 55 */     state.interactionDropNoItem.copyFrom(entity.getInteractionDropNoItemAnimationState());
/* 56 */     state
/* 57 */       .blockOnAntenna = Optional.<ItemStack>of(entity.getItemBySlot(CopperGolem.EQUIPMENT_SLOT_ANTENNA)).flatMap(itemStack -> {
/*    */           BlockItem blockItem;
/*    */           Item patt0$temp = itemStack.getItem();
/*    */           if (patt0$temp instanceof BlockItem) {
/*    */             blockItem = (BlockItem)patt0$temp;
/*    */           } else {
/*    */             return Optional.empty();
/*    */           } 
/*    */           BlockItemStateProperties blockItemState = (BlockItemStateProperties)itemStack.getOrDefault(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY);
/*    */           return Optional.of(blockItemState.apply(blockItem.getBlock().defaultBlockState()));
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/CopperGolemRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */