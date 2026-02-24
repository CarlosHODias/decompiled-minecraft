/*   */ package net.minecraft.client.resources.server;public interface PackReloadConfig { void scheduleReload(Callbacks paramCallbacks);
/*   */   public static interface Callbacks { void onSuccess();
/*   */     void onFailure(boolean param1Boolean);
/*   */     java.util.List<PackReloadConfig.IdAndPath> packsToLoad(); }
/*   */   public static final class IdAndPath extends Record { private final java.util.UUID id;
/*   */     private final java.nio.file.Path path;
/*   */     
/* 8 */     public IdAndPath(java.util.UUID id, java.nio.file.Path path) { this.id = id; this.path = path; } public final String toString() { // Byte code:
/*   */       //   0: aload_0
/*   */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/server/PackReloadConfig$IdAndPath;)Ljava/lang/String;
/*   */       //   6: areturn
/*   */       // Line number table:
/*   */       //   Java source line number -> byte code offset
/*   */       //   #8	-> 0
/*   */       // Local variable table:
/*   */       //   start	length	slot	name	descriptor
/* 8 */       //   0	7	0	this	Lnet/minecraft/client/resources/server/PackReloadConfig$IdAndPath; } public java.util.UUID id() { return this.id; } public final int hashCode() { // Byte code:
/*   */       //   0: aload_0
/*   */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/server/PackReloadConfig$IdAndPath;)I
/*   */       //   6: ireturn
/*   */       // Line number table:
/*   */       //   Java source line number -> byte code offset
/*   */       //   #8	-> 0
/*   */       // Local variable table:
/*   */       //   start	length	slot	name	descriptor
/*   */       //   0	7	0	this	Lnet/minecraft/client/resources/server/PackReloadConfig$IdAndPath; } public final boolean equals(Object o) { // Byte code:
/*   */       //   0: aload_0
/*   */       //   1: aload_1
/*   */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/server/PackReloadConfig$IdAndPath;Ljava/lang/Object;)Z
/*   */       //   7: ireturn
/*   */       // Line number table:
/*   */       //   Java source line number -> byte code offset
/*   */       //   #8	-> 0
/*   */       // Local variable table:
/*   */       //   start	length	slot	name	descriptor
/*   */       //   0	8	0	this	Lnet/minecraft/client/resources/server/PackReloadConfig$IdAndPath;
/* 8 */       //   0	8	1	o	Ljava/lang/Object; } public java.nio.file.Path path() { return this.path; }
/*   */      }
/*   */    }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/server/PackReloadConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */