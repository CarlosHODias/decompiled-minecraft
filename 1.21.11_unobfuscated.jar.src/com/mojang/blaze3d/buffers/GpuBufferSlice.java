/*    */ package com.mojang.blaze3d.buffers;public final class GpuBufferSlice extends Record { private final GpuBuffer buffer; private final long offset; private final long length;
/*    */   
/*  3 */   public GpuBufferSlice(GpuBuffer buffer, long offset, long length) { this.buffer = buffer; this.offset = offset; this.length = length; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  3 */     //   0	7	0	this	Lcom/mojang/blaze3d/buffers/GpuBufferSlice; } public GpuBuffer buffer() { return this.buffer; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/buffers/GpuBufferSlice; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #3	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/buffers/GpuBufferSlice;
/*  3 */     //   0	8	1	o	Ljava/lang/Object; } public long offset() { return this.offset; } public long length() { return this.length; }
/*    */ 
/*    */   
/*    */   public GpuBufferSlice slice(long offset, long length) {
/*  7 */     if (offset < 0L || length < 0L || offset + length > this.length) {
/*  8 */       throw new IllegalArgumentException("Offset of " + offset + " and length " + length + " would put new slice outside existing slice's range (of " + this.offset + "," + this.length + ")");
/*    */     }
/* 10 */     return new GpuBufferSlice(this.buffer, this.offset + offset, length);
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/buffers/GpuBufferSlice.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */