/*    */ package com.mojang.blaze3d.systems;
/*    */ 
/*    */ import com.google.common.annotations.VisibleForTesting;
/*    */ import com.mojang.blaze3d.textures.AddressMode;
/*    */ import com.mojang.blaze3d.textures.FilterMode;
/*    */ import com.mojang.blaze3d.textures.GpuSampler;
/*    */ import java.util.OptionalDouble;
/*    */ 
/*    */ public class SamplerCache
/*    */ {
/* 11 */   private final GpuSampler[] samplers = new GpuSampler[32];
/*    */   
/*    */   public void initialize() {
/* 14 */     GpuDevice device = RenderSystem.getDevice();
/* 15 */     if ((AddressMode.values()).length != 2 || (FilterMode.values()).length != 2) {
/* 16 */       throw new IllegalStateException("AddressMode and FilterMode enum sizes must be 2 - if you expanded them, please update SamplerCache");
/*    */     }
/* 18 */     for (AddressMode addressModeU : AddressMode.values()) {
/* 19 */       for (AddressMode addressModeV : AddressMode.values()) {
/* 20 */         for (FilterMode minFilter : FilterMode.values()) {
/* 21 */           for (FilterMode magFilter : FilterMode.values()) {
/* 22 */             for (boolean useMipmaps : new boolean[] { true, false }) {
/* 23 */               this.samplers[encode(addressModeU, addressModeV, minFilter, magFilter, useMipmaps)] = device.createSampler(addressModeU, addressModeV, minFilter, magFilter, 1, useMipmaps ? OptionalDouble.empty() : OptionalDouble.of(0.0D));
/*    */             }
/*    */           } 
/*    */         } 
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public GpuSampler getSampler(AddressMode addressModeU, AddressMode addressModeV, FilterMode minFilter, FilterMode magFilter, boolean useMipmaps) {
/* 38 */     return this.samplers[encode(addressModeU, addressModeV, minFilter, magFilter, useMipmaps)];
/*    */   }
/*    */   
/*    */   public GpuSampler getClampToEdge(FilterMode minMag) {
/* 42 */     return getSampler(AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE, minMag, minMag, false);
/*    */   }
/*    */   
/*    */   public GpuSampler getClampToEdge(FilterMode minMag, boolean mipmaps) {
/* 46 */     return getSampler(AddressMode.CLAMP_TO_EDGE, AddressMode.CLAMP_TO_EDGE, minMag, minMag, mipmaps);
/*    */   }
/*    */   
/*    */   public GpuSampler getRepeat(FilterMode minMag) {
/* 50 */     return getSampler(AddressMode.REPEAT, AddressMode.REPEAT, minMag, minMag, false);
/*    */   }
/*    */   
/*    */   public GpuSampler getRepeat(FilterMode minMag, boolean mipmaps) {
/* 54 */     return getSampler(AddressMode.REPEAT, AddressMode.REPEAT, minMag, minMag, mipmaps);
/*    */   }
/*    */   
/*    */   public void close() {
/* 58 */     for (GpuSampler sampler : this.samplers) {
/* 59 */       sampler.close();
/*    */     }
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   @VisibleForTesting
/*    */   static int encode(AddressMode addressModeU, AddressMode addressModeV, FilterMode minFilter, FilterMode magFilter, boolean useMipmaps) {
/* 71 */     int result = 0;
/* 72 */     result |= addressModeU.ordinal() & 0x1;
/* 73 */     result |= (addressModeV.ordinal() & 0x1) << 1;
/* 74 */     result |= (minFilter.ordinal() & 0x1) << 2;
/* 75 */     result |= (magFilter.ordinal() & 0x1) << 3;
/* 76 */     if (useMipmaps) {
/* 77 */       result |= 0x10;
/*    */     }
/* 79 */     return result;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/systems/SamplerCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */