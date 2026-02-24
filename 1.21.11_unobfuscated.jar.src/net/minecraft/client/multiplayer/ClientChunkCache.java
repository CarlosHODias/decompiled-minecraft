/*     */ package net.minecraft.client.multiplayer;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicReferenceArray;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.protocol.game.ClientboundLevelChunkPacketData;
/*     */ import net.minecraft.world.level.BlockGetter;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.biome.Biomes;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.chunk.ChunkSource;
/*     */ import net.minecraft.world.level.chunk.EmptyLevelChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunk;
/*     */ import net.minecraft.world.level.chunk.LevelChunkSection;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ import net.minecraft.world.level.levelgen.Heightmap;
/*     */ import net.minecraft.world.level.lighting.LevelLightEngine;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ClientChunkCache extends ChunkSource {
/*  33 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final LevelChunk emptyChunk;
/*     */   private final LevelLightEngine lightEngine;
/*     */   private volatile Storage storage;
/*     */   private final ClientLevel level;
/*     */   
/*     */   public ClientChunkCache(ClientLevel level, int serverChunkRadius) {
/*  41 */     this.level = level;
/*  42 */     this.emptyChunk = (LevelChunk)new EmptyLevelChunk(level, new ChunkPos(0, 0), (Holder)level.registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.PLAINS));
/*  43 */     this.lightEngine = new LevelLightEngine((LightChunkGetter)this, true, level.dimensionType().hasSkyLight());
/*  44 */     this.storage = new Storage(calculateStorageRange(serverChunkRadius));
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelLightEngine getLightEngine() {
/*  49 */     return this.lightEngine;
/*     */   }
/*     */   
/*     */   private static boolean isValidChunk(LevelChunk chunk, int x, int z) {
/*  53 */     if (chunk == null) {
/*  54 */       return false;
/*     */     }
/*  56 */     ChunkPos pos = chunk.getPos();
/*  57 */     return (pos.x == x && pos.z == z);
/*     */   }
/*     */   
/*     */   public void drop(ChunkPos pos) {
/*  61 */     if (!this.storage.inRange(pos.x, pos.z)) {
/*     */       return;
/*     */     }
/*  64 */     int index = this.storage.getIndex(pos.x, pos.z);
/*  65 */     LevelChunk currentChunk = this.storage.getChunk(index);
/*  66 */     if (isValidChunk(currentChunk, pos.x, pos.z)) {
/*  67 */       this.storage.drop(index, currentChunk);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public LevelChunk getChunk(int x, int z, ChunkStatus targetStatus, boolean loadOrGenerate) {
/*  73 */     if (this.storage.inRange(x, z)) {
/*  74 */       LevelChunk chunk = this.storage.getChunk(this.storage.getIndex(x, z));
/*  75 */       if (isValidChunk(chunk, x, z)) {
/*  76 */         return chunk;
/*     */       }
/*     */     } 
/*     */     
/*  80 */     if (loadOrGenerate) {
/*  81 */       return this.emptyChunk;
/*     */     }
/*  83 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public BlockGetter getLevel() {
/*  88 */     return (BlockGetter)this.level;
/*     */   }
/*     */   
/*     */   public void replaceBiomes(int chunkX, int chunkZ, FriendlyByteBuf readBuffer) {
/*  92 */     if (!this.storage.inRange(chunkX, chunkZ)) {
/*  93 */       LOGGER.warn("Ignoring chunk since it's not in the view range: {}, {}", chunkX, chunkZ);
/*     */       return;
/*     */     } 
/*  96 */     int index = this.storage.getIndex(chunkX, chunkZ);
/*     */     
/*  98 */     LevelChunk chunk = this.storage.chunks.get(index);
/*  99 */     if (!isValidChunk(chunk, chunkX, chunkZ)) {
/* 100 */       LOGGER.warn("Ignoring chunk since it's not present: {}, {}", chunkX, chunkZ);
/*     */     } else {
/* 102 */       chunk.replaceBiomes(readBuffer);
/*     */     } 
/*     */   }
/*     */   
/*     */   public LevelChunk replaceWithPacketData(int chunkX, int chunkZ, FriendlyByteBuf readBuffer, Map<Heightmap.Types, long[]> heightmaps, Consumer<ClientboundLevelChunkPacketData.BlockEntityTagOutput> blockEntities) {
/* 107 */     if (!this.storage.inRange(chunkX, chunkZ)) {
/* 108 */       LOGGER.warn("Ignoring chunk since it's not in the view range: {}, {}", chunkX, chunkZ);
/* 109 */       return null;
/*     */     } 
/* 111 */     int index = this.storage.getIndex(chunkX, chunkZ);
/*     */     
/* 113 */     LevelChunk chunk = this.storage.chunks.get(index);
/* 114 */     ChunkPos pos = new ChunkPos(chunkX, chunkZ);
/* 115 */     if (!isValidChunk(chunk, chunkX, chunkZ)) {
/*     */       
/* 117 */       chunk = new LevelChunk(this.level, pos);
/* 118 */       chunk.replaceWithPacketData(readBuffer, heightmaps, blockEntities);
/* 119 */       this.storage.replace(index, chunk);
/*     */     } else {
/* 121 */       chunk.replaceWithPacketData(readBuffer, heightmaps, blockEntities);
/* 122 */       this.storage.refreshEmptySections(chunk);
/*     */     } 
/* 124 */     this.level.onChunkLoaded(pos);
/* 125 */     return chunk;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick(BooleanSupplier haveTime, boolean tickChunks) {}
/*     */ 
/*     */   
/*     */   public void updateViewCenter(int x, int z) {
/* 133 */     this.storage.viewCenterX = x;
/* 134 */     this.storage.viewCenterZ = z;
/*     */   }
/*     */   
/*     */   public void updateViewRadius(int viewRange) {
/* 138 */     int chunkRadius = this.storage.chunkRadius;
/* 139 */     int newChunkRadius = calculateStorageRange(viewRange);
/*     */     
/* 141 */     if (chunkRadius != newChunkRadius) {
/* 142 */       Storage newStorage = new Storage(newChunkRadius);
/* 143 */       newStorage.viewCenterX = this.storage.viewCenterX;
/* 144 */       newStorage.viewCenterZ = this.storage.viewCenterZ;
/* 145 */       for (int i = 0; i < this.storage.chunks.length(); i++) {
/* 146 */         LevelChunk chunk = this.storage.chunks.get(i);
/* 147 */         if (chunk != null) {
/* 148 */           ChunkPos pos = chunk.getPos();
/* 149 */           if (newStorage.inRange(pos.x, pos.z)) {
/* 150 */             newStorage.replace(newStorage.getIndex(pos.x, pos.z), chunk);
/*     */           }
/*     */         } 
/*     */       } 
/* 154 */       this.storage = newStorage;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static int calculateStorageRange(int viewRange) {
/* 160 */     return Math.max(2, viewRange) + 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public String gatherStats() {
/* 165 */     return "" + this.storage.chunks.length() + ", " + this.storage.chunks.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLoadedChunksCount() {
/* 170 */     return this.storage.chunkCount;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onLightUpdate(LightLayer layer, SectionPos pos) {
/* 175 */     (Minecraft.getInstance()).levelRenderer.setSectionDirty(pos.x(), pos.y(), pos.z());
/*     */   }
/*     */   
/*     */   public LongOpenHashSet getLoadedEmptySections() {
/* 179 */     return this.storage.loadedEmptySections;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSectionEmptinessChanged(int sectionX, int sectionY, int sectionZ, boolean empty) {
/* 184 */     this.storage.onSectionEmptinessChanged(sectionX, sectionY, sectionZ, empty);
/*     */   }
/*     */   
/*     */   private final class Storage {
/*     */     private final AtomicReferenceArray<LevelChunk> chunks;
/* 189 */     private final LongOpenHashSet loadedEmptySections = new LongOpenHashSet();
/*     */     private final int chunkRadius;
/*     */     private final int viewRange;
/*     */     private volatile int viewCenterX;
/*     */     private volatile int viewCenterZ;
/*     */     private int chunkCount;
/*     */     
/*     */     private Storage(int chunkRadius) {
/* 197 */       this.chunkRadius = chunkRadius;
/* 198 */       this.viewRange = chunkRadius * 2 + 1;
/* 199 */       this.chunks = new AtomicReferenceArray<>(this.viewRange * this.viewRange);
/*     */     }
/*     */     
/*     */     private int getIndex(int chunkX, int chunkZ) {
/* 203 */       return Math.floorMod(chunkZ, this.viewRange) * this.viewRange + Math.floorMod(chunkX, this.viewRange);
/*     */     }
/*     */     
/*     */     private void replace(int index, LevelChunk newChunk) {
/* 207 */       LevelChunk removedChunk = this.chunks.getAndSet(index, newChunk);
/* 208 */       if (removedChunk != null) {
/* 209 */         this.chunkCount--;
/* 210 */         dropEmptySections(removedChunk);
/* 211 */         ClientChunkCache.this.level.unload(removedChunk);
/*     */       } 
/*     */       
/* 214 */       if (newChunk != null) {
/* 215 */         this.chunkCount++;
/* 216 */         addEmptySections(newChunk);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void drop(int index, LevelChunk oldChunk) {
/* 221 */       if (this.chunks.compareAndSet(index, oldChunk, null)) {
/* 222 */         this.chunkCount--;
/* 223 */         dropEmptySections(oldChunk);
/*     */       } 
/* 225 */       ClientChunkCache.this.level.unload(oldChunk);
/*     */     }
/*     */     
/*     */     public void onSectionEmptinessChanged(int sectionX, int sectionY, int sectionZ, boolean empty) {
/* 229 */       if (!inRange(sectionX, sectionZ)) {
/*     */         return;
/*     */       }
/* 232 */       long sectionNode = SectionPos.asLong(sectionX, sectionY, sectionZ);
/* 233 */       if (empty) {
/* 234 */         this.loadedEmptySections.add(sectionNode);
/* 235 */       } else if (this.loadedEmptySections.remove(sectionNode)) {
/* 236 */         ClientChunkCache.this.level.onSectionBecomingNonEmpty(sectionNode);
/*     */       } 
/*     */     }
/*     */     
/*     */     private void dropEmptySections(LevelChunk chunk) {
/* 241 */       LevelChunkSection[] sections = chunk.getSections();
/* 242 */       for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
/* 243 */         ChunkPos chunkPos = chunk.getPos();
/* 244 */         this.loadedEmptySections.remove(SectionPos.asLong(chunkPos.x, chunk.getSectionYFromSectionIndex(sectionIndex), chunkPos.z));
/*     */       } 
/*     */     }
/*     */     
/*     */     private void addEmptySections(LevelChunk chunk) {
/* 249 */       LevelChunkSection[] sections = chunk.getSections();
/* 250 */       for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
/* 251 */         LevelChunkSection section = sections[sectionIndex];
/* 252 */         if (section.hasOnlyAir()) {
/* 253 */           ChunkPos chunkPos = chunk.getPos();
/* 254 */           this.loadedEmptySections.add(SectionPos.asLong(chunkPos.x, chunk.getSectionYFromSectionIndex(sectionIndex), chunkPos.z));
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private void refreshEmptySections(LevelChunk chunk) {
/* 260 */       ChunkPos chunkPos = chunk.getPos();
/* 261 */       LevelChunkSection[] sections = chunk.getSections();
/* 262 */       for (int sectionIndex = 0; sectionIndex < sections.length; sectionIndex++) {
/* 263 */         LevelChunkSection section = sections[sectionIndex];
/* 264 */         long sectionNode = SectionPos.asLong(chunkPos.x, chunk.getSectionYFromSectionIndex(sectionIndex), chunkPos.z);
/* 265 */         if (section.hasOnlyAir()) {
/* 266 */           this.loadedEmptySections.add(sectionNode);
/* 267 */         } else if (this.loadedEmptySections.remove(sectionNode)) {
/* 268 */           ClientChunkCache.this.level.onSectionBecomingNonEmpty(sectionNode);
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/*     */     private boolean inRange(int chunkX, int chunkZ) {
/* 274 */       return (Math.abs(chunkX - this.viewCenterX) <= this.chunkRadius && Math.abs(chunkZ - this.viewCenterZ) <= this.chunkRadius);
/*     */     }
/*     */     
/*     */     protected LevelChunk getChunk(int index) {
/* 278 */       return this.chunks.get(index);
/*     */     }
/*     */     
/*     */     private void dumpChunks(String file) {
/*     */       
/* 283 */       try { FileOutputStream stream = new FileOutputStream(file); 
/* 284 */         try { int chunkRadius = ClientChunkCache.this.storage.chunkRadius;
/* 285 */           for (int z = this.viewCenterZ - chunkRadius; z <= this.viewCenterZ + chunkRadius; z++) {
/* 286 */             for (int x = this.viewCenterX - chunkRadius; x <= this.viewCenterX + chunkRadius; x++) {
/* 287 */               LevelChunk chunk = ClientChunkCache.this.storage.chunks.get(ClientChunkCache.this.storage.getIndex(x, z));
/* 288 */               if (chunk != null) {
/* 289 */                 ChunkPos pos = chunk.getPos();
/* 290 */                 stream.write(("" + pos.x + "\t" + pos.x + "\t" + pos.z + "\n").getBytes(StandardCharsets.UTF_8));
/*     */               } 
/*     */             } 
/*     */           } 
/* 294 */           stream.close(); } catch (Throwable throwable) { try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (IOException e)
/* 295 */       { ClientChunkCache.LOGGER.error("Failed to dump chunks to file {}", file, e); }
/*     */     
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/ClientChunkCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */