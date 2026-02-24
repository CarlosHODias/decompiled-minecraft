/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.GpuOutOfMemoryException;
/*     */ import com.mojang.blaze3d.GraphicsWorkarounds;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.pipeline.CompiledRenderPipeline;
/*     */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*     */ import com.mojang.blaze3d.preprocessor.GlslPreprocessor;
/*     */ import com.mojang.blaze3d.shaders.ShaderSource;
/*     */ import com.mojang.blaze3d.shaders.ShaderType;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.textures.AddressMode;
/*     */ import com.mojang.blaze3d.textures.FilterMode;
/*     */ import com.mojang.blaze3d.textures.GpuSampler;
/*     */ import com.mojang.blaze3d.textures.GpuTexture;
/*     */ import com.mojang.blaze3d.textures.GpuTexture.Usage;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.textures.TextureFormat;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.OptionalDouble;
/*     */ import java.util.Set;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.renderer.ShaderDefines;
/*     */ import net.minecraft.client.renderer.ShaderManager;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import org.lwjgl.opengl.GL;
/*     */ import org.lwjgl.opengl.GL11;
/*     */ import org.lwjgl.opengl.GLCapabilities;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GlDevice
/*     */   implements GpuDevice
/*     */ {
/*  51 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   protected static boolean USE_GL_ARB_vertex_attrib_binding = true;
/*     */   
/*     */   protected static boolean USE_GL_KHR_debug = true;
/*     */   
/*     */   protected static boolean USE_GL_EXT_debug_label = true;
/*     */   protected static boolean USE_GL_ARB_debug_output = true;
/*     */   protected static boolean USE_GL_ARB_direct_state_access = true;
/*     */   protected static boolean USE_GL_ARB_buffer_storage = true;
/*     */   private final CommandEncoder encoder;
/*     */   private final GlDebug debugLog;
/*     */   private final GlDebugLabel debugLabels;
/*     */   private final int maxSupportedTextureSize;
/*     */   private final DirectStateAccess directStateAccess;
/*     */   private final ShaderSource defaultShaderSource;
/*  67 */   private final Map<RenderPipeline, GlRenderPipeline> pipelineCache = new IdentityHashMap<>();
/*  68 */   private final Map<ShaderCompilationKey, GlShaderModule> shaderCache = new HashMap<>();
/*     */   private final VertexArrayCache vertexArrayCache;
/*     */   private final BufferStorage bufferStorage;
/*  71 */   private final Set<String> enabledExtensions = new HashSet<>();
/*     */   private final int uniformOffsetAlignment;
/*     */   private final int maxSupportedAnisotropy;
/*     */   
/*     */   public GlDevice(long windowHandle, int debugLevel, boolean synchronousLogs, ShaderSource defaultShaderSource, boolean wantsDebugLabels) {
/*  76 */     GLFW.glfwMakeContextCurrent(windowHandle);
/*  77 */     GLCapabilities capabilities = GL.createCapabilities();
/*     */ 
/*     */ 
/*     */     
/*  81 */     int maxSize = getMaxSupportedTextureSize();
/*  82 */     GLFW.glfwSetWindowSizeLimits(windowHandle, -1, -1, maxSize, maxSize);
/*     */     
/*  84 */     GraphicsWorkarounds workarounds = GraphicsWorkarounds.get(this);
/*     */     
/*  86 */     this.debugLog = GlDebug.enableDebugCallback(debugLevel, synchronousLogs, this.enabledExtensions);
/*  87 */     this.debugLabels = GlDebugLabel.create(capabilities, wantsDebugLabels, this.enabledExtensions);
/*  88 */     this.vertexArrayCache = VertexArrayCache.create(capabilities, this.debugLabels, this.enabledExtensions);
/*  89 */     this.bufferStorage = BufferStorage.create(capabilities, this.enabledExtensions);
/*  90 */     this.directStateAccess = DirectStateAccess.create(capabilities, this.enabledExtensions, workarounds);
/*  91 */     this.maxSupportedTextureSize = maxSize;
/*     */     
/*  93 */     this.defaultShaderSource = defaultShaderSource;
/*  94 */     this.encoder = new GlCommandEncoder(this);
/*     */     
/*  96 */     this.uniformOffsetAlignment = GL11.glGetInteger(35380);
/*     */     
/*  98 */     GL11.glEnable(34895);
/*  99 */     GL11.glEnable(34370);
/*     */     
/* 101 */     if (capabilities.GL_EXT_texture_filter_anisotropic) {
/* 102 */       this.maxSupportedAnisotropy = Mth.floor(GL11.glGetFloat(34047));
/* 103 */       this.enabledExtensions.add("GL_EXT_texture_filter_anisotropic");
/*     */     } else {
/* 105 */       this.maxSupportedAnisotropy = 1;
/*     */     } 
/*     */   }
/*     */   
/*     */   public GlDebugLabel debugLabels() {
/* 110 */     return this.debugLabels;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public CommandEncoder createCommandEncoder() {
/* 116 */     return this.encoder;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxSupportedAnisotropy() {
/* 121 */     return this.maxSupportedAnisotropy;
/*     */   }
/*     */ 
/*     */   
/*     */   public GpuSampler createSampler(AddressMode addressModeU, AddressMode addressModeV, FilterMode minFilter, FilterMode magFilter, int maxAnisotropy, OptionalDouble maxLod) {
/* 126 */     if (maxAnisotropy < 1 || maxAnisotropy > this.maxSupportedAnisotropy) {
/* 127 */       throw new IllegalArgumentException("maxAnisotropy out of range; must be >= 1 and <= " + getMaxSupportedAnisotropy() + ", but was " + maxAnisotropy);
/*     */     }
/* 129 */     return new GlSampler(addressModeU, addressModeV, minFilter, magFilter, maxAnisotropy, maxLod);
/*     */   }
/*     */ 
/*     */   
/*     */   public GpuTexture createTexture(Supplier<String> label, @GpuTexture.Usage int usage, TextureFormat format, int width, int height, int depthOrLayers, int mipLevels) {
/* 134 */     return createTexture((this.debugLabels.exists() && label != null) ? label.get() : null, usage, format, width, height, depthOrLayers, mipLevels);
/*     */   }
/*     */   
/*     */   public GpuTexture createTexture(String label, @GpuTexture.Usage int usage, TextureFormat format, int width, int height, int depthOrLayers, int mipLevels) {
/*     */     int target;
/* 139 */     if (mipLevels < 1) {
/* 140 */       throw new IllegalArgumentException("mipLevels must be at least 1");
/*     */     }
/* 142 */     if (depthOrLayers < 1) {
/* 143 */       throw new IllegalArgumentException("depthOrLayers must be at least 1");
/*     */     }
/*     */     
/* 146 */     boolean isCubemap = ((usage & 0x10) != 0);
/* 147 */     if (isCubemap) {
/* 148 */       if (width != height) {
/* 149 */         throw new IllegalArgumentException("Cubemap compatible textures must be square, but size is " + width + "x" + height);
/*     */       }
/* 151 */       if (depthOrLayers % 6 != 0) {
/* 152 */         throw new IllegalArgumentException("Cubemap compatible textures must have a layer count with a multiple of 6, was " + depthOrLayers);
/*     */       }
/* 154 */       if (depthOrLayers > 6) {
/* 155 */         throw new UnsupportedOperationException("Array textures are not yet supported");
/*     */       }
/* 157 */     } else if (depthOrLayers > 1) {
/* 158 */       throw new UnsupportedOperationException("Array or 3D textures are not yet supported");
/*     */     } 
/*     */ 
/*     */     
/* 162 */     GlStateManager.clearGlErrors();
/* 163 */     int id = GlStateManager._genTexture();
/* 164 */     if (label == null) {
/* 165 */       label = String.valueOf(id);
/*     */     }
/*     */     
/* 168 */     if (isCubemap) {
/* 169 */       GL11.glBindTexture(34067, id);
/* 170 */       target = 34067;
/*     */     } else {
/* 172 */       GlStateManager._bindTexture(id);
/* 173 */       target = 3553;
/*     */     } 
/* 175 */     GlStateManager._texParameter(target, 33085, mipLevels - 1);
/* 176 */     GlStateManager._texParameter(target, 33082, 0);
/* 177 */     GlStateManager._texParameter(target, 33083, mipLevels - 1);
/* 178 */     if (format.hasDepthAspect()) {
/* 179 */       GlStateManager._texParameter(target, 34892, 0);
/*     */     }
/*     */     
/* 182 */     if (isCubemap) {
/* 183 */       for (int cubeTarget : GlConst.CUBEMAP_TARGETS) {
/* 184 */         for (int i = 0; i < mipLevels; i++) {
/* 185 */           GlStateManager._texImage2D(cubeTarget, i, GlConst.toGlInternalId(format), width >> i, height >> i, 0, GlConst.toGlExternalId(format), GlConst.toGlType(format), null);
/*     */         }
/*     */       } 
/*     */     } else {
/* 189 */       for (int i = 0; i < mipLevels; i++) {
/* 190 */         GlStateManager._texImage2D(target, i, GlConst.toGlInternalId(format), width >> i, height >> i, 0, GlConst.toGlExternalId(format), GlConst.toGlType(format), null);
/*     */       }
/*     */     } 
/*     */     
/* 194 */     int error = GlStateManager._getError();
/* 195 */     if (error == 1285)
/* 196 */       throw new GpuOutOfMemoryException("Could not allocate texture of " + width + "x" + height + " for " + label); 
/* 197 */     if (error != 0) {
/* 198 */       throw new IllegalStateException("OpenGL error " + error);
/*     */     }
/*     */     
/* 201 */     GlTexture texture = new GlTexture(usage, label, format, width, height, depthOrLayers, mipLevels, id);
/* 202 */     this.debugLabels.applyLabel(texture);
/* 203 */     return texture;
/*     */   }
/*     */ 
/*     */   
/*     */   public GpuTextureView createTextureView(GpuTexture texture) {
/* 208 */     return createTextureView(texture, 0, texture.getMipLevels());
/*     */   }
/*     */ 
/*     */   
/*     */   public GpuTextureView createTextureView(GpuTexture texture, int baseMipLevel, int mipLevels) {
/* 213 */     if (texture.isClosed()) {
/* 214 */       throw new IllegalArgumentException("Can't create texture view with closed texture");
/*     */     }
/*     */     
/* 217 */     if (baseMipLevel < 0 || baseMipLevel + mipLevels > texture.getMipLevels()) {
/* 218 */       throw new IllegalArgumentException("" + mipLevels + " mip levels starting from " + mipLevels + " would be out of range for texture with only " + baseMipLevel + " mip levels");
/*     */     }
/*     */     
/* 221 */     return new GlTextureView((GlTexture)texture, baseMipLevel, mipLevels);
/*     */   }
/*     */ 
/*     */   
/*     */   public GpuBuffer createBuffer(Supplier<String> label, @GpuBuffer.Usage int usage, long size) {
/* 226 */     if (size <= 0L) {
/* 227 */       throw new IllegalArgumentException("Buffer size must be greater than zero");
/*     */     }
/*     */     
/* 230 */     GlStateManager.clearGlErrors();
/* 231 */     GlBuffer buffer = this.bufferStorage.createBuffer(this.directStateAccess, label, usage, size);
/* 232 */     int error = GlStateManager._getError();
/* 233 */     if (error == 1285)
/* 234 */       throw new GpuOutOfMemoryException("Could not allocate buffer of " + size + " for " + String.valueOf(label)); 
/* 235 */     if (error != 0) {
/* 236 */       throw new IllegalStateException("OpenGL error " + error);
/*     */     }
/* 238 */     this.debugLabels.applyLabel(buffer);
/* 239 */     return buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public GpuBuffer createBuffer(Supplier<String> label, @GpuBuffer.Usage int usage, ByteBuffer data) {
/* 244 */     if (!data.hasRemaining()) {
/* 245 */       throw new IllegalArgumentException("Buffer source must not be empty");
/*     */     }
/*     */     
/* 248 */     GlStateManager.clearGlErrors();
/* 249 */     long size = data.remaining();
/* 250 */     GlBuffer buffer = this.bufferStorage.createBuffer(this.directStateAccess, label, usage, data);
/* 251 */     int error = GlStateManager._getError();
/* 252 */     if (error == 1285)
/* 253 */       throw new GpuOutOfMemoryException("Could not allocate buffer of " + size + " for " + String.valueOf(label)); 
/* 254 */     if (error != 0) {
/* 255 */       throw new IllegalStateException("OpenGL error " + error);
/*     */     }
/* 257 */     this.debugLabels.applyLabel(buffer);
/* 258 */     return buffer;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getImplementationInformation() {
/* 263 */     if (GLFW.glfwGetCurrentContext() == 0L) {
/* 264 */       return "NO CONTEXT";
/*     */     }
/* 266 */     return GlStateManager._getString(7937) + " GL version " + GlStateManager._getString(7937) + ", " + GlStateManager._getString(7938);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getLastDebugMessages() {
/* 271 */     return (this.debugLog == null) ? Collections.<String>emptyList() : this.debugLog.getLastOpenGlDebugMessages();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isDebuggingEnabled() {
/* 276 */     return (this.debugLog != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRenderer() {
/* 281 */     return GlStateManager._getString(7937);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getVendor() {
/* 286 */     return GlStateManager._getString(7936);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getBackendName() {
/* 291 */     return "OpenGL";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getVersion() {
/* 296 */     return GlStateManager._getString(7938);
/*     */   }
/*     */   
/*     */   private static int getMaxSupportedTextureSize() {
/* 300 */     int maxReported = GlStateManager._getInteger(3379);
/* 301 */     for (int texSize = Math.max(32768, maxReported); texSize >= 1024; texSize >>= 1) {
/* 302 */       GlStateManager._texImage2D(32868, 0, 6408, texSize, texSize, 0, 6408, 5121, null);
/* 303 */       int width = GlStateManager._getTexLevelParameter(32868, 0, 4096);
/* 304 */       if (width != 0) {
/* 305 */         return texSize;
/*     */       }
/*     */     } 
/* 308 */     int maxSupportedTextureSize = Math.max(maxReported, 1024);
/* 309 */     LOGGER.info("Failed to determine maximum texture size by probing, trying GL_MAX_TEXTURE_SIZE = {}", maxSupportedTextureSize);
/* 310 */     return maxSupportedTextureSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxTextureSize() {
/* 315 */     return this.maxSupportedTextureSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getUniformOffsetAlignment() {
/* 320 */     return this.uniformOffsetAlignment;
/*     */   }
/*     */ 
/*     */   
/*     */   public void clearPipelineCache() {
/* 325 */     for (GlRenderPipeline pipeline : this.pipelineCache.values()) {
/* 326 */       if (pipeline.program() != GlProgram.INVALID_PROGRAM) {
/* 327 */         pipeline.program().close();
/*     */       }
/*     */     } 
/* 330 */     this.pipelineCache.clear();
/* 331 */     for (GlShaderModule shader : this.shaderCache.values()) {
/* 332 */       if (shader != GlShaderModule.INVALID_SHADER) {
/* 333 */         shader.close();
/*     */       }
/*     */     } 
/* 336 */     this.shaderCache.clear();
/*     */     
/* 338 */     String glRenderer = GlStateManager._getString(7937);
/* 339 */     if (glRenderer.contains("AMD")) {
/* 340 */       sacrificeShaderToOpenGlAndAmd();
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static void sacrificeShaderToOpenGlAndAmd() {
/* 347 */     int shader = GlStateManager.glCreateShader(35633);
/* 348 */     int program = GlStateManager.glCreateProgram();
/* 349 */     GlStateManager.glAttachShader(program, shader);
/* 350 */     GlStateManager.glDeleteShader(shader);
/* 351 */     GlStateManager.glDeleteProgram(program);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<String> getEnabledExtensions() {
/* 356 */     return new ArrayList<>(this.enabledExtensions);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 361 */     clearPipelineCache();
/*     */   }
/*     */   
/*     */   public DirectStateAccess directStateAccess() {
/* 365 */     return this.directStateAccess;
/*     */   }
/*     */   
/*     */   protected GlRenderPipeline getOrCompilePipeline(RenderPipeline pipeline) {
/* 369 */     return this.pipelineCache.computeIfAbsent(pipeline, p -> compilePipeline(p, this.defaultShaderSource));
/*     */   }
/*     */   
/*     */   protected GlShaderModule getOrCompileShader(Identifier id, ShaderType type, ShaderDefines defines, ShaderSource shaderSource) {
/* 373 */     ShaderCompilationKey key = new ShaderCompilationKey(id, type, defines);
/* 374 */     return this.shaderCache.computeIfAbsent(key, k -> compileShader(shaderSource, shaderSource));
/*     */   }
/*     */ 
/*     */   
/*     */   public GlRenderPipeline precompilePipeline(RenderPipeline pipeline, ShaderSource customShaderSource) {
/* 379 */     ShaderSource shaderSource = (customShaderSource == null) ? this.defaultShaderSource : customShaderSource;
/* 380 */     return this.pipelineCache.computeIfAbsent(pipeline, p -> compilePipeline(shaderSource, shaderSource));
/*     */   }
/*     */   
/*     */   private GlShaderModule compileShader(ShaderCompilationKey key, ShaderSource shaderSource) {
/* 384 */     String source = shaderSource.get(key.id, key.type);
/* 385 */     if (source == null) {
/* 386 */       LOGGER.error("Couldn't find source for {} shader ({})", key.type, key.id);
/* 387 */       return GlShaderModule.INVALID_SHADER;
/*     */     } 
/* 389 */     String sourceWithDefines = GlslPreprocessor.injectDefines(source, key.defines);
/* 390 */     int shaderId = GlStateManager.glCreateShader(GlConst.toGl(key.type));
/* 391 */     GlStateManager.glShaderSource(shaderId, sourceWithDefines);
/* 392 */     GlStateManager.glCompileShader(shaderId);
/*     */     
/* 394 */     if (GlStateManager.glGetShaderi(shaderId, 35713) == 0) {
/* 395 */       String logInfo = StringUtils.trim(GlStateManager.glGetShaderInfoLog(shaderId, 32768));
/* 396 */       LOGGER.error("Couldn't compile {} shader ({}): {}", new Object[] { key.type.getName(), key.id, logInfo });
/* 397 */       return GlShaderModule.INVALID_SHADER;
/*     */     } 
/*     */     
/* 400 */     GlShaderModule module = new GlShaderModule(shaderId, key.id, key.type);
/* 401 */     this.debugLabels.applyLabel(module);
/* 402 */     return module;
/*     */   }
/*     */   
/*     */   private GlProgram compileProgram(RenderPipeline pipeline, ShaderSource shaderSource) {
/* 406 */     GlShaderModule vertexShader = getOrCompileShader(pipeline.getVertexShader(), ShaderType.VERTEX, pipeline.getShaderDefines(), shaderSource);
/* 407 */     GlShaderModule fragmentShader = getOrCompileShader(pipeline.getFragmentShader(), ShaderType.FRAGMENT, pipeline.getShaderDefines(), shaderSource);
/* 408 */     if (vertexShader == GlShaderModule.INVALID_SHADER) {
/* 409 */       LOGGER.error("Couldn't compile pipeline {}: vertex shader {} was invalid", pipeline.getLocation(), pipeline.getVertexShader());
/* 410 */       return GlProgram.INVALID_PROGRAM;
/*     */     } 
/* 412 */     if (fragmentShader == GlShaderModule.INVALID_SHADER) {
/* 413 */       LOGGER.error("Couldn't compile pipeline {}: fragment shader {} was invalid", pipeline.getLocation(), pipeline.getFragmentShader());
/* 414 */       return GlProgram.INVALID_PROGRAM;
/*     */     } 
/*     */     try {
/* 417 */       GlProgram compiled = GlProgram.link(vertexShader, fragmentShader, pipeline.getVertexFormat(), pipeline.getLocation().toString());
/* 418 */       compiled.setupUniforms(pipeline.getUniforms(), pipeline.getSamplers());
/* 419 */       this.debugLabels.applyLabel(compiled);
/* 420 */       return compiled;
/* 421 */     } catch (ShaderManager.CompilationException e) {
/* 422 */       LOGGER.error("Couldn't compile program for pipeline {}: {}", pipeline.getLocation(), e);
/* 423 */       return GlProgram.INVALID_PROGRAM;
/*     */     } 
/*     */   }
/*     */   
/*     */   private GlRenderPipeline compilePipeline(RenderPipeline pipeline, ShaderSource shaderSource) {
/* 428 */     return new GlRenderPipeline(pipeline, compileProgram(pipeline, shaderSource));
/*     */   }
/*     */   
/*     */   public VertexArrayCache vertexArrayCache() {
/* 432 */     return this.vertexArrayCache;
/*     */   }
/*     */   
/*     */   public BufferStorage getBufferStorage() {
/* 436 */     return this.bufferStorage;
/*     */   }
/*     */   private static final class ShaderCompilationKey extends Record { private final Identifier id; private final ShaderType type; private final ShaderDefines defines;
/* 439 */     private ShaderCompilationKey(Identifier id, ShaderType type, ShaderDefines defines) { this.id = id; this.type = type; this.defines = defines; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/GlDevice$ShaderCompilationKey;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #439	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 439 */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlDevice$ShaderCompilationKey; } public Identifier id() { return this.id; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/GlDevice$ShaderCompilationKey;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #439	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/opengl/GlDevice$ShaderCompilationKey;
/* 439 */       //   0	8	1	o	Ljava/lang/Object; } public ShaderType type() { return this.type; } public ShaderDefines defines() { return this.defines; }
/*     */     
/*     */     public String toString() {
/* 442 */       String string = String.valueOf(this.id) + " (" + String.valueOf(this.id) + ")";
/* 443 */       if (!this.defines.isEmpty()) {
/* 444 */         return string + " with " + string;
/*     */       }
/* 446 */       return string;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/opengl/GlDevice.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */