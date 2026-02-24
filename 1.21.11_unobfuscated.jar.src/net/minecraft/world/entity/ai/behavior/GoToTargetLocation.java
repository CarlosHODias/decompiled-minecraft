/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ 
/*    */ public class GoToTargetLocation {
/*    */   private static BlockPos getNearbyPos(Mob body, BlockPos pos) {
/* 11 */     net.minecraft.util.RandomSource random = (body.level()).random;
/* 12 */     return pos.offset(getRandomOffset(random), 0, getRandomOffset(random));
/*    */   }
/*    */   
/*    */   private static int getRandomOffset(net.minecraft.util.RandomSource random) {
/* 16 */     return random.nextInt(3) - 1;
/*    */   }
/*    */   
/*    */   public static <E extends Mob> OneShot<E> create(MemoryModuleType<BlockPos> locationMemory, int closeEnoughDist, float speedModifier) {
/* 20 */     return BehaviorBuilder.create(i -> i.group((App)i.present(locationMemory), (App)i.absent(MemoryModuleType.ATTACK_TARGET), (App)i.absent(MemoryModuleType.WALK_TARGET), (App)i.registered(MemoryModuleType.LOOK_TARGET)).apply((com.mojang.datafixers.kinds.Applicative)i, ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/GoToTargetLocation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */