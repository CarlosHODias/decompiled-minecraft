/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMaps;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.longs.LongSet;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.LightLayer;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LayerLightSectionStorage<M extends DataLayerStorageMap<M>>
/*     */ {
/*     */   private final LightLayer layer;
/*     */   protected final LightChunkGetter chunkSource;
/*  26 */   protected final Long2ByteMap sectionStates = (Long2ByteMap)new Long2ByteOpenHashMap();
/*     */   
/*  28 */   private final LongSet columnsWithSources = (LongSet)new LongOpenHashSet();
/*     */   
/*     */   protected volatile M visibleSectionData;
/*     */   
/*     */   protected final M updatingSectionData;
/*     */   
/*  34 */   protected final LongSet changedSections = (LongSet)new LongOpenHashSet();
/*  35 */   protected final LongSet sectionsAffectedByLightUpdates = (LongSet)new LongOpenHashSet();
/*     */ 
/*     */   
/*  38 */   protected final Long2ObjectMap<DataLayer> queuedSections = Long2ObjectMaps.synchronize((Long2ObjectMap)new Long2ObjectOpenHashMap());
/*     */   
/*  40 */   private final LongSet columnsToRetainQueuedDataFor = (LongSet)new LongOpenHashSet();
/*     */   
/*  42 */   private final LongSet toRemove = (LongSet)new LongOpenHashSet();
/*     */   
/*     */   protected volatile boolean hasInconsistencies;
/*     */   
/*     */   protected LayerLightSectionStorage(LightLayer layer, LightChunkGetter chunkSource, M initialMap) {
/*  47 */     this.layer = layer;
/*  48 */     this.chunkSource = chunkSource;
/*  49 */     this.updatingSectionData = initialMap;
/*  50 */     this.visibleSectionData = initialMap.copy();
/*  51 */     this.visibleSectionData.disableCache();
/*  52 */     this.sectionStates.defaultReturnValue((byte)0);
/*     */   }
/*     */   
/*     */   protected boolean storingLightForSection(long sectionNode) {
/*  56 */     return (getDataLayer(sectionNode, true) != null);
/*     */   }
/*     */   
/*     */   protected DataLayer getDataLayer(long sectionNode, boolean updating) {
/*  60 */     return getDataLayer(updating ? this.updatingSectionData : this.visibleSectionData, sectionNode);
/*     */   }
/*     */   
/*     */   protected DataLayer getDataLayer(M sections, long sectionNode) {
/*  64 */     return sections.getLayer(sectionNode);
/*     */   }
/*     */   
/*     */   protected DataLayer getDataLayerToWrite(long sectionNode) {
/*  68 */     DataLayer dataLayer = this.updatingSectionData.getLayer(sectionNode);
/*  69 */     if (dataLayer == null) {
/*  70 */       return null;
/*     */     }
/*  72 */     if (this.changedSections.add(sectionNode)) {
/*  73 */       dataLayer = dataLayer.copy();
/*  74 */       this.updatingSectionData.setLayer(sectionNode, dataLayer);
/*  75 */       this.updatingSectionData.clearCache();
/*     */     } 
/*  77 */     return dataLayer;
/*     */   }
/*     */   
/*     */   public DataLayer getDataLayerData(long sectionNode) {
/*  81 */     DataLayer layer = (DataLayer)this.queuedSections.get(sectionNode);
/*  82 */     if (layer != null) {
/*  83 */       return layer;
/*     */     }
/*  85 */     return getDataLayer(sectionNode, false);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected abstract int getLightValue(long paramLong);
/*     */ 
/*     */ 
/*     */   
/*     */   protected int getStoredLevel(long blockNode) {
/*  95 */     long sectionNode = SectionPos.blockToSection(blockNode);
/*  96 */     DataLayer layer = getDataLayer(sectionNode, true);
/*  97 */     return layer.get(
/*  98 */         SectionPos.sectionRelative(BlockPos.getX(blockNode)), 
/*  99 */         SectionPos.sectionRelative(BlockPos.getY(blockNode)), 
/* 100 */         SectionPos.sectionRelative(BlockPos.getZ(blockNode)));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setStoredLevel(long blockNode, int level) {
/*     */     DataLayer layer;
/* 107 */     long sectionNode = SectionPos.blockToSection(blockNode);
/*     */     
/* 109 */     if (this.changedSections.add(sectionNode)) {
/* 110 */       layer = this.updatingSectionData.copyDataLayer(sectionNode);
/*     */     } else {
/* 112 */       layer = getDataLayer(sectionNode, true);
/*     */     } 
/* 114 */     layer.set(
/* 115 */         SectionPos.sectionRelative(BlockPos.getX(blockNode)), 
/* 116 */         SectionPos.sectionRelative(BlockPos.getY(blockNode)), 
/* 117 */         SectionPos.sectionRelative(BlockPos.getZ(blockNode)), level);
/*     */ 
/*     */     
/* 120 */     Objects.requireNonNull(this.sectionsAffectedByLightUpdates); SectionPos.aroundAndAtBlockPos(blockNode, this.sectionsAffectedByLightUpdates::add);
/*     */   }
/*     */   
/*     */   protected void markSectionAndNeighborsAsAffected(long sectionNode) {
/* 124 */     int x = SectionPos.x(sectionNode);
/* 125 */     int y = SectionPos.y(sectionNode);
/* 126 */     int z = SectionPos.z(sectionNode);
/*     */     
/* 128 */     for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
/* 129 */       for (int offsetX = -1; offsetX <= 1; offsetX++) {
/* 130 */         for (int offsetY = -1; offsetY <= 1; offsetY++) {
/* 131 */           this.sectionsAffectedByLightUpdates.add(SectionPos.asLong(x + offsetX, y + offsetY, z + offsetZ));
/*     */         }
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected DataLayer createDataLayer(long sectionNode) {
/* 138 */     DataLayer queuedLayer = (DataLayer)this.queuedSections.get(sectionNode);
/* 139 */     if (queuedLayer != null) {
/* 140 */       return queuedLayer;
/*     */     }
/* 142 */     return new DataLayer();
/*     */   }
/*     */   
/*     */   protected boolean hasInconsistencies() {
/* 146 */     return this.hasInconsistencies;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void markNewInconsistencies(LightEngine<M, ?> engine) {
/* 151 */     if (!this.hasInconsistencies) {
/*     */       return;
/*     */     }
/* 154 */     this.hasInconsistencies = false;
/*     */     
/* 156 */     for (LongIterator<Long> longIterator2 = this.toRemove.iterator(); longIterator2.hasNext(); ) { long node = (Long)longIterator2.next();
/* 157 */       DataLayer queued = (DataLayer)this.queuedSections.remove(node);
/* 158 */       DataLayer stored = this.updatingSectionData.removeLayer(node);
/* 159 */       if (this.columnsToRetainQueuedDataFor.contains(SectionPos.getZeroNode(node))) {
/* 160 */         if (queued != null) {
/* 161 */           this.queuedSections.put(node, queued); continue;
/* 162 */         }  if (stored != null) {
/* 163 */           this.queuedSections.put(node, stored);
/*     */         }
/*     */       }  }
/*     */     
/* 167 */     this.updatingSectionData.clearCache();
/* 168 */     for (LongIterator<Long> longIterator1 = this.toRemove.iterator(); longIterator1.hasNext(); ) { long node = (Long)longIterator1.next();
/* 169 */       onNodeRemoved(node);
/* 170 */       this.changedSections.add(node); }
/*     */     
/* 172 */     this.toRemove.clear();
/*     */     
/* 174 */     ObjectIterator<Long2ObjectMap.Entry<DataLayer>> iterator = Long2ObjectMaps.fastIterator(this.queuedSections);
/* 175 */     while (iterator.hasNext()) {
/* 176 */       Long2ObjectMap.Entry<DataLayer> entry = (Long2ObjectMap.Entry<DataLayer>)iterator.next();
/* 177 */       long sectionNode = entry.getLongKey();
/* 178 */       if (!storingLightForSection(sectionNode)) {
/*     */         continue;
/*     */       }
/* 181 */       DataLayer data = (DataLayer)entry.getValue();
/*     */       
/* 183 */       if (this.updatingSectionData.getLayer(sectionNode) != data) {
/*     */         
/* 185 */         this.updatingSectionData.setLayer(sectionNode, data);
/* 186 */         this.changedSections.add(sectionNode);
/*     */       } 
/* 188 */       iterator.remove();
/*     */     } 
/* 190 */     this.updatingSectionData.clearCache();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void onNodeAdded(long sectionNode) {}
/*     */ 
/*     */   
/*     */   protected void onNodeRemoved(long sectionNode) {}
/*     */   
/*     */   protected void setLightEnabled(long zeroNode, boolean enable) {
/* 200 */     if (enable) {
/* 201 */       this.columnsWithSources.add(zeroNode);
/*     */     } else {
/* 203 */       this.columnsWithSources.remove(zeroNode);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected boolean lightOnInSection(long sectionNode) {
/* 208 */     long zeroNode = SectionPos.getZeroNode(sectionNode);
/* 209 */     return this.columnsWithSources.contains(zeroNode);
/*     */   }
/*     */   
/*     */   protected boolean lightOnInColumn(long sectionZeroNode) {
/* 213 */     return this.columnsWithSources.contains(sectionZeroNode);
/*     */   }
/*     */   
/*     */   public void retainData(long zeroNode, boolean retain) {
/* 217 */     if (retain) {
/* 218 */       this.columnsToRetainQueuedDataFor.add(zeroNode);
/*     */     } else {
/* 220 */       this.columnsToRetainQueuedDataFor.remove(zeroNode);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void queueSectionData(long sectionNode, DataLayer data) {
/* 225 */     if (data != null) {
/* 226 */       this.queuedSections.put(sectionNode, data);
/* 227 */       this.hasInconsistencies = true;
/*     */     } else {
/* 229 */       this.queuedSections.remove(sectionNode);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void updateSectionStatus(long sectionNode, boolean sectionEmpty) {
/* 234 */     byte state = this.sectionStates.get(sectionNode);
/* 235 */     byte newState = SectionState.hasData(state, !sectionEmpty);
/* 236 */     if (state == newState) {
/*     */       return;
/*     */     }
/*     */     
/* 240 */     putSectionState(sectionNode, newState);
/*     */     
/* 242 */     int neighborIncrement = sectionEmpty ? -1 : 1;
/* 243 */     for (int offsetX = -1; offsetX <= 1; offsetX++) {
/* 244 */       for (int offsetY = -1; offsetY <= 1; offsetY++) {
/* 245 */         for (int offsetZ = -1; offsetZ <= 1; offsetZ++) {
/* 246 */           if (offsetX != 0 || offsetY != 0 || offsetZ != 0) {
/*     */ 
/*     */             
/* 249 */             long neighborNode = SectionPos.offset(sectionNode, offsetX, offsetY, offsetZ);
/* 250 */             byte neighborState = this.sectionStates.get(neighborNode);
/* 251 */             putSectionState(neighborNode, SectionState.neighborCount(neighborState, SectionState.neighborCount(neighborState) + neighborIncrement));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   protected void putSectionState(long sectionNode, byte state) {
/* 258 */     if (state != 0) {
/* 259 */       if (this.sectionStates.put(sectionNode, state) == 0) {
/* 260 */         initializeSection(sectionNode);
/*     */       }
/*     */     }
/* 263 */     else if (this.sectionStates.remove(sectionNode) != 0) {
/* 264 */       removeSection(sectionNode);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void initializeSection(long sectionNode) {
/* 270 */     if (!this.toRemove.remove(sectionNode)) {
/* 271 */       this.updatingSectionData.setLayer(sectionNode, createDataLayer(sectionNode));
/* 272 */       this.changedSections.add(sectionNode);
/* 273 */       onNodeAdded(sectionNode);
/* 274 */       markSectionAndNeighborsAsAffected(sectionNode);
/* 275 */       this.hasInconsistencies = true;
/*     */     } 
/*     */   }
/*     */   
/*     */   private void removeSection(long sectionNode) {
/* 280 */     this.toRemove.add(sectionNode);
/* 281 */     this.hasInconsistencies = true;
/*     */   }
/*     */   
/*     */   protected void swapSectionMap() {
/* 285 */     if (!this.changedSections.isEmpty()) {
/* 286 */       M copy = this.updatingSectionData.copy();
/* 287 */       copy.disableCache();
/* 288 */       this.visibleSectionData = copy;
/* 289 */       this.changedSections.clear();
/*     */     } 
/* 291 */     if (!this.sectionsAffectedByLightUpdates.isEmpty()) {
/* 292 */       LongIterator iterator = this.sectionsAffectedByLightUpdates.iterator();
/* 293 */       while (iterator.hasNext()) {
/* 294 */         long sectionNode = iterator.nextLong();
/* 295 */         this.chunkSource.onLightUpdate(this.layer, SectionPos.of(sectionNode));
/*     */       } 
/* 297 */       this.sectionsAffectedByLightUpdates.clear();
/*     */     } 
/*     */   }
/*     */   
/*     */   public SectionType getDebugSectionType(long sectionNode) {
/* 302 */     return SectionState.type(this.sectionStates.get(sectionNode));
/*     */   }
/*     */ 
/*     */   
/*     */   protected static class SectionState
/*     */   {
/*     */     public static final byte EMPTY = 0;
/*     */     private static final int MIN_NEIGHBORS = 0;
/*     */     private static final int MAX_NEIGHBORS = 26;
/*     */     private static final byte HAS_DATA_BIT = 32;
/*     */     private static final byte NEIGHBOR_COUNT_BITS = 31;
/*     */     
/*     */     public static byte hasData(byte state, boolean hasData) {
/* 315 */       return (byte)(hasData ? (state | 0x20) : (state & 0xFFFFFFDF));
/*     */     }
/*     */     
/*     */     public static byte neighborCount(byte state, int neighborCount) {
/* 319 */       if (neighborCount < 0 || neighborCount > 26) {
/* 320 */         throw new IllegalArgumentException("Neighbor count was not within range [0; 26]");
/*     */       }
/* 322 */       return (byte)(state & 0xFFFFFFE0 | neighborCount & 0x1F);
/*     */     }
/*     */     
/*     */     public static boolean hasData(byte state) {
/* 326 */       return ((state & 0x20) != 0);
/*     */     }
/*     */     
/*     */     public static int neighborCount(byte state) {
/* 330 */       return state & 0x1F;
/*     */     }
/*     */     
/*     */     public static LayerLightSectionStorage.SectionType type(byte state) {
/* 334 */       if (state == 0) {
/* 335 */         return LayerLightSectionStorage.SectionType.EMPTY;
/*     */       }
/* 337 */       if (hasData(state)) {
/* 338 */         return LayerLightSectionStorage.SectionType.LIGHT_AND_DATA;
/*     */       }
/* 340 */       return LayerLightSectionStorage.SectionType.LIGHT_ONLY;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum SectionType {
/* 345 */     EMPTY("2"),
/* 346 */     LIGHT_ONLY("1"),
/* 347 */     LIGHT_AND_DATA("0");
/*     */     
/*     */     private final String display;
/*     */ 
/*     */     
/*     */     SectionType(String display) {
/* 353 */       this.display = display;
/*     */     }
/*     */     
/*     */     public String display() {
/* 357 */       return this.display;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/lighting/LayerLightSectionStorage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */