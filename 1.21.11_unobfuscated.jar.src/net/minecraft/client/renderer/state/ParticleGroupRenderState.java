package net.minecraft.client.renderer.state;

import net.minecraft.client.renderer.SubmitNodeCollector;

public interface ParticleGroupRenderState {
  void submit(SubmitNodeCollector paramSubmitNodeCollector, CameraRenderState paramCameraRenderState);
  
  default void clear() {}
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/state/ParticleGroupRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */