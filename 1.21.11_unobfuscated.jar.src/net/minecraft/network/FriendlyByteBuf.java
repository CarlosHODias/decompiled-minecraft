/*      */ package net.minecraft.network;
/*      */ 
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Maps;
/*      */ import com.google.gson.Gson;
/*      */ import com.google.gson.JsonElement;
/*      */ import com.mojang.datafixers.util.Either;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.DataResult;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import com.mojang.serialization.JsonOps;
/*      */ import io.netty.buffer.ByteBuf;
/*      */ import io.netty.buffer.ByteBufAllocator;
/*      */ import io.netty.buffer.ByteBufInputStream;
/*      */ import io.netty.buffer.ByteBufOutputStream;
/*      */ import io.netty.handler.codec.DecoderException;
/*      */ import io.netty.handler.codec.EncoderException;
/*      */ import io.netty.util.ByteProcessor;
/*      */ import io.netty.util.ReferenceCounted;
/*      */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*      */ import it.unimi.dsi.fastutil.ints.IntList;
/*      */ import java.io.DataInput;
/*      */ import java.io.DataOutput;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.channels.FileChannel;
/*      */ import java.nio.channels.GatheringByteChannel;
/*      */ import java.nio.channels.ScatteringByteChannel;
/*      */ import java.nio.charset.Charset;
/*      */ import java.security.PublicKey;
/*      */ import java.time.Instant;
/*      */ import java.util.Arrays;
/*      */ import java.util.BitSet;
/*      */ import java.util.Collection;
/*      */ import java.util.EnumSet;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Optional;
/*      */ import java.util.UUID;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.IntFunction;
/*      */ import java.util.function.ToIntFunction;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Direction;
/*      */ import net.minecraft.core.GlobalPos;
/*      */ import net.minecraft.core.Registry;
/*      */ import net.minecraft.core.registries.Registries;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.EndTag;
/*      */ import net.minecraft.nbt.NbtAccounter;
/*      */ import net.minecraft.nbt.NbtIo;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.codec.StreamDecoder;
/*      */ import net.minecraft.network.codec.StreamEncoder;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.util.Crypt;
/*      */ import net.minecraft.util.CryptException;
/*      */ import net.minecraft.util.LenientJsonParser;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.world.level.ChunkPos;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.Vec3;
/*      */ import org.joml.Quaternionf;
/*      */ import org.joml.Quaternionfc;
/*      */ import org.joml.Vector3f;
/*      */ import org.joml.Vector3fc;
/*      */ 
/*      */ 
/*      */ 
/*      */ public class FriendlyByteBuf
/*      */   extends ByteBuf
/*      */ {
/*      */   private final ByteBuf source;
/*      */   public static final short MAX_STRING_LENGTH = 32767;
/*      */   public static final int MAX_COMPONENT_STRING_LENGTH = 262144;
/*      */   private static final int PUBLIC_KEY_SIZE = 256;
/*      */   private static final int MAX_PUBLIC_KEY_HEADER_SIZE = 256;
/*      */   private static final int MAX_PUBLIC_KEY_LENGTH = 512;
/*   84 */   private static final Gson GSON = new Gson();
/*      */   
/*      */   public FriendlyByteBuf(ByteBuf source) {
/*   87 */     this.source = source;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public <T> T readWithCodecTrusted(DynamicOps<Tag> ops, Codec<T> codec) {
/*  103 */     return readWithCodec(ops, codec, NbtAccounter.unlimitedHeap());
/*      */   }
/*      */   
/*      */   @Deprecated
/*      */   public <T> T readWithCodec(DynamicOps<Tag> ops, Codec<T> codec, NbtAccounter accounter) {
/*  108 */     Tag tag = readNbt(accounter);
/*  109 */     return (T)codec.parse(ops, tag).getOrThrow(msg -> new DecoderException("Failed to decode: " + msg + " " + String.valueOf(tag)));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   @Deprecated
/*      */   public <T> FriendlyByteBuf writeWithCodec(DynamicOps<Tag> ops, Codec<T> codec, T value) {
/*  120 */     Tag tag = (Tag)codec.encodeStart(ops, value).getOrThrow(msg -> new EncoderException("Failed to encode: " + msg + " " + String.valueOf(value)));
/*  121 */     writeNbt(tag);
/*  122 */     return this;
/*      */   }
/*      */   
/*      */   public <T> T readLenientJsonWithCodec(Codec<T> codec) {
/*  126 */     JsonElement json = LenientJsonParser.parse(readUtf());
/*  127 */     DataResult<T> result = codec.parse((DynamicOps)JsonOps.INSTANCE, json);
/*  128 */     return (T)result.getOrThrow(error -> new DecoderException("Failed to decode JSON: " + error));
/*      */   }
/*      */   
/*      */   public <T> void writeJsonWithCodec(Codec<T> codec, T value) {
/*  132 */     DataResult<JsonElement> result = codec.encodeStart((DynamicOps)JsonOps.INSTANCE, value);
/*  133 */     writeUtf(GSON.toJson((JsonElement)result.getOrThrow(error -> new EncoderException("Failed to encode: " + error + " " + String.valueOf(value)))));
/*      */   }
/*      */   
/*      */   public static <T> IntFunction<T> limitValue(IntFunction<T> original, int limit) {
/*  137 */     return value -> {
/*      */         if (value > limit) {
/*      */           throw new DecoderException("Value " + value + " is larger than limit " + limit);
/*      */         }
/*      */         return original.apply(value);
/*      */       };
/*      */   }
/*      */   
/*      */   public <T, C extends Collection<T>> C readCollection(IntFunction<C> ctor, StreamDecoder<? super FriendlyByteBuf, T> elementDecoder) {
/*  146 */     int count = readVarInt();
/*  147 */     Collection<Object> collection = (Collection)ctor.apply(count);
/*  148 */     for (int i = 0; i < count; i++) {
/*  149 */       collection.add(elementDecoder.decode(this));
/*      */     }
/*  151 */     return (C)collection;
/*      */   }
/*      */   
/*      */   public <T> void writeCollection(Collection<T> collection, StreamEncoder<? super FriendlyByteBuf, T> encoder) {
/*  155 */     writeVarInt(collection.size());
/*  156 */     for (T element : collection) {
/*  157 */       encoder.encode(this, element);
/*      */     }
/*      */   }
/*      */   
/*      */   public <T> List<T> readList(StreamDecoder<? super FriendlyByteBuf, T> elementDecoder) {
/*  162 */     return readCollection(Lists::newArrayListWithCapacity, elementDecoder);
/*      */   }
/*      */   
/*      */   public IntList readIntIdList() {
/*  166 */     int count = readVarInt();
/*  167 */     IntArrayList intArrayList = new IntArrayList();
/*  168 */     for (int i = 0; i < count; i++) {
/*  169 */       intArrayList.add(readVarInt());
/*      */     }
/*  171 */     return (IntList)intArrayList;
/*      */   }
/*      */   
/*      */   public void writeIntIdList(IntList ids) {
/*  175 */     writeVarInt(ids.size());
/*  176 */     ids.forEach(this::writeVarInt);
/*      */   }
/*      */   
/*      */   public <K, V, M extends Map<K, V>> M readMap(IntFunction<M> ctor, StreamDecoder<? super FriendlyByteBuf, K> keyDecoder, StreamDecoder<? super FriendlyByteBuf, V> valueDecoder) {
/*  180 */     int count = readVarInt();
/*  181 */     Map<K, V> map = (Map)ctor.apply(count);
/*  182 */     for (int i = 0; i < count; i++) {
/*  183 */       K key = (K)keyDecoder.decode(this);
/*  184 */       V value = (V)valueDecoder.decode(this);
/*  185 */       map.put(key, value);
/*      */     } 
/*  187 */     return (M)map;
/*      */   }
/*      */   
/*      */   public <K, V> Map<K, V> readMap(StreamDecoder<? super FriendlyByteBuf, K> keyDecoder, StreamDecoder<? super FriendlyByteBuf, V> valueDecoder) {
/*  191 */     return readMap(Maps::newHashMapWithExpectedSize, keyDecoder, valueDecoder);
/*      */   }
/*      */   
/*      */   public <K, V> void writeMap(Map<K, V> map, StreamEncoder<? super FriendlyByteBuf, K> keyEncoder, StreamEncoder<? super FriendlyByteBuf, V> valueEncoder) {
/*  195 */     writeVarInt(map.size());
/*  196 */     map.forEach((k, v) -> {
/*      */           keyEncoder.encode(this, valueEncoder);
/*      */           keyEncoder.encode(this, v);
/*      */         });
/*      */   }
/*      */   
/*      */   public void readWithCount(Consumer<FriendlyByteBuf> reader) {
/*  203 */     int count = readVarInt();
/*  204 */     for (int i = 0; i < count; i++) {
/*  205 */       reader.accept(this);
/*      */     }
/*      */   }
/*      */   
/*      */   public <E extends Enum<E>> void writeEnumSet(EnumSet<E> set, Class<E> clazz) {
/*  210 */     Enum[] arrayOfEnum = (Enum[])clazz.getEnumConstants();
/*  211 */     BitSet mask = new BitSet(arrayOfEnum.length);
/*  212 */     for (int i = 0; i < arrayOfEnum.length; i++) {
/*  213 */       mask.set(i, set.contains(arrayOfEnum[i]));
/*      */     }
/*  215 */     writeFixedBitSet(mask, arrayOfEnum.length);
/*      */   }
/*      */   
/*      */   public <E extends Enum<E>> EnumSet<E> readEnumSet(Class<E> clazz) {
/*  219 */     Enum[] arrayOfEnum = (Enum[])clazz.getEnumConstants();
/*  220 */     BitSet mask = readFixedBitSet(arrayOfEnum.length);
/*  221 */     EnumSet<E> result = EnumSet.noneOf(clazz);
/*  222 */     for (int i = 0; i < arrayOfEnum.length; i++) {
/*  223 */       if (mask.get(i)) {
/*  224 */         result.add((E)arrayOfEnum[i]);
/*      */       }
/*      */     } 
/*  227 */     return result;
/*      */   }
/*      */   
/*      */   public <T> void writeOptional(Optional<T> value, StreamEncoder<? super FriendlyByteBuf, T> valueWriter) {
/*  231 */     if (value.isPresent()) {
/*  232 */       writeBoolean(true);
/*  233 */       valueWriter.encode(this, value.get());
/*      */     } else {
/*  235 */       writeBoolean(false);
/*      */     } 
/*      */   }
/*      */   
/*      */   public <T> Optional<T> readOptional(StreamDecoder<? super FriendlyByteBuf, T> valueReader) {
/*  240 */     if (readBoolean()) {
/*  241 */       return Optional.of((T)valueReader.decode(this));
/*      */     }
/*  243 */     return Optional.empty();
/*      */   }
/*      */   
/*      */   public <L, R> void writeEither(Either<L, R> value, StreamEncoder<? super FriendlyByteBuf, L> leftWriter, StreamEncoder<? super FriendlyByteBuf, R> rightWriter) {
/*  247 */     value.ifLeft(left -> {
/*      */           writeBoolean(true);
/*      */           leftWriter.encode(this, leftWriter);
/*  250 */         }).ifRight(right -> {
/*      */           writeBoolean(false);
/*      */           rightWriter.encode(this, rightWriter);
/*      */         });
/*      */   }
/*      */   
/*      */   public <L, R> Either<L, R> readEither(StreamDecoder<? super FriendlyByteBuf, L> leftReader, StreamDecoder<? super FriendlyByteBuf, R> rightReader) {
/*  257 */     if (readBoolean()) {
/*  258 */       return Either.left(leftReader.decode(this));
/*      */     }
/*  260 */     return Either.right(rightReader.decode(this));
/*      */   }
/*      */   
/*      */   public <T> T readNullable(StreamDecoder<? super FriendlyByteBuf, T> valueDecoder) {
/*  264 */     return readNullable(this, valueDecoder);
/*      */   }
/*      */   
/*      */   public static <T, B extends ByteBuf> T readNullable(B input, StreamDecoder<? super B, T> valueDecoder) {
/*  268 */     if (input.readBoolean()) {
/*  269 */       return (T)valueDecoder.decode(input);
/*      */     }
/*  271 */     return null;
/*      */   }
/*      */   
/*      */   public <T> void writeNullable(T value, StreamEncoder<? super FriendlyByteBuf, T> valueEncoder) {
/*  275 */     writeNullable(this, value, valueEncoder);
/*      */   }
/*      */   
/*      */   public static <T, B extends ByteBuf> void writeNullable(B output, T value, StreamEncoder<? super B, T> valueEncoder) {
/*  279 */     if (value != null) {
/*  280 */       output.writeBoolean(true);
/*  281 */       valueEncoder.encode(output, value);
/*      */     } else {
/*  283 */       output.writeBoolean(false);
/*      */     } 
/*      */   }
/*      */   
/*      */   public byte[] readByteArray() {
/*  288 */     return readByteArray(this);
/*      */   }
/*      */   
/*      */   public static byte[] readByteArray(ByteBuf input) {
/*  292 */     return readByteArray(input, input.readableBytes());
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeByteArray(byte[] bytes) {
/*  296 */     writeByteArray(this, bytes);
/*  297 */     return this;
/*      */   }
/*      */   
/*      */   public static void writeByteArray(ByteBuf output, byte[] bytes) {
/*  301 */     VarInt.write(output, bytes.length);
/*  302 */     output.writeBytes(bytes);
/*      */   }
/*      */   
/*      */   public byte[] readByteArray(int maxSize) {
/*  306 */     return readByteArray(this, maxSize);
/*      */   }
/*      */   
/*      */   public static byte[] readByteArray(ByteBuf input, int maxSize) {
/*  310 */     int size = VarInt.read(input);
/*  311 */     if (size > maxSize) {
/*  312 */       throw new DecoderException("ByteArray with size " + size + " is bigger than allowed " + maxSize);
/*      */     }
/*  314 */     byte[] bytes = new byte[size];
/*  315 */     input.readBytes(bytes);
/*      */     
/*  317 */     return bytes;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeVarIntArray(int[] ints) {
/*  321 */     writeVarInt(ints.length);
/*      */     
/*  323 */     for (int i : ints) {
/*  324 */       writeVarInt(i);
/*      */     }
/*      */     
/*  327 */     return this;
/*      */   }
/*      */   
/*      */   public int[] readVarIntArray() {
/*  331 */     return readVarIntArray(readableBytes());
/*      */   }
/*      */   
/*      */   public int[] readVarIntArray(int maxSize) {
/*  335 */     int size = readVarInt();
/*  336 */     if (size > maxSize) {
/*  337 */       throw new DecoderException("VarIntArray with size " + size + " is bigger than allowed " + maxSize);
/*      */     }
/*  339 */     int[] ints = new int[size];
/*      */     
/*  341 */     for (int i = 0; i < ints.length; i++) {
/*  342 */       ints[i] = readVarInt();
/*      */     }
/*      */     
/*  345 */     return ints;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeLongArray(long[] longs) {
/*  349 */     writeLongArray(this, longs);
/*  350 */     return this;
/*      */   }
/*      */   
/*      */   public static void writeLongArray(ByteBuf output, long[] longs) {
/*  354 */     VarInt.write(output, longs.length);
/*  355 */     writeFixedSizeLongArray(output, longs);
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeFixedSizeLongArray(long[] longs) {
/*  359 */     writeFixedSizeLongArray(this, longs);
/*  360 */     return this;
/*      */   }
/*      */   
/*      */   public static void writeFixedSizeLongArray(ByteBuf output, long[] longs) {
/*  364 */     for (long l : longs) {
/*  365 */       output.writeLong(l);
/*      */     }
/*      */   }
/*      */   
/*      */   public long[] readLongArray() {
/*  370 */     return readLongArray(this);
/*      */   }
/*      */   
/*      */   public long[] readFixedSizeLongArray(long[] output) {
/*  374 */     return readFixedSizeLongArray(this, output);
/*      */   }
/*      */   
/*      */   public static long[] readLongArray(ByteBuf input) {
/*  378 */     int size = VarInt.read(input);
/*  379 */     int maxSize = input.readableBytes() / 8;
/*  380 */     if (size > maxSize) {
/*  381 */       throw new DecoderException("LongArray with size " + size + " is bigger than allowed " + maxSize);
/*      */     }
/*  383 */     return readFixedSizeLongArray(input, new long[size]);
/*      */   }
/*      */   
/*      */   public static long[] readFixedSizeLongArray(ByteBuf input, long[] output) {
/*  387 */     for (int i = 0; i < output.length; i++) {
/*  388 */       output[i] = input.readLong();
/*      */     }
/*  390 */     return output;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public BlockPos readBlockPos() {
/*  398 */     return readBlockPos(this);
/*      */   }
/*      */   
/*      */   public static BlockPos readBlockPos(ByteBuf input) {
/*  402 */     return BlockPos.of(input.readLong());
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeBlockPos(BlockPos pos) {
/*  406 */     writeBlockPos(this, pos);
/*  407 */     return this;
/*      */   }
/*      */   
/*      */   public static void writeBlockPos(ByteBuf output, BlockPos pos) {
/*  411 */     output.writeLong(pos.asLong());
/*      */   }
/*      */   
/*      */   public ChunkPos readChunkPos() {
/*  415 */     return new ChunkPos(readLong());
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeChunkPos(ChunkPos pos) {
/*  419 */     writeLong(pos.toLong());
/*  420 */     return this;
/*      */   }
/*      */   
/*      */   public static ChunkPos readChunkPos(ByteBuf input) {
/*  424 */     return new ChunkPos(input.readLong());
/*      */   }
/*      */   
/*      */   public static void writeChunkPos(ByteBuf output, ChunkPos chunkPos) {
/*  428 */     output.writeLong(chunkPos.toLong());
/*      */   }
/*      */   
/*      */   public GlobalPos readGlobalPos() {
/*  432 */     ResourceKey<Level> dimension = readResourceKey(Registries.DIMENSION);
/*  433 */     BlockPos pos = readBlockPos();
/*  434 */     return GlobalPos.of(dimension, pos);
/*      */   }
/*      */   
/*      */   public void writeGlobalPos(GlobalPos globalPos) {
/*  438 */     writeResourceKey(globalPos.dimension());
/*  439 */     writeBlockPos(globalPos.pos());
/*      */   }
/*      */   
/*      */   public Vector3f readVector3f() {
/*  443 */     return readVector3f(this);
/*      */   }
/*      */   
/*      */   public static Vector3f readVector3f(ByteBuf input) {
/*  447 */     return new Vector3f(input.readFloat(), input.readFloat(), input.readFloat());
/*      */   }
/*      */   
/*      */   public void writeVector3f(Vector3f v) {
/*  451 */     writeVector3f(this, (Vector3fc)v);
/*      */   }
/*      */   
/*      */   public static void writeVector3f(ByteBuf output, Vector3fc v) {
/*  455 */     output.writeFloat(v.x());
/*  456 */     output.writeFloat(v.y());
/*  457 */     output.writeFloat(v.z());
/*      */   }
/*      */   
/*      */   public Quaternionf readQuaternion() {
/*  461 */     return readQuaternion(this);
/*      */   }
/*      */   
/*      */   public static Quaternionf readQuaternion(ByteBuf input) {
/*  465 */     return new Quaternionf(input.readFloat(), input.readFloat(), input.readFloat(), input.readFloat());
/*      */   }
/*      */   
/*      */   public void writeQuaternion(Quaternionf q) {
/*  469 */     writeQuaternion(this, (Quaternionfc)q);
/*      */   }
/*      */   
/*      */   public static void writeQuaternion(ByteBuf output, Quaternionfc value) {
/*  473 */     output.writeFloat(value.x());
/*  474 */     output.writeFloat(value.y());
/*  475 */     output.writeFloat(value.z());
/*  476 */     output.writeFloat(value.w());
/*      */   }
/*      */   
/*      */   public static Vec3 readVec3(ByteBuf input) {
/*  480 */     return new Vec3(input.readDouble(), input.readDouble(), input.readDouble());
/*      */   }
/*      */   
/*      */   public Vec3 readVec3() {
/*  484 */     return readVec3(this);
/*      */   }
/*      */   
/*      */   public static void writeVec3(ByteBuf output, Vec3 v) {
/*  488 */     output.writeDouble(v.x());
/*  489 */     output.writeDouble(v.y());
/*  490 */     output.writeDouble(v.z());
/*      */   }
/*      */   
/*      */   public void writeVec3(Vec3 v) {
/*  494 */     writeVec3(this, v);
/*      */   }
/*      */   
/*      */   public Vec3 readLpVec3() {
/*  498 */     return LpVec3.read(this);
/*      */   }
/*      */   
/*      */   public void writeLpVec3(Vec3 v) {
/*  502 */     LpVec3.write(this, v);
/*      */   }
/*      */   
/*      */   public <T extends Enum<T>> T readEnum(Class<T> clazz) {
/*  506 */     return (T)((Enum[])clazz.getEnumConstants())[readVarInt()];
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeEnum(Enum<?> value) {
/*  510 */     return writeVarInt(value.ordinal());
/*      */   }
/*      */   
/*      */   public <T> T readById(IntFunction<T> converter) {
/*  514 */     int id = readVarInt();
/*  515 */     return converter.apply(id);
/*      */   }
/*      */   
/*      */   public <T> FriendlyByteBuf writeById(ToIntFunction<T> converter, T value) {
/*  519 */     int id = converter.applyAsInt(value);
/*  520 */     return writeVarInt(id);
/*      */   }
/*      */   
/*      */   public int readVarInt() {
/*  524 */     return VarInt.read(this.source);
/*      */   }
/*      */   
/*      */   public long readVarLong() {
/*  528 */     return VarLong.read(this.source);
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeUUID(UUID uuid) {
/*  532 */     writeUUID(this, uuid);
/*  533 */     return this;
/*      */   }
/*      */   
/*      */   public static void writeUUID(ByteBuf output, UUID uuid) {
/*  537 */     output.writeLong(uuid.getMostSignificantBits());
/*  538 */     output.writeLong(uuid.getLeastSignificantBits());
/*      */   }
/*      */   
/*      */   public UUID readUUID() {
/*  542 */     return readUUID(this);
/*      */   }
/*      */   
/*      */   public static UUID readUUID(ByteBuf input) {
/*  546 */     return new UUID(input.readLong(), input.readLong());
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeVarInt(int value) {
/*  550 */     VarInt.write(this.source, value);
/*  551 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeVarLong(long value) {
/*  555 */     VarLong.write(this.source, value);
/*  556 */     return this;
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeNbt(Tag tag) {
/*  560 */     writeNbt(this, tag);
/*  561 */     return this;
/*      */   }
/*      */   public static void writeNbt(ByteBuf output, Tag tag) {
/*      */     EndTag endTag;
/*  565 */     if (tag == null) {
/*  566 */       endTag = EndTag.INSTANCE;
/*      */     }
/*      */     
/*      */     try {
/*  570 */       NbtIo.writeAnyTag((Tag)endTag, (DataOutput)new ByteBufOutputStream(output));
/*  571 */     } catch (IOException e) {
/*  572 */       throw new EncoderException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public CompoundTag readNbt() {
/*  577 */     return readNbt(this);
/*      */   }
/*      */   
/*      */   public static CompoundTag readNbt(ByteBuf input) {
/*  581 */     Tag result = readNbt(input, NbtAccounter.defaultQuota());
/*  582 */     if (result == null || result instanceof CompoundTag) {
/*  583 */       return (CompoundTag)result;
/*      */     }
/*  585 */     throw new DecoderException("Not a compound tag: " + String.valueOf(result));
/*      */   }
/*      */   
/*      */   public static Tag readNbt(ByteBuf input, NbtAccounter accounter) {
/*      */     try {
/*  590 */       Tag tag = NbtIo.readAnyTag((DataInput)new ByteBufInputStream(input), accounter);
/*  591 */       if (tag.getId() == 0) {
/*  592 */         return null;
/*      */       }
/*  594 */       return tag;
/*  595 */     } catch (IOException e) {
/*  596 */       throw new EncoderException(e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public Tag readNbt(NbtAccounter accounter) {
/*  601 */     return readNbt(this, accounter);
/*      */   }
/*      */   
/*      */   public String readUtf() {
/*  605 */     return readUtf(32767);
/*      */   }
/*      */   
/*      */   public String readUtf(int maxLength) {
/*  609 */     return Utf8String.read(this.source, maxLength);
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeUtf(String value) {
/*  613 */     return writeUtf(value, 32767);
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeUtf(String value, int maxLength) {
/*  617 */     Utf8String.write(this.source, value, maxLength);
/*  618 */     return this;
/*      */   }
/*      */   
/*      */   public Identifier readIdentifier() {
/*  622 */     return Identifier.parse(readUtf(32767));
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writeIdentifier(Identifier identifier) {
/*  626 */     writeUtf(identifier.toString());
/*  627 */     return this;
/*      */   }
/*      */   
/*      */   public <T> ResourceKey<T> readResourceKey(ResourceKey<? extends Registry<T>> registry) {
/*  631 */     Identifier id = readIdentifier();
/*  632 */     return ResourceKey.create(registry, id);
/*      */   }
/*      */   
/*      */   public void writeResourceKey(ResourceKey<?> key) {
/*  636 */     writeIdentifier(key.identifier());
/*      */   }
/*      */   
/*      */   public <T> ResourceKey<? extends Registry<T>> readRegistryKey() {
/*  640 */     Identifier id = readIdentifier();
/*  641 */     return ResourceKey.createRegistryKey(id);
/*      */   }
/*      */   
/*      */   public Instant readInstant() {
/*  645 */     return Instant.ofEpochMilli(readLong());
/*      */   }
/*      */   
/*      */   public void writeInstant(Instant value) {
/*  649 */     writeLong(value.toEpochMilli());
/*      */   }
/*      */   
/*      */   public PublicKey readPublicKey() {
/*      */     try {
/*  654 */       return Crypt.byteToPublicKey(readByteArray(512));
/*  655 */     } catch (CryptException e) {
/*  656 */       throw new DecoderException("Malformed public key bytes", e);
/*      */     } 
/*      */   }
/*      */   
/*      */   public FriendlyByteBuf writePublicKey(PublicKey publicKey) {
/*  661 */     writeByteArray(publicKey.getEncoded());
/*  662 */     return this;
/*      */   }
/*      */   
/*      */   public BlockHitResult readBlockHitResult() {
/*  666 */     BlockPos pos = readBlockPos();
/*  667 */     Direction face = readEnum(Direction.class);
/*  668 */     float clickX = readFloat();
/*  669 */     float clickY = readFloat();
/*  670 */     float clickZ = readFloat();
/*  671 */     boolean inside = readBoolean();
/*  672 */     boolean worldBorder = readBoolean();
/*      */     
/*  674 */     return new BlockHitResult(new Vec3(
/*  675 */           pos.getX() + clickX, 
/*  676 */           pos.getY() + clickY, 
/*  677 */           pos.getZ() + clickZ), face, pos, inside, worldBorder);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void writeBlockHitResult(BlockHitResult blockHit) {
/*  686 */     BlockPos blockPos = blockHit.getBlockPos();
/*  687 */     writeBlockPos(blockPos);
/*  688 */     writeEnum((Enum<?>)blockHit.getDirection());
/*  689 */     Vec3 location = blockHit.getLocation();
/*  690 */     writeFloat((float)(location.x - blockPos.getX()));
/*  691 */     writeFloat((float)(location.y - blockPos.getY()));
/*  692 */     writeFloat((float)(location.z - blockPos.getZ()));
/*  693 */     writeBoolean(blockHit.isInside());
/*  694 */     writeBoolean(blockHit.isWorldBorderHit());
/*      */   }
/*      */   
/*      */   public BitSet readBitSet() {
/*  698 */     return BitSet.valueOf(readLongArray());
/*      */   }
/*      */   
/*      */   public void writeBitSet(BitSet bitSet) {
/*  702 */     writeLongArray(bitSet.toLongArray());
/*      */   }
/*      */   
/*      */   public BitSet readFixedBitSet(int size) {
/*  706 */     byte[] bytes = new byte[Mth.positiveCeilDiv(size, 8)];
/*  707 */     readBytes(bytes);
/*  708 */     return BitSet.valueOf(bytes);
/*      */   }
/*      */   
/*      */   public void writeFixedBitSet(BitSet bitSet, int size) {
/*  712 */     if (bitSet.length() > size) {
/*  713 */       throw new EncoderException("BitSet is larger than expected size (" + bitSet.length() + ">" + size + ")");
/*      */     }
/*  715 */     byte[] bytes = bitSet.toByteArray();
/*  716 */     writeBytes(Arrays.copyOf(bytes, Mth.positiveCeilDiv(size, 8)));
/*      */   }
/*      */ 
/*      */   
/*      */   public static int readContainerId(ByteBuf input) {
/*  721 */     return VarInt.read(input);
/*      */   }
/*      */   
/*      */   public int readContainerId() {
/*  725 */     return readContainerId(this.source);
/*      */   }
/*      */   
/*      */   public static void writeContainerId(ByteBuf output, int id) {
/*  729 */     VarInt.write(output, id);
/*      */   }
/*      */   
/*      */   public void writeContainerId(int id) {
/*  733 */     writeContainerId(this.source, id);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isContiguous() {
/*  740 */     return this.source.isContiguous();
/*      */   }
/*      */ 
/*      */   
/*      */   public int maxFastWritableBytes() {
/*  745 */     return this.source.maxFastWritableBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public int capacity() {
/*  750 */     return this.source.capacity();
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf capacity(int newCapacity) {
/*  755 */     this.source.capacity(newCapacity);
/*  756 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int maxCapacity() {
/*  761 */     return this.source.maxCapacity();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBufAllocator alloc() {
/*  766 */     return this.source.alloc();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteOrder order() {
/*  771 */     return this.source.order();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf order(ByteOrder endianness) {
/*  776 */     return this.source.order(endianness);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf unwrap() {
/*  781 */     return this.source;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isDirect() {
/*  786 */     return this.source.isDirect();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadOnly() {
/*  791 */     return this.source.isReadOnly();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf asReadOnly() {
/*  796 */     return this.source.asReadOnly();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readerIndex() {
/*  801 */     return this.source.readerIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf readerIndex(int readerIndex) {
/*  806 */     this.source.readerIndex(readerIndex);
/*  807 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int writerIndex() {
/*  812 */     return this.source.writerIndex();
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writerIndex(int writerIndex) {
/*  817 */     this.source.writerIndex(writerIndex);
/*  818 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setIndex(int readerIndex, int writerIndex) {
/*  823 */     this.source.setIndex(readerIndex, writerIndex);
/*  824 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readableBytes() {
/*  829 */     return this.source.readableBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public int writableBytes() {
/*  834 */     return this.source.writableBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public int maxWritableBytes() {
/*  839 */     return this.source.maxWritableBytes();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadable() {
/*  844 */     return this.source.isReadable();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isReadable(int size) {
/*  849 */     return this.source.isReadable(size);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWritable() {
/*  854 */     return this.source.isWritable();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isWritable(int size) {
/*  859 */     return this.source.isWritable(size);
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf clear() {
/*  864 */     this.source.clear();
/*  865 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf markReaderIndex() {
/*  870 */     this.source.markReaderIndex();
/*  871 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf resetReaderIndex() {
/*  876 */     this.source.resetReaderIndex();
/*  877 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf markWriterIndex() {
/*  882 */     this.source.markWriterIndex();
/*  883 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf resetWriterIndex() {
/*  888 */     this.source.resetWriterIndex();
/*  889 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf discardReadBytes() {
/*  894 */     this.source.discardReadBytes();
/*  895 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf discardSomeReadBytes() {
/*  900 */     this.source.discardSomeReadBytes();
/*  901 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf ensureWritable(int minWritableBytes) {
/*  906 */     this.source.ensureWritable(minWritableBytes);
/*  907 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int ensureWritable(int minWritableBytes, boolean force) {
/*  912 */     return this.source.ensureWritable(minWritableBytes, force);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean getBoolean(int index) {
/*  917 */     return this.source.getBoolean(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public byte getByte(int index) {
/*  922 */     return this.source.getByte(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getUnsignedByte(int index) {
/*  927 */     return this.source.getUnsignedByte(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShort(int index) {
/*  932 */     return this.source.getShort(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public short getShortLE(int index) {
/*  937 */     return this.source.getShortLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedShort(int index) {
/*  942 */     return this.source.getUnsignedShort(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedShortLE(int index) {
/*  947 */     return this.source.getUnsignedShortLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMedium(int index) {
/*  952 */     return this.source.getMedium(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getMediumLE(int index) {
/*  957 */     return this.source.getMediumLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedMedium(int index) {
/*  962 */     return this.source.getUnsignedMedium(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getUnsignedMediumLE(int index) {
/*  967 */     return this.source.getUnsignedMediumLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getInt(int index) {
/*  972 */     return this.source.getInt(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getIntLE(int index) {
/*  977 */     return this.source.getIntLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getUnsignedInt(int index) {
/*  982 */     return this.source.getUnsignedInt(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getUnsignedIntLE(int index) {
/*  987 */     return this.source.getUnsignedIntLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLong(int index) {
/*  992 */     return this.source.getLong(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public long getLongLE(int index) {
/*  997 */     return this.source.getLongLE(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public char getChar(int index) {
/* 1002 */     return this.source.getChar(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public float getFloat(int index) {
/* 1007 */     return this.source.getFloat(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public double getDouble(int index) {
/* 1012 */     return this.source.getDouble(index);
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, ByteBuf dst) {
/* 1017 */     this.source.getBytes(index, dst);
/* 1018 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, ByteBuf dst, int length) {
/* 1023 */     this.source.getBytes(index, dst, length);
/* 1024 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length) {
/* 1029 */     this.source.getBytes(index, dst, dstIndex, length);
/* 1030 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, byte[] dst) {
/* 1035 */     this.source.getBytes(index, dst);
/* 1036 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, byte[] dst, int dstIndex, int length) {
/* 1041 */     this.source.getBytes(index, dst, dstIndex, length);
/* 1042 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, ByteBuffer dst) {
/* 1047 */     this.source.getBytes(index, dst);
/* 1048 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf getBytes(int index, OutputStream out, int length) throws IOException {
/* 1053 */     this.source.getBytes(index, out, length);
/* 1054 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBytes(int index, GatheringByteChannel out, int length) throws IOException {
/* 1059 */     return this.source.getBytes(index, out, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int getBytes(int index, FileChannel out, long position, int length) throws IOException {
/* 1064 */     return this.source.getBytes(index, out, position, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public CharSequence getCharSequence(int index, int length, Charset charset) {
/* 1069 */     return this.source.getCharSequence(index, length, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setBoolean(int index, boolean value) {
/* 1074 */     this.source.setBoolean(index, value);
/* 1075 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setByte(int index, int value) {
/* 1080 */     this.source.setByte(index, value);
/* 1081 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setShort(int index, int value) {
/* 1086 */     this.source.setShort(index, value);
/* 1087 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setShortLE(int index, int value) {
/* 1092 */     this.source.setShortLE(index, value);
/* 1093 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setMedium(int index, int value) {
/* 1098 */     this.source.setMedium(index, value);
/* 1099 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setMediumLE(int index, int value) {
/* 1104 */     this.source.setMediumLE(index, value);
/* 1105 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setInt(int index, int value) {
/* 1110 */     this.source.setInt(index, value);
/* 1111 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setIntLE(int index, int value) {
/* 1116 */     this.source.setIntLE(index, value);
/* 1117 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setLong(int index, long value) {
/* 1122 */     this.source.setLong(index, value);
/* 1123 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setLongLE(int index, long value) {
/* 1128 */     this.source.setLongLE(index, value);
/* 1129 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setChar(int index, int value) {
/* 1134 */     this.source.setChar(index, value);
/* 1135 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setFloat(int index, float value) {
/* 1140 */     this.source.setFloat(index, value);
/* 1141 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setDouble(int index, double value) {
/* 1146 */     this.source.setDouble(index, value);
/* 1147 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setBytes(int index, ByteBuf src) {
/* 1152 */     this.source.setBytes(index, src);
/* 1153 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setBytes(int index, ByteBuf src, int length) {
/* 1158 */     this.source.setBytes(index, src, length);
/* 1159 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length) {
/* 1164 */     this.source.setBytes(index, src, srcIndex, length);
/* 1165 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setBytes(int index, byte[] src) {
/* 1170 */     this.source.setBytes(index, src);
/* 1171 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setBytes(int index, byte[] src, int srcIndex, int length) {
/* 1176 */     this.source.setBytes(index, src, srcIndex, length);
/* 1177 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setBytes(int index, ByteBuffer src) {
/* 1182 */     this.source.setBytes(index, src);
/* 1183 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int index, InputStream in, int length) throws IOException {
/* 1188 */     return this.source.setBytes(index, in, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int index, ScatteringByteChannel in, int length) throws IOException {
/* 1193 */     return this.source.setBytes(index, in, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int setBytes(int index, FileChannel in, long position, int length) throws IOException {
/* 1198 */     return this.source.setBytes(index, in, position, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf setZero(int index, int length) {
/* 1203 */     this.source.setZero(index, length);
/* 1204 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int setCharSequence(int index, CharSequence sequence, Charset charset) {
/* 1209 */     return this.source.setCharSequence(index, sequence, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean readBoolean() {
/* 1214 */     return this.source.readBoolean();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte readByte() {
/* 1219 */     return this.source.readByte();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readUnsignedByte() {
/* 1224 */     return this.source.readUnsignedByte();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readShort() {
/* 1229 */     return this.source.readShort();
/*      */   }
/*      */ 
/*      */   
/*      */   public short readShortLE() {
/* 1234 */     return this.source.readShortLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedShort() {
/* 1239 */     return this.source.readUnsignedShort();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedShortLE() {
/* 1244 */     return this.source.readUnsignedShortLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readMedium() {
/* 1249 */     return this.source.readMedium();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readMediumLE() {
/* 1254 */     return this.source.readMediumLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedMedium() {
/* 1259 */     return this.source.readUnsignedMedium();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readUnsignedMediumLE() {
/* 1264 */     return this.source.readUnsignedMediumLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readInt() {
/* 1269 */     return this.source.readInt();
/*      */   }
/*      */ 
/*      */   
/*      */   public int readIntLE() {
/* 1274 */     return this.source.readIntLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readUnsignedInt() {
/* 1279 */     return this.source.readUnsignedInt();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readUnsignedIntLE() {
/* 1284 */     return this.source.readUnsignedIntLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readLong() {
/* 1289 */     return this.source.readLong();
/*      */   }
/*      */ 
/*      */   
/*      */   public long readLongLE() {
/* 1294 */     return this.source.readLongLE();
/*      */   }
/*      */ 
/*      */   
/*      */   public char readChar() {
/* 1299 */     return this.source.readChar();
/*      */   }
/*      */ 
/*      */   
/*      */   public float readFloat() {
/* 1304 */     return this.source.readFloat();
/*      */   }
/*      */ 
/*      */   
/*      */   public double readDouble() {
/* 1309 */     return this.source.readDouble();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readBytes(int length) {
/* 1314 */     return this.source.readBytes(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readSlice(int length) {
/* 1319 */     return this.source.readSlice(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf readRetainedSlice(int length) {
/* 1324 */     return this.source.readRetainedSlice(length);
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf readBytes(ByteBuf dst) {
/* 1329 */     this.source.readBytes(dst);
/* 1330 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf readBytes(ByteBuf dst, int length) {
/* 1335 */     this.source.readBytes(dst, length);
/* 1336 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf readBytes(ByteBuf dst, int dstIndex, int length) {
/* 1341 */     this.source.readBytes(dst, dstIndex, length);
/* 1342 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf readBytes(byte[] dst) {
/* 1347 */     this.source.readBytes(dst);
/* 1348 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf readBytes(byte[] dst, int dstIndex, int length) {
/* 1353 */     this.source.readBytes(dst, dstIndex, length);
/* 1354 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf readBytes(ByteBuffer dst) {
/* 1359 */     this.source.readBytes(dst);
/* 1360 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf readBytes(OutputStream out, int length) throws IOException {
/* 1365 */     this.source.readBytes(out, length);
/* 1366 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int readBytes(GatheringByteChannel out, int length) throws IOException {
/* 1371 */     return this.source.readBytes(out, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public CharSequence readCharSequence(int length, Charset charset) {
/* 1376 */     return this.source.readCharSequence(length, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public String readString(int length, Charset charset) {
/* 1381 */     return this.source.readString(length, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public int readBytes(FileChannel out, long position, int length) throws IOException {
/* 1386 */     return this.source.readBytes(out, position, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf skipBytes(int length) {
/* 1391 */     this.source.skipBytes(length);
/* 1392 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeBoolean(boolean value) {
/* 1397 */     this.source.writeBoolean(value);
/* 1398 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeByte(int value) {
/* 1403 */     this.source.writeByte(value);
/* 1404 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeShort(int value) {
/* 1409 */     this.source.writeShort(value);
/* 1410 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeShortLE(int value) {
/* 1415 */     this.source.writeShortLE(value);
/* 1416 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeMedium(int value) {
/* 1421 */     this.source.writeMedium(value);
/* 1422 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeMediumLE(int value) {
/* 1427 */     this.source.writeMediumLE(value);
/* 1428 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeInt(int value) {
/* 1433 */     this.source.writeInt(value);
/* 1434 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeIntLE(int value) {
/* 1439 */     this.source.writeIntLE(value);
/* 1440 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeLong(long value) {
/* 1445 */     this.source.writeLong(value);
/* 1446 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeLongLE(long value) {
/* 1451 */     this.source.writeLongLE(value);
/* 1452 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeChar(int value) {
/* 1457 */     this.source.writeChar(value);
/* 1458 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeFloat(float value) {
/* 1463 */     this.source.writeFloat(value);
/* 1464 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeDouble(double value) {
/* 1469 */     this.source.writeDouble(value);
/* 1470 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeBytes(ByteBuf src) {
/* 1475 */     this.source.writeBytes(src);
/* 1476 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeBytes(ByteBuf src, int length) {
/* 1481 */     this.source.writeBytes(src, length);
/* 1482 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeBytes(ByteBuf src, int srcIndex, int length) {
/* 1487 */     this.source.writeBytes(src, srcIndex, length);
/* 1488 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeBytes(byte[] src) {
/* 1493 */     this.source.writeBytes(src);
/* 1494 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeBytes(byte[] src, int srcIndex, int length) {
/* 1499 */     this.source.writeBytes(src, srcIndex, length);
/* 1500 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeBytes(ByteBuffer src) {
/* 1505 */     this.source.writeBytes(src);
/* 1506 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(InputStream in, int length) throws IOException {
/* 1511 */     return this.source.writeBytes(in, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(ScatteringByteChannel in, int length) throws IOException {
/* 1516 */     return this.source.writeBytes(in, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeBytes(FileChannel in, long position, int length) throws IOException {
/* 1521 */     return this.source.writeBytes(in, position, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf writeZero(int length) {
/* 1526 */     this.source.writeZero(length);
/* 1527 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int writeCharSequence(CharSequence sequence, Charset charset) {
/* 1532 */     return this.source.writeCharSequence(sequence, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public int indexOf(int fromIndex, int toIndex, byte value) {
/* 1537 */     return this.source.indexOf(fromIndex, toIndex, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(byte value) {
/* 1542 */     return this.source.bytesBefore(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(int length, byte value) {
/* 1547 */     return this.source.bytesBefore(length, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public int bytesBefore(int index, int length, byte value) {
/* 1552 */     return this.source.bytesBefore(index, length, value);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByte(ByteProcessor processor) {
/* 1557 */     return this.source.forEachByte(processor);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByte(int index, int length, ByteProcessor processor) {
/* 1562 */     return this.source.forEachByte(index, length, processor);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByteDesc(ByteProcessor processor) {
/* 1567 */     return this.source.forEachByteDesc(processor);
/*      */   }
/*      */ 
/*      */   
/*      */   public int forEachByteDesc(int index, int length, ByteProcessor processor) {
/* 1572 */     return this.source.forEachByteDesc(index, length, processor);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf copy() {
/* 1577 */     return this.source.copy();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf copy(int index, int length) {
/* 1582 */     return this.source.copy(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf slice() {
/* 1587 */     return this.source.slice();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedSlice() {
/* 1592 */     return this.source.retainedSlice();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf slice(int index, int length) {
/* 1597 */     return this.source.slice(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedSlice(int index, int length) {
/* 1602 */     return this.source.retainedSlice(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf duplicate() {
/* 1607 */     return this.source.duplicate();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuf retainedDuplicate() {
/* 1612 */     return this.source.retainedDuplicate();
/*      */   }
/*      */ 
/*      */   
/*      */   public int nioBufferCount() {
/* 1617 */     return this.source.nioBufferCount();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer nioBuffer() {
/* 1622 */     return this.source.nioBuffer();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer nioBuffer(int index, int length) {
/* 1627 */     return this.source.nioBuffer(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer internalNioBuffer(int index, int length) {
/* 1632 */     return this.source.internalNioBuffer(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer[] nioBuffers() {
/* 1637 */     return this.source.nioBuffers();
/*      */   }
/*      */ 
/*      */   
/*      */   public ByteBuffer[] nioBuffers(int index, int length) {
/* 1642 */     return this.source.nioBuffers(index, length);
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasArray() {
/* 1647 */     return this.source.hasArray();
/*      */   }
/*      */ 
/*      */   
/*      */   public byte[] array() {
/* 1652 */     return this.source.array();
/*      */   }
/*      */ 
/*      */   
/*      */   public int arrayOffset() {
/* 1657 */     return this.source.arrayOffset();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean hasMemoryAddress() {
/* 1662 */     return this.source.hasMemoryAddress();
/*      */   }
/*      */ 
/*      */   
/*      */   public long memoryAddress() {
/* 1667 */     return this.source.memoryAddress();
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString(Charset charset) {
/* 1672 */     return this.source.toString(charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString(int index, int length, Charset charset) {
/* 1677 */     return this.source.toString(index, length, charset);
/*      */   }
/*      */ 
/*      */   
/*      */   public int hashCode() {
/* 1682 */     return this.source.hashCode();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean equals(Object obj) {
/* 1687 */     return this.source.equals(obj);
/*      */   }
/*      */ 
/*      */   
/*      */   public int compareTo(ByteBuf buffer) {
/* 1692 */     return this.source.compareTo(buffer);
/*      */   }
/*      */ 
/*      */   
/*      */   public String toString() {
/* 1697 */     return this.source.toString();
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf retain(int increment) {
/* 1702 */     this.source.retain(increment);
/* 1703 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf retain() {
/* 1708 */     this.source.retain();
/* 1709 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf touch() {
/* 1714 */     this.source.touch();
/* 1715 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public FriendlyByteBuf touch(Object hint) {
/* 1720 */     this.source.touch(hint);
/* 1721 */     return this;
/*      */   }
/*      */ 
/*      */   
/*      */   public int refCnt() {
/* 1726 */     return this.source.refCnt();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean release() {
/* 1731 */     return this.source.release();
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean release(int decrement) {
/* 1736 */     return this.source.release(decrement);
/*      */   }
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/FriendlyByteBuf.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */