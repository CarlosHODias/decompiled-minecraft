/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import io.netty.channel.ChannelFuture;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.atomic.AtomicInteger;
/*     */ import net.minecraft.DefaultUncaughtExceptionHandler;
/*     */ import net.minecraft.client.GameNarrator;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
/*     */ import net.minecraft.client.multiplayer.LevelLoadTracker;
/*     */ import net.minecraft.client.multiplayer.ServerData;
/*     */ import net.minecraft.client.multiplayer.TransferState;
/*     */ import net.minecraft.client.multiplayer.chat.report.ReportEnvironment;
/*     */ import net.minecraft.client.multiplayer.resolver.ResolvedServerAddress;
/*     */ import net.minecraft.client.multiplayer.resolver.ServerAddress;
/*     */ import net.minecraft.client.multiplayer.resolver.ServerNameResolver;
/*     */ import net.minecraft.client.quickplay.QuickPlay;
/*     */ import net.minecraft.client.quickplay.QuickPlayLog;
/*     */ import net.minecraft.client.resources.server.ServerPackManager;
/*     */ import net.minecraft.network.ClientboundPacketListener;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketFlow;
/*     */ import net.minecraft.network.protocol.login.LoginProtocols;
/*     */ import net.minecraft.network.protocol.login.ServerboundHelloPacket;
/*     */ import net.minecraft.server.network.EventLoopGroupHolder;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ConnectScreen
/*     */   extends Screen {
/*  38 */   private static final AtomicInteger UNIQUE_THREAD_ID = new AtomicInteger(0);
/*  39 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static final long NARRATION_DELAY_MS = 2000L;
/*  41 */   public static final Component ABORT_CONNECTION = (Component)Component.translatable("connect.aborted");
/*  42 */   public static final Component UNKNOWN_HOST_MESSAGE = (Component)Component.translatable("disconnect.genericReason", new Object[] { Component.translatable("disconnect.unknownHost") });
/*     */   
/*     */   private volatile Connection connection;
/*     */   private ChannelFuture channelFuture;
/*     */   private volatile boolean aborted;
/*     */   private final Screen parent;
/*  48 */   private Component status = (Component)Component.translatable("connect.connecting");
/*  49 */   private long lastNarration = -1L;
/*     */   private final Component connectFailedTitle;
/*     */   
/*     */   private ConnectScreen(Screen parent, Component connectFailedTitle) {
/*  53 */     super(GameNarrator.NO_TITLE);
/*  54 */     this.parent = parent;
/*  55 */     this.connectFailedTitle = connectFailedTitle;
/*     */   }
/*     */   public static void startConnecting(Screen parent, Minecraft minecraft, ServerAddress hostAndPort, ServerData data, boolean isQuickPlay, TransferState transferState) {
/*     */     Component connectFailedTitle;
/*  59 */     if (minecraft.screen instanceof ConnectScreen) {
/*  60 */       LOGGER.error("Attempt to connect while already connecting");
/*     */       
/*     */       return;
/*     */     } 
/*  64 */     if (transferState != null) {
/*  65 */       connectFailedTitle = CommonComponents.TRANSFER_CONNECT_FAILED;
/*  66 */     } else if (isQuickPlay) {
/*  67 */       connectFailedTitle = QuickPlay.ERROR_TITLE;
/*     */     } else {
/*  69 */       connectFailedTitle = CommonComponents.CONNECT_FAILED;
/*     */     } 
/*  71 */     ConnectScreen screen = new ConnectScreen(parent, connectFailedTitle);
/*  72 */     if (transferState != null) {
/*  73 */       screen.updateStatus((Component)Component.translatable("connect.transferring"));
/*     */     }
/*  75 */     minecraft.disconnectWithProgressScreen(false);
/*  76 */     minecraft.prepareForMultiplayer();
/*  77 */     minecraft.updateReportEnvironment(ReportEnvironment.thirdParty(data.ip));
/*  78 */     minecraft.quickPlayLog().setWorldData(QuickPlayLog.Type.MULTIPLAYER, data.ip, data.name);
/*  79 */     minecraft.setScreen(screen);
/*  80 */     screen.connect(minecraft, hostAndPort, data, transferState);
/*     */   }
/*     */   
/*     */   private void connect(final Minecraft minecraft, final ServerAddress hostAndPort, final ServerData server, final TransferState transferState) {
/*  84 */     LOGGER.info("Connecting to {}, {}", hostAndPort.getHost(), hostAndPort.getPort());
/*  85 */     Thread thread = new Thread("Server Connector #" + UNIQUE_THREAD_ID.incrementAndGet())
/*     */       {
/*     */         public void run() {
/*  88 */           InetSocketAddress address = null; try {
/*     */             Connection pendingConnection;
/*  90 */             if (ConnectScreen.this.aborted) {
/*     */               return;
/*     */             }
/*     */             
/*  94 */             Optional<InetSocketAddress> resolvedAddress = ServerNameResolver.DEFAULT.resolveAddress(hostAndPort)
/*  95 */               .map(ResolvedServerAddress::asInetSocketAddress);
/*  96 */             if (ConnectScreen.this.aborted) {
/*     */               return;
/*     */             }
/*  99 */             if (resolvedAddress.isEmpty()) {
/* 100 */               minecraft.execute(() -> minecraft.setScreen(new DisconnectedScreen(ConnectScreen.this.parent, ConnectScreen.this.connectFailedTitle, ConnectScreen.UNKNOWN_HOST_MESSAGE)));
/*     */               return;
/*     */             } 
/* 103 */             address = resolvedAddress.get();
/*     */ 
/*     */             
/* 106 */             synchronized (ConnectScreen.this) {
/* 107 */               if (ConnectScreen.this.aborted) {
/*     */                 return;
/*     */               }
/* 110 */               pendingConnection = new Connection(PacketFlow.CLIENTBOUND);
/* 111 */               pendingConnection.setBandwidthLogger(minecraft.getDebugOverlay().getBandwidthLogger());
/*     */               
/* 113 */               ConnectScreen.this.channelFuture = Connection.connect(address, EventLoopGroupHolder.remote(minecraft.options.useNativeTransport()), pendingConnection);
/*     */             } 
/* 115 */             ConnectScreen.this.channelFuture.syncUninterruptibly();
/*     */             
/* 117 */             synchronized (ConnectScreen.this) {
/* 118 */               if (ConnectScreen.this.aborted) {
/* 119 */                 pendingConnection.disconnect(ConnectScreen.ABORT_CONNECTION);
/*     */                 return;
/*     */               } 
/* 122 */               ConnectScreen.this.connection = pendingConnection;
/* 123 */               minecraft.getDownloadedPackSource().configureForServerControl(pendingConnection, convertPackStatus(server.getResourcePackStatus()));
/*     */             } 
/* 125 */             ConnectScreen.this.connection.initiateServerboundPlayConnection(
/* 126 */                 address.getHostName(), 
/* 127 */                 address.getPort(), LoginProtocols.SERVERBOUND, LoginProtocols.CLIENTBOUND, (ClientboundPacketListener)new ClientHandshakePacketListenerImpl(ConnectScreen.this.connection, minecraft, server, ConnectScreen.this.parent, false, null, ConnectScreen.this::updateStatus, new LevelLoadTracker(), transferState), (transferState != null));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 133 */             ConnectScreen.this.connection.send((Packet)new ServerboundHelloPacket(minecraft.getUser().getName(), minecraft.getUser().getProfileId()));
/* 134 */           } catch (Exception exception) {
/* 135 */             Exception cause; if (ConnectScreen.this.aborted) {
/*     */               return;
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 142 */             Throwable throwable = exception.getCause(); if (throwable instanceof Exception) { Exception originalCause = (Exception)throwable;
/* 143 */               cause = originalCause; }
/*     */             else
/* 145 */             { cause = exception; }
/*     */ 
/*     */             
/* 148 */             ConnectScreen.LOGGER.error("Couldn't connect to server", exception);
/* 149 */             String message = (address == null) ? cause.getMessage() : cause.getMessage().replaceAll(address.getHostName() + ":" + address.getHostName(), "").replaceAll(address.toString(), "");
/* 150 */             minecraft.execute(() -> minecraft.setScreen(new DisconnectedScreen(ConnectScreen.this.parent, ConnectScreen.this.connectFailedTitle, (Component)Component.translatable("disconnect.genericReason", new Object[] { message }))));
/*     */           } 
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*     */         private static ServerPackManager.PackPromptStatus convertPackStatus(ServerData.ServerPackStatus resourcePackStatus) {
/*     */           // Byte code:
/*     */           //   0: getstatic net/minecraft/client/gui/screens/ConnectScreen$2.$SwitchMap$net$minecraft$client$multiplayer$ServerData$ServerPackStatus : [I
/*     */           //   3: aload_0
/*     */           //   4: invokevirtual ordinal : ()I
/*     */           //   7: iaload
/*     */           //   8: tableswitch default -> 36, 1 -> 46, 2 -> 52, 3 -> 58
/*     */           //   36: new java/lang/MatchException
/*     */           //   39: dup
/*     */           //   40: aconst_null
/*     */           //   41: aconst_null
/*     */           //   42: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */           //   45: athrow
/*     */           //   46: getstatic net/minecraft/client/resources/server/ServerPackManager$PackPromptStatus.ALLOWED : Lnet/minecraft/client/resources/server/ServerPackManager$PackPromptStatus;
/*     */           //   49: goto -> 61
/*     */           //   52: getstatic net/minecraft/client/resources/server/ServerPackManager$PackPromptStatus.DECLINED : Lnet/minecraft/client/resources/server/ServerPackManager$PackPromptStatus;
/*     */           //   55: goto -> 61
/*     */           //   58: getstatic net/minecraft/client/resources/server/ServerPackManager$PackPromptStatus.PENDING : Lnet/minecraft/client/resources/server/ServerPackManager$PackPromptStatus;
/*     */           //   61: areturn
/*     */           // Line number table:
/*     */           //   Java source line number -> byte code offset
/*     */           //   #155	-> 0
/*     */           //   #156	-> 46
/*     */           //   #157	-> 52
/*     */           //   #158	-> 58
/*     */           //   #155	-> 61
/*     */           // Local variable table:
/*     */           //   start	length	slot	name	descriptor
/*     */           //   0	62	0	resourcePackStatus	Lnet/minecraft/client/multiplayer/ServerData$ServerPackStatus;
/*     */         }
/*     */       };
/* 162 */     thread.setUncaughtExceptionHandler((Thread.UncaughtExceptionHandler)new DefaultUncaughtExceptionHandler(LOGGER));
/* 163 */     thread.start();
/*     */   }
/*     */   
/*     */   private void updateStatus(Component status) {
/* 167 */     this.status = status;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 172 */     if (this.connection != null) {
/* 173 */       if (this.connection.isConnected()) {
/* 174 */         this.connection.tick();
/*     */       } else {
/* 176 */         this.connection.handleDisconnection();
/*     */       } 
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldCloseOnEsc() {
/* 183 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/* 188 */     addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button -> {
/*     */             synchronized (this) {
/*     */               this.aborted = true;
/*     */               if (this.channelFuture != null) {
/*     */                 this.channelFuture.cancel(true);
/*     */                 this.channelFuture = null;
/*     */               } 
/*     */               if (this.connection != null) {
/*     */                 this.connection.disconnect(ABORT_CONNECTION);
/*     */               }
/*     */             } 
/*     */             this.minecraft.setScreen(this.parent);
/* 200 */           }).bounds(this.width / 2 - 100, this.height / 4 + 120 + 12, 200, 20).build());
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 205 */     super.render(graphics, mouseX, mouseY, a);
/*     */     
/* 207 */     long current = Util.getMillis();
/* 208 */     if (current - this.lastNarration > 2000L) {
/* 209 */       this.lastNarration = current;
/*     */       
/* 211 */       this.minecraft.getNarrator().saySystemNow((Component)Component.translatable("narrator.joining"));
/*     */     } 
/*     */     
/* 214 */     graphics.drawCenteredString(this.font, this.status, this.width / 2, this.height / 2 - 50, -1);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/ConnectScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */