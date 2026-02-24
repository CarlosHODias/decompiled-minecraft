/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ 
/*     */ public abstract class MoveToBlockGoal
/*     */   extends Goal {
/*     */   private static final int GIVE_UP_TICKS = 1200;
/*     */   private static final int STAY_TICKS = 1200;
/*     */   private static final int INTERVAL_TICKS = 200;
/*     */   protected final PathfinderMob mob;
/*     */   public final double speedModifier;
/*     */   protected int nextStartTick;
/*     */   protected int tryTicks;
/*     */   private int maxStayTicks;
/*  20 */   protected BlockPos blockPos = BlockPos.ZERO;
/*     */   
/*     */   private boolean reachedTarget;
/*     */   private final int searchRange;
/*     */   private final int verticalSearchRange;
/*     */   protected int verticalSearchStart;
/*     */   
/*     */   public MoveToBlockGoal(PathfinderMob mob, double speedModifier, int searchRange) {
/*  28 */     this(mob, speedModifier, searchRange, 1);
/*     */   }
/*     */   
/*     */   public MoveToBlockGoal(PathfinderMob mob, double speedModifier, int searchRange, int verticalSearchRange) {
/*  32 */     this.mob = mob;
/*  33 */     this.speedModifier = speedModifier;
/*  34 */     this.searchRange = searchRange;
/*  35 */     this.verticalSearchStart = 0;
/*  36 */     this.verticalSearchRange = verticalSearchRange;
/*  37 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  42 */     if (this.nextStartTick > 0) {
/*  43 */       this.nextStartTick--;
/*  44 */       return false;
/*     */     } 
/*  46 */     this.nextStartTick = nextStartTick(this.mob);
/*  47 */     return findNearestBlock();
/*     */   }
/*     */   
/*     */   protected int nextStartTick(PathfinderMob mob) {
/*  51 */     return reducedTickDelay(200 + mob.getRandom().nextInt(200));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  56 */     return (this.tryTicks >= -this.maxStayTicks && this.tryTicks <= 1200 && isValidTarget((LevelReader)this.mob.level(), this.blockPos));
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  61 */     moveMobToBlock();
/*  62 */     this.tryTicks = 0;
/*  63 */     this.maxStayTicks = this.mob.getRandom().nextInt(this.mob.getRandom().nextInt(1200) + 1200) + 1200;
/*     */   }
/*     */   
/*     */   protected void moveMobToBlock() {
/*  67 */     this.mob.getNavigation().moveTo(this.blockPos.getX() + 0.5D, (this.blockPos.getY() + 1), this.blockPos.getZ() + 0.5D, this.speedModifier);
/*     */   }
/*     */   
/*     */   public double acceptedDistance() {
/*  71 */     return 1.0D;
/*     */   }
/*     */   
/*     */   protected BlockPos getMoveToTarget() {
/*  75 */     return this.blockPos.above();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/*  80 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  85 */     BlockPos moveToTarget = getMoveToTarget();
/*  86 */     if (!moveToTarget.closerToCenterThan((Position)this.mob.position(), acceptedDistance())) {
/*  87 */       this.reachedTarget = false;
/*  88 */       this.tryTicks++;
/*  89 */       if (shouldRecalculatePath()) {
/*  90 */         this.mob.getNavigation().moveTo(moveToTarget.getX() + 0.5D, moveToTarget.getY(), moveToTarget.getZ() + 0.5D, this.speedModifier);
/*     */       }
/*     */     } else {
/*  93 */       this.reachedTarget = true;
/*  94 */       this.tryTicks--;
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean shouldRecalculatePath() {
/*  99 */     return (this.tryTicks % 40 == 0);
/*     */   }
/*     */   
/*     */   protected boolean isReachedTarget() {
/* 103 */     return this.reachedTarget;
/*     */   }
/*     */   
/*     */   protected boolean findNearestBlock() {
/* 107 */     int horizontalSearch = this.searchRange;
/* 108 */     int verticalSearch = this.verticalSearchRange;
/* 109 */     BlockPos mobPos = this.mob.blockPosition();
/*     */     
/* 111 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 112 */     for (int y = this.verticalSearchStart; y <= verticalSearch; y = (y > 0) ? -y : (1 - y)) {
/* 113 */       for (int r = 0; r < horizontalSearch; r++) {
/* 114 */         for (int x = 0; x <= r; x = (x > 0) ? -x : (1 - x)) {
/*     */           
/* 116 */           int z = (x < r && x > -r) ? r : 0;
/* 117 */           for (; z <= r; z = (z > 0) ? -z : (1 - z)) {
/* 118 */             pos.setWithOffset((Vec3i)mobPos, x, y - 1, z);
/* 119 */             if (this.mob.isWithinHome((BlockPos)pos) && isValidTarget((LevelReader)this.mob.level(), (BlockPos)pos)) {
/* 120 */               this.blockPos = (BlockPos)pos;
/* 121 */               return true;
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/* 128 */     return false;
/*     */   }
/*     */   
/*     */   protected abstract boolean isValidTarget(LevelReader paramLevelReader, BlockPos paramBlockPos);
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/goal/MoveToBlockGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */