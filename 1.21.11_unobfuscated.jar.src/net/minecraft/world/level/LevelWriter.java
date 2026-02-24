/*    */ package net.minecraft.world.level;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.level.block.Block.UpdateFlags;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ 
/*    */ public interface LevelWriter
/*    */ {
/*    */   boolean setBlock(BlockPos paramBlockPos, BlockState paramBlockState, @net.minecraft.world.level.block.Block.UpdateFlags int paramInt1, int paramInt2);
/*    */   
/*    */   default boolean setBlock(BlockPos pos, BlockState blockState, @net.minecraft.world.level.block.Block.UpdateFlags int updateFlags) {
/* 14 */     return setBlock(pos, blockState, updateFlags, 512);
/*    */   }
/*    */ 
/*    */   
/*    */   boolean removeBlock(BlockPos paramBlockPos, boolean paramBoolean);
/*    */   
/*    */   default boolean destroyBlock(BlockPos pos, boolean dropResources) {
/* 21 */     return destroyBlock(pos, dropResources, null);
/*    */   }
/*    */ 
/*    */   
/*    */   default boolean destroyBlock(BlockPos pos, boolean dropResources, Entity breaker) {
/* 26 */     return destroyBlock(pos, dropResources, breaker, 512);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   boolean destroyBlock(BlockPos paramBlockPos, boolean paramBoolean, Entity paramEntity, int paramInt);
/*    */ 
/*    */   
/*    */   default boolean addFreshEntity(Entity entity) {
/* 35 */     return false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/LevelWriter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */