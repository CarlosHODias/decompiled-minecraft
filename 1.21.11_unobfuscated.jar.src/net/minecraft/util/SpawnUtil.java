/*     */ package net.minecraft.util;
/*     */ 
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.tags.BlockTags;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.EntitySpawnReason;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.entity.Mob;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.LevelAccessor;
/*     */ import net.minecraft.world.level.LevelReader;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ 
/*     */ public class SpawnUtil
/*     */ {
/*     */   public static <T extends Mob> Optional<T> trySpawnMob(EntityType<T> entityType, EntitySpawnReason spawnReason, ServerLevel level, BlockPos start, int spawnAttempts, int spawnRangeXZ, int spawnRangeY, Strategy strategy, boolean checkCollisions) {
/*  23 */     BlockPos.MutableBlockPos searchPos = start.mutable();
/*  24 */     for (int i = 0; i < spawnAttempts; i++) {
/*  25 */       int dx = Mth.randomBetweenInclusive(level.random, -spawnRangeXZ, spawnRangeXZ);
/*  26 */       int dz = Mth.randomBetweenInclusive(level.random, -spawnRangeXZ, spawnRangeXZ);
/*     */       
/*  28 */       searchPos.setWithOffset((Vec3i)start, dx, spawnRangeY, dz);
/*  29 */       if (level.getWorldBorder().isWithinBounds((BlockPos)searchPos) && moveToPossibleSpawnPosition(level, spawnRangeY, searchPos, strategy))
/*     */       {
/*     */         
/*  32 */         if (!checkCollisions || level.noCollision(entityType.getSpawnAABB(searchPos.getX() + 0.5D, searchPos.getY(), searchPos.getZ() + 0.5D))) {
/*     */ 
/*     */ 
/*     */           
/*  36 */           Mob mob = (Mob)entityType.create(level, null, (BlockPos)searchPos, spawnReason, false, false);
/*  37 */           if (mob != null) {
/*  38 */             if (mob.checkSpawnRules((LevelAccessor)level, spawnReason) && mob.checkSpawnObstruction((LevelReader)level)) {
/*  39 */               level.addFreshEntityWithPassengers((Entity)mob);
/*  40 */               mob.playAmbientSound();
/*  41 */               return Optional.of((T)mob);
/*     */             } 
/*  43 */             mob.discard();
/*     */           } 
/*     */         }  } 
/*     */     } 
/*  47 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static interface Strategy {
/*     */     @Deprecated
/*     */     public static final Strategy LEGACY_IRON_GOLEM;
/*     */     public static final Strategy ON_TOP_OF_COLLIDER;
/*     */     public static final Strategy ON_TOP_OF_COLLIDER_NO_LEAVES;
/*     */     
/*     */     boolean canSpawnOn(ServerLevel param1ServerLevel, BlockPos param1BlockPos1, BlockState param1BlockState1, BlockPos param1BlockPos2, BlockState param1BlockState2);
/*     */     
/*     */     static {
/*  59 */       LEGACY_IRON_GOLEM = ((level, pos, blockState, abovePos, aboveState) -> 
/*  60 */         (blockState.is(Blocks.COBWEB) || blockState.is(Blocks.CACTUS) || blockState.is(Blocks.GLASS_PANE) || blockState.getBlock() instanceof net.minecraft.world.level.block.StainedGlassPaneBlock || blockState.getBlock() instanceof net.minecraft.world.level.block.StainedGlassBlock || blockState.getBlock() instanceof net.minecraft.world.level.block.LeavesBlock || blockState.is(Blocks.CONDUIT) || blockState.is(Blocks.ICE) || blockState.is(Blocks.TNT) || blockState.is(Blocks.GLOWSTONE) || blockState.is(Blocks.BEACON) || blockState.is(Blocks.SEA_LANTERN) || blockState.is(Blocks.FROSTED_ICE) || blockState.is(Blocks.TINTED_GLASS) || blockState.is(Blocks.GLASS)) ? false : (
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  78 */         ((aboveState.isAir() || aboveState.liquid()) && (blockState.isSolid() || blockState.is(Blocks.POWDER_SNOW)))));
/*     */ 
/*     */       
/*  81 */       ON_TOP_OF_COLLIDER = ((level, pos, blockState, abovePos, aboveState) -> 
/*  82 */         (aboveState.getCollisionShape((BlockGetter)level, abovePos).isEmpty() && Block.isFaceFull(blockState.getCollisionShape((BlockGetter)level, pos), Direction.UP)));
/*     */       
/*  84 */       ON_TOP_OF_COLLIDER_NO_LEAVES = ((level, pos, blockState, abovePos, aboveState) -> 
/*  85 */         (aboveState.getCollisionShape((BlockGetter)level, abovePos).isEmpty() && !blockState.is(BlockTags.LEAVES) && Block.isFaceFull(blockState.getCollisionShape((BlockGetter)level, pos), Direction.UP)));
/*     */     } }
/*     */   
/*     */   private static boolean moveToPossibleSpawnPosition(ServerLevel level, int spawnRangeY, BlockPos.MutableBlockPos searchPos, Strategy strategy) {
/*  89 */     BlockPos.MutableBlockPos abovePos = new BlockPos.MutableBlockPos().set((Vec3i)searchPos);
/*  90 */     BlockState aboveState = level.getBlockState((BlockPos)abovePos);
/*     */     
/*  92 */     for (int y = spawnRangeY; y >= -spawnRangeY; y--) {
/*  93 */       searchPos.move(Direction.DOWN);
/*  94 */       abovePos.setWithOffset((Vec3i)searchPos, Direction.UP);
/*     */       
/*  96 */       BlockState currentState = level.getBlockState((BlockPos)searchPos);
/*  97 */       if (strategy.canSpawnOn(level, (BlockPos)searchPos, currentState, (BlockPos)abovePos, aboveState)) {
/*  98 */         searchPos.move(Direction.UP);
/*  99 */         return true;
/*     */       } 
/* 101 */       aboveState = currentState;
/*     */     } 
/* 103 */     return false;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/SpawnUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */