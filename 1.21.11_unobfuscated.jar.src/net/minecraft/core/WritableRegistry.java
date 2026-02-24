package net.minecraft.core;

import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;

public interface WritableRegistry<T> extends Registry<T> {
  Holder.Reference<T> register(ResourceKey<T> paramResourceKey, T paramT, RegistrationInfo paramRegistrationInfo);
  
  void bindTag(TagKey<T> paramTagKey, List<Holder<T>> paramList);
  
  boolean isEmpty();
  
  HolderGetter<T> createRegistrationLookup();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/WritableRegistry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */