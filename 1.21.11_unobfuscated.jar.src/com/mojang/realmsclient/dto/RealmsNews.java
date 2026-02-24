/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.realmsclient.util.JsonUtils;
/*    */ import net.minecraft.util.LenientJsonParser;
/*    */ 
/*    */ public final class RealmsNews extends Record {
/*    */   private final String newsLink;
/*    */   
/* 11 */   public RealmsNews(String newsLink) { this.newsLink = newsLink; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsNews;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsNews; } public String newsLink() { return this.newsLink; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsNews;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsNews; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsNews;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsNews;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 14 */   } private static final org.slf4j.Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static RealmsNews parse(String json) {
/* 17 */     String newsLink = null;
/*    */     try {
/* 19 */       JsonObject object = LenientJsonParser.parse(json).getAsJsonObject();
/* 20 */       newsLink = JsonUtils.getStringOr("newsLink", object, null);
/* 21 */     } catch (Exception e) {
/* 22 */       LOGGER.error("Could not parse RealmsNews", e);
/*    */     } 
/*    */     
/* 25 */     return new RealmsNews(newsLink);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RealmsNews.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */