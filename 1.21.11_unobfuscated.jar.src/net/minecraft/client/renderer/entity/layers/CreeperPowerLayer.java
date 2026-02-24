/*    */ package net.minecraft.client.renderer.entity.layers;
/*    */ import net.minecraft.client.model.geom.EntityModelSet;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.creeper.CreeperModel;
/*    */ import net.minecraft.client.renderer.entity.RenderLayerParent;
/*    */ import net.minecraft.client.renderer.entity.state.CreeperRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class CreeperPowerLayer extends EnergySwirlLayer<CreeperRenderState, CreeperModel> {
/* 11 */   private static final Identifier POWER_LOCATION = Identifier.withDefaultNamespace("textures/entity/creeper/creeper_armor.png");
/*    */   
/*    */   private final CreeperModel model;
/*    */   
/*    */   public CreeperPowerLayer(RenderLayerParent<CreeperRenderState, CreeperModel> renderer, EntityModelSet modelSet) {
/* 16 */     super(renderer);
/* 17 */     this.model = new CreeperModel(modelSet.bakeLayer(ModelLayers.CREEPER_ARMOR));
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPowered(CreeperRenderState state) {
/* 22 */     return state.isPowered;
/*    */   }
/*    */ 
/*    */   
/*    */   protected float xOffset(float t) {
/* 27 */     return t * 0.01F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Identifier getTextureLocation() {
/* 32 */     return POWER_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   protected CreeperModel model() {
/* 37 */     return this.model;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/layers/CreeperPowerLayer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */