/*    */ package net.minecraft.world.entity.ai.control;
/*    */ 
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ 
/*    */ public class BodyRotationControl
/*    */   implements Control
/*    */ {
/*    */   private final Mob mob;
/*    */   private static final int HEAD_STABLE_ANGLE = 15;
/*    */   private static final int DELAY_UNTIL_STARTING_TO_FACE_FORWARD = 10;
/*    */   private static final int HOW_LONG_IT_TAKES_TO_FACE_FORWARD = 10;
/*    */   private int headStableTime;
/*    */   private float lastStableYHeadRot;
/*    */   
/*    */   public BodyRotationControl(Mob mob) {
/* 17 */     this.mob = mob;
/*    */   }
/*    */   
/*    */   public void clientTick() {
/* 21 */     if (isMoving()) {
/* 22 */       this.mob.yBodyRot = this.mob.getYRot();
/* 23 */       rotateHeadIfNecessary();
/*    */       
/* 25 */       this.lastStableYHeadRot = this.mob.yHeadRot;
/* 26 */       this.headStableTime = 0;
/*    */       
/*    */       return;
/*    */     } 
/* 30 */     if (notCarryingMobPassengers()) {
/* 31 */       if (Math.abs(this.mob.yHeadRot - this.lastStableYHeadRot) > 15.0F) {
/*    */ 
/*    */         
/* 34 */         this.headStableTime = 0;
/* 35 */         this.lastStableYHeadRot = this.mob.yHeadRot;
/* 36 */         rotateBodyIfNecessary();
/*    */       } else {
/* 38 */         this.headStableTime++;
/* 39 */         if (this.headStableTime > 10)
/*    */         {
/*    */           
/* 42 */           rotateHeadTowardsFront();
/*    */         }
/*    */       } 
/*    */     }
/*    */   }
/*    */   
/*    */   private void rotateBodyIfNecessary() {
/* 49 */     this.mob.yBodyRot = Mth.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, this.mob.getMaxHeadYRot());
/*    */   }
/*    */   
/*    */   private void rotateHeadIfNecessary() {
/* 53 */     this.mob.yHeadRot = Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, this.mob.getMaxHeadYRot());
/*    */   }
/*    */   
/*    */   private void rotateHeadTowardsFront() {
/* 57 */     int timeSinceStartingToFaceForward = this.headStableTime - 10;
/*    */ 
/*    */     
/* 60 */     float faceForwardFraction = Mth.clamp(timeSinceStartingToFaceForward / 10.0F, 0.0F, 1.0F);
/*    */     
/* 62 */     float angleRemainingUntilFacingForward = this.mob.getMaxHeadYRot() * (1.0F - faceForwardFraction);
/*    */     
/* 64 */     this.mob.yBodyRot = Mth.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, angleRemainingUntilFacingForward);
/*    */   }
/*    */   
/*    */   private boolean notCarryingMobPassengers() {
/* 68 */     return !(this.mob.getFirstPassenger() instanceof Mob);
/*    */   }
/*    */   
/*    */   private boolean isMoving() {
/* 72 */     double xd = this.mob.getX() - this.mob.xo;
/* 73 */     double zd = this.mob.getZ() - this.mob.zo;
/*    */     
/* 75 */     return (xd * xd + zd * zd > 2.500000277905201E-7D);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/control/BodyRotationControl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */