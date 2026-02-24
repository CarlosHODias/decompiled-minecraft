/*    */ package com.mojang.realmsclient.dto;
/*    */ public final class Subscription extends Record {
/*    */   private final java.time.Instant startDate;
/*    */   private final int daysLeft;
/*    */   private final SubscriptionType type;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/dto/Subscription;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/Subscription;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/dto/Subscription;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lcom/mojang/realmsclient/dto/Subscription;
/*    */   }
/*    */   
/* 15 */   public Subscription(java.time.Instant startDate, int daysLeft, SubscriptionType type) { this.startDate = startDate; this.daysLeft = daysLeft; this.type = type; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/dto/Subscription;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lcom/mojang/realmsclient/dto/Subscription;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public java.time.Instant startDate() { return this.startDate; } public int daysLeft() { return this.daysLeft; } public SubscriptionType type() { return this.type; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 20 */   private static final org.slf4j.Logger LOGGER = com.mojang.logging.LogUtils.getLogger();
/*    */   
/*    */   public static Subscription parse(String json) {
/*    */     try {
/* 24 */       com.google.gson.JsonObject jsonObject = net.minecraft.util.LenientJsonParser.parse(json).getAsJsonObject();
/* 25 */       return new Subscription(
/* 26 */           com.mojang.realmsclient.util.JsonUtils.getDateOr("startDate", jsonObject), 
/* 27 */           com.mojang.realmsclient.util.JsonUtils.getIntOr("daysLeft", jsonObject, 0), 
/* 28 */           typeFrom(com.mojang.realmsclient.util.JsonUtils.getStringOr("subscriptionType", jsonObject, null)));
/*    */     }
/* 30 */     catch (Exception e) {
/* 31 */       LOGGER.error("Could not parse Subscription", e);
/*    */       
/* 33 */       return new Subscription(java.time.Instant.EPOCH, 0, SubscriptionType.NORMAL);
/*    */     } 
/*    */   }
/*    */   private static SubscriptionType typeFrom(String subscriptionType) {
/*    */     try {
/* 38 */       if (subscriptionType != null) {
/* 39 */         return SubscriptionType.valueOf(subscriptionType);
/*    */       }
/* 41 */     } catch (Exception exception) {}
/*    */     
/* 43 */     return SubscriptionType.NORMAL;
/*    */   }
/*    */   
/*    */   public enum SubscriptionType {
/* 47 */     NORMAL, RECURRING;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/dto/Subscription.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */