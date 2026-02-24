/*     */ package net.minecraft.server.packs.resources;
/*     */ 
/*     */ import com.google.common.collect.ImmutableSet;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.io.StringWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Predicate;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.PackType;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class FallbackResourceManager
/*     */   implements ResourceManager {
/*  33 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  35 */   protected final List<PackEntry> fallbacks = Lists.newArrayList();
/*     */   private final PackType type;
/*     */   private final String namespace;
/*     */   
/*     */   public FallbackResourceManager(PackType type, String namespace) {
/*  40 */     this.type = type;
/*  41 */     this.namespace = namespace;
/*     */   }
/*     */   
/*     */   public void push(PackResources pack) {
/*  45 */     pushInternal(pack.packId(), pack, null);
/*     */   }
/*     */   
/*     */   public void push(PackResources pack, Predicate<Identifier> filter) {
/*  49 */     pushInternal(pack.packId(), pack, filter);
/*     */   }
/*     */   
/*     */   public void pushFilterOnly(String name, Predicate<Identifier> filter) {
/*  53 */     pushInternal(name, null, filter);
/*     */   }
/*     */   
/*     */   private void pushInternal(String name, PackResources pack, Predicate<Identifier> contentFilter) {
/*  57 */     this.fallbacks.add(new PackEntry(name, pack, contentFilter));
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<String> getNamespaces() {
/*  62 */     return (Set<String>)ImmutableSet.of(this.namespace);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Optional<Resource> getResource(Identifier location) {
/*  68 */     for (int i = this.fallbacks.size() - 1; i >= 0; i--) {
/*  69 */       PackEntry entry = this.fallbacks.get(i);
/*  70 */       PackResources fallback = entry.resources;
/*  71 */       if (fallback != null) {
/*  72 */         IoSupplier<InputStream> resource = fallback.getResource(this.type, location);
/*  73 */         if (resource != null) {
/*  74 */           IoSupplier<ResourceMetadata> metadataGetter = createStackMetadataFinder(location, i);
/*  75 */           return Optional.of(createResource(fallback, location, resource, metadataGetter));
/*     */         } 
/*     */       } 
/*     */       
/*  79 */       if (entry.isFiltered(location)) {
/*  80 */         LOGGER.warn("Resource {} not found, but was filtered by pack {}", location, entry.name);
/*  81 */         return Optional.empty();
/*     */       } 
/*     */     } 
/*     */     
/*  85 */     return Optional.empty();
/*     */   }
/*     */   
/*     */   private static Resource createResource(PackResources source, Identifier location, IoSupplier<InputStream> resource, IoSupplier<ResourceMetadata> metadata) {
/*  89 */     return new Resource(source, wrapForDebug(location, source, resource), metadata);
/*     */   }
/*     */   
/*     */   private static IoSupplier<InputStream> wrapForDebug(Identifier location, PackResources source, IoSupplier<InputStream> resource) {
/*  93 */     if (LOGGER.isDebugEnabled()) {
/*  94 */       return () -> new LeakedResourceWarningInputStream(resource.get(), location, source.packId());
/*     */     }
/*  96 */     return resource;
/*     */   }
/*     */   
/*     */   private static class LeakedResourceWarningInputStream
/*     */     extends FilterInputStream {
/*     */     private final Supplier<String> message;
/*     */     private boolean closed;
/*     */     
/*     */     public LeakedResourceWarningInputStream(InputStream wrapped, Identifier location, String name) {
/* 105 */       super(wrapped);
/* 106 */       Exception exception = new Exception("Stacktrace");
/* 107 */       this.message = (() -> {
/*     */           StringWriter data = new StringWriter();
/*     */           exception.printStackTrace(new PrintWriter(data));
/*     */           return "Leaked resource: '" + String.valueOf(location) + "' loaded from pack: '" + name + "'\n" + String.valueOf(data);
/*     */         });
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() throws IOException {
/* 116 */       super.close();
/* 117 */       this.closed = true;
/*     */     }
/*     */ 
/*     */     
/*     */     protected void finalize() throws Throwable {
/* 122 */       if (!this.closed) {
/* 123 */         FallbackResourceManager.LOGGER.warn("{}", this.message.get());
/*     */       }
/*     */       
/* 126 */       super.finalize();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Resource> getResourceStack(Identifier location) {
/* 132 */     Identifier metadataLocation = getMetadataLocation(location);
/* 133 */     List<Resource> result = new ArrayList<>();
/*     */     
/*     */     boolean filterMeta = false;
/* 136 */     String lastFilterName = null;
/*     */ 
/*     */     
/* 139 */     for (int i = this.fallbacks.size() - 1; i >= 0; i--) {
/* 140 */       PackEntry entry = this.fallbacks.get(i);
/* 141 */       PackResources fileSource = entry.resources;
/* 142 */       if (fileSource != null) {
/* 143 */         IoSupplier<InputStream> resource = fileSource.getResource(this.type, location);
/* 144 */         if (resource != null) {
/*     */           IoSupplier<ResourceMetadata> metadataGetter;
/* 146 */           if (filterMeta) {
/* 147 */             metadataGetter = ResourceMetadata.EMPTY_SUPPLIER;
/*     */           } else {
/* 149 */             metadataGetter = (() -> {
/*     */                 IoSupplier<InputStream> metaResource = fileSource.getResource(this.type, metadataLocation);
/*     */                 return (metaResource != null) ? parseMetadata(metaResource) : ResourceMetadata.EMPTY;
/*     */               });
/*     */           } 
/* 154 */           result.add(new Resource(fileSource, resource, metadataGetter));
/*     */         } 
/*     */       } 
/*     */       
/* 158 */       if (entry.isFiltered(location)) {
/* 159 */         lastFilterName = entry.name; break;
/*     */       } 
/* 161 */       if (entry.isFiltered(metadataLocation)) {
/* 162 */         filterMeta = true;
/*     */       }
/*     */     } 
/*     */     
/* 166 */     if (result.isEmpty() && lastFilterName != null) {
/* 167 */       LOGGER.warn("Resource {} not found, but was filtered by pack {}", location, lastFilterName);
/*     */     }
/*     */     
/* 170 */     return Lists.reverse(result);
/*     */   }
/*     */   
/*     */   private static boolean isMetadata(Identifier location) {
/* 174 */     return location.getPath().endsWith(".mcmeta");
/*     */   }
/*     */   
/*     */   private static Identifier getIdentifierFromMetadata(Identifier identifier) {
/* 178 */     String newPath = identifier.getPath().substring(0, identifier.getPath().length() - ".mcmeta".length());
/* 179 */     return identifier.withPath(newPath);
/*     */   }
/*     */   
/*     */   private static Identifier getMetadataLocation(Identifier identifier) {
/* 183 */     return identifier.withPath(identifier.getPath() + ".mcmeta");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<Identifier, Resource> listResources(String directory, Predicate<Identifier> filter) {
/* 190 */     Map<Identifier, ResourceWithSourceAndIndex> topResourceForFileLocation = new HashMap<>();
/* 191 */     Map<Identifier, ResourceWithSourceAndIndex> topResourceForMetaLocation = new HashMap<>();
/*     */     
/* 193 */     int packCount = this.fallbacks.size();
/* 194 */     for (int i = 0; i < packCount; i++) {
/* 195 */       PackEntry entry = this.fallbacks.get(i);
/* 196 */       entry.filterAll(topResourceForFileLocation.keySet());
/* 197 */       entry.filterAll(topResourceForMetaLocation.keySet());
/*     */       
/* 199 */       PackResources packResources = entry.resources;
/* 200 */       if (packResources != null) {
/* 201 */         int packIndex = i;
/* 202 */         packResources.listResources(this.type, this.namespace, directory, (resource, streamSupplier) -> { if (isMetadata(resource)) { if (filter.test(getIdentifierFromMetadata(resource))) { static final class ResourceWithSourceAndIndex extends Record {
/*     */                     private final PackResources packResources;
/*     */                     private final IoSupplier<InputStream> resource;
/*     */                     private final int packIndex;
/*     */                     ResourceWithSourceAndIndex(PackResources packResources, IoSupplier<InputStream> resource, int packIndex) { this.packResources = packResources; this.resource = resource; this.packIndex = packIndex; }
/*     */                     public final String toString() { // Byte code:
/*     */                       //   0: aload_0
/*     */                       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$1ResourceWithSourceAndIndex;)Ljava/lang/String;
/*     */                       //   6: areturn
/*     */                       // Line number table:
/*     */                       //   Java source line number -> byte code offset
/*     */                       //   #188	-> 0
/*     */                       // Local variable table:
/*     */                       //   start	length	slot	name	descriptor
/*     */                       //   0	7	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$1ResourceWithSourceAndIndex; }
/*     */                     public final int hashCode() { // Byte code:
/*     */                       //   0: aload_0
/*     */                       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$1ResourceWithSourceAndIndex;)I
/*     */                       //   6: ireturn
/*     */                       // Line number table:
/*     */                       //   Java source line number -> byte code offset
/*     */                       //   #188	-> 0
/*     */                       // Local variable table:
/*     */                       //   start	length	slot	name	descriptor
/*     */                       //   0	7	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$1ResourceWithSourceAndIndex; }
/*     */                     public final boolean equals(Object o) { // Byte code:
/*     */                       //   0: aload_0
/*     */                       //   1: aload_1
/*     */                       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$1ResourceWithSourceAndIndex;Ljava/lang/Object;)Z
/*     */                       //   7: ireturn
/*     */                       // Line number table:
/*     */                       //   Java source line number -> byte code offset
/*     */                       //   #188	-> 0
/*     */                       // Local variable table:
/*     */                       //   start	length	slot	name	descriptor
/*     */                       //   0	8	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$1ResourceWithSourceAndIndex;
/*     */                       //   0	8	1	o	Ljava/lang/Object; }
/*     */                     public PackResources packResources() { return this.packResources; } public IoSupplier<InputStream> resource() { return this.resource; } public int packIndex() { return this.packIndex; } }; topResourceForMetaLocation.put(resource, new ResourceWithSourceAndIndex(packResources, streamSupplier, packIndex)); }
/*     */                  }
/*     */               else if (filter.test(resource)) { topResourceForFileLocation.put(resource, new ResourceWithSourceAndIndex(packResources, streamSupplier, packIndex)); }
/*     */             
/*     */             });
/*     */       } 
/* 216 */     }  Map<Identifier, Resource> result = Maps.newTreeMap();
/* 217 */     topResourceForFileLocation.forEach((location, resource) -> {
/*     */           IoSupplier<ResourceMetadata> metaGetter;
/*     */           Identifier metadataLocation = getMetadataLocation(location);
/*     */           ResourceWithSourceAndIndex metaResource = (ResourceWithSourceAndIndex)topResourceForMetaLocation.get(metadataLocation);
/*     */           if (metaResource != null && metaResource.packIndex >= resource.packIndex) {
/*     */             metaGetter = convertToMetadata(metaResource.resource);
/*     */           } else {
/*     */             metaGetter = ResourceMetadata.EMPTY_SUPPLIER;
/*     */           } 
/*     */           result.put(location, createResource(resource.packResources, location, resource.resource, metaGetter));
/*     */         });
/* 228 */     return result;
/*     */   }
/*     */   
/*     */   private IoSupplier<ResourceMetadata> createStackMetadataFinder(Identifier location, int finalPackIndex) {
/* 232 */     return () -> {
/*     */         Identifier metadataLocation = getMetadataLocation(location);
/*     */         for (int i = this.fallbacks.size() - 1; i >= finalPackIndex; i--) {
/*     */           PackEntry entry = this.fallbacks.get(i);
/*     */           PackResources metadataPackCandidate = entry.resources;
/*     */           if (metadataPackCandidate != null) {
/*     */             IoSupplier<InputStream> resource = metadataPackCandidate.getResource(this.type, metadataLocation);
/*     */             if (resource != null) {
/*     */               return parseMetadata(resource);
/*     */             }
/*     */           } 
/*     */           if (entry.isFiltered(metadataLocation)) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */         return ResourceMetadata.EMPTY;
/*     */       };
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static IoSupplier<ResourceMetadata> convertToMetadata(IoSupplier<InputStream> input) {
/* 255 */     return () -> parseMetadata(input);
/*     */   }
/*     */   
/*     */   private static ResourceMetadata parseMetadata(IoSupplier<InputStream> input) throws IOException {
/* 259 */     InputStream metadata = input.get(); 
/* 260 */     try { ResourceMetadata resourceMetadata = ResourceMetadata.fromJsonStream(metadata);
/* 261 */       if (metadata != null) metadata.close();  return resourceMetadata; } catch (Throwable throwable) { if (metadata != null)
/*     */         try { metadata.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 264 */      } private static final class EntryStack extends Record { private final Identifier fileLocation; private final Identifier metadataLocation; private final List<FallbackResourceManager.ResourceWithSource> fileSources; private final Map<PackResources, IoSupplier<InputStream>> metaSources; public Map<PackResources, IoSupplier<InputStream>> metaSources() { return this.metaSources; } public List<FallbackResourceManager.ResourceWithSource> fileSources() { return this.fileSources; } public Identifier metadataLocation() { return this.metadataLocation; } public Identifier fileLocation() { return this.fileLocation; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$EntryStack;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #264	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$EntryStack;
/*     */       //   0	8	1	o	Ljava/lang/Object; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$EntryStack;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #264	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$EntryStack; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$EntryStack;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #264	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 264 */       //   0	7	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$EntryStack; } private EntryStack(Identifier fileLocation, Identifier metadataLocation, List<FallbackResourceManager.ResourceWithSource> fileSources, Map<PackResources, IoSupplier<InputStream>> metaSources) { this.fileLocation = fileLocation; this.metadataLocation = metadataLocation; this.fileSources = fileSources; this.metaSources = metaSources; }
/*     */      EntryStack(Identifier fileLocation) {
/* 266 */       this(fileLocation, 
/*     */           
/* 268 */           FallbackResourceManager.getMetadataLocation(fileLocation), new ArrayList<>(), (Map<PackResources, IoSupplier<InputStream>>)new Object2ObjectArrayMap());
/*     */     } }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void applyPackFiltersToExistingResources(PackEntry entry, Map<Identifier, EntryStack> foundResources) {
/* 276 */     for (EntryStack e : foundResources.values()) {
/* 277 */       if (entry.isFiltered(e.fileLocation)) {
/* 278 */         e.fileSources.clear(); continue;
/* 279 */       }  if (entry.isFiltered(e.metadataLocation())) {
/* 280 */         e.metaSources.clear();
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private void listPackResources(PackEntry entry, String directory, Predicate<Identifier> filter, Map<Identifier, EntryStack> foundResources) {
/* 286 */     PackResources pack = entry.resources;
/* 287 */     if (pack == null) {
/*     */       return;
/*     */     }
/* 290 */     pack.listResources(this.type, this.namespace, directory, (id, resource) -> {
/*     */           if (isMetadata(id)) {
/*     */             Identifier actualId = getIdentifierFromMetadata(id);
/*     */             if (!filter.test(actualId)) {
/*     */               return;
/*     */             }
/*     */             ((EntryStack)foundResources.computeIfAbsent((K)actualId, EntryStack::new)).metaSources.put(pack, resource);
/*     */           } else {
/*     */             if (!filter.test(id)) {
/*     */               return;
/*     */             }
/*     */             ((EntryStack)foundResources.computeIfAbsent((K)id, EntryStack::new)).fileSources.add(new ResourceWithSource(pack, resource));
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Identifier, List<Resource>> listResourceStacks(String directory, Predicate<Identifier> filter) {
/* 308 */     Map<Identifier, EntryStack> foundResources = Maps.newHashMap();
/*     */     
/* 310 */     for (PackEntry entry : this.fallbacks) {
/* 311 */       applyPackFiltersToExistingResources(entry, foundResources);
/* 312 */       listPackResources(entry, directory, filter, foundResources);
/*     */     } 
/*     */     
/* 315 */     TreeMap<Identifier, List<Resource>> result = Maps.newTreeMap();
/* 316 */     for (EntryStack entry : foundResources.values()) {
/* 317 */       if (entry.fileSources.isEmpty()) {
/*     */         continue;
/*     */       }
/*     */       
/* 321 */       List<Resource> resources = new ArrayList<>();
/* 322 */       for (ResourceWithSource stackEntry : entry.fileSources) {
/* 323 */         PackResources source = stackEntry.source;
/* 324 */         IoSupplier<InputStream> metaSource = entry.metaSources.get(source);
/* 325 */         IoSupplier<ResourceMetadata> metaGetter = (metaSource != null) ? convertToMetadata(metaSource) : ResourceMetadata.EMPTY_SUPPLIER;
/* 326 */         resources.add(createResource(source, entry.fileLocation, stackEntry.resource, metaGetter));
/*     */       } 
/*     */       
/* 329 */       result.put(entry.fileLocation, resources);
/*     */     } 
/* 331 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public Stream<PackResources> listPacks() {
/* 336 */     return this.fallbacks.stream().map(p -> p.resources).filter(Objects::nonNull);
/*     */   } private static final class PackEntry extends Record {
/*     */     private final String name; private final PackResources resources; private final Predicate<Identifier> filter;
/* 339 */     private PackEntry(String name, PackResources resources, Predicate<Identifier> filter) { this.name = name; this.resources = resources; this.filter = filter; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$PackEntry;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #339	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$PackEntry; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$PackEntry;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #339	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$PackEntry; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$PackEntry;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #339	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$PackEntry;
/* 339 */       //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public PackResources resources() { return this.resources; } public Predicate<Identifier> filter() { return this.filter; }
/*     */      public void filterAll(Collection<Identifier> collection) {
/* 341 */       if (this.filter != null) {
/* 342 */         collection.removeIf(this.filter);
/*     */       }
/*     */     }
/*     */     
/*     */     public boolean isFiltered(Identifier location) {
/* 347 */       return (this.filter != null && this.filter.test(location));
/*     */     }
/*     */   }
/*     */   private static final class ResourceWithSource extends Record { private final PackResources source; private final IoSupplier<InputStream> resource;
/* 351 */     private ResourceWithSource(PackResources source, IoSupplier<InputStream> resource) { this.source = source; this.resource = resource; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$ResourceWithSource;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #351	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$ResourceWithSource; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$ResourceWithSource;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #351	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$ResourceWithSource; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/resources/FallbackResourceManager$ResourceWithSource;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #351	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/server/packs/resources/FallbackResourceManager$ResourceWithSource;
/* 351 */       //   0	8	1	o	Ljava/lang/Object; } public PackResources source() { return this.source; } public IoSupplier<InputStream> resource() { return this.resource; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/resources/FallbackResourceManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */