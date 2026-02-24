/*    */ package net.minecraft.util;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface AbortableIterationConsumer<T> {
/*    */   Continuation accept(T paramT);
/*    */   
/*    */   public enum Continuation {
/*  8 */     CONTINUE,
/*  9 */     ABORT;
/*    */     
/*    */     public boolean shouldAbort() {
/* 12 */       return (this == ABORT);
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static <T> AbortableIterationConsumer<T> forConsumer(java.util.function.Consumer<T> consumer) {
/* 24 */     return e -> {
/*    */         consumer.accept(e);
/*    */         return Continuation.CONTINUE;
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/AbortableIterationConsumer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */