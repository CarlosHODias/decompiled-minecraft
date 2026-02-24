package net.minecraft.client.resources.sounds;

public interface TickableSoundInstance extends SoundInstance {
  boolean isStopped();
  
  void tick();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/sounds/TickableSoundInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */