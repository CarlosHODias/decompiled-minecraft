package net.minecraft.gametest.framework;

import java.util.stream.Stream;
import net.minecraft.core.Holder;

@FunctionalInterface
public interface TestInstanceFinder {
  Stream<Holder.Reference<GameTestInstance>> findTests();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/gametest/framework/TestInstanceFinder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */