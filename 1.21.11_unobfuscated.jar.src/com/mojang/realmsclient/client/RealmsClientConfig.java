/*    */ package com.mojang.realmsclient.client;
/*    */ 
/*    */ import java.net.Proxy;
/*    */ 
/*    */ 
/*    */ public class RealmsClientConfig
/*    */ {
/*    */   private static Proxy proxy;
/*    */   
/*    */   public static Proxy getProxy() {
/* 11 */     return proxy;
/*    */   }
/*    */   
/*    */   public static void setProxy(Proxy proxy) {
/* 15 */     if (RealmsClientConfig.proxy == null)
/* 16 */       RealmsClientConfig.proxy = proxy; 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/client/RealmsClientConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */