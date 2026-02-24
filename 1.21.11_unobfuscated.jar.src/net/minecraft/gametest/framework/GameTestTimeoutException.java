/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class GameTestTimeoutException extends GameTestException {
/*    */   protected final Component message;
/*    */   
/*    */   public GameTestTimeoutException(Component message) {
/*  9 */     super(message.getString());
/* 10 */     this.message = message;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getDescription() {
/* 15 */     return this.message;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/GameTestTimeoutException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */