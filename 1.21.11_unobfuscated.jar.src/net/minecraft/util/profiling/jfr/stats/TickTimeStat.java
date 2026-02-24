/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ public final class TickTimeStat extends Record { private final java.time.Instant timestamp; private final java.time.Duration currentAverage;
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/TickTimeStat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/TickTimeStat;
/*    */   }
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/TickTimeStat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/TickTimeStat;
/*    */   }
/*  9 */   public TickTimeStat(java.time.Instant timestamp, java.time.Duration currentAverage) { this.timestamp = timestamp; this.currentAverage = currentAverage; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/TickTimeStat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/TickTimeStat;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public java.time.Instant timestamp() { return this.timestamp; } public java.time.Duration currentAverage() { return this.currentAverage; }
/*    */    public static TickTimeStat from(jdk.jfr.consumer.RecordedEvent event) {
/* 11 */     return new TickTimeStat(event.getStartTime(), event.getDuration("averageTickDuration"));
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/jfr/stats/TickTimeStat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */