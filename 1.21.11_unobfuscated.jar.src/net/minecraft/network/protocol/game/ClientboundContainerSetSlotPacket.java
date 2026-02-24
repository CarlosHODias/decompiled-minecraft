/*    */ package net.minecraft.network.protocol.game;
/*    */ import net.minecraft.network.PacketListener;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.network.codec.StreamDecoder;
/*    */ import net.minecraft.network.protocol.Packet;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ClientboundContainerSetSlotPacket implements Packet<ClientGamePacketListener> {
/* 10 */   public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundContainerSetSlotPacket> STREAM_CODEC = Packet.codec(ClientboundContainerSetSlotPacket::write, ClientboundContainerSetSlotPacket::new);
/*    */   
/*    */   private final int containerId;
/*    */   private final int stateId;
/*    */   private final int slot;
/*    */   private final ItemStack itemStack;
/*    */   
/*    */   public ClientboundContainerSetSlotPacket(int containerId, int stateId, int slot, ItemStack itemStack) {
/* 18 */     this.containerId = containerId;
/* 19 */     this.stateId = stateId;
/* 20 */     this.slot = slot;
/* 21 */     this.itemStack = itemStack.copy();
/*    */   }
/*    */   
/*    */   private ClientboundContainerSetSlotPacket(RegistryFriendlyByteBuf input) {
/* 25 */     this.containerId = input.readContainerId();
/* 26 */     this.stateId = input.readVarInt();
/* 27 */     this.slot = input.readShort();
/* 28 */     this.itemStack = (ItemStack)ItemStack.OPTIONAL_STREAM_CODEC.decode(input);
/*    */   }
/*    */   
/*    */   private void write(RegistryFriendlyByteBuf output) {
/* 32 */     output.writeContainerId(this.containerId);
/* 33 */     output.writeVarInt(this.stateId);
/* 34 */     output.writeShort(this.slot);
/* 35 */     ItemStack.OPTIONAL_STREAM_CODEC.encode(output, this.itemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.network.protocol.PacketType<ClientboundContainerSetSlotPacket> type() {
/* 40 */     return GamePacketTypes.CLIENTBOUND_CONTAINER_SET_SLOT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handle(ClientGamePacketListener listener) {
/* 45 */     listener.handleContainerSetSlot(this);
/*    */   }
/*    */   
/*    */   public int getContainerId() {
/* 49 */     return this.containerId;
/*    */   }
/*    */   
/*    */   public int getSlot() {
/* 53 */     return this.slot;
/*    */   }
/*    */   
/*    */   public ItemStack getItem() {
/* 57 */     return this.itemStack;
/*    */   }
/*    */   
/*    */   public int getStateId() {
/* 61 */     return this.stateId;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/game/ClientboundContainerSetSlotPacket.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */