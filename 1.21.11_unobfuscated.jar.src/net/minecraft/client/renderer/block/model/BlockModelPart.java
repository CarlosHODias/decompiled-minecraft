package net.minecraft.client.renderer.block.model;

import java.util.List;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.core.Direction;

public interface BlockModelPart {
  List<BakedQuad> getQuads(Direction paramDirection);
  
  boolean useAmbientOcclusion();
  
  TextureAtlasSprite particleIcon();
  
  public static interface Unbaked extends ResolvableModel {
    BlockModelPart bake(ModelBaker param1ModelBaker);
  }
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/BlockModelPart.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */