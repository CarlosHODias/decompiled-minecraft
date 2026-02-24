/*    */ package net.minecraft.util.debug;
/*    */ 
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.world.level.levelgen.structure.BoundingBox;
/*    */ 
/*    */ public final class DebugStructureInfo extends Record {
/*    */   private final BoundingBox boundingBox;
/*    */   private final java.util.List<Piece> pieces;
/*    */   
/* 10 */   public DebugStructureInfo(BoundingBox boundingBox, java.util.List<Piece> pieces) { this.boundingBox = boundingBox; this.pieces = pieces; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugStructureInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugStructureInfo; } public BoundingBox boundingBox() { return this.boundingBox; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugStructureInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugStructureInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugStructureInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugStructureInfo;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.List<Piece> pieces() { return this.pieces; }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final StreamCodec<io.netty.buffer.ByteBuf, DebugStructureInfo> STREAM_CODEC = StreamCodec.composite(BoundingBox.STREAM_CODEC, DebugStructureInfo::boundingBox, 
/*    */       
/* 16 */       Piece.STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs.list()), DebugStructureInfo::pieces, DebugStructureInfo::new);
/*    */   public static final class Piece extends Record { private final BoundingBox boundingBox;
/*    */     private final boolean isStart;
/*    */     
/* 20 */     public Piece(BoundingBox boundingBox, boolean isStart) { this.boundingBox = boundingBox; this.isStart = isStart; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugStructureInfo$Piece;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #20	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/debug/DebugStructureInfo$Piece; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugStructureInfo$Piece;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #20	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/util/debug/DebugStructureInfo$Piece; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugStructureInfo$Piece;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #20	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/util/debug/DebugStructureInfo$Piece;
/* 20 */       //   0	8	1	o	Ljava/lang/Object; } public BoundingBox boundingBox() { return this.boundingBox; } public boolean isStart() { return this.isStart; }
/* 21 */      public static final StreamCodec<io.netty.buffer.ByteBuf, Piece> STREAM_CODEC = StreamCodec.composite(BoundingBox.STREAM_CODEC, Piece::boundingBox, net.minecraft.network.codec.ByteBufCodecs.BOOL, Piece::isStart, Piece::new); }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/debug/DebugStructureInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */