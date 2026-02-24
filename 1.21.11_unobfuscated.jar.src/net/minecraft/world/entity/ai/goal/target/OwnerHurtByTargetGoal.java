/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.TamableAnimal;
/*    */ import net.minecraft.world.entity.ai.goal.Goal;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ 
/*    */ public class OwnerHurtByTargetGoal extends TargetGoal {
/*    */   private final TamableAnimal tameAnimal;
/*    */   private LivingEntity ownerLastHurtBy;
/*    */   private int timestamp;
/*    */   
/*    */   public OwnerHurtByTargetGoal(TamableAnimal tameAnimal) {
/* 16 */     super((Mob)tameAnimal, false);
/* 17 */     this.tameAnimal = tameAnimal;
/* 18 */     setFlags(EnumSet.of(Goal.Flag.TARGET));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 23 */     if (!this.tameAnimal.isTame() || this.tameAnimal.isOrderedToSit()) {
/* 24 */       return false;
/*    */     }
/* 26 */     LivingEntity owner = this.tameAnimal.getOwner();
/* 27 */     if (owner == null) {
/* 28 */       return false;
/*    */     }
/* 30 */     this.ownerLastHurtBy = owner.getLastHurtByMob();
/* 31 */     int ts = owner.getLastHurtByMobTimestamp();
/* 32 */     return (ts != this.timestamp && canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT) && this.tameAnimal.wantsToAttack(this.ownerLastHurtBy, owner));
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 37 */     this.mob.setTarget(this.ownerLastHurtBy);
/*    */     
/* 39 */     LivingEntity owner = this.tameAnimal.getOwner();
/* 40 */     if (owner != null) {
/* 41 */       this.timestamp = owner.getLastHurtByMobTimestamp();
/*    */     }
/*    */     
/* 44 */     super.start();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/goal/target/OwnerHurtByTargetGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */