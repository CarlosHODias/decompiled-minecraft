/*    */ package com.mojang.blaze3d.pipeline;public final class BlendFunction extends Record { private final com.mojang.blaze3d.platform.SourceFactor sourceColor;
/*    */   private final com.mojang.blaze3d.platform.DestFactor destColor;
/*    */   private final com.mojang.blaze3d.platform.SourceFactor sourceAlpha;
/*    */   private final com.mojang.blaze3d.platform.DestFactor destAlpha;
/*    */   
/*  6 */   public com.mojang.blaze3d.platform.DestFactor destAlpha() { return this.destAlpha; } public com.mojang.blaze3d.platform.SourceFactor sourceAlpha() { return this.sourceAlpha; } public com.mojang.blaze3d.platform.DestFactor destColor() { return this.destColor; } public com.mojang.blaze3d.platform.SourceFactor sourceColor() { return this.sourceColor; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/pipeline/BlendFunction;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/pipeline/BlendFunction;
/*  6 */     //   0	8	1	o	Ljava/lang/Object; } public BlendFunction(com.mojang.blaze3d.platform.SourceFactor sourceColor, com.mojang.blaze3d.platform.DestFactor destColor, com.mojang.blaze3d.platform.SourceFactor sourceAlpha, com.mojang.blaze3d.platform.DestFactor destAlpha) { this.sourceColor = sourceColor; this.destColor = destColor; this.sourceAlpha = sourceAlpha; this.destAlpha = destAlpha; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/pipeline/BlendFunction;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/BlendFunction; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/pipeline/BlendFunction;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lcom/mojang/blaze3d/pipeline/BlendFunction; } public static final BlendFunction LIGHTNING = new BlendFunction(com.mojang.blaze3d.platform.SourceFactor.SRC_ALPHA, com.mojang.blaze3d.platform.DestFactor.ONE);
/*  8 */   public static final BlendFunction GLINT = new BlendFunction(com.mojang.blaze3d.platform.SourceFactor.SRC_COLOR, com.mojang.blaze3d.platform.DestFactor.ONE, com.mojang.blaze3d.platform.SourceFactor.ZERO, com.mojang.blaze3d.platform.DestFactor.ONE);
/*  9 */   public static final BlendFunction OVERLAY = new BlendFunction(com.mojang.blaze3d.platform.SourceFactor.SRC_ALPHA, com.mojang.blaze3d.platform.DestFactor.ONE, com.mojang.blaze3d.platform.SourceFactor.ONE, com.mojang.blaze3d.platform.DestFactor.ZERO);
/* 10 */   public static final BlendFunction TRANSLUCENT = new BlendFunction(com.mojang.blaze3d.platform.SourceFactor.SRC_ALPHA, com.mojang.blaze3d.platform.DestFactor.ONE_MINUS_SRC_ALPHA, com.mojang.blaze3d.platform.SourceFactor.ONE, com.mojang.blaze3d.platform.DestFactor.ONE_MINUS_SRC_ALPHA);
/* 11 */   public static final BlendFunction TRANSLUCENT_PREMULTIPLIED_ALPHA = new BlendFunction(com.mojang.blaze3d.platform.SourceFactor.ONE, com.mojang.blaze3d.platform.DestFactor.ONE_MINUS_SRC_ALPHA, com.mojang.blaze3d.platform.SourceFactor.ONE, com.mojang.blaze3d.platform.DestFactor.ONE_MINUS_SRC_ALPHA);
/* 12 */   public static final BlendFunction ADDITIVE = new BlendFunction(com.mojang.blaze3d.platform.SourceFactor.ONE, com.mojang.blaze3d.platform.DestFactor.ONE);
/* 13 */   public static final BlendFunction ENTITY_OUTLINE_BLIT = new BlendFunction(com.mojang.blaze3d.platform.SourceFactor.SRC_ALPHA, com.mojang.blaze3d.platform.DestFactor.ONE_MINUS_SRC_ALPHA, com.mojang.blaze3d.platform.SourceFactor.ZERO, com.mojang.blaze3d.platform.DestFactor.ONE);
/* 14 */   public static final BlendFunction INVERT = new BlendFunction(com.mojang.blaze3d.platform.SourceFactor.ONE_MINUS_DST_COLOR, com.mojang.blaze3d.platform.DestFactor.ONE_MINUS_SRC_COLOR, com.mojang.blaze3d.platform.SourceFactor.ONE, com.mojang.blaze3d.platform.DestFactor.ZERO);
/*    */   
/*    */   public BlendFunction(com.mojang.blaze3d.platform.SourceFactor source, com.mojang.blaze3d.platform.DestFactor dest) {
/* 17 */     this(source, dest, source, dest);
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/pipeline/BlendFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */