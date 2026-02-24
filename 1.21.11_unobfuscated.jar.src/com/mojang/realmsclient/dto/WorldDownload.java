/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ 
/*    */ public final class WorldDownload extends Record {
/*    */   private final String downloadLink;
/*    */   private final String resourcePackUrl;
/*    */   private final String resourcePackHash;
/*    */   
/* 10 */   public WorldDownload(String downloadLink, String resourcePackUrl, String resourcePackHash) { this.downloadLink = downloadLink; this.resourcePackUrl = resourcePackUrl; this.resourcePackHash = resourcePackHash; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/WorldDownload;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/WorldDownload; } public String downloadLink() { return this.downloadLink; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/WorldDownload;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/WorldDownload; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/WorldDownload;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/WorldDownload;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public String resourcePackUrl() { return this.resourcePackUrl; } public String resourcePackHash() { return this.resourcePackHash; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 15 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*    */   
/*    */   public static WorldDownload parse(String json) {
/* 18 */     JsonObject jsonObject = net.minecraft.util.LenientJsonParser.parse(json).getAsJsonObject();
/*    */     try {
/* 20 */       return new WorldDownload(
/* 21 */           com.mojang.realmsclient.util.JsonUtils.getStringOr("downloadLink", jsonObject, ""), 
/* 22 */           com.mojang.realmsclient.util.JsonUtils.getStringOr("resourcePackUrl", jsonObject, ""), 
/* 23 */           com.mojang.realmsclient.util.JsonUtils.getStringOr("resourcePackHash", jsonObject, ""));
/*    */     }
/* 25 */     catch (Exception e) {
/* 26 */       LOGGER.error("Could not parse WorldDownload", e);
/*    */ 
/*    */       
/* 29 */       return new WorldDownload("", "", "");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/WorldDownload.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */