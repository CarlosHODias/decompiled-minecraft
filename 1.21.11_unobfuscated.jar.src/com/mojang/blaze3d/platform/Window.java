/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import com.mojang.blaze3d.TracyFrameCapture;
/*     */ import com.mojang.blaze3d.platform.cursor.CursorType;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.client.main.SilentInitException;
/*     */ import net.minecraft.server.packs.PackResources;
/*     */ import net.minecraft.server.packs.resources.IoSupplier;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ import org.lwjgl.glfw.Callbacks;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import org.lwjgl.glfw.GLFWErrorCallback;
/*     */ import org.lwjgl.glfw.GLFWErrorCallbackI;
/*     */ import org.lwjgl.glfw.GLFWImage;
/*     */ import org.lwjgl.glfw.GLFWWindowCloseCallback;
/*     */ import org.lwjgl.system.MemoryStack;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ import org.lwjgl.util.tinyfd.TinyFileDialogs;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public final class Window
/*     */   implements AutoCloseable
/*     */ {
/*  36 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final int BASE_WIDTH = 320;
/*     */   
/*     */   public static final int BASE_HEIGHT = 240;
/*  41 */   private final GLFWErrorCallback defaultErrorCallback = GLFWErrorCallback.create(this::defaultErrorCallback);
/*     */   
/*     */   private final WindowEventHandler eventHandler;
/*     */   private final ScreenManager screenManager;
/*     */   private final long handle;
/*     */   private int windowedX;
/*     */   private int windowedY;
/*     */   private int windowedWidth;
/*     */   private int windowedHeight;
/*     */   private Optional<VideoMode> preferredFullscreenVideoMode;
/*     */   private boolean fullscreen;
/*     */   private boolean actuallyFullscreen;
/*     */   private int x;
/*     */   private int y;
/*     */   private int width;
/*     */   private int height;
/*     */   private int framebufferWidth;
/*     */   private int framebufferHeight;
/*     */   private int guiScaledWidth;
/*     */   private int guiScaledHeight;
/*     */   private int guiScale;
/*  62 */   private String errorSection = "";
/*     */   
/*     */   private boolean dirty;
/*     */   
/*     */   private boolean vsync;
/*     */   
/*     */   private boolean iconified;
/*     */   
/*     */   private boolean minimized;
/*     */   private boolean allowCursorChanges;
/*  72 */   private CursorType currentCursor = CursorType.DEFAULT;
/*     */   
/*     */   public Window(WindowEventHandler eventHandler, ScreenManager screenManager, DisplayData displayData, String fullscreenVideoModeString, String title) {
/*  75 */     this.screenManager = screenManager;
/*     */     
/*  77 */     setBootErrorCallback();
/*  78 */     setErrorSection("Pre startup");
/*     */     
/*  80 */     this.eventHandler = eventHandler;
/*     */     
/*  82 */     Optional<VideoMode> optionsMode = VideoMode.read(fullscreenVideoModeString);
/*  83 */     if (optionsMode.isPresent()) {
/*  84 */       this.preferredFullscreenVideoMode = optionsMode;
/*  85 */     } else if (displayData.fullscreenWidth().isPresent() && displayData.fullscreenHeight().isPresent()) {
/*  86 */       this.preferredFullscreenVideoMode = Optional.of(new VideoMode(displayData.fullscreenWidth().getAsInt(), displayData.fullscreenHeight().getAsInt(), 8, 8, 8, 60));
/*     */     } else {
/*  88 */       this.preferredFullscreenVideoMode = Optional.empty();
/*     */     } 
/*  90 */     this.actuallyFullscreen = this.fullscreen = displayData.isFullscreen();
/*     */ 
/*     */     
/*  93 */     Monitor initialMonitor = screenManager.getMonitor(GLFW.glfwGetPrimaryMonitor());
/*     */     
/*  95 */     this.windowedWidth = this.width = Math.max(displayData.width(), 1);
/*  96 */     this.windowedHeight = this.height = Math.max(displayData.height(), 1);
/*     */     
/*  98 */     GLFW.glfwDefaultWindowHints();
/*     */     
/* 100 */     GLFW.glfwWindowHint(139265, 196609);
/* 101 */     GLFW.glfwWindowHint(139275, 221185);
/* 102 */     GLFW.glfwWindowHint(139266, 3);
/* 103 */     GLFW.glfwWindowHint(139267, 3);
/* 104 */     GLFW.glfwWindowHint(139272, 204801);
/* 105 */     GLFW.glfwWindowHint(139270, 1);
/*     */     
/* 107 */     this.handle = GLFW.glfwCreateWindow(this.width, this.height, title, (this.fullscreen && initialMonitor != null) ? initialMonitor.getMonitor() : 0L, 0L);
/*     */     
/* 109 */     if (initialMonitor != null) {
/* 110 */       VideoMode mode = initialMonitor.getPreferredVidMode(this.fullscreen ? this.preferredFullscreenVideoMode : Optional.<VideoMode>empty());
/* 111 */       this.windowedX = this.x = initialMonitor.getX() + mode.getWidth() / 2 - this.width / 2;
/* 112 */       this.windowedY = this.y = initialMonitor.getY() + mode.getHeight() / 2 - this.height / 2;
/*     */     } else {
/* 114 */       int[] actualX = new int[1];
/* 115 */       int[] actualY = new int[1];
/* 116 */       GLFW.glfwGetWindowPos(this.handle, actualX, actualY);
/* 117 */       this.windowedX = this.x = actualX[0];
/* 118 */       this.windowedY = this.y = actualY[0];
/*     */     } 
/*     */     
/* 121 */     setMode();
/*     */     
/* 123 */     refreshFramebufferSize();
/*     */     
/* 125 */     GLFW.glfwSetFramebufferSizeCallback(this.handle, this::onFramebufferResize);
/* 126 */     GLFW.glfwSetWindowPosCallback(this.handle, this::onMove);
/* 127 */     GLFW.glfwSetWindowSizeCallback(this.handle, this::onResize);
/* 128 */     GLFW.glfwSetWindowFocusCallback(this.handle, this::onFocus);
/* 129 */     GLFW.glfwSetCursorEnterCallback(this.handle, this::onEnter);
/* 130 */     GLFW.glfwSetWindowIconifyCallback(this.handle, this::onIconify);
/*     */   }
/*     */   
/*     */   public static String getPlatform() {
/* 134 */     int platform = GLFW.glfwGetPlatform();
/* 135 */     switch (platform) { case 0: case 393217: case 393218: case 393219: case 393220: case 393221: default: break; }  return 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 142 */       String.format(Locale.ROOT, "unknown (%08X)", new Object[] { platform });
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRefreshRate() {
/* 147 */     RenderSystem.assertOnRenderThread();
/* 148 */     return GLX._getRefreshRate(this);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean shouldClose() {
/* 153 */     return GLX._shouldClose(this);
/*     */   }
/*     */   
/*     */   public static void checkGlfwError(BiConsumer<Integer, String> errorConsumer) {
/* 157 */     MemoryStack stack = MemoryStack.stackPush(); 
/* 158 */     try { PointerBuffer errorDescription = stack.mallocPointer(1);
/* 159 */       int errorCode = GLFW.glfwGetError(errorDescription);
/* 160 */       if (errorCode != 0) {
/* 161 */         long errorDescriptionAddress = errorDescription.get();
/* 162 */         String errorMessage = (errorDescriptionAddress == 0L) ? "" : MemoryUtil.memUTF8(errorDescriptionAddress);
/* 163 */         errorConsumer.accept(errorCode, errorMessage);
/*     */       } 
/* 165 */       if (stack != null) stack.close();  }
/*     */     catch (Throwable throwable) { if (stack != null)
/*     */         try { stack.close(); }
/*     */         catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
/*     */           throw throwable; }
/* 170 */      } public void setIcon(PackResources resources, IconSet iconSet) throws IOException { List<IoSupplier<InputStream>> iconStreams; List<ByteBuffer> allocatedBuffers; int platform = GLFW.glfwGetPlatform();
/* 171 */     switch (platform) { case 393217:
/*     */       case 393220:
/* 173 */         iconStreams = iconSet.getStandardIcons(resources);
/* 174 */         allocatedBuffers = new ArrayList<>(iconStreams.size()); 
/* 175 */         try { MemoryStack stack = MemoryStack.stackPush(); 
/* 176 */           try { GLFWImage.Buffer icons = GLFWImage.malloc(iconStreams.size(), stack);
/* 177 */             for (int i = 0; i < iconStreams.size(); i++) {
/* 178 */               NativeImage image = NativeImage.read((InputStream)((IoSupplier)iconStreams.get(i)).get()); 
/* 179 */               try { ByteBuffer pixels = MemoryUtil.memAlloc(image.getWidth() * image.getHeight() * 4);
/* 180 */                 allocatedBuffers.add(pixels);
/* 181 */                 pixels.asIntBuffer().put(image.getPixelsABGR());
/*     */                 
/* 183 */                 icons.position(i);
/* 184 */                 icons.width(image.getWidth());
/* 185 */                 icons.height(image.getHeight());
/* 186 */                 icons.pixels(pixels);
/* 187 */                 if (image != null) image.close();  } catch (Throwable throwable) { if (image != null)
/*     */                   try { image.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }
/*     */             
/* 190 */             }  GLFW.glfwSetWindowIcon(this.handle, (GLFWImage.Buffer)icons.position(0));
/* 191 */             if (stack != null) stack.close();  } catch (Throwable throwable) { if (stack != null)
/* 192 */               try { stack.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } finally { allocatedBuffers.forEach(MemoryUtil::memFree); }
/*     */          break;
/*     */       case 393218:
/* 195 */         MacosUtil.loadIcon(iconSet.getMacIcon(resources)); break;
/*     */       case 393219: case 393221:
/*     */         break;
/*     */       default:
/* 199 */         LOGGER.warn("Not setting icon for unrecognized platform: {}", platform);
/*     */         break; }
/*     */      }
/*     */   
/*     */   public void setErrorSection(String string) {
/* 204 */     this.errorSection = string;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void setBootErrorCallback() {
/* 210 */     GLFW.glfwSetErrorCallback(Window::bootCrash);
/*     */   }
/*     */   
/*     */   private static void bootCrash(int error, long description) {
/* 214 */     String message = "GLFW error " + error + ": " + MemoryUtil.memUTF8(description);
/* 215 */     TinyFileDialogs.tinyfd_messageBox("Minecraft", message + ".\n\nPlease make sure you have up-to-date drivers (see aka.ms/mcdriver for instructions).", "ok", "error", false);
/* 216 */     throw new WindowInitFailed(message);
/*     */   }
/*     */   
/*     */   public void defaultErrorCallback(int errorCode, long description) {
/* 220 */     RenderSystem.assertOnRenderThread();
/* 221 */     String errorString = MemoryUtil.memUTF8(description);
/* 222 */     LOGGER.error("########## GL ERROR ##########");
/* 223 */     LOGGER.error("@ {}", this.errorSection);
/* 224 */     LOGGER.error("{}: {}", errorCode, errorString);
/*     */   }
/*     */   
/*     */   public void setDefaultErrorCallback() {
/* 228 */     GLFWErrorCallback previousCallback = GLFW.glfwSetErrorCallback((GLFWErrorCallbackI)this.defaultErrorCallback);
/* 229 */     if (previousCallback != null) {
/* 230 */       previousCallback.free();
/*     */     }
/*     */   }
/*     */   
/*     */   public void updateVsync(boolean enableVsync) {
/* 235 */     RenderSystem.assertOnRenderThread();
/* 236 */     this.vsync = enableVsync;
/* 237 */     GLFW.glfwSwapInterval(enableVsync ? 1 : 0);
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/* 242 */     RenderSystem.assertOnRenderThread();
/* 243 */     Callbacks.glfwFreeCallbacks(this.handle);
/* 244 */     this.defaultErrorCallback.close();
/* 245 */     GLFW.glfwDestroyWindow(this.handle);
/* 246 */     GLFW.glfwTerminate();
/*     */   }
/*     */   
/*     */   private void onMove(long handle, int x, int y) {
/* 250 */     this.x = x;
/* 251 */     this.y = y;
/*     */   }
/*     */   
/*     */   private void onFramebufferResize(long handle, int newWidth, int newHeight) {
/* 255 */     if (handle != this.handle) {
/*     */       return;
/*     */     }
/* 258 */     int oldWidth = getWidth();
/* 259 */     int oldHeight = getHeight();
/*     */     
/* 261 */     if (newWidth == 0 || newHeight == 0) {
/* 262 */       this.minimized = true;
/*     */       return;
/*     */     } 
/* 265 */     this.minimized = false;
/*     */     
/* 267 */     this.framebufferWidth = newWidth;
/* 268 */     this.framebufferHeight = newHeight;
/* 269 */     if (getWidth() != oldWidth || getHeight() != oldHeight) {
/*     */       try {
/* 271 */         this.eventHandler.resizeDisplay();
/* 272 */       } catch (Exception e) {
/* 273 */         CrashReport report = CrashReport.forThrowable(e, "Window resize");
/* 274 */         CrashReportCategory windowSizeDetails = report.addCategory("Window Dimensions");
/* 275 */         windowSizeDetails.setDetail("Old", "" + oldWidth + "x" + oldWidth);
/* 276 */         windowSizeDetails.setDetail("New", "" + newWidth + "x" + newWidth);
/* 277 */         throw new ReportedException(report);
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void refreshFramebufferSize() {
/* 283 */     int[] outWidth = new int[1];
/* 284 */     int[] outHeight = new int[1];
/* 285 */     GLFW.glfwGetFramebufferSize(this.handle, outWidth, outHeight);
/*     */     
/* 287 */     this.framebufferWidth = (outWidth[0] > 0) ? outWidth[0] : 1;
/* 288 */     this.framebufferHeight = (outHeight[0] > 0) ? outHeight[0] : 1;
/*     */   }
/*     */   
/*     */   private void onResize(long handle, int newWidth, int newHeight) {
/* 292 */     this.width = newWidth;
/* 293 */     this.height = newHeight;
/*     */   }
/*     */   
/*     */   private void onFocus(long handle, boolean focused) {
/* 297 */     if (handle == this.handle) {
/* 298 */       this.eventHandler.setWindowActive(focused);
/*     */     }
/*     */   }
/*     */   
/*     */   private void onEnter(long handle, boolean entered) {
/* 303 */     if (entered) {
/* 304 */       this.eventHandler.cursorEntered();
/*     */     }
/*     */   }
/*     */   
/*     */   private void onIconify(long handle, boolean iconified) {
/* 309 */     this.iconified = iconified;
/*     */   }
/*     */   
/*     */   public void updateDisplay(TracyFrameCapture tracyFrameCapture) {
/* 313 */     RenderSystem.flipFrame(this, tracyFrameCapture);
/* 314 */     if (this.fullscreen != this.actuallyFullscreen) {
/* 315 */       this.actuallyFullscreen = this.fullscreen;
/* 316 */       updateFullscreen(this.vsync, tracyFrameCapture);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Optional<VideoMode> getPreferredFullscreenVideoMode() {
/* 321 */     return this.preferredFullscreenVideoMode;
/*     */   }
/*     */   
/*     */   public void setPreferredFullscreenVideoMode(Optional<VideoMode> preferredFullscreenVideoMode) {
/* 325 */     boolean changed = !preferredFullscreenVideoMode.equals(this.preferredFullscreenVideoMode);
/* 326 */     this.preferredFullscreenVideoMode = preferredFullscreenVideoMode;
/* 327 */     if (changed) {
/* 328 */       this.dirty = true;
/*     */     }
/*     */   }
/*     */   
/*     */   public void changeFullscreenVideoMode() {
/* 333 */     if (this.fullscreen && this.dirty) {
/* 334 */       this.dirty = false;
/* 335 */       setMode();
/* 336 */       this.eventHandler.resizeDisplay();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void setMode() {
/* 343 */     boolean wasFullscreen = (GLFW.glfwGetWindowMonitor(this.handle) != 0L);
/* 344 */     if (this.fullscreen) {
/* 345 */       Monitor monitor = this.screenManager.findBestMonitor(this);
/* 346 */       if (monitor == null) {
/* 347 */         LOGGER.warn("Failed to find suitable monitor for fullscreen mode");
/* 348 */         this.fullscreen = false;
/*     */       } else {
/* 350 */         if (MacosUtil.IS_MACOS)
/*     */         {
/* 352 */           MacosUtil.exitNativeFullscreen(this);
/*     */         }
/* 354 */         VideoMode mode = monitor.getPreferredVidMode(this.preferredFullscreenVideoMode);
/* 355 */         if (!wasFullscreen) {
/* 356 */           this.windowedX = this.x;
/* 357 */           this.windowedY = this.y;
/* 358 */           this.windowedWidth = this.width;
/* 359 */           this.windowedHeight = this.height;
/*     */         } 
/* 361 */         this.x = 0;
/* 362 */         this.y = 0;
/* 363 */         this.width = mode.getWidth();
/* 364 */         this.height = mode.getHeight();
/* 365 */         GLFW.glfwSetWindowMonitor(this.handle, monitor.getMonitor(), this.x, this.y, this.width, this.height, mode.getRefreshRate());
/* 366 */         if (MacosUtil.IS_MACOS)
/*     */         {
/* 368 */           MacosUtil.clearResizableBit(this);
/*     */         }
/*     */       } 
/*     */     } else {
/* 372 */       this.x = this.windowedX;
/* 373 */       this.y = this.windowedY;
/* 374 */       this.width = this.windowedWidth;
/* 375 */       this.height = this.windowedHeight;
/* 376 */       GLFW.glfwSetWindowMonitor(this.handle, 0L, this.x, this.y, this.width, this.height, -1);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void toggleFullScreen() {
/* 381 */     this.fullscreen = !this.fullscreen;
/*     */   }
/*     */   
/*     */   public void setWindowed(int width, int height) {
/* 385 */     this.windowedWidth = width;
/* 386 */     this.windowedHeight = height;
/* 387 */     this.fullscreen = false;
/* 388 */     setMode();
/*     */   }
/*     */   
/*     */   private void updateFullscreen(boolean enableVsync, TracyFrameCapture tracyFrameCapture) {
/* 392 */     RenderSystem.assertOnRenderThread();
/*     */     try {
/* 394 */       setMode();
/* 395 */       this.eventHandler.resizeDisplay();
/* 396 */       updateVsync(enableVsync);
/* 397 */       updateDisplay(tracyFrameCapture);
/* 398 */     } catch (Exception e) {
/* 399 */       LOGGER.error("Couldn't toggle fullscreen", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public int calculateScale(int maxScale, boolean enforceUnicode) {
/* 404 */     int scale = 1;
/* 405 */     while (scale != maxScale && scale < this.framebufferWidth && scale < this.framebufferHeight && this.framebufferWidth / (scale + 1) >= 320 && this.framebufferHeight / (scale + 1) >= 240) {
/* 406 */       scale++;
/*     */     }
/* 408 */     if (enforceUnicode && scale % 2 != 0) {
/* 409 */       scale++;
/*     */     }
/* 411 */     return scale;
/*     */   }
/*     */   
/*     */   public void setGuiScale(int guiScale) {
/* 415 */     this.guiScale = guiScale;
/* 416 */     double doubleGuiScale = guiScale;
/* 417 */     int width = (int)(this.framebufferWidth / doubleGuiScale);
/* 418 */     this.guiScaledWidth = (this.framebufferWidth / doubleGuiScale > width) ? (width + 1) : width;
/* 419 */     int height = (int)(this.framebufferHeight / doubleGuiScale);
/* 420 */     this.guiScaledHeight = (this.framebufferHeight / doubleGuiScale > height) ? (height + 1) : height;
/*     */   }
/*     */   
/*     */   public void setTitle(String title) {
/* 424 */     GLFW.glfwSetWindowTitle(this.handle, title);
/*     */   }
/*     */   
/*     */   public long handle() {
/* 428 */     return this.handle;
/*     */   }
/*     */   
/*     */   public boolean isFullscreen() {
/* 432 */     return this.fullscreen;
/*     */   }
/*     */   
/*     */   public boolean isIconified() {
/* 436 */     return this.iconified;
/*     */   }
/*     */   
/*     */   public int getWidth() {
/* 440 */     return this.framebufferWidth;
/*     */   }
/*     */   
/*     */   public int getHeight() {
/* 444 */     return this.framebufferHeight;
/*     */   }
/*     */   
/*     */   public void setWidth(int width) {
/* 448 */     this.framebufferWidth = width;
/*     */   }
/*     */   
/*     */   public void setHeight(int height) {
/* 452 */     this.framebufferHeight = height;
/*     */   }
/*     */   
/*     */   public int getScreenWidth() {
/* 456 */     return this.width;
/*     */   }
/*     */   
/*     */   public int getScreenHeight() {
/* 460 */     return this.height;
/*     */   }
/*     */   
/*     */   public int getGuiScaledWidth() {
/* 464 */     return this.guiScaledWidth;
/*     */   }
/*     */   
/*     */   public int getGuiScaledHeight() {
/* 468 */     return this.guiScaledHeight;
/*     */   }
/*     */   
/*     */   public int getX() {
/* 472 */     return this.x;
/*     */   }
/*     */   
/*     */   public int getY() {
/* 476 */     return this.y;
/*     */   }
/*     */   
/*     */   public int getGuiScale() {
/* 480 */     return this.guiScale;
/*     */   }
/*     */   
/*     */   public Monitor findBestMonitor() {
/* 484 */     return this.screenManager.findBestMonitor(this);
/*     */   }
/*     */   
/*     */   public void updateRawMouseInput(boolean value) {
/* 488 */     InputConstants.updateRawMouseInput(this, value);
/*     */   }
/*     */   
/*     */   public void setWindowCloseCallback(Runnable task) {
/* 492 */     GLFWWindowCloseCallback prev = GLFW.glfwSetWindowCloseCallback(this.handle, id -> task.run());
/* 493 */     if (prev != null)
/* 494 */       prev.free(); 
/*     */   }
/*     */   
/*     */   public static class WindowInitFailed
/*     */     extends SilentInitException {
/*     */     private WindowInitFailed(String message) {
/* 500 */       super(message);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean isMinimized() {
/* 505 */     return this.minimized;
/*     */   }
/*     */   
/*     */   public void setAllowCursorChanges(boolean value) {
/* 509 */     this.allowCursorChanges = value;
/*     */   }
/*     */   
/*     */   public void selectCursor(CursorType cursor) {
/* 513 */     CursorType effectiveCursor = this.allowCursorChanges ? cursor : CursorType.DEFAULT;
/*     */     
/* 515 */     if (this.currentCursor != effectiveCursor) {
/* 516 */       this.currentCursor = effectiveCursor;
/* 517 */       effectiveCursor.select(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public float getAppropriateLineWidth() {
/* 522 */     return Math.max(2.5F, getWidth() / 1920.0F * 2.5F);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/platform/Window.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */