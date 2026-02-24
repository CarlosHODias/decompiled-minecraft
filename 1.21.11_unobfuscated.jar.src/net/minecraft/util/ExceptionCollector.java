/*    */ package net.minecraft.util;
/*    */ 
/*    */ 
/*    */ public class ExceptionCollector<T extends Throwable>
/*    */ {
/*    */   private T result;
/*    */   
/*    */   public void add(T throwable) {
/*  9 */     if (this.result == null) {
/* 10 */       this.result = throwable;
/*    */     } else {
/* 12 */       this.result.addSuppressed((Throwable)throwable);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void throwIfPresent() throws T {
/* 17 */     if (this.result != null)
/* 18 */       throw this.result; 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/ExceptionCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */