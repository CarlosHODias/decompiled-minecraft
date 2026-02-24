/*    */ package com.mojang.blaze3d.vertex;
/*    */ 
/*    */ public class DefaultVertexFormat {
/*  4 */   public static final VertexFormat EMPTY = VertexFormat.builder()
/*  5 */     .build();
/*    */   
/*  7 */   public static final VertexFormat BLOCK = VertexFormat.builder()
/*  8 */     .add("Position", VertexFormatElement.POSITION)
/*  9 */     .add("Color", VertexFormatElement.COLOR)
/* 10 */     .add("UV0", VertexFormatElement.UV0)
/* 11 */     .add("UV2", VertexFormatElement.UV2)
/* 12 */     .add("Normal", VertexFormatElement.NORMAL)
/* 13 */     .padding(1)
/* 14 */     .build();
/*    */   
/* 16 */   public static final VertexFormat NEW_ENTITY = VertexFormat.builder()
/* 17 */     .add("Position", VertexFormatElement.POSITION)
/* 18 */     .add("Color", VertexFormatElement.COLOR)
/* 19 */     .add("UV0", VertexFormatElement.UV0)
/* 20 */     .add("UV1", VertexFormatElement.UV1)
/* 21 */     .add("UV2", VertexFormatElement.UV2)
/* 22 */     .add("Normal", VertexFormatElement.NORMAL)
/* 23 */     .padding(1)
/* 24 */     .build();
/*    */   
/* 26 */   public static final VertexFormat PARTICLE = VertexFormat.builder()
/* 27 */     .add("Position", VertexFormatElement.POSITION)
/* 28 */     .add("UV0", VertexFormatElement.UV0)
/* 29 */     .add("Color", VertexFormatElement.COLOR)
/* 30 */     .add("UV2", VertexFormatElement.UV2)
/* 31 */     .build();
/*    */   
/* 33 */   public static final VertexFormat POSITION = VertexFormat.builder()
/* 34 */     .add("Position", VertexFormatElement.POSITION)
/* 35 */     .build();
/*    */   
/* 37 */   public static final VertexFormat POSITION_COLOR = VertexFormat.builder()
/* 38 */     .add("Position", VertexFormatElement.POSITION)
/* 39 */     .add("Color", VertexFormatElement.COLOR)
/* 40 */     .build();
/*    */   
/* 42 */   public static final VertexFormat POSITION_COLOR_NORMAL = VertexFormat.builder()
/* 43 */     .add("Position", VertexFormatElement.POSITION)
/* 44 */     .add("Color", VertexFormatElement.COLOR)
/* 45 */     .add("Normal", VertexFormatElement.NORMAL)
/* 46 */     .padding(1)
/* 47 */     .build();
/*    */   
/* 49 */   public static final VertexFormat POSITION_COLOR_LIGHTMAP = VertexFormat.builder()
/* 50 */     .add("Position", VertexFormatElement.POSITION)
/* 51 */     .add("Color", VertexFormatElement.COLOR)
/* 52 */     .add("UV2", VertexFormatElement.UV2)
/* 53 */     .build();
/*    */   
/* 55 */   public static final VertexFormat POSITION_TEX = VertexFormat.builder()
/* 56 */     .add("Position", VertexFormatElement.POSITION)
/* 57 */     .add("UV0", VertexFormatElement.UV0)
/* 58 */     .build();
/*    */   
/* 60 */   public static final VertexFormat POSITION_TEX_COLOR = VertexFormat.builder()
/* 61 */     .add("Position", VertexFormatElement.POSITION)
/* 62 */     .add("UV0", VertexFormatElement.UV0)
/* 63 */     .add("Color", VertexFormatElement.COLOR)
/* 64 */     .build();
/*    */   
/* 66 */   public static final VertexFormat POSITION_COLOR_TEX_LIGHTMAP = VertexFormat.builder()
/* 67 */     .add("Position", VertexFormatElement.POSITION)
/* 68 */     .add("Color", VertexFormatElement.COLOR)
/* 69 */     .add("UV0", VertexFormatElement.UV0)
/* 70 */     .add("UV2", VertexFormatElement.UV2)
/* 71 */     .build();
/*    */   
/* 73 */   public static final VertexFormat POSITION_TEX_LIGHTMAP_COLOR = VertexFormat.builder()
/* 74 */     .add("Position", VertexFormatElement.POSITION)
/* 75 */     .add("UV0", VertexFormatElement.UV0)
/* 76 */     .add("UV2", VertexFormatElement.UV2)
/* 77 */     .add("Color", VertexFormatElement.COLOR)
/* 78 */     .build();
/*    */   
/* 80 */   public static final VertexFormat POSITION_TEX_COLOR_NORMAL = VertexFormat.builder()
/* 81 */     .add("Position", VertexFormatElement.POSITION)
/* 82 */     .add("UV0", VertexFormatElement.UV0)
/* 83 */     .add("Color", VertexFormatElement.COLOR)
/* 84 */     .add("Normal", VertexFormatElement.NORMAL)
/* 85 */     .padding(1)
/* 86 */     .build();
/*    */   
/* 88 */   public static final VertexFormat POSITION_COLOR_LINE_WIDTH = VertexFormat.builder()
/* 89 */     .add("Position", VertexFormatElement.POSITION)
/* 90 */     .add("Color", VertexFormatElement.COLOR)
/* 91 */     .add("LineWidth", VertexFormatElement.LINE_WIDTH)
/* 92 */     .build();
/*    */   
/* 94 */   public static final VertexFormat POSITION_COLOR_NORMAL_LINE_WIDTH = VertexFormat.builder()
/* 95 */     .add("Position", VertexFormatElement.POSITION)
/* 96 */     .add("Color", VertexFormatElement.COLOR)
/* 97 */     .add("Normal", VertexFormatElement.NORMAL)
/* 98 */     .add("LineWidth", VertexFormatElement.LINE_WIDTH)
/* 99 */     .build();
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/vertex/DefaultVertexFormat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */