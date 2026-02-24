/*    */ package net.minecraft.client.gui.render.state.pip;public final class GuiEntityRenderState extends Record implements PictureInPictureRenderState { private final net.minecraft.client.renderer.entity.state.EntityRenderState renderState; private final org.joml.Vector3f translation; private final org.joml.Quaternionf rotation; private final org.joml.Quaternionf overrideCameraAngle; private final int x0;
/*    */   private final int y0;
/*    */   private final int x1;
/*    */   private final int y1;
/*    */   private final float scale;
/*    */   private final net.minecraft.client.gui.navigation.ScreenRectangle scissorArea;
/*    */   private final net.minecraft.client.gui.navigation.ScreenRectangle bounds;
/*    */   
/*  9 */   public net.minecraft.client.gui.navigation.ScreenRectangle bounds() { return this.bounds; } public net.minecraft.client.gui.navigation.ScreenRectangle scissorArea() { return this.scissorArea; } public float scale() { return this.scale; } public int y1() { return this.y1; } public int x1() { return this.x1; } public int y0() { return this.y0; } public int x0() { return this.x0; } public org.joml.Quaternionf overrideCameraAngle() { return this.overrideCameraAngle; } public org.joml.Quaternionf rotation() { return this.rotation; } public org.joml.Vector3f translation() { return this.translation; } public net.minecraft.client.renderer.entity.state.EntityRenderState renderState() { return this.renderState; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/render/state/pip/GuiEntityRenderState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/render/state/pip/GuiEntityRenderState;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public GuiEntityRenderState(net.minecraft.client.renderer.entity.state.EntityRenderState renderState, org.joml.Vector3f translation, org.joml.Quaternionf rotation, org.joml.Quaternionf overrideCameraAngle, int x0, int y0, int x1, int y1, float scale, net.minecraft.client.gui.navigation.ScreenRectangle scissorArea, net.minecraft.client.gui.navigation.ScreenRectangle bounds) { this.renderState = renderState; this.translation = translation; this.rotation = rotation; this.overrideCameraAngle = overrideCameraAngle; this.x0 = x0; this.y0 = y0; this.x1 = x1; this.y1 = y1; this.scale = scale; this.scissorArea = scissorArea; this.bounds = bounds; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/render/state/pip/GuiEntityRenderState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/render/state/pip/GuiEntityRenderState; }
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/render/state/pip/GuiEntityRenderState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/render/state/pip/GuiEntityRenderState; } public GuiEntityRenderState(net.minecraft.client.renderer.entity.state.EntityRenderState renderState, org.joml.Vector3f translation, org.joml.Quaternionf rotation, org.joml.Quaternionf overrideCameraAngle, int x0, int y0, int x1, int y1, float scale, net.minecraft.client.gui.navigation.ScreenRectangle scissorArea) {
/* 12 */     this(renderState, translation, rotation, overrideCameraAngle, x0, y0, x1, y1, scale, scissorArea, PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea));
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/state/pip/GuiEntityRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */