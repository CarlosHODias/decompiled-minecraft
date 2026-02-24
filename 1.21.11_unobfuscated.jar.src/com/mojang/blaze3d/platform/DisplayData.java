/*    */ package com.mojang.blaze3d.platform;public final class DisplayData extends Record { private final int width; private final int height; private final java.util.OptionalInt fullscreenWidth;
/*    */   private final java.util.OptionalInt fullscreenHeight;
/*    */   private final boolean isFullscreen;
/*    */   
/*  5 */   public DisplayData(int width, int height, java.util.OptionalInt fullscreenWidth, java.util.OptionalInt fullscreenHeight, boolean isFullscreen) { this.width = width; this.height = height; this.fullscreenWidth = fullscreenWidth; this.fullscreenHeight = fullscreenHeight; this.isFullscreen = isFullscreen; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/blaze3d/platform/DisplayData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  5 */     //   0	7	0	this	Lcom/mojang/blaze3d/platform/DisplayData; } public int width() { return this.width; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/blaze3d/platform/DisplayData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/blaze3d/platform/DisplayData; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/blaze3d/platform/DisplayData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/blaze3d/platform/DisplayData;
/*  5 */     //   0	8	1	o	Ljava/lang/Object; } public int height() { return this.height; } public java.util.OptionalInt fullscreenWidth() { return this.fullscreenWidth; } public java.util.OptionalInt fullscreenHeight() { return this.fullscreenHeight; } public boolean isFullscreen() { return this.isFullscreen; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public DisplayData withSize(int width, int height) {
/* 13 */     return new DisplayData(width, height, this.fullscreenWidth, this.fullscreenHeight, this.isFullscreen);
/*    */   }
/*    */   
/*    */   public DisplayData withFullscreen(boolean isFullscreen) {
/* 17 */     return new DisplayData(this.width, this.height, this.fullscreenWidth, this.fullscreenHeight, isFullscreen);
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/platform/DisplayData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */