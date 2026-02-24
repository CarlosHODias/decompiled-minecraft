/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.monster.illager.IllagerModel;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.IllusionerRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.illager.Illusioner;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class IllusionerRenderer extends IllagerRenderer<Illusioner, IllusionerRenderState> {
/* 19 */   private static final Identifier ILLUSIONER = Identifier.withDefaultNamespace("textures/entity/illager/illusioner.png");
/*    */   
/*    */   public IllusionerRenderer(EntityRendererProvider.Context context) {
/* 22 */     super(context, new IllagerModel(context.bakeLayer(net.minecraft.client.model.geom.ModelLayers.ILLUSIONER)), 0.5F);
/*    */     
/* 24 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<IllusionerRenderState, IllagerModel<IllusionerRenderState>>)new net.minecraft.client.renderer.entity.layers.ItemInHandLayer<IllusionerRenderState, IllagerModel<IllusionerRenderState>>(this, this)
/*    */         {
/*    */           public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, IllusionerRenderState state, float yRot, float xRot) {
/* 27 */             if (state.isCastingSpell || state.isAggressive) {
/* 28 */               super.submit(poseStack, submitNodeCollector, lightCoords, (ArmedEntityRenderState)state, yRot, xRot);
/*    */             }
/*    */           }
/*    */         });
/* 32 */     (this.model.getHat()).visible = true;
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(IllusionerRenderState state) {
/* 37 */     return ILLUSIONER;
/*    */   }
/*    */ 
/*    */   
/*    */   public IllusionerRenderState createRenderState() {
/* 42 */     return new IllusionerRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Illusioner entity, IllusionerRenderState state, float partialTicks) {
/* 47 */     super.extractRenderState(entity, state, partialTicks);
/* 48 */     Vec3[] illusionOffsets = entity.getIllusionOffsets(partialTicks);
/* 49 */     state.illusionOffsets = java.util.Arrays.<Vec3>copyOf(illusionOffsets, illusionOffsets.length);
/* 50 */     state.isCastingSpell = entity.isCastingSpell();
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(IllusionerRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 55 */     if (state.isInvisible) {
/* 56 */       Vec3[] offsets = state.illusionOffsets;
/* 57 */       for (int i = 0; i < offsets.length; i++) {
/* 58 */         poseStack.pushPose();
/* 59 */         poseStack.translate((offsets[i]).x + Mth.cos((i + state.ageInTicks * 0.5F)) * 0.025D, (offsets[i]).y + Mth.cos((i + state.ageInTicks * 0.75F)) * 0.0125D, (offsets[i]).z + Mth.cos((i + state.ageInTicks * 0.7F)) * 0.025D);
/* 60 */         super.submit(state, poseStack, submitNodeCollector, camera);
/* 61 */         poseStack.popPose();
/*    */       } 
/*    */     } else {
/* 64 */       super.submit(state, poseStack, submitNodeCollector, camera);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isBodyVisible(IllusionerRenderState state) {
/* 70 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   protected AABB getBoundingBoxForCulling(Illusioner entity) {
/* 75 */     return super.getBoundingBoxForCulling(entity).inflate(3.0D, 0.0D, 3.0D);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/IllusionerRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */