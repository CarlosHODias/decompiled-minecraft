/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import com.google.common.collect.Interner;
/*     */ import com.google.common.collect.Interners;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.client.model.geom.EntityModelSet;
/*     */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.block.model.BlockModelPart;
/*     */ import net.minecraft.client.renderer.block.model.BlockStateModel;
/*     */ import net.minecraft.client.renderer.block.model.ItemTransforms;
/*     */ import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
/*     */ import net.minecraft.client.renderer.block.model.SingleVariant;
/*     */ import net.minecraft.client.renderer.block.model.TextureSlots;
/*     */ import net.minecraft.client.renderer.item.ClientItem;
/*     */ import net.minecraft.client.renderer.item.ItemModel;
/*     */ import net.minecraft.client.renderer.item.MissingItemModel;
/*     */ import net.minecraft.client.renderer.item.ModelRenderProperties;
/*     */ import net.minecraft.client.renderer.rendertype.RenderType;
/*     */ import net.minecraft.client.renderer.rendertype.RenderTypes;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.thread.ParallelMapTransform;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import org.joml.Vector3fc;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ModelBakery
/*     */ {
/*  39 */   public static final Material FIRE_0 = Sheets.BLOCKS_MAPPER.defaultNamespaceApply("fire_0");
/*  40 */   public static final Material FIRE_1 = Sheets.BLOCKS_MAPPER.defaultNamespaceApply("fire_1");
/*  41 */   public static final Material LAVA_STILL = Sheets.BLOCKS_MAPPER.defaultNamespaceApply("lava_still");
/*  42 */   public static final Material LAVA_FLOW = Sheets.BLOCKS_MAPPER.defaultNamespaceApply("lava_flow");
/*  43 */   public static final Material WATER_STILL = Sheets.BLOCKS_MAPPER.defaultNamespaceApply("water_still");
/*  44 */   public static final Material WATER_FLOW = Sheets.BLOCKS_MAPPER.defaultNamespaceApply("water_flow");
/*  45 */   public static final Material WATER_OVERLAY = Sheets.BLOCKS_MAPPER.defaultNamespaceApply("water_overlay");
/*     */   
/*  47 */   public static final Material BANNER_BASE = new Material(Sheets.BANNER_SHEET, Identifier.withDefaultNamespace("entity/banner_base"));
/*  48 */   public static final Material SHIELD_BASE = new Material(Sheets.SHIELD_SHEET, Identifier.withDefaultNamespace("entity/shield_base"));
/*  49 */   public static final Material NO_PATTERN_SHIELD = new Material(Sheets.SHIELD_SHEET, Identifier.withDefaultNamespace("entity/shield_base_nopattern")); public static final int DESTROY_STAGE_COUNT = 10; public static final List<Identifier> DESTROY_STAGES; public static final List<Identifier> BREAKING_LOCATIONS;
/*     */   
/*     */   static {
/*  52 */     DESTROY_STAGES = (List<Identifier>)IntStream.range(0, 10).mapToObj(i -> Identifier.withDefaultNamespace("block/destroy_stage_" + i)).collect(Collectors.toList());
/*     */ 
/*     */ 
/*     */     
/*  56 */     BREAKING_LOCATIONS = (List<Identifier>)DESTROY_STAGES.stream().map(location -> location.withPath(())).collect(Collectors.toList());
/*     */   }
/*  58 */   public static final List<RenderType> DESTROY_TYPES = (List<RenderType>)BREAKING_LOCATIONS.stream().map(RenderTypes::crumbling).collect(Collectors.toList());
/*     */   
/*  60 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final EntityModelSet entityModelSet;
/*     */   
/*     */   private final MaterialSet materials;
/*     */   
/*     */   private final PlayerSkinRenderCache playerSkinRenderCache;
/*     */   private final Map<BlockState, BlockStateModel.UnbakedRoot> unbakedBlockStateModels;
/*     */   private final Map<Identifier, ClientItem> clientInfos;
/*     */   private final Map<Identifier, ResolvedModel> resolvedModels;
/*     */   private final ResolvedModel missingModel;
/*     */   
/*     */   public ModelBakery(EntityModelSet entityModelSet, MaterialSet materials, PlayerSkinRenderCache playerSkinRenderCache, Map<BlockState, BlockStateModel.UnbakedRoot> unbakedBlockStateModels, Map<Identifier, ClientItem> clientInfos, Map<Identifier, ResolvedModel> resolvedModels, ResolvedModel missingModel) {
/*  73 */     this.entityModelSet = entityModelSet;
/*  74 */     this.materials = materials;
/*  75 */     this.playerSkinRenderCache = playerSkinRenderCache;
/*  76 */     this.unbakedBlockStateModels = unbakedBlockStateModels;
/*  77 */     this.clientInfos = clientInfos;
/*  78 */     this.resolvedModels = resolvedModels;
/*  79 */     this.missingModel = missingModel;
/*     */   }
/*     */   public static final class MissingModels extends Record { private final BlockModelPart blockPart; private final BlockStateModel block; private final ItemModel item;
/*  82 */     public MissingModels(BlockModelPart blockPart, BlockStateModel block, ItemModel item) { this.blockPart = blockPart; this.block = block; this.item = item; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ModelBakery$MissingModels;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #82	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelBakery$MissingModels; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ModelBakery$MissingModels;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #82	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelBakery$MissingModels; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ModelBakery$MissingModels;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #82	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/ModelBakery$MissingModels;
/*  82 */       //   0	8	1	o	Ljava/lang/Object; } public BlockModelPart blockPart() { return this.blockPart; } public BlockStateModel block() { return this.block; } public ItemModel item() { return this.item; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static MissingModels bake(ResolvedModel unbaked, final SpriteGetter sprites, final ModelBaker.PartCache parts) {
/*  88 */       ModelBaker missingModelBakery = new ModelBaker()
/*     */         {
/*     */           public ResolvedModel getModel(Identifier location) {
/*  91 */             throw new IllegalStateException("Missing model can't have dependencies, but asked for " + String.valueOf(location));
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public BlockModelPart missingBlockModelPart() {
/*  97 */             throw new IllegalStateException();
/*     */           }
/*     */ 
/*     */ 
/*     */           
/*     */           public <T> T compute(ModelBaker.SharedOperationKey<T> key) {
/* 103 */             return key.compute(this);
/*     */           }
/*     */ 
/*     */           
/*     */           public SpriteGetter sprites() {
/* 108 */             return sprites;
/*     */           }
/*     */ 
/*     */           
/*     */           public ModelBaker.PartCache parts() {
/* 113 */             return parts;
/*     */           }
/*     */         };
/*     */       
/* 117 */       TextureSlots textureSlots = unbaked.getTopTextureSlots();
/* 118 */       boolean hasAmbientOcclusion = unbaked.getTopAmbientOcclusion();
/* 119 */       boolean usesBlockLight = unbaked.getTopGuiLight().lightLikeBlock();
/* 120 */       ItemTransforms transforms = unbaked.getTopTransforms();
/* 121 */       QuadCollection geometry = unbaked.bakeTopGeometry(textureSlots, missingModelBakery, BlockModelRotation.IDENTITY);
/*     */       
/* 123 */       TextureAtlasSprite particleSprite = unbaked.resolveParticleSprite(textureSlots, missingModelBakery);
/*     */       
/* 125 */       SimpleModelWrapper missingModelPart = new SimpleModelWrapper(geometry, hasAmbientOcclusion, particleSprite);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 130 */       SingleVariant singleVariant = new SingleVariant((BlockModelPart)missingModelPart);
/*     */       
/* 132 */       MissingItemModel missingItemModel = new MissingItemModel(
/* 133 */           geometry.getAll(), new ModelRenderProperties(usesBlockLight, particleSprite, transforms));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 141 */       return new MissingModels((BlockModelPart)missingModelPart, (BlockStateModel)singleVariant, (ItemModel)missingItemModel);
/*     */     } }
/*     */   class null implements ModelBaker {
/*     */     public ResolvedModel getModel(Identifier location) { throw new IllegalStateException("Missing model can't have dependencies, but asked for " + String.valueOf(location)); }
/*     */     public BlockModelPart missingBlockModelPart() { throw new IllegalStateException(); } public <T> T compute(ModelBaker.SharedOperationKey<T> key) { return key.compute(this); } public SpriteGetter sprites() { return sprites; } public ModelBaker.PartCache parts() { return parts; }
/* 146 */   } public CompletableFuture<BakingResult> bakeModels(SpriteGetter sprites, Executor taskExecutor) { PartCacheImpl parts = new PartCacheImpl();
/* 147 */     MissingModels missingModels = MissingModels.bake(this.missingModel, sprites, parts);
/*     */     
/* 149 */     ModelBakerImpl baker = new ModelBakerImpl(sprites, parts, missingModels);
/* 150 */     CompletableFuture<Map<BlockState, BlockStateModel>> bakedBlockStateModelFuture = ParallelMapTransform.schedule(this.unbakedBlockStateModels, (blockState, model) -> {
/*     */ 
/*     */           
/*     */           try {
/*     */             return model.bake(blockState, baker);
/* 155 */           } catch (Exception e) {
/*     */             LOGGER.warn("Unable to bake model: '{}': {}", blockState, e);
/*     */ 
/*     */             
/*     */             return null;
/*     */           } 
/*     */         }, taskExecutor);
/*     */     
/* 163 */     CompletableFuture<Map<Identifier, ItemModel>> bakedItemStackModelFuture = ParallelMapTransform.schedule(this.clientInfos, (location, clientInfo) -> {
/*     */ 
/*     */           
/*     */           try {
/*     */             return clientInfo.model().bake(new ItemModel.BakingContext(baker, this.entityModelSet, this.materials, this.playerSkinRenderCache, baker.item, clientInfo.registrySwapper()));
/* 168 */           } catch (Exception e) {
/*     */             LOGGER.warn("Unable to bake item model: '{}'", missingModels, e);
/*     */ 
/*     */             
/*     */             return null;
/*     */           } 
/*     */         }, taskExecutor);
/*     */     
/* 176 */     Map<Identifier, ClientItem.Properties> itemStackModelProperties = new HashMap<>(this.clientInfos.size());
/* 177 */     this.clientInfos.forEach((id, clientInfo) -> {
/*     */           ClientItem.Properties properties = clientInfo.properties();
/*     */           
/*     */           if (!properties.equals(ClientItem.Properties.DEFAULT)) {
/*     */             itemStackModelProperties.put(id, properties);
/*     */           }
/*     */         });
/*     */     
/* 185 */     return bakedBlockStateModelFuture.thenCombine(bakedItemStackModelFuture, (bakedBlockStateModels, bakedItemStateModels) -> new BakingResult(missingModels, bakedBlockStateModels, bakedItemStateModels, itemStackModelProperties)); }
/*     */ 
/*     */ 
/*     */   
/*     */   private class ModelBakerImpl
/*     */     implements ModelBaker
/*     */   {
/*     */     private final SpriteGetter sprites;
/*     */     private final ModelBaker.PartCache parts;
/*     */     private final ModelBakery.MissingModels missingModels;
/* 195 */     private final Map<ModelBaker.SharedOperationKey<Object>, Object> operationCache = new ConcurrentHashMap<>(); private final Function<ModelBaker.SharedOperationKey<Object>, Object> cacheComputeFunction; private ModelBakerImpl(SpriteGetter textures, ModelBaker.PartCache parts, ModelBakery.MissingModels missingModels) {
/* 196 */       this.cacheComputeFunction = (k -> k.compute(this));
/*     */ 
/*     */       
/* 199 */       this.sprites = textures;
/* 200 */       this.parts = parts;
/* 201 */       this.missingModels = missingModels;
/*     */     }
/*     */ 
/*     */     
/*     */     public BlockModelPart missingBlockModelPart() {
/* 206 */       return this.missingModels.blockPart;
/*     */     }
/*     */ 
/*     */     
/*     */     public SpriteGetter sprites() {
/* 211 */       return this.sprites;
/*     */     }
/*     */ 
/*     */     
/*     */     public ModelBaker.PartCache parts() {
/* 216 */       return this.parts;
/*     */     }
/*     */ 
/*     */     
/*     */     public ResolvedModel getModel(Identifier location) {
/* 221 */       ResolvedModel result = ModelBakery.this.resolvedModels.get(location);
/* 222 */       if (result == null) {
/* 223 */         ModelBakery.LOGGER.warn("Requested a model that was not discovered previously: {}", location);
/* 224 */         return ModelBakery.this.missingModel;
/*     */       } 
/* 226 */       return result;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public <T> T compute(ModelBaker.SharedOperationKey<T> key) {
/* 232 */       return (T)this.operationCache.computeIfAbsent(key, this.cacheComputeFunction);
/*     */     } }
/*     */   public static final class BakingResult extends Record { private final ModelBakery.MissingModels missingModels; private final Map<BlockState, BlockStateModel> blockStateModels; private final Map<Identifier, ItemModel> itemStackModels; private final Map<Identifier, ClientItem.Properties> itemProperties;
/*     */     
/* 236 */     public BakingResult(ModelBakery.MissingModels missingModels, Map<BlockState, BlockStateModel> blockStateModels, Map<Identifier, ItemModel> itemStackModels, Map<Identifier, ClientItem.Properties> itemProperties) { this.missingModels = missingModels; this.blockStateModels = blockStateModels; this.itemStackModels = itemStackModels; this.itemProperties = itemProperties; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/ModelBakery$BakingResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #236	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelBakery$BakingResult; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/ModelBakery$BakingResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #236	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/ModelBakery$BakingResult; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/ModelBakery$BakingResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #236	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/ModelBakery$BakingResult;
/* 236 */       //   0	8	1	o	Ljava/lang/Object; } public ModelBakery.MissingModels missingModels() { return this.missingModels; } public Map<BlockState, BlockStateModel> blockStateModels() { return this.blockStateModels; } public Map<Identifier, ItemModel> itemStackModels() { return this.itemStackModels; } public Map<Identifier, ClientItem.Properties> itemProperties() { return this.itemProperties; }
/*     */      }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class PartCacheImpl
/*     */     implements ModelBaker.PartCache
/*     */   {
/* 245 */     private final Interner<Vector3fc> vectors = Interners.newStrongInterner();
/*     */ 
/*     */     
/*     */     public Vector3fc vector(Vector3fc v) {
/* 249 */       return (Vector3fc)this.vectors.intern(v);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/ModelBakery.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */