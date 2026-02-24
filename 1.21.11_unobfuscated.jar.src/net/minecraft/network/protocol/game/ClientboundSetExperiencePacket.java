/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ 
/*    */ public class ClientboundSetExperiencePacket implements Packet<ClientGamePacketListener> {
/*  9 */   public static final StreamCodec<FriendlyByteBuf, ClientboundSetExperiencePacket> STREAM_CODEC = Packet.codec(ClientboundSetExperiencePacket::write, ClientboundSetExperiencePacket::new);
/*    */   
/*    */   private final float experienceProgress;
/*    */   private final int totalExperience;
/*    */   private final int experienceLevel;
/*    */   
/*    */   public ClientboundSetExperiencePacket(float experienceProgress, int totalExperience, int experienceLevel) {
/* 16 */     this.experienceProgress = experienceProgress;
/* 17 */     this.totalExperience = totalExperience;
/* 18 */     this.experienceLevel = experienceLevel;
/*    */   }
/*    */   
/*    */   private ClientboundSetExperiencePacket(FriendlyByteBuf input) {
/* 22 */     this.experienceProgress = input.readFloat();
/* 23 */     this.experienceLevel = input.readVarInt();
/* 24 */     this.totalExperience = input.readVarInt();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 28 */     output.writeFloat(this.experienceProgress);
/* 29 */     output.writeVarInt(this.experienceLevel);
/* 30 */     output.writeVarInt(this.totalExperience);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundSetExperiencePacket> type() {
/* 35 */     return GamePacketTypes.CLIENTBOUND_SET_EXPERIENCE;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 40 */     listener.handleSetExperience(this);
/*    */   }
/*    */   
/*    */   public float getExperienceProgress() {
/* 44 */     return this.experienceProgress;
/*    */   }
/*    */   
/*    */   public int getTotalExperience() {
/* 48 */     return this.totalExperience;
/*    */   }
/*    */   
/*    */   public int getExperienceLevel() {
/* 52 */     return this.experienceLevel;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundSetExperiencePacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */