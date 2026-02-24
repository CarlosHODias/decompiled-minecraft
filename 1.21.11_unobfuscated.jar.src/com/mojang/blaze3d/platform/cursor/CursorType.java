/*    */ package com.mojang.blaze3d.platform.cursor;
/*    */ 
/*    */ import com.mojang.blaze3d.platform.Window;
/*    */ import org.lwjgl.glfw.GLFW;
/*    */ 
/*    */ public class CursorType
/*    */ {
/*  8 */   public static final CursorType DEFAULT = new CursorType("default", 0L);
/*    */   
/*    */   private final String name;
/*    */   private final long handle;
/*    */   
/*    */   private CursorType(String name, long handle) {
/* 14 */     this.name = name;
/* 15 */     this.handle = handle;
/*    */   }
/*    */   
/*    */   public void select(Window window) {
/* 19 */     GLFW.glfwSetCursor(window.handle(), this.handle);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 24 */     return this.name;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static CursorType createStandardCursor(int shape, String name, CursorType fallback) {
/* 33 */     long handle = GLFW.glfwCreateStandardCursor(shape);
/* 34 */     if (handle == 0L)
/*    */     {
/* 36 */       return fallback;
/*    */     }
/*    */     
/* 39 */     return new CursorType(name, handle);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/platform/cursor/CursorType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */