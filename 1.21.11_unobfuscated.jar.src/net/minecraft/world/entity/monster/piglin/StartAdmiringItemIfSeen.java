/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ 
/*    */ public class StartAdmiringItemIfSeen {
/*    */   public static net.minecraft.world.entity.ai.behavior.BehaviorControl<LivingEntity> create(int admireDuration) {
/* 11 */     return (net.minecraft.world.entity.ai.behavior.BehaviorControl<LivingEntity>)BehaviorBuilder.create(i -> i.group((App)i.present(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM), (App)i.absent(MemoryModuleType.ADMIRING_ITEM), (App)i.absent(MemoryModuleType.ADMIRING_DISABLED), (App)i.absent(MemoryModuleType.DISABLE_WALK_TO_ADMIRE_ITEM)).apply((com.mojang.datafixers.kinds.Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/monster/piglin/StartAdmiringItemIfSeen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */