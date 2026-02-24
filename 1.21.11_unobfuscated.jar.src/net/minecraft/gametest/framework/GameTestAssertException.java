/*    */ package net.minecraft.gametest.framework;
/*    */ 
/*    */ import net.minecraft.network.chat.Component;
/*    */ 
/*    */ public class GameTestAssertException extends GameTestException {
/*    */   protected final Component message;
/*    */   protected final int tick;
/*    */   
/*    */   public GameTestAssertException(Component message, int tick) {
/* 10 */     super(message.getString());
/* 11 */     this.message = message;
/* 12 */     this.tick = tick;
/*    */   }
/*    */ 
/*    */   
/*    */   public Component getDescription() {
/* 17 */     return (Component)Component.translatable("test.error.tick", new Object[] { this.message, this.tick });
/*    */   }
/*    */ 
/*    */   
/*    */   public String getMessage() {
/* 22 */     return getDescription().getString();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/GameTestAssertException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */