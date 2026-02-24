/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ 
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Dynamic;
/*     */ import com.mojang.serialization.Lifecycle;
/*     */ import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
/*     */ import java.io.IOException;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.Objects;
/*     */ import java.util.UUID;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.SharedConstants;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.gui.screens.AlertScreen;
/*     */ import net.minecraft.client.gui.screens.BackupConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.ConfirmScreen;
/*     */ import net.minecraft.client.gui.screens.DatapackLoadFailureScreen;
/*     */ import net.minecraft.client.gui.screens.GenericMessageScreen;
/*     */ import net.minecraft.client.gui.screens.NoticeWithLinkScreen;
/*     */ import net.minecraft.client.gui.screens.RecoverWorldDataScreen;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.resources.server.DownloadedPackSource;
/*     */ import net.minecraft.commands.Commands;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.LayeredRegistryAccess;
/*     */ import net.minecraft.core.MappedRegistry;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.registries.Registries;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.server.RegistryLayer;
/*     */ import net.minecraft.server.ReloadableServerResources;
/*     */ import net.minecraft.server.WorldLoader;
/*     */ import net.minecraft.server.WorldStem;
/*     */ import net.minecraft.server.packs.repository.PackRepository;
/*     */ import net.minecraft.server.packs.repository.ServerPacksSource;
/*     */ import net.minecraft.server.packs.resources.CloseableResourceManager;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.util.MemoryReserve;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.level.LevelSettings;
/*     */ import net.minecraft.world.level.WorldDataConfiguration;
/*     */ import net.minecraft.world.level.dimension.LevelStem;
/*     */ import net.minecraft.world.level.gamerules.GameRuleMap;
/*     */ import net.minecraft.world.level.levelgen.WorldDimensions;
/*     */ import net.minecraft.world.level.levelgen.WorldOptions;
/*     */ import net.minecraft.world.level.storage.LevelDataAndDimensions;
/*     */ import net.minecraft.world.level.storage.LevelResource;
/*     */ import net.minecraft.world.level.storage.LevelStorageSource;
/*     */ import net.minecraft.world.level.storage.LevelSummary;
/*     */ import net.minecraft.world.level.storage.PrimaryLevelData;
/*     */ import net.minecraft.world.level.storage.WorldData;
/*     */ import net.minecraft.world.level.validation.ContentValidationException;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class WorldOpenFlows
/*     */ {
/*  70 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  71 */   private static final UUID WORLD_PACK_ID = UUID.fromString("640a6a92-b6cb-48a0-b391-831586500359");
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   private final LevelStorageSource levelSource;
/*     */   
/*     */   public WorldOpenFlows(Minecraft minecraft, LevelStorageSource levelSource) {
/*  77 */     this.minecraft = minecraft;
/*  78 */     this.levelSource = levelSource;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void createFreshLevel(String levelId, LevelSettings levelSettings, WorldOptions options, Function<HolderLookup.Provider, WorldDimensions> dimensionsProvider, Screen parentScreen) {
/*  85 */     this.minecraft.setScreenAndShow((Screen)new GenericMessageScreen((Component)Component.translatable("selectWorld.data_read")));
/*  86 */     LevelStorageSource.LevelStorageAccess levelSourceAccess = createWorldAccess(levelId);
/*  87 */     if (levelSourceAccess == null) {
/*     */       return;
/*     */     }
/*     */     
/*  91 */     PackRepository packRepository = ServerPacksSource.createPackRepository(levelSourceAccess);
/*     */     
/*  93 */     WorldDataConfiguration dataConfiguration = levelSettings.getDataConfiguration();
/*     */     try {
/*  95 */       WorldLoader.PackConfig packConfig = new WorldLoader.PackConfig(packRepository, dataConfiguration, false, false);
/*  96 */       WorldStem worldStem = loadWorldDataBlocking(packConfig, context -> { WorldDimensions.Complete dimensions = ((WorldDimensions)dimensionsProvider.apply(context.datapackWorldgen())).bake(context.datapackDimensions().lookupOrThrow(Registries.LEVEL_STEM)); return new WorldLoader.DataLoadOutput(new PrimaryLevelData(levelSettings, options, dimensions.specialWorldProperty(), dimensions.lifecycle()), dimensions.dimensionsRegistryAccess()); }, WorldStem::new);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 106 */       this.minecraft.doWorldLoad(levelSourceAccess, packRepository, worldStem, true);
/* 107 */     } catch (Exception e) {
/* 108 */       LOGGER.warn("Failed to load datapacks, can't proceed with server load", e);
/* 109 */       levelSourceAccess.safeClose();
/* 110 */       this.minecraft.setScreen(parentScreen);
/*     */     } 
/*     */   }
/*     */   
/*     */   private LevelStorageSource.LevelStorageAccess createWorldAccess(String levelId) {
/*     */     try {
/* 116 */       return this.levelSource.validateAndCreateAccess(levelId);
/* 117 */     } catch (IOException e) {
/* 118 */       LOGGER.warn("Failed to read level {} data", levelId, e);
/* 119 */       SystemToast.onWorldAccessFailure(this.minecraft, levelId);
/* 120 */       this.minecraft.setScreen(null);
/* 121 */       return null;
/* 122 */     } catch (ContentValidationException e) {
/* 123 */       LOGGER.warn("{}", e.getMessage());
/* 124 */       this.minecraft.setScreen(NoticeWithLinkScreen.createWorldSymlinkWarningScreen(() -> this.minecraft.setScreen(null)));
/* 125 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void createLevelFromExistingSettings(LevelStorageSource.LevelStorageAccess levelSourceAccess, ReloadableServerResources serverResources, LayeredRegistryAccess<RegistryLayer> registryAccess, WorldData worldData) {
/* 131 */     PackRepository packRepository = ServerPacksSource.createPackRepository(levelSourceAccess);
/* 132 */     CloseableResourceManager resourceManager = (CloseableResourceManager)new WorldLoader.PackConfig(packRepository, worldData.getDataConfiguration(), false, false).createResourceManager().getSecond();
/* 133 */     this.minecraft.doWorldLoad(levelSourceAccess, packRepository, new WorldStem(resourceManager, serverResources, registryAccess, worldData), true);
/*     */   }
/*     */   
/*     */   public WorldStem loadWorldStem(Dynamic<?> levelDataTag, boolean safeMode, PackRepository packRepository) throws Exception {
/* 137 */     WorldLoader.PackConfig packConfig = LevelStorageSource.getPackConfig(levelDataTag, packRepository, safeMode);
/* 138 */     return loadWorldDataBlocking(packConfig, context -> { Registry<LevelStem> datapackDimensions = context.datapackDimensions().lookupOrThrow(Registries.LEVEL_STEM); LevelDataAndDimensions data = LevelStorageSource.getLevelDataAndDimensions(levelDataTag, context.dataConfiguration(), datapackDimensions, context.datapackWorldgen()); return new WorldLoader.DataLoadOutput(data.worldData(), data.dimensions().dimensionsRegistryAccess()); }, WorldStem::new);
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
/*     */   public Pair<LevelSettings, WorldCreationContext> recreateWorldData(LevelStorageSource.LevelStorageAccess levelSourceAccess) throws Exception {
/* 153 */     PackRepository packRepository = ServerPacksSource.createPackRepository(levelSourceAccess);
/*     */     
/* 155 */     Dynamic<?> levelDataTag = levelSourceAccess.getDataTag();
/* 156 */     WorldLoader.PackConfig packConfig = LevelStorageSource.getPackConfig(levelDataTag, packRepository, false);
/* 157 */     return loadWorldDataBlocking(packConfig, context -> {
/*     */           Registry<LevelStem> noDatapackDimensions = new MappedRegistry(Registries.LEVEL_STEM, Lifecycle.stable()).freeze();
/*     */           LevelDataAndDimensions existingData = LevelStorageSource.getLevelDataAndDimensions(levelDataTag, context.dataConfiguration(), noDatapackDimensions, context.datapackWorldgen());
/*     */           static final class Data extends Record {
/*     */             private final LevelSettings levelSettings;
/*     */             private final WorldOptions options;
/*     */             private final Registry<LevelStem> existingDimensions;
/*     */             Data(LevelSettings levelSettings, WorldOptions options, Registry<LevelStem> existingDimensions) { this.levelSettings = levelSettings;
/*     */               this.options = options;
/*     */               this.existingDimensions = existingDimensions; } public final String toString() { // Byte code:
/*     */               //   0: aload_0
/*     */               //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data;)Ljava/lang/String;
/*     */               //   6: areturn
/*     */               // Line number table:
/*     */               //   Java source line number -> byte code offset
/*     */               //   #152	-> 0
/*     */               // Local variable table:
/*     */               //   start	length	slot	name	descriptor
/*     */               //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data; } public final int hashCode() { // Byte code:
/*     */               //   0: aload_0
/*     */               //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data;)I
/*     */               //   6: ireturn
/*     */               // Line number table:
/*     */               //   Java source line number -> byte code offset
/*     */               //   #152	-> 0
/*     */               // Local variable table:
/*     */               //   start	length	slot	name	descriptor
/*     */               //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data; } public final boolean equals(Object o) {
/*     */               // Byte code:
/*     */               //   0: aload_0
/*     */               //   1: aload_1
/*     */               //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data;Ljava/lang/Object;)Z
/*     */               //   7: ireturn
/*     */               // Line number table:
/*     */               //   Java source line number -> byte code offset
/*     */               //   #152	-> 0
/*     */               // Local variable table:
/*     */               //   start	length	slot	name	descriptor
/*     */               //   0	8	0	this	Lnet/minecraft/client/gui/screens/worldselection/WorldOpenFlows$1Data;
/*     */               //   0	8	1	o	Ljava/lang/Object;
/*     */             } public LevelSettings levelSettings() {
/*     */               return this.levelSettings;
/*     */             } public WorldOptions options() {
/*     */               return this.options;
/*     */             } public Registry<LevelStem> existingDimensions() {
/*     */               return this.existingDimensions;
/*     */             } };
/*     */           return new WorldLoader.DataLoadOutput(new Data(existingData.worldData().getLevelSettings(), existingData.worldData().worldGenOptions(), existingData.dimensions().dimensions()), context.datapackDimensions());
/*     */         }, (resources, managers, registries, loadedData) -> {
/*     */           resources.close();
/*     */           InitialWorldCreationOptions initialWorldCreationOptions = new InitialWorldCreationOptions(WorldCreationUiState.SelectedGameMode.SURVIVAL, GameRuleMap.of(), null);
/*     */           return Pair.of(loadedData.levelSettings, new WorldCreationContext(loadedData.options, new WorldDimensions(loadedData.existingDimensions), registries, managers, loadedData.levelSettings.getDataConfiguration(), initialWorldCreationOptions));
/*     */         });
/*     */   } private <D, R> R loadWorldDataBlocking(WorldLoader.PackConfig packConfig, WorldLoader.WorldDataSupplier<D> worldDataGetter, WorldLoader.ResultFactory<D, R> worldDataSupplier) throws Exception {
/* 182 */     WorldLoader.InitConfig config = new WorldLoader.InitConfig(packConfig, Commands.CommandSelection.INTEGRATED, (PermissionSet)LevelBasedPermissionSet.GAMEMASTER);
/* 183 */     CompletableFuture<R> resourceLoad = WorldLoader.load(config, worldDataGetter, worldDataSupplier, (Executor)Util.backgroundExecutor(), (Executor)this.minecraft);
/* 184 */     Objects.requireNonNull(resourceLoad); this.minecraft.managedBlock(resourceLoad::isDone);
/* 185 */     return resourceLoad.get();
/*     */   }
/*     */ 
/*     */   
/*     */   private void askForBackup(LevelStorageSource.LevelStorageAccess worldAccess, boolean oldCustomized, Runnable proceedCallback, Runnable cancelCallback) {
/*     */     MutableComponent mutableComponent1, mutableComponent2;
/* 191 */     if (oldCustomized) {
/* 192 */       mutableComponent1 = Component.translatable("selectWorld.backupQuestion.customized");
/* 193 */       mutableComponent2 = Component.translatable("selectWorld.backupWarning.customized");
/*     */     } else {
/* 195 */       mutableComponent1 = Component.translatable("selectWorld.backupQuestion.experimental");
/* 196 */       mutableComponent2 = Component.translatable("selectWorld.backupWarning.experimental");
/*     */     } 
/*     */     
/* 199 */     this.minecraft.setScreen((Screen)new BackupConfirmScreen(cancelCallback, (backup, eraseCache) -> { if (backup) EditWorldScreen.makeBackupAndShowToast(worldAccess);  proceedCallback.run(); }, (Component)mutableComponent1, (Component)mutableComponent2, false));
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
/*     */   public static void confirmWorldCreation(Minecraft minecraft, CreateWorldScreen parent, Lifecycle lifecycle, Runnable task, boolean skipWarning) {
/*     */     BooleanConsumer callback = confirmed -> {
/*     */         if (confirmed) {
/*     */           task.run();
/*     */         } else {
/*     */           minecraft.setScreen(parent);
/*     */         } 
/*     */       };
/* 221 */     if (skipWarning || lifecycle == Lifecycle.stable()) {
/* 222 */       task.run();
/* 223 */     } else if (lifecycle == Lifecycle.experimental()) {
/* 224 */       minecraft.setScreen((Screen)new ConfirmScreen(callback, 
/*     */             
/* 226 */             (Component)Component.translatable("selectWorld.warning.experimental.title"), 
/* 227 */             (Component)Component.translatable("selectWorld.warning.experimental.question")));
/*     */     }
/*     */     else {
/*     */       
/* 231 */       minecraft.setScreen((Screen)new ConfirmScreen(callback, 
/*     */             
/* 233 */             (Component)Component.translatable("selectWorld.warning.deprecated.title"), 
/* 234 */             (Component)Component.translatable("selectWorld.warning.deprecated.question")));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void openWorld(String levelId, Runnable onCancel) {
/* 240 */     this.minecraft.setScreenAndShow((Screen)new GenericMessageScreen((Component)Component.translatable("selectWorld.data_read")));
/* 241 */     LevelStorageSource.LevelStorageAccess worldAccess = createWorldAccess(levelId);
/* 242 */     if (worldAccess == null) {
/*     */       return;
/*     */     }
/* 245 */     openWorldLoadLevelData(worldAccess, onCancel);
/*     */   } private void openWorldLoadLevelData(LevelStorageSource.LevelStorageAccess worldAccess, Runnable onCancel) {
/*     */     Dynamic<?> levelDataTag;
/*     */     LevelSummary summary;
/* 249 */     this.minecraft.setScreenAndShow((Screen)new GenericMessageScreen((Component)Component.translatable("selectWorld.data_read")));
/*     */ 
/*     */     
/*     */     try {
/* 253 */       levelDataTag = worldAccess.getDataTag();
/* 254 */       summary = worldAccess.getSummary(levelDataTag);
/* 255 */     } catch (IOException|net.minecraft.nbt.NbtException|net.minecraft.nbt.ReportedNbtException e) {
/* 256 */       this.minecraft.setScreen((Screen)new RecoverWorldDataScreen(this.minecraft, success -> { if (onCancel) { openWorldLoadLevelData(worldAccess, worldAccess); } else { worldAccess.safeClose(); worldAccess.run(); }  }, worldAccess));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/* 265 */     } catch (OutOfMemoryError e) {
/* 266 */       MemoryReserve.release();
/*     */       
/* 268 */       String detailedMessage = "Ran out of memory trying to read level data of world folder \"" + worldAccess.getLevelId() + "\"";
/* 269 */       LOGGER.error(LogUtils.FATAL_MARKER, detailedMessage);
/*     */       
/* 271 */       OutOfMemoryError detailedException = new OutOfMemoryError("Ran out of memory reading level data");
/* 272 */       detailedException.initCause(e);
/* 273 */       CrashReport crashReport = CrashReport.forThrowable(detailedException, detailedMessage);
/* 274 */       CrashReportCategory worldDetails = crashReport.addCategory("World details");
/* 275 */       worldDetails.setDetail("World folder", worldAccess.getLevelId());
/*     */       
/* 277 */       throw new ReportedException(crashReport);
/*     */     } 
/* 279 */     openWorldCheckVersionCompatibility(worldAccess, summary, levelDataTag, onCancel);
/*     */   }
/*     */   
/*     */   private void openWorldCheckVersionCompatibility(LevelStorageSource.LevelStorageAccess worldAccess, LevelSummary summary, Dynamic<?> levelDataTag, Runnable onCancel) {
/* 283 */     if (!summary.isCompatible()) {
/* 284 */       worldAccess.safeClose();
/* 285 */       this.minecraft.setScreen((Screen)new AlertScreen(onCancel, (Component)Component.translatable("selectWorld.incompatible.title").withColor(-65536), (Component)Component.translatable("selectWorld.incompatible.description", new Object[] { summary.getWorldVersionName() })));
/*     */       return;
/*     */     } 
/* 288 */     LevelSummary.BackupStatus backupStatus = summary.backupStatus();
/* 289 */     if (backupStatus.shouldBackup()) {
/* 290 */       String questionKey = "selectWorld.backupQuestion." + backupStatus.getTranslationKey();
/* 291 */       String warningKey = "selectWorld.backupWarning." + backupStatus.getTranslationKey();
/* 292 */       MutableComponent backupQuestion = Component.translatable(questionKey);
/* 293 */       if (backupStatus.isSevere()) {
/* 294 */         backupQuestion.withColor(-2142128);
/*     */       }
/* 296 */       MutableComponent mutableComponent1 = Component.translatable(warningKey, new Object[] { summary.getWorldVersionName(), SharedConstants.getCurrentVersion().name() });
/*     */       
/* 298 */       this.minecraft.setScreen((Screen)new BackupConfirmScreen(() -> { worldAccess.safeClose(); onCancel.run(); }, (backup, eraseCache) -> { if (onCancel) EditWorldScreen.makeBackupAndShowToast(worldAccess);  openWorldLoadLevelStem(worldAccess, worldAccess, false, levelDataTag); }, (Component)backupQuestion, (Component)mutableComponent1, false));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     }
/*     */     else {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 314 */       openWorldLoadLevelStem(worldAccess, levelDataTag, false, onCancel);
/*     */     } 
/*     */   }
/*     */   private void openWorldLoadLevelStem(LevelStorageSource.LevelStorageAccess worldAccess, Dynamic<?> levelDataTag, boolean safeMode, Runnable onCancel) {
/*     */     WorldStem worldStem;
/* 319 */     this.minecraft.setScreenAndShow((Screen)new GenericMessageScreen((Component)Component.translatable("selectWorld.resource_load")));
/* 320 */     PackRepository packRepository = ServerPacksSource.createPackRepository(worldAccess);
/*     */ 
/*     */     
/*     */     try {
/* 324 */       worldStem = loadWorldStem(levelDataTag, safeMode, packRepository);
/* 325 */       for (LevelStem levelStem : (Iterable<LevelStem>)worldStem.registries().compositeAccess().lookupOrThrow(Registries.LEVEL_STEM)) {
/* 326 */         levelStem.generator().validate();
/*     */       }
/* 328 */     } catch (Exception e) {
/* 329 */       LOGGER.warn("Failed to load level data or datapacks, can't proceed with server load", e);
/* 330 */       if (!safeMode) {
/* 331 */         this.minecraft.setScreen((Screen)new DatapackLoadFailureScreen(() -> {
/*     */                 worldAccess.safeClose();
/*     */                 onCancel.run();
/*     */               }, () -> openWorldLoadLevelStem(worldAccess, levelDataTag, true, onCancel)));
/*     */       } else {
/* 336 */         worldAccess.safeClose();
/* 337 */         this.minecraft.setScreen((Screen)new AlertScreen(onCancel, (Component)Component.translatable("datapackFailure.safeMode.failed.title"), (Component)Component.translatable("datapackFailure.safeMode.failed.description"), CommonComponents.GUI_BACK, true));
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 342 */     openWorldCheckWorldStemCompatibility(worldAccess, worldStem, packRepository, onCancel);
/*     */   }
/*     */   
/*     */   private void openWorldCheckWorldStemCompatibility(LevelStorageSource.LevelStorageAccess worldAccess, WorldStem worldStem, PackRepository packRepository, Runnable onCancel) {
/* 346 */     WorldData data = worldStem.worldData();
/*     */     
/* 348 */     boolean oldCustomized = data.worldGenOptions().isOldCustomizedWorld();
/* 349 */     boolean unstable = (data.worldGenSettingsLifecycle() != Lifecycle.stable());
/*     */     
/* 351 */     if (oldCustomized || unstable) {
/* 352 */       askForBackup(worldAccess, oldCustomized, () -> openWorldLoadBundledResourcePack(worldAccess, worldStem, packRepository, onCancel), () -> {
/*     */             worldStem.close();
/*     */             
/*     */             worldAccess.safeClose();
/*     */             onCancel.run();
/*     */           });
/*     */       return;
/*     */     } 
/* 360 */     openWorldLoadBundledResourcePack(worldAccess, worldStem, packRepository, onCancel);
/*     */   }
/*     */   
/*     */   private void openWorldLoadBundledResourcePack(LevelStorageSource.LevelStorageAccess worldAccess, WorldStem worldStem, PackRepository packRepository, Runnable onCancel) {
/* 364 */     DownloadedPackSource packSource = this.minecraft.getDownloadedPackSource();
/* 365 */     loadBundledResourcePack(packSource, worldAccess)
/* 366 */       .thenApply(unused -> true)
/* 367 */       .exceptionallyComposeAsync(t -> {
/*     */           LOGGER.warn("Failed to load pack: ", t);
/*     */           
/*     */           return promptBundledPackLoadFailure();
/* 371 */         }, (Executor)this.minecraft).thenAcceptAsync(result -> {
/*     */           if (onCancel) {
/*     */             openWorldCheckDiskSpace(worldAccess, worldAccess, worldAccess, packSource, packSource);
/*     */           } else {
/*     */             worldAccess.popAll();
/*     */             
/*     */             worldAccess.close();
/*     */             worldAccess.safeClose();
/*     */             packSource.run();
/*     */           } 
/* 381 */         }, (Executor)this.minecraft).exceptionally(e -> {
/*     */           this.minecraft.delayCrash(CrashReport.forThrowable(e, "Load world"));
/*     */           return null;
/*     */         });
/*     */   }
/*     */   
/*     */   private void openWorldCheckDiskSpace(LevelStorageSource.LevelStorageAccess worldAccess, WorldStem worldStem, DownloadedPackSource packSource, PackRepository packRepository, Runnable onCancel) {
/* 388 */     if (worldAccess.checkForLowDiskSpace()) {
/* 389 */       this.minecraft.setScreen((Screen)new ConfirmScreen(skip -> {
/*     */               if (onCancel) {
/*     */                 openWorldDoLoad(worldAccess, worldAccess, worldStem);
/*     */               } else {
/*     */                 packRepository.popAll();
/*     */                 
/*     */                 worldAccess.close();
/*     */                 
/*     */                 worldAccess.safeClose();
/*     */                 packSource.run();
/*     */               } 
/* 400 */             }, (Component)Component.translatable("selectWorld.warning.lowDiskSpace.title").withStyle(ChatFormatting.RED), 
/* 401 */             (Component)Component.translatable("selectWorld.warning.lowDiskSpace.description"), CommonComponents.GUI_CONTINUE, CommonComponents.GUI_BACK));
/*     */     
/*     */     }
/*     */     else {
/*     */       
/* 406 */       openWorldDoLoad(worldAccess, worldStem, packRepository);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void openWorldDoLoad(LevelStorageSource.LevelStorageAccess worldAccess, WorldStem worldStem, PackRepository packRepository) {
/* 411 */     this.minecraft.doWorldLoad(worldAccess, packRepository, worldStem, false);
/*     */   }
/*     */   
/*     */   private CompletableFuture<Void> loadBundledResourcePack(DownloadedPackSource packSource, LevelStorageSource.LevelStorageAccess levelSourceAccess) {
/* 415 */     Path mapResourceFile = levelSourceAccess.getLevelPath(LevelResource.MAP_RESOURCE_FILE);
/* 416 */     if (Files.exists(mapResourceFile, new java.nio.file.LinkOption[0]) && !Files.isDirectory(mapResourceFile, new java.nio.file.LinkOption[0])) {
/* 417 */       packSource.configureForLocalWorld();
/* 418 */       CompletableFuture<Void> result = packSource.waitForPackFeedback(WORLD_PACK_ID);
/* 419 */       packSource.pushLocalPack(WORLD_PACK_ID, mapResourceFile);
/* 420 */       return result;
/*     */     } 
/* 422 */     return CompletableFuture.completedFuture(null);
/*     */   }
/*     */ 
/*     */   
/*     */   private CompletableFuture<Boolean> promptBundledPackLoadFailure() {
/* 427 */     CompletableFuture<Boolean> result = new CompletableFuture<>();
/*     */     
/* 429 */     Objects.requireNonNull(result); this.minecraft.setScreen((Screen)new ConfirmScreen(result::complete, 
/* 430 */           (Component)Component.translatable("multiplayer.texturePrompt.failure.line1"), 
/* 431 */           (Component)Component.translatable("multiplayer.texturePrompt.failure.line2"), CommonComponents.GUI_PROCEED, CommonComponents.GUI_CANCEL));
/*     */ 
/*     */ 
/*     */     
/* 435 */     return result;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/worldselection/WorldOpenFlows.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */