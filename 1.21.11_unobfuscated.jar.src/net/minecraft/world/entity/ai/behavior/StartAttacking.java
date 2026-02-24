/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class StartAttacking {
/*    */   public static <E extends Mob> BehaviorControl<E> create(TargetFinder<E> targetFinderFunction) {
/* 16 */     return create((level, body) -> true, targetFinderFunction);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E extends Mob> BehaviorControl<E> create(StartAttackingCondition<E> canAttackPredicate, TargetFinder<E> targetFinderFunction) {
/* 23 */     return BehaviorBuilder.create(i -> i.group((App)i.absent(MemoryModuleType.ATTACK_TARGET), (App)i.registered(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)).apply((Applicative)i, ()));
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface StartAttackingCondition<E> {
/*    */     boolean test(ServerLevel param1ServerLevel, E param1E);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface TargetFinder<E> {
/*    */     Optional<? extends LivingEntity> get(ServerLevel param1ServerLevel, E param1E);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/StartAttacking.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */