/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.google.common.collect.ImmutableMap;
/*     */ import com.mojang.blaze3d.GraphicsWorkarounds;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer;
/*     */ import com.mojang.blaze3d.buffers.GpuBuffer.Usage;
/*     */ import com.mojang.blaze3d.systems.CommandEncoder;
/*     */ import com.mojang.blaze3d.systems.GpuDevice;
/*     */ import com.mojang.blaze3d.systems.RenderSystem;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.function.Supplier;
/*     */ 
/*     */ 
/*     */ public class VertexFormat
/*     */ {
/*     */   public static final int UNKNOWN_ELEMENT = -1;
/*     */   private final List<VertexFormatElement> elements;
/*     */   private final List<String> names;
/*     */   private final int vertexSize;
/*     */   private final int elementsMask;
/*  26 */   private final int[] offsetsByElement = new int[32];
/*     */   
/*     */   private GpuBuffer immediateDrawVertexBuffer;
/*     */   private GpuBuffer immediateDrawIndexBuffer;
/*     */   
/*     */   private VertexFormat(List<VertexFormatElement> elements, List<String> names, IntList offsets, int vertexSize) {
/*  32 */     this.elements = elements;
/*  33 */     this.names = names;
/*  34 */     this.vertexSize = vertexSize;
/*  35 */     this.elementsMask = elements.stream().mapToInt(VertexFormatElement::mask).reduce(0, (left, right) -> left | right);
/*  36 */     for (int id = 0; id < this.offsetsByElement.length; id++) {
/*  37 */       VertexFormatElement element = VertexFormatElement.byId(id);
/*  38 */       int index = (element != null) ? elements.indexOf(element) : -1;
/*  39 */       this.offsetsByElement[id] = (index != -1) ? offsets.getInt(index) : -1;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Builder builder() {
/*  44 */     return new Builder();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  49 */     return "VertexFormat" + String.valueOf(this.names);
/*     */   }
/*     */   
/*     */   public int getVertexSize() {
/*  53 */     return this.vertexSize;
/*     */   }
/*     */   
/*     */   public List<VertexFormatElement> getElements() {
/*  57 */     return this.elements;
/*     */   }
/*     */   
/*     */   public List<String> getElementAttributeNames() {
/*  61 */     return this.names;
/*     */   }
/*     */   
/*     */   public int[] getOffsetsByElement() {
/*  65 */     return this.offsetsByElement;
/*     */   }
/*     */   
/*     */   public int getOffset(VertexFormatElement element) {
/*  69 */     return this.offsetsByElement[element.id()];
/*     */   }
/*     */   
/*     */   public boolean contains(VertexFormatElement element) {
/*  73 */     return ((this.elementsMask & element.mask()) != 0);
/*     */   }
/*     */   
/*     */   public int getElementsMask() {
/*  77 */     return this.elementsMask;
/*     */   }
/*     */   
/*     */   public String getElementName(VertexFormatElement element) {
/*  81 */     int index = this.elements.indexOf(element);
/*  82 */     if (index == -1) {
/*  83 */       throw new IllegalArgumentException(String.valueOf(element) + " is not contained in format");
/*     */     }
/*  85 */     return this.names.get(index);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/*     */     // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: if_acmpne -> 7
/*     */     //   5: iconst_1
/*     */     //   6: ireturn
/*     */     //   7: aload_1
/*     */     //   8: instanceof com/mojang/blaze3d/vertex/VertexFormat
/*     */     //   11: ifeq -> 75
/*     */     //   14: aload_1
/*     */     //   15: checkcast com/mojang/blaze3d/vertex/VertexFormat
/*     */     //   18: astore_2
/*     */     //   19: aload_0
/*     */     //   20: getfield elementsMask : I
/*     */     //   23: aload_2
/*     */     //   24: getfield elementsMask : I
/*     */     //   27: if_icmpne -> 75
/*     */     //   30: aload_0
/*     */     //   31: getfield vertexSize : I
/*     */     //   34: aload_2
/*     */     //   35: getfield vertexSize : I
/*     */     //   38: if_icmpne -> 75
/*     */     //   41: aload_0
/*     */     //   42: getfield names : Ljava/util/List;
/*     */     //   45: aload_2
/*     */     //   46: getfield names : Ljava/util/List;
/*     */     //   49: invokeinterface equals : (Ljava/lang/Object;)Z
/*     */     //   54: ifeq -> 75
/*     */     //   57: aload_0
/*     */     //   58: getfield offsetsByElement : [I
/*     */     //   61: aload_2
/*     */     //   62: getfield offsetsByElement : [I
/*     */     //   65: invokestatic equals : ([I[I)Z
/*     */     //   68: ifeq -> 75
/*     */     //   71: iconst_1
/*     */     //   72: goto -> 76
/*     */     //   75: iconst_0
/*     */     //   76: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #90	-> 0
/*     */     //   #91	-> 5
/*     */     //   #97	-> 7
/*     */     //   #93	-> 14
/*     */     //   #96	-> 49
/*     */     //   #97	-> 65
/*     */     //   #93	-> 76
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   19	56	2	format	Lcom/mojang/blaze3d/vertex/VertexFormat;
/*     */     //   0	77	0	this	Lcom/mojang/blaze3d/vertex/VertexFormat;
/*     */     //   0	77	1	o	Ljava/lang/Object;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 102 */     return this.elementsMask * 31 + Arrays.hashCode(this.offsetsByElement);
/*     */   }
/*     */   
/*     */   private static GpuBuffer uploadToBuffer(GpuBuffer target, ByteBuffer buffer, @GpuBuffer.Usage int usage, Supplier<String> label) {
/* 106 */     GpuDevice device = RenderSystem.getDevice();
/* 107 */     if (GraphicsWorkarounds.get(device).alwaysCreateFreshImmediateBuffer()) {
/* 108 */       if (target != null) {
/* 109 */         target.close();
/*     */       }
/* 111 */       return device.createBuffer(label, usage, buffer);
/*     */     } 
/* 113 */     if (target == null) {
/* 114 */       target = device.createBuffer(label, usage, buffer);
/*     */     } else {
/* 116 */       CommandEncoder encoder = device.createCommandEncoder();
/* 117 */       if (target.size() < buffer.remaining()) {
/* 118 */         target.close();
/* 119 */         target = device.createBuffer(label, usage, buffer);
/*     */       } else {
/* 121 */         encoder.writeToBuffer(target.slice(), buffer);
/*     */       } 
/*     */     } 
/* 124 */     return target;
/*     */   }
/*     */   
/*     */   public GpuBuffer uploadImmediateVertexBuffer(ByteBuffer buffer) {
/* 128 */     this.immediateDrawVertexBuffer = uploadToBuffer(this.immediateDrawVertexBuffer, buffer, 40, () -> "Immediate vertex buffer for " + String.valueOf(this));
/* 129 */     return this.immediateDrawVertexBuffer;
/*     */   }
/*     */   
/*     */   public GpuBuffer uploadImmediateIndexBuffer(ByteBuffer buffer) {
/* 133 */     this.immediateDrawIndexBuffer = uploadToBuffer(this.immediateDrawIndexBuffer, buffer, 72, () -> "Immediate index buffer for " + String.valueOf(this));
/* 134 */     return this.immediateDrawIndexBuffer;
/*     */   }
/*     */   
/*     */   public static class Builder {
/* 138 */     private final ImmutableMap.Builder<String, VertexFormatElement> elements = ImmutableMap.builder();
/* 139 */     private final IntList offsets = (IntList)new IntArrayList();
/*     */ 
/*     */     
/*     */     private int offset;
/*     */ 
/*     */     
/*     */     public Builder add(String name, VertexFormatElement element) {
/* 146 */       this.elements.put(name, element);
/* 147 */       this.offsets.add(this.offset);
/* 148 */       this.offset += element.byteSize();
/* 149 */       return this;
/*     */     }
/*     */     
/*     */     public Builder padding(int bytes) {
/* 153 */       this.offset += bytes;
/* 154 */       return this;
/*     */     }
/*     */     
/*     */     public VertexFormat build() {
/* 158 */       ImmutableMap<String, VertexFormatElement> elementMap = this.elements.buildOrThrow();
/* 159 */       ImmutableList<VertexFormatElement> elements = elementMap.values().asList();
/* 160 */       ImmutableList<String> names = elementMap.keySet().asList();
/* 161 */       return new VertexFormat((List<VertexFormatElement>)elements, (List<String>)names, this.offsets, this.offset);
/*     */     }
/*     */   }
/*     */   
/*     */   public enum IndexType {
/* 166 */     SHORT(2),
/* 167 */     INT(4);
/*     */     
/*     */     public final int bytes;
/*     */     
/*     */     IndexType(int bytes) {
/* 172 */       this.bytes = bytes;
/*     */     }
/*     */     
/*     */     public static IndexType least(int length) {
/* 176 */       if ((length & 0xFFFF0000) != 0) {
/* 177 */         return INT;
/*     */       }
/* 179 */       return SHORT;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Mode {
/* 184 */     LINES(2, 2, false),
/* 185 */     DEBUG_LINES(2, 2, false),
/* 186 */     DEBUG_LINE_STRIP(2, 1, true),
/* 187 */     POINTS(1, 1, false),
/* 188 */     TRIANGLES(3, 3, false),
/* 189 */     TRIANGLE_STRIP(3, 1, true),
/* 190 */     TRIANGLE_FAN(3, 1, true),
/* 191 */     QUADS(4, 4, false);
/*     */     
/*     */     public final int primitiveLength;
/*     */     public final int primitiveStride;
/*     */     public final boolean connectedPrimitives;
/*     */     
/*     */     Mode(int primitiveLength, int primitiveStride, boolean connectedPrimitives) {
/* 198 */       this.primitiveLength = primitiveLength;
/* 199 */       this.primitiveStride = primitiveStride;
/* 200 */       this.connectedPrimitives = connectedPrimitives;
/*     */     }
/*     */     
/*     */     public int indexCount(int vertexCount) {
/*     */       int indexCount;
/* 205 */       switch (ordinal()) {
/*     */         case 1:
/*     */         case 2:
/*     */         case 3:
/*     */         case 4:
/*     */         case 5:
/*     */         case 6:
/* 212 */           indexCount = vertexCount;
/*     */           break;
/*     */         case 0:
/*     */         case 7:
/* 216 */           indexCount = vertexCount / 4 * 6;
/*     */           break;
/*     */         default:
/* 219 */           indexCount = 0;
/*     */           break;
/*     */       } 
/* 222 */       return indexCount;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/vertex/VertexFormat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */