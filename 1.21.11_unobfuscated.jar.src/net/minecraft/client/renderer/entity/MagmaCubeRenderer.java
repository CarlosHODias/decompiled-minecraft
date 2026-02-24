/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.monster.slime.MagmaCubeModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SlimeRenderState;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.MagmaCube;
/*    */ 
/*    */ public class MagmaCubeRenderer extends MobRenderer<MagmaCube, SlimeRenderState, MagmaCubeModel> {
/* 14 */   private static final Identifier MAGMACUBE_LOCATION = Identifier.withDefaultNamespace("textures/entity/slime/magmacube.png");
/*    */   
/*    */   public MagmaCubeRenderer(EntityRendererProvider.Context context) {
/* 17 */     super(context, new MagmaCubeModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.MAGMA_CUBE)), 0.25F);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBlockLightLevel(MagmaCube entity, BlockPos blockPos) {
/* 22 */     return 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(SlimeRenderState state) {
/* 27 */     return MAGMACUBE_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public SlimeRenderState createRenderState() {
/* 32 */     return new SlimeRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(MagmaCube entity, SlimeRenderState state, float partialTicks) {
/* 37 */     super.extractRenderState(entity, state, partialTicks);
/* 38 */     state.squish = net.minecraft.util.Mth.lerp(partialTicks, entity.oSquish, entity.squish);
/* 39 */     state.size = entity.getSize();
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getShadowRadius(SlimeRenderState state) {
/* 44 */     return state.size * 0.25F;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void scale(SlimeRenderState state, PoseStack poseStack) {
/* 49 */     int size = state.size;
/* 50 */     float ss = state.squish / (size * 0.5F + 1.0F);
/* 51 */     float w = 1.0F / (ss + 1.0F);
/* 52 */     poseStack.scale(w * size, 1.0F / w * size, w * size);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/MagmaCubeRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */