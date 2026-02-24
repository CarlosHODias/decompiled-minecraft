/*    */ package net.minecraft.client.model.monster.piglin;
/*    */ import net.minecraft.client.model.AnimationUtils;
/*    */ import net.minecraft.client.model.geom.ModelPart;
/*    */ import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
/*    */ import net.minecraft.client.renderer.entity.state.PiglinRenderState;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.HumanoidArm;
/*    */ import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
/*    */ 
/*    */ public class PiglinModel extends AbstractPiglinModel<PiglinRenderState> {
/*    */   public PiglinModel(ModelPart root) {
/* 12 */     super(root);
/*    */   }
/*    */ 
/*    */   
/*    */   public void setupAnim(PiglinRenderState state) {
/* 17 */     super.setupAnim(state);
/*    */     
/* 19 */     float defaultAngle = 0.5235988F;
/*    */     
/* 21 */     float attackTime = state.attackTime;
/*    */     
/* 23 */     PiglinArmPose pose = state.armPose;
/*    */     
/* 25 */     if (pose == PiglinArmPose.DANCING) {
/* 26 */       float dancePos = state.ageInTicks / 60.0F;
/* 27 */       this.rightEar.zRot = 0.5235988F + 0.017453292F * Mth.sin((dancePos * 30.0F)) * 10.0F;
/* 28 */       this.leftEar.zRot = -0.5235988F - 0.017453292F * Mth.cos((dancePos * 30.0F)) * 10.0F;
/* 29 */       this.head.x += Mth.sin((dancePos * 10.0F));
/* 30 */       this.head.y += Mth.sin((dancePos * 40.0F)) + 0.4F;
/* 31 */       this.rightArm.zRot = 0.017453292F * (70.0F + Mth.cos((dancePos * 40.0F)) * 10.0F);
/* 32 */       this.rightArm.zRot *= -1.0F;
/*    */       
/* 34 */       this.rightArm.y += Mth.sin((dancePos * 40.0F)) * 0.5F - 0.5F;
/* 35 */       this.leftArm.y += Mth.sin((dancePos * 40.0F)) * 0.5F + 0.5F;
/*    */       
/* 37 */       this.body.y += Mth.sin((dancePos * 40.0F)) * 0.35F;
/* 38 */     } else if (pose == PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON && attackTime == 0.0F) {
/*    */       
/* 40 */       holdWeaponHigh(state);
/* 41 */     } else if (pose == PiglinArmPose.CROSSBOW_HOLD) {
/* 42 */       AnimationUtils.animateCrossbowHold(this.rightArm, this.leftArm, this.head, (state.mainArm == HumanoidArm.RIGHT));
/* 43 */     } else if (pose == PiglinArmPose.CROSSBOW_CHARGE) {
/* 44 */       AnimationUtils.animateCrossbowCharge(this.rightArm, this.leftArm, state.maxCrossbowChageDuration, state.ticksUsingItem, (state.mainArm == HumanoidArm.RIGHT));
/* 45 */     } else if (pose == PiglinArmPose.ADMIRING_ITEM) {
/* 46 */       this.head.xRot = 0.5F;
/* 47 */       this.head.yRot = 0.0F;
/* 48 */       if (state.mainArm == HumanoidArm.LEFT) {
/* 49 */         this.rightArm.yRot = -0.5F;
/* 50 */         this.rightArm.xRot = -0.9F;
/*    */       } else {
/* 52 */         this.leftArm.yRot = 0.5F;
/* 53 */         this.leftArm.xRot = -0.9F;
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void setupAttackAnimation(PiglinRenderState state) {
/* 60 */     float attackTime = state.attackTime;
/* 61 */     if (attackTime > 0.0F && state.armPose == PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON) {
/* 62 */       AnimationUtils.swingWeaponDown(this.rightArm, this.leftArm, state.mainArm, attackTime, state.ageInTicks);
/*    */       return;
/*    */     } 
/* 65 */     super.setupAttackAnimation((HumanoidRenderState)state);
/*    */   }
/*    */   
/*    */   private void holdWeaponHigh(PiglinRenderState state) {
/* 69 */     if (state.mainArm == HumanoidArm.LEFT) {
/* 70 */       this.leftArm.xRot = -1.8F;
/*    */     } else {
/* 72 */       this.rightArm.xRot = -1.8F;
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void setAllVisible(boolean visible) {
/* 78 */     super.setAllVisible(visible);
/* 79 */     this.leftSleeve.visible = visible;
/* 80 */     this.rightSleeve.visible = visible;
/* 81 */     this.leftPants.visible = visible;
/* 82 */     this.rightPants.visible = visible;
/* 83 */     this.jacket.visible = visible;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/model/monster/piglin/PiglinModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */