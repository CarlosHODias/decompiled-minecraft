/*    */ package net.minecraft.world.entity.ai.navigation;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*    */ import net.minecraft.world.level.pathfinder.PathFinder;
/*    */ import net.minecraft.world.level.pathfinder.SwimNodeEvaluator;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class WaterBoundPathNavigation
/*    */   extends PathNavigation {
/*    */   public WaterBoundPathNavigation(Mob mob, Level level) {
/* 15 */     super(mob, level);
/*    */   }
/*    */   private boolean allowBreaching;
/*    */   
/*    */   protected PathFinder createPathFinder(int maxVisitedNodes) {
/* 20 */     this.allowBreaching = (this.mob.getType() == EntityType.DOLPHIN);
/* 21 */     this.nodeEvaluator = (NodeEvaluator)new SwimNodeEvaluator(this.allowBreaching);
/*    */     
/* 23 */     this.nodeEvaluator.setCanPassDoors(false);
/* 24 */     return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canUpdatePath() {
/* 29 */     return (this.allowBreaching || this.mob.isInLiquid());
/*    */   }
/*    */ 
/*    */   
/*    */   protected Vec3 getTempMobPos() {
/* 34 */     return new Vec3(this.mob.getX(), this.mob.getY(0.5D), this.mob.getZ());
/*    */   }
/*    */ 
/*    */   
/*    */   protected double getGroundY(Vec3 target) {
/* 39 */     return target.y;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canMoveDirectly(Vec3 startPos, Vec3 stopPos) {
/* 44 */     return isClearForMovementBetween(this.mob, startPos, stopPos, false);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isStableDestination(BlockPos pos) {
/* 49 */     return !this.level.getBlockState(pos).isSolidRender();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setCanFloat(boolean canFloat) {}
/*    */ 
/*    */   
/*    */   public boolean canNavigateGround() {
/* 58 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/navigation/WaterBoundPathNavigation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */