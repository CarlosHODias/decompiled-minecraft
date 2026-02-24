/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Set;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.damagesource.DamageSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HurtBySensor
/*    */   extends Sensor<LivingEntity>
/*    */ {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 21 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.HURT_BY, MemoryModuleType.HURT_BY_ENTITY);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel level, LivingEntity body) {
/* 26 */     Brain<?> brain = body.getBrain();
/* 27 */     DamageSource damageSource = body.getLastDamageSource();
/* 28 */     if (damageSource != null) {
/* 29 */       brain.setMemory(MemoryModuleType.HURT_BY, body.getLastDamageSource());
/* 30 */       Entity entitySource = damageSource.getEntity();
/* 31 */       if (entitySource instanceof LivingEntity) {
/* 32 */         brain.setMemory(MemoryModuleType.HURT_BY_ENTITY, entitySource);
/*    */       }
/*    */     } else {
/* 35 */       brain.eraseMemory(MemoryModuleType.HURT_BY);
/*    */     } 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 41 */     brain.getMemory(MemoryModuleType.HURT_BY_ENTITY).ifPresent(hurtByEntity -> {
/*    */           if (!hurtByEntity.isAlive() || hurtByEntity.level() != level)
/*    */             brain.eraseMemory(MemoryModuleType.HURT_BY_ENTITY); 
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/sensing/HurtBySensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */