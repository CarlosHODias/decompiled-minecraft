/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import java.io.IOException;
/*     */ import java.io.UncheckedIOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalLong;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.components.tabs.GridLayoutTab;
/*     */ import net.minecraft.client.gui.components.tabs.Tab;
/*     */ import net.minecraft.client.gui.components.tabs.TabManager;
/*     */ import net.minecraft.client.gui.components.tabs.TabNavigationBar;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.gui.layouts.CommonLayouts;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*     */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.GenericMessageScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.core.LayeredRegistryAccess;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryOps;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.RegistryLayer;
/*     */ import net.minecraft.server.ReloadableServerResources;
/*     */ import net.minecraft.server.WorldLoader;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.repository.RepositorySource;
/*     */ import net.minecraft.server.packs.repository.ServerPacksSource;
/*     */ import net.minecraft.server.packs.resources.CloseableResourceManager;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.flag.FeatureFlagSet;
/*     */ import net.minecraft.world.flag.FeatureFlags;
/*     */ import net.minecraft.world.level.DataPackConfig;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.gamerules.GameRuleMap;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*     */ import net.minecraft.world.level.levelgen.WorldGenSettings;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPresets;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.PrimaryLevelData;
/*     */ import net.minecraft.world.level.storage.WorldData;
/*     */ import net.minecraft.world.level.validation.DirectoryValidator;
/*     */ import org.apache.commons.lang3.mutable.MutableObject;
/*     */ 
/*     */ public class CreateWorldScreen extends Screen {
/*     */   private static final int GROUP_BOTTOM = 1;
/*     */   private static final int TAB_COLUMN_WIDTH = 210;
/*  92 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*     */   private static final String TEMP_WORLD_PREFIX = "mcworld-";
/*  94 */   private static final Component GAME_MODEL_LABEL = (Component)Component.translatable("selectWorld.gameMode");
/*  95 */   private static final Component NAME_LABEL = (Component)Component.translatable("selectWorld.enterName");
/*  96 */   private static final Component EXPERIMENTS_LABEL = (Component)Component.translatable("selectWorld.experiments");
/*  97 */   private static final Component ALLOW_COMMANDS_INFO = (Component)Component.translatable("selectWorld.allowCommands.info");
/*  98 */   private static final Component PREPARING_WORLD_DATA = (Component)Component.translatable("createWorld.preparing");
/*     */   
/*     */   private static final int HORIZONTAL_BUTTON_SPACING = 10;
/*     */   private static final int VERTICAL_BUTTON_SPACING = 8;
/* 102 */   public static final Identifier TAB_HEADER_BACKGROUND = Identifier.withDefaultNamespace("textures/gui/tab_header_background.png");
/*     */   
/* 104 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */   private final WorldCreationUiState uiState;
/*     */   private final TabManager tabManager;
/*     */   private boolean recreated;
/*     */   private final DirectoryValidator packValidator;
/*     */   private final CreateWorldCallback createWorldCallback;
/*     */   private final Runnable onClose;
/*     */   private Path tempDataPackDir;
/*     */   private PackRepository tempDataPackRepository;
/*     */   private TabNavigationBar tabNavigationBar;
/*     */   
/*     */   private class GameTab
/*     */     extends GridLayoutTab
/*     */   {
/* 118 */     private static final Component TITLE = (Component)Component.translatable("createWorld.tab.game.title");
/* 119 */     private static final Component ALLOW_COMMANDS = (Component)Component.translatable("selectWorld.allowCommands");
/*     */     private final EditBox nameEdit;
/*     */     
/*     */     private GameTab() {
/* 123 */       super(TITLE);
/*     */ 
/*     */       
/* 126 */       GridLayout.RowHelper helper = this.layout.rowSpacing(8)
/* 127 */         .createRowHelper(1);
/* 128 */       LayoutSettings buttonLayoutSettings = helper.newCellSettings();
/*     */       
/* 130 */       this.nameEdit = new EditBox(CreateWorldScreen.this.font, 208, 20, (Component)Component.translatable("selectWorld.enterName"));
/* 131 */       this.nameEdit.setValue(CreateWorldScreen.this.uiState.getName());
/* 132 */       Objects.requireNonNull(CreateWorldScreen.this.uiState); this.nameEdit.setResponder(CreateWorldScreen.this.uiState::setName);
/* 133 */       CreateWorldScreen.this.uiState.addListener(uiState -> this.nameEdit.setTooltip(Tooltip.create((Component)Component.translatable("selectWorld.targetFolder", new Object[] { Component.literal(uiState.getTargetFolder()).withStyle(ChatFormatting.ITALIC) }))));
/* 134 */       CreateWorldScreen.this.setInitialFocus((GuiEventListener)this.nameEdit);
/* 135 */       helper.addChild((LayoutElement)CommonLayouts.labeledElement(CreateWorldScreen.this.font, (LayoutElement)this.nameEdit, CreateWorldScreen.NAME_LABEL), helper.newCellSettings().alignHorizontallyCenter());
/*     */       
/* 137 */       CycleButton<WorldCreationUiState.SelectedGameMode> gameModeButton = (CycleButton<WorldCreationUiState.SelectedGameMode>)helper.addChild(
/* 138 */           (LayoutElement)CycleButton.builder(selectedGameMode -> selectedGameMode.displayName, CreateWorldScreen.this.uiState.getGameMode())
/* 139 */           .withValues((Object[])new WorldCreationUiState.SelectedGameMode[] { WorldCreationUiState.SelectedGameMode.SURVIVAL, WorldCreationUiState.SelectedGameMode.HARDCORE, WorldCreationUiState.SelectedGameMode.CREATIVE
/* 140 */             }).create(0, 0, 210, 20, CreateWorldScreen.GAME_MODEL_LABEL, (button, gameMode) -> CreateWorldScreen.this.uiState.setGameMode(gameMode)), buttonLayoutSettings);
/*     */ 
/*     */ 
/*     */       
/* 144 */       CreateWorldScreen.this.uiState.addListener(data -> {
/*     */             gameModeButton.setValue(data.getGameMode());
/*     */             
/*     */             gameModeButton.active = !data.isDebug();
/*     */             gameModeButton.setTooltip(Tooltip.create(data.getGameMode().getInfo()));
/*     */           });
/* 150 */       CycleButton<Difficulty> difficultyButton = (CycleButton<Difficulty>)helper.addChild(
/* 151 */           (LayoutElement)CycleButton.builder(Difficulty::getDisplayName, CreateWorldScreen.this.uiState.getDifficulty())
/* 152 */           .withValues((Object[])Difficulty.values())
/* 153 */           .create(0, 0, 210, 20, (Component)Component.translatable("options.difficulty"), (button, value) -> CreateWorldScreen.this.uiState.setDifficulty(value)), buttonLayoutSettings);
/*     */ 
/*     */       
/* 156 */       CreateWorldScreen.this.uiState.addListener(d -> {
/*     */             difficultyButton.setValue(CreateWorldScreen.this.uiState.getDifficulty());
/*     */             
/*     */             difficultyButton.active = !CreateWorldScreen.this.uiState.isHardcore();
/*     */             difficultyButton.setTooltip(Tooltip.create(CreateWorldScreen.this.uiState.getDifficulty().getInfo()));
/*     */           });
/* 162 */       CycleButton<Boolean> allowCommandsButton = (CycleButton<Boolean>)helper.addChild((LayoutElement)CycleButton.onOffBuilder(CreateWorldScreen.this.uiState.isAllowCommands())
/* 163 */           .withTooltip(state -> Tooltip.create(CreateWorldScreen.ALLOW_COMMANDS_INFO))
/* 164 */           .create(0, 0, 210, 20, ALLOW_COMMANDS, (b, state) -> CreateWorldScreen.this.uiState.setAllowCommands(state)));
/*     */       
/* 166 */       CreateWorldScreen.this.uiState.addListener(d -> {
/*     */             allowCommandsButton.setValue(CreateWorldScreen.this.uiState.isAllowCommands());
/* 168 */             allowCommandsButton.active = (!CreateWorldScreen.this.uiState.isDebug() && !CreateWorldScreen.this.uiState.isHardcore());
/*     */           });
/*     */       
/* 171 */       if (!SharedConstants.getCurrentVersion().stable())
/* 172 */         helper.addChild((LayoutElement)Button.builder(CreateWorldScreen.EXPERIMENTS_LABEL, button -> CreateWorldScreen.this.openExperimentsScreen(CreateWorldScreen.this.uiState.getSettings().dataConfiguration())).width(210).build()); 
/*     */     }
/*     */   }
/*     */   
/*     */   private class WorldTab
/*     */     extends GridLayoutTab {
/* 178 */     private static final Component TITLE = (Component)Component.translatable("createWorld.tab.world.title");
/* 179 */     private static final Component AMPLIFIED_HELP_TEXT = (Component)Component.translatable("generator.minecraft.amplified.info");
/* 180 */     private static final Component GENERATE_STRUCTURES = (Component)Component.translatable("selectWorld.mapFeatures");
/* 181 */     private static final Component GENERATE_STRUCTURES_INFO = (Component)Component.translatable("selectWorld.mapFeatures.info");
/* 182 */     private static final Component BONUS_CHEST = (Component)Component.translatable("selectWorld.bonusItems");
/* 183 */     private static final Component SEED_LABEL = (Component)Component.translatable("selectWorld.enterSeed");
/* 184 */     private static final Component SEED_EMPTY_HINT = (Component)Component.translatable("selectWorld.seedInfo");
/*     */     private static final int WORLD_TAB_WIDTH = 310;
/*     */     private final EditBox seedEdit;
/*     */     private final Button customizeTypeButton;
/*     */     
/*     */     private WorldTab() {
/* 190 */       super(TITLE);
/*     */ 
/*     */       
/* 193 */       GridLayout.RowHelper helper = this.layout.columnSpacing(10)
/* 194 */         .rowSpacing(8)
/* 195 */         .createRowHelper(2);
/*     */       
/* 197 */       CycleButton<WorldCreationUiState.WorldTypeEntry> typeButton = (CycleButton<WorldCreationUiState.WorldTypeEntry>)helper.addChild((LayoutElement)CycleButton.builder(WorldCreationUiState.WorldTypeEntry::describePreset, CreateWorldScreen.this.uiState.getWorldType())
/* 198 */           .withValues(createWorldTypeValueSupplier())
/* 199 */           .withCustomNarration(WorldTab::createTypeButtonNarration)
/* 200 */           .create(0, 0, 150, 20, (Component)Component.translatable("selectWorld.mapType"), (button, newPreset) -> CreateWorldScreen.this.uiState.setWorldType(newPreset)));
/*     */ 
/*     */       
/* 203 */       typeButton.setValue(CreateWorldScreen.this.uiState.getWorldType());
/* 204 */       CreateWorldScreen.this.uiState.addListener(data -> {
/*     */             WorldCreationUiState.WorldTypeEntry worldType = typeButton.getWorldType();
/*     */             
/*     */             typeButton.setValue(worldType);
/*     */             
/*     */             if (worldType.isAmplified()) {
/*     */               typeButton.setTooltip(Tooltip.create(AMPLIFIED_HELP_TEXT));
/*     */             } else {
/*     */               typeButton.setTooltip(null);
/*     */             } 
/*     */             typeButton.active = (CreateWorldScreen.this.uiState.getWorldType().preset() != null);
/*     */           });
/* 216 */       this.customizeTypeButton = (Button)helper.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectWorld.customizeType"), b -> openPresetEditor()).build());
/* 217 */       CreateWorldScreen.this.uiState.addListener(data -> this.customizeTypeButton.active = (!data.isDebug() && data.getPresetEditor() != null));
/*     */       
/* 219 */       this.seedEdit = new EditBox(this, CreateWorldScreen.this.font, 308, 20, (Component)Component.translatable("selectWorld.enterSeed"))
/*     */         {
/*     */           protected MutableComponent createNarrationMessage() {
/* 222 */             return super.createNarrationMessage().append(CommonComponents.NARRATION_SEPARATOR).append(CreateWorldScreen.WorldTab.SEED_EMPTY_HINT);
/*     */           }
/*     */         };
/* 225 */       this.seedEdit.setHint(SEED_EMPTY_HINT);
/* 226 */       this.seedEdit.setValue(this$0.uiState.getSeed());
/* 227 */       this.seedEdit.setResponder(value -> CreateWorldScreen.this.uiState.setSeed(this.seedEdit.getValue()));
/* 228 */       helper.addChild((LayoutElement)CommonLayouts.labeledElement(this$0.font, (LayoutElement)this.seedEdit, SEED_LABEL), 2);
/*     */       
/* 230 */       SwitchGrid.Builder switchGridBuilder = SwitchGrid.builder(310);
/* 231 */       Objects.requireNonNull(this$0.uiState); Objects.requireNonNull(this$0.uiState); switchGridBuilder.addSwitch(GENERATE_STRUCTURES, this$0.uiState::isGenerateStructures, this$0.uiState::setGenerateStructures)
/* 232 */         .withIsActiveCondition(() -> !CreateWorldScreen.this.uiState.isDebug())
/* 233 */         .withInfo(GENERATE_STRUCTURES_INFO);
/* 234 */       Objects.requireNonNull(this$0.uiState); Objects.requireNonNull(this$0.uiState); switchGridBuilder.addSwitch(BONUS_CHEST, this$0.uiState::isBonusChest, this$0.uiState::setBonusChest)
/* 235 */         .withIsActiveCondition(() -> (!CreateWorldScreen.this.uiState.isHardcore() && !CreateWorldScreen.this.uiState.isDebug()));
/* 236 */       SwitchGrid switchGrid = switchGridBuilder.build();
/* 237 */       helper.addChild((LayoutElement)switchGrid.layout(), 2);
/* 238 */       this$0.uiState.addListener(d -> switchGrid.refreshStates());
/*     */     }
/*     */     
/*     */     private void openPresetEditor() {
/* 242 */       PresetEditor editor = CreateWorldScreen.this.uiState.getPresetEditor();
/* 243 */       if (editor != null) {
/* 244 */         CreateWorldScreen.this.minecraft.setScreen(editor.createEditScreen(CreateWorldScreen.this, CreateWorldScreen.this.uiState.getSettings()));
/*     */       }
/*     */     }
/*     */     
/*     */     private CycleButton.ValueListSupplier<WorldCreationUiState.WorldTypeEntry> createWorldTypeValueSupplier() {
/* 249 */       return new CycleButton.ValueListSupplier<WorldCreationUiState.WorldTypeEntry>()
/*     */         {
/*     */           public List<WorldCreationUiState.WorldTypeEntry> getSelectedList() {
/* 252 */             return CycleButton.DEFAULT_ALT_LIST_SELECTOR.getAsBoolean() ? CreateWorldScreen.this.uiState.getAltPresetList() : CreateWorldScreen.this.uiState.getNormalPresetList();
/*     */           }
/*     */ 
/*     */           
/*     */           public List<WorldCreationUiState.WorldTypeEntry> getDefaultList() {
/* 257 */             return CreateWorldScreen.this.uiState.getNormalPresetList();
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     private static MutableComponent createTypeButtonNarration(CycleButton<WorldCreationUiState.WorldTypeEntry> button) {
/* 263 */       if (((WorldCreationUiState.WorldTypeEntry)button.getValue()).isAmplified()) {
/* 264 */         return CommonComponents.joinForNarration(new Component[] { (Component)button.createDefaultNarrationMessage(), AMPLIFIED_HELP_TEXT });
/*     */       }
/* 266 */       return button.createDefaultNarrationMessage();
/*     */     }
/*     */   } class null extends EditBox { null(CreateWorldScreen.WorldTab this$1, Font font, int width, int height, Component narration) { super(font, width, height, narration); } protected MutableComponent createNarrationMessage() { return super.createNarrationMessage().append(CommonComponents.NARRATION_SEPARATOR).append(CreateWorldScreen.WorldTab.SEED_EMPTY_HINT); } } class null implements CycleButton.ValueListSupplier<WorldCreationUiState.WorldTypeEntry> {
/*     */     public List<WorldCreationUiState.WorldTypeEntry> getSelectedList() { return CycleButton.DEFAULT_ALT_LIST_SELECTOR.getAsBoolean() ? CreateWorldScreen.this.uiState.getAltPresetList() : CreateWorldScreen.this.uiState.getNormalPresetList(); } public List<WorldCreationUiState.WorldTypeEntry> getDefaultList() { return CreateWorldScreen.this.uiState.getNormalPresetList(); }
/*     */   } private class MoreTab extends GridLayoutTab {
/* 271 */     private static final Component TITLE = (Component)Component.translatable("createWorld.tab.more.title");
/* 272 */     private static final Component GAME_RULES_LABEL = (Component)Component.translatable("selectWorld.gameRules");
/* 273 */     private static final Component DATA_PACKS_LABEL = (Component)Component.translatable("selectWorld.dataPacks");
/*     */     
/*     */     private MoreTab() {
/* 276 */       super(TITLE);
/*     */ 
/*     */       
/* 279 */       GridLayout.RowHelper helper = this.layout.rowSpacing(8)
/* 280 */         .createRowHelper(1);
/*     */       
/* 282 */       helper.addChild((LayoutElement)Button.builder(GAME_RULES_LABEL, b -> openGameRulesScreen())
/* 283 */           .width(210)
/* 284 */           .build());
/*     */ 
/*     */       
/* 287 */       helper.addChild((LayoutElement)Button.builder(CreateWorldScreen.EXPERIMENTS_LABEL, b -> CreateWorldScreen.this.openExperimentsScreen(CreateWorldScreen.this.uiState.getSettings().dataConfiguration()))
/* 288 */           .width(210)
/* 289 */           .build());
/*     */       
/* 291 */       helper.addChild((LayoutElement)Button.builder(DATA_PACKS_LABEL, b -> CreateWorldScreen.this.openDataPackSelectionScreen(CreateWorldScreen.this.uiState.getSettings().dataConfiguration()))
/* 292 */           .width(210)
/* 293 */           .build());
/*     */     }
/*     */     
/*     */     private void openGameRulesScreen() {
/* 297 */       CreateWorldScreen.this.minecraft.setScreen(new EditGameRulesScreen(CreateWorldScreen.this.uiState.getGameRules().copy(CreateWorldScreen.this.uiState.getSettings().dataConfiguration().enabledFeatures()), gameRules -> {
/*     */               CreateWorldScreen.this.minecraft.setScreen(CreateWorldScreen.this);
/*     */               Objects.requireNonNull(CreateWorldScreen.this.uiState);
/*     */               gameRules.ifPresent(CreateWorldScreen.this.uiState::setGameRules);
/*     */             }));
/*     */     } }
/*     */   
/*     */   public static void openFresh(Minecraft minecraft, Runnable onClose) {
/* 305 */     openFresh(minecraft, onClose, (createWorldScreen, finalLayers, worldData, tempDataPackDir) -> createWorldScreen.createNewWorld(finalLayers, (WorldData)worldData));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void openFresh(Minecraft minecraft, Runnable onClose, CreateWorldCallback createWorld) {
/*     */     WorldCreationContextMapper worldCreationContext = (managers, registries, cookie) -> new WorldCreationContext(cookie.worldGenSettings(), registries, managers, cookie.dataConfiguration());
/*     */     Function<WorldLoader.DataLoadContext, WorldGenSettings> worldGenSettings = context -> new WorldGenSettings(WorldOptions.defaultWithRandomSeed(), WorldPresets.createNormalWorldDimensions(context.datapackWorldgen()));
/* 318 */     openCreateWorldScreen(minecraft, onClose, worldGenSettings, worldCreationContext, WorldPresets.NORMAL, createWorld);
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
/*     */   public static void testWorld(Minecraft minecraft, Runnable onClose) {
/*     */     WorldCreationContextMapper worldCreationContext = (managers, registries, cookie) -> new WorldCreationContext(cookie.worldGenSettings().options(), cookie.worldGenSettings().dimensions(), registries, managers, cookie.dataConfiguration(), new InitialWorldCreationOptions(WorldCreationUiState.SelectedGameMode.CREATIVE, new GameRuleMap.Builder().set(GameRules.ADVANCE_TIME, false).set(GameRules.ADVANCE_WEATHER, false).set(GameRules.SPAWN_MOBS, false).build(), FlatLevelGeneratorPresets.REDSTONE_READY));
/*     */     Function<WorldLoader.DataLoadContext, WorldGenSettings> worldGenSettings = context -> new WorldGenSettings(WorldOptions.testWorldWithRandomSeed(), WorldPresets.createFlatWorldDimensions(context.datapackWorldgen()));
/* 346 */     openCreateWorldScreen(minecraft, onClose, worldGenSettings, worldCreationContext, WorldPresets.FLAT, (createWorldScreen, finalLayers, worldData, tempDataPackDir) -> createWorldScreen.createNewWorld(finalLayers, (WorldData)worldData));
/*     */   }
/*     */   
/*     */   private static void openCreateWorldScreen(Minecraft minecraft, Runnable onClose, Function<WorldLoader.DataLoadContext, WorldGenSettings> worldGenSettings, WorldCreationContextMapper worldCreationContext, ResourceKey<WorldPreset> worldPreset, CreateWorldCallback createWorld) {
/* 350 */     queueLoadScreen(minecraft, PREPARING_WORLD_DATA);
/*     */     
/* 352 */     PackRepository vanillaOnlyPackRepository = new PackRepository(new RepositorySource[] { (RepositorySource)new ServerPacksSource(minecraft.directoryValidator()) });
/* 353 */     WorldDataConfiguration dataConfig = SharedConstants.IS_RUNNING_IN_IDE ? 
/* 354 */       new WorldDataConfiguration(new DataPackConfig(List.of("vanilla", "tests"), List.of()), FeatureFlags.DEFAULT_FLAGS) : 
/* 355 */       WorldDataConfiguration.DEFAULT;
/* 356 */     WorldLoader.InitConfig loadConfig = createDefaultLoadConfig(vanillaOnlyPackRepository, dataConfig);
/*     */     
/* 358 */     CompletableFuture<WorldCreationContext> loadResult = WorldLoader.load(loadConfig, context -> new WorldLoader.DataLoadOutput(new DataPackReloadCookie(worldGenSettings.apply(context), context.dataConfiguration()), context.datapackDimensions()), (resources, managers, registries, cookie) -> {
/*     */           resources.close();
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
/*     */           return worldCreationContext.apply(managers, registries, cookie);
/* 371 */         }, (Executor)Util.backgroundExecutor(), (Executor)minecraft);
/*     */ 
/*     */     
/* 374 */     Objects.requireNonNull(loadResult); minecraft.managedBlock(loadResult::isDone);
/*     */ 
/*     */     
/* 377 */     minecraft.setScreen(new CreateWorldScreen(minecraft, onClose, loadResult.join(), Optional.of(worldPreset), OptionalLong.empty(), createWorld));
/*     */   }
/*     */   
/*     */   public static CreateWorldScreen createFromExisting(Minecraft minecraft, Runnable onClose, LevelSettings levelSettings, WorldCreationContext worldCreationContext, Path newDataPackDir) {
/* 381 */     CreateWorldScreen result = new CreateWorldScreen(minecraft, onClose, worldCreationContext, WorldPresets.fromSettings(worldCreationContext.selectedDimensions()), OptionalLong.of(worldCreationContext.options().seed()), (createWorldScreen, finalLayers, worldData, tempDataPackDir) -> createWorldScreen.createNewWorld(finalLayers, (WorldData)worldData));
/* 382 */     result.recreated = true;
/*     */     
/* 384 */     result.uiState.setName(levelSettings.levelName());
/* 385 */     result.uiState.setAllowCommands(levelSettings.allowCommands());
/* 386 */     result.uiState.setDifficulty(levelSettings.difficulty());
/* 387 */     result.uiState.getGameRules().setAll(levelSettings.gameRules(), null);
/*     */     
/* 389 */     if (levelSettings.hardcore()) {
/* 390 */       result.uiState.setGameMode(WorldCreationUiState.SelectedGameMode.HARDCORE);
/* 391 */     } else if (levelSettings.gameType().isSurvival()) {
/* 392 */       result.uiState.setGameMode(WorldCreationUiState.SelectedGameMode.SURVIVAL);
/* 393 */     } else if (levelSettings.gameType().isCreative()) {
/* 394 */       result.uiState.setGameMode(WorldCreationUiState.SelectedGameMode.CREATIVE);
/*     */     } 
/* 396 */     result.tempDataPackDir = newDataPackDir;
/* 397 */     return result;
/*     */   }
/*     */   
/*     */   private CreateWorldScreen(Minecraft minecraft, Runnable onClose, WorldCreationContext settings, Optional<ResourceKey<WorldPreset>> preset, OptionalLong seed, CreateWorldCallback createWorldCallback) {
/* 401 */     super((Component)Component.translatable("selectWorld.create")); this.tabManager = new TabManager(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0), x$0 -> rec$.removeWidget(x$0));
/* 402 */     this.onClose = onClose;
/* 403 */     this.packValidator = minecraft.directoryValidator();
/* 404 */     this.createWorldCallback = createWorldCallback;
/*     */     
/* 406 */     this.uiState = new WorldCreationUiState(minecraft.getLevelSource().getBaseDir(), settings, preset, seed);
/*     */   }
/*     */   
/*     */   public WorldCreationUiState getUiState() {
/* 410 */     return this.uiState;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/* 415 */     this
/*     */       
/* 417 */       .tabNavigationBar = TabNavigationBar.builder(this.tabManager, this.width).addTabs(new Tab[] { (Tab)new GameTab(), (Tab)new WorldTab(), (Tab)new MoreTab() }).build();
/* 418 */     addRenderableWidget((GuiEventListener)this.tabNavigationBar);
/*     */     
/* 420 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/* 421 */     footer.addChild((LayoutElement)Button.builder((Component)Component.translatable("selectWorld.create"), button -> onCreate()).build());
/* 422 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_CANCEL, button -> popScreen()).build());
/*     */     
/* 424 */     this.layout.visitWidgets(button -> {
/*     */           button.setTabOrderGroup(1);
/*     */           
/*     */           addRenderableWidget((GuiEventListener)button);
/*     */         });
/* 429 */     this.tabNavigationBar.selectTab(0, false);
/*     */ 
/*     */     
/* 432 */     this.uiState.onChanged();
/*     */     
/* 434 */     repositionElements();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void repositionElements() {
/* 444 */     if (this.tabNavigationBar == null) {
/*     */       return;
/*     */     }
/* 447 */     this.tabNavigationBar.setWidth(this.width);
/* 448 */     this.tabNavigationBar.arrangeElements();
/*     */     
/* 450 */     int tabAreaTop = this.tabNavigationBar.getRectangle().bottom();
/* 451 */     ScreenRectangle tabArea = new ScreenRectangle(0, tabAreaTop, this.width, this.height - this.layout.getFooterHeight() - tabAreaTop);
/* 452 */     this.tabManager.setTabArea(tabArea);
/* 453 */     this.layout.setHeaderHeight(tabAreaTop);
/* 454 */     this.layout.arrangeElements();
/*     */   }
/*     */   
/*     */   private static void queueLoadScreen(Minecraft minecraft, Component message) {
/* 458 */     minecraft.setScreenAndShow((Screen)new GenericMessageScreen(message));
/*     */   }
/*     */   
/*     */   private void onCreate() {
/* 462 */     WorldCreationContext context = this.uiState.getSettings();
/* 463 */     WorldDimensions.Complete finalDimensions = context.selectedDimensions().bake(context.datapackDimensions());
/*     */     
/* 465 */     LayeredRegistryAccess<RegistryLayer> finalLayers = context.worldgenRegistries().replaceFrom(RegistryLayer.DIMENSIONS, new net.minecraft.core.RegistryAccess.Frozen[] { finalDimensions.dimensionsRegistryAccess() });
/* 466 */     Lifecycle lifecycleFromFeatures = FeatureFlags.isExperimental(context.dataConfiguration().enabledFeatures()) ? Lifecycle.experimental() : Lifecycle.stable();
/* 467 */     Lifecycle lifecycleFromRegistries = finalLayers.compositeAccess().allRegistriesLifecycle();
/* 468 */     Lifecycle lifecycle = lifecycleFromRegistries.add(lifecycleFromFeatures);
/*     */ 
/*     */     
/* 471 */     boolean skipWarning = (!this.recreated && lifecycleFromRegistries == Lifecycle.stable());
/* 472 */     LevelSettings levelSettings = createLevelSettings((finalDimensions.specialWorldProperty() == PrimaryLevelData.SpecialWorldProperty.DEBUG));
/* 473 */     PrimaryLevelData worldData = new PrimaryLevelData(levelSettings, this.uiState.getSettings().options(), finalDimensions.specialWorldProperty(), lifecycle);
/* 474 */     WorldOpenFlows.confirmWorldCreation(this.minecraft, this, lifecycle, () -> createWorldAndCleanup(finalLayers, worldData), skipWarning);
/*     */   }
/*     */   
/*     */   private void createWorldAndCleanup(LayeredRegistryAccess<RegistryLayer> finalLayers, PrimaryLevelData worldData) {
/* 478 */     boolean worldCreationSuccessful = this.createWorldCallback.create(this, finalLayers, worldData, this.tempDataPackDir);
/* 479 */     removeTempDataPackDir();
/* 480 */     if (!worldCreationSuccessful) {
/* 481 */       popScreen();
/*     */     }
/*     */   }
/*     */   
/*     */   private boolean createNewWorld(LayeredRegistryAccess<RegistryLayer> finalLayers, WorldData worldData) {
/* 486 */     String worldFolder = this.uiState.getTargetFolder();
/* 487 */     WorldCreationContext context = this.uiState.getSettings();
/* 488 */     queueLoadScreen(this.minecraft, PREPARING_WORLD_DATA);
/*     */     
/* 490 */     Optional<LevelStorageSource.LevelStorageAccess> newWorldAccess = createNewWorldDirectory(this.minecraft, worldFolder, this.tempDataPackDir);
/* 491 */     if (newWorldAccess.isEmpty()) {
/* 492 */       SystemToast.onPackCopyFailure(this.minecraft, worldFolder);
/* 493 */       return false;
/*     */     } 
/*     */     
/* 496 */     this.minecraft.createWorldOpenFlows().createLevelFromExistingSettings(newWorldAccess.get(), context.dataPackResources(), finalLayers, worldData);
/*     */     
/* 498 */     return true;
/*     */   }
/*     */   
/*     */   private LevelSettings createLevelSettings(boolean isDebug) {
/* 502 */     String name = this.uiState.getName().trim();
/* 503 */     if (isDebug) {
/* 504 */       GameRules debugGameRules = new GameRules(WorldDataConfiguration.DEFAULT.enabledFeatures());
/* 505 */       debugGameRules.set(GameRules.ADVANCE_TIME, false, null);
/* 506 */       return new LevelSettings(name, net.minecraft.world.level.GameType.SPECTATOR, false, Difficulty.PEACEFUL, true, debugGameRules, WorldDataConfiguration.DEFAULT);
/*     */     } 
/* 508 */     return new LevelSettings(name, (this.uiState.getGameMode()).gameType, this.uiState.isHardcore(), this.uiState.getDifficulty(), this.uiState.isAllowCommands(), this.uiState.getGameRules(), this.uiState.getSettings().dataConfiguration());
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean keyPressed(KeyEvent event) {
/* 513 */     if (this.tabNavigationBar.keyPressed(event)) {
/* 514 */       return true;
/*     */     }
/*     */     
/* 517 */     if (super.keyPressed(event)) {
/* 518 */       return true;
/*     */     }
/*     */     
/* 521 */     if (event.isConfirmation()) {
/* 522 */       onCreate();
/* 523 */       return true;
/*     */     } 
/*     */     
/* 526 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/* 531 */     popScreen();
/*     */   }
/*     */   
/*     */   public void popScreen() {
/* 535 */     this.onClose.run();
/* 536 */     removeTempDataPackDir();
/*     */   }
/*     */ 
/*     */   
/*     */   public void render(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 541 */     super.render(graphics, mouseX, mouseY, a);
/* 542 */     graphics.blit(RenderPipelines.GUI_TEXTURED, Screen.FOOTER_SEPARATOR, 0, this.height - this.layout.getFooterHeight() - 2, 0.0F, 0.0F, this.width, 2, 32, 2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderMenuBackground(GuiGraphics graphics) {
/* 547 */     graphics.blit(RenderPipelines.GUI_TEXTURED, TAB_HEADER_BACKGROUND, 0, 0, 0.0F, 0.0F, this.width, this.layout.getHeaderHeight(), 16, 16);
/* 548 */     renderMenuBackground(graphics, 0, this.layout.getHeaderHeight(), this.width, this.height);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private Path getOrCreateTempDataPackDir() {
/* 554 */     if (this.tempDataPackDir == null) {
/*     */       try {
/* 556 */         this.tempDataPackDir = Files.createTempDirectory("mcworld-", (FileAttribute<?>[])new FileAttribute[0]);
/* 557 */       } catch (IOException e) {
/* 558 */         LOGGER.warn("Failed to create temporary dir", e);
/* 559 */         SystemToast.onPackCopyFailure(this.minecraft, this.uiState.getTargetFolder());
/* 560 */         popScreen();
/*     */       } 
/*     */     }
/*     */     
/* 564 */     return this.tempDataPackDir;
/*     */   }
/*     */   
/*     */   private void openExperimentsScreen(WorldDataConfiguration dataConfiguration) {
/* 568 */     Pair<Path, PackRepository> settings = getDataPackSelectionSettings(dataConfiguration);
/*     */     
/* 570 */     if (settings != null) {
/* 571 */       this.minecraft.setScreen(new ExperimentsScreen(this, (PackRepository)settings.getSecond(), packRepository -> tryApplyNewDataPacks(packRepository, false, this::openExperimentsScreen)));
/*     */     }
/*     */   }
/*     */   
/*     */   private void openDataPackSelectionScreen(WorldDataConfiguration dataConfiguration) {
/* 576 */     Pair<Path, PackRepository> settings = getDataPackSelectionSettings(dataConfiguration);
/*     */     
/* 578 */     if (settings != null) {
/* 579 */       this.minecraft.setScreen((Screen)new net.minecraft.client.gui.screens.packs.PackSelectionScreen((PackRepository)settings.getSecond(), packRepository -> tryApplyNewDataPacks(packRepository, true, this::openDataPackSelectionScreen), (Path)settings.getFirst(), (Component)Component.translatable("dataPack.title")));
/*     */     }
/*     */   }
/*     */   
/*     */   private void tryApplyNewDataPacks(PackRepository packRepository, boolean isDataPackScreen, Consumer<WorldDataConfiguration> onAbort) {
/* 584 */     ImmutableList immutableList = ImmutableList.copyOf(packRepository.getSelectedIds());
/* 585 */     List<String> newDisabled = (List<String>)packRepository.getAvailableIds().stream().filter(id -> !newEnabled.contains(id)).collect(ImmutableList.toImmutableList());
/* 586 */     WorldDataConfiguration newConfig = new WorldDataConfiguration(new DataPackConfig((List)immutableList, newDisabled), this.uiState.getSettings().dataConfiguration().enabledFeatures());
/*     */     
/* 588 */     if (this.uiState.tryUpdateDataConfiguration(newConfig)) {
/*     */       
/* 590 */       this.minecraft.setScreen(this);
/*     */       
/*     */       return;
/*     */     } 
/* 594 */     FeatureFlagSet requestedFeatureFlags = packRepository.getRequestedFeatureFlags();
/* 595 */     if (FeatureFlags.isExperimental(requestedFeatureFlags) && isDataPackScreen) {
/* 596 */       this.minecraft.setScreen(new ConfirmExperimentalFeaturesScreen(
/* 597 */             packRepository.getSelectedPacks(), accepted -> {
/*     */ 
/*     */               
/*     */               if (onAbort) {
/*     */                 applyNewPackConfig(packRepository, packRepository, packRepository);
/*     */               } else {
/*     */                 packRepository.accept(this.uiState.getSettings().dataConfiguration());
/*     */               } 
/*     */             }));
/*     */     } else {
/* 607 */       applyNewPackConfig(packRepository, newConfig, onAbort);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void applyNewPackConfig(PackRepository packRepository, WorldDataConfiguration newConfig, Consumer<WorldDataConfiguration> onAbort) {
/* 613 */     this.minecraft.setScreenAndShow((Screen)new GenericMessageScreen((Component)Component.translatable("dataPack.validation.working")));
/*     */     
/* 615 */     WorldLoader.InitConfig config = createDefaultLoadConfig(packRepository, newConfig);
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
/* 651 */     Objects.requireNonNull(this.uiState); WorldLoader.load(config, context -> { if (context.datapackWorldgen().lookupOrThrow(Registries.WORLD_PRESET).listElements().findAny().isEmpty()) throw new IllegalStateException("Needs at least one world preset to continue");  if (context.datapackWorldgen().lookupOrThrow(Registries.BIOME).listElements().findAny().isEmpty()) throw new IllegalStateException("Needs at least one biome continue");  WorldCreationContext existingContext = this.uiState.getSettings(); RegistryOps registryOps1 = existingContext.worldgenLoadContext().createSerializationContext((DynamicOps)JsonOps.INSTANCE); DataResult<JsonElement> encoded = WorldGenSettings.encode((DynamicOps)registryOps1, existingContext.options(), existingContext.selectedDimensions()).setLifecycle(Lifecycle.stable()); RegistryOps registryOps2 = context.datapackWorldgen().createSerializationContext((DynamicOps)JsonOps.INSTANCE); WorldGenSettings settings = (WorldGenSettings)encoded.flatMap(()).getOrThrow(()); return new WorldLoader.DataLoadOutput(new DataPackReloadCookie(settings, context.dataConfiguration()), context.datapackDimensions()); }, (resources, managers, registries, cookie) -> { resources.close(); return new WorldCreationContext(cookie.worldGenSettings(), registries, managers, cookie.dataConfiguration()); }, (Executor)Util.backgroundExecutor(), (Executor)this.minecraft).thenApply(settings -> { settings.validate(); return settings; }).thenAcceptAsync(this.uiState::setSettings, (Executor)this.minecraft)
/* 652 */       .handleAsync((nothing, throwable) -> {
/*     */           if (throwable != null) {
/*     */             LOGGER.warn("Failed to validate datapack", throwable);
/*     */             this.minecraft.setScreen((Screen)new ConfirmScreen((), (Component)Component.translatable("dataPack.validation.failed"), CommonComponents.EMPTY, (Component)Component.translatable("dataPack.validation.back"), (Component)Component.translatable("dataPack.validation.reset")));
/*     */           } else {
/*     */             this.minecraft.setScreen(this);
/*     */           } 
/*     */           return null;
/*     */         }, (Executor)this.minecraft);
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
/*     */   
/*     */   private static WorldLoader.InitConfig createDefaultLoadConfig(PackRepository packRepository, WorldDataConfiguration config) {
/* 675 */     WorldLoader.PackConfig packConfig = new WorldLoader.PackConfig(packRepository, config, false, true);
/* 676 */     return new WorldLoader.InitConfig(packConfig, net.minecraft.commands.Commands.CommandSelection.INTEGRATED, (PermissionSet)net.minecraft.server.permissions.LevelBasedPermissionSet.GAMEMASTER);
/*     */   }
/*     */   
/*     */   private void removeTempDataPackDir() {
/* 680 */     if (this.tempDataPackDir != null && Files.exists(this.tempDataPackDir, new java.nio.file.LinkOption[0])) {
/* 681 */       try { Stream<Path> files = Files.walk(this.tempDataPackDir, new java.nio.file.FileVisitOption[0]); 
/* 682 */         try { files.sorted(java.util.Comparator.reverseOrder()).forEach(path -> {
/*     */                 try {
/*     */                   Files.delete(path);
/* 685 */                 } catch (IOException e) {
/*     */                   LOGGER.warn("Failed to remove temporary file {}", path, e);
/*     */                 } 
/*     */               });
/* 689 */           if (files != null) files.close();  } catch (Throwable throwable) { if (files != null) try { files.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 690 */       { LOGGER.warn("Failed to list temporary dir {}", this.tempDataPackDir); }
/*     */     
/*     */     }
/* 693 */     this.tempDataPackDir = null;
/*     */   }
/*     */   
/*     */   private static void copyBetweenDirs(Path sourceDir, Path targetDir, Path sourcePath) {
/*     */     try {
/* 698 */       Util.copyBetweenDirs(sourceDir, targetDir, sourcePath);
/* 699 */     } catch (IOException e) {
/* 700 */       LOGGER.warn("Failed to copy datapack file from {} to {}", sourcePath, targetDir);
/* 701 */       throw new UncheckedIOException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private static Optional<LevelStorageSource.LevelStorageAccess> createNewWorldDirectory(Minecraft minecraft, String worldFolder, Path tempDataPackDir) {
/*     */     try {
/* 708 */       LevelStorageSource.LevelStorageAccess access = minecraft.getLevelSource().createAccess(worldFolder);
/* 709 */       if (tempDataPackDir == null)
/* 710 */         return Optional.of(access); 
/*     */       
/* 712 */       try { Stream<Path> files = Files.walk(tempDataPackDir, new java.nio.file.FileVisitOption[0]); 
/* 713 */         try { Path targetDir = access.getLevelPath(LevelResource.DATAPACK_DIR);
/* 714 */           net.minecraft.util.FileUtil.createDirectoriesSafe(targetDir);
/* 715 */           files.filter(f -> !f.equals(tempDataPackDir)).forEach(source -> copyBetweenDirs(tempDataPackDir, targetDir, source));
/* 716 */           Optional<LevelStorageSource.LevelStorageAccess> optional = Optional.of(access);
/* 717 */           if (files != null) files.close();  return optional; } catch (Throwable throwable) { if (files != null) try { files.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException|UncheckedIOException e)
/* 718 */       { LOGGER.warn("Failed to copy datapacks to world {}", worldFolder, e);
/* 719 */         access.close(); }
/*     */     
/* 721 */     } catch (IOException|UncheckedIOException e) {
/* 722 */       LOGGER.warn("Failed to create access for {}", worldFolder, e);
/*     */     } 
/* 724 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   public static Path createTempDataPackDirFromExistingWorld(Path sourcePackDir, Minecraft minecraft) {
/* 728 */     MutableObject<Path> tempDataPackDir = new MutableObject(); 
/* 729 */     try { Stream<Path> dataPackContents = Files.walk(sourcePackDir, new java.nio.file.FileVisitOption[0]); 
/* 730 */       try { dataPackContents.filter(p -> !p.equals(sourcePackDir)).forEach(source -> {
/*     */               Path targetDir = (Path)tempDataPackDir.get();
/*     */               
/*     */               if (targetDir == null) {
/*     */                 try {
/*     */                   targetDir = Files.createTempDirectory("mcworld-", (FileAttribute<?>[])new FileAttribute[0]);
/* 736 */                 } catch (IOException e) {
/*     */                   LOGGER.warn("Failed to create temporary dir");
/*     */                   throw new UncheckedIOException(e);
/*     */                 } 
/*     */                 tempDataPackDir.setValue(targetDir);
/*     */               } 
/*     */               copyBetweenDirs(sourcePackDir, targetDir, source);
/*     */             });
/* 744 */         if (dataPackContents != null) dataPackContents.close();  } catch (Throwable throwable) { if (dataPackContents != null) try { dataPackContents.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException|UncheckedIOException e)
/* 745 */     { LOGGER.warn("Failed to copy datapacks from world {}", sourcePackDir, e);
/* 746 */       SystemToast.onPackCopyFailure(minecraft, sourcePackDir.toString());
/* 747 */       return null; }
/*     */     
/* 749 */     return (Path)tempDataPackDir.get();
/*     */   }
/*     */   
/*     */   private Pair<Path, PackRepository> getDataPackSelectionSettings(WorldDataConfiguration dataConfiguration) {
/* 753 */     Path dataPackDir = getOrCreateTempDataPackDir();
/* 754 */     if (dataPackDir != null) {
/* 755 */       if (this.tempDataPackRepository == null) {
/* 756 */         this.tempDataPackRepository = ServerPacksSource.createPackRepository(dataPackDir, this.packValidator);
/* 757 */         this.tempDataPackRepository.reload();
/*     */       } 
/*     */       
/* 760 */       this.tempDataPackRepository.setSelected(dataConfiguration.dataPacks().getEnabled());
/* 761 */       return Pair.of(dataPackDir, this.tempDataPackRepository);
/*     */     } 
/*     */     
/* 764 */     return null;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/worldselection/CreateWorldScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */