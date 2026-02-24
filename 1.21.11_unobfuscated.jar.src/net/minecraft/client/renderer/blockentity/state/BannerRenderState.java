package net.minecraft.client.renderer.blockentity.state;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

public class BannerRenderState extends BlockEntityRenderState {
  public DyeColor baseColor;
  
  public BannerPatternLayers patterns;
  
  public float phase;
  
  public float angle;
  
  public boolean standing;
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/blockentity/state/BannerRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */