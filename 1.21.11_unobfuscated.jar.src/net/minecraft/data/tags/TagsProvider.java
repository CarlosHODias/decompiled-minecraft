/*     */ package net.minecraft.data.tags;
/*     */ import com.google.common.collect.Maps;
/*     */ import java.nio.file.Path;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.CompletableFuture;
/*     */ import java.util.concurrent.CompletionStage;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.data.CachedOutput;
/*     */ import net.minecraft.data.DataProvider;
/*     */ import net.minecraft.data.PackOutput;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.tags.TagBuilder;
/*     */ import net.minecraft.tags.TagEntry;
/*     */ import net.minecraft.tags.TagFile;
/*     */ import net.minecraft.tags.TagKey;
/*     */ import net.minecraft.util.Util;
/*     */ 
/*     */ public abstract class TagsProvider<T> implements DataProvider {
/*     */   protected final PackOutput.PathProvider pathProvider;
/*     */   private final CompletableFuture<HolderLookup.Provider> lookupProvider;
/*  31 */   private final CompletableFuture<Void> contentsDone = new CompletableFuture<>();
/*     */   
/*     */   private final CompletableFuture<TagLookup<T>> parentProvider;
/*     */   protected final ResourceKey<? extends Registry<T>> registryKey;
/*  35 */   private final Map<Identifier, TagBuilder> builders = Maps.newLinkedHashMap();
/*     */   
/*     */   protected TagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider) {
/*  38 */     this(output, registryKey, lookupProvider, CompletableFuture.completedFuture(TagLookup.empty()));
/*     */   }
/*     */   
/*     */   protected TagsProvider(PackOutput output, ResourceKey<? extends Registry<T>> registryKey, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<T>> parentProvider) {
/*  42 */     this.pathProvider = output.createRegistryTagsPathProvider(registryKey);
/*  43 */     this.registryKey = registryKey;
/*     */     
/*  45 */     this.parentProvider = parentProvider;
/*  46 */     this.lookupProvider = lookupProvider;
/*     */   }
/*     */ 
/*     */   
/*     */   public final String getName() {
/*  51 */     return "Tags for " + String.valueOf(this.registryKey.identifier());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CompletableFuture<?> run(CachedOutput cache) {
/*  60 */     return createContentsProvider()
/*  61 */       .thenApply(provider -> {
/*     */           this.contentsDone.complete(null);
/*     */           
/*     */           return provider;
/*  65 */         }).thenCombineAsync(this.parentProvider, (x$0, x$1) -> new CombinedData(x$0, x$1), (Executor)Util.backgroundExecutor())
/*  66 */       .thenCompose(c -> {
/*     */           static final class CombinedData<T> extends Record {
/*     */             private final HolderLookup.Provider contents; private final TagsProvider.TagLookup<T> parent; CombinedData(HolderLookup.Provider contents, TagsProvider.TagLookup<T> parent) {
/*     */               this.contents = contents;
/*     */               this.parent = parent;
/*     */             } public final String toString() {
/*     */               // Byte code:
/*     */               //   0: aload_0
/*     */               //   1: <illegal opcode> toString : (Lnet/minecraft/data/tags/TagsProvider$1CombinedData;)Ljava/lang/String;
/*     */               //   6: areturn
/*     */               // Line number table:
/*     */               //   Java source line number -> byte code offset
/*     */               //   #58	-> 0
/*     */               // Local variable table:
/*     */               //   start	length	slot	name	descriptor
/*     */               //   0	7	0	this	Lnet/minecraft/data/tags/TagsProvider$1CombinedData;
/*     */               // Local variable type table:
/*     */               //   start	length	slot	name	signature
/*     */               //   0	7	0	this	Lnet/minecraft/data/tags/TagsProvider$1CombinedData<TT;>;
/*     */             } public final int hashCode() {
/*     */               // Byte code:
/*     */               //   0: aload_0
/*     */               //   1: <illegal opcode> hashCode : (Lnet/minecraft/data/tags/TagsProvider$1CombinedData;)I
/*     */               //   6: ireturn
/*     */               // Line number table:
/*     */               //   Java source line number -> byte code offset
/*     */               //   #58	-> 0
/*     */               // Local variable table:
/*     */               //   start	length	slot	name	descriptor
/*     */               //   0	7	0	this	Lnet/minecraft/data/tags/TagsProvider$1CombinedData;
/*     */               // Local variable type table:
/*     */               //   start	length	slot	name	signature
/*     */               //   0	7	0	this	Lnet/minecraft/data/tags/TagsProvider$1CombinedData<TT;>;
/*     */             } public final boolean equals(Object o) {
/*     */               // Byte code:
/*     */               //   0: aload_0
/*     */               //   1: aload_1
/*     */               //   2: <illegal opcode> equals : (Lnet/minecraft/data/tags/TagsProvider$1CombinedData;Ljava/lang/Object;)Z
/*     */               //   7: ireturn
/*     */               // Line number table:
/*     */               //   Java source line number -> byte code offset
/*     */               //   #58	-> 0
/*     */               // Local variable table:
/*     */               //   start	length	slot	name	descriptor
/*     */               //   0	8	0	this	Lnet/minecraft/data/tags/TagsProvider$1CombinedData;
/*     */               //   0	8	1	o	Ljava/lang/Object;
/*     */               // Local variable type table:
/*     */               //   start	length	slot	name	signature
/*     */               //   0	8	0	this	Lnet/minecraft/data/tags/TagsProvider$1CombinedData<TT;>;
/*     */             }
/*     */             public HolderLookup.Provider contents() {
/*     */               return this.contents;
/*     */             }
/*     */             public TagsProvider.TagLookup<T> parent() {
/*     */               return this.parent;
/*     */             } };
/*     */           HolderLookup.RegistryLookup<T> lookup = cache.contents.lookupOrThrow(this.registryKey);
/*     */           Predicate<Identifier> elementCheck = (), tagCheck = ();
/*     */           return CompletableFuture.allOf((CompletableFuture<?>[])this.builders.entrySet().stream().map(()).toArray(()));
/*     */         });
/*     */   }
/*     */   protected TagBuilder getOrCreateRawBuilder(TagKey<T> tag) {
/*  90 */     return this.builders.computeIfAbsent(tag.location(), k -> TagBuilder.create());
/*     */   }
/*     */   
/*     */   public CompletableFuture<TagLookup<T>> contentsGetter() {
/*  94 */     return this.contentsDone.thenApply(ignore -> ());
/*     */   }
/*     */   
/*     */   protected CompletableFuture<HolderLookup.Provider> createContentsProvider() {
/*  98 */     return this.lookupProvider.thenApply(registries -> {
/*     */           this.builders.clear();
/*     */           addTags(registries);
/*     */           return registries;
/*     */         });
/*     */   }
/*     */   protected abstract void addTags(HolderLookup.Provider paramProvider);
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface TagLookup<T> extends Function<TagKey<T>, Optional<TagBuilder>> { static <T> TagLookup<T> empty() {
/* 108 */       return id -> Optional.empty();
/*     */     }
/*     */     
/*     */     default boolean contains(TagKey<T> key) {
/* 112 */       return apply(key).isPresent();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/data/tags/TagsProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */