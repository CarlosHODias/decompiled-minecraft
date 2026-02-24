/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.level.LevelReader;
/*    */ 
/*    */ public interface SpawnPlacementType
/*    */ {
/*    */   boolean isSpawnPositionOk(LevelReader paramLevelReader, BlockPos paramBlockPos, EntityType<?> paramEntityType);
/*    */   
/*    */   default BlockPos adjustSpawnPosition(LevelReader level, BlockPos candidate) {
/* 11 */     return candidate;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/SpawnPlacementType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */