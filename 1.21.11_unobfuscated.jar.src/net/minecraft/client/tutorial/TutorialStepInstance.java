package net.minecraft.client.tutorial;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.ClientInput;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;

public interface TutorialStepInstance {
  default void clear() {}
  
  default void tick() {}
  
  default void onInput(ClientInput input) {}
  
  default void onMouse(double xd, double yd) {}
  
  default void onLookAt(ClientLevel level, HitResult hit) {}
  
  default void onDestroyBlock(ClientLevel level, BlockPos pos, BlockState state, float percent) {}
  
  default void onOpenInventory() {}
  
  default void onGetItem(ItemStack itemStack) {}
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/tutorial/TutorialStepInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */