package net.minecraft.world.level.entity;

import net.minecraft.server.level.FullChunkStatus;
import net.minecraft.world.level.ChunkPos;

@FunctionalInterface
public interface ChunkStatusUpdateListener {
  void onChunkStatusChange(ChunkPos paramChunkPos, FullChunkStatus paramFullChunkStatus);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/entity/ChunkStatusUpdateListener.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */