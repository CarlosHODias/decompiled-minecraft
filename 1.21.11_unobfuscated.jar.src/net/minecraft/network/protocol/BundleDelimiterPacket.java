/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import net.minecraft.network.PacketListener;
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class BundleDelimiterPacket<T extends PacketListener>
/*    */   implements Packet<T>
/*    */ {
/*    */   public final void handle(T listener) {
/* 11 */     throw new AssertionError("This packet should be handled by pipeline");
/*    */   }
/*    */   
/*    */   public abstract PacketType<? extends BundleDelimiterPacket<T>> type();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/BundleDelimiterPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */