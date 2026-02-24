/*    */ package com.mojang.realmsclient.dto;
/*    */ public final class PendingInvite extends Record { private final String invitationId;
/*    */   private final String realmName;
/*    */   private final String realmOwnerName;
/*    */   private final java.util.UUID realmOwnerUuid;
/*    */   private final java.time.Instant date;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/PendingInvite;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/PendingInvite;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/PendingInvite;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/PendingInvite;
/*    */   }
/*    */   
/* 16 */   public PendingInvite(String invitationId, String realmName, String realmOwnerName, java.util.UUID realmOwnerUuid, java.time.Instant date) { this.invitationId = invitationId; this.realmName = realmName; this.realmOwnerName = realmOwnerName; this.realmOwnerUuid = realmOwnerUuid; this.date = date; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/PendingInvite;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/PendingInvite;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public String invitationId() { return this.invitationId; } public String realmName() { return this.realmName; } public String realmOwnerName() { return this.realmOwnerName; } public java.util.UUID realmOwnerUuid() { return this.realmOwnerUuid; } public java.time.Instant date() { return this.date; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 23 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*    */   
/*    */   public static PendingInvite parse(com.google.gson.JsonObject json) {
/*    */     try {
/* 27 */       return new PendingInvite(
/* 28 */           com.mojang.realmsclient.util.JsonUtils.getStringOr("invitationId", json, ""), 
/* 29 */           com.mojang.realmsclient.util.JsonUtils.getStringOr("worldName", json, ""), 
/* 30 */           com.mojang.realmsclient.util.JsonUtils.getStringOr("worldOwnerName", json, ""), 
/* 31 */           com.mojang.realmsclient.util.JsonUtils.getUuidOr("worldOwnerUuid", json, net.minecraft.util.Util.NIL_UUID), 
/* 32 */           com.mojang.realmsclient.util.JsonUtils.getDateOr("date", json));
/*    */     }
/* 34 */     catch (Exception e) {
/* 35 */       LOGGER.error("Could not parse PendingInvite", e);
/*    */       
/* 37 */       return null;
/*    */     } 
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/PendingInvite.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */