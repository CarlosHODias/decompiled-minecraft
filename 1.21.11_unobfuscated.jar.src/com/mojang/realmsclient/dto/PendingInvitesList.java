/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.LenientJsonParser;
/*    */ 
/*    */ public final class PendingInvitesList extends Record {
/*    */   private final List<PendingInvite> pendingInvites;
/*    */   
/* 12 */   public PendingInvitesList(List<PendingInvite> pendingInvites) { this.pendingInvites = pendingInvites; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/PendingInvitesList;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/PendingInvitesList; } public List<PendingInvite> pendingInvites() { return this.pendingInvites; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/PendingInvitesList;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/PendingInvitesList; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/PendingInvitesList;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/PendingInvitesList;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 15 */   } private static final org.slf4j.Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static PendingInvitesList parse(String json) {
/* 18 */     List<PendingInvite> pendingInvites = new java.util.ArrayList<>();
/*    */     try {
/* 20 */       JsonObject jsonObject = LenientJsonParser.parse(json).getAsJsonObject();
/* 21 */       if (jsonObject.get("invites").isJsonArray()) {
/* 22 */         for (JsonElement element : (Iterable<JsonElement>)jsonObject.get("invites").getAsJsonArray()) {
/* 23 */           PendingInvite entry = PendingInvite.parse(element.getAsJsonObject());
/* 24 */           if (entry != null) {
/* 25 */             pendingInvites.add(entry);
/*    */           }
/*    */         } 
/*    */       }
/* 29 */     } catch (Exception e) {
/* 30 */       LOGGER.error("Could not parse PendingInvitesList", e);
/*    */     } 
/* 32 */     return new PendingInvitesList(pendingInvites);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/PendingInvitesList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */