/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.mojang.authlib.GameProfile;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.UUID;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.Optionull;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Gui;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.multiplayer.PlayerInfo;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.client.renderer.entity.player.AvatarRenderer;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.network.chat.numbers.NumberFormat;
/*     */ import net.minecraft.network.chat.numbers.StyledFormat;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.FormattedCharSequence;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.entity.player.Player;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.scores.Objective;
/*     */ import net.minecraft.world.scores.PlayerTeam;
/*     */ import net.minecraft.world.scores.ReadOnlyScoreInfo;
/*     */ import net.minecraft.world.scores.ScoreHolder;
/*     */ import net.minecraft.world.scores.Scoreboard;
/*     */ import net.minecraft.world.scores.Team;
/*     */ import net.minecraft.world.scores.criteria.ObjectiveCriteria;
/*     */ 
/*     */ public class PlayerTabOverlay
/*     */ {
/*  43 */   private static final Identifier PING_UNKNOWN_SPRITE = Identifier.withDefaultNamespace("icon/ping_unknown");
/*  44 */   private static final Identifier PING_1_SPRITE = Identifier.withDefaultNamespace("icon/ping_1");
/*  45 */   private static final Identifier PING_2_SPRITE = Identifier.withDefaultNamespace("icon/ping_2");
/*  46 */   private static final Identifier PING_3_SPRITE = Identifier.withDefaultNamespace("icon/ping_3");
/*  47 */   private static final Identifier PING_4_SPRITE = Identifier.withDefaultNamespace("icon/ping_4");
/*  48 */   private static final Identifier PING_5_SPRITE = Identifier.withDefaultNamespace("icon/ping_5");
/*  49 */   private static final Identifier HEART_CONTAINER_BLINKING_SPRITE = Identifier.withDefaultNamespace("hud/heart/container_blinking");
/*  50 */   private static final Identifier HEART_CONTAINER_SPRITE = Identifier.withDefaultNamespace("hud/heart/container");
/*  51 */   private static final Identifier HEART_FULL_BLINKING_SPRITE = Identifier.withDefaultNamespace("hud/heart/full_blinking");
/*  52 */   private static final Identifier HEART_HALF_BLINKING_SPRITE = Identifier.withDefaultNamespace("hud/heart/half_blinking");
/*  53 */   private static final Identifier HEART_ABSORBING_FULL_BLINKING_SPRITE = Identifier.withDefaultNamespace("hud/heart/absorbing_full_blinking");
/*  54 */   private static final Identifier HEART_FULL_SPRITE = Identifier.withDefaultNamespace("hud/heart/full");
/*  55 */   private static final Identifier HEART_ABSORBING_HALF_BLINKING_SPRITE = Identifier.withDefaultNamespace("hud/heart/absorbing_half_blinking");
/*  56 */   private static final Identifier HEART_HALF_SPRITE = Identifier.withDefaultNamespace("hud/heart/half"); private static final Comparator<PlayerInfo> PLAYER_COMPARATOR; public static final int MAX_ROWS_PER_COL = 20;
/*     */   private final Minecraft minecraft;
/*     */   private final Gui gui;
/*     */   private Component footer;
/*     */   private Component header;
/*     */   private boolean visible;
/*     */   
/*     */   static {
/*  64 */     PLAYER_COMPARATOR = Comparator.comparingInt(p -> -p.getTabListOrder()).thenComparingInt(p -> (p.getGameMode() == GameType.SPECTATOR) ? 1 : 0).thenComparing(p -> (String)Optionull.mapOrDefault(p.getTeam(), PlayerTeam::getName, "")).thenComparing(p -> p.getProfile().name(), String::compareToIgnoreCase);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  74 */   private final Map<UUID, HealthState> healthStates = (Map<UUID, HealthState>)new Object2ObjectOpenHashMap();
/*     */   
/*     */   public PlayerTabOverlay(Minecraft minecraft, Gui gui) {
/*  77 */     this.minecraft = minecraft;
/*  78 */     this.gui = gui;
/*     */   }
/*     */   
/*     */   public Component getNameForDisplay(PlayerInfo info) {
/*  82 */     if (info.getTabListDisplayName() != null) {
/*  83 */       return decorateName(info, info.getTabListDisplayName().copy());
/*     */     }
/*  85 */     return decorateName(info, PlayerTeam.formatNameForTeam((Team)info.getTeam(), (Component)Component.literal(info.getProfile().name())));
/*     */   }
/*     */ 
/*     */   
/*     */   private Component decorateName(PlayerInfo info, MutableComponent name) {
/*  90 */     return (info.getGameMode() == GameType.SPECTATOR) ? (Component)name.withStyle(ChatFormatting.ITALIC) : (Component)name;
/*     */   }
/*     */   
/*     */   public void setVisible(boolean visible) {
/*  94 */     if (this.visible != visible) {
/*  95 */       this.healthStates.clear();
/*  96 */       this.visible = visible;
/*  97 */       if (visible) {
/*  98 */         MutableComponent mutableComponent = ComponentUtils.formatList(getPlayerInfos(), (Component)Component.literal(", "), this::getNameForDisplay);
/*  99 */         this.minecraft.getNarrator().saySystemNow((Component)Component.translatable("multiplayer.player.list.narration", new Object[] { mutableComponent }));
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private List<PlayerInfo> getPlayerInfos() {
/* 105 */     return this.minecraft.player.connection.getListedOnlinePlayers().stream()
/* 106 */       .sorted(PLAYER_COMPARATOR)
/* 107 */       .limit(80L)
/* 108 */       .toList();
/*     */   }
/*     */   private static final class ScoreDisplayEntry extends Record { private final Component name; private final int score; private final Component formattedScore; private final int scoreWidth;
/* 111 */     private ScoreDisplayEntry(Component name, int score, Component formattedScore, int scoreWidth) { this.name = name; this.score = score; this.formattedScore = formattedScore; this.scoreWidth = scoreWidth; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #111	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 111 */       //   0	7	0	this	Lnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry; } public Component name() { return this.name; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #111	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 111 */       //   0	7	0	this	Lnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry; } public int score() { return this.score; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #111	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/components/PlayerTabOverlay$ScoreDisplayEntry;
/* 111 */       //   0	8	1	o	Ljava/lang/Object; } public Component formattedScore() { return this.formattedScore; } public int scoreWidth() { return this.scoreWidth; }
/*     */      } public void render(GuiGraphics graphics, int screenWidth, Scoreboard scoreboard, Objective displayObjective) {
/*     */     int widthForScore;
/* 114 */     List<PlayerInfo> playerInfos = getPlayerInfos();
/* 115 */     List<ScoreDisplayEntry> entriesToDisplay = new ArrayList<>(playerInfos.size());
/*     */     
/* 117 */     int spacerWidth = this.minecraft.font.width(" ");
/*     */     
/* 119 */     int maxNameWidth = 0;
/* 120 */     int maxScoreWidth = 0;
/*     */     
/* 122 */     for (PlayerInfo info : playerInfos) {
/* 123 */       MutableComponent mutableComponent; Component playerName = getNameForDisplay(info);
/* 124 */       maxNameWidth = Math.max(maxNameWidth, this.minecraft.font.width((FormattedText)playerName));
/*     */       
/* 126 */       int playerScore = 0;
/* 127 */       Component formattedPlayerScore = null;
/* 128 */       int playerScoreWidth = 0;
/*     */       
/* 130 */       if (displayObjective != null) {
/* 131 */         ScoreHolder scoreHolder = ScoreHolder.fromGameProfile(info.getProfile());
/* 132 */         ReadOnlyScoreInfo scoreInfo = scoreboard.getPlayerScoreInfo(scoreHolder, displayObjective);
/*     */         
/* 134 */         if (scoreInfo != null) {
/* 135 */           playerScore = scoreInfo.value();
/*     */         }
/*     */         
/* 138 */         if (displayObjective.getRenderType() != ObjectiveCriteria.RenderType.HEARTS) {
/* 139 */           NumberFormat objectiveDefaultFormat = displayObjective.numberFormatOrDefault((NumberFormat)StyledFormat.PLAYER_LIST_DEFAULT);
/* 140 */           mutableComponent = ReadOnlyScoreInfo.safeFormatValue(scoreInfo, objectiveDefaultFormat);
/* 141 */           playerScoreWidth = this.minecraft.font.width((FormattedText)mutableComponent);
/* 142 */           maxScoreWidth = Math.max(maxScoreWidth, (playerScoreWidth > 0) ? (spacerWidth + playerScoreWidth) : 0);
/*     */         } 
/*     */       } 
/*     */       
/* 146 */       entriesToDisplay.add(new ScoreDisplayEntry(playerName, playerScore, (Component)mutableComponent, playerScoreWidth));
/*     */     } 
/*     */     
/* 149 */     if (!this.healthStates.isEmpty()) {
/* 150 */       Set<UUID> playerIds = (Set<UUID>)playerInfos.stream().map(player -> player.getProfile().id()).collect(Collectors.toSet());
/* 151 */       this.healthStates.keySet().removeIf(id -> !playerIds.contains(id));
/*     */     } 
/*     */     
/* 154 */     int slots = playerInfos.size();
/* 155 */     int rows = slots;
/* 156 */     int cols = 1;
/* 157 */     while (rows > 20) {
/* 158 */       cols++;
/* 159 */       rows = (slots + cols - 1) / cols;
/*     */     } 
/*     */     
/* 162 */     boolean showHead = (this.minecraft.isLocalServer() || this.minecraft.getConnection().getConnection().isEncrypted());
/*     */ 
/*     */     
/* 165 */     if (displayObjective != null) {
/* 166 */       if (displayObjective.getRenderType() == ObjectiveCriteria.RenderType.HEARTS) {
/* 167 */         widthForScore = 90;
/*     */       } else {
/* 169 */         widthForScore = maxScoreWidth;
/*     */       } 
/*     */     } else {
/* 172 */       widthForScore = 0;
/*     */     } 
/*     */     
/* 175 */     int slotWidth = Math.min(cols * ((showHead ? 9 : 0) + maxNameWidth + widthForScore + 13), screenWidth - 50) / cols;
/*     */     
/* 177 */     int xxo = screenWidth / 2 - (slotWidth * cols + (cols - 1) * 5) / 2;
/* 178 */     int yyo = 10;
/*     */     
/* 180 */     int maxLineWidth = slotWidth * cols + (cols - 1) * 5;
/*     */     
/* 182 */     List<FormattedCharSequence> headerLines = null;
/* 183 */     if (this.header != null) {
/* 184 */       headerLines = this.minecraft.font.split((FormattedText)this.header, screenWidth - 50);
/* 185 */       for (FormattedCharSequence line : headerLines) {
/* 186 */         maxLineWidth = Math.max(maxLineWidth, this.minecraft.font.width(line));
/*     */       }
/*     */     } 
/*     */     
/* 190 */     List<FormattedCharSequence> footerLines = null;
/* 191 */     if (this.footer != null) {
/* 192 */       footerLines = this.minecraft.font.split((FormattedText)this.footer, screenWidth - 50);
/* 193 */       for (FormattedCharSequence line : footerLines) {
/* 194 */         maxLineWidth = Math.max(maxLineWidth, this.minecraft.font.width(line));
/*     */       }
/*     */     } 
/*     */     
/* 198 */     if (headerLines != null) {
/* 199 */       Objects.requireNonNull(this.minecraft.font); graphics.fill(screenWidth / 2 - maxLineWidth / 2 - 1, yyo - 1, screenWidth / 2 + maxLineWidth / 2 + 1, yyo + headerLines.size() * 9, Integer.MIN_VALUE);
/* 200 */       for (FormattedCharSequence line : headerLines) {
/* 201 */         int lineWidth = this.minecraft.font.width(line);
/* 202 */         graphics.drawString(this.minecraft.font, line, screenWidth / 2 - lineWidth / 2, yyo, -1);
/* 203 */         Objects.requireNonNull(this.minecraft.font); yyo += 9;
/*     */       } 
/* 205 */       yyo++;
/*     */     } 
/*     */     
/* 208 */     graphics.fill(screenWidth / 2 - maxLineWidth / 2 - 1, yyo - 1, screenWidth / 2 + maxLineWidth / 2 + 1, yyo + rows * 9, Integer.MIN_VALUE);
/*     */     
/* 210 */     int background = this.minecraft.options.getBackgroundColor(553648127);
/* 211 */     for (int i = 0; i < slots; i++) {
/* 212 */       int col = i / rows;
/* 213 */       int row = i % rows;
/* 214 */       int xo = xxo + col * slotWidth + col * 5;
/* 215 */       int yo = yyo + row * 9;
/*     */       
/* 217 */       graphics.fill(xo, yo, xo + slotWidth, yo + 8, background);
/*     */       
/* 219 */       if (i < playerInfos.size()) {
/* 220 */         PlayerInfo info = playerInfos.get(i);
/* 221 */         ScoreDisplayEntry displayInfo = entriesToDisplay.get(i);
/*     */         
/* 223 */         GameProfile profile = info.getProfile();
/* 224 */         if (showHead) {
/* 225 */           Player playerByUUID = this.minecraft.level.getPlayerByUUID(profile.id());
/* 226 */           boolean flip = (playerByUUID != null && AvatarRenderer.isPlayerUpsideDown(playerByUUID));
/*     */           
/* 228 */           PlayerFaceRenderer.draw(graphics, info.getSkin().body().texturePath(), xo, yo, 8, info.showHat(), flip, -1);
/*     */           
/* 230 */           xo += 9;
/*     */         } 
/*     */         
/* 233 */         graphics.drawString(this.minecraft.font, displayInfo.name, xo, yo, (info.getGameMode() == GameType.SPECTATOR) ? -1862270977 : -1);
/*     */         
/* 235 */         if (displayObjective != null && info.getGameMode() != GameType.SPECTATOR) {
/* 236 */           int left = xo + maxNameWidth + 1;
/* 237 */           int right = left + widthForScore;
/*     */           
/* 239 */           if (right - left > 5) {
/* 240 */             renderTablistScore(displayObjective, yo, displayInfo, left, right, profile.id(), graphics);
/*     */           }
/*     */         } 
/*     */         
/* 244 */         renderPingIcon(graphics, slotWidth, xo - (showHead ? 9 : 0), yo, info);
/*     */       } 
/*     */     } 
/*     */     
/* 248 */     if (footerLines != null) {
/* 249 */       yyo += rows * 9 + 1;
/* 250 */       Objects.requireNonNull(this.minecraft.font); graphics.fill(screenWidth / 2 - maxLineWidth / 2 - 1, yyo - 1, screenWidth / 2 + maxLineWidth / 2 + 1, yyo + footerLines.size() * 9, Integer.MIN_VALUE);
/* 251 */       for (FormattedCharSequence line : footerLines) {
/* 252 */         int lineWidth = this.minecraft.font.width(line);
/* 253 */         graphics.drawString(this.minecraft.font, line, screenWidth / 2 - lineWidth / 2, yyo, -1);
/* 254 */         Objects.requireNonNull(this.minecraft.font); yyo += 9;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   protected void renderPingIcon(GuiGraphics graphics, int slotWidth, int xo, int yo, PlayerInfo info) {
/*     */     Identifier sprite;
/* 261 */     if (info.getLatency() < 0) {
/* 262 */       sprite = PING_UNKNOWN_SPRITE;
/* 263 */     } else if (info.getLatency() < 150) {
/* 264 */       sprite = PING_5_SPRITE;
/* 265 */     } else if (info.getLatency() < 300) {
/* 266 */       sprite = PING_4_SPRITE;
/* 267 */     } else if (info.getLatency() < 600) {
/* 268 */       sprite = PING_3_SPRITE;
/* 269 */     } else if (info.getLatency() < 1000) {
/* 270 */       sprite = PING_2_SPRITE;
/*     */     } else {
/* 272 */       sprite = PING_1_SPRITE;
/*     */     } 
/*     */     
/* 275 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, xo + slotWidth - 11, yo, 10, 8);
/*     */   }
/*     */   
/*     */   private void renderTablistScore(Objective displayObjective, int yo, ScoreDisplayEntry entry, int left, int right, UUID profileId, GuiGraphics graphics) {
/* 279 */     if (displayObjective.getRenderType() == ObjectiveCriteria.RenderType.HEARTS) {
/* 280 */       renderTablistHearts(yo, left, right, profileId, graphics, entry.score);
/* 281 */     } else if (entry.formattedScore != null) {
/* 282 */       graphics.drawString(this.minecraft.font, entry.formattedScore, right - entry.scoreWidth, yo, -1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void renderTablistHearts(int yo, int left, int right, UUID profileId, GuiGraphics graphics, int score) {
/* 287 */     HealthState health = this.healthStates.computeIfAbsent(profileId, id -> new HealthState(score));
/* 288 */     health.update(score, this.gui.getGuiTicks());
/*     */     
/* 290 */     int fullHearts = Mth.positiveCeilDiv(Math.max(score, health.displayedValue()), 2);
/* 291 */     int heartsToRender = Math.max(score, Math.max(health.displayedValue(), 20)) / 2;
/* 292 */     boolean blink = health.isBlinking(this.gui.getGuiTicks());
/*     */     
/* 294 */     if (fullHearts <= 0) {
/*     */       return;
/*     */     }
/*     */     
/* 298 */     int widthPerHeart = Mth.floor(Math.min((right - left - 4) / heartsToRender, 9.0F));
/* 299 */     if (widthPerHeart <= 3) {
/* 300 */       MutableComponent mutableComponent2; float pct = Mth.clamp(score / 20.0F, 0.0F, 1.0F);
/* 301 */       int color = (int)((1.0F - pct) * 255.0F) << 16 | (int)(pct * 255.0F) << 8;
/* 302 */       float hearts = score / 2.0F;
/* 303 */       MutableComponent mutableComponent1 = Component.translatable("multiplayer.player.list.hp", new Object[] { hearts });
/*     */       
/* 305 */       if (right - this.minecraft.font.width((FormattedText)mutableComponent1) >= left) {
/* 306 */         mutableComponent2 = mutableComponent1;
/*     */       } else {
/* 308 */         mutableComponent2 = Component.literal(Float.toString(hearts));
/*     */       } 
/*     */       
/* 311 */       graphics.drawString(this.minecraft.font, (Component)mutableComponent2, (right + left - this.minecraft.font.width((FormattedText)mutableComponent2)) / 2, yo, ARGB.opaque(color));
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 316 */     Identifier sprite = blink ? HEART_CONTAINER_BLINKING_SPRITE : HEART_CONTAINER_SPRITE;
/* 317 */     for (int heart = fullHearts; heart < heartsToRender; heart++) {
/* 318 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, left + heart * widthPerHeart, yo, 9, 9);
/*     */     }
/*     */ 
/*     */     
/* 322 */     for (int i = 0; i < fullHearts; i++) {
/* 323 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, left + i * widthPerHeart, yo, 9, 9);
/*     */       
/* 325 */       if (blink) {
/* 326 */         if (i * 2 + 1 < health.displayedValue()) {
/* 327 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_FULL_BLINKING_SPRITE, left + i * widthPerHeart, yo, 9, 9);
/*     */         }
/* 329 */         if (i * 2 + 1 == health.displayedValue()) {
/* 330 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_HALF_BLINKING_SPRITE, left + i * widthPerHeart, yo, 9, 9);
/*     */         }
/*     */       } 
/*     */       
/* 334 */       if (i * 2 + 1 < score) {
/* 335 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, (i >= 10) ? HEART_ABSORBING_FULL_BLINKING_SPRITE : HEART_FULL_SPRITE, left + i * widthPerHeart, yo, 9, 9);
/*     */       }
/* 337 */       if (i * 2 + 1 == score) {
/* 338 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, (i >= 10) ? HEART_ABSORBING_HALF_BLINKING_SPRITE : HEART_HALF_SPRITE, left + i * widthPerHeart, yo, 9, 9);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setFooter(Component footer) {
/* 344 */     this.footer = footer;
/*     */   }
/*     */   
/*     */   public void setHeader(Component header) {
/* 348 */     this.header = header;
/*     */   }
/*     */   
/*     */   public void reset() {
/* 352 */     this.header = null;
/* 353 */     this.footer = null;
/*     */   }
/*     */   
/*     */   private static class HealthState
/*     */   {
/*     */     private static final long DISPLAY_UPDATE_DELAY = 20L;
/*     */     private static final long DECREASE_BLINK_DURATION = 20L;
/*     */     private static final long INCREASE_BLINK_DURATION = 10L;
/*     */     private int lastValue;
/*     */     private int displayedValue;
/*     */     private long lastUpdateTick;
/*     */     private long blinkUntilTick;
/*     */     
/*     */     public HealthState(int value) {
/* 367 */       this.displayedValue = value;
/* 368 */       this.lastValue = value;
/*     */     }
/*     */     
/*     */     public void update(int value, long tick) {
/* 372 */       if (value != this.lastValue) {
/* 373 */         long blinkDuration = (value < this.lastValue) ? 20L : 10L;
/* 374 */         this.blinkUntilTick = tick + blinkDuration;
/* 375 */         this.lastValue = value;
/* 376 */         this.lastUpdateTick = tick;
/*     */       } 
/*     */       
/* 379 */       if (tick - this.lastUpdateTick > 20L) {
/* 380 */         this.displayedValue = value;
/*     */       }
/*     */     }
/*     */     
/*     */     public int displayedValue() {
/* 385 */       return this.displayedValue;
/*     */     }
/*     */     
/*     */     public boolean isBlinking(long tick) {
/* 389 */       return (this.blinkUntilTick > tick && (this.blinkUntilTick - tick) % 6L >= 3L);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/PlayerTabOverlay.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */