/*    */ package net.minecraft.client.gui.render.state;
/*    */ 
/*    */ import com.mojang.blaze3d.vertex.VertexConsumer;
/*    */ import net.minecraft.client.gui.font.TextRenderable;
/*    */ import net.minecraft.client.gui.navigation.ScreenRectangle;
/*    */ import net.minecraft.client.gui.render.TextureSetup;
/*    */ import org.joml.Matrix3x2fc;
/*    */ import org.joml.Matrix4f;
/*    */ 
/*    */ public final class GlyphRenderState extends Record implements GuiElementRenderState {
/*    */   private final Matrix3x2fc pose;
/*    */   private final TextRenderable renderable;
/*    */   private final ScreenRectangle scissorArea;
/*    */   
/* 15 */   public GlyphRenderState(Matrix3x2fc pose, TextRenderable renderable, ScreenRectangle scissorArea) { this.pose = pose; this.renderable = renderable; this.scissorArea = scissorArea; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/render/state/GlyphRenderState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/client/gui/render/state/GlyphRenderState; } public Matrix3x2fc pose() { return this.pose; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/render/state/GlyphRenderState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/render/state/GlyphRenderState; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/render/state/GlyphRenderState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/render/state/GlyphRenderState;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public TextRenderable renderable() { return this.renderable; } public ScreenRectangle scissorArea() { return this.scissorArea; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void buildVertices(VertexConsumer vertexConsumer) {
/* 22 */     this.renderable.render(new Matrix4f().mul(this.pose), vertexConsumer, 15728880, true);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.blaze3d.pipeline.RenderPipeline pipeline() {
/* 27 */     return this.renderable.guiPipeline();
/*    */   }
/*    */ 
/*    */   
/*    */   public TextureSetup textureSetup() {
/* 32 */     return TextureSetup.singleTextureWithLightmap(this.renderable.textureView(), com.mojang.blaze3d.systems.RenderSystem.getSamplerCache().getClampToEdge(com.mojang.blaze3d.textures.FilterMode.NEAREST));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ScreenRectangle bounds() {
/* 38 */     return null;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/state/GlyphRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */