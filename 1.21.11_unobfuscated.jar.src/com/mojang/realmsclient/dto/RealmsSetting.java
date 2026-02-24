/*    */ package com.mojang.realmsclient.dto;
/*    */ public final class RealmsSetting extends Record implements ReflectionBasedSerialization { @com.google.gson.annotations.SerializedName("name")
/*    */   private final String name;
/*    */   @com.google.gson.annotations.SerializedName("value")
/*    */   private final String value;
/*    */   
/*  7 */   public RealmsSetting(String name, String value) { this.name = name; this.value = value; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsSetting;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsSetting; } @com.google.gson.annotations.SerializedName("name") public String name() { return this.name; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsSetting;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsSetting; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsSetting;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsSetting;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } @com.google.gson.annotations.SerializedName("value") public String value() { return this.value; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static RealmsSetting hardcoreSetting(boolean hardcore) {
/* 13 */     return new RealmsSetting("hardcore", Boolean.toString(hardcore));
/*    */   }
/*    */   
/*    */   public static boolean isHardcore(java.util.List<RealmsSetting> settings) {
/* 17 */     for (RealmsSetting setting : settings) {
/* 18 */       if (setting.name().equals("hardcore")) {
/* 19 */         return Boolean.parseBoolean(setting.value());
/*    */       }
/*    */     } 
/* 22 */     return false;
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RealmsSetting.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */