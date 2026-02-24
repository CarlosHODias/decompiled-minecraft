package net.minecraft.client.particle;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.RandomSource;

public interface SpriteSet {
  TextureAtlasSprite get(int paramInt1, int paramInt2);
  
  TextureAtlasSprite get(RandomSource paramRandomSource);
  
  TextureAtlasSprite first();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/SpriteSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */