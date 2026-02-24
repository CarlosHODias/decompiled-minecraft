/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ 
/*    */ 
/*    */ public final class ChunkGenStat extends Record implements TimedStat {
/*    */   private final java.time.Duration duration;
/*    */   private final net.minecraft.world.level.ChunkPos chunkPos;
/*    */   private final net.minecraft.server.level.ColumnPos worldPos;
/*    */   private final net.minecraft.world.level.chunk.status.ChunkStatus status;
/*    */   private final String level;
/*    */   
/* 11 */   public ChunkGenStat(java.time.Duration duration, net.minecraft.world.level.ChunkPos chunkPos, net.minecraft.server.level.ColumnPos worldPos, net.minecraft.world.level.chunk.status.ChunkStatus status, String level) { this.duration = duration; this.chunkPos = chunkPos; this.worldPos = worldPos; this.status = status; this.level = level; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/ChunkGenStat;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ChunkGenStat; } public java.time.Duration duration() { return this.duration; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/ChunkGenStat;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ChunkGenStat; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/ChunkGenStat;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/ChunkGenStat;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.world.level.ChunkPos chunkPos() { return this.chunkPos; } public net.minecraft.server.level.ColumnPos worldPos() { return this.worldPos; } public net.minecraft.world.level.chunk.status.ChunkStatus status() { return this.status; } public String level() { return this.level; }
/*    */   
/*    */   public static ChunkGenStat from(jdk.jfr.consumer.RecordedEvent event) {
/* 14 */     return new ChunkGenStat(event.getDuration(), new net.minecraft.world.level.ChunkPos(
/* 15 */           event.getInt("chunkPosX"), event.getInt("chunkPosX")), new net.minecraft.server.level.ColumnPos(
/* 16 */           event.getInt("worldPosX"), event.getInt("worldPosZ")), 
/* 17 */         net.minecraft.world.level.chunk.status.ChunkStatus.byName(event.getString("status")), 
/* 18 */         event.getString("level"));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/jfr/stats/ChunkGenStat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */