/*     */ package net.minecraft.client.multiplayer;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.bootstrap.Bootstrap;
/*     */ import io.netty.channel.Channel;
/*     */ import io.netty.channel.ChannelException;
/*     */ import io.netty.channel.ChannelHandler;
/*     */ import io.netty.channel.ChannelInitializer;
/*     */ import io.netty.channel.ChannelOption;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.gui.screens.ConnectScreen;
/*     */ import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
/*     */ import net.minecraft.client.multiplayer.resolver.ServerAddress;
/*     */ import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.DisconnectionDetails;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.ping.ClientboundPongResponsePacket;
/*     */ import net.minecraft.network.protocol.ping.ServerboundPingRequestPacket;
/*     */ import net.minecraft.network.protocol.status.ClientStatusPacketListener;
/*     */ import net.minecraft.network.protocol.status.ClientboundStatusResponsePacket;
/*     */ import net.minecraft.network.protocol.status.ServerStatus;
/*     */ import net.minecraft.network.protocol.status.ServerboundStatusRequestPacket;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.server.network.EventLoopGroupHolder;
/*     */ import net.minecraft.server.players.NameAndId;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ServerStatusPinger {
/*  42 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  43 */   private static final Component CANT_CONNECT_MESSAGE = (Component)Component.translatable("multiplayer.status.cannot_connect").withColor(-65536);
/*     */   
/*  45 */   private final List<Connection> connections = Collections.synchronizedList(Lists.newArrayList());
/*     */   
/*     */   public void pingServer(final ServerData data, final Runnable onPersistentDataChange, final Runnable onPongResponse, final EventLoopGroupHolder eventLoopGroupHolder) throws UnknownHostException {
/*  48 */     final ServerAddress rawAddress = ServerAddress.parseString(data.ip);
/*     */     
/*  50 */     Optional<InetSocketAddress> resolvedAddress = ServerNameResolver.DEFAULT.resolveAddress(rawAddress)
/*  51 */       .map(ResolvedServerAddress::asInetSocketAddress);
/*  52 */     if (resolvedAddress.isEmpty()) {
/*  53 */       onPingFailed(ConnectScreen.UNKNOWN_HOST_MESSAGE, data);
/*     */       
/*     */       return;
/*     */     } 
/*  57 */     final InetSocketAddress address = resolvedAddress.get();
/*     */     
/*  59 */     final Connection connection = Connection.connectToServer(address, eventLoopGroupHolder, null);
/*     */     
/*  61 */     this.connections.add(connection);
/*     */     
/*  63 */     data.motd = (Component)Component.translatable("multiplayer.status.pinging");
/*  64 */     data.playerList = Collections.emptyList();
/*     */     
/*  66 */     ClientStatusPacketListener listener = new ClientStatusPacketListener()
/*     */       {
/*     */         private boolean success;
/*     */         private boolean receivedPing;
/*     */         private long pingStart;
/*     */         
/*     */         public void handleStatusResponse(ClientboundStatusResponsePacket packet) {
/*  73 */           if (this.receivedPing) {
/*  74 */             connection.disconnect((Component)Component.translatable("multiplayer.status.unrequested"));
/*     */             return;
/*     */           } 
/*  77 */           this.receivedPing = true;
/*  78 */           ServerStatus status = packet.status();
/*  79 */           data.motd = status.description();
/*     */           
/*  81 */           status.version().ifPresentOrElse(version -> {
/*     */                 data.version = (Component)Component.literal(version.name());
/*     */                 
/*     */                 data.protocol = version.protocol();
/*     */               }, () -> {
/*     */                 data.version = (Component)Component.translatable("multiplayer.status.old");
/*     */                 data.protocol = 0;
/*     */               });
/*  89 */           status.players().ifPresentOrElse(players -> {
/*     */                 data.status = ServerStatusPinger.formatPlayerCount(players.online(), players.max());
/*     */                 
/*     */                 data.players = players;
/*     */                 
/*     */                 if (!players.sample().isEmpty()) {
/*     */                   List<Component> playerNames = new ArrayList<>(players.sample().size());
/*     */                   
/*     */                   for (NameAndId profile : (Iterable<NameAndId>)players.sample()) {
/*     */                     MutableComponent mutableComponent;
/*     */                     
/*     */                     if (profile.equals(MinecraftServer.ANONYMOUS_PLAYER_PROFILE)) {
/*     */                       mutableComponent = Component.translatable("multiplayer.status.anonymous_player");
/*     */                     } else {
/*     */                       mutableComponent = Component.literal(profile.name());
/*     */                     } 
/*     */                     playerNames.add(mutableComponent);
/*     */                   } 
/*     */                   if (players.sample().size() < players.online()) {
/*     */                     playerNames.add(Component.translatable("multiplayer.status.and_more", new Object[] { players.online() - players.sample().size() }));
/*     */                   }
/*     */                   data.playerList = playerNames;
/*     */                 } else {
/*     */                   data.playerList = List.of();
/*     */                 } 
/*     */               }, () -> data.status = (Component)Component.translatable("multiplayer.status.unknown").withStyle(ChatFormatting.DARK_GRAY));
/* 115 */           status.favicon().ifPresent(newIcon -> {
/*     */                 if (!Arrays.equals(newIcon.iconBytes(), data.getIconBytes())) {
/*     */                   data.setIconBytes(ServerData.validateIcon(newIcon.iconBytes()));
/*     */                   
/*     */                   onPersistentDataChange.run();
/*     */                 } 
/*     */               });
/* 122 */           this.pingStart = Util.getMillis();
/* 123 */           connection.send((Packet)new ServerboundPingRequestPacket(this.pingStart));
/* 124 */           this.success = true;
/*     */         }
/*     */ 
/*     */         
/*     */         public void handlePongResponse(ClientboundPongResponsePacket packet) {
/* 129 */           long then = this.pingStart;
/* 130 */           long now = Util.getMillis();
/* 131 */           data.ping = now - then;
/*     */           
/* 133 */           connection.disconnect((Component)Component.translatable("multiplayer.status.finished"));
/*     */           
/* 135 */           onPongResponse.run();
/*     */         }
/*     */ 
/*     */         
/*     */         public void onDisconnect(DisconnectionDetails details) {
/* 140 */           if (!this.success) {
/* 141 */             ServerStatusPinger.this.onPingFailed(details.reason(), data);
/* 142 */             ServerStatusPinger.this.pingLegacyServer(address, rawAddress, data, eventLoopGroupHolder);
/*     */           } 
/*     */         }
/*     */ 
/*     */         
/*     */         public boolean isAcceptingMessages() {
/* 148 */           return connection.isConnected();
/*     */         }
/*     */       };
/*     */     
/*     */     try {
/* 153 */       connection.initiateServerboundStatusConnection(rawAddress.getHost(), rawAddress.getPort(), listener);
/* 154 */       connection.send((Packet)ServerboundStatusRequestPacket.INSTANCE);
/* 155 */     } catch (Throwable t) {
/* 156 */       LOGGER.error("Failed to ping server {}", rawAddress, t);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void onPingFailed(Component reason, ServerData data) {
/* 161 */     LOGGER.error("Can't ping {}: {}", data.ip, reason.getString());
/* 162 */     data.motd = CANT_CONNECT_MESSAGE;
/* 163 */     data.status = CommonComponents.EMPTY;
/*     */   }
/*     */   
/*     */   private void pingLegacyServer(InetSocketAddress resolvedAddress, final ServerAddress rawAddress, final ServerData data, EventLoopGroupHolder eventLoopGroupHolder) {
/* 167 */     ((Bootstrap)((Bootstrap)((Bootstrap)new Bootstrap()
/* 168 */       .group(eventLoopGroupHolder.eventLoopGroup()))
/* 169 */       .handler((ChannelHandler)new ChannelInitializer<Channel>(this)
/*     */         {
/*     */           protected void initChannel(Channel channel)
/*     */           {
/*     */             try {
/* 174 */               channel.config().setOption(ChannelOption.TCP_NODELAY, true);
/* 175 */             } catch (ChannelException channelException) {}
/*     */ 
/*     */             
/* 178 */             channel.pipeline().addLast(new ChannelHandler[] { (ChannelHandler)new LegacyServerPinger(rawAddress, (protocolVersion, gameVersion, motd, players, maxPlayers) -> {
/*     */                       data.setState(ServerData.State.INCOMPATIBLE);
/*     */                       
/*     */                       data.version = (Component)Component.literal(gameVersion);
/*     */                       
/*     */                       data.motd = (Component)Component.literal(motd);
/*     */                       
/*     */                       data.status = ServerStatusPinger.formatPlayerCount(players, maxPlayers);
/*     */                       data.players = new ServerStatus.Players(maxPlayers, players, List.of());
/*     */                     }) });
/*     */           }
/* 189 */         })).channel(eventLoopGroupHolder.channelCls()))
/* 190 */       .connect(resolvedAddress.getAddress(), resolvedAddress.getPort());
/*     */   }
/*     */   
/*     */   public static Component formatPlayerCount(int curPlayers, int maxPlayers) {
/* 194 */     MutableComponent mutableComponent1 = Component.literal(Integer.toString(curPlayers)).withStyle(ChatFormatting.GRAY);
/* 195 */     MutableComponent mutableComponent2 = Component.literal(Integer.toString(maxPlayers)).withStyle(ChatFormatting.GRAY);
/* 196 */     return (Component)Component.translatable("multiplayer.status.player_count", new Object[] { mutableComponent1, mutableComponent2 }).withStyle(ChatFormatting.DARK_GRAY);
/*     */   }
/*     */   
/*     */   public void tick() {
/* 200 */     synchronized (this.connections) {
/* 201 */       Iterator<Connection> iterator = this.connections.iterator();
/* 202 */       while (iterator.hasNext()) {
/* 203 */         Connection connection = iterator.next();
/*     */         
/* 205 */         if (connection.isConnected()) {
/* 206 */           connection.tick(); continue;
/*     */         } 
/* 208 */         iterator.remove();
/* 209 */         connection.handleDisconnection();
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAll() {
/* 216 */     synchronized (this.connections) {
/* 217 */       Iterator<Connection> iterator = this.connections.iterator();
/* 218 */       while (iterator.hasNext()) {
/* 219 */         Connection connection = iterator.next();
/*     */         
/* 221 */         if (connection.isConnected()) {
/* 222 */           iterator.remove();
/* 223 */           connection.disconnect((Component)Component.translatable("multiplayer.status.cancelled"));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/ServerStatusPinger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */