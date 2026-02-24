/*    */ package net.minecraft.server;
/*    */ 
/*    */ import net.minecraft.commands.CommandSourceStack;
/*    */ 
/*    */ public class ConsoleInput {
/*    */   public final String msg;
/*    */   public final CommandSourceStack source;
/*    */   
/*    */   public ConsoleInput(String msg, CommandSourceStack source) {
/* 10 */     this.msg = msg;
/* 11 */     this.source = source;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/ConsoleInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */