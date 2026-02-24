/*     */ package net.minecraft.world.level.lighting;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
/*     */ import it.unimi.dsi.fastutil.longs.LongIterator;
/*     */ import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
/*     */ import java.util.Arrays;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.Direction;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.DataLayer;
/*     */ import net.minecraft.world.level.chunk.LightChunk;
/*     */ import net.minecraft.world.level.chunk.LightChunkGetter;
/*     */ import net.minecraft.world.phys.shapes.Shapes;
/*     */ import net.minecraft.world.phys.shapes.VoxelShape;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class LightEngine<M extends DataLayerStorageMap<M>, S extends LayerLightSectionStorage<M>>
/*     */   implements LayerLightEventListener
/*     */ {
/*     */   public static final int MAX_LEVEL = 15;
/*     */   protected static final int MIN_OPACITY = 1;
/*  29 */   protected static final long PULL_LIGHT_IN_ENTRY = QueueEntry.decreaseAllDirections(1);
/*     */   
/*     */   private static final int MIN_QUEUE_SIZE = 512;
/*     */   
/*  33 */   protected static final Direction[] PROPAGATION_DIRECTIONS = Direction.values();
/*     */   
/*     */   protected final LightChunkGetter chunkSource;
/*     */   
/*     */   protected final S storage;
/*  38 */   private final LongOpenHashSet blockNodesToCheck = new LongOpenHashSet(512, 0.5F);
/*  39 */   private final LongArrayFIFOQueue decreaseQueue = new LongArrayFIFOQueue();
/*  40 */   private final LongArrayFIFOQueue increaseQueue = new LongArrayFIFOQueue();
/*     */   
/*     */   private static final int CACHE_SIZE = 2;
/*  43 */   private final long[] lastChunkPos = new long[2];
/*  44 */   private final LightChunk[] lastChunk = new LightChunk[2];
/*     */   
/*     */   protected LightEngine(LightChunkGetter chunkSource, S storage) {
/*  47 */     this.chunkSource = chunkSource;
/*  48 */     this.storage = storage;
/*  49 */     clearChunkCache();
/*     */   }
/*     */   
/*     */   public static boolean hasDifferentLightProperties(BlockState oldState, BlockState newState) {
/*  53 */     if (newState == oldState) {
/*  54 */       return false;
/*     */     }
/*  56 */     return (newState.getLightBlock() != oldState.getLightBlock() || 
/*  57 */       newState.getLightEmission() != oldState.getLightEmission() || 
/*  58 */       newState.useShapeForLightOcclusion() || 
/*  59 */       oldState.useShapeForLightOcclusion());
/*     */   }
/*     */   
/*     */   public static int getLightBlockInto(BlockState fromState, BlockState toState, Direction direction, int simpleOpacity) {
/*  63 */     boolean fromEmpty = isEmptyShape(fromState);
/*  64 */     boolean toEmpty = isEmptyShape(toState);
/*     */     
/*  66 */     if (fromEmpty && toEmpty) {
/*  67 */       return simpleOpacity;
/*     */     }
/*     */     
/*  70 */     VoxelShape fromShape = fromEmpty ? Shapes.empty() : fromState.getOcclusionShape();
/*  71 */     VoxelShape toShape = toEmpty ? Shapes.empty() : toState.getOcclusionShape();
/*     */     
/*  73 */     if (Shapes.mergedFaceOccludes(fromShape, toShape, direction)) {
/*  74 */       return 16;
/*     */     }
/*     */     
/*  77 */     return simpleOpacity;
/*     */   }
/*     */   
/*     */   public static VoxelShape getOcclusionShape(BlockState state, Direction direction) {
/*  81 */     return isEmptyShape(state) ? Shapes.empty() : state.getFaceOcclusionShape(direction);
/*     */   }
/*     */   
/*     */   protected static boolean isEmptyShape(BlockState state) {
/*  85 */     return (!state.canOcclude() || !state.useShapeForLightOcclusion());
/*     */   }
/*     */   
/*     */   protected BlockState getState(BlockPos pos) {
/*  89 */     int chunkX = SectionPos.blockToSectionCoord(pos.getX());
/*  90 */     int chunkZ = SectionPos.blockToSectionCoord(pos.getZ());
/*  91 */     LightChunk chunk = getChunk(chunkX, chunkZ);
/*  92 */     if (chunk == null)
/*     */     {
/*     */ 
/*     */       
/*  96 */       return Blocks.BEDROCK.defaultBlockState();
/*     */     }
/*  98 */     return chunk.getBlockState(pos);
/*     */   }
/*     */   
/*     */   protected int getOpacity(BlockState state) {
/* 102 */     return Math.max(1, state.getLightBlock());
/*     */   }
/*     */   
/*     */   protected boolean shapeOccludes(BlockState fromState, BlockState toState, Direction direction) {
/* 106 */     VoxelShape fromShape = getOcclusionShape(fromState, direction);
/* 107 */     VoxelShape toShape = getOcclusionShape(toState, direction.getOpposite());
/* 108 */     return Shapes.faceShapeOccludes(fromShape, toShape);
/*     */   }
/*     */   
/*     */   protected LightChunk getChunk(int chunkX, int chunkZ) {
/* 112 */     long pos = ChunkPos.asLong(chunkX, chunkZ);
/* 113 */     for (int i = 0; i < 2; i++) {
/* 114 */       if (pos == this.lastChunkPos[i]) {
/* 115 */         return this.lastChunk[i];
/*     */       }
/*     */     } 
/* 118 */     LightChunk chunk = this.chunkSource.getChunkForLighting(chunkX, chunkZ);
/* 119 */     for (int j = 1; j > 0; j--) {
/* 120 */       this.lastChunkPos[j] = this.lastChunkPos[j - 1];
/* 121 */       this.lastChunk[j] = this.lastChunk[j - 1];
/*     */     } 
/* 123 */     this.lastChunkPos[0] = pos;
/* 124 */     this.lastChunk[0] = chunk;
/* 125 */     return chunk;
/*     */   }
/*     */   
/*     */   private void clearChunkCache() {
/* 129 */     Arrays.fill(this.lastChunkPos, ChunkPos.INVALID_CHUNK_POS);
/* 130 */     Arrays.fill((Object[])this.lastChunk, null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkBlock(BlockPos pos) {
/* 135 */     this.blockNodesToCheck.add(pos.asLong());
/*     */   }
/*     */   
/*     */   public void queueSectionData(long pos, DataLayer data) {
/* 139 */     this.storage.queueSectionData(pos, data);
/*     */   }
/*     */   
/*     */   public void retainData(ChunkPos pos, boolean retain) {
/* 143 */     this.storage.retainData(SectionPos.getZeroNode(pos.x, pos.z), retain);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateSectionStatus(SectionPos pos, boolean sectionEmpty) {
/* 148 */     this.storage.updateSectionStatus(pos.asLong(), sectionEmpty);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setLightEnabled(ChunkPos pos, boolean enable) {
/* 153 */     this.storage.setLightEnabled(SectionPos.getZeroNode(pos.x, pos.z), enable);
/*     */   }
/*     */ 
/*     */   
/*     */   public int runLightUpdates() {
/* 158 */     LongIterator iterator = this.blockNodesToCheck.iterator();
/* 159 */     while (iterator.hasNext()) {
/* 160 */       checkNode(iterator.nextLong());
/*     */     }
/* 162 */     this.blockNodesToCheck.clear();
/* 163 */     this.blockNodesToCheck.trim(512);
/*     */     
/* 165 */     int count = 0;
/* 166 */     count += propagateDecreases();
/* 167 */     count += propagateIncreases();
/*     */     
/* 169 */     clearChunkCache();
/*     */     
/* 171 */     this.storage.markNewInconsistencies(this);
/* 172 */     this.storage.swapSectionMap();
/*     */     
/* 174 */     return count;
/*     */   }
/*     */   
/*     */   private int propagateIncreases() {
/* 178 */     int count = 0;
/* 179 */     while (!this.increaseQueue.isEmpty()) {
/* 180 */       long fromNode = this.increaseQueue.dequeueLong();
/* 181 */       long increaseData = this.increaseQueue.dequeueLong();
/*     */       
/* 183 */       int fromLevel = this.storage.getStoredLevel(fromNode);
/*     */       
/* 185 */       int fromTargetLevel = QueueEntry.getFromLevel(increaseData);
/* 186 */       if (QueueEntry.isIncreaseFromEmission(increaseData) && fromLevel < fromTargetLevel) {
/* 187 */         this.storage.setStoredLevel(fromNode, fromTargetLevel);
/* 188 */         fromLevel = fromTargetLevel;
/*     */       } 
/* 190 */       if (fromLevel == fromTargetLevel) {
/* 191 */         propagateIncrease(fromNode, increaseData, fromLevel);
/*     */       }
/*     */       
/* 194 */       count++;
/*     */     } 
/* 196 */     return count;
/*     */   }
/*     */   
/*     */   private int propagateDecreases() {
/* 200 */     int count = 0;
/* 201 */     while (!this.decreaseQueue.isEmpty()) {
/* 202 */       long fromNode = this.decreaseQueue.dequeueLong();
/* 203 */       long decreaseData = this.decreaseQueue.dequeueLong();
/* 204 */       propagateDecrease(fromNode, decreaseData);
/* 205 */       count++;
/*     */     } 
/* 207 */     return count;
/*     */   }
/*     */   
/*     */   protected void enqueueDecrease(long fromNode, long decreaseData) {
/* 211 */     this.decreaseQueue.enqueue(fromNode);
/* 212 */     this.decreaseQueue.enqueue(decreaseData);
/*     */   }
/*     */   
/*     */   protected void enqueueIncrease(long fromNode, long increaseData) {
/* 216 */     this.increaseQueue.enqueue(fromNode);
/* 217 */     this.increaseQueue.enqueue(increaseData);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasLightWork() {
/* 222 */     return (this.storage.hasInconsistencies() || !this.blockNodesToCheck.isEmpty() || !this.decreaseQueue.isEmpty() || !this.increaseQueue.isEmpty());
/*     */   }
/*     */ 
/*     */   
/*     */   public DataLayer getDataLayerData(SectionPos pos) {
/* 227 */     return this.storage.getDataLayerData(pos.asLong());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getLightValue(BlockPos pos) {
/* 232 */     return this.storage.getLightValue(pos.asLong());
/*     */   }
/*     */   
/*     */   public String getDebugData(long sectionNode) {
/* 236 */     return getDebugSectionType(sectionNode).display();
/*     */   }
/*     */   
/*     */   public LayerLightSectionStorage.SectionType getDebugSectionType(long sectionNode) {
/* 240 */     return this.storage.getDebugSectionType(sectionNode);
/*     */   }
/*     */   
/*     */   protected abstract void checkNode(long paramLong);
/*     */   
/*     */   protected abstract void propagateIncrease(long paramLong1, long paramLong2, int paramInt);
/*     */   
/*     */   protected abstract void propagateDecrease(long paramLong1, long paramLong2);
/*     */   
/*     */   public static class QueueEntry {
/*     */     private static final int FROM_LEVEL_BITS = 4;
/*     */     private static final int DIRECTION_BITS = 6;
/*     */     private static final long LEVEL_MASK = 15L;
/*     */     private static final long DIRECTIONS_MASK = 1008L;
/*     */     private static final long FLAG_FROM_EMPTY_SHAPE = 1024L;
/*     */     private static final long FLAG_INCREASE_FROM_EMISSION = 2048L;
/*     */     
/*     */     public static long decreaseSkipOneDirection(int oldFromLevel, Direction skipDirection) {
/* 258 */       long decreaseData = withoutDirection(1008L, skipDirection);
/* 259 */       return withLevel(decreaseData, oldFromLevel);
/*     */     }
/*     */     
/*     */     public static long decreaseAllDirections(int oldFromLevel) {
/* 263 */       return withLevel(1008L, oldFromLevel);
/*     */     }
/*     */     
/*     */     public static long increaseLightFromEmission(int newFromLevel, boolean fromEmptyShape) {
/* 267 */       long increaseData = 1008L;
/* 268 */       increaseData |= 0x800L;
/* 269 */       if (fromEmptyShape) {
/* 270 */         increaseData |= 0x400L;
/*     */       }
/* 272 */       return withLevel(increaseData, newFromLevel);
/*     */     }
/*     */     
/*     */     public static long increaseSkipOneDirection(int newFromLevel, boolean fromEmptyShape, Direction skipDirection) {
/* 276 */       long increaseData = withoutDirection(1008L, skipDirection);
/* 277 */       if (fromEmptyShape) {
/* 278 */         increaseData |= 0x400L;
/*     */       }
/* 280 */       return withLevel(increaseData, newFromLevel);
/*     */     }
/*     */     
/*     */     public static long increaseOnlyOneDirection(int newFromLevel, boolean fromEmptyShape, Direction direction) {
/* 284 */       long increaseData = 0L;
/* 285 */       if (fromEmptyShape) {
/* 286 */         increaseData |= 0x400L;
/*     */       }
/* 288 */       increaseData = withDirection(increaseData, direction);
/* 289 */       return withLevel(increaseData, newFromLevel);
/*     */     }
/*     */     
/*     */     public static long increaseSkySourceInDirections(boolean down, boolean north, boolean south, boolean west, boolean east) {
/* 293 */       long increaseData = withLevel(0L, 15);
/* 294 */       if (down) {
/* 295 */         increaseData = withDirection(increaseData, Direction.DOWN);
/*     */       }
/* 297 */       if (north) {
/* 298 */         increaseData = withDirection(increaseData, Direction.NORTH);
/*     */       }
/* 300 */       if (south) {
/* 301 */         increaseData = withDirection(increaseData, Direction.SOUTH);
/*     */       }
/* 303 */       if (west) {
/* 304 */         increaseData = withDirection(increaseData, Direction.WEST);
/*     */       }
/* 306 */       if (east) {
/* 307 */         increaseData = withDirection(increaseData, Direction.EAST);
/*     */       }
/* 309 */       return increaseData;
/*     */     }
/*     */     
/*     */     public static int getFromLevel(long entry) {
/* 313 */       return (int)(entry & 0xFL);
/*     */     }
/*     */     
/*     */     public static boolean isFromEmptyShape(long entry) {
/* 317 */       return ((entry & 0x400L) != 0L);
/*     */     }
/*     */     
/*     */     public static boolean isIncreaseFromEmission(long entry) {
/* 321 */       return ((entry & 0x800L) != 0L);
/*     */     }
/*     */     
/*     */     public static boolean shouldPropagateInDirection(long entry, Direction direction) {
/* 325 */       return ((entry & 1L << direction.ordinal() + 4) != 0L);
/*     */     }
/*     */     
/*     */     private static long withLevel(long entry, int level) {
/* 329 */       return entry & 0xFFFFFFFFFFFFFFF0L | level & 0xFL;
/*     */     }
/*     */     
/*     */     private static long withDirection(long entry, Direction direction) {
/* 333 */       return entry | 1L << direction.ordinal() + 4;
/*     */     }
/*     */     
/*     */     private static long withoutDirection(long entry, Direction direction) {
/* 337 */       return entry & (1L << direction.ordinal() + 4 ^ 0xFFFFFFFFFFFFFFFFL);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/lighting/LightEngine.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */