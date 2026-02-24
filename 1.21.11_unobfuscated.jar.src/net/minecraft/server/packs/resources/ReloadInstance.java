/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ 
/*    */ public interface ReloadInstance {
/*    */   CompletableFuture<?> done();
/*    */   
/*    */   float getActualProgress();
/*    */   
/*    */   default boolean isDone() {
/* 11 */     return done().isDone();
/*    */   }
/*    */   
/*    */   default void checkExceptions() {
/* 15 */     CompletableFuture<?> done = done();
/* 16 */     if (done.isCompletedExceptionally())
/* 17 */       done.join(); 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/resources/ReloadInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */