/*    */ package net.minecraft.client.renderer;
/*    */ public final class MaterialMapper extends Record {
/*    */   private final net.minecraft.resources.Identifier sheet;
/*    */   private final String prefix;
/*    */   
/*  6 */   public MaterialMapper(net.minecraft.resources.Identifier sheet, String prefix) { this.sheet = sheet; this.prefix = prefix; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/MaterialMapper;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  6 */     //   0	7	0	this	Lnet/minecraft/client/renderer/MaterialMapper; } public net.minecraft.resources.Identifier sheet() { return this.sheet; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/MaterialMapper;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/MaterialMapper; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/MaterialMapper;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/MaterialMapper;
/*  6 */     //   0	8	1	o	Ljava/lang/Object; } public String prefix() { return this.prefix; }
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.client.resources.model.Material apply(net.minecraft.resources.Identifier path) {
/* 11 */     return new net.minecraft.client.resources.model.Material(this.sheet, path.withPrefix(this.prefix + "/"));
/*    */   }
/*    */   
/*    */   public net.minecraft.client.resources.model.Material defaultNamespaceApply(String path) {
/* 15 */     return apply(net.minecraft.resources.Identifier.withDefaultNamespace(path));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/MaterialMapper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */