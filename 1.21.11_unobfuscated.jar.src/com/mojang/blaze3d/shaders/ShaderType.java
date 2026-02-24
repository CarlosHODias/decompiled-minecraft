/*    */ package com.mojang.blaze3d.shaders;
/*    */ 
/*    */ import net.minecraft.resources.FileToIdConverter;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ public enum ShaderType
/*    */ {
/*  9 */   VERTEX("vertex", ".vsh"),
/* 10 */   FRAGMENT("fragment", ".fsh");
/*    */   
/* 12 */   private static final ShaderType[] TYPES = values();
/*    */   
/*    */   private final String name;
/*    */   private final String extension;
/*    */   
/*    */   ShaderType(String name, String extension) {
/* 18 */     this.name = name;
/* 19 */     this.extension = extension;
/*    */   }
/*    */   
/*    */   public static ShaderType byLocation(Identifier location) {
/* 23 */     for (ShaderType type : TYPES) {
/* 24 */       if (location.getPath().endsWith(type.extension)) {
/* 25 */         return type;
/*    */       }
/*    */     } 
/* 28 */     return null;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 32 */     return this.name;
/*    */   }
/*    */   
/*    */   public FileToIdConverter idConverter() {
/* 36 */     return new FileToIdConverter("shaders", this.extension);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/shaders/ShaderType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */