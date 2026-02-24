/*    */ package com.mojang.realmsclient.dto;public final class RealmsSlotUpdateDto extends Record implements ReflectionBasedSerialization { @com.google.gson.annotations.SerializedName("slotId") private final int slotId; @com.google.gson.annotations.SerializedName("spawnProtection")
/*    */   private final int spawnProtection; @com.google.gson.annotations.SerializedName("forceGameMode")
/*    */   private final boolean forceGameMode; @com.google.gson.annotations.SerializedName("difficulty")
/*    */   private final int difficulty; @com.google.gson.annotations.SerializedName("gameMode")
/*    */   private final int gameMode; @com.google.gson.annotations.SerializedName("hardcore")
/*  6 */   public boolean hardcore() { return this.hardcore; } @com.google.gson.annotations.SerializedName("slotName") private final String slotName; @com.google.gson.annotations.SerializedName("version") private final String version; @com.google.gson.annotations.SerializedName("compatibility") private final RealmsServer.Compatibility compatibility; @com.google.gson.annotations.SerializedName("worldTemplateId") private final long templateId; @com.google.gson.annotations.SerializedName("worldTemplateImage") private final String templateImage; @com.google.gson.annotations.SerializedName("hardcore") private final boolean hardcore; @com.google.gson.annotations.SerializedName("worldTemplateImage") public String templateImage() { return this.templateImage; } @com.google.gson.annotations.SerializedName("worldTemplateId") public long templateId() { return this.templateId; } @com.google.gson.annotations.SerializedName("compatibility") public RealmsServer.Compatibility compatibility() { return this.compatibility; } @com.google.gson.annotations.SerializedName("version") public String version() { return this.version; } @com.google.gson.annotations.SerializedName("slotName") public String slotName() { return this.slotName; } @com.google.gson.annotations.SerializedName("gameMode") public int gameMode() { return this.gameMode; } @com.google.gson.annotations.SerializedName("difficulty") public int difficulty() { return this.difficulty; } @com.google.gson.annotations.SerializedName("forceGameMode") public boolean forceGameMode() { return this.forceGameMode; } @com.google.gson.annotations.SerializedName("spawnProtection") public int spawnProtection() { return this.spawnProtection; } @com.google.gson.annotations.SerializedName("slotId") public int slotId() { return this.slotId; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsSlotUpdateDto;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsSlotUpdateDto;
/*    */     //   0	8	1	o	Ljava/lang/Object; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsSlotUpdateDto;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsSlotUpdateDto; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsSlotUpdateDto;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  6 */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsSlotUpdateDto; } public RealmsSlotUpdateDto(int slotId, int spawnProtection, boolean forceGameMode, int difficulty, int gameMode, String slotName, String version, RealmsServer.Compatibility compatibility, long templateId, String templateImage, boolean hardcore) { this.slotId = slotId; this.spawnProtection = spawnProtection; this.forceGameMode = forceGameMode; this.difficulty = difficulty; this.gameMode = gameMode; this.slotName = slotName; this.version = version; this.compatibility = compatibility; this.templateId = templateId; this.templateImage = templateImage; this.hardcore = hardcore; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public RealmsSlotUpdateDto(int slotId, RealmsWorldOptions options, boolean hardcore) {
/* 21 */     this(slotId, options.spawnProtection, options.forceGameMode, options.difficulty, options.gameMode, 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 27 */         options.getSlotName(slotId), options.version, options.compatibility, options.templateId, options.templateImage, hardcore);
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RealmsSlotUpdateDto.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */