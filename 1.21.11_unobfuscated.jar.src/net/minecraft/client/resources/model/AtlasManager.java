/*     */ package net.minecraft.client.resources.model;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.client.renderer.Sheets;
/*     */ import net.minecraft.client.renderer.texture.AbstractTexture;
/*     */ import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.SpriteLoader;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlasSprite;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.metadata.gui.GuiMetadataSection;
/*     */ import net.minecraft.data.AtlasIds;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*     */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class AtlasManager implements AutoCloseable, PreparableReloadListener, MaterialSet {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  31 */   private static final List<AtlasConfig> KNOWN_ATLASES = List.of(new AtlasConfig[] { new AtlasConfig(Sheets.ARMOR_TRIMS_SHEET, AtlasIds.ARMOR_TRIMS, false), new AtlasConfig(Sheets.BANNER_SHEET, AtlasIds.BANNER_PATTERNS, false), new AtlasConfig(Sheets.BED_SHEET, AtlasIds.BEDS, false), new AtlasConfig(TextureAtlas.LOCATION_BLOCKS, AtlasIds.BLOCKS, true), new AtlasConfig(TextureAtlas.LOCATION_ITEMS, AtlasIds.ITEMS, false), new AtlasConfig(Sheets.CHEST_SHEET, AtlasIds.CHESTS, false), new AtlasConfig(Sheets.DECORATED_POT_SHEET, AtlasIds.DECORATED_POT, false), new AtlasConfig(Sheets.GUI_SHEET, AtlasIds.GUI, false, 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  39 */           Set.of(GuiMetadataSection.TYPE)), new AtlasConfig(Sheets.MAP_DECORATIONS_SHEET, AtlasIds.MAP_DECORATIONS, false), new AtlasConfig(Sheets.PAINTINGS_SHEET, AtlasIds.PAINTINGS, false), new AtlasConfig(TextureAtlas.LOCATION_PARTICLES, AtlasIds.PARTICLES, false), new AtlasConfig(Sheets.SHIELD_SHEET, AtlasIds.SHIELD_PATTERNS, false), new AtlasConfig(Sheets.SHULKER_SHEET, AtlasIds.SHULKER_BOXES, false), new AtlasConfig(Sheets.SIGN_SHEET, AtlasIds.SIGNS, false), new AtlasConfig(Sheets.CELESTIAL_SHEET, AtlasIds.CELESTIALS, false) });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  49 */   public static final PreparableReloadListener.StateKey<PendingStitchResults> PENDING_STITCH = new PreparableReloadListener.StateKey();
/*     */   
/*  51 */   private final Map<Identifier, AtlasEntry> atlasByTexture = new HashMap<>();
/*  52 */   private final Map<Identifier, AtlasEntry> atlasById = new HashMap<>();
/*  53 */   private Map<Material, TextureAtlasSprite> materialLookup = Map.of();
/*     */   private int maxMipmapLevels;
/*     */   
/*     */   public AtlasManager(TextureManager textureManager, int maxMipmapLevels) {
/*  57 */     for (AtlasConfig info : KNOWN_ATLASES) {
/*  58 */       TextureAtlas atlasTexture = new TextureAtlas(info.textureId);
/*  59 */       textureManager.register(info.textureId, (AbstractTexture)atlasTexture);
/*  60 */       AtlasEntry atlasEntry = new AtlasEntry(atlasTexture, info);
/*  61 */       this.atlasByTexture.put(info.textureId, atlasEntry);
/*  62 */       this.atlasById.put(info.definitionLocation, atlasEntry);
/*     */     } 
/*  64 */     this.maxMipmapLevels = maxMipmapLevels;
/*     */   }
/*     */   
/*     */   public TextureAtlas getAtlasOrThrow(Identifier atlasId) {
/*  68 */     AtlasEntry atlasEntry = this.atlasById.get(atlasId);
/*  69 */     if (atlasEntry == null) {
/*  70 */       throw new IllegalArgumentException("Invalid atlas id: " + String.valueOf(atlasId));
/*     */     }
/*  72 */     return atlasEntry.atlas();
/*     */   }
/*     */   
/*     */   public void forEach(BiConsumer<Identifier, TextureAtlas> output) {
/*  76 */     this.atlasById.forEach((atlasId, entry) -> output.accept(atlasId, entry.atlas));
/*     */   }
/*     */   
/*     */   public void updateMaxMipLevel(int maxMipmapLevels) {
/*  80 */     this.maxMipmapLevels = maxMipmapLevels;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  85 */     this.materialLookup = Map.of();
/*  86 */     this.atlasById.values().forEach(AtlasEntry::close);
/*  87 */     this.atlasById.clear();
/*  88 */     this.atlasByTexture.clear();
/*     */   }
/*     */ 
/*     */   
/*     */   public TextureAtlasSprite get(Material material) {
/*  93 */     TextureAtlasSprite result = this.materialLookup.get(material);
/*  94 */     if (result != null) {
/*  95 */       return result;
/*     */     }
/*     */     
/*  98 */     Identifier atlasTextureId = material.atlasLocation();
/*  99 */     AtlasEntry atlasEntry = this.atlasByTexture.get(atlasTextureId);
/* 100 */     if (atlasEntry == null) {
/* 101 */       throw new IllegalArgumentException("Invalid atlas texture id: " + String.valueOf(atlasTextureId));
/*     */     }
/* 103 */     return atlasEntry.atlas().missingSprite();
/*     */   }
/*     */ 
/*     */   
/*     */   public void prepareSharedState(PreparableReloadListener.SharedState currentReload) {
/* 108 */     int atlasCount = this.atlasById.size();
/* 109 */     List<PendingStitch> pendingStitches = new ArrayList<>(atlasCount);
/* 110 */     Map<Identifier, CompletableFuture<SpriteLoader.Preparations>> pendingStitchById = new HashMap<>(atlasCount);
/* 111 */     List<CompletableFuture<?>> readyForUploads = new ArrayList<>(atlasCount);
/*     */     
/* 113 */     this.atlasById.forEach((atlasId, atlasEntry) -> {
/*     */           CompletableFuture<SpriteLoader.Preparations> stitchingDone = new CompletableFuture<>();
/*     */           
/*     */           pendingStitchById.put(atlasId, stitchingDone);
/*     */           
/*     */           pendingStitches.add(new PendingStitch(atlasEntry, stitchingDone));
/*     */           readyForUploads.add(stitchingDone.thenCompose(SpriteLoader.Preparations::readyForUpload));
/*     */         });
/* 121 */     CompletableFuture<?> allReadyForUploads = CompletableFuture.allOf((CompletableFuture<?>[])readyForUploads.toArray(x$0 -> new CompletableFuture[x$0]));
/*     */     
/* 123 */     currentReload.set(PENDING_STITCH, new PendingStitchResults(pendingStitches, pendingStitchById, allReadyForUploads));
/*     */   }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Void> reload(PreparableReloadListener.SharedState currentReload, Executor taskExecutor, PreparableReloadListener.PreparationBarrier preparationBarrier, Executor reloadExecutor) {
/* 128 */     PendingStitchResults pendingStitches = (PendingStitchResults)currentReload.get(PENDING_STITCH);
/* 129 */     ResourceManager resourceManager = currentReload.resourceManager();
/*     */     
/* 131 */     pendingStitches.pendingStitches.forEach(pending -> taskExecutor.entry.scheduleLoad(resourceManager, resourceManager, this.maxMipmapLevels).whenComplete(()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 141 */     Objects.requireNonNull(preparationBarrier); return pendingStitches.allReadyToUpload.thenCompose(preparationBarrier::wait)
/* 142 */       .thenAcceptAsync(unused -> updateSpriteMaps(pendingStitches), reloadExecutor);
/*     */   }
/*     */   
/*     */   private void updateSpriteMaps(PendingStitchResults pendingStitches) {
/* 146 */     this.materialLookup = pendingStitches.joinAndUpload();
/* 147 */     Map<Identifier, TextureAtlasSprite> globalSpriteLookup = new HashMap<>();
/* 148 */     this.materialLookup.forEach((material, sprite) -> {
/*     */           if (!material.texture().equals(MissingTextureAtlasSprite.getLocation())) {
/*     */             TextureAtlasSprite previous = globalSpriteLookup.putIfAbsent(material.texture(), sprite);
/*     */             if (previous != null)
/*     */               LOGGER.warn("Duplicate sprite {} from atlas {}, already defined in atlas {}. This will be rejected in a future version", new Object[] { material.texture(), material.atlasLocation(), previous.atlasLocation() }); 
/*     */           } 
/*     */         });
/*     */   } private static final class PendingStitch extends Record {
/*     */     private final AtlasManager.AtlasEntry entry; private final CompletableFuture<SpriteLoader.Preparations> preparations; public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/AtlasManager$PendingStitch;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #162	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/AtlasManager$PendingStitch;
/*     */     }
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/AtlasManager$PendingStitch;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #162	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/AtlasManager$PendingStitch;
/*     */     }
/* 162 */     private PendingStitch(AtlasManager.AtlasEntry entry, CompletableFuture<SpriteLoader.Preparations> preparations) { this.entry = entry; this.preparations = preparations; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/AtlasManager$PendingStitch;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #162	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/AtlasManager$PendingStitch;
/* 162 */       //   0	8	1	o	Ljava/lang/Object; } public AtlasManager.AtlasEntry entry() { return this.entry; } public CompletableFuture<SpriteLoader.Preparations> preparations() { return this.preparations; }
/*     */ 
/*     */ 
/*     */     
/*     */     public void joinAndUpload(Map<Material, TextureAtlasSprite> result) {
/* 167 */       SpriteLoader.Preparations preparations = this.preparations.join();
/* 168 */       this.entry.atlas.upload(preparations);
/* 169 */       preparations.regions().forEach((spriteId, spriteContents) -> result.put(new Material(this.entry.config.textureId, result), spriteContents));
/*     */     } }
/*     */   private static final class AtlasEntry extends Record implements AutoCloseable { private final TextureAtlas atlas; private final AtlasManager.AtlasConfig config;
/*     */     
/* 173 */     private AtlasEntry(TextureAtlas atlas, AtlasManager.AtlasConfig config) { this.atlas = atlas; this.config = config; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/AtlasManager$AtlasEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #173	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/AtlasManager$AtlasEntry; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/AtlasManager$AtlasEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #173	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/AtlasManager$AtlasEntry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/AtlasManager$AtlasEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #173	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/AtlasManager$AtlasEntry;
/* 173 */       //   0	8	1	o	Ljava/lang/Object; } public TextureAtlas atlas() { return this.atlas; } public AtlasManager.AtlasConfig config() { return this.config; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void close() {
/* 179 */       this.atlas.clearTextureData();
/*     */     }
/*     */     
/*     */     private CompletableFuture<SpriteLoader.Preparations> scheduleLoad(ResourceManager resourceManager, Executor executor, int maxMipmapLevels) {
/* 183 */       return SpriteLoader.create(this.atlas)
/* 184 */         .loadAndStitch(resourceManager, this.config.definitionLocation, this.config.createMipmaps ? maxMipmapLevels : 0, executor, this.config.additionalMetadata);
/*     */     } }
/*     */   public static final class AtlasConfig extends Record { private final Identifier textureId; private final Identifier definitionLocation; private final boolean createMipmaps; private final Set<MetadataSectionType<?>> additionalMetadata;
/*     */     
/* 188 */     public Set<MetadataSectionType<?>> additionalMetadata() { return this.additionalMetadata; } public boolean createMipmaps() { return this.createMipmaps; } public Identifier definitionLocation() { return this.definitionLocation; } public Identifier textureId() { return this.textureId; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/model/AtlasManager$AtlasConfig;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #188	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/resources/model/AtlasManager$AtlasConfig;
/*     */       //   0	8	1	o	Ljava/lang/Object; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/model/AtlasManager$AtlasConfig;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #188	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/resources/model/AtlasManager$AtlasConfig; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/model/AtlasManager$AtlasConfig;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #188	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 188 */       //   0	7	0	this	Lnet/minecraft/client/resources/model/AtlasManager$AtlasConfig; } public AtlasConfig(Identifier textureId, Identifier definitionLocation, boolean createMipmaps, Set<MetadataSectionType<?>> additionalMetadata) { this.textureId = textureId; this.definitionLocation = definitionLocation; this.createMipmaps = createMipmaps; this.additionalMetadata = additionalMetadata; }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public AtlasConfig(Identifier textureId, Identifier definitionLocation, boolean createMipmaps) {
/* 195 */       this(textureId, definitionLocation, createMipmaps, Set.of());
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class PendingStitchResults {
/*     */     private final List<AtlasManager.PendingStitch> pendingStitches;
/*     */     private final Map<Identifier, CompletableFuture<SpriteLoader.Preparations>> stitchFuturesById;
/*     */     private final CompletableFuture<?> allReadyToUpload;
/*     */     
/*     */     private PendingStitchResults(List<AtlasManager.PendingStitch> pendingStitches, Map<Identifier, CompletableFuture<SpriteLoader.Preparations>> stitchFuturesById, CompletableFuture<?> allReadyToUpload) {
/* 205 */       this.pendingStitches = pendingStitches;
/* 206 */       this.stitchFuturesById = stitchFuturesById;
/* 207 */       this.allReadyToUpload = allReadyToUpload;
/*     */     }
/*     */     
/*     */     public Map<Material, TextureAtlasSprite> joinAndUpload() {
/* 211 */       Map<Material, TextureAtlasSprite> result = new HashMap<>();
/* 212 */       this.pendingStitches.forEach(pendingStitch -> pendingStitch.joinAndUpload(result));
/* 213 */       return result;
/*     */     }
/*     */     
/*     */     public CompletableFuture<SpriteLoader.Preparations> get(Identifier atlasId) {
/* 217 */       return Objects.<CompletableFuture<SpriteLoader.Preparations>>requireNonNull(this.stitchFuturesById.get(atlasId));
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/AtlasManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */