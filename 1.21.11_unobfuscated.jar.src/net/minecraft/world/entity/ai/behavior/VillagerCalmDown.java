/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class VillagerCalmDown
/*    */ {
/*    */   public static BehaviorControl<LivingEntity> create() {
/* 15 */     return BehaviorBuilder.create(i -> i.group((App)i.registered(MemoryModuleType.HURT_BY), (App)i.registered(MemoryModuleType.HURT_BY_ENTITY), (App)i.registered(MemoryModuleType.NEAREST_HOSTILE)).apply((Applicative)i, ()));
/*    */   }
/*    */   
/*    */   private static final int SAFE_DISTANCE_FROM_DANGER = 36;
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/VillagerCalmDown.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */