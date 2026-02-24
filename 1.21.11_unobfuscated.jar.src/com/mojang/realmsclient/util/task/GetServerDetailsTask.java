/*     */ package com.mojang.realmsclient.util.task;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.client.RealmsClient;
/*     */ import com.mojang.realmsclient.dto.RealmsJoinInformation;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.exception.RealmsServiceException;
/*     */ import com.mojang.realmsclient.exception.RetryCallException;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsBrokenWorldScreen;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsGenericErrorScreen;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoConnectTaskScreen;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsLongRunningMcoTaskScreen;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsPopups;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsTermsScreen;
/*     */ import java.net.URL;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CancellationException;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.TimeoutException;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.PopupScreen;
/*     */ import net.minecraft.client.gui.screens.GenericMessageScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.resources.server.DownloadedPackSource;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class GetServerDetailsTask extends LongRunningTask {
/*  33 */   private static final Component APPLYING_PACK_TEXT = (Component)Component.translatable("multiplayer.applyingPack");
/*     */   
/*  35 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  37 */   private static final Component TITLE = (Component)Component.translatable("mco.connect.connecting");
/*     */   
/*     */   private final RealmsServer server;
/*     */   private final Screen lastScreen;
/*     */   
/*     */   public GetServerDetailsTask(Screen lastScreen, RealmsServer server) {
/*  43 */     this.lastScreen = lastScreen;
/*  44 */     this.server = server;
/*     */   }
/*     */ 
/*     */   
/*     */   public void run() {
/*     */     RealmsJoinInformation address;
/*     */     try {
/*  51 */       address = fetchServerAddress();
/*  52 */     } catch (CancellationException e) {
/*  53 */       LOGGER.info("User aborted connecting to realms");
/*     */       return;
/*  55 */     } catch (RealmsServiceException e) {
/*  56 */       boolean isOwner; switch (e.realmsError.errorCode()) {
/*     */         case 6002:
/*  58 */           setScreen((Screen)new RealmsTermsScreen(this.lastScreen, this.server));
/*     */           return;
/*     */         case 6006:
/*  61 */           isOwner = Minecraft.getInstance().isLocalPlayer(this.server.ownerUUID);
/*  62 */           setScreen(isOwner ? 
/*  63 */               (Screen)new RealmsBrokenWorldScreen(this.lastScreen, this.server.id, this.server.isMinigameActive()) : 
/*  64 */               (Screen)new RealmsGenericErrorScreen((Component)Component.translatable("mco.brokenworld.nonowner.title"), (Component)Component.translatable("mco.brokenworld.nonowner.error"), this.lastScreen));
/*     */           return;
/*     */       } 
/*  67 */       error(e);
/*  68 */       LOGGER.error("Couldn't connect to world", (Throwable)e);
/*     */       
/*     */       return;
/*  71 */     } catch (TimeoutException e) {
/*  72 */       error((Component)Component.translatable("mco.errorMessage.connectionFailure"));
/*     */       return;
/*  74 */     } catch (Exception e) {
/*  75 */       LOGGER.error("Couldn't connect to world", e);
/*  76 */       error(e);
/*     */       
/*     */       return;
/*     */     } 
/*  80 */     if (address.address() == null) {
/*  81 */       error((Component)Component.translatable("mco.errorMessage.connectionFailure"));
/*     */       
/*     */       return;
/*     */     } 
/*  85 */     boolean requiresResourcePack = (address.resourcePackUrl() != null && address.resourcePackHash() != null);
/*     */     
/*  87 */     Screen nextScreen = requiresResourcePack ? 
/*  88 */       (Screen)resourcePackDownloadConfirmationScreen(address, generatePackId(this.server), this::connectScreen) : 
/*  89 */       (Screen)connectScreen(address);
/*     */     
/*  91 */     setScreen(nextScreen);
/*     */   }
/*     */   
/*     */   private static UUID generatePackId(RealmsServer serverData) {
/*  95 */     if (serverData.minigameName != null) {
/*  96 */       return UUID.nameUUIDFromBytes(("minigame:" + serverData.minigameName).getBytes(StandardCharsets.UTF_8));
/*     */     }
/*  98 */     return UUID.nameUUIDFromBytes(("realms:" + (String)Objects.<String>requireNonNullElse(serverData.name, "") + ":" + serverData.activeSlot).getBytes(StandardCharsets.UTF_8));
/*     */   }
/*     */ 
/*     */   
/*     */   public Component getTitle() {
/* 103 */     return TITLE;
/*     */   }
/*     */   
/*     */   private RealmsJoinInformation fetchServerAddress() throws RealmsServiceException, TimeoutException, CancellationException {
/* 107 */     RealmsClient client = RealmsClient.getOrCreate();
/* 108 */     for (int i = 0; i < 40; i++) {
/* 109 */       if (aborted()) {
/* 110 */         throw new CancellationException();
/*     */       }
/*     */       
/*     */       try {
/* 114 */         return client.join(this.server.id);
/* 115 */       } catch (RetryCallException e) {
/* 116 */         pause(e.delaySeconds);
/*     */       } 
/*     */     } 
/* 119 */     throw new TimeoutException();
/*     */   }
/*     */   
/*     */   public RealmsLongRunningMcoTaskScreen connectScreen(RealmsJoinInformation address) {
/* 123 */     return (RealmsLongRunningMcoTaskScreen)new RealmsLongRunningMcoConnectTaskScreen(this.lastScreen, address, new ConnectTask(this.lastScreen, this.server, address));
/*     */   }
/*     */   
/*     */   private PopupScreen resourcePackDownloadConfirmationScreen(RealmsJoinInformation address, UUID packId, Function<RealmsJoinInformation, Screen> onCompletionScreen) {
/* 127 */     MutableComponent mutableComponent = Component.translatable("mco.configure.world.resourcepack.question");
/* 128 */     return RealmsPopups.infoPopupScreen(this.lastScreen, (Component)mutableComponent, popupScreen -> {
/*     */           setScreen((Screen)new GenericMessageScreen(APPLYING_PACK_TEXT));
/*     */           scheduleResourcePackDownload(address, address).thenRun(()).exceptionally(());
/*     */         });
/*     */   }
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
/*     */   private CompletableFuture<?> scheduleResourcePackDownload(RealmsJoinInformation address, UUID packId) {
/*     */     try {
/* 147 */       if (address.resourcePackUrl() == null) {
/* 148 */         return CompletableFuture.failedFuture(new IllegalStateException("resourcePackUrl was null"));
/*     */       }
/* 150 */       if (address.resourcePackHash() == null) {
/* 151 */         return CompletableFuture.failedFuture(new IllegalStateException("resourcePackHash was null"));
/*     */       }
/* 153 */       DownloadedPackSource packSource = Minecraft.getInstance().getDownloadedPackSource();
/* 154 */       CompletableFuture<Void> result = packSource.waitForPackFeedback(packId);
/* 155 */       packSource.allowServerPacks();
/* 156 */       packSource.pushPack(packId, new URL(address.resourcePackUrl()), address.resourcePackHash());
/* 157 */       return result;
/* 158 */     } catch (Exception e) {
/* 159 */       return CompletableFuture.failedFuture(e);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/util/task/GetServerDetailsTask.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */