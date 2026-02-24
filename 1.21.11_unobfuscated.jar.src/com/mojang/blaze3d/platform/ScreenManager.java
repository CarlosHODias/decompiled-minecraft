/*     */ package com.mojang.blaze3d.platform;
/*     */ 
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectIterator;
/*     */ import org.lwjgl.PointerBuffer;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import org.lwjgl.glfw.GLFWMonitorCallback;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class ScreenManager
/*     */ {
/*  15 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  16 */   private final Long2ObjectMap<Monitor> monitors = (Long2ObjectMap<Monitor>)new Long2ObjectOpenHashMap();
/*     */   private final MonitorCreator monitorCreator;
/*     */   
/*     */   public ScreenManager(MonitorCreator monitorCreator) {
/*  20 */     this.monitorCreator = monitorCreator;
/*  21 */     GLFW.glfwSetMonitorCallback(this::onMonitorChange);
/*  22 */     PointerBuffer buffer = GLFW.glfwGetMonitors();
/*  23 */     if (buffer != null) {
/*  24 */       for (int i = 0; i < buffer.limit(); i++) {
/*  25 */         long monitor = buffer.get(i);
/*  26 */         this.monitors.put(monitor, monitorCreator.createMonitor(monitor));
/*     */       } 
/*     */     }
/*     */   }
/*     */   
/*     */   private void onMonitorChange(long monitor, int event) {
/*  32 */     RenderSystem.assertOnRenderThread();
/*  33 */     if (event == 262145) {
/*  34 */       this.monitors.put(monitor, this.monitorCreator.createMonitor(monitor));
/*  35 */       LOGGER.debug("Monitor {} connected. Current monitors: {}", monitor, this.monitors);
/*  36 */     } else if (event == 262146) {
/*  37 */       this.monitors.remove(monitor);
/*  38 */       LOGGER.debug("Monitor {} disconnected. Current monitors: {}", monitor, this.monitors);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Monitor getMonitor(long monitor) {
/*  43 */     return (Monitor)this.monitors.get(monitor);
/*     */   }
/*     */ 
/*     */   
/*     */   public Monitor findBestMonitor(Window window) {
/*  48 */     long windowMonitor = GLFW.glfwGetWindowMonitor(window.handle());
/*  49 */     if (windowMonitor != 0L) {
/*  50 */       return getMonitor(windowMonitor);
/*     */     }
/*     */     
/*  53 */     int winMinX = window.getX();
/*  54 */     int winMaxX = winMinX + window.getScreenWidth();
/*  55 */     int winMinY = window.getY();
/*  56 */     int winMaxY = winMinY + window.getScreenHeight();
/*     */     
/*  58 */     int maxArea = -1;
/*  59 */     Monitor result = null;
/*  60 */     long primaryMonitor = GLFW.glfwGetPrimaryMonitor();
/*  61 */     LOGGER.debug("Selecting monitor - primary: {}, current monitors: {}", primaryMonitor, this.monitors);
/*     */     
/*  63 */     for (ObjectIterator<Monitor> objectIterator = this.monitors.values().iterator(); objectIterator.hasNext(); ) { Monitor monitor = objectIterator.next();
/*  64 */       int monMinX = monitor.getX();
/*  65 */       int monMaxX = monMinX + monitor.getCurrentMode().getWidth();
/*  66 */       int monMinY = monitor.getY();
/*  67 */       int monMaxY = monMinY + monitor.getCurrentMode().getHeight();
/*     */       
/*  69 */       int minX = clamp(winMinX, monMinX, monMaxX);
/*  70 */       int maxX = clamp(winMaxX, monMinX, monMaxX);
/*  71 */       int minY = clamp(winMinY, monMinY, monMaxY);
/*  72 */       int maxY = clamp(winMaxY, monMinY, monMaxY);
/*     */       
/*  74 */       int sx = Math.max(0, maxX - minX);
/*  75 */       int sy = Math.max(0, maxY - minY);
/*  76 */       int area = sx * sy;
/*  77 */       if (area > maxArea) {
/*  78 */         result = monitor;
/*  79 */         maxArea = area; continue;
/*  80 */       }  if (area == maxArea && primaryMonitor == monitor.getMonitor()) {
/*  81 */         LOGGER.debug("Primary monitor {} is preferred to monitor {}", monitor, result);
/*  82 */         result = monitor;
/*     */       }  }
/*     */     
/*  85 */     LOGGER.debug("Selected monitor: {}", result);
/*  86 */     return result;
/*     */   }
/*     */   
/*     */   public static int clamp(int value, int min, int max) {
/*  90 */     if (value < min) {
/*  91 */       return min;
/*     */     }
/*  93 */     if (value > max) {
/*  94 */       return max;
/*     */     }
/*  96 */     return value;
/*     */   }
/*     */   
/*     */   public void shutdown() {
/* 100 */     RenderSystem.assertOnRenderThread();
/* 101 */     GLFWMonitorCallback callback = GLFW.glfwSetMonitorCallback(null);
/* 102 */     if (callback != null)
/* 103 */       callback.free(); 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/platform/ScreenManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */