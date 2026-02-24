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
/*    */ 
/*    */ public class MergingUniqueIterator<T>
/*    */   extends AbstractIterator<T>
/*    */ {
/*    */   private final PeekingIterator<T> firstIterator;
/*    */   private final PeekingIterator<T> secondIterator;
/*    */   private final Comparator<T> comparator;
/*    */   
/*    */   public MergingUniqueIterator(Iterator<T> firstIterator, Iterator<T> secondIterator, Comparator<T> comparator) {
/* 20 */     this.firstIterator = Iterators.peekingIterator(firstIterator);
/* 21 */     this.secondIterator = Iterators.peekingIterator(secondIterator);
/*    */     
/* 23 */     this.comparator = comparator;
/*    */   }
/*    */ 
/*    */   
/*    */   protected T computeNext() {
/* 28 */     boolean firstEmpty = !this.firstIterator.hasNext();
/* 29 */     boolean secondEmpty = !this.secondIterator.hasNext();
/* 30 */     if (firstEmpty && secondEmpty) {
/* 31 */       return (T)endOfData();
/*    */     }
/*    */     
/* 34 */     if (firstEmpty) {
/* 35 */       return (T)this.secondIterator.next();
/*    */     }
/* 37 */     if (secondEmpty) {
/* 38 */       return (T)this.firstIterator.next();
/*    */     }
/*    */     
/* 41 */     int compare = this.comparator.compare((T)this.firstIterator.peek(), (T)this.secondIterator.peek());
/* 42 */     if (compare == 0) {
/* 43 */       this.secondIterator.next();
/*    */     }
/*    */     
/* 46 */     return (compare <= 0) ? (T)this.firstIterator.next() : (T)this.secondIterator.next();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/searchtree/MergingUniqueIterator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */