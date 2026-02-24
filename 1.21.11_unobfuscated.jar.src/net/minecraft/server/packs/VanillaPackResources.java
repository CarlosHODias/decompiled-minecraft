/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.metadata.MetadataSectionType;
/*     */ import net.minecraft.server.packs.resources.IoSupplier;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceProvider;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class VanillaPackResources implements PackResources {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   private final PackLocationInfo location;
/*     */   
/*     */   private final BuiltInMetadata metadata;
/*     */   
/*     */   private final Set<String> namespaces;
/*     */   private final List<Path> rootPaths;
/*     */   private final Map<PackType, List<Path>> pathsForType;
/*     */   
/*     */   VanillaPackResources(PackLocationInfo location, BuiltInMetadata metadata, Set<String> namespaces, List<Path> rootPaths, Map<PackType, List<Path>> pathsForType) {
/*  36 */     this.location = location;
/*  37 */     this.metadata = metadata;
/*  38 */     this.namespaces = namespaces;
/*  39 */     this.rootPaths = rootPaths;
/*  40 */     this.pathsForType = pathsForType;
/*     */   }
/*     */ 
/*     */   
/*     */   public IoSupplier<InputStream> getRootResource(String... path) {
/*  45 */     FileUtil.validatePath(path);
/*     */     
/*  47 */     List<String> pathList = List.of(path);
/*  48 */     for (Path rootPath : this.rootPaths) {
/*  49 */       Path pathInRoot = FileUtil.resolvePath(rootPath, pathList);
/*  50 */       if (Files.exists(pathInRoot, new java.nio.file.LinkOption[0]) && PathPackResources.validatePath(pathInRoot)) {
/*  51 */         return IoSupplier.create(pathInRoot);
/*     */       }
/*     */     } 
/*  54 */     return null;
/*     */   }
/*     */   
/*     */   public void listRawPaths(PackType type, Identifier resource, Consumer<Path> output) {
/*  58 */     FileUtil.decomposePath(resource.getPath())
/*  59 */       .ifSuccess(decomposedPath -> {
/*     */           String namespace = resource.getNamespace();
/*     */ 
/*     */           
/*     */           for (Path typePath : this.pathsForType.get(resource)) {
/*     */             Path namespacedPath = typePath.resolve(namespace);
/*     */             
/*     */             type.accept(FileUtil.resolvePath(namespacedPath, output));
/*     */           } 
/*  68 */         }).ifError(error -> LOGGER.error("Invalid path {}: {}", resource, error.message()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void listResources(PackType type, String namespace, String directory, PackResources.ResourceOutput output) {
/*  75 */     FileUtil.decomposePath(directory)
/*  76 */       .ifSuccess(decomposedPath -> {
/*     */           List<Path> paths = this.pathsForType.get(type);
/*     */           
/*     */           int pathsSize = paths.size();
/*     */           
/*     */           if (pathsSize == 1) {
/*     */             getResources(type, type, paths.get(0), type);
/*     */           } else if (pathsSize > 1) {
/*     */             Map<Identifier, IoSupplier<InputStream>> resources = new HashMap<>();
/*     */             
/*     */             for (int i = 0; i < pathsSize - 1; i++) {
/*     */               Objects.requireNonNull(resources);
/*     */               getResources(resources::putIfAbsent, type, paths.get(i), type);
/*     */             } 
/*     */             Path lastPath = paths.get(pathsSize - 1);
/*     */             if (resources.isEmpty()) {
/*     */               getResources(type, type, lastPath, type);
/*     */             } else {
/*     */               Objects.requireNonNull(resources);
/*     */               getResources(resources::putIfAbsent, type, lastPath, type);
/*     */               resources.forEach(type);
/*     */             } 
/*     */           } 
/*  99 */         }).ifError(error -> LOGGER.error("Invalid path {}: {}", directory, error.message()));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void getResources(PackResources.ResourceOutput result, String namespace, Path root, List<String> directory) {
/* 105 */     Path namespaceDir = root.resolve(namespace);
/* 106 */     PathPackResources.listPath(namespace, namespaceDir, directory, result);
/*     */   }
/*     */ 
/*     */   
/*     */   public IoSupplier<InputStream> getResource(PackType type, Identifier location) {
/* 111 */     return (IoSupplier<InputStream>)FileUtil.decomposePath(location.getPath()).mapOrElse(decomposedPath -> {
/*     */           String namespace = location.getNamespace();
/*     */           for (Path typePath : this.pathsForType.get(location)) {
/*     */             Path resource = FileUtil.resolvePath(typePath.resolve(namespace), type);
/*     */             if (Files.exists(resource, new java.nio.file.LinkOption[0]) && PathPackResources.validatePath(resource)) {
/*     */               return IoSupplier.create(resource);
/*     */             }
/*     */           } 
/*     */           return null;
/*     */         }, error -> {
/*     */           LOGGER.error("Invalid path {}: {}", location, error.message());
/*     */           return null;
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getNamespaces(PackType type) {
/* 131 */     return this.namespaces;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T> T getMetadataSection(MetadataSectionType<T> metadataSerializer) {
/* 136 */     IoSupplier<InputStream> resource = getRootResource(new String[] { "pack.mcmeta" });
/* 137 */     if (resource != null) {
/* 138 */       try { InputStream stream = (InputStream)resource.get(); 
/* 139 */         try { T result = AbstractPackResources.getMetadataFromStream(metadataSerializer, stream, this.location);
/* 140 */           if (result != null)
/* 141 */           { T t = result;
/*     */             
/* 143 */             if (stream != null) stream.close();  return t; }  if (stream != null) stream.close();  } catch (Throwable throwable) { if (stream != null) try { stream.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException iOException) {}
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 148 */     return this.metadata.get(metadataSerializer);
/*     */   }
/*     */ 
/*     */   
/*     */   public PackLocationInfo location() {
/* 153 */     return this.location;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void close() {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ResourceProvider asProvider() {
/* 165 */     return location -> Optional.<IoSupplier<InputStream>>ofNullable(getResource(PackType.CLIENT_RESOURCES, location)).map(());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/VanillaPackResources.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */