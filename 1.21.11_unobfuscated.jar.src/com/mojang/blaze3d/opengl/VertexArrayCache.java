/*     */ package com.mojang.blaze3d.opengl;
/*     */ 
/*     */ import com.mojang.blaze3d.vertex.VertexFormat;
/*     */ import com.mojang.blaze3d.vertex.VertexFormatElement;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import org.lwjgl.opengl.GLCapabilities;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class VertexArrayCache
/*     */ {
/*     */   public static VertexArrayCache create(GLCapabilities capabilities, GlDebugLabel debugLabels, Set<String> enabledExtensions) {
/*  17 */     if (capabilities.GL_ARB_vertex_attrib_binding && GlDevice.USE_GL_ARB_vertex_attrib_binding) {
/*  18 */       enabledExtensions.add("GL_ARB_vertex_attrib_binding");
/*  19 */       return new Separate(debugLabels);
/*     */     } 
/*  21 */     return new Emulated(debugLabels);
/*     */   }
/*     */   
/*     */   public abstract void bindVertexArray(VertexFormat paramVertexFormat, GlBuffer paramGlBuffer);
/*     */   
/*     */   private static class Emulated extends VertexArrayCache {
/*  27 */     private final Map<VertexFormat, VertexArrayCache.VertexArray> cache = new HashMap<>();
/*     */     private final GlDebugLabel debugLabels;
/*     */     
/*     */     public Emulated(GlDebugLabel debugLabels) {
/*  31 */       this.debugLabels = debugLabels;
/*     */     }
/*     */ 
/*     */     
/*     */     public void bindVertexArray(VertexFormat format, GlBuffer vertexBuffer) {
/*  36 */       VertexArrayCache.VertexArray vertexArray = this.cache.get(format);
/*  37 */       if (vertexArray == null) {
/*     */ 
/*     */         
/*  40 */         int id = GlStateManager._glGenVertexArrays();
/*  41 */         GlStateManager._glBindVertexArray(id);
/*  42 */         if (vertexBuffer != null) {
/*  43 */           GlStateManager._glBindBuffer(34962, vertexBuffer.handle);
/*  44 */           setupCombinedAttributes(format, true);
/*     */         } 
/*     */         
/*  47 */         VertexArrayCache.VertexArray vao = new VertexArrayCache.VertexArray(id, format, vertexBuffer);
/*  48 */         this.debugLabels.applyLabel(vao);
/*  49 */         this.cache.put(format, vao);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*  54 */       GlStateManager._glBindVertexArray(vertexArray.id);
/*     */ 
/*     */       
/*  57 */       if (vertexBuffer != null && vertexArray.lastVertexBuffer != vertexBuffer) {
/*  58 */         GlStateManager._glBindBuffer(34962, vertexBuffer.handle);
/*  59 */         vertexArray.lastVertexBuffer = vertexBuffer;
/*  60 */         setupCombinedAttributes(format, false);
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private static void setupCombinedAttributes(VertexFormat format, boolean enable) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: invokevirtual getVertexSize : ()I
/*     */       //   4: istore_2
/*     */       //   5: aload_0
/*     */       //   6: invokevirtual getElements : ()Ljava/util/List;
/*     */       //   9: astore_3
/*     */       //   10: iconst_0
/*     */       //   11: istore #4
/*     */       //   13: iload #4
/*     */       //   15: aload_3
/*     */       //   16: invokeinterface size : ()I
/*     */       //   21: if_icmpge -> 195
/*     */       //   24: aload_3
/*     */       //   25: iload #4
/*     */       //   27: invokeinterface get : (I)Ljava/lang/Object;
/*     */       //   32: checkcast com/mojang/blaze3d/vertex/VertexFormatElement
/*     */       //   35: astore #5
/*     */       //   37: iload_1
/*     */       //   38: ifeq -> 46
/*     */       //   41: iload #4
/*     */       //   43: invokestatic _enableVertexAttribArray : (I)V
/*     */       //   46: getstatic com/mojang/blaze3d/opengl/VertexArrayCache$1.$SwitchMap$com$mojang$blaze3d$vertex$VertexFormatElement$Usage : [I
/*     */       //   49: aload #5
/*     */       //   51: invokevirtual usage : ()Lcom/mojang/blaze3d/vertex/VertexFormatElement$Usage;
/*     */       //   54: invokevirtual ordinal : ()I
/*     */       //   57: iaload
/*     */       //   58: tableswitch default -> 189, 1 -> 92, 2 -> 92, 3 -> 92, 4 -> 162, 5 -> 162
/*     */       //   92: aload #5
/*     */       //   94: invokevirtual type : ()Lcom/mojang/blaze3d/vertex/VertexFormatElement$Type;
/*     */       //   97: getstatic com/mojang/blaze3d/vertex/VertexFormatElement$Type.FLOAT : Lcom/mojang/blaze3d/vertex/VertexFormatElement$Type;
/*     */       //   100: if_acmpne -> 133
/*     */       //   103: iload #4
/*     */       //   105: aload #5
/*     */       //   107: invokevirtual count : ()I
/*     */       //   110: aload #5
/*     */       //   112: invokevirtual type : ()Lcom/mojang/blaze3d/vertex/VertexFormatElement$Type;
/*     */       //   115: invokestatic toGl : (Lcom/mojang/blaze3d/vertex/VertexFormatElement$Type;)I
/*     */       //   118: iconst_0
/*     */       //   119: iload_2
/*     */       //   120: aload_0
/*     */       //   121: aload #5
/*     */       //   123: invokevirtual getOffset : (Lcom/mojang/blaze3d/vertex/VertexFormatElement;)I
/*     */       //   126: i2l
/*     */       //   127: invokestatic _vertexAttribPointer : (IIIZIJ)V
/*     */       //   130: goto -> 189
/*     */       //   133: iload #4
/*     */       //   135: aload #5
/*     */       //   137: invokevirtual count : ()I
/*     */       //   140: aload #5
/*     */       //   142: invokevirtual type : ()Lcom/mojang/blaze3d/vertex/VertexFormatElement$Type;
/*     */       //   145: invokestatic toGl : (Lcom/mojang/blaze3d/vertex/VertexFormatElement$Type;)I
/*     */       //   148: iload_2
/*     */       //   149: aload_0
/*     */       //   150: aload #5
/*     */       //   152: invokevirtual getOffset : (Lcom/mojang/blaze3d/vertex/VertexFormatElement;)I
/*     */       //   155: i2l
/*     */       //   156: invokestatic _vertexAttribIPointer : (IIIIJ)V
/*     */       //   159: goto -> 189
/*     */       //   162: iload #4
/*     */       //   164: aload #5
/*     */       //   166: invokevirtual count : ()I
/*     */       //   169: aload #5
/*     */       //   171: invokevirtual type : ()Lcom/mojang/blaze3d/vertex/VertexFormatElement$Type;
/*     */       //   174: invokestatic toGl : (Lcom/mojang/blaze3d/vertex/VertexFormatElement$Type;)I
/*     */       //   177: iconst_1
/*     */       //   178: iload_2
/*     */       //   179: aload_0
/*     */       //   180: aload #5
/*     */       //   182: invokevirtual getOffset : (Lcom/mojang/blaze3d/vertex/VertexFormatElement;)I
/*     */       //   185: i2l
/*     */       //   186: invokestatic _vertexAttribPointer : (IIIZIJ)V
/*     */       //   189: iinc #4, 1
/*     */       //   192: goto -> 13
/*     */       //   195: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #65	-> 0
/*     */       //   #66	-> 5
/*     */       //   #67	-> 10
/*     */       //   #68	-> 24
/*     */       //   #69	-> 37
/*     */       //   #70	-> 41
/*     */       //   #72	-> 46
/*     */       //   #74	-> 92
/*     */       //   #75	-> 103
/*     */       //   #77	-> 133
/*     */       //   #79	-> 159
/*     */       //   #81	-> 162
/*     */       //   #67	-> 189
/*     */       //   #84	-> 195
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   37	152	5	element	Lcom/mojang/blaze3d/vertex/VertexFormatElement;
/*     */       //   13	182	4	i	I
/*     */       //   0	196	0	format	Lcom/mojang/blaze3d/vertex/VertexFormat;
/*     */       //   0	196	1	enable	Z
/*     */       //   5	191	2	vertexSize	I
/*     */       //   10	186	3	elements	Ljava/util/List;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   10	186	3	elements	Ljava/util/List<Lcom/mojang/blaze3d/vertex/VertexFormatElement;>;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static class Separate
/*     */     extends VertexArrayCache
/*     */   {
/*  88 */     private final Map<VertexFormat, VertexArrayCache.VertexArray> cache = new HashMap<>();
/*     */     private final GlDebugLabel debugLabels;
/*     */     private final boolean needsMesaWorkaround;
/*     */     
/*     */     public Separate(GlDebugLabel debugLabels) {
/*  93 */       this.debugLabels = debugLabels;
/*  94 */       if ("Mesa".equals(GlStateManager._getString(7936))) {
/*  95 */         String version = GlStateManager._getString(7938);
/*     */         
/*  97 */         this.needsMesaWorkaround = (version.contains("25.0.0") || version.contains("25.0.1") || version.contains("25.0.2"));
/*     */       } else {
/*  99 */         this.needsMesaWorkaround = false;
/*     */       } 
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public void bindVertexArray(VertexFormat format, GlBuffer vertexBuffer) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: getfield cache : Ljava/util/Map;
/*     */       //   4: aload_1
/*     */       //   5: invokeinterface get : (Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   10: checkcast com/mojang/blaze3d/opengl/VertexArrayCache$VertexArray
/*     */       //   13: astore_3
/*     */       //   14: aload_3
/*     */       //   15: ifnonnull -> 276
/*     */       //   18: invokestatic _glGenVertexArrays : ()I
/*     */       //   21: istore #4
/*     */       //   23: iload #4
/*     */       //   25: invokestatic _glBindVertexArray : (I)V
/*     */       //   28: aload_2
/*     */       //   29: ifnull -> 223
/*     */       //   32: aload_1
/*     */       //   33: invokevirtual getElements : ()Ljava/util/List;
/*     */       //   36: astore #5
/*     */       //   38: iconst_0
/*     */       //   39: istore #6
/*     */       //   41: iload #6
/*     */       //   43: aload #5
/*     */       //   45: invokeinterface size : ()I
/*     */       //   50: if_icmpge -> 223
/*     */       //   53: aload #5
/*     */       //   55: iload #6
/*     */       //   57: invokeinterface get : (I)Ljava/lang/Object;
/*     */       //   62: checkcast com/mojang/blaze3d/vertex/VertexFormatElement
/*     */       //   65: astore #7
/*     */       //   67: iload #6
/*     */       //   69: invokestatic _enableVertexAttribArray : (I)V
/*     */       //   72: getstatic com/mojang/blaze3d/opengl/VertexArrayCache$1.$SwitchMap$com$mojang$blaze3d$vertex$VertexFormatElement$Usage : [I
/*     */       //   75: aload #7
/*     */       //   77: invokevirtual usage : ()Lcom/mojang/blaze3d/vertex/VertexFormatElement$Usage;
/*     */       //   80: invokevirtual ordinal : ()I
/*     */       //   83: iaload
/*     */       //   84: tableswitch default -> 211, 1 -> 120, 2 -> 120, 3 -> 120, 4 -> 186, 5 -> 186
/*     */       //   120: aload #7
/*     */       //   122: invokevirtual type : ()Lcom/mojang/blaze3d/vertex/VertexFormatElement$Type;
/*     */       //   125: getstatic com/mojang/blaze3d/vertex/VertexFormatElement$Type.FLOAT : Lcom/mojang/blaze3d/vertex/VertexFormatElement$Type;
/*     */       //   128: if_acmpne -> 159
/*     */       //   131: iload #6
/*     */       //   133: aload #7
/*     */       //   135: invokevirtual count : ()I
/*     */       //   138: aload #7
/*     */       //   140: invokevirtual type : ()Lcom/mojang/blaze3d/vertex/VertexFormatElement$Type;
/*     */       //   143: invokestatic toGl : (Lcom/mojang/blaze3d/vertex/VertexFormatElement$Type;)I
/*     */       //   146: iconst_0
/*     */       //   147: aload_1
/*     */       //   148: aload #7
/*     */       //   150: invokevirtual getOffset : (Lcom/mojang/blaze3d/vertex/VertexFormatElement;)I
/*     */       //   153: invokestatic glVertexAttribFormat : (IIIZI)V
/*     */       //   156: goto -> 211
/*     */       //   159: iload #6
/*     */       //   161: aload #7
/*     */       //   163: invokevirtual count : ()I
/*     */       //   166: aload #7
/*     */       //   168: invokevirtual type : ()Lcom/mojang/blaze3d/vertex/VertexFormatElement$Type;
/*     */       //   171: invokestatic toGl : (Lcom/mojang/blaze3d/vertex/VertexFormatElement$Type;)I
/*     */       //   174: aload_1
/*     */       //   175: aload #7
/*     */       //   177: invokevirtual getOffset : (Lcom/mojang/blaze3d/vertex/VertexFormatElement;)I
/*     */       //   180: invokestatic glVertexAttribIFormat : (IIII)V
/*     */       //   183: goto -> 211
/*     */       //   186: iload #6
/*     */       //   188: aload #7
/*     */       //   190: invokevirtual count : ()I
/*     */       //   193: aload #7
/*     */       //   195: invokevirtual type : ()Lcom/mojang/blaze3d/vertex/VertexFormatElement$Type;
/*     */       //   198: invokestatic toGl : (Lcom/mojang/blaze3d/vertex/VertexFormatElement$Type;)I
/*     */       //   201: iconst_1
/*     */       //   202: aload_1
/*     */       //   203: aload #7
/*     */       //   205: invokevirtual getOffset : (Lcom/mojang/blaze3d/vertex/VertexFormatElement;)I
/*     */       //   208: invokestatic glVertexAttribFormat : (IIIZI)V
/*     */       //   211: iload #6
/*     */       //   213: iconst_0
/*     */       //   214: invokestatic glVertexAttribBinding : (II)V
/*     */       //   217: iinc #6, 1
/*     */       //   220: goto -> 41
/*     */       //   223: aload_2
/*     */       //   224: ifnull -> 240
/*     */       //   227: iconst_0
/*     */       //   228: aload_2
/*     */       //   229: getfield handle : I
/*     */       //   232: lconst_0
/*     */       //   233: aload_1
/*     */       //   234: invokevirtual getVertexSize : ()I
/*     */       //   237: invokestatic glBindVertexBuffer : (IIJI)V
/*     */       //   240: new com/mojang/blaze3d/opengl/VertexArrayCache$VertexArray
/*     */       //   243: dup
/*     */       //   244: iload #4
/*     */       //   246: aload_1
/*     */       //   247: aload_2
/*     */       //   248: invokespecial <init> : (ILcom/mojang/blaze3d/vertex/VertexFormat;Lcom/mojang/blaze3d/opengl/GlBuffer;)V
/*     */       //   251: astore #5
/*     */       //   253: aload_0
/*     */       //   254: getfield debugLabels : Lcom/mojang/blaze3d/opengl/GlDebugLabel;
/*     */       //   257: aload #5
/*     */       //   259: invokevirtual applyLabel : (Lcom/mojang/blaze3d/opengl/VertexArrayCache$VertexArray;)V
/*     */       //   262: aload_0
/*     */       //   263: getfield cache : Ljava/util/Map;
/*     */       //   266: aload_1
/*     */       //   267: aload #5
/*     */       //   269: invokeinterface put : (Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
/*     */       //   274: pop
/*     */       //   275: return
/*     */       //   276: aload_3
/*     */       //   277: getfield id : I
/*     */       //   280: invokestatic _glBindVertexArray : (I)V
/*     */       //   283: aload_2
/*     */       //   284: ifnull -> 348
/*     */       //   287: aload_3
/*     */       //   288: getfield lastVertexBuffer : Lcom/mojang/blaze3d/opengl/GlBuffer;
/*     */       //   291: aload_2
/*     */       //   292: if_acmpeq -> 348
/*     */       //   295: aload_0
/*     */       //   296: getfield needsMesaWorkaround : Z
/*     */       //   299: ifeq -> 330
/*     */       //   302: aload_3
/*     */       //   303: getfield lastVertexBuffer : Lcom/mojang/blaze3d/opengl/GlBuffer;
/*     */       //   306: ifnull -> 330
/*     */       //   309: aload_3
/*     */       //   310: getfield lastVertexBuffer : Lcom/mojang/blaze3d/opengl/GlBuffer;
/*     */       //   313: getfield handle : I
/*     */       //   316: aload_2
/*     */       //   317: getfield handle : I
/*     */       //   320: if_icmpne -> 330
/*     */       //   323: iconst_0
/*     */       //   324: iconst_0
/*     */       //   325: lconst_0
/*     */       //   326: iconst_0
/*     */       //   327: invokestatic glBindVertexBuffer : (IIJI)V
/*     */       //   330: iconst_0
/*     */       //   331: aload_2
/*     */       //   332: getfield handle : I
/*     */       //   335: lconst_0
/*     */       //   336: aload_1
/*     */       //   337: invokevirtual getVertexSize : ()I
/*     */       //   340: invokestatic glBindVertexBuffer : (IIJI)V
/*     */       //   343: aload_3
/*     */       //   344: aload_2
/*     */       //   345: putfield lastVertexBuffer : Lcom/mojang/blaze3d/opengl/GlBuffer;
/*     */       //   348: return
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       //   #106	-> 14
/*     */       //   #107	-> 18
/*     */       //   #108	-> 23
/*     */       //   #109	-> 28
/*     */       //   #110	-> 32
/*     */       //   #111	-> 38
/*     */       //   #112	-> 53
/*     */       //   #113	-> 67
/*     */       //   #114	-> 72
/*     */       //   #116	-> 120
/*     */       //   #117	-> 131
/*     */       //   #119	-> 159
/*     */       //   #121	-> 183
/*     */       //   #123	-> 186
/*     */       //   #125	-> 211
/*     */       //   #111	-> 217
/*     */       //   #129	-> 223
/*     */       //   #130	-> 227
/*     */       //   #133	-> 240
/*     */       //   #134	-> 253
/*     */       //   #135	-> 262
/*     */       //   #136	-> 275
/*     */       //   #140	-> 276
/*     */       //   #143	-> 283
/*     */       //   #144	-> 295
/*     */       //   #148	-> 323
/*     */       //   #150	-> 330
/*     */       //   #151	-> 343
/*     */       //   #153	-> 348
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   67	150	7	element	Lcom/mojang/blaze3d/vertex/VertexFormatElement;
/*     */       //   41	182	6	i	I
/*     */       //   38	185	5	elements	Ljava/util/List;
/*     */       //   23	253	4	id	I
/*     */       //   253	23	5	vao	Lcom/mojang/blaze3d/opengl/VertexArrayCache$VertexArray;
/*     */       //   0	349	0	this	Lcom/mojang/blaze3d/opengl/VertexArrayCache$Separate;
/*     */       //   0	349	1	format	Lcom/mojang/blaze3d/vertex/VertexFormat;
/*     */       //   0	349	2	vertexBuffer	Lcom/mojang/blaze3d/opengl/GlBuffer;
/*     */       //   14	335	3	vertexArray	Lcom/mojang/blaze3d/opengl/VertexArrayCache$VertexArray;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   38	185	5	elements	Ljava/util/List<Lcom/mojang/blaze3d/vertex/VertexFormatElement;>;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static class VertexArray
/*     */   {
/*     */     final int id;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     final VertexFormat format;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     GlBuffer lastVertexBuffer;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     private VertexArray(int id, VertexFormat format, GlBuffer lastVertexBuffer) {
/* 162 */       this.id = id;
/* 163 */       this.format = format;
/* 164 */       this.lastVertexBuffer = lastVertexBuffer;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/opengl/VertexArrayCache.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */