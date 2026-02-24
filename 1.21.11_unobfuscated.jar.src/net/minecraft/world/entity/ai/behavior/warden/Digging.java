/*    */ package net.minecraft.world.entity.ai.behavior.warden;
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Pose;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ import net.minecraft.world.entity.monster.warden.Warden;
/*    */ 
/*    */ public class Digging<E extends Warden> extends net.minecraft.world.entity.ai.behavior.Behavior<E> {
/*    */   public Digging(int ticks) {
/* 15 */     super((Map)ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_ABSENT, MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT), ticks);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, E body, long timestamp) {
/* 23 */     return (body.getRemovalReason() == null);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, E body) {
/* 28 */     return (body.onGround() || body.isInWater() || body.isInLava());
/*    */   }
/*    */ 
/*    */   
/*    */   protected void start(ServerLevel level, E body, long timestamp) {
/* 33 */     if (body.onGround()) {
/* 34 */       body.setPose(Pose.DIGGING);
/* 35 */       body.playSound(SoundEvents.WARDEN_DIG, 5.0F, 1.0F);
/*    */     } else {
/* 37 */       body.playSound(SoundEvents.WARDEN_AGITATED, 5.0F, 1.0F);
/* 38 */       stop(level, body, timestamp);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   protected void stop(ServerLevel level, E body, long timestamp) {
/* 44 */     if (body.getRemovalReason() == null)
/* 45 */       body.remove(Entity.RemovalReason.DISCARDED); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/warden/Digging.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */