/*    */ package com.mojang.realmsclient.dto;public final class RealmsJoinInformation extends Record implements ReflectionBasedSerialization { @com.google.gson.annotations.SerializedName("address")
/*    */   private final String address; @com.google.gson.annotations.SerializedName("resourcePackUrl")
/*    */   private final String resourcePackUrl;
/*    */   @com.google.gson.annotations.SerializedName("resourcePackHash")
/*    */   private final String resourcePackHash;
/*    */   @com.google.gson.annotations.SerializedName("sessionRegionData")
/*    */   private final RegionData regionData;
/*    */   
/*  9 */   public RealmsJoinInformation(String address, String resourcePackUrl, String resourcePackHash, RegionData regionData) { this.address = address; this.resourcePackUrl = resourcePackUrl; this.resourcePackHash = resourcePackHash; this.regionData = regionData; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsJoinInformation;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  9 */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsJoinInformation; } @com.google.gson.annotations.SerializedName("address") public String address() { return this.address; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsJoinInformation;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsJoinInformation; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsJoinInformation;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsJoinInformation;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } @com.google.gson.annotations.SerializedName("resourcePackUrl") public String resourcePackUrl() { return this.resourcePackUrl; } @com.google.gson.annotations.SerializedName("resourcePackHash") public String resourcePackHash() { return this.resourcePackHash; } @com.google.gson.annotations.SerializedName("sessionRegionData") public RegionData regionData() { return this.regionData; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/* 16 */   private static final RealmsJoinInformation EMPTY = new RealmsJoinInformation(null, null, null, null); public static final class RegionData extends Record implements ReflectionBasedSerialization { @com.google.gson.annotations.SerializedName("regionName") @com.google.gson.annotations.JsonAdapter(RealmsRegion.RealmsRegionJsonAdapter.class) private final RealmsRegion region; @com.google.gson.annotations.SerializedName("serviceQuality")
/*    */     @com.google.gson.annotations.JsonAdapter(ServiceQuality.RealmsServiceQualityJsonAdapter.class)
/* 18 */     private final ServiceQuality serviceQuality; public RegionData(RealmsRegion region, ServiceQuality serviceQuality) { this.region = region; this.serviceQuality = serviceQuality; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsJoinInformation$RegionData;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsJoinInformation$RegionData; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsJoinInformation$RegionData;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsJoinInformation$RegionData; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsJoinInformation$RegionData;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsJoinInformation$RegionData;
/* 18 */       //   0	8	1	o	Ljava/lang/Object; } @com.google.gson.annotations.SerializedName("regionName") public RealmsRegion region() { return this.region; } @com.google.gson.annotations.SerializedName("serviceQuality") public ServiceQuality serviceQuality() { return this.serviceQuality; }
/*    */      }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static RealmsJoinInformation parse(GuardedSerializer gson, String json) {
/*    */     try {
/* 29 */       RealmsJoinInformation server = gson.<RealmsJoinInformation>fromJson(json, RealmsJoinInformation.class);
/* 30 */       if (server == null) {
/* 31 */         LOGGER.error("Could not parse RealmsServerAddress: {}", json);
/* 32 */         return EMPTY;
/*    */       } 
/* 34 */       return server;
/* 35 */     } catch (Exception e) {
/* 36 */       LOGGER.error("Could not parse RealmsServerAddress", e);
/* 37 */       return EMPTY;
/*    */     } 
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RealmsJoinInformation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */