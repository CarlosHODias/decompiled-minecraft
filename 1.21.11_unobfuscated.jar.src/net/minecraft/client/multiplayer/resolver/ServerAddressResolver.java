/*    */ package net.minecraft.client.multiplayer.resolver;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.net.InetAddress;
/*    */ import java.net.InetSocketAddress;
/*    */ import java.net.UnknownHostException;
/*    */ import java.util.Optional;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ @FunctionalInterface
/*    */ public interface ServerAddressResolver
/*    */ {
/* 13 */   public static final Logger LOGGER = LogUtils.getLogger();
/*    */   static {
/* 15 */     SYSTEM = (address -> {
/*    */         try {
/*    */           InetAddress resolvedAddress = InetAddress.getByName(address.getHost());
/*    */           return Optional.of(ResolvedServerAddress.from(new InetSocketAddress(resolvedAddress, address.getPort())));
/* 19 */         } catch (UnknownHostException e) {
/*    */           LOGGER.debug("Couldn't resolve server {} address", address.getHost(), e);
/*    */           return Optional.empty();
/*    */         } 
/*    */       });
/*    */   }
/*    */   
/*    */   public static final ServerAddressResolver SYSTEM;
/*    */   
/*    */   Optional<ResolvedServerAddress> resolve(ServerAddress paramServerAddress);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/resolver/ServerAddressResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */