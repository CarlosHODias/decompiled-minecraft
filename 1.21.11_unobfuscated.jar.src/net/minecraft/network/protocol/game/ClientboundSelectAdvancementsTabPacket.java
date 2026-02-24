/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.network.protocol.PacketType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class ClientboundSelectAdvancementsTabPacket implements Packet<ClientGamePacketListener> {
/* 11 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSelectAdvancementsTabPacket> STREAM_CODEC = Packet.codec(ClientboundSelectAdvancementsTabPacket::write, ClientboundSelectAdvancementsTabPacket::new);
/*    */   
/*    */   private final Identifier tab;
/*    */   
/*    */   public ClientboundSelectAdvancementsTabPacket(Identifier tab) {
/* 16 */     this.tab = tab;
/*    */   }
/*    */   
/*    */   private ClientboundSelectAdvancementsTabPacket(FriendlyByteBuf input) {
/* 20 */     this.tab = (Identifier)input.readNullable(FriendlyByteBuf::readIdentifier);
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 24 */     output.writeNullable(this.tab, FriendlyByteBuf::writeIdentifier);
/*    */   }
/*    */ 
/*    */   
/*    */   public PacketType<ClientboundSelectAdvancementsTabPacket> type() {
/* 29 */     return GamePacketTypes.CLIENTBOUND_SELECT_ADVANCEMENTS_TAB;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 34 */     listener.handleSelectAdvancementsTab(this);
/*    */   }
/*    */   
/*    */   public Identifier getTab() {
/* 38 */     return this.tab;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundSelectAdvancementsTabPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */