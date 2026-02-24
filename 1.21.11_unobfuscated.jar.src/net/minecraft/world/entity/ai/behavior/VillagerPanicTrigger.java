/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.npc.villager.Villager;
/*    */ import net.minecraft.world.entity.schedule.Activity;
/*    */ 
/*    */ public class VillagerPanicTrigger
/*    */   extends Behavior<Villager> {
/*    */   public VillagerPanicTrigger() {
/* 16 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, Villager body, long timestamp) {
/* 21 */     return (isHurt((LivingEntity)body) || hasHostile((LivingEntity)body));
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel level, Villager body, long timestamp) {
/* 26 */     if (isHurt((LivingEntity)body) || hasHostile((LivingEntity)body)) {
/* 27 */       Brain<?> brain = body.getBrain();
/*    */ 
/*    */       
/* 30 */       if (!brain.isActive(Activity.PANIC)) {
/* 31 */         brain.eraseMemory(MemoryModuleType.PATH);
/* 32 */         brain.eraseMemory(MemoryModuleType.WALK_TARGET);
/* 33 */         brain.eraseMemory(MemoryModuleType.LOOK_TARGET);
/* 34 */         brain.eraseMemory(MemoryModuleType.BREED_TARGET);
/* 35 */         brain.eraseMemory(MemoryModuleType.INTERACTION_TARGET);
/*    */       } 
/* 37 */       brain.setActiveActivityIfPossible(Activity.PANIC);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel level, Villager body, long timestamp) {
/* 43 */     if (timestamp % 100L == 0L) {
/* 44 */       body.spawnGolemIfNeeded(level, timestamp, 3);
/*    */     }
/*    */   }
/*    */   
/*    */   public static boolean hasHostile(LivingEntity myBody) {
/* 49 */     return myBody.getBrain().hasMemoryValue(MemoryModuleType.NEAREST_HOSTILE);
/*    */   }
/*    */   
/*    */   public static boolean isHurt(LivingEntity myBody) {
/* 53 */     return myBody.getBrain().hasMemoryValue(MemoryModuleType.HURT_BY);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/VillagerPanicTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */