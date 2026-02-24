/*    */ package net.minecraft.client.multiplayer.resolver;
/*    */ 
/*    */ import java.net.InetSocketAddress;
/*    */ 
/*    */ public interface ResolvedServerAddress {
/*    */   String getHostName();
/*    */   
/*    */   String getHostIp();
/*    */   
/*    */   int getPort();
/*    */   
/*    */   InetSocketAddress asInetSocketAddress();
/*    */   
/*    */   static ResolvedServerAddress from(final InetSocketAddress address) {
/* 15 */     return new ResolvedServerAddress()
/*    */       {
/*    */         public String getHostName() {
/* 18 */           return address.getAddress().getHostName();
/*    */         }
/*    */ 
/*    */         
/*    */         public String getHostIp() {
/* 23 */           return address.getAddress().getHostAddress();
/*    */         }
/*    */ 
/*    */         
/*    */         public int getPort() {
/* 28 */           return address.getPort();
/*    */         }
/*    */ 
/*    */         
/*    */         public InetSocketAddress asInetSocketAddress() {
/* 33 */           return address;
/*    */         }
/*    */       };
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/resolver/ResolvedServerAddress.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */