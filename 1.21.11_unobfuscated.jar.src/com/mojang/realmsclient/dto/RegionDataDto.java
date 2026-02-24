/*   */ package com.mojang.realmsclient.dto;public final class RegionDataDto extends Record implements ReflectionBasedSerialization { @com.google.gson.annotations.SerializedName("regionName")
/*   */   @com.google.gson.annotations.JsonAdapter(RealmsRegion.RealmsRegionJsonAdapter.class)
/*   */   private final RealmsRegion region; @com.google.gson.annotations.SerializedName("serviceQuality")
/*   */   @com.google.gson.annotations.JsonAdapter(ServiceQuality.RealmsServiceQualityJsonAdapter.class)
/*   */   private final ServiceQuality serviceQuality;
/* 6 */   public RegionDataDto(RealmsRegion region, ServiceQuality serviceQuality) { this.region = region; this.serviceQuality = serviceQuality; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RegionDataDto;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 6 */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RegionDataDto; } @com.google.gson.annotations.SerializedName("regionName") public RealmsRegion region() { return this.region; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RegionDataDto;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RegionDataDto; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RegionDataDto;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RegionDataDto;
/* 6 */     //   0	8	1	o	Ljava/lang/Object; } @com.google.gson.annotations.SerializedName("serviceQuality") public ServiceQuality serviceQuality() { return this.serviceQuality; }
/*   */    }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RegionDataDto.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */