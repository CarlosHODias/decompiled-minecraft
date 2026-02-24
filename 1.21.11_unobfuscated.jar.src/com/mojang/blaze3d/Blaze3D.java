/*    */ package com.mojang.blaze3d;
/*    */ 
/*    */ import org.lwjgl.glfw.GLFW;
/*    */ import org.lwjgl.system.MemoryUtil;
/*    */ 
/*    */ public class Blaze3D
/*    */ {
/*    */   public static void youJustLostTheGame() {
/*  9 */     MemoryUtil.memSet(0L, 0, 1L);
/*    */   }
/*    */   
/*    */   public static double getTime() {
/* 13 */     return GLFW.glfwGetTime();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/Blaze3D.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */