/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.ZombieRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class HuskRenderer extends ZombieRenderer {
/*  8 */   private static final Identifier HUSK_LOCATION = Identifier.withDefaultNamespace("textures/entity/zombie/husk.png");
/*    */   
/*    */   public HuskRenderer(EntityRendererProvider.Context context) {
/* 11 */     super(context, ModelLayers.HUSK, ModelLayers.HUSK_BABY, ModelLayers.HUSK_ARMOR, ModelLayers.HUSK_BABY_ARMOR);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(ZombieRenderState state) {
/* 16 */     return HUSK_LOCATION;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/HuskRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */