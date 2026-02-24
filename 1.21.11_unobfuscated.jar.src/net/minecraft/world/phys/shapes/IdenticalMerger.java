/*    */ package net.minecraft.world.phys.shapes;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.doubles.DoubleList;
/*    */ 
/*    */ public class IdenticalMerger implements IndexMerger {
/*    */   private final DoubleList coords;
/*    */   
/*    */   public IdenticalMerger(DoubleList coords) {
/*  9 */     this.coords = coords;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean forMergedIndexes(IndexMerger.IndexConsumer consumer) {
/* 14 */     int size = this.coords.size() - 1;
/* 15 */     for (int i = 0; i < size; i++) {
/* 16 */       if (!consumer.merge(i, i, i)) {
/* 17 */         return false;
/*    */       }
/*    */     } 
/* 20 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public int size() {
/* 25 */     return this.coords.size();
/*    */   }
/*    */ 
/*    */   
/*    */   public DoubleList getList() {
/* 30 */     return this.coords;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/phys/shapes/IdenticalMerger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */