/*    */ package com.mojang.realmsclient.dto;public final class WorldTemplate extends Record { private final String id; private final String name;
/*    */   private final String version;
/*    */   private final String author;
/*    */   private final String link;
/*    */   private final String image;
/*    */   private final String trailer;
/*    */   private final String recommendedPlayers;
/*    */   private final WorldTemplateType type;
/*    */   
/* 10 */   public WorldTemplate(String id, String name, String version, String author, String link, String image, String trailer, String recommendedPlayers, WorldTemplateType type) { this.id = id; this.name = name; this.version = version; this.author = author; this.link = link; this.image = image; this.trailer = trailer; this.recommendedPlayers = recommendedPlayers; this.type = type; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/WorldTemplate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/WorldTemplate; } public String id() { return this.id; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/WorldTemplate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/WorldTemplate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/WorldTemplate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/WorldTemplate;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public String version() { return this.version; } public String author() { return this.author; } public String link() { return this.link; } public String image() { return this.image; } public String trailer() { return this.trailer; } public String recommendedPlayers() { return this.recommendedPlayers; } public WorldTemplateType type() { return this.type; }
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
/* 21 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*    */   
/*    */   public static WorldTemplate parse(com.google.gson.JsonObject node) {
/*    */     try {
/* 25 */       String templateTypeName = com.mojang.realmsclient.util.JsonUtils.getStringOr("type", node, null);
/* 26 */       return new WorldTemplate(
/* 27 */           com.mojang.realmsclient.util.JsonUtils.getStringOr("id", node, ""), 
/* 28 */           com.mojang.realmsclient.util.JsonUtils.getStringOr("name", node, ""), 
/* 29 */           com.mojang.realmsclient.util.JsonUtils.getStringOr("version", node, ""), 
/* 30 */           com.mojang.realmsclient.util.JsonUtils.getStringOr("author", node, ""), 
/* 31 */           com.mojang.realmsclient.util.JsonUtils.getStringOr("link", node, ""), 
/* 32 */           com.mojang.realmsclient.util.JsonUtils.getStringOr("image", node, null), 
/* 33 */           com.mojang.realmsclient.util.JsonUtils.getStringOr("trailer", node, ""), 
/* 34 */           com.mojang.realmsclient.util.JsonUtils.getStringOr("recommendedPlayers", node, ""), 
/* 35 */           (templateTypeName == null) ? WorldTemplateType.WORLD_TEMPLATE : WorldTemplateType.valueOf(templateTypeName));
/*    */     }
/* 37 */     catch (Exception e) {
/* 38 */       LOGGER.error("Could not parse WorldTemplate", e);
/*    */       
/* 40 */       return null;
/*    */     } 
/*    */   }
/*    */   
/* 44 */   public enum WorldTemplateType { WORLD_TEMPLATE,
/* 45 */     MINIGAME,
/* 46 */     ADVENTUREMAP,
/* 47 */     EXPERIENCE,
/* 48 */     INSPIRATION; }
/*    */    }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/WorldTemplate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */