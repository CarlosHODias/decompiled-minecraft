package com.mojang.blaze3d.textures;

import java.util.OptionalDouble;

public abstract class GpuSampler implements AutoCloseable {
  public abstract AddressMode getAddressModeU();
  
  public abstract AddressMode getAddressModeV();
  
  public abstract FilterMode getMinFilter();
  
  public abstract FilterMode getMagFilter();
  
  public abstract int getMaxAnisotropy();
  
  public abstract OptionalDouble getMaxLod();
  
  public abstract void close();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/textures/GpuSampler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */