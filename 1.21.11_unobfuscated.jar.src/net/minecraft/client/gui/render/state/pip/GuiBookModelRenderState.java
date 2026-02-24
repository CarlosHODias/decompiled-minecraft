/*    */ package net.minecraft.client.gui.render.state.pip;public final class GuiBookModelRenderState extends Record implements PictureInPictureRenderState { private final net.minecraft.client.model.object.book.BookModel bookModel; private final net.minecraft.resources.Identifier texture; private final float open; private final float flip; private final int x0; private final int y0;
/*    */   private final int x1;
/*    */   private final int y1;
/*    */   private final float scale;
/*    */   private final net.minecraft.client.gui.navigation.ScreenRectangle scissorArea;
/*    */   private final net.minecraft.client.gui.navigation.ScreenRectangle bounds;
/*    */   
/*  8 */   public net.minecraft.client.gui.navigation.ScreenRectangle bounds() { return this.bounds; } public net.minecraft.client.gui.navigation.ScreenRectangle scissorArea() { return this.scissorArea; } public float scale() { return this.scale; } public int y1() { return this.y1; } public int x1() { return this.x1; } public int y0() { return this.y0; } public int x0() { return this.x0; } public float flip() { return this.flip; } public float open() { return this.open; } public net.minecraft.resources.Identifier texture() { return this.texture; } public net.minecraft.client.model.object.book.BookModel bookModel() { return this.bookModel; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/render/state/pip/GuiBookModelRenderState;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/render/state/pip/GuiBookModelRenderState;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public GuiBookModelRenderState(net.minecraft.client.model.object.book.BookModel bookModel, net.minecraft.resources.Identifier texture, float open, float flip, int x0, int y0, int x1, int y1, float scale, net.minecraft.client.gui.navigation.ScreenRectangle scissorArea, net.minecraft.client.gui.navigation.ScreenRectangle bounds) { this.bookModel = bookModel; this.texture = texture; this.open = open; this.flip = flip; this.x0 = x0; this.y0 = y0; this.x1 = x1; this.y1 = y1; this.scale = scale; this.scissorArea = scissorArea; this.bounds = bounds; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/render/state/pip/GuiBookModelRenderState;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/render/state/pip/GuiBookModelRenderState; }
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/render/state/pip/GuiBookModelRenderState;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/render/state/pip/GuiBookModelRenderState; } public GuiBookModelRenderState(net.minecraft.client.model.object.book.BookModel bookModel, net.minecraft.resources.Identifier texture, float open, float flip, int x0, int y0, int x1, int y1, float scale, net.minecraft.client.gui.navigation.ScreenRectangle scissorArea) {
/* 11 */     this(bookModel, texture, open, flip, x0, y0, x1, y1, scale, scissorArea, PictureInPictureRenderState.getBounds(x0, y0, x1, y1, scissorArea));
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/render/state/pip/GuiBookModelRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */