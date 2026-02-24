/*    */ package com.mojang.blaze3d.opengl;
/*    */ 
/*    */ import com.mojang.blaze3d.textures.AddressMode;
/*    */ import com.mojang.blaze3d.textures.FilterMode;
/*    */ import com.mojang.blaze3d.textures.GpuSampler;
/*    */ import java.util.OptionalDouble;
/*    */ import org.lwjgl.opengl.GL33C;
/*    */ 
/*    */ public class GlSampler
/*    */   extends GpuSampler
/*    */ {
/*    */   private final int id;
/*    */   private final AddressMode addressModeU;
/*    */   private final AddressMode addressModeV;
/*    */   private final FilterMode minFilter;
/*    */   private final FilterMode magFilter;
/*    */   private final int maxAnisotropy;
/*    */   private final OptionalDouble maxLod;
/*    */   private boolean closed;
/*    */   
/*    */   public GlSampler(AddressMode addressModeU, AddressMode addressModeV, FilterMode minFilter, FilterMode magFilter, int maxAnisotropy, OptionalDouble maxLod) {
/* 22 */     this.addressModeU = addressModeU;
/* 23 */     this.addressModeV = addressModeV;
/* 24 */     this.minFilter = minFilter;
/* 25 */     this.magFilter = magFilter;
/* 26 */     this.maxAnisotropy = maxAnisotropy;
/* 27 */     this.maxLod = maxLod;
/*    */     
/* 29 */     this.id = GL33C.glGenSamplers();
/* 30 */     GL33C.glSamplerParameteri(this.id, 10242, GlConst.toGl(addressModeU));
/* 31 */     GL33C.glSamplerParameteri(this.id, 10243, GlConst.toGl(addressModeV));
/* 32 */     if (maxAnisotropy > 1)
/*    */     {
/* 34 */       GL33C.glSamplerParameterf(this.id, 34046, maxAnisotropy);
/*    */     }
/* 36 */     switch (minFilter) {
/*    */       case NEAREST:
/* 38 */         GL33C.glSamplerParameteri(this.id, 10241, 9986); break;
/*    */       case LINEAR:
/* 40 */         GL33C.glSamplerParameteri(this.id, 10241, 9987); break;
/*    */     } 
/* 42 */     switch (magFilter) { case NEAREST:
/* 43 */         GL33C.glSamplerParameteri(this.id, 10240, 9728); break;
/* 44 */       case LINEAR: GL33C.glSamplerParameteri(this.id, 10240, 9729); break; }
/*    */     
/* 46 */     if (maxLod.isPresent()) {
/* 47 */       GL33C.glSamplerParameterf(this.id, 33083, (float)maxLod.getAsDouble());
/*    */     }
/*    */   }
/*    */   
/*    */   public int getId() {
/* 52 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public AddressMode getAddressModeU() {
/* 57 */     return this.addressModeU;
/*    */   }
/*    */ 
/*    */   
/*    */   public AddressMode getAddressModeV() {
/* 62 */     return this.addressModeV;
/*    */   }
/*    */ 
/*    */   
/*    */   public FilterMode getMinFilter() {
/* 67 */     return this.minFilter;
/*    */   }
/*    */ 
/*    */   
/*    */   public FilterMode getMagFilter() {
/* 72 */     return this.magFilter;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getMaxAnisotropy() {
/* 77 */     return this.maxAnisotropy;
/*    */   }
/*    */ 
/*    */   
/*    */   public OptionalDouble getMaxLod() {
/* 82 */     return this.maxLod;
/*    */   }
/*    */ 
/*    */   
/*    */   public void close() {
/* 87 */     if (!this.closed) {
/* 88 */       this.closed = true;
/* 89 */       GL33C.glDeleteSamplers(this.id);
/*    */     } 
/*    */   }
/*    */   
/*    */   public boolean isClosed() {
/* 94 */     return this.closed;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/opengl/GlSampler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */