/*    */ package net.minecraft.server.level;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.tags.TagKey;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.targeting.TargetingConditions;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.level.EntityGetter;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface ServerEntityGetter
/*    */   extends EntityGetter
/*    */ {
/*    */   default Player getNearestPlayer(TargetingConditions targetConditions, LivingEntity source) {
/* 19 */     return getNearestEntity(players(), targetConditions, source, source.getX(), source.getY(), source.getZ());
/*    */   }
/*    */   
/*    */   default Player getNearestPlayer(TargetingConditions targetConditions, LivingEntity source, double x, double y, double z) {
/* 23 */     return getNearestEntity(players(), targetConditions, source, x, y, z);
/*    */   }
/*    */   
/*    */   default Player getNearestPlayer(TargetingConditions targetConditions, double x, double y, double z) {
/* 27 */     return getNearestEntity(players(), targetConditions, null, x, y, z);
/*    */   }
/*    */   
/*    */   default <T extends LivingEntity> T getNearestEntity(Class<? extends T> type, TargetingConditions targetConditions, LivingEntity source, double x, double y, double z, AABB bb) {
/* 31 */     return getNearestEntity(getEntitiesOfClass(type, bb, entity -> true), targetConditions, source, x, y, z);
/*    */   }
/*    */   
/*    */   default LivingEntity getNearestEntity(TagKey<EntityType<?>> tag, TargetingConditions targetConditions, LivingEntity source, double x, double y, double z, AABB bb) {
/* 35 */     double bestDistance = Double.MAX_VALUE;
/* 36 */     LivingEntity nearestEntity = null;
/* 37 */     for (LivingEntity entity : (Iterable<LivingEntity>)getEntitiesOfClass(LivingEntity.class, bb, e -> e.getType().is(tag))) {
/* 38 */       if (!targetConditions.test(getLevel(), source, entity)) {
/*    */         continue;
/*    */       }
/*    */       
/* 42 */       double distance = entity.distanceToSqr(x, y, z);
/* 43 */       if (distance < bestDistance) {
/* 44 */         bestDistance = distance;
/* 45 */         nearestEntity = entity;
/*    */       } 
/*    */     } 
/* 48 */     return nearestEntity;
/*    */   }
/*    */   default <T extends LivingEntity> T getNearestEntity(List<? extends T> entities, TargetingConditions targetConditions, LivingEntity source, double x, double y, double z) {
/*    */     LivingEntity livingEntity;
/* 52 */     double best = -1.0D;
/* 53 */     T result = null;
/* 54 */     for (LivingEntity livingEntity1 : entities) {
/* 55 */       if (!targetConditions.test(getLevel(), source, livingEntity1)) {
/*    */         continue;
/*    */       }
/*    */       
/* 59 */       double dist = livingEntity1.distanceToSqr(x, y, z);
/* 60 */       if (best == -1.0D || dist < best) {
/* 61 */         best = dist;
/* 62 */         livingEntity = livingEntity1;
/*    */       } 
/*    */     } 
/*    */     
/* 66 */     return (T)livingEntity;
/*    */   }
/*    */   
/*    */   default List<Player> getNearbyPlayers(TargetingConditions targetConditions, LivingEntity source, AABB bb) {
/* 70 */     List<Player> foundPlayers = new ArrayList<>();
/* 71 */     for (Player player : (Iterable<Player>)players()) {
/* 72 */       if (bb.contains(player.getX(), player.getY(), player.getZ()) && targetConditions.test(getLevel(), source, (LivingEntity)player)) {
/* 73 */         foundPlayers.add(player);
/*    */       }
/*    */     } 
/*    */     
/* 77 */     return foundPlayers;
/*    */   }
/*    */   
/*    */   default <T extends LivingEntity> List<T> getNearbyEntities(Class<T> type, TargetingConditions targetConditions, LivingEntity source, AABB bb) {
/* 81 */     List<T> nearby = getEntitiesOfClass(type, bb, entity -> true);
/* 82 */     List<T> entities = new ArrayList<>();
/*    */     
/* 84 */     for (LivingEntity livingEntity : nearby) {
/* 85 */       if (targetConditions.test(getLevel(), source, livingEntity)) {
/* 86 */         entities.add((T)livingEntity);
/*    */       }
/*    */     } 
/*    */     
/* 90 */     return entities;
/*    */   }
/*    */   
/*    */   ServerLevel getLevel();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/level/ServerEntityGetter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */