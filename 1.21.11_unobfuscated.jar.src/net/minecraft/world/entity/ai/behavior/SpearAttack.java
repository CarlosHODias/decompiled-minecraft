/*     */ package net.minecraft.world.entity.ai.behavior;
/*     */ 
/*     */ import java.util.Map;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.InteractionHand;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*     */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*     */ import net.minecraft.world.entity.ai.util.LandRandomPos;
/*     */ import net.minecraft.world.item.component.KineticWeapon;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ 
/*     */ public class SpearAttack
/*     */   extends Behavior<PathfinderMob>
/*     */ {
/*     */   public static final int MIN_REPOSITION_DISTANCE = 6;
/*     */   public static final int MAX_REPOSITION_DISTANCE = 7;
/*     */   double speedModifierWhenCharging;
/*     */   double speedModifierWhenRepositioning;
/*     */   float approachDistanceSq;
/*     */   float targetInRangeRadiusSq;
/*     */   
/*     */   public SpearAttack(double speedModifierWhenCharging, double speedModifierWhenRepositioning, float approachDistance, float targetInRangeRadius) {
/*  30 */     super(Map.of(MemoryModuleType.SPEAR_STATUS, MemoryStatus.VALUE_PRESENT));
/*  31 */     this.speedModifierWhenCharging = speedModifierWhenCharging;
/*  32 */     this.speedModifierWhenRepositioning = speedModifierWhenRepositioning;
/*  33 */     this.approachDistanceSq = approachDistance * approachDistance;
/*  34 */     this.targetInRangeRadiusSq = targetInRangeRadius * targetInRangeRadius;
/*     */   }
/*     */ 
/*     */   
/*     */   private LivingEntity getTarget(PathfinderMob mob) {
/*  39 */     return mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
/*     */   }
/*     */   
/*     */   private boolean ableToAttack(PathfinderMob mob) {
/*  43 */     return (getTarget(mob) != null && mob.getMainHandItem().has(DataComponents.KINETIC_WEAPON));
/*     */   }
/*     */   
/*     */   private int getKineticWeaponUseDuration(PathfinderMob mob) {
/*  47 */     return (Integer)Optional.<KineticWeapon>ofNullable((KineticWeapon)mob.getMainHandItem().get(DataComponents.KINETIC_WEAPON)).map(KineticWeapon::computeDamageUseDuration).orElse(0);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean checkExtraStartConditions(ServerLevel level, PathfinderMob body) {
/*  52 */     return (body.getBrain().getMemory(MemoryModuleType.SPEAR_STATUS).orElse(SpearStatus.APPROACH) == SpearStatus.CHARGING && 
/*  53 */       ableToAttack(body) && !body.isUsingItem());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void start(ServerLevel level, PathfinderMob body, long timestamp) {
/*  58 */     body.setAggressive(true);
/*  59 */     body.getBrain().setMemory(MemoryModuleType.SPEAR_ENGAGE_TIME, getKineticWeaponUseDuration(body));
/*     */     
/*  61 */     body.getBrain().eraseMemory(MemoryModuleType.SPEAR_CHARGE_POSITION);
/*  62 */     body.startUsingItem(InteractionHand.MAIN_HAND);
/*  63 */     super.start(level, body, timestamp);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean canStillUse(ServerLevel level, PathfinderMob body, long timestamp) {
/*  68 */     return ((Integer)body.getBrain().getMemory(MemoryModuleType.SPEAR_ENGAGE_TIME).orElse(0) > 0 && ableToAttack(body));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void tick(ServerLevel level, PathfinderMob mob, long timestamp) {
/*  73 */     LivingEntity target = getTarget(mob);
/*  74 */     double targetDistSqr = mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
/*  75 */     Entity mount = mob.getRootVehicle();
/*  76 */     float speedModifier = 1.0F;
/*  77 */     if (mount instanceof Mob) { Mob vehicleMob = (Mob)mount;
/*  78 */       speedModifier = vehicleMob.chargeSpeedModifier(); }
/*     */     
/*  80 */     int mountDistance = mob.isPassenger() ? 2 : 0;
/*     */     
/*  82 */     mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker((Entity)target, true));
/*     */     
/*  84 */     mob.getBrain().setMemory(MemoryModuleType.SPEAR_ENGAGE_TIME, (Integer)mob.getBrain().getMemory(MemoryModuleType.SPEAR_ENGAGE_TIME).orElse(0) - 1);
/*     */     
/*  86 */     Vec3 awayPos = mob.getBrain().getMemory(MemoryModuleType.SPEAR_CHARGE_POSITION).orElse(null);
/*     */ 
/*     */     
/*  89 */     if (awayPos != null) {
/*  90 */       mob.getNavigation().moveTo(awayPos.x, awayPos.y, awayPos.z, speedModifier * this.speedModifierWhenRepositioning);
/*  91 */       if (mob.getNavigation().isDone()) {
/*  92 */         mob.getBrain().eraseMemory(MemoryModuleType.SPEAR_CHARGE_POSITION);
/*     */       }
/*     */     } else {
/*  95 */       mob.getNavigation().moveTo((Entity)target, speedModifier * this.speedModifierWhenCharging);
/*     */       
/*  97 */       if (targetDistSqr < this.targetInRangeRadiusSq || mob.getNavigation().isDone()) {
/*  98 */         double distance = Math.sqrt(targetDistSqr);
/*     */         
/* 100 */         Vec3 newAwayPos = LandRandomPos.getPosAway(mob, (6 + mountDistance) - distance, (7 + mountDistance) - distance, 7, target.position());
/* 101 */         mob.getBrain().setMemory(MemoryModuleType.SPEAR_CHARGE_POSITION, newAwayPos);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void stop(ServerLevel level, PathfinderMob body, long timestamp) {
/* 108 */     body.getNavigation().stop();
/* 109 */     body.stopUsingItem();
/* 110 */     body.getBrain().eraseMemory(MemoryModuleType.SPEAR_CHARGE_POSITION);
/* 111 */     body.getBrain().eraseMemory(MemoryModuleType.SPEAR_ENGAGE_TIME);
/* 112 */     body.getBrain().setMemory(MemoryModuleType.SPEAR_STATUS, SpearStatus.RETREAT);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean timedOut(long timestamp) {
/* 117 */     return false;
/*     */   }
/*     */   
/*     */   public enum SpearStatus {
/* 121 */     APPROACH,
/* 122 */     CHARGING,
/* 123 */     RETREAT;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/SpearAttack.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */