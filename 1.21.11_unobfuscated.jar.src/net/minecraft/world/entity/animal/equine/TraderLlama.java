/*     */ package net.minecraft.world.entity.animal.equine;
/*     */ 
/*     */ import java.util.EnumSet;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.world.DifficultyInstance;
/*     */ import net.minecraft.world.entity.AgeableMob;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.entity.PathfinderMob;
/*     */ import net.minecraft.world.entity.SpawnGroupData;
/*     */ import net.minecraft.world.entity.ai.goal.Goal;
/*     */ import net.minecraft.world.entity.ai.goal.PanicGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
/*     */ import net.minecraft.world.entity.ai.goal.target.TargetGoal;
/*     */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*     */ import net.minecraft.world.entity.monster.illager.AbstractIllager;
/*     */ import net.minecraft.world.entity.monster.zombie.Zombie;
/*     */ import net.minecraft.world.entity.npc.wanderingtrader.WanderingTrader;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.ServerLevelAccessor;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import net.minecraft.world.level.storage.ValueOutput;
/*     */ 
/*     */ public class TraderLlama extends Llama {
/*  29 */   private int despawnDelay = 47999; private static final int DEFAULT_DESPAWN_DELAY = 47999;
/*     */   
/*     */   public TraderLlama(EntityType<? extends TraderLlama> type, Level level) {
/*  32 */     super((EntityType)type, level);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isTraderLlama() {
/*  37 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Llama makeNewLlama() {
/*  42 */     return (Llama)EntityType.TRADER_LLAMA.create(level(), EntitySpawnReason.BREEDING);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addAdditionalSaveData(ValueOutput output) {
/*  47 */     super.addAdditionalSaveData(output);
/*  48 */     output.putInt("DespawnDelay", this.despawnDelay);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void readAdditionalSaveData(ValueInput input) {
/*  53 */     super.readAdditionalSaveData(input);
/*  54 */     this.despawnDelay = input.getIntOr("DespawnDelay", 47999);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void registerGoals() {
/*  59 */     super.registerGoals();
/*     */     
/*  61 */     this.goalSelector.addGoal(1, (Goal)new PanicGoal((PathfinderMob)this, 2.0D));
/*     */     
/*  63 */     this.targetSelector.addGoal(1, (Goal)new TraderLlamaDefendWanderingTraderGoal(this));
/*  64 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, Zombie.class, true, (target, level) -> (target.getType() != EntityType.ZOMBIFIED_PIGLIN)));
/*  65 */     this.targetSelector.addGoal(2, (Goal)new NearestAttackableTargetGoal((Mob)this, AbstractIllager.class, true));
/*     */   }
/*     */   
/*     */   public void setDespawnDelay(int despawnDelay) {
/*  69 */     this.despawnDelay = despawnDelay;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void doPlayerRide(Player player) {
/*  74 */     Entity leashHolder = getLeashHolder();
/*  75 */     if (leashHolder instanceof WanderingTrader) {
/*     */       return;
/*     */     }
/*     */     
/*  79 */     super.doPlayerRide(player);
/*     */   }
/*     */ 
/*     */   
/*     */   public void aiStep() {
/*  84 */     super.aiStep();
/*     */     
/*  86 */     if (!level().isClientSide()) {
/*  87 */       maybeDespawn();
/*     */     }
/*     */   }
/*     */   
/*     */   private void maybeDespawn() {
/*  92 */     if (!canDespawn()) {
/*     */       return;
/*     */     }
/*     */     
/*  96 */     this.despawnDelay = isLeashedToWanderingTrader() ? (((WanderingTrader)getLeashHolder()).getDespawnDelay() - 1) : (this.despawnDelay - 1);
/*     */     
/*  98 */     if (this.despawnDelay <= 0) {
/*  99 */       removeLeash();
/* 100 */       discard();
/*     */     } 
/*     */   }
/*     */   
/*     */   private boolean canDespawn() {
/* 105 */     return (!isTamed() && 
/* 106 */       !isLeashedToSomethingOtherThanTheWanderingTrader() && 
/* 107 */       !hasExactlyOnePlayerPassenger());
/*     */   }
/*     */   
/*     */   private boolean isLeashedToWanderingTrader() {
/* 111 */     return getLeashHolder() instanceof WanderingTrader;
/*     */   }
/*     */   
/*     */   private boolean isLeashedToSomethingOtherThanTheWanderingTrader() {
/* 115 */     return (isLeashed() && !isLeashedToWanderingTrader());
/*     */   }
/*     */   
/*     */   public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, SpawnGroupData groupData) {
/*     */     AgeableMob.AgeableMobGroupData ageableMobGroupData;
/* 120 */     if (spawnReason == EntitySpawnReason.EVENT) {
/* 121 */       setAge(0);
/*     */     }
/*     */     
/* 124 */     if (groupData == null) {
/* 125 */       ageableMobGroupData = new AgeableMob.AgeableMobGroupData(false);
/*     */     }
/*     */     
/* 128 */     return super.finalizeSpawn(level, difficulty, spawnReason, (SpawnGroupData)ageableMobGroupData);
/*     */   }
/*     */   
/*     */   protected static class TraderLlamaDefendWanderingTraderGoal extends TargetGoal {
/*     */     private final Llama llama;
/*     */     private LivingEntity ownerLastHurtBy;
/*     */     private int timestamp;
/*     */     
/*     */     public TraderLlamaDefendWanderingTraderGoal(Llama tameAnimal) {
/* 137 */       super((Mob)tameAnimal, false);
/* 138 */       this.llama = tameAnimal;
/* 139 */       setFlags(EnumSet.of(Goal.Flag.TARGET));
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean canUse() {
/* 144 */       if (!this.llama.isLeashed()) {
/* 145 */         return false;
/*     */       }
/* 147 */       Entity leashHolder = this.llama.getLeashHolder();
/* 148 */       if (!(leashHolder instanceof WanderingTrader)) {
/* 149 */         return false;
/*     */       }
/*     */       
/* 152 */       WanderingTrader owner = (WanderingTrader)leashHolder;
/* 153 */       this.ownerLastHurtBy = owner.getLastHurtByMob();
/* 154 */       int timeStamp = owner.getLastHurtByMobTimestamp();
/* 155 */       return (timeStamp != this.timestamp && canAttack(this.ownerLastHurtBy, TargetingConditions.DEFAULT));
/*     */     }
/*     */ 
/*     */     
/*     */     public void start() {
/* 160 */       this.mob.setTarget(this.ownerLastHurtBy);
/*     */       
/* 162 */       Entity leashHolder = this.llama.getLeashHolder();
/* 163 */       if (leashHolder instanceof WanderingTrader) {
/* 164 */         this.timestamp = ((WanderingTrader)leashHolder).getLastHurtByMobTimestamp();
/*     */       }
/*     */       
/* 167 */       super.start();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/equine/TraderLlama.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */