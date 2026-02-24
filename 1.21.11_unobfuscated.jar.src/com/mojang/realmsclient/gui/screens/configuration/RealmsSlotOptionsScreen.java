/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.RealmsSlot;
/*     */ import com.mojang.realmsclient.dto.RealmsWorldOptions;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsPopups;
/*     */ import java.util.List;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSliderButton;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.PopupScreen;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.realms.RealmsLabel;
/*     */ import net.minecraft.realms.RealmsScreen;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ 
/*     */ public class RealmsSlotOptionsScreen extends RealmsScreen {
/*  28 */   public static final List<Difficulty> DIFFICULTIES = (List<Difficulty>)ImmutableList.of(Difficulty.PEACEFUL, Difficulty.EASY, Difficulty.NORMAL, Difficulty.HARD);
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_DIFFICULTY = 2;
/*     */ 
/*     */   
/*     */   private static final int DEFAULT_GAME_MODE = 0;
/*     */   
/*  36 */   public static final List<GameType> GAME_MODES = (List<GameType>)ImmutableList.of(GameType.SURVIVAL, GameType.CREATIVE, GameType.ADVENTURE);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  42 */   private static final Component NAME_LABEL = (Component)Component.translatable("mco.configure.world.edit.slot.name");
/*  43 */   private static final Component SPAWN_PROTECTION_TEXT = (Component)Component.translatable("mco.configure.world.spawnProtection");
/*     */   
/*     */   private EditBox nameEdit;
/*     */   
/*     */   protected final RealmsConfigureWorldScreen parentScreen;
/*     */   
/*     */   private int column1X;
/*     */   
/*     */   private int columnWidth;
/*     */   
/*     */   private final RealmsSlot slot;
/*     */   
/*     */   private final RealmsServer.WorldType worldType;
/*     */   
/*     */   private Difficulty difficulty;
/*     */   private GameType gameMode;
/*     */   private final String defaultSlotName;
/*     */   private String worldName;
/*     */   private int spawnProtection;
/*     */   private boolean forceGameMode;
/*     */   private SettingsSlider spawnProtectionButton;
/*     */   
/*     */   public RealmsSlotOptionsScreen(RealmsConfigureWorldScreen configureWorldScreen, RealmsSlot slot, RealmsServer.WorldType worldType, int activeSlot) {
/*  66 */     super((Component)Component.translatable("mco.configure.world.buttons.options"));
/*  67 */     this.parentScreen = configureWorldScreen;
/*  68 */     this.slot = slot;
/*  69 */     this.worldType = worldType;
/*     */     
/*  71 */     this.difficulty = findByIndex(DIFFICULTIES, slot.options.difficulty, 2);
/*  72 */     this.gameMode = findByIndex(GAME_MODES, slot.options.gameMode, 0);
/*     */     
/*  74 */     this.defaultSlotName = slot.options.getDefaultSlotName(activeSlot);
/*  75 */     setWorldName(slot.options.getSlotName(activeSlot));
/*     */     
/*  77 */     if (worldType == RealmsServer.WorldType.NORMAL) {
/*  78 */       this.spawnProtection = slot.options.spawnProtection;
/*  79 */       this.forceGameMode = slot.options.forceGameMode;
/*     */     } else {
/*  81 */       this.spawnProtection = 0;
/*  82 */       this.forceGameMode = false;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/*  88 */     this.minecraft.setScreen((Screen)this.parentScreen);
/*     */   }
/*     */   
/*     */   private static <T> T findByIndex(List<T> values, int index, int defaultIndex) {
/*     */     try {
/*  93 */       return values.get(index);
/*  94 */     } catch (IndexOutOfBoundsException e) {
/*  95 */       return values.get(defaultIndex);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static <T> int findIndex(List<T> values, T value, int defaultIndex) {
/* 100 */     int result = values.indexOf(value);
/* 101 */     return (result == -1) ? defaultIndex : result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/* 106 */     this.columnWidth = 170;
/* 107 */     this.column1X = this.width / 2 - this.columnWidth;
/* 108 */     int column2X = this.width / 2 + 10;
/*     */     
/* 110 */     if (this.worldType != RealmsServer.WorldType.NORMAL) {
/*     */       MutableComponent mutableComponent;
/* 112 */       if (this.worldType == RealmsServer.WorldType.ADVENTUREMAP) {
/* 113 */         mutableComponent = Component.translatable("mco.configure.world.edit.subscreen.adventuremap");
/* 114 */       } else if (this.worldType == RealmsServer.WorldType.INSPIRATION) {
/* 115 */         mutableComponent = Component.translatable("mco.configure.world.edit.subscreen.inspiration");
/*     */       } else {
/* 117 */         mutableComponent = Component.translatable("mco.configure.world.edit.subscreen.experience");
/*     */       } 
/* 119 */       addLabel(new RealmsLabel((Component)mutableComponent, this.width / 2, 26, -65536));
/*     */     } 
/*     */     
/* 122 */     this.nameEdit = (EditBox)addWidget((GuiEventListener)new EditBox(this.minecraft.font, this.column1X, row(1), this.columnWidth, 20, null, (Component)Component.translatable("mco.configure.world.edit.slot.name")));
/* 123 */     this.nameEdit.setValue(this.worldName);
/* 124 */     this.nameEdit.setResponder(this::setWorldName);
/*     */     
/* 126 */     CycleButton<Difficulty> difficultyCycleButton = (CycleButton<Difficulty>)addRenderableWidget((GuiEventListener)CycleButton.builder(Difficulty::getDisplayName, this.difficulty)
/* 127 */         .withValues(DIFFICULTIES)
/* 128 */         .create(column2X, row(1), this.columnWidth, 20, (Component)Component.translatable("options.difficulty"), (button, value) -> this.difficulty = value));
/*     */ 
/*     */     
/* 131 */     CycleButton<GameType> gameTypeCycleButton = (CycleButton<GameType>)addRenderableWidget((GuiEventListener)CycleButton.builder(GameType::getShortDisplayName, this.gameMode)
/* 132 */         .withValues(GAME_MODES)
/* 133 */         .create(this.column1X, row(3), this.columnWidth, 20, (Component)Component.translatable("selectWorld.gameMode"), (button, value) -> this.gameMode = value));
/*     */ 
/*     */     
/* 136 */     CycleButton<Boolean> forceGameModeButton = (CycleButton<Boolean>)addRenderableWidget((GuiEventListener)CycleButton.onOffBuilder(this.forceGameMode).create(column2X, row(3), this.columnWidth, 20, (Component)Component.translatable("mco.configure.world.forceGameMode"), (button, value) -> this.forceGameMode = value));
/*     */     
/* 138 */     this.spawnProtectionButton = (SettingsSlider)addRenderableWidget((GuiEventListener)new SettingsSlider(this.column1X, row(5), this.columnWidth, this.spawnProtection, 0.0F, 16.0F));
/*     */     
/* 140 */     if (this.worldType != RealmsServer.WorldType.NORMAL) {
/* 141 */       this.spawnProtectionButton.active = false;
/* 142 */       forceGameModeButton.active = false;
/*     */     } 
/*     */     
/* 145 */     if (this.slot.isHardcore()) {
/* 146 */       difficultyCycleButton.active = false;
/* 147 */       gameTypeCycleButton.active = false;
/* 148 */       forceGameModeButton.active = false;
/*     */     } 
/*     */     
/* 151 */     addRenderableWidget((GuiEventListener)Button.builder((Component)Component.translatable("mco.configure.world.buttons.done"), button -> saveSettings()).bounds(this.column1X, row(13), this.columnWidth, 20).build());
/* 152 */     addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_CANCEL, button -> onClose()).bounds(column2X, row(13), this.columnWidth, 20).build());
/*     */   }
/*     */   
/*     */   private CycleButton.OnValueChange<Boolean> confirmDangerousOption(Component message, Consumer<Boolean> setter) {
/* 156 */     return (button, value) -> {
/*     */         if (value) {
/*     */           setter.accept(true);
/*     */         } else {
/*     */           this.minecraft.setScreen((Screen)RealmsPopups.warningPopupScreen((Screen)this, setter, ()));
/*     */         } 
/*     */       };
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
/*     */   public Component getNarrationMessage() {
/* 176 */     return (Component)CommonComponents.joinForNarration(new Component[] { getTitle(), createLabelNarration() });
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int xm, int ym, float a) {
/* 181 */     super.render(graphics, xm, ym, a);
/*     */     
/* 183 */     graphics.drawCenteredString(this.font, this.title, this.width / 2, 17, -1);
/* 184 */     graphics.drawString(this.font, NAME_LABEL, this.column1X + this.columnWidth / 2 - this.font.width((FormattedText)NAME_LABEL) / 2, row(0) - 5, -1);
/*     */     
/* 186 */     this.nameEdit.render(graphics, xm, ym, a);
/*     */   }
/*     */   
/*     */   private void setWorldName(String value) {
/* 190 */     if (value.equals(this.defaultSlotName)) {
/* 191 */       this.worldName = "";
/*     */     } else {
/* 193 */       this.worldName = value;
/*     */     } 
/*     */   }
/*     */   
/*     */   private class SettingsSlider extends AbstractSliderButton {
/*     */     private final double minValue;
/*     */     private final double maxValue;
/*     */     
/*     */     public SettingsSlider(int x, int y, int width, int currentValue, float minValue, float maxValue) {
/* 202 */       super(x, y, width, 20, CommonComponents.EMPTY, 0.0D);
/*     */       
/* 204 */       this.minValue = minValue;
/* 205 */       this.maxValue = maxValue;
/*     */       
/* 207 */       this.value = ((Mth.clamp(currentValue, minValue, maxValue) - minValue) / (maxValue - minValue));
/*     */       
/* 209 */       updateMessage();
/*     */     }
/*     */ 
/*     */     
/*     */     public void applyValue() {
/* 214 */       if (!RealmsSlotOptionsScreen.this.spawnProtectionButton.active) {
/*     */         return;
/*     */       }
/*     */       
/* 218 */       RealmsSlotOptionsScreen.this.spawnProtection = (int)Mth.lerp(Mth.clamp(this.value, 0.0D, 1.0D), this.minValue, this.maxValue);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void updateMessage() {
/* 223 */       setMessage((Component)CommonComponents.optionNameValue(RealmsSlotOptionsScreen.SPAWN_PROTECTION_TEXT, (RealmsSlotOptionsScreen.this.spawnProtection == 0) ? CommonComponents.OPTION_OFF : (Component)Component.literal(String.valueOf(RealmsSlotOptionsScreen.this.spawnProtection))));
/*     */     }
/*     */   }
/*     */   
/*     */   private void saveSettings() {
/* 228 */     int difficultyId = findIndex(DIFFICULTIES, this.difficulty, 2);
/* 229 */     int gameModeId = findIndex(GAME_MODES, this.gameMode, 0);
/*     */     
/* 231 */     if (this.worldType == RealmsServer.WorldType.ADVENTUREMAP || this.worldType == RealmsServer.WorldType.EXPERIENCE || this.worldType == RealmsServer.WorldType.INSPIRATION) {
/* 232 */       this.parentScreen.saveSlotSettings(new RealmsSlot(this.slot.slotId, new RealmsWorldOptions(this.slot.options.spawnProtection, difficultyId, gameModeId, this.slot.options.forceGameMode, this.worldName, this.slot.options.version, this.slot.options.compatibility), this.slot.settings));
/*     */     } else {
/* 234 */       this.parentScreen.saveSlotSettings(new RealmsSlot(this.slot.slotId, new RealmsWorldOptions(this.spawnProtection, difficultyId, gameModeId, this.forceGameMode, this.worldName, this.slot.options.version, this.slot.options.compatibility), this.slot.settings));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/configuration/RealmsSlotOptionsScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */