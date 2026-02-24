/*     */ package net.minecraft.client.renderer.entity;
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import java.util.Map;
/*     */ import net.minecraft.client.model.animal.panda.PandaModel;
/*     */ import net.minecraft.client.model.geom.ModelLayers;
/*     */ import net.minecraft.client.renderer.entity.state.EntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.HoldingEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.PandaRenderState;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.animal.panda.Panda;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class PandaRenderer extends AgeableMobRenderer<Panda, PandaRenderState, PandaModel> {
/*  18 */   private static final Map<Panda.Gene, Identifier> TEXTURES = com.google.common.collect.Maps.newEnumMap(Map.of(Panda.Gene.NORMAL, 
/*  19 */         Identifier.withDefaultNamespace("textures/entity/panda/panda.png"), Panda.Gene.LAZY, 
/*  20 */         Identifier.withDefaultNamespace("textures/entity/panda/lazy_panda.png"), Panda.Gene.WORRIED, 
/*  21 */         Identifier.withDefaultNamespace("textures/entity/panda/worried_panda.png"), Panda.Gene.PLAYFUL, 
/*  22 */         Identifier.withDefaultNamespace("textures/entity/panda/playful_panda.png"), Panda.Gene.BROWN, 
/*  23 */         Identifier.withDefaultNamespace("textures/entity/panda/brown_panda.png"), Panda.Gene.WEAK, 
/*  24 */         Identifier.withDefaultNamespace("textures/entity/panda/weak_panda.png"), Panda.Gene.AGGRESSIVE, 
/*  25 */         Identifier.withDefaultNamespace("textures/entity/panda/aggressive_panda.png")));
/*     */ 
/*     */   
/*     */   public PandaRenderer(EntityRendererProvider.Context context) {
/*  29 */     super(context, new PandaModel(context.bakeLayer(ModelLayers.PANDA)), new PandaModel(context.bakeLayer(ModelLayers.PANDA_BABY)), 0.9F);
/*     */     
/*  31 */     addLayer((net.minecraft.client.renderer.entity.layers.RenderLayer<PandaRenderState, PandaModel>)new net.minecraft.client.renderer.entity.layers.PandaHoldsItemLayer(this));
/*     */   }
/*     */ 
/*     */   
/*     */   public Identifier getTextureLocation(PandaRenderState state) {
/*  36 */     return TEXTURES.getOrDefault(state.variant, TEXTURES.get(Panda.Gene.NORMAL));
/*     */   }
/*     */ 
/*     */   
/*     */   public PandaRenderState createRenderState() {
/*  41 */     return new PandaRenderState();
/*     */   }
/*     */ 
/*     */   
/*     */   public void extractRenderState(Panda entity, PandaRenderState state, float partialTicks) {
/*  46 */     super.extractRenderState(entity, state, partialTicks);
/*  47 */     HoldingEntityRenderState.extractHoldingEntityRenderState((LivingEntity)entity, (HoldingEntityRenderState)state, this.itemModelResolver);
/*  48 */     state.variant = entity.getVariant();
/*  49 */     state.isUnhappy = (entity.getUnhappyCounter() > 0);
/*  50 */     state.isSneezing = entity.isSneezing();
/*  51 */     state.sneezeTime = entity.getSneezeCounter();
/*  52 */     state.isEating = entity.isEating();
/*  53 */     state.isScared = entity.isScared();
/*  54 */     state.isSitting = entity.isSitting();
/*  55 */     state.sitAmount = entity.getSitAmount(partialTicks);
/*  56 */     state.lieOnBackAmount = entity.getLieOnBackAmount(partialTicks);
/*  57 */     state.rollAmount = entity.isBaby() ? 0.0F : entity.getRollAmount(partialTicks);
/*  58 */     state.rollTime = (entity.rollCounter > 0) ? (entity.rollCounter + partialTicks) : 0.0F;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setupRotations(PandaRenderState state, PoseStack poseStack, float bodyRot, float entityScale) {
/*  63 */     super.setupRotations(state, poseStack, bodyRot, entityScale);
/*     */     
/*  65 */     if (state.rollTime > 0.0F) {
/*  66 */       float rollTransitionTime = Mth.frac(state.rollTime);
/*  67 */       int rollPos = Mth.floor(state.rollTime);
/*  68 */       int nextRollPos = rollPos + 1;
/*     */       
/*  70 */       float divider = 7.0F;
/*  71 */       float y = state.isBaby ? 0.3F : 0.8F;
/*     */       
/*  73 */       if (rollPos < 8.0F) {
/*  74 */         float thisAngle = 90.0F * rollPos / 7.0F;
/*  75 */         float nextAngle = 90.0F * nextRollPos / 7.0F;
/*  76 */         float angle = getAngle(thisAngle, nextAngle, nextRollPos, rollTransitionTime, 8.0F);
/*     */         
/*  78 */         poseStack.translate(0.0F, (y + 0.2F) * angle / 90.0F, 0.0F);
/*  79 */         poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-angle));
/*  80 */       } else if (rollPos < 16.0F) {
/*  81 */         float internalRollCounter = (rollPos - 8.0F) / 7.0F;
/*  82 */         float thisAngle = 90.0F + 90.0F * internalRollCounter;
/*  83 */         float nextAngle = 90.0F + 90.0F * (nextRollPos - 8.0F) / 7.0F;
/*  84 */         float angle = getAngle(thisAngle, nextAngle, nextRollPos, rollTransitionTime, 16.0F);
/*     */         
/*  86 */         poseStack.translate(0.0F, y + 0.2F + (y - 0.2F) * (angle - 90.0F) / 90.0F, 0.0F);
/*  87 */         poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-angle));
/*  88 */       } else if (rollPos < 24.0F) {
/*  89 */         float internalRollCounter = (rollPos - 16.0F) / 7.0F;
/*  90 */         float thisAngle = 180.0F + 90.0F * internalRollCounter;
/*  91 */         float nextAngle = 180.0F + 90.0F * (nextRollPos - 16.0F) / 7.0F;
/*  92 */         float angle = getAngle(thisAngle, nextAngle, nextRollPos, rollTransitionTime, 24.0F);
/*     */         
/*  94 */         poseStack.translate(0.0F, y + y * (270.0F - angle) / 90.0F, 0.0F);
/*  95 */         poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-angle));
/*  96 */       } else if (rollPos < 32) {
/*  97 */         float internalRollCounter = (rollPos - 24.0F) / 7.0F;
/*  98 */         float thisAngle = 270.0F + 90.0F * internalRollCounter;
/*  99 */         float nextAngle = 270.0F + 90.0F * (nextRollPos - 24.0F) / 7.0F;
/* 100 */         float angle = getAngle(thisAngle, nextAngle, nextRollPos, rollTransitionTime, 32.0F);
/*     */         
/* 102 */         poseStack.translate(0.0F, y * (360.0F - angle) / 90.0F, 0.0F);
/* 103 */         poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-angle));
/*     */       } 
/*     */     } 
/*     */     
/* 107 */     float sitAmount = state.sitAmount;
/* 108 */     if (sitAmount > 0.0F) {
/* 109 */       poseStack.translate(0.0F, 0.8F * sitAmount, 0.0F);
/* 110 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(Mth.lerp(sitAmount, state.xRot, state.xRot + 90.0F)));
/*     */ 
/*     */       
/* 113 */       poseStack.translate(0.0F, -1.0F * sitAmount, 0.0F);
/*     */       
/* 115 */       if (state.isScared) {
/* 116 */         float shakeRot = (float)(Math.cos((state.ageInTicks * 1.25F)) * Math.PI * 0.05000000074505806D);
/*     */         
/* 118 */         poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(shakeRot));
/* 119 */         if (state.isBaby) {
/* 120 */           poseStack.translate(0.0F, 0.8F, 0.55F);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 125 */     float lieOnBackAmount = state.lieOnBackAmount;
/* 126 */     if (lieOnBackAmount > 0.0F) {
/* 127 */       float y = state.isBaby ? 0.5F : 1.3F;
/* 128 */       poseStack.translate(0.0F, y * lieOnBackAmount, 0.0F);
/* 129 */       poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(Mth.lerp(lieOnBackAmount, state.xRot, state.xRot + 180.0F)));
/*     */     } 
/*     */   }
/*     */   
/*     */   private float getAngle(float thisAngle, float nextAngle, int nextRollPos, float rollTransitionTime, float threshold) {
/* 134 */     if (nextRollPos < threshold) {
/* 135 */       return Mth.lerp(rollTransitionTime, thisAngle, nextAngle);
/*     */     }
/* 137 */     return thisAngle;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/PandaRenderer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */