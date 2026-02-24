/*    */ package net.minecraft.network.protocol;
/*    */ 
/*    */ import net.minecraft.network.PacketListener;
/*    */ 
/*    */ public abstract class BundlePacket<T extends PacketListener> implements Packet<T> {
/*    */   private final Iterable<Packet<? super T>> packets;
/*    */   
/*    */   protected BundlePacket(Iterable<Packet<? super T>> packets) {
/*  9 */     this.packets = packets;
/*    */   }
/*    */   
/*    */   public final Iterable<Packet<? super T>> subPackets() {
/* 13 */     return this.packets;
/*    */   }
/*    */   
/*    */   public abstract PacketType<? extends BundlePacket<T>> type();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/BundlePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */