/*    */ package net.minecraft.client.server;
/*    */ 
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class LanServer {
/*    */   private final String motd;
/*    */   private final String address;
/*    */   private long pingTime;
/*    */   
/*    */   public LanServer(String motd, String address) {
/* 11 */     this.motd = motd;
/* 12 */     this.address = address;
/* 13 */     this.pingTime = Util.getMillis();
/*    */   }
/*    */   
/*    */   public String getMotd() {
/* 17 */     return this.motd;
/*    */   }
/*    */   
/*    */   public String getAddress() {
/* 21 */     return this.address;
/*    */   }
/*    */   
/*    */   public void updatePingTime() {
/* 25 */     this.pingTime = Util.getMillis();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/server/LanServer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */