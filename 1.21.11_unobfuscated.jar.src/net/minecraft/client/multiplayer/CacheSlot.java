/*    */ package net.minecraft.client.multiplayer;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CacheSlot<C extends CacheSlot.Cleaner<C>, D>
/*    */ {
/*    */   private final Function<C, D> operation;
/*    */   private C context;
/*    */   private D value;
/*    */   
/*    */   public CacheSlot(Function<C, D> operation) {
/* 19 */     this.operation = operation;
/*    */   }
/*    */   
/*    */   public D compute(C context) {
/* 23 */     if (context == this.context && this.value != null) {
/* 24 */       return this.value;
/*    */     }
/*    */     
/* 27 */     D newValue = this.operation.apply(context);
/* 28 */     this.value = newValue;
/* 29 */     this.context = context;
/* 30 */     context.registerForCleaning(this);
/* 31 */     return newValue;
/*    */   }
/*    */   
/*    */   public void clear() {
/* 35 */     this.value = null;
/* 36 */     this.context = null;
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Cleaner<C extends Cleaner<C>> {
/*    */     void registerForCleaning(CacheSlot<C, ?> param1CacheSlot);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/CacheSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */