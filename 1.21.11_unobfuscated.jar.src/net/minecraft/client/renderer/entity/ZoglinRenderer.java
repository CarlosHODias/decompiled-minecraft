/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.HoglinRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.monster.Zoglin;
/*    */ 
/*    */ public class ZoglinRenderer extends AbstractHoglinRenderer<Zoglin> {
/*  9 */   private static final Identifier ZOGLIN_LOCATION = Identifier.withDefaultNamespace("textures/entity/hoglin/zoglin.png");
/*    */   
/*    */   public ZoglinRenderer(EntityRendererProvider.Context context) {
/* 12 */     super(context, ModelLayers.ZOGLIN, ModelLayers.ZOGLIN_BABY, 0.7F);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(HoglinRenderState state) {
/* 17 */     return ZOGLIN_LOCATION;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ZoglinRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */