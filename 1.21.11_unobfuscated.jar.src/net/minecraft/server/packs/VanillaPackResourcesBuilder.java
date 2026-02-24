/*     */ package net.minecraft.server.packs;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.EnumMap;
/*     */ import java.util.Enumeration;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.util.FileSystemUtil;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class VanillaPackResourcesBuilder
/*     */ {
/*  29 */   private static final Logger LOGGER = LogUtils.getLogger(); public static Consumer<VanillaPackResourcesBuilder> developmentConfig = builder -> {
/*     */     
/*     */     }; private static final Map<PackType, Path> ROOT_DIR_BY_TYPE;
/*     */   static {
/*  33 */     ROOT_DIR_BY_TYPE = (Map<PackType, Path>)Util.make(() -> {
/*     */           synchronized (VanillaPackResources.class) {
/*     */             ImmutableMap.Builder<PackType, Path> result = ImmutableMap.builder();
/*     */             
/*     */             for (PackType type : PackType.values()) {
/*     */               String probeName = "/" + type.getDirectory() + "/.mcassetsroot";
/*     */               
/*     */               URL probeUrl = VanillaPackResources.class.getResource(probeName);
/*     */               if (probeUrl == null) {
/*     */                 LOGGER.error("File {} does not exist in classpath", probeName);
/*     */               } else {
/*     */                 try {
/*     */                   URI probeUri = probeUrl.toURI();
/*     */                   String scheme = probeUri.getScheme();
/*     */                   if (!"jar".equals(scheme) && !"file".equals(scheme)) {
/*     */                     LOGGER.warn("Assets URL '{}' uses unexpected schema", probeUri);
/*     */                   }
/*     */                   Path probePath = FileSystemUtil.safeGetPath(probeUri);
/*     */                   result.put(type, probePath.getParent());
/*  52 */                 } catch (Exception e) {
/*     */                   LOGGER.error("Couldn't resolve path to vanilla assets", e);
/*     */                 } 
/*     */               } 
/*     */             } 
/*     */             return result.build();
/*     */           } 
/*     */         });
/*  60 */   } private final Set<Path> rootPaths = new LinkedHashSet<>();
/*  61 */   private final Map<PackType, Set<Path>> pathsForType = new EnumMap<>(PackType.class);
/*     */   
/*  63 */   private BuiltInMetadata metadata = BuiltInMetadata.of();
/*  64 */   private final Set<String> namespaces = new HashSet<>();
/*     */   
/*     */   private boolean validateDirPath(Path path) {
/*  67 */     if (!Files.exists(path, new java.nio.file.LinkOption[0])) {
/*  68 */       return false;
/*     */     }
/*  70 */     if (!Files.isDirectory(path, new java.nio.file.LinkOption[0])) {
/*  71 */       throw new IllegalArgumentException("Path " + String.valueOf(path.toAbsolutePath()) + " is not directory");
/*     */     }
/*  73 */     return true;
/*     */   }
/*     */   
/*     */   private void pushRootPath(Path path) {
/*  77 */     if (validateDirPath(path)) {
/*  78 */       this.rootPaths.add(path);
/*     */     }
/*     */   }
/*     */   
/*     */   private void pushPathForType(PackType packType, Path path) {
/*  83 */     if (validateDirPath(path)) {
/*  84 */       ((Set<Path>)this.pathsForType.computeIfAbsent(packType, k -> new LinkedHashSet())).add(path);
/*     */     }
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder pushJarResources() {
/*  89 */     ROOT_DIR_BY_TYPE.forEach((packType, path) -> {
/*     */           pushRootPath(path.getParent());
/*     */           pushPathForType(packType, path);
/*     */         });
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public VanillaPackResourcesBuilder pushClasspathResources(PackType packType, Class<?> source) {
/*  99 */     Enumeration<URL> resources = null;
/*     */     try {
/* 101 */       resources = source.getClassLoader().getResources(packType.getDirectory() + "/");
/* 102 */     } catch (IOException iOException) {}
/*     */ 
/*     */     
/* 105 */     while (resources != null && resources.hasMoreElements()) {
/* 106 */       URL url = resources.nextElement();
/*     */       try {
/* 108 */         URI uri = url.toURI();
/* 109 */         if ("file".equals(uri.getScheme())) {
/* 110 */           Path assetsPath = Paths.get(uri);
/* 111 */           pushRootPath(assetsPath.getParent());
/* 112 */           pushPathForType(packType, assetsPath);
/*     */         } 
/* 114 */       } catch (Exception e) {
/* 115 */         LOGGER.error("Failed to extract path from {}", url, e);
/*     */       } 
/*     */     } 
/* 118 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder applyDevelopmentConfig() {
/* 122 */     developmentConfig.accept(this);
/* 123 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VanillaPackResourcesBuilder pushUniversalPath(Path path) {
/* 130 */     pushRootPath(path);
/* 131 */     for (PackType packType : PackType.values()) {
/* 132 */       pushPathForType(packType, path.resolve(packType.getDirectory()));
/*     */     }
/* 134 */     return this;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public VanillaPackResourcesBuilder pushAssetPath(PackType packType, Path path) {
/* 141 */     pushRootPath(path);
/* 142 */     pushPathForType(packType, path);
/* 143 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder setMetadata(BuiltInMetadata metadata) {
/* 147 */     this.metadata = metadata;
/* 148 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResourcesBuilder exposeNamespace(String... namespaces) {
/* 152 */     this.namespaces.addAll(Arrays.asList(namespaces));
/* 153 */     return this;
/*     */   }
/*     */   
/*     */   public VanillaPackResources build(PackLocationInfo location) {
/* 157 */     return new VanillaPackResources(location, this.metadata, 
/*     */ 
/*     */         
/* 160 */         Set.copyOf(this.namespaces), 
/* 161 */         copyAndReverse(this.rootPaths), 
/* 162 */         Util.makeEnumMap(PackType.class, packType -> copyAndReverse(this.pathsForType.getOrDefault(packType, Set.of()))));
/*     */   }
/*     */ 
/*     */   
/*     */   private static List<Path> copyAndReverse(Collection<Path> input) {
/* 167 */     List<Path> paths = new ArrayList<>(input);
/* 168 */     Collections.reverse(paths);
/* 169 */     return List.copyOf(paths);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/VanillaPackResourcesBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */