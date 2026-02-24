/*   */ package net.minecraft.core;
/*   */ 
/*   */ 
/*   */ public final class RegistrationInfo extends Record {
/*   */   private final java.util.Optional<net.minecraft.server.packs.repository.KnownPack> knownPackInfo;
/*   */   private final com.mojang.serialization.Lifecycle lifecycle;
/*   */   
/* 8 */   public RegistrationInfo(java.util.Optional<net.minecraft.server.packs.repository.KnownPack> knownPackInfo, com.mojang.serialization.Lifecycle lifecycle) { this.knownPackInfo = knownPackInfo; this.lifecycle = lifecycle; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/RegistrationInfo;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 8 */     //   0	7	0	this	Lnet/minecraft/core/RegistrationInfo; } public java.util.Optional<net.minecraft.server.packs.repository.KnownPack> knownPackInfo() { return this.knownPackInfo; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/RegistrationInfo;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/core/RegistrationInfo; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/RegistrationInfo;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/core/RegistrationInfo;
/* 8 */     //   0	8	1	o	Ljava/lang/Object; } public com.mojang.serialization.Lifecycle lifecycle() { return this.lifecycle; }
/* 9 */    public static final RegistrationInfo BUILT_IN = new RegistrationInfo(java.util.Optional.empty(), com.mojang.serialization.Lifecycle.stable());
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/RegistrationInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */