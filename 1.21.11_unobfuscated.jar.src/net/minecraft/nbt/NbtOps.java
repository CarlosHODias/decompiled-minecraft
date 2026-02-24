/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.MapLike;
/*     */ import com.mojang.serialization.RecordBuilder;
/*     */ import it.unimi.dsi.fastutil.bytes.ByteArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.longs.LongArrayList;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.stream.IntStream;
/*     */ import java.util.stream.LongStream;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public class NbtOps
/*     */   implements DynamicOps<Tag> {
/*  28 */   public static final NbtOps INSTANCE = new NbtOps();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag empty() {
/*  35 */     return EndTag.INSTANCE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag emptyList() {
/*  40 */     return new ListTag();
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag emptyMap() {
/*  45 */     return new CompoundTag();
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
/*     */   public <U> U convertTo(DynamicOps<U> outOps, Tag input) {
/*     */     // Byte code:
/*     */     //   0: aload_2
/*     */     //   1: dup
/*     */     //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   5: pop
/*     */     //   6: astore_3
/*     */     //   7: iconst_0
/*     */     //   8: istore #4
/*     */     //   10: aload_3
/*     */     //   11: iload #4
/*     */     //   13: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   18: tableswitch default -> 84, 0 -> 94, 1 -> 112, 2 -> 143, 3 -> 174, 4 -> 205, 5 -> 236, 6 -> 267, 7 -> 298, 8 -> 324, 9 -> 355, 10 -> 374, 11 -> 393, 12 -> 419
/*     */     //   84: new java/lang/MatchException
/*     */     //   87: dup
/*     */     //   88: aconst_null
/*     */     //   89: aconst_null
/*     */     //   90: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   93: athrow
/*     */     //   94: aload_3
/*     */     //   95: checkcast net/minecraft/nbt/EndTag
/*     */     //   98: astore #5
/*     */     //   100: aload_1
/*     */     //   101: invokeinterface empty : ()Ljava/lang/Object;
/*     */     //   106: checkcast java/lang/Object
/*     */     //   109: goto -> 442
/*     */     //   112: aload_3
/*     */     //   113: checkcast net/minecraft/nbt/ByteTag
/*     */     //   116: astore #6
/*     */     //   118: aload #6
/*     */     //   120: invokevirtual value : ()B
/*     */     //   123: istore #8
/*     */     //   125: iload #8
/*     */     //   127: istore #7
/*     */     //   129: aload_1
/*     */     //   130: iload #7
/*     */     //   132: invokeinterface createByte : (B)Ljava/lang/Object;
/*     */     //   137: checkcast java/lang/Object
/*     */     //   140: goto -> 442
/*     */     //   143: aload_3
/*     */     //   144: checkcast net/minecraft/nbt/ShortTag
/*     */     //   147: astore #8
/*     */     //   149: aload #8
/*     */     //   151: invokevirtual value : ()S
/*     */     //   154: istore #10
/*     */     //   156: iload #10
/*     */     //   158: istore #9
/*     */     //   160: aload_1
/*     */     //   161: iload #9
/*     */     //   163: invokeinterface createShort : (S)Ljava/lang/Object;
/*     */     //   168: checkcast java/lang/Object
/*     */     //   171: goto -> 442
/*     */     //   174: aload_3
/*     */     //   175: checkcast net/minecraft/nbt/IntTag
/*     */     //   178: astore #10
/*     */     //   180: aload #10
/*     */     //   182: invokevirtual value : ()I
/*     */     //   185: istore #12
/*     */     //   187: iload #12
/*     */     //   189: istore #11
/*     */     //   191: aload_1
/*     */     //   192: iload #11
/*     */     //   194: invokeinterface createInt : (I)Ljava/lang/Object;
/*     */     //   199: checkcast java/lang/Object
/*     */     //   202: goto -> 442
/*     */     //   205: aload_3
/*     */     //   206: checkcast net/minecraft/nbt/LongTag
/*     */     //   209: astore #12
/*     */     //   211: aload #12
/*     */     //   213: invokevirtual value : ()J
/*     */     //   216: lstore #15
/*     */     //   218: lload #15
/*     */     //   220: lstore #13
/*     */     //   222: aload_1
/*     */     //   223: lload #13
/*     */     //   225: invokeinterface createLong : (J)Ljava/lang/Object;
/*     */     //   230: checkcast java/lang/Object
/*     */     //   233: goto -> 442
/*     */     //   236: aload_3
/*     */     //   237: checkcast net/minecraft/nbt/FloatTag
/*     */     //   240: astore #15
/*     */     //   242: aload #15
/*     */     //   244: invokevirtual value : ()F
/*     */     //   247: fstore #17
/*     */     //   249: fload #17
/*     */     //   251: fstore #16
/*     */     //   253: aload_1
/*     */     //   254: fload #16
/*     */     //   256: invokeinterface createFloat : (F)Ljava/lang/Object;
/*     */     //   261: checkcast java/lang/Object
/*     */     //   264: goto -> 442
/*     */     //   267: aload_3
/*     */     //   268: checkcast net/minecraft/nbt/DoubleTag
/*     */     //   271: astore #17
/*     */     //   273: aload #17
/*     */     //   275: invokevirtual value : ()D
/*     */     //   278: dstore #20
/*     */     //   280: dload #20
/*     */     //   282: dstore #18
/*     */     //   284: aload_1
/*     */     //   285: dload #18
/*     */     //   287: invokeinterface createDouble : (D)Ljava/lang/Object;
/*     */     //   292: checkcast java/lang/Object
/*     */     //   295: goto -> 442
/*     */     //   298: aload_3
/*     */     //   299: checkcast net/minecraft/nbt/ByteArrayTag
/*     */     //   302: astore #20
/*     */     //   304: aload_1
/*     */     //   305: aload #20
/*     */     //   307: invokevirtual getAsByteArray : ()[B
/*     */     //   310: invokestatic wrap : ([B)Ljava/nio/ByteBuffer;
/*     */     //   313: invokeinterface createByteList : (Ljava/nio/ByteBuffer;)Ljava/lang/Object;
/*     */     //   318: checkcast java/lang/Object
/*     */     //   321: goto -> 442
/*     */     //   324: aload_3
/*     */     //   325: checkcast net/minecraft/nbt/StringTag
/*     */     //   328: astore #21
/*     */     //   330: aload #21
/*     */     //   332: invokevirtual value : ()Ljava/lang/String;
/*     */     //   335: astore #23
/*     */     //   337: aload #23
/*     */     //   339: astore #22
/*     */     //   341: aload_1
/*     */     //   342: aload #22
/*     */     //   344: invokeinterface createString : (Ljava/lang/String;)Ljava/lang/Object;
/*     */     //   349: checkcast java/lang/Object
/*     */     //   352: goto -> 442
/*     */     //   355: aload_3
/*     */     //   356: checkcast net/minecraft/nbt/ListTag
/*     */     //   359: astore #23
/*     */     //   361: aload_0
/*     */     //   362: aload_1
/*     */     //   363: aload #23
/*     */     //   365: invokevirtual convertList : (Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   368: checkcast java/lang/Object
/*     */     //   371: goto -> 442
/*     */     //   374: aload_3
/*     */     //   375: checkcast net/minecraft/nbt/CompoundTag
/*     */     //   378: astore #24
/*     */     //   380: aload_0
/*     */     //   381: aload_1
/*     */     //   382: aload #24
/*     */     //   384: invokevirtual convertMap : (Lcom/mojang/serialization/DynamicOps;Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   387: checkcast java/lang/Object
/*     */     //   390: goto -> 442
/*     */     //   393: aload_3
/*     */     //   394: checkcast net/minecraft/nbt/IntArrayTag
/*     */     //   397: astore #25
/*     */     //   399: aload_1
/*     */     //   400: aload #25
/*     */     //   402: invokevirtual getAsIntArray : ()[I
/*     */     //   405: invokestatic stream : ([I)Ljava/util/stream/IntStream;
/*     */     //   408: invokeinterface createIntList : (Ljava/util/stream/IntStream;)Ljava/lang/Object;
/*     */     //   413: checkcast java/lang/Object
/*     */     //   416: goto -> 442
/*     */     //   419: aload_3
/*     */     //   420: checkcast net/minecraft/nbt/LongArrayTag
/*     */     //   423: astore #26
/*     */     //   425: aload_1
/*     */     //   426: aload #26
/*     */     //   428: invokevirtual getAsLongArray : ()[J
/*     */     //   431: invokestatic stream : ([J)Ljava/util/stream/LongStream;
/*     */     //   434: invokeinterface createLongList : (Ljava/util/stream/LongStream;)Ljava/lang/Object;
/*     */     //   439: checkcast java/lang/Object
/*     */     //   442: areturn
/*     */     //   443: astore_3
/*     */     //   444: new java/lang/MatchException
/*     */     //   447: dup
/*     */     //   448: aload_3
/*     */     //   449: invokevirtual toString : ()Ljava/lang/String;
/*     */     //   452: aload_3
/*     */     //   453: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   456: athrow
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #50	-> 0
/*     */     //   #51	-> 94
/*     */     //   #52	-> 112
/*     */     //   #53	-> 143
/*     */     //   #54	-> 174
/*     */     //   #55	-> 205
/*     */     //   #56	-> 236
/*     */     //   #57	-> 267
/*     */     //   #58	-> 298
/*     */     //   #59	-> 324
/*     */     //   #60	-> 355
/*     */     //   #61	-> 374
/*     */     //   #62	-> 393
/*     */     //   #63	-> 419
/*     */     //   #50	-> 442
/*     */     //   #63	-> 443
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   100	12	5	ignored	Lnet/minecraft/nbt/EndTag;
/*     */     //   129	14	7	value	B
/*     */     //   160	14	9	value	S
/*     */     //   191	14	11	value	I
/*     */     //   222	14	13	value	J
/*     */     //   253	14	16	value	F
/*     */     //   284	14	18	value	D
/*     */     //   304	20	20	byteArrayTag	Lnet/minecraft/nbt/ByteArrayTag;
/*     */     //   341	14	22	value	Ljava/lang/String;
/*     */     //   361	13	23	listTag	Lnet/minecraft/nbt/ListTag;
/*     */     //   380	13	24	compoundTag	Lnet/minecraft/nbt/CompoundTag;
/*     */     //   399	20	25	intArrayTag	Lnet/minecraft/nbt/IntArrayTag;
/*     */     //   425	17	26	longArrayTag	Lnet/minecraft/nbt/LongArrayTag;
/*     */     //   0	457	0	this	Lnet/minecraft/nbt/NbtOps;
/*     */     //   0	457	1	outOps	Lcom/mojang/serialization/DynamicOps;
/*     */     //   0	457	2	input	Lnet/minecraft/nbt/Tag;
/*     */     // Local variable type table:
/*     */     //   start	length	slot	name	signature
/*     */     //   0	457	1	outOps	Lcom/mojang/serialization/DynamicOps<TU;>;
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   120	123	443	java/lang/Throwable
/*     */     //   151	154	443	java/lang/Throwable
/*     */     //   182	185	443	java/lang/Throwable
/*     */     //   213	216	443	java/lang/Throwable
/*     */     //   244	247	443	java/lang/Throwable
/*     */     //   275	278	443	java/lang/Throwable
/*     */     //   332	335	443	java/lang/Throwable
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
/*     */   public DataResult<Number> getNumberValue(Tag input) {
/*  69 */     return input.asNumber()
/*  70 */       .<DataResult<Number>>map(DataResult::success)
/*  71 */       .orElseGet(() -> DataResult.error(()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createNumeric(Number i) {
/*  76 */     return DoubleTag.valueOf(i.doubleValue());
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createByte(byte value) {
/*  81 */     return ByteTag.valueOf(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createShort(short value) {
/*  86 */     return ShortTag.valueOf(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createInt(int value) {
/*  91 */     return IntTag.valueOf(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createLong(long value) {
/*  96 */     return LongTag.valueOf(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createFloat(float value) {
/* 101 */     return FloatTag.valueOf(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createDouble(double value) {
/* 106 */     return DoubleTag.valueOf(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createBoolean(boolean value) {
/* 111 */     return ByteTag.valueOf(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<String> getStringValue(Tag input) {
/* 116 */     if (input instanceof StringTag) { StringTag stringTag = (StringTag)input; try { String str1 = stringTag.value(), value = str1;
/* 117 */         return DataResult.success(value); } catch (Throwable throwable) { throw new MatchException(throwable.toString(), throwable); }
/*     */        }
/* 119 */      return DataResult.error(() -> "Not a string");
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createString(String value) {
/* 124 */     return StringTag.valueOf(value);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Tag> mergeToList(Tag list, Tag value) {
/* 129 */     return createCollector(list)
/* 130 */       .<DataResult<Tag>>map(collector -> DataResult.success(collector.accept(value).result()))
/* 131 */       .orElseGet(() -> DataResult.error((), list));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Tag> mergeToList(Tag list, List<Tag> values) {
/* 136 */     return createCollector(list)
/* 137 */       .<DataResult<Tag>>map(collector -> DataResult.success(collector.acceptAll(values).result()))
/* 138 */       .orElseGet(() -> DataResult.error((), list));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Tag> mergeToMap(Tag map, Tag key, Tag value) {
/* 143 */     if (!(map instanceof CompoundTag) && !(map instanceof EndTag)) {
/* 144 */       return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(map), map);
/*     */     }
/* 146 */     if (key instanceof StringTag) { StringTag stringTag = (StringTag)key; try { String str1 = stringTag.value(), stringKey = str1;
/*     */ 
/*     */ 
/*     */         
/* 150 */         CompoundTag tag = (CompoundTag)map, output = (map instanceof CompoundTag) ? tag.shallowCopy() : new CompoundTag();
/* 151 */         output.put(stringKey, value);
/* 152 */         return DataResult.success(output); }
/*     */       catch (Throwable throwable)
/*     */       { throw new MatchException(throwable.toString(), throwable); }
/*     */        }
/*     */     
/* 157 */     return DataResult.error(() -> "key is not a string: " + String.valueOf(key), map); } public DataResult<Tag> mergeToMap(Tag map, MapLike<Tag> values) { if (!(map instanceof CompoundTag) && !(map instanceof EndTag)) {
/* 158 */       return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(map), map);
/*     */     }
/*     */     
/* 161 */     Iterator<Pair<Tag, Tag>> valuesIterator = values.entries().iterator();
/* 162 */     if (!valuesIterator.hasNext()) {
/* 163 */       if (map == empty()) {
/* 164 */         return DataResult.success(emptyMap());
/*     */       }
/* 166 */       return DataResult.success(map);
/*     */     } 
/*     */     
/* 169 */     CompoundTag tag = (CompoundTag)map, output = (map instanceof CompoundTag) ? tag.shallowCopy() : new CompoundTag();
/* 170 */     List<Tag> missed = new ArrayList<>();
/*     */     
/* 172 */     valuesIterator.forEachRemaining(entry -> {
/*     */           Tag key = (Tag)entry.getFirst();
/*     */           if (key instanceof StringTag)
/*     */           { StringTag $b$0 = (StringTag)key;
/*     */             
/*     */             try { String patt1$temp = $b$0.value(), stringKey = patt1$temp;
/*     */               output.put(stringKey, (Tag)entry.getSecond()); }
/* 179 */             catch (Throwable throwable) { throw new MatchException(throwable.toString(), throwable); }  }
/*     */           else { missed.add(key); return; }
/*     */         
/* 182 */         }); if (!missed.isEmpty()) {
/* 183 */       return DataResult.error(() -> "some keys are not strings: " + String.valueOf(missed), output);
/*     */     }
/*     */     
/* 186 */     return DataResult.success(output); }
/*     */ 
/*     */ 
/*     */   
/*     */   public DataResult<Tag> mergeToMap(Tag map, Map<Tag, Tag> values) {
/* 191 */     if (!(map instanceof CompoundTag) && !(map instanceof EndTag)) {
/* 192 */       return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(map), map);
/*     */     }
/*     */     
/* 195 */     if (values.isEmpty()) {
/* 196 */       if (map == empty()) {
/* 197 */         return DataResult.success(emptyMap());
/*     */       }
/* 199 */       return DataResult.success(map);
/*     */     } 
/*     */     
/* 202 */     CompoundTag tag = (CompoundTag)map, output = (map instanceof CompoundTag) ? tag.shallowCopy() : new CompoundTag();
/*     */     
/* 204 */     List<Tag> missed = new ArrayList<>();
/* 205 */     Iterator<Map.Entry<Tag, Tag>> iterator = values.entrySet().iterator(); while (true) { if (iterator.hasNext()) { Map.Entry<Tag, Tag> entry = iterator.next();
/* 206 */         Tag key = entry.getKey();
/* 207 */         if (key instanceof StringTag) { StringTag stringTag = (StringTag)key; try { String str1 = stringTag.value(), stringKey = str1;
/* 208 */             output.put(stringKey, entry.getValue()); } catch (Throwable throwable) { throw new MatchException(throwable.toString(), throwable); }
/*     */            continue; }
/* 210 */          missed.add(key);
/*     */         
/*     */         continue; }
/*     */       
/* 214 */       if (!missed.isEmpty()) {
/* 215 */         return DataResult.error(() -> "some keys are not strings: " + String.valueOf(missed), output);
/*     */       }
/*     */       
/* 218 */       return DataResult.success(output); }
/*     */   
/*     */   }
/*     */   
/*     */   public DataResult<Stream<Pair<Tag, Tag>>> getMapValues(Tag input) {
/* 223 */     if (input instanceof CompoundTag) { CompoundTag tag = (CompoundTag)input;
/* 224 */       return DataResult.success(tag.entrySet().stream().map(entry -> Pair.of(createString((String)entry.getKey()), entry.getValue()))); }
/*     */     
/* 226 */     return DataResult.error(() -> "Not a map: " + String.valueOf(input));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Consumer<BiConsumer<Tag, Tag>>> getMapEntries(Tag input) {
/* 231 */     if (input instanceof CompoundTag) { CompoundTag tag = (CompoundTag)input;
/* 232 */       return DataResult.success(c -> {
/*     */             for (Map.Entry<String, Tag> entry : tag.entrySet()) {
/*     */               tag.accept(createString(entry.getKey()), entry.getValue());
/*     */             }
/*     */           }); }
/*     */     
/* 238 */     return DataResult.error(() -> "Not a map: " + String.valueOf(input));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<MapLike<Tag>> getMap(Tag input) {
/* 243 */     if (input instanceof CompoundTag) { final CompoundTag tag = (CompoundTag)input;
/* 244 */       return DataResult.success(new MapLike<Tag>()
/*     */           {
/*     */             public Tag get(Tag key) {
/* 247 */               if (key instanceof StringTag) { StringTag stringTag = (StringTag)key; try { String str1 = stringTag.value(), stringKey = str1;
/* 248 */                   return tag.get(stringKey); } catch (Throwable throwable) { throw new MatchException(throwable.toString(), throwable); }
/*     */                  }
/* 250 */                throw new UnsupportedOperationException("Cannot get map entry with non-string key: " + String.valueOf(key));
/*     */             }
/*     */ 
/*     */             
/*     */             public Tag get(String key) {
/* 255 */               return tag.get(key);
/*     */             }
/*     */ 
/*     */             
/*     */             public Stream<Pair<Tag, Tag>> entries() {
/* 260 */               return tag.entrySet().stream().map(entry -> Pair.of(NbtOps.this.createString((String)entry.getKey()), entry.getValue()));
/*     */             }
/*     */ 
/*     */             
/*     */             public String toString() {
/* 265 */               return "MapLike[" + String.valueOf(tag) + "]";
/*     */             }
/*     */           }); }
/*     */     
/* 269 */     return DataResult.error(() -> "Not a map: " + String.valueOf(input));
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createMap(Stream<Pair<Tag, Tag>> map) {
/* 274 */     CompoundTag tag = new CompoundTag();
/* 275 */     map.forEach(entry -> { Tag key = (Tag)entry.getFirst(), value = (Tag)entry.getSecond(); if (key instanceof StringTag) {
/*     */             StringTag $b$0 = (StringTag)key; try {
/*     */               String patt1$temp = $b$0.value(), stringKey = patt1$temp; tag.put(stringKey, value);
/* 278 */             } catch (Throwable throwable) {
/*     */               throw new MatchException(throwable.toString(), throwable);
/*     */             } 
/*     */           } else {
/*     */             throw new UnsupportedOperationException("Cannot create map with non-string key: " + String.valueOf(key));
/*     */           } 
/* 284 */         }); return tag;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Stream<Tag>> getStream(Tag input) {
/* 289 */     if (input instanceof CollectionTag) { CollectionTag collection = (CollectionTag)input;
/* 290 */       return DataResult.success(collection.stream()); }
/*     */     
/* 292 */     return DataResult.error(() -> "Not a list");
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<Consumer<Consumer<Tag>>> getList(Tag input) {
/* 297 */     if (input instanceof CollectionTag) { CollectionTag collection = (CollectionTag)input;
/* 298 */       Objects.requireNonNull(collection); return DataResult.success(collection::forEach); }
/*     */     
/* 300 */     return DataResult.error(() -> "Not a list: " + String.valueOf(input));
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<ByteBuffer> getByteBuffer(Tag input) {
/* 305 */     if (input instanceof ByteArrayTag) { ByteArrayTag array = (ByteArrayTag)input;
/* 306 */       return DataResult.success(ByteBuffer.wrap(array.getAsByteArray())); }
/*     */     
/* 308 */     return super.getByteBuffer(input);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Tag createByteList(ByteBuffer input) {
/* 314 */     ByteBuffer wholeBuffer = input.duplicate().clear();
/*     */     
/* 316 */     byte[] bytes = new byte[input.capacity()];
/* 317 */     wholeBuffer.get(0, bytes, 0, bytes.length);
/* 318 */     return new ByteArrayTag(bytes);
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<IntStream> getIntStream(Tag input) {
/* 323 */     if (input instanceof IntArrayTag) { IntArrayTag array = (IntArrayTag)input;
/* 324 */       return DataResult.success(Arrays.stream(array.getAsIntArray())); }
/*     */     
/* 326 */     return super.getIntStream(input);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createIntList(IntStream input) {
/* 331 */     return new IntArrayTag(input.toArray());
/*     */   }
/*     */ 
/*     */   
/*     */   public DataResult<LongStream> getLongStream(Tag input) {
/* 336 */     if (input instanceof LongArrayTag) { LongArrayTag array = (LongArrayTag)input;
/* 337 */       return DataResult.success(Arrays.stream(array.getAsLongArray())); }
/*     */     
/* 339 */     return super.getLongStream(input);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createLongList(LongStream input) {
/* 344 */     return new LongArrayTag(input.toArray());
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag createList(Stream<Tag> input) {
/* 349 */     return new ListTag(input.collect(Util.toMutableList()));
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag remove(Tag input, String key) {
/* 354 */     if (input instanceof CompoundTag) { CompoundTag tag = (CompoundTag)input;
/* 355 */       CompoundTag result = tag.shallowCopy();
/* 356 */       result.remove(key);
/* 357 */       return result; }
/*     */     
/* 359 */     return input;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 364 */     return "NBT";
/*     */   }
/*     */ 
/*     */   
/*     */   public RecordBuilder<Tag> mapBuilder() {
/* 369 */     return (RecordBuilder<Tag>)new NbtRecordBuilder(this);
/*     */   }
/*     */   
/*     */   private class NbtRecordBuilder extends RecordBuilder.AbstractStringBuilder<Tag, CompoundTag> {
/*     */     protected NbtRecordBuilder(NbtOps this$0) {
/* 374 */       super(this$0);
/*     */     }
/*     */ 
/*     */     
/*     */     protected CompoundTag initBuilder() {
/* 379 */       return new CompoundTag();
/*     */     }
/*     */ 
/*     */     
/*     */     protected CompoundTag append(String key, Tag value, CompoundTag builder) {
/* 384 */       builder.put(key, value);
/* 385 */       return builder;
/*     */     }
/*     */ 
/*     */     
/*     */     protected DataResult<Tag> build(CompoundTag builder, Tag prefix) {
/* 390 */       if (prefix == null || prefix == EndTag.INSTANCE) {
/* 391 */         return DataResult.success(builder);
/*     */       }
/* 393 */       if (prefix instanceof CompoundTag) { CompoundTag compound = (CompoundTag)prefix;
/* 394 */         CompoundTag result = compound.shallowCopy();
/* 395 */         for (Map.Entry<String, Tag> entry : builder.entrySet()) {
/* 396 */           result.put(entry.getKey(), entry.getValue());
/*     */         }
/* 398 */         return DataResult.success(result); }
/*     */       
/* 400 */       return DataResult.error(() -> "mergeToMap called with not a map: " + String.valueOf(prefix), prefix);
/*     */     }
/*     */   }
/*     */   
/*     */   private static interface ListCollector {
/*     */     ListCollector accept(Tag param1Tag);
/*     */     
/*     */     default ListCollector acceptAll(Iterable<Tag> tags) {
/* 408 */       ListCollector collector = this;
/* 409 */       for (Tag tag : tags) {
/* 410 */         collector = collector.accept(tag);
/*     */       }
/* 412 */       return collector;
/*     */     }
/*     */ 
/*     */     
/*     */     default ListCollector acceptAll(Stream<Tag> tags) {
/* 417 */       Objects.requireNonNull(tags); return acceptAll(tags::iterator);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     Tag result();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Optional<ListCollector> createCollector(Tag tag) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: instanceof net/minecraft/nbt/EndTag
/*     */     //   4: ifeq -> 18
/*     */     //   7: new net/minecraft/nbt/NbtOps$GenericListCollector
/*     */     //   10: dup
/*     */     //   11: invokespecial <init> : ()V
/*     */     //   14: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */     //   17: areturn
/*     */     //   18: aload_0
/*     */     //   19: instanceof net/minecraft/nbt/CollectionTag
/*     */     //   22: ifeq -> 197
/*     */     //   25: aload_0
/*     */     //   26: checkcast net/minecraft/nbt/CollectionTag
/*     */     //   29: astore_1
/*     */     //   30: aload_1
/*     */     //   31: invokeinterface isEmpty : ()Z
/*     */     //   36: ifeq -> 50
/*     */     //   39: new net/minecraft/nbt/NbtOps$GenericListCollector
/*     */     //   42: dup
/*     */     //   43: invokespecial <init> : ()V
/*     */     //   46: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */     //   49: areturn
/*     */     //   50: aload_1
/*     */     //   51: dup
/*     */     //   52: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */     //   55: pop
/*     */     //   56: astore_2
/*     */     //   57: iconst_0
/*     */     //   58: istore_3
/*     */     //   59: aload_2
/*     */     //   60: iload_3
/*     */     //   61: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */     //   66: tableswitch default -> 96, 0 -> 106, 1 -> 127, 2 -> 151, 3 -> 175
/*     */     //   96: new java/lang/MatchException
/*     */     //   99: dup
/*     */     //   100: aconst_null
/*     */     //   101: aconst_null
/*     */     //   102: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */     //   105: athrow
/*     */     //   106: aload_2
/*     */     //   107: checkcast net/minecraft/nbt/ListTag
/*     */     //   110: astore #4
/*     */     //   112: new net/minecraft/nbt/NbtOps$GenericListCollector
/*     */     //   115: dup
/*     */     //   116: aload #4
/*     */     //   118: invokespecial <init> : (Lnet/minecraft/nbt/ListTag;)V
/*     */     //   121: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */     //   124: goto -> 196
/*     */     //   127: aload_2
/*     */     //   128: checkcast net/minecraft/nbt/ByteArrayTag
/*     */     //   131: astore #5
/*     */     //   133: new net/minecraft/nbt/NbtOps$ByteListCollector
/*     */     //   136: dup
/*     */     //   137: aload #5
/*     */     //   139: invokevirtual getAsByteArray : ()[B
/*     */     //   142: invokespecial <init> : ([B)V
/*     */     //   145: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */     //   148: goto -> 196
/*     */     //   151: aload_2
/*     */     //   152: checkcast net/minecraft/nbt/IntArrayTag
/*     */     //   155: astore #6
/*     */     //   157: new net/minecraft/nbt/NbtOps$IntListCollector
/*     */     //   160: dup
/*     */     //   161: aload #6
/*     */     //   163: invokevirtual getAsIntArray : ()[I
/*     */     //   166: invokespecial <init> : ([I)V
/*     */     //   169: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */     //   172: goto -> 196
/*     */     //   175: aload_2
/*     */     //   176: checkcast net/minecraft/nbt/LongArrayTag
/*     */     //   179: astore #7
/*     */     //   181: new net/minecraft/nbt/NbtOps$LongListCollector
/*     */     //   184: dup
/*     */     //   185: aload #7
/*     */     //   187: invokevirtual getAsLongArray : ()[J
/*     */     //   190: invokespecial <init> : ([J)V
/*     */     //   193: invokestatic of : (Ljava/lang/Object;)Ljava/util/Optional;
/*     */     //   196: areturn
/*     */     //   197: invokestatic empty : ()Ljava/util/Optional;
/*     */     //   200: areturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #424	-> 0
/*     */     //   #425	-> 7
/*     */     //   #427	-> 18
/*     */     //   #428	-> 30
/*     */     //   #429	-> 39
/*     */     //   #431	-> 50
/*     */     //   #432	-> 106
/*     */     //   #436	-> 127
/*     */     //   #437	-> 151
/*     */     //   #438	-> 175
/*     */     //   #431	-> 196
/*     */     //   #441	-> 197
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   112	15	4	list	Lnet/minecraft/nbt/ListTag;
/*     */     //   133	18	5	array	Lnet/minecraft/nbt/ByteArrayTag;
/*     */     //   157	18	6	array	Lnet/minecraft/nbt/IntArrayTag;
/*     */     //   181	15	7	array	Lnet/minecraft/nbt/LongArrayTag;
/*     */     //   30	167	1	collection	Lnet/minecraft/nbt/CollectionTag;
/*     */     //   0	201	0	tag	Lnet/minecraft/nbt/Tag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class GenericListCollector
/*     */     implements ListCollector
/*     */   {
/* 445 */     private final ListTag result = new ListTag();
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private GenericListCollector(ListTag initial) {
/* 451 */       this.result.addAll(initial);
/*     */     }
/*     */     
/*     */     public GenericListCollector(IntArrayList initials) {
/* 455 */       initials.forEach(v -> this.result.add(IntTag.valueOf(v)));
/*     */     }
/*     */     
/*     */     public GenericListCollector(ByteArrayList initials) {
/* 459 */       initials.forEach(v -> this.result.add(ByteTag.valueOf(v)));
/*     */     }
/*     */     
/*     */     public GenericListCollector(LongArrayList initials) {
/* 463 */       initials.forEach(v -> this.result.add(LongTag.valueOf(v)));
/*     */     }
/*     */ 
/*     */     
/*     */     public NbtOps.ListCollector accept(Tag tag) {
/* 468 */       this.result.add(tag);
/* 469 */       return this;
/*     */     }
/*     */ 
/*     */     
/*     */     public Tag result() {
/* 474 */       return this.result;
/*     */     }
/*     */     
/*     */     private GenericListCollector() {} }
/*     */   
/* 479 */   private static class IntListCollector implements ListCollector { private final IntArrayList values = new IntArrayList();
/*     */     
/*     */     public IntListCollector(int[] initialValues) {
/* 482 */       this.values.addElements(0, initialValues);
/*     */     }
/*     */ 
/*     */     
/*     */     public NbtOps.ListCollector accept(Tag tag) {
/* 487 */       if (tag instanceof IntTag) { IntTag intTag = (IntTag)tag;
/* 488 */         this.values.add(intTag.intValue());
/* 489 */         return this; }
/*     */       
/* 491 */       return new NbtOps.GenericListCollector(this.values).accept(tag);
/*     */     }
/*     */ 
/*     */     
/*     */     public Tag result() {
/* 496 */       return new IntArrayTag(this.values.toIntArray());
/*     */     } }
/*     */ 
/*     */   
/*     */   private static class ByteListCollector implements ListCollector {
/* 501 */     private final ByteArrayList values = new ByteArrayList();
/*     */     
/*     */     public ByteListCollector(byte[] initialValues) {
/* 504 */       this.values.addElements(0, initialValues);
/*     */     }
/*     */ 
/*     */     
/*     */     public NbtOps.ListCollector accept(Tag tag) {
/* 509 */       if (tag instanceof ByteTag) { ByteTag byteTag = (ByteTag)tag;
/* 510 */         this.values.add(byteTag.byteValue());
/* 511 */         return this; }
/*     */       
/* 513 */       return new NbtOps.GenericListCollector(this.values).accept(tag);
/*     */     }
/*     */ 
/*     */     
/*     */     public Tag result() {
/* 518 */       return new ByteArrayTag(this.values.toByteArray());
/*     */     }
/*     */   }
/*     */   
/*     */   private static class LongListCollector implements ListCollector {
/* 523 */     private final LongArrayList values = new LongArrayList();
/*     */     
/*     */     public LongListCollector(long[] initialValues) {
/* 526 */       this.values.addElements(0, initialValues);
/*     */     }
/*     */ 
/*     */     
/*     */     public NbtOps.ListCollector accept(Tag tag) {
/* 531 */       if (tag instanceof LongTag) { LongTag longTag = (LongTag)tag;
/* 532 */         this.values.add(longTag.longValue());
/* 533 */         return this; }
/*     */       
/* 535 */       return new NbtOps.GenericListCollector(this.values).accept(tag);
/*     */     }
/*     */ 
/*     */     
/*     */     public Tag result() {
/* 540 */       return new LongArrayTag(this.values.toLongArray());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/NbtOps.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */