/*     */ package net.minecraft.server.level;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteMaps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntMaps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongConsumer;
/*     */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectSet;
/*     */ import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.TriState;
/*     */ import net.minecraft.util.thread.TaskScheduler;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.NaturalSpawner;
/*     */ import net.minecraft.world.level.TicketStorage;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public abstract class DistanceManager
/*     */ {
/*  36 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  38 */   private static final int PLAYER_TICKET_LEVEL = ChunkLevel.byStatus(FullChunkStatus.ENTITY_TICKING);
/*     */   
/*  40 */   private final Long2ObjectMap<ObjectSet<ServerPlayer>> playersPerChunk = (Long2ObjectMap<ObjectSet<ServerPlayer>>)new Long2ObjectOpenHashMap();
/*     */   
/*     */   private final LoadingChunkTracker loadingChunkTracker;
/*     */   
/*     */   private final SimulationChunkTracker simulationChunkTracker;
/*     */   
/*     */   private final TicketStorage ticketStorage;
/*  47 */   private final FixedPlayerDistanceChunkTracker naturalSpawnChunkCounter = new FixedPlayerDistanceChunkTracker(8);
/*  48 */   private final PlayerTicketTracker playerTicketManager = new PlayerTicketTracker(32);
/*     */   
/*  50 */   protected final Set<ChunkHolder> chunksToUpdateFutures = (Set<ChunkHolder>)new ReferenceOpenHashSet();
/*     */   private final ThrottlingChunkTaskDispatcher ticketDispatcher;
/*  52 */   private final LongSet ticketsToRelease = (LongSet)new LongOpenHashSet();
/*     */   private final Executor mainThreadExecutor;
/*  54 */   private int simulationDistance = 10;
/*     */   
/*     */   protected DistanceManager(TicketStorage ticketStorage, Executor executor, Executor mainThreadExecutor) {
/*  57 */     this.ticketStorage = ticketStorage;
/*  58 */     this.loadingChunkTracker = new LoadingChunkTracker(this, ticketStorage);
/*  59 */     this.simulationChunkTracker = new SimulationChunkTracker(ticketStorage);
/*     */     
/*  61 */     TaskScheduler<Runnable> mainThreadTaskScheduler = TaskScheduler.wrapExecutor("player ticket throttler", mainThreadExecutor);
/*     */     
/*  63 */     this.ticketDispatcher = new ThrottlingChunkTaskDispatcher(mainThreadTaskScheduler, executor, 4);
/*  64 */     this.mainThreadExecutor = mainThreadExecutor;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean runAllUpdates(ChunkMap scheduler) {
/*  74 */     this.naturalSpawnChunkCounter.runAllUpdates();
/*  75 */     this.simulationChunkTracker.runAllUpdates();
/*  76 */     this.playerTicketManager.runAllUpdates();
/*     */     
/*  78 */     int updates = Integer.MAX_VALUE - this.loadingChunkTracker.runDistanceUpdates(Integer.MAX_VALUE);
/*  79 */     boolean updated = (updates != 0);
/*  80 */     if (updated && SharedConstants.DEBUG_VERBOSE_SERVER_EVENTS) {
/*  81 */       LOGGER.debug("DMU {}", updates);
/*     */     }
/*  83 */     if (!this.chunksToUpdateFutures.isEmpty()) {
/*     */       
/*  85 */       for (ChunkHolder chunksToUpdateFuture : this.chunksToUpdateFutures) {
/*  86 */         chunksToUpdateFuture.updateHighestAllowedStatus(scheduler);
/*     */       }
/*  88 */       for (ChunkHolder chunkHolder : this.chunksToUpdateFutures) {
/*  89 */         chunkHolder.updateFutures(scheduler, this.mainThreadExecutor);
/*     */       }
/*  91 */       this.chunksToUpdateFutures.clear();
/*  92 */       return true;
/*     */     } 
/*  94 */     if (!this.ticketsToRelease.isEmpty()) {
/*  95 */       LongIterator iterator = this.ticketsToRelease.iterator();
/*  96 */       while (iterator.hasNext()) {
/*  97 */         long pos = iterator.nextLong();
/*  98 */         if (this.ticketStorage.getTickets(pos).stream().anyMatch(t -> (t.getType() == TicketType.PLAYER_LOADING))) {
/*  99 */           ChunkHolder chunk = scheduler.getUpdatingChunkIfPresent(pos);
/* 100 */           if (chunk == null) {
/* 101 */             throw new IllegalStateException();
/*     */           }
/* 103 */           CompletableFuture<ChunkResult<LevelChunk>> future = chunk.getEntityTickingChunkFuture();
/* 104 */           future.thenAccept(c -> this.mainThreadExecutor.execute(()));
/*     */         } 
/*     */       } 
/* 107 */       this.ticketsToRelease.clear();
/*     */     } 
/* 109 */     return updated;
/*     */   }
/*     */   
/*     */   public void addPlayer(SectionPos pos, ServerPlayer player) {
/* 113 */     ChunkPos chunk = pos.chunk();
/* 114 */     long chunkPos = chunk.toLong();
/* 115 */     ((ObjectSet)this.playersPerChunk.computeIfAbsent(chunkPos, k -> new ObjectOpenHashSet())).add(player);
/* 116 */     this.naturalSpawnChunkCounter.update(chunkPos, 0, true);
/* 117 */     this.playerTicketManager.update(chunkPos, 0, true);
/* 118 */     this.ticketStorage.addTicket(new Ticket(TicketType.PLAYER_SIMULATION, getPlayerTicketLevel()), chunk);
/*     */   }
/*     */   
/*     */   public void removePlayer(SectionPos pos, ServerPlayer player) {
/* 122 */     ChunkPos chunk = pos.chunk();
/* 123 */     long chunkPos = chunk.toLong();
/* 124 */     ObjectSet<ServerPlayer> chunkPlayers = (ObjectSet<ServerPlayer>)this.playersPerChunk.get(chunkPos);
/* 125 */     chunkPlayers.remove(player);
/* 126 */     if (chunkPlayers.isEmpty()) {
/* 127 */       this.playersPerChunk.remove(chunkPos);
/* 128 */       this.naturalSpawnChunkCounter.update(chunkPos, Integer.MAX_VALUE, false);
/* 129 */       this.playerTicketManager.update(chunkPos, Integer.MAX_VALUE, false);
/* 130 */       this.ticketStorage.removeTicket(new Ticket(TicketType.PLAYER_SIMULATION, getPlayerTicketLevel()), chunk);
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getPlayerTicketLevel() {
/* 135 */     return Math.max(0, ChunkLevel.byStatus(FullChunkStatus.ENTITY_TICKING) - this.simulationDistance);
/*     */   }
/*     */   
/*     */   public boolean inEntityTickingRange(long key) {
/* 139 */     return ChunkLevel.isEntityTicking(this.simulationChunkTracker.getLevel(key));
/*     */   }
/*     */   
/*     */   public boolean inBlockTickingRange(long key) {
/* 143 */     return ChunkLevel.isBlockTicking(this.simulationChunkTracker.getLevel(key));
/*     */   }
/*     */   
/*     */   public int getChunkLevel(long key, boolean simulation) {
/* 147 */     if (simulation) {
/* 148 */       return this.simulationChunkTracker.getLevel(key);
/*     */     }
/* 150 */     return this.loadingChunkTracker.getLevel(key);
/*     */   }
/*     */   
/*     */   protected void updatePlayerTickets(int viewDistance) {
/* 154 */     this.playerTicketManager.updateViewDistance(viewDistance);
/*     */   }
/*     */   
/*     */   public void updateSimulationDistance(int newDistance) {
/* 158 */     if (newDistance != this.simulationDistance) {
/* 159 */       this.simulationDistance = newDistance;
/* 160 */       this.ticketStorage.replaceTicketLevelOfType(getPlayerTicketLevel(), TicketType.PLAYER_SIMULATION);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int getNaturalSpawnChunkCount() {
/* 165 */     this.naturalSpawnChunkCounter.runAllUpdates();
/* 166 */     return this.naturalSpawnChunkCounter.chunks.size();
/*     */   }
/*     */   
/*     */   public TriState hasPlayersNearby(long pos) {
/* 170 */     this.naturalSpawnChunkCounter.runAllUpdates();
/* 171 */     int distance = this.naturalSpawnChunkCounter.getLevel(pos);
/* 172 */     if (distance <= NaturalSpawner.INSCRIBED_SQUARE_SPAWN_DISTANCE_CHUNK)
/* 173 */       return TriState.TRUE; 
/* 174 */     if (distance > 8) {
/* 175 */       return TriState.FALSE;
/*     */     }
/* 177 */     return TriState.DEFAULT;
/*     */   }
/*     */   
/*     */   public void forEachEntityTickingChunk(LongConsumer consumer) {
/* 181 */     for (ObjectIterator<Long2ByteMap.Entry> objectIterator = Long2ByteMaps.fastIterable(this.simulationChunkTracker.chunks).iterator(); objectIterator.hasNext(); ) { Long2ByteMap.Entry entry = objectIterator.next();
/* 182 */       byte level = entry.getByteValue();
/* 183 */       long key = entry.getLongKey();
/* 184 */       if (ChunkLevel.isEntityTicking(level)) {
/* 185 */         consumer.accept(key);
/*     */       } }
/*     */   
/*     */   }
/*     */   
/*     */   public LongIterator getSpawnCandidateChunks() {
/* 191 */     this.naturalSpawnChunkCounter.runAllUpdates();
/* 192 */     return this.naturalSpawnChunkCounter.chunks.keySet().iterator();
/*     */   }
/*     */   
/*     */   public String getDebugStatus() {
/* 196 */     return this.ticketDispatcher.getDebugStatus();
/*     */   } protected abstract boolean isChunkToRemove(long paramLong);
/*     */   protected abstract ChunkHolder getChunk(long paramLong);
/*     */   public boolean hasTickets() {
/* 200 */     return this.ticketStorage.hasTickets();
/*     */   }
/*     */   protected abstract ChunkHolder updateChunkScheduling(long paramLong, int paramInt1, ChunkHolder paramChunkHolder, int paramInt2);
/*     */   
/* 204 */   private class FixedPlayerDistanceChunkTracker extends ChunkTracker { protected final Long2ByteMap chunks = (Long2ByteMap)new Long2ByteOpenHashMap();
/*     */     protected final int maxDistance;
/*     */     
/*     */     protected FixedPlayerDistanceChunkTracker(int maxDistance) {
/* 208 */       super(maxDistance + 2, 16, 256);
/* 209 */       this.maxDistance = maxDistance;
/* 210 */       this.chunks.defaultReturnValue((byte)(maxDistance + 2));
/*     */     }
/*     */ 
/*     */     
/*     */     protected int getLevel(long node) {
/* 215 */       return this.chunks.get(node);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void setLevel(long node, int level) {
/*     */       byte oldLevel;
/* 221 */       if (level > this.maxDistance) {
/* 222 */         oldLevel = this.chunks.remove(node);
/*     */       } else {
/* 224 */         oldLevel = this.chunks.put(node, (byte)level);
/*     */       } 
/* 226 */       onLevelChange(node, oldLevel, level);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void onLevelChange(long node, int oldLevel, int level) {}
/*     */ 
/*     */     
/*     */     protected int getLevelFromSource(long to) {
/* 234 */       return havePlayer(to) ? 0 : Integer.MAX_VALUE;
/*     */     }
/*     */     
/*     */     private boolean havePlayer(long chunkPos) {
/* 238 */       ObjectSet<ServerPlayer> players = (ObjectSet<ServerPlayer>)DistanceManager.this.playersPerChunk.get(chunkPos);
/* 239 */       return (players != null && !players.isEmpty());
/*     */     }
/*     */     
/*     */     public void runAllUpdates() {
/* 243 */       runUpdates(Integer.MAX_VALUE);
/*     */     } }
/*     */ 
/*     */   
/*     */   private class PlayerTicketTracker
/*     */     extends FixedPlayerDistanceChunkTracker {
/*     */     private int viewDistance;
/* 250 */     private final Long2IntMap queueLevels = Long2IntMaps.synchronize((Long2IntMap)new Long2IntOpenHashMap());
/* 251 */     private final LongSet toUpdate = (LongSet)new LongOpenHashSet();
/*     */     
/*     */     protected PlayerTicketTracker(int maxDistance) {
/* 254 */       super(maxDistance);
/* 255 */       this.viewDistance = 0;
/* 256 */       this.queueLevels.defaultReturnValue(maxDistance + 2);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void onLevelChange(long node, int oldLevel, int level) {
/* 261 */       this.toUpdate.add(node);
/*     */     }
/*     */     
/*     */     public void updateViewDistance(int viewDistance) {
/* 265 */       for (ObjectIterator<Long2ByteMap.Entry> objectIterator = this.chunks.long2ByteEntrySet().iterator(); objectIterator.hasNext(); ) { Long2ByteMap.Entry entry = objectIterator.next();
/* 266 */         byte level = entry.getByteValue();
/* 267 */         long key = entry.getLongKey();
/* 268 */         onLevelChange(key, level, haveTicketFor(level), (level <= viewDistance)); }
/*     */       
/* 270 */       this.viewDistance = viewDistance;
/*     */     }
/*     */     
/*     */     private void onLevelChange(long key, int level, boolean saw, boolean sees) {
/* 274 */       if (saw != sees) {
/* 275 */         Ticket ticket = new Ticket(TicketType.PLAYER_LOADING, DistanceManager.PLAYER_TICKET_LEVEL);
/* 276 */         if (sees) {
/* 277 */           DistanceManager.this.ticketDispatcher.submit(() -> DistanceManager.this.mainThreadExecutor.execute(()), key, () -> level);
/*     */ 
/*     */ 
/*     */         
/*     */         }
/*     */         else {
/*     */ 
/*     */ 
/*     */           
/* 286 */           DistanceManager.this.ticketDispatcher.release(key, () -> DistanceManager.this.mainThreadExecutor.execute(()), true);
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void runAllUpdates() {
/* 293 */       super.runAllUpdates();
/* 294 */       if (!this.toUpdate.isEmpty()) {
/* 295 */         LongIterator iterator = this.toUpdate.iterator();
/* 296 */         while (iterator.hasNext()) {
/* 297 */           long node = iterator.nextLong();
/* 298 */           int oldLevel = this.queueLevels.get(node);
/* 299 */           int level = getLevel(node);
/* 300 */           if (oldLevel != level) {
/* 301 */             DistanceManager.this.ticketDispatcher.onLevelChange(new ChunkPos(node), () -> this.queueLevels.get(node), level, l -> {
/*     */                   if (node >= this.queueLevels.defaultReturnValue()) {
/*     */                     this.queueLevels.remove(node);
/*     */                   } else {
/*     */                     this.queueLevels.put(node, node);
/*     */                   } 
/*     */                 });
/* 308 */             onLevelChange(node, level, haveTicketFor(oldLevel), haveTicketFor(level));
/*     */           } 
/*     */         } 
/* 311 */         this.toUpdate.clear();
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean haveTicketFor(int level) {
/* 316 */       return (level <= this.viewDistance);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/level/DistanceManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */