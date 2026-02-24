/*    */ package net.minecraft.util.debug;
/*    */ 
/*    */ 
/*    */ public final class DebugHiveInfo extends Record {
/*    */   private final net.minecraft.world.level.block.Block type;
/*    */   private final int occupantCount;
/*    */   private final int honeyLevel;
/*    */   private final boolean sedated;
/*    */   
/* 10 */   public DebugHiveInfo(net.minecraft.world.level.block.Block type, int occupantCount, int honeyLevel, boolean sedated) { this.type = type; this.occupantCount = occupantCount; this.honeyLevel = honeyLevel; this.sedated = sedated; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/util/debug/DebugHiveInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugHiveInfo; } public net.minecraft.world.level.block.Block type() { return this.type; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/util/debug/DebugHiveInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/util/debug/DebugHiveInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/util/debug/DebugHiveInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/util/debug/DebugHiveInfo;
/* 10 */     //   0	8	1	o	Ljava/lang/Object; } public int occupantCount() { return this.occupantCount; } public int honeyLevel() { return this.honeyLevel; } public boolean sedated() { return this.sedated; }
/* 11 */    public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, DebugHiveInfo> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(
/* 12 */       net.minecraft.network.codec.ByteBufCodecs.registry(net.minecraft.core.registries.Registries.BLOCK), DebugHiveInfo::type, net.minecraft.network.codec.ByteBufCodecs.VAR_INT, DebugHiveInfo::occupantCount, net.minecraft.network.codec.ByteBufCodecs.VAR_INT, DebugHiveInfo::honeyLevel, net.minecraft.network.codec.ByteBufCodecs.BOOL, DebugHiveInfo::sedated, DebugHiveInfo::new);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static DebugHiveInfo pack(net.minecraft.world.level.block.entity.BeehiveBlockEntity beehive) {
/* 20 */     return new DebugHiveInfo(
/* 21 */         beehive.getBlockState().getBlock(), 
/* 22 */         beehive.getOccupantCount(), 
/* 23 */         net.minecraft.world.level.block.entity.BeehiveBlockEntity.getHoneyLevel(beehive.getBlockState()), 
/* 24 */         beehive.isSedated());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/debug/DebugHiveInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */