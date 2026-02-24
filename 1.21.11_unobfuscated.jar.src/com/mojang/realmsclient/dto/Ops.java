/*    */ package com.mojang.realmsclient.dto;
/*    */ 
/*    */ import com.google.gson.JsonElement;
/*    */ import com.google.gson.JsonObject;
/*    */ import com.mojang.logging.LogUtils;
/*    */ import java.util.Set;
/*    */ import net.minecraft.util.LenientJsonParser;
/*    */ 
/*    */ public final class Ops extends Record {
/*    */   private final Set<String> ops;
/*    */   
/* 12 */   public Ops(Set<String> ops) { this.ops = ops; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/Ops;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/Ops; } public Set<String> ops() { return this.ops; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/Ops;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/Ops; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/Ops;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/Ops;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 15 */   } private static final org.slf4j.Logger LOGGER = LogUtils.getLogger();
/*    */   
/*    */   public static Ops parse(String json) {
/* 18 */     Set<String> ops = new java.util.HashSet<>();
/*    */     try {
/* 20 */       JsonObject jsonObject = LenientJsonParser.parse(json).getAsJsonObject();
/* 21 */       JsonElement opsArray = jsonObject.get("ops");
/* 22 */       if (opsArray.isJsonArray()) {
/* 23 */         for (JsonElement opsElement : (Iterable<JsonElement>)opsArray.getAsJsonArray()) {
/* 24 */           ops.add(opsElement.getAsString());
/*    */         }
/*    */       }
/* 27 */     } catch (Exception e) {
/* 28 */       LOGGER.error("Could not parse Ops", e);
/*    */     } 
/* 30 */     return new Ops(ops);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/Ops.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */