/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.geom.ModelLayerLocation;
/*    */ import net.minecraft.client.model.monster.spider.SpiderModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.spider.Spider;
/*    */ 
/*    */ public class SpiderRenderer<T extends Spider> extends MobRenderer<T, LivingEntityRenderState, SpiderModel> {
/* 12 */   private static final Identifier SPIDER_LOCATION = Identifier.withDefaultNamespace("textures/entity/spider/spider.png");
/*    */   
/*    */   public SpiderRenderer(EntityRendererProvider.Context context) {
/* 15 */     this(context, net.minecraft.client.model.geom.ModelLayers.SPIDER);
/*    */   }
/*    */   
/*    */   public SpiderRenderer(EntityRendererProvider.Context context, ModelLayerLocation model) {
/* 19 */     super(context, new SpiderModel(context.bakeLayer(model)), 0.8F);
/*    */     
/* 21 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<LivingEntityRenderState, SpiderModel>)new net.minecraft.client.renderer.entity.layers.SpiderEyesLayer(this));
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getFlipDegrees() {
/* 26 */     return 180.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(LivingEntityRenderState state) {
/* 31 */     return SPIDER_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public LivingEntityRenderState createRenderState() {
/* 36 */     return new LivingEntityRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(T entity, LivingEntityRenderState state, float partialTicks) {
/* 41 */     super.extractRenderState(entity, state, partialTicks);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/SpiderRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */