/*    */ package net.minecraft.world.entity.ai.behavior;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.core.Vec3i;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
/*    */ import net.minecraft.world.entity.ai.behavior.declarative.MemoryAccessor;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.ai.memory.WalkTarget;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public class MoveToSkySeeingSpot {
/*    */   public static OneShot<LivingEntity> create(float speedModifier) {
/* 18 */     return BehaviorBuilder.create(i -> i.group((App)i.absent(MemoryModuleType.WALK_TARGET)).apply((Applicative)i, ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private static Vec3 getOutdoorPosition(ServerLevel level, LivingEntity body) {
/* 33 */     RandomSource random = body.getRandom();
/* 34 */     BlockPos pos = body.blockPosition();
/*    */     
/* 36 */     for (int i = 0; i < 10; i++) {
/* 37 */       BlockPos randomPos = pos.offset(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
/*    */       
/* 39 */       if (hasNoBlocksAbove(level, body, randomPos)) {
/* 40 */         return Vec3.atBottomCenterOf((Vec3i)randomPos);
/*    */       }
/*    */     } 
/* 43 */     return null;
/*    */   }
/*    */   
/*    */   public static boolean hasNoBlocksAbove(ServerLevel level, LivingEntity body, BlockPos target) {
/* 47 */     return (level.canSeeSky(target) && level.getHeightmapPos(net.minecraft.world.level.levelgen.Heightmap.Types.MOTION_BLOCKING, target).getY() <= body.getY());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/behavior/MoveToSkySeeingSpot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */