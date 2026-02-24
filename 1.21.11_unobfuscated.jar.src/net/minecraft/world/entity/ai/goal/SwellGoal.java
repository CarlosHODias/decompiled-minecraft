/*    */ package net.minecraft.world.entity.ai.goal;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.monster.Creeper;
/*    */ 
/*    */ public class SwellGoal
/*    */   extends Goal {
/*    */   private final Creeper creeper;
/*    */   private LivingEntity target;
/*    */   
/*    */   public SwellGoal(Creeper creeper) {
/* 14 */     this.creeper = creeper;
/* 15 */     setFlags(EnumSet.of(Goal.Flag.MOVE));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 20 */     LivingEntity target = this.creeper.getTarget();
/* 21 */     return (this.creeper.getSwellDir() > 0 || (target != null && this.creeper.distanceToSqr((Entity)target) < 9.0D));
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 26 */     this.creeper.getNavigation().stop();
/* 27 */     this.target = this.creeper.getTarget();
/*    */   }
/*    */ 
/*    */   
/*    */   public void stop() {
/* 32 */     this.target = null;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean requiresUpdateEveryTick() {
/* 37 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 42 */     if (this.target == null) {
/* 43 */       this.creeper.setSwellDir(-1);
/*    */       
/*    */       return;
/*    */     } 
/* 47 */     if (this.creeper.distanceToSqr((Entity)this.target) > 49.0D) {
/* 48 */       this.creeper.setSwellDir(-1);
/*    */       
/*    */       return;
/*    */     } 
/* 52 */     if (!this.creeper.getSensing().hasLineOfSight((Entity)this.target)) {
/* 53 */       this.creeper.setSwellDir(-1);
/*    */       
/*    */       return;
/*    */     } 
/* 57 */     this.creeper.setSwellDir(1);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/goal/SwellGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */