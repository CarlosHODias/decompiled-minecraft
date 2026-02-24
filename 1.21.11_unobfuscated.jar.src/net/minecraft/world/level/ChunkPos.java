/*     */ package net.minecraft.world.level;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.core.SectionPos;
/*     */ import net.minecraft.network.FriendlyByteBuf;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.chunk.status.ChunkPyramid;
/*     */ import net.minecraft.world.level.chunk.status.ChunkStatus;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ChunkPos
/*     */ {
/*     */   public static final Codec<ChunkPos> CODEC;
/*     */   
/*     */   static {
/*  29 */     CODEC = Codec.INT_STREAM.comapFlatMap(input -> Util.fixedSize(input, 2).map(()), pos -> IntStream.of(new int[] { pos.x, pos.z })).stable();
/*     */   }
/*  31 */   public static final StreamCodec<ByteBuf, ChunkPos> STREAM_CODEC = new StreamCodec<ByteBuf, ChunkPos>()
/*     */     {
/*     */       public ChunkPos decode(ByteBuf input) {
/*  34 */         return FriendlyByteBuf.readChunkPos(input);
/*     */       }
/*     */ 
/*     */       
/*     */       public void encode(ByteBuf output, ChunkPos value) {
/*  39 */         FriendlyByteBuf.writeChunkPos(output, value);
/*     */       }
/*     */     };
/*     */   
/*     */   private static final int SAFETY_MARGIN = 1056;
/*  44 */   public static final long INVALID_CHUNK_POS = asLong(1875066, 1875066);
/*     */   
/*  46 */   private static final int SAFETY_MARGIN_CHUNKS = (32 + ChunkPyramid.GENERATION_PYRAMID.getStepTo(ChunkStatus.FULL).accumulatedDependencies().size() + 1) * 2;
/*     */   
/*  48 */   public static final int MAX_COORDINATE_VALUE = SectionPos.blockToSectionCoord(BlockPos.MAX_HORIZONTAL_COORDINATE) - SAFETY_MARGIN_CHUNKS;
/*     */   
/*  50 */   public static final ChunkPos ZERO = new ChunkPos(0, 0);
/*     */   
/*     */   private static final long COORD_BITS = 32L;
/*     */   
/*     */   private static final long COORD_MASK = 4294967295L;
/*     */   
/*     */   private static final int REGION_BITS = 5;
/*     */   
/*     */   public static final int REGION_SIZE = 32;
/*     */   private static final int REGION_MASK = 31;
/*     */   
/*     */   public ChunkPos(int x, int z) {
/*  62 */     this.x = x;
/*  63 */     this.z = z;
/*     */   }
/*     */   public static final int REGION_MAX_INDEX = 31; public final int x; public final int z; private static final int HASH_A = 1664525; private static final int HASH_C = 1013904223; private static final int HASH_Z_XOR = -559038737;
/*     */   public ChunkPos(BlockPos pos) {
/*  67 */     this.x = SectionPos.blockToSectionCoord(pos.getX());
/*  68 */     this.z = SectionPos.blockToSectionCoord(pos.getZ());
/*     */   }
/*     */   
/*     */   public ChunkPos(long key) {
/*  72 */     this.x = (int)key;
/*  73 */     this.z = (int)(key >> 32L);
/*     */   }
/*     */   
/*     */   public static ChunkPos minFromRegion(int regionX, int regionZ) {
/*  77 */     return new ChunkPos(regionX << 5, regionZ << 5);
/*     */   }
/*     */   
/*     */   public static ChunkPos maxFromRegion(int regionX, int regionZ) {
/*  81 */     return new ChunkPos((regionX << 5) + 31, (regionZ << 5) + 31);
/*     */   }
/*     */   
/*     */   public boolean isValid() {
/*  85 */     return isValid(this.x, this.z);
/*     */   }
/*     */   
/*     */   public static boolean isValid(int x, int z) {
/*  89 */     return (Mth.absMax(x, z) <= MAX_COORDINATE_VALUE);
/*     */   }
/*     */   
/*     */   public long toLong() {
/*  93 */     return asLong(this.x, this.z);
/*     */   }
/*     */   
/*     */   public static long asLong(int x, int z) {
/*  97 */     return x & 0xFFFFFFFFL | (z & 0xFFFFFFFFL) << 32L;
/*     */   }
/*     */   
/*     */   public static long asLong(BlockPos pos) {
/* 101 */     return asLong(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
/*     */   }
/*     */   
/*     */   public static int getX(long pos) {
/* 105 */     return (int)(pos & 0xFFFFFFFFL);
/*     */   }
/*     */   
/*     */   public static int getZ(long pos) {
/* 109 */     return (int)(pos >>> 32L & 0xFFFFFFFFL);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 118 */     return hash(this.x, this.z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int hash(int x, int z) {
/* 125 */     int xTransform = 1664525 * x + 1013904223;
/* 126 */     int zTransform = 1664525 * (z ^ 0xDEADBEEF) + 1013904223;
/* 127 */     return xTransform ^ zTransform;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 132 */     if (this == o) {
/* 133 */       return true;
/*     */     }
/*     */     
/* 136 */     if (o instanceof ChunkPos) { ChunkPos chunkPos = (ChunkPos)o;
/* 137 */       return (this.x == chunkPos.x && this.z == chunkPos.z); }
/*     */ 
/*     */     
/* 140 */     return false;
/*     */   }
/*     */   
/*     */   public int getMiddleBlockX() {
/* 144 */     return getBlockX(8);
/*     */   }
/*     */   
/*     */   public int getMiddleBlockZ() {
/* 148 */     return getBlockZ(8);
/*     */   }
/*     */   
/*     */   public int getMinBlockX() {
/* 152 */     return SectionPos.sectionToBlockCoord(this.x);
/*     */   }
/*     */   
/*     */   public int getMinBlockZ() {
/* 156 */     return SectionPos.sectionToBlockCoord(this.z);
/*     */   }
/*     */   
/*     */   public int getMaxBlockX() {
/* 160 */     return getBlockX(15);
/*     */   }
/*     */   
/*     */   public int getMaxBlockZ() {
/* 164 */     return getBlockZ(15);
/*     */   }
/*     */   
/*     */   public int getRegionX() {
/* 168 */     return this.x >> 5;
/*     */   }
/*     */   
/*     */   public int getRegionZ() {
/* 172 */     return this.z >> 5;
/*     */   }
/*     */   
/*     */   public int getRegionLocalX() {
/* 176 */     return this.x & 0x1F;
/*     */   }
/*     */   
/*     */   public int getRegionLocalZ() {
/* 180 */     return this.z & 0x1F;
/*     */   }
/*     */   
/*     */   public BlockPos getBlockAt(int x, int y, int z) {
/* 184 */     return new BlockPos(getBlockX(x), y, getBlockZ(z));
/*     */   }
/*     */   
/*     */   public int getBlockX(int offset) {
/* 188 */     return SectionPos.sectionToBlockCoord(this.x, offset);
/*     */   }
/*     */   
/*     */   public int getBlockZ(int offset) {
/* 192 */     return SectionPos.sectionToBlockCoord(this.z, offset);
/*     */   }
/*     */   
/*     */   public BlockPos getMiddleBlockPosition(int y) {
/* 196 */     return new BlockPos(getMiddleBlockX(), y, getMiddleBlockZ());
/*     */   }
/*     */   
/*     */   public boolean contains(BlockPos pos) {
/* 200 */     return (pos.getX() >= getMinBlockX() && pos.getZ() >= getMinBlockZ() && 
/* 201 */       pos.getX() <= getMaxBlockX() && pos.getZ() <= getMaxBlockZ());
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 206 */     return "[" + this.x + ", " + this.z + "]";
/*     */   }
/*     */   
/*     */   public BlockPos getWorldPosition() {
/* 210 */     return new BlockPos(getMinBlockX(), 0, getMinBlockZ());
/*     */   }
/*     */   
/*     */   public int getChessboardDistance(ChunkPos pos) {
/* 214 */     return getChessboardDistance(pos.x, pos.z);
/*     */   }
/*     */   
/*     */   public int getChessboardDistance(int x, int z) {
/* 218 */     return Mth.chessboardDistance(x, z, this.x, this.z);
/*     */   }
/*     */   
/*     */   public int distanceSquared(ChunkPos pos) {
/* 222 */     return distanceSquared(pos.x, pos.z);
/*     */   }
/*     */   
/*     */   public int distanceSquared(long pos) {
/* 226 */     return distanceSquared(getX(pos), getZ(pos));
/*     */   }
/*     */   
/*     */   private int distanceSquared(int x, int z) {
/* 230 */     int deltaX = x - this.x;
/* 231 */     int deltaZ = z - this.z;
/* 232 */     return deltaX * deltaX + deltaZ * deltaZ;
/*     */   }
/*     */   
/*     */   public static Stream<ChunkPos> rangeClosed(ChunkPos center, int radius) {
/* 236 */     return rangeClosed(new ChunkPos(center.x - radius, center.z - radius), new ChunkPos(center.x + radius, center.z + radius));
/*     */   }
/*     */   
/*     */   public static Stream<ChunkPos> rangeClosed(final ChunkPos from, final ChunkPos to) {
/* 240 */     int xSize = Math.abs(from.x - to.x) + 1;
/* 241 */     int zSize = Math.abs(from.z - to.z) + 1;
/* 242 */     final int xDiff = (from.x < to.x) ? 1 : -1;
/* 243 */     final int zDiff = (from.z < to.z) ? 1 : -1;
/* 244 */     return StreamSupport.stream(new Spliterators.AbstractSpliterator<ChunkPos>((xSize * zSize), 64)
/*     */         {
/*     */           private ChunkPos pos;
/*     */           
/*     */           public boolean tryAdvance(Consumer<? super ChunkPos> action) {
/* 249 */             if (this.pos == null) {
/* 250 */               this.pos = from;
/*     */             } else {
/* 252 */               int x = this.pos.x;
/* 253 */               int z = this.pos.z;
/* 254 */               if (x == to.x) {
/* 255 */                 if (z == to.z) {
/* 256 */                   return false;
/*     */                 }
/* 258 */                 this.pos = new ChunkPos(from.x, z + zDiff);
/*     */               } else {
/* 260 */                 this.pos = new ChunkPos(x + xDiff, z);
/*     */               } 
/*     */             } 
/* 263 */             action.accept(this.pos);
/* 264 */             return true;
/*     */           }
/*     */         },  false);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/ChunkPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */