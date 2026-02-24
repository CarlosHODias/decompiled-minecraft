/*   */ package net.minecraft.world.level.validation;
/*   */ public final class ForbiddenSymlinkInfo extends Record { private final java.nio.file.Path link;
/*   */   private final java.nio.file.Path target;
/*   */   
/* 5 */   public ForbiddenSymlinkInfo(java.nio.file.Path link, java.nio.file.Path target) { this.link = link; this.target = target; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/validation/ForbiddenSymlinkInfo;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 5 */     //   0	7	0	this	Lnet/minecraft/world/level/validation/ForbiddenSymlinkInfo; } public java.nio.file.Path link() { return this.link; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/validation/ForbiddenSymlinkInfo;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/level/validation/ForbiddenSymlinkInfo; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/validation/ForbiddenSymlinkInfo;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #5	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/level/validation/ForbiddenSymlinkInfo;
/* 5 */     //   0	8	1	o	Ljava/lang/Object; } public java.nio.file.Path target() { return this.target; }
/*   */    }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/validation/ForbiddenSymlinkInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */