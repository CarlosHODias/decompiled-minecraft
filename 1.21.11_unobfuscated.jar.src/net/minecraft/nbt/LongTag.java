/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ public final class LongTag extends Record implements NumericTag {
/*     */   private final long value;
/*     */   
/*     */   public long value() {
/*   7 */     return this.value;
/*     */   }
/*     */   private static final int SELF_SIZE_IN_BYTES = 16;
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/LongTag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/LongTag;
/*     */   }
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/LongTag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/LongTag;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   private static class Cache { private static final int HIGH = 1024;
/*     */     private static final int LOW = -128;
/*  18 */     static final LongTag[] cache = new LongTag[1153];
/*     */     
/*     */     static {
/*  21 */       for (int i = 0; i < cache.length; i++) {
/*  22 */         cache[i] = new LongTag((-128 + i));
/*     */       }
/*     */     } }
/*     */ 
/*     */   
/*  27 */   public static final TagType<LongTag> TYPE = new TagType.StaticSize<LongTag>()
/*     */     {
/*     */       public LongTag load(java.io.DataInput input, NbtAccounter accounter) throws java.io.IOException {
/*  30 */         return LongTag.valueOf(readAccounted(input, accounter));
/*     */       }
/*     */ 
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(java.io.DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws java.io.IOException {
/*  35 */         return output.visit(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       private static long readAccounted(java.io.DataInput input, NbtAccounter accounter) throws java.io.IOException {
/*  39 */         accounter.accountBytes(16L);
/*  40 */         return input.readLong();
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/*  45 */         return 8;
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  50 */         return "LONG";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  55 */         return "TAG_Long";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*     */   public LongTag(long value) {
/*  63 */     this.value = value;
/*     */   }
/*     */   
/*     */   public static LongTag valueOf(long i) {
/*  67 */     if (i >= -128L && i <= 1024L) {
/*  68 */       return Cache.cache[(int)i - -128];
/*     */     }
/*  70 */     return new LongTag(i);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(java.io.DataOutput output) throws java.io.IOException {
/*  75 */     output.writeLong(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int sizeInBytes() {
/*  80 */     return 16;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  85 */     return 4;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<LongTag> getType() {
/*  90 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public LongTag copy() {
/*  95 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept(TagVisitor visitor) {
/* 100 */     visitor.visitLong(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 105 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 110 */     return (int)(this.value & 0xFFFFFFFFFFFFFFFFL);
/*     */   }
/*     */ 
/*     */   
/*     */   public short shortValue() {
/* 115 */     return (short)(int)(this.value & 0xFFFFL);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte byteValue() {
/* 120 */     return (byte)(int)(this.value & 0xFFL);
/*     */   }
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 125 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 130 */     return (float)this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public Number box() {
/* 135 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
/* 140 */     return visitor.visit(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 145 */     StringTagVisitor visitor = new StringTagVisitor();
/* 146 */     visitor.visitLong(this);
/* 147 */     return visitor.build();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/LongTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */