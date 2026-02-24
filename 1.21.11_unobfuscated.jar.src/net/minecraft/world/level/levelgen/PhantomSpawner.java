/*    */ package net.minecraft.world.level.levelgen;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.stats.ServerStatsCounter;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.DifficultyInstance;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySpawnReason;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ import net.minecraft.world.entity.SpawnGroupData;
/*    */ import net.minecraft.world.entity.monster.Phantom;
/*    */ import net.minecraft.world.level.BlockGetter;
/*    */ import net.minecraft.world.level.CustomSpawner;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.NaturalSpawner;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.gamerules.GameRules;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class PhantomSpawner implements CustomSpawner {
/*    */   public void tick(ServerLevel level, boolean spawnEnemies) {
/* 27 */     if (!spawnEnemies) {
/*    */       return;
/*    */     }
/*    */     
/* 31 */     if (!((Boolean)level.getGameRules().get(GameRules.SPAWN_PHANTOMS))) {
/*    */       return;
/*    */     }
/*    */     
/* 35 */     RandomSource random = level.random;
/*    */     
/* 37 */     this.nextTick--;
/* 38 */     if (this.nextTick > 0) {
/*    */       return;
/*    */     }
/* 41 */     this.nextTick += (60 + random.nextInt(60)) * 20;
/*    */     
/* 43 */     if (level.getSkyDarken() < 5 && level.dimensionType().hasSkyLight()) {
/*    */       return;
/*    */     }
/*    */     
/* 47 */     for (ServerPlayer player : (Iterable<ServerPlayer>)level.players()) {
/* 48 */       if (player.isSpectator()) {
/*    */         continue;
/*    */       }
/* 51 */       BlockPos playerPos = player.blockPosition();
/* 52 */       if (level.dimensionType().hasSkyLight() && (playerPos.getY() < level.getSeaLevel() || !level.canSeeSky(playerPos))) {
/*    */         continue;
/*    */       }
/* 55 */       DifficultyInstance difficulty = level.getCurrentDifficultyAt(playerPos);
/* 56 */       if (!difficulty.isHarderThan(random.nextFloat() * 3.0F)) {
/*    */         continue;
/*    */       }
/*    */       
/* 60 */       ServerStatsCounter stats = player.getStats();
/* 61 */       int value = Mth.clamp(stats.getValue(Stats.CUSTOM.get(Stats.TIME_SINCE_REST)), 1, Integer.MAX_VALUE);
/* 62 */       int dayLength = 24000;
/* 63 */       if (random.nextInt(value) < 72000) {
/*    */         continue;
/*    */       }
/*    */       
/* 67 */       BlockPos spawnPos = playerPos.above(20 + random.nextInt(15)).east(-10 + random.nextInt(21)).south(-10 + random.nextInt(21));
/* 68 */       BlockState blockState = level.getBlockState(spawnPos);
/* 69 */       FluidState fluidState = level.getFluidState(spawnPos);
/* 70 */       if (!NaturalSpawner.isValidEmptySpawnBlock((BlockGetter)level, spawnPos, blockState, fluidState, EntityType.PHANTOM)) {
/*    */         continue;
/*    */       }
/*    */       
/* 74 */       SpawnGroupData groupData = null;
/* 75 */       int groupSize = 1 + random.nextInt(difficulty.getDifficulty().getId() + 1);
/* 76 */       for (int i = 0; i < groupSize; i++) {
/* 77 */         Phantom phantom = (Phantom)EntityType.PHANTOM.create((Level)level, EntitySpawnReason.NATURAL);
/* 78 */         if (phantom != null) {
/* 79 */           phantom.snapTo(spawnPos, 0.0F, 0.0F);
/* 80 */           groupData = phantom.finalizeSpawn((ServerLevelAccessor)level, difficulty, EntitySpawnReason.NATURAL, groupData);
/* 81 */           level.addFreshEntityWithPassengers((Entity)phantom);
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private int nextTick;
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/PhantomSpawner.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */