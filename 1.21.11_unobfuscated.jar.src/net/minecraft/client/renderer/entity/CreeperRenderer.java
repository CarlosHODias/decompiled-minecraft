/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.monster.creeper.CreeperModel;
/*    */ import net.minecraft.client.renderer.entity.state.CreeperRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Creeper;
/*    */ 
/*    */ public class CreeperRenderer extends MobRenderer<Creeper, CreeperRenderState, CreeperModel> {
/* 13 */   private static final Identifier CREEPER_LOCATION = Identifier.withDefaultNamespace("textures/entity/creeper/creeper.png");
/*    */   
/*    */   public CreeperRenderer(EntityRendererProvider.Context context) {
/* 16 */     super(context, new CreeperModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.CREEPER)), 0.5F);
/*    */     
/* 18 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<CreeperRenderState, CreeperModel>)new net.minecraft.client.renderer.entity.layers.CreeperPowerLayer(this, context.getModelSet()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void scale(CreeperRenderState state, PoseStack poseStack) {
/* 23 */     float g = state.swelling;
/*    */     
/* 25 */     float wobble = 1.0F + Mth.sin((g * 100.0F)) * g * 0.01F;
/* 26 */     g = Mth.clamp(g, 0.0F, 1.0F);
/* 27 */     g *= g;
/* 28 */     g *= g;
/* 29 */     float s = (1.0F + g * 0.4F) * wobble;
/* 30 */     float hs = (1.0F + g * 0.1F) / wobble;
/* 31 */     poseStack.scale(s, hs, s);
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getWhiteOverlayProgress(CreeperRenderState state) {
/* 36 */     float step = state.swelling;
/*    */     
/* 38 */     if ((int)(step * 10.0F) % 2 == 0) {
/* 39 */       return 0.0F;
/*    */     }
/*    */     
/* 42 */     return Mth.clamp(step, 0.5F, 1.0F);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(CreeperRenderState state) {
/* 47 */     return CREEPER_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public CreeperRenderState createRenderState() {
/* 52 */     return new CreeperRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Creeper entity, CreeperRenderState state, float partialTicks) {
/* 57 */     super.extractRenderState(entity, state, partialTicks);
/* 58 */     state.swelling = entity.getSwelling(partialTicks);
/* 59 */     state.isPowered = entity.isPowered();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/CreeperRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */