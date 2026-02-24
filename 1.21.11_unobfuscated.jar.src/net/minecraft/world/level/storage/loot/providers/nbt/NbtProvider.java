package net.minecraft.world.level.storage.loot.providers.nbt;

import java.util.Set;
import net.minecraft.nbt.Tag;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.level.storage.loot.LootContext;

public interface NbtProvider {
  Tag get(LootContext paramLootContext);
  
  Set<ContextKey<?>> getReferencedContextParams();
  
  LootNbtProviderType getType();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/providers/nbt/NbtProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */