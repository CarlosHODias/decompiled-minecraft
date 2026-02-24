package net.minecraft.world.waypoints;

import net.minecraft.world.entity.Entity;

@FunctionalInterface
public interface PartialTickSupplier {
  float apply(Entity paramEntity);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/waypoints/PartialTickSupplier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */