/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class SetLookAndInteract {
/*    */   public static BehaviorControl<LivingEntity> create(EntityType<?> type, int interactionRange) {
/* 12 */     int interactionRangeSqr = interactionRange * interactionRange;
/* 13 */     return BehaviorBuilder.create(i -> i.group((App)i.registered(MemoryModuleType.LOOK_TARGET), (App)i.absent(MemoryModuleType.INTERACTION_TARGET), (App)i.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply((com.mojang.datafixers.kinds.Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/SetLookAndInteract.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */