/*    */ package net.minecraft.world.entity.ai.goal.target;
/*    */ 
/*    */ import java.util.EnumSet;
/*    */ import java.util.List;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.goal.Goal;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.animal.golem.IronGolem;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ public class DefendVillageTargetGoal extends TargetGoal {
/*    */   private final IronGolem golem;
/*    */   private LivingEntity potentialTarget;
/* 19 */   private final TargetingConditions attackTargeting = TargetingConditions.forCombat().range(64.0D);
/*    */   
/*    */   public DefendVillageTargetGoal(IronGolem golem) {
/* 22 */     super((Mob)golem, false, true);
/* 23 */     this.golem = golem;
/* 24 */     setFlags(EnumSet.of(Goal.Flag.TARGET));
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean canUse() {
/* 29 */     AABB grow = this.golem.getBoundingBox().inflate(10.0D, 8.0D, 10.0D);
/* 30 */     ServerLevel level = getServerLevel((Entity)this.golem);
/* 31 */     List<? extends LivingEntity> villagers = level.getNearbyEntities(Villager.class, this.attackTargeting, (LivingEntity)this.golem, grow);
/* 32 */     List<Player> players = level.getNearbyPlayers(this.attackTargeting, (LivingEntity)this.golem, grow);
/*    */     
/* 34 */     for (LivingEntity livingEntity1 : villagers) {
/* 35 */       Villager villager = (Villager)livingEntity1;
/* 36 */       for (Player player : players) {
/* 37 */         int reputation = villager.getPlayerReputation(player);
/*    */         
/* 39 */         if (reputation <= -100) {
/* 40 */           this.potentialTarget = (LivingEntity)player;
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 45 */     if (this.potentialTarget == null) {
/* 46 */       return false;
/*    */     }
/*    */     
/* 49 */     LivingEntity livingEntity = this.potentialTarget; if (livingEntity instanceof Player) { Player player = (Player)livingEntity; if (player.isSpectator() || player.isCreative()) {
/* 50 */         return false;
/*    */       } }
/*    */     
/* 53 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public void start() {
/* 58 */     this.golem.setTarget(this.potentialTarget);
/* 59 */     super.start();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/goal/target/DefendVillageTargetGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */