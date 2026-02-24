/*    */ package com.mojang.blaze3d.opengl;
/*    */ public interface Uniform extends AutoCloseable { default void close() {}
/*    */   public static final class Ubo extends Record implements Uniform { private final int blockBinding;
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/Uniform$Ubo;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #10	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Ubo;
/*    */     }
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/Uniform$Ubo;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #10	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Ubo;
/*    */     }
/* 10 */     public Ubo(int blockBinding) { this.blockBinding = blockBinding; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/Uniform$Ubo;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #10	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Ubo;
/* 10 */       //   0	8	1	o	Ljava/lang/Object; } public int blockBinding() { return this.blockBinding; }
/*    */      }
/*    */   public static final class Utb extends Record implements Uniform { private final int location; private final int samplerIndex; private final com.mojang.blaze3d.textures.TextureFormat format; private final int texture;
/* 13 */     public Utb(int location, int samplerIndex, com.mojang.blaze3d.textures.TextureFormat format, int texture) { this.location = location; this.samplerIndex = samplerIndex; this.format = format; this.texture = texture; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/Uniform$Utb;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #13	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Utb; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/Uniform$Utb;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #13	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Utb; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/Uniform$Utb;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #13	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Utb;
/* 13 */       //   0	8	1	o	Ljava/lang/Object; } public int location() { return this.location; } public int samplerIndex() { return this.samplerIndex; } public com.mojang.blaze3d.textures.TextureFormat format() { return this.format; } public int texture() { return this.texture; }
/*    */      public Utb(int location, int samplerIndex, com.mojang.blaze3d.textures.TextureFormat format) {
/* 15 */       this(location, samplerIndex, format, GlStateManager._genTexture());
/*    */     }
/*    */ 
/*    */     
/*    */     public void close() {
/* 20 */       GlStateManager._deleteTexture(this.texture);
/*    */     } }
/*    */   public static final class Sampler extends Record implements Uniform { private final int location; private final int samplerIndex;
/*    */     
/* 24 */     public Sampler(int location, int samplerIndex) { this.location = location; this.samplerIndex = samplerIndex; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/opengl/Uniform$Sampler;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Sampler; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/opengl/Uniform$Sampler;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Sampler; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/opengl/Uniform$Sampler;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #24	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/blaze3d/opengl/Uniform$Sampler;
/* 24 */       //   0	8	1	o	Ljava/lang/Object; } public int location() { return this.location; } public int samplerIndex() { return this.samplerIndex; }
/*    */      }
/*    */    }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/opengl/Uniform.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */