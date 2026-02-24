/*    */ package net.minecraft.network;
/*    */ 
/*    */ public enum ConnectionProtocol {
/*  4 */   HANDSHAKING("handshake"),
/*  5 */   PLAY("play"),
/*  6 */   STATUS("status"),
/*  7 */   LOGIN("login"),
/*  8 */   CONFIGURATION("configuration");
/*    */   
/*    */   private final String id;
/*    */ 
/*    */   
/*    */   ConnectionProtocol(String id) {
/* 14 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String id() {
/* 18 */     return this.id;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/ConnectionProtocol.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */