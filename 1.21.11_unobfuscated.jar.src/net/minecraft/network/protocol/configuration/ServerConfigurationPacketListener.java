/*   */ package net.minecraft.network.protocol.configuration;
/*   */ 
/*   */ import net.minecraft.network.ConnectionProtocol;
/*   */ import net.minecraft.network.protocol.common.ServerCommonPacketListener;
/*   */ 
/*   */ public interface ServerConfigurationPacketListener
/*   */   extends ServerCommonPacketListener {
/*   */   default ConnectionProtocol protocol() {
/* 9 */     return ConnectionProtocol.CONFIGURATION;
/*   */   }
/*   */   
/*   */   void handleConfigurationFinished(ServerboundFinishConfigurationPacket paramServerboundFinishConfigurationPacket);
/*   */   
/*   */   void handleSelectKnownPacks(ServerboundSelectKnownPacks paramServerboundSelectKnownPacks);
/*   */   
/*   */   void handleAcceptCodeOfConduct(ServerboundAcceptCodeOfConductPacket paramServerboundAcceptCodeOfConductPacket);
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/configuration/ServerConfigurationPacketListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */