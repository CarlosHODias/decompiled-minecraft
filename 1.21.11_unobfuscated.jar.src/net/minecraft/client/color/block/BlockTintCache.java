/*     */ package net.minecraft.client.color.block;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
/*     */ import java.util.Arrays;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.ToIntFunction;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ 
/*     */ public class BlockTintCache
/*     */ {
/*     */   private static final int MAX_CACHE_ENTRIES = 256;
/*     */   
/*     */   private static class CacheData {
/*  19 */     private final Int2ObjectArrayMap<int[]> cache = new Int2ObjectArrayMap(16);
/*  20 */     private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
/*  21 */     private static final int BLOCKS_PER_LAYER = Mth.square(16);
/*     */     
/*     */     private volatile boolean invalidated;
/*     */     
/*     */     public int[] getLayer(int y) {
/*  26 */       this.lock.readLock().lock();
/*     */       try {
/*  28 */         int[] existing = (int[])this.cache.get(y);
/*  29 */         if (existing != null) {
/*  30 */           return existing;
/*     */         }
/*     */       } finally {
/*  33 */         this.lock.readLock().unlock();
/*     */       } 
/*     */       
/*  36 */       this.lock.writeLock().lock();
/*     */       
/*     */       try {
/*  39 */         return (int[])this.cache.computeIfAbsent(y, n -> allocateLayer());
/*     */       } finally {
/*  41 */         this.lock.writeLock().unlock();
/*     */       } 
/*     */     }
/*     */     
/*     */     private int[] allocateLayer() {
/*  46 */       int[] newCache = new int[BLOCKS_PER_LAYER];
/*  47 */       Arrays.fill(newCache, -1);
/*  48 */       return newCache;
/*     */     }
/*     */     
/*     */     public boolean isInvalidated() {
/*  52 */       return this.invalidated;
/*     */     }
/*     */     
/*     */     public void invalidate() {
/*  56 */       this.invalidated = true;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LatestCacheInfo {
/*  61 */     public int x = Integer.MIN_VALUE;
/*  62 */     public int z = Integer.MIN_VALUE;
/*     */ 
/*     */     
/*     */     BlockTintCache.CacheData cache;
/*     */   }
/*     */ 
/*     */   
/*  69 */   private final ThreadLocal<LatestCacheInfo> latestChunkOnThread = ThreadLocal.withInitial(LatestCacheInfo::new);
/*     */ 
/*     */ 
/*     */   
/*  73 */   private final Long2ObjectLinkedOpenHashMap<CacheData> cache = new Long2ObjectLinkedOpenHashMap(256, 0.25F);
/*  74 */   private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
/*     */   private final ToIntFunction<BlockPos> source;
/*     */   
/*     */   public BlockTintCache(ToIntFunction<BlockPos> source) {
/*  78 */     this.source = source;
/*     */   }
/*     */   
/*     */   public int getColor(BlockPos pos) {
/*  82 */     int chunkX = SectionPos.blockToSectionCoord(pos.getX());
/*  83 */     int chunkZ = SectionPos.blockToSectionCoord(pos.getZ());
/*     */     
/*  85 */     LatestCacheInfo chunkInfo = this.latestChunkOnThread.get();
/*  86 */     if (chunkInfo.x != chunkX || chunkInfo.z != chunkZ || chunkInfo.cache == null || chunkInfo.cache.isInvalidated()) {
/*  87 */       chunkInfo.x = chunkX;
/*  88 */       chunkInfo.z = chunkZ;
/*  89 */       chunkInfo.cache = findOrCreateChunkCache(chunkX, chunkZ);
/*     */     } 
/*  91 */     int[] layer = chunkInfo.cache.getLayer(pos.getY());
/*     */     
/*  93 */     int x = pos.getX() & 0xF;
/*  94 */     int z = pos.getZ() & 0xF;
/*  95 */     int index = z << 4 | x;
/*  96 */     int cached = layer[index];
/*  97 */     if (cached != -1) {
/*  98 */       return cached;
/*     */     }
/* 100 */     int calculated = this.source.applyAsInt(pos);
/* 101 */     layer[index] = calculated;
/* 102 */     return calculated;
/*     */   }
/*     */   
/*     */   public void invalidateForChunk(int chunkX, int chunkZ) {
/*     */     try {
/* 107 */       this.lock.writeLock().lock();
/*     */       
/* 109 */       for (int offsetX = -1; offsetX <= 1; offsetX++) {
/* 110 */         for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
/* 111 */           long key = ChunkPos.asLong(chunkX + offsetX, chunkZ + offsetZ);
/* 112 */           CacheData removed = (CacheData)this.cache.remove(key);
/* 113 */           if (removed != null) {
/* 114 */             removed.invalidate();
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } finally {
/* 119 */       this.lock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void invalidateAll() {
/*     */     try {
/* 125 */       this.lock.writeLock().lock();
/* 126 */       this.cache.values().forEach(CacheData::invalidate);
/* 127 */       this.cache.clear();
/*     */     } finally {
/* 129 */       this.lock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   private CacheData findOrCreateChunkCache(int x, int z) {
/* 134 */     long key = ChunkPos.asLong(x, z);
/* 135 */     this.lock.readLock().lock();
/*     */     try {
/* 137 */       CacheData existing = (CacheData)this.cache.get(key);
/* 138 */       if (existing != null) {
/* 139 */         return existing;
/*     */       }
/*     */     } finally {
/* 142 */       this.lock.readLock().unlock();
/*     */     } 
/*     */     
/* 145 */     this.lock.writeLock().lock();
/*     */     
/*     */     try {
/* 148 */       CacheData existingNow = (CacheData)this.cache.get(key);
/* 149 */       if (existingNow != null) {
/* 150 */         return existingNow;
/*     */       }
/* 152 */       CacheData newCache = new CacheData();
/*     */       
/* 154 */       if (this.cache.size() >= 256) {
/* 155 */         CacheData cacheData = (CacheData)this.cache.removeFirst();
/* 156 */         if (cacheData != null) {
/* 157 */           cacheData.invalidate();
/*     */         }
/*     */       } 
/* 160 */       this.cache.put(key, newCache);
/* 161 */       return newCache;
/*     */     } finally {
/*     */       
/* 164 */       this.lock.writeLock().unlock();
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/color/block/BlockTintCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */