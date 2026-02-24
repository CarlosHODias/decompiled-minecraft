/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ public final class PreferredRegionsDto extends Record implements ReflectionBasedSerialization {
/*    */   @com.google.gson.annotations.SerializedName("regionDataList")
/*    */   private final java.util.List<RegionDataDto> regionData;
/*    */   
/*  7 */   public PreferredRegionsDto(java.util.List<RegionDataDto> regionData) { this.regionData = regionData; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/PreferredRegionsDto;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/PreferredRegionsDto; } @com.google.gson.annotations.SerializedName("regionDataList") public java.util.List<RegionDataDto> regionData() { return this.regionData; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/PreferredRegionsDto;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/PreferredRegionsDto; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/PreferredRegionsDto;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/PreferredRegionsDto;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   } public static PreferredRegionsDto empty() {
/* 11 */     return new PreferredRegionsDto(java.util.List.of());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/PreferredRegionsDto.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */