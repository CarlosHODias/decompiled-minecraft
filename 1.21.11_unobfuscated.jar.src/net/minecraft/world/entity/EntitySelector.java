/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.google.common.base.Predicates;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.scores.PlayerTeam;
/*    */ import net.minecraft.world.scores.Team;
/*    */ 
/*    */ public final class EntitySelector
/*    */ {
/*    */   public static final Predicate<Entity> LIVING_ENTITY_STILL_ALIVE;
/*    */   public static final Predicate<Entity> ENTITY_NOT_BEING_RIDDEN;
/*    */   public static final Predicate<Entity> CONTAINER_ENTITY_SELECTOR;
/* 14 */   public static final Predicate<Entity> ENTITY_STILL_ALIVE = Entity::isAlive; public static final Predicate<Entity> NO_CREATIVE_OR_SPECTATOR; public static final Predicate<Entity> NO_SPECTATORS; public static final Predicate<Entity> CAN_BE_COLLIDED_WITH; static {
/* 15 */     LIVING_ENTITY_STILL_ALIVE = (entity -> (entity.isAlive() && entity instanceof LivingEntity));
/* 16 */     ENTITY_NOT_BEING_RIDDEN = (entity -> (entity.isAlive() && !entity.isVehicle() && !entity.isPassenger()));
/* 17 */     CONTAINER_ENTITY_SELECTOR = (entity -> (entity instanceof net.minecraft.world.Container && entity.isAlive()));
/* 18 */     NO_CREATIVE_OR_SPECTATOR = (entity -> { if (entity instanceof Player) { Player player = (Player)entity; if (!entity.isSpectator() && !player.isCreative()); return false; } 
/* 19 */       }); NO_SPECTATORS = (entity -> !entity.isSpectator());
/* 20 */     CAN_BE_COLLIDED_WITH = NO_SPECTATORS.and(entity -> entity.canBeCollidedWith(null));
/* 21 */   } public static final Predicate<Entity> CAN_BE_PICKED = NO_SPECTATORS.and(Entity::isPickable);
/*    */   
/*    */   public static Predicate<Entity> withinDistance(double centerX, double centerY, double centerZ, double distance) {
/* 24 */     double distanceSqr = distance * distance;
/* 25 */     return input -> (input.distanceToSqr(centerX, centerY, centerZ) <= distanceSqr);
/*    */   }
/*    */   
/*    */   public static Predicate<Entity> pushableBy(Entity entity) {
/* 29 */     PlayerTeam playerTeam = entity.getTeam();
/* 30 */     Team.CollisionRule ownCollisionRule = (playerTeam == null) ? Team.CollisionRule.ALWAYS : playerTeam.getCollisionRule();
/* 31 */     if (ownCollisionRule == Team.CollisionRule.NEVER) {
/* 32 */       return (Predicate<Entity>)Predicates.alwaysFalse();
/*    */     }
/* 34 */     return NO_SPECTATORS.and(input -> {
/*    */           if (!input.isPushable())
/*    */             return false;  if (entity.level().isClientSide())
/*    */             if (input instanceof Player) {
/*    */               Player player = (Player)input; if (!player.isLocalPlayer())
/*    */                 return false; 
/*    */             } else {
/*    */               return false;
/*    */             }   PlayerTeam playerTeam = input.getTeam();
/*    */           Team.CollisionRule theirCollisionRule = (playerTeam == null) ? Team.CollisionRule.ALWAYS : playerTeam.getCollisionRule();
/*    */           if (theirCollisionRule == Team.CollisionRule.NEVER)
/*    */             return false; 
/* 46 */           boolean sameTeam = (ownTeam != null && ownTeam.isAlliedTo((Team)playerTeam));
/* 47 */           return ((ownCollisionRule == Team.CollisionRule.PUSH_OWN_TEAM || theirCollisionRule == Team.CollisionRule.PUSH_OWN_TEAM) && sameTeam) ? false : (
/*    */ 
/*    */             
/* 50 */             !((ownCollisionRule == Team.CollisionRule.PUSH_OTHER_TEAMS || theirCollisionRule == Team.CollisionRule.PUSH_OTHER_TEAMS) && !sameTeam));
/*    */         });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Predicate<Entity> notRiding(Entity entity) {
/* 58 */     return input -> {
/*    */         while (input.isPassenger()) {
/*    */           input = input.getVehicle();
/*    */           if (input == entity)
/*    */             return false; 
/*    */         } 
/*    */         return true;
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/EntitySelector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */