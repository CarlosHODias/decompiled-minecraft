/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ 
/*    */ import com.google.common.collect.ImmutableMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.tags.FluidTags;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryStatus;
/*    */ 
/*    */ public class Swim<T extends Mob>
/*    */   extends Behavior<T> {
/*    */   public Swim(float chance) {
/* 15 */     super((Map<MemoryModuleType<?>, MemoryStatus>)ImmutableMap.of());
/* 16 */     this.chance = chance;
/*    */   }
/*    */   private final float chance;
/*    */   public static <T extends Mob> boolean shouldSwim(T mob) {
/* 20 */     return ((mob.isInWater() && mob.getFluidHeight(FluidTags.WATER) > mob.getFluidJumpThreshold()) || mob.isInLava());
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean checkExtraStartConditions(ServerLevel level, Mob body) {
/* 25 */     return shouldSwim(body);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean canStillUse(ServerLevel level, Mob body, long timestamp) {
/* 30 */     return checkExtraStartConditions(level, body);
/*    */   }
/*    */ 
/*    */   
/*    */   protected void tick(ServerLevel level, Mob body, long timestamp) {
/* 35 */     if (body.getRandom().nextFloat() < this.chance)
/* 36 */       body.getJumpControl().jump(); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/Swim.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */