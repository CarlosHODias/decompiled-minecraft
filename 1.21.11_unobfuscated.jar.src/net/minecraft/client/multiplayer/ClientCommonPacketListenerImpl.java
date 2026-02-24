/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Path;
/*     */ import java.time.Duration;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportType;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.ConnectScreen;
/*     */ import net.minecraft.client.gui.screens.DisconnectedScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.TitleScreen;
/*     */ import net.minecraft.client.gui.screens.dialog.DialogConnectionAccess;
/*     */ import net.minecraft.client.gui.screens.dialog.DialogScreen;
/*     */ import net.minecraft.client.gui.screens.dialog.DialogScreens;
/*     */ import net.minecraft.client.gui.screens.dialog.WaitingForResponseScreen;
/*     */ import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
/*     */ import net.minecraft.client.multiplayer.resolver.ServerAddress;
/*     */ import net.minecraft.client.resources.server.DownloadedPackSource;
/*     */ import net.minecraft.client.telemetry.WorldSessionTelemetryManager;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.DisconnectionDetails;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.ServerboundPacketListener;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketUtils;
/*     */ import net.minecraft.network.protocol.common.ClientCommonPacketListener;
/*     */ import net.minecraft.network.protocol.common.ClientboundClearDialogPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundCustomReportDetailsPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundDisconnectPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundKeepAlivePacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundPingPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundResourcePackPopPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundResourcePackPushPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundServerLinksPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundShowDialogPacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundStoreCookiePacket;
/*     */ import net.minecraft.network.protocol.common.ClientboundTransferPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundCustomClickActionPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundKeepAlivePacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundPongPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundResourcePackPacket;
/*     */ import net.minecraft.network.protocol.common.custom.BrandPayload;
/*     */ import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
/*     */ import net.minecraft.network.protocol.cookie.ClientboundCookieRequestPacket;
/*     */ import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.ServerLinks;
/*     */ import net.minecraft.server.dialog.Dialog;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public abstract class ClientCommonPacketListenerImpl
/*     */   implements ClientCommonPacketListener
/*     */ {
/*  79 */   private static final Component GENERIC_DISCONNECT_MESSAGE = (Component)Component.translatable("disconnect.lost");
/*     */   
/*  81 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   protected final Minecraft minecraft;
/*     */   
/*     */   protected final Connection connection;
/*     */   
/*     */   protected final ServerData serverData;
/*     */   protected String serverBrand;
/*     */   protected final WorldSessionTelemetryManager telemetryManager;
/*     */   protected final Screen postDisconnectScreen;
/*     */   protected boolean isTransferring;
/*  92 */   private final List<DeferredPacket> deferredPackets = new ArrayList<>();
/*     */   
/*     */   protected final Map<Identifier, byte[]> serverCookies;
/*     */   protected Map<String, String> customReportDetails;
/*     */   private ServerLinks serverLinks;
/*     */   protected final Map<UUID, PlayerInfo> seenPlayers;
/*     */   protected boolean seenInsecureChatWarning;
/*     */   
/*     */   protected ClientCommonPacketListenerImpl(Minecraft minecraft, Connection connection, CommonListenerCookie cookie) {
/* 101 */     this.minecraft = minecraft;
/* 102 */     this.connection = connection;
/* 103 */     this.serverData = cookie.serverData();
/* 104 */     this.serverBrand = cookie.serverBrand();
/* 105 */     this.telemetryManager = cookie.telemetryManager();
/* 106 */     this.postDisconnectScreen = cookie.postDisconnectScreen();
/* 107 */     this.serverCookies = cookie.serverCookies();
/* 108 */     this.customReportDetails = cookie.customReportDetails();
/* 109 */     this.serverLinks = cookie.serverLinks();
/* 110 */     this.seenPlayers = new HashMap<>(cookie.seenPlayers());
/* 111 */     this.seenInsecureChatWarning = cookie.seenInsecureChatWarning();
/*     */   }
/*     */   
/*     */   public ServerLinks serverLinks() {
/* 115 */     return this.serverLinks;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPacketError(Packet packet, Exception cause) {
/* 120 */     LOGGER.error("Failed to handle packet {}, disconnecting", packet, cause);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 125 */     Optional<Path> report = storeDisconnectionReport(packet, cause);
/* 126 */     Optional<URI> bugReportLink = this.serverLinks.findKnownType(ServerLinks.KnownLinkType.BUG_REPORT).map(ServerLinks.Entry::link);
/*     */     
/* 128 */     this.connection.disconnect(new DisconnectionDetails(
/* 129 */           (Component)Component.translatable("disconnect.packetError"), report, bugReportLink));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DisconnectionDetails createDisconnectionInfo(Component reason, Throwable cause) {
/* 137 */     Optional<Path> report = storeDisconnectionReport(null, cause);
/* 138 */     Optional<URI> bugReportUrl = this.serverLinks.findKnownType(ServerLinks.KnownLinkType.BUG_REPORT).map(ServerLinks.Entry::link);
/*     */     
/* 140 */     return new DisconnectionDetails(reason, report, bugReportUrl);
/*     */   }
/*     */   
/*     */   private Optional<Path> storeDisconnectionReport(Packet packet, Throwable cause) {
/* 144 */     CrashReport report = CrashReport.forThrowable(cause, "Packet handling error");
/*     */     
/* 146 */     PacketUtils.fillCrashReport(report, (PacketListener)this, packet);
/*     */     
/* 148 */     Path debugDir = this.minecraft.gameDirectory.toPath().resolve("debug");
/* 149 */     Path reportFile = debugDir.resolve("disconnect-" + Util.getFilenameFormattedDateTime() + "-client.txt");
/* 150 */     Optional<ServerLinks.Entry> bugReportLink = this.serverLinks.findKnownType(ServerLinks.KnownLinkType.BUG_REPORT);
/* 151 */     List<String> extraComments = bugReportLink.<List<String>>map(link -> List.of("Server bug reporting link: " + String.valueOf(link.link()))).orElse(List.of());
/* 152 */     if (report.saveToFile(reportFile, ReportType.NETWORK_PROTOCOL_ERROR, extraComments)) {
/* 153 */       return Optional.of(reportFile);
/*     */     }
/* 155 */     return Optional.empty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldHandleMessage(Packet<?> packet) {
/* 160 */     if (super.shouldHandleMessage(packet)) {
/* 161 */       return true;
/*     */     }
/*     */     
/* 164 */     return (this.isTransferring && (packet instanceof ClientboundStoreCookiePacket || packet instanceof ClientboundTransferPacket));
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleKeepAlive(ClientboundKeepAlivePacket packet) {
/* 169 */     sendWhen((Packet<? extends ServerboundPacketListener>)new ServerboundKeepAlivePacket(packet.getId()), () -> !RenderSystem.isFrozenAtPollEvents(), Duration.ofMinutes(1L));
/*     */   }
/*     */ 
/*     */   
/*     */   public void handlePing(ClientboundPingPacket packet) {
/* 174 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 175 */     send((Packet<?>)new ServerboundPongPacket(packet.getId()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleCustomPayload(ClientboundCustomPayloadPacket packet) {
/* 180 */     CustomPacketPayload payload = packet.payload();
/* 181 */     if (payload instanceof net.minecraft.network.protocol.common.custom.DiscardedPayload) {
/*     */       return;
/*     */     }
/*     */     
/* 185 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 186 */     if (payload instanceof BrandPayload) { BrandPayload brand = (BrandPayload)payload;
/* 187 */       this.serverBrand = brand.brand();
/* 188 */       this.telemetryManager.onServerBrandReceived(brand.brand()); }
/*     */     else
/* 190 */     { handleCustomPayload(payload); }
/*     */   
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleResourcePackPush(ClientboundResourcePackPushPacket packet) {
/* 198 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 199 */     UUID packId = packet.id();
/* 200 */     URL url = parseResourcePackUrl(packet.url());
/* 201 */     if (url == null) {
/* 202 */       this.connection.send((Packet)new ServerboundResourcePackPacket(packId, ServerboundResourcePackPacket.Action.INVALID_URL));
/*     */       
/*     */       return;
/*     */     } 
/* 206 */     String hash = packet.hash();
/* 207 */     boolean required = packet.required();
/*     */     
/* 209 */     ServerData.ServerPackStatus serverPackStatus = (this.serverData != null) ? this.serverData.getResourcePackStatus() : ServerData.ServerPackStatus.PROMPT;
/* 210 */     if (serverPackStatus == ServerData.ServerPackStatus.PROMPT || (required && serverPackStatus == ServerData.ServerPackStatus.DISABLED)) {
/* 211 */       this.minecraft.setScreen(addOrUpdatePackPrompt(packId, url, hash, required, packet.prompt().orElse(null)));
/*     */     } else {
/*     */       
/* 214 */       this.minecraft.getDownloadedPackSource().pushPack(packId, url, hash);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleResourcePackPop(ClientboundResourcePackPopPacket packet) {
/* 220 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 221 */     packet.id().ifPresentOrElse(id -> this.minecraft.getDownloadedPackSource().popPack(id), () -> this.minecraft.getDownloadedPackSource().popAll());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Component preparePackPrompt(Component header, Component prompt) {
/* 228 */     if (prompt == null) {
/* 229 */       return header;
/*     */     }
/* 231 */     return (Component)Component.translatable("multiplayer.texturePrompt.serverPrompt", new Object[] { header, prompt });
/*     */   }
/*     */ 
/*     */   
/*     */   private static URL parseResourcePackUrl(String urlString) {
/*     */     try {
/* 237 */       URL url = new URL(urlString);
/* 238 */       String protocol = url.getProtocol();
/* 239 */       if ("http".equals(protocol) || "https".equals(protocol)) {
/* 240 */         return url;
/*     */       }
/* 242 */     } catch (MalformedURLException e) {
/* 243 */       return null;
/*     */     } 
/* 245 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleRequestCookie(ClientboundCookieRequestPacket packet) {
/* 250 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 251 */     this.connection.send((Packet)new ServerboundCookieResponsePacket(packet.key(), this.serverCookies.get(packet.key())));
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleStoreCookie(ClientboundStoreCookiePacket packet) {
/* 256 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 257 */     this.serverCookies.put(packet.key(), packet.payload());
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleCustomReportDetails(ClientboundCustomReportDetailsPacket packet) {
/* 262 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 263 */     this.customReportDetails = packet.details();
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleServerLinks(ClientboundServerLinksPacket packet) {
/* 268 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*     */     
/* 270 */     List<ServerLinks.UntrustedEntry> untrustedEntries = packet.links();
/* 271 */     ImmutableList.Builder<ServerLinks.Entry> trustedEntries = ImmutableList.builderWithExpectedSize(untrustedEntries.size());
/*     */     
/* 273 */     for (ServerLinks.UntrustedEntry entry : untrustedEntries) {
/*     */       try {
/* 275 */         URI parsedLink = Util.parseAndValidateUntrustedUri(entry.link());
/* 276 */         trustedEntries.add(new ServerLinks.Entry(entry.type(), parsedLink));
/* 277 */       } catch (Exception e) {
/* 278 */         LOGGER.warn("Received invalid link for type {}:{}", new Object[] { entry.type(), entry.link(), e });
/*     */       } 
/*     */     } 
/*     */     
/* 282 */     this.serverLinks = new ServerLinks((List)trustedEntries.build());
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleShowDialog(ClientboundShowDialogPacket packet) {
/* 287 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 288 */     showDialog(packet.dialog(), this.minecraft.screen);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void showDialog(Holder<Dialog> dialog, Screen activeScreen) {
/* 294 */     showDialog(dialog, createDialogAccess(), activeScreen);
/*     */   }
/*     */   
/*     */   protected void showDialog(Holder<Dialog> dialog, DialogConnectionAccess connectionAccess, Screen activeScreen) {
/*     */     Screen previousScreen;
/* 299 */     if (activeScreen instanceof DialogScreen.WarningScreen) { DialogScreen.WarningScreen existingWarningScreen = (DialogScreen.WarningScreen)activeScreen;
/*     */       
/* 301 */       Screen hiddenScreen = existingWarningScreen.returnScreen();
/* 302 */       DialogScreen<?> hiddenDialog = (DialogScreen)hiddenScreen; Screen screen1 = (hiddenScreen instanceof DialogScreen) ? hiddenDialog.previousScreen() : hiddenScreen;
/* 303 */       DialogScreen<?> newDialogScreen = DialogScreens.createFromData((Dialog)dialog.value(), screen1, connectionAccess);
/*     */       
/* 305 */       if (newDialogScreen != null) {
/* 306 */         existingWarningScreen.updateReturnScreen((Screen)newDialogScreen);
/*     */       } else {
/* 308 */         LOGGER.warn("Failed to show dialog for data {}", dialog);
/*     */       } 
/*     */       
/*     */       return; }
/*     */ 
/*     */     
/* 314 */     if (activeScreen instanceof DialogScreen) { DialogScreen<?> existingDialog = (DialogScreen)activeScreen;
/*     */ 
/*     */       
/* 317 */       previousScreen = existingDialog.previousScreen(); }
/* 318 */     else if (activeScreen instanceof WaitingForResponseScreen) { WaitingForResponseScreen waitScreen = (WaitingForResponseScreen)activeScreen;
/* 319 */       previousScreen = waitScreen.previousScreen(); }
/*     */     else
/* 321 */     { previousScreen = activeScreen; }
/*     */     
/* 323 */     DialogScreen dialogScreen = DialogScreens.createFromData((Dialog)dialog.value(), previousScreen, connectionAccess);
/* 324 */     if (dialogScreen != null) {
/* 325 */       this.minecraft.setScreen((Screen)dialogScreen);
/*     */     } else {
/* 327 */       LOGGER.warn("Failed to show dialog for data {}", dialog);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleClearDialog(ClientboundClearDialogPacket packet) {
/* 333 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 334 */     clearDialog();
/*     */   }
/*     */   
/*     */   public void clearDialog() {
/* 338 */     Screen screen = this.minecraft.screen; if (screen instanceof DialogScreen.WarningScreen) { DialogScreen.WarningScreen existingWarningScreen = (DialogScreen.WarningScreen)screen;
/* 339 */       Screen currentReturnScreen = existingWarningScreen.returnScreen();
/* 340 */       if (currentReturnScreen instanceof DialogScreen) { DialogScreen<?> dialogScreen = (DialogScreen)currentReturnScreen;
/* 341 */         existingWarningScreen.updateReturnScreen(dialogScreen.previousScreen()); }
/*     */        }
/* 343 */     else { screen = this.minecraft.screen; if (screen instanceof DialogScreen) { DialogScreen<?> dialog = (DialogScreen)screen;
/*     */         
/* 345 */         this.minecraft.setScreen(dialog.previousScreen()); }
/*     */        }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleTransfer(ClientboundTransferPacket packet) {
/* 352 */     this.isTransferring = true;
/* 353 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*     */     
/* 355 */     if (this.serverData == null) {
/* 356 */       throw new IllegalStateException("Cannot transfer to server from singleplayer");
/*     */     }
/*     */     
/* 359 */     this.connection.disconnect((Component)Component.translatable("disconnect.transfer"));
/* 360 */     this.connection.setReadOnly();
/* 361 */     this.connection.handleDisconnection();
/*     */     
/* 363 */     ServerAddress address = new ServerAddress(packet.host(), packet.port());
/* 364 */     ConnectScreen.startConnecting(Objects.<Screen>requireNonNullElseGet(this.postDisconnectScreen, TitleScreen::new), this.minecraft, address, this.serverData, false, new TransferState(this.serverCookies, this.seenPlayers, this.seenInsecureChatWarning));
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleDisconnect(ClientboundDisconnectPacket packet) {
/* 369 */     this.connection.disconnect(packet.reason());
/*     */   }
/*     */   
/*     */   protected void sendDeferredPackets() {
/* 373 */     Iterator<DeferredPacket> iterator = this.deferredPackets.iterator();
/* 374 */     while (iterator.hasNext()) {
/* 375 */       DeferredPacket deferredPacket = iterator.next();
/* 376 */       if (deferredPacket.sendCondition().getAsBoolean()) {
/* 377 */         send(deferredPacket.packet);
/* 378 */         iterator.remove(); continue;
/* 379 */       }  if (deferredPacket.expirationTime() <= Util.getMillis()) {
/* 380 */         iterator.remove();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void send(Packet<?> packet) {
/* 386 */     this.connection.send(packet);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisconnect(DisconnectionDetails details) {
/* 391 */     this.telemetryManager.onDisconnect();
/* 392 */     this.minecraft.disconnect(createDisconnectScreen(details), this.isTransferring);
/* 393 */     LOGGER.warn("Client disconnected with reason: {}", details.reason().getString());
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillListenerSpecificCrashDetails(CrashReport report, CrashReportCategory connectionDetails) {
/* 398 */     connectionDetails.setDetail("Is Local", () -> String.valueOf(this.connection.isMemoryConnection()));
/* 399 */     connectionDetails.setDetail("Server type", () -> (this.serverData != null) ? this.serverData.type().toString() : "<none>");
/* 400 */     connectionDetails.setDetail("Server brand", () -> this.serverBrand);
/*     */     
/* 402 */     if (!this.customReportDetails.isEmpty()) {
/* 403 */       CrashReportCategory serverDetailsCategory = report.addCategory("Custom Server Details");
/* 404 */       Objects.requireNonNull(serverDetailsCategory); this.customReportDetails.forEach(serverDetailsCategory::setDetail);
/*     */     } 
/*     */   }
/*     */   
/*     */   protected Screen createDisconnectScreen(DisconnectionDetails details) {
/* 409 */     Screen callbackScreen = Objects.<Screen>requireNonNullElseGet(this.postDisconnectScreen, () -> (this.serverData != null) ? (Screen)new JoinMultiplayerScreen((Screen)new TitleScreen()) : (Screen)new TitleScreen());
/* 410 */     if (this.serverData != null && this.serverData.isRealm()) {
/* 411 */       return (Screen)new DisconnectedScreen(callbackScreen, GENERIC_DISCONNECT_MESSAGE, details, CommonComponents.GUI_BACK);
/*     */     }
/* 413 */     return (Screen)new DisconnectedScreen(callbackScreen, GENERIC_DISCONNECT_MESSAGE, details);
/*     */   }
/*     */ 
/*     */   
/*     */   public String serverBrand() {
/* 418 */     return this.serverBrand;
/*     */   }
/*     */   
/*     */   private void sendWhen(Packet<? extends ServerboundPacketListener> packet, BooleanSupplier condition, Duration expireAfterDuration) {
/* 422 */     if (condition.getAsBoolean()) {
/* 423 */       send(packet);
/*     */     } else {
/* 425 */       this.deferredPackets.add(new DeferredPacket(packet, condition, Util.getMillis() + expireAfterDuration.toMillis()));
/*     */     } 
/*     */   } private static final class DeferredPacket extends Record {
/*     */     private final Packet<? extends ServerboundPacketListener> packet; private final BooleanSupplier sendCondition; private final long expirationTime;
/* 429 */     private DeferredPacket(Packet<? extends ServerboundPacketListener> packet, BooleanSupplier sendCondition, long expirationTime) { this.packet = packet; this.sendCondition = sendCondition; this.expirationTime = expirationTime; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$DeferredPacket;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #429	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$DeferredPacket; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$DeferredPacket;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #429	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$DeferredPacket; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$DeferredPacket;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #429	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$DeferredPacket;
/* 429 */       //   0	8	1	o	Ljava/lang/Object; } public Packet<? extends ServerboundPacketListener> packet() { return this.packet; } public BooleanSupplier sendCondition() { return this.sendCondition; } public long expirationTime() { return this.expirationTime; }
/*     */   
/*     */   }
/*     */   
/*     */   private Screen addOrUpdatePackPrompt(UUID packId, URL url, String hash, boolean required, Component prompt) {
/* 434 */     Screen currentScreen = this.minecraft.screen;
/* 435 */     if (currentScreen instanceof PackConfirmScreen) { PackConfirmScreen promptScreen = (PackConfirmScreen)currentScreen;
/* 436 */       return (Screen)promptScreen.update(this.minecraft, packId, url, hash, required, prompt); }
/*     */     
/* 438 */     return (Screen)new PackConfirmScreen(this.minecraft, currentScreen, List.of(new PackConfirmScreen.PendingRequest(packId, url, hash)), required, prompt);
/*     */   } protected abstract void handleCustomPayload(CustomPacketPayload paramCustomPacketPayload); protected abstract DialogConnectionAccess createDialogAccess(); private class PackConfirmScreen extends ConfirmScreen {
/*     */     private final List<PendingRequest> requests; private final Screen parentScreen;
/*     */     private static final class PendingRequest extends Record { private final UUID id; private final URL url; private final String hash;
/* 442 */       private PendingRequest(UUID id, URL url, String hash) { this.id = id; this.url = url; this.hash = hash; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #442	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #442	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #442	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest;
/* 442 */         //   0	8	1	o	Ljava/lang/Object; } public UUID id() { return this.id; } public URL url() { return this.url; } public String hash() { return this.hash; }
/*     */        }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private PackConfirmScreen(Minecraft minecraft, Screen parentScreen, List<PendingRequest> requests, boolean required, Component prompt) {
/* 449 */       super(result -> {
/*     */             minecraft.setScreen(parentScreen);
/*     */             
/*     */             DownloadedPackSource packSource = minecraft.getDownloadedPackSource();
/*     */             
/*     */             if (result) {
/*     */               if (ClientCommonPacketListenerImpl.this.serverData != null) {
/*     */                 ClientCommonPacketListenerImpl.this.serverData.setResourcePackStatus(ServerData.ServerPackStatus.ENABLED);
/*     */               }
/*     */               
/*     */               packSource.allowServerPacks();
/*     */             } else {
/*     */               packSource.rejectServerPacks();
/*     */               
/*     */               if (required) {
/*     */                 ClientCommonPacketListenerImpl.this.connection.disconnect((Component)Component.translatable("multiplayer.requiredTexturePrompt.disconnect"));
/*     */               } else if (ClientCommonPacketListenerImpl.this.serverData != null) {
/*     */                 ClientCommonPacketListenerImpl.this.serverData.setResourcePackStatus(ServerData.ServerPackStatus.DISABLED);
/*     */               } 
/*     */             } 
/*     */             
/*     */             for (PendingRequest request : (Iterable<PendingRequest>)requests) {
/*     */               packSource.pushPack(request.id, request.url, request.hash);
/*     */             }
/*     */             if (ClientCommonPacketListenerImpl.this.serverData != null) {
/*     */               ServerList.saveSingleServer(ClientCommonPacketListenerImpl.this.serverData);
/*     */             }
/* 476 */           }, required ? (Component)Component.translatable("multiplayer.requiredTexturePrompt.line1") : (Component)Component.translatable("multiplayer.texturePrompt.line1"), 
/* 477 */           ClientCommonPacketListenerImpl.preparePackPrompt(required ? (Component)Component.translatable("multiplayer.requiredTexturePrompt.line2").withStyle(new ChatFormatting[] { ChatFormatting.YELLOW, ChatFormatting.BOLD }) : (Component)Component.translatable("multiplayer.texturePrompt.line2"), prompt), 
/* 478 */           required ? CommonComponents.GUI_PROCEED : CommonComponents.GUI_YES, 
/* 479 */           required ? CommonComponents.GUI_DISCONNECT : CommonComponents.GUI_NO);
/*     */       
/* 481 */       this.requests = requests;
/* 482 */       this.parentScreen = parentScreen;
/*     */     }
/*     */     
/*     */     public PackConfirmScreen update(Minecraft minecraft, UUID id, URL url, String hash, boolean required, Component prompt) {
/* 486 */       ImmutableList immutableList = ImmutableList.builderWithExpectedSize(this.requests.size() + 1)
/* 487 */         .addAll(this.requests)
/* 488 */         .add(new PendingRequest(id, url, hash))
/* 489 */         .build();
/* 490 */       return new PackConfirmScreen(minecraft, this.parentScreen, (List<PendingRequest>)immutableList, required, prompt);
/*     */     }
/*     */   }
/*     */   private static final class PendingRequest extends Record {
/*     */     private final UUID id;
/*     */     private final URL url; private final String hash; private PendingRequest(UUID id, URL url, String hash) { this.id = id; this.url = url; this.hash = hash; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #442	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #442	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #442	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/ClientCommonPacketListenerImpl$PackConfirmScreen$PendingRequest;
/*     */       //   0	8	1	o	Ljava/lang/Object; } public UUID id() { return this.id; } public URL url() { return this.url; } public String hash() { return this.hash; }
/*     */   } protected abstract class CommonDialogAccess implements DialogConnectionAccess {
/* 497 */     public void disconnect(Component message) { ClientCommonPacketListenerImpl.this.connection.disconnect(message);
/*     */       
/* 499 */       ClientCommonPacketListenerImpl.this.connection.handleDisconnection(); }
/*     */ 
/*     */ 
/*     */     
/*     */     public void openDialog(Holder<Dialog> dialog, Screen activeScreen) {
/* 504 */       ClientCommonPacketListenerImpl.this.showDialog(dialog, this, activeScreen);
/*     */     }
/*     */ 
/*     */     
/*     */     public void sendCustomAction(Identifier id, Optional<Tag> payload) {
/* 509 */       ClientCommonPacketListenerImpl.this.send((Packet<?>)new ServerboundCustomClickActionPacket(id, payload));
/*     */     }
/*     */ 
/*     */     
/*     */     public ServerLinks serverLinks() {
/* 514 */       return ClientCommonPacketListenerImpl.this.serverLinks();
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/ClientCommonPacketListenerImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */