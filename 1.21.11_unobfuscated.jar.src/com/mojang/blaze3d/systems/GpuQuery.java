package com.mojang.blaze3d.systems;

import java.util.OptionalLong;

public interface GpuQuery extends AutoCloseable {
  OptionalLong getValue();
  
  void close();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/systems/GpuQuery.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */