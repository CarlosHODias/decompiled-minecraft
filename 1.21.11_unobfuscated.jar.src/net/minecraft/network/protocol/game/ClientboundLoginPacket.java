/*    */ package net.minecraft.network.protocol.game;
/*    */ 
/*    */ 
/*    */ public final class ClientboundLoginPacket extends Record implements net.minecraft.network.protocol.Packet<ClientGamePacketListener> {
/*    */   private final int playerId;
/*    */   private final boolean hardcore;
/*    */   private final java.util.Set<net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level>> levels;
/*    */   private final int maxPlayers;
/*    */   private final int chunkRadius;
/*    */   private final int simulationDistance;
/*    */   private final boolean reducedDebugInfo;
/*    */   private final boolean showDeathScreen;
/*    */   private final boolean doLimitedCrafting;
/*    */   private final CommonPlayerSpawnInfo commonPlayerSpawnInfo;
/*    */   private final boolean enforcesSecureChat;
/*    */   
/* 17 */   public ClientboundLoginPacket(int playerId, boolean hardcore, java.util.Set<net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level>> levels, int maxPlayers, int chunkRadius, int simulationDistance, boolean reducedDebugInfo, boolean showDeathScreen, boolean doLimitedCrafting, CommonPlayerSpawnInfo commonPlayerSpawnInfo, boolean enforcesSecureChat) { this.playerId = playerId; this.hardcore = hardcore; this.levels = levels; this.maxPlayers = maxPlayers; this.chunkRadius = chunkRadius; this.simulationDistance = simulationDistance; this.reducedDebugInfo = reducedDebugInfo; this.showDeathScreen = showDeathScreen; this.doLimitedCrafting = doLimitedCrafting; this.commonPlayerSpawnInfo = commonPlayerSpawnInfo; this.enforcesSecureChat = enforcesSecureChat; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 17 */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundLoginPacket; } public int playerId() { return this.playerId; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/game/ClientboundLoginPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/game/ClientboundLoginPacket;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public boolean hardcore() { return this.hardcore; } public java.util.Set<net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level>> levels() { return this.levels; } public int maxPlayers() { return this.maxPlayers; } public int chunkRadius() { return this.chunkRadius; } public int simulationDistance() { return this.simulationDistance; } public boolean reducedDebugInfo() { return this.reducedDebugInfo; } public boolean showDeathScreen() { return this.showDeathScreen; } public boolean doLimitedCrafting() { return this.doLimitedCrafting; } public CommonPlayerSpawnInfo commonPlayerSpawnInfo() { return this.commonPlayerSpawnInfo; } public boolean enforcesSecureChat() { return this.enforcesSecureChat; }
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
/*    */   
/* 30 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, ClientboundLoginPacket> STREAM_CODEC = net.minecraft.network.protocol.Packet.codec(ClientboundLoginPacket::write, ClientboundLoginPacket::new);
/*    */   
/*    */   private ClientboundLoginPacket(net.minecraft.network.RegistryFriendlyByteBuf input) {
/* 33 */     this(
/* 34 */         input.readInt(), 
/* 35 */         input.readBoolean(), (java.util.Set<net.minecraft.resources.ResourceKey<net.minecraft.world.level.Level>>)
/* 36 */         input.readCollection(com.google.common.collect.Sets::newHashSetWithExpectedSize, buf -> buf.readResourceKey(net.minecraft.core.registries.Registries.DIMENSION)), 
/* 37 */         input.readVarInt(), 
/* 38 */         input.readVarInt(), 
/* 39 */         input.readVarInt(), 
/* 40 */         input.readBoolean(), 
/* 41 */         input.readBoolean(), 
/* 42 */         input.readBoolean(), new CommonPlayerSpawnInfo(input), 
/*    */         
/* 44 */         input.readBoolean());
/*    */   }
/*    */ 
/*    */   
/*    */   private void write(net.minecraft.network.RegistryFriendlyByteBuf output) {
/* 49 */     output.writeInt(this.playerId);
/* 50 */     output.writeBoolean(this.hardcore);
/* 51 */     output.writeCollection(this.levels, net.minecraft.network.FriendlyByteBuf::writeResourceKey);
/* 52 */     output.writeVarInt(this.maxPlayers);
/* 53 */     output.writeVarInt(this.chunkRadius);
/* 54 */     output.writeVarInt(this.simulationDistance);
/* 55 */     output.writeBoolean(this.reducedDebugInfo);
/* 56 */     output.writeBoolean(this.showDeathScreen);
/* 57 */     output.writeBoolean(this.doLimitedCrafting);
/* 58 */     this.commonPlayerSpawnInfo.write(output);
/* 59 */     output.writeBoolean(this.enforcesSecureChat);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundLoginPacket> type() {
/* 64 */     return GamePacketTypes.CLIENTBOUND_LOGIN;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 69 */     listener.handleLogin(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundLoginPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */