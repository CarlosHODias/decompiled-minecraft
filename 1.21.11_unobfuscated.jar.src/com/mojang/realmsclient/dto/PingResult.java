/*   */ package com.mojang.realmsclient.dto;
/*   */ public final class PingResult extends Record implements ReflectionBasedSerialization { @com.google.gson.annotations.SerializedName("pingResults")
/*   */   private final java.util.List<RegionPingResult> pingResults;
/*   */   @com.google.gson.annotations.SerializedName("worldIds")
/*   */   private final java.util.List<Long> realmIds;
/*   */   
/* 7 */   public PingResult(java.util.List<RegionPingResult> pingResults, java.util.List<Long> realmIds) { this.pingResults = pingResults; this.realmIds = realmIds; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/PingResult;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 7 */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/PingResult; } @com.google.gson.annotations.SerializedName("pingResults") public java.util.List<RegionPingResult> pingResults() { return this.pingResults; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/PingResult;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/PingResult; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/PingResult;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/PingResult;
/* 7 */     //   0	8	1	o	Ljava/lang/Object; } @com.google.gson.annotations.SerializedName("worldIds") public java.util.List<Long> realmIds() { return this.realmIds; }
/*   */    }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/PingResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */