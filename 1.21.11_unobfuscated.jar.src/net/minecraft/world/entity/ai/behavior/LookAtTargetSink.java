/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class LookAtTargetSink extends Behavior<Mob> {
/*    */   public LookAtTargetSink(int minDuration, int maxDuration) {
/* 11 */     super((java.util.Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.VALUE_PRESENT), minDuration, maxDuration);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, Mob body, long timestamp) {
/* 16 */     return body.getBrain().getMemory(MemoryModuleType.LOOK_TARGET)
/* 17 */       .filter(pos -> pos.isVisibleBy((LivingEntity)body))
/* 18 */       .isPresent();
/*    */   }
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel level, Mob body, long timestamp) {
/* 23 */     body.getBrain().eraseMemory(MemoryModuleType.LOOK_TARGET);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel level, Mob body, long timestamp) {
/* 28 */     body.getBrain().getMemory(MemoryModuleType.LOOK_TARGET).ifPresent(target -> body.getLookControl().setLookAt(target.currentPosition()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/LookAtTargetSink.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */