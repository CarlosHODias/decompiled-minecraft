/*    */ package net.minecraft.world.entity.ai.util;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class HoverRandomPos
/*    */ {
/*    */   public static Vec3 getPos(PathfinderMob mob, int horizontalDist, int verticalDist, double xDir, double zDir, float maxXzRadiansDifference, int hoverMaxHeight, int hoverMinHeight) {
/* 10 */     boolean restrict = GoalUtils.mobRestricted(mob, horizontalDist);
/*    */     
/* 12 */     return RandomPos.generateRandomPos(mob, () -> {
/*    */           BlockPos direction = RandomPos.generateRandomDirectionWithinRadians(mob.getRandom(), 0.0D, horizontalDist, verticalDist, 0, xDir, zDir, maxXzRadiansDifference);
/*    */           
/*    */           if (direction == null) {
/*    */             return null;
/*    */           }
/*    */           
/*    */           BlockPos pos = LandRandomPos.generateRandomPosTowardDirection(mob, horizontalDist, restrict, direction);
/*    */           if (pos == null) {
/*    */             return null;
/*    */           }
/*    */           pos = RandomPos.moveUpToAboveSolid(pos, mob.getRandom().nextInt(hoverMaxHeight - hoverMinHeight + 1) + hoverMinHeight, mob.level().getMaxY(), ());
/* 24 */           return (GoalUtils.isWater(mob, pos) || GoalUtils.hasMalus(mob, pos)) ? null : pos;
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/util/HoverRandomPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */