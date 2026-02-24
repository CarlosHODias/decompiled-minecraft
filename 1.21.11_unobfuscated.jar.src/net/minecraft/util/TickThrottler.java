/*    */ package net.minecraft.util;
/*    */ 
/*    */ public class TickThrottler {
/*    */   private final int incrementStep;
/*    */   private final int threshold;
/*    */   private int count;
/*    */   
/*    */   public TickThrottler(int incrementStep, int threshold) {
/*  9 */     this.incrementStep = incrementStep;
/* 10 */     this.threshold = threshold;
/*    */   }
/*    */   
/*    */   public void increment() {
/* 14 */     this.count += this.incrementStep;
/*    */   }
/*    */   
/*    */   public void tick() {
/* 18 */     if (this.count > 0) {
/* 19 */       this.count--;
/*    */     }
/*    */   }
/*    */   
/*    */   public boolean isUnderThreshold() {
/* 24 */     return (this.count < this.threshold);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/TickThrottler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */