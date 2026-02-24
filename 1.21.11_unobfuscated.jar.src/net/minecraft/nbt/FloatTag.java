/*     */ package net.minecraft.nbt;
/*     */ import java.io.DataInput;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public final class FloatTag extends Record implements NumericTag {
/*     */   private final float value;
/*     */   
/*     */   public float value() {
/*   9 */     return this.value;
/*     */   } private static final int SELF_SIZE_IN_BYTES = 12;
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/FloatTag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #9	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/FloatTag;
/*     */   }
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/FloatTag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #9	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/FloatTag;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*  17 */   public static final FloatTag ZERO = new FloatTag(0.0F);
/*     */   
/*  19 */   public static final TagType<FloatTag> TYPE = new TagType.StaticSize<FloatTag>()
/*     */     {
/*     */       public FloatTag load(DataInput input, NbtAccounter accounter) throws IOException {
/*  22 */         return FloatTag.valueOf(readAccounted(input, accounter));
/*     */       }
/*     */ 
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws IOException {
/*  27 */         return output.visit(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       private static float readAccounted(DataInput input, NbtAccounter accounter) throws IOException {
/*  31 */         accounter.accountBytes(12L);
/*  32 */         return input.readFloat();
/*     */       }
/*     */ 
/*     */       
/*     */       public int size() {
/*  37 */         return 4;
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  42 */         return "FLOAT";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  47 */         return "TAG_Float";
/*     */       }
/*     */     };
/*     */ 
/*     */ 
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*     */   public FloatTag(float value) {
/*  55 */     this.value = value;
/*     */   }
/*     */   
/*     */   public static FloatTag valueOf(float data) {
/*  59 */     if (data == 0.0F) {
/*  60 */       return ZERO;
/*     */     }
/*  62 */     return new FloatTag(data);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(java.io.DataOutput output) throws IOException {
/*  67 */     output.writeFloat(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int sizeInBytes() {
/*  72 */     return 12;
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  77 */     return 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<FloatTag> getType() {
/*  82 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public FloatTag copy() {
/*  87 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept(TagVisitor visitor) {
/*  92 */     visitor.visitFloat(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public long longValue() {
/*  97 */     return (long)this.value;
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
/* 122 */     return this.value;
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
/* 138 */     visitor.visitFloat(this);
/* 139 */     return visitor.build();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/FloatTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */