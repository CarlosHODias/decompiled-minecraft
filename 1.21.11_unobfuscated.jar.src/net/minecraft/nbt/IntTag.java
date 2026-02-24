/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ public final class IntTag extends Record implements NumericTag {
/*     */   private final int value;
/*     */   
/*     */   public int value() {
/*   7 */     return this.value;
/*     */   }
/*     */   private static final int SELF_SIZE_IN_BYTES = 12;
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/IntTag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/IntTag;
/*     */   }
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/IntTag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/IntTag;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*     */   private static class Cache { private static final int HIGH = 1024;
/*     */     private static final int LOW = -128;
/*  18 */     static final IntTag[] cache = new IntTag[1153];
/*     */     
/*     */     static {
/*  21 */       for (int i = 0; i < cache.length; i++) {
/*  22 */         cache[i] = new IntTag(-128 + i);
/*     */       }
/*     */     } }
/*     */ 
/*     */   
/*  27 */   public static final TagType<IntTag> TYPE = new TagType.StaticSize<IntTag>()
/*     */     {
/*     */       public IntTag load(java.io.DataInput input, NbtAccounter accounter) throws java.io.IOException {
/*  30 */         return IntTag.valueOf(readAccounted(input, accounter));
/*     */       }
/*     */ 
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(java.io.DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws java.io.IOException {
/*  35 */         return output.visit(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       private static int readAccounted(java.io.DataInput input, NbtAccounter accounter) throws java.io.IOException {
/*  39 */         accounter.accountBytes(12L);
/*  40 */         return input.readInt();
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/*  45 */         return 4;
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  50 */         return "INT";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  55 */         return "TAG_Int";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*     */   public IntTag(int value) {
/*  63 */     this.value = value;
/*     */   }
/*     */   
/*     */   public static IntTag valueOf(int i) {
/*  67 */     if (i >= -128 && i <= 1024) {
/*  68 */       return Cache.cache[i - -128];
/*     */     }
/*  70 */     return new IntTag(i);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(java.io.DataOutput output) throws java.io.IOException {
/*  75 */     output.writeInt(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int sizeInBytes() {
/*  80 */     return 12;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  85 */     return 3;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<IntTag> getType() {
/*  90 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public IntTag copy() {
/*  95 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept(TagVisitor visitor) {
/* 100 */     visitor.visitInt(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 105 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 110 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public short shortValue() {
/* 115 */     return (short)(this.value & 0xFFFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte byteValue() {
/* 120 */     return (byte)(this.value & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 125 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 130 */     return this.value;
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
/* 146 */     visitor.visitInt(this);
/* 147 */     return visitor.build();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/IntTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */