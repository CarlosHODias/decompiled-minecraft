/*    */ package net.minecraft.client.multiplayer.resolver;
/*    */ 
/*    */ import com.google.common.net.HostAndPort;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.net.IDN;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class ServerAddress
/*    */ {
/* 12 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final HostAndPort hostAndPort;
/*    */   
/* 16 */   private static final ServerAddress INVALID = new ServerAddress(HostAndPort.fromParts("server.invalid", 25565));
/*    */   
/*    */   public ServerAddress(String host, int port) {
/* 19 */     this(HostAndPort.fromParts(host, port));
/*    */   }
/*    */   
/*    */   private ServerAddress(HostAndPort hostAndPort) {
/* 23 */     this.hostAndPort = hostAndPort;
/*    */   }
/*    */   
/*    */   public String getHost() {
/*    */     try {
/* 28 */       return IDN.toASCII(this.hostAndPort.getHost());
/* 29 */     } catch (IllegalArgumentException ignored) {
/* 30 */       return "";
/*    */     } 
/*    */   }
/*    */   
/*    */   public int getPort() {
/* 35 */     return this.hostAndPort.getPort();
/*    */   }
/*    */   
/*    */   public static ServerAddress parseString(String input) {
/* 39 */     if (input == null) {
/* 40 */       return INVALID;
/*    */     }
/*    */     
/*    */     try {
/* 44 */       HostAndPort result = HostAndPort.fromString(input).withDefaultPort(25565);
/* 45 */       if (result.getHost().isEmpty()) {
/* 46 */         return INVALID;
/*    */       }
/* 48 */       return new ServerAddress(result);
/*    */     }
/* 50 */     catch (IllegalArgumentException e) {
/* 51 */       LOGGER.info("Failed to parse URL {}", input, e);
/* 52 */       return INVALID;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static boolean isValidAddress(String input) {
/*    */     try {
/* 58 */       HostAndPort hostAndPort = HostAndPort.fromString(input);
/* 59 */       String host = hostAndPort.getHost();
/* 60 */       if (!host.isEmpty()) {
/* 61 */         IDN.toASCII(host);
/* 62 */         return true;
/*    */       } 
/* 64 */     } catch (IllegalArgumentException illegalArgumentException) {}
/*    */     
/* 66 */     return false;
/*    */   }
/*    */   
/*    */   static int parsePort(String str) {
/*    */     try {
/* 71 */       return Integer.parseInt(str.trim());
/* 72 */     } catch (Exception exception) {
/*    */ 
/*    */       
/* 75 */       return 25565;
/*    */     } 
/*    */   }
/*    */   
/*    */   public String toString() {
/* 80 */     return this.hostAndPort.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object o) {
/* 85 */     if (this == o) {
/* 86 */       return true;
/*    */     }
/* 88 */     if (o instanceof ServerAddress) {
/* 89 */       return this.hostAndPort.equals(((ServerAddress)o).hostAndPort);
/*    */     }
/*    */     
/* 92 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 97 */     return this.hostAndPort.hashCode();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/resolver/ServerAddress.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */