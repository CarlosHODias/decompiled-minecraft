/*    */ package net.minecraft.client.telemetry;
/*    */ 
/*    */ public final class TelemetryEventInstance extends Record {
/*    */   private final TelemetryEventType type;
/*    */   private final TelemetryPropertyMap properties;
/*    */   
/*  7 */   public TelemetryEventType type() { return this.type; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/telemetry/TelemetryEventInstance;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/telemetry/TelemetryEventInstance; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/telemetry/TelemetryEventInstance;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/telemetry/TelemetryEventInstance; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/telemetry/TelemetryEventInstance;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/telemetry/TelemetryEventInstance;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public TelemetryPropertyMap properties() { return this.properties; }
/*  8 */    public static final com.mojang.serialization.Codec<TelemetryEventInstance> CODEC = TelemetryEventType.CODEC.dispatchStable(TelemetryEventInstance::type, TelemetryEventType::codec);
/*    */   
/*    */   public TelemetryEventInstance(TelemetryEventType type, TelemetryPropertyMap properties) {
/* 11 */     properties.propertySet().forEach(property -> {
/*    */           if (!type.contains(property))
/*    */             throw new IllegalArgumentException("Property '" + property.id() + "' not expected for event: '" + type.id() + "'"); 
/*    */         });
/*    */     this.type = type;
/*    */     this.properties = properties;
/*    */   }
/*    */   public com.mojang.authlib.minecraft.TelemetryEvent export(com.mojang.authlib.minecraft.TelemetrySession session) {
/* 19 */     return this.type.export(session, this.properties);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/telemetry/TelemetryEventInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */