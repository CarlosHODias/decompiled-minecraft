/*      */ package net.minecraft.client;
/*      */ import com.google.common.base.MoreObjects;
/*      */ import com.google.common.base.Splitter;
/*      */ import com.google.common.collect.ImmutableList;
/*      */ import com.google.common.collect.Lists;
/*      */ import com.google.common.collect.Sets;
/*      */ import com.google.common.io.Files;
/*      */ import com.google.gson.Gson;
/*      */ import com.google.gson.JsonElement;
/*      */ import com.google.gson.reflect.TypeToken;
/*      */ import com.mojang.blaze3d.platform.InputConstants;
/*      */ import com.mojang.blaze3d.platform.VideoMode;
/*      */ import com.mojang.blaze3d.platform.Window;
/*      */ import com.mojang.blaze3d.systems.RenderSystem;
/*      */ import com.mojang.datafixers.util.Pair;
/*      */ import com.mojang.logging.LogUtils;
/*      */ import com.mojang.serialization.Codec;
/*      */ import com.mojang.serialization.DataResult;
/*      */ import com.mojang.serialization.DynamicOps;
/*      */ import com.mojang.serialization.JsonOps;
/*      */ import java.io.BufferedReader;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.OutputStreamWriter;
/*      */ import java.io.PrintWriter;
/*      */ import java.nio.charset.StandardCharsets;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.EnumSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Objects;
/*      */ import java.util.Optional;
/*      */ import java.util.Set;
/*      */ import java.util.function.Consumer;
/*      */ import java.util.function.Function;
/*      */ import java.util.stream.Collectors;
/*      */ import java.util.stream.Stream;
/*      */ import net.minecraft.SharedConstants;
/*      */ import net.minecraft.client.gui.components.ChatComponent;
/*      */ import net.minecraft.client.gui.components.Tooltip;
/*      */ import net.minecraft.client.gui.screens.Screen;
/*      */ import net.minecraft.client.gui.screens.options.OptionsSubScreen;
/*      */ import net.minecraft.client.input.InputQuirks;
/*      */ import net.minecraft.client.renderer.GpuWarnlistManager;
/*      */ import net.minecraft.client.renderer.LevelRenderer;
/*      */ import net.minecraft.client.resources.sounds.SimpleSoundInstance;
/*      */ import net.minecraft.client.resources.sounds.SoundInstance;
/*      */ import net.minecraft.client.sounds.MusicManager;
/*      */ import net.minecraft.client.sounds.SoundEngine;
/*      */ import net.minecraft.client.sounds.SoundManager;
/*      */ import net.minecraft.client.sounds.SoundPreviewHandler;
/*      */ import net.minecraft.client.tutorial.TutorialSteps;
/*      */ import net.minecraft.core.Holder;
/*      */ import net.minecraft.nbt.CompoundTag;
/*      */ import net.minecraft.nbt.StringTag;
/*      */ import net.minecraft.nbt.Tag;
/*      */ import net.minecraft.network.chat.CommonComponents;
/*      */ import net.minecraft.network.chat.Component;
/*      */ import net.minecraft.server.level.ClientInformation;
/*      */ import net.minecraft.server.level.ParticleStatus;
/*      */ import net.minecraft.server.packs.repository.Pack;
/*      */ import net.minecraft.server.packs.repository.PackRepository;
/*      */ import net.minecraft.sounds.SoundEvents;
/*      */ import net.minecraft.sounds.SoundSource;
/*      */ import net.minecraft.util.ARGB;
/*      */ import net.minecraft.util.GsonHelper;
/*      */ import net.minecraft.util.LenientJsonParser;
/*      */ import net.minecraft.util.Mth;
/*      */ import net.minecraft.util.Util;
/*      */ import net.minecraft.util.datafix.DataFixTypes;
/*      */ import net.minecraft.world.entity.HumanoidArm;
/*      */ import net.minecraft.world.entity.player.ChatVisiblity;
/*      */ import net.minecraft.world.entity.player.PlayerModelPart;
/*      */ import org.slf4j.Logger;
/*      */ 
/*      */ public class Options {
/*   81 */   private static final Logger LOGGER = LogUtils.getLogger();
/*   82 */   private static final Gson GSON = new Gson();
/*   83 */   private static final TypeToken<List<String>> LIST_OF_STRINGS_TYPE = new TypeToken<List<String>>() {
/*      */     
/*      */     };
/*      */   public static final int RENDER_DISTANCE_SHORT = 4;
/*      */   public static final int RENDER_DISTANCE_FAR = 12;
/*      */   public static final int RENDER_DISTANCE_REALLY_FAR = 16;
/*      */   public static final int RENDER_DISTANCE_EXTREME = 32;
/*   90 */   private static final Splitter OPTION_SPLITTER = Splitter.on(':').limit(2);
/*      */   public static final String DEFAULT_SOUND_DEVICE = "";
/*      */   
/*      */   private static void operateOnLevelRenderer(Consumer<LevelRenderer> consumer) {
/*   94 */     LevelRenderer levelRenderer = (Minecraft.getInstance()).levelRenderer;
/*   95 */     if (levelRenderer != null) {
/*   96 */       consumer.accept(levelRenderer);
/*      */     }
/*      */   }
/*      */   
/*  100 */   private static final Component ACCESSIBILITY_TOOLTIP_DARK_MOJANG_BACKGROUND = (Component)Component.translatable("options.darkMojangStudiosBackgroundColor.tooltip");
/*  101 */   private final OptionInstance<Boolean> darkMojangStudiosBackground = OptionInstance.createBoolean("options.darkMojangStudiosBackgroundColor", 
/*      */       
/*  103 */       OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_DARK_MOJANG_BACKGROUND), false);
/*      */ 
/*      */ 
/*      */   
/*      */   public OptionInstance<Boolean> darkMojangStudiosBackground() {
/*  108 */     return this.darkMojangStudiosBackground;
/*      */   }
/*      */   
/*  111 */   private static final Component ACCESSIBILITY_TOOLTIP_HIDE_LIGHTNING_FLASHES = (Component)Component.translatable("options.hideLightningFlashes.tooltip");
/*  112 */   private final OptionInstance<Boolean> hideLightningFlash = OptionInstance.createBoolean("options.hideLightningFlashes", 
/*      */       
/*  114 */       OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_HIDE_LIGHTNING_FLASHES), false);
/*      */ 
/*      */ 
/*      */   
/*      */   public OptionInstance<Boolean> hideLightningFlash() {
/*  119 */     return this.hideLightningFlash;
/*      */   }
/*      */   
/*  122 */   private static final Component ACCESSIBILITY_TOOLTIP_HIDE_SPLASH_TEXTS = (Component)Component.translatable("options.hideSplashTexts.tooltip");
/*  123 */   private final OptionInstance<Boolean> hideSplashTexts = OptionInstance.createBoolean("options.hideSplashTexts", 
/*      */       
/*  125 */       OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_HIDE_SPLASH_TEXTS), false); private final OptionInstance<Double> sensitivity; private final OptionInstance<Integer> renderDistance; private final OptionInstance<Integer> simulationDistance; private int serverRenderDistance; private final OptionInstance<Double> entityDistanceScaling; public static final int UNLIMITED_FRAMERATE_CUTOFF = 260; private final OptionInstance<Integer> framerateLimit;
/*      */   private boolean isApplyingGraphicsPreset;
/*      */   private final OptionInstance<GraphicsPreset> graphicsPreset;
/*      */   
/*      */   public OptionInstance<Boolean> hideSplashTexts() {
/*  130 */     return this.hideSplashTexts;
/*      */   }
/*      */   
/*  133 */   public Options(Minecraft minecraft, File workingDirectory) { this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  146 */       .sensitivity = new OptionInstance<>("options.sensitivity", OptionInstance.noTooltip(), (caption, value) -> (value == 0.0D) ? genericValueLabel(caption, (Component)Component.translatable("options.sensitivity.min")) : ((value == 1.0D) ? genericValueLabel(caption, (Component)Component.translatable("options.sensitivity.max")) : percentValueLabel(caption, 2.0D * value)), OptionInstance.UnitDouble.INSTANCE, 0.5D, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  166 */     this.serverRenderDistance = 0;
/*      */     
/*  168 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  174 */       .entityDistanceScaling = new OptionInstance<>("options.entityDistanceScaling", OptionInstance.noTooltip(), Options::percentValueLabel, new OptionInstance.IntRange(2, 20).xmap(value -> value / 4.0D, value -> (int)(value * 4.0D), true), Codec.doubleRange(0.5D, 5.0D), 1.0D, value -> setGraphicsPresetToCustom());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  184 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  196 */       .framerateLimit = new OptionInstance<>("options.framerateLimit", OptionInstance.noTooltip(), (caption, value) -> (value == 260) ? genericValueLabel(caption, (Component)Component.translatable("options.framerateLimit.max")) : genericValueLabel(caption, (Component)Component.translatable("options.framerate", new Object[] { value })), new OptionInstance.IntRange(1, 26).xmap(value -> value * 10, value -> value / 10, true), Codec.intRange(10, 260), 120, value -> Minecraft.getInstance().getFramerateLimitTracker().setFramerateLimit(value));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  206 */     this
/*      */ 
/*      */ 
/*      */       
/*  210 */       .graphicsPreset = new OptionInstance<>("options.graphics.preset", OptionInstance.cachedConstantTooltip((Component)Component.translatable("options.graphics.preset.tooltip")), (caption, value) -> genericValueLabel(caption, (Component)Component.translatable(value.getKey())), new OptionInstance.SliderableEnum<>(List.of(GraphicsPreset.values()), GraphicsPreset.CODEC), GraphicsPreset.CODEC, GraphicsPreset.FANCY, this::applyGraphicsPreset);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  229 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  236 */       .inactivityFpsLimit = new OptionInstance<>("options.inactivityFpsLimit", value -> { switch (value) { default: throw new MatchException(null, null);case MINIMIZED: case AFK: break; }  return Tooltip.create(INACTIVITY_FPS_LIMIT_TOOLTIP_AFK); }, (caption, value) -> value.caption(), new OptionInstance.Enum<>(Arrays.asList(InactivityFpsLimit.values()), InactivityFpsLimit.CODEC), InactivityFpsLimit.AFK, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  247 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  252 */       .cloudStatus = new OptionInstance<>("options.renderClouds", OptionInstance.noTooltip(), (caption, value) -> value.caption(), new OptionInstance.Enum<>(Arrays.asList(CloudStatus.values()), Codec.withAlternative(CloudStatus.CODEC, (Codec)Codec.BOOL, b -> b ? CloudStatus.FANCY : CloudStatus.OFF)), CloudStatus.FANCY, value -> setGraphicsPresetToCustom());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  266 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  275 */       .cloudRange = new OptionInstance<>("options.renderCloudsDistance", OptionInstance.noTooltip(), (caption, value) -> genericValueLabel(caption, (Component)Component.translatable("options.chunks", new Object[] { value })), new OptionInstance.IntRange(2, 128, true), 128, value -> {
/*      */           operateOnLevelRenderer(());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           setGraphicsPresetToCustom();
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  288 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  297 */       .weatherRadius = new OptionInstance<>("options.weatherRadius", OptionInstance.cachedConstantTooltip(GRAPHICS_TOOLTIP_WEATHER_RADIUS), (caption, value) -> genericValueLabel(caption, (Component)Component.translatable("options.blocks", new Object[] { value })), new OptionInstance.IntRange(3, 10, true), 10, ignored -> setGraphicsPresetToCustom());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  307 */     this.cutoutLeaves = OptionInstance.createBoolean("options.cutoutLeaves", 
/*      */         
/*  309 */         OptionInstance.cachedConstantTooltip(GRAPHICS_TOOLTIP_CUTOUT_LEAVES), true, ignored -> {
/*      */           operateOnLevelRenderer(LevelRenderer::allChanged);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           setGraphicsPresetToCustom();
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  323 */     this.vignette = OptionInstance.createBoolean("options.vignette", 
/*      */         
/*  325 */         OptionInstance.cachedConstantTooltip(GRAPHICS_TOOLTIP_VIGNETTE), true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  335 */     this.improvedTransparency = OptionInstance.createBoolean("options.improvedTransparency", 
/*      */         
/*  337 */         OptionInstance.cachedConstantTooltip(GRAPHICS_TOOLTIP_IMPROVED_TRANSPARENCY), false, value -> {
/*      */           Minecraft minecraft = Minecraft.getInstance();
/*      */ 
/*      */           
/*      */           GpuWarnlistManager gpuWarnlistManager = minecraft.getGpuWarnlistManager();
/*      */ 
/*      */           
/*      */           if (value && gpuWarnlistManager.willShowWarning()) {
/*      */             gpuWarnlistManager.showWarning();
/*      */ 
/*      */             
/*      */             return;
/*      */           } 
/*      */ 
/*      */           
/*      */           operateOnLevelRenderer(LevelRenderer::allChanged);
/*      */ 
/*      */           
/*      */           setGraphicsPresetToCustom();
/*      */         });
/*      */ 
/*      */     
/*  359 */     this.ambientOcclusion = OptionInstance.createBoolean("options.ao", true, value -> {
/*      */           operateOnLevelRenderer(LevelRenderer::allChanged);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           setGraphicsPresetToCustom();
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  374 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  385 */       .chunkSectionFadeInTime = new OptionInstance<>("options.chunkFade", OptionInstance.cachedConstantTooltip(GRAPHICS_TOOLTIP_CHUNK_FADE), (caption, value) -> (value <= 0.0D) ? Component.translatable("options.chunkFade.none") : Component.translatable("options.chunkFade.seconds", new Object[] { String.format(Locale.ROOT, "%.2f", new Object[] { value }) }), new OptionInstance.IntRange(0, 40).xmap(value -> value / 20.0D, value -> (int)(value * 20.0D), true), Codec.doubleRange(0.0D, 2.0D), 0.75D, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  397 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  405 */       .prioritizeChunkUpdates = new OptionInstance<>("options.prioritizeChunkUpdates", value -> { switch (value) { default: throw new MatchException(null, null);case NONE: case PLAYER_AFFECTED: case NEARBY: break; }  return Tooltip.create(PRIORITIZE_CHUNK_TOOLTIP_NEARBY); }, (caption, value) -> value.caption(), new OptionInstance.Enum<>(Arrays.asList(PrioritizeChunkUpdates.values()), PrioritizeChunkUpdates.LEGACY_CODEC), PrioritizeChunkUpdates.NONE, value -> setGraphicsPresetToCustom());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  414 */     this.resourcePacks = Lists.newArrayList();
/*  415 */     this.incompatibleResourcePacks = Lists.newArrayList();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  436 */     this
/*      */ 
/*      */ 
/*      */       
/*  440 */       .chatVisibility = new OptionInstance<>("options.chat.visibility", OptionInstance.noTooltip(), (caption, value) -> value.caption(), new OptionInstance.Enum<>(Arrays.asList(ChatVisiblity.values()), ChatVisiblity.LEGACY_CODEC), ChatVisiblity.FULL, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  449 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  454 */       .chatOpacity = new OptionInstance<>("options.chat.opacity", OptionInstance.noTooltip(), (caption, value) -> percentValueLabel(caption, value * 0.9D + 0.1D), OptionInstance.UnitDouble.INSTANCE, 1.0D, value -> (Minecraft.getInstance()).gui.getChat().rescaleChat());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  462 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  467 */       .chatLineSpacing = new OptionInstance<>("options.chat.line_spacing", OptionInstance.noTooltip(), Options::percentValueLabel, OptionInstance.UnitDouble.INSTANCE, 0.0D, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  479 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  487 */       .menuBackgroundBlurriness = new OptionInstance<>("options.accessibility.menu_background_blurriness", OptionInstance.cachedConstantTooltip(MENU_BACKGROUND_BLURRINESS_TOOLTIP), Options::genericValueOrOffLabel, new OptionInstance.IntRange(0, 10), 5, value -> setGraphicsPresetToCustom());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  500 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  505 */       .textBackgroundOpacity = new OptionInstance<>("options.accessibility.text_background_opacity", OptionInstance.noTooltip(), Options::percentValueLabel, OptionInstance.UnitDouble.INSTANCE, 0.5D, value -> (Minecraft.getInstance()).gui.getChat().rescaleChat());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  513 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  518 */       .panoramaSpeed = new OptionInstance<>("options.accessibility.panorama_speed", OptionInstance.noTooltip(), Options::percentValueLabel, OptionInstance.UnitDouble.INSTANCE, 1.0D, v -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  527 */     this.highContrast = OptionInstance.createBoolean("options.accessibility.high_contrast", 
/*      */         
/*  529 */         OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_CONTRAST_MODE), false, value -> {
/*      */           PackRepository packRepo = Minecraft.getInstance().getResourcePackRepository();
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           boolean isSelected = packRepo.getSelectedIds().contains("high_contrast");
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           if (!isSelected && value) {
/*      */             if (packRepo.addPack("high_contrast")) {
/*      */               updateResourcePacks(packRepo);
/*      */             }
/*      */           } else if (isSelected && !value && packRepo.removePack("high_contrast")) {
/*      */             updateResourcePacks(packRepo);
/*      */           } 
/*      */         });
/*      */ 
/*      */ 
/*      */     
/*  551 */     this.highContrastBlockOutline = OptionInstance.createBoolean("options.accessibility.high_contrast_block_outline", 
/*      */         
/*  553 */         OptionInstance.cachedConstantTooltip(HIGH_CONTRAST_BLOCK_OUTLINE_TOOLTIP), false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  560 */     this.narratorHotkey = OptionInstance.createBoolean("options.accessibility.narrator_hotkey", 
/*      */         
/*  562 */         OptionInstance.cachedConstantTooltip(
/*  563 */           InputQuirks.REPLACE_CTRL_KEY_WITH_CMD_KEY ? 
/*  564 */           (Component)Component.translatable("options.accessibility.narrator_hotkey.mac.tooltip") : 
/*  565 */           (Component)Component.translatable("options.accessibility.narrator_hotkey.tooltip")), true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  577 */     this.pauseOnLostFocus = true;
/*  578 */     this.modelParts = EnumSet.allOf(PlayerModelPart.class);
/*  579 */     this
/*      */ 
/*      */ 
/*      */       
/*  583 */       .mainHand = new OptionInstance<>("options.mainHand", OptionInstance.noTooltip(), (caption, value) -> value.caption(), new OptionInstance.Enum<>(Arrays.asList(HumanoidArm.values()), HumanoidArm.CODEC), HumanoidArm.RIGHT, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  595 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  605 */       .chatScale = new OptionInstance<>("options.chat.scale", OptionInstance.noTooltip(), (caption, value) -> (value == 0.0D) ? CommonComponents.optionStatus(caption, false) : percentValueLabel(caption, value), OptionInstance.UnitDouble.INSTANCE, 1.0D, value -> (Minecraft.getInstance()).gui.getChat().rescaleChat());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  613 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  618 */       .chatWidth = new OptionInstance<>("options.chat.width", OptionInstance.noTooltip(), (caption, value) -> pixelValueLabel(caption, ChatComponent.getWidth(value)), OptionInstance.UnitDouble.INSTANCE, 1.0D, value -> (Minecraft.getInstance()).gui.getChat().rescaleChat());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  626 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  631 */       .chatHeightUnfocused = new OptionInstance<>("options.chat.height.unfocused", OptionInstance.noTooltip(), (caption, value) -> pixelValueLabel(caption, ChatComponent.getHeight(value)), OptionInstance.UnitDouble.INSTANCE, ChatComponent.defaultUnfocusedPct(), value -> (Minecraft.getInstance()).gui.getChat().rescaleChat());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  639 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  644 */       .chatHeightFocused = new OptionInstance<>("options.chat.height.focused", OptionInstance.noTooltip(), (caption, value) -> pixelValueLabel(caption, ChatComponent.getHeight(value)), OptionInstance.UnitDouble.INSTANCE, 1.0D, value -> (Minecraft.getInstance()).gui.getChat().rescaleChat());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  652 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  663 */       .chatDelay = new OptionInstance<>("options.chat.delay_instant", OptionInstance.noTooltip(), (caption, value) -> (value <= 0.0D) ? Component.translatable("options.chat.delay_none") : Component.translatable("options.chat.delay", new Object[] { String.format(Locale.ROOT, "%.1f", new Object[] { value }) }), new OptionInstance.IntRange(0, 60).xmap(value -> value / 10.0D, value -> (int)(value * 10.0D), true), Codec.doubleRange(0.0D, 6.0D), 0.0D, value -> Minecraft.getInstance().getChatListener().setMessageDelay(value));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  672 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  678 */       .notificationDisplayTime = new OptionInstance<>("options.notifications.display_time", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_NOTIFICATION_DISPLAY_TIME), (caption, value) -> genericValueLabel(caption, (Component)Component.translatable("options.multiplier", new Object[] { value })), new OptionInstance.IntRange(5, 100).xmap(value -> value / 10.0D, value -> (int)(value * 10.0D), true), Codec.doubleRange(0.5D, 10.0D), 1.0D, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  686 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  696 */       .mipmapLevels = new OptionInstance<>("options.mipmapLevels", OptionInstance.noTooltip(), (caption, value) -> (value == 0) ? CommonComponents.optionStatus(caption, false) : genericValueLabel(caption, value), new OptionInstance.IntRange(0, 4), 4, value -> setGraphicsPresetToCustom());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  706 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  716 */       .maxAnisotropyBit = new OptionInstance<>("options.maxAnisotropy", OptionInstance.cachedConstantTooltip(GRAPHICS_TOOLTIP_ANISOTROPIC_FILTERING), (caption, value) -> (value == 0) ? CommonComponents.optionStatus(caption, false) : genericValueLabel(caption, (Component)Component.translatable("options.multiplier", new Object[] { Integer.toString(1 << value) })), new OptionInstance.IntRange(1, 3), 2, value -> {
/*      */           setGraphicsPresetToCustom();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           operateOnLevelRenderer(LevelRenderer::resetSampler);
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  735 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  743 */       .textureFiltering = new OptionInstance<>("options.textureFiltering", value -> { switch (value) { default: throw new MatchException(null, null);case NONE: case RGSS: case ANISOTROPIC: break; }  return Tooltip.create(FILTERING_ANISOTROPIC_TOOLTIP); }, (caption, value) -> value.caption(), new OptionInstance.Enum<>(Arrays.asList(TextureFilteringMethod.values()), TextureFilteringMethod.LEGACY_CODEC), TextureFilteringMethod.NONE, value -> {
/*      */           setGraphicsPresetToCustom();
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           operateOnLevelRenderer(LevelRenderer::resetSampler);
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  755 */     this.useNativeTransport = true;
/*      */     
/*  757 */     this
/*      */ 
/*      */ 
/*      */       
/*  761 */       .attackIndicator = new OptionInstance<>("options.attackIndicator", OptionInstance.noTooltip(), (caption, value) -> value.caption(), new OptionInstance.Enum<>(Arrays.asList(AttackIndicatorStatus.values()), AttackIndicatorStatus.LEGACY_CODEC), AttackIndicatorStatus.CROSSHAIR, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  770 */     this.tutorialStep = TutorialSteps.MOVEMENT;
/*  771 */     this.joinedFirstServer = false;
/*      */     
/*  773 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  781 */       .biomeBlendRadius = new OptionInstance<>("options.biomeBlendRadius", OptionInstance.noTooltip(), (caption, value) -> {
/*      */           int dist = value * 2 + 1;
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           return genericValueLabel(caption, (Component)Component.translatable("options.biomeBlendRadius." + dist));
/*      */         }, new OptionInstance.IntRange(0, 7, false), 2, value -> {
/*      */           operateOnLevelRenderer(LevelRenderer::allChanged);
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           setGraphicsPresetToCustom();
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  800 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  806 */       .mouseWheelSensitivity = new OptionInstance<>("options.mouseWheelSensitivity", OptionInstance.noTooltip(), (caption, value) -> genericValueLabel(caption, (Component)Component.literal(String.format(Locale.ROOT, "%.2f", new Object[] { value }))), new OptionInstance.IntRange(-200, 100).xmap(Options::logMouse, Options::unlogMouse, false), Codec.doubleRange(logMouse(-200), logMouse(100)), logMouse(0), value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  814 */     this.rawMouseInput = OptionInstance.createBoolean("options.rawMouseInput", true, value -> {
/*      */           Window window = Minecraft.getInstance().getWindow();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           if (window != null) {
/*      */             window.updateRawMouseInput(value);
/*      */           }
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  830 */     this.allowCursorChanges = OptionInstance.createBoolean("options.allowCursorChanges", 
/*      */         
/*  832 */         OptionInstance.cachedConstantTooltip(ALLOW_CURSOR_CHANGES_TOOLTIP), true, value -> {
/*      */           Window window = Minecraft.getInstance().getWindow();
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           if (window != null) {
/*      */             window.setAllowCursorChanges(value);
/*      */           }
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  846 */     this.glDebugVerbosity = 1;
/*      */     
/*  848 */     this.autoJump = OptionInstance.createBoolean("options.autoJump", false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  855 */     this.rotateWithMinecart = OptionInstance.createBoolean("options.rotateWithMinecart", 
/*      */         
/*  857 */         OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_ROTATE_WITH_MINECART), false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  865 */     this.operatorItemsTab = OptionInstance.createBoolean("options.operatorItemsTab", false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  871 */     this.autoSuggestions = OptionInstance.createBoolean("options.autoSuggestCommands", true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  877 */     this.chatColors = OptionInstance.createBoolean("options.chat.color", true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  883 */     this.chatLinks = OptionInstance.createBoolean("options.chat.links", true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  889 */     this.chatLinksPrompt = OptionInstance.createBoolean("options.chat.links.prompt", true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  895 */     this.enableVsync = OptionInstance.createBoolean("options.vsync", true, value -> {
/*      */           if (Minecraft.getInstance().getWindow() != null) {
/*      */             Minecraft.getInstance().getWindow().updateVsync(value);
/*      */           }
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  905 */     this.entityShadows = OptionInstance.createBoolean("options.entityShadows", 
/*      */         
/*  907 */         OptionInstance.noTooltip(), true, value -> setGraphicsPresetToCustom());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  926 */     this.forceUnicodeFont = OptionInstance.createBoolean("options.forceUnicodeFont", false, value -> updateFontOptions());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  937 */     this.japaneseGlyphVariants = OptionInstance.createBoolean("options.japaneseGlyphVariants", 
/*  938 */         OptionInstance.cachedConstantTooltip((Component)Component.translatable("options.japaneseGlyphVariants.tooltip")), 
/*  939 */         japaneseGlyphVariantsDefault(), value -> updateFontOptions());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  947 */     this.invertXMouse = OptionInstance.createBoolean("options.invertMouseX", false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  953 */     this.invertYMouse = OptionInstance.createBoolean("options.invertMouseY", false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  959 */     this.discreteMouseScroll = OptionInstance.createBoolean("options.discrete_mouse_scroll", false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  967 */     this.realmsNotifications = OptionInstance.createBoolean("options.realmsNotifications", 
/*  968 */         OptionInstance.cachedConstantTooltip(REALMS_NOTIFICATIONS_TOOLTIP), true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  977 */     this.allowServerListing = OptionInstance.createBoolean("options.allowServerListing", 
/*      */         
/*  979 */         OptionInstance.cachedConstantTooltip(ALLOW_SERVER_LISTING_TOOLTIP), true, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  988 */     this.reducedDebugInfo = OptionInstance.createBoolean("options.reducedDebugInfo", OptionInstance.noTooltip(), false, ignored -> (Minecraft.getInstance()).debugEntries.rebuildCurrentList());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  994 */     this.soundSourceVolumes = Util.makeEnumMap(SoundSource.class, source -> createSoundSliderOptionInstance("soundCategory." + source.getName(), source));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1036 */     this.showSubtitles = OptionInstance.createBoolean("options.showSubtitles", 
/* 1037 */         OptionInstance.cachedConstantTooltip(CLOSED_CAPTIONS_TOOLTIP), false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1047 */     this.directionalAudio = OptionInstance.createBoolean("options.directionalAudio", value -> value ? Tooltip.create(DIRECTIONAL_AUDIO_TOOLTIP_ON) : Tooltip.create(DIRECTIONAL_AUDIO_TOOLTIP_OFF), false, value -> {
/*      */           SoundManager soundManager = Minecraft.getInstance().getSoundManager();
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           soundManager.reload();
/*      */ 
/*      */ 
/*      */           
/*      */           soundManager.play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/*      */         });
/*      */ 
/*      */ 
/*      */     
/* 1062 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1067 */       .backgroundForChatOnly = new OptionInstance<>("options.accessibility.text_background", OptionInstance.noTooltip(), (caption, value) -> value ? (Component)Component.translatable("options.accessibility.text_background.chat") : (Component)Component.translatable("options.accessibility.text_background.everywhere"), OptionInstance.BOOLEAN_VALUES, true, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1075 */     this.touchscreen = OptionInstance.createBoolean("options.touchscreen", false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1081 */     this.fullscreen = OptionInstance.createBoolean("options.fullscreen", false, value -> {
/*      */           Minecraft minecraft = Minecraft.getInstance();
/*      */ 
/*      */           
/*      */           if (minecraft.getWindow() != null && minecraft.getWindow().isFullscreen() != value) {
/*      */             minecraft.getWindow().toggleFullScreen();
/*      */ 
/*      */             
/*      */             fullscreen().set(minecraft.getWindow().isFullscreen());
/*      */           } 
/*      */         });
/*      */     
/* 1093 */     this.bobView = OptionInstance.createBoolean("options.viewBobbing", true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1102 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1107 */       .toggleCrouch = new OptionInstance<>("key.sneak", OptionInstance.noTooltip(), (caption, value) -> value ? KEY_TOGGLE : KEY_HOLD, OptionInstance.BOOLEAN_VALUES, false, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1115 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1120 */       .toggleSprint = new OptionInstance<>("key.sprint", OptionInstance.noTooltip(), (caption, value) -> value ? KEY_TOGGLE : KEY_HOLD, OptionInstance.BOOLEAN_VALUES, false, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1128 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1133 */       .toggleAttack = new OptionInstance<>("key.attack", OptionInstance.noTooltip(), (caption, value) -> value ? KEY_TOGGLE : KEY_HOLD, OptionInstance.BOOLEAN_VALUES, false, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1141 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1146 */       .toggleUse = new OptionInstance<>("key.use", OptionInstance.noTooltip(), (caption, value) -> value ? KEY_TOGGLE : KEY_HOLD, OptionInstance.BOOLEAN_VALUES, false, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1156 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1166 */       .sprintWindow = new OptionInstance<>("options.sprintWindow", OptionInstance.cachedConstantTooltip(SPRINT_WINDOW_TOOLTIP), (caption, value) -> (value == 0) ? genericValueLabel(caption, (Component)Component.translatable("options.off")) : genericValueLabel(caption, (Component)Component.translatable("options.value", new Object[] { value })), new OptionInstance.IntRange(0, 10), 7, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1178 */     this.hideMatchedNames = OptionInstance.createBoolean("options.hideMatchedNames", 
/*      */         
/* 1180 */         OptionInstance.cachedConstantTooltip(CHAT_TOOLTIP_HIDE_MATCHED_NAMES), true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1188 */     this.showAutosaveIndicator = OptionInstance.createBoolean("options.autosaveIndicator", true);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1196 */     this.onlyShowSecureChat = OptionInstance.createBoolean("options.onlyShowSecureChat", 
/*      */         
/* 1198 */         OptionInstance.cachedConstantTooltip(CHAT_TOOLTIP_ONLY_SHOW_SECURE), false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1208 */     this.saveChatDrafts = OptionInstance.createBoolean("options.chat.drafts", 
/*      */         
/* 1210 */         OptionInstance.cachedConstantTooltip(CHAT_TOOLTIP_SAVE_DRAFTS), false);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1228 */     this.keyUp = new KeyMapping("key.forward", 87, KeyMapping.Category.MOVEMENT);
/* 1229 */     this.keyLeft = new KeyMapping("key.left", 65, KeyMapping.Category.MOVEMENT);
/* 1230 */     this.keyDown = new KeyMapping("key.back", 83, KeyMapping.Category.MOVEMENT);
/* 1231 */     this.keyRight = new KeyMapping("key.right", 68, KeyMapping.Category.MOVEMENT);
/* 1232 */     this.keyJump = new KeyMapping("key.jump", 32, KeyMapping.Category.MOVEMENT);
/*      */ 
/*      */     
/* 1235 */     Objects.requireNonNull(this.toggleCrouch); this.keyShift = new ToggleKeyMapping("key.sneak", 340, KeyMapping.Category.MOVEMENT, this.toggleCrouch::get, true);
/* 1236 */     Objects.requireNonNull(this.toggleSprint); this.keySprint = new ToggleKeyMapping("key.sprint", 341, KeyMapping.Category.MOVEMENT, this.toggleSprint::get, true);
/*      */     
/* 1238 */     this.keyInventory = new KeyMapping("key.inventory", 69, KeyMapping.Category.INVENTORY);
/* 1239 */     this.keySwapOffhand = new KeyMapping("key.swapOffhand", 70, KeyMapping.Category.INVENTORY);
/* 1240 */     this.keyDrop = new KeyMapping("key.drop", 81, KeyMapping.Category.INVENTORY);
/*      */     
/* 1242 */     Objects.requireNonNull(this.toggleUse); this.keyUse = new ToggleKeyMapping("key.use", InputConstants.Type.MOUSE, 1, KeyMapping.Category.GAMEPLAY, this.toggleUse::get, false);
/* 1243 */     Objects.requireNonNull(this.toggleAttack); this.keyAttack = new ToggleKeyMapping("key.attack", InputConstants.Type.MOUSE, 0, KeyMapping.Category.GAMEPLAY, this.toggleAttack::get, true);
/* 1244 */     this.keyPickItem = new KeyMapping("key.pickItem", InputConstants.Type.MOUSE, 2, KeyMapping.Category.GAMEPLAY);
/*      */     
/* 1246 */     this.keyChat = new KeyMapping("key.chat", 84, KeyMapping.Category.MULTIPLAYER);
/* 1247 */     this.keyPlayerList = new KeyMapping("key.playerlist", 258, KeyMapping.Category.MULTIPLAYER);
/* 1248 */     this.keyCommand = new KeyMapping("key.command", 47, KeyMapping.Category.MULTIPLAYER);
/* 1249 */     this.keySocialInteractions = new KeyMapping("key.socialInteractions", 80, KeyMapping.Category.MULTIPLAYER);
/*      */     
/* 1251 */     this.keyScreenshot = new KeyMapping("key.screenshot", 291, KeyMapping.Category.MISC);
/* 1252 */     this.keyTogglePerspective = new KeyMapping("key.togglePerspective", 294, KeyMapping.Category.MISC);
/* 1253 */     this.keySmoothCamera = new KeyMapping("key.smoothCamera", InputConstants.UNKNOWN.getValue(), KeyMapping.Category.MISC);
/* 1254 */     this.keyFullscreen = new KeyMapping("key.fullscreen", 300, KeyMapping.Category.MISC);
/* 1255 */     this.keyAdvancements = new KeyMapping("key.advancements", 76, KeyMapping.Category.MISC);
/* 1256 */     this.keyQuickActions = new KeyMapping("key.quickActions", 71, KeyMapping.Category.MISC);
/* 1257 */     this.keyToggleGui = new KeyMapping("key.toggleGui", 290, KeyMapping.Category.MISC);
/* 1258 */     this.keyToggleSpectatorShaderEffects = new KeyMapping("key.toggleSpectatorShaderEffects", 293, KeyMapping.Category.MISC);
/*      */     
/* 1260 */     this.keyHotbarSlots = new KeyMapping[] { new KeyMapping("key.hotbar.1", 49, KeyMapping.Category.INVENTORY), new KeyMapping("key.hotbar.2", 50, KeyMapping.Category.INVENTORY), new KeyMapping("key.hotbar.3", 51, KeyMapping.Category.INVENTORY), new KeyMapping("key.hotbar.4", 52, KeyMapping.Category.INVENTORY), new KeyMapping("key.hotbar.5", 53, KeyMapping.Category.INVENTORY), new KeyMapping("key.hotbar.6", 54, KeyMapping.Category.INVENTORY), new KeyMapping("key.hotbar.7", 55, KeyMapping.Category.INVENTORY), new KeyMapping("key.hotbar.8", 56, KeyMapping.Category.INVENTORY), new KeyMapping("key.hotbar.9", 57, KeyMapping.Category.INVENTORY) };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1271 */     this.keySaveHotbarActivator = new KeyMapping("key.saveToolbarActivator", 67, KeyMapping.Category.CREATIVE);
/* 1272 */     this.keyLoadHotbarActivator = new KeyMapping("key.loadToolbarActivator", 88, KeyMapping.Category.CREATIVE);
/*      */     
/* 1274 */     this.keySpectatorOutlines = new KeyMapping("key.spectatorOutlines", InputConstants.UNKNOWN.getValue(), KeyMapping.Category.SPECTATOR);
/* 1275 */     this.keySpectatorHotbar = new KeyMapping("key.spectatorHotbar", InputConstants.Type.MOUSE, 2, KeyMapping.Category.SPECTATOR);
/*      */     
/* 1277 */     this.keyDebugOverlay = new KeyMapping("key.debug.overlay", InputConstants.Type.KEYSYM, 292, KeyMapping.Category.DEBUG, -2);
/* 1278 */     this.keyDebugModifier = new KeyMapping("key.debug.modifier", InputConstants.Type.KEYSYM, 292, KeyMapping.Category.DEBUG, -1);
/* 1279 */     this.keyDebugCrash = new KeyMapping("key.debug.crash", InputConstants.Type.KEYSYM, 67, KeyMapping.Category.DEBUG);
/* 1280 */     this.keyDebugReloadChunk = new KeyMapping("key.debug.reloadChunk", InputConstants.Type.KEYSYM, 65, KeyMapping.Category.DEBUG);
/* 1281 */     this.keyDebugShowHitboxes = new KeyMapping("key.debug.showHitboxes", InputConstants.Type.KEYSYM, 66, KeyMapping.Category.DEBUG);
/* 1282 */     this.keyDebugClearChat = new KeyMapping("key.debug.clearChat", InputConstants.Type.KEYSYM, 68, KeyMapping.Category.DEBUG);
/* 1283 */     this.keyDebugShowChunkBorders = new KeyMapping("key.debug.showChunkBorders", InputConstants.Type.KEYSYM, 71, KeyMapping.Category.DEBUG);
/* 1284 */     this.keyDebugShowAdvancedTooltips = new KeyMapping("key.debug.showAdvancedTooltips", InputConstants.Type.KEYSYM, 72, KeyMapping.Category.DEBUG);
/* 1285 */     this.keyDebugCopyRecreateCommand = new KeyMapping("key.debug.copyRecreateCommand", InputConstants.Type.KEYSYM, 73, KeyMapping.Category.DEBUG);
/* 1286 */     this.keyDebugSpectate = new KeyMapping("key.debug.spectate", InputConstants.Type.KEYSYM, 78, KeyMapping.Category.DEBUG);
/* 1287 */     this.keyDebugSwitchGameMode = new KeyMapping("key.debug.switchGameMode", InputConstants.Type.KEYSYM, 293, KeyMapping.Category.DEBUG);
/* 1288 */     this.keyDebugDebugOptions = new KeyMapping("key.debug.debugOptions", InputConstants.Type.KEYSYM, 295, KeyMapping.Category.DEBUG);
/* 1289 */     this.keyDebugFocusPause = new KeyMapping("key.debug.focusPause", InputConstants.Type.KEYSYM, 80, KeyMapping.Category.DEBUG);
/* 1290 */     this.keyDebugDumpDynamicTextures = new KeyMapping("key.debug.dumpDynamicTextures", InputConstants.Type.KEYSYM, 83, KeyMapping.Category.DEBUG);
/* 1291 */     this.keyDebugReloadResourcePacks = new KeyMapping("key.debug.reloadResourcePacks", InputConstants.Type.KEYSYM, 84, KeyMapping.Category.DEBUG);
/* 1292 */     this.keyDebugProfiling = new KeyMapping("key.debug.profiling", InputConstants.Type.KEYSYM, 76, KeyMapping.Category.DEBUG);
/* 1293 */     this.keyDebugCopyLocation = new KeyMapping("key.debug.copyLocation", InputConstants.Type.KEYSYM, 67, KeyMapping.Category.DEBUG);
/* 1294 */     this.keyDebugDumpVersion = new KeyMapping("key.debug.dumpVersion", InputConstants.Type.KEYSYM, 86, KeyMapping.Category.DEBUG);
/* 1295 */     this.keyDebugPofilingChart = new KeyMapping("key.debug.profilingChart", InputConstants.Type.KEYSYM, 49, KeyMapping.Category.DEBUG, 1);
/* 1296 */     this.keyDebugFpsCharts = new KeyMapping("key.debug.fpsCharts", InputConstants.Type.KEYSYM, 50, KeyMapping.Category.DEBUG, 2);
/* 1297 */     this.keyDebugNetworkCharts = new KeyMapping("key.debug.networkCharts", InputConstants.Type.KEYSYM, 51, KeyMapping.Category.DEBUG, 3);
/*      */     
/* 1299 */     this.debugKeys = new KeyMapping[] { this.keyDebugReloadChunk, this.keyDebugShowHitboxes, this.keyDebugClearChat, this.keyDebugCrash, this.keyDebugShowChunkBorders, this.keyDebugShowAdvancedTooltips, this.keyDebugCopyRecreateCommand, this.keyDebugSpectate, this.keyDebugSwitchGameMode, this.keyDebugDebugOptions, this.keyDebugFocusPause, this.keyDebugDumpDynamicTextures, this.keyDebugReloadResourcePacks, this.keyDebugProfiling, this.keyDebugCopyLocation, this.keyDebugDumpVersion, this.keyDebugPofilingChart, this.keyDebugFpsCharts, this.keyDebugNetworkCharts };
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1308 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1317 */       .keyMappings = (KeyMapping[])Stream.<KeyMapping[]>of(new KeyMapping[][] { { this.keyAttack, this.keyUse, this.keyUp, this.keyLeft, this.keyDown, this.keyRight, this.keyJump, this.keyShift, this.keySprint, this.keyDrop, this.keyInventory, this.keyChat, this.keyPlayerList, this.keyPickItem, this.keyCommand, this.keySocialInteractions, this.keyToggleGui, this.keyToggleSpectatorShaderEffects, this.keyScreenshot, this.keyTogglePerspective, this.keySmoothCamera, this.keyFullscreen, this.keySpectatorOutlines, this.keySpectatorHotbar, this.keySwapOffhand, this.keySaveHotbarActivator, this.keyLoadHotbarActivator, this.keyAdvancements, this.keyQuickActions, this.keyDebugOverlay, this.keyDebugModifier }, this.keyHotbarSlots, this.debugKeys }).flatMap(Stream::of).toArray(x$0 -> new KeyMapping[x$0]);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1323 */     this.cameraType = CameraType.FIRST_PERSON;
/* 1324 */     this.lastMpIp = "";
/*      */ 
/*      */ 
/*      */     
/* 1328 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1338 */       .fov = new OptionInstance<>("options.fov", OptionInstance.noTooltip(), (caption, value) -> {
/*      */           switch (value) {
/*      */             case 70:
/*      */             
/*      */             
/*      */             case 110:
/*      */             
/*      */             
/*      */             default:
/*      */               break;
/*      */           } 
/*      */           return genericValueLabel(caption, value);
/*      */         }, new OptionInstance.IntRange(30, 110), Codec.DOUBLE.xmap(value -> (int)(value * 40.0D + 70.0D), value -> (value - 70.0D) / 40.0D), 70, value -> operateOnLevelRenderer(LevelRenderer::needsUpdate));
/* 1351 */     this.telemetryOptInExtra = OptionInstance.createBoolean("options.telemetry.button", 
/*      */         
/* 1353 */         OptionInstance.cachedConstantTooltip(TELEMETRY_TOOLTIP), (caption, value) -> {
/*      */           Minecraft minecraft = Minecraft.getInstance();
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1359 */           return !minecraft.allowsTelemetry() ? Component.translatable("options.telemetry.state.none") : ((value && minecraft.extraTelemetryAvailable()) ? Component.translatable("options.telemetry.state.all") : Component.translatable("options.telemetry.state.minimal"));
/*      */         }, false, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1374 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1379 */       .screenEffectScale = new OptionInstance<>("options.screenEffectScale", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_SCREEN_EFFECT), Options::percentValueOrOffLabel, OptionInstance.UnitDouble.INSTANCE, 1.0D, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1389 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1398 */       .fovEffectScale = new OptionInstance<>("options.fovEffectScale", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_FOV_EFFECT), Options::percentValueOrOffLabel, OptionInstance.UnitDouble.INSTANCE.xmap(Mth::square, Math::sqrt), Codec.doubleRange(0.0D, 1.0D), 1.0D, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1408 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1416 */       .darknessEffectScale = new OptionInstance<>("options.darknessEffectScale", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_DARKNESS_EFFECT), Options::percentValueOrOffLabel, OptionInstance.UnitDouble.INSTANCE.xmap(Mth::square, Math::sqrt), 1.0D, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1426 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1431 */       .glintSpeed = new OptionInstance<>("options.glintSpeed", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_GLINT_SPEED), Options::percentValueOrOffLabel, OptionInstance.UnitDouble.INSTANCE, 0.5D, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1441 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1446 */       .glintStrength = new OptionInstance<>("options.glintStrength", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_GLINT_STRENGTH), Options::percentValueOrOffLabel, OptionInstance.UnitDouble.INSTANCE, 0.75D, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1456 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1461 */       .damageTiltStrength = new OptionInstance<>("options.damageTiltStrength", OptionInstance.cachedConstantTooltip(ACCESSIBILITY_TOOLTIP_DAMAGE_TILT_STRENGTH), Options::percentValueOrOffLabel, OptionInstance.UnitDouble.INSTANCE, 1.0D, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1469 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1486 */       .gamma = new OptionInstance<>("options.gamma", OptionInstance.noTooltip(), (caption, value) -> {
/*      */           int progressValueToDisplay = (int)(value * 100.0D);
/*      */ 
/*      */           
/*      */           return (progressValueToDisplay == 0) ? genericValueLabel(caption, (Component)Component.translatable("options.gamma.min")) : ((progressValueToDisplay == 50) ? genericValueLabel(caption, (Component)Component.translatable("options.gamma.default")) : ((progressValueToDisplay == 100) ? genericValueLabel(caption, (Component)Component.translatable("options.gamma.max")) : genericValueLabel(caption, progressValueToDisplay)));
/*      */         }, OptionInstance.UnitDouble.INSTANCE, 0.5D, value -> {
/*      */         
/*      */         });
/*      */ 
/*      */     
/* 1496 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1507 */       .guiScale = new OptionInstance<>("options.guiScale", OptionInstance.noTooltip(), (caption, value) -> (value == 0) ? (Component)Component.translatable("options.guiScale.auto") : (Component)Component.literal(Integer.toString(value)), new OptionInstance.ClampingLazyMaxIntRange(0, () -> {
/*      */             Minecraft minecraft = Minecraft.getInstance();
/*      */ 
/*      */             
/*      */             return !minecraft.isRunning() ? 2147483646 : minecraft.getWindow().calculateScale(0, minecraft.isEnforceUnicode());
/*      */           }, 2147483646), 0, value -> this.minecraft.resizeDisplay());
/*      */ 
/*      */     
/* 1515 */     this
/*      */ 
/*      */ 
/*      */       
/* 1519 */       .particles = new OptionInstance<>("options.particles", OptionInstance.noTooltip(), (caption, value) -> value.caption(), new OptionInstance.Enum<>(Arrays.asList(ParticleStatus.values()), ParticleStatus.LEGACY_CODEC), ParticleStatus.ALL, value -> setGraphicsPresetToCustom());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1528 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1538 */       .narrator = new OptionInstance<>("options.narrator", OptionInstance.noTooltip(), (caption, value) -> this.minecraft.getNarrator().isActive() ? value.getName() : Component.translatable("options.narrator.notavailable"), new OptionInstance.Enum<>(Arrays.asList(NarratorStatus.values()), NarratorStatus.LEGACY_CODEC), NarratorStatus.OFF, value -> this.minecraft.getNarrator().updateNarratorStatus(value));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1547 */     this.languageCode = "en_us";
/* 1548 */     this
/*      */       
/* 1550 */       .soundDevice = new OptionInstance<>("options.audioDevice", OptionInstance.noTooltip(), (caption, value) -> "".equals(value) ? Component.translatable("options.audioDevice.default") : (value.startsWith("OpenAL Soft on ") ? Component.literal(value.substring(SoundEngine.OPEN_AL_SOFT_PREFIX_LENGTH)) : Component.literal(value)), new OptionInstance.LazyEnum<>(() -> Stream.concat(Stream.of(""), Minecraft.getInstance().getSoundManager().getAvailableSoundDevices().stream()).toList(), device -> 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/* 1563 */           (!Minecraft.getInstance().isRunning() || device == "" || Minecraft.getInstance().getSoundManager().getAvailableSoundDevices().contains(device)) ? Optional.of(device) : Optional.empty(), (Codec<String>)Codec.STRING), "", value -> {
/*      */           SoundManager soundManager = Minecraft.getInstance().getSoundManager();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           soundManager.reload();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */           
/*      */           soundManager.play((SoundInstance)SimpleSoundInstance.forUI((Holder)SoundEvents.UI_BUTTON_CLICK, 1.0F));
/*      */         });
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1582 */     this.onboardAccessibility = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1591 */     this
/*      */ 
/*      */ 
/*      */       
/* 1595 */       .musicFrequency = new OptionInstance<>("options.music_frequency", OptionInstance.cachedConstantTooltip(MUSIC_FREQUENCY_TOOLTIP), (caption, value) -> value.caption(), new OptionInstance.Enum<>(Arrays.asList(MusicManager.MusicFrequency.values()), MusicManager.MusicFrequency.CODEC), MusicManager.MusicFrequency.DEFAULT, value -> Minecraft.getInstance().getMusicManager().setMinutesBetweenSongs(value));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1604 */     this
/*      */ 
/*      */ 
/*      */       
/* 1608 */       .musicToast = new OptionInstance<>("options.musicToast", value -> Tooltip.create(value.tooltip()), (caption, value) -> value.text(), new OptionInstance.Enum<>(Arrays.asList(MusicToastDisplayState.values()), MusicToastDisplayState.CODEC), MusicToastDisplayState.NEVER, value -> this.minecraft.getToastManager().setMusicToastDisplayState(value));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1618 */     this.startedCleanly = true;
/*      */ 
/*      */     
/* 1621 */     this.minecraft = minecraft;
/* 1622 */     this.optionsFile = new File(workingDirectory, "options.txt");
/*      */     
/* 1624 */     boolean largeDistances = (Runtime.getRuntime().maxMemory() >= 1000000000L);
/*      */     
/* 1626 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1635 */       .renderDistance = new OptionInstance<>("options.renderDistance", OptionInstance.noTooltip(), (caption, value) -> genericValueLabel(caption, (Component)Component.translatable("options.chunks", new Object[] { value })), new OptionInstance.IntRange(2, largeDistances ? 32 : 16, false), 12, value -> {
/*      */           operateOnLevelRenderer(LevelRenderer::needsUpdate);
/*      */ 
/*      */           
/*      */           setGraphicsPresetToCustom();
/*      */         });
/*      */     
/* 1642 */     this
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1651 */       .simulationDistance = new OptionInstance<>("options.simulationDistance", OptionInstance.noTooltip(), (caption, value) -> genericValueLabel(caption, (Component)Component.translatable("options.chunks", new Object[] { value })), new OptionInstance.IntRange(SharedConstants.DEBUG_ALLOW_LOW_SIM_DISTANCE ? 2 : 5, largeDistances ? 32 : 16, false), 12, value -> setGraphicsPresetToCustom());
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1657 */     this.syncWrites = (Util.getPlatform() == Util.OS.WINDOWS);
/*      */     
/* 1659 */     load(); }
/*      */   public OptionInstance<Double> sensitivity() { return this.sensitivity; }
/*      */   public OptionInstance<Integer> renderDistance() { return this.renderDistance; }
/*      */   public OptionInstance<Integer> simulationDistance() { return this.simulationDistance; }
/* 1663 */   public OptionInstance<Double> entityDistanceScaling() { return this.entityDistanceScaling; } public OptionInstance<Integer> framerateLimit() { return this.framerateLimit; } public void applyGraphicsPreset(GraphicsPreset value) { this.isApplyingGraphicsPreset = true; value.apply(this.minecraft); this.isApplyingGraphicsPreset = false; } public OptionInstance<GraphicsPreset> graphicsPreset() { return this.graphicsPreset; } private static final Component INACTIVITY_FPS_LIMIT_TOOLTIP_MINIMIZED = (Component)Component.translatable("options.inactivityFpsLimit.minimized.tooltip"); private static final Component INACTIVITY_FPS_LIMIT_TOOLTIP_AFK = (Component)Component.translatable("options.inactivityFpsLimit.afk.tooltip"); private final OptionInstance<InactivityFpsLimit> inactivityFpsLimit; private final OptionInstance<CloudStatus> cloudStatus; private final OptionInstance<Integer> cloudRange; public OptionInstance<InactivityFpsLimit> inactivityFpsLimit() { return this.inactivityFpsLimit; } public OptionInstance<CloudStatus> cloudStatus() { return this.cloudStatus; } public OptionInstance<Integer> cloudRange() { return this.cloudRange; } private static final Component GRAPHICS_TOOLTIP_WEATHER_RADIUS = (Component)Component.translatable("options.weatherRadius.tooltip"); private final OptionInstance<Integer> weatherRadius; public OptionInstance<Integer> weatherRadius() { return this.weatherRadius; } private static final Component GRAPHICS_TOOLTIP_CUTOUT_LEAVES = (Component)Component.translatable("options.cutoutLeaves.tooltip"); private final OptionInstance<Boolean> cutoutLeaves; public OptionInstance<Boolean> cutoutLeaves() { return this.cutoutLeaves; } private static final Component GRAPHICS_TOOLTIP_VIGNETTE = (Component)Component.translatable("options.vignette.tooltip"); private final OptionInstance<Boolean> vignette; public OptionInstance<Boolean> vignette() { return this.vignette; } private static final Component GRAPHICS_TOOLTIP_IMPROVED_TRANSPARENCY = (Component)Component.translatable("options.improvedTransparency.tooltip"); private final OptionInstance<Boolean> improvedTransparency; private final OptionInstance<Boolean> ambientOcclusion; public OptionInstance<Boolean> improvedTransparency() { return this.improvedTransparency; } public OptionInstance<Boolean> ambientOcclusion() { return this.ambientOcclusion; } private static final Component GRAPHICS_TOOLTIP_CHUNK_FADE = (Component)Component.translatable("options.chunkFade.tooltip"); private final OptionInstance<Double> chunkSectionFadeInTime; public OptionInstance<Double> chunkSectionFadeInTime() { return this.chunkSectionFadeInTime; } private static final Component PRIORITIZE_CHUNK_TOOLTIP_NONE = (Component)Component.translatable("options.prioritizeChunkUpdates.none.tooltip"); private static final Component PRIORITIZE_CHUNK_TOOLTIP_PLAYER_AFFECTED = (Component)Component.translatable("options.prioritizeChunkUpdates.byPlayer.tooltip"); private static final Component PRIORITIZE_CHUNK_TOOLTIP_NEARBY = (Component)Component.translatable("options.prioritizeChunkUpdates.nearby.tooltip"); private final OptionInstance<PrioritizeChunkUpdates> prioritizeChunkUpdates; public List<String> resourcePacks; public List<String> incompatibleResourcePacks; private final OptionInstance<ChatVisiblity> chatVisibility; private final OptionInstance<Double> chatOpacity; private final OptionInstance<Double> chatLineSpacing; public OptionInstance<PrioritizeChunkUpdates> prioritizeChunkUpdates() { return this.prioritizeChunkUpdates; } public void updateResourcePacks(PackRepository packRepository) { ImmutableList immutableList1 = ImmutableList.copyOf(this.resourcePacks); this.resourcePacks.clear(); this.incompatibleResourcePacks.clear(); for (Pack entry : (Iterable<Pack>)packRepository.getSelectedPacks()) { if (!entry.isFixedPosition()) { this.resourcePacks.add(entry.getId()); if (!entry.getCompatibility().isCompatible()) this.incompatibleResourcePacks.add(entry.getId());  }  }  save(); ImmutableList immutableList2 = ImmutableList.copyOf(this.resourcePacks); if (!immutableList2.equals(immutableList1)) this.minecraft.reloadResourcePacks();  } public OptionInstance<ChatVisiblity> chatVisibility() { return this.chatVisibility; } public OptionInstance<Double> chatOpacity() { return this.chatOpacity; } public OptionInstance<Double> chatLineSpacing() { return this.chatLineSpacing; } private static final Component MENU_BACKGROUND_BLURRINESS_TOOLTIP = (Component)Component.translatable("options.accessibility.menu_background_blurriness.tooltip"); private static final int BLURRINESS_DEFAULT_VALUE = 5; private final OptionInstance<Integer> menuBackgroundBlurriness; private final OptionInstance<Double> textBackgroundOpacity; private final OptionInstance<Double> panoramaSpeed; public OptionInstance<Integer> menuBackgroundBlurriness() { return this.menuBackgroundBlurriness; } public int getMenuBackgroundBlurriness() { return (Integer)menuBackgroundBlurriness().get(); } public OptionInstance<Double> textBackgroundOpacity() { return this.textBackgroundOpacity; } public OptionInstance<Double> panoramaSpeed() { return this.panoramaSpeed; } private static final Component ACCESSIBILITY_TOOLTIP_CONTRAST_MODE = (Component)Component.translatable("options.accessibility.high_contrast.tooltip"); private final OptionInstance<Boolean> highContrast; public OptionInstance<Boolean> highContrast() { return this.highContrast; } private static final Component HIGH_CONTRAST_BLOCK_OUTLINE_TOOLTIP = (Component)Component.translatable("options.accessibility.high_contrast_block_outline.tooltip"); private final OptionInstance<Boolean> highContrastBlockOutline; private final OptionInstance<Boolean> narratorHotkey; public String fullscreenVideoModeString; public boolean hideServerAddress; public boolean advancedItemTooltips; public boolean pauseOnLostFocus; private final Set<PlayerModelPart> modelParts; private final OptionInstance<HumanoidArm> mainHand; public int overrideWidth; public int overrideHeight; private final OptionInstance<Double> chatScale; private final OptionInstance<Double> chatWidth; private final OptionInstance<Double> chatHeightUnfocused; private final OptionInstance<Double> chatHeightFocused; private final OptionInstance<Double> chatDelay; public OptionInstance<Boolean> highContrastBlockOutline() { return this.highContrastBlockOutline; } public OptionInstance<Boolean> narratorHotkey() { return this.narratorHotkey; } public OptionInstance<HumanoidArm> mainHand() { return this.mainHand; } public OptionInstance<Double> chatScale() { return this.chatScale; } public OptionInstance<Double> chatWidth() { return this.chatWidth; } public OptionInstance<Double> chatHeightUnfocused() { return this.chatHeightUnfocused; } public OptionInstance<Double> chatHeightFocused() { return this.chatHeightFocused; } public OptionInstance<Double> chatDelay() { return this.chatDelay; } private static final Component ACCESSIBILITY_TOOLTIP_NOTIFICATION_DISPLAY_TIME = (Component)Component.translatable("options.notifications.display_time.tooltip"); private final OptionInstance<Double> notificationDisplayTime; private final OptionInstance<Integer> mipmapLevels; public OptionInstance<Double> notificationDisplayTime() { return this.notificationDisplayTime; } public OptionInstance<Integer> mipmapLevels() { return this.mipmapLevels; } private static final Component GRAPHICS_TOOLTIP_ANISOTROPIC_FILTERING = (Component)Component.translatable("options.maxAnisotropy.tooltip"); private final OptionInstance<Integer> maxAnisotropyBit; public OptionInstance<Integer> maxAnisotropyBit() { return this.maxAnisotropyBit; } public int maxAnisotropyValue() { return Math.min(1 << (Integer)this.maxAnisotropyBit.get(), RenderSystem.getDevice().getMaxSupportedAnisotropy()); } private static final Component FILTERING_NONE_TOOLTIP = (Component)Component.translatable("options.textureFiltering.none.tooltip"); private static final Component FILTERING_RGSS_TOOLTIP = (Component)Component.translatable("options.textureFiltering.rgss.tooltip"); private static final Component FILTERING_ANISOTROPIC_TOOLTIP = (Component)Component.translatable("options.textureFiltering.anisotropic.tooltip"); private final OptionInstance<TextureFilteringMethod> textureFiltering; private boolean useNativeTransport; private final OptionInstance<AttackIndicatorStatus> attackIndicator; public TutorialSteps tutorialStep; public boolean joinedFirstServer; private final OptionInstance<Integer> biomeBlendRadius; private final OptionInstance<Double> mouseWheelSensitivity; private final OptionInstance<Boolean> rawMouseInput; public OptionInstance<TextureFilteringMethod> textureFiltering() { return this.textureFiltering; } public OptionInstance<AttackIndicatorStatus> attackIndicator() { return this.attackIndicator; } public OptionInstance<Integer> biomeBlendRadius() { return this.biomeBlendRadius; } private static double logMouse(int value) { return Math.pow(10.0D, value / 100.0D); } private static int unlogMouse(double value) { return Mth.floor(Math.log10(value) * 100.0D); } public OptionInstance<Double> mouseWheelSensitivity() { return this.mouseWheelSensitivity; } public OptionInstance<Boolean> rawMouseInput() { return this.rawMouseInput; } private static final Component ALLOW_CURSOR_CHANGES_TOOLTIP = (Component)Component.translatable("options.allowCursorChanges.tooltip"); private final OptionInstance<Boolean> allowCursorChanges; public int glDebugVerbosity; private final OptionInstance<Boolean> autoJump; public OptionInstance<Boolean> allowCursorChanges() { return this.allowCursorChanges; } public OptionInstance<Boolean> autoJump() { return this.autoJump; } private static final Component ACCESSIBILITY_TOOLTIP_ROTATE_WITH_MINECART = (Component)Component.translatable("options.rotateWithMinecart.tooltip"); private final OptionInstance<Boolean> rotateWithMinecart; private final OptionInstance<Boolean> operatorItemsTab; private final OptionInstance<Boolean> autoSuggestions; private final OptionInstance<Boolean> chatColors; private final OptionInstance<Boolean> chatLinks; private final OptionInstance<Boolean> chatLinksPrompt; private final OptionInstance<Boolean> enableVsync; private final OptionInstance<Boolean> entityShadows; private final OptionInstance<Boolean> forceUnicodeFont; private final OptionInstance<Boolean> japaneseGlyphVariants; private final OptionInstance<Boolean> invertXMouse; private final OptionInstance<Boolean> invertYMouse; private final OptionInstance<Boolean> discreteMouseScroll; public OptionInstance<Boolean> rotateWithMinecart() { return this.rotateWithMinecart; } public OptionInstance<Boolean> operatorItemsTab() { return this.operatorItemsTab; } public OptionInstance<Boolean> autoSuggestions() { return this.autoSuggestions; } public OptionInstance<Boolean> chatColors() { return this.chatColors; } public OptionInstance<Boolean> chatLinks() { return this.chatLinks; } public OptionInstance<Boolean> chatLinksPrompt() { return this.chatLinksPrompt; } public OptionInstance<Boolean> enableVsync() { return this.enableVsync; } public OptionInstance<Boolean> entityShadows() { return this.entityShadows; } private static void updateFontOptions() { Minecraft instance = Minecraft.getInstance(); if (instance.getWindow() != null) { instance.updateFontOptions(); instance.resizeDisplay(); }  } public OptionInstance<Boolean> forceUnicodeFont() { return this.forceUnicodeFont; } private static boolean japaneseGlyphVariantsDefault() { return Locale.getDefault().getLanguage().equalsIgnoreCase("ja"); } public OptionInstance<Boolean> japaneseGlyphVariants() { return this.japaneseGlyphVariants; } public OptionInstance<Boolean> invertMouseX() { return this.invertXMouse; } public OptionInstance<Boolean> invertMouseY() { return this.invertYMouse; } public OptionInstance<Boolean> discreteMouseScroll() { return this.discreteMouseScroll; } private static final Component REALMS_NOTIFICATIONS_TOOLTIP = (Component)Component.translatable("options.realmsNotifications.tooltip"); private final OptionInstance<Boolean> realmsNotifications; public OptionInstance<Boolean> realmsNotifications() { return this.realmsNotifications; } private static final Component ALLOW_SERVER_LISTING_TOOLTIP = (Component)Component.translatable("options.allowServerListing.tooltip"); private final OptionInstance<Boolean> allowServerListing; private final OptionInstance<Boolean> reducedDebugInfo; private final Map<SoundSource, OptionInstance<Double>> soundSourceVolumes; public float getBackgroundOpacity(float defaultOpacity) { return (Boolean)this.backgroundForChatOnly.get() ? defaultOpacity : ((Double)textBackgroundOpacity().get()).floatValue(); }
/*      */   public OptionInstance<Boolean> allowServerListing() { return this.allowServerListing; }
/*      */   public OptionInstance<Boolean> reducedDebugInfo() { return this.reducedDebugInfo; }
/*      */   public final float getFinalSoundSourceVolume(SoundSource source) { if (source == SoundSource.MASTER) return getSoundSourceVolume(source);  return getSoundSourceVolume(source) * getSoundSourceVolume(SoundSource.MASTER); }
/* 1667 */   public final float getSoundSourceVolume(SoundSource source) { return ((Double)getSoundSourceOptionInstance(source).get()).floatValue(); } public final OptionInstance<Double> getSoundSourceOptionInstance(SoundSource source) { return Objects.<OptionInstance<Double>>requireNonNull(this.soundSourceVolumes.get(source)); } private OptionInstance<Double> createSoundSliderOptionInstance(String captionId, SoundSource category) { return new OptionInstance<>(captionId, OptionInstance.noTooltip(), Options::percentValueOrOffLabel, OptionInstance.UnitDouble.INSTANCE, 1.0D, value -> { Minecraft minecraft = Minecraft.getInstance(); SoundManager soundManager = minecraft.getSoundManager(); if ((category == SoundSource.MASTER || category == SoundSource.MUSIC) && getFinalSoundSourceVolume(SoundSource.MUSIC) > 0.0F) minecraft.getMusicManager().showNowPlayingToastIfNeeded();  soundManager.refreshCategoryVolume(category); if (minecraft.level == null) SoundPreviewHandler.preview(soundManager, category, category.floatValue());  }); } private static final Component CLOSED_CAPTIONS_TOOLTIP = (Component)Component.translatable("options.showSubtitles.tooltip"); private final OptionInstance<Boolean> showSubtitles; public OptionInstance<Boolean> showSubtitles() { return this.showSubtitles; } private static final Component DIRECTIONAL_AUDIO_TOOLTIP_ON = (Component)Component.translatable("options.directionalAudio.on.tooltip"); private static final Component DIRECTIONAL_AUDIO_TOOLTIP_OFF = (Component)Component.translatable("options.directionalAudio.off.tooltip"); private final OptionInstance<Boolean> directionalAudio; private final OptionInstance<Boolean> backgroundForChatOnly; private final OptionInstance<Boolean> touchscreen; private final OptionInstance<Boolean> fullscreen; private final OptionInstance<Boolean> bobView; public OptionInstance<Boolean> directionalAudio() { return this.directionalAudio; } public OptionInstance<Boolean> backgroundForChatOnly() { return this.backgroundForChatOnly; } public OptionInstance<Boolean> touchscreen() { return this.touchscreen; } public OptionInstance<Boolean> fullscreen() { return this.fullscreen; } public OptionInstance<Boolean> bobView() { return this.bobView; } private static final Component KEY_TOGGLE = (Component)Component.translatable("options.key.toggle"); private static final Component KEY_HOLD = (Component)Component.translatable("options.key.hold"); private final OptionInstance<Boolean> toggleCrouch; private final OptionInstance<Boolean> toggleSprint; private final OptionInstance<Boolean> toggleAttack; private final OptionInstance<Boolean> toggleUse; public OptionInstance<Boolean> toggleCrouch() { return this.toggleCrouch; } public OptionInstance<Boolean> toggleSprint() { return this.toggleSprint; } public OptionInstance<Boolean> toggleAttack() { return this.toggleAttack; } public OptionInstance<Boolean> toggleUse() { return this.toggleUse; } private static final Component SPRINT_WINDOW_TOOLTIP = (Component)Component.translatable("options.sprintWindow.tooltip"); private final OptionInstance<Integer> sprintWindow; public boolean skipMultiplayerWarning; public OptionInstance<Integer> sprintWindow() { return this.sprintWindow; } private static final Component CHAT_TOOLTIP_HIDE_MATCHED_NAMES = (Component)Component.translatable("options.hideMatchedNames.tooltip"); private final OptionInstance<Boolean> hideMatchedNames; private final OptionInstance<Boolean> showAutosaveIndicator; public OptionInstance<Boolean> hideMatchedNames() { return this.hideMatchedNames; } public OptionInstance<Boolean> showAutosaveIndicator() { return this.showAutosaveIndicator; } private static final Component CHAT_TOOLTIP_ONLY_SHOW_SECURE = (Component)Component.translatable("options.onlyShowSecureChat.tooltip"); private final OptionInstance<Boolean> onlyShowSecureChat; public OptionInstance<Boolean> onlyShowSecureChat() { return this.onlyShowSecureChat; } private static final Component CHAT_TOOLTIP_SAVE_DRAFTS = (Component)Component.translatable("options.chat.drafts.tooltip"); private final OptionInstance<Boolean> saveChatDrafts; public final KeyMapping keyUp; public final KeyMapping keyLeft; public final KeyMapping keyDown; public final KeyMapping keyRight; public final KeyMapping keyJump; public final KeyMapping keyShift; public final KeyMapping keySprint; public final KeyMapping keyInventory; public final KeyMapping keySwapOffhand; public final KeyMapping keyDrop; public final KeyMapping keyUse; public final KeyMapping keyAttack; public final KeyMapping keyPickItem; public final KeyMapping keyChat; public final KeyMapping keyPlayerList; public final KeyMapping keyCommand; public final KeyMapping keySocialInteractions; public final KeyMapping keyScreenshot; public final KeyMapping keyTogglePerspective; public final KeyMapping keySmoothCamera; public final KeyMapping keyFullscreen; public final KeyMapping keyAdvancements; public final KeyMapping keyQuickActions; public final KeyMapping keyToggleGui; public final KeyMapping keyToggleSpectatorShaderEffects; public final KeyMapping[] keyHotbarSlots; public final KeyMapping keySaveHotbarActivator; public final KeyMapping keyLoadHotbarActivator; public final KeyMapping keySpectatorOutlines; public final KeyMapping keySpectatorHotbar; public final KeyMapping keyDebugOverlay; public final KeyMapping keyDebugModifier; public final KeyMapping keyDebugCrash; public final KeyMapping keyDebugReloadChunk; public final KeyMapping keyDebugShowHitboxes; public final KeyMapping keyDebugClearChat; public final KeyMapping keyDebugShowChunkBorders; public final KeyMapping keyDebugShowAdvancedTooltips; public final KeyMapping keyDebugCopyRecreateCommand; public final KeyMapping keyDebugSpectate; public final KeyMapping keyDebugSwitchGameMode; public final KeyMapping keyDebugDebugOptions; public final KeyMapping keyDebugFocusPause; public final KeyMapping keyDebugDumpDynamicTextures; public final KeyMapping keyDebugReloadResourcePacks; public final KeyMapping keyDebugProfiling; public final KeyMapping keyDebugCopyLocation; public final KeyMapping keyDebugDumpVersion; public final KeyMapping keyDebugPofilingChart; public final KeyMapping keyDebugFpsCharts; public final KeyMapping keyDebugNetworkCharts; public final KeyMapping[] debugKeys; public final KeyMapping[] keyMappings; protected Minecraft minecraft; private final File optionsFile; public boolean hideGui; private CameraType cameraType; public String lastMpIp; public boolean smoothCamera; private final OptionInstance<Integer> fov; public OptionInstance<Boolean> saveChatDrafts() { return this.saveChatDrafts; } private void setGraphicsPresetToCustom() { if (this.isApplyingGraphicsPreset) return;  this.graphicsPreset.set(GraphicsPreset.CUSTOM); Screen screen = this.minecraft.screen; if (screen instanceof OptionsSubScreen) { OptionsSubScreen optionsSubScreen = (OptionsSubScreen)screen; optionsSubScreen.resetOption(this.graphicsPreset); }  } public OptionInstance<Integer> fov() { return this.fov; } private static final Component TELEMETRY_TOOLTIP = (Component)Component.translatable("options.telemetry.button.tooltip", new Object[] { Component.translatable("options.telemetry.state.minimal"), Component.translatable("options.telemetry.state.all") }); private final OptionInstance<Boolean> telemetryOptInExtra; public OptionInstance<Boolean> telemetryOptInExtra() { return this.telemetryOptInExtra; } private static final Component ACCESSIBILITY_TOOLTIP_SCREEN_EFFECT = (Component)Component.translatable("options.screenEffectScale.tooltip"); private final OptionInstance<Double> screenEffectScale; public OptionInstance<Double> screenEffectScale() { return this.screenEffectScale; } private static final Component ACCESSIBILITY_TOOLTIP_FOV_EFFECT = (Component)Component.translatable("options.fovEffectScale.tooltip"); private final OptionInstance<Double> fovEffectScale; public OptionInstance<Double> fovEffectScale() { return this.fovEffectScale; } private static final Component ACCESSIBILITY_TOOLTIP_DARKNESS_EFFECT = (Component)Component.translatable("options.darknessEffectScale.tooltip"); private final OptionInstance<Double> darknessEffectScale; public OptionInstance<Double> darknessEffectScale() { return this.darknessEffectScale; } private static final Component ACCESSIBILITY_TOOLTIP_GLINT_SPEED = (Component)Component.translatable("options.glintSpeed.tooltip"); private final OptionInstance<Double> glintSpeed; public OptionInstance<Double> glintSpeed() { return this.glintSpeed; } private static final Component ACCESSIBILITY_TOOLTIP_GLINT_STRENGTH = (Component)Component.translatable("options.glintStrength.tooltip"); private final OptionInstance<Double> glintStrength; public OptionInstance<Double> glintStrength() { return this.glintStrength; } private static final Component ACCESSIBILITY_TOOLTIP_DAMAGE_TILT_STRENGTH = (Component)Component.translatable("options.damageTiltStrength.tooltip"); private final OptionInstance<Double> damageTiltStrength; private final OptionInstance<Double> gamma; public static final int AUTO_GUI_SCALE = 0; private static final int MAX_GUI_SCALE_INCLUSIVE = 2147483646; private final OptionInstance<Integer> guiScale; private final OptionInstance<ParticleStatus> particles; private final OptionInstance<NarratorStatus> narrator; public String languageCode; private final OptionInstance<String> soundDevice; public boolean onboardAccessibility; public OptionInstance<Double> damageTiltStrength() { return this.damageTiltStrength; } public OptionInstance<Double> gamma() { return this.gamma; } public OptionInstance<Integer> guiScale() { return this.guiScale; } public OptionInstance<ParticleStatus> particles() { return this.particles; } public OptionInstance<NarratorStatus> narrator() { return this.narrator; } public OptionInstance<String> soundDevice() { return this.soundDevice; } public void onboardingAccessibilityFinished() { this.onboardAccessibility = false; save(); } private static final Component MUSIC_FREQUENCY_TOOLTIP = (Component)Component.translatable("options.music_frequency.tooltip"); private final OptionInstance<MusicManager.MusicFrequency> musicFrequency; private final OptionInstance<MusicToastDisplayState> musicToast; public boolean syncWrites; public boolean startedCleanly; public OptionInstance<MusicManager.MusicFrequency> musicFrequency() { return this.musicFrequency; } public OptionInstance<MusicToastDisplayState> musicToast() { return this.musicToast; } public int getBackgroundColor(float defaultOpacity) { return ARGB.colorFromFloat(getBackgroundOpacity(defaultOpacity), 0.0F, 0.0F, 0.0F); }
/*      */ 
/*      */   
/*      */   public int getBackgroundColor(int defaultColor) {
/* 1671 */     return (Boolean)this.backgroundForChatOnly.get() ? defaultColor : ARGB.colorFromFloat(((Double)this.textBackgroundOpacity.get()).floatValue(), 0.0F, 0.0F, 0.0F);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private void processDumpedOptions(OptionAccess access) {
/* 1680 */     access.process("ao", this.ambientOcclusion);
/* 1681 */     access.process("biomeBlendRadius", this.biomeBlendRadius);
/* 1682 */     access.process("chunkSectionFadeInTime", this.chunkSectionFadeInTime);
/* 1683 */     access.process("cutoutLeaves", this.cutoutLeaves);
/* 1684 */     access.process("enableVsync", this.enableVsync);
/* 1685 */     access.process("entityDistanceScaling", this.entityDistanceScaling);
/* 1686 */     access.process("entityShadows", this.entityShadows);
/* 1687 */     access.process("forceUnicodeFont", this.forceUnicodeFont);
/* 1688 */     access.process("japaneseGlyphVariants", this.japaneseGlyphVariants);
/* 1689 */     access.process("fov", this.fov);
/* 1690 */     access.process("fovEffectScale", this.fovEffectScale);
/* 1691 */     access.process("darknessEffectScale", this.darknessEffectScale);
/* 1692 */     access.process("glintSpeed", this.glintSpeed);
/* 1693 */     access.process("glintStrength", this.glintStrength);
/* 1694 */     access.process("graphicsPreset", this.graphicsPreset);
/* 1695 */     access.process("prioritizeChunkUpdates", this.prioritizeChunkUpdates);
/* 1696 */     access.process("fullscreen", this.fullscreen);
/* 1697 */     access.process("gamma", this.gamma);
/* 1698 */     access.process("guiScale", this.guiScale);
/* 1699 */     access.process("maxAnisotropyBit", this.maxAnisotropyBit);
/* 1700 */     access.process("textureFiltering", this.textureFiltering);
/* 1701 */     access.process("maxFps", this.framerateLimit);
/* 1702 */     access.process("improvedTransparency", this.improvedTransparency);
/* 1703 */     access.process("inactivityFpsLimit", this.inactivityFpsLimit);
/* 1704 */     access.process("mipmapLevels", this.mipmapLevels);
/* 1705 */     access.process("narrator", this.narrator);
/* 1706 */     access.process("particles", this.particles);
/* 1707 */     access.process("reducedDebugInfo", this.reducedDebugInfo);
/* 1708 */     access.process("renderClouds", this.cloudStatus);
/* 1709 */     access.process("cloudRange", this.cloudRange);
/*      */     
/* 1711 */     access.process("renderDistance", this.renderDistance);
/* 1712 */     access.process("simulationDistance", this.simulationDistance);
/* 1713 */     access.process("screenEffectScale", this.screenEffectScale);
/* 1714 */     access.process("soundDevice", this.soundDevice);
/* 1715 */     access.process("vignette", this.vignette);
/* 1716 */     access.process("weatherRadius", this.weatherRadius);
/*      */   }
/*      */   
/*      */   private void processOptions(FieldAccess access) {
/* 1720 */     processDumpedOptions(access);
/*      */     
/* 1722 */     access.process("autoJump", this.autoJump);
/* 1723 */     access.process("rotateWithMinecart", this.rotateWithMinecart);
/* 1724 */     access.process("operatorItemsTab", this.operatorItemsTab);
/* 1725 */     access.process("autoSuggestions", this.autoSuggestions);
/* 1726 */     access.process("chatColors", this.chatColors);
/* 1727 */     access.process("chatLinks", this.chatLinks);
/* 1728 */     access.process("chatLinksPrompt", this.chatLinksPrompt);
/* 1729 */     access.process("discrete_mouse_scroll", this.discreteMouseScroll);
/* 1730 */     access.process("invertXMouse", this.invertXMouse);
/* 1731 */     access.process("invertYMouse", this.invertYMouse);
/* 1732 */     access.process("realmsNotifications", this.realmsNotifications);
/* 1733 */     access.process("showSubtitles", this.showSubtitles);
/* 1734 */     access.process("directionalAudio", this.directionalAudio);
/* 1735 */     access.process("touchscreen", this.touchscreen);
/* 1736 */     access.process("bobView", this.bobView);
/* 1737 */     access.process("toggleCrouch", this.toggleCrouch);
/* 1738 */     access.process("toggleSprint", this.toggleSprint);
/* 1739 */     access.process("toggleAttack", this.toggleAttack);
/* 1740 */     access.process("toggleUse", this.toggleUse);
/* 1741 */     access.process("sprintWindow", this.sprintWindow);
/* 1742 */     access.process("darkMojangStudiosBackground", this.darkMojangStudiosBackground);
/* 1743 */     access.process("hideLightningFlashes", this.hideLightningFlash);
/* 1744 */     access.process("hideSplashTexts", this.hideSplashTexts);
/* 1745 */     access.process("mouseSensitivity", this.sensitivity);
/* 1746 */     access.process("damageTiltStrength", this.damageTiltStrength);
/* 1747 */     access.process("highContrast", this.highContrast);
/* 1748 */     access.process("highContrastBlockOutline", this.highContrastBlockOutline);
/* 1749 */     access.process("narratorHotkey", this.narratorHotkey);
/*      */     
/* 1751 */     Objects.requireNonNull(GSON); this.resourcePacks = access.<List<String>>process("resourcePacks", this.resourcePacks, Options::readListOfStrings, GSON::toJson);
/* 1752 */     Objects.requireNonNull(GSON); this.incompatibleResourcePacks = access.<List<String>>process("incompatibleResourcePacks", this.incompatibleResourcePacks, Options::readListOfStrings, GSON::toJson);
/* 1753 */     this.lastMpIp = access.process("lastServer", this.lastMpIp);
/* 1754 */     this.languageCode = access.process("lang", this.languageCode);
/* 1755 */     access.process("chatVisibility", this.chatVisibility);
/* 1756 */     access.process("chatOpacity", this.chatOpacity);
/* 1757 */     access.process("chatLineSpacing", this.chatLineSpacing);
/* 1758 */     access.process("textBackgroundOpacity", this.textBackgroundOpacity);
/* 1759 */     access.process("backgroundForChatOnly", this.backgroundForChatOnly);
/*      */     
/* 1761 */     this.hideServerAddress = access.process("hideServerAddress", this.hideServerAddress);
/* 1762 */     this.advancedItemTooltips = access.process("advancedItemTooltips", this.advancedItemTooltips);
/* 1763 */     this.pauseOnLostFocus = access.process("pauseOnLostFocus", this.pauseOnLostFocus);
/* 1764 */     this.overrideWidth = access.process("overrideWidth", this.overrideWidth);
/* 1765 */     this.overrideHeight = access.process("overrideHeight", this.overrideHeight);
/* 1766 */     access.process("chatHeightFocused", this.chatHeightFocused);
/* 1767 */     access.process("chatDelay", this.chatDelay);
/* 1768 */     access.process("chatHeightUnfocused", this.chatHeightUnfocused);
/* 1769 */     access.process("chatScale", this.chatScale);
/* 1770 */     access.process("chatWidth", this.chatWidth);
/* 1771 */     access.process("notificationDisplayTime", this.notificationDisplayTime);
/* 1772 */     this.useNativeTransport = access.process("useNativeTransport", this.useNativeTransport);
/* 1773 */     access.process("mainHand", this.mainHand);
/* 1774 */     access.process("attackIndicator", this.attackIndicator);
/* 1775 */     this.tutorialStep = access.<TutorialSteps>process("tutorialStep", this.tutorialStep, TutorialSteps::getByName, TutorialSteps::getName);
/* 1776 */     access.process("mouseWheelSensitivity", this.mouseWheelSensitivity);
/* 1777 */     access.process("rawMouseInput", this.rawMouseInput);
/* 1778 */     access.process("allowCursorChanges", this.allowCursorChanges);
/* 1779 */     this.glDebugVerbosity = access.process("glDebugVerbosity", this.glDebugVerbosity);
/* 1780 */     this.skipMultiplayerWarning = access.process("skipMultiplayerWarning", this.skipMultiplayerWarning);
/* 1781 */     access.process("hideMatchedNames", this.hideMatchedNames);
/* 1782 */     this.joinedFirstServer = access.process("joinedFirstServer", this.joinedFirstServer);
/* 1783 */     this.syncWrites = access.process("syncChunkWrites", this.syncWrites);
/* 1784 */     access.process("showAutosaveIndicator", this.showAutosaveIndicator);
/* 1785 */     access.process("allowServerListing", this.allowServerListing);
/* 1786 */     access.process("onlyShowSecureChat", this.onlyShowSecureChat);
/* 1787 */     access.process("saveChatDrafts", this.saveChatDrafts);
/* 1788 */     access.process("panoramaScrollSpeed", this.panoramaSpeed);
/* 1789 */     access.process("telemetryOptInExtra", this.telemetryOptInExtra);
/* 1790 */     this.onboardAccessibility = access.process("onboardAccessibility", this.onboardAccessibility);
/* 1791 */     access.process("menuBackgroundBlurriness", this.menuBackgroundBlurriness);
/* 1792 */     this.startedCleanly = access.process("startedCleanly", this.startedCleanly);
/* 1793 */     access.process("musicToast", this.musicToast);
/* 1794 */     access.process("musicFrequency", this.musicFrequency);
/*      */     
/* 1796 */     for (KeyMapping keyMapping : this.keyMappings) {
/* 1797 */       String currentValue = keyMapping.saveString();
/* 1798 */       String newValue = access.process("key_" + keyMapping.getName(), currentValue);
/* 1799 */       if (!currentValue.equals(newValue)) {
/* 1800 */         keyMapping.setKey(InputConstants.getKey(newValue));
/*      */       }
/*      */     } 
/*      */     
/* 1804 */     for (SoundSource source : SoundSource.values()) {
/* 1805 */       access.process("soundCategory_" + source.getName(), (OptionInstance)this.soundSourceVolumes.get(source));
/*      */     }
/*      */     
/* 1808 */     for (PlayerModelPart part : PlayerModelPart.values()) {
/* 1809 */       boolean wasEnabled = this.modelParts.contains(part);
/* 1810 */       boolean isEnabled = access.process("modelPart_" + part.getId(), wasEnabled);
/* 1811 */       if (isEnabled != wasEnabled) {
/* 1812 */         setModelPart(part, isEnabled);
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   public void load() {
/*      */     try {
/* 1819 */       if (!this.optionsFile.exists()) {
/*      */         return;
/*      */       }
/*      */       
/* 1823 */       CompoundTag rawOptions = new CompoundTag();
/* 1824 */       BufferedReader reader = Files.newReader(this.optionsFile, StandardCharsets.UTF_8); 
/* 1825 */       try { reader.lines().forEach(line -> {
/*      */               try {
/*      */                 Iterator<String> iterator = OPTION_SPLITTER.split(line).iterator();
/*      */                 rawOptions.putString(iterator.next(), iterator.next());
/* 1829 */               } catch (Exception ignored) {
/*      */                 LOGGER.warn("Skipping bad option: {}", line);
/*      */               } 
/*      */             });
/* 1833 */         if (reader != null) reader.close();  } catch (Throwable throwable) { if (reader != null)
/*      */           try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/* 1835 */        final CompoundTag options = dataFix(rawOptions);
/*      */       
/* 1837 */       processOptions(new FieldAccess(this) {
/*      */             private String getValue(String name) {
/* 1839 */               Tag tag = options.get(name);
/* 1840 */               if (tag == null) {
/* 1841 */                 return null;
/*      */               }
/* 1843 */               if (tag instanceof StringTag) { StringTag stringTag = (StringTag)tag; try { String str1 = stringTag.value(), value = str1;
/* 1844 */                   return value; } catch (Throwable throwable) { throw new MatchException(throwable.toString(), throwable); }
/*      */                  }
/* 1846 */                throw new IllegalStateException("Cannot read field of wrong type, expected string: " + String.valueOf(tag));
/*      */             }
/*      */ 
/*      */             
/*      */             public <T> void process(String name, OptionInstance<T> option) {
/* 1851 */               String result = getValue(name);
/* 1852 */               if (result != null) {
/*      */ 
/*      */                 
/* 1855 */                 JsonElement element = LenientJsonParser.parse(result.isEmpty() ? "\"\"" : result);
/*      */ 
/*      */                 
/* 1858 */                 Objects.requireNonNull(option); option.codec().parse((DynamicOps)JsonOps.INSTANCE, element).ifError(error -> Options.LOGGER.error("Error parsing option value {} for option {}: {}", new Object[] { result, option, error.message() })).ifSuccess(option::set);
/*      */               } 
/*      */             }
/*      */ 
/*      */             
/*      */             public int process(String name, int current) {
/* 1864 */               String result = getValue(name);
/* 1865 */               if (result != null) {
/*      */                 try {
/* 1867 */                   return Integer.parseInt(result);
/* 1868 */                 } catch (NumberFormatException e) {
/* 1869 */                   Options.LOGGER.warn("Invalid integer value for option {} = {}", new Object[] { name, result, e });
/*      */                 } 
/*      */               }
/* 1872 */               return current;
/*      */             }
/*      */ 
/*      */             
/*      */             public boolean process(String name, boolean current) {
/* 1877 */               String result = getValue(name);
/* 1878 */               return (result != null) ? Options.isTrue(result) : current;
/*      */             }
/*      */ 
/*      */             
/*      */             public String process(String name, String current) {
/* 1883 */               return (String)MoreObjects.firstNonNull(getValue(name), current);
/*      */             }
/*      */ 
/*      */             
/*      */             public float process(String name, float current) {
/* 1888 */               String result = getValue(name);
/* 1889 */               if (result != null) {
/*      */                 
/* 1891 */                 if (Options.isTrue(result)) {
/* 1892 */                   return 1.0F;
/*      */                 }
/* 1894 */                 if (Options.isFalse(result)) {
/* 1895 */                   return 0.0F;
/*      */                 }
/*      */                 try {
/* 1898 */                   return Float.parseFloat(result);
/* 1899 */                 } catch (NumberFormatException e) {
/* 1900 */                   Options.LOGGER.warn("Invalid floating point value for option {} = {}", new Object[] { name, result, e });
/*      */                 } 
/*      */               } 
/* 1903 */               return current;
/*      */             }
/*      */ 
/*      */             
/*      */             public <T> T process(String name, T current, Function<String, T> reader, Function<T, String> writer) {
/* 1908 */               String rawResult = getValue(name);
/* 1909 */               return (rawResult == null) ? current : reader.apply(rawResult);
/*      */             }
/*      */           });
/*      */       
/* 1913 */       options.getString("fullscreenResolution").ifPresent(fullscreenResolution -> this.fullscreenVideoModeString = fullscreenResolution);
/*      */       
/* 1915 */       KeyMapping.resetMapping();
/* 1916 */     } catch (Exception e) {
/* 1917 */       LOGGER.error("Failed to load options", e);
/*      */     } 
/*      */   }
/*      */   
/*      */   private static boolean isTrue(String value) {
/* 1922 */     return "true".equals(value);
/*      */   }
/*      */   
/*      */   private static boolean isFalse(String value) {
/* 1926 */     return "false".equals(value);
/*      */   }
/*      */   
/*      */   private CompoundTag dataFix(CompoundTag tag) {
/* 1930 */     int version = 0;
/*      */     try {
/* 1932 */       version = (Integer)tag.getString("version").map(Integer::parseInt).orElse(0);
/* 1933 */     } catch (RuntimeException runtimeException) {}
/*      */ 
/*      */     
/* 1936 */     return DataFixTypes.OPTIONS.updateToCurrentVersion(this.minecraft.getFixerUpper(), tag, version);
/*      */   }
/*      */   public void save() {
/*      */     
/* 1940 */     try { final PrintWriter writer = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFile), StandardCharsets.UTF_8)); 
/* 1941 */       try { writer.println("version:" + SharedConstants.getCurrentVersion().dataVersion().version());
/*      */         
/* 1943 */         processOptions(new FieldAccess(this) {
/*      */               public void writePrefix(String name) {
/* 1945 */                 writer.print(name);
/* 1946 */                 writer.print(':');
/*      */               }
/*      */ 
/*      */               
/*      */               public <T> void process(String name, OptionInstance<T> option) {
/* 1951 */                 option.codec().encodeStart((DynamicOps)JsonOps.INSTANCE, option.get())
/* 1952 */                   .ifError(error -> Options.LOGGER.error("Error saving option {}: {}", option, error.message()))
/* 1953 */                   .ifSuccess(element -> {
/*      */                       writePrefix(name);
/*      */                       name.println(Options.GSON.toJson(element));
/*      */                     });
/*      */               }
/*      */ 
/*      */               
/*      */               public int process(String name, int value) {
/* 1961 */                 writePrefix(name);
/* 1962 */                 writer.println(value);
/* 1963 */                 return value;
/*      */               }
/*      */ 
/*      */               
/*      */               public boolean process(String name, boolean value) {
/* 1968 */                 writePrefix(name);
/* 1969 */                 writer.println(value);
/* 1970 */                 return value;
/*      */               }
/*      */ 
/*      */               
/*      */               public String process(String name, String value) {
/* 1975 */                 writePrefix(name);
/* 1976 */                 writer.println(value);
/* 1977 */                 return value;
/*      */               }
/*      */ 
/*      */               
/*      */               public float process(String name, float value) {
/* 1982 */                 writePrefix(name);
/* 1983 */                 writer.println(value);
/* 1984 */                 return value;
/*      */               }
/*      */ 
/*      */               
/*      */               public <T> T process(String name, T value, Function<String, T> reader, Function<T, String> converter) {
/* 1989 */                 writePrefix(name);
/* 1990 */                 writer.println(converter.apply(value));
/* 1991 */                 return value;
/*      */               }
/*      */             });
/*      */         
/* 1995 */         String fullscreenVideoModeString = getFullscreenVideoModeString();
/* 1996 */         if (fullscreenVideoModeString != null) {
/* 1997 */           writer.println("fullscreenResolution:" + fullscreenVideoModeString);
/*      */         }
/* 1999 */         writer.close(); } catch (Throwable throwable) { try { writer.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }  } catch (Exception e)
/* 2000 */     { LOGGER.error("Failed to save options", e); }
/*      */ 
/*      */     
/* 2003 */     broadcastOptions();
/*      */   }
/*      */   
/*      */   private String getFullscreenVideoModeString() {
/* 2007 */     Window window = this.minecraft.getWindow();
/* 2008 */     if (window == null)
/*      */     {
/* 2010 */       return this.fullscreenVideoModeString;
/*      */     }
/* 2012 */     if (window.getPreferredFullscreenVideoMode().isPresent()) {
/* 2013 */       return ((VideoMode)window.getPreferredFullscreenVideoMode().get()).write();
/*      */     }
/* 2015 */     return null;
/*      */   }
/*      */   
/*      */   public ClientInformation buildPlayerInformation() {
/* 2019 */     int parts = 0;
/* 2020 */     for (PlayerModelPart part : this.modelParts) {
/* 2021 */       parts |= part.getMask();
/*      */     }
/*      */     
/* 2024 */     return new ClientInformation(this.languageCode, (Integer)this.renderDistance.get(), this.chatVisibility.get(), (Boolean)this.chatColors.get(), parts, this.mainHand.get(), this.minecraft.isTextFilteringEnabled(), (Boolean)this.allowServerListing.get(), this.particles.get());
/*      */   }
/*      */   
/*      */   public void broadcastOptions() {
/* 2028 */     if (this.minecraft.player != null) {
/* 2029 */       this.minecraft.player.connection.broadcastClientInformation(buildPlayerInformation());
/*      */     }
/*      */   }
/*      */   
/*      */   public void setModelPart(PlayerModelPart part, boolean visible) {
/* 2034 */     if (visible) {
/* 2035 */       this.modelParts.add(part);
/*      */     } else {
/* 2037 */       this.modelParts.remove(part);
/*      */     } 
/*      */   }
/*      */   
/*      */   public boolean isModelPartEnabled(PlayerModelPart part) {
/* 2042 */     return this.modelParts.contains(part);
/*      */   }
/*      */   
/*      */   public CloudStatus getCloudsType() {
/* 2046 */     return this.cloudStatus.get();
/*      */   }
/*      */   
/*      */   public boolean useNativeTransport() {
/* 2050 */     return this.useNativeTransport;
/*      */   }
/*      */   
/*      */   public void loadSelectedResourcePacks(PackRepository repository) {
/* 2054 */     Set<String> selected = Sets.newLinkedHashSet();
/* 2055 */     for (Iterator<String> iterator = this.resourcePacks.iterator(); iterator.hasNext(); ) {
/* 2056 */       String id = iterator.next();
/* 2057 */       Pack pack = repository.getPack(id);
/*      */       
/* 2059 */       if (pack == null && !id.startsWith("file/"))
/*      */       {
/* 2061 */         pack = repository.getPack("file/" + id);
/*      */       }
/*      */       
/* 2064 */       if (pack == null) {
/* 2065 */         LOGGER.warn("Removed resource pack {} from options because it doesn't seem to exist anymore", id);
/* 2066 */         iterator.remove(); continue;
/* 2067 */       }  if (!pack.getCompatibility().isCompatible() && !this.incompatibleResourcePacks.contains(id)) {
/* 2068 */         LOGGER.warn("Removed resource pack {} from options because it is no longer compatible", id);
/* 2069 */         iterator.remove(); continue;
/* 2070 */       }  if (pack.getCompatibility().isCompatible() && this.incompatibleResourcePacks.contains(id)) {
/* 2071 */         LOGGER.info("Removed resource pack {} from incompatibility list because it's now compatible", id);
/* 2072 */         this.incompatibleResourcePacks.remove(id); continue;
/*      */       } 
/* 2074 */       selected.add(pack.getId());
/*      */     } 
/*      */     
/* 2077 */     repository.setSelected(selected);
/*      */   }
/*      */   
/*      */   public CameraType getCameraType() {
/* 2081 */     return this.cameraType;
/*      */   }
/*      */   
/*      */   public void setCameraType(CameraType cameraType) {
/* 2085 */     this.cameraType = cameraType;
/*      */   }
/*      */   
/*      */   private static List<String> readListOfStrings(String value) {
/* 2089 */     List<String> result = (List<String>)GsonHelper.fromNullableJson(GSON, value, LIST_OF_STRINGS_TYPE);
/* 2090 */     return (result != null) ? result : Lists.newArrayList();
/*      */   }
/*      */   
/*      */   public File getFile() {
/* 2094 */     return this.optionsFile;
/*      */   }
/*      */   
/*      */   public String dumpOptionsForReport() {
/* 2098 */     final List<Pair<String, Object>> optionsForReport = new ArrayList<>();
/*      */     
/* 2100 */     processDumpedOptions(new OptionAccess(this)
/*      */         {
/*      */           public <T> void process(String name, OptionInstance<T> option) {
/* 2103 */             optionsForReport.add(Pair.of(name, option.get()));
/*      */           }
/*      */         });
/*      */ 
/*      */     
/* 2108 */     optionsForReport.add(Pair.of("fullscreenResolution", String.valueOf(this.fullscreenVideoModeString)));
/* 2109 */     optionsForReport.add(Pair.of("glDebugVerbosity", this.glDebugVerbosity));
/* 2110 */     optionsForReport.add(Pair.of("overrideHeight", this.overrideHeight));
/* 2111 */     optionsForReport.add(Pair.of("overrideWidth", this.overrideWidth));
/* 2112 */     optionsForReport.add(Pair.of("syncChunkWrites", this.syncWrites));
/* 2113 */     optionsForReport.add(Pair.of("useNativeTransport", this.useNativeTransport));
/* 2114 */     optionsForReport.add(Pair.of("resourcePacks", this.resourcePacks));
/*      */     
/* 2116 */     return optionsForReport.stream()
/* 2117 */       .sorted(Comparator.comparing(Pair::getFirst))
/* 2118 */       .map(e -> (String)e.getFirst() + ": " + (String)e.getFirst())
/* 2119 */       .collect(Collectors.joining(System.lineSeparator()));
/*      */   }
/*      */   
/*      */   public void setServerRenderDistance(int serverRenderDistance) {
/* 2123 */     this.serverRenderDistance = serverRenderDistance;
/*      */   }
/*      */   
/*      */   public int getEffectiveRenderDistance() {
/* 2127 */     return (this.serverRenderDistance > 0) ? Math.min((Integer)this.renderDistance.get(), this.serverRenderDistance) : (Integer)this.renderDistance.get();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static Component pixelValueLabel(Component caption, int value) {
/* 2147 */     return (Component)Component.translatable("options.pixel_value", new Object[] { caption, value });
/*      */   }
/*      */   
/*      */   private static Component percentValueLabel(Component caption, double value) {
/* 2151 */     return (Component)Component.translatable("options.percent_value", new Object[] { caption, (int)(value * 100.0D) });
/*      */   }
/*      */   
/*      */   public static Component genericValueLabel(Component caption, Component value) {
/* 2155 */     return (Component)Component.translatable("options.generic_value", new Object[] { caption, value });
/*      */   }
/*      */   
/*      */   public static Component genericValueLabel(Component caption, int value) {
/* 2159 */     return genericValueLabel(caption, (Component)Component.literal(Integer.toString(value)));
/*      */   }
/*      */   
/*      */   public static Component genericValueOrOffLabel(Component caption, int value) {
/* 2163 */     if (value == 0) {
/* 2164 */       return genericValueLabel(caption, CommonComponents.OPTION_OFF);
/*      */     }
/* 2166 */     return genericValueLabel(caption, value);
/*      */   }
/*      */   
/*      */   private static Component percentValueOrOffLabel(Component caption, double value) {
/* 2170 */     if (value == 0.0D) {
/* 2171 */       return genericValueLabel(caption, CommonComponents.OPTION_OFF);
/*      */     }
/* 2173 */     return percentValueLabel(caption, value);
/*      */   }
/*      */   
/*      */   private static interface OptionAccess {
/*      */     <T> void process(String param1String, OptionInstance<T> param1OptionInstance);
/*      */   }
/*      */   
/*      */   private static interface FieldAccess extends OptionAccess {
/*      */     int process(String param1String, int param1Int);
/*      */     
/*      */     boolean process(String param1String, boolean param1Boolean);
/*      */     
/*      */     String process(String param1String1, String param1String2);
/*      */     
/*      */     float process(String param1String, float param1Float);
/*      */     
/*      */     <T> T process(String param1String, T param1T, Function<String, T> param1Function, Function<T, String> param1Function1);
/*      */   }
/*      */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/Options.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */