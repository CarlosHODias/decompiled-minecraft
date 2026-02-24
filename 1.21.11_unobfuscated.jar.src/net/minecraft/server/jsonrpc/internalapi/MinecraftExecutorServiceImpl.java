/*    */ package net.minecraft.server.jsonrpc.internalapi;
/*    */ 
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.server.dedicated.DedicatedServer;
/*    */ 
/*    */ public class MinecraftExecutorServiceImpl
/*    */   implements MinecraftExecutorService
/*    */ {
/*    */   private final DedicatedServer server;
/*    */   
/*    */   public MinecraftExecutorServiceImpl(DedicatedServer server) {
/* 13 */     this.server = server;
/*    */   }
/*    */ 
/*    */   
/*    */   public <V> CompletableFuture<V> submit(Supplier<V> supplier) {
/* 18 */     return this.server.submit(supplier);
/*    */   }
/*    */ 
/*    */   
/*    */   public CompletableFuture<Void> submit(Runnable runnable) {
/* 23 */     return this.server.submit(runnable);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/internalapi/MinecraftExecutorServiceImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */