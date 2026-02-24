package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface BlockEntityTicker<T extends BlockEntity> {
  void tick(Level paramLevel, BlockPos paramBlockPos, BlockState paramBlockState, T paramT);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/entity/BlockEntityTicker.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */