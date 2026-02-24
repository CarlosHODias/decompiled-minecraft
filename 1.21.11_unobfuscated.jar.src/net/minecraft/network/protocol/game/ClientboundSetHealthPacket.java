/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundSetHealthPacket implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetHealthPacket> STREAM_CODEC = Packet.codec(ClientboundSetHealthPacket::write, ClientboundSetHealthPacket::new);
/*    */   
/*    */   private final float health;
/*    */   private final int food;
/*    */   private final float saturation;
/*    */   
/*    */   public ClientboundSetHealthPacket(float health, int food, float saturation) {
/* 16 */     this.health = health;
/* 17 */     this.food = food;
/* 18 */     this.saturation = saturation;
/*    */   }
/*    */   
/*    */   private ClientboundSetHealthPacket(FriendlyByteBuf input) {
/* 22 */     this.health = input.readFloat();
/* 23 */     this.food = input.readVarInt();
/* 24 */     this.saturation = input.readFloat();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 28 */     output.writeFloat(this.health);
/* 29 */     output.writeVarInt(this.food);
/* 30 */     output.writeFloat(this.saturation);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundSetHealthPacket> type() {
/* 35 */     return GamePacketTypes.CLIENTBOUND_SET_HEALTH;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 40 */     listener.handleSetHealth(this);
/*    */   }
/*    */   
/*    */   public float getHealth() {
/* 44 */     return this.health;
/*    */   }
/*    */   
/*    */   public int getFood() {
/* 48 */     return this.food;
/*    */   }
/*    */   
/*    */   public float getSaturation() {
/* 52 */     return this.saturation;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundSetHealthPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */