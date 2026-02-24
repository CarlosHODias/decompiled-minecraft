/*    */ package net.minecraft.nbt;
/*    */ 
/*    */ import java.util.Iterator;
/*    */ import java.util.NoSuchElementException;
/*    */ import java.util.stream.Stream;
/*    */ import java.util.stream.StreamSupport;
/*    */ 
/*    */ public interface CollectionTag extends Tag, Iterable<Tag> {
/*    */   void clear();
/*    */   
/*    */   boolean setTag(int paramInt, Tag paramTag);
/*    */   
/*    */   boolean addTag(int paramInt, Tag paramTag);
/*    */   
/*    */   Tag remove(int paramInt);
/*    */   
/*    */   Tag get(int paramInt);
/*    */   
/*    */   int size();
/*    */   
/*    */   default boolean isEmpty() {
/* 22 */     return (size() == 0);
/*    */   }
/*    */ 
/*    */   
/*    */   default Iterator<Tag> iterator() {
/* 27 */     return new Iterator<Tag>()
/*    */       {
/*    */         private int index;
/*    */         
/*    */         public boolean hasNext() {
/* 32 */           return (this.index < CollectionTag.this.size());
/*    */         }
/*    */ 
/*    */         
/*    */         public Tag next() {
/* 37 */           if (!hasNext()) {
/* 38 */             throw new NoSuchElementException();
/*    */           }
/* 40 */           return CollectionTag.this.get(this.index++);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   default Stream<Tag> stream() {
/* 46 */     return StreamSupport.stream(spliterator(), false);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/nbt/CollectionTag.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */