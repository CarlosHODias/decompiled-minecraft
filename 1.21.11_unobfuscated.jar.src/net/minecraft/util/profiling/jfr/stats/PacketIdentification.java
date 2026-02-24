/*    */ package net.minecraft.util.profiling.jfr.stats;
/*    */ public final class PacketIdentification extends Record { private final String direction;
/*    */   private final String protocolId;
/*    */   private final String packetId;
/*    */   
/*  6 */   public PacketIdentification(String direction, String protocolId, String packetId) { this.direction = direction; this.protocolId = protocolId; this.packetId = packetId; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/profiling/jfr/stats/PacketIdentification;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  6 */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/PacketIdentification; } public String direction() { return this.direction; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/profiling/jfr/stats/PacketIdentification;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/profiling/jfr/stats/PacketIdentification; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/profiling/jfr/stats/PacketIdentification;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #6	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/profiling/jfr/stats/PacketIdentification;
/*  6 */     //   0	8	1	o	Ljava/lang/Object; } public String protocolId() { return this.protocolId; } public String packetId() { return this.packetId; }
/*    */    public static PacketIdentification from(jdk.jfr.consumer.RecordedEvent event) {
/*  8 */     return new PacketIdentification(
/*  9 */         event.getString("packetDirection"), 
/* 10 */         event.getString("protocolId"), 
/* 11 */         event.getString("packetId"));
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/profiling/jfr/stats/PacketIdentification.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */