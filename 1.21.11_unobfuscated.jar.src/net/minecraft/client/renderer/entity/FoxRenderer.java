/*    */ package net.minecraft.client.renderer.entity;
/*    */ import com.mojang.blaze3d.vertex.PoseStack;
/*    */ import net.minecraft.client.model.animal.fox.FoxModel;
/*    */ import net.minecraft.client.model.geom.ModelLayers;
/*    */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.FoxRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.animal.fox.Fox;
/*    */ 
/*    */ public class FoxRenderer extends AgeableMobRenderer<Fox, FoxRenderState, FoxModel> {
/* 14 */   private static final Identifier RED_FOX_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fox/fox.png");
/* 15 */   private static final Identifier RED_FOX_SLEEP_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fox/fox_sleep.png");
/* 16 */   private static final Identifier SNOW_FOX_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fox/snow_fox.png");
/* 17 */   private static final Identifier SNOW_FOX_SLEEP_TEXTURE = Identifier.withDefaultNamespace("textures/entity/fox/snow_fox_sleep.png");
/*    */   
/*    */   public FoxRenderer(EntityRendererProvider.Context context) {
/* 20 */     super(context, new FoxModel(context.bakeLayer(ModelLayers.FOX)), new FoxModel(context.bakeLayer(ModelLayers.FOX_BABY)), 0.4F);
/*    */     
/* 22 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<FoxRenderState, FoxModel>)new net.minecraft.client.renderer.entity.layers.FoxHeldItemLayer(this));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setupRotations(FoxRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/* 27 */     super.setupRotations(state, poseStack, bodyRot, entityScale);
/*    */     
/* 29 */     if (state.isPouncing || state.isFaceplanted) {
/* 30 */       poseStack.mulPose((org.joml.Quaternionfc)com.mojang.math.Axis.XP.rotationDegrees(-state.xRot));
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public Identifier getTextureLocation(FoxRenderState state) {
/* 36 */     if (state.variant == Fox.Variant.RED) {
/* 37 */       return state.isSleeping ? RED_FOX_SLEEP_TEXTURE : RED_FOX_TEXTURE;
/*    */     }
/* 39 */     return state.isSleeping ? SNOW_FOX_SLEEP_TEXTURE : SNOW_FOX_TEXTURE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public FoxRenderState createRenderState() {
/* 45 */     return new FoxRenderState();
/*    */   }
/*    */ 
/*    */   
/*    */   public void extractRenderState(Fox entity, FoxRenderState state, float partialTicks) {
/* 50 */     super.extractRenderState(entity, state, partialTicks);
/* 51 */     HoldingEntityRenderState.extractHoldingEntityRenderState((LivingEntity)entity, (HoldingEntityRenderState)state, this.itemModelResolver);
/* 52 */     state.headRollAngle = entity.getHeadRollAngle(partialTicks);
/* 53 */     state.isCrouching = entity.isCrouching();
/* 54 */     state.crouchAmount = entity.getCrouchAmount(partialTicks);
/* 55 */     state.isSleeping = entity.isSleeping();
/* 56 */     state.isSitting = entity.isSitting();
/* 57 */     state.isFaceplanted = entity.isFaceplanted();
/* 58 */     state.isPouncing = entity.isPouncing();
/* 59 */     state.variant = entity.getVariant();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/FoxRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */