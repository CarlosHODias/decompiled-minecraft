/*     */ package net.minecraft.world.level.levelgen;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectListIterator;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Set;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.BitStorage;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.SimpleBitStorage;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.level.block.Blocks;
/*     */ import net.minecraft.world.level.block.state.BlockBehaviour;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.chunk.ChunkAccess;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Heightmap
/*     */ {
/*  30 */   private static final Logger LOGGER = LogUtils.getLogger(); private static final Predicate<BlockState> NOT_AIR; static {
/*  31 */     NOT_AIR = (input -> !input.isAir());
/*  32 */   } private static final Predicate<BlockState> MATERIAL_MOTION_BLOCKING = BlockBehaviour.BlockStateBase::blocksMotion; private final BitStorage data; private final Predicate<BlockState> isOpaque;
/*     */   private final ChunkAccess chunk;
/*     */   
/*  35 */   public enum Usage { WORLDGEN,
/*  36 */     LIVE_WORLD,
/*  37 */     CLIENT; }
/*     */ 
/*     */   
/*     */   public enum Types
/*     */     implements StringRepresentable {
/*  42 */     WORLD_SURFACE_WG(0, "WORLD_SURFACE_WG", Heightmap.Usage.WORLDGEN, Heightmap.NOT_AIR),
/*  43 */     WORLD_SURFACE(1, "WORLD_SURFACE", Heightmap.Usage.CLIENT, Heightmap.NOT_AIR),
/*  44 */     OCEAN_FLOOR_WG(2, "OCEAN_FLOOR_WG", Heightmap.Usage.WORLDGEN, Heightmap.MATERIAL_MOTION_BLOCKING),
/*  45 */     OCEAN_FLOOR(3, "OCEAN_FLOOR", Heightmap.Usage.LIVE_WORLD, Heightmap.MATERIAL_MOTION_BLOCKING), MOTION_BLOCKING(3, "OCEAN_FLOOR", Heightmap.Usage.LIVE_WORLD, Heightmap.MATERIAL_MOTION_BLOCKING), MOTION_BLOCKING_NO_LEAVES(3, "OCEAN_FLOOR", Heightmap.Usage.LIVE_WORLD, Heightmap.MATERIAL_MOTION_BLOCKING); static {
/*  46 */       MOTION_BLOCKING = new Types("MOTION_BLOCKING", 4, 4, "MOTION_BLOCKING", Heightmap.Usage.CLIENT, input -> (input.blocksMotion() || !input.getFluidState().isEmpty()));
/*  47 */       MOTION_BLOCKING_NO_LEAVES = new Types("MOTION_BLOCKING_NO_LEAVES", 5, 5, "MOTION_BLOCKING_NO_LEAVES", Heightmap.Usage.CLIENT, input -> ((input.blocksMotion() || !input.getFluidState().isEmpty()) && !(input.getBlock() instanceof net.minecraft.world.level.block.LeavesBlock)));
/*     */     }
/*     */ 
/*     */     
/*  51 */     public static final Codec<Types> CODEC = (Codec<Types>)StringRepresentable.fromEnum(Types::values); private static final IntFunction<Types> BY_ID;
/*     */     static {
/*  53 */       BY_ID = ByIdMap.continuous(t -> t.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*  54 */       STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, t -> t.id);
/*     */     }
/*     */     public static final StreamCodec<ByteBuf, Types> STREAM_CODEC; private final int id;
/*     */     private final String serializationKey;
/*     */     private final Heightmap.Usage usage;
/*     */     private final Predicate<BlockState> isOpaque;
/*     */     
/*     */     Types(int id, String serializationKey, Heightmap.Usage usage, Predicate<BlockState> isOpaque) {
/*  62 */       this.id = id;
/*  63 */       this.serializationKey = serializationKey;
/*  64 */       this.usage = usage;
/*  65 */       this.isOpaque = isOpaque;
/*     */     }
/*     */     
/*     */     public String getSerializationKey() {
/*  69 */       return this.serializationKey;
/*     */     }
/*     */     
/*     */     public boolean sendToClient() {
/*  73 */       return (this.usage == Heightmap.Usage.CLIENT);
/*     */     }
/*     */     
/*     */     public boolean keepAfterWorldgen() {
/*  77 */       return (this.usage != Heightmap.Usage.WORLDGEN);
/*     */     }
/*     */     
/*     */     public Predicate<BlockState> isOpaque() {
/*  81 */       return this.isOpaque;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/*  86 */       return this.serializationKey;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Heightmap(ChunkAccess chunk, Types heightmapType) {
/*  95 */     this.isOpaque = heightmapType.isOpaque();
/*  96 */     this.chunk = chunk;
/*  97 */     int heightBits = Mth.ceillog2(chunk.getHeight() + 1);
/*  98 */     this.data = (BitStorage)new SimpleBitStorage(heightBits, 256);
/*     */   }
/*     */   
/*     */   public static void primeHeightmaps(ChunkAccess chunk, Set<Types> types) {
/* 102 */     if (types.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/* 106 */     int size = types.size();
/* 107 */     ObjectArrayList objectArrayList = new ObjectArrayList(size);
/* 108 */     ObjectListIterator<Heightmap> iterator = objectArrayList.iterator();
/*     */     
/* 110 */     int highestSectionPosition = chunk.getHighestSectionPosition() + 16;
/* 111 */     BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 112 */     for (int x = 0; x < 16; x++) {
/* 113 */       for (int z = 0; z < 16; z++) {
/* 114 */         for (Types type : types) {
/* 115 */           objectArrayList.add(chunk.getOrCreateHeightmapUnprimed(type));
/*     */         }
/*     */         
/* 118 */         for (int y = highestSectionPosition - 1; y >= chunk.getMinY(); y--) {
/* 119 */           pos.set(x, y, z);
/* 120 */           BlockState state = chunk.getBlockState((BlockPos)pos);
/* 121 */           if (!state.is(Blocks.AIR)) {
/*     */ 
/*     */             
/* 124 */             while (iterator.hasNext()) {
/* 125 */               Heightmap heightmap = (Heightmap)iterator.next();
/* 126 */               if (heightmap.isOpaque.test(state)) {
/* 127 */                 heightmap.setHeight(x, z, y + 1);
/* 128 */                 iterator.remove();
/*     */               } 
/*     */             } 
/* 131 */             if (objectArrayList.isEmpty()) {
/*     */               break;
/*     */             }
/* 134 */             iterator.back(size);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public boolean update(int localX, int localY, int localZ, BlockState state) {
/* 141 */     int firstAvailable = getFirstAvailable(localX, localZ);
/* 142 */     if (localY <= firstAvailable - 2)
/*     */     {
/* 144 */       return false;
/*     */     }
/*     */     
/* 147 */     if (this.isOpaque.test(state)) {
/*     */       
/* 149 */       if (localY >= firstAvailable) {
/* 150 */         setHeight(localX, localZ, localY + 1);
/* 151 */         return true;
/*     */       }
/*     */     
/*     */     }
/* 155 */     else if (firstAvailable - 1 == localY) {
/* 156 */       BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
/* 157 */       for (int y = localY - 1; y >= this.chunk.getMinY(); y--) {
/* 158 */         pos.set(localX, y, localZ);
/* 159 */         if (this.isOpaque.test(this.chunk.getBlockState((BlockPos)pos))) {
/* 160 */           setHeight(localX, localZ, y + 1);
/* 161 */           return true;
/*     */         } 
/*     */       } 
/* 164 */       setHeight(localX, localZ, this.chunk.getMinY());
/* 165 */       return true;
/*     */     } 
/*     */     
/* 168 */     return false;
/*     */   }
/*     */   
/*     */   public int getFirstAvailable(int x, int z) {
/* 172 */     return getFirstAvailable(getIndex(x, z));
/*     */   }
/*     */   
/*     */   public int getHighestTaken(int x, int z) {
/* 176 */     return getFirstAvailable(getIndex(x, z)) - 1;
/*     */   }
/*     */   
/*     */   private int getFirstAvailable(int index) {
/* 180 */     return this.data.get(index) + this.chunk.getMinY();
/*     */   }
/*     */   
/*     */   private void setHeight(int x, int z, int height) {
/* 184 */     this.data.set(getIndex(x, z), height - this.chunk.getMinY());
/*     */   }
/*     */   
/*     */   public void setRawData(ChunkAccess chunk, Types type, long[] data) {
/* 188 */     long[] rawData = this.data.getRaw();
/* 189 */     if (rawData.length == data.length) {
/* 190 */       System.arraycopy(data, 0, rawData, 0, data.length);
/*     */       return;
/*     */     } 
/* 193 */     LOGGER.warn("Ignoring heightmap data for chunk {}, size does not match; expected: {}, got: {}", new Object[] { chunk.getPos(), rawData.length, data.length });
/* 194 */     primeHeightmaps(chunk, EnumSet.of(type));
/*     */   }
/*     */   
/*     */   public long[] getRawData() {
/* 198 */     return this.data.getRaw();
/*     */   }
/*     */   
/*     */   private static int getIndex(int x, int z) {
/* 202 */     return x + z * 16;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/Heightmap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */