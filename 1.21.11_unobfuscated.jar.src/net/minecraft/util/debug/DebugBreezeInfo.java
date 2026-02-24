/*    */ package net.minecraft.util.debug;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public final class DebugBreezeInfo extends Record {
/*    */   private final Optional<Integer> attackTarget;
/*    */   private final Optional<BlockPos> jumpTarget;
/*    */   
/* 10 */   public DebugBreezeInfo(Optional<Integer> attackTarget, Optional<BlockPos> jumpTarget) { this.attackTarget = attackTarget; this.jumpTarget = jumpTarget; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugBreezeInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugBreezeInfo; } public Optional<Integer> attackTarget() { return this.attackTarget; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugBreezeInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugBreezeInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugBreezeInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugBreezeInfo;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<BlockPos> jumpTarget() { return this.jumpTarget; }
/*    */ 
/*    */ 
/*    */   
/* 14 */   public static final net.minecraft.network.codec.StreamCodec<io.netty.buffer.ByteBuf, DebugBreezeInfo> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(
/* 15 */       net.minecraft.network.codec.ByteBufCodecs.VAR_INT.apply(net.minecraft.network.codec.ByteBufCodecs::optional), DebugBreezeInfo::attackTarget, 
/* 16 */       BlockPos.STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs::optional), DebugBreezeInfo::jumpTarget, DebugBreezeInfo::new);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/debug/DebugBreezeInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */