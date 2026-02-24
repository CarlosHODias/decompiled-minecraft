/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.protocol.BundlePacket;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ 
/*    */ public class ClientboundBundlePacket extends BundlePacket<ClientGamePacketListener> {
/*    */   public ClientboundBundlePacket(Iterable<Packet<? super ClientGamePacketListener>> packets) {
/*  9 */     super(packets);
/*    */   }
/*    */ 
/*    */   
/*    */   public PacketType<ClientboundBundlePacket> type() {
/* 14 */     return GamePacketTypes.CLIENTBOUND_BUNDLE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 19 */     listener.handleBundlePacket(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundBundlePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */