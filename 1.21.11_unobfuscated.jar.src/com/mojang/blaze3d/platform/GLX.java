/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import com.google.common.base.Joiner;
/*     */ import com.google.common.collect.Lists;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.LongSupplier;
/*     */ import java.util.function.Supplier;
/*     */ import org.lwjgl.Version;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import org.lwjgl.glfw.GLFWErrorCallback;
/*     */ import org.lwjgl.glfw.GLFWErrorCallbackI;
/*     */ import org.lwjgl.glfw.GLFWVidMode;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.slf4j.Logger;
/*     */ import oshi.SystemInfo;
/*     */ import oshi.hardware.CentralProcessor;
/*     */ 
/*     */ 
/*     */ public class GLX
/*     */ {
/*  25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static int _getRefreshRate(Window window) {
/*  28 */     RenderSystem.assertOnRenderThread();
/*  29 */     long monitor = GLFW.glfwGetWindowMonitor(window.handle());
/*  30 */     if (monitor == 0L) {
/*  31 */       monitor = GLFW.glfwGetPrimaryMonitor();
/*     */     }
/*  33 */     GLFWVidMode videoMode = (monitor == 0L) ? null : GLFW.glfwGetVideoMode(monitor);
/*  34 */     return (videoMode == null) ? 0 : videoMode.refreshRate();
/*     */   }
/*     */   private static String cpuInfo;
/*     */   public static String _getLWJGLVersion() {
/*  38 */     return Version.getVersion();
/*     */   }
/*     */   public static LongSupplier _initGlfw() {
/*     */     LongSupplier timeSource;
/*  42 */     Window.checkGlfwError((error, description) -> {
/*     */           throw new IllegalStateException(String.format(Locale.ROOT, "GLFW error before init: [0x%X]%s", new Object[] { error, description }));
/*     */         });
/*     */     
/*  46 */     List<String> collectedErrors = Lists.newArrayList();
/*     */     
/*  48 */     GLFWErrorCallback prevCallback = GLFW.glfwSetErrorCallback((error, descriptionPtr) -> {
/*     */           String description = (descriptionPtr == 0L) ? "" : MemoryUtil.memUTF8(descriptionPtr);
/*     */           
/*     */           collectedErrors.add(String.format(Locale.ROOT, "GLFW error during init: [0x%X]%s", new Object[] { error, description }));
/*     */         });
/*     */     
/*  54 */     if (GLFW.glfwInit()) {
/*  55 */       timeSource = (() -> (long)(GLFW.glfwGetTime() * 1.0E9D));
/*     */       
/*  57 */       for (String error : collectedErrors) {
/*  58 */         LOGGER.error("GLFW error collected during initialization: {}", error);
/*     */       }
/*     */     } else {
/*  61 */       throw new IllegalStateException("Failed to initialize GLFW, errors: " + Joiner.on(",").join(collectedErrors));
/*     */     } 
/*     */     
/*  64 */     RenderSystem.setErrorCallback((GLFWErrorCallbackI)prevCallback);
/*  65 */     return timeSource;
/*     */   }
/*     */   
/*     */   public static void _setGlfwErrorCallback(GLFWErrorCallbackI onFullscreenError) {
/*  69 */     GLFWErrorCallback previousCallback = GLFW.glfwSetErrorCallback(onFullscreenError);
/*  70 */     if (previousCallback != null) {
/*  71 */       previousCallback.free();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean _shouldClose(Window window) {
/*  77 */     return GLFW.glfwWindowShouldClose(window.handle());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String _getCpuInfo() {
/*  83 */     if (cpuInfo == null) {
/*  84 */       cpuInfo = "<unknown>";
/*     */       try {
/*  86 */         CentralProcessor processor = new SystemInfo().getHardware().getProcessor();
/*  87 */         cpuInfo = String.format(Locale.ROOT, "%dx %s", new Object[] { processor.getLogicalProcessorCount(), processor.getProcessorIdentifier().getName() }).replaceAll("\\s+", " ");
/*  88 */       } catch (Throwable throwable) {}
/*     */     } 
/*     */     
/*  91 */     return cpuInfo;
/*     */   }
/*     */   
/*     */   public static <T> T make(Supplier<T> factory) {
/*  95 */     return factory.get();
/*     */   }
/*     */   
/*     */   public static <T> T make(T t, Consumer<T> consumer) {
/*  99 */     consumer.accept(t);
/* 100 */     return t;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/platform/GLX.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */