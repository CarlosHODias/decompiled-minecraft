/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import net.minecraft.core.GlobalPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ public class SocializeAtBell {
/*    */   public static OneShot<LivingEntity> create() {
/* 15 */     return BehaviorBuilder.create(i -> i.group((App)i.registered(MemoryModuleType.WALK_TARGET), (App)i.registered(MemoryModuleType.LOOK_TARGET), (App)i.present(MemoryModuleType.MEETING_POINT), (App)i.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES), (App)i.absent(MemoryModuleType.INTERACTION_TARGET)).apply((Applicative)i, ()));
/*    */   }
/*    */   
/*    */   private static final float SPEED_MODIFIER = 0.3F;
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/SocializeAtBell.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */