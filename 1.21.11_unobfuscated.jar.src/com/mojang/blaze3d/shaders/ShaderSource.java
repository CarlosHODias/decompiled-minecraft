package com.mojang.blaze3d.shaders;

import net.minecraft.resources.Identifier;

@FunctionalInterface
public interface ShaderSource {
  String get(Identifier paramIdentifier, ShaderType paramShaderType);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/shaders/ShaderSource.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */