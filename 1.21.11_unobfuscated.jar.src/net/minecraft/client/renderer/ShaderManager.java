/*     */ package net.minecraft.client.renderer;
/*     */ 
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.google.gson.JsonElement;
/*     */ import com.mojang.blaze3d.pipeline.CompiledRenderPipeline;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
/*     */ import com.mojang.blaze3d.shaders.ShaderType;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.DynamicOps;
/*     */ import com.mojang.serialization.JsonOps;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArraySet;
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.IdentifierException;
/*     */ import net.minecraft.client.renderer.texture.TextureManager;
/*     */ import net.minecraft.resources.FileToIdConverter;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.server.packs.resources.Resource;
/*     */ import net.minecraft.server.packs.resources.ResourceManager;
/*     */ import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
/*     */ import net.minecraft.util.FileUtil;
/*     */ import net.minecraft.util.StrictJsonParser;
/*     */ import net.minecraft.util.profiling.ProfilerFiller;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ShaderManager
/*     */   extends SimplePreparableReloadListener<ShaderManager.Configs> implements AutoCloseable {
/*  43 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int MAX_LOG_LENGTH = 32768;
/*     */   
/*     */   public static final String SHADER_PATH = "shaders";
/*     */   private static final String SHADER_INCLUDE_PATH = "shaders/include/";
/*  49 */   private static final FileToIdConverter POST_CHAIN_ID_CONVERTER = FileToIdConverter.json("post_effect");
/*     */   
/*     */   private final TextureManager textureManager;
/*     */   
/*     */   private final Consumer<Exception> recoveryHandler;
/*  54 */   private CompilationCache compilationCache = new CompilationCache(Configs.EMPTY);
/*  55 */   private final CachedOrthoProjectionMatrixBuffer postChainProjectionMatrixBuffer = new CachedOrthoProjectionMatrixBuffer("post", 0.1F, 1000.0F, false);
/*     */   
/*     */   public ShaderManager(TextureManager textureManager, Consumer<Exception> recoveryHandler) {
/*  58 */     this.textureManager = textureManager;
/*  59 */     this.recoveryHandler = recoveryHandler;
/*     */   }
/*     */ 
/*     */   
/*     */   protected Configs prepare(ResourceManager manager, ProfilerFiller profiler) {
/*  64 */     ImmutableMap.Builder<ShaderSourceKey, String> shaderSources = ImmutableMap.builder();
/*     */     
/*  66 */     Map<Identifier, Resource> files = manager.listResources("shaders", ShaderManager::isShader);
/*  67 */     for (Map.Entry<Identifier, Resource> entry : files.entrySet()) {
/*  68 */       Identifier location = entry.getKey();
/*  69 */       ShaderType shaderType = ShaderType.byLocation(location);
/*  70 */       if (shaderType != null) {
/*  71 */         loadShader(location, entry.getValue(), shaderType, files, shaderSources);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/*  76 */     ImmutableMap.Builder<Identifier, PostChainConfig> postChains = ImmutableMap.builder();
/*  77 */     for (Map.Entry<Identifier, Resource> entry : (Iterable<Map.Entry<Identifier, Resource>>)POST_CHAIN_ID_CONVERTER.listMatchingResources(manager).entrySet()) {
/*  78 */       loadPostChain(entry.getKey(), entry.getValue(), postChains);
/*     */     }
/*     */     
/*  81 */     return new Configs((Map<ShaderSourceKey, String>)shaderSources.build(), (Map<Identifier, PostChainConfig>)postChains.build());
/*     */   }
/*     */   
/*     */   private static void loadShader(Identifier location, Resource resource, ShaderType type, Map<Identifier, Resource> files, ImmutableMap.Builder<ShaderSourceKey, String> output) {
/*  85 */     Identifier id = type.idConverter().fileToId(location);
/*  86 */     GlslPreprocessor preprocessor = createPreprocessor(files, location); 
/*  87 */     try { Reader reader = resource.openAsReader(); 
/*  88 */       try { String source = IOUtils.toString(reader);
/*  89 */         output.put(new ShaderSourceKey(id, type), String.join("", preprocessor.process(source)));
/*  90 */         if (reader != null) reader.close();  } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/*  91 */     { LOGGER.error("Failed to load shader source at {}", location, e); }
/*     */   
/*     */   }
/*     */   
/*     */   private static GlslPreprocessor createPreprocessor(final Map<Identifier, Resource> files, Identifier location) {
/*  96 */     final Identifier parentLocation = location.withPath(FileUtil::getFullResourcePath);
/*  97 */     return new GlslPreprocessor() {
/*  98 */         private final Set<Identifier> importedLocations = (Set<Identifier>)new ObjectArraySet();
/*     */ 
/*     */         
/*     */         public String applyImport(boolean isRelative, String path) {
/*     */           Identifier location;
/*     */           try {
/* 104 */             if (isRelative) {
/* 105 */               location = parentLocation.withPath(parentPath -> FileUtil.normalizeResourcePath(parentPath + parentPath));
/*     */             } else {
/* 107 */               location = Identifier.parse(path).withPrefix("shaders/include/");
/*     */             } 
/* 109 */           } catch (IdentifierException e) {
/* 110 */             ShaderManager.LOGGER.error("Malformed GLSL import {}: {}", path, e.getMessage());
/* 111 */             return "#error " + e.getMessage();
/*     */           } 
/*     */           
/* 114 */           if (!this.importedLocations.add(location)) {
/* 115 */             return null;
/*     */           }
/*     */           
/* 118 */           try { Reader importResource = ((Resource)files.get(location)).openAsReader(); 
/* 119 */             try { String str = IOUtils.toString(importResource);
/* 120 */               if (importResource != null) importResource.close();  return str; } catch (Throwable throwable) { if (importResource != null) try { importResource.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException e)
/* 121 */           { ShaderManager.LOGGER.error("Could not open GLSL import {}: {}", location, e.getMessage());
/* 122 */             return "#error " + e.getMessage(); }
/*     */         
/*     */         }
/*     */       };
/*     */   }
/*     */   
/*     */   private static void loadPostChain(Identifier location, Resource resource, ImmutableMap.Builder<Identifier, PostChainConfig> output) {
/* 129 */     Identifier id = POST_CHAIN_ID_CONVERTER.fileToId(location); 
/* 130 */     try { Reader reader = resource.openAsReader(); 
/* 131 */       try { JsonElement json = StrictJsonParser.parse(reader);
/* 132 */         output.put(id, PostChainConfig.CODEC.parse((DynamicOps)JsonOps.INSTANCE, json).getOrThrow(com.google.gson.JsonSyntaxException::new));
/* 133 */         if (reader != null) reader.close();  } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException|com.google.gson.JsonParseException e)
/* 134 */     { LOGGER.error("Failed to parse post chain at {}", location, e); }
/*     */   
/*     */   }
/*     */   
/*     */   private static boolean isShader(Identifier location) {
/* 139 */     return (ShaderType.byLocation(location) != null || location.getPath().endsWith(".glsl"));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void apply(Configs preparations, ResourceManager manager, ProfilerFiller profiler) {
/* 144 */     CompilationCache newCompilationCache = new CompilationCache(preparations);
/* 145 */     Set<RenderPipeline> pipelinesToPreload = new HashSet<>(RenderPipelines.getStaticPipelines());
/* 146 */     List<Identifier> failedLoads = new ArrayList<>();
/*     */     
/* 148 */     GpuDevice device = RenderSystem.getDevice();
/* 149 */     device.clearPipelineCache();
/*     */ 
/*     */     
/* 152 */     for (RenderPipeline pipeline : pipelinesToPreload) {
/* 153 */       Objects.requireNonNull(newCompilationCache); CompiledRenderPipeline compiled = device.precompilePipeline(pipeline, newCompilationCache::getShaderSource);
/* 154 */       if (!compiled.isValid()) {
/* 155 */         failedLoads.add(pipeline.getLocation());
/*     */       }
/*     */     } 
/*     */     
/* 159 */     if (!failedLoads.isEmpty()) {
/*     */       
/* 161 */       device.clearPipelineCache();
/* 162 */       throw new RuntimeException("Failed to load required shader programs:\n" + (String)failedLoads.stream()
/* 163 */           .map(entry -> " - " + String.valueOf(entry))
/* 164 */           .collect(Collectors.joining("\n")));
/*     */     } 
/*     */ 
/*     */     
/* 168 */     this.compilationCache.close();
/* 169 */     this.compilationCache = newCompilationCache;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 174 */     return "Shader Loader";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void tryTriggerRecovery(Exception exception) {
/* 180 */     if (this.compilationCache.triggeredRecovery) {
/*     */       return;
/*     */     }
/* 183 */     this.recoveryHandler.accept(exception);
/* 184 */     this.compilationCache.triggeredRecovery = true;
/*     */   }
/*     */   
/*     */   public PostChain getPostChain(Identifier id, Set<Identifier> allowedTargets) {
/*     */     try {
/* 189 */       return this.compilationCache.getOrLoadPostChain(id, allowedTargets);
/* 190 */     } catch (CompilationException e) {
/* 191 */       LOGGER.error("Failed to load post chain: {}", id, e);
/*     */       
/* 193 */       this.compilationCache.postChains.put(id, Optional.empty());
/* 194 */       tryTriggerRecovery(e);
/* 195 */       return null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 201 */     this.compilationCache.close();
/* 202 */     this.postChainProjectionMatrixBuffer.close();
/*     */   }
/*     */   
/*     */   public String getShader(Identifier id, ShaderType type) {
/* 206 */     return this.compilationCache.getShaderSource(id, type);
/*     */   }
/*     */   public static final class Configs extends Record { private final Map<ShaderManager.ShaderSourceKey, String> shaderSources; private final Map<Identifier, PostChainConfig> postChains;
/* 209 */     public Configs(Map<ShaderManager.ShaderSourceKey, String> shaderSources, Map<Identifier, PostChainConfig> postChains) { this.shaderSources = shaderSources; this.postChains = postChains; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/ShaderManager$Configs;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #209	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 209 */       //   0	7	0	this	Lnet/minecraft/client/renderer/ShaderManager$Configs; } public Map<ShaderManager.ShaderSourceKey, String> shaderSources() { return this.shaderSources; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/ShaderManager$Configs;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #209	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/ShaderManager$Configs; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/ShaderManager$Configs;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #209	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/ShaderManager$Configs;
/* 209 */       //   0	8	1	o	Ljava/lang/Object; } public Map<Identifier, PostChainConfig> postChains() { return this.postChains; }
/*     */ 
/*     */ 
/*     */     
/* 213 */     public static final Configs EMPTY = new Configs(Map.of(), Map.of()); }
/*     */ 
/*     */   
/*     */   private class CompilationCache
/*     */     implements AutoCloseable {
/*     */     private final ShaderManager.Configs configs;
/* 219 */     private final Map<Identifier, Optional<PostChain>> postChains = new HashMap<>();
/*     */     
/*     */     private boolean triggeredRecovery;
/*     */     
/*     */     private CompilationCache(ShaderManager.Configs configs) {
/* 224 */       this.configs = configs;
/*     */     }
/*     */     
/*     */     public PostChain getOrLoadPostChain(Identifier id, Set<Identifier> allowedTargets) throws ShaderManager.CompilationException {
/* 228 */       Optional<PostChain> cached = this.postChains.get(id);
/* 229 */       if (cached != null) {
/* 230 */         return cached.orElse(null);
/*     */       }
/* 232 */       PostChain postChain = loadPostChain(id, allowedTargets);
/* 233 */       this.postChains.put(id, Optional.of(postChain));
/* 234 */       return postChain;
/*     */     }
/*     */     
/*     */     private PostChain loadPostChain(Identifier id, Set<Identifier> allowedTargets) throws ShaderManager.CompilationException {
/* 238 */       PostChainConfig config = this.configs.postChains.get(id);
/* 239 */       if (config == null) {
/* 240 */         throw new ShaderManager.CompilationException("Could not find post chain with id: " + String.valueOf(id));
/*     */       }
/* 242 */       return PostChain.load(config, ShaderManager.this.textureManager, allowedTargets, id, ShaderManager.this.postChainProjectionMatrixBuffer);
/*     */     }
/*     */ 
/*     */     
/*     */     public void close() {
/* 247 */       this.postChains.values().forEach(chain -> chain.ifPresent(PostChain::close));
/* 248 */       this.postChains.clear();
/*     */     }
/*     */     
/*     */     public String getShaderSource(Identifier id, ShaderType type) {
/* 252 */       return this.configs.shaderSources.get(new ShaderManager.ShaderSourceKey(id, type));
/*     */     } }
/*     */   private static final class ShaderSourceKey extends Record { private final Identifier id; private final ShaderType type;
/*     */     
/* 256 */     private ShaderSourceKey(Identifier id, ShaderType type) { this.id = id; this.type = type; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/ShaderManager$ShaderSourceKey;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #256	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/ShaderManager$ShaderSourceKey; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/ShaderManager$ShaderSourceKey;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #256	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/ShaderManager$ShaderSourceKey;
/* 256 */       //   0	8	1	o	Ljava/lang/Object; } public Identifier id() { return this.id; } public ShaderType type() { return this.type; }
/*     */     
/*     */     public String toString() {
/* 259 */       return String.valueOf(this.id) + " (" + String.valueOf(this.id) + ")";
/*     */     } }
/*     */ 
/*     */   
/*     */   public static class CompilationException extends Exception {
/*     */     public CompilationException(String message) {
/* 265 */       super(message);
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/ShaderManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */