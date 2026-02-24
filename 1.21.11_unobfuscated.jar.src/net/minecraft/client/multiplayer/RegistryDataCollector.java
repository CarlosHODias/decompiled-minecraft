/*     */ package net.minecraft.client.multiplayer;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.core.HolderLookup;
/*     */ import net.minecraft.core.LayeredRegistryAccess;
/*     */ import net.minecraft.core.Registry;
/*     */ import net.minecraft.core.RegistryAccess;
/*     */ import net.minecraft.core.RegistrySynchronization;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.resources.RegistryDataLoader;
/*     */ import net.minecraft.resources.ResourceKey;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.tags.TagLoader;
/*     */ import net.minecraft.tags.TagNetworkSerialization;
/*     */ 
/*     */ public class RegistryDataCollector {
/*     */   private ContentsCollector contentsCollector;
/*     */   private TagCollector tagCollector;
/*     */   
/*     */   public void appendContents(ResourceKey<? extends Registry<?>> registry, List<RegistrySynchronization.PackedRegistryEntry> elementData) {
/*  32 */     if (this.contentsCollector == null) {
/*  33 */       this.contentsCollector = new ContentsCollector();
/*     */     }
/*     */     
/*  36 */     this.contentsCollector.append(registry, elementData);
/*     */   }
/*     */   
/*     */   public void appendTags(Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> data) {
/*  40 */     if (this.tagCollector == null) {
/*  41 */       this.tagCollector = new TagCollector();
/*     */     }
/*     */     
/*  44 */     Objects.requireNonNull(this.tagCollector); data.forEach(this.tagCollector::append);
/*     */   }
/*     */   
/*     */   private static class ContentsCollector {
/*  48 */     private final Map<ResourceKey<? extends Registry<?>>, List<RegistrySynchronization.PackedRegistryEntry>> elements = new HashMap<>();
/*     */ 
/*     */     
/*     */     public void append(ResourceKey<? extends Registry<?>> registry, List<RegistrySynchronization.PackedRegistryEntry> elementData) {
/*  52 */       ((List<RegistrySynchronization.PackedRegistryEntry>)this.elements.computeIfAbsent(registry, ignore -> new ArrayList())).addAll(elementData);
/*     */     }
/*     */   }
/*     */   
/*     */   private static <T> Registry.PendingTags<T> resolveRegistryTags(RegistryAccess.Frozen context, ResourceKey<? extends Registry<? extends T>> registryKey, TagNetworkSerialization.NetworkPayload tags) {
/*  57 */     Registry<T> staticRegistry = context.lookupOrThrow(registryKey);
/*  58 */     return staticRegistry.prepareTagReload(tags.resolve(staticRegistry));
/*     */   }
/*     */   
/*     */   private RegistryAccess loadNewElementsAndTags(ResourceProvider knownDataSource, ContentsCollector contentsCollector, boolean tagsForSynchronizedRegistriesOnly) {
/*     */     RegistryAccess.Frozen receivedRegistries;
/*  63 */     LayeredRegistryAccess<ClientRegistryLayer> base = ClientRegistryLayer.createRegistryAccess();
/*  64 */     RegistryAccess.Frozen loadingContext = base.getAccessForLoading(ClientRegistryLayer.REMOTE);
/*     */     
/*  66 */     Map<ResourceKey<? extends Registry<?>>, RegistryDataLoader.NetworkedRegistryData> entriesToLoad = new HashMap<>();
/*  67 */     contentsCollector.elements.forEach((registryKey, elements) -> entriesToLoad.put(registryKey, new RegistryDataLoader.NetworkedRegistryData(elements, TagNetworkSerialization.NetworkPayload.EMPTY)));
/*     */     
/*  69 */     List<Registry.PendingTags<?>> pendingStaticTags = new ArrayList<>();
/*  70 */     if (this.tagCollector != null) {
/*  71 */       this.tagCollector.forEach((registryKey, tags) -> {
/*     */             if (tags.isEmpty()) {
/*     */               return;
/*     */             }
/*     */ 
/*     */ 
/*     */ 
/*     */             
/*     */             if (RegistrySynchronization.isNetworkable(registryKey)) {
/*     */               entriesToLoad.compute(registryKey, ());
/*     */             } else if (!tagsForSynchronizedRegistriesOnly) {
/*     */               pendingStaticTags.add(resolveRegistryTags(loadingContext, registryKey, tags));
/*     */             } 
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  90 */     List<HolderLookup.RegistryLookup<?>> contextRegistriesWithTags = TagLoader.buildUpdatedLookups(loadingContext, pendingStaticTags);
/*     */     
/*     */     try {
/*  93 */       receivedRegistries = RegistryDataLoader.load(entriesToLoad, knownDataSource, contextRegistriesWithTags, RegistryDataLoader.SYNCHRONIZED_REGISTRIES).freeze();
/*  94 */     } catch (Exception e) {
/*  95 */       CrashReport report = CrashReport.forThrowable(e, "Network Registry Load");
/*  96 */       addCrashDetails(report, entriesToLoad, pendingStaticTags);
/*  97 */       throw new ReportedException(report);
/*     */     } 
/*     */ 
/*     */     
/* 101 */     RegistryAccess.Frozen frozen1 = base.replaceFrom(ClientRegistryLayer.REMOTE, new RegistryAccess.Frozen[] { receivedRegistries
/* 102 */         }).compositeAccess();
/*     */ 
/*     */     
/* 105 */     pendingStaticTags.forEach(Registry.PendingTags::apply);
/* 106 */     return (RegistryAccess)frozen1;
/*     */   }
/*     */   
/*     */   private static void addCrashDetails(CrashReport report, Map<ResourceKey<? extends Registry<?>>, RegistryDataLoader.NetworkedRegistryData> dynamicRegistries, List<Registry.PendingTags<?>> staticRegistries) {
/* 110 */     CrashReportCategory details = report.addCategory("Received Elements and Tags");
/* 111 */     details.setDetail("Dynamic Registries", () -> (String)dynamicRegistries.entrySet().stream().sorted(Comparator.comparing(())).map(()).collect(Collectors.joining()));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 121 */     details.setDetail("Static Registries", () -> (String)staticRegistries.stream().sorted(Comparator.comparing(())).map(()).collect(Collectors.joining()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void loadOnlyTags(TagCollector tagCollector, RegistryAccess.Frozen originalRegistries, boolean includeSharedTags) {
/* 132 */     tagCollector.forEach((registryKey, tags) -> {
/*     */           if (includeSharedTags || RegistrySynchronization.isNetworkable(registryKey)) {
/*     */             resolveRegistryTags(originalRegistries, registryKey, tags).apply();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public RegistryAccess.Frozen collectGameRegistries(ResourceProvider knownDataSource, RegistryAccess.Frozen originalRegistries, boolean tagsForSynchronizedRegistriesOnly) {
/*     */     RegistryAccess.Frozen frozen;
/* 142 */     if (this.contentsCollector != null) {
/*     */       
/* 144 */       RegistryAccess registries = loadNewElementsAndTags(knownDataSource, this.contentsCollector, tagsForSynchronizedRegistriesOnly);
/*     */     } else {
/*     */       
/* 147 */       if (this.tagCollector != null) {
/* 148 */         loadOnlyTags(this.tagCollector, originalRegistries, !tagsForSynchronizedRegistriesOnly);
/*     */       }
/* 150 */       frozen = originalRegistries;
/*     */     } 
/*     */     
/* 153 */     return frozen.freeze();
/*     */   }
/*     */   
/*     */   private static class TagCollector { private TagCollector() {
/* 157 */       this.tags = new HashMap<>();
/*     */     } private final Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> tags;
/*     */     public void append(ResourceKey<? extends Registry<?>> registry, TagNetworkSerialization.NetworkPayload tagData) {
/* 160 */       this.tags.put(registry, tagData);
/*     */     }
/*     */     
/*     */     public void forEach(BiConsumer<? super ResourceKey<? extends Registry<?>>, ? super TagNetworkSerialization.NetworkPayload> action) {
/* 164 */       this.tags.forEach(action);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/RegistryDataCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */