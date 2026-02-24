/*    */ package net.minecraft.network.protocol.configuration;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.RegistrySynchronization;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ 
/*    */ public final class ClientboundRegistryDataPacket extends Record implements Packet<ClientConfigurationPacketListener> {
/*    */   private final ResourceKey<? extends Registry<?>> registry;
/*    */   private final List<RegistrySynchronization.PackedRegistryEntry> entries;
/*    */   
/* 16 */   public ClientboundRegistryDataPacket(ResourceKey<? extends Registry<?>> registry, List<RegistrySynchronization.PackedRegistryEntry> entries) { this.registry = registry; this.entries = entries; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/network/protocol/configuration/ClientboundRegistryDataPacket;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 16 */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundRegistryDataPacket; } public ResourceKey<? extends Registry<?>> registry() { return this.registry; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/protocol/configuration/ClientboundRegistryDataPacket;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundRegistryDataPacket; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/network/protocol/configuration/ClientboundRegistryDataPacket;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #16	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/network/protocol/configuration/ClientboundRegistryDataPacket;
/* 16 */     //   0	8	1	o	Ljava/lang/Object; } public List<RegistrySynchronization.PackedRegistryEntry> entries() { return this.entries; }
/*    */ 
/*    */ 
/*    */   
/* 20 */   private static final StreamCodec<ByteBuf, ResourceKey<? extends Registry<?>>> REGISTRY_KEY_STREAM_CODEC = net.minecraft.resources.Identifier.STREAM_CODEC.map(ResourceKey::createRegistryKey, ResourceKey::identifier);
/*    */   
/* 22 */   public static final StreamCodec<net.minecraft.network.FriendlyByteBuf, ClientboundRegistryDataPacket> STREAM_CODEC = StreamCodec.composite(REGISTRY_KEY_STREAM_CODEC, ClientboundRegistryDataPacket::registry, 
/*    */       
/* 24 */       RegistrySynchronization.PackedRegistryEntry.STREAM_CODEC.apply(net.minecraft.network.codec.ByteBufCodecs.list()), ClientboundRegistryDataPacket::entries, ClientboundRegistryDataPacket::new);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundRegistryDataPacket> type() {
/* 30 */     return ConfigurationPacketTypes.CLIENTBOUND_REGISTRY_DATA;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientConfigurationPacketListener listener) {
/* 35 */     listener.handleRegistryData(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/configuration/ClientboundRegistryDataPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */