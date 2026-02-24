package com.mojang.blaze3d.framegraph;

import com.mojang.blaze3d.resource.ResourceDescriptor;
import com.mojang.blaze3d.resource.ResourceHandle;

public interface FramePass {
  <T> ResourceHandle<T> createsInternal(String paramString, ResourceDescriptor<T> paramResourceDescriptor);
  
  <T> void reads(ResourceHandle<T> paramResourceHandle);
  
  <T> ResourceHandle<T> readsAndWrites(ResourceHandle<T> paramResourceHandle);
  
  void requires(FramePass paramFramePass);
  
  void disableCulling();
  
  void executes(Runnable paramRunnable);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/framegraph/FramePass.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */