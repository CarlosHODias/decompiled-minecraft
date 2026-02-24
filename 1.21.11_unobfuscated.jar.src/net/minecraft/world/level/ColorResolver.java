package net.minecraft.world.level;

import net.minecraft.world.level.biome.Biome;

@FunctionalInterface
public interface ColorResolver {
  int getColor(Biome paramBiome, double paramDouble1, double paramDouble2);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/ColorResolver.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */