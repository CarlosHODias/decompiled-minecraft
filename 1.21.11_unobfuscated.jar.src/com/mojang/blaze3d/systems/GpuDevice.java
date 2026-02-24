/*    */ package com.mojang.blaze3d.systems;
/*    */ 
/*    */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*    */ import com.mojang.blaze3d.pipeline.CompiledRenderPipeline;
/*    */ import com.mojang.blaze3d.pipeline.RenderPipeline;
/*    */ import com.mojang.blaze3d.shaders.ShaderSource;
/*    */ import com.mojang.blaze3d.textures.AddressMode;
/*    */ import com.mojang.blaze3d.textures.FilterMode;
/*    */ import com.mojang.blaze3d.textures.GpuSampler;
/*    */ import com.mojang.blaze3d.textures.GpuTexture;
/*    */ import com.mojang.blaze3d.textures.GpuTexture.Usage;
/*    */ import com.mojang.blaze3d.textures.GpuTextureView;
/*    */ import com.mojang.blaze3d.textures.TextureFormat;
/*    */ import java.nio.ByteBuffer;
/*    */ import java.util.List;
/*    */ import java.util.OptionalDouble;
/*    */ import java.util.function.Supplier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public interface GpuDevice
/*    */ {
/*    */   CommandEncoder createCommandEncoder();
/*    */   
/*    */   GpuSampler createSampler(AddressMode paramAddressMode1, AddressMode paramAddressMode2, FilterMode paramFilterMode1, FilterMode paramFilterMode2, int paramInt, OptionalDouble paramOptionalDouble);
/*    */   
/*    */   GpuTexture createTexture(Supplier<String> paramSupplier, @GpuTexture.Usage int paramInt1, TextureFormat paramTextureFormat, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*    */   
/*    */   GpuTexture createTexture(String paramString, @GpuTexture.Usage int paramInt1, TextureFormat paramTextureFormat, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*    */   
/*    */   GpuTextureView createTextureView(GpuTexture paramGpuTexture);
/*    */   
/*    */   GpuTextureView createTextureView(GpuTexture paramGpuTexture, int paramInt1, int paramInt2);
/*    */   
/*    */   GpuBuffer createBuffer(Supplier<String> paramSupplier, @GpuBuffer.Usage int paramInt, long paramLong);
/*    */   
/*    */   GpuBuffer createBuffer(Supplier<String> paramSupplier, @GpuBuffer.Usage int paramInt, ByteBuffer paramByteBuffer);
/*    */   
/*    */   String getImplementationInformation();
/*    */   
/*    */   List<String> getLastDebugMessages();
/*    */   
/*    */   boolean isDebuggingEnabled();
/*    */   
/*    */   String getVendor();
/*    */   
/*    */   String getBackendName();
/*    */   
/*    */   String getVersion();
/*    */   
/*    */   String getRenderer();
/*    */   
/*    */   int getMaxTextureSize();
/*    */   
/*    */   int getUniformOffsetAlignment();
/*    */   
/*    */   default CompiledRenderPipeline precompilePipeline(RenderPipeline pipeline) {
/* 63 */     return precompilePipeline(pipeline, null);
/*    */   }
/*    */   
/*    */   CompiledRenderPipeline precompilePipeline(RenderPipeline paramRenderPipeline, ShaderSource paramShaderSource);
/*    */   
/*    */   void clearPipelineCache();
/*    */   
/*    */   List<String> getEnabledExtensions();
/*    */   
/*    */   int getMaxSupportedAnisotropy();
/*    */   
/*    */   void close();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/systems/GpuDevice.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */