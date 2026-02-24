package net.minecraft.client.renderer.entity.state;

import net.minecraft.world.entity.Display;

public abstract class DisplayEntityRenderState extends EntityRenderState {
  public Display.RenderState renderState;
  
  public float interpolationProgress;
  
  public float entityYRot;
  
  public float entityXRot;
  
  public float cameraYRot;
  
  public float cameraXRot;
  
  public abstract boolean hasSubState();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/state/DisplayEntityRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */