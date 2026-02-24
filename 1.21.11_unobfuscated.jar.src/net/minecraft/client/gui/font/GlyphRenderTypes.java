/*    */ package net.minecraft.client.gui.font;
/*    */ 
/*    */ 
/*    */ public final class GlyphRenderTypes extends Record {
/*    */   private final net.minecraft.client.renderer.rendertype.RenderType normal;
/*    */   private final net.minecraft.client.renderer.rendertype.RenderType seeThrough;
/*    */   private final net.minecraft.client.renderer.rendertype.RenderType polygonOffset;
/*    */   private final com.mojang.blaze3d.pipeline.RenderPipeline guiPipeline;
/*    */   
/* 10 */   public GlyphRenderTypes(net.minecraft.client.renderer.rendertype.RenderType normal, net.minecraft.client.renderer.rendertype.RenderType seeThrough, net.minecraft.client.renderer.rendertype.RenderType polygonOffset, com.mojang.blaze3d.pipeline.RenderPipeline guiPipeline) { this.normal = normal; this.seeThrough = seeThrough; this.polygonOffset = polygonOffset; this.guiPipeline = guiPipeline; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/font/GlyphRenderTypes;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/client/gui/font/GlyphRenderTypes; } public net.minecraft.client.renderer.rendertype.RenderType normal() { return this.normal; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/font/GlyphRenderTypes;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/font/GlyphRenderTypes; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/font/GlyphRenderTypes;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/font/GlyphRenderTypes;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.client.renderer.rendertype.RenderType seeThrough() { return this.seeThrough; } public net.minecraft.client.renderer.rendertype.RenderType polygonOffset() { return this.polygonOffset; } public com.mojang.blaze3d.pipeline.RenderPipeline guiPipeline() { return this.guiPipeline; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static GlyphRenderTypes createForIntensityTexture(net.minecraft.resources.Identifier name) {
/* 17 */     return new GlyphRenderTypes(
/* 18 */         net.minecraft.client.renderer.rendertype.RenderTypes.textIntensity(name), 
/* 19 */         net.minecraft.client.renderer.rendertype.RenderTypes.textIntensitySeeThrough(name), 
/* 20 */         net.minecraft.client.renderer.rendertype.RenderTypes.textIntensityPolygonOffset(name), net.minecraft.client.renderer.RenderPipelines.GUI_TEXT_INTENSITY);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static GlyphRenderTypes createForColorTexture(net.minecraft.resources.Identifier name) {
/* 26 */     return new GlyphRenderTypes(
/* 27 */         net.minecraft.client.renderer.rendertype.RenderTypes.text(name), 
/* 28 */         net.minecraft.client.renderer.rendertype.RenderTypes.textSeeThrough(name), 
/* 29 */         net.minecraft.client.renderer.rendertype.RenderTypes.textPolygonOffset(name), net.minecraft.client.renderer.RenderPipelines.GUI_TEXT);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.client.renderer.rendertype.RenderType select(net.minecraft.client.gui.Font.DisplayMode mode) {
/* 35 */     switch (mode) { default: throw new MatchException(null, null);case NORMAL: case SEE_THROUGH: case POLYGON_OFFSET: break; }  return 
/*    */ 
/*    */       
/* 38 */       this.polygonOffset;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/font/GlyphRenderTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */