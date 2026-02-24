/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.raid.Raider;
/*    */ 
/*    */ public class NearestAttackableWitchTargetGoal<T extends LivingEntity> extends NearestAttackableTargetGoal<T> {
/*    */   private boolean canAttack;
/*    */   
/*    */   public NearestAttackableWitchTargetGoal(Raider raider, Class<T> targetType, int randomInterval, boolean mustSee, boolean mustReach, TargetingConditions.Selector subselector) {
/* 12 */     super((Mob)raider, targetType, randomInterval, mustSee, mustReach, subselector);
/* 13 */     this.canAttack = true;
/*    */   }
/*    */   
/*    */   public void setCanAttack(boolean canAttack) {
/* 17 */     this.canAttack = canAttack;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 22 */     return (this.canAttack && super.canUse());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/goal/target/NearestAttackableWitchTargetGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */