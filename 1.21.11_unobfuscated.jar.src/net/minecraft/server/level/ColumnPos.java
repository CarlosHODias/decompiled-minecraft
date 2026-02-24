/*    */ package net.minecraft.server.level;public final class ColumnPos extends Record { private final int x;
/*    */   private final int z;
/*    */   private static final long COORD_BITS = 32L;
/*    */   private static final long COORD_MASK = 4294967295L;
/*    */   
/*  6 */   public ColumnPos(int x, int z) { this.x = x; this.z = z; } public int x() { return this.x; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/ColumnPos;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/level/ColumnPos;
/*  6 */     //   0	8	1	o	Ljava/lang/Object; } public int z() { return this.z; }
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.world.level.ChunkPos toChunkPos() {
/* 11 */     return new net.minecraft.world.level.ChunkPos(net.minecraft.core.SectionPos.blockToSectionCoord(this.x), net.minecraft.core.SectionPos.blockToSectionCoord(this.z));
/*    */   }
/*    */   
/*    */   public long toLong() {
/* 15 */     return asLong(this.x, this.z);
/*    */   }
/*    */   
/*    */   public static long asLong(int x, int z) {
/* 19 */     return x & 0xFFFFFFFFL | (z & 0xFFFFFFFFL) << 32L;
/*    */   }
/*    */   
/*    */   public static int getX(long pos) {
/* 23 */     return (int)(pos & 0xFFFFFFFFL);
/*    */   }
/*    */   
/*    */   public static int getZ(long pos) {
/* 27 */     return (int)(pos >>> 32L & 0xFFFFFFFFL);
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 32 */     return "[" + this.x + ", " + this.z + "]";
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 37 */     return net.minecraft.world.level.ChunkPos.hash(this.x, this.z);
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/level/ColumnPos.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */