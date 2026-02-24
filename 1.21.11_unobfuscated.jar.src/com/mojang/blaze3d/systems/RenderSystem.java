/*     */ package com.mojang.blaze3d.systems;
/*     */ 
/*     */ import com.mojang.blaze3d.ProjectionType;
/*     */ import com.mojang.blaze3d.TracyFrameCapture;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBufferSlice;
/*     */ import com.mojang.blaze3d.buffers.GpuFence;
/*     */ import com.mojang.blaze3d.buffers.Std140SizeCalculator;
/*     */ import com.mojang.blaze3d.opengl.GlDevice;
/*     */ import com.mojang.blaze3d.platform.GLX;
/*     */ import com.mojang.blaze3d.platform.Window;
/*     */ import com.mojang.blaze3d.shaders.ShaderSource;
/*     */ import com.mojang.blaze3d.textures.GpuTextureView;
/*     */ import com.mojang.blaze3d.vertex.Tesselator;
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Locale;
/*     */ import java.util.Objects;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.concurrent.atomic.AtomicLong;
/*     */ import java.util.function.IntConsumer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.renderer.DynamicUniforms;
/*     */ import net.minecraft.util.ArrayListDeque;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.TimeSource;
/*     */ import net.minecraft.util.Util;
/*     */ import org.joml.Matrix4f;
/*     */ import org.joml.Matrix4fStack;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import org.lwjgl.glfw.GLFWErrorCallbackI;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class RenderSystem
/*     */ {
/*  38 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int MINIMUM_ATLAS_TEXTURE_SIZE = 1024;
/*  41 */   public static final int PROJECTION_MATRIX_UBO_SIZE = new Std140SizeCalculator().putMat4f().get();
/*     */   
/*     */   private static Thread renderThread;
/*     */   
/*     */   private static GpuDevice DEVICE;
/*     */   
/*  47 */   private static double lastDrawTime = Double.MIN_VALUE;
/*     */   
/*  49 */   private static final AutoStorageIndexBuffer sharedSequential = new AutoStorageIndexBuffer(1, 1, IntConsumer::accept); private static final AutoStorageIndexBuffer sharedSequentialQuad; private static final AutoStorageIndexBuffer sharedSequentialLines; static {
/*  50 */     sharedSequentialQuad = new AutoStorageIndexBuffer(4, 6, (c, i) -> {
/*     */           c.accept(i);
/*     */           c.accept(i + 1);
/*     */           c.accept(i + 2);
/*     */           c.accept(i + 2);
/*     */           c.accept(i + 3);
/*     */           c.accept(i);
/*     */         });
/*  58 */     sharedSequentialLines = new AutoStorageIndexBuffer(4, 6, (c, i) -> {
/*     */           c.accept(i);
/*     */           c.accept(i + 1);
/*     */           c.accept(i + 2);
/*     */           c.accept(i + 3);
/*     */           c.accept(i + 2);
/*     */           c.accept(i + 1);
/*     */         });
/*     */   }
/*  67 */   private static ProjectionType projectionType = ProjectionType.PERSPECTIVE;
/*  68 */   private static ProjectionType savedProjectionType = ProjectionType.PERSPECTIVE;
/*     */   
/*  70 */   private static final Matrix4fStack modelViewStack = new Matrix4fStack(16);
/*     */   
/*  72 */   private static GpuBufferSlice shaderFog = null;
/*     */   
/*     */   private static GpuBufferSlice shaderLightDirections;
/*     */   
/*     */   private static GpuBufferSlice projectionMatrixBuffer;
/*     */   
/*     */   private static GpuBufferSlice savedProjectionMatrixBuffer;
/*     */   
/*  80 */   private static String apiDescription = "Unknown";
/*     */   
/*  82 */   private static final AtomicLong pollEventsWaitStart = new AtomicLong();
/*  83 */   private static final AtomicBoolean pollingEvents = new AtomicBoolean(false);
/*     */   
/*  85 */   private static final ArrayListDeque<GpuAsyncTask> PENDING_FENCES = new ArrayListDeque();
/*     */   
/*     */   public static GpuTextureView outputColorTextureOverride;
/*     */   
/*     */   public static GpuTextureView outputDepthTextureOverride;
/*     */   private static GpuBuffer globalSettingsUniform;
/*     */   private static DynamicUniforms dynamicUniforms;
/*  92 */   private static final ScissorState scissorStateForRenderTypeDraws = new ScissorState();
/*  93 */   private static SamplerCache samplerCache = new SamplerCache();
/*     */   
/*     */   public static SamplerCache getSamplerCache() {
/*  96 */     return samplerCache;
/*     */   }
/*     */   
/*     */   public static void initRenderThread() {
/* 100 */     if (renderThread != null) {
/* 101 */       throw new IllegalStateException("Could not initialize render thread");
/*     */     }
/* 103 */     renderThread = Thread.currentThread();
/*     */   }
/*     */   
/*     */   public static boolean isOnRenderThread() {
/* 107 */     return (Thread.currentThread() == renderThread);
/*     */   }
/*     */   
/*     */   public static void assertOnRenderThread() {
/* 111 */     if (!isOnRenderThread()) {
/* 112 */       throw constructThreadException();
/*     */     }
/*     */   }
/*     */   
/*     */   private static IllegalStateException constructThreadException() {
/* 117 */     return new IllegalStateException("Rendersystem called from wrong thread");
/*     */   }
/*     */   
/*     */   private static void pollEvents() {
/* 121 */     pollEventsWaitStart.set(Util.getMillis());
/* 122 */     pollingEvents.set(true);
/* 123 */     GLFW.glfwPollEvents();
/*     */     
/* 125 */     pollingEvents.set(false);
/*     */   }
/*     */   
/*     */   public static boolean isFrozenAtPollEvents() {
/* 129 */     return (pollingEvents.get() && Util.getMillis() - pollEventsWaitStart.get() > 200L);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void flipFrame(Window window, TracyFrameCapture tracyFrameCapture) {
/* 134 */     pollEvents();
/*     */ 
/*     */     
/* 137 */     Tesselator.getInstance().clear();
/*     */     
/* 139 */     GLFW.glfwSwapBuffers(window.handle());
/* 140 */     if (tracyFrameCapture != null) {
/* 141 */       tracyFrameCapture.endFrame();
/*     */     }
/* 143 */     dynamicUniforms.reset();
/* 144 */     (Minecraft.getInstance()).levelRenderer.endFrame();
/* 145 */     pollEvents();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void limitDisplayFPS(int framerateLimit) {
/* 151 */     double targetTime = lastDrawTime + 1.0D / framerateLimit;
/* 152 */     double drawTime = GLFW.glfwGetTime();
/* 153 */     while (drawTime < targetTime) {
/* 154 */       GLFW.glfwWaitEventsTimeout(targetTime - drawTime);
/* 155 */       drawTime = GLFW.glfwGetTime();
/*     */     } 
/* 157 */     lastDrawTime = drawTime;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setShaderFog(GpuBufferSlice fog) {
/* 163 */     shaderFog = fog;
/*     */   }
/*     */   
/*     */   public static GpuBufferSlice getShaderFog() {
/* 167 */     return shaderFog;
/*     */   }
/*     */   
/*     */   public static void setShaderLights(GpuBufferSlice buffer) {
/* 171 */     shaderLightDirections = buffer;
/*     */   }
/*     */   
/*     */   public static GpuBufferSlice getShaderLights() {
/* 175 */     return shaderLightDirections;
/*     */   }
/*     */   
/*     */   public static void enableScissorForRenderTypeDraws(int x, int y, int width, int height) {
/* 179 */     scissorStateForRenderTypeDraws.enable(x, y, width, height);
/*     */   }
/*     */   
/*     */   public static void disableScissorForRenderTypeDraws() {
/* 183 */     scissorStateForRenderTypeDraws.disable();
/*     */   }
/*     */   
/*     */   public static ScissorState getScissorStateForRenderTypeDraws() {
/* 187 */     return scissorStateForRenderTypeDraws;
/*     */   }
/*     */   
/*     */   public static String getBackendDescription() {
/* 191 */     return String.format(Locale.ROOT, "LWJGL version %s", new Object[] { GLX._getLWJGLVersion() });
/*     */   }
/*     */   
/*     */   public static String getApiDescription() {
/* 195 */     return apiDescription;
/*     */   }
/*     */   
/*     */   public static TimeSource.NanoTimeSource initBackendSystem() {
/* 199 */     Objects.requireNonNull(GLX._initGlfw()); return GLX._initGlfw()::getAsLong;
/*     */   }
/*     */   
/*     */   public static void initRenderer(long windowHandle, int logVerbosity, boolean synchronousLogs, ShaderSource shaderSource, boolean wantsDebugLabels) {
/* 203 */     DEVICE = (GpuDevice)new GlDevice(windowHandle, logVerbosity, synchronousLogs, shaderSource, wantsDebugLabels);
/* 204 */     apiDescription = getDevice().getImplementationInformation();
/* 205 */     dynamicUniforms = new DynamicUniforms();
/* 206 */     samplerCache.initialize();
/*     */   }
/*     */   
/*     */   public static void setErrorCallback(GLFWErrorCallbackI onFullscreenError) {
/* 210 */     GLX._setGlfwErrorCallback(onFullscreenError);
/*     */   }
/*     */   
/*     */   public static void setupDefaultState() {
/* 214 */     modelViewStack.clear();
/*     */   }
/*     */   
/*     */   public static void setProjectionMatrix(GpuBufferSlice projectionMatrixBuffer, ProjectionType type) {
/* 218 */     assertOnRenderThread();
/* 219 */     RenderSystem.projectionMatrixBuffer = projectionMatrixBuffer;
/* 220 */     projectionType = type;
/*     */   }
/*     */   
/*     */   public static void backupProjectionMatrix() {
/* 224 */     assertOnRenderThread();
/* 225 */     savedProjectionMatrixBuffer = projectionMatrixBuffer;
/* 226 */     savedProjectionType = projectionType;
/*     */   }
/*     */   
/*     */   public static void restoreProjectionMatrix() {
/* 230 */     assertOnRenderThread();
/* 231 */     projectionMatrixBuffer = savedProjectionMatrixBuffer;
/* 232 */     projectionType = savedProjectionType;
/*     */   }
/*     */   
/*     */   public static GpuBufferSlice getProjectionMatrixBuffer() {
/* 236 */     assertOnRenderThread();
/* 237 */     return projectionMatrixBuffer;
/*     */   }
/*     */   
/*     */   public static Matrix4f getModelViewMatrix() {
/* 241 */     assertOnRenderThread();
/* 242 */     return (Matrix4f)modelViewStack;
/*     */   }
/*     */   
/*     */   public static Matrix4fStack getModelViewStack() {
/* 246 */     assertOnRenderThread();
/* 247 */     return modelViewStack;
/*     */   }
/*     */   
/*     */   public static AutoStorageIndexBuffer getSequentialBuffer(VertexFormat.Mode primitiveMode) {
/* 251 */     assertOnRenderThread();
/* 252 */     switch (primitiveMode) { case QUADS: case LINES: default: break; }  return 
/*     */ 
/*     */       
/* 255 */       sharedSequential;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setGlobalSettingsUniform(GpuBuffer buffer) {
/* 260 */     globalSettingsUniform = buffer;
/*     */   }
/*     */   
/*     */   public static GpuBuffer getGlobalSettingsUniform() {
/* 264 */     return globalSettingsUniform;
/*     */   }
/*     */   
/*     */   public static ProjectionType getProjectionType() {
/* 268 */     assertOnRenderThread();
/* 269 */     return projectionType;
/*     */   }
/*     */   
/*     */   public static void queueFencedTask(Runnable task) {
/* 273 */     PENDING_FENCES.addLast(new GpuAsyncTask(task, getDevice().createCommandEncoder().createFence()));
/*     */   }
/*     */ 
/*     */   
/*     */   public static void executePendingTasks() {
/* 278 */     GpuAsyncTask task = (GpuAsyncTask)PENDING_FENCES.peekFirst();
/* 279 */     while (task != null) {
/* 280 */       if (task.fence.awaitCompletion(0L)) {
/*     */         try {
/* 282 */           task.callback.run();
/*     */         } finally {
/* 284 */           task.fence.close();
/*     */         } 
/* 286 */         PENDING_FENCES.removeFirst();
/* 287 */         task = (GpuAsyncTask)PENDING_FENCES.peekFirst();
/*     */         continue;
/*     */       } 
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static GpuDevice getDevice() {
/* 296 */     if (DEVICE == null) {
/* 297 */       throw new IllegalStateException("Can't getDevice() before it was initialized");
/*     */     }
/* 299 */     return DEVICE;
/*     */   }
/*     */   
/*     */   public static GpuDevice tryGetDevice() {
/* 303 */     return DEVICE;
/*     */   }
/*     */   
/*     */   public static DynamicUniforms getDynamicUniforms() {
/* 307 */     if (dynamicUniforms == null) {
/* 308 */       throw new IllegalStateException("Can't getDynamicUniforms() before device was initialized");
/*     */     }
/* 310 */     return dynamicUniforms;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void bindDefaultUniforms(RenderPass renderPass) {
/* 315 */     GpuBufferSlice projectionMatrix = getProjectionMatrixBuffer();
/* 316 */     if (projectionMatrix != null) {
/* 317 */       renderPass.setUniform("Projection", projectionMatrix);
/*     */     }
/* 319 */     GpuBufferSlice fog = getShaderFog();
/* 320 */     if (fog != null) {
/* 321 */       renderPass.setUniform("Fog", fog);
/*     */     }
/* 323 */     GpuBuffer globalUniform = getGlobalSettingsUniform();
/* 324 */     if (globalUniform != null) {
/* 325 */       renderPass.setUniform("Globals", globalUniform);
/*     */     }
/* 327 */     GpuBufferSlice shaderLights = getShaderLights();
/* 328 */     if (shaderLights != null) {
/* 329 */       renderPass.setUniform("Lighting", shaderLights);
/*     */     }
/*     */   }
/*     */   
/*     */   public static final class AutoStorageIndexBuffer
/*     */   {
/*     */     private final int vertexStride;
/*     */     private final int indexStride;
/*     */     private final IndexGenerator generator;
/*     */     private GpuBuffer buffer;
/* 339 */     private VertexFormat.IndexType type = VertexFormat.IndexType.SHORT;
/*     */     private int indexCount;
/*     */     
/*     */     private AutoStorageIndexBuffer(int vertexStride, int indexStride, IndexGenerator generator) {
/* 343 */       this.vertexStride = vertexStride;
/* 344 */       this.indexStride = indexStride;
/* 345 */       this.generator = generator;
/*     */     }
/*     */     
/*     */     public boolean hasStorage(int indexCount) {
/* 349 */       return (indexCount <= this.indexCount);
/*     */     }
/*     */     
/*     */     public GpuBuffer getBuffer(int indexCount) {
/* 353 */       ensureStorage(indexCount);
/* 354 */       return this.buffer;
/*     */     }
/*     */     
/*     */     private void ensureStorage(int indexCount) {
/* 358 */       if (hasStorage(indexCount)) {
/*     */         return;
/*     */       }
/*     */       
/* 362 */       indexCount = Mth.roundToward(indexCount * 2, this.indexStride);
/* 363 */       RenderSystem.LOGGER.debug("Growing IndexBuffer: Old limit {}, new limit {}.", this.indexCount, indexCount);
/*     */       
/* 365 */       int primitiveCount = indexCount / this.indexStride;
/* 366 */       int vertexCount = primitiveCount * this.vertexStride;
/*     */       
/* 368 */       VertexFormat.IndexType type = VertexFormat.IndexType.least(vertexCount);
/* 369 */       int bufferSize = Mth.roundToward(indexCount * type.bytes, 4);
/*     */       
/* 371 */       ByteBuffer data = MemoryUtil.memAlloc(bufferSize);
/*     */       
/*     */       try {
/* 374 */         this.type = type;
/*     */         
/* 376 */         it.unimi.dsi.fastutil.ints.IntConsumer intConsumer = intConsumer(data);
/* 377 */         for (int ii = 0; ii < indexCount; ii += this.indexStride) {
/* 378 */           this.generator.accept(intConsumer, ii * this.vertexStride / this.indexStride);
/*     */         }
/* 380 */         data.flip();
/*     */         
/* 382 */         if (this.buffer != null) {
/* 383 */           this.buffer.close();
/*     */         }
/* 385 */         this.buffer = RenderSystem.getDevice().createBuffer(() -> "Auto Storage index buffer", 64, data);
/*     */       } finally {
/* 387 */         MemoryUtil.memFree(data);
/*     */       } 
/*     */       
/* 390 */       this.indexCount = indexCount;
/*     */     }
/*     */     
/*     */     private it.unimi.dsi.fastutil.ints.IntConsumer intConsumer(ByteBuffer buffer) {
/* 394 */       switch (this.type) {
/*     */         case SHORT:
/* 396 */           return value -> buffer.putShort((short)value);
/*     */       } 
/*     */       
/* 399 */       Objects.requireNonNull(buffer); return buffer::putInt;
/*     */     }
/*     */ 
/*     */     
/*     */     public VertexFormat.IndexType type() {
/* 404 */       return this.type;
/*     */     }
/*     */     private static interface IndexGenerator { void accept(it.unimi.dsi.fastutil.ints.IntConsumer param2IntConsumer, int param2Int); }
/*     */   }
/*     */   
/*     */   static final class GpuAsyncTask extends Record { private final Runnable callback;
/*     */     private final GpuFence fence;
/*     */     
/* 412 */     GpuAsyncTask(Runnable callback, GpuFence fence) { this.callback = callback; this.fence = fence; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/systems/RenderSystem$GpuAsyncTask;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #412	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 412 */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderSystem$GpuAsyncTask; } public Runnable callback() { return this.callback; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/systems/RenderSystem$GpuAsyncTask;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #412	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/systems/RenderSystem$GpuAsyncTask; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/systems/RenderSystem$GpuAsyncTask;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #412	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/systems/RenderSystem$GpuAsyncTask;
/* 412 */       //   0	8	1	o	Ljava/lang/Object; } public GpuFence fence() { return this.fence; }
/*     */      }
/*     */ 
/*     */   
/*     */   private static interface IndexGenerator {
/*     */     void accept(it.unimi.dsi.fastutil.ints.IntConsumer param1IntConsumer, int param1Int);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/systems/RenderSystem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */