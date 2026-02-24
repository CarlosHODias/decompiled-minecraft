/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.monster.spider.CaveSpider;
/*    */ 
/*    */ public class CaveSpiderRenderer extends SpiderRenderer<CaveSpider> {
/*  9 */   private static final Identifier CAVE_SPIDER_LOCATION = Identifier.withDefaultNamespace("textures/entity/spider/cave_spider.png");
/*    */   
/*    */   public CaveSpiderRenderer(EntityRendererProvider.Context context) {
/* 12 */     super(context, ModelLayers.CAVE_SPIDER);
/* 13 */     this.shadowRadius = 0.56F;
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(LivingEntityRenderState state) {
/* 18 */     return CAVE_SPIDER_LOCATION;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/CaveSpiderRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */