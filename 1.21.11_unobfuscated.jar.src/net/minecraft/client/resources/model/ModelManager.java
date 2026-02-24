/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import com.google.common.collect.HashMultimap;
/*     */ import com.google.common.collect.Multimap;
/*     */ import com.google.common.collect.Multimaps;
/*     */ import com.google.common.collect.Sets;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2IntMaps;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.client.color.block.BlockColors;
/*     */ import net.minecraft.client.model.geom.EntityModelSet;
/*     */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*     */ import net.minecraft.client.renderer.SpecialBlockModelRenderer;
/*     */ import net.minecraft.client.renderer.block.BlockModelShaper;
/*     */ import net.minecraft.client.renderer.block.model.BlockModel;
/*     */ import net.minecraft.client.renderer.block.model.BlockStateModel;
/*     */ import net.minecraft.client.renderer.block.model.ItemModelGenerator;
/*     */ import net.minecraft.client.renderer.item.ClientItem;
/*     */ import net.minecraft.client.renderer.item.ItemModel;
/*     */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*     */ import net.minecraft.client.renderer.texture.SpriteLoader;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.core.registries.BuiltInRegistries;
/*     */ import net.minecraft.data.AtlasIds;
/*     */ import net.minecraft.resources.FileToIdConverter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.Zone;
/*     */ import net.minecraft.world.level.block.Block;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.material.FluidState;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ModelManager
/*     */   implements PreparableReloadListener {
/*  54 */   public static final Identifier BLOCK_OR_ITEM = Identifier.withDefaultNamespace("block_or_item");
/*  55 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  57 */   private static final FileToIdConverter MODEL_LISTER = FileToIdConverter.json("models");
/*     */   
/*  59 */   private Map<Identifier, ItemModel> bakedItemStackModels = Map.of();
/*  60 */   private Map<Identifier, ClientItem.Properties> itemProperties = Map.of();
/*     */   private final AtlasManager atlasManager;
/*     */   private final PlayerSkinRenderCache playerSkinRenderCache;
/*     */   private final BlockModelShaper blockModelShaper;
/*     */   private final BlockColors blockColors;
/*  65 */   private EntityModelSet entityModelSet = EntityModelSet.EMPTY;
/*  66 */   private SpecialBlockModelRenderer specialBlockModelRenderer = SpecialBlockModelRenderer.EMPTY;
/*     */   private ModelBakery.MissingModels missingModels;
/*  68 */   private Object2IntMap<BlockState> modelGroups = Object2IntMaps.emptyMap();
/*     */   
/*     */   public ModelManager(BlockColors blockColors, AtlasManager atlasManager, PlayerSkinRenderCache playerSkinRenderCache) {
/*  71 */     this.blockColors = blockColors;
/*  72 */     this.atlasManager = atlasManager;
/*  73 */     this.playerSkinRenderCache = playerSkinRenderCache;
/*  74 */     this.blockModelShaper = new BlockModelShaper(this);
/*     */   }
/*     */   
/*     */   public BlockStateModel getMissingBlockStateModel() {
/*  78 */     return this.missingModels.block();
/*     */   }
/*     */   
/*     */   public ItemModel getItemModel(Identifier id) {
/*  82 */     return this.bakedItemStackModels.getOrDefault(id, this.missingModels.item());
/*     */   }
/*     */   
/*     */   public ClientItem.Properties getItemProperties(Identifier id) {
/*  86 */     return this.itemProperties.getOrDefault(id, ClientItem.Properties.DEFAULT);
/*     */   }
/*     */   
/*     */   public BlockModelShaper getBlockModelShaper() {
/*  90 */     return this.blockModelShaper;
/*     */   }
/*     */ 
/*     */   
/*     */   public final CompletableFuture<Void> reload(PreparableReloadListener.SharedState currentReload, Executor taskExecutor, PreparableReloadListener.PreparationBarrier preparationBarrier, Executor reloadExecutor) {
/*  95 */     ResourceManager manager = currentReload.resourceManager();
/*  96 */     CompletableFuture<EntityModelSet> entityModelSet = CompletableFuture.supplyAsync(EntityModelSet::vanilla, taskExecutor);
/*     */ 
/*     */     
/*  99 */     CompletableFuture<SpecialBlockModelRenderer> specialBlockModelRenderer = entityModelSet.thenApplyAsync(entityModels -> SpecialBlockModelRenderer.vanilla((SpecialModelRenderer.BakingContext)new SpecialModelRenderer.BakingContext.Simple(entityModels, this.atlasManager, this.playerSkinRenderCache)), taskExecutor);
/*     */     
/* 101 */     CompletableFuture<Map<Identifier, UnbakedModel>> modelCache = loadBlockModels(manager, taskExecutor);
/* 102 */     CompletableFuture<BlockStateModelLoader.LoadedModels> blockStateModels = BlockStateModelLoader.loadBlockStates(manager, taskExecutor);
/* 103 */     CompletableFuture<ClientItemInfoLoader.LoadedClientInfos> itemStackModels = ClientItemInfoLoader.scheduleLoad(manager, taskExecutor);
/* 104 */     CompletableFuture<ResolvedModels> modelDiscovery = CompletableFuture.allOf((CompletableFuture<?>[])new CompletableFuture[] { modelCache, blockStateModels, itemStackModels
/* 105 */         }).thenApplyAsync(unused -> discoverModelDependencies(modelCache.join(), blockStateModels.join(), itemStackModels.join()), taskExecutor);
/*     */     
/* 107 */     CompletableFuture<Object2IntMap<BlockState>> modelGroups = blockStateModels.thenApplyAsync(models -> buildModelGroups(this.blockColors, models), taskExecutor);
/*     */     
/* 109 */     AtlasManager.PendingStitchResults pendingStitches = (AtlasManager.PendingStitchResults)currentReload.get(AtlasManager.PENDING_STITCH);
/* 110 */     CompletableFuture<SpriteLoader.Preparations> pendingBlockAtlasSprites = pendingStitches.get(AtlasIds.BLOCKS);
/* 111 */     CompletableFuture<SpriteLoader.Preparations> pendingItemAtlasSprites = pendingStitches.get(AtlasIds.ITEMS);
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
/* 136 */     Objects.requireNonNull(preparationBarrier); return CompletableFuture.allOf((CompletableFuture<?>[])new CompletableFuture[] { pendingBlockAtlasSprites, pendingItemAtlasSprites, modelDiscovery, modelGroups, blockStateModels, itemStackModels, entityModelSet, specialBlockModelRenderer, modelCache }).thenComposeAsync(unused -> { SpriteLoader.Preparations blockAtlasSprites = pendingBlockAtlasSprites.join(), itemAtlasSprites = pendingBlockAtlasSprites.join(); ResolvedModels resolvedModels = pendingItemAtlasSprites.join(); Object2IntMap<BlockState> groups = modelDiscovery.join(); Sets.SetView setView = Sets.difference(((Map)modelGroups.join()).keySet(), resolvedModels.models.keySet()); if (!setView.isEmpty()) LOGGER.debug("Unreferenced models: \n{}", setView.stream().sorted().map(()).collect(Collectors.joining()));  ModelBakery bakery = new ModelBakery(modelGroups.join(), this.atlasManager, this.playerSkinRenderCache, ((BlockStateModelLoader.LoadedModels)entityModelSet.join()).models(), ((ClientItemInfoLoader.LoadedClientInfos)blockStateModels.join()).contents(), resolvedModels.models(), resolvedModels.missing()); return loadModels(blockAtlasSprites, itemAtlasSprites, bakery, groups, modelGroups.join(), itemStackModels.join(), specialBlockModelRenderer); }, taskExecutor).thenCompose(preparationBarrier::wait)
/* 137 */       .thenAcceptAsync(this::apply, reloadExecutor);
/*     */   }
/*     */   
/*     */   private static CompletableFuture<Map<Identifier, UnbakedModel>> loadBlockModels(ResourceManager manager, Executor executor) {
/* 141 */     return CompletableFuture.supplyAsync(() -> MODEL_LISTER.listMatchingResources(manager), executor)
/* 142 */       .thenCompose(resources -> {
/*     */           List<CompletableFuture<Pair<Identifier, BlockModel>>> result = new ArrayList<>(resources.size());
/*     */           for (Map.Entry<Identifier, Resource> resource : (Iterable<Map.Entry<Identifier, Resource>>)resources.entrySet()) {
/*     */             result.add(CompletableFuture.supplyAsync((), executor));
/*     */           }
/*     */           return Util.sequence(result).thenApply(());
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static ResolvedModels discoverModelDependencies(Map<Identifier, UnbakedModel> allModels, BlockStateModelLoader.LoadedModels blockStateModels, ClientItemInfoLoader.LoadedClientInfos itemInfos)
/*     */   {
/* 160 */     Zone ignored = Profiler.get().zone("dependencies"); 
/* 161 */     try { ModelDiscovery result = new ModelDiscovery(allModels, MissingBlockModel.missingModel());
/* 162 */       result.addSpecialModel(ItemModelGenerator.GENERATED_ITEM_MODEL_ID, (UnbakedModel)new ItemModelGenerator());
/* 163 */       Objects.requireNonNull(result); blockStateModels.models().values().forEach(result::addRoot);
/* 164 */       itemInfos.contents().values().forEach(info -> result.addRoot((ResolvableModel)info.model()));
/* 165 */       ResolvedModels resolvedModels = new ResolvedModels(result.missingModel(), result.resolve());
/* 166 */       if (ignored != null) ignored.close();  return resolvedModels; }
/*     */     catch (Throwable throwable) { if (ignored != null)
/*     */         try { ignored.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 171 */      } private static CompletableFuture<ReloadState> loadModels(final SpriteLoader.Preparations blockAtlas, final SpriteLoader.Preparations itemAtlas, ModelBakery bakery, Object2IntMap<BlockState> modelGroups, EntityModelSet entityModelSet, SpecialBlockModelRenderer specialBlockModelRenderer, Executor taskExecutor) { final Multimap<String, Material> missingMaterials = Multimaps.synchronizedMultimap((Multimap)HashMultimap.create());
/* 172 */     final Multimap<String, String> missingReferences = Multimaps.synchronizedMultimap((Multimap)HashMultimap.create());
/*     */     
/* 174 */     return bakery.bakeModels(new SpriteGetter() {
/* 175 */           private final TextureAtlasSprite blockMissing = blockAtlas.missing();
/* 176 */           private final TextureAtlasSprite itemMissing = itemAtlas.missing();
/*     */ 
/*     */           
/*     */           public TextureAtlasSprite get(Material material, ModelDebugName name) {
/* 180 */             Identifier atlasId = material.atlasLocation();
/* 181 */             boolean itemOrBlock = atlasId.equals(ModelManager.BLOCK_OR_ITEM);
/* 182 */             boolean onlyItem = atlasId.equals(TextureAtlas.LOCATION_ITEMS);
/* 183 */             boolean onlyBlock = atlasId.equals(TextureAtlas.LOCATION_BLOCKS);
/*     */             
/* 185 */             if (itemOrBlock || onlyItem) {
/* 186 */               TextureAtlasSprite result = itemAtlas.getSprite(material.texture());
/* 187 */               if (result != null) {
/* 188 */                 return result;
/*     */               }
/*     */             } 
/*     */             
/* 192 */             if (itemOrBlock || onlyBlock) {
/* 193 */               TextureAtlasSprite result = blockAtlas.getSprite(material.texture());
/* 194 */               if (result != null) {
/* 195 */                 return result;
/*     */               }
/*     */             } 
/*     */ 
/*     */ 
/*     */             
/* 201 */             missingMaterials.put(name.debugName(), material);
/* 202 */             return onlyItem ? this.itemMissing : this.blockMissing;
/*     */           }
/*     */ 
/*     */           
/*     */           public TextureAtlasSprite reportMissingReference(String reference, ModelDebugName responsibleModel) {
/* 207 */             missingReferences.put(responsibleModel.debugName(), reference);
/* 208 */             return this.blockMissing;
/*     */           }
/*     */         }, 
/* 211 */         taskExecutor).thenApply(bakingResult -> {
/*     */           missingMaterials.asMap().forEach(());
/*     */           missingReferences.asMap().forEach(());
/*     */           Map<BlockState, BlockStateModel> modelByStateCache = createBlockStateToModelDispatch(bakingResult.blockStateModels(), bakingResult.missingModels().block());
/*     */           return new ReloadState(bakingResult, modelGroups, modelByStateCache, entityModelSet, specialBlockModelRenderer);
/*     */         }); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static Map<BlockState, BlockStateModel> createBlockStateToModelDispatch(Map<BlockState, BlockStateModel> bakedModels, BlockStateModel missingModel)
/*     */   {
/* 227 */     Zone ignored = Profiler.get().zone("block state dispatch"); 
/* 228 */     try { Map<BlockState, BlockStateModel> modelByStateCache = new IdentityHashMap<>(bakedModels);
/*     */       
/* 230 */       for (Block block : (Iterable<Block>)BuiltInRegistries.BLOCK) {
/* 231 */         block.getStateDefinition().getPossibleStates().forEach(state -> {
/*     */               if (bakedModels.putIfAbsent(state, missingModel) == null) {
/*     */                 LOGGER.warn("Missing model for variant: '{}'", state);
/*     */               }
/*     */             });
/*     */       } 
/* 237 */       Map<BlockState, BlockStateModel> map1 = modelByStateCache;
/* 238 */       if (ignored != null) ignored.close();  return map1; } catch (Throwable throwable) { if (ignored != null)
/*     */         try { ignored.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 242 */      } private static Object2IntMap<BlockState> buildModelGroups(BlockColors blockColors, BlockStateModelLoader.LoadedModels blockStateModels) { Zone ignored = Profiler.get().zone("block groups"); 
/* 243 */     try { Object2IntMap<BlockState> object2IntMap = ModelGroupCollector.build(blockColors, blockStateModels);
/* 244 */       if (ignored != null) ignored.close();  return object2IntMap; } catch (Throwable throwable) { if (ignored != null)
/*     */         try { ignored.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 248 */      } private void apply(ReloadState preparations) { ModelBakery.BakingResult bakedModels = preparations.bakedModels;
/* 249 */     this.bakedItemStackModels = bakedModels.itemStackModels();
/* 250 */     this.itemProperties = bakedModels.itemProperties();
/* 251 */     this.modelGroups = preparations.modelGroups;
/* 252 */     this.missingModels = bakedModels.missingModels();
/* 253 */     this.blockModelShaper.replaceCache(preparations.modelCache);
/* 254 */     this.specialBlockModelRenderer = preparations.specialBlockModelRenderer;
/* 255 */     this.entityModelSet = preparations.entityModelSet; }
/*     */ 
/*     */   
/*     */   public boolean requiresRender(BlockState oldState, BlockState newState) {
/* 259 */     if (oldState == newState) {
/* 260 */       return false;
/*     */     }
/* 262 */     int oldModelGroup = this.modelGroups.getInt(oldState);
/* 263 */     if (oldModelGroup != -1) {
/* 264 */       int newModelGroup = this.modelGroups.getInt(newState);
/* 265 */       if (oldModelGroup == newModelGroup) {
/* 266 */         FluidState oldFluidState = oldState.getFluidState();
/* 267 */         FluidState newFluidState = newState.getFluidState();
/* 268 */         return (oldFluidState != newFluidState);
/*     */       } 
/*     */     } 
/*     */     
/* 272 */     return true;
/*     */   }
/*     */   
/*     */   public SpecialBlockModelRenderer specialBlockModelRenderer() {
/* 276 */     return this.specialBlockModelRenderer;
/*     */   }
/*     */   
/*     */   public Supplier<EntityModelSet> entityModels() {
/* 280 */     return () -> this.entityModelSet;
/*     */   }
/*     */   private static final class ResolvedModels extends Record { private final ResolvedModel missing; private final Map<Identifier, ResolvedModel> models;
/* 283 */     private ResolvedModels(ResolvedModel missing, Map<Identifier, ResolvedModel> models) { this.missing = missing; this.models = models; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ModelManager$ResolvedModels;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #283	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 283 */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelManager$ResolvedModels; } public ResolvedModel missing() { return this.missing; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ModelManager$ResolvedModels;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #283	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelManager$ResolvedModels; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ModelManager$ResolvedModels;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #283	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/ModelManager$ResolvedModels;
/* 283 */       //   0	8	1	o	Ljava/lang/Object; } public Map<Identifier, ResolvedModel> models() { return this.models; }
/*     */      }
/*     */   private static final class ReloadState extends Record { private final ModelBakery.BakingResult bakedModels; private final Object2IntMap<BlockState> modelGroups; private final Map<BlockState, BlockStateModel> modelCache; private final EntityModelSet entityModelSet;
/*     */     private final SpecialBlockModelRenderer specialBlockModelRenderer;
/*     */     
/* 288 */     private ReloadState(ModelBakery.BakingResult bakedModels, Object2IntMap<BlockState> modelGroups, Map<BlockState, BlockStateModel> modelCache, EntityModelSet entityModelSet, SpecialBlockModelRenderer specialBlockModelRenderer) { this.bakedModels = bakedModels; this.modelGroups = modelGroups; this.modelCache = modelCache; this.entityModelSet = entityModelSet; this.specialBlockModelRenderer = specialBlockModelRenderer; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ModelManager$ReloadState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #288	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelManager$ReloadState; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ModelManager$ReloadState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #288	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelManager$ReloadState; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ModelManager$ReloadState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #288	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/ModelManager$ReloadState;
/* 288 */       //   0	8	1	o	Ljava/lang/Object; } public ModelBakery.BakingResult bakedModels() { return this.bakedModels; } public Object2IntMap<BlockState> modelGroups() { return this.modelGroups; } public Map<BlockState, BlockStateModel> modelCache() { return this.modelCache; } public EntityModelSet entityModelSet() { return this.entityModelSet; } public SpecialBlockModelRenderer specialBlockModelRenderer() { return this.specialBlockModelRenderer; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/ModelManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */