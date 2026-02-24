/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ 
/*    */ 
/*    */ public final class TimedStatSummary<T extends TimedStat> extends Record {
/*    */   private final T fastest;
/*    */   private final T slowest;
/*    */   private final T secondSlowest;
/*    */   private final int count;
/*    */   private final java.util.Map<Integer, Double> percentilesNanos;
/*    */   private final java.time.Duration totalDuration;
/*    */   
/* 12 */   public TimedStatSummary(T fastest, T slowest, T secondSlowest, int count, java.util.Map<Integer, Double> percentilesNanos, java.time.Duration totalDuration) { this.fastest = fastest; this.slowest = slowest; this.secondSlowest = secondSlowest; this.count = count; this.percentilesNanos = percentilesNanos; this.totalDuration = totalDuration; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 12 */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary<TT;>; } public T fastest() { return this.fastest; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 12 */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/TimedStatSummary<TT;>; } public T slowest() { return this.slowest; } public T secondSlowest() { return this.secondSlowest; } public int count() { return this.count; } public java.util.Map<Integer, Double> percentilesNanos() { return this.percentilesNanos; } public java.time.Duration totalDuration() { return this.totalDuration; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T extends TimedStat> java.util.Optional<TimedStatSummary<T>> summary(java.util.List<T> values) {
/* 21 */     if (values.isEmpty()) {
/* 22 */       return java.util.Optional.empty();
/*    */     }
/* 24 */     java.util.List<T> sorted = values.stream().sorted(java.util.Comparator.comparing(TimedStat::duration)).toList();
/* 25 */     java.time.Duration totalDuration = sorted.stream().map(TimedStat::duration).reduce(java.time.Duration::plus).orElse(java.time.Duration.ZERO);
/* 26 */     TimedStat timedStat1 = (TimedStat)sorted.getFirst();
/* 27 */     TimedStat timedStat2 = (TimedStat)sorted.getLast();
/* 28 */     TimedStat timedStat3 = (sorted.size() > 1) ? (TimedStat)sorted.get(sorted.size() - 2) : null;
/* 29 */     int count = sorted.size();
/* 30 */     java.util.Map<Integer, Double> percentilesNanos = net.minecraft.util.profiling.jfr.Percentiles.evaluate(sorted.stream().mapToLong(it -> it.duration().toNanos()).toArray());
/* 31 */     return java.util.Optional.of(new TimedStatSummary<>((T)timedStat1, (T)timedStat2, (T)timedStat3, count, percentilesNanos, totalDuration));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/jfr/stats/TimedStatSummary.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */