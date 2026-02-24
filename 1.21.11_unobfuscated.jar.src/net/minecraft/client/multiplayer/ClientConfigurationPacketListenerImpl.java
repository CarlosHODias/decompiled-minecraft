/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.ChatComponent;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.dialog.DialogConnectionAccess;
/*     */ import net.minecraft.client.gui.screens.multiplayer.CodeOfConductScreen;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.network.Connection;
/*     */ import net.minecraft.network.DisconnectionDetails;
/*     */ import net.minecraft.network.PacketListener;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.TickablePacketListener;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.PacketUtils;
/*     */ import net.minecraft.network.protocol.common.ClientboundUpdateTagsPacket;
/*     */ import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
/*     */ import net.minecraft.network.protocol.configuration.ClientConfigurationPacketListener;
/*     */ import net.minecraft.network.protocol.configuration.ClientboundCodeOfConductPacket;
/*     */ import net.minecraft.network.protocol.configuration.ClientboundFinishConfigurationPacket;
/*     */ import net.minecraft.network.protocol.configuration.ClientboundRegistryDataPacket;
/*     */ import net.minecraft.network.protocol.configuration.ClientboundResetChatPacket;
/*     */ import net.minecraft.network.protocol.configuration.ClientboundSelectKnownPacks;
/*     */ import net.minecraft.network.protocol.configuration.ClientboundUpdateEnabledFeaturesPacket;
/*     */ import net.minecraft.network.protocol.configuration.ServerboundAcceptCodeOfConductPacket;
/*     */ import net.minecraft.network.protocol.configuration.ServerboundFinishConfigurationPacket;
/*     */ import net.minecraft.network.protocol.configuration.ServerboundSelectKnownPacks;
/*     */ import net.minecraft.network.protocol.game.GameProtocols;
/*     */ import net.minecraft.server.packs.repository.KnownPack;
/*     */ import net.minecraft.server.packs.resources.CloseableResourceManager;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ClientConfigurationPacketListenerImpl extends ClientCommonPacketListenerImpl implements ClientConfigurationPacketListener, TickablePacketListener {
/*  42 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  43 */   public static final Component DISCONNECTED_MESSAGE = (Component)Component.translatable("multiplayer.disconnect.code_of_conduct");
/*     */   
/*     */   private final LevelLoadTracker levelLoadTracker;
/*     */   private final GameProfile localGameProfile;
/*     */   private FeatureFlagSet enabledFeatures;
/*     */   private final RegistryAccess.Frozen receivedRegistries;
/*  49 */   private final RegistryDataCollector registryDataCollector = new RegistryDataCollector();
/*     */   private KnownPacksManager knownPacks;
/*     */   protected ChatComponent.State chatState;
/*     */   private boolean seenCodeOfConduct;
/*     */   
/*     */   public ClientConfigurationPacketListenerImpl(Minecraft minecraft, Connection connection, CommonListenerCookie cookie) {
/*  55 */     super(minecraft, connection, cookie);
/*  56 */     this.levelLoadTracker = cookie.levelLoadTracker();
/*  57 */     this.localGameProfile = cookie.localGameProfile();
/*  58 */     this.receivedRegistries = cookie.receivedRegistries();
/*  59 */     this.enabledFeatures = cookie.enabledFeatures();
/*  60 */     this.chatState = cookie.chatState();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isAcceptingMessages() {
/*  65 */     return this.connection.isConnected();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleCustomPayload(CustomPacketPayload payload) {
/*  70 */     handleUnknownCustomPayload(payload);
/*     */   }
/*     */ 
/*     */   
/*     */   private void handleUnknownCustomPayload(CustomPacketPayload payload) {
/*  75 */     LOGGER.warn("Unknown custom packet payload: {}", payload.type().id());
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleRegistryData(ClientboundRegistryDataPacket packet) {
/*  80 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  81 */     this.registryDataCollector.appendContents(packet.registry(), packet.entries());
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleUpdateTags(ClientboundUpdateTagsPacket packet) {
/*  86 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  87 */     this.registryDataCollector.appendTags(packet.getTags());
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleEnabledFeatures(ClientboundUpdateEnabledFeaturesPacket packet) {
/*  92 */     this.enabledFeatures = FeatureFlags.REGISTRY.fromNames(packet.features());
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleSelectKnownPacks(ClientboundSelectKnownPacks packet) {
/*  97 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/*  98 */     if (this.knownPacks == null) {
/*  99 */       this.knownPacks = new KnownPacksManager();
/*     */     }
/*     */     
/* 102 */     List<KnownPack> selected = this.knownPacks.trySelectingPacks(packet.knownPacks());
/* 103 */     send((Packet<?>)new ServerboundSelectKnownPacks(selected));
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleResetChat(ClientboundResetChatPacket packet) {
/* 108 */     this.chatState = null;
/*     */   }
/*     */   
/*     */   private <T> T runWithResources(Function<ResourceProvider, T> operation) {
/* 112 */     if (this.knownPacks == null) {
/* 113 */       return operation.apply(ResourceProvider.EMPTY);
/*     */     }
/*     */     
/* 116 */     CloseableResourceManager manager = this.knownPacks.createResourceManager(); 
/* 117 */     try { T t = operation.apply(manager);
/* 118 */       if (manager != null) manager.close();  return t; }
/*     */     catch (Throwable throwable) { if (manager != null)
/*     */         try { manager.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 123 */      } public void handleCodeOfConduct(ClientboundCodeOfConductPacket packet) { PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 124 */     if (this.seenCodeOfConduct) {
/* 125 */       throw new IllegalStateException("Server sent duplicate Code of Conduct");
/*     */     }
/*     */     
/* 128 */     this.seenCodeOfConduct = true;
/* 129 */     String codeOfConduct = packet.codeOfConduct();
/* 130 */     if (this.serverData != null && this.serverData.hasAcceptedCodeOfConduct(codeOfConduct)) {
/* 131 */       send((Packet<?>)ServerboundAcceptCodeOfConductPacket.INSTANCE);
/*     */     } else {
/* 133 */       Screen lastScreen = this.minecraft.screen;
/* 134 */       this.minecraft.setScreen((Screen)new CodeOfConductScreen(this.serverData, lastScreen, codeOfConduct, accepted -> {
/*     */               if (lastScreen) {
/*     */                 send((Packet<?>)ServerboundAcceptCodeOfConductPacket.INSTANCE);
/*     */                 this.minecraft.setScreen(lastScreen);
/*     */               } else {
/*     */                 createDialogAccess().disconnect(DISCONNECTED_MESSAGE);
/*     */               } 
/*     */             }));
/*     */     }  }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void handleConfigurationFinished(ClientboundFinishConfigurationPacket packet) {
/* 148 */     PacketUtils.ensureRunningOnSameThread((Packet)packet, (PacketListener)this, this.minecraft.packetProcessor());
/* 149 */     RegistryAccess.Frozen registries = runWithResources(knownPacksProvider -> this.registryDataCollector.collectGameRegistries(knownPacksProvider, this.receivedRegistries, this.connection.isMemoryConnection()));
/*     */     
/* 151 */     this.connection.setupInboundProtocol(GameProtocols.CLIENTBOUND_TEMPLATE.bind(RegistryFriendlyByteBuf.decorator((RegistryAccess)registries)), (PacketListener)new ClientPacketListener(this.minecraft, this.connection, new CommonListenerCookie(this.levelLoadTracker, this.localGameProfile, this.telemetryManager, registries, this.enabledFeatures, this.serverBrand, this.serverData, this.postDisconnectScreen, this.serverCookies, this.chatState, this.customReportDetails, 
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
/* 166 */             serverLinks(), this.seenPlayers, this.seenInsecureChatWarning)));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 171 */     this.connection.send((Packet)ServerboundFinishConfigurationPacket.INSTANCE);
/* 172 */     this.connection.setupOutboundProtocol(GameProtocols.SERVERBOUND_TEMPLATE.bind(RegistryFriendlyByteBuf.decorator((RegistryAccess)registries), new GameProtocols.Context(this)
/*     */           {
/*     */             public boolean hasInfiniteMaterials()
/*     */             {
/* 176 */               return true;
/*     */             }
/*     */           }));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 183 */     sendDeferredPackets();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisconnect(DisconnectionDetails reason) {
/* 188 */     super.onDisconnect(reason);
/*     */     
/* 190 */     this.minecraft.clearDownloadedResourcePacks();
/*     */   }
/*     */ 
/*     */   
/*     */   protected DialogConnectionAccess createDialogAccess() {
/* 195 */     return new ClientCommonPacketListenerImpl.CommonDialogAccess(this)
/*     */       {
/*     */         public void runCommand(String command, Screen activeScreen) {
/* 198 */           ClientConfigurationPacketListenerImpl.LOGGER.warn("Commands are not supported in configuration phase, trying to run '{}'", command);
/*     */         }
/*     */       };
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/ClientConfigurationPacketListenerImpl.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */