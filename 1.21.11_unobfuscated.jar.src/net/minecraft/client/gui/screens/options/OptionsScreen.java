/*     */ package net.minecraft.client.gui.screens.options;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.LockIconButton;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.EqualSpacingLayout;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.CreditsAndAttributionScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.options.controls.ControlsScreen;
/*     */ import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
/*     */ import net.minecraft.client.gui.screens.telemetry.TelemetryInfoScreen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.protocol.Packet;
/*     */ import net.minecraft.network.protocol.game.ServerboundChangeDifficultyPacket;
/*     */ import net.minecraft.network.protocol.game.ServerboundLockDifficultyPacket;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.world.Difficulty;
/*     */ 
/*     */ public class OptionsScreen extends Screen {
/*  33 */   private static final Component TITLE = (Component)Component.translatable("options.title");
/*  34 */   private static final Component SKIN_CUSTOMIZATION = (Component)Component.translatable("options.skinCustomisation");
/*  35 */   private static final Component SOUNDS = (Component)Component.translatable("options.sounds");
/*  36 */   private static final Component VIDEO = (Component)Component.translatable("options.video");
/*  37 */   public static final Component CONTROLS = (Component)Component.translatable("options.controls");
/*  38 */   private static final Component LANGUAGE = (Component)Component.translatable("options.language");
/*  39 */   private static final Component CHAT = (Component)Component.translatable("options.chat");
/*  40 */   private static final Component RESOURCEPACK = (Component)Component.translatable("options.resourcepack");
/*  41 */   private static final Component ACCESSIBILITY = (Component)Component.translatable("options.accessibility");
/*  42 */   private static final Component TELEMETRY = (Component)Component.translatable("options.telemetry");
/*  43 */   private static final Tooltip TELEMETRY_DISABLED_TOOLTIP = Tooltip.create((Component)Component.translatable("options.telemetry.disabled"));
/*  44 */   private static final Component CREDITS_AND_ATTRIBUTION = (Component)Component.translatable("options.credits_and_attribution");
/*     */   
/*     */   private static final int COLUMNS = 2;
/*     */   
/*  48 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this, 61, 33);
/*     */   
/*     */   private final Screen lastScreen;
/*     */   
/*     */   private final Options options;
/*     */   
/*     */   private CycleButton<Difficulty> difficultyButton;
/*     */   private LockIconButton lockButton;
/*     */   
/*     */   public OptionsScreen(Screen lastScreen, Options options) {
/*  58 */     super(TITLE);
/*  59 */     this.lastScreen = lastScreen;
/*  60 */     this.options = options;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  65 */     LinearLayout header = (LinearLayout)this.layout.addToHeader((LayoutElement)LinearLayout.vertical().spacing(8));
/*  66 */     header.addChild((LayoutElement)new StringWidget(TITLE, this.font), LayoutSettings::alignHorizontallyCenter);
/*  67 */     LinearLayout subHeader = ((LinearLayout)header.addChild((LayoutElement)LinearLayout.horizontal())).spacing(8);
/*  68 */     subHeader.addChild((LayoutElement)this.options.fov().createButton(this.minecraft.options));
/*  69 */     subHeader.addChild(createOnlineButton());
/*     */     
/*  71 */     GridLayout gridLayout = new GridLayout();
/*  72 */     gridLayout.defaultCellSetting().paddingHorizontal(4).paddingBottom(4).alignHorizontallyCenter();
/*     */     
/*  74 */     GridLayout.RowHelper helper = gridLayout.createRowHelper(2);
/*     */     
/*  76 */     helper.addChild((LayoutElement)openScreenButton(SKIN_CUSTOMIZATION, () -> new SkinCustomizationScreen(this, this.options)));
/*  77 */     helper.addChild((LayoutElement)openScreenButton(SOUNDS, () -> new SoundOptionsScreen(this, this.options)));
/*  78 */     helper.addChild((LayoutElement)openScreenButton(VIDEO, () -> new VideoSettingsScreen(this, this.minecraft, this.options)));
/*  79 */     helper.addChild((LayoutElement)openScreenButton(CONTROLS, () -> new ControlsScreen(this, this.options)));
/*  80 */     helper.addChild((LayoutElement)openScreenButton(LANGUAGE, () -> new LanguageSelectScreen(this, this.options, this.minecraft.getLanguageManager())));
/*  81 */     helper.addChild((LayoutElement)openScreenButton(CHAT, () -> new ChatOptionsScreen(this, this.options)));
/*  82 */     helper.addChild((LayoutElement)openScreenButton(RESOURCEPACK, () -> new PackSelectionScreen(this.minecraft.getResourcePackRepository(), this::applyPacks, this.minecraft.getResourcePackDirectory(), (Component)Component.translatable("resourcePack.title"))));
/*  83 */     helper.addChild((LayoutElement)openScreenButton(ACCESSIBILITY, () -> new AccessibilityOptionsScreen(this, this.options)));
/*  84 */     Button telemetryButton = (Button)helper.addChild((LayoutElement)openScreenButton(TELEMETRY, () -> new TelemetryInfoScreen(this, this.options)));
/*  85 */     if (!this.minecraft.allowsTelemetry()) {
/*  86 */       telemetryButton.active = false;
/*  87 */       telemetryButton.setTooltip(TELEMETRY_DISABLED_TOOLTIP);
/*     */     } 
/*  89 */     helper.addChild((LayoutElement)openScreenButton(CREDITS_AND_ATTRIBUTION, () -> new CreditsAndAttributionScreen(this)));
/*     */     
/*  91 */     this.layout.addToContents((LayoutElement)gridLayout);
/*  92 */     this.layout.addToFooter((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onClose()).width(200).build());
/*     */     
/*  94 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*  95 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/* 100 */     this.layout.arrangeElements();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 105 */     this.minecraft.setScreen(this.lastScreen);
/*     */   }
/*     */   
/*     */   private void applyPacks(PackRepository packRepository) {
/* 109 */     this.options.updateResourcePacks(packRepository);
/* 110 */     this.minecraft.setScreen(this);
/*     */   }
/*     */   
/*     */   private LayoutElement createOnlineButton() {
/* 114 */     if (this.minecraft.level != null && this.minecraft.hasSingleplayerServer()) {
/* 115 */       this.difficultyButton = createDifficultyButton(0, 0, "options.difficulty", this.minecraft);
/*     */       
/* 117 */       if (!this.minecraft.level.getLevelData().isHardcore()) {
/* 118 */         this.lockButton = new LockIconButton(0, 0, button -> this.minecraft.setScreen((Screen)new ConfirmScreen(this::lockCallback, (Component)Component.translatable("difficulty.lock.title"), (Component)Component.translatable("difficulty.lock.question", new Object[] { this.minecraft.level.getLevelData().getDifficulty().getDisplayName() }))));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 125 */         this.difficultyButton.setWidth(this.difficultyButton.getWidth() - this.lockButton.getWidth());
/*     */         
/* 127 */         this.lockButton.setLocked(this.minecraft.level.getLevelData().isDifficultyLocked());
/* 128 */         this.lockButton.active = !this.lockButton.isLocked();
/* 129 */         this.difficultyButton.active = !this.lockButton.isLocked();
/*     */         
/* 131 */         EqualSpacingLayout linearLayout = new EqualSpacingLayout(150, 0, EqualSpacingLayout.Orientation.HORIZONTAL);
/* 132 */         linearLayout.addChild((LayoutElement)this.difficultyButton);
/* 133 */         linearLayout.addChild((LayoutElement)this.lockButton);
/*     */         
/* 135 */         return (LayoutElement)linearLayout;
/*     */       } 
/* 137 */       this.difficultyButton.active = false;
/* 138 */       return (LayoutElement)this.difficultyButton;
/*     */     } 
/*     */     
/* 141 */     return (LayoutElement)Button.builder((Component)Component.translatable("options.online"), button -> this.minecraft.setScreen(new OnlineOptionsScreen(this, this.options))).bounds(this.width / 2 + 5, this.height / 6 - 12 + 24, 150, 20).build();
/*     */   }
/*     */ 
/*     */   
/*     */   public static CycleButton<Difficulty> createDifficultyButton(int x, int y, String title, Minecraft minecraft) {
/* 146 */     return CycleButton.builder(Difficulty::getDisplayName, minecraft.level.getDifficulty())
/* 147 */       .withValues((Object[])Difficulty.values())
/* 148 */       .create(x, y, 150, 20, (Component)Component.translatable(title), (button, value) -> minecraft.getConnection().send((Packet)new ServerboundChangeDifficultyPacket(value)));
/*     */   }
/*     */ 
/*     */   
/*     */   private void lockCallback(boolean result) {
/* 153 */     this.minecraft.setScreen(this);
/* 154 */     if (result && this.minecraft.level != null && this.lockButton != null && this.difficultyButton != null) {
/* 155 */       this.minecraft.getConnection().send((Packet)new ServerboundLockDifficultyPacket(true));
/* 156 */       this.lockButton.setLocked(true);
/* 157 */       this.lockButton.active = false;
/* 158 */       this.difficultyButton.active = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed() {
/* 164 */     this.options.save();
/*     */   }
/*     */   
/*     */   private Button openScreenButton(Component message, Supplier<Screen> screenToScreen) {
/* 168 */     return Button.builder(message, button -> this.minecraft.setScreen(screenToScreen.get())).build();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/options/OptionsScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */