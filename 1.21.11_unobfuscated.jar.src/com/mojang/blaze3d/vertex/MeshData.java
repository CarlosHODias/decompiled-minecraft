/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import it.unimi.dsi.fastutil.ints.IntConsumer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.FloatBuffer;
/*     */ import org.apache.commons.lang3.mutable.MutableLong;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ public class MeshData
/*     */   implements AutoCloseable
/*     */ {
/*     */   private final ByteBufferBuilder.Result vertexBuffer;
/*     */   private ByteBufferBuilder.Result indexBuffer;
/*     */   private final DrawState drawState;
/*     */   
/*     */   public MeshData(ByteBufferBuilder.Result vertexBuffer, DrawState drawState) {
/*  17 */     this.vertexBuffer = vertexBuffer;
/*  18 */     this.drawState = drawState;
/*     */   }
/*     */   
/*     */   private static CompactVectorArray unpackQuadCentroids(ByteBuffer vertexBuffer, int vertices, VertexFormat format) {
/*  22 */     int positionOffset = format.getOffset(VertexFormatElement.POSITION);
/*  23 */     if (positionOffset == -1) {
/*  24 */       throw new IllegalArgumentException("Cannot identify quad centers with no position element");
/*     */     }
/*     */     
/*  27 */     FloatBuffer floatBuffer = vertexBuffer.asFloatBuffer();
/*  28 */     int vertexStride = format.getVertexSize() / 4;
/*  29 */     int quadStride = vertexStride * 4;
/*  30 */     int quads = vertices / 4;
/*     */     
/*  32 */     CompactVectorArray sortingPoints = new CompactVectorArray(quads);
/*     */     
/*  34 */     for (int i = 0; i < quads; i++) {
/*  35 */       int firstPosOffset = i * quadStride + positionOffset;
/*  36 */       int secondPosOffset = firstPosOffset + vertexStride * 2;
/*  37 */       float x0 = floatBuffer.get(firstPosOffset + 0);
/*  38 */       float y0 = floatBuffer.get(firstPosOffset + 1);
/*  39 */       float z0 = floatBuffer.get(firstPosOffset + 2);
/*  40 */       float x1 = floatBuffer.get(secondPosOffset + 0);
/*  41 */       float y1 = floatBuffer.get(secondPosOffset + 1);
/*  42 */       float z1 = floatBuffer.get(secondPosOffset + 2);
/*  43 */       float xMid = (x0 + x1) / 2.0F;
/*  44 */       float yMid = (y0 + y1) / 2.0F;
/*  45 */       float zMid = (z0 + z1) / 2.0F;
/*  46 */       sortingPoints.set(i, xMid, yMid, zMid);
/*     */     } 
/*     */     
/*  49 */     return sortingPoints;
/*     */   }
/*     */   
/*     */   public ByteBuffer vertexBuffer() {
/*  53 */     return this.vertexBuffer.byteBuffer();
/*     */   }
/*     */   
/*     */   public ByteBuffer indexBuffer() {
/*  57 */     return (this.indexBuffer != null) ? this.indexBuffer.byteBuffer() : null;
/*     */   }
/*     */   
/*     */   public DrawState drawState() {
/*  61 */     return this.drawState;
/*     */   }
/*     */   
/*     */   public SortState sortQuads(ByteBufferBuilder indexBufferTarget, VertexSorting sorting) {
/*  65 */     if (this.drawState.mode() != VertexFormat.Mode.QUADS) {
/*  66 */       return null;
/*     */     }
/*  68 */     CompactVectorArray centroids = unpackQuadCentroids(this.vertexBuffer.byteBuffer(), this.drawState.vertexCount(), this.drawState.format());
/*  69 */     SortState sortState = new SortState(centroids, this.drawState.indexType());
/*  70 */     this.indexBuffer = sortState.buildSortedIndexBuffer(indexBufferTarget, sorting);
/*  71 */     return sortState;
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() {
/*  76 */     this.vertexBuffer.close();
/*  77 */     if (this.indexBuffer != null)
/*  78 */       this.indexBuffer.close(); 
/*     */   }
/*     */   public static final class DrawState extends Record { private final VertexFormat format; private final int vertexCount; private final int indexCount; private final VertexFormat.Mode mode; private final VertexFormat.IndexType indexType;
/*     */     
/*  82 */     public DrawState(VertexFormat format, int vertexCount, int indexCount, VertexFormat.Mode mode, VertexFormat.IndexType indexType) { this.format = format; this.vertexCount = vertexCount; this.indexCount = indexCount; this.mode = mode; this.indexType = indexType; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vertex/MeshData$DrawState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #82	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  82 */       //   0	7	0	this	Lcom/mojang/blaze3d/vertex/MeshData$DrawState; } public VertexFormat format() { return this.format; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vertex/MeshData$DrawState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #82	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vertex/MeshData$DrawState; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vertex/MeshData$DrawState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #82	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/vertex/MeshData$DrawState;
/*  82 */       //   0	8	1	o	Ljava/lang/Object; } public int vertexCount() { return this.vertexCount; } public int indexCount() { return this.indexCount; } public VertexFormat.Mode mode() { return this.mode; } public VertexFormat.IndexType indexType() { return this.indexType; }
/*     */      }
/*     */   public static final class SortState extends Record { private final CompactVectorArray centroids; private final VertexFormat.IndexType indexType;
/*  85 */     public SortState(CompactVectorArray centroids, VertexFormat.IndexType indexType) { this.centroids = centroids; this.indexType = indexType; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/vertex/MeshData$SortState;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vertex/MeshData$SortState; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vertex/MeshData$SortState;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lcom/mojang/blaze3d/vertex/MeshData$SortState; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vertex/MeshData$SortState;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #85	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/blaze3d/vertex/MeshData$SortState;
/*  85 */       //   0	8	1	o	Ljava/lang/Object; } public CompactVectorArray centroids() { return this.centroids; } public VertexFormat.IndexType indexType() { return this.indexType; }
/*     */ 
/*     */ 
/*     */     
/*     */     public ByteBufferBuilder.Result buildSortedIndexBuffer(ByteBufferBuilder target, VertexSorting sorting) {
/*  90 */       int[] startIndices = sorting.sort(this.centroids);
/*     */       
/*  92 */       long pointer = target.reserve(startIndices.length * 6 * this.indexType.bytes);
/*  93 */       IntConsumer indexWriter = indexWriter(pointer, this.indexType);
/*  94 */       for (int startIndex : startIndices) {
/*  95 */         indexWriter.accept(startIndex * 4 + 0);
/*  96 */         indexWriter.accept(startIndex * 4 + 1);
/*  97 */         indexWriter.accept(startIndex * 4 + 2);
/*  98 */         indexWriter.accept(startIndex * 4 + 2);
/*  99 */         indexWriter.accept(startIndex * 4 + 3);
/* 100 */         indexWriter.accept(startIndex * 4 + 0);
/*     */       } 
/*     */       
/* 103 */       return target.build();
/*     */     }
/*     */     
/*     */     private IntConsumer indexWriter(long pointer, VertexFormat.IndexType indexType) {
/*     */       // Byte code:
/*     */       //   0: new org/apache/commons/lang3/mutable/MutableLong
/*     */       //   3: dup
/*     */       //   4: lload_1
/*     */       //   5: invokespecial <init> : (J)V
/*     */       //   8: astore #4
/*     */       //   10: getstatic com/mojang/blaze3d/vertex/MeshData$1.$SwitchMap$com$mojang$blaze3d$vertex$VertexFormat$IndexType : [I
/*     */       //   13: aload_3
/*     */       //   14: invokevirtual ordinal : ()I
/*     */       //   17: iaload
/*     */       //   18: lookupswitch default -> 44, 1 -> 54, 2 -> 64
/*     */       //   44: new java/lang/MatchException
/*     */       //   47: dup
/*     */       //   48: aconst_null
/*     */       //   49: aconst_null
/*     */       //   50: invokespecial <init> : (Ljava/lang/String;Ljava/lang/Throwable;)V
/*     */       //   53: athrow
/*     */       //   54: aload #4
/*     */       //   56: <illegal opcode> accept : (Lorg/apache/commons/lang3/mutable/MutableLong;)Lit/unimi/dsi/fastutil/ints/IntConsumer;
/*     */       //   61: goto -> 71
/*     */       //   64: aload #4
/*     */       //   66: <illegal opcode> accept : (Lorg/apache/commons/lang3/mutable/MutableLong;)Lit/unimi/dsi/fastutil/ints/IntConsumer;
/*     */       //   71: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #107	-> 0
/*     */       //   #108	-> 10
/*     */       //   #109	-> 54
/*     */       //   #110	-> 64
/*     */       //   #108	-> 71
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	72	0	this	Lcom/mojang/blaze3d/vertex/MeshData$SortState;
/*     */       //   0	72	1	pointer	J
/*     */       //   0	72	3	indexType	Lcom/mojang/blaze3d/vertex/VertexFormat$IndexType;
/*     */       //   10	62	4	nextIndex	Lorg/apache/commons/lang3/mutable/MutableLong;
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/vertex/MeshData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */