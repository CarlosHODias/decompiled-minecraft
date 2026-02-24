/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.monster.skeleton.Stray;
/*    */ 
/*    */ public class StrayRenderer extends AbstractSkeletonRenderer<Stray, SkeletonRenderState> {
/* 10 */   private static final Identifier STRAY_SKELETON_LOCATION = Identifier.withDefaultNamespace("textures/entity/skeleton/stray.png");
/* 11 */   private static final Identifier STRAY_CLOTHES_LOCATION = Identifier.withDefaultNamespace("textures/entity/skeleton/stray_overlay.png");
/*    */   
/*    */   public StrayRenderer(EntityRendererProvider.Context context) {
/* 14 */     super(context, ModelLayers.STRAY, ModelLayers.STRAY_ARMOR);
/*    */     
/* 16 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<SkeletonRenderState, net.minecraft.client.model.monster.skeleton.SkeletonModel<SkeletonRenderState>>)new net.minecraft.client.renderer.entity.layers.SkeletonClothingLayer(this, context.getModelSet(), ModelLayers.STRAY_OUTER_LAYER, STRAY_CLOTHES_LOCATION));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(SkeletonRenderState state) {
/* 21 */     return STRAY_SKELETON_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public SkeletonRenderState createRenderState() {
/* 26 */     return new SkeletonRenderState();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/StrayRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */