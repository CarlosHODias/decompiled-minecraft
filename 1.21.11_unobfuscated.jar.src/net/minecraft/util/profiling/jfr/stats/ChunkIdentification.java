/*    */ package net.minecraft.util.profiling.jfr.stats;public final class ChunkIdentification extends Record { private final String level;
/*    */   private final String dimension;
/*    */   private final int x;
/*    */   private final int z;
/*    */   
/*  6 */   public ChunkIdentification(String level, String dimension, int x, int z) { this.level = level; this.dimension = dimension; this.x = x; this.z = z; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/ChunkIdentification;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  6 */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ChunkIdentification; } public String level() { return this.level; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/ChunkIdentification;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/ChunkIdentification; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/ChunkIdentification;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/ChunkIdentification;
/*  6 */     //   0	8	1	o	Ljava/lang/Object; } public String dimension() { return this.dimension; } public int x() { return this.x; } public int z() { return this.z; }
/*    */    public static ChunkIdentification from(jdk.jfr.consumer.RecordedEvent event) {
/*  8 */     return new ChunkIdentification(
/*  9 */         event.getString("level"), 
/* 10 */         event.getString("dimension"), 
/* 11 */         event.getInt("chunkPosX"), 
/* 12 */         event.getInt("chunkPosZ"));
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/jfr/stats/ChunkIdentification.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */