/*    */ package net.minecraft.client;
/*    */ 
/*    */ import net.minecraft.util.FormattedCharSequence;
/*    */ 
/*    */ public final class GuiMessage extends Record {
/*    */   private final int addedTime;
/*    */   private final net.minecraft.network.chat.Component content;
/*    */   private final net.minecraft.network.chat.MessageSignature signature;
/*    */   private final GuiMessageTag tag;
/*    */   private static final int MESSAGE_TAG_MARGIN_LEFT = 4;
/*    */   
/* 12 */   public GuiMessage(int addedTime, net.minecraft.network.chat.Component content, net.minecraft.network.chat.MessageSignature signature, GuiMessageTag tag) { this.addedTime = addedTime; this.content = content; this.signature = signature; this.tag = tag; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/GuiMessage;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/client/GuiMessage; } public int addedTime() { return this.addedTime; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/GuiMessage;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/GuiMessage; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/GuiMessage;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/GuiMessage;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.network.chat.Component content() { return this.content; } public net.minecraft.network.chat.MessageSignature signature() { return this.signature; } public GuiMessageTag tag() { return this.tag; }
/*    */   
/*    */   public static final class Line extends Record { private final int addedTime; private final FormattedCharSequence content; private final GuiMessageTag tag; private final boolean endOfEntry;
/* 15 */     public Line(int addedTime, FormattedCharSequence content, GuiMessageTag tag, boolean endOfEntry) { this.addedTime = addedTime; this.content = content; this.tag = tag; this.endOfEntry = endOfEntry; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/GuiMessage$Line;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #15	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/GuiMessage$Line; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/GuiMessage$Line;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #15	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/GuiMessage$Line; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/GuiMessage$Line;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #15	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/GuiMessage$Line;
/* 15 */       //   0	8	1	o	Ljava/lang/Object; } public int addedTime() { return this.addedTime; } public FormattedCharSequence content() { return this.content; } public GuiMessageTag tag() { return this.tag; } public boolean endOfEntry() { return this.endOfEntry; }
/*    */      public int getTagIconLeft(net.minecraft.client.gui.Font font) {
/* 17 */       return font.width(this.content) + 4;
/*    */     } }
/*    */ 
/*    */ 
/*    */   
/*    */   public java.util.List<FormattedCharSequence> splitLines(net.minecraft.client.gui.Font font, int maxWidth) {
/* 23 */     if (this.tag != null && this.tag.icon() != null) {
/* 24 */       maxWidth -= (this.tag.icon()).width + 4 + 2;
/*    */     }
/*    */     
/* 27 */     return net.minecraft.client.gui.components.ComponentRenderUtils.wrapComponents((net.minecraft.network.chat.FormattedText)this.content, maxWidth, font);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/GuiMessage.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */