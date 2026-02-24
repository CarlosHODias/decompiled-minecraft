/*    */ package net.minecraft.util;
/*    */ 
/*    */ import java.util.concurrent.TimeUnit;
/*    */ import java.util.function.LongSupplier;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface TimeSource {
/*    */   long get(TimeUnit paramTimeUnit);
/*    */   
/*    */   public static interface NanoTimeSource
/*    */     extends LongSupplier, TimeSource {
/*    */     default long get(TimeUnit timeUnit) {
/* 13 */       return timeUnit.convert(getAsLong(), TimeUnit.NANOSECONDS);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/TimeSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */