/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.PathfinderMob;
/*    */ import net.minecraft.world.entity.animal.feline.Cat;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ 
/*    */ public class CatLieOnBedGoal
/*    */   extends MoveToBlockGoal {
/*    */   private final Cat cat;
/*    */   
/*    */   public CatLieOnBedGoal(Cat cat, double speedModifier, int searchRange) {
/* 15 */     super((PathfinderMob)cat, speedModifier, searchRange, 6);
/* 16 */     this.cat = cat;
/* 17 */     this.verticalSearchStart = -2;
/* 18 */     setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 23 */     return (this.cat.isTame() && !this.cat.isOrderedToSit() && !this.cat.isLying() && super.canUse());
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 28 */     super.start();
/* 29 */     this.cat.setInSittingPose(false);
/*    */   }
/*    */ 
/*    */   
/*    */   protected int nextStartTick(PathfinderMob mob) {
/* 34 */     return 40;
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 39 */     super.stop();
/* 40 */     this.cat.setLying(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 45 */     super.tick();
/*    */     
/* 47 */     this.cat.setInSittingPose(false);
/* 48 */     if (!isReachedTarget()) {
/* 49 */       this.cat.setLying(false);
/* 50 */     } else if (!this.cat.isLying()) {
/* 51 */       this.cat.setLying(true);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isValidTarget(LevelReader level, BlockPos pos) {
/* 57 */     return (level.isEmptyBlock(pos.above()) && level.getBlockState(pos).is(BlockTags.BEDS));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/goal/CatLieOnBedGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */