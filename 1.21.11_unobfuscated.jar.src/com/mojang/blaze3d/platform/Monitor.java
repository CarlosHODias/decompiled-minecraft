/*    */ package com.mojang.blaze3d.platform;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import java.util.Optional;
/*    */ import org.lwjgl.glfw.GLFW;
/*    */ import org.lwjgl.glfw.GLFWVidMode;
/*    */ 
/*    */ public final class Monitor
/*    */ {
/*    */   private final long monitor;
/*    */   private final List<VideoMode> videoModes;
/*    */   private VideoMode currentMode;
/*    */   private int x;
/*    */   private int y;
/*    */   
/*    */   public Monitor(long monitor) {
/* 19 */     this.monitor = monitor;
/* 20 */     this.videoModes = Lists.newArrayList();
/* 21 */     refreshVideoModes();
/*    */   }
/*    */   
/*    */   public void refreshVideoModes() {
/* 25 */     this.videoModes.clear();
/* 26 */     GLFWVidMode.Buffer modes = GLFW.glfwGetVideoModes(this.monitor);
/* 27 */     for (int i = modes.limit() - 1; i >= 0; i--) {
/* 28 */       modes.position(i);
/* 29 */       VideoMode videoMode = new VideoMode(modes);
/* 30 */       if (videoMode.getRedBits() >= 8 && videoMode.getGreenBits() >= 8 && videoMode.getBlueBits() >= 8) {
/* 31 */         this.videoModes.add(videoMode);
/*    */       }
/*    */     } 
/* 34 */     int[] x = new int[1];
/* 35 */     int[] y = new int[1];
/* 36 */     GLFW.glfwGetMonitorPos(this.monitor, x, y);
/* 37 */     this.x = x[0];
/* 38 */     this.y = y[0];
/* 39 */     GLFWVidMode mode = GLFW.glfwGetVideoMode(this.monitor);
/* 40 */     this.currentMode = new VideoMode(mode);
/*    */   }
/*    */   
/*    */   public VideoMode getPreferredVidMode(Optional<VideoMode> expectedMode) {
/* 44 */     if (expectedMode.isPresent()) {
/* 45 */       VideoMode videoMode = expectedMode.get();
/*    */       
/* 47 */       for (VideoMode mode : this.videoModes) {
/* 48 */         if (mode.equals(videoMode)) {
/* 49 */           return mode;
/*    */         }
/*    */       } 
/*    */     } 
/* 53 */     return getCurrentMode();
/*    */   }
/*    */   
/*    */   public int getVideoModeIndex(VideoMode videoMode) {
/* 57 */     return this.videoModes.indexOf(videoMode);
/*    */   }
/*    */   
/*    */   public VideoMode getCurrentMode() {
/* 61 */     return this.currentMode;
/*    */   }
/*    */   
/*    */   public int getX() {
/* 65 */     return this.x;
/*    */   }
/*    */   
/*    */   public int getY() {
/* 69 */     return this.y;
/*    */   }
/*    */   
/*    */   public VideoMode getMode(int mode) {
/* 73 */     return this.videoModes.get(mode);
/*    */   }
/*    */   
/*    */   public int getModeCount() {
/* 77 */     return this.videoModes.size();
/*    */   }
/*    */   
/*    */   public long getMonitor() {
/* 81 */     return this.monitor;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 86 */     return String.format(Locale.ROOT, "Monitor[%s %sx%s %s]", new Object[] { this.monitor, this.x, this.y, this.currentMode });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/platform/Monitor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */