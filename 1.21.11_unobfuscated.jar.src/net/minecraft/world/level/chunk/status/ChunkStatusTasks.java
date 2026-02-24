/*     */ package net.minecraft.world.level.chunk.status;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import net.minecraft.server.level.GenerationChunkHolder;
/*     */ import net.minecraft.server.level.ServerLevel;
/*     */ import net.minecraft.server.level.ThreadedLevelLightEngine;
/*     */ import net.minecraft.server.level.WorldGenRegion;
/*     */ import net.minecraft.util.ProblemReporter;
/*     */ import net.minecraft.util.StaticCache2D;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ImposterProtoChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.ProtoChunk;
/*     */ import net.minecraft.world.level.levelgen.BelowZeroRetrogen;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.levelgen.blending.Blender;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import net.minecraft.world.level.storage.TagValueInput;
/*     */ import net.minecraft.world.level.storage.ValueInput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ChunkStatusTasks {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static boolean isLighted(ChunkAccess chunk) {
/*  31 */     return (chunk.getPersistedStatus().isOrAfter(ChunkStatus.LIGHT) && chunk.isLightCorrect());
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> passThrough(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  35 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateStructureStarts(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  39 */     ServerLevel level = context.level();
/*  40 */     if (level.getServer().getWorldData().worldGenOptions().generateStructures()) {
/*  41 */       context.generator().createStructures(level.registryAccess(), level.getChunkSource().getGeneratorState(), level.structureManager(), chunk, context.structureManager(), level.dimension());
/*     */     }
/*  43 */     level.onStructureStartsAvailable(chunk);
/*  44 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> loadStructureStarts(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> cache, ChunkAccess chunk) {
/*  48 */     context.level().onStructureStartsAvailable(chunk);
/*  49 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateStructureReferences(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  53 */     ServerLevel level = context.level();
/*  54 */     WorldGenRegion region = new WorldGenRegion(level, chunks, step, chunk);
/*  55 */     context.generator().createReferences((WorldGenLevel)region, level.structureManager().forWorldGenRegion(region), chunk);
/*  56 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateBiomes(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  60 */     ServerLevel level = context.level();
/*  61 */     WorldGenRegion region = new WorldGenRegion(level, chunks, step, chunk);
/*  62 */     return context.generator().createBiomes(level.getChunkSource().randomState(), Blender.of(region), level.structureManager().forWorldGenRegion(region), chunk);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateNoise(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  66 */     ServerLevel level = context.level();
/*  67 */     WorldGenRegion region = new WorldGenRegion(level, chunks, step, chunk);
/*  68 */     return context.generator().fillFromNoise(Blender.of(region), level.getChunkSource().randomState(), level.structureManager().forWorldGenRegion(region), chunk).thenApply(generatedChunk -> {
/*     */           if (generatedChunk instanceof ProtoChunk) {
/*     */             ProtoChunk protoChunk = (ProtoChunk)generatedChunk;
/*     */             BelowZeroRetrogen belowZeroRetrogen = protoChunk.getBelowZeroRetrogen();
/*     */             if (belowZeroRetrogen != null) {
/*     */               BelowZeroRetrogen.replaceOldBedrock(protoChunk);
/*     */               if (belowZeroRetrogen.hasBedrockHoles()) {
/*     */                 belowZeroRetrogen.applyBedrockMask(protoChunk);
/*     */               }
/*     */             } 
/*     */           } 
/*     */           return generatedChunk;
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateSurface(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  85 */     ServerLevel level = context.level();
/*  86 */     WorldGenRegion region = new WorldGenRegion(level, chunks, step, chunk);
/*  87 */     context.generator().buildSurface(region, level.structureManager().forWorldGenRegion(region), level.getChunkSource().randomState(), chunk);
/*  88 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateCarvers(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/*  92 */     ServerLevel level = context.level();
/*  93 */     WorldGenRegion region = new WorldGenRegion(level, chunks, step, chunk);
/*  94 */     if (chunk instanceof ProtoChunk) { ProtoChunk protoChunk = (ProtoChunk)chunk;
/*  95 */       Blender.addAroundOldChunksCarvingMaskFilter((WorldGenLevel)region, protoChunk); }
/*     */     
/*  97 */     context.generator().applyCarvers(region, level.getSeed(), level.getChunkSource().randomState(), level.getBiomeManager(), level.structureManager().forWorldGenRegion(region), chunk);
/*  98 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateFeatures(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/* 102 */     ServerLevel level = context.level();
/* 103 */     Heightmap.primeHeightmaps(chunk, EnumSet.of(Heightmap.Types.MOTION_BLOCKING, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Heightmap.Types.OCEAN_FLOOR, Heightmap.Types.WORLD_SURFACE));
/* 104 */     WorldGenRegion region = new WorldGenRegion(level, chunks, step, chunk);
/* 105 */     if (!net.minecraft.SharedConstants.DEBUG_DISABLE_FEATURES) {
/* 106 */       context.generator().applyBiomeDecoration((WorldGenLevel)region, chunk, level.structureManager().forWorldGenRegion(region));
/*     */     }
/*     */     
/* 109 */     Blender.generateBorderTicks(region, chunk);
/* 110 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> initializeLight(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/* 114 */     ThreadedLevelLightEngine lightEngine = context.lightEngine();
/* 115 */     chunk.initializeLightSources();
/* 116 */     ((ProtoChunk)chunk).setLightEngine((LevelLightEngine)lightEngine);
/* 117 */     boolean lighted = isLighted(chunk);
/*     */     
/* 119 */     return lightEngine.initializeLight(chunk, lighted);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> light(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/* 123 */     boolean lighted = isLighted(chunk);
/* 124 */     return context.lightEngine().lightChunk(chunk, lighted);
/*     */   }
/*     */ 
/*     */   
/*     */   static CompletableFuture<ChunkAccess> generateSpawn(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/* 129 */     if (!chunk.isUpgrading()) {
/* 130 */       context.generator().spawnOriginalMobs(new WorldGenRegion(context.level(), chunks, step, chunk));
/*     */     }
/* 132 */     return CompletableFuture.completedFuture(chunk);
/*     */   }
/*     */   
/*     */   static CompletableFuture<ChunkAccess> full(WorldGenContext context, ChunkStep step, StaticCache2D<GenerationChunkHolder> chunks, ChunkAccess chunk) {
/* 136 */     ChunkPos pos = chunk.getPos();
/* 137 */     GenerationChunkHolder holder = (GenerationChunkHolder)chunks.get(pos.x, pos.z);
/* 138 */     return CompletableFuture.supplyAsync(() -> {
/*     */           LevelChunk levelChunk;
/*     */           
/*     */           ProtoChunk protoChunk = (ProtoChunk)chunk;
/*     */           
/*     */           ServerLevel level = context.level();
/*     */           
/*     */           if (protoChunk instanceof ImposterProtoChunk) {
/*     */             ImposterProtoChunk imposter = (ImposterProtoChunk)protoChunk;
/*     */             levelChunk = imposter.getWrapped();
/*     */           } else {
/*     */             levelChunk = new LevelChunk(level, protoChunk, ());
/*     */             holder.replaceProtoChunk(new ImposterProtoChunk(levelChunk, false));
/*     */           } 
/*     */           Objects.requireNonNull(holder);
/*     */           levelChunk.setFullStatus(holder::getFullStatus);
/*     */           levelChunk.runPostLoad();
/*     */           levelChunk.setLoaded(true);
/*     */           levelChunk.registerAllBlockEntitiesAfterLevelLoad();
/*     */           levelChunk.registerTickContainerInLevel(level);
/*     */           levelChunk.setUnsavedListener(context.unsavedListener());
/*     */           return levelChunk;
/* 160 */         }, context.mainThreadExecutor());
/*     */   }
/*     */   
/*     */   private static void postLoadProtoChunk(ServerLevel level, ValueInput.ValueInputList entities) {
/* 164 */     if (!entities.isEmpty())
/* 165 */       level.addWorldGenChunkEntities(EntityType.loadEntitiesRecursive(entities, (Level)level, net.minecraft.world.entity.EntitySpawnReason.LOAD)); 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/status/ChunkStatusTasks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */