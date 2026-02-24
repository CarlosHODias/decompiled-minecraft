/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.AbstractList;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class ListTag
/*     */   extends AbstractList<Tag>
/*     */   implements CollectionTag
/*     */ {
/*     */   private static final String WRAPPER_MARKER = "";
/*     */   private static final int SELF_SIZE_IN_BYTES = 36;
/*     */   
/*  29 */   public static final TagType<ListTag> TYPE = new TagType.VariableSize<ListTag>()
/*     */     {
/*     */       public ListTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  32 */         accounter.pushDepth();
/*     */         try {
/*  34 */           return loadList(input, accounter);
/*     */         } finally {
/*  36 */           accounter.popDepth();
/*     */         } 
/*     */       }
/*     */       
/*     */       private static ListTag loadList(DataInput input, NbtAccounter accounter) throws IOException {
/*  41 */         accounter.accountBytes(36L);
/*  42 */         byte typeId = input.readByte();
/*  43 */         int count = readListCount(input);
/*  44 */         if (typeId == 0 && count > 0) {
/*  45 */           throw new NbtFormatException("Missing type on ListTag");
/*     */         }
/*  47 */         accounter.accountBytes(4L, count);
/*  48 */         TagType<?> type = TagTypes.getType(typeId);
/*  49 */         ListTag list = new ListTag(new ArrayList<>(count));
/*  50 */         for (int i = 0; i < count; i++) {
/*  51 */           list.addAndUnwrap((Tag)type.load(input, accounter));
/*     */         }
/*  53 */         return list;
/*     */       }
/*     */ 
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  58 */         accounter.pushDepth();
/*     */         try {
/*  60 */           return parseList(input, output, accounter);
/*     */         } finally {
/*  62 */           accounter.popDepth();
/*     */         } 
/*     */       }
/*     */       
/*     */       private static StreamTagVisitor.ValueResult parseList(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  67 */         accounter.accountBytes(36L);
/*  68 */         TagType<?> elementType = TagTypes.getType(input.readByte());
/*  69 */         int count = readListCount(input);
/*  70 */         switch (output.visitList(elementType, count)) {
/*     */           case HALT:
/*  72 */             return StreamTagVisitor.ValueResult.HALT;
/*     */           case BREAK:
/*  74 */             elementType.skip(input, count, accounter);
/*  75 */             return output.visitContainerEnd();
/*     */         } 
/*     */         
/*  78 */         accounter.accountBytes(4L, count);
/*     */         
/*     */         int i;
/*  81 */         for (i = 0; i < count; i++) {
/*  82 */           switch (output.visitElement(elementType, i)) {
/*     */             case HALT:
/*  84 */               return StreamTagVisitor.ValueResult.HALT;
/*     */             case BREAK:
/*  86 */               elementType.skip(input, accounter);
/*     */               break;
/*     */             case SKIP:
/*  89 */               elementType.skip(input, accounter);
/*     */               break;
/*     */             
/*     */             default:
/*  93 */               switch (elementType.parse(input, output, accounter)) {
/*     */                 case HALT:
/*  95 */                   return StreamTagVisitor.ValueResult.HALT;
/*     */                 case BREAK:
/*     */                   break;
/*     */               }  break;
/*     */           } 
/* 100 */         }  int amountToSkip = count - 1 - i;
/* 101 */         if (amountToSkip > 0) {
/* 102 */           elementType.skip(input, amountToSkip, accounter);
/*     */         }
/* 104 */         return output.visitContainerEnd();
/*     */       }
/*     */       
/*     */       private static int readListCount(DataInput input) throws IOException {
/* 108 */         int count = input.readInt();
/* 109 */         if (count < 0) {
/* 110 */           throw new NbtFormatException("ListTag length cannot be negative: " + count);
/*     */         }
/* 112 */         return count;
/*     */       }
/*     */ 
/*     */       
/*     */       public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/* 117 */         accounter.pushDepth();
/*     */         try {
/* 119 */           TagType<?> type = TagTypes.getType(input.readByte());
/* 120 */           int count = input.readInt();
/* 121 */           type.skip(input, count, accounter);
/*     */         } finally {
/* 123 */           accounter.popDepth();
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/* 129 */         return "LIST";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/* 134 */         return "TAG_List";
/*     */       }
/*     */     };
/*     */   
/*     */   private final List<Tag> list;
/*     */   
/*     */   public ListTag() {
/* 141 */     this(new ArrayList<>());
/*     */   }
/*     */   
/*     */   ListTag(List<Tag> list) {
/* 145 */     this.list = list;
/*     */   }
/*     */   
/*     */   private static Tag tryUnwrap(CompoundTag tag) {
/* 149 */     if (tag.size() == 1) {
/* 150 */       Tag value = tag.get("");
/* 151 */       if (value != null) {
/* 152 */         return value;
/*     */       }
/*     */     } 
/* 155 */     return tag;
/*     */   }
/*     */   
/*     */   private static boolean isWrapper(CompoundTag tag) {
/* 159 */     return (tag.size() == 1 && tag.contains(""));
/*     */   }
/*     */ 
/*     */   
/*     */   private static Tag wrapIfNeeded(byte elementType, Tag tag) {
/* 164 */     if (elementType != 10) {
/* 165 */       return tag;
/*     */     }
/* 167 */     if (tag instanceof CompoundTag) { CompoundTag compoundTag = (CompoundTag)tag; if (!isWrapper(compoundTag))
/* 168 */         return compoundTag;  }
/*     */     
/* 170 */     return wrapElement(tag);
/*     */   }
/*     */ 
/*     */   
/*     */   private static CompoundTag wrapElement(Tag tag) {
/* 175 */     return new CompoundTag(Map.of("", tag));
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(DataOutput output) throws IOException {
/* 180 */     byte elementType = identifyRawElementType();
/* 181 */     output.writeByte(elementType);
/* 182 */     output.writeInt(this.list.size());
/* 183 */     for (Tag element : this.list) {
/* 184 */       wrapIfNeeded(elementType, element).write(output);
/*     */     }
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   byte identifyRawElementType() {
/* 190 */     byte homogenousType = 0;
/* 191 */     for (Tag element : this.list) {
/* 192 */       byte elementType = element.getId();
/* 193 */       if (homogenousType == 0) {
/* 194 */         homogenousType = elementType; continue;
/* 195 */       }  if (homogenousType != elementType)
/*     */       {
/* 197 */         return 10;
/*     */       }
/*     */     } 
/* 200 */     return homogenousType;
/*     */   }
/*     */   
/*     */   public void addAndUnwrap(Tag tag) {
/* 204 */     if (tag instanceof CompoundTag) { CompoundTag compound = (CompoundTag)tag;
/* 205 */       add(tryUnwrap(compound)); }
/*     */     else
/* 207 */     { add(tag); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public int sizeInBytes() {
/* 213 */     int size = 36;
/* 214 */     size += 4 * this.list.size();
/* 215 */     for (Tag child : this.list) {
/* 216 */       size += child.sizeInBytes();
/*     */     }
/* 218 */     return size;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/* 223 */     return 9;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<ListTag> getType() {
/* 228 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 233 */     StringTagVisitor visitor = new StringTagVisitor();
/* 234 */     visitor.visitList(this);
/* 235 */     return visitor.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag remove(int index) {
/* 240 */     return this.list.remove(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 245 */     return this.list.isEmpty();
/*     */   }
/*     */   
/*     */   public Optional<CompoundTag> getCompound(int index) {
/* 249 */     Tag tag = getNullable(index); if (tag instanceof CompoundTag) { CompoundTag compoundTag = (CompoundTag)tag;
/* 250 */       return Optional.of(compoundTag); }
/*     */     
/* 252 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public CompoundTag getCompoundOrEmpty(int index) {
/* 256 */     return getCompound(index).orElseGet(CompoundTag::new);
/*     */   }
/*     */   
/*     */   public Optional<ListTag> getList(int index) {
/* 260 */     Tag tag = getNullable(index); if (tag instanceof ListTag) { ListTag listTag = (ListTag)tag;
/* 261 */       return Optional.of(listTag); }
/*     */     
/* 263 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public ListTag getListOrEmpty(int index) {
/* 267 */     return getList(index).orElseGet(ListTag::new);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Short> getShort(int index) {
/* 272 */     return getOptional(index).flatMap(Tag::asShort);
/*     */   }
/*     */   
/*     */   public short getShortOr(int index, short defaultValue) {
/* 276 */     Tag tag = getNullable(index); if (tag instanceof NumericTag) { NumericTag numericTag = (NumericTag)tag;
/* 277 */       return numericTag.shortValue(); }
/*     */     
/* 279 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Integer> getInt(int index) {
/* 284 */     return getOptional(index).flatMap(Tag::asInt);
/*     */   }
/*     */   
/*     */   public int getIntOr(int index, int defaultValue) {
/* 288 */     Tag tag = getNullable(index); if (tag instanceof NumericTag) { NumericTag numericTag = (NumericTag)tag;
/* 289 */       return numericTag.intValue(); }
/*     */     
/* 291 */     return defaultValue;
/*     */   }
/*     */   
/*     */   public Optional<int[]> getIntArray(int index) {
/* 295 */     Tag tag = getNullable(index); if (tag instanceof IntArrayTag) { IntArrayTag intArrayTag = (IntArrayTag)tag;
/* 296 */       return (Optional)Optional.of(intArrayTag.getAsIntArray()); }
/*     */     
/* 298 */     return (Optional)Optional.empty();
/*     */   }
/*     */   
/*     */   public Optional<long[]> getLongArray(int index) {
/* 302 */     Tag tag = getNullable(index); if (tag instanceof LongArrayTag) { LongArrayTag longArrayTag = (LongArrayTag)tag;
/* 303 */       return (Optional)Optional.of(longArrayTag.getAsLongArray()); }
/*     */     
/* 305 */     return (Optional)Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Double> getDouble(int index) {
/* 310 */     return getOptional(index).flatMap(Tag::asDouble);
/*     */   }
/*     */   
/*     */   public double getDoubleOr(int index, double defaultValue) {
/* 314 */     Tag tag = getNullable(index); if (tag instanceof NumericTag) { NumericTag numericTag = (NumericTag)tag;
/* 315 */       return numericTag.doubleValue(); }
/*     */     
/* 317 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<Float> getFloat(int index) {
/* 322 */     return getOptional(index).flatMap(Tag::asFloat);
/*     */   }
/*     */   
/*     */   public float getFloatOr(int index, float defaultValue) {
/* 326 */     Tag tag = getNullable(index); if (tag instanceof NumericTag) { NumericTag numericTag = (NumericTag)tag;
/* 327 */       return numericTag.floatValue(); }
/*     */     
/* 329 */     return defaultValue;
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<String> getString(int index) {
/* 334 */     return getOptional(index).flatMap(Tag::asString);
/*     */   }
/*     */   
/*     */   public String getStringOr(int index, String defaultValue) {
/* 338 */     Tag tag = getNullable(index);
/* 339 */     if (tag instanceof StringTag) { StringTag stringTag = (StringTag)tag; try { String str1 = stringTag.value(), value = str1;
/* 340 */         return value; } catch (Throwable throwable) { throw new MatchException(throwable.toString(), throwable); }
/*     */        }
/* 342 */      return defaultValue;
/*     */   }
/*     */   
/*     */   private Tag getNullable(int index) {
/* 346 */     return (index >= 0 && index < this.list.size()) ? this.list.get(index) : null;
/*     */   }
/*     */   
/*     */   private Optional<Tag> getOptional(int index) {
/* 350 */     return Optional.ofNullable(getNullable(index));
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 355 */     return this.list.size();
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag get(int index) {
/* 360 */     return this.list.get(index);
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag set(int index, Tag tag) {
/* 365 */     return this.list.set(index, tag);
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int index, Tag tag) {
/* 370 */     this.list.add(index, tag);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setTag(int index, Tag tag) {
/* 375 */     this.list.set(index, tag);
/* 376 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addTag(int index, Tag tag) {
/* 381 */     this.list.add(index, tag);
/* 382 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public ListTag copy() {
/* 387 */     List<Tag> copy = new ArrayList<>(this.list.size());
/* 388 */     for (Tag tag : this.list) {
/* 389 */       copy.add(tag.copy());
/*     */     }
/* 391 */     return new ListTag(copy);
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<ListTag> asList() {
/* 396 */     return Optional.of(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 401 */     if (this == obj) {
/* 402 */       return true;
/*     */     }
/*     */     
/* 405 */     return (obj instanceof ListTag && Objects.equals(this.list, ((ListTag)obj).list));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 410 */     return this.list.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<Tag> stream() {
/* 415 */     return super.stream();
/*     */   }
/*     */   
/*     */   public Stream<CompoundTag> compoundStream() {
/* 419 */     return stream().mapMulti((tag, output) -> {
/*     */           if (tag instanceof CompoundTag) {
/*     */             CompoundTag compound = (CompoundTag)tag;
/*     */             output.accept(compound);
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   public void accept(TagVisitor visitor) {
/* 428 */     visitor.visitList(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 433 */     this.list.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
/* 438 */     byte elementType = identifyRawElementType();
/* 439 */     switch (visitor.visitList(TagTypes.getType(elementType), this.list.size())) {
/*     */       case HALT:
/* 441 */         return StreamTagVisitor.ValueResult.HALT;
/*     */       case BREAK:
/* 443 */         return visitor.visitContainerEnd();
/*     */     } 
/* 445 */     for (int i = 0; i < this.list.size(); i++) {
/* 446 */       Tag tag = wrapIfNeeded(elementType, this.list.get(i));
/* 447 */       switch (visitor.visitElement(tag.getType(), i)) {
/*     */         case HALT:
/* 449 */           return StreamTagVisitor.ValueResult.HALT;
/*     */         case SKIP:
/*     */           break;
/*     */         case BREAK:
/* 453 */           return visitor.visitContainerEnd();
/*     */         default:
/* 455 */           switch (tag.accept(visitor)) {
/*     */             case HALT:
/* 457 */               return StreamTagVisitor.ValueResult.HALT;
/*     */             case BREAK:
/* 459 */               return visitor.visitContainerEnd();
/*     */           }  break;
/*     */       } 
/* 462 */     }  return visitor.visitContainerEnd();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/ListTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */