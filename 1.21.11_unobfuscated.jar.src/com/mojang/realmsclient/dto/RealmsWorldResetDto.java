/*   */ package com.mojang.realmsclient.dto;public final class RealmsWorldResetDto extends Record implements ReflectionBasedSerialization { @com.google.gson.annotations.SerializedName("seed")
/*   */   private final String seed; @com.google.gson.annotations.SerializedName("worldTemplateId")
/*   */   private final long worldTemplateId; @com.google.gson.annotations.SerializedName("levelType")
/*   */   private final int levelType; @com.google.gson.annotations.SerializedName("generateStructures")
/*   */   private final boolean generateStructures; @com.google.gson.annotations.SerializedName("experiments")
/*   */   private final java.util.Set<String> experiments;
/* 7 */   public RealmsWorldResetDto(String seed, long worldTemplateId, int levelType, boolean generateStructures, java.util.Set<String> experiments) { this.seed = seed; this.worldTemplateId = worldTemplateId; this.levelType = levelType; this.generateStructures = generateStructures; this.experiments = experiments; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsWorldResetDto;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 7 */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsWorldResetDto; } @com.google.gson.annotations.SerializedName("seed") public String seed() { return this.seed; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsWorldResetDto;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsWorldResetDto; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsWorldResetDto;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #7	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsWorldResetDto;
/* 7 */     //   0	8	1	o	Ljava/lang/Object; } @com.google.gson.annotations.SerializedName("worldTemplateId") public long worldTemplateId() { return this.worldTemplateId; } @com.google.gson.annotations.SerializedName("levelType") public int levelType() { return this.levelType; } @com.google.gson.annotations.SerializedName("generateStructures") public boolean generateStructures() { return this.generateStructures; } @com.google.gson.annotations.SerializedName("experiments") public java.util.Set<String> experiments() { return this.experiments; }
/*   */    }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RealmsWorldResetDto.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */