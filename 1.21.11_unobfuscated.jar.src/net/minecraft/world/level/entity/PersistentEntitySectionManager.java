/*     */ package net.minecraft.world.level.entity;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Queues;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectFunction;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.io.Writer;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.server.level.FullChunkStatus;
/*     */ import net.minecraft.util.CsvOutput;
/*     */ import net.minecraft.util.VisibleForDebug;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class PersistentEntitySectionManager<T extends EntityAccess> implements AutoCloseable {
/*     */   private enum ChunkLoadStatus {
/*  35 */     FRESH,
/*  36 */     PENDING,
/*  37 */     LOADED;
/*     */   }
/*     */   
/*     */   private class Callback
/*     */     implements EntityInLevelCallback {
/*     */     private final T entity;
/*     */     private long currentSectionKey;
/*     */     private EntitySection<T> currentSection;
/*     */     
/*     */     private Callback(T entity, long currentSectionKey, EntitySection<T> currentSection) {
/*  47 */       this.entity = entity;
/*  48 */       this.currentSectionKey = currentSectionKey;
/*  49 */       this.currentSection = currentSection;
/*     */     }
/*     */ 
/*     */     
/*     */     public void onMove() {
/*  54 */       BlockPos pos = this.entity.blockPosition();
/*  55 */       long newSectionPos = SectionPos.asLong(pos);
/*  56 */       if (newSectionPos != this.currentSectionKey) {
/*  57 */         Visibility previousStatus = this.currentSection.getStatus();
/*  58 */         if (!this.currentSection.remove(this.entity)) {
/*  59 */           PersistentEntitySectionManager.LOGGER.warn("Entity {} wasn't found in section {} (moving to {})", new Object[] { this.entity, SectionPos.of(this.currentSectionKey), newSectionPos });
/*     */         }
/*  61 */         PersistentEntitySectionManager.this.removeSectionIfEmpty(this.currentSectionKey, this.currentSection);
/*     */         
/*  63 */         EntitySection<T> newSection = PersistentEntitySectionManager.this.sectionStorage.getOrCreateSection(newSectionPos);
/*  64 */         newSection.add(this.entity);
/*  65 */         this.currentSection = newSection;
/*  66 */         this.currentSectionKey = newSectionPos;
/*     */         
/*  68 */         updateStatus(previousStatus, newSection.getStatus());
/*     */       } 
/*     */     }
/*     */     
/*     */     private void updateStatus(Visibility previousStatus, Visibility newStatus) {
/*  73 */       Visibility effectivePreviousStatus = PersistentEntitySectionManager.getEffectiveStatus(this.entity, previousStatus);
/*  74 */       Visibility effectiveNewStatus = PersistentEntitySectionManager.getEffectiveStatus(this.entity, newStatus);
/*     */       
/*  76 */       if (effectivePreviousStatus == effectiveNewStatus) {
/*  77 */         if (effectiveNewStatus.isAccessible()) {
/*  78 */           PersistentEntitySectionManager.this.callbacks.onSectionChange(this.entity);
/*     */         }
/*     */         
/*     */         return;
/*     */       } 
/*  83 */       boolean wasAccessible = effectivePreviousStatus.isAccessible();
/*  84 */       boolean isAccessible = effectiveNewStatus.isAccessible();
/*  85 */       if (wasAccessible && !isAccessible) {
/*  86 */         PersistentEntitySectionManager.this.stopTracking(this.entity);
/*  87 */       } else if (!wasAccessible && isAccessible) {
/*  88 */         PersistentEntitySectionManager.this.startTracking(this.entity);
/*     */       } 
/*     */       
/*  91 */       boolean wasTicking = effectivePreviousStatus.isTicking();
/*  92 */       boolean isTicking = effectiveNewStatus.isTicking();
/*  93 */       if (wasTicking && !isTicking) {
/*  94 */         PersistentEntitySectionManager.this.stopTicking(this.entity);
/*  95 */       } else if (!wasTicking && isTicking) {
/*  96 */         PersistentEntitySectionManager.this.startTicking(this.entity);
/*     */       } 
/*     */       
/*  99 */       if (isAccessible) {
/* 100 */         PersistentEntitySectionManager.this.callbacks.onSectionChange(this.entity);
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public void onRemove(Entity.RemovalReason reason) {
/* 106 */       if (!this.currentSection.remove(this.entity)) {
/* 107 */         PersistentEntitySectionManager.LOGGER.warn("Entity {} wasn't found in section {} (destroying due to {})", new Object[] { this.entity, SectionPos.of(this.currentSectionKey), reason });
/*     */       }
/*     */       
/* 110 */       Visibility status = PersistentEntitySectionManager.getEffectiveStatus(this.entity, this.currentSection.getStatus());
/* 111 */       if (status.isTicking()) {
/* 112 */         PersistentEntitySectionManager.this.stopTicking(this.entity);
/*     */       }
/* 114 */       if (status.isAccessible()) {
/* 115 */         PersistentEntitySectionManager.this.stopTracking(this.entity);
/*     */       }
/* 117 */       if (reason.shouldDestroy()) {
/* 118 */         PersistentEntitySectionManager.this.callbacks.onDestroyed(this.entity);
/*     */       }
/* 120 */       PersistentEntitySectionManager.this.knownUuids.remove(this.entity.getUUID());
/* 121 */       this.entity.setLevelCallback(NULL);
/*     */       
/* 123 */       PersistentEntitySectionManager.this.removeSectionIfEmpty(this.currentSectionKey, this.currentSection);
/*     */     }
/*     */   }
/*     */   
/* 127 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/* 129 */   private final Set<UUID> knownUuids = Sets.newHashSet();
/*     */   
/*     */   private final LevelCallback<T> callbacks;
/*     */   private final EntityPersistentStorage<T> permanentStorage;
/*     */   private final EntityLookup<T> visibleEntityStorage;
/*     */   private final EntitySectionStorage<T> sectionStorage;
/*     */   private final LevelEntityGetter<T> entityGetter;
/* 136 */   private final Long2ObjectMap<Visibility> chunkVisibility = (Long2ObjectMap<Visibility>)new Long2ObjectOpenHashMap();
/* 137 */   private final Long2ObjectMap<ChunkLoadStatus> chunkLoadStatuses = (Long2ObjectMap<ChunkLoadStatus>)new Long2ObjectOpenHashMap();
/*     */   
/* 139 */   private final LongSet chunksToUnload = (LongSet)new LongOpenHashSet();
/* 140 */   private final Queue<ChunkEntities<T>> loadingInbox = Queues.newConcurrentLinkedQueue();
/*     */   
/*     */   public PersistentEntitySectionManager(Class<T> entityClass, LevelCallback<T> callbacks, EntityPersistentStorage<T> permanentStorage) {
/* 143 */     this.visibleEntityStorage = new EntityLookup<>();
/* 144 */     this.sectionStorage = new EntitySectionStorage<>(entityClass, (Long2ObjectFunction<Visibility>)this.chunkVisibility);
/* 145 */     this.chunkVisibility.defaultReturnValue(Visibility.HIDDEN);
/* 146 */     this.chunkLoadStatuses.defaultReturnValue(ChunkLoadStatus.FRESH);
/* 147 */     this.callbacks = callbacks;
/* 148 */     this.permanentStorage = permanentStorage;
/* 149 */     this.entityGetter = new LevelEntityGetterAdapter<>(this.visibleEntityStorage, this.sectionStorage);
/*     */   }
/*     */   
/*     */   private void removeSectionIfEmpty(long sectionPos, EntitySection<T> section) {
/* 153 */     if (section.isEmpty()) {
/* 154 */       this.sectionStorage.remove(sectionPos);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean addEntityUuid(T entity) {
/* 159 */     if (!this.knownUuids.add(entity.getUUID())) {
/* 160 */       LOGGER.warn("UUID of added entity already exists: {}", entity);
/* 161 */       return false;
/*     */     } 
/* 163 */     return true;
/*     */   }
/*     */   
/*     */   public boolean addNewEntity(T entity) {
/* 167 */     return addEntity(entity, false);
/*     */   }
/*     */   
/*     */   private boolean addEntity(T entity, boolean loaded) {
/* 171 */     if (!addEntityUuid(entity)) {
/* 172 */       return false;
/*     */     }
/*     */     
/* 175 */     long sectionKey = SectionPos.asLong(entity.blockPosition());
/* 176 */     EntitySection<T> entitySection = this.sectionStorage.getOrCreateSection(sectionKey);
/* 177 */     entitySection.add(entity);
/*     */     
/* 179 */     entity.setLevelCallback(new Callback(entity, sectionKey, entitySection));
/* 180 */     if (!loaded) {
/* 181 */       this.callbacks.onCreated(entity);
/*     */     }
/*     */     
/* 184 */     Visibility status = getEffectiveStatus(entity, entitySection.getStatus());
/* 185 */     if (status.isAccessible()) {
/* 186 */       startTracking(entity);
/*     */     }
/* 188 */     if (status.isTicking()) {
/* 189 */       startTicking(entity);
/*     */     }
/* 191 */     return true;
/*     */   }
/*     */   
/*     */   private static <T extends EntityAccess> Visibility getEffectiveStatus(T entity, Visibility status) {
/* 195 */     return entity.isAlwaysTicking() ? Visibility.TICKING : status;
/*     */   }
/*     */   
/*     */   public boolean isTicking(ChunkPos pos) {
/* 199 */     return ((Visibility)this.chunkVisibility.get(pos.toLong())).isTicking();
/*     */   }
/*     */   
/*     */   public void addLegacyChunkEntities(Stream<T> entities) {
/* 203 */     entities.forEach(e -> addEntity((T)e, true));
/*     */   }
/*     */   
/*     */   public void addWorldGenChunkEntities(Stream<T> entities) {
/* 207 */     entities.forEach(e -> addEntity((T)e, false));
/*     */   }
/*     */   
/*     */   private void startTicking(T entity) {
/* 211 */     this.callbacks.onTickingStart(entity);
/*     */   }
/*     */   
/*     */   private void stopTicking(T entity) {
/* 215 */     this.callbacks.onTickingEnd(entity);
/*     */   }
/*     */   
/*     */   private void startTracking(T entity) {
/* 219 */     this.visibleEntityStorage.add(entity);
/* 220 */     this.callbacks.onTrackingStart(entity);
/*     */   }
/*     */   
/*     */   private void stopTracking(T entity) {
/* 224 */     this.callbacks.onTrackingEnd(entity);
/* 225 */     this.visibleEntityStorage.remove(entity);
/*     */   }
/*     */   
/*     */   public void updateChunkStatus(ChunkPos pos, FullChunkStatus fullChunkStatus) {
/* 229 */     Visibility chunkStatus = Visibility.fromFullChunkStatus(fullChunkStatus);
/* 230 */     updateChunkStatus(pos, chunkStatus);
/*     */   }
/*     */   
/*     */   public void updateChunkStatus(ChunkPos pos, Visibility chunkStatus) {
/* 234 */     long chunkPosKey = pos.toLong();
/* 235 */     if (chunkStatus == Visibility.HIDDEN) {
/* 236 */       this.chunkVisibility.remove(chunkPosKey);
/* 237 */       this.chunksToUnload.add(chunkPosKey);
/*     */     } else {
/* 239 */       this.chunkVisibility.put(chunkPosKey, chunkStatus);
/* 240 */       this.chunksToUnload.remove(chunkPosKey);
/* 241 */       ensureChunkQueuedForLoad(chunkPosKey);
/*     */     } 
/*     */     
/* 244 */     this.sectionStorage.getExistingSectionsInChunk(chunkPosKey).forEach(section -> {
/*     */           Visibility previousStatus = chunkStatus.updateChunkStatus(chunkStatus);
/*     */           boolean wasAccessible = previousStatus.isAccessible(), isAccessible = chunkStatus.isAccessible(), wasTicking = previousStatus.isTicking(), isTicking = chunkStatus.isTicking();
/*     */           if (wasTicking && !isTicking) {
/*     */             chunkStatus.getEntities().filter(()).forEach(this::stopTicking);
/*     */           }
/*     */           if (wasAccessible && !isAccessible) {
/*     */             chunkStatus.getEntities().filter(()).forEach(this::stopTracking);
/*     */           } else if (!wasAccessible && isAccessible) {
/*     */             chunkStatus.getEntities().filter(()).forEach(this::startTracking);
/*     */           } 
/*     */           if (!wasTicking && isTicking) {
/*     */             chunkStatus.getEntities().filter(()).forEach(this::startTicking);
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void ensureChunkQueuedForLoad(long chunkPos) {
/* 270 */     ChunkLoadStatus chunkLoadStatus = (ChunkLoadStatus)this.chunkLoadStatuses.get(chunkPos);
/*     */     
/* 272 */     if (chunkLoadStatus == ChunkLoadStatus.FRESH) {
/* 273 */       requestChunkLoad(chunkPos);
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean storeChunkSections(long chunkPos, Consumer<T> savedEntityVisitor) {
/* 278 */     ChunkLoadStatus chunkLoadStatus = (ChunkLoadStatus)this.chunkLoadStatuses.get(chunkPos);
/* 279 */     if (chunkLoadStatus == ChunkLoadStatus.PENDING) {
/* 280 */       return false;
/*     */     }
/*     */     
/* 283 */     List<T> rootEntitiesToSave = (List<T>)this.sectionStorage.getExistingSectionsInChunk(chunkPos)
/* 284 */       .flatMap(section -> section.getEntities().filter(EntityAccess::shouldBeSaved))
/* 285 */       .collect(Collectors.toList());
/*     */     
/* 287 */     if (rootEntitiesToSave.isEmpty()) {
/* 288 */       if (chunkLoadStatus == ChunkLoadStatus.LOADED)
/*     */       {
/* 290 */         this.permanentStorage.storeEntities(new ChunkEntities<>(new ChunkPos(chunkPos), (List<T>)ImmutableList.of()));
/*     */       }
/* 292 */       return true;
/*     */     } 
/*     */     
/* 295 */     if (chunkLoadStatus == ChunkLoadStatus.FRESH) {
/*     */       
/* 297 */       requestChunkLoad(chunkPos);
/* 298 */       return false;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 303 */     this.permanentStorage.storeEntities(new ChunkEntities<>(new ChunkPos(chunkPos), rootEntitiesToSave));
/* 304 */     rootEntitiesToSave.forEach(savedEntityVisitor);
/* 305 */     return true;
/*     */   }
/*     */   
/*     */   private void requestChunkLoad(long chunkKey) {
/* 309 */     this.chunkLoadStatuses.put(chunkKey, ChunkLoadStatus.PENDING);
/* 310 */     ChunkPos pos = new ChunkPos(chunkKey);
/*     */     
/* 312 */     Objects.requireNonNull(this.loadingInbox); this.permanentStorage.loadEntities(pos).thenAccept(this.loadingInbox::add)
/* 313 */       .exceptionally(t -> {
/*     */           LOGGER.error("Failed to read chunk {}", pos, t);
/*     */           return null;
/*     */         });
/*     */   }
/*     */   
/*     */   private boolean processChunkUnload(long chunkKey) {
/* 320 */     boolean storeSuccessful = storeChunkSections(chunkKey, entity -> entity.getPassengersAndSelf().forEach(this::unloadEntity));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 326 */     if (!storeSuccessful)
/*     */     {
/* 328 */       return false;
/*     */     }
/*     */ 
/*     */     
/* 332 */     this.chunkLoadStatuses.remove(chunkKey);
/* 333 */     return true;
/*     */   }
/*     */   
/*     */   private void unloadEntity(EntityAccess e) {
/* 337 */     e.setRemoved(Entity.RemovalReason.UNLOADED_TO_CHUNK);
/* 338 */     e.setLevelCallback(EntityInLevelCallback.NULL);
/*     */   }
/*     */   
/*     */   private void processUnloads() {
/* 342 */     this.chunksToUnload.removeIf(chunkKey -> (this.chunkVisibility.get(chunkKey) != Visibility.HIDDEN) ? true : processChunkUnload(chunkKey));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void processPendingLoads() {
/*     */     ChunkEntities<T> loadedChunk;
/* 353 */     while ((loadedChunk = this.loadingInbox.poll()) != null) {
/*     */       
/* 355 */       loadedChunk.getEntities().forEach(e -> addEntity((T)e, true));
/* 356 */       this.chunkLoadStatuses.put(loadedChunk.getPos().toLong(), ChunkLoadStatus.LOADED);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 361 */     processPendingLoads();
/* 362 */     processUnloads();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private LongSet getAllChunksToSave() {
/* 368 */     LongSet result = this.sectionStorage.getAllChunksWithExistingSections();
/* 369 */     for (ObjectIterator<Long2ObjectMap.Entry<ChunkLoadStatus>> objectIterator = Long2ObjectMaps.fastIterable(this.chunkLoadStatuses).iterator(); objectIterator.hasNext(); ) { Long2ObjectMap.Entry<ChunkLoadStatus> entry = objectIterator.next();
/* 370 */       if (entry.getValue() == ChunkLoadStatus.LOADED) {
/* 371 */         result.add(entry.getLongKey());
/*     */       } }
/*     */     
/* 374 */     return result;
/*     */   }
/*     */   
/*     */   public void autoSave() {
/* 378 */     getAllChunksToSave().forEach(chunkKey -> {
/*     */           boolean shouldUnload = (this.chunkVisibility.get(chunkKey) == Visibility.HIDDEN);
/*     */           if (shouldUnload) {
/*     */             processChunkUnload(chunkKey);
/*     */           } else {
/*     */             storeChunkSections(chunkKey, ());
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void saveAll() {
/* 391 */     LongSet chunksToSave = getAllChunksToSave();
/*     */     
/* 393 */     while (!chunksToSave.isEmpty()) {
/* 394 */       this.permanentStorage.flush(false);
/* 395 */       processPendingLoads();
/* 396 */       chunksToSave.removeIf(chunkKey -> {
/*     */             boolean shouldUnload = (this.chunkVisibility.get(chunkKey) == Visibility.HIDDEN);
/*     */             return shouldUnload ? processChunkUnload(chunkKey) : storeChunkSections(chunkKey, ());
/*     */           });
/*     */     } 
/* 401 */     this.permanentStorage.flush(true);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws IOException {
/* 406 */     saveAll();
/* 407 */     this.permanentStorage.close();
/*     */   }
/*     */   
/*     */   public boolean isLoaded(UUID uuid) {
/* 411 */     return this.knownUuids.contains(uuid);
/*     */   }
/*     */   
/*     */   public LevelEntityGetter<T> getEntityGetter() {
/* 415 */     return this.entityGetter;
/*     */   }
/*     */   
/*     */   public boolean canPositionTick(BlockPos pos) {
/* 419 */     return ((Visibility)this.chunkVisibility.get(ChunkPos.asLong(pos))).isTicking();
/*     */   }
/*     */   
/*     */   public boolean canPositionTick(ChunkPos pos) {
/* 423 */     return ((Visibility)this.chunkVisibility.get(pos.toLong())).isTicking();
/*     */   }
/*     */   
/*     */   public boolean areEntitiesLoaded(long chunkKey) {
/* 427 */     return (this.chunkLoadStatuses.get(chunkKey) == ChunkLoadStatus.LOADED);
/*     */   }
/*     */   
/*     */   public void dumpSections(Writer output) throws IOException {
/* 431 */     CsvOutput csvOutput = CsvOutput.builder()
/* 432 */       .addColumn("x")
/* 433 */       .addColumn("y")
/* 434 */       .addColumn("z")
/* 435 */       .addColumn("visibility")
/* 436 */       .addColumn("load_status")
/* 437 */       .addColumn("entity_count")
/* 438 */       .build(output);
/*     */     
/* 440 */     this.sectionStorage.getAllChunksWithExistingSections().forEach(chunkKey -> {
/*     */           ChunkLoadStatus loadStatus = (ChunkLoadStatus)this.chunkLoadStatuses.get(csvOutput);
/*     */           this.sectionStorage.getExistingSectionPositionsInChunk(csvOutput).forEach(());
/*     */         });
/*     */   }
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
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/*     */   public String gatherStats() {
/* 466 */     return "" + this.knownUuids.size() + "," + this.knownUuids.size() + "," + 
/* 467 */       this.visibleEntityStorage.count() + "," + 
/* 468 */       this.sectionStorage.count() + "," + 
/* 469 */       this.chunkLoadStatuses.size() + "," + 
/* 470 */       this.chunkVisibility.size() + "," + 
/* 471 */       this.loadingInbox.size();
/*     */   }
/*     */ 
/*     */   
/*     */   @VisibleForDebug
/*     */   public int count() {
/* 477 */     return this.visibleEntityStorage.count();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/entity/PersistentEntitySectionManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */