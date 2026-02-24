/*    */ package net.minecraft.commands;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface CommandResultCallback {
/*  5 */   public static final CommandResultCallback EMPTY = new CommandResultCallback()
/*    */     {
/*    */       public void onResult(boolean success, int result) {}
/*    */ 
/*    */ 
/*    */       
/*    */       public String toString() {
/* 12 */         return "<empty>";
/*    */       }
/*    */     };
/*    */   
/*    */   void onResult(boolean paramBoolean, int paramInt);
/*    */   
/*    */   default void onSuccess(int result) {
/* 19 */     onResult(true, result);
/*    */   }
/*    */   
/*    */   default void onFailure() {
/* 23 */     onResult(false, 0);
/*    */   }
/*    */   
/*    */   static CommandResultCallback chain(CommandResultCallback first, CommandResultCallback second) {
/* 27 */     if (first == EMPTY) {
/* 28 */       return second;
/*    */     }
/*    */     
/* 31 */     if (second == EMPTY) {
/* 32 */       return first;
/*    */     }
/*    */ 
/*    */ 
/*    */     
/* 37 */     return (success, result) -> {
/*    */         first.onResult(success, result);
/*    */         second.onResult(success, result);
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/CommandResultCallback.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */