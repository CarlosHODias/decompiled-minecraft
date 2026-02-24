/*     */ package net.minecraft.world.entity.ai.goal;
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.util.TimeUtil;
/*     */ import net.minecraft.util.valueproviders.UniformInt;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.monster.CrossbowAttackMob;
/*     */ import net.minecraft.world.entity.monster.Monster;
/*     */ import net.minecraft.world.entity.monster.RangedAttackMob;
/*     */ import net.minecraft.world.entity.projectile.ProjectileUtil;
/*     */ import net.minecraft.world.item.CrossbowItem;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.item.Items;
/*     */ import net.minecraft.world.item.component.ChargedProjectiles;
/*     */ 
/*     */ public class RangedCrossbowAttackGoal<T extends Monster & RangedAttackMob & CrossbowAttackMob> extends Goal {
/*     */   private final T mob;
/*  19 */   public static final UniformInt PATHFINDING_DELAY_RANGE = TimeUtil.rangeOfSeconds(1, 2);
/*     */   
/*     */   private enum CrossbowState {
/*  22 */     UNCHARGED,
/*  23 */     CHARGING,
/*  24 */     CHARGED,
/*  25 */     READY_TO_ATTACK;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  30 */   private CrossbowState crossbowState = CrossbowState.UNCHARGED;
/*     */   private final double speedModifier;
/*     */   private final float attackRadiusSqr;
/*     */   private int seeTime;
/*     */   private int attackDelay;
/*     */   private int updatePathDelay;
/*     */   
/*     */   public RangedCrossbowAttackGoal(T mob, double speedModifier, float attackRadius) {
/*  38 */     this.mob = mob;
/*  39 */     this.speedModifier = speedModifier;
/*  40 */     this.attackRadiusSqr = attackRadius * attackRadius;
/*  41 */     setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canUse() {
/*  46 */     return (isValidTarget() && isHoldingCrossbow());
/*     */   }
/*     */   
/*     */   private boolean isHoldingCrossbow() {
/*  50 */     return this.mob.isHolding(Items.CROSSBOW);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canContinueToUse() {
/*  55 */     return (isValidTarget() && (canUse() || !this.mob.getNavigation().isDone()) && isHoldingCrossbow());
/*     */   }
/*     */   
/*     */   private boolean isValidTarget() {
/*  59 */     return (this.mob.getTarget() != null && this.mob.getTarget().isAlive());
/*     */   }
/*     */ 
/*     */   
/*     */   public void stop() {
/*  64 */     super.stop();
/*  65 */     this.mob.setAggressive(false);
/*  66 */     this.mob.setTarget(null);
/*  67 */     this.seeTime = 0;
/*  68 */     if (this.mob.isUsingItem()) {
/*  69 */       this.mob.stopUsingItem();
/*  70 */       ((CrossbowAttackMob)this.mob).setChargingCrossbow(false);
/*  71 */       this.mob.getUseItem().set(DataComponents.CHARGED_PROJECTILES, ChargedProjectiles.EMPTY);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean requiresUpdateEveryTick() {
/*  77 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  82 */     LivingEntity target = this.mob.getTarget();
/*  83 */     if (target == null) {
/*     */       return;
/*     */     }
/*     */     
/*  87 */     boolean hasLineOfSight = this.mob.getSensing().hasLineOfSight((Entity)target);
/*  88 */     boolean hadLineOfSight = (this.seeTime > 0);
/*     */     
/*  90 */     if (hasLineOfSight != hadLineOfSight) {
/*  91 */       this.seeTime = 0;
/*     */     }
/*     */     
/*  94 */     if (hasLineOfSight) {
/*  95 */       this.seeTime++;
/*     */     } else {
/*  97 */       this.seeTime--;
/*     */     } 
/*     */     
/* 100 */     double distanceToSqr = this.mob.distanceToSqr((Entity)target);
/* 101 */     boolean needsToMove = ((distanceToSqr > this.attackRadiusSqr || this.seeTime < 5) && this.attackDelay == 0);
/* 102 */     if (needsToMove) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 107 */       this.updatePathDelay--;
/* 108 */       if (this.updatePathDelay <= 0) {
/* 109 */         this.mob.getNavigation().moveTo((Entity)target, canRun() ? this.speedModifier : (this.speedModifier * 0.5D));
/* 110 */         this.updatePathDelay = PATHFINDING_DELAY_RANGE.sample(this.mob.getRandom());
/*     */       } 
/*     */     } else {
/* 113 */       this.updatePathDelay = 0;
/* 114 */       this.mob.getNavigation().stop();
/*     */     } 
/*     */     
/* 117 */     this.mob.getLookControl().setLookAt((Entity)target, 30.0F, 30.0F);
/*     */     
/* 119 */     if (this.crossbowState == CrossbowState.UNCHARGED) {
/* 120 */       if (!needsToMove) {
/* 121 */         this.mob.startUsingItem(ProjectileUtil.getWeaponHoldingHand((LivingEntity)this.mob, Items.CROSSBOW));
/* 122 */         this.crossbowState = CrossbowState.CHARGING;
/* 123 */         ((CrossbowAttackMob)this.mob).setChargingCrossbow(true);
/*     */       } 
/* 125 */     } else if (this.crossbowState == CrossbowState.CHARGING) {
/* 126 */       if (!this.mob.isUsingItem()) {
/* 127 */         this.crossbowState = CrossbowState.UNCHARGED;
/*     */       }
/* 129 */       int pullTime = this.mob.getTicksUsingItem();
/* 130 */       ItemStack useItem = this.mob.getUseItem();
/* 131 */       if (pullTime >= CrossbowItem.getChargeDuration(useItem, (LivingEntity)this.mob)) {
/* 132 */         this.mob.releaseUsingItem();
/*     */         
/* 134 */         this.crossbowState = CrossbowState.CHARGED;
/* 135 */         this.attackDelay = 20 + this.mob.getRandom().nextInt(20);
/* 136 */         ((CrossbowAttackMob)this.mob).setChargingCrossbow(false);
/*     */       } 
/* 138 */     } else if (this.crossbowState == CrossbowState.CHARGED) {
/* 139 */       this.attackDelay--;
/* 140 */       if (this.attackDelay == 0) {
/* 141 */         this.crossbowState = CrossbowState.READY_TO_ATTACK;
/*     */       }
/* 143 */     } else if (this.crossbowState == CrossbowState.READY_TO_ATTACK && 
/* 144 */       hasLineOfSight) {
/* 145 */       ((RangedAttackMob)this.mob).performRangedAttack(target, 1.0F);
/* 146 */       this.crossbowState = CrossbowState.UNCHARGED;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private boolean canRun() {
/* 152 */     return (this.crossbowState == CrossbowState.UNCHARGED);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/goal/RangedCrossbowAttackGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */