/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class StopAdmiringIfTiredOfTryingToReachItem {
/*    */   public static BehaviorControl<LivingEntity> create(int maxTimeToReachItem, int disableTime) {
/* 12 */     return (BehaviorControl<LivingEntity>)BehaviorBuilder.create(i -> i.group((App)i.present(MemoryModuleType.ADMIRING_ITEM), (App)i.present(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM), (App)i.registered(MemoryModuleType.TIME_TRYING_TO_REACH_ADMIRE_ITEM), (App)i.registered(MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM)).apply((com.mojang.datafixers.kinds.Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/monster/piglin/StopAdmiringIfTiredOfTryingToReachItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */