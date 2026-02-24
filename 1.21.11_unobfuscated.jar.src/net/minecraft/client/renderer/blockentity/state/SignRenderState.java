package net.minecraft.client.renderer.blockentity.state;

import net.minecraft.world.level.block.entity.SignText;

public class SignRenderState extends BlockEntityRenderState {
  public SignText frontText;
  
  public SignText backText;
  
  public int textLineHeight;
  
  public int maxTextLineWidth;
  
  public boolean isTextFilteringEnabled;
  
  public boolean drawOutline;
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/state/SignRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */