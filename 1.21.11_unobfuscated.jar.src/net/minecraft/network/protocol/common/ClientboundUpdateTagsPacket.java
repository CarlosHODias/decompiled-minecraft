/*    */ package net.minecraft.network.protocol.common;
/*    */ import java.util.Map;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.tags.TagNetworkSerialization;
/*    */ 
/*    */ public class ClientboundUpdateTagsPacket implements Packet<ClientCommonPacketListener> {
/* 14 */   public static final StreamCodec<FriendlyByteBuf, ClientboundUpdateTagsPacket> STREAM_CODEC = Packet.codec(ClientboundUpdateTagsPacket::write, ClientboundUpdateTagsPacket::new);
/*    */   
/*    */   private final Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> tags;
/*    */   
/*    */   public ClientboundUpdateTagsPacket(Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> tags) {
/* 19 */     this.tags = tags;
/*    */   }
/*    */   
/*    */   private ClientboundUpdateTagsPacket(FriendlyByteBuf input) {
/* 23 */     this.tags = input.readMap(FriendlyByteBuf::readRegistryKey, TagNetworkSerialization.NetworkPayload::read);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 30 */     output.writeMap(this.tags, FriendlyByteBuf::writeResourceKey, (buffer, value) -> value.write(buffer));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public PacketType<ClientboundUpdateTagsPacket> type() {
/* 38 */     return CommonPacketTypes.CLIENTBOUND_UPDATE_TAGS;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientCommonPacketListener listener) {
/* 43 */     listener.handleUpdateTags(this);
/*    */   }
/*    */   
/*    */   public Map<ResourceKey<? extends Registry<?>>, TagNetworkSerialization.NetworkPayload> getTags() {
/* 47 */     return this.tags;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/common/ClientboundUpdateTagsPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */