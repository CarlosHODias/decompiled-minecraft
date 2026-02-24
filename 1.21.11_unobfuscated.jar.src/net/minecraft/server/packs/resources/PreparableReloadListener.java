/*    */ package net.minecraft.server.packs.resources;
/*    */ 
/*    */ import java.util.IdentityHashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface PreparableReloadListener
/*    */ {
/*    */   CompletableFuture<Void> reload(SharedState paramSharedState, Executor paramExecutor1, PreparationBarrier paramPreparationBarrier, Executor paramExecutor2);
/*    */   
/*    */   default void prepareSharedState(SharedState currentReload) {}
/*    */   
/*    */   default String getName() {
/* 17 */     return getClass().getSimpleName();
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface PreparationBarrier
/*    */   {
/*    */     <T> CompletableFuture<T> wait(T param1T);
/*    */   }
/*    */   
/*    */   public static final class StateKey<T> {}
/*    */   
/*    */   public static final class SharedState {
/*    */     private final ResourceManager manager;
/* 30 */     private final Map<PreparableReloadListener.StateKey<?>, Object> state = new IdentityHashMap<>();
/*    */     
/*    */     public SharedState(ResourceManager manager) {
/* 33 */       this.manager = manager;
/*    */     }
/*    */     
/*    */     public ResourceManager resourceManager() {
/* 37 */       return this.manager;
/*    */     }
/*    */     
/*    */     public <T> void set(PreparableReloadListener.StateKey<T> key, T value) {
/* 41 */       this.state.put(key, value);
/*    */     }
/*    */ 
/*    */     
/*    */     public <T> T get(PreparableReloadListener.StateKey<T> key) {
/* 46 */       return Objects.requireNonNull((T)this.state.get(key));
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/resources/PreparableReloadListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */