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
/*    */ 
/*    */ public class StopAttackingIfTargetInvalid
/*    */ {
/*    */   private static final int TIMEOUT_TO_GET_WITHIN_ATTACK_RANGE = 200;
/*    */   
/*    */   public static <E extends Mob> BehaviorControl<E> create(TargetErasedCallback<E> onTargetErased) {
/* 20 */     return create((level, entity) -> false, onTargetErased, true);
/*    */   }
/*    */   
/*    */   public static <E extends Mob> BehaviorControl<E> create(StopAttackCondition stopAttackingWhen) {
/* 24 */     return create(stopAttackingWhen, (level, body, target) -> {
/*    */         
/*    */         }, true);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static <E extends Mob> BehaviorControl<E> create() {
/* 32 */     return create((level, entity) -> false, (level, body, target) -> {
/*    */         
/*    */         }, true);
/*    */   } public static <E extends Mob> BehaviorControl<E> create(StopAttackCondition stopAttackingWhen, TargetErasedCallback<E> onTargetErased, boolean canGrowTiredOfTryingToReachTarget) {
/* 36 */     return BehaviorBuilder.create(i -> i.group((App)i.present(MemoryModuleType.ATTACK_TARGET), (App)i.registered(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE)).apply((Applicative)i, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static boolean isTiredOfTryingToReachTarget(LivingEntity body, Optional<Long> cantReachSince) {
/* 58 */     return (cantReachSince.isPresent() && body.level().getGameTime() - (Long)cantReachSince.get() > 200L);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface StopAttackCondition {
/*    */     boolean test(ServerLevel param1ServerLevel, LivingEntity param1LivingEntity);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface TargetErasedCallback<E> {
/*    */     void accept(ServerLevel param1ServerLevel, E param1E, LivingEntity param1LivingEntity);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/StopAttackingIfTargetInvalid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */