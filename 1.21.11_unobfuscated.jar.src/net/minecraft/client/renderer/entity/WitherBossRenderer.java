/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.monster.wither.WitherBossModel;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.WitherRenderState;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.boss.wither.WitherBoss;
/*    */ 
/*    */ public class WitherBossRenderer extends MobRenderer<WitherBoss, WitherRenderState, WitherBossModel> {
/* 16 */   private static final Identifier WITHER_INVULNERABLE_LOCATION = Identifier.withDefaultNamespace("textures/entity/wither/wither_invulnerable.png");
/* 17 */   private static final Identifier WITHER_LOCATION = Identifier.withDefaultNamespace("textures/entity/wither/wither.png");
/*    */   
/*    */   public WitherBossRenderer(EntityRendererProvider.Context context) {
/* 20 */     super(context, new WitherBossModel(context.bakeLayer(ModelLayers.WITHER)), 1.0F);
/*    */     
/* 22 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<WitherRenderState, WitherBossModel>)new net.minecraft.client.renderer.entity.layers.WitherArmorLayer(this, context.getModelSet()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBlockLightLevel(WitherBoss entity, BlockPos blockPos) {
/* 27 */     return 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(WitherRenderState state) {
/* 32 */     int invulnerableTicks = Mth.floor(state.invulnerableTicks);
/* 33 */     if (invulnerableTicks <= 0 || (invulnerableTicks <= 80 && invulnerableTicks / 5 % 2 == 1)) {
/* 34 */       return WITHER_LOCATION;
/*    */     }
/* 36 */     return WITHER_INVULNERABLE_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public WitherRenderState createRenderState() {
/* 41 */     return new WitherRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void scale(WitherRenderState state, PoseStack poseStack) {
/* 46 */     float scale = 2.0F;
/*    */     
/* 48 */     if (state.invulnerableTicks > 0.0F) {
/* 49 */       scale -= state.invulnerableTicks / 220.0F * 0.5F;
/*    */     }
/*    */     
/* 52 */     poseStack.scale(scale, scale, scale);
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(WitherBoss entity, WitherRenderState state, float partialTicks) {
/* 57 */     super.extractRenderState(entity, state, partialTicks);
/* 58 */     int invulnerableTicks = entity.getInvulnerableTicks();
/* 59 */     state.invulnerableTicks = (invulnerableTicks > 0) ? (invulnerableTicks - partialTicks) : 0.0F;
/* 60 */     System.arraycopy(entity.getHeadXRots(), 0, state.xHeadRots, 0, state.xHeadRots.length);
/* 61 */     System.arraycopy(entity.getHeadYRots(), 0, state.yHeadRots, 0, state.yHeadRots.length);
/* 62 */     state.isPowered = entity.isPowered();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/WitherBossRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */