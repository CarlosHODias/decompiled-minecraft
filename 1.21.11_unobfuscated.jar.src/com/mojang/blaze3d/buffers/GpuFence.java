package com.mojang.blaze3d.buffers;

public interface GpuFence extends AutoCloseable {
  void close();
  
  boolean awaitCompletion(long paramLong);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/buffers/GpuFence.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */