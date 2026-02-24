/*    */ package com.mojang.realmsclient.gui.task;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public interface RepeatedDelayStrategy {
/*  7 */   public static final RepeatedDelayStrategy CONSTANT = new RepeatedDelayStrategy()
/*    */     {
/*    */       public long delayCyclesAfterSuccess() {
/* 10 */         return 1L;
/*    */       }
/*    */ 
/*    */       
/*    */       public long delayCyclesAfterFailure() {
/* 15 */         return 1L;
/*    */       }
/*    */     };
/*    */   
/*    */   long delayCyclesAfterSuccess();
/*    */   
/*    */   long delayCyclesAfterFailure();
/*    */   
/*    */   static RepeatedDelayStrategy exponentialBackoff(final int maxBackoff) {
/* 24 */     return new RepeatedDelayStrategy() {
/* 25 */         private static final Logger LOGGER = LogUtils.getLogger();
/*    */         
/*    */         private int failureCount;
/*    */ 
/*    */         
/*    */         public long delayCyclesAfterSuccess() {
/* 31 */           this.failureCount = 0;
/* 32 */           return 1L;
/*    */         }
/*    */ 
/*    */         
/*    */         public long delayCyclesAfterFailure() {
/* 37 */           this.failureCount++;
/* 38 */           long expandedDelay = Math.min(1L << this.failureCount, maxBackoff);
/* 39 */           LOGGER.debug("Skipping for {} extra cycles", expandedDelay);
/* 40 */           return expandedDelay;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/task/RepeatedDelayStrategy.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */