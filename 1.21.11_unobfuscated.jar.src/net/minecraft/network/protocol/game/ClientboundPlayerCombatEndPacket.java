/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.damagesource.CombatTracker;
/*    */ 
/*    */ public class ClientboundPlayerCombatEndPacket implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundPlayerCombatEndPacket> STREAM_CODEC = Packet.codec(ClientboundPlayerCombatEndPacket::write, ClientboundPlayerCombatEndPacket::new);
/*    */   
/*    */   private final int duration;
/*    */   
/*    */   public ClientboundPlayerCombatEndPacket(CombatTracker tracker) {
/* 15 */     this(tracker.getCombatDuration());
/*    */   }
/*    */   
/*    */   public ClientboundPlayerCombatEndPacket(int duration) {
/* 19 */     this.duration = duration;
/*    */   }
/*    */   
/*    */   private ClientboundPlayerCombatEndPacket(FriendlyByteBuf input) {
/* 23 */     this.duration = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 27 */     output.writeVarInt(this.duration);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundPlayerCombatEndPacket> type() {
/* 32 */     return GamePacketTypes.CLIENTBOUND_PLAYER_COMBAT_END;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 37 */     listener.handlePlayerCombatEnd(this);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundPlayerCombatEndPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */