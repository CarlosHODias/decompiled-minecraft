/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ 
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import java.time.Duration;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ 
/*    */ public final class IoSummary<T>
/*    */ {
/*    */   private final CountAndSize totalCountAndSize;
/*    */   private final List<Pair<T, CountAndSize>> largestSizeContributors;
/*    */   private final Duration recordingDuration;
/*    */   
/*    */   public IoSummary(Duration recordingDuration, List<Pair<T, CountAndSize>> packetStats) {
/* 15 */     this.recordingDuration = recordingDuration;
/* 16 */     this
/*    */       
/* 18 */       .totalCountAndSize = packetStats.stream().map(Pair::getSecond).reduce(new CountAndSize(0L, 0L), CountAndSize::add);
/*    */     
/* 20 */     this
/*    */ 
/*    */       
/* 23 */       .largestSizeContributors = packetStats.stream().sorted(Comparator.comparing(Pair::getSecond, CountAndSize.SIZE_THEN_COUNT)).limit(10L).toList();
/*    */   }
/*    */   
/*    */   public double getCountsPerSecond() {
/* 27 */     return this.totalCountAndSize.totalCount / this.recordingDuration.getSeconds();
/*    */   }
/*    */   
/*    */   public double getSizePerSecond() {
/* 31 */     return this.totalCountAndSize.totalSize / this.recordingDuration.getSeconds();
/*    */   }
/*    */   
/*    */   public long getTotalCount() {
/* 35 */     return this.totalCountAndSize.totalCount;
/*    */   }
/*    */   
/*    */   public long getTotalSize() {
/* 39 */     return this.totalCountAndSize.totalSize;
/*    */   }
/*    */   
/*    */   public List<Pair<T, CountAndSize>> largestSizeContributors() {
/* 43 */     return this.largestSizeContributors;
/*    */   }
/*    */   public static final class CountAndSize extends Record { private final long totalCount; private final long totalSize;
/* 46 */     public CountAndSize(long totalCount, long totalSize) { this.totalCount = totalCount; this.totalSize = totalSize; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/IoSummary$CountAndSize;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #46	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/* 46 */       //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/IoSummary$CountAndSize; } public long totalCount() { return this.totalCount; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/IoSummary$CountAndSize;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #46	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/IoSummary$CountAndSize; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/IoSummary$CountAndSize;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #46	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/IoSummary$CountAndSize;
/* 46 */       //   0	8	1	o	Ljava/lang/Object; } public long totalSize() { return this.totalSize; }
/* 47 */      private static final Comparator<CountAndSize> SIZE_THEN_COUNT = Comparator.comparing(CountAndSize::totalSize).thenComparing(CountAndSize::totalCount).reversed();
/*    */     
/*    */     CountAndSize add(CountAndSize that) {
/* 50 */       return new CountAndSize(this.totalCount + that.totalCount, this.totalSize + that.totalSize);
/*    */     }
/*    */     
/*    */     public float averageSize() {
/* 54 */       return (float)this.totalSize / (float)this.totalCount;
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/jfr/stats/IoSummary.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */