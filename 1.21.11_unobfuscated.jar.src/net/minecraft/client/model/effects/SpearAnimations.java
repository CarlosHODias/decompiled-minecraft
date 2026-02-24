/*     */ package net.minecraft.client.model.effects;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.PoseStack;
/*     */ import com.mojang.math.Axis;
/*     */ import net.minecraft.client.model.HumanoidModel;
/*     */ import net.minecraft.client.model.geom.ModelPart;
/*     */ import net.minecraft.client.renderer.entity.state.ArmedEntityRenderState;
/*     */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.util.Ease;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.HumanoidArm;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.component.KineticWeapon;
/*     */ import org.joml.Quaternionfc;
/*     */ 
/*     */ public class SpearAnimations {
/*     */   private static float progress(float time, float start, float end) {
/*  20 */     return Mth.clamp(Mth.inverseLerp(time, start, end), 0.0F, 1.0F);
/*     */   }
/*     */   static final class UseParams extends Record { private final float raiseProgress; private final float raiseProgressStart; private final float raiseProgressMiddle; private final float raiseProgressEnd; private final float swayProgress; private final float lowerProgress; private final float raiseBackProgress; private final float swayIntensity; private final float swayScaleSlow; private final float swayScaleFast;
/*  23 */     UseParams(float raiseProgress, float raiseProgressStart, float raiseProgressMiddle, float raiseProgressEnd, float swayProgress, float lowerProgress, float raiseBackProgress, float swayIntensity, float swayScaleSlow, float swayScaleFast) { this.raiseProgress = raiseProgress; this.raiseProgressStart = raiseProgressStart; this.raiseProgressMiddle = raiseProgressMiddle; this.raiseProgressEnd = raiseProgressEnd; this.swayProgress = swayProgress; this.lowerProgress = lowerProgress; this.raiseBackProgress = raiseBackProgress; this.swayIntensity = swayIntensity; this.swayScaleSlow = swayScaleSlow; this.swayScaleFast = swayScaleFast; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/model/effects/SpearAnimations$UseParams;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #23	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  23 */       //   0	7	0	this	Lnet/minecraft/client/model/effects/SpearAnimations$UseParams; } public float raiseProgress() { return this.raiseProgress; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/model/effects/SpearAnimations$UseParams;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #23	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/model/effects/SpearAnimations$UseParams; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/model/effects/SpearAnimations$UseParams;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #23	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/model/effects/SpearAnimations$UseParams;
/*  23 */       //   0	8	1	o	Ljava/lang/Object; } public float raiseProgressStart() { return this.raiseProgressStart; } public float raiseProgressMiddle() { return this.raiseProgressMiddle; } public float raiseProgressEnd() { return this.raiseProgressEnd; } public float swayProgress() { return this.swayProgress; } public float lowerProgress() { return this.lowerProgress; } public float raiseBackProgress() { return this.raiseBackProgress; } public float swayIntensity() { return this.swayIntensity; } public float swayScaleSlow() { return this.swayScaleSlow; } public float swayScaleFast() { return this.swayScaleFast; }
/*     */     
/*     */     public static UseParams fromKineticWeapon(KineticWeapon kineticWeapon, float time) {
/*  26 */       int finishRaisingTick = kineticWeapon.delayTicks();
/*  27 */       int finishSwayingTick = (Integer)kineticWeapon.dismountConditions().map(KineticWeapon.Condition::maxDurationTicks).orElse(0) + finishRaisingTick;
/*  28 */       int startSwayingTick = finishSwayingTick - 20;
/*  29 */       int finishLoweringTick = (Integer)kineticWeapon.knockbackConditions().map(KineticWeapon.Condition::maxDurationTicks).orElse(0) + finishRaisingTick;
/*  30 */       int startLoweringTick = finishLoweringTick - 40;
/*  31 */       int finishRaisingBackTick = (Integer)kineticWeapon.damageConditions().map(KineticWeapon.Condition::maxDurationTicks).orElse(0) + finishRaisingTick;
/*     */       
/*  33 */       float raiseProgress = SpearAnimations.progress(time, 0.0F, finishRaisingTick);
/*  34 */       float raiseProgressStart = SpearAnimations.progress(raiseProgress, 0.0F, 0.5F);
/*  35 */       float raiseProgressMiddle = SpearAnimations.progress(raiseProgress, 0.5F, 0.8F);
/*  36 */       float raiseProgressEnd = SpearAnimations.progress(raiseProgress, 0.8F, 1.0F);
/*     */       
/*  38 */       float swayProgress = SpearAnimations.progress(time, startSwayingTick, startLoweringTick);
/*  39 */       float lowerProgress = Ease.outCubic(Ease.inOutElastic(SpearAnimations.progress(time - 20.0F, startLoweringTick, finishLoweringTick)));
/*  40 */       float raiseBackProgress = SpearAnimations.progress(time, (finishRaisingBackTick - 5), finishRaisingBackTick);
/*     */       
/*  42 */       float swayIntensity = 2.0F * Ease.outCirc(swayProgress) - 2.0F * Ease.inCirc(raiseBackProgress);
/*     */       
/*  44 */       float swayScaleSlow = Mth.sin((time * 19.0F * 0.017453292F)) * swayIntensity;
/*  45 */       float swayScaleFast = Mth.sin((time * 30.0F * 0.017453292F)) * swayIntensity;
/*     */       
/*  47 */       return new UseParams(raiseProgress, raiseProgressStart, raiseProgressMiddle, raiseProgressEnd, swayProgress, lowerProgress, raiseBackProgress, swayIntensity, swayScaleSlow, swayScaleFast);
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T extends HumanoidRenderState> void thirdPersonHandUse(ModelPart arm, ModelPart head, boolean holdingInRightArm, ItemStack item, T state) {
/*  53 */     int invert = holdingInRightArm ? 1 : -1;
/*     */     
/*  55 */     arm.yRot = -0.1F * invert + head.yRot;
/*  56 */     arm.xRot = -1.5707964F + head.xRot + 0.8F;
/*     */     
/*  58 */     if (((HumanoidRenderState)state).isFallFlying || ((HumanoidRenderState)state).swimAmount > 0.0F) {
/*  59 */       arm.xRot -= 0.9599311F;
/*     */     }
/*     */     
/*  62 */     arm.yRot = 0.017453292F * Math.clamp(57.295776F * arm.yRot, -60.0F, 60.0F);
/*  63 */     arm.xRot = 0.017453292F * Math.clamp(57.295776F * arm.xRot, -120.0F, 30.0F);
/*     */ 
/*     */     
/*  66 */     if (((HumanoidRenderState)state).ticksUsingItem > 0.0F) { if (((HumanoidRenderState)state).isUsingItem) if (((HumanoidRenderState)state).useItemHand != (holdingInRightArm ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND))
/*     */           return;   }
/*     */     else
/*     */     { return; }
/*  70 */      KineticWeapon kineticWeapon = (KineticWeapon)item.get(DataComponents.KINETIC_WEAPON);
/*  71 */     if (kineticWeapon == null) {
/*     */       return;
/*     */     }
/*  74 */     UseParams params = UseParams.fromKineticWeapon(kineticWeapon, ((HumanoidRenderState)state).ticksUsingItem);
/*     */     
/*  76 */     arm.yRot += -invert * params.swayScaleFast() * 0.017453292F * params.swayIntensity() * 1.0F;
/*  77 */     arm.zRot += -invert * params.swayScaleSlow() * 0.017453292F * params.swayIntensity() * 0.5F;
/*  78 */     arm.xRot += 0.017453292F * (-40.0F * 
/*  79 */       params.raiseProgressStart() + 30.0F * params.raiseProgressMiddle() + -20.0F * params.raiseProgressEnd() + 20.0F * params.lowerProgress() + 10.0F * params.raiseBackProgress() + 0.6F * 
/*  80 */       params.swayScaleSlow() * params.swayIntensity());
/*     */   }
/*     */ 
/*     */   
/*     */   public static <S extends ArmedEntityRenderState> void thirdPersonUseItem(S state, PoseStack poseStack, float timeHeld, HumanoidArm arm, ItemStack actualItem) {
/*  85 */     KineticWeapon kineticWeapon = (KineticWeapon)actualItem.get(DataComponents.KINETIC_WEAPON);
/*  86 */     if (kineticWeapon == null || timeHeld == 0.0F) {
/*     */       return;
/*     */     }
/*     */ 
/*     */     
/*  91 */     float attack = Ease.inQuad(progress(((ArmedEntityRenderState)state).attackTime, 0.05F, 0.2F));
/*  92 */     float retract = Ease.inOutExpo(progress(((ArmedEntityRenderState)state).attackTime, 0.4F, 1.0F));
/*     */     
/*  94 */     UseParams params = UseParams.fromKineticWeapon(kineticWeapon, timeHeld);
/*  95 */     int invert = (arm == HumanoidArm.RIGHT) ? 1 : -1;
/*     */     
/*  97 */     float raiseProgressModified = 1.0F - Ease.outBack(1.0F - params.raiseProgress());
/*  98 */     float itemInHandDepth = 0.125F;
/*  99 */     float hitFeedback = hitFeedbackAmount(((ArmedEntityRenderState)state).ticksSinceKineticHitFeedback);
/* 100 */     poseStack.translate(0.0D, -hitFeedback * 0.4D, (-kineticWeapon.forwardMovement() * (raiseProgressModified - params.raiseBackProgress()) + hitFeedback));
/*     */     
/* 102 */     poseStack.rotateAround((Quaternionfc)Axis.XN.rotationDegrees(70.0F * (params.raiseProgress() - params.raiseBackProgress()) - 40.0F * (attack - retract)), 0.0F, -0.03125F, 0.125F);
/*     */     
/* 104 */     poseStack.rotateAround((Quaternionfc)Axis.YP.rotationDegrees((invert * 90) * (params.raiseProgress() - params.swayProgress() + 3.0F * retract + attack)), 0.0F, 0.0F, 0.125F);
/*     */   }
/*     */   
/*     */   public static <T extends HumanoidRenderState> void thirdPersonAttackHand(HumanoidModel<T> model, T state) {
/* 108 */     float attackTime = ((HumanoidRenderState)state).attackTime;
/* 109 */     HumanoidArm arm = ((HumanoidRenderState)state).attackArm;
/* 110 */     model.rightArm.yRot -= model.body.yRot;
/* 111 */     model.leftArm.yRot -= model.body.yRot;
/* 112 */     model.leftArm.xRot -= model.body.yRot;
/*     */     
/* 114 */     float prepare = Ease.inOutSine(progress(attackTime, 0.0F, 0.05F));
/* 115 */     float attack = Ease.inQuad(progress(attackTime, 0.05F, 0.2F));
/* 116 */     float retract = Ease.inOutExpo(progress(attackTime, 0.4F, 1.0F));
/* 117 */     (model.getArm(arm)).xRot += (90.0F * prepare - 120.0F * attack + 30.0F * retract) * 0.017453292F;
/*     */   }
/*     */   
/*     */   public static <S extends ArmedEntityRenderState> void thirdPersonAttackItem(S state, PoseStack poseStack) {
/* 121 */     if (((ArmedEntityRenderState)state).attackTime <= 0.0F) {
/*     */       return;
/*     */     }
/* 124 */     KineticWeapon kineticWeapon = (KineticWeapon)state.getMainHandItemStack().get(DataComponents.KINETIC_WEAPON);
/* 125 */     float jetForward = (kineticWeapon != null) ? kineticWeapon.forwardMovement() : 0.0F;
/*     */     
/* 127 */     float itemInHandDepth = 0.125F;
/* 128 */     float attackTime = ((ArmedEntityRenderState)state).attackTime;
/*     */ 
/*     */     
/* 131 */     float attack = Ease.inQuad(progress(attackTime, 0.05F, 0.2F));
/* 132 */     float retract = Ease.inOutExpo(progress(attackTime, 0.4F, 1.0F));
/*     */     
/* 134 */     poseStack.rotateAround((Quaternionfc)Axis.XN.rotationDegrees(70.0F * (attack - retract)), 0.0F, -0.125F, 0.125F);
/* 135 */     poseStack.translate(0.0F, jetForward * (attack - retract), 0.0F);
/*     */   }
/*     */   
/*     */   private static float hitFeedbackAmount(float ticksSinceFeedbackStart) {
/* 139 */     return 0.4F * (Ease.outQuart(progress(ticksSinceFeedbackStart, 1.0F, 3.0F)) - Ease.inOutSine(progress(ticksSinceFeedbackStart, 3.0F, 10.0F)));
/*     */   }
/*     */   
/*     */   public static void firstPersonUse(float ticksSinceKineticHitFeedback, PoseStack poseStack, float timeHeld, HumanoidArm arm, ItemStack itemStack) {
/* 143 */     KineticWeapon kineticWeapon = (KineticWeapon)itemStack.get(DataComponents.KINETIC_WEAPON);
/* 144 */     if (kineticWeapon == null) {
/*     */       return;
/*     */     }
/* 147 */     UseParams params = UseParams.fromKineticWeapon(kineticWeapon, timeHeld);
/* 148 */     int invert = (arm == HumanoidArm.RIGHT) ? 1 : -1;
/*     */     
/* 150 */     poseStack.translate((invert * (params.raiseProgress() * 0.15F + params.raiseProgressEnd() * -0.05F + params.swayProgress() * -0.1F + params.swayScaleSlow() * 0.005F)), (
/* 151 */         params.raiseProgress() * -0.075F + params.raiseProgressMiddle() * 0.075F + params.swayScaleFast() * 0.01F), 
/* 152 */         params.raiseProgressStart() * 0.05D + params.raiseProgressEnd() * -0.05D + (params.swayScaleSlow() * 0.005F));
/*     */     
/* 154 */     poseStack.rotateAround((Quaternionfc)Axis.XP.rotationDegrees(-65.0F * 
/* 155 */           Ease.inOutBack(params.raiseProgress()) - 35.0F * params.lowerProgress() + 100.0F * params.raiseBackProgress() + -0.5F * params.swayScaleFast()), 0.0F, 0.1F, 0.0F);
/*     */ 
/*     */     
/* 158 */     poseStack.rotateAround((Quaternionfc)Axis.YN.rotationDegrees(invert * (-90.0F * 
/* 159 */           progress(params.raiseProgress(), 0.5F, 0.55F) + 90.0F * params.swayProgress() + 2.0F * params.swayScaleSlow())), invert * 0.15F, 0.0F, 0.0F);
/*     */ 
/*     */     
/* 162 */     poseStack.translate(0.0F, -hitFeedbackAmount(ticksSinceKineticHitFeedback), 0.0F);
/*     */   }
/*     */   
/*     */   public static void firstPersonAttack(float attack, PoseStack poseStack, int invert, HumanoidArm arm) {
/* 166 */     float startingAmount = Ease.inOutSine(progress(attack, 0.0F, 0.05F));
/* 167 */     float middleAmount = Ease.outBack(progress(attack, 0.05F, 0.2F));
/* 168 */     float endingAmount = Ease.inOutExpo(progress(attack, 0.4F, 1.0F));
/*     */     
/* 170 */     poseStack.translate(invert * 0.1F * (startingAmount - middleAmount), -0.075F * (startingAmount - endingAmount), 0.65F * (startingAmount - middleAmount));
/*     */ 
/*     */     
/* 173 */     poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-70.0F * (startingAmount - endingAmount)));
/* 174 */     poseStack.translate(0.0D, 0.0D, -0.25D * (endingAmount - middleAmount));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/effects/SpearAnimations.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */