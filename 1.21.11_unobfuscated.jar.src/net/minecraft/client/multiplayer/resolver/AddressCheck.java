/*    */ package net.minecraft.client.multiplayer.resolver;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import com.google.common.collect.Streams;
/*    */ import com.mojang.blocklist.BlockListSupplier;
/*    */ import java.util.Objects;
/*    */ import java.util.ServiceLoader;
/*    */ import java.util.function.Predicate;
/*    */ 
/*    */ public interface AddressCheck
/*    */ {
/*    */   boolean isAllowed(ResolvedServerAddress paramResolvedServerAddress);
/*    */   
/*    */   boolean isAllowed(ServerAddress paramServerAddress);
/*    */   
/*    */   static AddressCheck createFromService() {
/* 17 */     final ImmutableList<Predicate<String>> blockLists = (ImmutableList<Predicate<String>>)Streams.stream(ServiceLoader.load(BlockListSupplier.class))
/* 18 */       .map(BlockListSupplier::createBlockList)
/* 19 */       .filter(Objects::nonNull)
/* 20 */       .collect(ImmutableList.toImmutableList());
/*    */     
/* 22 */     return new AddressCheck()
/*    */       {
/*    */         public boolean isAllowed(ResolvedServerAddress address) {
/* 25 */           String hostName = address.getHostName();
/* 26 */           String hostIp = address.getHostIp();
/* 27 */           return blockLists.stream().noneMatch(p -> (p.test(hostName) || p.test(hostIp)));
/*    */         }
/*    */ 
/*    */         
/*    */         public boolean isAllowed(ServerAddress address) {
/* 32 */           String hostName = address.getHost();
/* 33 */           return blockLists.stream().noneMatch(p -> p.test(hostName));
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/resolver/AddressCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */