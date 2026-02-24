/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VillagerBabiesSensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 21 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.VISIBLE_VILLAGER_BABIES);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel level, LivingEntity body) {
/* 26 */     body.getBrain().setMemory(MemoryModuleType.VISIBLE_VILLAGER_BABIES, getNearestVillagerBabies(body));
/*    */   }
/*    */   
/*    */   private List<LivingEntity> getNearestVillagerBabies(LivingEntity myBody) {
/* 30 */     return (List<LivingEntity>)ImmutableList.copyOf(getVisibleEntities(myBody).findAll(this::isVillagerBaby));
/*    */   }
/*    */   
/*    */   private boolean isVillagerBaby(LivingEntity entity) {
/* 34 */     return (entity.getType() == EntityType.VILLAGER && entity.isBaby());
/*    */   }
/*    */   
/*    */   private NearestVisibleLivingEntities getVisibleEntities(LivingEntity myBody) {
/* 38 */     return myBody.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
/* 39 */       .orElse(NearestVisibleLivingEntities.empty());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/sensing/VillagerBabiesSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */