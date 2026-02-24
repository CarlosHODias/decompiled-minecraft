/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.io.DataInput;
/*     */ import java.io.DataOutput;
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import java.util.Optional;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class LongArrayTag
/*     */   implements CollectionTag
/*     */ {
/*     */   private static final int SELF_SIZE_IN_BYTES = 24;
/*     */   
/*  20 */   public static final TagType<LongArrayTag> TYPE = new TagType.VariableSize<LongArrayTag>()
/*     */     {
/*     */       public LongArrayTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  23 */         return new LongArrayTag(readAccounted(input, accounter));
/*     */       }
/*     */ 
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  28 */         return output.visit(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       private static long[] readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  32 */         accounter.accountBytes(24L);
/*  33 */         int length = input.readInt();
/*  34 */         accounter.accountBytes(8L, length);
/*  35 */         long[] data = new long[length];
/*  36 */         for (int i = 0; i < length; i++) {
/*  37 */           data[i] = input.readLong();
/*     */         }
/*  39 */         return data;
/*     */       }
/*     */ 
/*     */       
/*     */       public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/*  44 */         input.skipBytes(input.readInt() * 8);
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  49 */         return "LONG[]";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  54 */         return "TAG_Long_Array";
/*     */       }
/*     */     };
/*     */   
/*     */   private long[] data;
/*     */   
/*     */   public LongArrayTag(long[] data) {
/*  61 */     this.data = data;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(DataOutput output) throws IOException {
/*  66 */     output.writeInt(this.data.length);
/*  67 */     for (long i : this.data) {
/*  68 */       output.writeLong(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int sizeInBytes() {
/*  74 */     return 24 + 8 * this.data.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  79 */     return 12;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<LongArrayTag> getType() {
/*  84 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  89 */     StringTagVisitor visitor = new StringTagVisitor();
/*  90 */     visitor.visitLongArray(this);
/*  91 */     return visitor.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public LongArrayTag copy() {
/*  96 */     long[] cp = new long[this.data.length];
/*  97 */     System.arraycopy(this.data, 0, cp, 0, this.data.length);
/*  98 */     return new LongArrayTag(cp);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 103 */     if (this == obj) {
/* 104 */       return true;
/*     */     }
/*     */     
/* 107 */     return (obj instanceof LongArrayTag && Arrays.equals(this.data, ((LongArrayTag)obj).data));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 112 */     return Arrays.hashCode(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept(TagVisitor visitor) {
/* 117 */     visitor.visitLongArray(this);
/*     */   }
/*     */   
/*     */   public long[] getAsLongArray() {
/* 121 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 126 */     return this.data.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public LongTag get(int index) {
/* 131 */     return LongTag.valueOf(this.data[index]);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setTag(int index, Tag tag) {
/* 136 */     if (tag instanceof NumericTag) { NumericTag numeric = (NumericTag)tag;
/* 137 */       this.data[index] = numeric.longValue();
/* 138 */       return true; }
/*     */     
/* 140 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addTag(int index, Tag tag) {
/* 145 */     if (tag instanceof NumericTag) { NumericTag numeric = (NumericTag)tag;
/* 146 */       this.data = ArrayUtils.add(this.data, index, numeric.longValue());
/* 147 */       return true; }
/*     */     
/* 149 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public LongTag remove(int index) {
/* 154 */     long prev = this.data[index];
/* 155 */     this.data = ArrayUtils.remove(this.data, index);
/* 156 */     return LongTag.valueOf(prev);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 161 */     this.data = new long[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<long[]> asLongArray() {
/* 166 */     return (Optional)Optional.of(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
/* 171 */     return visitor.visit(this.data);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/LongArrayTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */