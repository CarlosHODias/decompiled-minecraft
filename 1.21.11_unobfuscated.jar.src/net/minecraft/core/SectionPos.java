/*     */ package net.minecraft.core;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import it.unimi.dsi.fastutil.longs.LongConsumer;
/*     */ import java.util.Spliterators;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.Stream;
/*     */ import java.util.stream.StreamSupport;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.level.ChunkPos;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import net.minecraft.world.level.entity.EntityAccess;
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
/*     */ public class SectionPos
/*     */   extends Vec3i
/*     */ {
/*     */   public static final int SECTION_BITS = 4;
/*     */   public static final int SECTION_SIZE = 16;
/*     */   public static final int SECTION_MASK = 15;
/*     */   public static final int SECTION_HALF_SIZE = 8;
/*     */   public static final int SECTION_MAX_INDEX = 15;
/*     */   private static final int PACKED_X_LENGTH = 22;
/*     */   private static final int PACKED_Y_LENGTH = 20;
/*     */   private static final int PACKED_Z_LENGTH = 22;
/*     */   private static final long PACKED_X_MASK = 4194303L;
/*     */   private static final long PACKED_Y_MASK = 1048575L;
/*     */   private static final long PACKED_Z_MASK = 4194303L;
/*     */   private static final int Y_OFFSET = 0;
/*     */   private static final int Z_OFFSET = 20;
/*     */   private static final int X_OFFSET = 42;
/*     */   private static final int RELATIVE_X_SHIFT = 8;
/*     */   private static final int RELATIVE_Y_SHIFT = 0;
/*     */   private static final int RELATIVE_Z_SHIFT = 4;
/*  50 */   public static final StreamCodec<ByteBuf, SectionPos> STREAM_CODEC = ByteBufCodecs.LONG.map(SectionPos::of, SectionPos::asLong);
/*     */   
/*     */   private SectionPos(int x, int y, int z) {
/*  53 */     super(x, y, z);
/*     */   }
/*     */   
/*     */   public static SectionPos of(int x, int y, int z) {
/*  57 */     return new SectionPos(x, y, z);
/*     */   }
/*     */   
/*     */   public static SectionPos of(BlockPos pos) {
/*  61 */     return new SectionPos(blockToSectionCoord(pos.getX()), blockToSectionCoord(pos.getY()), blockToSectionCoord(pos.getZ()));
/*     */   }
/*     */   
/*     */   public static SectionPos of(ChunkPos pos, int sectionY) {
/*  65 */     return new SectionPos(pos.x, sectionY, pos.z);
/*     */   }
/*     */   
/*     */   public static SectionPos of(EntityAccess entity) {
/*  69 */     return of(entity.blockPosition());
/*     */   }
/*     */   
/*     */   public static SectionPos of(Position pos) {
/*  73 */     return new SectionPos(
/*  74 */         blockToSectionCoord(pos.x()), 
/*  75 */         blockToSectionCoord(pos.y()), 
/*  76 */         blockToSectionCoord(pos.z()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static SectionPos of(long sectionNode) {
/*  81 */     return new SectionPos(x(sectionNode), y(sectionNode), z(sectionNode));
/*     */   }
/*     */   
/*     */   public static SectionPos bottomOf(ChunkAccess chunk) {
/*  85 */     return of(chunk.getPos(), chunk.getMinSectionY());
/*     */   }
/*     */   
/*     */   public static long offset(long sectionNode, Direction offset) {
/*  89 */     return offset(sectionNode, offset.getStepX(), offset.getStepY(), offset.getStepZ());
/*     */   }
/*     */   
/*     */   public static long offset(long sectionNode, int stepX, int stepY, int stepZ) {
/*  93 */     return asLong(x(sectionNode) + stepX, y(sectionNode) + stepY, z(sectionNode) + stepZ);
/*     */   }
/*     */   
/*     */   public static int posToSectionCoord(double pos) {
/*  97 */     return blockToSectionCoord(Mth.floor(pos));
/*     */   }
/*     */   
/*     */   public static int blockToSectionCoord(int blockCoord) {
/* 101 */     return blockCoord >> 4;
/*     */   }
/*     */   
/*     */   public static int blockToSectionCoord(double coord) {
/* 105 */     return Mth.floor(coord) >> 4;
/*     */   }
/*     */   
/*     */   public static int sectionRelative(int blockCoord) {
/* 109 */     return blockCoord & 0xF;
/*     */   }
/*     */   
/*     */   public static short sectionRelativePos(BlockPos pos) {
/* 113 */     int x = sectionRelative(pos.getX());
/* 114 */     int y = sectionRelative(pos.getY());
/* 115 */     int z = sectionRelative(pos.getZ());
/* 116 */     return (short)(x << 8 | z << 4 | y << 0);
/*     */   }
/*     */   
/*     */   public static int sectionRelativeX(short relative) {
/* 120 */     return relative >>> 8 & 0xF;
/*     */   }
/*     */   
/*     */   public static int sectionRelativeY(short relative) {
/* 124 */     return relative >>> 0 & 0xF;
/*     */   }
/*     */   
/*     */   public static int sectionRelativeZ(short relative) {
/* 128 */     return relative >>> 4 & 0xF;
/*     */   }
/*     */   
/*     */   public int relativeToBlockX(short relative) {
/* 132 */     return minBlockX() + sectionRelativeX(relative);
/*     */   }
/*     */   
/*     */   public int relativeToBlockY(short relative) {
/* 136 */     return minBlockY() + sectionRelativeY(relative);
/*     */   }
/*     */   
/*     */   public int relativeToBlockZ(short relative) {
/* 140 */     return minBlockZ() + sectionRelativeZ(relative);
/*     */   }
/*     */   
/*     */   public BlockPos relativeToBlockPos(short relative) {
/* 144 */     return new BlockPos(relativeToBlockX(relative), relativeToBlockY(relative), relativeToBlockZ(relative));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int sectionToBlockCoord(int sectionCoord) {
/* 152 */     return sectionCoord << 4;
/*     */   }
/*     */   
/*     */   public static int sectionToBlockCoord(int sectionCoord, int offset) {
/* 156 */     return sectionToBlockCoord(sectionCoord) + offset;
/*     */   }
/*     */   
/*     */   public static int x(long sectionNode) {
/* 160 */     return (int)(sectionNode << 0L >> 42L);
/*     */   }
/*     */   
/*     */   public static int y(long sectionNode) {
/* 164 */     return (int)(sectionNode << 44L >> 44L);
/*     */   }
/*     */   
/*     */   public static int z(long sectionNode) {
/* 168 */     return (int)(sectionNode << 22L >> 42L);
/*     */   }
/*     */   
/*     */   public int x() {
/* 172 */     return getX();
/*     */   }
/*     */   
/*     */   public int y() {
/* 176 */     return getY();
/*     */   }
/*     */   
/*     */   public int z() {
/* 180 */     return getZ();
/*     */   }
/*     */   
/*     */   public int minBlockX() {
/* 184 */     return sectionToBlockCoord(x());
/*     */   }
/*     */   
/*     */   public int minBlockY() {
/* 188 */     return sectionToBlockCoord(y());
/*     */   }
/*     */   
/*     */   public int minBlockZ() {
/* 192 */     return sectionToBlockCoord(z());
/*     */   }
/*     */   
/*     */   public int maxBlockX() {
/* 196 */     return sectionToBlockCoord(x(), 15);
/*     */   }
/*     */   
/*     */   public int maxBlockY() {
/* 200 */     return sectionToBlockCoord(y(), 15);
/*     */   }
/*     */   
/*     */   public int maxBlockZ() {
/* 204 */     return sectionToBlockCoord(z(), 15);
/*     */   }
/*     */   
/*     */   public static long blockToSection(long blockNode) {
/* 208 */     return asLong(
/* 209 */         blockToSectionCoord(BlockPos.getX(blockNode)), 
/* 210 */         blockToSectionCoord(BlockPos.getY(blockNode)), 
/* 211 */         blockToSectionCoord(BlockPos.getZ(blockNode)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getZeroNode(int x, int z) {
/* 216 */     return getZeroNode(asLong(x, 0, z));
/*     */   }
/*     */ 
/*     */   
/*     */   public static long getZeroNode(long sectionNode) {
/* 221 */     return sectionNode & 0xFFFFFFFFFFF00000L;
/*     */   }
/*     */   
/*     */   public static long sectionToChunk(long sectionNode) {
/* 225 */     return ChunkPos.asLong(x(sectionNode), z(sectionNode));
/*     */   }
/*     */   
/*     */   public BlockPos origin() {
/* 229 */     return new BlockPos(sectionToBlockCoord(x()), sectionToBlockCoord(y()), sectionToBlockCoord(z()));
/*     */   }
/*     */   
/*     */   public BlockPos center() {
/* 233 */     int delta = 8;
/* 234 */     return origin().offset(8, 8, 8);
/*     */   }
/*     */   
/*     */   public ChunkPos chunk() {
/* 238 */     return new ChunkPos(x(), z());
/*     */   }
/*     */   
/*     */   public static long asLong(BlockPos pos) {
/* 242 */     return asLong(blockToSectionCoord(pos.getX()), blockToSectionCoord(pos.getY()), blockToSectionCoord(pos.getZ()));
/*     */   }
/*     */   
/*     */   public static long asLong(int x, int y, int z) {
/* 246 */     long node = 0L;
/* 247 */     node |= (x & 0x3FFFFFL) << 42L;
/* 248 */     node |= (y & 0xFFFFFL) << 0L;
/* 249 */     node |= (z & 0x3FFFFFL) << 20L;
/* 250 */     return node;
/*     */   }
/*     */   
/*     */   public long asLong() {
/* 254 */     return asLong(x(), y(), z());
/*     */   }
/*     */ 
/*     */   
/*     */   public SectionPos offset(int x, int y, int z) {
/* 259 */     if (x == 0 && y == 0 && z == 0) {
/* 260 */       return this;
/*     */     }
/* 262 */     return new SectionPos(x() + x, y() + y, z() + z);
/*     */   }
/*     */   
/*     */   public Stream<BlockPos> blocksInside() {
/* 266 */     return BlockPos.betweenClosedStream(minBlockX(), minBlockY(), minBlockZ(), maxBlockX(), maxBlockY(), maxBlockZ());
/*     */   }
/*     */   
/*     */   public static Stream<SectionPos> cube(SectionPos center, int radius) {
/* 270 */     int x = center.x();
/* 271 */     int y = center.y();
/* 272 */     int z = center.z();
/* 273 */     return betweenClosedStream(x - radius, y - radius, z - radius, x + radius, y + radius, z + radius);
/*     */   }
/*     */   
/*     */   public static Stream<SectionPos> aroundChunk(ChunkPos center, int radius, int minSection, int maxSection) {
/* 277 */     int x = center.x;
/* 278 */     int z = center.z;
/* 279 */     return betweenClosedStream(x - radius, minSection, z - radius, x + radius, maxSection, z + radius);
/*     */   }
/*     */   
/*     */   public static Stream<SectionPos> betweenClosedStream(final int minX, final int minY, final int minZ, final int maxX, final int maxY, final int maxZ) {
/* 283 */     return StreamSupport.stream(new Spliterators.AbstractSpliterator<SectionPos>(((maxX - minX + 1) * (maxY - minY + 1) * (maxZ - minZ + 1)), 64) { {
/* 284 */             this.cursor = new Cursor3D(minX, minY, minZ, maxX, maxY, maxZ);
/*     */           }
/*     */           final Cursor3D cursor;
/*     */           public boolean tryAdvance(Consumer<? super SectionPos> action) {
/* 288 */             if (this.cursor.advance()) {
/* 289 */               action.accept(new SectionPos(this.cursor.nextX(), this.cursor.nextY(), this.cursor.nextZ()));
/* 290 */               return true;
/*     */             } 
/* 292 */             return false;
/*     */           }
/*     */         },  false);
/*     */   }
/*     */   
/*     */   public static void aroundAndAtBlockPos(BlockPos blockPos, LongConsumer sectionConsumer) {
/* 298 */     aroundAndAtBlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ(), sectionConsumer);
/*     */   }
/*     */   
/*     */   public static void aroundAndAtBlockPos(long blockPos, LongConsumer sectionConsumer) {
/* 302 */     aroundAndAtBlockPos(BlockPos.getX(blockPos), BlockPos.getY(blockPos), BlockPos.getZ(blockPos), sectionConsumer);
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
/*     */   public static void aroundAndAtBlockPos(int blockX, int blockY, int blockZ, LongConsumer sectionConsumer) {
/* 314 */     int minSectionX = blockToSectionCoord(blockX - 1);
/* 315 */     int maxSectionX = blockToSectionCoord(blockX + 1);
/*     */     
/* 317 */     int minSectionY = blockToSectionCoord(blockY - 1);
/* 318 */     int maxSectionY = blockToSectionCoord(blockY + 1);
/*     */     
/* 320 */     int minSectionZ = blockToSectionCoord(blockZ - 1);
/* 321 */     int maxSectionZ = blockToSectionCoord(blockZ + 1);
/*     */     
/* 323 */     if (minSectionX == maxSectionX && minSectionY == maxSectionY && minSectionZ == maxSectionZ) {
/* 324 */       sectionConsumer.accept(asLong(minSectionX, minSectionY, minSectionZ));
/*     */     } else {
/* 326 */       for (int sectionX = minSectionX; sectionX <= maxSectionX; sectionX++) {
/* 327 */         for (int sectionY = minSectionY; sectionY <= maxSectionY; sectionY++) {
/* 328 */           for (int sectionZ = minSectionZ; sectionZ <= maxSectionZ; sectionZ++)
/* 329 */             sectionConsumer.accept(asLong(sectionX, sectionY, sectionZ)); 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/SectionPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */