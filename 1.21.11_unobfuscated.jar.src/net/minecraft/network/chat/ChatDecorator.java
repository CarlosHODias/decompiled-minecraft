/*   */ package net.minecraft.network.chat;
/*   */ 
/*   */ import net.minecraft.server.level.ServerPlayer;
/*   */ 
/*   */ @FunctionalInterface
/*   */ public interface ChatDecorator {
/*   */   static {
/* 8 */     PLAIN = ((player, plain) -> plain);
/*   */   }
/*   */   
/*   */   public static final ChatDecorator PLAIN;
/*   */   
/*   */   Component decorate(ServerPlayer paramServerPlayer, Component paramComponent);
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/ChatDecorator.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */