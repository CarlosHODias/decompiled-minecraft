/*   */ package net.minecraft.gametest.framework;
/*   */ 
/*   */ import net.minecraft.network.chat.Component;
/*   */ 
/*   */ public abstract class GameTestException extends RuntimeException {
/*   */   public GameTestException(String message) {
/* 7 */     super(message);
/*   */   }
/*   */   
/*   */   public abstract Component getDescription();
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/GameTestException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */