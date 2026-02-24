/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import java.nio.ByteOrder;
/*     */ import java.util.Objects;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.util.ARGB;
/*     */ import net.minecraft.util.Mth;
/*     */ import org.lwjgl.system.MemoryUtil;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BufferBuilder
/*     */   implements VertexConsumer
/*     */ {
/*     */   private static final int MAX_VERTEX_COUNT = 16777215;
/*     */   private static final long NOT_BUILDING = -1L;
/*     */   private static final long UNKNOWN_ELEMENT = -1L;
/*  18 */   private static final boolean IS_LITTLE_ENDIAN = (ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN);
/*     */   
/*     */   private final ByteBufferBuilder buffer;
/*     */   
/*  22 */   private long vertexPointer = -1L;
/*     */   
/*     */   private int vertices;
/*     */   
/*     */   private final VertexFormat format;
/*     */   
/*     */   private final VertexFormat.Mode mode;
/*     */   private final boolean fastFormat;
/*     */   private final boolean fullFormat;
/*     */   private final int vertexSize;
/*     */   private final int initialElementsToFill;
/*     */   private final int[] offsetsByElement;
/*     */   private int elementsToFill;
/*     */   private boolean building = true;
/*     */   
/*     */   public BufferBuilder(ByteBufferBuilder buffer, VertexFormat.Mode mode, VertexFormat format) {
/*  38 */     if (!format.contains(VertexFormatElement.POSITION)) {
/*  39 */       throw new IllegalArgumentException("Cannot build mesh with no position element");
/*     */     }
/*     */     
/*  42 */     this.buffer = buffer;
/*  43 */     this.mode = mode;
/*  44 */     this.format = format;
/*     */     
/*  46 */     this.vertexSize = format.getVertexSize();
/*  47 */     this.initialElementsToFill = format.getElementsMask() & (VertexFormatElement.POSITION.mask() ^ 0xFFFFFFFF);
/*  48 */     this.offsetsByElement = format.getOffsetsByElement();
/*     */     
/*  50 */     boolean isFullFormat = (format == DefaultVertexFormat.NEW_ENTITY);
/*  51 */     boolean isBlockFormat = (format == DefaultVertexFormat.BLOCK);
/*  52 */     this.fastFormat = (isFullFormat || isBlockFormat);
/*  53 */     this.fullFormat = isFullFormat;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MeshData build() {
/*  60 */     ensureBuilding();
/*  61 */     endLastVertex();
/*  62 */     MeshData mesh = storeMesh();
/*  63 */     this.building = false;
/*  64 */     this.vertexPointer = -1L;
/*  65 */     return mesh;
/*     */   }
/*     */   
/*     */   public MeshData buildOrThrow() {
/*  69 */     MeshData buffer = build();
/*  70 */     if (buffer == null) {
/*  71 */       throw new IllegalStateException("BufferBuilder was empty");
/*     */     }
/*  73 */     return buffer;
/*     */   }
/*     */   
/*     */   private void ensureBuilding() {
/*  77 */     if (!this.building) {
/*  78 */       throw new IllegalStateException("Not building!");
/*     */     }
/*     */   }
/*     */   
/*     */   private MeshData storeMesh() {
/*  83 */     if (this.vertices == 0) {
/*  84 */       return null;
/*     */     }
/*  86 */     ByteBufferBuilder.Result vertexBuffer = this.buffer.build();
/*  87 */     if (vertexBuffer == null) {
/*  88 */       return null;
/*     */     }
/*  90 */     int indices = this.mode.indexCount(this.vertices);
/*  91 */     VertexFormat.IndexType indexType = VertexFormat.IndexType.least(this.vertices);
/*  92 */     return new MeshData(vertexBuffer, new MeshData.DrawState(this.format, this.vertices, indices, this.mode, indexType));
/*     */   }
/*     */   
/*     */   private long beginVertex() {
/*  96 */     ensureBuilding();
/*  97 */     endLastVertex();
/*  98 */     if (this.vertices >= 16777215) {
/*  99 */       throw new IllegalStateException("Trying to write too many vertices (>16777215) into BufferBuilder");
/*     */     }
/* 101 */     this.vertices++;
/* 102 */     long pointer = this.buffer.reserve(this.vertexSize);
/* 103 */     this.vertexPointer = pointer;
/* 104 */     return pointer;
/*     */   }
/*     */   
/*     */   private long beginElement(VertexFormatElement element) {
/* 108 */     int oldElements = this.elementsToFill;
/* 109 */     int newElements = oldElements & (element.mask() ^ 0xFFFFFFFF);
/* 110 */     if (newElements == oldElements) {
/* 111 */       return -1L;
/*     */     }
/* 113 */     this.elementsToFill = newElements;
/*     */     
/* 115 */     long vertexPointer = this.vertexPointer;
/* 116 */     if (vertexPointer == -1L) {
/* 117 */       throw new IllegalArgumentException("Not currently building vertex");
/*     */     }
/* 119 */     return vertexPointer + this.offsetsByElement[element.id()];
/*     */   }
/*     */   
/*     */   private void endLastVertex() {
/* 123 */     if (this.vertices == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 127 */     if (this.elementsToFill != 0) {
/*     */       
/* 129 */       Objects.requireNonNull(this.format); String missingElements = VertexFormatElement.elementsFromMask(this.elementsToFill).<CharSequence>map(this.format::getElementName)
/* 130 */         .collect(Collectors.joining(", "));
/* 131 */       throw new IllegalStateException("Missing elements in vertex: " + missingElements);
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 136 */     if (this.mode == VertexFormat.Mode.LINES) {
/* 137 */       long pointer = this.buffer.reserve(this.vertexSize);
/* 138 */       MemoryUtil.memCopy(pointer - this.vertexSize, pointer, this.vertexSize);
/* 139 */       this.vertices++;
/*     */     } 
/*     */   }
/*     */   
/*     */   private static void putRgba(long pointer, int argb) {
/* 144 */     int abgr = ARGB.toABGR(argb);
/*     */     
/* 146 */     MemoryUtil.memPutInt(pointer, IS_LITTLE_ENDIAN ? abgr : Integer.reverseBytes(abgr));
/*     */   }
/*     */   
/*     */   private static void putPackedUv(long pointer, int packedUv) {
/* 150 */     if (IS_LITTLE_ENDIAN) {
/* 151 */       MemoryUtil.memPutInt(pointer, packedUv);
/*     */     } else {
/* 153 */       MemoryUtil.memPutShort(pointer, (short)(packedUv & 0xFFFF));
/* 154 */       MemoryUtil.memPutShort(pointer + 2L, (short)(packedUv >> 16 & 0xFFFF));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public VertexConsumer addVertex(float x, float y, float z) {
/* 160 */     long pointer = beginVertex() + this.offsetsByElement[VertexFormatElement.POSITION.id()];
/* 161 */     this.elementsToFill = this.initialElementsToFill;
/* 162 */     MemoryUtil.memPutFloat(pointer, x);
/* 163 */     MemoryUtil.memPutFloat(pointer + 4L, y);
/* 164 */     MemoryUtil.memPutFloat(pointer + 8L, z);
/* 165 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VertexConsumer setColor(int r, int g, int b, int a) {
/* 170 */     long pointer = beginElement(VertexFormatElement.COLOR);
/* 171 */     if (pointer != -1L) {
/* 172 */       MemoryUtil.memPutByte(pointer, (byte)r);
/* 173 */       MemoryUtil.memPutByte(pointer + 1L, (byte)g);
/* 174 */       MemoryUtil.memPutByte(pointer + 2L, (byte)b);
/* 175 */       MemoryUtil.memPutByte(pointer + 3L, (byte)a);
/*     */     } 
/* 177 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VertexConsumer setColor(int color) {
/* 182 */     long pointer = beginElement(VertexFormatElement.COLOR);
/* 183 */     if (pointer != -1L) {
/* 184 */       putRgba(pointer, color);
/*     */     }
/* 186 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VertexConsumer setUv(float u, float v) {
/* 191 */     long pointer = beginElement(VertexFormatElement.UV0);
/* 192 */     if (pointer != -1L) {
/* 193 */       MemoryUtil.memPutFloat(pointer, u);
/* 194 */       MemoryUtil.memPutFloat(pointer + 4L, v);
/*     */     } 
/* 196 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VertexConsumer setUv1(int u, int v) {
/* 201 */     return uvShort((short)u, (short)v, VertexFormatElement.UV1);
/*     */   }
/*     */ 
/*     */   
/*     */   public VertexConsumer setOverlay(int packedOverlayCoords) {
/* 206 */     long pointer = beginElement(VertexFormatElement.UV1);
/* 207 */     if (pointer != -1L) {
/* 208 */       putPackedUv(pointer, packedOverlayCoords);
/*     */     }
/* 210 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VertexConsumer setUv2(int u, int v) {
/* 215 */     return uvShort((short)u, (short)v, VertexFormatElement.UV2);
/*     */   }
/*     */ 
/*     */   
/*     */   public VertexConsumer setLight(int packedLightCoords) {
/* 220 */     long pointer = beginElement(VertexFormatElement.UV2);
/* 221 */     if (pointer != -1L) {
/* 222 */       putPackedUv(pointer, packedLightCoords);
/*     */     }
/* 224 */     return this;
/*     */   }
/*     */   
/*     */   private VertexConsumer uvShort(short u, short v, VertexFormatElement element) {
/* 228 */     long pointer = beginElement(element);
/* 229 */     if (pointer != -1L) {
/* 230 */       MemoryUtil.memPutShort(pointer, u);
/* 231 */       MemoryUtil.memPutShort(pointer + 2L, v);
/*     */     } 
/* 233 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VertexConsumer setNormal(float x, float y, float z) {
/* 238 */     long pointer = beginElement(VertexFormatElement.NORMAL);
/* 239 */     if (pointer != -1L) {
/* 240 */       MemoryUtil.memPutByte(pointer, normalIntValue(x));
/* 241 */       MemoryUtil.memPutByte(pointer + 1L, normalIntValue(y));
/* 242 */       MemoryUtil.memPutByte(pointer + 2L, normalIntValue(z));
/*     */     } 
/* 244 */     return this;
/*     */   }
/*     */ 
/*     */   
/*     */   public VertexConsumer setLineWidth(float width) {
/* 249 */     long pointer = beginElement(VertexFormatElement.LINE_WIDTH);
/* 250 */     if (pointer != -1L) {
/* 251 */       MemoryUtil.memPutFloat(pointer, width);
/*     */     }
/* 253 */     return this;
/*     */   }
/*     */   
/*     */   private static byte normalIntValue(float c) {
/* 257 */     return (byte)((int)(Mth.clamp(c, -1.0F, 1.0F) * 127.0F) & 0xFF);
/*     */   }
/*     */ 
/*     */   
/*     */   public void addVertex(float x, float y, float z, int color, float u, float v, int overlayCoords, int lightCoords, float nx, float ny, float nz) {
/* 262 */     if (this.fastFormat) {
/* 263 */       long lightStart; long pointer = beginVertex();
/*     */ 
/*     */       
/* 266 */       MemoryUtil.memPutFloat(pointer + 0L, x);
/* 267 */       MemoryUtil.memPutFloat(pointer + 4L, y);
/* 268 */       MemoryUtil.memPutFloat(pointer + 8L, z);
/*     */       
/* 270 */       putRgba(pointer + 12L, color);
/*     */       
/* 272 */       MemoryUtil.memPutFloat(pointer + 16L, u);
/* 273 */       MemoryUtil.memPutFloat(pointer + 20L, v);
/*     */ 
/*     */       
/* 276 */       if (this.fullFormat) {
/* 277 */         putPackedUv(pointer + 24L, overlayCoords);
/* 278 */         lightStart = pointer + 28L;
/*     */       } else {
/* 280 */         lightStart = pointer + 24L;
/*     */       } 
/*     */       
/* 283 */       putPackedUv(lightStart + 0L, lightCoords);
/*     */       
/* 285 */       MemoryUtil.memPutByte(lightStart + 4L, normalIntValue(nx));
/* 286 */       MemoryUtil.memPutByte(lightStart + 5L, normalIntValue(ny));
/* 287 */       MemoryUtil.memPutByte(lightStart + 6L, normalIntValue(nz));
/*     */       
/*     */       return;
/*     */     } 
/* 291 */     super.addVertex(x, y, z, color, u, v, overlayCoords, lightCoords, nx, ny, nz);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/vertex/BufferBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */