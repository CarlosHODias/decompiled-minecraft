/*    */ package net.minecraft.client.multiplayer;
/*    */ 
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
/*    */ import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
/*    */ import net.minecraft.util.Util;
/*    */ import net.minecraft.util.debugchart.LocalSampleLogger;
/*    */ 
/*    */ public class PingDebugMonitor {
/*    */   private final ClientPacketListener connection;
/*    */   
/*    */   public PingDebugMonitor(ClientPacketListener connection, LocalSampleLogger delayTimer) {
/* 13 */     this.connection = connection;
/* 14 */     this.delayTimer = delayTimer;
/*    */   }
/*    */   private final LocalSampleLogger delayTimer;
/*    */   public void tick() {
/* 18 */     this.connection.send((Packet<?>)new ServerboundPingRequestPacket(Util.getMillis()));
/*    */   }
/*    */   
/*    */   public void onPongReceived(ClientboundPongResponsePacket packet) {
/* 22 */     this.delayTimer.logSample(Util.getMillis() - packet.time());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/PingDebugMonitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */