/*     */ package net.minecraft.util;
/*     */ 
/*     */ import com.google.common.annotations.VisibleForTesting;
/*     */ import java.util.AbstractList;
/*     */ import java.util.Deque;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Objects;
/*     */ import java.util.SequencedCollection;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.UnaryOperator;
/*     */ 
/*     */ public class ArrayListDeque<T> extends AbstractList<T> implements ListAndDeque<T> {
/*     */   private static final int MIN_GROWTH = 1;
/*     */   private Object[] contents;
/*     */   private int head;
/*     */   private int size;
/*     */   
/*     */   public ArrayListDeque() {
/*  22 */     this(1);
/*     */   }
/*     */   
/*     */   public ArrayListDeque(int capacity) {
/*  26 */     this.contents = new Object[capacity];
/*  27 */     this.head = 0;
/*  28 */     this.size = 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int size() {
/*  33 */     return this.size;
/*     */   }
/*     */   
/*     */   @VisibleForTesting
/*     */   public int capacity() {
/*  38 */     return this.contents.length;
/*     */   }
/*     */   
/*     */   private int getIndex(int index) {
/*  42 */     return (index + this.head) % this.contents.length;
/*     */   }
/*     */ 
/*     */   
/*     */   public T get(int index) {
/*  47 */     verifyIndexInRange(index);
/*  48 */     return getInner(getIndex(index));
/*     */   }
/*     */   
/*     */   private static void verifyIndexInRange(int index, int size) {
/*  52 */     if (index < 0 || index >= size) {
/*  53 */       throw new IndexOutOfBoundsException(index);
/*     */     }
/*     */   }
/*     */   
/*     */   private void verifyIndexInRange(int index) {
/*  58 */     verifyIndexInRange(index, this.size);
/*     */   }
/*     */ 
/*     */   
/*     */   private T getInner(int innerIndex) {
/*  63 */     return (T)this.contents[innerIndex];
/*     */   }
/*     */ 
/*     */   
/*     */   public T set(int index, T element) {
/*  68 */     verifyIndexInRange(index);
/*  69 */     Objects.requireNonNull(element);
/*  70 */     int innerIndex = getIndex(index);
/*  71 */     T current = getInner(innerIndex);
/*  72 */     this.contents[innerIndex] = element;
/*  73 */     return current;
/*     */   }
/*     */ 
/*     */   
/*     */   public void add(int index, T element) {
/*  78 */     verifyIndexInRange(index, this.size + 1);
/*  79 */     Objects.requireNonNull(element);
/*  80 */     if (this.size == this.contents.length) {
/*  81 */       grow();
/*     */     }
/*  83 */     int internalIndex = getIndex(index);
/*  84 */     if (index == this.size) {
/*  85 */       this.contents[internalIndex] = element;
/*  86 */     } else if (index == 0) {
/*  87 */       this.head--;
/*  88 */       if (this.head < 0) {
/*  89 */         this.head += this.contents.length;
/*     */       }
/*  91 */       this.contents[getIndex(0)] = element;
/*     */     } else {
/*  93 */       for (int i = this.size - 1; i >= index; i--) {
/*  94 */         this.contents[getIndex(i + 1)] = this.contents[getIndex(i)];
/*     */       }
/*  96 */       this.contents[internalIndex] = element;
/*     */     } 
/*  98 */     this.modCount++;
/*  99 */     this.size++;
/*     */   }
/*     */ 
/*     */   
/*     */   private void grow() {
/* 104 */     int newLength = this.contents.length + Math.max(this.contents.length >> 1, 1);
/* 105 */     Object[] newContents = new Object[newLength];
/* 106 */     copyCount(newContents, this.size);
/* 107 */     this.head = 0;
/* 108 */     this.contents = newContents;
/*     */   }
/*     */ 
/*     */   
/*     */   public T remove(int index) {
/* 113 */     verifyIndexInRange(index);
/* 114 */     int innerIndex = getIndex(index);
/* 115 */     T value = getInner(innerIndex);
/* 116 */     if (index == 0) {
/* 117 */       this.contents[innerIndex] = null;
/* 118 */       this.head++;
/* 119 */     } else if (index == this.size - 1) {
/* 120 */       this.contents[innerIndex] = null;
/*     */     } else {
/* 122 */       for (int i = index + 1; i < this.size; i++) {
/* 123 */         this.contents[getIndex(i - 1)] = get(i);
/*     */       }
/* 125 */       this.contents[getIndex(this.size - 1)] = null;
/*     */     } 
/* 127 */     this.modCount++;
/* 128 */     this.size--;
/* 129 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeIf(Predicate<? super T> filter) {
/* 134 */     int removed = 0;
/* 135 */     for (int i = 0; i < this.size; i++) {
/* 136 */       T value = get(i);
/* 137 */       if (filter.test(value)) {
/* 138 */         removed++;
/* 139 */       } else if (removed != 0) {
/* 140 */         this.contents[getIndex(i - removed)] = value;
/* 141 */         this.contents[getIndex(i)] = null;
/*     */       } 
/*     */     } 
/* 144 */     this.modCount += removed;
/* 145 */     this.size -= removed;
/* 146 */     return (removed != 0);
/*     */   }
/*     */   
/*     */   private void copyCount(Object[] newContents, int count) {
/* 150 */     for (int i = 0; i < count; i++) {
/* 151 */       newContents[i] = get(i);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void replaceAll(UnaryOperator<T> operator) {
/* 157 */     for (int i = 0; i < this.size; i++) {
/* 158 */       int index = getIndex(i);
/* 159 */       this.contents[index] = Objects.requireNonNull(operator.apply(getInner(i)));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void forEach(Consumer<? super T> action) {
/* 165 */     for (int i = 0; i < this.size; i++) {
/* 166 */       action.accept(get(i));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void addFirst(T value) {
/* 172 */     add(0, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addLast(T value) {
/* 177 */     add(this.size, value);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offerFirst(T value) {
/* 182 */     addFirst(value);
/* 183 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean offerLast(T value) {
/* 188 */     addLast(value);
/* 189 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public T removeFirst() {
/* 194 */     if (this.size == 0) {
/* 195 */       throw new NoSuchElementException();
/*     */     }
/* 197 */     return remove(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public T removeLast() {
/* 202 */     if (this.size == 0) {
/* 203 */       throw new NoSuchElementException();
/*     */     }
/* 205 */     return remove(this.size - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public ListAndDeque<T> reversed() {
/* 210 */     return new ReversedView(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public T pollFirst() {
/* 215 */     if (this.size == 0) {
/* 216 */       return null;
/*     */     }
/* 218 */     return removeFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public T pollLast() {
/* 223 */     if (this.size == 0) {
/* 224 */       return null;
/*     */     }
/* 226 */     return removeLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public T getFirst() {
/* 231 */     if (this.size == 0) {
/* 232 */       throw new NoSuchElementException();
/*     */     }
/* 234 */     return get(0);
/*     */   }
/*     */ 
/*     */   
/*     */   public T getLast() {
/* 239 */     if (this.size == 0) {
/* 240 */       throw new NoSuchElementException();
/*     */     }
/* 242 */     return get(this.size - 1);
/*     */   }
/*     */ 
/*     */   
/*     */   public T peekFirst() {
/* 247 */     if (this.size == 0) {
/* 248 */       return null;
/*     */     }
/* 250 */     return getFirst();
/*     */   }
/*     */ 
/*     */   
/*     */   public T peekLast() {
/* 255 */     if (this.size == 0) {
/* 256 */       return null;
/*     */     }
/* 258 */     return getLast();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeFirstOccurrence(Object o) {
/* 263 */     for (int i = 0; i < this.size; i++) {
/* 264 */       T value = get(i);
/* 265 */       if (Objects.equals(o, value)) {
/* 266 */         remove(i);
/* 267 */         return true;
/*     */       } 
/*     */     } 
/* 270 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeLastOccurrence(Object o) {
/* 275 */     for (int i = this.size - 1; i >= 0; i--) {
/* 276 */       T value = get(i);
/* 277 */       if (Objects.equals(o, value)) {
/* 278 */         remove(i);
/* 279 */         return true;
/*     */       } 
/*     */     } 
/* 282 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Iterator<T> descendingIterator() {
/* 287 */     return new DescendingIterator();
/*     */   }
/*     */ 
/*     */   
/*     */   private class DescendingIterator
/*     */     implements Iterator<T>
/*     */   {
/* 294 */     private int index = ArrayListDeque.this.size() - 1;
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean hasNext() {
/* 299 */       return (this.index >= 0);
/*     */     }
/*     */ 
/*     */     
/*     */     public T next() {
/* 304 */       return ArrayListDeque.this.get(this.index--);
/*     */     }
/*     */ 
/*     */     
/*     */     public void remove() {
/* 309 */       ArrayListDeque.this.remove(this.index + 1);
/*     */     }
/*     */   }
/*     */   
/*     */   private class ReversedView extends AbstractList<T> implements ListAndDeque<T> {
/*     */     private final ArrayListDeque<T> source;
/*     */     
/*     */     public ReversedView(ArrayListDeque<T> source) {
/* 317 */       this.source = source;
/*     */     }
/*     */ 
/*     */     
/*     */     public ListAndDeque<T> reversed() {
/* 322 */       return this.source;
/*     */     }
/*     */ 
/*     */     
/*     */     public T getFirst() {
/* 327 */       return this.source.getLast();
/*     */     }
/*     */ 
/*     */     
/*     */     public T getLast() {
/* 332 */       return this.source.getFirst();
/*     */     }
/*     */ 
/*     */     
/*     */     public void addFirst(T t) {
/* 337 */       this.source.addLast(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public void addLast(T t) {
/* 342 */       this.source.addFirst(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean offerFirst(T t) {
/* 347 */       return this.source.offerLast(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean offerLast(T t) {
/* 352 */       return this.source.offerFirst(t);
/*     */     }
/*     */ 
/*     */     
/*     */     public T pollFirst() {
/* 357 */       return this.source.pollLast();
/*     */     }
/*     */ 
/*     */     
/*     */     public T pollLast() {
/* 362 */       return this.source.pollFirst();
/*     */     }
/*     */ 
/*     */     
/*     */     public T peekFirst() {
/* 367 */       return this.source.peekLast();
/*     */     }
/*     */ 
/*     */     
/*     */     public T peekLast() {
/* 372 */       return this.source.peekFirst();
/*     */     }
/*     */ 
/*     */     
/*     */     public T removeFirst() {
/* 377 */       return this.source.removeLast();
/*     */     }
/*     */ 
/*     */     
/*     */     public T removeLast() {
/* 382 */       return this.source.removeFirst();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeFirstOccurrence(Object o) {
/* 387 */       return this.source.removeLastOccurrence(o);
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean removeLastOccurrence(Object o) {
/* 392 */       return this.source.removeFirstOccurrence(o);
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<T> descendingIterator() {
/* 397 */       return this.source.iterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public int size() {
/* 402 */       return this.source.size();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean isEmpty() {
/* 407 */       return this.source.isEmpty();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean contains(Object o) {
/* 412 */       return this.source.contains(o);
/*     */     }
/*     */ 
/*     */     
/*     */     public T get(int index) {
/* 417 */       return this.source.get(reverseIndex(index));
/*     */     }
/*     */ 
/*     */     
/*     */     public T set(int index, T element) {
/* 422 */       return this.source.set(reverseIndex(index), element);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public void add(int index, T element) {
/* 428 */       this.source.add(reverseIndex(index) + 1, element);
/*     */     }
/*     */ 
/*     */     
/*     */     public T remove(int index) {
/* 433 */       return this.source.remove(reverseIndex(index));
/*     */     }
/*     */ 
/*     */     
/*     */     public int indexOf(Object o) {
/* 438 */       return reverseIndex(this.source.lastIndexOf(o));
/*     */     }
/*     */ 
/*     */     
/*     */     public int lastIndexOf(Object o) {
/* 443 */       return reverseIndex(this.source.indexOf(o));
/*     */     }
/*     */ 
/*     */     
/*     */     public List<T> subList(int fromIndex, int toIndex) {
/* 448 */       return this.source.subList(reverseIndex(toIndex) + 1, reverseIndex(fromIndex) + 1).reversed();
/*     */     }
/*     */ 
/*     */     
/*     */     public Iterator<T> iterator() {
/* 453 */       return this.source.descendingIterator();
/*     */     }
/*     */ 
/*     */     
/*     */     public void clear() {
/* 458 */       this.source.clear();
/*     */     }
/*     */     
/*     */     private int reverseIndex(int index) {
/* 462 */       return (index == -1) ? -1 : (this.source.size() - 1 - index);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/ArrayListDeque.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */