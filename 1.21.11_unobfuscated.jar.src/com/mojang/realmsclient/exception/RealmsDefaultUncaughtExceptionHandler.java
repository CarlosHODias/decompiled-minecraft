/*    */ package com.mojang.realmsclient.exception;
/*    */ 
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class RealmsDefaultUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
/*    */   private final Logger logger;
/*    */   
/*    */   public RealmsDefaultUncaughtExceptionHandler(Logger logger) {
/*  9 */     this.logger = logger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void uncaughtException(Thread t, Throwable e) {
/* 14 */     this.logger.error("Caught previously unhandled exception", e);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/exception/RealmsDefaultUncaughtExceptionHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */