package net.minecraft.client.renderer.texture;

import java.io.IOException;
import java.nio.file.Path;
import net.minecraft.resources.Identifier;

public interface Dumpable {
  void dumpContents(Identifier paramIdentifier, Path paramPath) throws IOException;
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/Dumpable.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */