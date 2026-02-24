/*     */ package net.minecraft.server.level;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.Vec3i;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.entity.EntityDimensions;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.CollisionGetter;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.phys.Vec3;
/*     */ 
/*     */ public class PlayerSpawnFinder {
/*  30 */   private static final EntityDimensions PLAYER_DIMENSIONS = EntityType.PLAYER.getDimensions();
/*     */   
/*     */   private static final int ABSOLUTE_MAX_ATTEMPTS = 1024;
/*     */   
/*     */   private final ServerLevel level;
/*     */   
/*     */   private final BlockPos spawnSuggestion;
/*     */   
/*     */   private final int radius;
/*     */   private final int candidateCount;
/*     */   private final int coprime;
/*     */   private final int offset;
/*     */   private int nextCandidateIndex;
/*  43 */   private final CompletableFuture<Vec3> finishedFuture = new CompletableFuture<>();
/*     */   
/*     */   private PlayerSpawnFinder(ServerLevel level, BlockPos spawnSuggestion, int radius) {
/*  46 */     this.level = level;
/*  47 */     this.spawnSuggestion = spawnSuggestion;
/*  48 */     this.radius = radius;
/*  49 */     long squareSide = radius * 2L + 1L;
/*  50 */     this.candidateCount = (int)Math.min(1024L, squareSide * squareSide);
/*  51 */     this.coprime = getCoprime(this.candidateCount);
/*  52 */     this.offset = RandomSource.create().nextInt(this.candidateCount);
/*     */   }
/*     */ 
/*     */   
/*     */   public static CompletableFuture<Vec3> findSpawn(ServerLevel level, BlockPos spawnSuggestion) {
/*  57 */     if (!level.dimensionType().hasSkyLight() || level.getServer().getWorldData().getGameType() == GameType.ADVENTURE) {
/*  58 */       return CompletableFuture.completedFuture(fixupSpawnHeight((CollisionGetter)level, spawnSuggestion));
/*     */     }
/*     */     
/*  61 */     int radius = Math.max(0, (Integer)level.getGameRules().get(GameRules.RESPAWN_RADIUS));
/*  62 */     int distToBorder = Mth.floor(level.getWorldBorder().getDistanceToBorder(spawnSuggestion.getX(), spawnSuggestion.getZ()));
/*  63 */     if (distToBorder < radius) {
/*  64 */       radius = distToBorder;
/*     */     }
/*  66 */     if (distToBorder <= 1) {
/*  67 */       radius = 1;
/*     */     }
/*     */     
/*  70 */     PlayerSpawnFinder finder = new PlayerSpawnFinder(level, spawnSuggestion, radius);
/*  71 */     finder.scheduleNext();
/*     */     
/*  73 */     return finder.finishedFuture;
/*     */   }
/*     */   
/*     */   private void scheduleNext() {
/*  77 */     int candidateIndex = this.nextCandidateIndex++;
/*  78 */     if (candidateIndex < this.candidateCount) {
/*  79 */       int value = (this.offset + this.coprime * candidateIndex) % this.candidateCount;
/*  80 */       int deltaX = value % (this.radius * 2 + 1);
/*  81 */       int deltaZ = value / (this.radius * 2 + 1);
/*  82 */       int targetX = this.spawnSuggestion.getX() + deltaX - this.radius;
/*  83 */       int targetZ = this.spawnSuggestion.getZ() + deltaZ - this.radius;
/*  84 */       scheduleCandidate(targetX, targetZ, candidateIndex, () -> {
/*     */             BlockPos spawnPos = getOverworldRespawnPos(this.level, targetX, targetZ);
/*  86 */             return (spawnPos != null && noCollisionNoLiquid((CollisionGetter)this.level, spawnPos)) ? Optional.of(Vec3.atBottomCenterOf((Vec3i)spawnPos)) : Optional.empty();
/*     */ 
/*     */           
/*     */           });
/*     */     } else {
/*     */       
/*  92 */       scheduleCandidate(this.spawnSuggestion.getX(), this.spawnSuggestion.getZ(), candidateIndex, () -> Optional.of(fixupSpawnHeight((CollisionGetter)this.level, this.spawnSuggestion)));
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static Vec3 fixupSpawnHeight(CollisionGetter level, BlockPos spawnPos) {
/*  99 */     BlockPos.MutableBlockPos mutablePos = spawnPos.mutable();
/*     */     
/* 101 */     while (!noCollisionNoLiquid(level, (BlockPos)mutablePos) && mutablePos.getY() < level.getMaxY()) {
/* 102 */       mutablePos.move(Direction.UP);
/*     */     }
/*     */     
/* 105 */     mutablePos.move(Direction.DOWN);
/* 106 */     while (noCollisionNoLiquid(level, (BlockPos)mutablePos) && mutablePos.getY() > level.getMinY()) {
/* 107 */       mutablePos.move(Direction.DOWN);
/*     */     }
/* 109 */     mutablePos.move(Direction.UP);
/*     */     
/* 111 */     return Vec3.atBottomCenterOf((Vec3i)mutablePos);
/*     */   }
/*     */   
/*     */   private static boolean noCollisionNoLiquid(CollisionGetter level, BlockPos pos) {
/* 115 */     return level.noCollision(null, PLAYER_DIMENSIONS.makeBoundingBox(pos.getBottomCenter()), true);
/*     */   }
/*     */ 
/*     */   
/*     */   private static int getCoprime(int possibleOrigins) {
/* 120 */     return (possibleOrigins <= 16) ? (possibleOrigins - 1) : 17;
/*     */   }
/*     */   
/*     */   private void scheduleCandidate(int candidateX, int candidateZ, int candidateIndex, Supplier<Optional<Vec3>> candidateChecker) {
/* 124 */     if (this.finishedFuture.isDone()) {
/*     */       return;
/*     */     }
/*     */     
/* 128 */     int chunkX = SectionPos.blockToSectionCoord(candidateX);
/* 129 */     int chunkZ = SectionPos.blockToSectionCoord(candidateZ);
/* 130 */     this.level.getChunkSource().addTicketAndLoadWithRadius(TicketType.SPAWN_SEARCH, new ChunkPos(chunkX, chunkZ), 0).whenCompleteAsync((ignored, throwable) -> {
/*     */           if (throwable == null)
/*     */             try {
/*     */               Optional<Vec3> spawnPos = candidateChecker.get();
/*     */               if (spawnPos.isPresent()) {
/*     */                 this.finishedFuture.complete(spawnPos.get());
/*     */               } else {
/*     */                 scheduleNext();
/*     */               } 
/* 139 */             } catch (Throwable t) {
/*     */               throwable = t;
/*     */             }  
/*     */           if (throwable != null) {
/*     */             CrashReport report = CrashReport.forThrowable(throwable, "Searching for spawn");
/*     */             CrashReportCategory details = report.addCategory("Spawn Lookup");
/*     */             Objects.requireNonNull(this.spawnSuggestion);
/*     */             details.setDetail("Origin", this.spawnSuggestion::toString);
/*     */             details.setDetail("Radius", ());
/*     */             details.setDetail("Candidate", ());
/*     */             details.setDetail("Progress", ());
/*     */             this.finishedFuture.completeExceptionally((Throwable)new ReportedException(report));
/*     */           } 
/* 152 */         }, (Executor)this.level.getServer());
/*     */   }
/*     */   
/*     */   protected static BlockPos getOverworldRespawnPos(ServerLevel level, int x, int z) {
/* 156 */     boolean caveWorld = level.dimensionType().hasCeiling();
/*     */ 
/*     */ 
/*     */     
/* 160 */     LevelChunk chunk = level.getChunk(SectionPos.blockToSectionCoord(x), SectionPos.blockToSectionCoord(z));
/* 161 */     int topY = caveWorld ? level.getChunkSource().getGenerator().getSpawnHeight((net.minecraft.world.level.LevelHeightAccessor)level) : chunk.getHeight(Heightmap.Types.MOTION_BLOCKING, x & 0xF, z & 0xF);
/*     */ 
/*     */     
/* 164 */     if (topY < level.getMinY()) {
/* 165 */       return null;
/*     */     }
/*     */ 
/*     */     
/* 169 */     int surface = chunk.getHeight(Heightmap.Types.WORLD_SURFACE, x & 0xF, z & 0xF);
/* 170 */     if (surface <= topY && surface > chunk.getHeight(Heightmap.Types.OCEAN_FLOOR, x & 0xF, z & 0xF)) {
/* 171 */       return null;
/*     */     }
/*     */     
/* 174 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/*     */     
/* 176 */     for (int y = topY + 1; y >= level.getMinY(); y--) {
/* 177 */       pos.set(x, y, z);
/* 178 */       BlockState blockState = level.getBlockState((BlockPos)pos);
/*     */ 
/*     */       
/* 181 */       if (!blockState.getFluidState().isEmpty()) {
/*     */         break;
/*     */       }
/*     */ 
/*     */       
/* 186 */       if (Block.isFaceFull(blockState.getCollisionShape((BlockGetter)level, (BlockPos)pos), Direction.UP)) {
/* 187 */         return pos.above().immutable();
/*     */       }
/*     */     } 
/* 190 */     return null;
/*     */   }
/*     */   
/*     */   public static BlockPos getSpawnPosInChunk(ServerLevel level, ChunkPos chunkPos) {
/* 194 */     if (net.minecraft.SharedConstants.debugVoidTerrain(chunkPos)) {
/* 195 */       return null;
/*     */     }
/*     */     
/* 198 */     for (int x = chunkPos.getMinBlockX(); x <= chunkPos.getMaxBlockX(); x++) {
/* 199 */       for (int z = chunkPos.getMinBlockZ(); z <= chunkPos.getMaxBlockZ(); z++) {
/* 200 */         BlockPos validSpawnPosition = getOverworldRespawnPos(level, x, z);
/* 201 */         if (validSpawnPosition != null) {
/* 202 */           return validSpawnPosition;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 207 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/level/PlayerSpawnFinder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */