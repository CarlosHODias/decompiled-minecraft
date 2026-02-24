/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ public final class ByteTag extends Record implements NumericTag {
/*     */   private final byte value;
/*     */   
/*     */   public byte value() {
/*   7 */     return this.value;
/*     */   }
/*     */   private static final int SELF_SIZE_IN_BYTES = 9;
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/ByteTag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/ByteTag;
/*     */   }
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/ByteTag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #7	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/ByteTag;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*  16 */   private static class Cache { private static final ByteTag[] cache = new ByteTag[256];
/*     */     
/*     */     static {
/*  19 */       for (int i = 0; i < cache.length; i++) {
/*  20 */         cache[i] = new ByteTag((byte)(i - 128));
/*     */       }
/*     */     } }
/*     */ 
/*     */   
/*  25 */   public static final TagType<ByteTag> TYPE = new TagType.StaticSize<ByteTag>()
/*     */     {
/*     */       public ByteTag load(java.io.DataInput input, NbtAccounter accounter) throws java.io.IOException {
/*  28 */         return ByteTag.valueOf(readAccounted(input, accounter));
/*     */       }
/*     */ 
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(java.io.DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws java.io.IOException {
/*  33 */         return output.visit(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       private static byte readAccounted(java.io.DataInput input, NbtAccounter accounter) throws java.io.IOException {
/*  37 */         accounter.accountBytes(9L);
/*  38 */         return input.readByte();
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/*  43 */         return 1;
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  48 */         return "BYTE";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  53 */         return "TAG_Byte";
/*     */       }
/*     */     };
/*     */   
/*  57 */   public static final ByteTag ZERO = valueOf((byte)0);
/*  58 */   public static final ByteTag ONE = valueOf((byte)1);
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*     */   public ByteTag(byte value) {
/*  64 */     this.value = value;
/*     */   }
/*     */   
/*     */   public static ByteTag valueOf(byte data) {
/*  68 */     return Cache.cache[128 + data];
/*     */   }
/*     */   
/*     */   public static ByteTag valueOf(boolean data) {
/*  72 */     return data ? ONE : ZERO;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(java.io.DataOutput output) throws java.io.IOException {
/*  77 */     output.writeByte(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int sizeInBytes() {
/*  82 */     return 9;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  87 */     return 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<ByteTag> getType() {
/*  92 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public ByteTag copy() {
/*  97 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept(TagVisitor visitor) {
/* 102 */     visitor.visitByte(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public long longValue() {
/* 107 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 112 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public short shortValue() {
/* 117 */     return (short)this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte byteValue() {
/* 122 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 127 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 132 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public Number box() {
/* 137 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
/* 142 */     return visitor.visit(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 147 */     StringTagVisitor visitor = new StringTagVisitor();
/* 148 */     visitor.visitByte(this);
/* 149 */     return visitor.build();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/ByteTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */