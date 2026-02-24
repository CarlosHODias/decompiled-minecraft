/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.base.Predicates;
/*     */ import com.google.common.collect.Iterators;
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ import net.minecraft.core.IdMap;
/*     */ 
/*     */ public class CrudeIncrementalIntIdentityHashBiMap<K>
/*     */   implements IdMap<K>
/*     */ {
/*     */   private static final int NOT_FOUND = -1;
/*  13 */   private static final Object EMPTY_SLOT = null;
/*     */   
/*     */   private static final float LOADFACTOR = 0.8F;
/*     */   
/*     */   private K[] keys;
/*     */   
/*     */   private int[] values;
/*     */   private K[] byId;
/*     */   private int nextId;
/*     */   private int size;
/*     */   
/*     */   private CrudeIncrementalIntIdentityHashBiMap(int capacity) {
/*  25 */     this.keys = (K[])new Object[capacity];
/*  26 */     this.values = new int[capacity];
/*  27 */     this.byId = (K[])new Object[capacity];
/*     */   }
/*     */   
/*     */   private CrudeIncrementalIntIdentityHashBiMap(K[] keys, int[] values, K[] byId, int nextId, int size) {
/*  31 */     this.keys = keys;
/*  32 */     this.values = values;
/*  33 */     this.byId = byId;
/*  34 */     this.nextId = nextId;
/*  35 */     this.size = size;
/*     */   }
/*     */   
/*     */   public static <A> CrudeIncrementalIntIdentityHashBiMap<A> create(int initialCapacity) {
/*  39 */     return new CrudeIncrementalIntIdentityHashBiMap<>((int)(initialCapacity / 0.8F));
/*     */   }
/*     */ 
/*     */   
/*     */   public int getId(K thing) {
/*  44 */     return getValue(indexOf(thing, hash(thing)));
/*     */   }
/*     */ 
/*     */   
/*     */   public K byId(int id) {
/*  49 */     if (id < 0 || id >= this.byId.length) {
/*  50 */       return null;
/*     */     }
/*     */     
/*  53 */     return this.byId[id];
/*     */   }
/*     */   
/*     */   private int getValue(int index) {
/*  57 */     if (index == -1) {
/*  58 */       return -1;
/*     */     }
/*  60 */     return this.values[index];
/*     */   }
/*     */   
/*     */   public boolean contains(K key) {
/*  64 */     return (getId(key) != -1);
/*     */   }
/*     */   
/*     */   public boolean contains(int id) {
/*  68 */     return (byId(id) != null);
/*     */   }
/*     */   
/*     */   public int add(K key) {
/*  72 */     int value = nextId();
/*     */     
/*  74 */     addMapping(key, value);
/*     */     
/*  76 */     return value;
/*     */   }
/*     */   
/*     */   private int nextId() {
/*  80 */     while (this.nextId < this.byId.length && this.byId[this.nextId] != null) {
/*  81 */       this.nextId++;
/*     */     }
/*  83 */     return this.nextId;
/*     */   }
/*     */ 
/*     */   
/*     */   private void grow(int newSize) {
/*  88 */     K[] oldKeys = this.keys;
/*  89 */     int[] oldValues = this.values;
/*     */     
/*  91 */     CrudeIncrementalIntIdentityHashBiMap<K> resized = new CrudeIncrementalIntIdentityHashBiMap(newSize);
/*  92 */     for (int i = 0; i < oldKeys.length; i++) {
/*  93 */       if (oldKeys[i] != null) {
/*  94 */         resized.addMapping(oldKeys[i], oldValues[i]);
/*     */       }
/*     */     } 
/*     */     
/*  98 */     this.keys = resized.keys;
/*  99 */     this.values = resized.values;
/* 100 */     this.byId = resized.byId;
/* 101 */     this.nextId = resized.nextId;
/* 102 */     this.size = resized.size;
/*     */   }
/*     */   
/*     */   public void addMapping(K key, int id) {
/* 106 */     int minSize = Math.max(id, this.size + 1);
/* 107 */     if (minSize >= this.keys.length * 0.8F) {
/* 108 */       int newSize = this.keys.length << 1;
/* 109 */       while (newSize < id) {
/* 110 */         newSize <<= 1;
/*     */       }
/* 112 */       grow(newSize);
/*     */     } 
/*     */     
/* 115 */     int index = findEmpty(hash(key));
/* 116 */     this.keys[index] = key;
/* 117 */     this.values[index] = id;
/* 118 */     this.byId[id] = key;
/* 119 */     this.size++;
/*     */     
/* 121 */     if (id == this.nextId) {
/* 122 */       this.nextId++;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private int hash(K key) {
/* 138 */     return (Mth.murmurHash3Mixer(System.identityHashCode(key)) & Integer.MAX_VALUE) % this.keys.length;
/*     */   }
/*     */   
/*     */   private int indexOf(K key, int startFrom) {
/* 142 */     for (int i = startFrom; i < this.keys.length; i++) {
/* 143 */       if (this.keys[i] == key) {
/* 144 */         return i;
/*     */       }
/* 146 */       if (this.keys[i] == EMPTY_SLOT) {
/* 147 */         return -1;
/*     */       }
/*     */     } 
/*     */     
/* 151 */     for (int j = 0; j < startFrom; j++) {
/* 152 */       if (this.keys[j] == key) {
/* 153 */         return j;
/*     */       }
/* 155 */       if (this.keys[j] == EMPTY_SLOT) {
/* 156 */         return -1;
/*     */       }
/*     */     } 
/*     */     
/* 160 */     return -1;
/*     */   }
/*     */   
/*     */   private int findEmpty(int startFrom) {
/* 164 */     for (int i = startFrom; i < this.keys.length; i++) {
/* 165 */       if (this.keys[i] == EMPTY_SLOT) {
/* 166 */         return i;
/*     */       }
/*     */     } 
/*     */     
/* 170 */     for (int j = 0; j < startFrom; j++) {
/* 171 */       if (this.keys[j] == EMPTY_SLOT) {
/* 172 */         return j;
/*     */       }
/*     */     } 
/*     */     
/* 176 */     throw new RuntimeException("Overflowed :(");
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<K> iterator() {
/* 181 */     return (Iterator<K>)Iterators.filter((Iterator)Iterators.forArray((Object[])this.byId), Predicates.notNull());
/*     */   }
/*     */   
/*     */   public void clear() {
/* 185 */     Arrays.fill((Object[])this.keys, null);
/* 186 */     Arrays.fill((Object[])this.byId, null);
/* 187 */     this.nextId = 0;
/* 188 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 193 */     return this.size;
/*     */   }
/*     */   
/*     */   public CrudeIncrementalIntIdentityHashBiMap<K> copy() {
/* 197 */     return new CrudeIncrementalIntIdentityHashBiMap((K[])
/* 198 */         this.keys.clone(), (int[])
/* 199 */         this.values.clone(), (K[])
/* 200 */         this.byId.clone(), this.nextId, this.size);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/CrudeIncrementalIntIdentityHashBiMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */