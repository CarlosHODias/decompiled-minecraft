/*    */ package net.minecraft.util.profiling.jfr.event;
/*    */ 
/*    */ import java.net.SocketAddress;
/*    */ import jdk.jfr.EventType;
/*    */ import jdk.jfr.Label;
/*    */ import jdk.jfr.Name;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Name("minecraft.PacketReceived")
/*    */ @Label("Network Packet Received")
/*    */ public class PacketReceivedEvent
/*    */   extends PacketEvent
/*    */ {
/*    */   public static final String NAME = "minecraft.PacketReceived";
/* 18 */   public static final EventType TYPE = EventType.getEventType((Class)PacketReceivedEvent.class);
/*    */   
/*    */   public PacketReceivedEvent(String protocolId, String packetDirection, String packetId, SocketAddress remoteAddress, int readableBytes) {
/* 21 */     super(protocolId, packetDirection, packetId, remoteAddress, readableBytes);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/jfr/event/PacketReceivedEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */