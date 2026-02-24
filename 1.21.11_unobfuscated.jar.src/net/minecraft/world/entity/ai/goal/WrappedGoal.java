/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ 
/*     */ public class WrappedGoal
/*     */   extends Goal
/*     */ {
/*     */   private final Goal goal;
/*     */   private final int priority;
/*     */   private boolean isRunning;
/*     */   
/*     */   public WrappedGoal(int priority, Goal goal) {
/*  13 */     this.priority = priority;
/*  14 */     this.goal = goal;
/*     */   }
/*     */   
/*     */   public boolean canBeReplacedBy(WrappedGoal goal) {
/*  18 */     return (isInterruptable() && goal.getPriority() < getPriority());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  23 */     return this.goal.canUse();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  28 */     return this.goal.canContinueToUse();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInterruptable() {
/*  33 */     return this.goal.isInterruptable();
/*     */   }
/*     */ 
/*     */   
/*     */   public void start() {
/*  38 */     if (this.isRunning) {
/*     */       return;
/*     */     }
/*  41 */     this.isRunning = true;
/*  42 */     this.goal.start();
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  47 */     if (!this.isRunning) {
/*     */       return;
/*     */     }
/*  50 */     this.isRunning = false;
/*  51 */     this.goal.stop();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/*  56 */     return this.goal.requiresUpdateEveryTick();
/*     */   }
/*     */ 
/*     */   
/*     */   protected int adjustedTickDelay(int ticks) {
/*  61 */     return this.goal.adjustedTickDelay(ticks);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  66 */     this.goal.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void setFlags(EnumSet<Goal.Flag> requiredControlFlags) {
/*  71 */     this.goal.setFlags(requiredControlFlags);
/*     */   }
/*     */ 
/*     */   
/*     */   public EnumSet<Goal.Flag> getFlags() {
/*  76 */     return this.goal.getFlags();
/*     */   }
/*     */   
/*     */   public boolean isRunning() {
/*  80 */     return this.isRunning;
/*     */   }
/*     */   
/*     */   public int getPriority() {
/*  84 */     return this.priority;
/*     */   }
/*     */   
/*     */   public Goal getGoal() {
/*  88 */     return this.goal;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*  93 */     if (this == o) {
/*  94 */       return true;
/*     */     }
/*  96 */     if (o == null || getClass() != o.getClass()) {
/*  97 */       return false;
/*     */     }
/*  99 */     return this.goal.equals(((WrappedGoal)o).goal);
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 104 */     return this.goal.hashCode();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/goal/WrappedGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */