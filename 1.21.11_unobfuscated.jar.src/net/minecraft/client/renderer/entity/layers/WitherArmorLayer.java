/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.wither.WitherBossModel;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.WitherRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ 
/*    */ public class WitherArmorLayer extends EnergySwirlLayer<WitherRenderState, WitherBossModel> {
/* 12 */   private static final Identifier WITHER_ARMOR_LOCATION = Identifier.withDefaultNamespace("textures/entity/wither/wither_armor.png");
/*    */   
/*    */   private final WitherBossModel model;
/*    */   
/*    */   public WitherArmorLayer(RenderLayerParent<WitherRenderState, WitherBossModel> renderer, EntityModelSet modelSet) {
/* 17 */     super(renderer);
/* 18 */     this.model = new WitherBossModel(modelSet.bakeLayer(ModelLayers.WITHER_ARMOR));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPowered(WitherRenderState state) {
/* 23 */     return state.isPowered;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float xOffset(float t) {
/* 28 */     return Mth.cos((t * 0.02F)) * 3.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Identifier getTextureLocation() {
/* 33 */     return WITHER_ARMOR_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   protected WitherBossModel model() {
/* 38 */     return this.model;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/WitherArmorLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */