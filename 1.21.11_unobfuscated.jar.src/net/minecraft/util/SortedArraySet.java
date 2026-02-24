/*     */ package net.minecraft.util;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrays;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.Arrays;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SortedArraySet<T>
/*     */   extends AbstractSet<T>
/*     */ {
/*     */   private static final int DEFAULT_INITIAL_CAPACITY = 10;
/*     */   private final Comparator<T> comparator;
/*     */   private T[] contents;
/*     */   private int size;
/*     */   
/*     */   private SortedArraySet(int initialCapacity, Comparator<T> comparator) {
/*  26 */     this.comparator = comparator;
/*     */     
/*  28 */     if (initialCapacity < 0) {
/*  29 */       throw new IllegalArgumentException("Initial capacity (" + initialCapacity + ") is negative");
/*     */     }
/*  31 */     this.contents = castRawArray(new Object[initialCapacity]);
/*     */   }
/*     */   
/*     */   public static <T extends Comparable<T>> SortedArraySet<T> create() {
/*  35 */     return create(10);
/*     */   }
/*     */   
/*     */   public static <T extends Comparable<T>> SortedArraySet<T> create(int initialCapacity) {
/*  39 */     return new SortedArraySet<>(initialCapacity, (Comparator)Comparator.naturalOrder());
/*     */   }
/*     */   
/*     */   public static <T> SortedArraySet<T> create(Comparator<T> comparator) {
/*  43 */     return create(comparator, 10);
/*     */   }
/*     */   
/*     */   public static <T> SortedArraySet<T> create(Comparator<T> comparator, int initialCapacity) {
/*  47 */     return new SortedArraySet<>(initialCapacity, comparator);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T> T[] castRawArray(Object[] array) {
/*  52 */     return (T[])array;
/*     */   }
/*     */   
/*     */   private int findIndex(T t) {
/*  56 */     return Arrays.binarySearch(this.contents, 0, this.size, t, this.comparator);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int getInsertionPosition(int position) {
/*  65 */     return -position - 1;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean add(T t) {
/*  70 */     int position = findIndex(t);
/*  71 */     if (position >= 0) {
/*  72 */       return false;
/*     */     }
/*     */     
/*  75 */     int pos = getInsertionPosition(position);
/*  76 */     addInternal(t, pos);
/*  77 */     return true;
/*     */   }
/*     */   
/*     */   private void grow(int capacity) {
/*  81 */     if (capacity <= this.contents.length) {
/*     */       return;
/*     */     }
/*  84 */     if (this.contents != ObjectArrays.DEFAULT_EMPTY_ARRAY) {
/*  85 */       capacity = Util.growByHalf(this.contents.length, capacity);
/*  86 */     } else if (capacity < 10) {
/*  87 */       capacity = 10;
/*     */     } 
/*     */     
/*  90 */     Object[] t = new Object[capacity];
/*  91 */     System.arraycopy(this.contents, 0, t, 0, this.size);
/*  92 */     this.contents = castRawArray(t);
/*     */   }
/*     */   
/*     */   private void addInternal(T t, int pos) {
/*  96 */     grow(this.size + 1);
/*  97 */     if (pos != this.size) {
/*  98 */       System.arraycopy(this.contents, pos, this.contents, pos + 1, this.size - pos);
/*     */     }
/* 100 */     this.contents[pos] = t;
/* 101 */     this.size++;
/*     */   }
/*     */   
/*     */   private void removeInternal(int position) {
/* 105 */     this.size--;
/* 106 */     if (position != this.size) {
/* 107 */       System.arraycopy(this.contents, position + 1, this.contents, position, this.size - position);
/*     */     }
/* 109 */     this.contents[this.size] = null;
/*     */   }
/*     */   
/*     */   private T getInternal(int position) {
/* 113 */     return this.contents[position];
/*     */   }
/*     */   
/*     */   public T addOrGet(T t) {
/* 117 */     int position = findIndex(t);
/* 118 */     if (position >= 0) {
/* 119 */       return getInternal(position);
/*     */     }
/*     */     
/* 122 */     addInternal(t, getInsertionPosition(position));
/* 123 */     return t;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean remove(Object o) {
/* 129 */     int position = findIndex((T)o);
/* 130 */     if (position >= 0) {
/* 131 */       removeInternal(position);
/* 132 */       return true;
/*     */     } 
/* 134 */     return false;
/*     */   }
/*     */   
/*     */   public T get(T t) {
/* 138 */     int position = findIndex(t);
/* 139 */     if (position >= 0) {
/* 140 */       return getInternal(position);
/*     */     }
/* 142 */     return null;
/*     */   }
/*     */   
/*     */   public T first() {
/* 146 */     return getInternal(0);
/*     */   }
/*     */   
/*     */   public T last() {
/* 150 */     return getInternal(this.size - 1);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean contains(Object o) {
/* 156 */     int result = findIndex((T)o);
/* 157 */     return (result >= 0);
/*     */   }
/*     */   
/*     */   private class ArrayIterator implements Iterator<T> {
/*     */     private int index;
/* 162 */     private int last = -1;
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 166 */       return (this.index < SortedArraySet.this.size);
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 171 */       if (this.index >= SortedArraySet.this.size) {
/* 172 */         throw new NoSuchElementException();
/*     */       }
/* 174 */       this.last = this.index++;
/* 175 */       return SortedArraySet.this.contents[this.last];
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 180 */       if (this.last == -1) {
/* 181 */         throw new IllegalStateException();
/*     */       }
/* 183 */       SortedArraySet.this.removeInternal(this.last);
/* 184 */       this.index--;
/* 185 */       this.last = -1;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<T> iterator() {
/* 191 */     return new ArrayIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/* 196 */     return this.size;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object[] toArray() {
/* 201 */     return Arrays.copyOf(this.contents, this.size, Object[].class);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public <U> U[] toArray(U[] a) {
/* 207 */     if (a.length < this.size) {
/* 208 */       return Arrays.copyOf(this.contents, this.size, (Class)a.getClass());
/*     */     }
/* 210 */     System.arraycopy(this.contents, 0, a, 0, this.size);
/* 211 */     if (a.length > this.size) {
/* 212 */       a[this.size] = null;
/*     */     }
/* 214 */     return a;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 219 */     Arrays.fill((Object[])this.contents, 0, this.size, null);
/* 220 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 225 */     if (this == o) {
/* 226 */       return true;
/*     */     }
/* 228 */     if (o instanceof SortedArraySet) { SortedArraySet<?> that = (SortedArraySet)o;
/* 229 */       if (this.comparator.equals(that.comparator)) {
/* 230 */         return (this.size == that.size && Arrays.equals((Object[])this.contents, (Object[])that.contents));
/*     */       } }
/*     */ 
/*     */     
/* 234 */     return super.equals(o);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/SortedArraySet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */