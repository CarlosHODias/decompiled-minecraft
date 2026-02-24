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
/*    */ @Name("minecraft.PacketSent")
/*    */ @Label("Network Packet Sent")
/*    */ public class PacketSentEvent
/*    */   extends PacketEvent
/*    */ {
/*    */   public static final String NAME = "minecraft.PacketSent";
/* 18 */   public static final EventType TYPE = EventType.getEventType((Class)PacketSentEvent.class);
/*    */   
/*    */   public PacketSentEvent(String protocolId, String packetDirection, String packetId, SocketAddress remoteAddress, int writtenBytes) {
/* 21 */     super(protocolId, packetDirection, packetId, remoteAddress, writtenBytes);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/jfr/event/PacketSentEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */