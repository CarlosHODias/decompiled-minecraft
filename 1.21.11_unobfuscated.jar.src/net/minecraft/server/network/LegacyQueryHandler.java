/*     */ package net.minecraft.server.network;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import io.netty.buffer.ByteBufAllocator;
/*     */ import io.netty.channel.ChannelFutureListener;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*     */ import io.netty.util.concurrent.GenericFutureListener;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.server.ServerInfo;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class LegacyQueryHandler extends ChannelInboundHandlerAdapter {
/*  16 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final ServerInfo server;
/*     */   
/*     */   public LegacyQueryHandler(ServerInfo server) {
/*  21 */     this.server = server;
/*     */   }
/*     */ 
/*     */   
/*     */   public void channelRead(ChannelHandlerContext ctx, Object msg) {
/*  26 */     ByteBuf in = (ByteBuf)msg;
/*     */     
/*  28 */     in.markReaderIndex();
/*     */     
/*     */     boolean connectNormally = true;
/*     */     
/*  32 */     try { if (in.readUnsignedByte() != 254) {
/*     */         return;
/*     */       }
/*     */       
/*  36 */       SocketAddress socket = ctx.channel().remoteAddress();
/*     */       
/*  38 */       int length = in.readableBytes();
/*  39 */       if (length == 0) {
/*  40 */         LOGGER.debug("Ping: (<1.3.x) from {}", socket);
/*     */ 
/*     */         
/*  43 */         String body = createVersion0Response(this.server);
/*  44 */         sendFlushAndClose(ctx, createLegacyDisconnectPacket(ctx.alloc(), body));
/*     */       } else {
/*     */         
/*  47 */         if (in.readUnsignedByte() != 1) {
/*     */           return;
/*     */         }
/*     */         
/*  51 */         if (in.isReadable()) {
/*     */ 
/*     */           
/*  54 */           if (!readCustomPayloadPacket(in)) {
/*     */             return;
/*     */           }
/*  57 */           LOGGER.debug("Ping: (1.6) from {}", socket);
/*     */         } else {
/*  59 */           LOGGER.debug("Ping: (1.4-1.5.x) from {}", socket);
/*     */         } 
/*     */         
/*  62 */         String body = createVersion1Response(this.server);
/*  63 */         sendFlushAndClose(ctx, createLegacyDisconnectPacket(ctx.alloc(), body));
/*     */       } 
/*     */       
/*  66 */       in.release();
/*  67 */       connectNormally = false; }
/*  68 */     catch (RuntimeException runtimeException) {  }
/*     */     finally
/*  70 */     { if (connectNormally) {
/*     */         
/*  72 */         in.resetReaderIndex();
/*  73 */         ctx.channel().pipeline().remove((ChannelHandler)this);
/*  74 */         ctx.fireChannelRead(msg);
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   private static boolean readCustomPayloadPacket(ByteBuf in) {
/*  80 */     short packetId = in.readUnsignedByte();
/*  81 */     if (packetId != 250) {
/*  82 */       return false;
/*     */     }
/*  84 */     String channelId = LegacyProtocolUtils.readLegacyString(in);
/*  85 */     if (!"MC|PingHost".equals(channelId)) {
/*  86 */       return false;
/*     */     }
/*  88 */     int payloadSize = in.readUnsignedShort();
/*  89 */     if (in.readableBytes() != payloadSize) {
/*  90 */       return false;
/*     */     }
/*  92 */     short protocolVersion = in.readUnsignedByte();
/*  93 */     if (protocolVersion < 73) {
/*  94 */       return false;
/*     */     }
/*  96 */     String host = LegacyProtocolUtils.readLegacyString(in);
/*  97 */     int port = in.readInt();
/*  98 */     if (port > 65535) {
/*  99 */       return false;
/*     */     }
/* 101 */     return true;
/*     */   }
/*     */   
/*     */   private static String createVersion0Response(ServerInfo server) {
/* 105 */     return String.format(Locale.ROOT, "%s§%d§%d", new Object[] { server.getMotd(), server.getPlayerCount(), server.getMaxPlayers() });
/*     */   }
/*     */   
/*     */   private static String createVersion1Response(ServerInfo server) {
/* 109 */     return String.format(Locale.ROOT, "§1\000%d\000%s\000%s\000%d\000%d", new Object[] { 127, server.getServerVersion(), server.getMotd(), server.getPlayerCount(), server.getMaxPlayers() });
/*     */   }
/*     */   
/*     */   private static void sendFlushAndClose(ChannelHandlerContext ctx, ByteBuf out) {
/* 113 */     ctx.pipeline().firstContext().writeAndFlush(out).addListener((GenericFutureListener)ChannelFutureListener.CLOSE);
/*     */   }
/*     */   
/*     */   private static ByteBuf createLegacyDisconnectPacket(ByteBufAllocator alloc, String reason) {
/* 117 */     ByteBuf out = alloc.buffer();
/* 118 */     out.writeByte(255);
/* 119 */     LegacyProtocolUtils.writeLegacyString(out, reason);
/* 120 */     return out;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/network/LegacyQueryHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */