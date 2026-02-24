/*    */ package net.minecraft.util.profiling.metrics;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.WeakHashMap;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class MetricsRegistry
/*    */ {
/* 12 */   public static final MetricsRegistry INSTANCE = new MetricsRegistry();
/*    */ 
/*    */   
/* 15 */   private final WeakHashMap<ProfilerMeasured, Void> measuredInstances = new WeakHashMap<>();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void add(ProfilerMeasured profilerMeasured) {
/* 21 */     this.measuredInstances.put(profilerMeasured, null);
/*    */   }
/*    */   
/*    */   public List<MetricSampler> getRegisteredSamplers() {
/* 25 */     Map<String, List<MetricSampler>> samplersByName = (Map<String, List<MetricSampler>>)this.measuredInstances.keySet().stream()
/* 26 */       .flatMap(measuredInstance -> measuredInstance.profiledMetrics().stream())
/* 27 */       .collect(Collectors.groupingBy(MetricSampler::getName));
/*    */     
/* 29 */     return aggregateDuplicates(samplersByName);
/*    */   }
/*    */   
/*    */   private static List<MetricSampler> aggregateDuplicates(Map<String, List<MetricSampler>> potentialDuplicates) {
/* 33 */     return (List<MetricSampler>)potentialDuplicates.entrySet().stream()
/* 34 */       .map(entry -> {
/*    */           String samplerName = (String)entry.getKey();
/*    */           
/*    */           List<MetricSampler> duplicateSamplers = (List<MetricSampler>)entry.getValue();
/*    */           return (duplicateSamplers.size() > 1) ? new AggregatedMetricSampler(samplerName, duplicateSamplers) : duplicateSamplers.get(0);
/* 39 */         }).collect(Collectors.toList());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static class AggregatedMetricSampler
/*    */     extends MetricSampler
/*    */   {
/*    */     private final List<MetricSampler> delegates;
/*    */ 
/*    */     
/*    */     private AggregatedMetricSampler(String name, List<MetricSampler> delegates) {
/* 51 */       super(name, ((MetricSampler)delegates.get(0)).getCategory(), () -> averageValueFromDelegates(delegates), () -> beforeTick(delegates), thresholdTest(delegates));
/* 52 */       this.delegates = delegates;
/*    */     }
/*    */     
/*    */     private static MetricSampler.ThresholdTest thresholdTest(List<MetricSampler> delegates) {
/* 56 */       return value -> delegates.stream().anyMatch(());
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     private static void beforeTick(List<MetricSampler> delegates) {
/* 65 */       for (MetricSampler delegate : delegates) {
/* 66 */         delegate.onStartTick();
/*    */       }
/*    */     }
/*    */     
/*    */     private static double averageValueFromDelegates(List<MetricSampler> delegates) {
/* 71 */       double aggregatedValue = 0.0D;
/*    */       
/* 73 */       for (MetricSampler delegate : delegates) {
/* 74 */         aggregatedValue += delegate.getSampler().getAsDouble();
/*    */       }
/*    */       
/* 77 */       return aggregatedValue / delegates.size();
/*    */     }
/*    */ 
/*    */     
/*    */     public boolean equals(Object o) {
/* 82 */       if (this == o) {
/* 83 */         return true;
/*    */       }
/* 85 */       if (o == null || getClass() != o.getClass()) {
/* 86 */         return false;
/*    */       }
/* 88 */       if (!super.equals(o)) {
/* 89 */         return false;
/*    */       }
/* 91 */       AggregatedMetricSampler that = (AggregatedMetricSampler)o;
/* 92 */       return this.delegates.equals(that.delegates);
/*    */     }
/*    */ 
/*    */     
/*    */     public int hashCode() {
/* 97 */       return Objects.hash(new Object[] { super.hashCode(), this.delegates });
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/metrics/MetricsRegistry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */