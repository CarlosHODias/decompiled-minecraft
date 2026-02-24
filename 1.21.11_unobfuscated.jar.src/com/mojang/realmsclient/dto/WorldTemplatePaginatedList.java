/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ 
/*    */ public final class WorldTemplatePaginatedList extends Record {
/*    */   private final java.util.List<WorldTemplate> templates;
/*    */   private final int page;
/*    */   private final int size;
/*    */   private final int total;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/WorldTemplatePaginatedList;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/WorldTemplatePaginatedList;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/WorldTemplatePaginatedList;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/WorldTemplatePaginatedList;
/*    */   }
/*    */   
/* 18 */   public WorldTemplatePaginatedList(java.util.List<WorldTemplate> templates, int page, int size, int total) { this.templates = templates; this.page = page; this.size = size; this.total = total; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/WorldTemplatePaginatedList;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/WorldTemplatePaginatedList;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.List<WorldTemplate> templates() { return this.templates; } public int page() { return this.page; } public int size() { return this.size; } public int total() { return this.total; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 24 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*    */   
/*    */   public WorldTemplatePaginatedList(int size) {
/* 27 */     this(
/* 28 */         java.util.List.of(), 0, size, -1);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isLastPage() {
/* 38 */     return (this.page * this.size >= this.total && this.page > 0 && this.total > 0 && this.size > 0);
/*    */   }
/*    */   
/*    */   public static WorldTemplatePaginatedList parse(String json) {
/* 42 */     java.util.List<WorldTemplate> templates = new java.util.ArrayList<>();
/* 43 */     int page = 0;
/* 44 */     int size = 0;
/* 45 */     int total = 0;
/*    */     try {
/* 47 */       com.google.gson.JsonObject object = net.minecraft.util.LenientJsonParser.parse(json).getAsJsonObject();
/* 48 */       if (object.get("templates").isJsonArray()) {
/* 49 */         for (com.google.gson.JsonElement element : (Iterable<com.google.gson.JsonElement>)object.get("templates").getAsJsonArray()) {
/* 50 */           WorldTemplate template = WorldTemplate.parse(element.getAsJsonObject());
/* 51 */           if (template != null) {
/* 52 */             templates.add(template);
/*    */           }
/*    */         } 
/*    */       }
/*    */       
/* 57 */       page = com.mojang.realmsclient.util.JsonUtils.getIntOr("page", object, 0);
/* 58 */       size = com.mojang.realmsclient.util.JsonUtils.getIntOr("size", object, 0);
/* 59 */       total = com.mojang.realmsclient.util.JsonUtils.getIntOr("total", object, 0);
/* 60 */     } catch (Exception e) {
/* 61 */       LOGGER.error("Could not parse WorldTemplatePaginatedList", e);
/*    */     } 
/* 63 */     return new WorldTemplatePaginatedList(templates, page, size, total);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/WorldTemplatePaginatedList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */