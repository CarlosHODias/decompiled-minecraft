/*     */ package net.minecraft.client.gui.screens.options;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.blaze3d.platform.Monitor;
/*     */ import com.mojang.blaze3d.platform.VideoMode;
/*     */ import com.mojang.blaze3d.platform.Window;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.OptionInstance;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.TextureFilteringMethod;
/*     */ import net.minecraft.client.gui.components.AbstractSliderButton;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.GpuWarnlistManager;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class VideoSettingsScreen extends OptionsSubScreen {
/*  26 */   private static final Component TITLE = (Component)Component.translatable("options.videoTitle");
/*     */   
/*  28 */   private static final Component IMPROVED_TRANSPARENCY = (Component)Component.translatable("options.improvedTransparency").withStyle(ChatFormatting.ITALIC);
/*  29 */   private static final Component WARNING_MESSAGE = (Component)Component.translatable("options.graphics.warning.message", new Object[] { IMPROVED_TRANSPARENCY, IMPROVED_TRANSPARENCY });
/*  30 */   private static final Component WARNING_TITLE = (Component)Component.translatable("options.graphics.warning.title").withStyle(ChatFormatting.RED);
/*  31 */   private static final Component BUTTON_ACCEPT = (Component)Component.translatable("options.graphics.warning.accept");
/*  32 */   private static final Component BUTTON_CANCEL = (Component)Component.translatable("options.graphics.warning.cancel");
/*  33 */   private static final Component DISPLAY_HEADER = (Component)Component.translatable("options.video.display.header");
/*  34 */   private static final Component QUALITY_HEADER = (Component)Component.translatable("options.video.quality.header");
/*  35 */   private static final Component PREFERENCES_HEADER = (Component)Component.translatable("options.video.preferences.header"); private final GpuWarnlistManager gpuWarnlistManager; private final int oldMipmaps; private final int oldAnisotropyBit; private final TextureFilteringMethod oldTextureFiltering;
/*     */   
/*     */   private static OptionInstance<?>[] qualityOptions(Options options) {
/*  38 */     return (OptionInstance<?>[])new OptionInstance[] { 
/*  39 */         options.biomeBlendRadius(), 
/*  40 */         options.renderDistance(), 
/*     */         
/*  42 */         options.prioritizeChunkUpdates(), 
/*  43 */         options.simulationDistance(), 
/*     */         
/*  45 */         options.ambientOcclusion(), 
/*  46 */         options.cloudStatus(), 
/*     */         
/*  48 */         options.particles(), 
/*  49 */         options.mipmapLevels(), 
/*     */         
/*  51 */         options.entityShadows(), 
/*  52 */         options.entityDistanceScaling(),
/*     */         
/*  54 */         options.menuBackgroundBlurriness(), 
/*  55 */         options.cloudRange(), 
/*     */         
/*  57 */         options.cutoutLeaves(), 
/*  58 */         options.improvedTransparency(), 
/*     */         
/*  60 */         options.textureFiltering(), 
/*  61 */         options.maxAnisotropyBit(), 
/*     */         
/*  63 */         options.weatherRadius() };
/*     */   }
/*     */ 
/*     */   
/*     */   private static OptionInstance<?>[] displayOptions(Options options) {
/*  68 */     return (OptionInstance<?>[])new OptionInstance[] {
/*  69 */         options.framerateLimit(), 
/*  70 */         options.enableVsync(), 
/*     */         
/*  72 */         options.inactivityFpsLimit(), 
/*  73 */         options.guiScale(), 
/*     */         
/*  75 */         options.fullscreen(), 
/*  76 */         options.gamma()
/*     */       };
/*     */   }
/*     */   
/*     */   private static OptionInstance<?>[] preferenceOptions(Options options) {
/*  81 */     return (OptionInstance<?>[])new OptionInstance[] {
/*  82 */         options.showAutosaveIndicator(), 
/*  83 */         options.vignette(), 
/*     */         
/*  85 */         options.attackIndicator(), 
/*  86 */         options.chunkSectionFadeInTime()
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VideoSettingsScreen(Screen lastScreen, Minecraft minecraft, Options options) {
/*  96 */     super(lastScreen, options, TITLE);
/*  97 */     this.gpuWarnlistManager = minecraft.getGpuWarnlistManager();
/*     */     
/*  99 */     this.gpuWarnlistManager.resetWarnings();
/* 100 */     if ((Boolean)options.improvedTransparency().get())
/*     */     {
/* 102 */       this.gpuWarnlistManager.dismissWarning();
/*     */     }
/*     */     
/* 105 */     this.oldMipmaps = (Integer)options.mipmapLevels().get();
/* 106 */     this.oldAnisotropyBit = (Integer)options.maxAnisotropyBit().get();
/* 107 */     this.oldTextureFiltering = (TextureFilteringMethod)options.textureFiltering().get();
/*     */   }
/*     */   
/*     */   protected void addOptions() {
/*     */     int initialValue;
/* 112 */     int CURRENT_MODE = -1;
/*     */     
/* 114 */     Window window = this.minecraft.getWindow();
/* 115 */     Monitor monitor = window.findBestMonitor();
/*     */     
/* 117 */     if (monitor == null) {
/* 118 */       initialValue = -1;
/*     */     } else {
/* 120 */       Optional<VideoMode> preferredFullscreenVideoMode = window.getPreferredFullscreenVideoMode();
/* 121 */       Objects.requireNonNull(monitor); initialValue = (Integer)preferredFullscreenVideoMode.<Integer>map(monitor::getVideoModeIndex).orElse(-1);
/*     */     } 
/*     */     
/* 124 */     OptionInstance<Integer> fullscreenOption = new OptionInstance("options.fullscreen.resolution", 
/*     */         
/* 126 */         OptionInstance.noTooltip(), (caption, value) -> { if (monitor == null) return Component.translatable("options.fullscreen.unavailable");  if (value == -1) return Options.genericValueLabel(caption, (Component)Component.translatable("options.fullscreen.current"));  VideoMode mode = monitor.getMode(value); return Options.genericValueLabel(caption, (Component)Component.translatable("options.fullscreen.entry", new Object[] { mode.getWidth(), mode.getHeight(), mode.getRefreshRate(), mode.getRedBits() + mode.getGreenBits() + mode.getBlueBits() })); }, (OptionInstance.ValueSet)new OptionInstance.IntRange(-1, 
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
/* 138 */           (monitor != null) ? (monitor.getModeCount() - 1) : -1), initialValue, value -> {
/*     */           if (monitor == null) {
/*     */             return;
/*     */           }
/*     */ 
/*     */           
/*     */           window.setPreferredFullscreenVideoMode((value == -1) ? Optional.empty() : Optional.<VideoMode>of(monitor.getMode(value)));
/*     */         });
/*     */ 
/*     */     
/* 148 */     this.list.addHeader(DISPLAY_HEADER);
/* 149 */     this.list.addBig(fullscreenOption);
/* 150 */     this.list.addSmall((OptionInstance[])displayOptions(this.options));
/*     */     
/* 152 */     this.list.addHeader(QUALITY_HEADER);
/* 153 */     this.list.addBig(this.options.graphicsPreset());
/* 154 */     this.list.addSmall((OptionInstance[])qualityOptions(this.options));
/*     */     
/* 156 */     this.list.addHeader(PREFERENCES_HEADER);
/* 157 */     this.list.addSmall((OptionInstance[])preferenceOptions(this.options));
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/* 162 */     if (this.list != null) { AbstractWidget abstractWidget = this.list.findOption(this.options.maxAnisotropyBit()); if (abstractWidget instanceof AbstractSliderButton) { AbstractSliderButton maxAnisotropy = (AbstractSliderButton)abstractWidget;
/* 163 */         maxAnisotropy.active = (this.options.textureFiltering().get() == TextureFilteringMethod.ANISOTROPIC); }
/*     */        }
/* 165 */      super.tick();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 170 */     this.minecraft.getWindow().changeFullscreenVideoMode();
/* 171 */     super.onClose();
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed() {
/* 176 */     if ((Integer)this.options.mipmapLevels().get() != this.oldMipmaps || (Integer)this.options.maxAnisotropyBit().get() != this.oldAnisotropyBit || this.options.textureFiltering().get() != this.oldTextureFiltering) {
/* 177 */       this.minecraft.updateMaxMipLevel((Integer)this.options.mipmapLevels().get());
/* 178 */       this.minecraft.delayTextureReload();
/*     */     } 
/* 180 */     super.removed();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 185 */     if (super.mouseClicked(event, doubleClick)) {
/* 186 */       if (this.gpuWarnlistManager.isShowingWarning()) {
/*     */         
/* 188 */         List<Component> warningMessage = Lists.newArrayList((Object[])new Component[] { WARNING_MESSAGE, CommonComponents.NEW_LINE });
/*     */         
/* 190 */         String rendererWarnings = this.gpuWarnlistManager.getRendererWarnings();
/* 191 */         if (rendererWarnings != null) {
/* 192 */           warningMessage.add(CommonComponents.NEW_LINE);
/* 193 */           warningMessage.add(Component.translatable("options.graphics.warning.renderer", new Object[] { rendererWarnings }).withStyle(ChatFormatting.GRAY));
/*     */         } 
/*     */         
/* 196 */         String vendorWarnings = this.gpuWarnlistManager.getVendorWarnings();
/* 197 */         if (vendorWarnings != null) {
/* 198 */           warningMessage.add(CommonComponents.NEW_LINE);
/* 199 */           warningMessage.add(Component.translatable("options.graphics.warning.vendor", new Object[] { vendorWarnings }).withStyle(ChatFormatting.GRAY));
/*     */         } 
/*     */         
/* 202 */         String versionWarnings = this.gpuWarnlistManager.getVersionWarnings();
/* 203 */         if (versionWarnings != null) {
/* 204 */           warningMessage.add(CommonComponents.NEW_LINE);
/* 205 */           warningMessage.add(Component.translatable("options.graphics.warning.version", new Object[] { versionWarnings }).withStyle(ChatFormatting.GRAY));
/*     */         } 
/*     */         
/* 208 */         this.minecraft.setScreen(new UnsupportedGraphicsWarningScreen(WARNING_TITLE, warningMessage, ImmutableList.of(new UnsupportedGraphicsWarningScreen.ButtonOption(BUTTON_ACCEPT, btn -> { this.options.improvedTransparency().set(true); (Minecraft.getInstance()).levelRenderer.allChanged(); this.gpuWarnlistManager.dismissWarning(); this.minecraft.setScreen(this); }), new UnsupportedGraphicsWarningScreen.ButtonOption(BUTTON_CANCEL, btn -> {
/*     */                     this.gpuWarnlistManager.dismissWarning();
/*     */ 
/*     */ 
/*     */                     
/*     */                     this.options.improvedTransparency().set(false);
/*     */ 
/*     */ 
/*     */                     
/*     */                     updateTransparencyButton();
/*     */ 
/*     */                     
/*     */                     this.minecraft.setScreen(this);
/*     */                   }))));
/*     */       } 
/*     */ 
/*     */       
/* 225 */       return true;
/*     */     } 
/* 227 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/* 232 */     if (this.minecraft.hasControlDown()) {
/* 233 */       OptionInstance<Integer> guiScale = this.options.guiScale();
/* 234 */       OptionInstance.ValueSet valueSet = guiScale.values(); if (valueSet instanceof OptionInstance.ClampingLazyMaxIntRange) { OptionInstance.ClampingLazyMaxIntRange clampingLazyMaxIntRange = (OptionInstance.ClampingLazyMaxIntRange)valueSet;
/* 235 */         int oldValue = (Integer)guiScale.get();
/* 236 */         int adjustedOldValue = (oldValue == 0) ? (clampingLazyMaxIntRange.maxInclusive() + 1) : oldValue;
/* 237 */         int newValue = adjustedOldValue + (int)Math.signum(scrollY);
/* 238 */         if (newValue != 0 && newValue <= clampingLazyMaxIntRange.maxInclusive() && newValue >= clampingLazyMaxIntRange.minInclusive()) {
/* 239 */           CycleButton<Integer> cycleButton = (CycleButton<Integer>)this.list.findOption(guiScale);
/* 240 */           if (cycleButton != null) {
/* 241 */             guiScale.set(newValue);
/* 242 */             cycleButton.setValue(newValue);
/* 243 */             this.list.setScrollAmount(0.0D);
/* 244 */             return true;
/*     */           } 
/*     */         }  }
/*     */       
/* 248 */       return false;
/*     */     } 
/* 250 */     return super.mouseScrolled(x, y, scrollX, scrollY);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateFullscreenButton(boolean fullscreen) {
/* 255 */     if (this.list != null) {
/* 256 */       AbstractWidget fullscreenWidget = this.list.findOption(this.options.fullscreen());
/* 257 */       if (fullscreenWidget != null) {
/* 258 */         CycleButton<Boolean> fullscreenButton = (CycleButton<Boolean>)fullscreenWidget;
/* 259 */         fullscreenButton.setValue(fullscreen);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public void updateTransparencyButton() {
/* 265 */     if (this.list != null) {
/* 266 */       OptionInstance<Boolean> option = this.options.improvedTransparency();
/* 267 */       AbstractWidget widget = this.list.findOption(option);
/* 268 */       if (widget != null) {
/* 269 */         CycleButton<Boolean> button = (CycleButton<Boolean>)widget;
/* 270 */         button.setValue(option.get());
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/options/VideoSettingsScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */