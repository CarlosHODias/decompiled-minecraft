/*    */ package net.minecraft.client.renderer.entity;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.model.animal.axolotl.AxolotlModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.AxolotlRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.axolotl.Axolotl;
/*    */ 
/*    */ public class AxolotlRenderer extends AgeableMobRenderer<Axolotl, AxolotlRenderState, AxolotlModel> {
/*    */   static {
/* 15 */     TEXTURE_BY_TYPE = (Map<Axolotl.Variant, Identifier>)net.minecraft.util.Util.make(com.google.common.collect.Maps.newHashMap(), map -> {
/*    */           for (Axolotl.Variant variant : Axolotl.Variant.values()) {
/*    */             map.put(variant, Identifier.withDefaultNamespace(String.format(java.util.Locale.ROOT, "textures/entity/axolotl/axolotl_%s.png", new Object[] { variant.getName() })));
/*    */           } 
/*    */         });
/*    */   } private static final Map<Axolotl.Variant, Identifier> TEXTURE_BY_TYPE;
/*    */   public AxolotlRenderer(EntityRendererProvider.Context context) {
/* 22 */     super(context, new AxolotlModel(context.bakeLayer(ModelLayers.AXOLOTL)), new AxolotlModel(context.bakeLayer(ModelLayers.AXOLOTL_BABY)), 0.5F);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(AxolotlRenderState state) {
/* 27 */     return TEXTURE_BY_TYPE.get(state.variant);
/*    */   }
/*    */ 
/*    */   
/*    */   public AxolotlRenderState createRenderState() {
/* 32 */     return new AxolotlRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Axolotl entity, AxolotlRenderState state, float partialTicks) {
/* 37 */     super.extractRenderState(entity, state, partialTicks);
/* 38 */     state.variant = entity.getVariant();
/* 39 */     state.playingDeadFactor = entity.playingDeadAnimator.getFactor(partialTicks);
/* 40 */     state.inWaterFactor = entity.inWaterAnimator.getFactor(partialTicks);
/* 41 */     state.onGroundFactor = entity.onGroundAnimator.getFactor(partialTicks);
/* 42 */     state.movingFactor = entity.movingAnimator.getFactor(partialTicks);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/AxolotlRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */