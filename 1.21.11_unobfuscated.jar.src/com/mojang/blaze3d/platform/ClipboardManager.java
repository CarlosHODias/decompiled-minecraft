/*    */ package com.mojang.blaze3d.platform;
/*    */ 
/*    */ import java.nio.ByteBuffer;
/*    */ import java.nio.charset.StandardCharsets;
/*    */ import net.minecraft.util.StringDecomposer;
/*    */ import org.lwjgl.BufferUtils;
/*    */ import org.lwjgl.glfw.GLFW;
/*    */ import org.lwjgl.glfw.GLFWErrorCallback;
/*    */ import org.lwjgl.glfw.GLFWErrorCallbackI;
/*    */ import org.lwjgl.system.MemoryUtil;
/*    */ 
/*    */ 
/*    */ public class ClipboardManager
/*    */ {
/*    */   public static final int FORMAT_UNAVAILABLE = 65545;
/* 16 */   private final ByteBuffer clipboardScratchBuffer = BufferUtils.createByteBuffer(8192);
/*    */   
/*    */   public String getClipboard(Window window, GLFWErrorCallbackI errorCallback) {
/* 19 */     GLFWErrorCallback prevCallback = GLFW.glfwSetErrorCallback(errorCallback);
/* 20 */     String clipboard = GLFW.glfwGetClipboardString(window.handle());
/* 21 */     clipboard = (clipboard != null) ? StringDecomposer.filterBrokenSurrogates(clipboard) : "";
/* 22 */     GLFWErrorCallback oldCallback = GLFW.glfwSetErrorCallback((GLFWErrorCallbackI)prevCallback);
/* 23 */     if (oldCallback != null) {
/* 24 */       oldCallback.free();
/*    */     }
/* 26 */     return clipboard;
/*    */   }
/*    */   
/*    */   private static void pushClipboard(Window window, ByteBuffer buffer, byte[] data) {
/* 30 */     buffer.clear();
/* 31 */     buffer.put(data);
/* 32 */     buffer.put((byte)0);
/* 33 */     buffer.flip();
/* 34 */     GLFW.glfwSetClipboardString(window.handle(), buffer);
/*    */   }
/*    */   
/*    */   public void setClipboard(Window window, String clipboard) {
/* 38 */     byte[] encoded = clipboard.getBytes(StandardCharsets.UTF_8);
/*    */     
/* 40 */     int encodedLength = encoded.length + 1;
/* 41 */     if (encodedLength < this.clipboardScratchBuffer.capacity()) {
/* 42 */       pushClipboard(window, this.clipboardScratchBuffer, encoded);
/*    */     } else {
/* 44 */       ByteBuffer buffer = MemoryUtil.memAlloc(encodedLength);
/*    */       try {
/* 46 */         pushClipboard(window, buffer, encoded);
/*    */       } finally {
/* 48 */         MemoryUtil.memFree(buffer);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/platform/ClipboardManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */