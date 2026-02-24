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
/*     */ public final class ByteArrayTag
/*     */   implements CollectionTag
/*     */ {
/*     */   private static final int SELF_SIZE_IN_BYTES = 24;
/*     */   
/*  20 */   public static final TagType<ByteArrayTag> TYPE = new TagType.VariableSize<ByteArrayTag>()
/*     */     {
/*     */       public ByteArrayTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  23 */         return new ByteArrayTag(readAccounted(input, accounter));
/*     */       }
/*     */ 
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  28 */         return output.visit(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       private static byte[] readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  32 */         accounter.accountBytes(24L);
/*  33 */         int length = input.readInt();
/*  34 */         accounter.accountBytes(1L, length);
/*  35 */         byte[] data = new byte[length];
/*  36 */         input.readFully(data);
/*  37 */         return data;
/*     */       }
/*     */ 
/*     */       
/*     */       public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/*  42 */         input.skipBytes(input.readInt() * 1);
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  47 */         return "BYTE[]";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  52 */         return "TAG_Byte_Array";
/*     */       }
/*     */     };
/*     */   
/*     */   private byte[] data;
/*     */   
/*     */   public ByteArrayTag(byte[] data) {
/*  59 */     this.data = data;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(DataOutput output) throws IOException {
/*  64 */     output.writeInt(this.data.length);
/*  65 */     output.write(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public int sizeInBytes() {
/*  70 */     return 24 + 1 * this.data.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  75 */     return 7;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<ByteArrayTag> getType() {
/*  80 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  85 */     StringTagVisitor visitor = new StringTagVisitor();
/*  86 */     visitor.visitByteArray(this);
/*  87 */     return visitor.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public Tag copy() {
/*  92 */     byte[] cp = new byte[this.data.length];
/*  93 */     System.arraycopy(this.data, 0, cp, 0, this.data.length);
/*  94 */     return new ByteArrayTag(cp);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/*  99 */     if (this == obj) {
/* 100 */       return true;
/*     */     }
/*     */     
/* 103 */     return (obj instanceof ByteArrayTag && Arrays.equals(this.data, ((ByteArrayTag)obj).data));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 108 */     return Arrays.hashCode(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept(TagVisitor visitor) {
/* 113 */     visitor.visitByteArray(this);
/*     */   }
/*     */   
/*     */   public byte[] getAsByteArray() {
/* 117 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 122 */     return this.data.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteTag get(int index) {
/* 127 */     return ByteTag.valueOf(this.data[index]);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setTag(int index, Tag tag) {
/* 132 */     if (tag instanceof NumericTag) { NumericTag numeric = (NumericTag)tag;
/* 133 */       this.data[index] = numeric.byteValue();
/* 134 */       return true; }
/*     */     
/* 136 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addTag(int index, Tag tag) {
/* 141 */     if (tag instanceof NumericTag) { NumericTag numeric = (NumericTag)tag;
/* 142 */       this.data = ArrayUtils.add(this.data, index, numeric.byteValue());
/* 143 */       return true; }
/*     */     
/* 145 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteTag remove(int index) {
/* 150 */     byte prev = this.data[index];
/* 151 */     this.data = ArrayUtils.remove(this.data, index);
/* 152 */     return ByteTag.valueOf(prev);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 157 */     this.data = new byte[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<byte[]> asByteArray() {
/* 162 */     return (Optional)Optional.of(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
/* 167 */     return visitor.visit(this.data);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/ByteArrayTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */