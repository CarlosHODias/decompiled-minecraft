/*    */ package net.minecraft.client;
/*    */ public final class Line extends Record { private final int addedTime;
/*    */   private final net.minecraft.util.FormattedCharSequence content;
/*    */   private final GuiMessageTag tag;
/*    */   private final boolean endOfEntry;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/GuiMessage$Line;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/GuiMessage$Line;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/GuiMessage$Line;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/GuiMessage$Line;
/*    */   }
/*    */   
/* 15 */   public Line(int addedTime, net.minecraft.util.FormattedCharSequence content, GuiMessageTag tag, boolean endOfEntry) { this.addedTime = addedTime; this.content = content; this.tag = tag; this.endOfEntry = endOfEntry; } public int addedTime() { return this.addedTime; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/GuiMessage$Line;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/GuiMessage$Line;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.util.FormattedCharSequence content() { return this.content; } public GuiMessageTag tag() { return this.tag; } public boolean endOfEntry() { return this.endOfEntry; }
/*    */    public int getTagIconLeft(net.minecraft.client.gui.Font font) {
/* 17 */     return font.width(this.content) + 4;
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/GuiMessage$Line.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */