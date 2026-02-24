/*   */ package com.mojang.realmsclient.dto;public final class RealmsConfigurationDto extends Record implements ReflectionBasedSerialization { @com.google.gson.annotations.SerializedName("options")
/*   */   private final RealmsSlotUpdateDto options; @com.google.gson.annotations.SerializedName("settings")
/*   */   private final java.util.List<RealmsSetting> settings; @com.google.gson.annotations.SerializedName("regionSelectionPreference")
/*   */   private final RegionSelectionPreferenceDto regionSelectionPreference;
/*   */   @com.google.gson.annotations.SerializedName("description")
/*   */   private final RealmsDescriptionDto description;
/*   */   
/* 8 */   public RealmsConfigurationDto(RealmsSlotUpdateDto options, java.util.List<RealmsSetting> settings, RegionSelectionPreferenceDto regionSelectionPreference, RealmsDescriptionDto description) { this.options = options; this.settings = settings; this.regionSelectionPreference = regionSelectionPreference; this.description = description; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsConfigurationDto;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 8 */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsConfigurationDto; } @com.google.gson.annotations.SerializedName("options") public RealmsSlotUpdateDto options() { return this.options; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsConfigurationDto;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsConfigurationDto; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsConfigurationDto;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsConfigurationDto;
/* 8 */     //   0	8	1	o	Ljava/lang/Object; } @com.google.gson.annotations.SerializedName("settings") public java.util.List<RealmsSetting> settings() { return this.settings; } @com.google.gson.annotations.SerializedName("regionSelectionPreference") public RegionSelectionPreferenceDto regionSelectionPreference() { return this.regionSelectionPreference; } @com.google.gson.annotations.SerializedName("description") public RealmsDescriptionDto description() { return this.description; }
/*   */    }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RealmsConfigurationDto.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */