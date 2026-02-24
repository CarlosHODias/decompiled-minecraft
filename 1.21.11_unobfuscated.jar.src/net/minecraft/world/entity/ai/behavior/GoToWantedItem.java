/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.entity.item.ItemEntity;
/*    */ 
/*    */ public class GoToWantedItem {
/*    */   public static BehaviorControl<LivingEntity> create(float speedModifier, boolean interruptOngoingWalk, int maxDistToWalk) {
/* 13 */     return create(body -> true, speedModifier, interruptOngoingWalk, maxDistToWalk);
/*    */   }
/*    */   
/*    */   public static <E extends LivingEntity> BehaviorControl<E> create(Predicate<E> predicate, float speedModifier, boolean interruptOngoingWalk, int maxDistToWalk) {
/* 17 */     return BehaviorBuilder.create(i -> {
/*    */           BehaviorBuilder<E, ? extends MemoryAccessor<? extends com.mojang.datafixers.kinds.K1, WalkTarget>> walkCondition = interruptOngoingWalk ? i.registered(MemoryModuleType.WALK_TARGET) : i.absent(MemoryModuleType.WALK_TARGET);
/*    */           return i.group((App)i.registered(MemoryModuleType.LOOK_TARGET), (App)walkCondition, (App)i.present(MemoryModuleType.NEAREST_VISIBLE_WANTED_ITEM), (App)i.registered(MemoryModuleType.ITEM_PICKUP_COOLDOWN_TICKS)).apply((com.mojang.datafixers.kinds.Applicative)i, ());
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/GoToWantedItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */