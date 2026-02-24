/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.renderer.entity.state.ArrowRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.TippableArrowRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.projectile.arrow.Arrow;
/*    */ 
/*    */ public class TippableArrowRenderer extends ArrowRenderer<Arrow, TippableArrowRenderState> {
/*  8 */   public static final Identifier NORMAL_ARROW_LOCATION = Identifier.withDefaultNamespace("textures/entity/projectiles/arrow.png");
/*  9 */   public static final Identifier TIPPED_ARROW_LOCATION = Identifier.withDefaultNamespace("textures/entity/projectiles/tipped_arrow.png");
/*    */   
/*    */   public TippableArrowRenderer(EntityRendererProvider.Context context) {
/* 12 */     super(context);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Identifier getTextureLocation(TippableArrowRenderState state) {
/* 17 */     return state.isTipped ? TIPPED_ARROW_LOCATION : NORMAL_ARROW_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public TippableArrowRenderState createRenderState() {
/* 22 */     return new TippableArrowRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Arrow entity, TippableArrowRenderState state, float partialTicks) {
/* 27 */     super.extractRenderState(entity, state, partialTicks);
/* 28 */     state.isTipped = (entity.getColor() > 0);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/TippableArrowRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */