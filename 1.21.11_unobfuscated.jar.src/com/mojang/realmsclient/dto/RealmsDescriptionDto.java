/*   */ package com.mojang.realmsclient.dto;public final class RealmsDescriptionDto extends Record implements ReflectionBasedSerialization { @com.google.gson.annotations.SerializedName("name")
/*   */   private final String name;
/*   */   @com.google.gson.annotations.SerializedName("description")
/*   */   private final String description;
/*   */   
/* 6 */   public RealmsDescriptionDto(String name, String description) { this.name = name; this.description = description; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsDescriptionDto;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 6 */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsDescriptionDto; } @com.google.gson.annotations.SerializedName("name") public String name() { return this.name; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsDescriptionDto;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsDescriptionDto; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsDescriptionDto;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsDescriptionDto;
/* 6 */     //   0	8	1	o	Ljava/lang/Object; } @com.google.gson.annotations.SerializedName("description") public String description() { return this.description; }
/*   */    }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RealmsDescriptionDto.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */