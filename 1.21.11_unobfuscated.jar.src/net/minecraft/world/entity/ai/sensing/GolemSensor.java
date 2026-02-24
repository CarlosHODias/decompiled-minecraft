/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class GolemSensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   private static final int GOLEM_SCAN_RATE = 200;
/*    */   private static final int MEMORY_TIME_TO_LIVE = 599;
/*    */   
/*    */   public GolemSensor() {
/* 22 */     this(200);
/*    */   }
/*    */   
/*    */   public GolemSensor(int scanRate) {
/* 26 */     super(scanRate);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel level, LivingEntity body) {
/* 31 */     checkForNearbyGolem(body);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 36 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_LIVING_ENTITIES);
/*    */   }
/*    */   
/*    */   public static void checkForNearbyGolem(LivingEntity body) {
/* 40 */     Optional<List<LivingEntity>> livingEntitiesMemory = body.getBrain().getMemory(MemoryModuleType.NEAREST_LIVING_ENTITIES);
/* 41 */     if (livingEntitiesMemory.isEmpty()) {
/*    */       return;
/*    */     }
/* 44 */     boolean golemPresent = ((List)livingEntitiesMemory.get()).stream()
/* 45 */       .anyMatch(entity -> entity.getType().equals(EntityType.IRON_GOLEM));
/*    */     
/* 47 */     if (golemPresent) {
/* 48 */       golemDetected(body);
/*    */     }
/*    */   }
/*    */   
/*    */   public static void golemDetected(LivingEntity body) {
/* 53 */     body.getBrain().setMemoryWithExpiry(MemoryModuleType.GOLEM_DETECTED_RECENTLY, true, 599L);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/sensing/GolemSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */