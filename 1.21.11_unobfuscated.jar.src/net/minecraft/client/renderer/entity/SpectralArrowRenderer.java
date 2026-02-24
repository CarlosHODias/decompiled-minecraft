/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.renderer.entity.state.ArrowRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.projectile.arrow.SpectralArrow;
/*    */ 
/*    */ public class SpectralArrowRenderer extends ArrowRenderer<SpectralArrow, ArrowRenderState> {
/*  8 */   public static final Identifier SPECTRAL_ARROW_LOCATION = Identifier.withDefaultNamespace("textures/entity/projectiles/spectral_arrow.png");
/*    */   
/*    */   public SpectralArrowRenderer(EntityRendererProvider.Context context) {
/* 11 */     super(context);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Identifier getTextureLocation(ArrowRenderState state) {
/* 16 */     return SPECTRAL_ARROW_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public ArrowRenderState createRenderState() {
/* 21 */     return new ArrowRenderState();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/SpectralArrowRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */