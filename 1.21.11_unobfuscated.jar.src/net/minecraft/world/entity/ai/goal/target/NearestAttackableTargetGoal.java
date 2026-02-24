/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.goal.Goal;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class NearestAttackableTargetGoal<T extends LivingEntity>
/*    */   extends TargetGoal
/*    */ {
/*    */   private static final int DEFAULT_RANDOM_INTERVAL = 10;
/*    */   protected final Class<T> targetType;
/*    */   protected final int randomInterval;
/*    */   protected LivingEntity target;
/*    */   protected TargetingConditions targetConditions;
/*    */   
/*    */   public NearestAttackableTargetGoal(Mob mob, Class<T> targetType, boolean mustSee) {
/* 24 */     this(mob, targetType, 10, mustSee, false, null);
/*    */   }
/*    */   
/*    */   public NearestAttackableTargetGoal(Mob mob, Class<T> targetType, boolean mustSee, TargetingConditions.Selector selector) {
/* 28 */     this(mob, targetType, 10, mustSee, false, selector);
/*    */   }
/*    */   
/*    */   public NearestAttackableTargetGoal(Mob mob, Class<T> targetType, boolean mustSee, boolean mustReach) {
/* 32 */     this(mob, targetType, 10, mustSee, mustReach, null);
/*    */   }
/*    */   
/*    */   public NearestAttackableTargetGoal(Mob mob, Class<T> targetType, int randomInterval, boolean mustSee, boolean mustReach, TargetingConditions.Selector selector) {
/* 36 */     super(mob, mustSee, mustReach);
/* 37 */     this.targetType = targetType;
/* 38 */     this.randomInterval = reducedTickDelay(randomInterval);
/* 39 */     setFlags(EnumSet.of(Goal.Flag.TARGET));
/*    */     
/* 41 */     this.targetConditions = TargetingConditions.forCombat().range(getFollowDistance()).selector(selector);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 46 */     if (this.randomInterval > 0 && this.mob.getRandom().nextInt(this.randomInterval) != 0) {
/* 47 */       return false;
/*    */     }
/*    */     
/* 50 */     findTarget();
/* 51 */     return (this.target != null);
/*    */   }
/*    */   
/*    */   protected AABB getTargetSearchArea(double followDistance) {
/* 55 */     return this.mob.getBoundingBox().inflate(followDistance, followDistance, followDistance);
/*    */   }
/*    */   
/*    */   protected void findTarget() {
/* 59 */     ServerLevel level = getServerLevel((Entity)this.mob);
/* 60 */     if (this.targetType == Player.class || this.targetType == ServerPlayer.class) {
/* 61 */       this.target = (LivingEntity)level.getNearestPlayer(getTargetConditions(), (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
/*    */     } else {
/* 63 */       this.target = level.getNearestEntity(this.mob.level().getEntitiesOfClass(this.targetType, getTargetSearchArea(getFollowDistance()), entity -> true), getTargetConditions(), (LivingEntity)this.mob, this.mob.getX(), this.mob.getEyeY(), this.mob.getZ());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 69 */     this.mob.setTarget(this.target);
/* 70 */     super.start();
/*    */   }
/*    */   
/*    */   public void setTarget(LivingEntity target) {
/* 74 */     this.target = target;
/*    */   }
/*    */   
/*    */   private TargetingConditions getTargetConditions() {
/* 78 */     return this.targetConditions.range(getFollowDistance());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/goal/target/NearestAttackableTargetGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */