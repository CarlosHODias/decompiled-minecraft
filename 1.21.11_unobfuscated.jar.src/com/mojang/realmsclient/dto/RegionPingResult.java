/*    */ package com.mojang.realmsclient.dto;
/*    */ public final class RegionPingResult extends Record implements ReflectionBasedSerialization { @com.google.gson.annotations.SerializedName("regionName")
/*    */   private final String regionName;
/*    */   @com.google.gson.annotations.SerializedName("ping")
/*    */   private final int ping;
/*    */   
/*  7 */   public RegionPingResult(String regionName, int ping) { this.regionName = regionName; this.ping = ping; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RegionPingResult;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RegionPingResult; } @com.google.gson.annotations.SerializedName("regionName") public String regionName() { return this.regionName; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RegionPingResult;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RegionPingResult;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } @com.google.gson.annotations.SerializedName("ping") public int ping() { return this.ping; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 14 */     return String.format(java.util.Locale.ROOT, "%s --> %.2f ms", new Object[] { this.regionName, this.ping });
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RegionPingResult.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */