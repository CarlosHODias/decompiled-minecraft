/*     */ package net.minecraft.client.gui.font;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.gson.Gson;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.blaze3d.font.GlyphProvider;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.datafixers.util.Pair;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.ints.IntCollection;
/*     */ import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GlyphSource;
/*     */ import net.minecraft.client.gui.font.glyphs.EffectGlyph;
/*     */ import net.minecraft.client.gui.font.providers.GlyphProviderDefinition;
/*     */ import net.minecraft.client.renderer.PlayerSkinRenderCache;
/*     */ import net.minecraft.client.renderer.texture.TextureAtlas;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.client.resources.model.AtlasManager;
/*     */ import net.minecraft.network.chat.FontDescription;
/*     */ import net.minecraft.resources.FileToIdConverter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.PreparableReloadListener;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.util.DependencySorter;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.util.profiling.Profiler;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FontManager implements AutoCloseable, PreparableReloadListener {
/*  55 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   private static final String FONTS_PATH = "fonts.json";
/*  57 */   public static final Identifier MISSING_FONT = Identifier.withDefaultNamespace("missing");
/*  58 */   private static final FileToIdConverter FONT_DEFINITIONS = FileToIdConverter.json("font");
/*     */   
/*  60 */   private static final Gson GSON = new com.google.gson.GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
/*     */   
/*     */   private final FontSet missingFontSet;
/*  63 */   private final List<GlyphProvider> providersToClose = new ArrayList<>();
/*  64 */   private final Map<Identifier, FontSet> fontSets = new HashMap<>();
/*     */   private final TextureManager textureManager;
/*  66 */   private final CachedFontProvider anyGlyphs = new CachedFontProvider(false);
/*  67 */   private final CachedFontProvider nonFishyGlyphs = new CachedFontProvider(true);
/*     */   
/*     */   private final AtlasManager atlasManager;
/*  70 */   private final Map<Identifier, AtlasGlyphProvider> atlasProviders = new HashMap<>();
/*     */   private final PlayerGlyphProvider playerProvider;
/*     */   
/*     */   public FontManager(TextureManager textureManager, AtlasManager atlasManager, PlayerSkinRenderCache playerSkinRenderCache) {
/*  74 */     this.textureManager = textureManager;
/*  75 */     this.atlasManager = atlasManager;
/*  76 */     this.missingFontSet = createFontSet(MISSING_FONT, List.of(createFallbackProvider()), Set.of());
/*  77 */     this.playerProvider = new PlayerGlyphProvider(playerSkinRenderCache);
/*     */   }
/*     */   
/*     */   private FontSet createFontSet(Identifier id, List<GlyphProvider.Conditional> providers, Set<FontOption> options) {
/*  81 */     GlyphStitcher stitcher = new GlyphStitcher(this.textureManager, id);
/*  82 */     FontSet result = new FontSet(stitcher);
/*  83 */     result.reload(providers, options);
/*  84 */     return result;
/*     */   }
/*     */   
/*     */   private static GlyphProvider.Conditional createFallbackProvider() {
/*  88 */     return new GlyphProvider.Conditional(new AllMissingGlyphProvider(), FontOption.Filter.ALWAYS_PASS);
/*     */   }
/*     */   private static final class BuilderId extends Record { private final Identifier fontId; private final String pack; private final int index;
/*  91 */     private BuilderId(Identifier fontId, String pack, int index) { this.fontId = fontId; this.pack = pack; this.index = index; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/FontManager$BuilderId;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #91	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$BuilderId; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/FontManager$BuilderId;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #91	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/FontManager$BuilderId;
/*  91 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier fontId() { return this.fontId; } public String pack() { return this.pack; } public int index() { return this.index; }
/*     */     
/*     */     public String toString() {
/*  94 */       return "(" + String.valueOf(this.fontId) + ": builder #" + this.index + " from pack " + this.pack + ")";
/*     */     } }
/*     */   private static final class BuilderResult extends Record { private final FontManager.BuilderId id; private final FontOption.Filter filter; private final Either<CompletableFuture<Optional<GlyphProvider>>, Identifier> result;
/*     */     
/*  98 */     private BuilderResult(FontManager.BuilderId id, FontOption.Filter filter, Either<CompletableFuture<Optional<GlyphProvider>>, Identifier> result) { this.id = id; this.filter = filter; this.result = result; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/FontManager$BuilderResult;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #98	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  98 */       //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$BuilderResult; } public FontManager.BuilderId id() { return this.id; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/FontManager$BuilderResult;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #98	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$BuilderResult; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/FontManager$BuilderResult;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #98	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/FontManager$BuilderResult;
/*  98 */       //   0	8	1	o	Ljava/lang/Object; } public FontOption.Filter filter() { return this.filter; } public Either<CompletableFuture<Optional<GlyphProvider>>, Identifier> result() { return this.result; }
/*     */      public Optional<List<GlyphProvider.Conditional>> resolve(Function<Identifier, List<GlyphProvider.Conditional>> resolver) {
/* 100 */       return (Optional<List<GlyphProvider.Conditional>>)this.result.map(provider -> ((Optional)provider.join()).map(()), reference -> {
/*     */             List<GlyphProvider.Conditional> resolvedReferences = resolver.apply(resolver);
/*     */             if (resolvedReferences == null) {
/*     */               FontManager.LOGGER.warn("Can't find font {} referenced by builder {}, either because it's missing, failed to load or is part of loading cycle", resolver, this.id);
/*     */               return Optional.empty();
/*     */             } 
/*     */             return Optional.of(resolvedReferences.stream().map(this::mergeFilters).toList());
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private GlyphProvider.Conditional mergeFilters(GlyphProvider.Conditional original) {
/* 115 */       return new GlyphProvider.Conditional(original.provider(), this.filter.merge(original.filter()));
/*     */     } }
/*     */   private static final class UnresolvedBuilderBundle extends Record implements DependencySorter.Entry<Identifier> { private final Identifier fontId; private final List<FontManager.BuilderResult> builders; private final Set<Identifier> dependencies;
/*     */     
/* 119 */     private UnresolvedBuilderBundle(Identifier fontId, List<FontManager.BuilderResult> builders, Set<Identifier> dependencies) { this.fontId = fontId; this.builders = builders; this.dependencies = dependencies; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/FontManager$UnresolvedBuilderBundle;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #119	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$UnresolvedBuilderBundle; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/FontManager$UnresolvedBuilderBundle;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #119	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$UnresolvedBuilderBundle; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/FontManager$UnresolvedBuilderBundle;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #119	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/FontManager$UnresolvedBuilderBundle;
/* 119 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier fontId() { return this.fontId; } public List<FontManager.BuilderResult> builders() { return this.builders; } public Set<Identifier> dependencies() { return this.dependencies; }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public UnresolvedBuilderBundle(Identifier fontId) {
/* 125 */       this(fontId, new ArrayList<>(), new HashSet<>());
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void add(FontManager.BuilderId builderId, FontOption.Filter filter, GlyphProviderDefinition.Reference reference) {
/* 133 */       this.builders.add(new FontManager.BuilderResult(builderId, filter, Either.right(reference.id())));
/* 134 */       this.dependencies.add(reference.id());
/*     */     }
/*     */     
/*     */     public void add(FontManager.BuilderId builderId, FontOption.Filter filter, CompletableFuture<Optional<GlyphProvider>> provider) {
/* 138 */       this.builders.add(new FontManager.BuilderResult(builderId, filter, Either.left(provider)));
/*     */     }
/*     */     
/*     */     private Stream<CompletableFuture<Optional<GlyphProvider>>> listBuilders() {
/* 142 */       return this.builders.stream().flatMap(e -> e.result.left().stream());
/*     */     }
/*     */     
/*     */     public Optional<List<GlyphProvider.Conditional>> resolve(Function<Identifier, List<GlyphProvider.Conditional>> resolver) {
/* 146 */       List<GlyphProvider.Conditional> resolved = new ArrayList<>();
/* 147 */       for (FontManager.BuilderResult builder : this.builders) {
/* 148 */         Optional<List<GlyphProvider.Conditional>> resolvedBuilder = builder.resolve(resolver);
/* 149 */         if (resolvedBuilder.isPresent()) {
/* 150 */           resolved.addAll(resolvedBuilder.get()); continue;
/*     */         } 
/* 152 */         return Optional.empty();
/*     */       } 
/*     */       
/* 155 */       return Optional.of(resolved);
/*     */     }
/*     */ 
/*     */     
/*     */     public void visitRequiredDependencies(Consumer<Identifier> output) {
/* 160 */       this.dependencies.forEach(output);
/*     */     }
/*     */     
/*     */     public void visitOptionalDependencies(Consumer<Identifier> output) {} }
/*     */ 
/*     */   
/*     */   private static final class Preparation extends Record { private final Map<Identifier, List<GlyphProvider.Conditional>> fontSets;
/*     */     private final List<GlyphProvider> allProviders;
/*     */     
/* 169 */     private Preparation(Map<Identifier, List<GlyphProvider.Conditional>> fontSets, List<GlyphProvider> allProviders) { this.fontSets = fontSets; this.allProviders = allProviders; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/FontManager$Preparation;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #169	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$Preparation; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/FontManager$Preparation;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #169	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$Preparation; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/FontManager$Preparation;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #169	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/FontManager$Preparation;
/* 169 */       //   0	8	1	o	Ljava/lang/Object; } public Map<Identifier, List<GlyphProvider.Conditional>> fontSets() { return this.fontSets; } public List<GlyphProvider> allProviders() { return this.allProviders; }
/*     */      }
/*     */ 
/*     */   
/*     */   public CompletableFuture<Void> reload(PreparableReloadListener.SharedState currentReload, Executor taskExecutor, PreparableReloadListener.PreparationBarrier preparationBarrier, Executor reloadExecutor) {
/* 174 */     Objects.requireNonNull(preparationBarrier); return prepare(currentReload.resourceManager(), taskExecutor).thenCompose(preparationBarrier::wait)
/* 175 */       .thenAcceptAsync(preparations -> apply(preparations, Profiler.get()), reloadExecutor);
/*     */   }
/*     */   
/*     */   private CompletableFuture<Preparation> prepare(ResourceManager manager, Executor executor) {
/* 179 */     List<CompletableFuture<UnresolvedBuilderBundle>> builderFutures = new ArrayList<>();
/*     */     
/* 181 */     for (Map.Entry<Identifier, List<Resource>> fontStack : (Iterable<Map.Entry<Identifier, List<Resource>>>)FONT_DEFINITIONS.listMatchingResourceStacks(manager).entrySet()) {
/* 182 */       Identifier fontName = FONT_DEFINITIONS.fileToId(fontStack.getKey());
/* 183 */       builderFutures.add(CompletableFuture.supplyAsync(() -> { List<Pair<BuilderId, GlyphProviderDefinition.Conditional>> builderStack = loadResourceStack((List<Resource>)fontStack.getValue(), fontName); UnresolvedBuilderBundle bundle = new UnresolvedBuilderBundle(fontName); for (Pair<BuilderId, GlyphProviderDefinition.Conditional> stackEntry : builderStack) { BuilderId id = (BuilderId)stackEntry.getFirst(); FontOption.Filter options = ((GlyphProviderDefinition.Conditional)stackEntry.getSecond()).filter(); ((GlyphProviderDefinition.Conditional)stackEntry.getSecond()).definition().unpack().ifLeft(()).ifRight(()); }  return bundle; }, executor));
/*     */     } 
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
/* 201 */     return Util.sequence(builderFutures).thenCompose(builders -> {
/*     */           List<CompletableFuture<Optional<GlyphProvider>>> allProviderFutures = (List<CompletableFuture<Optional<GlyphProvider>>>)executor.stream().flatMap(UnresolvedBuilderBundle::listBuilders).collect(Util.toMutableList());
/*     */           GlyphProvider.Conditional fallback = createFallbackProvider();
/*     */           allProviderFutures.add(CompletableFuture.completedFuture(Optional.of(fallback.provider())));
/*     */           return Util.sequence(allProviderFutures).thenCompose(());
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
/*     */ 
/*     */   
/*     */   private CompletableFuture<Optional<GlyphProvider>> safeLoad(BuilderId id, GlyphProviderDefinition.Loader provider, ResourceManager manager, Executor executor) {
/* 219 */     return CompletableFuture.supplyAsync(() -> {
/*     */           try {
/*     */             return Optional.of(provider.load(manager));
/* 222 */           } catch (Exception e) {
/*     */             LOGGER.warn("Failed to load builder {}, rejecting", id, e);
/*     */             return Optional.empty();
/*     */           } 
/*     */         }, executor);
/*     */   }
/*     */   
/*     */   private Map<Identifier, List<GlyphProvider.Conditional>> resolveProviders(List<UnresolvedBuilderBundle> unresolvedProviders) {
/* 230 */     Map<Identifier, List<GlyphProvider.Conditional>> result = new HashMap<>();
/*     */     
/* 232 */     DependencySorter<Identifier, UnresolvedBuilderBundle> sorter = new DependencySorter();
/* 233 */     unresolvedProviders.forEach(e -> sorter.addEntry(e.fontId, e));
/*     */     
/* 235 */     sorter.orderByDependencies((id, bundle) -> { Objects.requireNonNull(result); bundle.resolve(result::get).ifPresent(());
/* 236 */         }); return result;
/*     */   }
/*     */ 
/*     */   
/*     */   private void finalizeProviderLoading(List<GlyphProvider.Conditional> list, GlyphProvider.Conditional fallback) {
/* 241 */     list.add(0, fallback);
/*     */     
/* 243 */     IntOpenHashSet intOpenHashSet = new IntOpenHashSet();
/* 244 */     for (GlyphProvider.Conditional provider : list) {
/* 245 */       intOpenHashSet.addAll((IntCollection)provider.provider().getSupportedGlyphs());
/*     */     }
/*     */ 
/*     */     
/* 249 */     intOpenHashSet.forEach(codepoint -> {
/*     */           if (codepoint == 32) {
/*     */             return;
/*     */           }
/*     */           for (GlyphProvider.Conditional provider : (Iterable<GlyphProvider.Conditional>)Lists.reverse(list)) {
/*     */             if (provider.provider().getGlyph(codepoint) != null) {
/*     */               break;
/*     */             }
/*     */           } 
/*     */         });
/*     */   }
/*     */   
/*     */   private static Set<FontOption> getFontOptions(Options options) {
/* 262 */     Set<FontOption> result = EnumSet.noneOf(FontOption.class);
/*     */     
/* 264 */     if ((Boolean)options.forceUnicodeFont().get()) {
/* 265 */       result.add(FontOption.UNIFORM);
/*     */     }
/*     */     
/* 268 */     if ((Boolean)options.japaneseGlyphVariants().get()) {
/* 269 */       result.add(FontOption.JAPANESE_VARIANTS);
/*     */     }
/*     */     
/* 272 */     return result;
/*     */   }
/*     */   
/*     */   private void apply(Preparation preparations, ProfilerFiller profiler) {
/* 276 */     profiler.push("closing");
/*     */     
/* 278 */     this.anyGlyphs.invalidate();
/* 279 */     this.nonFishyGlyphs.invalidate();
/*     */     
/* 281 */     this.fontSets.values().forEach(FontSet::close);
/* 282 */     this.fontSets.clear();
/*     */     
/* 284 */     this.providersToClose.forEach(GlyphProvider::close);
/* 285 */     this.providersToClose.clear();
/*     */     
/* 287 */     Set<FontOption> fontOptions = getFontOptions((Minecraft.getInstance()).options);
/*     */     
/* 289 */     profiler.popPush("reloading");
/* 290 */     preparations.fontSets().forEach((id, newProviders) -> this.fontSets.put(fontOptions, createFontSet(fontOptions, Lists.reverse(newProviders), fontOptions)));
/*     */ 
/*     */ 
/*     */     
/* 294 */     this.providersToClose.addAll(preparations.allProviders);
/*     */     
/* 296 */     profiler.pop();
/*     */     
/* 298 */     if (!this.fontSets.containsKey(Minecraft.DEFAULT_FONT))
/*     */     {
/* 300 */       throw new IllegalStateException("Default font failed to load");
/*     */     }
/*     */     
/* 303 */     this.atlasProviders.clear();
/* 304 */     this.atlasManager.forEach((atlasId, atlasTexture) -> this.atlasProviders.put(atlasId, new AtlasGlyphProvider(atlasTexture)));
/*     */   }
/*     */   
/*     */   public void updateOptions(Options options) {
/* 308 */     Set<FontOption> fontOptions = getFontOptions(options);
/* 309 */     for (FontSet value : this.fontSets.values()) {
/* 310 */       value.reload(fontOptions);
/*     */     }
/*     */   }
/*     */   
/*     */   private static List<Pair<BuilderId, GlyphProviderDefinition.Conditional>> loadResourceStack(List<Resource> resourceStack, Identifier fontName) {
/* 315 */     List<Pair<BuilderId, GlyphProviderDefinition.Conditional>> builderStack = new ArrayList<>();
/*     */     
/* 317 */     for (Resource resource : resourceStack) { 
/* 318 */       try { Reader reader = resource.openAsReader(); 
/* 319 */         try { JsonElement jsonContents = (JsonElement)GSON.fromJson(reader, JsonElement.class);
/* 320 */           FontDefinitionFile definition = (FontDefinitionFile)FontDefinitionFile.CODEC.parse((com.mojang.serialization.DynamicOps)JsonOps.INSTANCE, jsonContents).getOrThrow(com.google.gson.JsonParseException::new);
/* 321 */           List<GlyphProviderDefinition.Conditional> providers = definition.providers;
/*     */           
/* 323 */           for (int i = providers.size() - 1; i >= 0; i--) {
/* 324 */             BuilderId id = new BuilderId(fontName, resource.sourcePackId(), i);
/* 325 */             builderStack.add(Pair.of(id, providers.get(i)));
/*     */           } 
/* 327 */           if (reader != null) reader.close();  } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (Exception e)
/* 328 */       { LOGGER.warn("Unable to load font '{}' in {} in resourcepack: '{}'", new Object[] { fontName, "fonts.json", resource.sourcePackId(), e }); }
/*     */        }
/*     */     
/* 331 */     return builderStack;
/*     */   }
/*     */   
/*     */   public Font createFont() {
/* 335 */     return new Font(this.anyGlyphs);
/*     */   }
/*     */   
/*     */   public Font createFontFilterFishy() {
/* 339 */     return new Font(this.nonFishyGlyphs);
/*     */   }
/*     */   
/*     */   private FontSet getFontSetRaw(Identifier id) {
/* 343 */     return this.fontSets.getOrDefault(id, this.missingFontSet);
/*     */   }
/*     */   
/*     */   private GlyphSource getSpriteFont(FontDescription.AtlasSprite contents) {
/* 347 */     AtlasGlyphProvider provider = this.atlasProviders.get(contents.atlasId());
/* 348 */     if (provider == null) {
/* 349 */       return this.missingFontSet.source(false);
/*     */     }
/* 351 */     return provider.sourceForSprite(contents.spriteId());
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 356 */     this.anyGlyphs.close();
/* 357 */     this.nonFishyGlyphs.close();
/* 358 */     this.fontSets.values().forEach(FontSet::close);
/* 359 */     this.providersToClose.forEach(GlyphProvider::close);
/* 360 */     this.missingFontSet.close();
/*     */   }
/*     */   private static final class FontDefinitionFile extends Record { private final List<GlyphProviderDefinition.Conditional> providers; public static final Codec<FontDefinitionFile> CODEC;
/* 363 */     private FontDefinitionFile(List<GlyphProviderDefinition.Conditional> providers) { this.providers = providers; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/FontManager$FontDefinitionFile;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #363	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$FontDefinitionFile; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/FontManager$FontDefinitionFile;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #363	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$FontDefinitionFile; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/FontManager$FontDefinitionFile;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #363	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/FontManager$FontDefinitionFile;
/* 363 */       //   0	8	1	o	Ljava/lang/Object; } public List<GlyphProviderDefinition.Conditional> providers() { return this.providers; } static {
/* 364 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)GlyphProviderDefinition.Conditional.CODEC.listOf().fieldOf("providers").forGetter(FontDefinitionFile::providers)).apply((Applicative)i, FontDefinitionFile::new));
/*     */     } }
/*     */   private class CachedFontProvider implements Font.Provider, AutoCloseable { private final boolean nonFishyOnly; private volatile CachedEntry lastEntry;
/*     */     private volatile EffectGlyph whiteGlyph;
/*     */     
/*     */     private static final class CachedEntry extends Record { private final FontDescription description;
/*     */       private final GlyphSource source;
/*     */       
/* 372 */       private CachedEntry(FontDescription description, GlyphSource source) { this.description = description; this.source = source; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #372	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #372	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #372	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;
/* 372 */         //   0	8	1	o	Ljava/lang/Object; } public FontDescription description() { return this.description; } public GlyphSource source() { return this.source; }
/*     */        }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private CachedFontProvider(boolean nonFishyOnly) {
/* 383 */       this.nonFishyOnly = nonFishyOnly;
/*     */     }
/*     */     
/*     */     public void invalidate() {
/* 387 */       this.lastEntry = null;
/* 388 */       this.whiteGlyph = null;
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() {
/* 393 */       invalidate();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private GlyphSource getGlyphSource(FontDescription description) {
/*     */       // Byte code:
/*     */       //   0: aload_1
/*     */       //   1: dup
/*     */       //   2: invokestatic requireNonNull : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   5: pop
/*     */       //   6: astore_2
/*     */       //   7: iconst_0
/*     */       //   8: istore_3
/*     */       //   9: aload_2
/*     */       //   10: iload_3
/*     */       //   11: <illegal opcode> typeSwitch : (Ljava/lang/Object;I)I
/*     */       //   16: tableswitch default -> 111, 0 -> 44, 1 -> 72, 2 -> 90
/*     */       //   44: aload_2
/*     */       //   45: checkcast net/minecraft/network/chat/FontDescription$Resource
/*     */       //   48: astore #4
/*     */       //   50: aload_0
/*     */       //   51: getfield this$0 : Lnet/minecraft/client/gui/font/FontManager;
/*     */       //   54: aload #4
/*     */       //   56: invokevirtual id : ()Lnet/minecraft/resources/Identifier;
/*     */       //   59: invokevirtual getFontSetRaw : (Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/gui/font/FontSet;
/*     */       //   62: aload_0
/*     */       //   63: getfield nonFishyOnly : Z
/*     */       //   66: invokevirtual source : (Z)Lnet/minecraft/client/gui/GlyphSource;
/*     */       //   69: goto -> 125
/*     */       //   72: aload_2
/*     */       //   73: checkcast net/minecraft/network/chat/FontDescription$AtlasSprite
/*     */       //   76: astore #5
/*     */       //   78: aload_0
/*     */       //   79: getfield this$0 : Lnet/minecraft/client/gui/font/FontManager;
/*     */       //   82: aload #5
/*     */       //   84: invokevirtual getSpriteFont : (Lnet/minecraft/network/chat/FontDescription$AtlasSprite;)Lnet/minecraft/client/gui/GlyphSource;
/*     */       //   87: goto -> 125
/*     */       //   90: aload_2
/*     */       //   91: checkcast net/minecraft/network/chat/FontDescription$PlayerSprite
/*     */       //   94: astore #6
/*     */       //   96: aload_0
/*     */       //   97: getfield this$0 : Lnet/minecraft/client/gui/font/FontManager;
/*     */       //   100: getfield playerProvider : Lnet/minecraft/client/gui/font/PlayerGlyphProvider;
/*     */       //   103: aload #6
/*     */       //   105: invokevirtual sourceForPlayer : (Lnet/minecraft/network/chat/FontDescription$PlayerSprite;)Lnet/minecraft/client/gui/GlyphSource;
/*     */       //   108: goto -> 125
/*     */       //   111: aload_0
/*     */       //   112: getfield this$0 : Lnet/minecraft/client/gui/font/FontManager;
/*     */       //   115: getfield missingFontSet : Lnet/minecraft/client/gui/font/FontSet;
/*     */       //   118: aload_0
/*     */       //   119: getfield nonFishyOnly : Z
/*     */       //   122: invokevirtual source : (Z)Lnet/minecraft/client/gui/GlyphSource;
/*     */       //   125: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #397	-> 0
/*     */       //   #398	-> 44
/*     */       //   #399	-> 72
/*     */       //   #400	-> 90
/*     */       //   #401	-> 111
/*     */       //   #397	-> 125
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   50	22	4	resource	Lnet/minecraft/network/chat/FontDescription$Resource;
/*     */       //   78	12	5	sprite	Lnet/minecraft/network/chat/FontDescription$AtlasSprite;
/*     */       //   96	15	6	player	Lnet/minecraft/network/chat/FontDescription$PlayerSprite;
/*     */       //   0	126	0	this	Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider;
/*     */       //   0	126	1	description	Lnet/minecraft/network/chat/FontDescription;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public GlyphSource glyphs(FontDescription description) {
/* 407 */       CachedEntry lastEntry = this.lastEntry;
/* 408 */       if (lastEntry != null && description.equals(lastEntry.description)) {
/* 409 */         return lastEntry.source;
/*     */       }
/* 411 */       GlyphSource result = getGlyphSource(description);
/*     */       
/* 413 */       this.lastEntry = new CachedEntry(description, result);
/* 414 */       return result;
/*     */     }
/*     */ 
/*     */     
/*     */     public EffectGlyph effect() {
/* 419 */       EffectGlyph whiteGlyph = this.whiteGlyph;
/* 420 */       if (whiteGlyph == null) {
/* 421 */         whiteGlyph = FontManager.this.getFontSetRaw(FontDescription.DEFAULT.id()).whiteGlyph();
/* 422 */         this.whiteGlyph = whiteGlyph;
/*     */       } 
/* 424 */       return whiteGlyph;
/*     */     } }
/*     */ 
/*     */   
/*     */   private static final class CachedEntry extends Record {
/*     */     private final FontDescription description;
/*     */     private final GlyphSource source;
/*     */     
/*     */     private CachedEntry(FontDescription description, GlyphSource source) {
/*     */       this.description = description;
/*     */       this.source = source;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #372	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #372	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #372	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/font/FontManager$CachedFontProvider$CachedEntry;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public FontDescription description() {
/*     */       return this.description;
/*     */     }
/*     */     
/*     */     public GlyphSource source() {
/*     */       return this.source;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/FontManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */