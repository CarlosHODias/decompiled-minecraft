/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.LenientJsonParser;
/*    */ 
/*    */ public final class BackupList extends Record {
/*    */   private final List<Backup> backups;
/*    */   
/* 11 */   public BackupList(List<Backup> backups) { this.backups = backups; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/BackupList;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/BackupList; } public List<Backup> backups() { return this.backups; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/BackupList;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/BackupList; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/BackupList;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/BackupList;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 14 */   } private static final org.slf4j.Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static BackupList parse(String json) {
/* 17 */     List<Backup> backups = new java.util.ArrayList<>();
/*    */     try {
/* 19 */       JsonElement node = LenientJsonParser.parse(json).getAsJsonObject().get("backups");
/* 20 */       if (node.isJsonArray()) {
/* 21 */         for (JsonElement element : (Iterable<JsonElement>)node.getAsJsonArray()) {
/* 22 */           Backup entry = Backup.parse(element);
/* 23 */           if (entry != null) {
/* 24 */             backups.add(entry);
/*    */           }
/*    */         } 
/*    */       }
/* 28 */     } catch (Exception e) {
/* 29 */       LOGGER.error("Could not parse BackupList", e);
/*    */     } 
/* 31 */     return new BackupList(backups);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/BackupList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */