package net.minecraft.world.ticks;

import net.minecraft.core.BlockPos;

public interface LevelTickAccess<T> extends TickAccess<T> {
  boolean willTickThisTick(BlockPos paramBlockPos, T paramT);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/ticks/LevelTickAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */