/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ 
/*    */ public final class StructureGenStat extends Record implements TimedStat {
/*    */   private final java.time.Duration duration;
/*    */   private final net.minecraft.world.level.ChunkPos chunkPos;
/*    */   private final String structureName;
/*    */   private final String level;
/*    */   private final boolean success;
/*    */   
/* 10 */   public StructureGenStat(java.time.Duration duration, net.minecraft.world.level.ChunkPos chunkPos, String structureName, String level, boolean success) { this.duration = duration; this.chunkPos = chunkPos; this.structureName = structureName; this.level = level; this.success = success; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/StructureGenStat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/StructureGenStat; } public java.time.Duration duration() { return this.duration; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/StructureGenStat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/StructureGenStat; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/StructureGenStat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/StructureGenStat;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.world.level.ChunkPos chunkPos() { return this.chunkPos; } public String structureName() { return this.structureName; } public String level() { return this.level; } public boolean success() { return this.success; }
/*    */   
/*    */   public static StructureGenStat from(jdk.jfr.consumer.RecordedEvent event) {
/* 13 */     return new StructureGenStat(event.getDuration(), new net.minecraft.world.level.ChunkPos(
/* 14 */           event.getInt("chunkPosX"), event.getInt("chunkPosX")), 
/* 15 */         event.getString("structure"), 
/* 16 */         event.getString("level"), 
/* 17 */         event.getBoolean("success"));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/jfr/stats/StructureGenStat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */