/*    */ package net.minecraft.util.debug;
/*    */ 
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public final class DebugBeeInfo extends Record {
/*    */   private final java.util.Optional<BlockPos> hivePos;
/*    */   private final java.util.Optional<BlockPos> flowerPos;
/*    */   private final int travelTicks;
/*    */   private final java.util.List<BlockPos> blacklistedHives;
/*    */   
/* 11 */   public DebugBeeInfo(java.util.Optional<BlockPos> hivePos, java.util.Optional<BlockPos> flowerPos, int travelTicks, java.util.List<BlockPos> blacklistedHives) { this.hivePos = hivePos; this.flowerPos = flowerPos; this.travelTicks = travelTicks; this.blacklistedHives = blacklistedHives; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugBeeInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugBeeInfo; } public java.util.Optional<BlockPos> hivePos() { return this.hivePos; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugBeeInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugBeeInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugBeeInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugBeeInfo;
/* 11 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.Optional<BlockPos> flowerPos() { return this.flowerPos; } public int travelTicks() { return this.travelTicks; } public java.util.List<BlockPos> blacklistedHives() { return this.blacklistedHives; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 17 */   public static final net.minecraft.network.codec.StreamCodec<io.netty.buffer.ByteBuf, DebugBeeInfo> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(
/* 18 */       BlockPos.STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs::optional), DebugBeeInfo::hivePos, 
/* 19 */       BlockPos.STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs::optional), DebugBeeInfo::flowerPos, net.minecraft.network.codec.ByteBufCodecs.VAR_INT, DebugBeeInfo::travelTicks, 
/*    */       
/* 21 */       BlockPos.STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs.list()), DebugBeeInfo::blacklistedHives, DebugBeeInfo::new);
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean hasHive(BlockPos hivePos) {
/* 26 */     return (this.hivePos.isPresent() && hivePos.equals(this.hivePos.get()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/debug/DebugBeeInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */