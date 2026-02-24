/*     */ package net.minecraft.world.level.block.entity.trialspawner;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.Position;
/*     */ import net.minecraft.core.particles.ParticleOptions;
/*     */ import net.minecraft.core.particles.ParticleTypes;
/*     */ import net.minecraft.core.particles.SimpleParticleType;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.sounds.SoundEvents;
/*     */ import net.minecraft.sounds.SoundSource;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.entity.OminousItemSpawner;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ClipContext;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.SpawnData;
/*     */ import net.minecraft.world.phys.BlockHitResult;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public enum TrialSpawnerState implements StringRepresentable {
/*  34 */   INACTIVE("inactive", 0, ParticleEmission.NONE, -1.0D, false),
/*  35 */   WAITING_FOR_PLAYERS("waiting_for_players", 4, ParticleEmission.SMALL_FLAMES, 200.0D, true),
/*  36 */   ACTIVE("active", 8, ParticleEmission.FLAMES_AND_SMOKE, 1000.0D, true),
/*  37 */   WAITING_FOR_REWARD_EJECTION("waiting_for_reward_ejection", 8, ParticleEmission.SMALL_FLAMES, -1.0D, false),
/*  38 */   EJECTING_REWARD("ejecting_reward", 8, ParticleEmission.SMALL_FLAMES, -1.0D, false),
/*  39 */   COOLDOWN("cooldown", 0, ParticleEmission.SMOKE_INSIDE_AND_TOP_FACE, -1.0D, false);
/*     */ 
/*     */   
/*  42 */   private static final int TIME_BETWEEN_EACH_EJECTION = Mth.floor(30.0F); private static final float DELAY_BEFORE_EJECT_AFTER_KILLING_LAST_MOB = 40.0F;
/*     */   private final String name;
/*     */   private final int lightLevel;
/*     */   private final double spinningMobSpeed;
/*     */   private final ParticleEmission particleEmission;
/*     */   private final boolean isCapableOfSpawning;
/*     */   
/*     */   TrialSpawnerState(String name, int lightLevel, ParticleEmission particleEmission, double spinningMobSpeed, boolean isCapableOfSpawning) {
/*  50 */     this.name = name;
/*  51 */     this.lightLevel = lightLevel;
/*  52 */     this.particleEmission = particleEmission;
/*  53 */     this.spinningMobSpeed = spinningMobSpeed;
/*  54 */     this.isCapableOfSpawning = isCapableOfSpawning;
/*     */   }
/*     */   TrialSpawnerState tickAndGetNext(BlockPos spawnerPos, TrialSpawner trialSpawner, ServerLevel serverLevel) {
/*     */     int additionalPlayers;
/*  58 */     TrialSpawnerStateData data = trialSpawner.getStateData();
/*  59 */     TrialSpawnerConfig config = trialSpawner.activeConfig();
/*     */     
/*  61 */     switch (ordinal()) { default: throw new MatchException(null, null);
/*  62 */       case 0: if (data.getOrCreateDisplayEntity(trialSpawner, (Level)serverLevel, WAITING_FOR_PLAYERS) == null);
/*     */ 
/*     */       
/*     */       case 1:
/*  66 */         if (!trialSpawner.canSpawnInLevel(serverLevel)) {
/*  67 */           data.resetStatistics();
/*     */         }
/*     */         
/*  70 */         if (!data.hasMobToSpawn(trialSpawner, serverLevel.random));
/*     */ 
/*     */         
/*  73 */         data.tryDetectPlayers(serverLevel, spawnerPos, trialSpawner);
/*  74 */         if (data.detectedPlayers.isEmpty());
/*     */ 
/*     */ 
/*     */       
/*     */       case 2:
/*  79 */         if (!trialSpawner.canSpawnInLevel(serverLevel)) {
/*  80 */           data.resetStatistics();
/*     */         }
/*     */ 
/*     */         
/*  84 */         if (!data.hasMobToSpawn(trialSpawner, serverLevel.random));
/*     */ 
/*     */ 
/*     */         
/*  88 */         additionalPlayers = data.countAdditionalPlayers(spawnerPos);
/*  89 */         data.tryDetectPlayers(serverLevel, spawnerPos, trialSpawner);
/*     */         
/*  91 */         if (trialSpawner.isOminous()) {
/*  92 */           spawnOminousOminousItemSpawner(serverLevel, spawnerPos, trialSpawner);
/*     */         }
/*     */         
/*  95 */         if (data.hasFinishedSpawningAllMobs(config, additionalPlayers)) {
/*  96 */           if (data.haveAllCurrentMobsDied()) {
/*  97 */             data.cooldownEndsAt = serverLevel.getGameTime() + trialSpawner.getTargetCooldownLength();
/*  98 */             data.totalMobsSpawned = 0;
/*  99 */             data.nextMobSpawnsAt = 0L;
/*     */           }
/*     */         
/* 102 */         } else if (data.isReadyToSpawnNextMob(serverLevel, config, additionalPlayers)) {
/* 103 */           trialSpawner.spawnMob(serverLevel, spawnerPos).ifPresent(entityId -> {
/*     */                 data.currentMobs.add(entityId);
/*     */                 data.totalMobsSpawned++;
/*     */                 data.nextMobSpawnsAt = serverLevel.getGameTime() + config.ticksBetweenSpawn();
/*     */                 config.spawnPotentialsDefinition().getRandom(serverLevel.getRandom()).ifPresent(());
/*     */               });
/*     */         } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       case 3:
/* 118 */         if (data.isReadyToOpenShutter(serverLevel, 40.0F, trialSpawner.getTargetCooldownLength())) {
/* 119 */           serverLevel.playSound(null, spawnerPos, SoundEvents.TRIAL_SPAWNER_OPEN_SHUTTER, SoundSource.BLOCKS);
/*     */         }
/*     */ 
/*     */ 
/*     */       
/*     */       case 4:
/* 125 */         if (!data.isReadyToEjectItems(serverLevel, TIME_BETWEEN_EACH_EJECTION, trialSpawner.getTargetCooldownLength()));
/*     */ 
/*     */ 
/*     */         
/* 129 */         if (data.detectedPlayers.isEmpty()) {
/* 130 */           serverLevel.playSound(null, spawnerPos, SoundEvents.TRIAL_SPAWNER_CLOSE_SHUTTER, SoundSource.BLOCKS);
/* 131 */           data.ejectingLootTable = Optional.empty();
/*     */         } 
/*     */ 
/*     */         
/* 135 */         if (data.ejectingLootTable.isEmpty()) {
/* 136 */           data.ejectingLootTable = config.lootTablesToEject().getRandom(serverLevel.getRandom());
/*     */         }
/*     */         
/* 139 */         data.ejectingLootTable.ifPresent(lootTable -> trialSpawner.ejectReward(serverLevel, spawnerPos, lootTable));
/* 140 */         data.detectedPlayers.remove(data.detectedPlayers.iterator().next());
/*     */ 
/*     */       
/*     */       case 5:
/* 144 */         data.tryDetectPlayers(serverLevel, spawnerPos, trialSpawner);
/* 145 */         if (!data.detectedPlayers.isEmpty()) {
/*     */           
/* 147 */           data.totalMobsSpawned = 0;
/* 148 */           data.nextMobSpawnsAt = 0L;
/*     */         } 
/* 150 */         if (data.isCooldownFinished(serverLevel)) {
/* 151 */           trialSpawner.removeOminous(serverLevel, spawnerPos);
/* 152 */           data.reset();
/*     */         }  break; }
/*     */     
/* 155 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void spawnOminousOminousItemSpawner(ServerLevel level, BlockPos trialSpawnerPos, TrialSpawner trialSpawner) {
/* 161 */     TrialSpawnerStateData data = trialSpawner.getStateData();
/* 162 */     TrialSpawnerConfig config = trialSpawner.activeConfig();
/*     */     
/* 164 */     ItemStack itemToDispense = data.getDispensingItems(level, config, trialSpawnerPos).getRandom(level.random).orElse(ItemStack.EMPTY);
/* 165 */     if (itemToDispense.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 169 */     if (timeToSpawnItemSpawner(level, data)) {
/* 170 */       calculatePositionToSpawnSpawner(level, trialSpawnerPos, trialSpawner, data).ifPresent(pos -> {
/*     */             OminousItemSpawner itemSpawner = OminousItemSpawner.create((Level)level, itemToDispense);
/*     */             itemSpawner.snapTo(pos);
/*     */             level.addFreshEntity((Entity)itemSpawner);
/*     */             float pitch = (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F;
/*     */             level.playSound(null, BlockPos.containing((Position)pos), SoundEvents.TRIAL_SPAWNER_SPAWN_ITEM_BEGIN, SoundSource.BLOCKS, 1.0F, pitch);
/*     */             data.cooldownEndsAt = level.getGameTime() + trialSpawner.ominousConfig().ticksBetweenItemSpawners();
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Optional<Vec3> calculatePositionToSpawnSpawner(ServerLevel level, BlockPos trialSpawnerPos, TrialSpawner trialSpawner, TrialSpawnerStateData data) {
/* 184 */     Objects.requireNonNull(level); List<Player> nearbyPlayers = data.detectedPlayers.stream().map(level::getPlayerByUUID)
/* 185 */       .filter(Objects::nonNull)
/* 186 */       .filter(player -> (!player.isCreative() && !player.isSpectator() && player.isAlive() && player.distanceToSqr(trialSpawnerPos.getCenter()) <= Mth.square(trialSpawner.getRequiredPlayerRange())))
/*     */ 
/*     */ 
/*     */       
/* 190 */       .toList();
/*     */     
/* 192 */     if (nearbyPlayers.isEmpty()) {
/* 193 */       return Optional.empty();
/*     */     }
/*     */     
/* 196 */     Entity entity = selectEntityToSpawnItemAbove(nearbyPlayers, data.currentMobs, trialSpawner, trialSpawnerPos, level);
/*     */     
/* 198 */     if (entity == null) {
/* 199 */       return Optional.empty();
/*     */     }
/*     */     
/* 202 */     return calculatePositionAbove(entity, level);
/*     */   }
/*     */   
/*     */   private static Optional<Vec3> calculatePositionAbove(Entity entityToSpawnItemAbove, ServerLevel level) {
/* 206 */     Vec3 entityPos = entityToSpawnItemAbove.position();
/* 207 */     Vec3 trySpawnPos = entityPos.relative(Direction.UP, (entityToSpawnItemAbove.getBbHeight() + 2.0F + level.random.nextInt(4)));
/* 208 */     BlockHitResult hitResult = level.clip(new ClipContext(entityPos, trySpawnPos, ClipContext.Block.VISUAL, ClipContext.Fluid.NONE, net.minecraft.world.phys.shapes.CollisionContext.empty()));
/* 209 */     Vec3 down = hitResult.getBlockPos().getCenter().relative(Direction.DOWN, 1.0D);
/* 210 */     BlockPos blockPosDown = BlockPos.containing((Position)down);
/* 211 */     if (!level.getBlockState(blockPosDown).getCollisionShape((BlockGetter)level, blockPosDown).isEmpty()) {
/* 212 */       return Optional.empty();
/*     */     }
/* 214 */     return Optional.of(down);
/*     */   }
/*     */ 
/*     */   
/*     */   private static Entity selectEntityToSpawnItemAbove(List<Player> nearbyPlayers, Set<UUID> mobIds, TrialSpawner trialSpawner, BlockPos spawnerPos, ServerLevel level) {
/* 219 */     Objects.requireNonNull(level); Stream<Entity> nearbyMobs = mobIds.stream().map(level::getEntity)
/* 220 */       .filter(Objects::nonNull)
/* 221 */       .filter(target -> (target.isAlive() && target.distanceToSqr(spawnerPos.getCenter()) <= Mth.square(trialSpawner.getRequiredPlayerRange())));
/*     */     
/* 223 */     List<? extends Entity> eligibleEntities = level.random.nextBoolean() ? nearbyMobs.toList() : (List)nearbyPlayers;
/*     */     
/* 225 */     if (eligibleEntities.isEmpty()) {
/* 226 */       return null;
/*     */     }
/*     */     
/* 229 */     if (eligibleEntities.size() == 1) {
/* 230 */       return eligibleEntities.getFirst();
/*     */     }
/*     */     
/* 233 */     return (Entity)Util.getRandom(eligibleEntities, level.random);
/*     */   }
/*     */   
/*     */   private boolean timeToSpawnItemSpawner(ServerLevel serverLevel, TrialSpawnerStateData data) {
/* 237 */     return (serverLevel.getGameTime() >= data.cooldownEndsAt);
/*     */   }
/*     */   
/*     */   public int lightLevel() {
/* 241 */     return this.lightLevel;
/*     */   }
/*     */   
/*     */   public double spinningMobSpeed() {
/* 245 */     return this.spinningMobSpeed;
/*     */   }
/*     */   
/*     */   public boolean hasSpinningMob() {
/* 249 */     return (this.spinningMobSpeed >= 0.0D);
/*     */   }
/*     */   
/*     */   public boolean isCapableOfSpawning() {
/* 253 */     return this.isCapableOfSpawning;
/*     */   }
/*     */   
/*     */   public void emitParticles(Level level, BlockPos blockPos, boolean isOminous) {
/* 257 */     this.particleEmission.emit(level, level.getRandom(), blockPos, isOminous);
/*     */   }
/*     */   private static class LightLevel {
/*     */     private static final int UNLIT = 0; private static final int HALF_LIT = 4; private static final int LIT = 8; }
/*     */   public String getSerializedName() {
/* 262 */     return this.name;
/*     */   }
/*     */   
/*     */   private static class SpinningMob
/*     */   {
/*     */     private static final double NONE = -1.0D;
/*     */     private static final double SLOW = 200.0D;
/*     */     private static final double FAST = 1000.0D;
/*     */   }
/*     */   
/*     */   private static interface ParticleEmission {
/*     */     public static final ParticleEmission NONE = (level, random, pos, isOminous) -> {
/*     */       
/*     */       };
/*     */     public static final ParticleEmission SMALL_FLAMES;
/*     */     
/*     */     static {
/* 279 */       SMALL_FLAMES = ((level, random, pos, isOminous) -> {
/*     */           if (random.nextInt(2) == 0) {
/*     */             Vec3 vec = pos.getCenter().offsetRandom(random, 0.9F);
/*     */             addParticle(isOminous ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.SMALL_FLAME, vec, level);
/*     */           } 
/*     */         });
/* 285 */       FLAMES_AND_SMOKE = ((level, random, pos, isOminous) -> {
/*     */           Vec3 vec = pos.getCenter().offsetRandom(random, 1.0F);
/*     */           addParticle(ParticleTypes.SMOKE, vec, level);
/*     */           addParticle(isOminous ? ParticleTypes.SOUL_FIRE_FLAME : ParticleTypes.FLAME, vec, level);
/*     */         });
/* 290 */       SMOKE_INSIDE_AND_TOP_FACE = ((level, random, pos, isOminous) -> {
/*     */           Vec3 vec = pos.getCenter().offsetRandom(random, 0.9F);
/*     */           if (random.nextInt(3) == 0)
/*     */             addParticle(ParticleTypes.SMOKE, vec, level); 
/*     */           if (level.getGameTime() % 20L == 0L) {
/*     */             Vec3 topFaceVec = pos.getCenter().add(0.0D, 0.5D, 0.0D);
/*     */             int smokeCount = level.getRandom().nextInt(4) + 20;
/*     */             for (int i = 0; i < smokeCount; i++)
/*     */               addParticle(ParticleTypes.SMOKE, topFaceVec, level); 
/*     */           } 
/*     */         });
/*     */     }
/*     */     public static final ParticleEmission FLAMES_AND_SMOKE;
/*     */     public static final ParticleEmission SMOKE_INSIDE_AND_TOP_FACE;
/*     */     
/*     */     private static void addParticle(SimpleParticleType smoke, Vec3 vec, Level level) {
/* 306 */       level.addParticle((ParticleOptions)smoke, vec.x(), vec.y(), vec.z(), 0.0D, 0.0D, 0.0D);
/*     */     }
/*     */     
/*     */     void emit(Level param1Level, RandomSource param1RandomSource, BlockPos param1BlockPos, boolean param1Boolean);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/trialspawner/TrialSpawnerState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */