/*     */ package net.minecraft.client.model;
/*     */ 
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.renderer.entity.state.UndeadRenderState;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.item.SwingAnimationType;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class AnimationUtils
/*     */ {
/*     */   public static void animateCrossbowHold(ModelPart rightArm, ModelPart leftArm, ModelPart head, boolean holdingInRightArm) {
/*  17 */     ModelPart holdingArm = holdingInRightArm ? rightArm : leftArm;
/*  18 */     ModelPart shootingArm = holdingInRightArm ? leftArm : rightArm;
/*     */     
/*  20 */     holdingArm.yRot = (holdingInRightArm ? -0.3F : 0.3F) + head.yRot;
/*  21 */     shootingArm.yRot = (holdingInRightArm ? 0.6F : -0.6F) + head.yRot;
/*  22 */     holdingArm.xRot = -1.5707964F + head.xRot + 0.1F;
/*  23 */     shootingArm.xRot = -1.5F + head.xRot;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void animateCrossbowCharge(ModelPart rightArm, ModelPart leftArm, float maxCrossbowChargeDuration, float ticksUsingItem, boolean holdingInRightArm) {
/*  30 */     ModelPart holdingArm = holdingInRightArm ? rightArm : leftArm;
/*  31 */     ModelPart pullingArm = holdingInRightArm ? leftArm : rightArm;
/*     */     
/*  33 */     holdingArm.yRot = holdingInRightArm ? -0.8F : 0.8F;
/*  34 */     holdingArm.xRot = -0.97079635F;
/*  35 */     pullingArm.xRot = holdingArm.xRot;
/*     */ 
/*     */     
/*  38 */     float useTicks = Mth.clamp(ticksUsingItem, 0.0F, maxCrossbowChargeDuration);
/*  39 */     float lerpAlpha = useTicks / maxCrossbowChargeDuration;
/*  40 */     pullingArm.yRot = Mth.lerp(lerpAlpha, 0.4F, 0.85F) * (holdingInRightArm ? true : -1);
/*  41 */     pullingArm.xRot = Mth.lerp(lerpAlpha, pullingArm.xRot, -1.5707964F);
/*     */   }
/*     */   
/*     */   public static void swingWeaponDown(ModelPart rightArm, ModelPart leftArm, HumanoidArm mainArm, float attackTime, float ageInTicks) {
/*  45 */     float attack2 = Mth.sin((attackTime * 3.1415927F));
/*  46 */     float attack = Mth.sin(((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * 3.1415927F));
/*  47 */     rightArm.zRot = 0.0F;
/*  48 */     leftArm.zRot = 0.0F;
/*  49 */     rightArm.yRot = 0.15707964F;
/*  50 */     leftArm.yRot = -0.15707964F;
/*     */     
/*  52 */     if (mainArm == HumanoidArm.RIGHT) {
/*  53 */       rightArm.xRot = -1.8849558F + Mth.cos((ageInTicks * 0.09F)) * 0.15F;
/*  54 */       leftArm.xRot = -0.0F + Mth.cos((ageInTicks * 0.19F)) * 0.5F;
/*     */       
/*  56 */       rightArm.xRot += attack2 * 2.2F - attack * 0.4F;
/*  57 */       leftArm.xRot += attack2 * 1.2F - attack * 0.4F;
/*     */     } else {
/*  59 */       rightArm.xRot = -0.0F + Mth.cos((ageInTicks * 0.19F)) * 0.5F;
/*  60 */       leftArm.xRot = -1.8849558F + Mth.cos((ageInTicks * 0.09F)) * 0.15F;
/*     */       
/*  62 */       rightArm.xRot += attack2 * 1.2F - attack * 0.4F;
/*  63 */       leftArm.xRot += attack2 * 2.2F - attack * 0.4F;
/*     */     } 
/*     */     
/*  66 */     bobArms(rightArm, leftArm, ageInTicks);
/*     */   }
/*     */   
/*     */   public static void bobModelPart(ModelPart modelPart, float ageInTicks, float scale) {
/*  70 */     modelPart.zRot += scale * (Mth.cos((ageInTicks * 0.09F)) * 0.05F + 0.05F);
/*  71 */     modelPart.xRot += scale * Mth.sin((ageInTicks * 0.067F)) * 0.05F;
/*     */   }
/*     */   
/*     */   public static void bobArms(ModelPart rightArm, ModelPart leftArm, float ageInTicks) {
/*  75 */     bobModelPart(rightArm, ageInTicks, 1.0F);
/*  76 */     bobModelPart(leftArm, ageInTicks, -1.0F);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends UndeadRenderState> void animateZombieArms(ModelPart leftArm, ModelPart rightArm, boolean aggressive, T state) {
/*  83 */     boolean animateAttack = (((UndeadRenderState)state).swingAnimationType != SwingAnimationType.STAB);
/*  84 */     if (animateAttack) {
/*  85 */       float attackTime = ((UndeadRenderState)state).attackTime;
/*  86 */       float armDrop = -3.1415927F / (aggressive ? 1.5F : 2.25F);
/*  87 */       float attackYRotModifier = Mth.sin((attackTime * 3.1415927F));
/*  88 */       float attackXRotModifier = Mth.sin(((1.0F - (1.0F - attackTime) * (1.0F - attackTime)) * 3.1415927F));
/*     */       
/*  90 */       rightArm.zRot = 0.0F;
/*  91 */       rightArm.yRot = -(0.1F - attackYRotModifier * 0.6F);
/*  92 */       rightArm.xRot = armDrop;
/*  93 */       rightArm.xRot += attackYRotModifier * 1.2F - attackXRotModifier * 0.4F;
/*     */       
/*  95 */       leftArm.zRot = 0.0F;
/*  96 */       leftArm.yRot = 0.1F - attackYRotModifier * 0.6F;
/*  97 */       leftArm.xRot = armDrop;
/*  98 */       leftArm.xRot += attackYRotModifier * 1.2F - attackXRotModifier * 0.4F;
/*     */     } 
/*     */     
/* 101 */     bobArms(rightArm, leftArm, ((UndeadRenderState)state).ageInTicks);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/AnimationUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */