/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ 
/*    */ public final class CommonPlayerSpawnInfo extends Record {
/*    */   private final net.minecraft.core.Holder<net.minecraft.world.level.dimension.DimensionType> dimensionType;
/*    */   private final net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension;
/*    */   private final long seed;
/*    */   private final net.minecraft.world.level.GameType gameType;
/*    */   private final net.minecraft.world.level.GameType previousGameType;
/*    */   private final boolean isDebug;
/*    */   private final boolean isFlat;
/*    */   private final java.util.Optional<net.minecraft.core.GlobalPos> lastDeathLocation;
/*    */   private final int portalCooldown;
/*    */   private final int seaLevel;
/*    */   
/* 16 */   public CommonPlayerSpawnInfo(net.minecraft.core.Holder<net.minecraft.world.level.dimension.DimensionType> dimensionType, net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension, long seed, net.minecraft.world.level.GameType gameType, net.minecraft.world.level.GameType previousGameType, boolean isDebug, boolean isFlat, java.util.Optional<net.minecraft.core.GlobalPos> lastDeathLocation, int portalCooldown, int seaLevel) { this.dimensionType = dimensionType; this.dimension = dimension; this.seed = seed; this.gameType = gameType; this.previousGameType = previousGameType; this.isDebug = isDebug; this.isFlat = isFlat; this.lastDeathLocation = lastDeathLocation; this.portalCooldown = portalCooldown; this.seaLevel = seaLevel; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/CommonPlayerSpawnInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/CommonPlayerSpawnInfo; } public net.minecraft.core.Holder<net.minecraft.world.level.dimension.DimensionType> dimensionType() { return this.dimensionType; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/CommonPlayerSpawnInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/CommonPlayerSpawnInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/CommonPlayerSpawnInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/CommonPlayerSpawnInfo;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level> dimension() { return this.dimension; } public long seed() { return this.seed; } public net.minecraft.world.level.GameType gameType() { return this.gameType; } public net.minecraft.world.level.GameType previousGameType() { return this.previousGameType; } public boolean isDebug() { return this.isDebug; } public boolean isFlat() { return this.isFlat; } public java.util.Optional<net.minecraft.core.GlobalPos> lastDeathLocation() { return this.lastDeathLocation; } public int portalCooldown() { return this.portalCooldown; } public int seaLevel() { return this.seaLevel; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public CommonPlayerSpawnInfo(net.minecraft.network.RegistryFriendlyByteBuf input) {
/* 29 */     this((net.minecraft.core.Holder<net.minecraft.world.level.dimension.DimensionType>)
/* 30 */         net.minecraft.world.level.dimension.DimensionType.STREAM_CODEC.decode(input), 
/* 31 */         input.readResourceKey(net.minecraft.core.registries.Registries.DIMENSION), 
/* 32 */         input.readLong(), 
/* 33 */         net.minecraft.world.level.GameType.byId(input.readByte()), 
/* 34 */         net.minecraft.world.level.GameType.byNullableId(input.readByte()), 
/* 35 */         input.readBoolean(), 
/* 36 */         input.readBoolean(), 
/* 37 */         input.readOptional(net.minecraft.network.FriendlyByteBuf::readGlobalPos), 
/* 38 */         input.readVarInt(), 
/* 39 */         input.readVarInt());
/*    */   }
/*    */ 
/*    */   
/*    */   public void write(net.minecraft.network.RegistryFriendlyByteBuf output) {
/* 44 */     net.minecraft.world.level.dimension.DimensionType.STREAM_CODEC.encode(output, this.dimensionType);
/* 45 */     output.writeResourceKey(this.dimension);
/* 46 */     output.writeLong(this.seed);
/* 47 */     output.writeByte(this.gameType.getId());
/* 48 */     output.writeByte(net.minecraft.world.level.GameType.getNullableId(this.previousGameType));
/* 49 */     output.writeBoolean(this.isDebug);
/* 50 */     output.writeBoolean(this.isFlat);
/* 51 */     output.writeOptional(this.lastDeathLocation, net.minecraft.network.FriendlyByteBuf::writeGlobalPos);
/* 52 */     output.writeVarInt(this.portalCooldown);
/* 53 */     output.writeVarInt(this.seaLevel);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/CommonPlayerSpawnInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */