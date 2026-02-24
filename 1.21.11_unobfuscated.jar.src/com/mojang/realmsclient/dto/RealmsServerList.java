/*    */ package com.mojang.realmsclient.dto;
/*    */ public final class RealmsServerList extends Record implements ReflectionBasedSerialization { @com.google.gson.annotations.SerializedName("servers")
/*    */   private final java.util.List<RealmsServer> servers;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/RealmsServerList;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsServerList;
/*    */   }
/*    */   
/*  9 */   public RealmsServerList(java.util.List<RealmsServer> servers) { this.servers = servers; } @com.google.gson.annotations.SerializedName("servers") public java.util.List<RealmsServer> servers() { return this.servers; } public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/RealmsServerList;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/RealmsServerList;
/*    */   } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/RealmsServerList;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/RealmsServerList;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 13 */   } private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*    */   
/*    */   public static RealmsServerList parse(GuardedSerializer gson, String json) {
/*    */     try {
/* 17 */       RealmsServerList realmsServerList = gson.<RealmsServerList>fromJson(json, RealmsServerList.class);
/* 18 */       if (realmsServerList == null) {
/* 19 */         LOGGER.error("Could not parse McoServerList: {}", json);
/*    */       } else {
/* 21 */         realmsServerList.servers.forEach(RealmsServer::finalize);
/* 22 */         return realmsServerList;
/*    */       } 
/* 24 */     } catch (Exception e) {
/* 25 */       LOGGER.error("Could not parse McoServerList", e);
/*    */     } 
/* 27 */     return new RealmsServerList(java.util.List.of());
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/RealmsServerList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */