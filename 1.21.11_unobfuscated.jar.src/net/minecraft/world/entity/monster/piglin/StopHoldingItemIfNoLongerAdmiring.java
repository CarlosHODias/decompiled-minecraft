/*    */ package net.minecraft.world.entity.monster.piglin;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.ai.behavior.BehaviorControl;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class StopHoldingItemIfNoLongerAdmiring {
/*    */   public static BehaviorControl<Piglin> create() {
/* 10 */     return (BehaviorControl<Piglin>)BehaviorBuilder.create(i -> i.group((App)i.absent(MemoryModuleType.ADMIRING_ITEM)).apply((com.mojang.datafixers.kinds.Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/monster/piglin/StopHoldingItemIfNoLongerAdmiring.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */