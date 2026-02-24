package net.minecraft.util.profiling;

import it.unimi.dsi.fastutil.objects.Object2LongMap;

public interface ProfilerPathEntry {
  long getDuration();
  
  long getMaxDuration();
  
  long getCount();
  
  Object2LongMap<String> getCounters();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/ProfilerPathEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */