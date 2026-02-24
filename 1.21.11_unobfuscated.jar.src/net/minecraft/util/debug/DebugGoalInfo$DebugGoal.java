/*    */ package net.minecraft.util.debug;
/*    */ public final class DebugGoal extends Record {
/*    */   private final int priority;
/*    */   private final boolean isRunning;
/*    */   private final String name;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugGoalInfo$DebugGoal;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugGoalInfo$DebugGoal;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugGoalInfo$DebugGoal;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugGoalInfo$DebugGoal;
/*    */   }
/*    */   
/* 15 */   public DebugGoal(int priority, boolean isRunning, String name) { this.priority = priority; this.isRunning = isRunning; this.name = name; } public int priority() { return this.priority; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugGoalInfo$DebugGoal;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugGoalInfo$DebugGoal;
/* 15 */     //   0	8	1	o	Ljava/lang/Object; } public boolean isRunning() { return this.isRunning; } public String name() { return this.name; }
/* 16 */    public static final net.minecraft.network.codec.StreamCodec<io.netty.buffer.ByteBuf, DebugGoal> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(net.minecraft.network.codec.ByteBufCodecs.VAR_INT, DebugGoal::priority, net.minecraft.network.codec.ByteBufCodecs.BOOL, DebugGoal::isRunning, 
/*    */ 
/*    */       
/* 19 */       net.minecraft.network.codec.ByteBufCodecs.stringUtf8(255), DebugGoal::name, DebugGoal::new);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/debug/DebugGoalInfo$DebugGoal.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */