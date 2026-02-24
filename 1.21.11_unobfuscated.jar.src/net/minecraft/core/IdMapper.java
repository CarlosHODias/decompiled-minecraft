/*    */ package net.minecraft.core;
/*    */ 
/*    */ import com.google.common.collect.Iterators;
/*    */ import com.google.common.collect.Lists;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2IntMap;
/*    */ import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ 
/*    */ 
/*    */ public class IdMapper<T>
/*    */   implements IdMap<T>
/*    */ {
/*    */   private int nextId;
/*    */   private final Reference2IntMap<T> tToId;
/*    */   private final List<T> idToT;
/*    */   
/*    */   public IdMapper() {
/* 20 */     this(512);
/*    */   }
/*    */   
/*    */   public IdMapper(int expectedSize) {
/* 24 */     this.idToT = Lists.newArrayListWithExpectedSize(expectedSize);
/* 25 */     this.tToId = (Reference2IntMap<T>)new Reference2IntOpenHashMap(expectedSize);
/* 26 */     this.tToId.defaultReturnValue(-1);
/*    */   }
/*    */   
/*    */   public void addMapping(T thing, int id) {
/* 30 */     this.tToId.put(thing, id);
/*    */ 
/*    */     
/* 33 */     while (this.idToT.size() <= id) {
/* 34 */       this.idToT.add(null);
/*    */     }
/*    */     
/* 37 */     this.idToT.set(id, thing);
/*    */     
/* 39 */     if (this.nextId <= id) {
/* 40 */       this.nextId = id + 1;
/*    */     }
/*    */   }
/*    */   
/*    */   public void add(T thing) {
/* 45 */     addMapping(thing, this.nextId);
/*    */   }
/*    */ 
/*    */   
/*    */   public int getId(T thing) {
/* 50 */     return this.tToId.getInt(thing);
/*    */   }
/*    */ 
/*    */   
/*    */   public final T byId(int id) {
/* 55 */     if (id >= 0 && id < this.idToT.size()) {
/* 56 */       return this.idToT.get(id);
/*    */     }
/*    */     
/* 59 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<T> iterator() {
/* 64 */     return (Iterator<T>)Iterators.filter(this.idToT.iterator(), Objects::nonNull);
/*    */   }
/*    */   
/*    */   public boolean contains(int id) {
/* 68 */     return (byId(id) != null);
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 73 */     return this.tToId.size();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/IdMapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */