/*    */ package net.minecraft.server.network;
/*    */ public final class FilteredText extends Record { private final String raw; private final net.minecraft.network.chat.FilterMask mask; public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/network/FilteredText;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/network/FilteredText;
/*    */   }
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/network/FilteredText;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/network/FilteredText;
/*    */   }
/*  8 */   public FilteredText(String raw, net.minecraft.network.chat.FilterMask mask) { this.raw = raw; this.mask = mask; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/network/FilteredText;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/network/FilteredText;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public String raw() { return this.raw; } public net.minecraft.network.chat.FilterMask mask() { return this.mask; }
/*  9 */    public static final FilteredText EMPTY = passThrough("");
/*    */   
/*    */   public static FilteredText passThrough(String message) {
/* 12 */     return new FilteredText(message, net.minecraft.network.chat.FilterMask.PASS_THROUGH);
/*    */   }
/*    */   
/*    */   public static FilteredText fullyFiltered(String message) {
/* 16 */     return new FilteredText(message, net.minecraft.network.chat.FilterMask.FULLY_FILTERED);
/*    */   }
/*    */   
/*    */   public String filtered() {
/* 20 */     return this.mask.apply(this.raw);
/*    */   }
/*    */   
/*    */   public String filteredOrEmpty() {
/* 24 */     return java.util.Objects.<String>requireNonNullElse(filtered(), "");
/*    */   }
/*    */   
/*    */   public boolean isFiltered() {
/* 28 */     return !this.mask.isEmpty();
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/network/FilteredText.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */