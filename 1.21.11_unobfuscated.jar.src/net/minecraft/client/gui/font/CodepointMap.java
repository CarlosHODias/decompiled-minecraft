/*     */ package net.minecraft.client.gui.font;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import it.unimi.dsi.fastutil.ints.IntSet;
/*     */ import java.util.Arrays;
/*     */ import java.util.function.IntFunction;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CodepointMap<T>
/*     */ {
/*     */   private static final int BLOCK_BITS = 8;
/*     */   private static final int BLOCK_SIZE = 256;
/*     */   private static final int IN_BLOCK_MASK = 255;
/*     */   private static final int MAX_BLOCK = 4351;
/*     */   private static final int BLOCK_COUNT = 4352;
/*     */   private final T[] empty;
/*     */   private final T[][] blockMap;
/*     */   private final IntFunction<T[]> blockConstructor;
/*     */   
/*     */   public CodepointMap(IntFunction<T[]> blockConstructor, IntFunction<T[][]> blockMapConstructor) {
/*  22 */     this.empty = blockConstructor.apply(256);
/*  23 */     this.blockMap = blockMapConstructor.apply(4352);
/*  24 */     Arrays.fill((Object[])this.blockMap, this.empty);
/*  25 */     this.blockConstructor = blockConstructor;
/*     */   }
/*     */   
/*     */   public void clear() {
/*  29 */     Arrays.fill((Object[])this.blockMap, this.empty);
/*     */   }
/*     */   
/*     */   public T get(int codepoint) {
/*  33 */     int block = codepoint >> 8;
/*  34 */     int offset = codepoint & 0xFF;
/*  35 */     return this.blockMap[block][offset];
/*     */   }
/*     */   
/*     */   public T put(int codepoint, T value) {
/*  39 */     int block = codepoint >> 8;
/*  40 */     int offset = codepoint & 0xFF;
/*     */     
/*  42 */     T[] blockData = this.blockMap[block];
/*  43 */     if (blockData == this.empty) {
/*  44 */       blockData = this.blockConstructor.apply(256);
/*  45 */       this.blockMap[block] = blockData;
/*  46 */       blockData[offset] = value;
/*  47 */       return null;
/*     */     } 
/*  49 */     T previous = blockData[offset];
/*  50 */     blockData[offset] = value;
/*  51 */     return previous;
/*     */   }
/*     */ 
/*     */   
/*     */   public T computeIfAbsent(int codepoint, IntFunction<T> mapper) {
/*  56 */     int block = codepoint >> 8;
/*  57 */     int offset = codepoint & 0xFF;
/*     */     
/*  59 */     T[] blockData = this.blockMap[block];
/*  60 */     T current = blockData[offset];
/*  61 */     if (current != null) {
/*  62 */       return current;
/*     */     }
/*     */     
/*  65 */     if (blockData == this.empty) {
/*  66 */       blockData = this.blockConstructor.apply(256);
/*  67 */       this.blockMap[block] = blockData;
/*     */     } 
/*     */     
/*  70 */     T result = mapper.apply(codepoint);
/*  71 */     blockData[offset] = result;
/*  72 */     return result;
/*     */   }
/*     */   
/*     */   public T remove(int codepoint) {
/*  76 */     int block = codepoint >> 8;
/*  77 */     int offset = codepoint & 0xFF;
/*     */     
/*  79 */     T[] blockData = this.blockMap[block];
/*  80 */     if (blockData == this.empty) {
/*  81 */       return null;
/*     */     }
/*     */     
/*  84 */     T previous = blockData[offset];
/*  85 */     blockData[offset] = null;
/*  86 */     return previous;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void forEach(Output<T> output) {
/*  95 */     for (int block = 0; block < this.blockMap.length; block++) {
/*  96 */       T[] blockData = this.blockMap[block];
/*  97 */       if (blockData != this.empty) {
/*  98 */         for (int offset = 0; offset < blockData.length; offset++) {
/*  99 */           T value = blockData[offset];
/* 100 */           if (value != null) {
/* 101 */             int codepoint = block << 8 | offset;
/* 102 */             output.accept(codepoint, value);
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public IntSet keySet() {
/* 110 */     IntOpenHashSet result = new IntOpenHashSet();
/* 111 */     forEach((codepoint, value) -> result.add(codepoint));
/* 112 */     return (IntSet)result;
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface Output<T> {
/*     */     void accept(int param1Int, T param1T);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/CodepointMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */