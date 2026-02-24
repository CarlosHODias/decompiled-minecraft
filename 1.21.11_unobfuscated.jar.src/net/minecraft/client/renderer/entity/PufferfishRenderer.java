/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.EntityModel;
/*    */ import net.minecraft.client.model.animal.fish.PufferfishBigModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.SubmitNodeCollector;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.PufferfishRenderState;
/*    */ import net.minecraft.client.renderer.state.CameraRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.fish.Pufferfish;
/*    */ 
/*    */ public class PufferfishRenderer extends MobRenderer<Pufferfish, PufferfishRenderState, EntityModel<EntityRenderState>> {
/* 18 */   private static final Identifier PUFFER_LOCATION = Identifier.withDefaultNamespace("textures/entity/fish/pufferfish.png");
/*    */   
/*    */   private final EntityModel<EntityRenderState> small;
/*    */   private final EntityModel<EntityRenderState> mid;
/*    */   private final EntityModel<EntityRenderState> big;
/*    */   
/*    */   public PufferfishRenderer(EntityRendererProvider.Context context) {
/* 25 */     super(context, (EntityModel<EntityRenderState>)new PufferfishBigModel(context.bakeLayer(ModelLayers.PUFFERFISH_BIG)), 0.2F);
/* 26 */     this.big = getModel();
/* 27 */     this.mid = (EntityModel<EntityRenderState>)new net.minecraft.client.model.animal.fish.PufferfishMidModel(context.bakeLayer(ModelLayers.PUFFERFISH_MEDIUM));
/* 28 */     this.small = (EntityModel<EntityRenderState>)new net.minecraft.client.model.animal.fish.PufferfishSmallModel(context.bakeLayer(ModelLayers.PUFFERFISH_SMALL));
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(PufferfishRenderState state) {
/* 33 */     return PUFFER_LOCATION;
/*    */   }
/*    */ 
/*    */   
/*    */   public PufferfishRenderState createRenderState() {
/* 38 */     return new PufferfishRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   protected float getShadowRadius(PufferfishRenderState state) {
/* 43 */     return 0.1F + 0.1F * state.puffState;
/*    */   }
/*    */ 
/*    */   
/*    */   public void submit(PufferfishRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
/* 48 */     switch (state.puffState) { case 0: case 1: default: break; }  this
/*    */ 
/*    */       
/* 51 */       .model = this.big;
/*    */     
/* 53 */     super.submit(state, poseStack, submitNodeCollector, camera);
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Pufferfish entity, PufferfishRenderState state, float partialTicks) {
/* 58 */     super.extractRenderState(entity, state, partialTicks);
/* 59 */     state.puffState = entity.getPuffState();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setupRotations(PufferfishRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 64 */     poseStack.translate(0.0F, Mth.cos((state.ageInTicks * 0.05F)) * 0.08F, 0.0F);
/* 65 */     super.setupRotations(state, poseStack, bodyRot, entityScale);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/PufferfishRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */