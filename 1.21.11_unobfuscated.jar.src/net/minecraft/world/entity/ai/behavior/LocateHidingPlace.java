/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class LocateHidingPlace {
/*    */   public static OneShot<LivingEntity> create(int radius, float speedModifier, int closeEnoughDist) {
/* 13 */     return BehaviorBuilder.create(i -> i.group((App)i.absent(MemoryModuleType.WALK_TARGET), (App)i.registered(MemoryModuleType.HOME), (App)i.registered(MemoryModuleType.HIDING_PLACE), (App)i.registered(MemoryModuleType.PATH), (App)i.registered(MemoryModuleType.LOOK_TARGET), (App)i.registered(MemoryModuleType.BREED_TARGET), (App)i.registered(MemoryModuleType.INTERACTION_TARGET)).apply((com.mojang.datafixers.kinds.Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/LocateHidingPlace.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */