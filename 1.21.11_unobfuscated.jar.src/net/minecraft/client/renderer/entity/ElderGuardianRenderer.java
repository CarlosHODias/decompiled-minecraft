/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.GuardianRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ElderGuardianRenderer extends GuardianRenderer {
/*  8 */   public static final Identifier GUARDIAN_ELDER_LOCATION = Identifier.withDefaultNamespace("textures/entity/guardian_elder.png");
/*    */   
/*    */   public ElderGuardianRenderer(EntityRendererProvider.Context context) {
/* 11 */     super(context, 1.2F, ModelLayers.ELDER_GUARDIAN);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(GuardianRenderState state) {
/* 16 */     return GUARDIAN_ELDER_LOCATION;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ElderGuardianRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */