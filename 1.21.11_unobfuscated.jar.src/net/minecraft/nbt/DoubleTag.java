/*     */ package net.minecraft.nbt;
/*     */ import java.io.DataInput;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class DoubleTag extends Record implements NumericTag {
/*     */   private final double value;
/*     */   
/*     */   public double value() {
/*   9 */     return this.value;
/*     */   } private static final int SELF_SIZE_IN_BYTES = 16;
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/DoubleTag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #9	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/DoubleTag;
/*     */   }
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/DoubleTag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #9	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/DoubleTag;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*  17 */   public static final DoubleTag ZERO = new DoubleTag(0.0D);
/*     */   
/*  19 */   public static final TagType<DoubleTag> TYPE = new TagType.StaticSize<DoubleTag>()
/*     */     {
/*     */       public DoubleTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  22 */         return DoubleTag.valueOf(readAccounted(input, accounter));
/*     */       }
/*     */ 
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  27 */         return output.visit(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       private static double readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  31 */         accounter.accountBytes(16L);
/*  32 */         return input.readDouble();
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/*  37 */         return 8;
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  42 */         return "DOUBLE";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  47 */         return "TAG_Double";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*     */   public DoubleTag(double value) {
/*  55 */     this.value = value;
/*     */   }
/*     */   
/*     */   public static DoubleTag valueOf(double data) {
/*  59 */     if (data == 0.0D) {
/*  60 */       return ZERO;
/*     */     }
/*  62 */     return new DoubleTag(data);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(java.io.DataOutput output) throws IOException {
/*  67 */     output.writeDouble(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int sizeInBytes() {
/*  72 */     return 16;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  77 */     return 6;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<DoubleTag> getType() {
/*  82 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public DoubleTag copy() {
/*  87 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept(TagVisitor visitor) {
/*  92 */     visitor.visitDouble(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public long longValue() {
/*  97 */     return (long)Math.floor(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int intValue() {
/* 102 */     return net.minecraft.util.Mth.floor(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public short shortValue() {
/* 107 */     return (short)(net.minecraft.util.Mth.floor(this.value) & 0xFFFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public byte byteValue() {
/* 112 */     return (byte)(net.minecraft.util.Mth.floor(this.value) & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public double doubleValue() {
/* 117 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public float floatValue() {
/* 122 */     return (float)this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public Number box() {
/* 127 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
/* 132 */     return visitor.visit(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 137 */     StringTagVisitor visitor = new StringTagVisitor();
/* 138 */     visitor.visitDouble(this);
/* 139 */     return visitor.build();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/DoubleTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */