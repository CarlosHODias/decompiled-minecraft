/*    */ package net.minecraft.client.searchtree;
/*    */ 
/*    */ import com.google.common.collect.AbstractIterator;
/*    */ import com.google.common.collect.Iterators;
/*    */ import com.google.common.collect.PeekingIterator;
/*    */ import java.util.Comparator;
/*    */ import java.util.Iterator;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class IntersectionIterator<T>
/*    */   extends AbstractIterator<T>
/*    */ {
/*    */   private final PeekingIterator<T> firstIterator;
/*    */   private final PeekingIterator<T> secondIterator;
/*    */   private final Comparator<T> comparator;
/*    */   
/*    */   public IntersectionIterator(Iterator<T> firstIterator, Iterator<T> secondIterator, Comparator<T> comparator) {
/* 19 */     this.firstIterator = Iterators.peekingIterator(firstIterator);
/* 20 */     this.secondIterator = Iterators.peekingIterator(secondIterator);
/*    */     
/* 22 */     this.comparator = comparator;
/*    */   }
/*    */ 
/*    */   
/*    */   protected T computeNext() {
/*    */     while (true) {
/* 28 */       if (!this.firstIterator.hasNext() || !this.secondIterator.hasNext()) {
/* 29 */         return (T)endOfData();
/*    */       }
/*    */       
/* 32 */       int compare = this.comparator.compare((T)this.firstIterator.peek(), (T)this.secondIterator.peek());
/* 33 */       if (compare == 0) {
/* 34 */         this.secondIterator.next();
/* 35 */         return (T)this.firstIterator.next();
/*    */       } 
/*    */       
/* 38 */       if (compare < 0) {
/* 39 */         this.firstIterator.next(); continue;
/*    */       } 
/* 41 */       this.secondIterator.next();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/searchtree/IntersectionIterator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */