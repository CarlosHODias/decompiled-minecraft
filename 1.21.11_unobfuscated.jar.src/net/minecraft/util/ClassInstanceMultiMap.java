/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.Iterators;
/*    */ import com.google.common.collect.Lists;
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.AbstractCollection;
/*    */ import java.util.Collection;
/*    */ import java.util.Collections;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class ClassInstanceMultiMap<T> extends AbstractCollection<T> {
/* 16 */   private final Map<Class<?>, List<T>> byClass = Maps.newHashMap();
/*    */   
/*    */   private final Class<T> baseClass;
/* 19 */   private final List<T> allInstances = Lists.newArrayList();
/*    */   
/*    */   public ClassInstanceMultiMap(Class<T> baseClass) {
/* 22 */     this.baseClass = baseClass;
/* 23 */     this.byClass.put(baseClass, this.allInstances);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean add(T instance) {
/*    */     boolean success = false;
/* 29 */     for (Map.Entry<Class<?>, List<T>> entry : this.byClass.entrySet()) {
/* 30 */       if (((Class)entry.getKey()).isInstance(instance)) {
/* 31 */         success |= ((List<T>)entry.getValue()).add(instance);
/*    */       }
/*    */     } 
/* 34 */     return success;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean remove(Object object) {
/*    */     boolean success = false;
/* 40 */     for (Map.Entry<Class<?>, List<T>> entry : this.byClass.entrySet()) {
/* 41 */       if (((Class)entry.getKey()).isInstance(object)) {
/* 42 */         List<T> list = entry.getValue();
/* 43 */         success |= list.remove(object);
/*    */       } 
/*    */     } 
/* 46 */     return success;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean contains(Object o) {
/* 51 */     return find(o.getClass()).contains(o);
/*    */   }
/*    */ 
/*    */   
/*    */   public <S> Collection<S> find(Class<S> index) {
/* 56 */     if (!this.baseClass.isAssignableFrom(index)) {
/* 57 */       throw new IllegalArgumentException("Don't know how to search for " + String.valueOf(index));
/*    */     }
/* 59 */     List<? extends T> instances = this.byClass.computeIfAbsent(index, k -> { Objects.requireNonNull(k); return this.allInstances.stream().filter(k::isInstance).collect(Util.toMutableList());
/* 60 */         }); return Collections.unmodifiableCollection((Collection)instances);
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<T> iterator() {
/* 65 */     if (this.allInstances.isEmpty()) {
/* 66 */       return Collections.emptyIterator();
/*    */     }
/* 68 */     return (Iterator<T>)Iterators.unmodifiableIterator(this.allInstances.iterator());
/*    */   }
/*    */   
/*    */   public List<T> getAllInstances() {
/* 72 */     return (List<T>)ImmutableList.copyOf(this.allInstances);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 77 */     return this.allInstances.size();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/ClassInstanceMultiMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */