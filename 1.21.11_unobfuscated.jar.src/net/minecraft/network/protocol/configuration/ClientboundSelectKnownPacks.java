/*    */ package net.minecraft.network.protocol.configuration;
/*    */ 
/*    */ import java.util.List;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.server.packs.repository.KnownPack;
/*    */ 
/*    */ public final class ClientboundSelectKnownPacks extends Record implements net.minecraft.network.protocol.Packet<ClientConfigurationPacketListener> {
/*    */   private final List<KnownPack> knownPacks;
/*    */   
/* 12 */   public ClientboundSelectKnownPacks(List<KnownPack> knownPacks) { this.knownPacks = knownPacks; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/configuration/ClientboundSelectKnownPacks;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundSelectKnownPacks; } public List<KnownPack> knownPacks() { return this.knownPacks; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/configuration/ClientboundSelectKnownPacks;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundSelectKnownPacks; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/configuration/ClientboundSelectKnownPacks;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundSelectKnownPacks;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/* 15 */   } public static final StreamCodec<io.netty.buffer.ByteBuf, ClientboundSelectKnownPacks> STREAM_CODEC = StreamCodec.composite(
/* 16 */       KnownPack.STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs.list()), ClientboundSelectKnownPacks::knownPacks, ClientboundSelectKnownPacks::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundSelectKnownPacks> type() {
/* 22 */     return ConfigurationPacketTypes.CLIENTBOUND_SELECT_KNOWN_PACKS;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientConfigurationPacketListener listener) {
/* 27 */     listener.handleSelectKnownPacks(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/configuration/ClientboundSelectKnownPacks.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */