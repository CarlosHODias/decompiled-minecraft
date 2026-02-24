/*    */ package net.minecraft.world.level.pathfinder;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.world.entity.Mob;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.CollisionGetter;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ 
/*    */ public class PathfindingContext
/*    */ {
/*    */   private final CollisionGetter level;
/* 14 */   private final BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos(); private final PathTypeCache cache; private final BlockPos mobPosition;
/*    */   
/*    */   public PathfindingContext(CollisionGetter level, Mob mob) {
/* 17 */     this.level = level;
/* 18 */     Level level1 = mob.level(); if (level1 instanceof ServerLevel) { ServerLevel serverLevel = (ServerLevel)level1;
/* 19 */       this.cache = serverLevel.getPathTypeCache(); }
/*    */     else
/* 21 */     { this.cache = null; }
/*    */     
/* 23 */     this.mobPosition = mob.blockPosition();
/*    */   }
/*    */   
/*    */   public PathType getPathTypeFromState(int x, int y, int z) {
/* 27 */     BlockPos.MutableBlockPos mutableBlockPos = this.mutablePos.set(x, y, z);
/* 28 */     if (this.cache == null) {
/* 29 */       return WalkNodeEvaluator.getPathTypeFromState((BlockGetter)this.level, (BlockPos)mutableBlockPos);
/*    */     }
/* 31 */     return this.cache.getOrCompute((BlockGetter)this.level, (BlockPos)mutableBlockPos);
/*    */   }
/*    */   
/*    */   public BlockState getBlockState(BlockPos pos) {
/* 35 */     return this.level.getBlockState(pos);
/*    */   }
/*    */   
/*    */   public CollisionGetter level() {
/* 39 */     return this.level;
/*    */   }
/*    */   
/*    */   public BlockPos mobPosition() {
/* 43 */     return this.mobPosition;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/pathfinder/PathfindingContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */