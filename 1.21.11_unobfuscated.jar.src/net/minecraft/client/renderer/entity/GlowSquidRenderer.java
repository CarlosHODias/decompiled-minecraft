/*    */ package net.minecraft.client.renderer.entity;
/*    */ import net.minecraft.client.model.animal.squid.SquidModel;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SquidRenderState;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.animal.squid.GlowSquid;
/*    */ 
/*    */ public class GlowSquidRenderer extends SquidRenderer<GlowSquid> {
/* 12 */   private static final Identifier GLOW_SQUID_LOCATION = Identifier.withDefaultNamespace("textures/entity/squid/glow_squid.png");
/*    */   
/*    */   public GlowSquidRenderer(EntityRendererProvider.Context context, SquidModel model, SquidModel babyModel) {
/* 15 */     super(context, model, babyModel);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(SquidRenderState state) {
/* 20 */     return GLOW_SQUID_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBlockLightLevel(GlowSquid entity, BlockPos blockPos) {
/* 25 */     int glowLightLevel = (int)Mth.clampedLerp(1.0F - entity.getDarkTicksRemaining() / 10.0F, 0.0F, 15.0F);
/* 26 */     if (glowLightLevel == 15) {
/* 27 */       return 15;
/*    */     }
/* 29 */     return Math.max(glowLightLevel, super.getBlockLightLevel(entity, blockPos));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/GlowSquidRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */