/*    */ package net.minecraft.network.protocol.handshake;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.ConnectionProtocol;
/*    */ import net.minecraft.network.ProtocolInfo;
/*    */ import net.minecraft.network.protocol.ProtocolInfoBuilder;
/*    */ import net.minecraft.network.protocol.SimpleUnboundProtocol;
/*    */ 
/*    */ public class HandshakeProtocols {
/*    */   static {
/* 10 */     SERVERBOUND_TEMPLATE = ProtocolInfoBuilder.serverboundProtocol(ConnectionProtocol.HANDSHAKING, builder -> builder.addPacket(HandshakePacketTypes.CLIENT_INTENTION, ClientIntentionPacket.STREAM_CODEC));
/*    */   }
/*    */   
/*    */   public static final SimpleUnboundProtocol<ServerHandshakePacketListener, net.minecraft.network.FriendlyByteBuf> SERVERBOUND_TEMPLATE;
/* 14 */   public static final ProtocolInfo<ServerHandshakePacketListener> SERVERBOUND = SERVERBOUND_TEMPLATE.bind(net.minecraft.network.FriendlyByteBuf::new);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/handshake/HandshakeProtocols.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */