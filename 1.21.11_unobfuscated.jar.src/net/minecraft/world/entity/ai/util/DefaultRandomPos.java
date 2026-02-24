/*    */ package net.minecraft.world.entity.ai.util;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DefaultRandomPos
/*    */ {
/*    */   public static Vec3 getPos(PathfinderMob mob, int horizontalDist, int verticalDist) {
/* 12 */     boolean restrict = GoalUtils.mobRestricted(mob, horizontalDist);
/*    */     
/* 14 */     return RandomPos.generateRandomPos(mob, () -> {
/*    */           BlockPos direction = RandomPos.generateRandomDirection(mob.getRandom(), horizontalDist, verticalDist);
/*    */           return generateRandomPosTowardDirection(mob, horizontalDist, restrict, direction);
/*    */         });
/*    */   }
/*    */ 
/*    */   
/*    */   public static Vec3 getPosTowards(PathfinderMob mob, int horizontalDist, int verticalDist, Vec3 towardsPos, double maxXzRadiansFromDir) {
/* 22 */     Vec3 dir = towardsPos.subtract(mob.getX(), mob.getY(), mob.getZ());
/* 23 */     boolean restrict = GoalUtils.mobRestricted(mob, horizontalDist);
/*    */     
/* 25 */     return RandomPos.generateRandomPos(mob, () -> {
/*    */           BlockPos direction = RandomPos.generateRandomDirectionWithinRadians(mob.getRandom(), 0.0D, horizontalDist, verticalDist, 0, dir.x, dir.z, maxXzRadiansFromDir);
/*    */           return (direction == null) ? null : generateRandomPosTowardDirection(mob, horizontalDist, restrict, direction);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Vec3 getPosAway(PathfinderMob mob, int horizontalDist, int verticalDist, Vec3 avoidPos) {
/* 37 */     Vec3 dirAway = mob.position().subtract(avoidPos);
/* 38 */     boolean restrict = GoalUtils.mobRestricted(mob, horizontalDist);
/*    */     
/* 40 */     return RandomPos.generateRandomPos(mob, () -> {
/*    */           BlockPos direction = RandomPos.generateRandomDirectionWithinRadians(mob.getRandom(), 0.0D, horizontalDist, verticalDist, 0, dirAway.x, dirAway.z, 1.5707963705062866D);
/*    */           return (direction == null) ? null : generateRandomPosTowardDirection(mob, horizontalDist, restrict, direction);
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static BlockPos generateRandomPosTowardDirection(PathfinderMob mob, int horizontalDist, boolean restrict, BlockPos direction) {
/* 51 */     BlockPos pos = RandomPos.generateRandomPosTowardDirection(mob, horizontalDist, mob.getRandom(), direction);
/* 52 */     if (GoalUtils.isOutsideLimits(pos, mob) || GoalUtils.isRestricted(restrict, mob, pos) || GoalUtils.isNotStable(mob.getNavigation(), pos) || GoalUtils.hasMalus(mob, pos)) {
/* 53 */       return null;
/*    */     }
/*    */     
/* 56 */     return pos;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/util/DefaultRandomPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */