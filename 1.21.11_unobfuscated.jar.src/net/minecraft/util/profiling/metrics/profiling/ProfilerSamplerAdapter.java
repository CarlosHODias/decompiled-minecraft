/*    */ package net.minecraft.util.profiling.metrics.profiling;
/*    */ 
/*    */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*    */ import java.util.Set;
/*    */ import java.util.function.Supplier;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.util.TimeUtil;
/*    */ import net.minecraft.util.profiling.ActiveProfiler;
/*    */ import net.minecraft.util.profiling.ProfileCollector;
/*    */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*    */ import net.minecraft.util.profiling.metrics.MetricSampler;
/*    */ import org.apache.commons.lang3.tuple.Pair;
/*    */ 
/*    */ public class ProfilerSamplerAdapter {
/* 15 */   private final Set<String> previouslyFoundSamplerNames = (Set<String>)new ObjectOpenHashSet();
/*    */   
/*    */   public Set<MetricSampler> newSamplersFoundInProfiler(Supplier<ProfileCollector> profiler) {
/* 18 */     Set<MetricSampler> newSamplers = (Set<MetricSampler>)((ProfileCollector)profiler.get()).getChartedPaths().stream()
/* 19 */       .filter(pathAndCategory -> !this.previouslyFoundSamplerNames.contains(pathAndCategory.getLeft()))
/* 20 */       .map(pathAndCategory -> samplerForProfilingPath(profiler, (String)pathAndCategory.getLeft(), (MetricCategory)pathAndCategory.getRight()))
/* 21 */       .collect(Collectors.toSet());
/*    */     
/* 23 */     for (MetricSampler sampler : newSamplers) {
/* 24 */       this.previouslyFoundSamplerNames.add(sampler.getName());
/*    */     }
/*    */     
/* 27 */     return newSamplers;
/*    */   }
/*    */   
/*    */   private static MetricSampler samplerForProfilingPath(Supplier<ProfileCollector> profiler, String profilerPath, MetricCategory category) {
/* 31 */     return MetricSampler.create(profilerPath, category, () -> {
/*    */           ActiveProfiler.PathEntry entry = ((ProfileCollector)profiler.get()).getEntry(profilerPath);
/*    */           return (entry == null) ? 0.0D : (entry.getMaxDuration() / TimeUtil.NANOSECONDS_PER_MILLISECOND);
/*    */         });
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/metrics/profiling/ProfilerSamplerAdapter.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */