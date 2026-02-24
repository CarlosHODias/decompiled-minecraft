package net.minecraft.util.profiling;

import java.util.Set;
import net.minecraft.util.profiling.metrics.MetricCategory;
import org.apache.commons.lang3.tuple.Pair;

public interface ProfileCollector extends ProfilerFiller {
  ProfileResults getResults();
  
  ActiveProfiler.PathEntry getEntry(String paramString);
  
  Set<Pair<String, MetricCategory>> getChartedPaths();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/ProfileCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */