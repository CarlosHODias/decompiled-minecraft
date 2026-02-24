/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.TamableAnimal;
/*    */ 
/*    */ public class SitWhenOrderedToGoal extends Goal {
/*    */   private final TamableAnimal mob;
/*    */   
/*    */   public SitWhenOrderedToGoal(TamableAnimal mob) {
/* 12 */     this.mob = mob;
/* 13 */     setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 18 */     return this.mob.isOrderedToSit();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 23 */     boolean orderedToSit = this.mob.isOrderedToSit();
/*    */     
/* 25 */     if (!orderedToSit && !this.mob.isTame()) {
/* 26 */       return false;
/*    */     }
/* 28 */     if (this.mob.isInWater()) {
/* 29 */       return false;
/*    */     }
/* 31 */     if (!this.mob.onGround()) {
/* 32 */       return false;
/*    */     }
/*    */     
/* 35 */     LivingEntity owner = this.mob.getOwner();
/* 36 */     if (owner == null || owner.level() != this.mob.level()) {
/* 37 */       return true;
/*    */     }
/*    */     
/* 40 */     if (this.mob.distanceToSqr((Entity)owner) < 144.0D && owner.getLastHurtByMob() != null) {
/* 41 */       return false;
/*    */     }
/*    */     
/* 44 */     return orderedToSit;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 49 */     this.mob.getNavigation().stop();
/* 50 */     this.mob.setInSittingPose(true);
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 55 */     this.mob.setInSittingPose(false);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/goal/SitWhenOrderedToGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */