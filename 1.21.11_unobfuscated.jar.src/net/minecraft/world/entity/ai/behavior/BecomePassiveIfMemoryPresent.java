/*   */ package net.minecraft.world.entity.ai.behavior;
/*   */ import com.mojang.datafixers.kinds.App;
/*   */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*   */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*   */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*   */ 
/*   */ public class BecomePassiveIfMemoryPresent {
/*   */   public static BehaviorControl<net.minecraft.world.entity.LivingEntity> create(MemoryModuleType<?> pacifyingMemory, int pacifyDuration) {
/* 9 */     return BehaviorBuilder.create(i -> i.group((App)i.registered(MemoryModuleType.ATTACK_TARGET), (App)i.absent(MemoryModuleType.PACIFIED), (App)i.present(pacifyingMemory)).apply((com.mojang.datafixers.kinds.Applicative)i, (App)i.point((), ())));
/*   */   }
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/BecomePassiveIfMemoryPresent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */