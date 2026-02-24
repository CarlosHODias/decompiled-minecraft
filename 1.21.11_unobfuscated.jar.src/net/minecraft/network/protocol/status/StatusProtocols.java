/*    */ package net.minecraft.network.protocol.status;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.ProtocolInfo;
/*    */ import net.minecraft.network.protocol.ProtocolInfoBuilder;
/*    */ import net.minecraft.network.protocol.SimpleUnboundProtocol;
/*    */ import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
/*    */ import net.minecraft.network.protocol.ping.PingPacketTypes;
/*    */ import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
/*    */ 
/*    */ public class StatusProtocols {
/*    */   static {
/* 14 */     SERVERBOUND_TEMPLATE = ProtocolInfoBuilder.serverboundProtocol(ConnectionProtocol.STATUS, builder -> builder.addPacket(StatusPacketTypes.SERVERBOUND_STATUS_REQUEST, ServerboundStatusRequestPacket.STREAM_CODEC).addPacket(PingPacketTypes.SERVERBOUND_PING_REQUEST, ServerboundPingRequestPacket.STREAM_CODEC));
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 19 */     SERVERBOUND = SERVERBOUND_TEMPLATE.bind(e -> e);
/*    */     
/* 21 */     CLIENTBOUND_TEMPLATE = ProtocolInfoBuilder.clientboundProtocol(ConnectionProtocol.STATUS, builder -> builder.addPacket(StatusPacketTypes.CLIENTBOUND_STATUS_RESPONSE, ClientboundStatusResponsePacket.STREAM_CODEC).addPacket(PingPacketTypes.CLIENTBOUND_PONG_RESPONSE, ClientboundPongResponsePacket.STREAM_CODEC));
/*    */   }
/*    */   public static final SimpleUnboundProtocol<ServerStatusPacketListener, ByteBuf> SERVERBOUND_TEMPLATE;
/*    */   public static final ProtocolInfo<ServerStatusPacketListener> SERVERBOUND;
/*    */   public static final SimpleUnboundProtocol<ClientStatusPacketListener, net.minecraft.network.FriendlyByteBuf> CLIENTBOUND_TEMPLATE;
/* 26 */   public static final ProtocolInfo<ClientStatusPacketListener> CLIENTBOUND = CLIENTBOUND_TEMPLATE.bind(net.minecraft.network.FriendlyByteBuf::new);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/status/StatusProtocols.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */