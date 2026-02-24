/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.valueproviders.UniformInt;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.Trigger;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
/*    */ 
/*    */ @Deprecated
/*    */ public class SetEntityLookTargetSometimes {
/*    */   public static BehaviorControl<LivingEntity> create(float maxDist, UniformInt interval) {
/* 20 */     return create(maxDist, interval, mob -> true);
/*    */   }
/*    */   
/*    */   public static BehaviorControl<LivingEntity> create(EntityType<?> type, float maxDist, UniformInt interval) {
/* 24 */     return create(maxDist, interval, mob -> type.equals(mob.getType()));
/*    */   }
/*    */   
/*    */   private static BehaviorControl<LivingEntity> create(float maxDist, UniformInt interval, Predicate<LivingEntity> predicate) {
/* 28 */     float maxDistSqr = maxDist * maxDist;
/*    */     
/* 30 */     Ticker ticker = new Ticker(interval);
/*    */     
/* 32 */     return BehaviorBuilder.create(i -> i.group((App)i.absent(MemoryModuleType.LOOK_TARGET), (App)i.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)).apply((Applicative)i, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static final class Ticker
/*    */   {
/*    */     private final UniformInt interval;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     private int ticksUntilNextStart;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public Ticker(UniformInt interval) {
/* 56 */       if (interval.getMinValue() <= 1) {
/* 57 */         throw new IllegalArgumentException();
/*    */       }
/* 59 */       this.interval = interval;
/*    */     }
/*    */     
/*    */     public boolean tickDownAndCheck(RandomSource random) {
/* 63 */       if (this.ticksUntilNextStart == 0) {
/* 64 */         this.ticksUntilNextStart = this.interval.sample(random) - 1;
/* 65 */         return false;
/*    */       } 
/*    */       
/* 68 */       return (--this.ticksUntilNextStart == 0);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/SetEntityLookTargetSometimes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */