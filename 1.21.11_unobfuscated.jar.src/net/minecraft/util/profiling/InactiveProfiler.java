/*    */ package net.minecraft.util.profiling;
/*    */ 
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Set;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.util.profiling.metrics.MetricCategory;
/*    */ import org.apache.commons.lang3.tuple.Pair;
/*    */ 
/*    */ public class InactiveProfiler
/*    */   implements ProfileCollector
/*    */ {
/* 12 */   public static final InactiveProfiler INSTANCE = new InactiveProfiler();
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void startTick() {}
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void endTick() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void push(String name) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void push(Supplier<String> name) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void markForCharting(MetricCategory category) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void pop() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void popPush(String name) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void popPush(Supplier<String> name) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public Zone zone(String name) {
/* 51 */     return Zone.INACTIVE;
/*    */   }
/*    */ 
/*    */   
/*    */   public Zone zone(Supplier<String> name) {
/* 56 */     return Zone.INACTIVE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void incrementCounter(String name, int amount) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void incrementCounter(Supplier<String> name, int amount) {}
/*    */ 
/*    */   
/*    */   public ProfileResults getResults() {
/* 69 */     return EmptyProfileResults.EMPTY;
/*    */   }
/*    */ 
/*    */   
/*    */   public ActiveProfiler.PathEntry getEntry(String path) {
/* 74 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<Pair<String, MetricCategory>> getChartedPaths() {
/* 79 */     return (Set<Pair<String, MetricCategory>>)ImmutableSet.of();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/InactiveProfiler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */