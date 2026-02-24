/*   */ package net.minecraft.network.protocol.game;
/*   */ 
/*   */ import net.minecraft.network.protocol.BundleDelimiterPacket;
/*   */ import net.minecraft.network.protocol.PacketType;
/*   */ 
/*   */ public class ClientboundBundleDelimiterPacket
/*   */   extends BundleDelimiterPacket<ClientGamePacketListener> {
/*   */   public PacketType<ClientboundBundleDelimiterPacket> type() {
/* 9 */     return GamePacketTypes.CLIENTBOUND_BUNDLE_DELIMITER;
/*   */   }
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundBundleDelimiterPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */