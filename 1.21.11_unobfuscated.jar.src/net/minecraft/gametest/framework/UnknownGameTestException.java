/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class UnknownGameTestException extends GameTestException {
/*    */   private final Throwable reason;
/*    */   
/*    */   public UnknownGameTestException(Throwable reason) {
/*  9 */     super(reason.getMessage());
/* 10 */     this.reason = reason;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getDescription() {
/* 15 */     return (Component)Component.translatable("test.error.unknown", new Object[] { this.reason.getMessage() });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/UnknownGameTestException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */