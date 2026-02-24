/*    */ package net.minecraft.client.multiplayer.resolver;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.Hashtable;
/*    */ import java.util.Optional;
/*    */ import javax.naming.directory.Attribute;
/*    */ import javax.naming.directory.Attributes;
/*    */ import javax.naming.directory.DirContext;
/*    */ import javax.naming.directory.InitialDirContext;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface ServerRedirectHandler
/*    */ {
/* 16 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */ 
/*    */   
/*    */   public static final ServerRedirectHandler EMPTY = originalAddress -> Optional.empty();
/*    */ 
/*    */   
/*    */   static ServerRedirectHandler createDnsSrvRedirectHandler() {
/*    */     DirContext context;
/*    */     try {
/* 25 */       String dnsContextClass = "com.sun.jndi.dns.DnsContextFactory";
/*    */       
/* 27 */       Class.forName("com.sun.jndi.dns.DnsContextFactory");
/*    */       
/* 29 */       Hashtable<String, String> env = new Hashtable<>();
/* 30 */       env.put("java.naming.factory.initial", "com.sun.jndi.dns.DnsContextFactory");
/* 31 */       env.put("java.naming.provider.url", "dns:");
/* 32 */       env.put("com.sun.jndi.dns.timeout.retries", "1");
/* 33 */       context = new InitialDirContext(env);
/* 34 */     } catch (Throwable e) {
/* 35 */       LOGGER.error("Failed to initialize SRV redirect resolved, some servers might not work", e);
/* 36 */       return EMPTY;
/*    */     } 
/*    */     
/* 39 */     return originalAddress -> {
/*    */         if (originalAddress.getPort() == 25565)
/*    */           try {
/*    */             Attributes attributes = context.getAttributes("_minecraft._tcp." + originalAddress.getHost(), new String[] { "SRV" });
/*    */             
/*    */             Attribute srvAttribute = attributes.get("srv");
/*    */             if (srvAttribute != null) {
/*    */               String[] arguments = srvAttribute.get().toString().split(" ", 4);
/*    */               return Optional.of(new ServerAddress(arguments[3], ServerAddress.parsePort(arguments[2])));
/*    */             } 
/* 49 */           } catch (Throwable throwable) {} 
/*    */         return Optional.empty();
/*    */       };
/*    */   }
/*    */   
/*    */   Optional<ServerAddress> lookupRedirect(ServerAddress paramServerAddress);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/resolver/ServerRedirectHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */