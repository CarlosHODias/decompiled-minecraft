/*      */ package net.minecraft.client.gui;
/*      */ 
/*      */ import com.google.common.collect.ImmutableMap;
/*      */ import com.google.common.collect.Ordering;
/*      */ import com.mojang.blaze3d.platform.Window;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.function.Supplier;
/*      */ import net.minecraft.ChatFormatting;
/*      */ import net.minecraft.Optionull;
/*      */ import net.minecraft.client.AttackIndicatorStatus;
/*      */ import net.minecraft.client.DeltaTracker;
/*      */ import net.minecraft.client.Minecraft;
/*      */ import net.minecraft.client.Options;
/*      */ import net.minecraft.client.gui.components.BossHealthOverlay;
/*      */ import net.minecraft.client.gui.components.ChatComponent;
/*      */ import net.minecraft.client.gui.components.DebugScreenOverlay;
/*      */ import net.minecraft.client.gui.components.PlayerTabOverlay;
/*      */ import net.minecraft.client.gui.components.SubtitleOverlay;
/*      */ import net.minecraft.client.gui.components.debug.DebugScreenEntries;
/*      */ import net.minecraft.client.gui.components.spectator.SpectatorGui;
/*      */ import net.minecraft.client.gui.contextualbar.ContextualBarRenderer;
/*      */ import net.minecraft.client.gui.contextualbar.ExperienceBarRenderer;
/*      */ import net.minecraft.client.gui.contextualbar.JumpableVehicleBarRenderer;
/*      */ import net.minecraft.client.gui.contextualbar.LocatorBarRenderer;
/*      */ import net.minecraft.client.multiplayer.ClientLevel;
/*      */ import net.minecraft.client.player.LocalPlayer;
/*      */ import net.minecraft.client.renderer.LightTexture;
/*      */ import net.minecraft.client.renderer.RenderPipelines;
/*      */ import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
/*      */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*      */ import net.minecraft.client.server.IntegratedServer;
/*      */ import net.minecraft.core.BlockPos;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.core.component.DataComponents;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.network.chat.FormattedText;
/*      */ import net.minecraft.network.chat.MutableComponent;
/*      */ import net.minecraft.network.chat.numbers.NumberFormat;
/*      */ import net.minecraft.network.chat.numbers.StyledFormat;
/*      */ import net.minecraft.resources.Identifier;
/*      */ import net.minecraft.resources.ResourceKey;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.tags.FluidTags;
/*      */ import net.minecraft.util.ARGB;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.RandomSource;
/*      */ import net.minecraft.util.StringUtil;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.profiling.Profiler;
/*      */ import net.minecraft.world.effect.MobEffect;
/*      */ import net.minecraft.world.effect.MobEffectInstance;
/*      */ import net.minecraft.world.effect.MobEffects;
/*      */ import net.minecraft.world.entity.Entity;
/*      */ import net.minecraft.world.entity.EquipmentSlot;
/*      */ import net.minecraft.world.entity.HumanoidArm;
/*      */ import net.minecraft.world.entity.LivingEntity;
/*      */ import net.minecraft.world.entity.PlayerRideableJumping;
/*      */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*      */ import net.minecraft.world.entity.player.Player;
/*      */ import net.minecraft.world.food.FoodData;
/*      */ import net.minecraft.world.item.ItemStack;
/*      */ import net.minecraft.world.item.component.AttackRange;
/*      */ import net.minecraft.world.item.equipment.Equippable;
/*      */ import net.minecraft.world.level.GameType;
/*      */ import net.minecraft.world.level.Level;
/*      */ import net.minecraft.world.level.block.Blocks;
/*      */ import net.minecraft.world.level.border.WorldBorder;
/*      */ import net.minecraft.world.phys.BlockHitResult;
/*      */ import net.minecraft.world.phys.EntityHitResult;
/*      */ import net.minecraft.world.phys.HitResult;
/*      */ import net.minecraft.world.scores.DisplaySlot;
/*      */ import net.minecraft.world.scores.Objective;
/*      */ import net.minecraft.world.scores.PlayerScoreEntry;
/*      */ import net.minecraft.world.scores.PlayerTeam;
/*      */ import net.minecraft.world.scores.Scoreboard;
/*      */ import net.minecraft.world.scores.Team;
/*      */ import org.apache.commons.lang3.tuple.Pair;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class Gui
/*      */ {
/*   88 */   private static final Identifier CROSSHAIR_SPRITE = Identifier.withDefaultNamespace("hud/crosshair");
/*   89 */   private static final Identifier CROSSHAIR_ATTACK_INDICATOR_FULL_SPRITE = Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_full");
/*   90 */   private static final Identifier CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_background");
/*   91 */   private static final Identifier CROSSHAIR_ATTACK_INDICATOR_PROGRESS_SPRITE = Identifier.withDefaultNamespace("hud/crosshair_attack_indicator_progress");
/*   92 */   private static final Identifier EFFECT_BACKGROUND_AMBIENT_SPRITE = Identifier.withDefaultNamespace("hud/effect_background_ambient");
/*   93 */   private static final Identifier EFFECT_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("hud/effect_background");
/*   94 */   private static final Identifier HOTBAR_SPRITE = Identifier.withDefaultNamespace("hud/hotbar");
/*   95 */   private static final Identifier HOTBAR_SELECTION_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_selection");
/*   96 */   private static final Identifier HOTBAR_OFFHAND_LEFT_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_offhand_left");
/*   97 */   private static final Identifier HOTBAR_OFFHAND_RIGHT_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_offhand_right");
/*   98 */   private static final Identifier HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_attack_indicator_background");
/*   99 */   private static final Identifier HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE = Identifier.withDefaultNamespace("hud/hotbar_attack_indicator_progress");
/*  100 */   private static final Identifier ARMOR_EMPTY_SPRITE = Identifier.withDefaultNamespace("hud/armor_empty");
/*  101 */   private static final Identifier ARMOR_HALF_SPRITE = Identifier.withDefaultNamespace("hud/armor_half");
/*  102 */   private static final Identifier ARMOR_FULL_SPRITE = Identifier.withDefaultNamespace("hud/armor_full");
/*  103 */   private static final Identifier FOOD_EMPTY_HUNGER_SPRITE = Identifier.withDefaultNamespace("hud/food_empty_hunger");
/*  104 */   private static final Identifier FOOD_HALF_HUNGER_SPRITE = Identifier.withDefaultNamespace("hud/food_half_hunger");
/*  105 */   private static final Identifier FOOD_FULL_HUNGER_SPRITE = Identifier.withDefaultNamespace("hud/food_full_hunger");
/*  106 */   private static final Identifier FOOD_EMPTY_SPRITE = Identifier.withDefaultNamespace("hud/food_empty");
/*  107 */   private static final Identifier FOOD_HALF_SPRITE = Identifier.withDefaultNamespace("hud/food_half");
/*  108 */   private static final Identifier FOOD_FULL_SPRITE = Identifier.withDefaultNamespace("hud/food_full");
/*  109 */   private static final Identifier AIR_SPRITE = Identifier.withDefaultNamespace("hud/air");
/*  110 */   private static final Identifier AIR_POPPING_SPRITE = Identifier.withDefaultNamespace("hud/air_bursting");
/*  111 */   private static final Identifier AIR_EMPTY_SPRITE = Identifier.withDefaultNamespace("hud/air_empty");
/*  112 */   private static final Identifier HEART_VEHICLE_CONTAINER_SPRITE = Identifier.withDefaultNamespace("hud/heart/vehicle_container");
/*  113 */   private static final Identifier HEART_VEHICLE_FULL_SPRITE = Identifier.withDefaultNamespace("hud/heart/vehicle_full");
/*  114 */   private static final Identifier HEART_VEHICLE_HALF_SPRITE = Identifier.withDefaultNamespace("hud/heart/vehicle_half");
/*  115 */   private static final Identifier VIGNETTE_LOCATION = Identifier.withDefaultNamespace("textures/misc/vignette.png");
/*  116 */   public static final Identifier NAUSEA_LOCATION = Identifier.withDefaultNamespace("textures/misc/nausea.png");
/*  117 */   private static final Identifier SPYGLASS_SCOPE_LOCATION = Identifier.withDefaultNamespace("textures/misc/spyglass_scope.png");
/*  118 */   private static final Identifier POWDER_SNOW_OUTLINE_LOCATION = Identifier.withDefaultNamespace("textures/misc/powder_snow_outline.png");
/*      */   
/*  120 */   private static final Comparator<PlayerScoreEntry> SCORE_DISPLAY_ORDER = Comparator.comparing(PlayerScoreEntry::value).reversed().thenComparing(PlayerScoreEntry::owner, String.CASE_INSENSITIVE_ORDER);
/*      */   
/*  122 */   private static final Component DEMO_EXPIRED_TEXT = (Component)Component.translatable("demo.demoExpired");
/*  123 */   private static final Component SAVING_TEXT = (Component)Component.translatable("menu.savingLevel");
/*      */   
/*      */   private static final float MIN_CROSSHAIR_ATTACK_SPEED = 5.0F;
/*      */   
/*      */   private static final int EXPERIENCE_BAR_DISPLAY_TICKS = 100;
/*      */   
/*      */   private static final int NUM_HEARTS_PER_ROW = 10;
/*      */   
/*      */   private static final int LINE_HEIGHT = 10;
/*      */   
/*      */   private static final String SPACER = ": ";
/*      */   private static final float PORTAL_OVERLAY_ALPHA_MIN = 0.2F;
/*      */   private static final int HEART_SIZE = 9;
/*      */   private static final int HEART_SEPARATION = 8;
/*      */   private static final int NUM_AIR_BUBBLES = 10;
/*      */   private static final int AIR_BUBBLE_SIZE = 9;
/*      */   private static final int AIR_BUBBLE_SEPERATION = 8;
/*      */   private static final int AIR_BUBBLE_POPPING_DURATION = 2;
/*      */   private static final int EMPTY_AIR_BUBBLE_DELAY_DURATION = 1;
/*      */   private static final float AIR_BUBBLE_POP_SOUND_VOLUME_BASE = 0.5F;
/*      */   private static final float AIR_BUBBLE_POP_SOUND_VOLUME_INCREMENT = 0.1F;
/*      */   private static final float AIR_BUBBLE_POP_SOUND_PITCH_BASE = 1.0F;
/*      */   private static final float AIR_BUBBLE_POP_SOUND_PITCH_INCREMENT = 0.1F;
/*      */   private static final int NUM_AIR_BUBBLE_POPPED_BEFORE_SOUND_VOLUME_INCREASE = 3;
/*      */   private static final int NUM_AIR_BUBBLE_POPPED_BEFORE_SOUND_PITCH_INCREASE = 5;
/*      */   private static final float AUTOSAVE_FADE_SPEED_FACTOR = 0.2F;
/*      */   private static final int SAVING_INDICATOR_WIDTH_PADDING_RIGHT = 5;
/*      */   private static final int SAVING_INDICATOR_HEIGHT_PADDING_BOTTOM = 5;
/*  151 */   private final RandomSource random = RandomSource.create();
/*      */   
/*      */   private final Minecraft minecraft;
/*      */   
/*      */   private final ChatComponent chat;
/*      */   private int tickCount;
/*      */   private Component overlayMessageString;
/*      */   private int overlayMessageTime;
/*      */   private boolean animateOverlayMessageColor;
/*      */   private boolean chatDisabledByPlayerShown;
/*  161 */   public float vignetteBrightness = 1.0F;
/*      */   
/*      */   private int toolHighlightTimer;
/*  164 */   private ItemStack lastToolHighlight = ItemStack.EMPTY;
/*      */   
/*      */   private final DebugScreenOverlay debugOverlay;
/*      */   
/*      */   private final SubtitleOverlay subtitleOverlay;
/*      */   
/*      */   private final SpectatorGui spectatorGui;
/*      */   
/*      */   private final PlayerTabOverlay tabList;
/*      */   private final BossHealthOverlay bossOverlay;
/*      */   private int titleTime;
/*      */   private Component title;
/*      */   private Component subtitle;
/*      */   private int titleFadeInTime;
/*      */   private int titleStayTime;
/*      */   private int titleFadeOutTime;
/*      */   private int lastHealth;
/*      */   private int displayHealth;
/*      */   private long lastHealthTime;
/*      */   private long healthBlinkTime;
/*      */   private int lastBubblePopSoundPlayed;
/*      */   private Runnable deferredSubtitles;
/*      */   private float autosaveIndicatorValue;
/*      */   private float lastAutosaveIndicatorValue;
/*  188 */   private Pair<ContextualInfo, ContextualBarRenderer> contextualInfoBar = Pair.of(ContextualInfo.EMPTY, ContextualBarRenderer.EMPTY); private final Map<ContextualInfo, Supplier<ContextualBarRenderer>> contextualInfoBarRenderers;
/*      */   private float scopeScale;
/*      */   
/*      */   public Gui(Minecraft minecraft) {
/*  192 */     this.minecraft = minecraft;
/*  193 */     this.debugOverlay = new DebugScreenOverlay(minecraft);
/*  194 */     this.spectatorGui = new SpectatorGui(minecraft);
/*  195 */     this.chat = new ChatComponent(minecraft);
/*  196 */     this.tabList = new PlayerTabOverlay(minecraft, this);
/*  197 */     this.bossOverlay = new BossHealthOverlay(minecraft);
/*  198 */     this.subtitleOverlay = new SubtitleOverlay(minecraft);
/*      */     
/*  200 */     this.contextualInfoBarRenderers = (Map<ContextualInfo, Supplier<ContextualBarRenderer>>)ImmutableMap.of(ContextualInfo.EMPTY, () -> ContextualBarRenderer.EMPTY, ContextualInfo.EXPERIENCE, () -> new ExperienceBarRenderer(minecraft), ContextualInfo.LOCATOR, () -> new LocatorBarRenderer(minecraft), ContextualInfo.JUMPABLE_VEHICLE, () -> new JumpableVehicleBarRenderer(minecraft));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  207 */     resetTitleTimes();
/*      */   }
/*      */   
/*      */   public void resetTitleTimes() {
/*  211 */     this.titleFadeInTime = 10;
/*  212 */     this.titleStayTime = 70;
/*  213 */     this.titleFadeOutTime = 20;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void render(GuiGraphics graphics, DeltaTracker deltaTracker) {
/*  219 */     if (this.minecraft.screen instanceof net.minecraft.client.gui.screens.LevelLoadingScreen) {
/*      */       return;
/*      */     }
/*  222 */     if (!this.minecraft.options.hideGui) {
/*  223 */       renderCameraOverlays(graphics, deltaTracker);
/*  224 */       renderCrosshair(graphics, deltaTracker);
/*  225 */       graphics.nextStratum();
/*  226 */       renderHotbarAndDecorations(graphics, deltaTracker);
/*  227 */       renderEffects(graphics, deltaTracker);
/*  228 */       renderBossOverlay(graphics, deltaTracker);
/*      */     } 
/*  230 */     renderSleepOverlay(graphics, deltaTracker);
/*      */     
/*  232 */     if (!this.minecraft.options.hideGui) {
/*  233 */       renderDemoOverlay(graphics, deltaTracker);
/*  234 */       renderScoreboardSidebar(graphics, deltaTracker);
/*  235 */       renderOverlayMessage(graphics, deltaTracker);
/*  236 */       renderTitle(graphics, deltaTracker);
/*  237 */       renderChat(graphics, deltaTracker);
/*  238 */       renderTabList(graphics, deltaTracker);
/*  239 */       renderSubtitleOverlay(graphics, (this.minecraft.screen == null || this.minecraft.screen.isInGameUi()));
/*  240 */     } else if (this.minecraft.screen != null && this.minecraft.screen.isInGameUi()) {
/*  241 */       renderSubtitleOverlay(graphics, true);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void renderBossOverlay(GuiGraphics graphics, DeltaTracker deltaTracker) {
/*  246 */     this.bossOverlay.render(graphics);
/*      */   }
/*      */   
/*      */   public void renderDebugOverlay(GuiGraphics graphics) {
/*  250 */     this.debugOverlay.render(graphics);
/*      */   }
/*      */   
/*      */   private void renderSubtitleOverlay(GuiGraphics graphics, boolean deferRendering) {
/*  254 */     if (deferRendering) {
/*  255 */       this.deferredSubtitles = (() -> this.subtitleOverlay.render(graphics));
/*      */     } else {
/*  257 */       this.deferredSubtitles = null;
/*  258 */       this.subtitleOverlay.render(graphics);
/*      */     } 
/*      */   }
/*      */   
/*      */   public void renderDeferredSubtitles() {
/*  263 */     if (this.deferredSubtitles != null) {
/*  264 */       this.deferredSubtitles.run();
/*  265 */       this.deferredSubtitles = null;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void renderCameraOverlays(GuiGraphics graphics, DeltaTracker deltaTracker) {
/*  270 */     if ((Boolean)this.minecraft.options.vignette().get()) {
/*  271 */       renderVignette(graphics, this.minecraft.getCameraEntity());
/*      */     }
/*      */     
/*  274 */     LocalPlayer player = this.minecraft.player;
/*  275 */     float gameTimeDeltaTicks = deltaTracker.getGameTimeDeltaTicks();
/*      */     
/*  277 */     this.scopeScale = Mth.lerp(0.5F * gameTimeDeltaTicks, this.scopeScale, 1.125F);
/*      */     
/*  279 */     if (this.minecraft.options.getCameraType().isFirstPerson()) {
/*  280 */       if (player.isScoping()) {
/*  281 */         renderSpyglassOverlay(graphics, this.scopeScale);
/*      */       } else {
/*  283 */         this.scopeScale = 0.5F;
/*      */         
/*  285 */         for (EquipmentSlot slot : EquipmentSlot.values()) {
/*  286 */           ItemStack item = player.getItemBySlot(slot);
/*  287 */           Equippable equippable = (Equippable)item.get(DataComponents.EQUIPPABLE);
/*  288 */           if (equippable != null && equippable.slot() == slot && equippable.cameraOverlay().isPresent()) {
/*  289 */             renderTextureOverlay(graphics, ((Identifier)equippable.cameraOverlay().get()).withPath(p -> "textures/" + p + ".png"), 1.0F);
/*      */           }
/*      */         } 
/*      */       } 
/*      */     }
/*      */     
/*  295 */     if (player.getTicksFrozen() > 0) {
/*  296 */       renderTextureOverlay(graphics, POWDER_SNOW_OUTLINE_LOCATION, player.getPercentFrozen());
/*      */     }
/*      */     
/*  299 */     float partialTicks = deltaTracker.getGameTimeDeltaPartialTick(false);
/*  300 */     float portalIntensity = Mth.lerp(partialTicks, player.oPortalEffectIntensity, player.portalEffectIntensity);
/*  301 */     float nauseaIntensity = player.getEffectBlendFactor(MobEffects.NAUSEA, partialTicks);
/*  302 */     if (portalIntensity > 0.0F) {
/*  303 */       renderPortalOverlay(graphics, portalIntensity);
/*  304 */     } else if (nauseaIntensity > 0.0F) {
/*  305 */       float screenEffectScale = ((Double)this.minecraft.options.screenEffectScale().get()).floatValue();
/*  306 */       if (screenEffectScale < 1.0F) {
/*  307 */         float overlayStrength = nauseaIntensity * (1.0F - screenEffectScale);
/*  308 */         renderConfusionOverlay(graphics, overlayStrength);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void renderSleepOverlay(GuiGraphics graphics, DeltaTracker deltaTracker) {
/*  314 */     if (this.minecraft.player.getSleepTimer() <= 0) {
/*      */       return;
/*      */     }
/*      */     
/*  318 */     Profiler.get().push("sleep");
/*      */     
/*  320 */     graphics.nextStratum();
/*  321 */     float sleepTimer = this.minecraft.player.getSleepTimer();
/*  322 */     float amount = sleepTimer / 100.0F;
/*  323 */     if (amount > 1.0F)
/*      */     {
/*  325 */       amount = 1.0F - (sleepTimer - 100.0F) / 10.0F;
/*      */     }
/*      */     
/*  328 */     int color = (int)(220.0F * amount) << 24 | 0x101020;
/*  329 */     graphics.fill(0, 0, graphics.guiWidth(), graphics.guiHeight(), color);
/*  330 */     Profiler.get().pop();
/*      */   }
/*      */   
/*      */   private void renderOverlayMessage(GuiGraphics graphics, DeltaTracker deltaTracker) {
/*  334 */     Font font = getFont();
/*  335 */     if (this.overlayMessageString == null || this.overlayMessageTime <= 0) {
/*      */       return;
/*      */     }
/*  338 */     Profiler.get().push("overlayMessage");
/*  339 */     float t = this.overlayMessageTime - deltaTracker.getGameTimeDeltaPartialTick(false);
/*  340 */     int alpha = (int)(t * 255.0F / 20.0F);
/*  341 */     if (alpha > 255) {
/*  342 */       alpha = 255;
/*      */     }
/*  344 */     if (alpha > 0) {
/*  345 */       int color; graphics.nextStratum();
/*  346 */       graphics.pose().pushMatrix();
/*  347 */       graphics.pose().translate((graphics.guiWidth() / 2), (graphics.guiHeight() - 68));
/*      */ 
/*      */       
/*  350 */       if (this.animateOverlayMessageColor) {
/*  351 */         color = Mth.hsvToArgb(t / 50.0F, 0.7F, 0.6F, alpha);
/*      */       } else {
/*  353 */         color = ARGB.white(alpha);
/*      */       } 
/*  355 */       int width = font.width((FormattedText)this.overlayMessageString);
/*  356 */       graphics.drawStringWithBackdrop(font, this.overlayMessageString, -width / 2, -4, width, color);
/*      */       
/*  358 */       graphics.pose().popMatrix();
/*      */     } 
/*  360 */     Profiler.get().pop();
/*      */   }
/*      */   
/*      */   private void renderTitle(GuiGraphics graphics, DeltaTracker deltaTracker) {
/*  364 */     if (this.title == null || this.titleTime <= 0) {
/*      */       return;
/*      */     }
/*      */     
/*  368 */     Font font = getFont();
/*  369 */     Profiler.get().push("titleAndSubtitle");
/*  370 */     float t = this.titleTime - deltaTracker.getGameTimeDeltaPartialTick(false);
/*  371 */     int alpha = 255;
/*  372 */     if (this.titleTime > this.titleFadeOutTime + this.titleStayTime) {
/*  373 */       float time = (this.titleFadeInTime + this.titleStayTime + this.titleFadeOutTime) - t;
/*  374 */       alpha = (int)(time * 255.0F / this.titleFadeInTime);
/*      */     } 
/*  376 */     if (this.titleTime <= this.titleFadeOutTime) {
/*  377 */       alpha = (int)(t * 255.0F / this.titleFadeOutTime);
/*      */     }
/*  379 */     alpha = Mth.clamp(alpha, 0, 255);
/*  380 */     if (alpha > 0) {
/*  381 */       graphics.nextStratum();
/*  382 */       graphics.pose().pushMatrix();
/*  383 */       graphics.pose().translate((graphics.guiWidth() / 2), (graphics.guiHeight() / 2));
/*      */       
/*  385 */       graphics.pose().pushMatrix();
/*  386 */       graphics.pose().scale(4.0F, 4.0F);
/*      */       
/*  388 */       int titleWidth = font.width((FormattedText)this.title);
/*  389 */       int textColor = ARGB.white(alpha);
/*  390 */       graphics.drawStringWithBackdrop(font, this.title, -titleWidth / 2, -10, titleWidth, textColor);
/*      */       
/*  392 */       graphics.pose().popMatrix();
/*      */       
/*  394 */       if (this.subtitle != null) {
/*  395 */         graphics.pose().pushMatrix();
/*  396 */         graphics.pose().scale(2.0F, 2.0F);
/*      */         
/*  398 */         int subtitleWidth = font.width((FormattedText)this.subtitle);
/*  399 */         graphics.drawStringWithBackdrop(font, this.subtitle, -subtitleWidth / 2, 5, subtitleWidth, textColor);
/*  400 */         graphics.pose().popMatrix();
/*      */       } 
/*      */       
/*  403 */       graphics.pose().popMatrix();
/*      */     } 
/*  405 */     Profiler.get().pop();
/*      */   }
/*      */   
/*      */   private void renderChat(GuiGraphics graphics, DeltaTracker deltaTracker) {
/*  409 */     if (!this.chat.isChatFocused()) {
/*  410 */       Window window = this.minecraft.getWindow();
/*  411 */       int mouseX = Mth.floor(this.minecraft.mouseHandler.getScaledXPos(window));
/*  412 */       int mouseY = Mth.floor(this.minecraft.mouseHandler.getScaledYPos(window));
/*  413 */       graphics.nextStratum();
/*  414 */       this.chat.render(graphics, getFont(), this.tickCount, mouseX, mouseY, false, false);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void renderScoreboardSidebar(GuiGraphics graphics, DeltaTracker deltaTracker) {
/*  419 */     Scoreboard scoreboard = this.minecraft.level.getScoreboard();
/*  420 */     Objective teamObjective = null;
/*  421 */     PlayerTeam playerTeam = scoreboard.getPlayersTeam(this.minecraft.player.getScoreboardName());
/*  422 */     if (playerTeam != null) {
/*  423 */       DisplaySlot displaySlot = DisplaySlot.teamColorToSlot(playerTeam.getColor());
/*  424 */       if (displaySlot != null) {
/*  425 */         teamObjective = scoreboard.getDisplayObjective(displaySlot);
/*      */       }
/*      */     } 
/*  428 */     Objective displayObjective = (teamObjective != null) ? teamObjective : scoreboard.getDisplayObjective(DisplaySlot.SIDEBAR);
/*  429 */     if (displayObjective != null) {
/*  430 */       graphics.nextStratum();
/*  431 */       displayScoreboardSidebar(graphics, displayObjective);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void renderTabList(GuiGraphics graphics, DeltaTracker deltaTracker) {
/*  436 */     Scoreboard scoreboard = this.minecraft.level.getScoreboard();
/*  437 */     Objective displayObjective = scoreboard.getDisplayObjective(DisplaySlot.LIST);
/*  438 */     if (this.minecraft.options.keyPlayerList.isDown() && (!this.minecraft.isLocalServer() || this.minecraft.player.connection.getListedOnlinePlayers().size() > 1 || displayObjective != null)) {
/*  439 */       this.tabList.setVisible(true);
/*  440 */       graphics.nextStratum();
/*  441 */       this.tabList.render(graphics, graphics.guiWidth(), scoreboard, displayObjective);
/*      */     } else {
/*  443 */       this.tabList.setVisible(false);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void renderCrosshair(GuiGraphics graphics, DeltaTracker deltaTracker) {
/*  448 */     Options options = this.minecraft.options;
/*      */     
/*  450 */     if (!options.getCameraType().isFirstPerson()) {
/*      */       return;
/*      */     }
/*      */     
/*  454 */     if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR && 
/*  455 */       !canRenderCrosshairForSpectator(this.minecraft.hitResult)) {
/*      */       return;
/*      */     }
/*      */ 
/*      */     
/*  460 */     if (!this.minecraft.debugEntries.isCurrentlyEnabled(DebugScreenEntries.THREE_DIMENSIONAL_CROSSHAIR)) {
/*  461 */       graphics.nextStratum();
/*  462 */       int size = 15;
/*  463 */       graphics.blitSprite(RenderPipelines.CROSSHAIR, CROSSHAIR_SPRITE, (graphics.guiWidth() - 15) / 2, (graphics.guiHeight() - 15) / 2, 15, 15);
/*      */       
/*  465 */       if (this.minecraft.options.attackIndicator().get() == AttackIndicatorStatus.CROSSHAIR) {
/*  466 */         float attackStrengthScale = this.minecraft.player.getAttackStrengthScale(0.0F);
/*      */         boolean renderMaxAttackIndicator = false;
/*  468 */         if (this.minecraft.crosshairPickEntity != null && this.minecraft.crosshairPickEntity instanceof LivingEntity && attackStrengthScale >= 1.0F) {
/*  469 */           renderMaxAttackIndicator = (this.minecraft.player.getCurrentItemAttackStrengthDelay() > 5.0F);
/*  470 */           renderMaxAttackIndicator &= this.minecraft.crosshairPickEntity.isAlive();
/*  471 */           AttackRange attackRange = (AttackRange)this.minecraft.player.getActiveItem().get(DataComponents.ATTACK_RANGE);
/*  472 */           renderMaxAttackIndicator &= (attackRange == null || attackRange.isInRange((LivingEntity)this.minecraft.player, this.minecraft.hitResult.getLocation())) ? true : false;
/*      */         } 
/*      */         
/*  475 */         int y = graphics.guiHeight() / 2 - 7 + 16;
/*  476 */         int x = graphics.guiWidth() / 2 - 8;
/*      */         
/*  478 */         if (renderMaxAttackIndicator) {
/*  479 */           graphics.blitSprite(RenderPipelines.CROSSHAIR, CROSSHAIR_ATTACK_INDICATOR_FULL_SPRITE, x, y, 16, 16);
/*  480 */         } else if (attackStrengthScale < 1.0F) {
/*  481 */           int progress = (int)(attackStrengthScale * 17.0F);
/*      */           
/*  483 */           graphics.blitSprite(RenderPipelines.CROSSHAIR, CROSSHAIR_ATTACK_INDICATOR_BACKGROUND_SPRITE, x, y, 16, 4);
/*  484 */           graphics.blitSprite(RenderPipelines.CROSSHAIR, CROSSHAIR_ATTACK_INDICATOR_PROGRESS_SPRITE, 16, 4, 0, 0, x, y, progress, 4);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean canRenderCrosshairForSpectator(HitResult hitResult) {
/*  491 */     if (hitResult == null) {
/*  492 */       return false;
/*      */     }
/*      */     
/*  495 */     if (hitResult.getType() == HitResult.Type.ENTITY)
/*  496 */       return ((EntityHitResult)hitResult).getEntity() instanceof net.minecraft.world.MenuProvider; 
/*  497 */     if (hitResult.getType() == HitResult.Type.BLOCK) {
/*  498 */       BlockPos pos = ((BlockHitResult)hitResult).getBlockPos();
/*  499 */       ClientLevel clientLevel = this.minecraft.level;
/*  500 */       return (clientLevel.getBlockState(pos).getMenuProvider((Level)clientLevel, pos) != null);
/*      */     } 
/*      */     
/*  503 */     return false;
/*      */   }
/*      */   
/*      */   private void renderEffects(GuiGraphics graphics, DeltaTracker deltaTracker) {
/*  507 */     Collection<MobEffectInstance> activeEffects = this.minecraft.player.getActiveEffects();
/*      */     
/*  509 */     if (activeEffects.isEmpty() || (this.minecraft.screen != null && this.minecraft.screen.showsActiveEffects())) {
/*      */       return;
/*      */     }
/*      */     
/*  513 */     int beneficialCount = 0;
/*  514 */     int harmfulCount = 0;
/*      */     
/*  516 */     for (MobEffectInstance instance : (Iterable<MobEffectInstance>)Ordering.natural().reverse().sortedCopy(activeEffects)) {
/*  517 */       Holder<MobEffect> effect = instance.getEffect();
/*      */       
/*  519 */       if (instance.showIcon()) {
/*  520 */         int x = graphics.guiWidth();
/*  521 */         int y = 1;
/*  522 */         if (this.minecraft.isDemo()) {
/*  523 */           y += 15;
/*      */         }
/*      */         
/*  526 */         if (((MobEffect)effect.value()).isBeneficial()) {
/*  527 */           beneficialCount++;
/*  528 */           x -= 25 * beneficialCount;
/*      */         } else {
/*  530 */           harmfulCount++;
/*  531 */           x -= 25 * harmfulCount;
/*  532 */           y += 26;
/*      */         } 
/*      */         
/*  535 */         float alpha = 1.0F;
/*  536 */         if (instance.isAmbient()) {
/*  537 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EFFECT_BACKGROUND_AMBIENT_SPRITE, x, y, 24, 24);
/*      */         } else {
/*  539 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, EFFECT_BACKGROUND_SPRITE, x, y, 24, 24);
/*      */           
/*  541 */           if (instance.endsWithin(200)) {
/*  542 */             int remainingDuration = instance.getDuration();
/*  543 */             int usedSeconds = 10 - remainingDuration / 20;
/*  544 */             alpha = Mth.clamp(remainingDuration / 10.0F / 5.0F * 0.5F, 0.0F, 0.5F) + Mth.cos((remainingDuration * 3.1415927F / 5.0F)) * Mth.clamp(usedSeconds / 10.0F * 0.25F, 0.0F, 0.25F);
/*  545 */             alpha = Mth.clamp(alpha, 0.0F, 1.0F);
/*      */           } 
/*      */         } 
/*      */         
/*  549 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, getMobEffectSprite(effect), x + 3, y + 3, 18, 18, ARGB.white(alpha));
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   public static Identifier getMobEffectSprite(Holder<MobEffect> effect) {
/*  555 */     return effect.unwrapKey().map(ResourceKey::identifier)
/*  556 */       .map(id -> id.withPrefix("mob_effect/"))
/*  557 */       .orElseGet(MissingTextureAtlasSprite::getLocation);
/*      */   }
/*      */   
/*      */   private void renderHotbarAndDecorations(GuiGraphics graphics, DeltaTracker deltaTracker) {
/*  561 */     if (this.minecraft.gameMode.getPlayerMode() == GameType.SPECTATOR) {
/*  562 */       this.spectatorGui.renderHotbar(graphics);
/*      */     } else {
/*  564 */       renderItemHotbar(graphics, deltaTracker);
/*      */     } 
/*      */     
/*  567 */     if (this.minecraft.gameMode.canHurtPlayer()) {
/*  568 */       renderPlayerHealth(graphics);
/*      */     }
/*  570 */     renderVehicleHealth(graphics);
/*      */     
/*  572 */     ContextualInfo nextContextualInfo = nextContextualInfoState();
/*  573 */     if (nextContextualInfo != this.contextualInfoBar.getKey()) {
/*  574 */       this.contextualInfoBar = Pair.of(nextContextualInfo, ((Supplier<ContextualBarRenderer>)this.contextualInfoBarRenderers.get(nextContextualInfo)).get());
/*      */     }
/*      */     
/*  577 */     ((ContextualBarRenderer)this.contextualInfoBar.getValue()).renderBackground(graphics, deltaTracker);
/*      */     
/*  579 */     if (this.minecraft.gameMode.hasExperience() && this.minecraft.player.experienceLevel > 0) {
/*  580 */       ContextualBarRenderer.renderExperienceLevel(graphics, this.minecraft.font, this.minecraft.player.experienceLevel);
/*      */     }
/*  582 */     ((ContextualBarRenderer)this.contextualInfoBar.getValue()).render(graphics, deltaTracker);
/*      */     
/*  584 */     if (this.minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {
/*  585 */       renderSelectedItemName(graphics);
/*  586 */     } else if (this.minecraft.player.isSpectator()) {
/*  587 */       this.spectatorGui.renderAction(graphics);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void renderItemHotbar(GuiGraphics graphics, DeltaTracker deltaTracker) {
/*  592 */     Player player = getCameraPlayer();
/*  593 */     if (player == null) {
/*      */       return;
/*      */     }
/*      */     
/*  597 */     ItemStack offhand = player.getOffhandItem();
/*  598 */     HumanoidArm offhandArm = player.getMainArm().getOpposite();
/*  599 */     int screenCenter = graphics.guiWidth() / 2;
/*  600 */     int hotbarWidth = 182;
/*  601 */     int halfHotbar = 91;
/*      */     
/*  603 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SPRITE, screenCenter - 91, graphics.guiHeight() - 22, 182, 22);
/*  604 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_SELECTION_SPRITE, screenCenter - 91 - 1 + player.getInventory().getSelectedSlot() * 20, graphics.guiHeight() - 22 - 1, 24, 23);
/*      */     
/*  606 */     if (!offhand.isEmpty()) {
/*  607 */       if (offhandArm == HumanoidArm.LEFT) {
/*  608 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_LEFT_SPRITE, screenCenter - 91 - 29, graphics.guiHeight() - 23, 29, 24);
/*      */       } else {
/*  610 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_OFFHAND_RIGHT_SPRITE, screenCenter + 91, graphics.guiHeight() - 23, 29, 24);
/*      */       } 
/*      */     }
/*      */     
/*  614 */     int seed = 1;
/*  615 */     for (int i = 0; i < 9; i++) {
/*  616 */       int x = screenCenter - 90 + i * 20 + 2;
/*  617 */       int y = graphics.guiHeight() - 16 - 3;
/*  618 */       renderSlot(graphics, x, y, deltaTracker, player, player.getInventory().getItem(i), seed++);
/*      */     } 
/*      */     
/*  621 */     if (!offhand.isEmpty()) {
/*  622 */       int y = graphics.guiHeight() - 16 - 3;
/*  623 */       if (offhandArm == HumanoidArm.LEFT) {
/*  624 */         renderSlot(graphics, screenCenter - 91 - 26, y, deltaTracker, player, offhand, seed++);
/*      */       } else {
/*  626 */         renderSlot(graphics, screenCenter + 91 + 10, y, deltaTracker, player, offhand, seed++);
/*      */       } 
/*      */     } 
/*      */     
/*  630 */     if (this.minecraft.options.attackIndicator().get() == AttackIndicatorStatus.HOTBAR) {
/*  631 */       float attackStrengthScale = this.minecraft.player.getAttackStrengthScale(0.0F);
/*  632 */       if (attackStrengthScale < 1.0F) {
/*  633 */         int y = graphics.guiHeight() - 20;
/*  634 */         int x = screenCenter + 91 + 6;
/*  635 */         if (offhandArm == HumanoidArm.RIGHT) {
/*  636 */           x = screenCenter - 91 - 22;
/*      */         }
/*      */         
/*  639 */         int progress = (int)(attackStrengthScale * 19.0F);
/*      */         
/*  641 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_ATTACK_INDICATOR_BACKGROUND_SPRITE, x, y, 18, 18);
/*  642 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HOTBAR_ATTACK_INDICATOR_PROGRESS_SPRITE, 18, 18, 0, 18 - progress, x, y + 18 - progress, 18, progress);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void renderSelectedItemName(GuiGraphics graphics) {
/*  648 */     Profiler.get().push("selectedItemName");
/*      */     
/*  650 */     if (this.toolHighlightTimer > 0 && !this.lastToolHighlight.isEmpty()) {
/*  651 */       MutableComponent str = Component.empty().append(this.lastToolHighlight.getHoverName()).withStyle(this.lastToolHighlight.getRarity().color());
/*  652 */       if (this.lastToolHighlight.has(DataComponents.CUSTOM_NAME)) {
/*  653 */         str.withStyle(ChatFormatting.ITALIC);
/*      */       }
/*      */       
/*  656 */       int strWidth = getFont().width((FormattedText)str);
/*  657 */       int x = (graphics.guiWidth() - strWidth) / 2;
/*  658 */       int y = graphics.guiHeight() - 59;
/*  659 */       if (!this.minecraft.gameMode.canHurtPlayer())
/*      */       {
/*  661 */         y += 14;
/*      */       }
/*      */       
/*  664 */       int alpha = (int)(this.toolHighlightTimer * 256.0F / 10.0F);
/*  665 */       if (alpha > 255) {
/*  666 */         alpha = 255;
/*      */       }
/*  668 */       if (alpha > 0) {
/*  669 */         graphics.drawStringWithBackdrop(getFont(), (Component)str, x, y, strWidth, ARGB.white(alpha));
/*      */       }
/*      */     } 
/*      */     
/*  673 */     Profiler.get().pop();
/*      */   }
/*      */   private void renderDemoOverlay(GuiGraphics graphics, DeltaTracker deltaTracker) {
/*      */     MutableComponent mutableComponent;
/*  677 */     if (!this.minecraft.isDemo()) {
/*      */       return;
/*      */     }
/*      */     
/*  681 */     Profiler.get().push("demo");
/*  682 */     graphics.nextStratum();
/*      */     
/*  684 */     if (this.minecraft.level.getGameTime() >= 120500L) {
/*  685 */       Component msg = DEMO_EXPIRED_TEXT;
/*      */     } else {
/*  687 */       mutableComponent = Component.translatable("demo.remainingTime", new Object[] { StringUtil.formatTickDuration((int)(120500L - this.minecraft.level.getGameTime()), this.minecraft.level.tickRateManager().tickrate()) });
/*      */     } 
/*      */     
/*  690 */     int width = getFont().width((FormattedText)mutableComponent);
/*  691 */     int textX = graphics.guiWidth() - width - 10;
/*  692 */     int textY = 5;
/*  693 */     graphics.drawStringWithBackdrop(getFont(), (Component)mutableComponent, textX, 5, width, -1);
/*  694 */     Profiler.get().pop();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private void displayScoreboardSidebar(GuiGraphics graphics, Objective objective) {
/*  700 */     Scoreboard scoreboard = objective.getScoreboard();
/*  701 */     NumberFormat objectiveScoreFormat = objective.numberFormatOrDefault((NumberFormat)StyledFormat.SIDEBAR_DEFAULT);
/*  702 */     DisplayEntry[] entriesToDisplay = (DisplayEntry[])scoreboard.listPlayerScores(objective).stream()
/*  703 */       .filter(input -> !input.isHidden())
/*  704 */       .sorted(SCORE_DISPLAY_ORDER)
/*  705 */       .limit(15L)
/*  706 */       .map(score -> { PlayerTeam team = scoreboard.getPlayersTeam(scoreboard.owner()); Component ownerName = scoreboard.ownerName(); MutableComponent mutableComponent1 = PlayerTeam.formatNameForTeam((Team)team, ownerName), mutableComponent2 = scoreboard.formatValue(scoreboard); int scoreWidth = getFont().width((FormattedText)mutableComponent2); static final class DisplayEntry extends Record {
/*      */             private final Component name;
/*      */             private final Component score;
/*      */             private final int scoreWidth;
/*      */             DisplayEntry(Component name, Component score, int scoreWidth) { this.name = name; this.score = score; this.scoreWidth = scoreWidth; }
/*      */             public final String toString() { // Byte code:
/*      */               //   0: aload_0
/*      */               //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/Gui$1DisplayEntry;)Ljava/lang/String;
/*      */               //   6: areturn
/*      */               // Line number table:
/*      */               //   Java source line number -> byte code offset
/*      */               //   #698	-> 0
/*      */               // Local variable table:
/*      */               //   start	length	slot	name	descriptor
/*      */               //   0	7	0	this	Lnet/minecraft/client/gui/Gui$1DisplayEntry; }
/*      */             public final int hashCode() { // Byte code:
/*      */               //   0: aload_0
/*      */               //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/Gui$1DisplayEntry;)I
/*      */               //   6: ireturn
/*      */               // Line number table:
/*      */               //   Java source line number -> byte code offset
/*      */               //   #698	-> 0
/*      */               // Local variable table:
/*      */               //   start	length	slot	name	descriptor
/*      */               //   0	7	0	this	Lnet/minecraft/client/gui/Gui$1DisplayEntry; }
/*      */             public final boolean equals(Object o) { // Byte code:
/*      */               //   0: aload_0
/*      */               //   1: aload_1
/*      */               //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/Gui$1DisplayEntry;Ljava/lang/Object;)Z
/*      */               //   7: ireturn
/*      */               // Line number table:
/*      */               //   Java source line number -> byte code offset
/*      */               //   #698	-> 0
/*      */               // Local variable table:
/*      */               //   start	length	slot	name	descriptor
/*      */               //   0	8	0	this	Lnet/minecraft/client/gui/Gui$1DisplayEntry;
/*      */               //   0	8	1	o	Ljava/lang/Object; }
/*  714 */             public Component name() { return this.name; } public Component score() { return this.score; } public int scoreWidth() { return this.scoreWidth; } }; return new DisplayEntry((Component)mutableComponent1, (Component)mutableComponent2, scoreWidth); }).toArray(x$0 -> new DisplayEntry[x$0]);
/*      */     
/*  716 */     Component objectiveDisplayName = objective.getDisplayName();
/*  717 */     int objectiveDisplayNameWidth = getFont().width((FormattedText)objectiveDisplayName);
/*  718 */     int biggestWidth = objectiveDisplayNameWidth;
/*      */     
/*  720 */     int spacerWidth = getFont().width(": ");
/*  721 */     for (DisplayEntry entry : entriesToDisplay) {
/*  722 */       biggestWidth = Math.max(biggestWidth, getFont().width((FormattedText)entry.name) + ((entry.scoreWidth > 0) ? (spacerWidth + entry.scoreWidth) : 0));
/*      */     }
/*      */     
/*  725 */     int width = biggestWidth;
/*  726 */     int entriesCount = entriesToDisplay.length;
/*  727 */     Objects.requireNonNull(getFont()); int height = entriesCount * 9;
/*  728 */     int bottom = graphics.guiHeight() / 2 + height / 3;
/*  729 */     int rightPadding = 3;
/*  730 */     int left = graphics.guiWidth() - width - 3;
/*  731 */     int right = graphics.guiWidth() - 3 + 2;
/*      */     
/*  733 */     int backgroundColor = this.minecraft.options.getBackgroundColor(0.3F);
/*  734 */     int headerBackgroundColor = this.minecraft.options.getBackgroundColor(0.4F);
/*      */     
/*  736 */     Objects.requireNonNull(getFont()); int headerY = bottom - entriesCount * 9;
/*      */     
/*  738 */     Objects.requireNonNull(getFont()); graphics.fill(left - 2, headerY - 9 - 1, right, headerY - 1, headerBackgroundColor);
/*  739 */     graphics.fill(left - 2, headerY - 1, right, bottom, backgroundColor);
/*      */     
/*  741 */     Objects.requireNonNull(getFont()); graphics.drawString(getFont(), objectiveDisplayName, left + width / 2 - objectiveDisplayNameWidth / 2, headerY - 9, -1, false);
/*  742 */     for (int i = 0; i < entriesCount; i++) {
/*  743 */       DisplayEntry e = entriesToDisplay[i];
/*  744 */       Objects.requireNonNull(getFont()); int y = bottom - (entriesCount - i) * 9;
/*      */       
/*  746 */       graphics.drawString(getFont(), e.name, left, y, -1, false);
/*  747 */       graphics.drawString(getFont(), e.score, right - e.scoreWidth, y, -1, false);
/*      */     } 
/*      */   }
/*      */   
/*      */   private Player getCameraPlayer() {
/*  752 */     Entity entity = this.minecraft.getCameraEntity(); Player player = (Player)entity; return (entity instanceof Player) ? player : null;
/*      */   }
/*      */   
/*      */   private LivingEntity getPlayerVehicleWithHealth() {
/*  756 */     Player player = getCameraPlayer();
/*  757 */     if (player != null) {
/*  758 */       Entity vehicle = player.getVehicle();
/*  759 */       if (vehicle == null) {
/*  760 */         return null;
/*      */       }
/*  762 */       if (vehicle instanceof LivingEntity) {
/*  763 */         return (LivingEntity)vehicle;
/*      */       }
/*      */     } 
/*  766 */     return null;
/*      */   }
/*      */   
/*      */   private int getVehicleMaxHearts(LivingEntity vehicle) {
/*  770 */     if (vehicle == null || !vehicle.showVehicleHealth()) {
/*  771 */       return 0;
/*      */     }
/*      */     
/*  774 */     float maxVehicleHealth = vehicle.getMaxHealth();
/*  775 */     int hearts = (int)(maxVehicleHealth + 0.5F) / 2;
/*  776 */     if (hearts > 30) {
/*  777 */       hearts = 30;
/*      */     }
/*  779 */     return hearts;
/*      */   }
/*      */   
/*      */   private int getVisibleVehicleHeartRows(int hearts) {
/*  783 */     return (int)Math.ceil(hearts / 10.0D);
/*      */   }
/*      */   
/*      */   private void renderPlayerHealth(GuiGraphics graphics) {
/*  787 */     Player player = getCameraPlayer();
/*  788 */     if (player == null) {
/*      */       return;
/*      */     }
/*  791 */     int currentHealth = Mth.ceil(player.getHealth());
/*      */     
/*  793 */     boolean blink = (this.healthBlinkTime > this.tickCount && (this.healthBlinkTime - this.tickCount) / 3L % 2L == 1L);
/*  794 */     long timeMillis = Util.getMillis();
/*  795 */     if (currentHealth < this.lastHealth && player.invulnerableTime > 0) {
/*  796 */       this.lastHealthTime = timeMillis;
/*  797 */       this.healthBlinkTime = (this.tickCount + 20);
/*  798 */     } else if (currentHealth > this.lastHealth && player.invulnerableTime > 0) {
/*  799 */       this.lastHealthTime = timeMillis;
/*  800 */       this.healthBlinkTime = (this.tickCount + 10);
/*      */     } 
/*  802 */     if (timeMillis - this.lastHealthTime > 1000L) {
/*  803 */       this.displayHealth = currentHealth;
/*  804 */       this.lastHealthTime = timeMillis;
/*      */     } 
/*  806 */     this.lastHealth = currentHealth;
/*  807 */     int oldHealth = this.displayHealth;
/*  808 */     this.random.setSeed((this.tickCount * 312871));
/*      */     
/*  810 */     int xLeft = graphics.guiWidth() / 2 - 91;
/*  811 */     int xRight = graphics.guiWidth() / 2 + 91;
/*      */     
/*  813 */     int yLineBase = graphics.guiHeight() - 39;
/*  814 */     float maxHealth = Math.max((float)player.getAttributeValue(Attributes.MAX_HEALTH), Math.max(oldHealth, currentHealth));
/*  815 */     int totalAbsorption = Mth.ceil(player.getAbsorptionAmount());
/*  816 */     int numHealthRows = Mth.ceil((maxHealth + totalAbsorption) / 2.0F / 10.0F);
/*  817 */     int healthRowHeight = Math.max(10 - numHealthRows - 2, 3);
/*  818 */     int yLineAir = yLineBase - 10;
/*  819 */     int heartOffsetIndex = -1;
/*  820 */     if (player.hasEffect(MobEffects.REGENERATION)) {
/*  821 */       heartOffsetIndex = this.tickCount % Mth.ceil(maxHealth + 5.0F);
/*      */     }
/*      */ 
/*      */     
/*  825 */     Profiler.get().push("armor");
/*  826 */     renderArmor(graphics, player, yLineBase, numHealthRows, healthRowHeight, xLeft);
/*      */     
/*  828 */     Profiler.get().popPush("health");
/*  829 */     renderHearts(graphics, player, xLeft, yLineBase, healthRowHeight, heartOffsetIndex, maxHealth, currentHealth, oldHealth, totalAbsorption, blink);
/*      */     
/*  831 */     LivingEntity vehicleWithHearts = getPlayerVehicleWithHealth();
/*  832 */     int vehicleHearts = getVehicleMaxHearts(vehicleWithHearts);
/*  833 */     if (vehicleHearts == 0) {
/*  834 */       Profiler.get().popPush("food");
/*  835 */       renderFood(graphics, player, yLineBase, xRight);
/*  836 */       yLineAir -= 10;
/*      */     } 
/*      */     
/*  839 */     Profiler.get().popPush("air");
/*  840 */     renderAirBubbles(graphics, player, vehicleHearts, yLineAir, xRight);
/*      */     
/*  842 */     Profiler.get().pop();
/*      */   }
/*      */   
/*      */   private static void renderArmor(GuiGraphics graphics, Player player, int yLineBase, int numHealthRows, int healthRowHeight, int xLeft) {
/*  846 */     int armor = player.getArmorValue();
/*  847 */     if (armor <= 0) {
/*      */       return;
/*      */     }
/*  850 */     int yLineArmor = yLineBase - (numHealthRows - 1) * healthRowHeight - 10;
/*  851 */     for (int i = 0; i < 10; i++) {
/*  852 */       int xo = xLeft + i * 8;
/*  853 */       if (i * 2 + 1 < armor) {
/*  854 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_FULL_SPRITE, xo, yLineArmor, 9, 9);
/*      */       }
/*  856 */       if (i * 2 + 1 == armor) {
/*  857 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_HALF_SPRITE, xo, yLineArmor, 9, 9);
/*      */       }
/*  859 */       if (i * 2 + 1 > armor)
/*  860 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, ARMOR_EMPTY_SPRITE, xo, yLineArmor, 9, 9); 
/*      */     } 
/*      */   }
/*      */   public static interface RenderFunction {
/*      */     void render(GuiGraphics param1GuiGraphics, DeltaTracker param1DeltaTracker); }
/*      */   
/*  866 */   private enum HeartType { CONTAINER(
/*  867 */       Identifier.withDefaultNamespace("hud/heart/container"), 
/*  868 */       Identifier.withDefaultNamespace("hud/heart/container_blinking"), 
/*  869 */       Identifier.withDefaultNamespace("hud/heart/container"), 
/*  870 */       Identifier.withDefaultNamespace("hud/heart/container_blinking"), 
/*  871 */       Identifier.withDefaultNamespace("hud/heart/container_hardcore"), 
/*  872 */       Identifier.withDefaultNamespace("hud/heart/container_hardcore_blinking"), 
/*  873 */       Identifier.withDefaultNamespace("hud/heart/container_hardcore"), 
/*  874 */       Identifier.withDefaultNamespace("hud/heart/container_hardcore_blinking")),
/*      */     
/*  876 */     NORMAL(
/*  877 */       Identifier.withDefaultNamespace("hud/heart/full"), 
/*  878 */       Identifier.withDefaultNamespace("hud/heart/full_blinking"), 
/*  879 */       Identifier.withDefaultNamespace("hud/heart/half"), 
/*  880 */       Identifier.withDefaultNamespace("hud/heart/half_blinking"), 
/*  881 */       Identifier.withDefaultNamespace("hud/heart/hardcore_full"), 
/*  882 */       Identifier.withDefaultNamespace("hud/heart/hardcore_full_blinking"), 
/*  883 */       Identifier.withDefaultNamespace("hud/heart/hardcore_half"), 
/*  884 */       Identifier.withDefaultNamespace("hud/heart/hardcore_half_blinking")),
/*      */     
/*  886 */     POISIONED(
/*  887 */       Identifier.withDefaultNamespace("hud/heart/poisoned_full"), 
/*  888 */       Identifier.withDefaultNamespace("hud/heart/poisoned_full_blinking"), 
/*  889 */       Identifier.withDefaultNamespace("hud/heart/poisoned_half"), 
/*  890 */       Identifier.withDefaultNamespace("hud/heart/poisoned_half_blinking"), 
/*  891 */       Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_full"), 
/*  892 */       Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_full_blinking"), 
/*  893 */       Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_half"), 
/*  894 */       Identifier.withDefaultNamespace("hud/heart/poisoned_hardcore_half_blinking")),
/*      */     
/*  896 */     WITHERED(
/*  897 */       Identifier.withDefaultNamespace("hud/heart/withered_full"), 
/*  898 */       Identifier.withDefaultNamespace("hud/heart/withered_full_blinking"), 
/*  899 */       Identifier.withDefaultNamespace("hud/heart/withered_half"), 
/*  900 */       Identifier.withDefaultNamespace("hud/heart/withered_half_blinking"), 
/*  901 */       Identifier.withDefaultNamespace("hud/heart/withered_hardcore_full"), 
/*  902 */       Identifier.withDefaultNamespace("hud/heart/withered_hardcore_full_blinking"), 
/*  903 */       Identifier.withDefaultNamespace("hud/heart/withered_hardcore_half"), 
/*  904 */       Identifier.withDefaultNamespace("hud/heart/withered_hardcore_half_blinking")),
/*      */     
/*  906 */     ABSORBING(
/*  907 */       Identifier.withDefaultNamespace("hud/heart/absorbing_full"), 
/*  908 */       Identifier.withDefaultNamespace("hud/heart/absorbing_full_blinking"), 
/*  909 */       Identifier.withDefaultNamespace("hud/heart/absorbing_half"), 
/*  910 */       Identifier.withDefaultNamespace("hud/heart/absorbing_half_blinking"), 
/*  911 */       Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_full"), 
/*  912 */       Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_full_blinking"), 
/*  913 */       Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_half"), 
/*  914 */       Identifier.withDefaultNamespace("hud/heart/absorbing_hardcore_half_blinking")),
/*      */     
/*  916 */     FROZEN(
/*  917 */       Identifier.withDefaultNamespace("hud/heart/frozen_full"), 
/*  918 */       Identifier.withDefaultNamespace("hud/heart/frozen_full_blinking"), 
/*  919 */       Identifier.withDefaultNamespace("hud/heart/frozen_half"), 
/*  920 */       Identifier.withDefaultNamespace("hud/heart/frozen_half_blinking"), 
/*  921 */       Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_full"), 
/*  922 */       Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_full_blinking"), 
/*  923 */       Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_half"), 
/*  924 */       Identifier.withDefaultNamespace("hud/heart/frozen_hardcore_half_blinking"));
/*      */     
/*      */     private final Identifier full;
/*      */     
/*      */     private final Identifier fullBlinking;
/*      */     
/*      */     private final Identifier half;
/*      */     private final Identifier halfBlinking;
/*      */     private final Identifier hardcoreFull;
/*      */     private final Identifier hardcoreFullBlinking;
/*      */     private final Identifier hardcoreHalf;
/*      */     private final Identifier hardcoreHalfBlinking;
/*      */     
/*      */     HeartType(Identifier full, Identifier fullBlinking, Identifier half, Identifier halfBlinking, Identifier hardcoreFull, Identifier hardcoreFullBlinking, Identifier hardcoreHalf, Identifier hardcoreHalfBlinking) {
/*  938 */       this.full = full;
/*  939 */       this.fullBlinking = fullBlinking;
/*  940 */       this.half = half;
/*  941 */       this.halfBlinking = halfBlinking;
/*  942 */       this.hardcoreFull = hardcoreFull;
/*  943 */       this.hardcoreFullBlinking = hardcoreFullBlinking;
/*  944 */       this.hardcoreHalf = hardcoreHalf;
/*  945 */       this.hardcoreHalfBlinking = hardcoreHalfBlinking;
/*      */     }
/*      */     
/*      */     public Identifier getSprite(boolean isHardcore, boolean isHalf, boolean isBlink) {
/*  949 */       if (!isHardcore) {
/*  950 */         if (isHalf) {
/*  951 */           return isBlink ? this.halfBlinking : this.half;
/*      */         }
/*  953 */         return isBlink ? this.fullBlinking : this.full;
/*      */       } 
/*      */       
/*  956 */       if (isHalf) {
/*  957 */         return isBlink ? this.hardcoreHalfBlinking : this.hardcoreHalf;
/*      */       }
/*  959 */       return isBlink ? this.hardcoreFullBlinking : this.hardcoreFull;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     private static HeartType forPlayer(Player player) {
/*      */       HeartType type;
/*  966 */       if (player.hasEffect(MobEffects.POISON)) {
/*  967 */         type = POISIONED;
/*  968 */       } else if (player.hasEffect(MobEffects.WITHER)) {
/*  969 */         type = WITHERED;
/*  970 */       } else if (player.isFullyFrozen()) {
/*  971 */         type = FROZEN;
/*      */       } else {
/*  973 */         type = NORMAL;
/*      */       } 
/*  975 */       return type;
/*      */     } }
/*      */ 
/*      */   
/*      */   private void renderHearts(GuiGraphics graphics, Player player, int xLeft, int yLineBase, int healthRowHeight, int heartOffsetIndex, float maxHealth, int currentHealth, int oldHealth, int absorption, boolean blink) {
/*  980 */     HeartType type = HeartType.forPlayer(player);
/*  981 */     boolean isHardcore = player.level().getLevelData().isHardcore();
/*      */     
/*  983 */     int healthContainerCount = Mth.ceil(maxHealth / 2.0D);
/*  984 */     int absorptionContainerCount = Mth.ceil(absorption / 2.0D);
/*  985 */     int maxHealthHalvesCount = healthContainerCount * 2;
/*      */     
/*  987 */     for (int containerIndex = healthContainerCount + absorptionContainerCount - 1; containerIndex >= 0; containerIndex--) {
/*  988 */       int row = containerIndex / 10;
/*  989 */       int column = containerIndex % 10;
/*      */       
/*  991 */       int xo = xLeft + column * 8;
/*  992 */       int yo = yLineBase - row * healthRowHeight;
/*      */       
/*  994 */       if (currentHealth + absorption <= 4) {
/*  995 */         yo += this.random.nextInt(2);
/*      */       }
/*  997 */       if (containerIndex < healthContainerCount && containerIndex == heartOffsetIndex) {
/*  998 */         yo -= 2;
/*      */       }
/*      */       
/* 1001 */       renderHeart(graphics, HeartType.CONTAINER, xo, yo, isHardcore, blink, false);
/*      */       
/* 1003 */       int halves = containerIndex * 2;
/*      */       
/* 1005 */       boolean isAbsorptionHeart = (containerIndex >= healthContainerCount);
/* 1006 */       if (isAbsorptionHeart) {
/* 1007 */         int absorptionHalves = halves - maxHealthHalvesCount;
/* 1008 */         if (absorptionHalves < absorption) {
/* 1009 */           boolean halfHeart = (absorptionHalves + 1 == absorption);
/* 1010 */           renderHeart(graphics, (type == HeartType.WITHERED) ? type : HeartType.ABSORBING, xo, yo, isHardcore, false, halfHeart);
/*      */         } 
/*      */       } 
/* 1013 */       if (blink && halves < oldHealth) {
/* 1014 */         boolean halfHeart = (halves + 1 == oldHealth);
/* 1015 */         renderHeart(graphics, type, xo, yo, isHardcore, true, halfHeart);
/*      */       } 
/* 1017 */       if (halves < currentHealth) {
/* 1018 */         boolean halfHeart = (halves + 1 == currentHealth);
/* 1019 */         renderHeart(graphics, type, xo, yo, isHardcore, false, halfHeart);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private void renderHeart(GuiGraphics graphics, HeartType type, int xo, int yo, boolean isHardcore, boolean blinks, boolean half) {
/* 1025 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, type.getSprite(isHardcore, half, blinks), xo, yo, 9, 9);
/*      */   }
/*      */   
/*      */   private void renderAirBubbles(GuiGraphics graphics, Player player, int vehicleHearts, int yLineAir, int xRight) {
/* 1029 */     int maxAirSupplyTicks = player.getMaxAirSupply();
/* 1030 */     int currentAirSupplyTicks = Math.clamp(player.getAirSupply(), 0, maxAirSupplyTicks);
/* 1031 */     boolean isUnderWater = player.isEyeInFluid(FluidTags.WATER);
/* 1032 */     if (isUnderWater || currentAirSupplyTicks < maxAirSupplyTicks) {
/* 1033 */       yLineAir = getAirBubbleYLine(vehicleHearts, yLineAir);
/* 1034 */       int fullAirBubbles = getCurrentAirSupplyBubble(currentAirSupplyTicks, maxAirSupplyTicks, -2);
/* 1035 */       int poppingAirBubblePosition = getCurrentAirSupplyBubble(currentAirSupplyTicks, maxAirSupplyTicks, 0);
/* 1036 */       int emptyAirBubbles = 10 - getCurrentAirSupplyBubble(currentAirSupplyTicks, maxAirSupplyTicks, getEmptyBubbleDelayDuration(currentAirSupplyTicks, isUnderWater));
/* 1037 */       boolean isPoppingBubble = (fullAirBubbles != poppingAirBubblePosition);
/*      */       
/* 1039 */       if (!isUnderWater) {
/* 1040 */         this.lastBubblePopSoundPlayed = 0;
/*      */       }
/*      */       
/* 1043 */       for (int airBubble = 1; airBubble <= 10; airBubble++) {
/* 1044 */         int airBubbleXPos = xRight - (airBubble - 1) * 8 - 9;
/*      */         
/* 1046 */         if (airBubble <= fullAirBubbles) {
/* 1047 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, AIR_SPRITE, airBubbleXPos, yLineAir, 9, 9);
/* 1048 */         } else if (isPoppingBubble && airBubble == poppingAirBubblePosition && isUnderWater) {
/* 1049 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, AIR_POPPING_SPRITE, airBubbleXPos, yLineAir, 9, 9);
/* 1050 */           playAirBubblePoppedSound(airBubble, player, emptyAirBubbles);
/* 1051 */         } else if (airBubble > 10 - emptyAirBubbles) {
/* 1052 */           int wobbleYOffset = (emptyAirBubbles == 10 && this.tickCount % 2 == 0) ? this.random.nextInt(2) : 0;
/* 1053 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, AIR_EMPTY_SPRITE, airBubbleXPos, yLineAir + wobbleYOffset, 9, 9);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   private int getAirBubbleYLine(int vehicleHearts, int yLineAir) {
/* 1061 */     int rowOffset = getVisibleVehicleHeartRows(vehicleHearts) - 1;
/* 1062 */     yLineAir -= rowOffset * 10;
/* 1063 */     return yLineAir;
/*      */   }
/*      */   
/*      */   private static int getCurrentAirSupplyBubble(int currentAirSupplyTicks, int maxAirSupplyTicks, int tickOffset) {
/* 1067 */     return Mth.ceil(((currentAirSupplyTicks + tickOffset) * 10) / maxAirSupplyTicks);
/*      */   }
/*      */   
/*      */   private static int getEmptyBubbleDelayDuration(int currentAirSupplyTicks, boolean isUnderWater) {
/* 1071 */     return (currentAirSupplyTicks == 0 || !isUnderWater) ? 0 : 1;
/*      */   }
/*      */   
/*      */   private void playAirBubblePoppedSound(int bubble, Player player, int emptyAirBubbles) {
/* 1075 */     if (this.lastBubblePopSoundPlayed != bubble) {
/* 1076 */       float soundVolume = 0.5F + 0.1F * Math.max(0, emptyAirBubbles - 3 + 1);
/* 1077 */       float soundPitch = 1.0F + 0.1F * Math.max(0, emptyAirBubbles - 5 + 1);
/* 1078 */       player.playSound(SoundEvents.BUBBLE_POP, soundVolume, soundPitch);
/* 1079 */       this.lastBubblePopSoundPlayed = bubble;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void renderFood(GuiGraphics graphics, Player player, int yLineBase, int xRight) {
/* 1084 */     FoodData foodData = player.getFoodData();
/* 1085 */     int food = foodData.getFoodLevel();
/* 1086 */     for (int i = 0; i < 10; i++) {
/* 1087 */       Identifier empty, half, full; int yo = yLineBase;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1092 */       if (player.hasEffect(MobEffects.HUNGER)) {
/* 1093 */         empty = FOOD_EMPTY_HUNGER_SPRITE;
/* 1094 */         half = FOOD_HALF_HUNGER_SPRITE;
/* 1095 */         full = FOOD_FULL_HUNGER_SPRITE;
/*      */       } else {
/* 1097 */         empty = FOOD_EMPTY_SPRITE;
/* 1098 */         half = FOOD_HALF_SPRITE;
/* 1099 */         full = FOOD_FULL_SPRITE;
/*      */       } 
/*      */       
/* 1102 */       if (player.getFoodData().getSaturationLevel() <= 0.0F && 
/* 1103 */         this.tickCount % (food * 3 + 1) == 0) {
/* 1104 */         yo += this.random.nextInt(3) - 1;
/*      */       }
/*      */ 
/*      */       
/* 1108 */       int xo = xRight - i * 8 - 9;
/* 1109 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, empty, xo, yo, 9, 9);
/* 1110 */       if (i * 2 + 1 < food) {
/* 1111 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, full, xo, yo, 9, 9);
/*      */       }
/* 1113 */       if (i * 2 + 1 == food) {
/* 1114 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, half, xo, yo, 9, 9);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   private void renderVehicleHealth(GuiGraphics graphics) {
/* 1120 */     LivingEntity vehicleWithHearts = getPlayerVehicleWithHealth();
/* 1121 */     if (vehicleWithHearts == null) {
/*      */       return;
/*      */     }
/* 1124 */     int hearts = getVehicleMaxHearts(vehicleWithHearts);
/* 1125 */     if (hearts == 0) {
/*      */       return;
/*      */     }
/* 1128 */     int currentHealth = (int)Math.ceil(vehicleWithHearts.getHealth());
/*      */ 
/*      */     
/* 1131 */     Profiler.get().popPush("mountHealth");
/*      */ 
/*      */     
/* 1134 */     int yLine1 = graphics.guiHeight() - 39;
/* 1135 */     int xRight = graphics.guiWidth() / 2 + 91;
/* 1136 */     int yo = yLine1;
/* 1137 */     int baseHealth = 0;
/*      */     
/* 1139 */     while (hearts > 0) {
/* 1140 */       int rowHearts = Math.min(hearts, 10);
/* 1141 */       hearts -= rowHearts;
/*      */       
/* 1143 */       for (int i = 0; i < rowHearts; i++) {
/* 1144 */         int xo = xRight - i * 8 - 9;
/* 1145 */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_CONTAINER_SPRITE, xo, yo, 9, 9);
/* 1146 */         if (i * 2 + 1 + baseHealth < currentHealth) {
/* 1147 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_FULL_SPRITE, xo, yo, 9, 9);
/*      */         }
/* 1149 */         if (i * 2 + 1 + baseHealth == currentHealth) {
/* 1150 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, HEART_VEHICLE_HALF_SPRITE, xo, yo, 9, 9);
/*      */         }
/*      */       } 
/* 1153 */       yo -= 10;
/* 1154 */       baseHealth += 20;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void renderTextureOverlay(GuiGraphics graphics, Identifier texture, float alpha) {
/* 1159 */     int color = ARGB.white(alpha);
/* 1160 */     graphics.blit(RenderPipelines.GUI_TEXTURED, texture, 0, 0, 0.0F, 0.0F, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight(), color);
/*      */   }
/*      */   
/*      */   private void renderSpyglassOverlay(GuiGraphics graphics, float scale) {
/* 1164 */     float srcWidth = Math.min(graphics.guiWidth(), graphics.guiHeight());
/* 1165 */     float srcHeight = srcWidth;
/* 1166 */     float ratio = Math.min(graphics.guiWidth() / srcWidth, graphics.guiHeight() / srcHeight) * scale;
/*      */     
/* 1168 */     int width = Mth.floor(srcWidth * ratio);
/* 1169 */     int height = Mth.floor(srcHeight * ratio);
/*      */     
/* 1171 */     int left = (graphics.guiWidth() - width) / 2;
/* 1172 */     int top = (graphics.guiHeight() - height) / 2;
/* 1173 */     int right = left + width;
/* 1174 */     int bottom = top + height;
/*      */ 
/*      */     
/* 1177 */     graphics.blit(RenderPipelines.GUI_TEXTURED, SPYGLASS_SCOPE_LOCATION, left, top, 0.0F, 0.0F, width, height, width, height);
/*      */     
/* 1179 */     graphics.fill(RenderPipelines.GUI, 0, bottom, graphics.guiWidth(), graphics.guiHeight(), -16777216);
/*      */     
/* 1181 */     graphics.fill(RenderPipelines.GUI, 0, 0, graphics.guiWidth(), top, -16777216);
/*      */     
/* 1183 */     graphics.fill(RenderPipelines.GUI, 0, top, left, bottom, -16777216);
/*      */     
/* 1185 */     graphics.fill(RenderPipelines.GUI, right, top, graphics.guiWidth(), bottom, -16777216);
/*      */   }
/*      */   
/*      */   private void updateVignetteBrightness(Entity camera) {
/* 1189 */     BlockPos blockPos = BlockPos.containing(camera.getX(), camera.getEyeY(), camera.getZ());
/* 1190 */     float levelBrightness = LightTexture.getBrightness(camera.level().dimensionType(), camera.level().getMaxLocalRawBrightness(blockPos));
/* 1191 */     float brightness = Mth.clamp(1.0F - levelBrightness, 0.0F, 1.0F);
/* 1192 */     this.vignetteBrightness += (brightness - this.vignetteBrightness) * 0.01F;
/*      */   }
/*      */   private void renderVignette(GuiGraphics graphics, Entity camera) {
/*      */     int color;
/* 1196 */     WorldBorder worldBorder = this.minecraft.level.getWorldBorder();
/*      */     
/* 1198 */     float borderWarningStrength = 0.0F;
/*      */     
/* 1200 */     if (camera != null) {
/* 1201 */       float distToBorder = (float)worldBorder.getDistanceToBorder(camera);
/* 1202 */       double movingBlocksThreshold = Math.min(worldBorder.getLerpSpeed() * worldBorder.getWarningTime(), Math.abs(worldBorder.getLerpTarget() - worldBorder.getSize()));
/* 1203 */       double warningDistance = Math.max(worldBorder.getWarningBlocks(), movingBlocksThreshold);
/* 1204 */       if (distToBorder < warningDistance) {
/* 1205 */         borderWarningStrength = 1.0F - (float)(distToBorder / warningDistance);
/*      */       }
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1211 */     if (borderWarningStrength > 0.0F) {
/* 1212 */       borderWarningStrength = Mth.clamp(borderWarningStrength, 0.0F, 1.0F);
/* 1213 */       color = ARGB.colorFromFloat(1.0F, 0.0F, borderWarningStrength, borderWarningStrength);
/*      */     } else {
/* 1215 */       float brightness = this.vignetteBrightness;
/* 1216 */       brightness = Mth.clamp(brightness, 0.0F, 1.0F);
/* 1217 */       color = ARGB.colorFromFloat(1.0F, brightness, brightness, brightness);
/*      */     } 
/*      */     
/* 1220 */     graphics.blit(RenderPipelines.VIGNETTE, VIGNETTE_LOCATION, 0, 0, 0.0F, 0.0F, graphics.guiWidth(), graphics.guiHeight(), graphics.guiWidth(), graphics.guiHeight(), color);
/*      */   }
/*      */   
/*      */   private void renderPortalOverlay(GuiGraphics graphics, float alpha) {
/* 1224 */     if (alpha < 1.0F) {
/* 1225 */       alpha *= alpha;
/* 1226 */       alpha *= alpha;
/* 1227 */       alpha = alpha * 0.8F + 0.2F;
/*      */     } 
/* 1229 */     int color = ARGB.white(alpha);
/*      */     
/* 1231 */     TextureAtlasSprite slot = this.minecraft.getBlockRenderer().getBlockModelShaper().getParticleIcon(Blocks.NETHER_PORTAL.defaultBlockState());
/* 1232 */     graphics.blitSprite(RenderPipelines.GUI_TEXTURED, slot, 0, 0, graphics.guiWidth(), graphics.guiHeight(), color);
/*      */   }
/*      */   
/*      */   private void renderConfusionOverlay(GuiGraphics graphics, float strength) {
/* 1236 */     int screenWidth = graphics.guiWidth();
/* 1237 */     int screenHeight = graphics.guiHeight();
/*      */     
/* 1239 */     graphics.pose().pushMatrix();
/* 1240 */     float size = Mth.lerp(strength, 2.0F, 1.0F);
/* 1241 */     graphics.pose().translate(screenWidth / 2.0F, screenHeight / 2.0F);
/* 1242 */     graphics.pose().scale(size, size);
/* 1243 */     graphics.pose().translate(-screenWidth / 2.0F, -screenHeight / 2.0F);
/*      */     
/* 1245 */     float red = 0.2F * strength;
/* 1246 */     float green = 0.4F * strength;
/* 1247 */     float blue = 0.2F * strength;
/*      */     
/* 1249 */     graphics.blit(RenderPipelines.GUI_NAUSEA_OVERLAY, NAUSEA_LOCATION, 0, 0, 0.0F, 0.0F, screenWidth, screenHeight, screenWidth, screenHeight, ARGB.colorFromFloat(1.0F, red, green, blue));
/*      */     
/* 1251 */     graphics.pose().popMatrix();
/*      */   }
/*      */   
/*      */   private void renderSlot(GuiGraphics graphics, int x, int y, DeltaTracker deltaTracker, Player player, ItemStack itemStack, int seed) {
/* 1255 */     if (itemStack.isEmpty()) {
/*      */       return;
/*      */     }
/*      */     
/* 1259 */     float pop = itemStack.getPopTime() - deltaTracker.getGameTimeDeltaPartialTick(false);
/* 1260 */     if (pop > 0.0F) {
/* 1261 */       float squeeze = 1.0F + pop / 5.0F;
/*      */       
/* 1263 */       graphics.pose().pushMatrix();
/* 1264 */       graphics.pose().translate((x + 8), (y + 12));
/* 1265 */       graphics.pose().scale(1.0F / squeeze, (squeeze + 1.0F) / 2.0F);
/* 1266 */       graphics.pose().translate(-(x + 8), -(y + 12));
/*      */     } 
/*      */     
/* 1269 */     graphics.renderItem((LivingEntity)player, itemStack, x, y, seed);
/*      */     
/* 1271 */     if (pop > 0.0F) {
/* 1272 */       graphics.pose().popMatrix();
/*      */     }
/* 1274 */     graphics.renderItemDecorations(this.minecraft.font, itemStack, x, y);
/*      */   }
/*      */   
/*      */   public void tick(boolean pause) {
/* 1278 */     tickAutosaveIndicator();
/* 1279 */     if (!pause) {
/* 1280 */       tick();
/*      */     }
/*      */   }
/*      */   
/*      */   private void tick() {
/* 1285 */     if (this.overlayMessageTime > 0) {
/* 1286 */       this.overlayMessageTime--;
/*      */     }
/* 1288 */     if (this.titleTime > 0) {
/* 1289 */       this.titleTime--;
/* 1290 */       if (this.titleTime <= 0) {
/* 1291 */         this.title = null;
/* 1292 */         this.subtitle = null;
/*      */       } 
/*      */     } 
/* 1295 */     this.tickCount++;
/*      */     
/* 1297 */     Entity camera = this.minecraft.getCameraEntity();
/* 1298 */     if (camera != null) {
/* 1299 */       updateVignetteBrightness(camera);
/*      */     }
/*      */     
/* 1302 */     if (this.minecraft.player != null) {
/* 1303 */       ItemStack selected = this.minecraft.player.getInventory().getSelectedItem();
/*      */       
/* 1305 */       if (selected.isEmpty()) {
/* 1306 */         this.toolHighlightTimer = 0;
/* 1307 */       } else if (this.lastToolHighlight.isEmpty() || !selected.is(this.lastToolHighlight.getItem()) || !selected.getHoverName().equals(this.lastToolHighlight.getHoverName())) {
/* 1308 */         this.toolHighlightTimer = (int)(40.0D * (Double)this.minecraft.options.notificationDisplayTime().get());
/* 1309 */       } else if (this.toolHighlightTimer > 0) {
/* 1310 */         this.toolHighlightTimer--;
/*      */       } 
/* 1312 */       this.lastToolHighlight = selected;
/*      */     } 
/*      */     
/* 1315 */     this.chat.tick();
/*      */   }
/*      */   
/*      */   private void tickAutosaveIndicator() {
/* 1319 */     IntegratedServer integratedServer = this.minecraft.getSingleplayerServer();
/* 1320 */     boolean isAutosaving = (integratedServer != null && integratedServer.isCurrentlySaving());
/* 1321 */     this.lastAutosaveIndicatorValue = this.autosaveIndicatorValue;
/* 1322 */     this.autosaveIndicatorValue = Mth.lerp(0.2F, this.autosaveIndicatorValue, isAutosaving ? 1.0F : 0.0F);
/*      */   }
/*      */   
/*      */   public void setNowPlaying(Component string) {
/* 1326 */     MutableComponent mutableComponent = Component.translatable("record.nowPlaying", new Object[] { string });
/* 1327 */     setOverlayMessage((Component)mutableComponent, true);
/* 1328 */     this.minecraft.getNarrator().saySystemNow((Component)mutableComponent);
/*      */   }
/*      */   
/*      */   public void setOverlayMessage(Component string, boolean animate) {
/* 1332 */     setChatDisabledByPlayerShown(false);
/* 1333 */     this.overlayMessageString = string;
/* 1334 */     this.overlayMessageTime = 60;
/* 1335 */     this.animateOverlayMessageColor = animate;
/*      */   }
/*      */   
/*      */   public void setChatDisabledByPlayerShown(boolean chatDisabledByPlayerShown) {
/* 1339 */     this.chatDisabledByPlayerShown = chatDisabledByPlayerShown;
/*      */   }
/*      */   
/*      */   public boolean isShowingChatDisabledByPlayer() {
/* 1343 */     return (this.chatDisabledByPlayerShown && this.overlayMessageTime > 0);
/*      */   }
/*      */   
/*      */   public void setTimes(int fadeInTime, int stayTime, int fadeOutTime) {
/* 1347 */     if (fadeInTime >= 0) {
/* 1348 */       this.titleFadeInTime = fadeInTime;
/*      */     }
/* 1350 */     if (stayTime >= 0) {
/* 1351 */       this.titleStayTime = stayTime;
/*      */     }
/* 1353 */     if (fadeOutTime >= 0) {
/* 1354 */       this.titleFadeOutTime = fadeOutTime;
/*      */     }
/* 1356 */     if (this.titleTime > 0) {
/* 1357 */       this.titleTime = this.titleFadeInTime + this.titleStayTime + this.titleFadeOutTime;
/*      */     }
/*      */   }
/*      */   
/*      */   public void setSubtitle(Component subtitle) {
/* 1362 */     this.subtitle = subtitle;
/*      */   }
/*      */   
/*      */   public void setTitle(Component title) {
/* 1366 */     this.title = title;
/* 1367 */     this.titleTime = this.titleFadeInTime + this.titleStayTime + this.titleFadeOutTime;
/*      */   }
/*      */   
/*      */   public void clearTitles() {
/* 1371 */     this.title = null;
/* 1372 */     this.subtitle = null;
/* 1373 */     this.titleTime = 0;
/*      */   }
/*      */   
/*      */   public ChatComponent getChat() {
/* 1377 */     return this.chat;
/*      */   }
/*      */   
/*      */   public int getGuiTicks() {
/* 1381 */     return this.tickCount;
/*      */   }
/*      */   
/*      */   public Font getFont() {
/* 1385 */     return this.minecraft.font;
/*      */   }
/*      */   
/*      */   public SpectatorGui getSpectatorGui() {
/* 1389 */     return this.spectatorGui;
/*      */   }
/*      */   
/*      */   public PlayerTabOverlay getTabList() {
/* 1393 */     return this.tabList;
/*      */   }
/*      */   
/*      */   public void onDisconnected() {
/* 1397 */     this.tabList.reset();
/* 1398 */     this.bossOverlay.reset();
/* 1399 */     this.minecraft.getToastManager().clear();
/* 1400 */     this.debugOverlay.reset();
/* 1401 */     this.chat.clearMessages(true);
/* 1402 */     clearTitles();
/* 1403 */     resetTitleTimes();
/*      */   }
/*      */   
/*      */   public BossHealthOverlay getBossOverlay() {
/* 1407 */     return this.bossOverlay;
/*      */   }
/*      */   
/*      */   public DebugScreenOverlay getDebugOverlay() {
/* 1411 */     return this.debugOverlay;
/*      */   }
/*      */   
/*      */   public void clearCache() {
/* 1415 */     this.debugOverlay.clearChunkCache();
/*      */   }
/*      */   
/*      */   public void renderSavingIndicator(GuiGraphics graphics, DeltaTracker deltaTracker) {
/* 1419 */     if ((Boolean)this.minecraft.options.showAutosaveIndicator().get() && (this.autosaveIndicatorValue > 0.0F || this.lastAutosaveIndicatorValue > 0.0F)) {
/* 1420 */       int alpha = Mth.floor(255.0F * Mth.clamp(Mth.lerp(deltaTracker.getRealtimeDeltaTicks(), this.lastAutosaveIndicatorValue, this.autosaveIndicatorValue), 0.0F, 1.0F));
/* 1421 */       if (alpha > 0) {
/* 1422 */         Font font = getFont();
/* 1423 */         int width = font.width((FormattedText)SAVING_TEXT);
/* 1424 */         int color = ARGB.color(alpha, -1);
/* 1425 */         int textX = graphics.guiWidth() - width - 5;
/* 1426 */         Objects.requireNonNull(font); int textY = graphics.guiHeight() - 9 - 5;
/* 1427 */         graphics.nextStratum();
/* 1428 */         graphics.drawStringWithBackdrop(font, SAVING_TEXT, textX, textY, width, color);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   private boolean willPrioritizeExperienceInfo() {
/* 1434 */     return (this.minecraft.player.experienceDisplayStartTick + 100 > this.minecraft.player.tickCount);
/*      */   }
/*      */   
/*      */   private boolean willPrioritizeJumpInfo() {
/* 1438 */     return (this.minecraft.player.getJumpRidingScale() > 0.0F || (Integer)Optionull.mapOrDefault(this.minecraft.player.jumpableVehicle(), PlayerRideableJumping::getJumpCooldown, 0) > 0);
/*      */   }
/*      */   
/*      */   private ContextualInfo nextContextualInfoState() {
/* 1442 */     boolean canShowLocatorInfo = this.minecraft.player.connection.getWaypointManager().hasWaypoints();
/* 1443 */     boolean canShowVehicleJumpInfo = (this.minecraft.player.jumpableVehicle() != null);
/* 1444 */     boolean canShowExperienceInfo = this.minecraft.gameMode.hasExperience();
/*      */     
/* 1446 */     if (canShowLocatorInfo) {
/* 1447 */       if (canShowVehicleJumpInfo && willPrioritizeJumpInfo()) {
/* 1448 */         return ContextualInfo.JUMPABLE_VEHICLE;
/*      */       }
/* 1450 */       if (canShowExperienceInfo && willPrioritizeExperienceInfo()) {
/* 1451 */         return ContextualInfo.EXPERIENCE;
/*      */       }
/* 1453 */       return ContextualInfo.LOCATOR;
/*      */     } 
/*      */     
/* 1456 */     if (canShowVehicleJumpInfo) {
/* 1457 */       return ContextualInfo.JUMPABLE_VEHICLE;
/*      */     }
/* 1459 */     if (canShowExperienceInfo) {
/* 1460 */       return ContextualInfo.EXPERIENCE;
/*      */     }
/* 1462 */     return ContextualInfo.EMPTY;
/*      */   }
/*      */   
/*      */   enum ContextualInfo {
/* 1466 */     EMPTY,
/* 1467 */     EXPERIENCE,
/* 1468 */     LOCATOR,
/* 1469 */     JUMPABLE_VEHICLE;
/*      */   }
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/Gui.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */