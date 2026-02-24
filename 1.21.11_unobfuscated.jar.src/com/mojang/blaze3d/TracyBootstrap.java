/*    */ package com.mojang.blaze3d;
/*    */ 
/*    */ import com.mojang.jtracy.TracyClient;
/*    */ import com.mojang.logging.LogListeners;
/*    */ import org.slf4j.event.Level;
/*    */ 
/*    */ public class TracyBootstrap {
/*    */   private static boolean setup;
/*    */   
/*    */   public static void setup() {
/* 11 */     if (setup) {
/*    */       return;
/*    */     }
/* 14 */     TracyClient.load();
/* 15 */     if (!TracyClient.isAvailable()) {
/*    */       return;
/*    */     }
/* 18 */     LogListeners.addListener("Tracy", (message, level) -> TracyClient.message(message, messageColor(level)));
/* 19 */     setup = true;
/*    */   }
/*    */   
/*    */   private static int messageColor(Level level) {
/* 23 */     switch (level) { default: case DEBUG: case WARN: case ERROR: break; }  return 
/*    */ 
/*    */ 
/*    */       
/* 27 */       16755370;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/TracyBootstrap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */