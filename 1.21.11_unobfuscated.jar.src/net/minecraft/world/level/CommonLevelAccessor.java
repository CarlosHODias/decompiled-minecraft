/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.entity.BlockEntityType;
/*    */ import net.minecraft.world.level.levelgen.Heightmap;
/*    */ import net.minecraft.world.phys.AABB;
/*    */ import net.minecraft.world.phys.shapes.VoxelShape;
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface CommonLevelAccessor
/*    */   extends LevelReader, LevelSimulatedRW, EntityGetter
/*    */ {
/*    */   default <T extends net.minecraft.world.level.block.entity.BlockEntity> Optional<T> getBlockEntity(BlockPos pos, BlockEntityType<T> type) {
/* 18 */     return super.getBlockEntity(pos, type);
/*    */   }
/*    */ 
/*    */   
/*    */   default List<VoxelShape> getEntityCollisions(Entity source, AABB testArea) {
/* 23 */     return super.getEntityCollisions(source, testArea);
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean isUnobstructed(Entity source, VoxelShape shape) {
/* 28 */     return super.isUnobstructed(source, shape);
/*    */   }
/*    */ 
/*    */   
/*    */   default BlockPos getHeightmapPos(Heightmap.Types type, BlockPos pos) {
/* 33 */     return super.getHeightmapPos(type, pos);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/CommonLevelAccessor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */