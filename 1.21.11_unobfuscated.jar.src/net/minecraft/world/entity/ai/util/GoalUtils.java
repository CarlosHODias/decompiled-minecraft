/*    */ package net.minecraft.world.entity.ai.util;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.ai.navigation.PathNavigation;
/*    */ import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class GoalUtils {
/*    */   public static boolean hasGroundPathNavigation(Mob mob) {
/* 13 */     return mob.getNavigation().canNavigateGround();
/*    */   }
/*    */   
/*    */   public static boolean mobRestricted(PathfinderMob mob, double horizontalDist) {
/* 17 */     return (mob.hasHome() && mob.getHomePosition().closerToCenterThan((Position)mob.position(), mob.getHomeRadius() + horizontalDist + 1.0D));
/*    */   }
/*    */   
/*    */   public static boolean isOutsideLimits(BlockPos pos, PathfinderMob mob) {
/* 21 */     return mob.level().isOutsideBuildHeight(pos.getY());
/*    */   }
/*    */   
/*    */   public static boolean isRestricted(boolean restrict, PathfinderMob mob, BlockPos pos) {
/* 25 */     return (restrict && !mob.isWithinHome(pos));
/*    */   }
/*    */   
/*    */   public static boolean isRestricted(boolean restrict, PathfinderMob mob, Vec3 pos) {
/* 29 */     return (restrict && !mob.isWithinHome(pos));
/*    */   }
/*    */   
/*    */   public static boolean isNotStable(PathNavigation navigation, BlockPos pos) {
/* 33 */     return !navigation.isStableDestination(pos);
/*    */   }
/*    */   
/*    */   public static boolean isWater(PathfinderMob mob, BlockPos pos) {
/* 37 */     return mob.level().getFluidState(pos).is(FluidTags.WATER);
/*    */   }
/*    */   
/*    */   public static boolean hasMalus(PathfinderMob mob, BlockPos pos) {
/* 41 */     return (mob.getPathfindingMalus(WalkNodeEvaluator.getPathTypeStatic((Mob)mob, pos)) != 0.0F);
/*    */   }
/*    */   
/*    */   public static boolean isSolid(PathfinderMob mob, BlockPos pos) {
/* 45 */     return mob.level().getBlockState(pos).isSolid();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/util/GoalUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */