package com.mojang.blaze3d.systems;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.buffers.GpuFence;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import java.nio.ByteBuffer;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.function.Supplier;

public interface CommandEncoder {
  RenderPass createRenderPass(Supplier<String> paramSupplier, GpuTextureView paramGpuTextureView, OptionalInt paramOptionalInt);
  
  RenderPass createRenderPass(Supplier<String> paramSupplier, GpuTextureView paramGpuTextureView1, OptionalInt paramOptionalInt, GpuTextureView paramGpuTextureView2, OptionalDouble paramOptionalDouble);
  
  void clearColorTexture(GpuTexture paramGpuTexture, int paramInt);
  
  void clearColorAndDepthTextures(GpuTexture paramGpuTexture1, int paramInt, GpuTexture paramGpuTexture2, double paramDouble);
  
  void clearColorAndDepthTextures(GpuTexture paramGpuTexture1, int paramInt1, GpuTexture paramGpuTexture2, double paramDouble, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  void clearDepthTexture(GpuTexture paramGpuTexture, double paramDouble);
  
  void writeToBuffer(GpuBufferSlice paramGpuBufferSlice, ByteBuffer paramByteBuffer);
  
  GpuBuffer.MappedView mapBuffer(GpuBuffer paramGpuBuffer, boolean paramBoolean1, boolean paramBoolean2);
  
  GpuBuffer.MappedView mapBuffer(GpuBufferSlice paramGpuBufferSlice, boolean paramBoolean1, boolean paramBoolean2);
  
  void copyToBuffer(GpuBufferSlice paramGpuBufferSlice1, GpuBufferSlice paramGpuBufferSlice2);
  
  void writeToTexture(GpuTexture paramGpuTexture, NativeImage paramNativeImage);
  
  void writeToTexture(GpuTexture paramGpuTexture, NativeImage paramNativeImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8);
  
  void writeToTexture(GpuTexture paramGpuTexture, ByteBuffer paramByteBuffer, NativeImage.Format paramFormat, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
  
  void copyTextureToBuffer(GpuTexture paramGpuTexture, GpuBuffer paramGpuBuffer, long paramLong, Runnable paramRunnable, int paramInt);
  
  void copyTextureToBuffer(GpuTexture paramGpuTexture, GpuBuffer paramGpuBuffer, long paramLong, Runnable paramRunnable, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
  
  void copyTextureToTexture(GpuTexture paramGpuTexture1, GpuTexture paramGpuTexture2, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7);
  
  void presentTexture(GpuTextureView paramGpuTextureView);
  
  GpuFence createFence();
  
  GpuQuery timerQueryBegin();
  
  void timerQueryEnd(GpuQuery paramGpuQuery);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/systems/CommandEncoder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */