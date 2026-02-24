/*    */ package net.minecraft.client.gui.render.state;
/*    */ public final class BlitRenderState extends Record implements GuiElementRenderState { private final com.mojang.blaze3d.pipeline.RenderPipeline pipeline;
/*    */   private final net.minecraft.client.gui.render.TextureSetup textureSetup;
/*    */   private final org.joml.Matrix3x2f pose;
/*    */   private final int x0;
/*    */   private final int y0;
/*    */   private final int x1;
/*    */   private final int y1;
/*    */   
/* 10 */   public BlitRenderState(com.mojang.blaze3d.pipeline.RenderPipeline pipeline, net.minecraft.client.gui.render.TextureSetup textureSetup, org.joml.Matrix3x2f pose, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1, int color, net.minecraft.client.gui.navigation.ScreenRectangle scissorArea, net.minecraft.client.gui.navigation.ScreenRectangle bounds) { this.pipeline = pipeline; this.textureSetup = textureSetup; this.pose = pose; this.x0 = x0; this.y0 = y0; this.x1 = x1; this.y1 = y1; this.u0 = u0; this.u1 = u1; this.v0 = v0; this.v1 = v1; this.color = color; this.scissorArea = scissorArea; this.bounds = bounds; } private final float u0; private final float u1; private final float v0; private final float v1; private final int color; private final net.minecraft.client.gui.navigation.ScreenRectangle scissorArea; private final net.minecraft.client.gui.navigation.ScreenRectangle bounds; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/render/state/BlitRenderState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/render/state/BlitRenderState; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/render/state/BlitRenderState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/render/state/BlitRenderState; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/render/state/BlitRenderState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/render/state/BlitRenderState;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public com.mojang.blaze3d.pipeline.RenderPipeline pipeline() { return this.pipeline; } public net.minecraft.client.gui.render.TextureSetup textureSetup() { return this.textureSetup; } public org.joml.Matrix3x2f pose() { return this.pose; } public int x0() { return this.x0; } public int y0() { return this.y0; } public int x1() { return this.x1; } public int y1() { return this.y1; } public float u0() { return this.u0; } public float u1() { return this.u1; } public float v0() { return this.v0; } public float v1() { return this.v1; } public int color() { return this.color; } public net.minecraft.client.gui.navigation.ScreenRectangle scissorArea() { return this.scissorArea; } public net.minecraft.client.gui.navigation.ScreenRectangle bounds() { return this.bounds; }
/*    */   
/*    */   public BlitRenderState(com.mojang.blaze3d.pipeline.RenderPipeline pipeline, net.minecraft.client.gui.render.TextureSetup textureSetup, org.joml.Matrix3x2f pose, int x0, int y0, int x1, int y1, float u0, float u1, float v0, float v1, int color, net.minecraft.client.gui.navigation.ScreenRectangle scissorArea) {
/* 13 */     this(pipeline, textureSetup, pose, x0, y0, x1, y1, u0, u1, v0, v1, color, scissorArea, getBounds(x0, y0, x1, y1, pose, scissorArea));
/*    */   }
/*    */ 
/*    */   
/*    */   public void buildVertices(com.mojang.blaze3d.vertex.VertexConsumer vertexConsumer) {
/* 18 */     vertexConsumer.addVertexWith2DPose((org.joml.Matrix3x2fc)pose(), x0(), y0()).setUv(u0(), v0()).setColor(color());
/* 19 */     vertexConsumer.addVertexWith2DPose((org.joml.Matrix3x2fc)pose(), x0(), y1()).setUv(u0(), v1()).setColor(color());
/* 20 */     vertexConsumer.addVertexWith2DPose((org.joml.Matrix3x2fc)pose(), x1(), y1()).setUv(u1(), v1()).setColor(color());
/* 21 */     vertexConsumer.addVertexWith2DPose((org.joml.Matrix3x2fc)pose(), x1(), y0()).setUv(u1(), v0()).setColor(color());
/*    */   }
/*    */   
/*    */   private static net.minecraft.client.gui.navigation.ScreenRectangle getBounds(int x0, int y0, int x1, int y1, org.joml.Matrix3x2f pose, net.minecraft.client.gui.navigation.ScreenRectangle scissorArea) {
/* 25 */     net.minecraft.client.gui.navigation.ScreenRectangle bounds = new net.minecraft.client.gui.navigation.ScreenRectangle(x0, y0, x1 - x0, y1 - y0).transformMaxBounds((org.joml.Matrix3x2fc)pose);
/* 26 */     return (scissorArea != null) ? scissorArea.intersection(bounds) : bounds;
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/state/BlitRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */