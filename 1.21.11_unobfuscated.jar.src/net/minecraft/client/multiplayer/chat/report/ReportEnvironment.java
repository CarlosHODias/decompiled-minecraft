/*    */ package net.minecraft.client.multiplayer.chat.report;
/*    */ public final class ReportEnvironment extends Record { private final String clientVersion; private final Server server;
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment;
/*    */   }
/*    */   
/* 11 */   public ReportEnvironment(String clientVersion, Server server) { this.clientVersion = clientVersion; this.server = server; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public String clientVersion() { return this.clientVersion; } public Server server() { return this.server; }
/*    */    public static ReportEnvironment local() {
/* 13 */     return create(null);
/*    */   }
/*    */   
/*    */   public static ReportEnvironment thirdParty(String ip) {
/* 17 */     return create(new Server.ThirdParty(ip));
/*    */   }
/*    */   
/*    */   public static ReportEnvironment realm(com.mojang.realmsclient.dto.RealmsServer realm) {
/* 21 */     return create(new Server.Realm(realm));
/*    */   }
/*    */   
/*    */   public static ReportEnvironment create(Server server) {
/* 25 */     return new ReportEnvironment(getClientVersion(), server);
/*    */   }
/*    */   
/*    */   public com.mojang.authlib.yggdrasil.request.AbuseReportRequest.ClientInfo clientInfo() {
/* 29 */     return new com.mojang.authlib.yggdrasil.request.AbuseReportRequest.ClientInfo(this.clientVersion, java.util.Locale.getDefault().toLanguageTag());
/*    */   }
/*    */   
/*    */   public com.mojang.authlib.yggdrasil.request.AbuseReportRequest.ThirdPartyServerInfo thirdPartyServerInfo() {
/* 33 */     Server server = this.server; if (server instanceof Server.ThirdParty) { Server.ThirdParty thirdParty = (Server.ThirdParty)server;
/* 34 */       return new com.mojang.authlib.yggdrasil.request.AbuseReportRequest.ThirdPartyServerInfo(thirdParty.ip); }
/*    */     
/* 36 */     return null;
/*    */   }
/*    */   
/*    */   public com.mojang.authlib.yggdrasil.request.AbuseReportRequest.RealmInfo realmInfo() {
/* 40 */     Server server = this.server; if (server instanceof Server.Realm) { Server.Realm realm = (Server.Realm)server;
/* 41 */       return new com.mojang.authlib.yggdrasil.request.AbuseReportRequest.RealmInfo(String.valueOf(realm.realmId()), realm.slotId()); }
/*    */     
/* 43 */     return null;
/*    */   }
/*    */   
/*    */   private static String getClientVersion() {
/* 47 */     StringBuilder version = new StringBuilder();
/*    */     
/* 49 */     version.append(net.minecraft.SharedConstants.getCurrentVersion().id());
/* 50 */     if (net.minecraft.client.Minecraft.checkModStatus().shouldReportAsModified()) {
/* 51 */       version.append(" (modded)");
/*    */     }
/*    */     
/* 54 */     return version.toString();
/*    */   }
/*    */   public static final class ThirdParty extends Record implements Server { private final String ip;
/*    */     
/* 58 */     public ThirdParty(String ip) { this.ip = ip; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$ThirdParty;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #58	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$ThirdParty; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$ThirdParty;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #58	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$ThirdParty; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$ThirdParty;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #58	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$ThirdParty;
/* 58 */       //   0	8	1	o	Ljava/lang/Object; } public String ip() { return this.ip; } } public static interface Server { public static final class ThirdParty extends Record implements Server { private final String ip; public ThirdParty(String ip) { this.ip = ip; } public final String toString() { // Byte code:
/*    */         //   0: aload_0
/*    */         //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$ThirdParty;)Ljava/lang/String;
/*    */         //   6: areturn
/*    */         // Line number table:
/*    */         //   Java source line number -> byte code offset
/*    */         //   #58	-> 0
/*    */         // Local variable table:
/*    */         //   start	length	slot	name	descriptor
/*    */         //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$ThirdParty; } public final int hashCode() { // Byte code:
/*    */         //   0: aload_0
/*    */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$ThirdParty;)I
/*    */         //   6: ireturn
/*    */         // Line number table:
/*    */         //   Java source line number -> byte code offset
/*    */         //   #58	-> 0
/*    */         // Local variable table:
/*    */         //   start	length	slot	name	descriptor
/*    */         //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$ThirdParty; } public final boolean equals(Object o) { // Byte code:
/*    */         //   0: aload_0
/*    */         //   1: aload_1
/*    */         //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$ThirdParty;Ljava/lang/Object;)Z
/*    */         //   7: ireturn
/*    */         // Line number table:
/*    */         //   Java source line number -> byte code offset
/*    */         //   #58	-> 0
/*    */         // Local variable table:
/*    */         //   start	length	slot	name	descriptor
/*    */         //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$ThirdParty;
/* 58 */         //   0	8	1	o	Ljava/lang/Object; } public String ip() { return this.ip; }
/*    */        }
/*    */     public static final class Realm extends Record implements Server { private final long realmId; private final int slotId;
/* 61 */       public int slotId() { return this.slotId; } public long realmId() { return this.realmId; } public final boolean equals(Object o) { // Byte code:
/*    */         //   0: aload_0
/*    */         //   1: aload_1
/*    */         //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$Realm;Ljava/lang/Object;)Z
/*    */         //   7: ireturn
/*    */         // Line number table:
/*    */         //   Java source line number -> byte code offset
/*    */         //   #61	-> 0
/*    */         // Local variable table:
/*    */         //   start	length	slot	name	descriptor
/*    */         //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$Realm;
/*    */         //   0	8	1	o	Ljava/lang/Object; } public final int hashCode() { // Byte code:
/*    */         //   0: aload_0
/*    */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$Realm;)I
/*    */         //   6: ireturn
/*    */         // Line number table:
/*    */         //   Java source line number -> byte code offset
/*    */         //   #61	-> 0
/*    */         // Local variable table:
/*    */         //   start	length	slot	name	descriptor
/*    */         //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$Realm; } public final String toString() { // Byte code:
/*    */         //   0: aload_0
/*    */         //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$Realm;)Ljava/lang/String;
/*    */         //   6: areturn
/*    */         // Line number table:
/*    */         //   Java source line number -> byte code offset
/*    */         //   #61	-> 0
/*    */         // Local variable table:
/*    */         //   start	length	slot	name	descriptor
/* 61 */         //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$Realm; } public Realm(long realmId, int slotId) { this.realmId = realmId; this.slotId = slotId; }
/*    */       
/* 63 */       public Realm(com.mojang.realmsclient.dto.RealmsServer realm) { this(realm.id, realm.activeSlot); } } } public static final class Realm extends Record implements Server { private final long realmId; private final int slotId; public Realm(com.mojang.realmsclient.dto.RealmsServer realm) { this(realm.id, realm.activeSlot); }
/*    */ 
/*    */     
/*    */     public int slotId() {
/*    */       return this.slotId;
/*    */     }
/*    */     
/*    */     public long realmId() {
/*    */       return this.realmId;
/*    */     }
/*    */     
/*    */     public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$Realm;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$Realm;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */     }
/*    */     
/*    */     public final int hashCode() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$Realm;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$Realm;
/*    */     }
/*    */     
/*    */     public final String toString() {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$Realm;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #61	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/multiplayer/chat/report/ReportEnvironment$Server$Realm;
/*    */     }
/*    */     
/*    */     public Realm(long realmId, int slotId) {
/*    */       this.realmId = realmId;
/*    */       this.slotId = slotId;
/*    */     } }
/*    */    }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/multiplayer/chat/report/ReportEnvironment.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */