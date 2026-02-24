/*    */ package net.minecraft.network;
/*    */ 
/*    */ 
/*    */ public final class DisconnectionDetails extends Record {
/*    */   private final net.minecraft.network.chat.Component reason;
/*    */   private final java.util.Optional<java.nio.file.Path> report;
/*    */   private final java.util.Optional<java.net.URI> bugReportLink;
/*    */   
/*  9 */   public java.util.Optional<java.net.URI> bugReportLink() { return this.bugReportLink; } public java.util.Optional<java.nio.file.Path> report() { return this.report; } public net.minecraft.network.chat.Component reason() { return this.reason; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/DisconnectionDetails;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/DisconnectionDetails;
/*  9 */     //   0	8	1	o	Ljava/lang/Object; } public DisconnectionDetails(net.minecraft.network.chat.Component reason, java.util.Optional<java.nio.file.Path> report, java.util.Optional<java.net.URI> bugReportLink) { this.reason = reason; this.report = report; this.bugReportLink = bugReportLink; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/DisconnectionDetails;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/DisconnectionDetails; }
/*    */   public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/DisconnectionDetails;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #9	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/network/DisconnectionDetails; } public DisconnectionDetails(net.minecraft.network.chat.Component reason) { this(reason, java.util.Optional.empty(), java.util.Optional.empty()); }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/DisconnectionDetails.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */