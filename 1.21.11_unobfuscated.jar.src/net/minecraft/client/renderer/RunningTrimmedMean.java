/*    */ package net.minecraft.client.renderer;
/*    */ 
/*    */ public class RunningTrimmedMean {
/*    */   private final long[] values;
/*    */   private int count;
/*    */   private int cursor;
/*    */   
/*    */   public RunningTrimmedMean(int maxCount) {
/*  9 */     this.values = new long[maxCount];
/*    */   }
/*    */   
/*    */   public long registerValueAndGetMean(long value) {
/* 13 */     if (this.count < this.values.length) {
/* 14 */       this.count++;
/*    */     }
/* 16 */     this.values[this.cursor] = value;
/* 17 */     this.cursor = (this.cursor + 1) % this.values.length;
/*    */     
/* 19 */     long min = Long.MAX_VALUE;
/* 20 */     long max = Long.MIN_VALUE;
/*    */     
/* 22 */     long total = 0L;
/* 23 */     for (int i = 0; i < this.count; i++) {
/* 24 */       long current = this.values[i];
/* 25 */       total += current;
/* 26 */       min = Math.min(min, current);
/* 27 */       max = Math.max(max, current);
/*    */     } 
/*    */     
/* 30 */     if (this.count > 2) {
/* 31 */       total -= min + max;
/* 32 */       return total / (this.count - 2);
/*    */     } 
/*    */     
/* 35 */     if (total > 0L) {
/* 36 */       return this.count / total;
/*    */     }
/* 38 */     return 0L;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/RunningTrimmedMean.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */