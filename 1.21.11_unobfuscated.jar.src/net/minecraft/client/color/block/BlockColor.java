package net.minecraft.client.color.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockColor {
  int getColor(BlockState paramBlockState, BlockAndTintGetter paramBlockAndTintGetter, BlockPos paramBlockPos, int paramInt);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/color/block/BlockColor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */