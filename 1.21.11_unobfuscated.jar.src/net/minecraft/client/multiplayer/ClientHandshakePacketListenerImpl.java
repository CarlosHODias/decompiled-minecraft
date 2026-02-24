/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.authlib.exceptions.AuthenticationException;
/*     */ import com.mojang.authlib.exceptions.AuthenticationUnavailableException;
/*     */ import com.mojang.authlib.exceptions.InsufficientPrivilegesException;
/*     */ import com.mojang.authlib.exceptions.InvalidCredentialsException;
/*     */ import com.mojang.authlib.exceptions.UserBannedException;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.math.BigInteger;
/*     */ import java.security.PublicKey;
/*     */ import java.time.Duration;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.atomic.AtomicReference;
/*     */ import java.util.function.Consumer;
/*     */ import javax.crypto.Cipher;
/*     */ import javax.crypto.SecretKey;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.client.ClientBrandRetriever;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.screens.DisconnectedScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.DisconnectionDetails;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.PacketSendListener;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.common.ServerboundClientInformationPacket;
/*     */ import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
/*     */ import net.minecraft.network.protocol.common.custom.BrandPayload;
/*     */ import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
/*     */ import net.minecraft.network.protocol.configuration.ConfigurationProtocols;
/*     */ import net.minecraft.network.protocol.cookie.ClientboundCookieRequestPacket;
/*     */ import net.minecraft.network.protocol.cookie.ServerboundCookieResponsePacket;
/*     */ import net.minecraft.network.protocol.login.ClientLoginPacketListener;
/*     */ import net.minecraft.network.protocol.login.ClientboundCustomQueryPacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundHelloPacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundLoginCompressionPacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundLoginDisconnectPacket;
/*     */ import net.minecraft.network.protocol.login.ClientboundLoginFinishedPacket;
/*     */ import net.minecraft.network.protocol.login.ServerboundCustomQueryAnswerPacket;
/*     */ import net.minecraft.network.protocol.login.ServerboundKeyPacket;
/*     */ import net.minecraft.network.protocol.login.ServerboundLoginAcknowledgedPacket;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.ServerLinks;
/*     */ import net.minecraft.util.Crypt;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ClientHandshakePacketListenerImpl implements ClientLoginPacketListener {
/*  58 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private final ServerData serverData;
/*     */   
/*     */   private final Screen parent;
/*     */   
/*     */   private final Consumer<Component> updateStatus;
/*     */   private final Connection connection;
/*     */   private final boolean newWorld;
/*     */   private final Duration worldLoadDuration;
/*     */   private String minigameName;
/*     */   private final LevelLoadTracker levelLoadTracker;
/*     */   private final Map<Identifier, byte[]> cookies;
/*     */   private final boolean wasTransferredTo;
/*     */   private final Map<UUID, PlayerInfo> seenPlayers;
/*     */   private final boolean seenInsecureChatWarning;
/*  76 */   private final AtomicReference<State> state = new AtomicReference<>(State.CONNECTING);
/*     */   
/*     */   public ClientHandshakePacketListenerImpl(Connection connection, Minecraft minecraft, ServerData serverData, Screen parent, boolean newWorld, Duration worldLoadDuration, Consumer<Component> updateStatus, LevelLoadTracker levelLoadTracker, TransferState transferState) {
/*  79 */     this.connection = connection;
/*  80 */     this.minecraft = minecraft;
/*  81 */     this.serverData = serverData;
/*  82 */     this.parent = parent;
/*  83 */     this.updateStatus = updateStatus;
/*  84 */     this.newWorld = newWorld;
/*  85 */     this.worldLoadDuration = worldLoadDuration;
/*  86 */     this.levelLoadTracker = levelLoadTracker;
/*  87 */     this.cookies = (transferState != null) ? (Map)new HashMap<>((Map)transferState.cookies()) : (Map)new HashMap<>();
/*  88 */     this.seenPlayers = (transferState != null) ? transferState.seenPlayers() : Map.<UUID, PlayerInfo>of();
/*  89 */     this.seenInsecureChatWarning = (transferState != null) ? transferState.seenInsecureChatWarning() : false;
/*  90 */     this.wasTransferredTo = (transferState != null);
/*     */   }
/*     */   
/*     */   private void switchState(State toState) {
/*  94 */     State newState = this.state.updateAndGet(lastState -> {
/*     */           if (!toState.fromStates.contains(lastState)) {
/*     */             throw new IllegalStateException("Tried to switch to " + String.valueOf(toState) + " from " + String.valueOf(lastState) + ", but expected one of " + String.valueOf(toState.fromStates));
/*     */           }
/*     */           return toState;
/*     */         });
/* 100 */     this.updateStatus.accept(newState.message);
/*     */   } public void handleHello(ClientboundHelloPacket packet) {
/*     */     Cipher decryptCipher, encryptCipher;
/*     */     String digest;
/*     */     ServerboundKeyPacket setKeyPacket;
/* 105 */     switchState(State.AUTHORIZING);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     try {
/* 113 */       SecretKey secretKey = Crypt.generateSecretKey();
/* 114 */       PublicKey publicKey = packet.getPublicKey();
/*     */       
/* 116 */       digest = new BigInteger(Crypt.digestData(packet.getServerId(), publicKey, secretKey)).toString(16);
/*     */       
/* 118 */       decryptCipher = Crypt.getCipher(2, secretKey);
/* 119 */       encryptCipher = Crypt.getCipher(1, secretKey);
/*     */       
/* 121 */       byte[] challenge = packet.getChallenge();
/* 122 */       setKeyPacket = new ServerboundKeyPacket(secretKey, publicKey, challenge);
/* 123 */     } catch (Exception e) {
/* 124 */       throw new IllegalStateException("Protocol error", e);
/*     */     } 
/*     */     
/* 127 */     if (packet.shouldAuthenticate()) {
/* 128 */       Util.ioPool().execute(() -> {
/*     */             Component error = authenticateServer(digest);
/*     */             if (error != null) {
/*     */               if (this.serverData != null && this.serverData.isLan()) {
/*     */                 LOGGER.warn(error.getString());
/*     */               } else {
/*     */                 this.connection.disconnect(error);
/*     */                 return;
/*     */               } 
/*     */             }
/*     */             setEncryption(setKeyPacket, decryptCipher, encryptCipher);
/*     */           });
/*     */     } else {
/* 141 */       setEncryption(setKeyPacket, decryptCipher, encryptCipher);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void setEncryption(ServerboundKeyPacket setKeyPacket, Cipher decryptCipher, Cipher encryptCipher) {
/* 146 */     switchState(State.ENCRYPTING);
/* 147 */     this.connection.send((Packet)setKeyPacket, PacketSendListener.thenRun(() -> this.connection.setEncryptionKey(decryptCipher, encryptCipher)));
/*     */   }
/*     */   
/*     */   private Component authenticateServer(String digest) {
/*     */     try {
/* 152 */       this.minecraft.services().sessionService().joinServer(this.minecraft.getUser().getProfileId(), this.minecraft.getUser().getAccessToken(), digest);
/* 153 */     } catch (AuthenticationUnavailableException ignored) {
/* 154 */       return (Component)Component.translatable("disconnect.loginFailedInfo", new Object[] { Component.translatable("disconnect.loginFailedInfo.serversUnavailable") });
/* 155 */     } catch (InvalidCredentialsException ignored) {
/* 156 */       return (Component)Component.translatable("disconnect.loginFailedInfo", new Object[] { Component.translatable("disconnect.loginFailedInfo.invalidSession") });
/* 157 */     } catch (InsufficientPrivilegesException ignored) {
/* 158 */       return (Component)Component.translatable("disconnect.loginFailedInfo", new Object[] { Component.translatable("disconnect.loginFailedInfo.insufficientPrivileges") });
/* 159 */     } catch (UserBannedException|com.mojang.authlib.exceptions.ForcedUsernameChangeException ignored) {
/* 160 */       return (Component)Component.translatable("disconnect.loginFailedInfo", new Object[] { Component.translatable("disconnect.loginFailedInfo.userBanned") });
/* 161 */     } catch (AuthenticationException e) {
/* 162 */       return (Component)Component.translatable("disconnect.loginFailedInfo", new Object[] { e.getMessage() });
/*     */     } 
/*     */     
/* 165 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleLoginFinished(ClientboundLoginFinishedPacket packet) {
/* 170 */     switchState(State.JOINING);
/*     */     
/* 172 */     GameProfile localGameProfile = packet.gameProfile();
/*     */     
/* 174 */     this.connection.setupInboundProtocol(ConfigurationProtocols.CLIENTBOUND, (PacketListener)new ClientConfigurationPacketListenerImpl(this.minecraft, this.connection, new CommonListenerCookie(this.levelLoadTracker, localGameProfile, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 180 */             this.minecraft.getTelemetryManager().createWorldSessionManager(this.newWorld, this.worldLoadDuration, this.minigameName), 
/* 181 */             ClientRegistryLayer.createRegistryAccess().compositeAccess(), FeatureFlags.DEFAULT_FLAGS, null, this.serverData, this.parent, this.cookies, null, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 188 */             Map.of(), ServerLinks.EMPTY, this.seenPlayers, false)));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 195 */     this.connection.send((Packet)ServerboundLoginAcknowledgedPacket.INSTANCE);
/* 196 */     this.connection.setupOutboundProtocol(ConfigurationProtocols.SERVERBOUND);
/* 197 */     this.connection.send((Packet)new ServerboundCustomPayloadPacket((CustomPacketPayload)new BrandPayload(ClientBrandRetriever.getClientModName())));
/* 198 */     this.connection.send((Packet)new ServerboundClientInformationPacket(this.minecraft.options.buildPlayerInformation()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisconnect(DisconnectionDetails details) {
/* 203 */     Component title = this.wasTransferredTo ? CommonComponents.TRANSFER_CONNECT_FAILED : CommonComponents.CONNECT_FAILED;
/* 204 */     if (this.serverData != null && this.serverData.isRealm()) {
/* 205 */       this.minecraft.setScreen((Screen)new DisconnectedScreen(this.parent, title, details.reason(), CommonComponents.GUI_BACK));
/*     */     } else {
/* 207 */       this.minecraft.setScreen((Screen)new DisconnectedScreen(this.parent, title, details));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAcceptingMessages() {
/* 213 */     return this.connection.isConnected();
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleDisconnect(ClientboundLoginDisconnectPacket packet) {
/* 218 */     this.connection.disconnect(packet.reason());
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleCompression(ClientboundLoginCompressionPacket packet) {
/* 223 */     if (!this.connection.isMemoryConnection())
/*     */     {
/* 225 */       this.connection.setupCompression(packet.getCompressionThreshold(), false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleCustomQuery(ClientboundCustomQueryPacket packet) {
/* 231 */     this.updateStatus.accept(Component.translatable("connect.negotiating"));
/* 232 */     this.connection.send((Packet)new ServerboundCustomQueryAnswerPacket(packet.transactionId(), null));
/*     */   }
/*     */   
/*     */   public void setMinigameName(String minigameName) {
/* 236 */     this.minigameName = minigameName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleRequestCookie(ClientboundCookieRequestPacket packet) {
/* 241 */     this.connection.send((Packet)new ServerboundCookieResponsePacket(packet.key(), this.cookies.get(packet.key())));
/*     */   }
/*     */   
/*     */   private enum State {
/* 245 */     CONNECTING(Component.translatable("connect.connecting"), Set.of()),
/* 246 */     AUTHORIZING(Component.translatable("connect.authorizing"), Set.of(CONNECTING)),
/* 247 */     ENCRYPTING(Component.translatable("connect.encrypting"), Set.of(AUTHORIZING)),
/*     */     
/* 249 */     JOINING(Component.translatable("connect.joining"), Set.of(ENCRYPTING, CONNECTING));
/*     */     
/*     */     private final Component message;
/*     */     
/*     */     private final Set<State> fromStates;
/*     */     
/*     */     State(Component message, Set<State> fromStates) {
/* 256 */       this.message = message;
/* 257 */       this.fromStates = fromStates;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void fillListenerSpecificCrashDetails(CrashReport report, CrashReportCategory connectionDetails) {
/* 263 */     connectionDetails.setDetail("Server type", () -> (this.serverData != null) ? this.serverData.type().toString() : "<unknown>");
/* 264 */     connectionDetails.setDetail("Login phase", () -> ((State)this.state.get()).toString());
/* 265 */     connectionDetails.setDetail("Is Local", () -> String.valueOf(this.connection.isMemoryConnection()));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/ClientHandshakePacketListenerImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */