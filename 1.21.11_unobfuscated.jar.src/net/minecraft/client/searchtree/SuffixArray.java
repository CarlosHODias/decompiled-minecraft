/*     */ package net.minecraft.client.searchtree;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.Arrays;
/*     */ import it.unimi.dsi.fastutil.Swapper;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntComparator;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ public class SuffixArray<T>
/*     */ {
/*  20 */   private static final boolean DEBUG_COMPARISONS = Boolean.parseBoolean(System.getProperty("SuffixArray.printComparisons", "false"));
/*  21 */   private static final boolean DEBUG_ARRAY = Boolean.parseBoolean(System.getProperty("SuffixArray.printArray", "false"));
/*     */   
/*  23 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private static final int END_OF_TEXT_MARKER = -1;
/*     */   private static final int END_OF_DATA = -2;
/*  27 */   protected final List<T> list = Lists.newArrayList();
/*     */   
/*  29 */   private final IntList chars = (IntList)new IntArrayList();
/*  30 */   private final IntList wordStarts = (IntList)new IntArrayList();
/*  31 */   private IntList suffixToT = (IntList)new IntArrayList();
/*  32 */   private IntList offsets = (IntList)new IntArrayList();
/*     */   private int maxStringLength;
/*     */   
/*     */   public void add(T t, String text) {
/*  36 */     this.maxStringLength = Math.max(this.maxStringLength, text.length());
/*  37 */     int index = this.list.size();
/*  38 */     this.list.add(t);
/*     */     
/*  40 */     this.wordStarts.add(this.chars.size());
/*  41 */     for (int i = 0; i < text.length(); i++) {
/*  42 */       this.suffixToT.add(index);
/*  43 */       this.offsets.add(i);
/*  44 */       this.chars.add(text.charAt(i));
/*     */     } 
/*  46 */     this.suffixToT.add(index);
/*  47 */     this.offsets.add(text.length());
/*  48 */     this.chars.add(-1);
/*     */   }
/*     */   
/*     */   public void generate() {
/*  52 */     int charCount = this.chars.size();
/*     */     
/*  54 */     int[] positions = new int[charCount];
/*     */     
/*  56 */     int[] lefts = new int[charCount];
/*  57 */     int[] rights = new int[charCount];
/*  58 */     int[] reverse = new int[charCount];
/*     */     
/*     */     IntComparator comparator = (a, b) -> (lefts[a] == lefts[b]) ? Integer.compare(rights[a], rights[b]) : Integer.compare(lefts[a], lefts[b]);
/*     */     
/*     */     Swapper swapper = (a, b) -> {
/*     */         if (a != b) {
/*     */           int tmp = lefts[a];
/*     */           
/*     */           lefts[a] = lefts[b];
/*     */           
/*     */           lefts[b] = tmp;
/*     */           
/*     */           tmp = rights[a];
/*     */           
/*     */           rights[a] = rights[b];
/*     */           
/*     */           rights[b] = tmp;
/*     */           
/*     */           tmp = reverse[a];
/*     */           
/*     */           reverse[a] = reverse[b];
/*     */           
/*     */           reverse[b] = tmp;
/*     */         } 
/*     */       };
/*  83 */     for (int i = 0; i < charCount; i++) {
/*  84 */       positions[i] = this.chars.getInt(i);
/*     */     }
/*     */     
/*  87 */     int count = 1;
/*     */     
/*  89 */     int max = Math.min(charCount, this.maxStringLength);
/*  90 */     while (count * 2 < max) {
/*  91 */       for (int m = 0; m < charCount; m++) {
/*  92 */         lefts[m] = positions[m];
/*  93 */         rights[m] = (m + count < charCount) ? positions[m + count] : -2;
/*  94 */         reverse[m] = m;
/*     */       } 
/*     */       
/*  97 */       Arrays.quickSort(0, charCount, comparator, swapper);
/*     */       
/*  99 */       for (int k = 0; k < charCount; k++) {
/* 100 */         if (k > 0 && lefts[k] == lefts[k - 1] && rights[k] == rights[k - 1]) {
/* 101 */           positions[reverse[k]] = positions[reverse[k - 1]];
/*     */         } else {
/* 103 */           positions[reverse[k]] = k;
/*     */         } 
/*     */       } 
/*     */       
/* 107 */       count *= 2;
/*     */     } 
/*     */     
/* 110 */     IntList oldSuffixToT = this.suffixToT;
/* 111 */     IntList oldOffsets = this.offsets;
/*     */     
/* 113 */     this.suffixToT = (IntList)new IntArrayList(oldSuffixToT.size());
/* 114 */     this.offsets = (IntList)new IntArrayList(oldOffsets.size());
/* 115 */     for (int j = 0; j < charCount; j++) {
/* 116 */       int index = reverse[j];
/* 117 */       this.suffixToT.add(oldSuffixToT.getInt(index));
/* 118 */       this.offsets.add(oldOffsets.getInt(index));
/*     */     } 
/* 120 */     if (DEBUG_ARRAY) {
/* 121 */       print();
/*     */     }
/*     */   }
/*     */   
/*     */   private void print() {
/* 126 */     for (int i = 0; i < this.suffixToT.size(); i++) {
/* 127 */       LOGGER.debug("{} {}", i, getString(i));
/*     */     }
/* 129 */     LOGGER.debug("");
/*     */   }
/*     */   
/*     */   private String getString(int i) {
/* 133 */     int start = this.offsets.getInt(i);
/* 134 */     int offset = this.wordStarts.getInt(this.suffixToT.getInt(i));
/*     */     
/* 136 */     StringBuilder builder = new StringBuilder();
/* 137 */     for (int j = 0; offset + j < this.chars.size(); j++) {
/* 138 */       if (j == start) {
/* 139 */         builder.append('^');
/*     */       }
/* 141 */       int p = this.chars.getInt(offset + j);
/* 142 */       if (p == -1) {
/*     */         break;
/*     */       }
/* 145 */       builder.append((char)p);
/*     */     } 
/* 147 */     return builder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   private int compare(String text, int index) {
/* 152 */     int start = this.wordStarts.getInt(this.suffixToT.getInt(index));
/* 153 */     int offset = this.offsets.getInt(index);
/*     */     
/* 155 */     for (int i = 0; i < text.length(); i++) {
/* 156 */       int p = this.chars.getInt(start + offset + i);
/* 157 */       if (p == -1) {
/* 158 */         return 1;
/*     */       }
/*     */       
/* 161 */       char c = text.charAt(i);
/* 162 */       char c2 = (char)p;
/* 163 */       if (c < c2)
/* 164 */         return -1; 
/* 165 */       if (c > c2) {
/* 166 */         return 1;
/*     */       }
/*     */     } 
/*     */     
/* 170 */     return 0;
/*     */   }
/*     */   
/*     */   public List<T> search(String text) {
/* 174 */     int suffixCount = this.suffixToT.size();
/*     */ 
/*     */ 
/*     */     
/* 178 */     int low = 0;
/* 179 */     int high = suffixCount;
/*     */     
/* 181 */     while (low < high) {
/* 182 */       int mid = low + (high - low) / 2;
/* 183 */       int c = compare(text, mid);
/* 184 */       if (DEBUG_COMPARISONS) {
/* 185 */         LOGGER.debug("comparing lower \"{}\" with {} \"{}\": {}", new Object[] { text, mid, getString(mid), c });
/*     */       }
/* 187 */       if (c > 0) {
/* 188 */         low = mid + 1; continue;
/*     */       } 
/* 190 */       high = mid;
/*     */     } 
/*     */ 
/*     */     
/* 194 */     if (low < 0 || low >= suffixCount) {
/* 195 */       return Collections.emptyList();
/*     */     }
/*     */     
/* 198 */     int lowerBound = low;
/*     */     
/* 200 */     high = suffixCount;
/* 201 */     while (low < high) {
/* 202 */       int mid = low + (high - low) / 2;
/* 203 */       int c = compare(text, mid);
/* 204 */       if (DEBUG_COMPARISONS) {
/* 205 */         LOGGER.debug("comparing upper \"{}\" with {} \"{}\": {}", new Object[] { text, mid, getString(mid), c });
/*     */       }
/* 207 */       if (c >= 0) {
/* 208 */         low = mid + 1; continue;
/*     */       } 
/* 210 */       high = mid;
/*     */     } 
/*     */ 
/*     */     
/* 214 */     int upperBound = low;
/*     */ 
/*     */     
/* 217 */     IntOpenHashSet intOpenHashSet = new IntOpenHashSet();
/* 218 */     for (int i = lowerBound; i < upperBound; i++) {
/* 219 */       intOpenHashSet.add(this.suffixToT.getInt(i));
/*     */     }
/*     */     
/* 222 */     int[] ints = intOpenHashSet.toIntArray();
/* 223 */     java.util.Arrays.sort(ints);
/*     */     
/* 225 */     Set<T> result = Sets.newLinkedHashSet();
/* 226 */     for (int t : ints) {
/* 227 */       result.add(this.list.get(t));
/*     */     }
/* 229 */     return Lists.newArrayList(result);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/searchtree/SuffixArray.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */