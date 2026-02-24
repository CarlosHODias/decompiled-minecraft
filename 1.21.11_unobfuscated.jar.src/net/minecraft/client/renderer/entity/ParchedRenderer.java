/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ParchedRenderer extends AbstractSkeletonRenderer<net.minecraft.world.entity.monster.skeleton.Parched, SkeletonRenderState> {
/*  9 */   private static final Identifier PARCHED_SKELETON_LOCATION = Identifier.withDefaultNamespace("textures/entity/skeleton/parched.png");
/*    */   
/*    */   public ParchedRenderer(EntityRendererProvider.Context context) {
/* 12 */     super(context, ModelLayers.PARCHED, ModelLayers.PARCHED_ARMOR);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(SkeletonRenderState state) {
/* 17 */     return PARCHED_SKELETON_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public SkeletonRenderState createRenderState() {
/* 22 */     return new SkeletonRenderState();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ParchedRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */