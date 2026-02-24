/*   */ package net.minecraft.network;
/*   */ 
/*   */ import net.minecraft.network.protocol.PacketFlow;
/*   */ 
/*   */ public interface ClientboundPacketListener
/*   */   extends PacketListener {
/*   */   default PacketFlow flow() {
/* 8 */     return PacketFlow.CLIENTBOUND;
/*   */   }
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/ClientboundPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */