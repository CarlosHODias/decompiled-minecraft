/*    */ package net.minecraft.world.level.chunk.storage;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import it.unimi.dsi.fastutil.ints.IntCollection;
/*    */ import it.unimi.dsi.fastutil.ints.IntSet;
/*    */ import java.util.BitSet;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ public class RegionBitmap {
/* 10 */   private final BitSet used = new BitSet();
/*    */   
/*    */   public void force(int position, int size) {
/* 13 */     this.used.set(position, position + size);
/*    */   }
/*    */   
/*    */   public void free(int position, int size) {
/* 17 */     this.used.clear(position, position + size);
/*    */   }
/*    */   
/*    */   public int allocate(int size) {
/* 21 */     int current = 0;
/*    */     while (true) {
/* 23 */       int freeStart = this.used.nextClearBit(current);
/* 24 */       int freeEnd = this.used.nextSetBit(freeStart);
/* 25 */       if (freeEnd == -1 || freeEnd - freeStart >= size) {
/* 26 */         force(freeStart, size);
/* 27 */         return freeStart;
/*    */       } 
/* 29 */       current = freeEnd;
/*    */     } 
/*    */   }
/*    */   
/*    */   @VisibleForTesting
/*    */   public IntSet getUsed() {
/* 35 */     return this.used.stream().<IntSet>collect(it.unimi.dsi.fastutil.ints.IntArraySet::new, IntCollection::add, IntCollection::addAll);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/storage/RegionBitmap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */