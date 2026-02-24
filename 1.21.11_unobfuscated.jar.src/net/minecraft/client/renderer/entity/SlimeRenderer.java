/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.monster.slime.SlimeModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.SlimeRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Slime;
/*    */ 
/*    */ public class SlimeRenderer extends MobRenderer<Slime, SlimeRenderState, SlimeModel> {
/* 13 */   public static final Identifier SLIME_LOCATION = Identifier.withDefaultNamespace("textures/entity/slime/slime.png");
/*    */   
/*    */   public SlimeRenderer(EntityRendererProvider.Context context) {
/* 16 */     super(context, new SlimeModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.SLIME)), 0.25F);
/*    */     
/* 18 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<SlimeRenderState, SlimeModel>)new net.minecraft.client.renderer.entity.layers.SlimeOuterLayer(this, context.getModelSet()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getShadowRadius(SlimeRenderState state) {
/* 23 */     return state.size * 0.25F;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void scale(SlimeRenderState state, PoseStack poseStack) {
/* 29 */     float s = 0.999F;
/* 30 */     poseStack.scale(0.999F, 0.999F, 0.999F);
/* 31 */     poseStack.translate(0.0F, 0.001F, 0.0F);
/*    */     
/* 33 */     float size = state.size;
/* 34 */     float ss = state.squish / (size * 0.5F + 1.0F);
/* 35 */     float w = 1.0F / (ss + 1.0F);
/* 36 */     poseStack.scale(w * size, 1.0F / w * size, w * size);
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(SlimeRenderState state) {
/* 41 */     return SLIME_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public SlimeRenderState createRenderState() {
/* 46 */     return new SlimeRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Slime entity, SlimeRenderState state, float partialTicks) {
/* 51 */     super.extractRenderState(entity, state, partialTicks);
/* 52 */     state.squish = net.minecraft.util.Mth.lerp(partialTicks, entity.oSquish, entity.squish);
/* 53 */     state.size = entity.getSize();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/SlimeRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */