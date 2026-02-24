/*     */ package net.minecraft.nbt;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Pattern;
/*     */ 
/*     */ 
/*     */ public class StringTagVisitor
/*     */   implements TagVisitor
/*     */ {
/*  13 */   private static final Pattern UNQUOTED_KEY_MATCH = Pattern.compile("[A-Za-z._]+[A-Za-z0-9._+-]*");
/*     */   
/*  15 */   private final StringBuilder builder = new StringBuilder();
/*     */   
/*     */   public String build() {
/*  18 */     return this.builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitString(StringTag tag) {
/*  23 */     this.builder.append(StringTag.quoteAndEscape(tag.value()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitByte(ByteTag tag) {
/*  28 */     this.builder.append(tag.value()).append('b');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitShort(ShortTag tag) {
/*  33 */     this.builder.append(tag.value()).append('s');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitInt(IntTag tag) {
/*  38 */     this.builder.append(tag.value());
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitLong(LongTag tag) {
/*  43 */     this.builder.append(tag.value()).append('L');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitFloat(FloatTag tag) {
/*  48 */     this.builder.append(tag.value()).append('f');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitDouble(DoubleTag tag) {
/*  53 */     this.builder.append(tag.value()).append('d');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitByteArray(ByteArrayTag tag) {
/*  58 */     this.builder.append("[B;");
/*  59 */     byte[] data = tag.getAsByteArray();
/*  60 */     for (int i = 0; i < data.length; i++) {
/*  61 */       if (i != 0) {
/*  62 */         this.builder.append(',');
/*     */       }
/*  64 */       this.builder.append(data[i]).append('B');
/*     */     } 
/*  66 */     this.builder.append(']');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitIntArray(IntArrayTag tag) {
/*  71 */     this.builder.append("[I;");
/*  72 */     int[] data = tag.getAsIntArray();
/*  73 */     for (int i = 0; i < data.length; i++) {
/*  74 */       if (i != 0) {
/*  75 */         this.builder.append(',');
/*     */       }
/*  77 */       this.builder.append(data[i]);
/*     */     } 
/*  79 */     this.builder.append(']');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitLongArray(LongArrayTag tag) {
/*  84 */     this.builder.append("[L;");
/*  85 */     long[] data = tag.getAsLongArray();
/*  86 */     for (int i = 0; i < data.length; i++) {
/*  87 */       if (i != 0) {
/*  88 */         this.builder.append(',');
/*     */       }
/*  90 */       this.builder.append(data[i]).append('L');
/*     */     } 
/*  92 */     this.builder.append(']');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitList(ListTag tag) {
/*  97 */     this.builder.append('[');
/*  98 */     for (int i = 0; i < tag.size(); i++) {
/*  99 */       if (i != 0) {
/* 100 */         this.builder.append(',');
/*     */       }
/* 102 */       tag.get(i).accept(this);
/*     */     } 
/* 104 */     this.builder.append(']');
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitCompound(CompoundTag tag) {
/* 109 */     this.builder.append('{');
/*     */     
/* 111 */     List<Map.Entry<String, Tag>> entries = new ArrayList<>(tag.entrySet());
/* 112 */     entries.sort((Comparator)Map.Entry.comparingByKey());
/* 113 */     for (int i = 0; i < entries.size(); i++) {
/* 114 */       Map.Entry<String, Tag> entry = entries.get(i);
/* 115 */       if (i != 0) {
/* 116 */         this.builder.append(',');
/*     */       }
/* 118 */       handleKeyEscape(entry.getKey());
/* 119 */       this.builder.append(':');
/* 120 */       ((Tag)entry.getValue()).accept(this);
/*     */     } 
/*     */     
/* 123 */     this.builder.append('}');
/*     */   }
/*     */   
/*     */   private void handleKeyEscape(String input) {
/* 127 */     if (!input.equalsIgnoreCase("true") && !input.equalsIgnoreCase("false") && UNQUOTED_KEY_MATCH.matcher(input).matches()) {
/* 128 */       this.builder.append(input);
/*     */     } else {
/* 130 */       StringTag.quoteAndEscape(input, this.builder);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void visitEnd(EndTag tag) {
/* 136 */     this.builder.append("END");
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/StringTagVisitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */