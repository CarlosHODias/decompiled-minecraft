/*    */ package com.mojang.blaze3d.textures;
/*    */ 
/*    */ import java.lang.annotation.ElementType;
/*    */ import java.lang.annotation.Retention;
/*    */ import java.lang.annotation.RetentionPolicy;
/*    */ import java.lang.annotation.Target;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class GpuTexture
/*    */   implements AutoCloseable
/*    */ {
/*    */   public static final int USAGE_COPY_DST = 1;
/*    */   public static final int USAGE_COPY_SRC = 2;
/*    */   public static final int USAGE_TEXTURE_BINDING = 4;
/*    */   public static final int USAGE_RENDER_ATTACHMENT = 8;
/*    */   public static final int USAGE_CUBEMAP_COMPATIBLE = 16;
/*    */   private final TextureFormat format;
/*    */   private final int width;
/*    */   private final int height;
/*    */   private final int depthOrLayers;
/*    */   private final int mipLevels;
/*    */   @Usage
/*    */   private final int usage;
/*    */   private final String label;
/*    */   
/*    */   public GpuTexture(@Usage int usage, String label, TextureFormat format, int width, int height, int depthOrLayers, int mipLevels) {
/* 60 */     this.usage = usage;
/* 61 */     this.label = label;
/* 62 */     this.format = format;
/* 63 */     this.width = width;
/* 64 */     this.height = height;
/* 65 */     this.depthOrLayers = depthOrLayers;
/* 66 */     this.mipLevels = mipLevels;
/*    */   }
/*    */   
/*    */   public int getWidth(int mipLevel) {
/* 70 */     return this.width >> mipLevel;
/*    */   }
/*    */   
/*    */   public int getHeight(int mipLevel) {
/* 74 */     return this.height >> mipLevel;
/*    */   }
/*    */   
/*    */   public int getDepthOrLayers() {
/* 78 */     return this.depthOrLayers;
/*    */   }
/*    */   
/*    */   public int getMipLevels() {
/* 82 */     return this.mipLevels;
/*    */   }
/*    */   
/*    */   public TextureFormat getFormat() {
/* 86 */     return this.format;
/*    */   }
/*    */   
/*    */   @Usage
/*    */   public int usage() {
/* 91 */     return this.usage;
/*    */   }
/*    */   
/*    */   public String getLabel() {
/* 95 */     return this.label;
/*    */   }
/*    */   
/*    */   public abstract void close();
/*    */   
/*    */   public abstract boolean isClosed();
/*    */   
/*    */   @Retention(RetentionPolicy.CLASS)
/*    */   @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.TYPE_USE})
/*    */   public static @interface Usage {}
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/textures/GpuTexture.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */