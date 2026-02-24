/*    */ package net.minecraft.server.level;
/*    */ public final class TicketType extends Record {
/*    */   private final long timeout;
/*    */   @Flags
/*    */   private final int flags;
/*    */   public static final long NO_TIMEOUT = 0L;
/*    */   public static final int FLAG_PERSIST = 1;
/*    */   public static final int FLAG_LOADING = 2;
/*    */   public static final int FLAG_SIMULATION = 4;
/*    */   public static final int FLAG_KEEP_DIMENSION_ACTIVE = 8;
/*    */   public static final int FLAG_CAN_EXPIRE_IF_UNLOADED = 16;
/*    */   
/* 13 */   public TicketType(long timeout, @Flags int flags) { this.timeout = timeout; this.flags = flags; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/level/TicketType;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/server/level/TicketType; } public long timeout() { return this.timeout; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/level/TicketType;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/level/TicketType; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/level/TicketType;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/level/TicketType;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } @Flags public int flags() { return this.flags; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 22 */   public static final TicketType PLAYER_SPAWN = register("player_spawn", 20L, 2);
/* 23 */   public static final TicketType SPAWN_SEARCH = register("spawn_search", 1L, 2);
/* 24 */   public static final TicketType DRAGON = register("dragon", 0L, 6);
/* 25 */   public static final TicketType PLAYER_LOADING = register("player_loading", 0L, 2);
/* 26 */   public static final TicketType PLAYER_SIMULATION = register("player_simulation", 0L, 12);
/* 27 */   public static final TicketType FORCED = register("forced", 0L, 15);
/* 28 */   public static final TicketType PORTAL = register("portal", 300L, 15);
/* 29 */   public static final TicketType ENDER_PEARL = register("ender_pearl", 40L, 14);
/* 30 */   public static final TicketType UNKNOWN = register("unknown", 1L, 18);
/*    */   
/*    */   private static TicketType register(String name, long timeout, @Flags int flags) {
/* 33 */     return (TicketType)net.minecraft.core.Registry.register(net.minecraft.core.registries.BuiltInRegistries.TICKET_TYPE, name, new TicketType(timeout, flags));
/*    */   }
/*    */   
/*    */   public boolean persist() {
/* 37 */     return ((this.flags & 0x1) != 0);
/*    */   }
/*    */   
/*    */   public boolean doesLoad() {
/* 41 */     return ((this.flags & 0x2) != 0);
/*    */   }
/*    */   
/*    */   public boolean doesSimulate() {
/* 45 */     return ((this.flags & 0x4) != 0);
/*    */   }
/*    */   
/*    */   public boolean shouldKeepDimensionActive() {
/* 49 */     return ((this.flags & 0x8) != 0);
/*    */   }
/*    */   
/*    */   public boolean canExpireIfUnloaded() {
/* 53 */     return ((this.flags & 0x10) != 0);
/*    */   }
/*    */   
/*    */   public boolean hasTimeout() {
/* 57 */     return (this.timeout != 0L);
/*    */   }
/*    */   
/*    */   @java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.CLASS)
/*    */   @java.lang.annotation.Target({java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.LOCAL_VARIABLE, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE_USE})
/*    */   public static @interface Flags {}
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/level/TicketType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */