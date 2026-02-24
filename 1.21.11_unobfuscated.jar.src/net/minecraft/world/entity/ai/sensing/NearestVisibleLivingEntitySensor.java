/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class NearestVisibleLivingEntitySensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 22 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(
/* 23 */         getMemory());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel level, LivingEntity body) {
/* 29 */     body.getBrain().setMemory(getMemory(), getNearestEntity(level, body));
/*    */   }
/*    */   
/*    */   private Optional<LivingEntity> getNearestEntity(ServerLevel level, LivingEntity body) {
/* 33 */     return getVisibleEntities(body).flatMap(livingEntities -> body.findClosest(()));
/*    */   }
/*    */ 
/*    */   
/*    */   protected Optional<NearestVisibleLivingEntities> getVisibleEntities(LivingEntity body) {
/* 38 */     return body.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES);
/*    */   }
/*    */   
/*    */   protected abstract boolean isMatchingEntity(ServerLevel paramServerLevel, LivingEntity paramLivingEntity1, LivingEntity paramLivingEntity2);
/*    */   
/*    */   protected abstract MemoryModuleType<LivingEntity> getMemory();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/sensing/NearestVisibleLivingEntitySensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */