package net.minecraft.client.resources.model;

import net.minecraft.resources.Identifier;

public interface ResolvableModel {
  void resolveDependencies(Resolver paramResolver);
  
  public static interface Resolver {
    void markDependency(Identifier param1Identifier);
  }
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/model/ResolvableModel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */