/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.RemoteChatSession;
/*     */ import net.minecraft.network.chat.SignedMessageValidator;
/*     */ import net.minecraft.world.entity.player.PlayerSkin;
/*     */ import net.minecraft.world.entity.player.ProfilePublicKey;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class PlayerInfo
/*     */ {
/*     */   private final GameProfile profile;
/*     */   private Supplier<PlayerSkin> skinLookup;
/*  22 */   private GameType gameMode = GameType.DEFAULT_MODE;
/*     */   
/*     */   private int latency;
/*     */   private Component tabListDisplayName;
/*     */   private boolean showHat = true;
/*     */   private RemoteChatSession chatSession;
/*     */   private SignedMessageValidator messageValidator;
/*     */   private int tabListOrder;
/*     */   
/*     */   public PlayerInfo(GameProfile profile, boolean enforcesSecureChat) {
/*  32 */     this.profile = profile;
/*  33 */     this.messageValidator = fallbackMessageValidator(enforcesSecureChat);
/*     */   }
/*     */   
/*     */   private static Supplier<PlayerSkin> createSkinLookup(GameProfile profile) {
/*  37 */     Minecraft minecraft = Minecraft.getInstance();
/*     */ 
/*     */     
/*  40 */     boolean requireSecure = !minecraft.isLocalPlayer(profile.id());
/*  41 */     return minecraft.getSkinManager().createLookup(profile, requireSecure);
/*     */   }
/*     */   
/*     */   public GameProfile getProfile() {
/*  45 */     return this.profile;
/*     */   }
/*     */   
/*     */   public RemoteChatSession getChatSession() {
/*  49 */     return this.chatSession;
/*     */   }
/*     */   
/*     */   public SignedMessageValidator getMessageValidator() {
/*  53 */     return this.messageValidator;
/*     */   }
/*     */   
/*     */   public boolean hasVerifiableChat() {
/*  57 */     return (this.chatSession != null);
/*     */   }
/*     */   
/*     */   protected void setChatSession(RemoteChatSession chatSession) {
/*  61 */     this.chatSession = chatSession;
/*  62 */     this.messageValidator = chatSession.createMessageValidator(ProfilePublicKey.EXPIRY_GRACE_PERIOD);
/*     */   }
/*     */   
/*     */   protected void clearChatSession(boolean enforcesSecureChat) {
/*  66 */     this.chatSession = null;
/*  67 */     this.messageValidator = fallbackMessageValidator(enforcesSecureChat);
/*     */   }
/*     */   
/*     */   private static SignedMessageValidator fallbackMessageValidator(boolean enforcesSecureChat) {
/*  71 */     return enforcesSecureChat ? SignedMessageValidator.REJECT_ALL : SignedMessageValidator.ACCEPT_UNSIGNED;
/*     */   }
/*     */   
/*     */   public GameType getGameMode() {
/*  75 */     return this.gameMode;
/*     */   }
/*     */   
/*     */   protected void setGameMode(GameType gameMode) {
/*  79 */     this.gameMode = gameMode;
/*     */   }
/*     */   
/*     */   public int getLatency() {
/*  83 */     return this.latency;
/*     */   }
/*     */   
/*     */   protected void setLatency(int latency) {
/*  87 */     this.latency = latency;
/*     */   }
/*     */   
/*     */   public PlayerSkin getSkin() {
/*  91 */     if (this.skinLookup == null) {
/*  92 */       this.skinLookup = createSkinLookup(this.profile);
/*     */     }
/*  94 */     return this.skinLookup.get();
/*     */   }
/*     */   
/*     */   public PlayerTeam getTeam() {
/*  98 */     return (Minecraft.getInstance()).level.getScoreboard().getPlayersTeam(getProfile().name());
/*     */   }
/*     */   
/*     */   public void setTabListDisplayName(Component tabListDisplayName) {
/* 102 */     this.tabListDisplayName = tabListDisplayName;
/*     */   }
/*     */   
/*     */   public Component getTabListDisplayName() {
/* 106 */     return this.tabListDisplayName;
/*     */   }
/*     */   
/*     */   public void setShowHat(boolean showHat) {
/* 110 */     this.showHat = showHat;
/*     */   }
/*     */   
/*     */   public boolean showHat() {
/* 114 */     return this.showHat;
/*     */   }
/*     */   
/*     */   public void setTabListOrder(int tabListOrder) {
/* 118 */     this.tabListOrder = tabListOrder;
/*     */   }
/*     */   
/*     */   public int getTabListOrder() {
/* 122 */     return this.tabListOrder;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/PlayerInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */