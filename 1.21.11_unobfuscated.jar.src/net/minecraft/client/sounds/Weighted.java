package net.minecraft.client.sounds;

import net.minecraft.util.RandomSource;

public interface Weighted<T> {
  int getWeight();
  
  T getSound(RandomSource paramRandomSource);
  
  void preloadIfRequired(SoundEngine paramSoundEngine);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/sounds/Weighted.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */