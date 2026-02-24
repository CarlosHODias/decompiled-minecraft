/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.tags.BlockTags;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ 
/*    */ public class EatBlockGoal
/*    */   extends Goal {
/*    */   private static final int EAT_ANIMATION_TICKS = 40;
/*    */   private static final Predicate<BlockState> IS_EDIBLE;
/*    */   
/*    */   static {
/* 21 */     IS_EDIBLE = (state -> state.is(BlockTags.EDIBLE_FOR_SHEEP));
/*    */   }
/*    */ 
/*    */   
/*    */   private final Mob mob;
/*    */   
/*    */   public EatBlockGoal(Mob mob) {
/* 28 */     this.mob = mob;
/* 29 */     this.level = mob.level();
/* 30 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
/*    */   }
/*    */   private final Level level; private int eatAnimationTick;
/*    */   
/*    */   public boolean canUse() {
/* 35 */     if (this.mob.getRandom().nextInt(adjustedTickDelay(this.mob.isBaby() ? 50 : 1000)) != 0) {
/* 36 */       return false;
/*    */     }
/*    */     
/* 39 */     BlockPos pos = this.mob.blockPosition();
/* 40 */     if (IS_EDIBLE.test(this.level.getBlockState(pos))) {
/* 41 */       return true;
/*    */     }
/* 43 */     if (this.level.getBlockState(pos.below()).is(Blocks.GRASS_BLOCK)) {
/* 44 */       return true;
/*    */     }
/* 46 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 51 */     this.eatAnimationTick = adjustedTickDelay(40);
/* 52 */     this.level.broadcastEntityEvent((Entity)this.mob, (byte)10);
/* 53 */     this.mob.getNavigation().stop();
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 58 */     this.eatAnimationTick = 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canContinueToUse() {
/* 63 */     return (this.eatAnimationTick > 0);
/*    */   }
/*    */   
/*    */   public int getEatAnimationTick() {
/* 67 */     return this.eatAnimationTick;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 72 */     this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
/* 73 */     if (this.eatAnimationTick != adjustedTickDelay(4)) {
/*    */       return;
/*    */     }
/*    */     
/* 77 */     BlockPos pos = this.mob.blockPosition();
/*    */     
/* 79 */     if (IS_EDIBLE.test(this.level.getBlockState(pos))) {
/* 80 */       if ((Boolean)getServerLevel(this.level).getGameRules().get(GameRules.MOB_GRIEFING)) {
/* 81 */         this.level.destroyBlock(pos, false);
/*    */       }
/* 83 */       this.mob.ate();
/*    */     } else {
/* 85 */       BlockPos below = pos.below();
/* 86 */       if (this.level.getBlockState(below).is(Blocks.GRASS_BLOCK)) {
/* 87 */         if ((Boolean)getServerLevel(this.level).getGameRules().get(GameRules.MOB_GRIEFING)) {
/* 88 */           this.level.levelEvent(2001, below, Block.getId(Blocks.GRASS_BLOCK.defaultBlockState()));
/* 89 */           this.level.setBlock(below, Blocks.DIRT.defaultBlockState(), 2);
/*    */         } 
/* 91 */         this.mob.ate();
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/goal/EatBlockGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */