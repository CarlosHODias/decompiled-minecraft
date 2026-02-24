/*    */ package net.minecraft.client.gui.render.state.pip;
/*    */ public final class OversizedItemRenderState extends Record implements PictureInPictureRenderState {
/*    */   private final net.minecraft.client.gui.render.state.GuiItemRenderState guiItemRenderState;
/*    */   private final int x0;
/*    */   private final int y0;
/*    */   private final int x1;
/*    */   private final int y1;
/*    */   
/*  9 */   public OversizedItemRenderState(net.minecraft.client.gui.render.state.GuiItemRenderState guiItemRenderState, int x0, int y0, int x1, int y1) { this.guiItemRenderState = guiItemRenderState; this.x0 = x0; this.y0 = y0; this.x1 = x1; this.y1 = y1; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/render/state/pip/OversizedItemRenderState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lnet/minecraft/client/gui/render/state/pip/OversizedItemRenderState; } public net.minecraft.client.gui.render.state.GuiItemRenderState guiItemRenderState() { return this.guiItemRenderState; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/render/state/pip/OversizedItemRenderState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/render/state/pip/OversizedItemRenderState; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/render/state/pip/OversizedItemRenderState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/render/state/pip/OversizedItemRenderState;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public int x0() { return this.x0; } public int y0() { return this.y0; } public int x1() { return this.x1; } public int y1() { return this.y1; }
/*    */   
/*    */   public float scale() {
/* 12 */     return 16.0F;
/*    */   }
/*    */ 
/*    */   
/*    */   public org.joml.Matrix3x2f pose() {
/* 17 */     return this.guiItemRenderState.pose();
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.client.gui.navigation.ScreenRectangle scissorArea() {
/* 22 */     return this.guiItemRenderState.scissorArea();
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.client.gui.navigation.ScreenRectangle bounds() {
/* 27 */     return this.guiItemRenderState.bounds();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/state/pip/OversizedItemRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */