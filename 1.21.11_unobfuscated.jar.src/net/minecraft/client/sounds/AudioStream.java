package net.minecraft.client.sounds;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.sound.sampled.AudioFormat;

public interface AudioStream extends Closeable {
  AudioFormat getFormat();
  
  ByteBuffer read(int paramInt) throws IOException;
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/sounds/AudioStream.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */