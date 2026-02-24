/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.ListBuilder;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class NullOps
/*     */   implements DynamicOps<Unit>
/*     */ {
/*  27 */   public static final NullOps INSTANCE = new NullOps();
/*     */   
/*  29 */   private static final MapLike<Unit> EMPTY_MAP = new MapLike<Unit>()
/*     */     {
/*     */       public Unit get(Unit key) {
/*  32 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public Unit get(String key) {
/*  37 */         return null;
/*     */       }
/*     */ 
/*     */       
/*     */       public Stream<Pair<Unit, Unit>> entries() {
/*  42 */         return Stream.empty();
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public <U> U convertTo(DynamicOps<U> outOps, Unit input) {
/*  51 */     return (U)outOps.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit empty() {
/*  56 */     return Unit.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit emptyMap() {
/*  61 */     return Unit.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit emptyList() {
/*  66 */     return Unit.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit createNumeric(Number value) {
/*  71 */     return Unit.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit createByte(byte value) {
/*  76 */     return Unit.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit createShort(short value) {
/*  81 */     return Unit.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit createInt(int value) {
/*  86 */     return Unit.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit createLong(long value) {
/*  91 */     return Unit.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit createFloat(float value) {
/*  96 */     return Unit.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit createDouble(double value) {
/* 101 */     return Unit.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit createBoolean(boolean value) {
/* 106 */     return Unit.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit createString(String value) {
/* 111 */     return Unit.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Number> getNumberValue(Unit input) {
/* 116 */     return DataResult.success(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Boolean> getBooleanValue(Unit input) {
/* 121 */     return DataResult.success(false);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<String> getStringValue(Unit input) {
/* 126 */     return DataResult.success("");
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Unit> mergeToList(Unit input, Unit value) {
/* 131 */     return DataResult.success(Unit.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Unit> mergeToList(Unit input, List<Unit> values) {
/* 136 */     return DataResult.success(Unit.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Unit> mergeToMap(Unit input, Unit key, Unit value) {
/* 141 */     return DataResult.success(Unit.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Unit> mergeToMap(Unit input, Map<Unit, Unit> values) {
/* 146 */     return DataResult.success(Unit.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Unit> mergeToMap(Unit input, MapLike<Unit> values) {
/* 151 */     return DataResult.success(Unit.INSTANCE);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<Pair<Unit, Unit>>> getMapValues(Unit input) {
/* 156 */     return DataResult.success(Stream.empty());
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Consumer<BiConsumer<Unit, Unit>>> getMapEntries(Unit input) {
/* 161 */     return DataResult.success(consumer -> {
/*     */         
/*     */         });
/*     */   }
/*     */   public DataResult<MapLike<Unit>> getMap(Unit input) {
/* 166 */     return DataResult.success(EMPTY_MAP);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<Unit>> getStream(Unit input) {
/* 171 */     return DataResult.success(Stream.empty());
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Consumer<Consumer<Unit>>> getList(Unit input) {
/* 176 */     return DataResult.success(consumer -> {
/*     */         
/*     */         });
/*     */   }
/*     */   public DataResult<ByteBuffer> getByteBuffer(Unit input) {
/* 181 */     return DataResult.success(ByteBuffer.wrap(new byte[0]));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<IntStream> getIntStream(Unit input) {
/* 186 */     return DataResult.success(IntStream.empty());
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<LongStream> getLongStream(Unit input) {
/* 191 */     return DataResult.success(LongStream.empty());
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit createMap(Stream<Pair<Unit, Unit>> map) {
/* 196 */     return Unit.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit createMap(Map<Unit, Unit> map) {
/* 201 */     return Unit.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit createList(Stream<Unit> input) {
/* 206 */     return Unit.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit createByteList(ByteBuffer input) {
/* 211 */     return Unit.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit createIntList(IntStream input) {
/* 216 */     return Unit.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit createLongList(LongStream input) {
/* 221 */     return Unit.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Unit remove(Unit input, String key) {
/* 226 */     return input;
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<Unit> mapBuilder() {
/* 231 */     return (RecordBuilder<Unit>)new NullMapBuilder(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListBuilder<Unit> listBuilder() {
/* 236 */     return new NullListBuilder(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 241 */     return "Null";
/*     */   }
/*     */   
/*     */   private static final class NullMapBuilder extends RecordBuilder.AbstractUniversalBuilder<Unit, Unit> {
/*     */     public NullMapBuilder(DynamicOps<Unit> ops) {
/* 246 */       super(ops);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Unit initBuilder() {
/* 251 */       return Unit.INSTANCE;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Unit append(Unit key, Unit value, Unit builder) {
/* 256 */       return builder;
/*     */     }
/*     */ 
/*     */     
/*     */     protected DataResult<Unit> build(Unit builder, Unit prefix) {
/* 261 */       return DataResult.success(prefix);
/*     */     }
/*     */   }
/*     */   
/*     */   private static final class NullListBuilder extends AbstractListBuilder<Unit, Unit> {
/*     */     public NullListBuilder(DynamicOps<Unit> ops) {
/* 267 */       super(ops);
/*     */     }
/*     */ 
/*     */     
/*     */     protected Unit initBuilder() {
/* 272 */       return Unit.INSTANCE;
/*     */     }
/*     */ 
/*     */     
/*     */     protected Unit append(Unit builder, Unit value) {
/* 277 */       return builder;
/*     */     }
/*     */ 
/*     */     
/*     */     protected DataResult<Unit> build(Unit builder, Unit prefix) {
/* 282 */       return DataResult.success(builder);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/NullOps.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */