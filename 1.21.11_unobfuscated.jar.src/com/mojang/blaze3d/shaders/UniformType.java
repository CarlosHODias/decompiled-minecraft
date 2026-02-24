/*    */ package com.mojang.blaze3d.shaders;
/*    */ 
/*    */ public enum UniformType {
/*  4 */   UNIFORM_BUFFER("ubo"),
/*  5 */   TEXEL_BUFFER("utb");
/*    */   
/*    */   final String name;
/*    */ 
/*    */   
/*    */   UniformType(String name) {
/* 11 */     this.name = name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/shaders/UniformType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */