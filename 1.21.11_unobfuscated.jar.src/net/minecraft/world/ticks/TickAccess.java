package net.minecraft.world.ticks;

import net.minecraft.core.BlockPos;

public interface TickAccess<T> {
  void schedule(ScheduledTick<T> paramScheduledTick);
  
  boolean hasScheduledTick(BlockPos paramBlockPos, T paramT);
  
  int count();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/ticks/TickAccess.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */