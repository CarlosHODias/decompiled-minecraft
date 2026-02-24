/*     */ package net.minecraft.nbt;
/*     */ import java.io.DataInput;
/*     */ 
/*     */ public final class StringTag extends Record implements PrimitiveTag {
/*     */   private final String value;
/*     */   
/*     */   public String value() {
/*   8 */     return this.value;
/*     */   } private static final int SELF_SIZE_IN_BYTES = 36;
/*     */   public final int hashCode() {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/nbt/StringTag;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #8	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lnet/minecraft/nbt/StringTag;
/*     */   }
/*     */   public final boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lnet/minecraft/nbt/StringTag;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #8	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lnet/minecraft/nbt/StringTag;
/*     */     //   0	8	1	o	Ljava/lang/Object;
/*     */   }
/*  16 */   public static final TagType<StringTag> TYPE = new TagType.VariableSize<StringTag>()
/*     */     {
/*     */       public StringTag load(DataInput input, NbtAccounter accounter) throws java.io.IOException {
/*  19 */         return StringTag.valueOf(readAccounted(input, accounter));
/*     */       }
/*     */ 
/*     */       
/*     */       public StreamTagVisitor.ValueResult parse(DataInput input, StreamTagVisitor output, NbtAccounter accounter) throws java.io.IOException {
/*  24 */         return output.visit(readAccounted(input, accounter));
/*     */       }
/*     */       
/*     */       private static String readAccounted(DataInput input, NbtAccounter accounter) throws java.io.IOException {
/*  28 */         accounter.accountBytes(36L);
/*     */ 
/*     */         
/*  31 */         String data = input.readUTF();
/*  32 */         accounter.accountBytes(2L, data.length());
/*  33 */         return data;
/*     */       }
/*     */ 
/*     */       
/*     */       public void skip(DataInput input, NbtAccounter accounter) throws java.io.IOException {
/*  38 */         StringTag.skipString(input);
/*     */       }
/*     */ 
/*     */       
/*     */       public String getName() {
/*  43 */         return "STRING";
/*     */       }
/*     */ 
/*     */       
/*     */       public String getPrettyName() {
/*  48 */         return "TAG_String";
/*     */       }
/*     */     };
/*     */   
/*     */   public static void skipString(DataInput input) throws java.io.IOException {
/*  53 */     input.skipBytes(input.readUnsignedShort());
/*     */   }
/*     */   
/*  56 */   private static final StringTag EMPTY = new StringTag("");
/*     */   
/*     */   private static final char DOUBLE_QUOTE = '"';
/*     */   
/*     */   private static final char SINGLE_QUOTE = '\'';
/*     */   private static final char ESCAPE = '\\';
/*     */   private static final char NOT_SET = '\000';
/*     */   
/*     */   @Deprecated(forRemoval = true)
/*     */   public StringTag(String value) {
/*  66 */     this.value = value;
/*     */   }
/*     */   
/*     */   public static StringTag valueOf(String data) {
/*  70 */     if (data.isEmpty()) {
/*  71 */       return EMPTY;
/*     */     }
/*  73 */     return new StringTag(data);
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(java.io.DataOutput output) throws java.io.IOException {
/*  78 */     output.writeUTF(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public int sizeInBytes() {
/*  83 */     return 36 + 2 * this.value.length();
/*     */   }
/*     */ 
/*     */   
/*     */   public byte getId() {
/*  88 */     return 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public TagType<StringTag> getType() {
/*  93 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  98 */     StringTagVisitor visitor = new StringTagVisitor();
/*  99 */     visitor.visitString(this);
/* 100 */     return visitor.build();
/*     */   }
/*     */ 
/*     */   
/*     */   public StringTag copy() {
/* 105 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public java.util.Optional<String> asString() {
/* 110 */     return java.util.Optional.of(this.value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void accept(TagVisitor visitor) {
/* 115 */     visitor.visitString(this);
/*     */   }
/*     */   
/*     */   public static String quoteAndEscape(String input) {
/* 119 */     StringBuilder result = new StringBuilder();
/* 120 */     quoteAndEscape(input, result);
/* 121 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void quoteAndEscape(String input, StringBuilder result) {
/* 129 */     int quoteMarkIndex = result.length();
/* 130 */     result.append(' ');
/* 131 */     char quote = Character.MIN_VALUE;
/* 132 */     for (int i = 0; i < input.length(); i++) {
/* 133 */       char c = input.charAt(i);
/* 134 */       if (c == '\\') {
/* 135 */         result.append("\\\\");
/* 136 */       } else if (c == '"' || c == '\'') {
/* 137 */         if (quote == '\000') {
/* 138 */           quote = (c == '"') ? '\'' : '"';
/*     */         }
/* 140 */         if (quote == c) {
/* 141 */           result.append('\\');
/*     */         }
/* 143 */         result.append(c);
/*     */       } else {
/* 145 */         String escaped = SnbtGrammar.escapeControlCharacters(c);
/* 146 */         if (escaped != null) {
/* 147 */           result.append('\\');
/* 148 */           result.append(escaped);
/*     */         } else {
/* 150 */           result.append(c);
/*     */         } 
/*     */       } 
/*     */     } 
/* 154 */     if (quote == '\000') {
/* 155 */       quote = '"';
/*     */     }
/*     */     
/* 158 */     result.setCharAt(quoteMarkIndex, quote);
/* 159 */     result.append(quote);
/*     */   }
/*     */   
/*     */   public static String escapeWithoutQuotes(String input) {
/* 163 */     StringBuilder result = new StringBuilder();
/* 164 */     escapeWithoutQuotes(input, result);
/* 165 */     return result.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void escapeWithoutQuotes(String input, StringBuilder result) {
/* 172 */     for (int i = 0; i < input.length(); i++) {
/* 173 */       String escaped; char c = input.charAt(i);
/* 174 */       switch (c) { case '"': case '\'':
/*     */         case '\\':
/* 176 */           result.append('\\');
/* 177 */           result.append(c);
/*     */           break;
/*     */         default:
/* 180 */           escaped = SnbtGrammar.escapeControlCharacters(c);
/* 181 */           if (escaped != null) {
/* 182 */             result.append('\\');
/* 183 */             result.append(escaped); break;
/*     */           } 
/* 185 */           result.append(c);
/*     */           break; }
/*     */     
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public StreamTagVisitor.ValueResult accept(StreamTagVisitor visitor) {
/* 194 */     return visitor.visit(this.value);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/StringTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */