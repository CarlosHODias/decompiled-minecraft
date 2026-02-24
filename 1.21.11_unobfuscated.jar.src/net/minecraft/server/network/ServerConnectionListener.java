/*     */ package net.minecraft.server.network;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.bootstrap.ServerBootstrap;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelHandlerContext;
/*     */ import io.netty.channel.ChannelInboundHandlerAdapter;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import io.netty.channel.ChannelPipeline;
/*     */ import io.netty.channel.local.LocalAddress;
/*     */ import io.netty.handler.timeout.ReadTimeoutHandler;
/*     */ import io.netty.util.HashedWheelTimer;
/*     */ import io.netty.util.Timeout;
/*     */ import io.netty.util.Timer;
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketAddress;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.PacketSendListener;
/*     */ import net.minecraft.network.RateKickingConnection;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketFlow;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.ServerInfo;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerConnectionListener {
/*  42 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final MinecraftServer server;
/*     */   public volatile boolean running;
/*  46 */   private final List<ChannelFuture> channels = Collections.synchronizedList(Lists.newArrayList());
/*  47 */   private final List<Connection> connections = Collections.synchronizedList(Lists.newArrayList());
/*     */   
/*     */   public ServerConnectionListener(MinecraftServer server) {
/*  50 */     this.server = server;
/*  51 */     this.running = true;
/*     */   }
/*     */   
/*     */   public void startTcpServerListener(InetAddress address, int port) throws IOException {
/*  55 */     synchronized (this.channels) {
/*  56 */       EventLoopGroupHolder eventLoopGroupHolder = EventLoopGroupHolder.remote(this.server.useNativeTransport());
/*     */       
/*  58 */       this.channels.add(((ServerBootstrap)((ServerBootstrap)new ServerBootstrap()
/*  59 */           .channel(eventLoopGroupHolder.serverChannelCls()))
/*  60 */           .childHandler((ChannelHandler)new ChannelInitializer<Channel>()
/*     */             {
/*     */               protected void initChannel(Channel channel) {
/*     */                 try {
/*  64 */                   channel.config().setOption(ChannelOption.TCP_NODELAY, true);
/*  65 */                 } catch (ChannelException channelException) {}
/*     */ 
/*     */                 
/*  68 */                 ChannelPipeline pipeline = channel.pipeline()
/*  69 */                   .addLast("timeout", (ChannelHandler)new ReadTimeoutHandler(30));
/*     */                 
/*  71 */                 if (ServerConnectionListener.this.server.repliesToStatus()) {
/*  72 */                   pipeline.addLast("legacy_query", (ChannelHandler)new LegacyQueryHandler((ServerInfo)ServerConnectionListener.this.getServer()));
/*     */                 }
/*     */                 
/*  75 */                 Connection.configureSerialization(pipeline, PacketFlow.SERVERBOUND, false, null);
/*     */                 
/*  77 */                 int rateLimitPacketsPerSecond = ServerConnectionListener.this.server.getRateLimitPacketsPerSecond();
/*  78 */                 Connection connection = (rateLimitPacketsPerSecond > 0) ? (Connection)new RateKickingConnection(rateLimitPacketsPerSecond) : new Connection(PacketFlow.SERVERBOUND);
/*  79 */                 ServerConnectionListener.this.connections.add(connection);
/*  80 */                 connection.configurePacketHandler(pipeline);
/*  81 */                 connection.setListenerForServerboundHandshake((PacketListener)new ServerHandshakePacketListenerImpl(ServerConnectionListener.this.server, connection));
/*     */               }
/*  84 */             }).group(eventLoopGroupHolder.eventLoopGroup())
/*  85 */           .localAddress(address, port))
/*  86 */           .bind()
/*  87 */           .syncUninterruptibly());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public SocketAddress startMemoryChannel() {
/*     */     ChannelFuture newChannel;
/*  94 */     synchronized (this.channels) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 117 */       newChannel = ((ServerBootstrap)((ServerBootstrap)new ServerBootstrap().channel(EventLoopGroupHolder.local().serverChannelCls())).childHandler((ChannelHandler)new ChannelInitializer<Channel>() { protected void initChannel(Channel channel) { Connection connection = new Connection(PacketFlow.SERVERBOUND); connection.setListenerForServerboundHandshake((PacketListener)new MemoryServerHandshakePacketListenerImpl(ServerConnectionListener.this.server, connection)); ServerConnectionListener.this.connections.add(connection); ChannelPipeline pipeline = channel.pipeline(); Connection.configureInMemoryPipeline(pipeline, PacketFlow.SERVERBOUND); if (SharedConstants.DEBUG_FAKE_LATENCY_MS > 0) pipeline.addLast("latency", (ChannelHandler)new ServerConnectionListener.LatencySimulator(SharedConstants.DEBUG_FAKE_LATENCY_MS, SharedConstants.DEBUG_FAKE_JITTER_MS));  connection.configurePacketHandler(pipeline); } }).group(EventLoopGroupHolder.local().eventLoopGroup()).localAddress((SocketAddress)LocalAddress.ANY)).bind().syncUninterruptibly();
/*     */       
/* 119 */       this.channels.add(newChannel);
/*     */     } 
/*     */     
/* 122 */     return newChannel.channel().localAddress();
/*     */   }
/*     */   
/*     */   public void stop() {
/* 126 */     this.running = false;
/*     */     
/* 128 */     for (ChannelFuture channel : this.channels) {
/*     */       try {
/* 130 */         channel.channel().close().sync();
/* 131 */       } catch (InterruptedException ignored) {
/* 132 */         LOGGER.error("Interrupted whilst closing channel");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void tick() {
/* 138 */     synchronized (this.connections) {
/* 139 */       Iterator<Connection> iterator = this.connections.iterator();
/*     */       
/* 141 */       while (iterator.hasNext()) {
/* 142 */         Connection connection = iterator.next();
/*     */         
/* 144 */         if (connection.isConnecting()) {
/*     */           continue;
/*     */         }
/* 147 */         if (connection.isConnected()) {
/*     */           try {
/* 149 */             connection.tick();
/* 150 */           } catch (Exception e) {
/* 151 */             if (connection.isMemoryConnection()) {
/* 152 */               throw new ReportedException(CrashReport.forThrowable(e, "Ticking memory connection"));
/*     */             }
/* 154 */             LOGGER.warn("Failed to handle packet for {}", connection.getLoggableAddress(this.server.logIPs()), e);
/* 155 */             MutableComponent mutableComponent = Component.literal("Internal server error");
/* 156 */             connection.send((Packet)new net.minecraft.network.protocol.common.ClientboundDisconnectPacket((Component)mutableComponent), PacketSendListener.thenRun(() -> connection.disconnect(component)));
/* 157 */             connection.setReadOnly();
/*     */           } 
/*     */           continue;
/*     */         } 
/* 161 */         iterator.remove();
/* 162 */         connection.handleDisconnection();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public MinecraftServer getServer() {
/* 169 */     return this.server;
/*     */   }
/*     */   
/*     */   private static class LatencySimulator extends ChannelInboundHandlerAdapter {
/* 173 */     private static final Timer TIMER = (Timer)new HashedWheelTimer();
/*     */     
/*     */     private final int delay;
/*     */     private final int jitter;
/* 177 */     private final List<DelayedMessage> queuedMessages = Lists.newArrayList();
/*     */     
/*     */     public LatencySimulator(int delay, int jitter) {
/* 180 */       this.delay = delay;
/* 181 */       this.jitter = jitter;
/*     */     }
/*     */ 
/*     */     
/*     */     public void channelRead(ChannelHandlerContext ctx, Object msg) {
/* 186 */       delayDownstream(ctx, msg);
/*     */     }
/*     */     
/*     */     private void delayDownstream(ChannelHandlerContext ctx, Object msg) {
/* 190 */       int sendDelay = this.delay + (int)(Math.random() * this.jitter);
/* 191 */       this.queuedMessages.add(new DelayedMessage(ctx, msg));
/* 192 */       TIMER.newTimeout(this::onTimeout, sendDelay, TimeUnit.MILLISECONDS);
/*     */     }
/*     */     
/*     */     private void onTimeout(Timeout timeout) {
/* 196 */       DelayedMessage next = this.queuedMessages.remove(0);
/* 197 */       next.ctx.fireChannelRead(next.msg);
/*     */     }
/*     */     
/*     */     private static class DelayedMessage
/*     */     {
/*     */       public final ChannelHandlerContext ctx;
/*     */       public final Object msg;
/*     */       
/* 205 */       public DelayedMessage(ChannelHandlerContext ctx, Object msg) { this.ctx = ctx;
/* 206 */         this.msg = msg; } } } private static class DelayedMessage { public final ChannelHandlerContext ctx; public final Object msg; public DelayedMessage(ChannelHandlerContext ctx, Object msg) { this.ctx = ctx; this.msg = msg; }
/*     */      }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Connection> getConnections() {
/* 212 */     return this.connections;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/network/ServerConnectionListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */