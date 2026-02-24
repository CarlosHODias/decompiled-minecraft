/*     */ package com.mojang.blaze3d.vertex;
/*     */ 
/*     */ public final class VertexFormatElement extends Record {
/*     */   private final int id;
/*     */   private final int index;
/*     */   private final Type type;
/*     */   private final Usage usage;
/*     */   
/*   9 */   public int id() { return this.id; } private final int count; public static final int MAX_COUNT = 32; public final int hashCode() { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/vertex/VertexFormatElement;)I
/*     */     //   6: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #9	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	7	0	this	Lcom/mojang/blaze3d/vertex/VertexFormatElement; } public final boolean equals(Object o) { // Byte code:
/*     */     //   0: aload_0
/*     */     //   1: aload_1
/*     */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/vertex/VertexFormatElement;Ljava/lang/Object;)Z
/*     */     //   7: ireturn
/*     */     // Line number table:
/*     */     //   Java source line number -> byte code offset
/*     */     //   #9	-> 0
/*     */     // Local variable table:
/*     */     //   start	length	slot	name	descriptor
/*     */     //   0	8	0	this	Lcom/mojang/blaze3d/vertex/VertexFormatElement;
/*   9 */     //   0	8	1	o	Ljava/lang/Object; } public int index() { return this.index; } public Type type() { return this.type; } public Usage usage() { return this.usage; } public int count() { return this.count; }
/*     */ 
/*     */   
/*  12 */   private static final VertexFormatElement[] BY_ID = new VertexFormatElement[32];
/*  13 */   private static final java.util.List<VertexFormatElement> ELEMENTS = new java.util.ArrayList<>(32);
/*     */   
/*  15 */   public static final VertexFormatElement POSITION = register(0, 0, Type.FLOAT, Usage.POSITION, 3);
/*  16 */   public static final VertexFormatElement COLOR = register(1, 0, Type.UBYTE, Usage.COLOR, 4);
/*  17 */   public static final VertexFormatElement UV0 = register(2, 0, Type.FLOAT, Usage.UV, 2);
/*  18 */   public static final VertexFormatElement UV = UV0;
/*  19 */   public static final VertexFormatElement UV1 = register(3, 1, Type.SHORT, Usage.UV, 2);
/*  20 */   public static final VertexFormatElement UV2 = register(4, 2, Type.SHORT, Usage.UV, 2);
/*  21 */   public static final VertexFormatElement NORMAL = register(5, 0, Type.BYTE, Usage.NORMAL, 3);
/*  22 */   public static final VertexFormatElement LINE_WIDTH = register(6, 0, Type.FLOAT, Usage.GENERIC, 1);
/*     */   
/*     */   public static VertexFormatElement register(int id, int index, Type type, Usage usage, int count) {
/*  25 */     VertexFormatElement element = new VertexFormatElement(id, index, type, usage, count);
/*  26 */     if (BY_ID[id] != null) {
/*  27 */       throw new IllegalArgumentException("Duplicate element registration for: " + id);
/*     */     }
/*  29 */     BY_ID[id] = element;
/*  30 */     ELEMENTS.add(element);
/*  31 */     return element;
/*     */   }
/*     */   
/*     */   public VertexFormatElement(int id, int index, Type type, Usage usage, int count) {
/*  35 */     if (id < 0 || id >= BY_ID.length) {
/*  36 */       throw new IllegalArgumentException("Element ID must be in range [0; " + BY_ID.length + ")");
/*     */     }
/*  38 */     if (!supportsUsage(index, usage))
/*  39 */       throw new IllegalStateException("Multiple vertex elements of the same type other than UVs are not supported"); 
/*     */     this.id = id;
/*     */     this.index = index;
/*     */     this.type = type;
/*     */     this.usage = usage;
/*  44 */     this.count = count; } private boolean supportsUsage(int index, Usage usage) { return (index == 0 || usage == Usage.UV); }
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  49 */     return "" + this.count + "," + this.count + "," + String.valueOf(this.usage) + " (" + String.valueOf(this.type) + ")";
/*     */   }
/*     */   
/*     */   public int mask() {
/*  53 */     return 1 << this.id;
/*     */   }
/*     */   
/*     */   public int byteSize() {
/*  57 */     return this.type.size() * this.count;
/*     */   }
/*     */   
/*     */   public static VertexFormatElement byId(int id) {
/*  61 */     return BY_ID[id];
/*     */   }
/*     */   
/*     */   public static java.util.stream.Stream<VertexFormatElement> elementsFromMask(int mask) {
/*  65 */     return ELEMENTS.stream().filter(element -> ((mask & element.mask()) != 0));
/*     */   }
/*     */   
/*     */   public enum Usage {
/*  69 */     POSITION("Position"),
/*  70 */     NORMAL("Normal"),
/*  71 */     COLOR("Vertex Color"),
/*  72 */     UV("UV"),
/*  73 */     GENERIC("Generic");
/*     */     
/*     */     private final String name;
/*     */     
/*     */     Usage(String name) {
/*  78 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/*  83 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public enum Type {
/*  88 */     FLOAT(4, "Float"),
/*  89 */     UBYTE(1, "Unsigned Byte"),
/*  90 */     BYTE(1, "Byte"),
/*  91 */     USHORT(2, "Unsigned Short"),
/*  92 */     SHORT(2, "Short"),
/*  93 */     UINT(4, "Unsigned Int"),
/*  94 */     INT(4, "Int");
/*     */     
/*     */     private final int size;
/*     */     private final String name;
/*     */     
/*     */     Type(int size, String name) {
/* 100 */       this.size = size;
/* 101 */       this.name = name;
/*     */     }
/*     */     
/*     */     public int size() {
/* 105 */       return this.size;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 110 */       return this.name;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/vertex/VertexFormatElement.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */