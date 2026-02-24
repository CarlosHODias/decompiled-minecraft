/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.AbstractList;
/*    */ import java.util.Arrays;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class NonNullList<E> extends AbstractList<E> {
/*    */   private final List<E> list;
/*    */   
/*    */   public static <E> NonNullList<E> create() {
/* 13 */     return new NonNullList<>(Lists.newArrayList(), null);
/*    */   }
/*    */   private final E defaultValue;
/*    */   public static <E> NonNullList<E> createWithCapacity(int capacity) {
/* 17 */     return new NonNullList<>(Lists.newArrayListWithCapacity(capacity), null);
/*    */   }
/*    */ 
/*    */   
/*    */   public static <E> NonNullList<E> withSize(int size, E defaultValue) {
/* 22 */     Objects.requireNonNull(defaultValue);
/*    */     
/* 24 */     Object[] objects = new Object[size];
/* 25 */     Arrays.fill(objects, defaultValue);
/* 26 */     return new NonNullList<>(Arrays.asList((E[])objects), defaultValue);
/*    */   }
/*    */   
/*    */   @SafeVarargs
/*    */   public static <E> NonNullList<E> of(E defaultValue, E... values) {
/* 31 */     return new NonNullList<>(Arrays.asList(values), defaultValue);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected NonNullList(List<E> list, E defaultValue) {
/* 38 */     this.list = list;
/* 39 */     this.defaultValue = defaultValue;
/*    */   }
/*    */ 
/*    */   
/*    */   public E get(int index) {
/* 44 */     return this.list.get(index);
/*    */   }
/*    */ 
/*    */   
/*    */   public E set(int index, E element) {
/* 49 */     Objects.requireNonNull(element);
/*    */     
/* 51 */     return this.list.set(index, element);
/*    */   }
/*    */ 
/*    */   
/*    */   public void add(int index, E element) {
/* 56 */     Objects.requireNonNull(element);
/*    */     
/* 58 */     this.list.add(index, element);
/*    */   }
/*    */ 
/*    */   
/*    */   public E remove(int index) {
/* 63 */     return this.list.remove(index);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 68 */     return this.list.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public void clear() {
/* 73 */     if (this.defaultValue == null) {
/* 74 */       super.clear();
/*    */     } else {
/* 76 */       for (int i = 0; i < size(); i++)
/* 77 */         set(i, this.defaultValue); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/NonNullList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */