/*     */ package net.minecraft.client.gui.screens;
/*     */ 
/*     */ import com.mojang.authlib.minecraft.BanDetails;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.realmsclient.RealmsMainScreen;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
/*     */ import java.io.IOException;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CommonButtons;
/*     */ import net.minecraft.client.gui.components.LogoRenderer;
/*     */ import net.minecraft.client.gui.components.PlainTextButton;
/*     */ import net.minecraft.client.gui.components.SplashRenderer;
/*     */ import net.minecraft.client.gui.components.SpriteIconButton;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
/*     */ import net.minecraft.client.gui.screens.multiplayer.SafetyScreen;
/*     */ import net.minecraft.client.gui.screens.options.AccessibilityOptionsScreen;
/*     */ import net.minecraft.client.gui.screens.options.LanguageSelectScreen;
/*     */ import net.minecraft.client.gui.screens.options.OptionsScreen;
/*     */ import net.minecraft.client.gui.screens.worldselection.CreateWorldScreen;
/*     */ import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.PanoramaRenderer;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.server.MinecraftServer;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class TitleScreen
/*     */   extends Screen
/*     */ {
/*  45 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  47 */   private static final Component TITLE = (Component)Component.translatable("narrator.screen.title");
/*  48 */   private static final Component COPYRIGHT_TEXT = (Component)Component.translatable("title.credits");
/*     */   
/*     */   private static final String DEMO_LEVEL_ID = "Demo_World";
/*     */   
/*     */   private SplashRenderer splash;
/*     */   
/*     */   private RealmsNotificationsScreen realmsNotificationsScreen;
/*     */   
/*     */   private boolean fading;
/*     */   private long fadeInStart;
/*     */   private final LogoRenderer logoRenderer;
/*     */   
/*     */   public TitleScreen() {
/*  61 */     this(false);
/*     */   }
/*     */   
/*     */   public TitleScreen(boolean fading) {
/*  65 */     this(fading, null);
/*     */   }
/*     */   
/*     */   public TitleScreen(boolean fading, LogoRenderer logoRenderer) {
/*  69 */     super(TITLE);
/*  70 */     this.fading = fading;
/*  71 */     this.logoRenderer = Objects.<LogoRenderer>requireNonNullElseGet(logoRenderer, () -> new LogoRenderer(false));
/*     */   }
/*     */   
/*     */   private boolean realmsNotificationsEnabled() {
/*  75 */     return (this.realmsNotificationsScreen != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  80 */     if (realmsNotificationsEnabled()) {
/*  81 */       this.realmsNotificationsScreen.tick();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void registerTextures(TextureManager textureManager) {
/*  86 */     textureManager.registerForNextReload(LogoRenderer.MINECRAFT_LOGO);
/*  87 */     textureManager.registerForNextReload(LogoRenderer.MINECRAFT_EDITION);
/*  88 */     textureManager.registerForNextReload(PanoramaRenderer.PANORAMA_OVERLAY);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPauseScreen() {
/*  93 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldCloseOnEsc() {
/*  98 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/* 103 */     if (this.splash == null) {
/* 104 */       this.splash = this.minecraft.getSplashManager().getSplash();
/*     */     }
/* 106 */     int copyrightWidth = this.font.width((FormattedText)COPYRIGHT_TEXT);
/* 107 */     int copyrightX = this.width - copyrightWidth - 2;
/*     */     
/* 109 */     int spacing = 24;
/* 110 */     int topPos = this.height / 4 + 48;
/*     */     
/* 112 */     if (this.minecraft.isDemo()) {
/* 113 */       topPos = createDemoMenuOptions(topPos, 24);
/*     */     } else {
/* 115 */       topPos = createNormalMenuOptions(topPos, 24);
/*     */     } 
/* 117 */     topPos = createTestWorldButton(topPos, 24);
/*     */     
/* 119 */     SpriteIconButton language = addRenderableWidget(CommonButtons.language(20, button -> this.minecraft.setScreen((Screen)new LanguageSelectScreen(this, this.minecraft.options, this.minecraft.getLanguageManager())), true));
/* 120 */     topPos += 36; language.setPosition(this.width / 2 - 124, topPos);
/* 121 */     addRenderableWidget(Button.builder((Component)Component.translatable("menu.options"), button -> this.minecraft.setScreen((Screen)new OptionsScreen(this, this.minecraft.options))).bounds(this.width / 2 - 100, topPos, 98, 20).build());
/* 122 */     addRenderableWidget(Button.builder((Component)Component.translatable("menu.quit"), button -> this.minecraft.stop()).bounds(this.width / 2 + 2, topPos, 98, 20).build());
/* 123 */     SpriteIconButton accessibility = addRenderableWidget(CommonButtons.accessibility(20, button -> this.minecraft.setScreen((Screen)new AccessibilityOptionsScreen(this, this.minecraft.options)), true));
/* 124 */     accessibility.setPosition(this.width / 2 + 104, topPos);
/* 125 */     addRenderableWidget(new PlainTextButton(copyrightX, this.height - 10, copyrightWidth, 10, COPYRIGHT_TEXT, button -> this.minecraft.setScreen(new CreditsAndAttributionScreen(this)), this.font));
/*     */     
/* 127 */     if (this.realmsNotificationsScreen == null) {
/* 128 */       this.realmsNotificationsScreen = new RealmsNotificationsScreen();
/*     */     }
/*     */     
/* 131 */     if (realmsNotificationsEnabled()) {
/* 132 */       this.realmsNotificationsScreen.init(this.width, this.height);
/*     */     }
/*     */   }
/*     */   
/*     */   private int createTestWorldButton(int topPos, int spacing) {
/* 137 */     if (SharedConstants.IS_RUNNING_IN_IDE) {
/* 138 */       addRenderableWidget(Button.builder(
/* 139 */             (Component)Component.literal("Create Test World"), button -> CreateWorldScreen.testWorld(this.minecraft, ()))
/*     */           
/* 141 */           .bounds(this.width / 2 - 100, topPos += spacing, 200, 20).build());
/*     */     }
/*     */     
/* 144 */     return topPos;
/*     */   }
/*     */   
/*     */   private int createNormalMenuOptions(int topPos, int spacing) {
/* 148 */     addRenderableWidget(Button.builder((Component)Component.translatable("menu.singleplayer"), button -> this.minecraft.setScreen((Screen)new SelectWorldScreen(this))).bounds(this.width / 2 - 100, topPos, 200, 20).build());
/* 149 */     Component multiplayerDisabledReason = getMultiplayerDisabledReason();
/* 150 */     boolean multiplayerAllowed = (multiplayerDisabledReason == null);
/*     */     
/* 152 */     Tooltip tooltip = (multiplayerDisabledReason != null) ? Tooltip.create(multiplayerDisabledReason) : null;
/* 153 */     ((Button)addRenderableWidget((T)Button.builder((Component)Component.translatable("menu.multiplayer"), button -> {
/*     */             Screen screen = this.minecraft.options.skipMultiplayerWarning ? (Screen)new JoinMultiplayerScreen(this) : (Screen)new SafetyScreen(this);
/*     */             
/*     */             this.minecraft.setScreen(screen);
/* 157 */           }).bounds(this.width / 2 - 100, topPos += spacing, 200, 20).tooltip(tooltip).build())).active = multiplayerAllowed;
/* 158 */     ((Button)addRenderableWidget((T)Button.builder((Component)Component.translatable("menu.online"), button -> this.minecraft.setScreen((Screen)new RealmsMainScreen(this))).bounds(this.width / 2 - 100, topPos += spacing, 200, 20).tooltip(tooltip).build())).active = multiplayerAllowed;
/* 159 */     return topPos;
/*     */   }
/*     */   
/*     */   private Component getMultiplayerDisabledReason() {
/* 163 */     if (this.minecraft.allowsMultiplayer()) {
/* 164 */       return null;
/*     */     }
/*     */     
/* 167 */     if (this.minecraft.isNameBanned()) {
/* 168 */       return (Component)Component.translatable("title.multiplayer.disabled.banned.name");
/*     */     }
/*     */     
/* 171 */     BanDetails multiplayerBan = this.minecraft.multiplayerBan();
/* 172 */     if (multiplayerBan != null) {
/* 173 */       if (multiplayerBan.expires() != null) {
/* 174 */         return (Component)Component.translatable("title.multiplayer.disabled.banned.temporary");
/*     */       }
/* 176 */       return (Component)Component.translatable("title.multiplayer.disabled.banned.permanent");
/*     */     } 
/*     */ 
/*     */     
/* 180 */     return (Component)Component.translatable("title.multiplayer.disabled");
/*     */   }
/*     */   
/*     */   private int createDemoMenuOptions(int topPos, int spacing) {
/* 184 */     boolean demoWorldPresent = checkDemoWorldPresence();
/*     */     
/* 186 */     addRenderableWidget(Button.builder((Component)Component.translatable("menu.playdemo"), button -> {
/*     */             if (demoWorldPresent) {
/*     */               this.minecraft.createWorldOpenFlows().openWorld("Demo_World", ());
/*     */             } else {
/*     */               this.minecraft.createWorldOpenFlows().createFreshLevel("Demo_World", MinecraftServer.DEMO_SETTINGS, WorldOptions.DEMO_OPTIONS, WorldPresets::createNormalWorldDimensions, this);
/*     */             } 
/* 192 */           }).bounds(this.width / 2 - 100, topPos, 200, 20).build());
/* 193 */     Button resetDemoButton = addRenderableWidget(Button.builder((Component)Component.translatable("menu.resetdemo"), button -> { LevelStorageSource levelSource = this.minecraft.getLevelSource(); try { LevelStorageSource.LevelStorageAccess levelAccess = levelSource.createAccess("Demo_World"); try { if (levelAccess.hasWorldData())
/*     */                   this.minecraft.setScreen(new ConfirmScreen(this::confirmDemo, (Component)Component.translatable("selectWorld.deleteQuestion"), (Component)Component.translatable("selectWorld.deleteWarning", new Object[] { MinecraftServer.DEMO_SETTINGS.levelName() }), (Component)Component.translatable("selectWorld.deleteButton"), CommonComponents.GUI_CANCEL));  if (levelAccess != null)
/*     */                   levelAccess.close();  }
/* 196 */               catch (Throwable throwable) { if (levelAccess != null) try { levelAccess.close(); } catch (Throwable throwable1)
/*     */                   { throwable.addSuppressed(throwable1); }
/*     */                 
/*     */ 
/*     */ 
/*     */ 
/*     */                 
/*     */                 throw throwable; }
/*     */                }
/* 205 */             catch (IOException e)
/*     */             { SystemToast.onWorldAccessFailure(this.minecraft, "Demo_World");
/*     */               LOGGER.warn("Failed to access demo world", e); }
/*     */           
/* 209 */           }).bounds(this.width / 2 - 100, topPos += spacing, 200, 20).build());
/* 210 */     resetDemoButton.active = demoWorldPresent;
/* 211 */     return topPos;
/*     */   }
/*     */   
/*     */   private boolean checkDemoWorldPresence() {
/*     */     
/* 216 */     try { LevelStorageSource.LevelStorageAccess levelSource = this.minecraft.getLevelSource().createAccess("Demo_World"); 
/* 217 */       try { boolean bool = levelSource.hasWorldData();
/* 218 */         if (levelSource != null) levelSource.close();  return bool; } catch (Throwable throwable) { if (levelSource != null) try { levelSource.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 219 */     { SystemToast.onWorldAccessFailure(this.minecraft, "Demo_World");
/* 220 */       LOGGER.warn("Failed to read demo world data", e);
/*     */       
/* 222 */       return false; }
/*     */   
/*     */   }
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 227 */     if (this.fadeInStart == 0L && this.fading) {
/* 228 */       this.fadeInStart = Util.getMillis();
/*     */     }
/*     */     
/* 231 */     float widgetFade = 1.0F;
/* 232 */     if (this.fading) {
/* 233 */       float fade = (float)(Util.getMillis() - this.fadeInStart) / 2000.0F;
/* 234 */       if (fade > 1.0F) {
/* 235 */         this.fading = false;
/*     */       } else {
/* 237 */         fade = Mth.clamp(fade, 0.0F, 1.0F);
/* 238 */         widgetFade = Mth.clampedMap(fade, 0.5F, 1.0F, 0.0F, 1.0F);
/*     */       } 
/* 240 */       fadeWidgets(widgetFade);
/*     */     } 
/* 242 */     renderPanorama(graphics, a);
/*     */     
/* 244 */     super.render(graphics, mouseX, mouseY, a);
/* 245 */     this.logoRenderer.renderLogo(graphics, this.width, this.logoRenderer.keepLogoThroughFade() ? 1.0F : widgetFade);
/*     */     
/* 247 */     if (this.splash != null && !((Boolean)this.minecraft.options.hideSplashTexts().get())) {
/* 248 */       this.splash.render(graphics, this.width, this.font, widgetFade);
/*     */     }
/*     */     
/* 251 */     String versionString = "Minecraft " + SharedConstants.getCurrentVersion().name();
/*     */     
/* 253 */     if (this.minecraft.isDemo()) {
/* 254 */       versionString = versionString + " Demo";
/*     */     } else {
/* 256 */       versionString = versionString + versionString;
/*     */     } 
/*     */     
/* 259 */     if (Minecraft.checkModStatus().shouldReportAsModified()) {
/* 260 */       versionString = versionString + versionString;
/*     */     }
/*     */     
/* 263 */     graphics.drawString(this.font, versionString, 2, this.height - 10, ARGB.white(widgetFade));
/*     */     
/* 265 */     if (realmsNotificationsEnabled() && widgetFade >= 1.0F) {
/* 266 */       this.realmsNotificationsScreen.render(graphics, mouseX, mouseY, a);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float a) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 277 */     if (super.mouseClicked(event, doubleClick)) {
/* 278 */       return true;
/*     */     }
/*     */     
/* 281 */     if (realmsNotificationsEnabled() && this.realmsNotificationsScreen.mouseClicked(event, doubleClick)) {
/* 282 */       return true;
/*     */     }
/*     */     
/* 285 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed() {
/* 290 */     if (this.realmsNotificationsScreen != null) {
/* 291 */       this.realmsNotificationsScreen.removed();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void added() {
/* 297 */     super.added();
/* 298 */     if (this.realmsNotificationsScreen != null) {
/* 299 */       this.realmsNotificationsScreen.added();
/*     */     }
/*     */   }
/*     */   
/*     */   private void confirmDemo(boolean result) {
/* 304 */     if (result) {
/*     */       
/* 306 */       try { LevelStorageSource.LevelStorageAccess levelSource = this.minecraft.getLevelSource().createAccess("Demo_World"); 
/* 307 */         try { levelSource.deleteLevel();
/* 308 */           if (levelSource != null) levelSource.close();  } catch (Throwable throwable) { if (levelSource != null) try { levelSource.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 309 */       { SystemToast.onWorldDeleteFailure(this.minecraft, "Demo_World");
/* 310 */         LOGGER.warn("Failed to delete demo world", e); }
/*     */     
/*     */     }
/* 313 */     this.minecraft.setScreen(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean canInterruptWithAnotherScreen() {
/* 318 */     return true;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/TitleScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */