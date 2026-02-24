/*    */ package net.minecraft.client.multiplayer.resolver;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ 
/*    */ public class ServerNameResolver {
/*  8 */   public static final ServerNameResolver DEFAULT = new ServerNameResolver(ServerAddressResolver.SYSTEM, ServerRedirectHandler.createDnsSrvRedirectHandler(), AddressCheck.createFromService());
/*    */   
/*    */   private final ServerAddressResolver resolver;
/*    */   private final ServerRedirectHandler redirectHandler;
/*    */   private final AddressCheck addressCheck;
/*    */   
/*    */   @VisibleForTesting
/*    */   ServerNameResolver(ServerAddressResolver resolver, ServerRedirectHandler redirectHandler, AddressCheck addressCheck) {
/* 16 */     this.resolver = resolver;
/* 17 */     this.redirectHandler = redirectHandler;
/* 18 */     this.addressCheck = addressCheck;
/*    */   }
/*    */ 
/*    */   
/*    */   public Optional<ResolvedServerAddress> resolveAddress(ServerAddress address) {
/* 23 */     Optional<ResolvedServerAddress> resolvedAddress = this.resolver.resolve(address);
/*    */ 
/*    */     
/* 26 */     if ((resolvedAddress.isPresent() && !this.addressCheck.isAllowed(resolvedAddress.get())) || 
/* 27 */       !this.addressCheck.isAllowed(address)) {
/* 28 */       return Optional.empty();
/*    */     }
/*    */     
/* 31 */     Optional<ServerAddress> redirectedAddress = this.redirectHandler.lookupRedirect(address);
/* 32 */     if (redirectedAddress.isPresent()) {
/*    */       
/* 34 */       Objects.requireNonNull(this.addressCheck); resolvedAddress = this.resolver.resolve(redirectedAddress.get()).filter(this.addressCheck::isAllowed);
/*    */     } 
/*    */     
/* 37 */     return resolvedAddress;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/resolver/ServerNameResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */