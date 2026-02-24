/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ public class VillagerHostilesSensor
/*    */   extends NearestVisibleLivingEntitySensor
/*    */ {
/* 14 */   private static final ImmutableMap<EntityType<?>, Float> ACCEPTABLE_DISTANCE_FROM_HOSTILES = ImmutableMap.builder()
/* 15 */     .put(EntityType.DROWNED, 8.0F)
/* 16 */     .put(EntityType.EVOKER, 12.0F)
/* 17 */     .put(EntityType.HUSK, 8.0F)
/* 18 */     .put(EntityType.ILLUSIONER, 12.0F)
/* 19 */     .put(EntityType.PILLAGER, 15.0F)
/* 20 */     .put(EntityType.RAVAGER, 12.0F)
/* 21 */     .put(EntityType.VEX, 8.0F)
/* 22 */     .put(EntityType.VINDICATOR, 10.0F)
/* 23 */     .put(EntityType.ZOGLIN, 10.0F)
/* 24 */     .put(EntityType.ZOMBIE, 8.0F)
/* 25 */     .put(EntityType.ZOMBIE_VILLAGER, 8.0F)
/* 26 */     .build();
/*    */ 
/*    */   
/*    */   protected boolean isMatchingEntity(ServerLevel level, LivingEntity body, LivingEntity mob) {
/* 30 */     return (isHostile(mob) && isClose(body, mob));
/*    */   }
/*    */   
/*    */   private boolean isClose(LivingEntity body, LivingEntity mob) {
/* 34 */     float distThreshold = (Float)ACCEPTABLE_DISTANCE_FROM_HOSTILES.get(mob.getType());
/* 35 */     return (mob.distanceToSqr((Entity)body) <= (distThreshold * distThreshold));
/*    */   }
/*    */ 
/*    */   
/*    */   protected MemoryModuleType<LivingEntity> getMemory() {
/* 40 */     return MemoryModuleType.NEAREST_HOSTILE;
/*    */   }
/*    */   
/*    */   private boolean isHostile(LivingEntity entity) {
/* 44 */     return ACCEPTABLE_DISTANCE_FROM_HOSTILES.containsKey(entity.getType());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/sensing/VillagerHostilesSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */