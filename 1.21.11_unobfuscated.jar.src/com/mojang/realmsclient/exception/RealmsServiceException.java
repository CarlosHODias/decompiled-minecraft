/*    */ package com.mojang.realmsclient.exception;
/*    */ 
/*    */ import com.mojang.realmsclient.client.RealmsError;
/*    */ 
/*    */ public class RealmsServiceException extends Exception {
/*    */   public final RealmsError realmsError;
/*    */   
/*    */   public RealmsServiceException(RealmsError error) {
/*  9 */     this.realmsError = error;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 14 */     return this.realmsError.logMessage();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/exception/RealmsServiceException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */