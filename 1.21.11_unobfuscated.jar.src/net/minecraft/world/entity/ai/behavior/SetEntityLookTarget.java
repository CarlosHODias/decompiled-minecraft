/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.MobCategory;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ public class SetEntityLookTarget {
/*    */   public static BehaviorControl<LivingEntity> create(MobCategory category, float maxDist) {
/* 18 */     return create(mob -> category.equals(mob.getType().getCategory()), maxDist);
/*    */   }
/*    */   
/*    */   public static OneShot<LivingEntity> create(EntityType<?> type, float maxDist) {
/* 22 */     return create(mob -> type.equals(mob.getType()), maxDist);
/*    */   }
/*    */   
/*    */   public static OneShot<LivingEntity> create(float maxDist) {
/* 26 */     return create(mob -> true, maxDist);
/*    */   }
/*    */   
/*    */   public static OneShot<LivingEntity> create(Predicate<LivingEntity> predicate, float maxDist) {
/* 30 */     float maxDistSqr = maxDist * maxDist;
/*    */     
/* 32 */     return BehaviorBuilder.create(i -> i.group((App)i.absent(MemoryModuleType.LOOK_TARGET), (App)i.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply((Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/SetEntityLookTarget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */