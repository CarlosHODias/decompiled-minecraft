/*    */ package net.minecraft.network;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import io.netty.channel.ChannelHandlerContext;
/*    */ import io.netty.handler.codec.MessageToByteEncoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.util.profiling.jfr.JvmProfiler;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public class PacketEncoder<T extends PacketListener> extends MessageToByteEncoder<Packet<T>> {
/* 13 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   private final ProtocolInfo<T> protocolInfo;
/*    */   
/*    */   public PacketEncoder(ProtocolInfo<T> protocolInfo) {
/* 18 */     this.protocolInfo = protocolInfo;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void encode(ChannelHandlerContext ctx, Packet<T> packet, ByteBuf output) throws Exception {
/* 23 */     PacketType<? extends Packet<? super T>> packetId = packet.type();
/*    */     
/*    */     try {
/* 26 */       this.protocolInfo.codec().encode(output, packet);
/*    */       
/* 28 */       int writtenBytes = output.readableBytes();
/*    */       
/* 30 */       if (LOGGER.isDebugEnabled()) {
/* 31 */         LOGGER.debug(Connection.PACKET_SENT_MARKER, "OUT: [{}:{}] {} -> {} bytes", new Object[] { this.protocolInfo.id().id(), packetId, packet.getClass().getName(), writtenBytes });
/*    */       }
/*    */       
/* 34 */       JvmProfiler.INSTANCE.onPacketSent(
/* 35 */           this.protocolInfo.id(), packetId, 
/*    */           
/* 37 */           ctx.channel().remoteAddress(), writtenBytes);
/*    */     
/*    */     }
/* 40 */     catch (Throwable t) {
/* 41 */       LOGGER.error("Error sending packet {}", packetId, t);
/* 42 */       if (packet.isSkippable()) {
/* 43 */         throw new SkipPacketEncoderException(t);
/*    */       }
/* 45 */       throw t;
/*    */     } finally {
/*    */       
/* 48 */       ProtocolSwapHandler.handleOutboundTerminalPacket(ctx, packet);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/PacketEncoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */