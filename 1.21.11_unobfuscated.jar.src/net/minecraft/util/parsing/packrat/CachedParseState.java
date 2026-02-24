/*     */ package net.minecraft.util.parsing.packrat;
/*     */ 
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class CachedParseState<S>
/*     */   implements ParseState<S>
/*     */ {
/*  11 */   private PositionCache[] positionCache = new PositionCache[256];
/*     */   
/*     */   private final ErrorCollector<S> errorCollector;
/*     */   
/*  15 */   private final Scope scope = new Scope();
/*     */   
/*  17 */   private SimpleControl[] controlCache = new SimpleControl[16];
/*     */   
/*     */   private int nextControlToReturn;
/*  20 */   private final Silent silent = new Silent();
/*     */   
/*     */   protected CachedParseState(ErrorCollector<S> errorCollector) {
/*  23 */     this.errorCollector = errorCollector;
/*     */   }
/*     */ 
/*     */   
/*     */   public Scope scope() {
/*  28 */     return this.scope;
/*     */   }
/*     */ 
/*     */   
/*     */   public ErrorCollector<S> errorCollector() {
/*  33 */     return this.errorCollector;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T parse(NamedRule<S, T> rule) {
/*     */     CacheEntry<T> entry;
/*  39 */     int markBeforeParse = mark();
/*  40 */     PositionCache positionCache = getCacheForPosition(markBeforeParse);
/*     */     
/*  42 */     int entryIndex = positionCache.findKeyIndex(rule.name());
/*     */     
/*  44 */     if (entryIndex != -1) {
/*  45 */       CacheEntry<T> value = positionCache.getValue(entryIndex);
/*  46 */       if (value != null) {
/*  47 */         if (value == CacheEntry.NEGATIVE) {
/*  48 */           return null;
/*     */         }
/*  50 */         restore(value.markAfterParse);
/*  51 */         return value.value;
/*     */       } 
/*     */     } else {
/*     */       
/*  55 */       entryIndex = positionCache.allocateNewEntry(rule.name());
/*     */     } 
/*     */     
/*  58 */     T result = rule.value().parse(this);
/*     */ 
/*     */ 
/*     */     
/*  62 */     if (result == null) {
/*     */       
/*  64 */       entry = CacheEntry.negativeEntry();
/*     */     } else {
/*  66 */       int markAfterParse = mark();
/*  67 */       entry = new CacheEntry<>(result, markAfterParse);
/*     */     } 
/*  69 */     positionCache.setValue(entryIndex, entry);
/*     */     
/*  71 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private PositionCache getCacheForPosition(int index) {
/*  76 */     int currentSize = this.positionCache.length;
/*  77 */     if (index >= currentSize) {
/*  78 */       int newSize = Util.growByHalf(currentSize, index + 1);
/*  79 */       PositionCache[] newCache = new PositionCache[newSize];
/*  80 */       System.arraycopy(this.positionCache, 0, newCache, 0, currentSize);
/*  81 */       this.positionCache = newCache;
/*     */     } 
/*     */     
/*  84 */     PositionCache result = this.positionCache[index];
/*  85 */     if (result == null) {
/*  86 */       result = new PositionCache();
/*  87 */       this.positionCache[index] = result;
/*     */     } 
/*     */     
/*  90 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Control acquireControl() {
/*  95 */     int currentSize = this.controlCache.length;
/*  96 */     if (this.nextControlToReturn >= currentSize) {
/*  97 */       int newSize = Util.growByHalf(currentSize, this.nextControlToReturn + 1);
/*  98 */       SimpleControl[] newControlCache = new SimpleControl[newSize];
/*  99 */       System.arraycopy(this.controlCache, 0, newControlCache, 0, currentSize);
/* 100 */       this.controlCache = newControlCache;
/*     */     } 
/*     */     
/* 103 */     int controlIndex = this.nextControlToReturn++;
/* 104 */     SimpleControl entry = this.controlCache[controlIndex];
/* 105 */     if (entry == null) {
/* 106 */       entry = new SimpleControl();
/* 107 */       this.controlCache[controlIndex] = entry;
/*     */     } else {
/* 109 */       entry.reset();
/*     */     } 
/* 111 */     return entry;
/*     */   }
/*     */ 
/*     */   
/*     */   public void releaseControl() {
/* 116 */     this.nextControlToReturn--;
/*     */   }
/*     */ 
/*     */   
/*     */   private static class PositionCache
/*     */   {
/*     */     public static final int ENTRY_STRIDE = 2;
/*     */     private static final int NOT_FOUND = -1;
/* 124 */     private Object[] atomCache = new Object[16];
/*     */     private int nextKey;
/*     */     
/*     */     public int findKeyIndex(Atom<?> key) {
/* 128 */       for (int i = 0; i < this.nextKey; i += 2) {
/* 129 */         if (this.atomCache[i] == key) {
/* 130 */           return i;
/*     */         }
/*     */       } 
/*     */       
/* 134 */       return -1;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public int allocateNewEntry(Atom<?> key) {
/* 141 */       int newKeyIndex = this.nextKey;
/* 142 */       this.nextKey += 2;
/*     */       
/* 144 */       int newValueIndex = newKeyIndex + 1;
/* 145 */       int currentSize = this.atomCache.length;
/* 146 */       if (newValueIndex >= currentSize) {
/* 147 */         int newSize = Util.growByHalf(currentSize, newValueIndex + 1);
/* 148 */         Object[] newCache = new Object[newSize];
/* 149 */         System.arraycopy(this.atomCache, 0, newCache, 0, currentSize);
/* 150 */         this.atomCache = newCache;
/*     */       } 
/*     */       
/* 153 */       this.atomCache[newKeyIndex] = key;
/*     */       
/* 155 */       return newKeyIndex;
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> CachedParseState.CacheEntry<T> getValue(int keyIndex) {
/* 160 */       return (CachedParseState.CacheEntry<T>)this.atomCache[keyIndex + 1];
/*     */     }
/*     */     
/*     */     public void setValue(int keyIndex, CachedParseState.CacheEntry<?> entry) {
/* 164 */       this.atomCache[keyIndex + 1] = entry;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public ParseState<S> silent() {
/* 170 */     return this.silent;
/*     */   }
/*     */   private static final class CacheEntry<T> extends Record { private final T value; private final int markAfterParse;
/* 173 */     private CacheEntry(T value, int markAfterParse) { this.value = value; this.markAfterParse = markAfterParse; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/parsing/packrat/CachedParseState$CacheEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #173	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/CachedParseState$CacheEntry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 173 */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/CachedParseState$CacheEntry<TT;>; } public T value() { return this.value; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/parsing/packrat/CachedParseState$CacheEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #173	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/CachedParseState$CacheEntry;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/util/parsing/packrat/CachedParseState$CacheEntry<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/parsing/packrat/CachedParseState$CacheEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #173	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/CachedParseState$CacheEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 173 */       //   0	8	0	this	Lnet/minecraft/util/parsing/packrat/CachedParseState$CacheEntry<TT;>; } public int markAfterParse() { return this.markAfterParse; }
/*     */ 
/*     */ 
/*     */     
/* 177 */     public static final CacheEntry<?> NEGATIVE = new CacheEntry(null, -1);
/*     */ 
/*     */     
/*     */     public static <T> CacheEntry<T> negativeEntry() {
/* 181 */       return (CacheEntry)NEGATIVE;
/*     */     } }
/*     */ 
/*     */   
/*     */   private class Silent implements ParseState<S> {
/* 186 */     private final ErrorCollector<S> silentCollector = new ErrorCollector.Nop<>();
/*     */ 
/*     */     
/*     */     public ErrorCollector<S> errorCollector() {
/* 190 */       return this.silentCollector;
/*     */     }
/*     */ 
/*     */     
/*     */     public Scope scope() {
/* 195 */       return CachedParseState.this.scope();
/*     */     }
/*     */ 
/*     */     
/*     */     public <T> T parse(NamedRule<S, T> rule) {
/* 200 */       return CachedParseState.this.parse(rule);
/*     */     }
/*     */ 
/*     */     
/*     */     public S input() {
/* 205 */       return CachedParseState.this.input();
/*     */     }
/*     */ 
/*     */     
/*     */     public int mark() {
/* 210 */       return CachedParseState.this.mark();
/*     */     }
/*     */ 
/*     */     
/*     */     public void restore(int mark) {
/* 215 */       CachedParseState.this.restore(mark);
/*     */     }
/*     */ 
/*     */     
/*     */     public Control acquireControl() {
/* 220 */       return CachedParseState.this.acquireControl();
/*     */     }
/*     */ 
/*     */     
/*     */     public void releaseControl() {
/* 225 */       CachedParseState.this.releaseControl();
/*     */     }
/*     */ 
/*     */     
/*     */     public ParseState<S> silent() {
/* 230 */       return this;
/*     */     }
/*     */   }
/*     */   
/*     */   private static class SimpleControl
/*     */     implements Control {
/*     */     private boolean hasCut;
/*     */     
/*     */     public void cut() {
/* 239 */       this.hasCut = true;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean hasCut() {
/* 244 */       return this.hasCut;
/*     */     }
/*     */     
/*     */     public void reset() {
/* 248 */       this.hasCut = false;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/parsing/packrat/CachedParseState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */