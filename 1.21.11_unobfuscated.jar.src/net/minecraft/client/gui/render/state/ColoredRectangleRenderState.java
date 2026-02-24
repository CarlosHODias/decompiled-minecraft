/*    */ package net.minecraft.client.gui.render.state;public final class ColoredRectangleRenderState extends Record implements GuiElementRenderState { private final com.mojang.blaze3d.pipeline.RenderPipeline pipeline; private final net.minecraft.client.gui.render.TextureSetup textureSetup; private final org.joml.Matrix3x2fc pose; private final int x0;
/*    */   private final int y0;
/*    */   private final int x1;
/*    */   private final int y1;
/*    */   private final int col1;
/*    */   private final int col2;
/*    */   private final net.minecraft.client.gui.navigation.ScreenRectangle scissorArea;
/*    */   private final net.minecraft.client.gui.navigation.ScreenRectangle bounds;
/*    */   
/* 10 */   public ColoredRectangleRenderState(com.mojang.blaze3d.pipeline.RenderPipeline pipeline, net.minecraft.client.gui.render.TextureSetup textureSetup, org.joml.Matrix3x2fc pose, int x0, int y0, int x1, int y1, int col1, int col2, net.minecraft.client.gui.navigation.ScreenRectangle scissorArea, net.minecraft.client.gui.navigation.ScreenRectangle bounds) { this.pipeline = pipeline; this.textureSetup = textureSetup; this.pose = pose; this.x0 = x0; this.y0 = y0; this.x1 = x1; this.y1 = y1; this.col1 = col1; this.col2 = col2; this.scissorArea = scissorArea; this.bounds = bounds; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/render/state/ColoredRectangleRenderState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/client/gui/render/state/ColoredRectangleRenderState; } public com.mojang.blaze3d.pipeline.RenderPipeline pipeline() { return this.pipeline; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/render/state/ColoredRectangleRenderState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/render/state/ColoredRectangleRenderState; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/render/state/ColoredRectangleRenderState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/render/state/ColoredRectangleRenderState;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.client.gui.render.TextureSetup textureSetup() { return this.textureSetup; } public org.joml.Matrix3x2fc pose() { return this.pose; } public int x0() { return this.x0; } public int y0() { return this.y0; } public int x1() { return this.x1; } public int y1() { return this.y1; } public int col1() { return this.col1; } public int col2() { return this.col2; } public net.minecraft.client.gui.navigation.ScreenRectangle scissorArea() { return this.scissorArea; } public net.minecraft.client.gui.navigation.ScreenRectangle bounds() { return this.bounds; }
/*    */   
/*    */   public ColoredRectangleRenderState(com.mojang.blaze3d.pipeline.RenderPipeline pipeline, net.minecraft.client.gui.render.TextureSetup textureSetup, org.joml.Matrix3x2fc pose, int x0, int y0, int x1, int y1, int col1, int col2, net.minecraft.client.gui.navigation.ScreenRectangle scissorArea) {
/* 13 */     this(pipeline, textureSetup, pose, x0, y0, x1, y1, col1, col2, scissorArea, getBounds(x0, y0, x1, y1, pose, scissorArea));
/*    */   }
/*    */ 
/*    */   
/*    */   public void buildVertices(com.mojang.blaze3d.vertex.VertexConsumer vertexConsumer) {
/* 18 */     vertexConsumer.addVertexWith2DPose(pose(), x0(), y0()).setColor(col1());
/* 19 */     vertexConsumer.addVertexWith2DPose(pose(), x0(), y1()).setColor(col2());
/* 20 */     vertexConsumer.addVertexWith2DPose(pose(), x1(), y1()).setColor(col2());
/* 21 */     vertexConsumer.addVertexWith2DPose(pose(), x1(), y0()).setColor(col1());
/*    */   }
/*    */   
/*    */   private static net.minecraft.client.gui.navigation.ScreenRectangle getBounds(int x0, int y0, int x1, int y1, org.joml.Matrix3x2fc pose, net.minecraft.client.gui.navigation.ScreenRectangle scissorArea) {
/* 25 */     net.minecraft.client.gui.navigation.ScreenRectangle bounds = new net.minecraft.client.gui.navigation.ScreenRectangle(x0, y0, x1 - x0, y1 - y0).transformMaxBounds(pose);
/* 26 */     return (scissorArea != null) ? scissorArea.intersection(bounds) : bounds;
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/state/ColoredRectangleRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */