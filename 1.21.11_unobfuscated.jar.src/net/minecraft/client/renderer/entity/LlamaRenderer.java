/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.animal.llama.LlamaModel;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LlamaRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.animal.equine.Llama;
/*    */ 
/*    */ public class LlamaRenderer extends AgeableMobRenderer<Llama, LlamaRenderState, LlamaModel> {
/* 11 */   private static final Identifier CREAMY = Identifier.withDefaultNamespace("textures/entity/llama/creamy.png");
/* 12 */   private static final Identifier WHITE = Identifier.withDefaultNamespace("textures/entity/llama/white.png");
/* 13 */   private static final Identifier BROWN = Identifier.withDefaultNamespace("textures/entity/llama/brown.png");
/* 14 */   private static final Identifier GRAY = Identifier.withDefaultNamespace("textures/entity/llama/gray.png");
/*    */   
/*    */   public LlamaRenderer(EntityRendererProvider.Context context, ModelLayerLocation model, ModelLayerLocation babyModel) {
/* 17 */     super(context, new LlamaModel(context.bakeLayer(model)), new LlamaModel(context.bakeLayer(babyModel)), 0.7F);
/*    */     
/* 19 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<LlamaRenderState, LlamaModel>)new net.minecraft.client.renderer.entity.layers.LlamaDecorLayer(this, context.getModelSet(), context.getEquipmentRenderer()));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(LlamaRenderState state) {
/* 24 */     switch (state.variant) { default: throw new MatchException(null, null);case CREAMY: case WHITE: case BROWN: case GRAY: break; }  return 
/*    */ 
/*    */ 
/*    */       
/* 28 */       GRAY;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LlamaRenderState createRenderState() {
/* 34 */     return new LlamaRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Llama entity, LlamaRenderState state, float partialTicks) {
/* 39 */     super.extractRenderState(entity, state, partialTicks);
/* 40 */     state.variant = entity.getVariant();
/* 41 */     state.hasChest = (!entity.isBaby() && entity.hasChest());
/* 42 */     state.bodyItem = entity.getBodyArmorItem();
/* 43 */     state.isTraderLlama = entity.isTraderLlama();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/LlamaRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */