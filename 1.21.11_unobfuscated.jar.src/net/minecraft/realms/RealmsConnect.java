/*     */ package net.minecraft.realms;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.screens.DisconnectedScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.multiplayer.ClientHandshakePacketListenerImpl;
/*     */ import net.minecraft.client.multiplayer.LevelLoadTracker;
/*     */ import net.minecraft.client.multiplayer.chat.report.ReportEnvironment;
/*     */ import net.minecraft.client.multiplayer.resolver.ServerAddress;
/*     */ import net.minecraft.client.quickplay.QuickPlayLog;
/*     */ import net.minecraft.client.resources.server.ServerPackManager;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.login.ClientLoginPacketListener;
/*     */ import net.minecraft.network.protocol.login.ServerboundHelloPacket;
/*     */ import net.minecraft.server.network.EventLoopGroupHolder;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RealmsConnect {
/*  26 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final Screen onlineScreen;
/*     */   private volatile boolean aborted;
/*     */   private Connection connection;
/*     */   
/*     */   public RealmsConnect(Screen onlineScreen) {
/*  33 */     this.onlineScreen = onlineScreen;
/*     */   }
/*     */   
/*     */   public void connect(final RealmsServer server, ServerAddress hostAndPort) {
/*  37 */     final Minecraft minecraft = Minecraft.getInstance();
/*  38 */     minecraft.prepareForMultiplayer();
/*  39 */     minecraft.getNarrator().saySystemNow((Component)Component.translatable("mco.connect.success"));
/*     */     
/*  41 */     final String hostname = hostAndPort.getHost();
/*  42 */     final int port = hostAndPort.getPort();
/*  43 */     new Thread("Realms-connect-task")
/*     */       {
/*     */         public void run() {
/*  46 */           InetSocketAddress address = null;
/*     */           try {
/*  48 */             address = new InetSocketAddress(hostname, port);
/*     */             
/*  50 */             if (RealmsConnect.this.aborted) {
/*     */               return;
/*     */             }
/*     */             
/*  54 */             RealmsConnect.this.connection = Connection.connectToServer(address, EventLoopGroupHolder.remote(minecraft.options.useNativeTransport()), minecraft.getDebugOverlay().getBandwidthLogger());
/*     */             
/*  56 */             if (RealmsConnect.this.aborted) {
/*     */               return;
/*     */             }
/*     */             
/*  60 */             ClientHandshakePacketListenerImpl clientHandshakePacketListener = new ClientHandshakePacketListenerImpl(RealmsConnect.this.connection, minecraft, server.toServerData(hostname), RealmsConnect.this.onlineScreen, false, null, status -> {  }, new LevelLoadTracker(), null);
/*  61 */             if (server.isMinigameActive()) {
/*  62 */               clientHandshakePacketListener.setMinigameName(server.minigameName);
/*     */             }
/*     */             
/*  65 */             if (RealmsConnect.this.aborted) {
/*     */               return;
/*     */             }
/*     */             
/*  69 */             RealmsConnect.this.connection.initiateServerboundPlayConnection(hostname, port, (ClientLoginPacketListener)clientHandshakePacketListener);
/*     */             
/*  71 */             if (RealmsConnect.this.aborted) {
/*     */               return;
/*     */             }
/*     */             
/*  75 */             RealmsConnect.this.connection.send((Packet)new ServerboundHelloPacket(minecraft.getUser().getName(), minecraft.getUser().getProfileId()));
/*  76 */             minecraft.updateReportEnvironment(ReportEnvironment.realm(server));
/*  77 */             minecraft.quickPlayLog().setWorldData(QuickPlayLog.Type.REALMS, String.valueOf(server.id), Objects.<String>requireNonNullElse(server.name, "unknown"));
/*  78 */             minecraft.getDownloadedPackSource().configureForServerControl(RealmsConnect.this.connection, ServerPackManager.PackPromptStatus.ALLOWED);
/*  79 */           } catch (Exception e) {
/*  80 */             minecraft.getDownloadedPackSource().cleanupAfterDisconnect();
/*     */             
/*  82 */             if (RealmsConnect.this.aborted) {
/*     */               return;
/*     */             }
/*     */             
/*  86 */             RealmsConnect.LOGGER.error("Couldn't connect to world", e);
/*  87 */             String message = e.toString();
/*     */             
/*  89 */             if (address != null) {
/*  90 */               String filter = String.valueOf(address) + ":" + String.valueOf(address);
/*  91 */               message = message.replaceAll(filter, "");
/*     */             } 
/*     */             
/*  94 */             DisconnectedScreen screen = new DisconnectedScreen(RealmsConnect.this.onlineScreen, 
/*     */                 
/*  96 */                 (Component)Component.translatable("mco.connect.failed"), 
/*  97 */                 (Component)Component.translatable("disconnect.genericReason", new Object[] { message }), CommonComponents.GUI_BACK);
/*     */ 
/*     */             
/* 100 */             minecraft.execute(() -> minecraft.setScreen((Screen)screen));
/*     */           } 
/*     */         }
/* 103 */       }.start();
/*     */   }
/*     */   
/*     */   public void abort() {
/* 107 */     this.aborted = true;
/*     */     
/* 109 */     if (this.connection != null && 
/* 110 */       this.connection.isConnected()) {
/* 111 */       this.connection.disconnect((Component)Component.translatable("disconnect.genericReason"));
/* 112 */       this.connection.handleDisconnection();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 118 */     if (this.connection != null)
/* 119 */       if (this.connection.isConnected()) {
/* 120 */         this.connection.tick();
/*     */       } else {
/* 122 */         this.connection.handleDisconnection();
/*     */       }  
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/realms/RealmsConnect.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */