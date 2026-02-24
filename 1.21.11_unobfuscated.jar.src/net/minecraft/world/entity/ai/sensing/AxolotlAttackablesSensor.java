/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.EntityTypeTags;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class AxolotlAttackablesSensor
/*    */   extends NearestVisibleLivingEntitySensor {
/*    */   public static final float TARGET_DETECTION_DISTANCE = 8.0F;
/*    */   
/*    */   protected boolean isMatchingEntity(ServerLevel level, LivingEntity body, LivingEntity mob) {
/* 14 */     return (isClose(body, mob) && mob.isInWater() && (
/* 15 */       isHostileTarget(mob) || isHuntTarget(body, mob)) && 
/* 16 */       Sensor.isEntityAttackable(level, body, mob));
/*    */   }
/*    */   
/*    */   private boolean isHuntTarget(LivingEntity body, LivingEntity mob) {
/* 20 */     return (!body.getBrain().hasMemoryValue(MemoryModuleType.HAS_HUNTING_COOLDOWN) && mob.getType().is(EntityTypeTags.AXOLOTL_HUNT_TARGETS));
/*    */   }
/*    */   
/*    */   private boolean isHostileTarget(LivingEntity mob) {
/* 24 */     return mob.getType().is(EntityTypeTags.AXOLOTL_ALWAYS_HOSTILES);
/*    */   }
/*    */   
/*    */   private boolean isClose(LivingEntity body, LivingEntity mob) {
/* 28 */     return (mob.distanceToSqr((Entity)body) <= 64.0D);
/*    */   }
/*    */ 
/*    */   
/*    */   protected MemoryModuleType<LivingEntity> getMemory() {
/* 33 */     return MemoryModuleType.NEAREST_ATTACKABLE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/sensing/AxolotlAttackablesSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */