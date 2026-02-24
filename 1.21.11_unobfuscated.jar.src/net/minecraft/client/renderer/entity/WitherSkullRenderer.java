/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.Model;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.model.geom.PartPose;
/*    */ import net.minecraft.client.model.geom.builders.CubeListBuilder;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.client.model.geom.builders.MeshDefinition;
/*    */ import net.minecraft.client.model.geom.builders.PartDefinition;
/*    */ import net.minecraft.client.model.object.skull.SkullModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.WitherSkullRenderState;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.client.renderer.texture.OverlayTexture;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
/*    */ 
/*    */ public class WitherSkullRenderer extends EntityRenderer<WitherSkull, WitherSkullRenderState> {
/* 22 */   private static final Identifier WITHER_INVULNERABLE_LOCATION = Identifier.withDefaultNamespace("textures/entity/wither/wither_invulnerable.png");
/* 23 */   private static final Identifier WITHER_LOCATION = Identifier.withDefaultNamespace("textures/entity/wither/wither.png");
/*    */   
/*    */   private final SkullModel model;
/*    */   
/*    */   public WitherSkullRenderer(EntityRendererProvider.Context context) {
/* 28 */     super(context);
/* 29 */     this.model = new SkullModel(context.bakeLayer(ModelLayers.WITHER_SKULL));
/*    */   }
/*    */   
/*    */   public static LayerDefinition createSkullLayer() {
/* 33 */     MeshDefinition mesh = new MeshDefinition();
/* 34 */     PartDefinition root = mesh.getRoot();
/*    */     
/* 36 */     root.addOrReplaceChild("head", 
/* 37 */         CubeListBuilder.create()
/* 38 */         .texOffs(0, 35).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F), PartPose.ZERO);
/*    */ 
/*    */ 
/*    */     
/* 42 */     return LayerDefinition.create(mesh, 64, 64);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int getBlockLightLevel(WitherSkull entity, BlockPos blockPos) {
/* 47 */     return 15;
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(WitherSkullRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 52 */     poseStack.pushPose();
/*    */     
/* 54 */     poseStack.scale(-1.0F, -1.0F, 1.0F);
/*    */     
/* 56 */     submitNodeCollector.submitModel((Model)this.model, state.modelState, poseStack, this.model.renderType(getTextureLocation(state)), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
/*    */     
/* 58 */     poseStack.popPose();
/*    */     
/* 60 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */   
/*    */   private Identifier getTextureLocation(WitherSkullRenderState state) {
/* 64 */     return state.isDangerous ? WITHER_INVULNERABLE_LOCATION : WITHER_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public WitherSkullRenderState createRenderState() {
/* 69 */     return new WitherSkullRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(WitherSkull entity, WitherSkullRenderState state, float partialTicks) {
/* 74 */     super.extractRenderState(entity, state, partialTicks);
/* 75 */     state.isDangerous = entity.isDangerous();
/* 76 */     state.modelState.animationPos = 0.0F;
/* 77 */     state.modelState.yRot = entity.getYRot(partialTicks);
/* 78 */     state.modelState.xRot = entity.getXRot(partialTicks);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/WitherSkullRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */