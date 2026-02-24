/*   */ package com.mojang.blaze3d.opengl;
/*   */ public final class GlRenderPipeline extends Record implements com.mojang.blaze3d.pipeline.CompiledRenderPipeline {
/*   */   private final com.mojang.blaze3d.pipeline.RenderPipeline info;
/*   */   private final GlProgram program;
/*   */   
/* 6 */   public GlRenderPipeline(com.mojang.blaze3d.pipeline.RenderPipeline info, GlProgram program) { this.info = info; this.program = program; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/GlRenderPipeline;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 6 */     //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlRenderPipeline; } public com.mojang.blaze3d.pipeline.RenderPipeline info() { return this.info; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/GlRenderPipeline;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/blaze3d/opengl/GlRenderPipeline; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/GlRenderPipeline;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lcom/mojang/blaze3d/opengl/GlRenderPipeline;
/* 6 */     //   0	8	1	o	Ljava/lang/Object; } public GlProgram program() { return this.program; }
/*   */   
/*   */   public boolean isValid() {
/* 9 */     return (this.program != GlProgram.INVALID_PROGRAM);
/*   */   }
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/opengl/GlRenderPipeline.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */