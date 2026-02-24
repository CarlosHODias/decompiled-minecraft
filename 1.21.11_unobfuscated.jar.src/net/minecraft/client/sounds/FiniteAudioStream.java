package net.minecraft.client.sounds;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface FiniteAudioStream extends AudioStream {
  ByteBuffer readAll() throws IOException;
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/sounds/FiniteAudioStream.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */