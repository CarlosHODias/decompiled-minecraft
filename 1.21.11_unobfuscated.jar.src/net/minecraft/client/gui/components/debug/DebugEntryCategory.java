/*   */ package net.minecraft.client.gui.components.debug;
/*   */ public final class DebugEntryCategory extends Record {
/*   */   private final net.minecraft.network.chat.Component label;
/*   */   private final float sortKey;
/*   */   
/* 6 */   public DebugEntryCategory(net.minecraft.network.chat.Component label, float sortKey) { this.label = label; this.sortKey = sortKey; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/debug/DebugEntryCategory;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 6 */     //   0	7	0	this	Lnet/minecraft/client/gui/components/debug/DebugEntryCategory; } public net.minecraft.network.chat.Component label() { return this.label; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/debug/DebugEntryCategory;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/gui/components/debug/DebugEntryCategory; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/debug/DebugEntryCategory;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/gui/components/debug/DebugEntryCategory;
/* 6 */     //   0	8	1	o	Ljava/lang/Object; } public float sortKey() { return this.sortKey; }
/* 7 */    public static final DebugEntryCategory SCREEN_TEXT = new DebugEntryCategory((net.minecraft.network.chat.Component)net.minecraft.network.chat.Component.translatable("debug.options.category.text"), 1.0F);
/* 8 */   public static final DebugEntryCategory RENDERER = new DebugEntryCategory((net.minecraft.network.chat.Component)net.minecraft.network.chat.Component.translatable("debug.options.category.renderer"), 2.0F);
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/debug/DebugEntryCategory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */