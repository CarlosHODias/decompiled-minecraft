/*    */ package com.mojang.blaze3d.resource;
/*    */ public final class RenderTargetDescriptor extends Record implements ResourceDescriptor<com.mojang.blaze3d.pipeline.RenderTarget> { private final int width;
/*    */   private final int height;
/*    */   private final boolean useDepth;
/*    */   private final int clearColor;
/*    */   
/*  7 */   public RenderTargetDescriptor(int width, int height, boolean useDepth, int clearColor) { this.width = width; this.height = height; this.useDepth = useDepth; this.clearColor = clearColor; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/resource/RenderTargetDescriptor;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lcom/mojang/blaze3d/resource/RenderTargetDescriptor; } public int width() { return this.width; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/resource/RenderTargetDescriptor;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/resource/RenderTargetDescriptor; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/resource/RenderTargetDescriptor;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/resource/RenderTargetDescriptor;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public int height() { return this.height; } public boolean useDepth() { return this.useDepth; } public int clearColor() { return this.clearColor; }
/*    */   
/*    */   public com.mojang.blaze3d.pipeline.RenderTarget allocate() {
/* 10 */     return (com.mojang.blaze3d.pipeline.RenderTarget)new com.mojang.blaze3d.pipeline.TextureTarget(null, this.width, this.height, this.useDepth);
/*    */   }
/*    */ 
/*    */   
/*    */   public void prepare(com.mojang.blaze3d.pipeline.RenderTarget resource) {
/* 15 */     if (this.useDepth) {
/* 16 */       com.mojang.blaze3d.systems.RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(resource.getColorTexture(), this.clearColor, resource.getDepthTexture(), 1.0D);
/*    */     } else {
/* 18 */       com.mojang.blaze3d.systems.RenderSystem.getDevice().createCommandEncoder().clearColorTexture(resource.getColorTexture(), this.clearColor);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void free(com.mojang.blaze3d.pipeline.RenderTarget resource) {
/* 24 */     resource.destroyBuffers();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean canUsePhysicalResource(ResourceDescriptor<?> other) {
/* 30 */     if (other instanceof RenderTargetDescriptor) { RenderTargetDescriptor descriptor = (RenderTargetDescriptor)other;
/* 31 */       return (this.width == descriptor.width && this.height == descriptor.height && this.useDepth == descriptor.useDepth); }
/*    */     
/* 33 */     return false;
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/resource/RenderTargetDescriptor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */