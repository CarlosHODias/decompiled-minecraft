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
/*     */ public final class IntArrayTag
/*     */   implements CollectionTag
/*     */ {
/*     */   private static final int SELF_SIZE_IN_BYTES = 24;
/*     */   
/*  20 */   public static final TagType<IntArrayTag> TYPE = new TagType.VariableSize<IntArrayTag>()
/*     */     {
/*     */       public IntArrayTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  23 */         return new IntArrayTag(readAccounted(input, accounter));
/*     */       }
/*     */ 
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  28 */         return output.visit(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       private static int[] readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  32 */         accounter.accountBytes(24L);
/*     */         
/*  34 */         int length = input.readInt();
/*  35 */         accounter.accountBytes(4L, length);
/*  36 */         int[] data = new int[length];
/*  37 */         for (int i = 0; i < length; i++) {
/*  38 */           data[i] = input.readInt();
/*     */         }
/*  40 */         return data;
/*     */       }
/*     */ 
/*     */       
/*     */       public void skip(DataInput input, NbtAccounter accounter) throws IOException {
/*  45 */         input.skipBytes(input.readInt() * 4);
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  50 */         return "INT[]";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  55 */         return "TAG_Int_Array";
/*     */       }
/*     */     };
/*     */   
/*     */   private int[] data;
/*     */   
/*     */   public IntArrayTag(int[] data) {
/*  62 */     this.data = data;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(DataOutput output) throws IOException {
/*  67 */     output.writeInt(this.data.length);
/*  68 */     for (int i : this.data) {
/*  69 */       output.writeInt(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int sizeInBytes() {
/*  75 */     return 24 + 4 * this.data.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  80 */     return 11;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<IntArrayTag> getType() {
/*  85 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  90 */     StringTagVisitor visitor = new StringTagVisitor();
/*  91 */     visitor.visitIntArray(this);
/*  92 */     return visitor.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public IntArrayTag copy() {
/*  97 */     int[] cp = new int[this.data.length];
/*  98 */     System.arraycopy(this.data, 0, cp, 0, this.data.length);
/*  99 */     return new IntArrayTag(cp);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 104 */     if (this == obj) {
/* 105 */       return true;
/*     */     }
/*     */     
/* 108 */     return (obj instanceof IntArrayTag && Arrays.equals(this.data, ((IntArrayTag)obj).data));
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 113 */     return Arrays.hashCode(this.data);
/*     */   }
/*     */   
/*     */   public int[] getAsIntArray() {
/* 117 */     return this.data;
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept(TagVisitor visitor) {
/* 122 */     visitor.visitIntArray(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 127 */     return this.data.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public IntTag get(int index) {
/* 132 */     return IntTag.valueOf(this.data[index]);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setTag(int index, Tag tag) {
/* 137 */     if (tag instanceof NumericTag) { NumericTag numeric = (NumericTag)tag;
/* 138 */       this.data[index] = numeric.intValue();
/* 139 */       return true; }
/*     */     
/* 141 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addTag(int index, Tag tag) {
/* 146 */     if (tag instanceof NumericTag) { NumericTag numeric = (NumericTag)tag;
/* 147 */       this.data = ArrayUtils.add(this.data, index, numeric.intValue());
/* 148 */       return true; }
/*     */     
/* 150 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public IntTag remove(int index) {
/* 155 */     int prev = this.data[index];
/* 156 */     this.data = ArrayUtils.remove(this.data, index);
/* 157 */     return IntTag.valueOf(prev);
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 162 */     this.data = new int[0];
/*     */   }
/*     */ 
/*     */   
/*     */   public Optional<int[]> asIntArray() {
/* 167 */     return (Optional)Optional.of(this.data);
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
/* 172 */     return visitor.visit(this.data);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/IntArrayTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */