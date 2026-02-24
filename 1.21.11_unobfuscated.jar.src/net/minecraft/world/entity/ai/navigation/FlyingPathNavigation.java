/*    */ package net.minecraft.world.entity.ai.navigation;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.pathfinder.FlyNodeEvaluator;
/*    */ import net.minecraft.world.level.pathfinder.NodeEvaluator;
/*    */ import net.minecraft.world.level.pathfinder.Path;
/*    */ import net.minecraft.world.level.pathfinder.PathFinder;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class FlyingPathNavigation extends PathNavigation {
/*    */   public FlyingPathNavigation(Mob mob, Level level) {
/* 15 */     super(mob, level);
/*    */   }
/*    */ 
/*    */   
/*    */   protected PathFinder createPathFinder(int maxVisitedNodes) {
/* 20 */     this.nodeEvaluator = (NodeEvaluator)new FlyNodeEvaluator();
/* 21 */     return new PathFinder(this.nodeEvaluator, maxVisitedNodes);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canMoveDirectly(Vec3 startPos, Vec3 stopPos) {
/* 26 */     return isClearForMovementBetween(this.mob, startPos, stopPos, true);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canUpdatePath() {
/* 31 */     return ((canFloat() && this.mob.isInLiquid()) || !this.mob.isPassenger());
/*    */   }
/*    */ 
/*    */   
/*    */   protected Vec3 getTempMobPos() {
/* 36 */     return this.mob.position();
/*    */   }
/*    */ 
/*    */   
/*    */   public Path createPath(Entity target, int reachRange) {
/* 41 */     return createPath(target.blockPosition(), reachRange);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 46 */     this.tick++;
/*    */     
/* 48 */     if (this.hasDelayedRecomputation) {
/* 49 */       recomputePath();
/*    */     }
/*    */     
/* 52 */     if (isDone()) {
/*    */       return;
/*    */     }
/*    */     
/* 56 */     if (canUpdatePath()) {
/* 57 */       followThePath();
/* 58 */     } else if (this.path != null && !this.path.isDone()) {
/* 59 */       Vec3 pos = this.path.getNextEntityPos((Entity)this.mob);
/* 60 */       if (this.mob.getBlockX() == Mth.floor(pos.x) && this.mob.getBlockY() == Mth.floor(pos.y) && this.mob.getBlockZ() == Mth.floor(pos.z)) {
/* 61 */         this.path.advance();
/*    */       }
/*    */     } 
/*    */     
/* 65 */     if (isDone()) {
/*    */       return;
/*    */     }
/* 68 */     Vec3 target = this.path.getNextEntityPos((Entity)this.mob);
/*    */     
/* 70 */     this.mob.getMoveControl().setWantedPosition(target.x, target.y, target.z, this.speedModifier);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isStableDestination(BlockPos pos) {
/* 75 */     return this.level.getBlockState(pos).entityCanStandOn((net.minecraft.world.level.BlockGetter)this.level, pos, (Entity)this.mob);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canNavigateGround() {
/* 80 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/navigation/FlyingPathNavigation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */