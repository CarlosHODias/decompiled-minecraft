/*     */ package net.minecraft.client.gui.screens.social;
/*     */ 
/*     */ import com.google.common.base.Strings;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
/*     */ import java.util.Collection;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ import net.minecraft.client.multiplayer.ClientPacketListener;
/*     */ import net.minecraft.client.multiplayer.PlayerInfo;
/*     */ import net.minecraft.client.multiplayer.chat.ChatLog;
/*     */ import net.minecraft.client.multiplayer.chat.LoggedChatEvent;
/*     */ import net.minecraft.client.multiplayer.chat.LoggedChatMessage;
/*     */ 
/*     */ public class SocialInteractionsPlayerList extends ContainerObjectSelectionList<PlayerEntry> {
/*     */   private final SocialInteractionsScreen socialInteractionsScreen;
/*  28 */   private final List<PlayerEntry> players = Lists.newArrayList();
/*     */   
/*     */   private String filter;
/*     */   
/*     */   public SocialInteractionsPlayerList(SocialInteractionsScreen socialInteractionsScreen, Minecraft minecraft, int width, int height, int y, int itemHeight) {
/*  33 */     super(minecraft, width, height, y, itemHeight);
/*  34 */     this.socialInteractionsScreen = socialInteractionsScreen;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renderListBackground(GuiGraphics graphics) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void renderListSeparators(GuiGraphics graphics) {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void enableScissor(GuiGraphics graphics) {
/*  49 */     graphics.enableScissor(getX(), getY() + 4, getRight(), getBottom());
/*     */   }
/*     */   
/*     */   public void updatePlayerList(Collection<UUID> playersToAdd, double scrollAmount, boolean addOfflineEntries) {
/*  53 */     Map<UUID, PlayerEntry> newEntries = new HashMap<>();
/*  54 */     addOnlinePlayers(playersToAdd, newEntries);
/*  55 */     if (addOfflineEntries) {
/*  56 */       addSeenPlayers(newEntries);
/*     */     }
/*  58 */     updatePlayersFromChatLog(newEntries, addOfflineEntries);
/*  59 */     updateFiltersAndScroll(newEntries.values(), scrollAmount);
/*     */   }
/*     */   
/*     */   private void addOnlinePlayers(Collection<UUID> playersToAdd, Map<UUID, PlayerEntry> output) {
/*  63 */     ClientPacketListener connection = this.minecraft.player.connection;
/*  64 */     for (UUID id : playersToAdd) {
/*  65 */       PlayerInfo playerInfo = connection.getPlayerInfo(id);
/*  66 */       if (playerInfo != null) {
/*  67 */         PlayerEntry player = makePlayerEntry(id, playerInfo);
/*  68 */         output.put(id, player);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void addSeenPlayers(Map<UUID, PlayerEntry> newEntries) {
/*  74 */     Map<UUID, PlayerInfo> seenPlayers = this.minecraft.player.connection.getSeenPlayers();
/*  75 */     for (Iterator<Map.Entry<UUID, PlayerInfo>> iterator = seenPlayers.entrySet().iterator(); iterator.hasNext(); ) { Map.Entry<UUID, PlayerInfo> entry = iterator.next();
/*  76 */       newEntries.computeIfAbsent(entry.getKey(), uuid -> {
/*     */             PlayerEntry player = makePlayerEntry(entry, (PlayerInfo)entry.getValue());
/*     */             player.setRemoved(true);
/*     */             return player;
/*     */           }); }
/*     */   
/*     */   }
/*     */   
/*     */   private PlayerEntry makePlayerEntry(UUID id, PlayerInfo playerInfo) {
/*  85 */     Objects.requireNonNull(playerInfo); return new PlayerEntry(this.minecraft, this.socialInteractionsScreen, id, playerInfo.getProfile().name(), playerInfo::getSkin, playerInfo.hasVerifiableChat());
/*     */   }
/*     */   
/*     */   private void updatePlayersFromChatLog(Map<UUID, PlayerEntry> entries, boolean addOfflineEntries) {
/*  89 */     Map<UUID, GameProfile> gameProfiles = collectProfilesFromChatLog(this.minecraft.getReportingContext().chatLog());
/*  90 */     gameProfiles.forEach((id, gameProfile) -> {
/*     */           PlayerEntry entry;
/*     */           if (addOfflineEntries) {
/*     */             entry = addOfflineEntries.computeIfAbsent(addOfflineEntries, ());
/*     */           } else {
/*     */             entry = (PlayerEntry)addOfflineEntries.get(addOfflineEntries);
/*     */             if (entry == null) {
/*     */               return;
/*     */             }
/*     */           } 
/*     */           entry.setHasRecentMessages(true);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Map<UUID, GameProfile> collectProfilesFromChatLog(ChatLog chatLog) {
/* 110 */     Object2ObjectLinkedOpenHashMap<UUID, GameProfile> object2ObjectLinkedOpenHashMap = new Object2ObjectLinkedOpenHashMap();
/*     */     
/* 112 */     for (int id = chatLog.end(); id >= chatLog.start(); id--) {
/* 113 */       LoggedChatEvent event = chatLog.lookup(id);
/* 114 */       if (event instanceof LoggedChatMessage.Player) { LoggedChatMessage.Player message = (LoggedChatMessage.Player)event; if (message.message().hasSignature()) {
/* 115 */           object2ObjectLinkedOpenHashMap.put(message.profileId(), message.profile());
/*     */         } }
/*     */     
/*     */     } 
/* 119 */     return (Map<UUID, GameProfile>)object2ObjectLinkedOpenHashMap;
/*     */   }
/*     */   
/*     */   private void sortPlayerEntries() {
/* 123 */     this.players.sort(
/* 124 */         Comparator.comparing(e -> this.minecraft.isLocalPlayer(e.getPlayerId()) ? 0 : (this.minecraft.getReportingContext().hasDraftReportFor(e.getPlayerId()) ? 1 : ((e.getPlayerId().version() == 2) ? 4 : (e.hasRecentMessages() ? 2 : 3))))
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
/*     */ 
/*     */ 
/*     */         
/* 150 */         .thenComparing(e -> {
/*     */             if (!e.getPlayerName().isBlank()) {
/*     */               int firstCodepoint = e.getPlayerName().codePointAt(0);
/*     */ 
/*     */ 
/*     */               
/*     */               if (firstCodepoint == 95 || (firstCodepoint >= 97 && firstCodepoint <= 122) || (firstCodepoint >= 65 && firstCodepoint <= 90) || (firstCodepoint >= 48 && firstCodepoint <= 57)) {
/*     */                 return 0;
/*     */               }
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/*     */             return 1;
/* 164 */           }).thenComparing(PlayerEntry::getPlayerName, String::compareToIgnoreCase));
/*     */   }
/*     */   
/*     */   private void updateFiltersAndScroll(Collection<PlayerEntry> newEntries, double scrollAmount) {
/* 168 */     this.players.clear();
/* 169 */     this.players.addAll(newEntries);
/* 170 */     sortPlayerEntries();
/* 171 */     updateFilteredPlayers();
/* 172 */     replaceEntries(this.players);
/* 173 */     setScrollAmount(scrollAmount);
/*     */   }
/*     */   
/*     */   private void updateFilteredPlayers() {
/* 177 */     if (this.filter != null) {
/* 178 */       this.players.removeIf(p -> !p.getPlayerName().toLowerCase(Locale.ROOT).contains(this.filter));
/* 179 */       replaceEntries(this.players);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setFilter(String filter) {
/* 184 */     this.filter = filter;
/*     */   }
/*     */   
/*     */   public boolean isEmpty() {
/* 188 */     return this.players.isEmpty();
/*     */   }
/*     */   
/*     */   public void addPlayer(PlayerInfo player, SocialInteractionsScreen.Page page) {
/* 192 */     UUID playerId = player.getProfile().id();
/* 193 */     for (PlayerEntry playerEntry : this.players) {
/* 194 */       if (playerEntry.getPlayerId().equals(playerId)) {
/* 195 */         playerEntry.setRemoved(false);
/*     */         return;
/*     */       } 
/*     */     } 
/* 199 */     if ((page == SocialInteractionsScreen.Page.ALL || this.minecraft.getPlayerSocialManager().shouldHideMessageFrom(playerId)) && (
/* 200 */       Strings.isNullOrEmpty(this.filter) || player.getProfile().name().toLowerCase(Locale.ROOT).contains(this.filter))) {
/* 201 */       boolean chatReportable = player.hasVerifiableChat();
/* 202 */       Objects.requireNonNull(player); PlayerEntry playerEntry = new PlayerEntry(this.minecraft, this.socialInteractionsScreen, player.getProfile().id(), player.getProfile().name(), player::getSkin, chatReportable);
/* 203 */       addEntry((AbstractSelectionList.Entry)playerEntry);
/* 204 */       this.players.add(playerEntry);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removePlayer(UUID id) {
/* 210 */     for (PlayerEntry playerEntry : this.players) {
/* 211 */       if (playerEntry.getPlayerId().equals(id)) {
/* 212 */         playerEntry.setRemoved(true);
/*     */         return;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void refreshHasDraftReport() {
/* 219 */     this.players.forEach(playerEntry -> playerEntry.refreshHasDraftReport(this.minecraft.getReportingContext()));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/social/SocialInteractionsPlayerList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */