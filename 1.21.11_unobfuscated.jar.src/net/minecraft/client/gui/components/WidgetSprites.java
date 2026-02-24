/*    */ package net.minecraft.client.gui.components;public final class WidgetSprites extends Record { private final net.minecraft.resources.Identifier enabled; private final net.minecraft.resources.Identifier disabled;
/*    */   private final net.minecraft.resources.Identifier enabledFocused;
/*    */   private final net.minecraft.resources.Identifier disabledFocused;
/*    */   
/*  5 */   public WidgetSprites(net.minecraft.resources.Identifier enabled, net.minecraft.resources.Identifier disabled, net.minecraft.resources.Identifier enabledFocused, net.minecraft.resources.Identifier disabledFocused) { this.enabled = enabled; this.disabled = disabled; this.enabledFocused = enabledFocused; this.disabledFocused = disabledFocused; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/WidgetSprites;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  5 */     //   0	7	0	this	Lnet/minecraft/client/gui/components/WidgetSprites; } public net.minecraft.resources.Identifier enabled() { return this.enabled; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/WidgetSprites;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/gui/components/WidgetSprites; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/WidgetSprites;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #5	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/gui/components/WidgetSprites;
/*  5 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.resources.Identifier disabled() { return this.disabled; } public net.minecraft.resources.Identifier enabledFocused() { return this.enabledFocused; } public net.minecraft.resources.Identifier disabledFocused() { return this.disabledFocused; }
/*    */    public WidgetSprites(net.minecraft.resources.Identifier sprite) {
/*  7 */     this(sprite, sprite, sprite, sprite);
/*    */   }
/*    */   
/*    */   public WidgetSprites(net.minecraft.resources.Identifier sprite, net.minecraft.resources.Identifier focused) {
/* 11 */     this(sprite, sprite, focused, focused);
/*    */   }
/*    */   
/*    */   public WidgetSprites(net.minecraft.resources.Identifier enabled, net.minecraft.resources.Identifier disabled, net.minecraft.resources.Identifier focused) {
/* 15 */     this(enabled, disabled, focused, disabled);
/*    */   }
/*    */   
/*    */   public net.minecraft.resources.Identifier get(boolean enabled, boolean focused) {
/* 19 */     if (enabled) {
/* 20 */       return focused ? this.enabledFocused : this.enabled;
/*    */     }
/* 22 */     return focused ? this.disabledFocused : this.disabled;
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/WidgetSprites.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */