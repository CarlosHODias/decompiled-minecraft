/*    */ package net.minecraft.client.gui.screens.social;
/*    */ import com.google.common.collect.Maps;
/*    */ import com.google.common.collect.Sets;
/*    */ import com.mojang.authlib.GameProfile;
/*    */ import com.mojang.authlib.minecraft.UserApiService;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import java.util.UUID;
/*    */ import java.util.concurrent.CompletableFuture;
/*    */ import java.util.concurrent.Executor;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.client.multiplayer.PlayerInfo;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class PlayerSocialManager {
/* 18 */   private final Set<UUID> hiddenPlayers = Sets.newHashSet(); private final Minecraft minecraft;
/*    */   private final UserApiService service;
/* 20 */   private final Map<String, UUID> discoveredNamesToUUID = Maps.newHashMap();
/*    */   private boolean onlineMode;
/* 22 */   private CompletableFuture<?> pendingBlockListRefresh = CompletableFuture.completedFuture(null);
/*    */   
/*    */   public PlayerSocialManager(Minecraft minecraft, UserApiService service) {
/* 25 */     this.minecraft = minecraft;
/* 26 */     this.service = service;
/*    */   }
/*    */   
/*    */   public void hidePlayer(UUID id) {
/* 30 */     this.hiddenPlayers.add(id);
/*    */   }
/*    */   
/*    */   public void showPlayer(UUID id) {
/* 34 */     this.hiddenPlayers.remove(id);
/*    */   }
/*    */   
/*    */   public boolean shouldHideMessageFrom(UUID id) {
/* 38 */     return (isHidden(id) || isBlocked(id));
/*    */   }
/*    */   
/*    */   public boolean isHidden(UUID id) {
/* 42 */     return this.hiddenPlayers.contains(id);
/*    */   }
/*    */   
/*    */   public void startOnlineMode() {
/* 46 */     this.onlineMode = true;
/* 47 */     Objects.requireNonNull(this.service); this.pendingBlockListRefresh = this.pendingBlockListRefresh.thenRunAsync(this.service::refreshBlockList, (Executor)Util.ioPool());
/*    */   }
/*    */   
/*    */   public void stopOnlineMode() {
/* 51 */     this.onlineMode = false;
/*    */   }
/*    */   
/*    */   public boolean isBlocked(UUID id) {
/* 55 */     if (!this.onlineMode) {
/* 56 */       return false;
/*    */     }
/* 58 */     this.pendingBlockListRefresh.join();
/* 59 */     return this.service.isBlockedPlayer(id);
/*    */   }
/*    */   
/*    */   public Set<UUID> getHiddenPlayers() {
/* 63 */     return this.hiddenPlayers;
/*    */   }
/*    */   
/*    */   public UUID getDiscoveredUUID(String name) {
/* 67 */     return this.discoveredNamesToUUID.getOrDefault(name, Util.NIL_UUID);
/*    */   }
/*    */   
/*    */   public void addPlayer(PlayerInfo info) {
/* 71 */     GameProfile gameProfile = info.getProfile();
/* 72 */     this.discoveredNamesToUUID.put(gameProfile.name(), gameProfile.id());
/*    */     
/* 74 */     Screen screen = this.minecraft.screen; if (screen instanceof SocialInteractionsScreen) { SocialInteractionsScreen socialInteractionsScreen = (SocialInteractionsScreen)screen;
/* 75 */       socialInteractionsScreen.onAddPlayer(info); }
/*    */   
/*    */   }
/*    */   
/*    */   public void removePlayer(UUID id) {
/* 80 */     Screen screen = this.minecraft.screen; if (screen instanceof SocialInteractionsScreen) { SocialInteractionsScreen socialInteractionsScreen = (SocialInteractionsScreen)screen;
/* 81 */       socialInteractionsScreen.onRemovePlayer(id); }
/*    */   
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/social/PlayerSocialManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */