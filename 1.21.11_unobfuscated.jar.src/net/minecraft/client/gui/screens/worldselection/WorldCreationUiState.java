/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Path;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.OptionalLong;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.core.HolderSet;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.WorldPresetTags;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.world.Difficulty;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.gamerules.GameRules;
/*     */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorPreset;
/*     */ import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPreset;
/*     */ import net.minecraft.world.level.levelgen.presets.WorldPresets;
/*     */ 
/*     */ public class WorldCreationUiState {
/*  30 */   private static final Component DEFAULT_WORLD_NAME = (Component)Component.translatable("selectWorld.newWorld");
/*     */   
/*  32 */   private final List<Consumer<WorldCreationUiState>> listeners = new ArrayList<>();
/*     */   
/*  34 */   private String name = DEFAULT_WORLD_NAME.getString();
/*     */   
/*  36 */   private SelectedGameMode gameMode = SelectedGameMode.SURVIVAL;
/*  37 */   private Difficulty difficulty = Difficulty.NORMAL;
/*     */   
/*     */   private Boolean allowCommands;
/*     */   private String seed;
/*     */   private boolean generateStructures;
/*     */   private boolean bonusChest;
/*     */   private final Path savesFolder;
/*     */   private String targetFolder;
/*     */   private WorldCreationContext settings;
/*     */   private WorldTypeEntry worldType;
/*  47 */   private final List<WorldTypeEntry> normalPresetList = new ArrayList<>();
/*  48 */   private final List<WorldTypeEntry> altPresetList = new ArrayList<>();
/*     */   
/*     */   private GameRules gameRules;
/*     */   
/*     */   public WorldCreationUiState(Path savesFolder, WorldCreationContext settings, Optional<ResourceKey<WorldPreset>> preset, OptionalLong seed) {
/*  53 */     this.savesFolder = savesFolder;
/*  54 */     this.settings = settings;
/*  55 */     this.worldType = new WorldTypeEntry(findPreset(settings, preset).orElse(null));
/*  56 */     updatePresetLists();
/*  57 */     this.seed = seed.isPresent() ? Long.toString(seed.getAsLong()) : "";
/*  58 */     this.generateStructures = settings.options().generateStructures();
/*  59 */     this.bonusChest = settings.options().generateBonusChest();
/*  60 */     this.targetFolder = findResultFolder(this.name);
/*  61 */     this.gameMode = settings.initialWorldCreationOptions().selectedGameMode();
/*  62 */     this.gameRules = new GameRules(settings.dataConfiguration().enabledFeatures());
/*  63 */     this.gameRules.setAll(settings.initialWorldCreationOptions().gameRuleOverwrites(), null);
/*  64 */     Optional.<ResourceKey<FlatLevelGeneratorPreset>>ofNullable(settings.initialWorldCreationOptions().flatLevelPreset())
/*  65 */       .flatMap(key -> settings.worldgenLoadContext().lookup(Registries.FLAT_LEVEL_GENERATOR_PRESET).flatMap(()))
/*  66 */       .map(reference -> ((FlatLevelGeneratorPreset)reference.value()).settings())
/*  67 */       .ifPresent(generatorSettings -> updateDimensions(PresetEditor.flatWorldConfigurator(generatorSettings)));
/*     */   }
/*     */   
/*     */   public void addListener(Consumer<WorldCreationUiState> action) {
/*  71 */     this.listeners.add(action);
/*     */   }
/*     */   
/*     */   public void onChanged() {
/*  75 */     boolean bonusChest = isBonusChest();
/*  76 */     if (bonusChest != this.settings.options().generateBonusChest()) {
/*  77 */       this.settings = this.settings.withOptions(options -> options.withBonusChest(bonusChest));
/*     */     }
/*  79 */     boolean generateStructures = isGenerateStructures();
/*  80 */     if (generateStructures != this.settings.options().generateStructures()) {
/*  81 */       this.settings = this.settings.withOptions(options -> options.withStructures(generateStructures));
/*     */     }
/*  83 */     for (Consumer<WorldCreationUiState> listener : this.listeners) {
/*  84 */       listener.accept(this);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  89 */     this.name = name;
/*  90 */     this.targetFolder = findResultFolder(name);
/*  91 */     onChanged();
/*     */   }
/*     */   
/*     */   private String findResultFolder(String name) {
/*  95 */     String trimmedName = name.trim();
/*     */     try {
/*  97 */       return FileUtil.findAvailableName(this.savesFolder, !trimmedName.isEmpty() ? trimmedName : DEFAULT_WORLD_NAME.getString(), "");
/*  98 */     } catch (Exception exception) {
/*     */       
/*     */       try {
/* 101 */         return FileUtil.findAvailableName(this.savesFolder, "World", "");
/* 102 */       } catch (IOException e) {
/* 103 */         throw new RuntimeException("Could not create save folder", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public String getName() {
/* 108 */     return this.name;
/*     */   }
/*     */   
/*     */   public String getTargetFolder() {
/* 112 */     return this.targetFolder;
/*     */   }
/*     */   
/*     */   public void setGameMode(SelectedGameMode gameMode) {
/* 116 */     this.gameMode = gameMode;
/* 117 */     onChanged();
/*     */   }
/*     */   
/*     */   public SelectedGameMode getGameMode() {
/* 121 */     if (isDebug()) {
/* 122 */       return SelectedGameMode.DEBUG;
/*     */     }
/* 124 */     return this.gameMode;
/*     */   }
/*     */   
/*     */   public void setDifficulty(Difficulty difficulty) {
/* 128 */     this.difficulty = difficulty;
/* 129 */     onChanged();
/*     */   }
/*     */   
/*     */   public Difficulty getDifficulty() {
/* 133 */     if (isHardcore()) {
/* 134 */       return Difficulty.HARD;
/*     */     }
/* 136 */     return this.difficulty;
/*     */   }
/*     */   
/*     */   public boolean isHardcore() {
/* 140 */     return (getGameMode() == SelectedGameMode.HARDCORE);
/*     */   }
/*     */   
/*     */   public void setAllowCommands(boolean allowCommands) {
/* 144 */     this.allowCommands = allowCommands;
/* 145 */     onChanged();
/*     */   }
/*     */   
/*     */   public boolean isAllowCommands() {
/* 149 */     if (isDebug()) {
/* 150 */       return true;
/*     */     }
/* 152 */     if (isHardcore()) {
/* 153 */       return false;
/*     */     }
/* 155 */     if (this.allowCommands == null) {
/* 156 */       return (getGameMode() == SelectedGameMode.CREATIVE);
/*     */     }
/* 158 */     return this.allowCommands;
/*     */   }
/*     */   
/*     */   public void setSeed(String seed) {
/* 162 */     this.seed = seed;
/* 163 */     this.settings = this.settings.withOptions(options -> options.withSeed(WorldOptions.parseSeed(getSeed())));
/* 164 */     onChanged();
/*     */   }
/*     */   
/*     */   public String getSeed() {
/* 168 */     return this.seed;
/*     */   }
/*     */   
/*     */   public void setGenerateStructures(boolean generateStructures) {
/* 172 */     this.generateStructures = generateStructures;
/* 173 */     onChanged();
/*     */   }
/*     */   
/*     */   public boolean isGenerateStructures() {
/* 177 */     if (isDebug()) {
/* 178 */       return false;
/*     */     }
/* 180 */     return this.generateStructures;
/*     */   }
/*     */   
/*     */   public void setBonusChest(boolean bonusChest) {
/* 184 */     this.bonusChest = bonusChest;
/* 185 */     onChanged();
/*     */   }
/*     */   
/*     */   public boolean isBonusChest() {
/* 189 */     if (isDebug() || isHardcore()) {
/* 190 */       return false;
/*     */     }
/* 192 */     return this.bonusChest;
/*     */   }
/*     */   
/*     */   public void setSettings(WorldCreationContext settings) {
/* 196 */     this.settings = settings;
/* 197 */     updatePresetLists();
/* 198 */     onChanged();
/*     */   }
/*     */   
/*     */   public WorldCreationContext getSettings() {
/* 202 */     return this.settings;
/*     */   }
/*     */   
/*     */   public void updateDimensions(WorldCreationContext.DimensionsUpdater modifier) {
/* 206 */     this.settings = this.settings.withDimensions(modifier);
/* 207 */     onChanged();
/*     */   }
/*     */   
/*     */   protected boolean tryUpdateDataConfiguration(WorldDataConfiguration newConfig) {
/* 211 */     WorldDataConfiguration oldConfig = this.settings.dataConfiguration();
/* 212 */     if (oldConfig.dataPacks().getEnabled().equals(newConfig.dataPacks().getEnabled()) && 
/* 213 */       oldConfig.enabledFeatures().equals(newConfig.enabledFeatures())) {
/*     */ 
/*     */       
/* 216 */       this.settings = new WorldCreationContext(this.settings.options(), this.settings.datapackDimensions(), this.settings.selectedDimensions(), this.settings.worldgenRegistries(), this.settings.dataPackResources(), newConfig, this.settings.initialWorldCreationOptions());
/* 217 */       return true;
/*     */     } 
/* 219 */     return false;
/*     */   }
/*     */   
/*     */   public boolean isDebug() {
/* 223 */     return this.settings.selectedDimensions().isDebug();
/*     */   }
/*     */   
/*     */   public void setWorldType(WorldTypeEntry worldType) {
/* 227 */     this.worldType = worldType;
/* 228 */     Holder<WorldPreset> preset = worldType.preset();
/* 229 */     if (preset != null) {
/* 230 */       updateDimensions((registryAccess, dimensions) -> ((WorldPreset)preset.value()).createWorldDimensions());
/*     */     }
/*     */   }
/*     */   
/*     */   public WorldTypeEntry getWorldType() {
/* 235 */     return this.worldType;
/*     */   }
/*     */   
/*     */   public PresetEditor getPresetEditor() {
/* 239 */     Holder<WorldPreset> preset = getWorldType().preset();
/* 240 */     return (preset != null) ? PresetEditor.EDITORS.get(preset.unwrapKey()) : null;
/*     */   }
/*     */   
/*     */   public List<WorldTypeEntry> getNormalPresetList() {
/* 244 */     return this.normalPresetList;
/*     */   }
/*     */   
/*     */   public List<WorldTypeEntry> getAltPresetList() {
/* 248 */     return this.altPresetList;
/*     */   }
/*     */   
/*     */   private void updatePresetLists() {
/* 252 */     Registry<WorldPreset> presetRegistry = getSettings().worldgenLoadContext().lookupOrThrow(Registries.WORLD_PRESET);
/*     */     
/* 254 */     this.normalPresetList.clear();
/* 255 */     this.normalPresetList.addAll(getNonEmptyList(presetRegistry, WorldPresetTags.NORMAL).orElseGet(() -> presetRegistry.listElements().map(WorldTypeEntry::new).toList()));
/* 256 */     this.altPresetList.clear();
/* 257 */     this.altPresetList.addAll(getNonEmptyList(presetRegistry, WorldPresetTags.EXTENDED).orElse(this.normalPresetList));
/* 258 */     Holder<WorldPreset> preset = this.worldType.preset();
/* 259 */     if (preset != null) {
/* 260 */       WorldTypeEntry newPreset = findPreset(getSettings(), preset.unwrapKey()).<WorldTypeEntry>map(WorldTypeEntry::new).orElse(this.normalPresetList.getFirst());
/* 261 */       boolean isCustomizablePreset = (PresetEditor.EDITORS.get(preset.unwrapKey()) != null);
/* 262 */       if (isCustomizablePreset) {
/*     */ 
/*     */         
/* 265 */         this.worldType = newPreset;
/*     */       } else {
/*     */         
/* 268 */         setWorldType(newPreset);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public static final class WorldTypeEntry extends Record { private final Holder<WorldPreset> preset;
/* 273 */     public WorldTypeEntry(Holder<WorldPreset> preset) { this.preset = preset; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState$WorldTypeEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #273	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 273 */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState$WorldTypeEntry; } public Holder<WorldPreset> preset() { return this.preset; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState$WorldTypeEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #273	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState$WorldTypeEntry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState$WorldTypeEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #273	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldCreationUiState$WorldTypeEntry;
/* 274 */       //   0	8	1	o	Ljava/lang/Object; } private static final Component CUSTOM_WORLD_DESCRIPTION = (Component)Component.translatable("generator.custom");
/*     */     
/*     */     public Component describePreset() {
/* 277 */       return Optional.<Holder<WorldPreset>>ofNullable(this.preset).flatMap(Holder::unwrapKey)
/* 278 */         .map(key -> Component.translatable(key.identifier().toLanguageKey("generator")))
/* 279 */         .orElse(CUSTOM_WORLD_DESCRIPTION);
/*     */     }
/*     */     
/*     */     public boolean isAmplified() {
/* 283 */       return Optional.<Holder<WorldPreset>>ofNullable(this.preset).flatMap(Holder::unwrapKey).filter(k -> k.equals(WorldPresets.AMPLIFIED)).isPresent();
/*     */     } }
/*     */ 
/*     */   
/*     */   private static Optional<Holder<WorldPreset>> findPreset(WorldCreationContext settings, Optional<ResourceKey<WorldPreset>> preset) {
/* 288 */     return preset.flatMap(k -> settings.worldgenLoadContext().lookupOrThrow(Registries.WORLD_PRESET).get(k));
/*     */   }
/*     */   
/*     */   private static Optional<List<WorldTypeEntry>> getNonEmptyList(Registry<WorldPreset> presetRegistry, net.minecraft.tags.TagKey<WorldPreset> id) {
/* 292 */     return presetRegistry.get(id).map(tag -> tag.stream().map(WorldTypeEntry::new).toList()).filter(l -> !l.isEmpty());
/*     */   }
/*     */   
/*     */   public void setGameRules(GameRules gameRules) {
/* 296 */     this.gameRules = gameRules;
/* 297 */     onChanged();
/*     */   }
/*     */   
/*     */   public GameRules getGameRules() {
/* 301 */     return this.gameRules;
/*     */   }
/*     */   
/*     */   public enum SelectedGameMode {
/* 305 */     SURVIVAL("survival", GameType.SURVIVAL),
/* 306 */     HARDCORE("hardcore", GameType.SURVIVAL),
/* 307 */     CREATIVE("creative", GameType.CREATIVE),
/*     */     
/* 309 */     DEBUG("spectator", GameType.SPECTATOR);
/*     */     
/*     */     public final GameType gameType;
/*     */     
/*     */     public final Component displayName;
/*     */     private final Component info;
/*     */     
/*     */     SelectedGameMode(String name, GameType gameType) {
/* 317 */       this.gameType = gameType;
/* 318 */       this.displayName = (Component)Component.translatable("selectWorld.gameMode." + name);
/* 319 */       this.info = (Component)Component.translatable("selectWorld.gameMode." + name + ".info");
/*     */     }
/*     */     
/*     */     public Component getInfo() {
/* 323 */       return this.info;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/worldselection/WorldCreationUiState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */