/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.level.border.WorldBorder;
/*    */ 
/*    */ public class ClientboundInitializeBorderPacket implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<FriendlyByteBuf, ClientboundInitializeBorderPacket> STREAM_CODEC = Packet.codec(ClientboundInitializeBorderPacket::write, ClientboundInitializeBorderPacket::new);
/*    */   
/*    */   private final double newCenterX;
/*    */   private final double newCenterZ;
/*    */   private final double oldSize;
/*    */   private final double newSize;
/*    */   private final long lerpTime;
/*    */   private final int newAbsoluteMaxSize;
/*    */   private final int warningBlocks;
/*    */   private final int warningTime;
/*    */   
/*    */   private ClientboundInitializeBorderPacket(FriendlyByteBuf input) {
/* 22 */     this.newCenterX = input.readDouble();
/* 23 */     this.newCenterZ = input.readDouble();
/* 24 */     this.oldSize = input.readDouble();
/* 25 */     this.newSize = input.readDouble();
/* 26 */     this.lerpTime = input.readVarLong();
/* 27 */     this.newAbsoluteMaxSize = input.readVarInt();
/* 28 */     this.warningBlocks = input.readVarInt();
/* 29 */     this.warningTime = input.readVarInt();
/*    */   }
/*    */   
/*    */   public ClientboundInitializeBorderPacket(WorldBorder border) {
/* 33 */     this.newCenterX = border.getCenterX();
/* 34 */     this.newCenterZ = border.getCenterZ();
/* 35 */     this.oldSize = border.getSize();
/* 36 */     this.newSize = border.getLerpTarget();
/* 37 */     this.lerpTime = border.getLerpTime();
/* 38 */     this.newAbsoluteMaxSize = border.getAbsoluteMaxSize();
/* 39 */     this.warningBlocks = border.getWarningBlocks();
/* 40 */     this.warningTime = border.getWarningTime();
/*    */   }
/*    */   
/*    */   private void write(FriendlyByteBuf output) {
/* 44 */     output.writeDouble(this.newCenterX);
/* 45 */     output.writeDouble(this.newCenterZ);
/* 46 */     output.writeDouble(this.oldSize);
/* 47 */     output.writeDouble(this.newSize);
/* 48 */     output.writeVarLong(this.lerpTime);
/* 49 */     output.writeVarInt(this.newAbsoluteMaxSize);
/* 50 */     output.writeVarInt(this.warningBlocks);
/* 51 */     output.writeVarInt(this.warningTime);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundInitializeBorderPacket> type() {
/* 56 */     return GamePacketTypes.CLIENTBOUND_INITIALIZE_BORDER;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 61 */     listener.handleInitializeBorder(this);
/*    */   }
/*    */   
/*    */   public double getNewCenterX() {
/* 65 */     return this.newCenterX;
/*    */   }
/*    */   
/*    */   public double getNewCenterZ() {
/* 69 */     return this.newCenterZ;
/*    */   }
/*    */   
/*    */   public double getNewSize() {
/* 73 */     return this.newSize;
/*    */   }
/*    */   
/*    */   public double getOldSize() {
/* 77 */     return this.oldSize;
/*    */   }
/*    */   
/*    */   public long getLerpTime() {
/* 81 */     return this.lerpTime;
/*    */   }
/*    */   
/*    */   public int getNewAbsoluteMaxSize() {
/* 85 */     return this.newAbsoluteMaxSize;
/*    */   }
/*    */   
/*    */   public int getWarningTime() {
/* 89 */     return this.warningTime;
/*    */   }
/*    */   
/*    */   public int getWarningBlocks() {
/* 93 */     return this.warningBlocks;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundInitializeBorderPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */