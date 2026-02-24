/*    */ package net.minecraft.world.entity.animal.axolotl;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class ValidatePlayDead {
/*    */   public static net.minecraft.world.entity.ai.behavior.BehaviorControl<LivingEntity> create() {
/* 10 */     return (net.minecraft.world.entity.ai.behavior.BehaviorControl<LivingEntity>)BehaviorBuilder.create(i -> i.group((App)i.present(MemoryModuleType.PLAY_DEAD_TICKS), (App)i.registered(MemoryModuleType.HURT_BY_ENTITY)).apply((com.mojang.datafixers.kinds.Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/axolotl/ValidatePlayDead.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */