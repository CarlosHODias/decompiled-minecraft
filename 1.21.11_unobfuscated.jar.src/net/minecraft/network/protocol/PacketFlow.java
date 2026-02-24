/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ public enum PacketFlow {
/*  4 */   SERVERBOUND("serverbound"),
/*  5 */   CLIENTBOUND("clientbound");
/*    */   private final String id;
/*    */   
/*    */   PacketFlow(String id) {
/*  9 */     this.id = id;
/*    */   }
/*    */   
/*    */   public PacketFlow getOpposite() {
/* 13 */     return (this == CLIENTBOUND) ? SERVERBOUND : CLIENTBOUND;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String id() {
/* 19 */     return this.id;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/PacketFlow.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */